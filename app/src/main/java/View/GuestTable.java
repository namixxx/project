/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import Model.DatabaseConnection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.util.Date; 


/**
 *
 * @author Zyron
 */
public class GuestTable extends javax.swing.JPanel {
private static final String DEFAULT_GUEST_ID = "";
private static final String DEFAULT_FIRST_NAME = "";
private static final String DEFAULT_LAST_NAME = "";

    public GuestTable() {
        initComponents();
        setupListeners(); 
        populateTable();
    }
    
    
    
    
    private void setupListeners() {
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateGuestName();
            }
        });
        jButton2.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                clearSelectedRowData();
            }
        });
        jButton3.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });

        jTable2.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                updateDatabase((DefaultTableModel) e.getSource(), row, column);
            }
        });
    }
    private void updateDatabase(DefaultTableModel model, int row, int column) {
        String guestId = model.getValueAt(row, 0).toString();
        Object newValue = model.getValueAt(row, column);
        String sql = null;

        try (Connection con = DatabaseConnection.getConnection()) {
            switch (column) {
                case 2: 
                    sql = "UPDATE guestdb SET contactNumber = ? WHERE guestId = ?";
                    break;
                case 3:
                    sql = "UPDATE guestdb SET email = ? WHERE guestId = ?";
                    break;
                case 4:
                    sql = "UPDATE newbookingdb SET roomNumber = ? WHERE guestId = ?";
                    if (newValue != null) {
                        int roomNumber = Integer.parseInt(newValue.toString());
                        String roomType = updateRoomNumberAndFetchRoomType(guestId, roomNumber);
                        model.setValueAt(roomType, row, 5); // Update room type in the table as well
                    }
                    break;
                case 6:
                case 7:
                    if (newValue != null && newValue instanceof java.sql.Date) {
                        updateDateInDatabase(row, column, (java.sql.Date) newValue);
                    }
                    break;
            }

            if (sql != null) {
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setObject(1, newValue);
                    pstmt.setString(2, guestId);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
        }
    }


private void clearSelectedRowData() {
    int selectedRow = jTable2.getSelectedRow();
    if (selectedRow != -1) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        String guestId = model.getValueAt(selectedRow, 0).toString();

        String roomType = updateRoomNumberAndFetchRoomType(guestId, 0);

        model.setValueAt("", selectedRow, 1);
        model.setValueAt("", selectedRow, 2);
        model.setValueAt("", selectedRow, 3);
        model.setValueAt(0, selectedRow, 4); 
        model.setValueAt(roomType, selectedRow, 5);

        clearGuestDetailsInDatabase(guestId);
    } else {
        JOptionPane.showMessageDialog(this, "No row selected.");
    }
}

