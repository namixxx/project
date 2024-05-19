/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import View.RoomTable;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Zyron
 */
public class RoomTableModel {
    private RoomTable roomtable;
   // Method to fetch room details
    public ResultSet fetchRoomDetails() {
        String query = "SELECT roomNumber, roomType, roomStatus, roomUpdate FROM roomdb";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to update room details
    public boolean updateRoomDetails(int roomNumber, String roomStatus, String roomUpdate) {
        String sql = "UPDATE roomdb SET roomStatus = ?, roomUpdate = ? WHERE roomNumber = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roomStatus);
            stmt.setString(2, roomUpdate);
            stmt.setInt(3, roomNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean clearRoomDetails(int roomNumber) {
        String sql = "UPDATE roomdb SET roomStatus = NULL, roomUpdate = NULL WHERE roomNumber = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateDatabase(int roomNumber, int columnIndex, Object data) {
        String columnName = getColumnNameByIndex(columnIndex);
        String sql = "UPDATE roomdb SET " + columnName + " = ? WHERE roomNumber = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, data);
            pstmt.setInt(2, roomNumber);
            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Update successful");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }

    public String getColumnNameByIndex(int columnIndex) {
        switch(columnIndex) {
            case 1: return "roomStatus";
            case 2: return "roomUpdate";
            default: return null; 
        }
    }
}
