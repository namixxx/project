/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import Model.TransactionTableModel;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Zyron
 */
public class TransactionTable extends javax.swing.JPanel {
private TransactionTableModel model;
    /**
     * Creates new form TransactionTable
     */
    public TransactionTable() {
        initComponents();
        this.model = new TransactionTableModel();
        attachButtonActions();
        updateTransactionTable();
    }
 private void attachButtonActions() {
        jButton1.addActionListener(e -> updateTransaction());
        jButton2.addActionListener(e -> clearTransaction());
    }

private void updateTransaction() {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "No transaction selected.");
        return;
    }

    Integer guestId = (Integer) jTable1.getValueAt(selectedRow, 0);
    if (guestId == null) {
        JOptionPane.showMessageDialog(this, "Guest ID is missing.");
        return;
    }

    java.util.Date paymentDateUtil = jCalendar1.getDate();
    java.sql.Date paymentDateSql = paymentDateUtil != null ? new java.sql.Date(paymentDateUtil.getTime()) : null;

    Number paymentAmountNumber = (Number) jTable1.getValueAt(selectedRow, 2);
    double paymentAmount = paymentAmountNumber != null ? paymentAmountNumber.doubleValue() : 0; // Convert Number to double

    String paymentMethod = (String) jTable1.getValueAt(selectedRow, 3);
    if (paymentMethod == null) paymentMethod = ""; // Default to empty string if null

    try {
        if (model.updateTransaction(guestId, paymentDateSql, paymentAmount, paymentMethod)) {
            JOptionPane.showMessageDialog(this, "Transaction updated successfully!");
            updateTransactionTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update transaction. No changes were made.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Failed to update transaction due to an error: " + e.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    private void clearTransaction() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "No transaction selected.");
            return;
        }
        int guestId = (Integer) jTable1.getValueAt(selectedRow, 0);
        if (model.clearTransaction(guestId)) {
            JOptionPane.showMessageDialog(this, "Transaction details cleared successfully!");
            updateTransactionTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to clear transaction details.");
        }
    }

private void updateTransactionTable() {
    try {
        ResultSet rs = model.fetchTransactionDetails();
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        tableModel.setRowCount(0);

        while (rs.next()) {
            int guestId = rs.getInt("guestId");
            java.sql.Date paymentDate = rs.getDate("paymentDate");
            double paymentAmount = rs.getDouble("paymentAmount");
            String paymentMethod = rs.getString("paymentMethod");

            // Check if paymentDate is null and handle it appropriately
            String displayDate = paymentDate != null ? paymentDate.toString() : "No Date Set";

            tableModel.addRow(new Object[]{guestId, displayDate, paymentAmount, paymentMethod});
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error fetching transaction details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCalendar1 = new com.toedter.calendar.JCalendar();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Guest ID", "Payment Date", "Payment Amount", "Payment Method"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("UPDATE");

        jButton2.setText("CLEAR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(246, 246, 246)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(118, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