private String updateRoomNumberAndFetchRoomType(String guestId, int newRoomNumber) {
    String roomType = "Lounge"; 
    try (Connection con = DatabaseConnection.getConnection()) {
        try (PreparedStatement pstmt = con.prepareStatement("UPDATE newbookingdb SET roomNumber = ? WHERE guestId = ?")) {
            pstmt.setInt(1, newRoomNumber);
            pstmt.setString(2, guestId);
            pstmt.executeUpdate();
        }
        
        try (PreparedStatement pstmt = con.prepareStatement("SELECT roomType FROM roomdb WHERE roomNumber = ?")) {
            pstmt.setInt(1, newRoomNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    roomType = rs.getString("roomType");
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Error updating room number and fetching room type: " + ex.getMessage());
    }
    return roomType;
}


private void clearGuestDetailsInDatabase(String guestId) {
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement pstmt = con.prepareStatement("UPDATE guestdb SET firstName = '', lastName = '', contactNumber = '', email = '' WHERE guestId = ?")) {
        pstmt.setString(1, guestId);
        pstmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Error updating guest details: " + ex.getMessage());
    }
}

private void updateRoomNumberInDatabase(String guestId, int newRoomNumber) {
    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement pstmt = con.prepareStatement("UPDATE newbookingdb SET roomNumber = ? WHERE guestId = ?")) {
        pstmt.setInt(1, newRoomNumber);
        pstmt.setString(2, guestId);
        pstmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Error updating room number: " + ex.getMessage());
    }
}


 private void deleteSelectedRow() {
     int selectedRow = jTable2.getSelectedRow();
     if (selectedRow != -1) {
         DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
         String guestId = model.getValueAt(selectedRow, 0).toString();  // Get the Guest ID for database deletion

         deleteRowFromDatabase(guestId);

         model.removeRow(selectedRow);
     } else {
         JOptionPane.showMessageDialog(this, "No row selected.");
     }
 }

    private void populateTable() {
        ResultSet rs = fetchGuestsWithBookings();
        if (rs != null) {
            try {
                DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                model.setRowCount(0); 

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("guestId"),
                        rs.getString("firstName") + " " + rs.getString("lastName"),
                        rs.getString("contactNumber"),
                        rs.getString("email"),
                        rs.getInt("roomNumber"),
                        rs.getString("roomType"),
                        rs.getString("CheckinDate"),
                        rs.getString("CheckoutDate")
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (rs.getStatement() != null && rs.getStatement().getConnection() != null) {
                        rs.getStatement().getConnection().close();
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    private void updateRowInDatabase(String guestId, String firstName, String lastName, String contact, String email, String roomNum, String roomType) {
        Connection con = null;
        PreparedStatement pstmtGuest = null;
        PreparedStatement pstmtBooking = null;
        try {
            con = DatabaseConnection.getConnection();
            String sqlGuest = "UPDATE guestdb SET firstName = ?, lastName = ?, contactNumber = ?, email = ? WHERE guestId = ?";
            pstmtGuest = con.prepareStatement(sqlGuest);
            pstmtGuest.setString(1, firstName);
            pstmtGuest.setString(2, lastName);
            pstmtGuest.setString(3, contact);
            pstmtGuest.setString(4, email);
            pstmtGuest.setInt(5, Integer.parseInt(guestId));
            pstmtGuest.executeUpdate();

            String sqlBooking = "UPDATE newbookingdb, roomdb SET newbookingdb.roomNumber = ?, roomdb.roomType = ? WHERE newbookingdb.guestId = ? AND newbookingdb.roomNumber = roomdb.roomNumber";
            pstmtBooking = con.prepareStatement(sqlBooking);
            pstmtBooking.setInt(1, Integer.parseInt(roomNum));
            pstmtBooking.setString(2, roomType);
            pstmtBooking.setInt(3, Integer.parseInt(guestId));
            pstmtBooking.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error updating guest details: " + e.getMessage());
        } finally {
            try {
                if (pstmtGuest != null) pstmtGuest.close();
                if (pstmtBooking != null) pstmtBooking.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


private void deleteRowFromDatabase(String guestId) {
    Connection con = null;
    PreparedStatement pstmtDeleteGuest = null;
    PreparedStatement pstmtUpdateBooking = null;
    try {
        con = DatabaseConnection.getConnection();
        String sqlUpdateBooking = "UPDATE newbookingdb SET roomNumber = NULL WHERE guestId = ?";
        pstmtUpdateBooking = con.prepareStatement(sqlUpdateBooking);
        pstmtUpdateBooking.setInt(1, Integer.parseInt(guestId));
        pstmtUpdateBooking.executeUpdate();

        String sqlDeleteGuest = "DELETE FROM guestdb WHERE guestId = ?";
        pstmtDeleteGuest = con.prepareStatement(sqlDeleteGuest);
        pstmtDeleteGuest.setInt(1, Integer.parseInt(guestId));
        pstmtDeleteGuest.executeUpdate();

    } catch (SQLException e) {
        Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, e);
        JOptionPane.showMessageDialog(null, "Error deleting guest: " + e.getMessage());
    } finally {
        try {
            if (pstmtDeleteGuest != null) pstmtDeleteGuest.close();
            if (pstmtUpdateBooking != null) pstmtUpdateBooking.close();
            if (con != null) con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


    public ResultSet fetchGuestsWithBookings() {
        String query = "SELECT g.guestId, g.firstName, g.lastName, g.contactNumber, g.email, b.roomNumber, b.CheckinDate, b.CheckoutDate, r.roomType " +
                       "FROM guestdb g " +
                       "JOIN newbookingdb b ON g.guestId = b.guestId " +
                       "JOIN roomdb r ON b.roomNumber = r.roomNumber";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error fetching guest and booking data: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    private void updateGuestName() {
        String guestId = jTextField1.getText().trim();
        String firstName = jTextField2.getText().trim();
        String lastName = jTextField3.getText().trim();

        if (guestId.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields before updating.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return; 
        }

        String fullName = firstName + " " + lastName; 
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(guestId)) {
                model.setValueAt(fullName, i, 1); 
                updateGuestDetailsInDatabase(guestId, firstName, lastName); 
                found = true;
                break;
            }
        }
        if (found) {
            JOptionPane.showMessageDialog(this, "Guest name updated successfully.");
            clearTextFields(); 
        } else {
            JOptionPane.showMessageDialog(this, "Guest ID not found.");
        }
    }


    private void clearTextFields() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
    }

    

    private void updateGuestDetailsInDatabase(String guestId, String firstName, String lastName) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DatabaseConnection.getConnection();
            String sql = "UPDATE guestdb SET firstName = ?, lastName = ? WHERE guestId = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, Integer.parseInt(guestId));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, "Error updating guest details: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jCalendar2 = new com.toedter.calendar.JCalendar();
        jButton4 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(850, 449));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Guest ID", "Guest Name", "Contact Number", "Email Address", "Room Number", "Room Type", "Check In ", "Check Out"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel1.setText("GUEST ");

        jButton1.setText("Update");

        jButton3.setText("Delete");

        jButton2.setText("Clear");

        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextField1.setMinimumSize(new java.awt.Dimension(65, 18));
        jTextField1.setPreferredSize(new java.awt.Dimension(65, 18));

        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextField2.setMinimumSize(new java.awt.Dimension(65, 18));
        jTextField2.setPreferredSize(new java.awt.Dimension(65, 18));

        jTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextField3.setMinimumSize(new java.awt.Dimension(65, 18));
        jTextField3.setPreferredSize(new java.awt.Dimension(65, 18));

        jLabel2.setText("Guest ID");

        jLabel3.setText("First Name");

        jLabel4.setText("Last Name");

        jButton4.setText("OK");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 707, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jCalendar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addComponent(jButton4))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(211, 211, 211)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(14, 14, 14)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(23, 23, 23)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton3)
                            .addComponent(jButton2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jCalendar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jTextField1.getAccessibleContext().setAccessibleName("");

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int selectedRow = jTable2.getSelectedRow();
        int selectedColumn = jTable2.getSelectedColumn();
        if (selectedRow != -1 && (selectedColumn == 6 || selectedColumn == 7)) {
            java.util.Date selectedDate = jCalendar2.getDate();
            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
            jTable2.setValueAt(sqlDate.toString(), selectedRow, selectedColumn);
            updateDateInDatabase(selectedRow, selectedColumn, sqlDate);
        } else {
            JOptionPane.showMessageDialog(null, "No row or correct column selected.");
        }
    }//GEN-LAST:event_jButton4ActionPerformed
    private void updateDateInDatabase(int row, int column, java.sql.Date date) {
        String guestId = jTable2.getValueAt(row, 0).toString();
        String columnName = column == 6 ? "CheckinDate" : "CheckoutDate";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement("UPDATE newbookingdb SET " + columnName + " = ? WHERE guestId = ?")) {
            pstmt.setDate(1, date);
            pstmt.setString(2, guestId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GuestTable.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error updating date in the database: " + ex.getMessage());
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private com.toedter.calendar.JCalendar jCalendar2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
