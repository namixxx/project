/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zyron
 */
public class RoomAvailabilityModel {
    
    
         public String retrieveRoomStatus(int roomNumber) {
        String status = "";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT roomStatus FROM roomdb WHERE roomNumber = ?");
        ) {
            statement.setInt(1, roomNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    status = resultSet.getString("roomStatus");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

public List<Integer> getRoomNumbersFromType(String roomType) {
    List<Integer> roomNumbers = new ArrayList<>();
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT roomNumber FROM roomdb WHERE roomType = ?")) {
        statement.setString(1, roomType);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                roomNumbers.add(resultSet.getInt("roomNumber"));
                System.out.println("getting room numbers");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return roomNumbers;
}
// this method is suppose to get the room number by status // not used
    public List<Integer> getRoomNumbersByStatus(String status) {
    List<Integer> roomNumbers = new ArrayList<>();
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT roomNumber FROM roomdb WHERE roomStatus = ?")) {
        statement.setString(1, status);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                roomNumbers.add(resultSet.getInt("roomNumber"));
                System.out.println("getting room numbers");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return roomNumbers;
}    
    
    public int getLastInsertedBookingId() {
        int lastBookingId = -1;
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT MAX(BookingId) AS lastId FROM newbookingdb";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    lastBookingId = rs.getInt("lastId");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GuestInputModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastBookingId;
    }
    
public void updateRoomNumberAndStatus(int bookingId, int roomNumber) {
    if (bookingId != -1) {
        try (Connection con = DatabaseConnection.getConnection()) {
            // First, update the room number
            String queryUpdateRoomNumber = "UPDATE newbookingdb SET roomNumber=? WHERE bookingId=?";
            try (PreparedStatement stmtUpdateRoomNumber = con.prepareStatement(queryUpdateRoomNumber)) {
                stmtUpdateRoomNumber.setInt(1, roomNumber);
                stmtUpdateRoomNumber.setInt(2, bookingId);
                int rowsAffectedRoomNumber = stmtUpdateRoomNumber.executeUpdate();

                if (rowsAffectedRoomNumber > 0) {
                    // If room number update is successful, update the room status to 'Occupied'
                    String queryUpdateRoomStatus = "UPDATE roomdb SET roomStatus='Occupied' WHERE roomNumber=?";
                    try (PreparedStatement stmtUpdateRoomStatus = con.prepareStatement(queryUpdateRoomStatus)) {
                        stmtUpdateRoomStatus.setInt(1, roomNumber);
                        int rowsAffectedRoomStatus = stmtUpdateRoomStatus.executeUpdate();

                        if (rowsAffectedRoomStatus > 0) {
                            System.out.println("Room number and status updated successfully");
                        } else {
                            System.out.println("Failed to update room status");
                        }
                    }
                } else {
                    System.out.println("Failed to update room number");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GuestInputModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    } else {
        System.out.println("Invalid booking ID.");
    }
}

    
}
