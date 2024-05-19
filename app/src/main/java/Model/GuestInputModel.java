package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuestInputModel {
    private BookingModel model;

    public void signUp(String prefix, String firstname, String lastname, String suffix, String phonenumber, String emailaddress) {
        int guestId = getLastInsertedGuestId(); 

        if (guestId != -1) {
            try (Connection con = DatabaseConnection.getConnection()) {
                String queryUpdate = "UPDATE guestdb SET prefixName=?, firstName=?, lastName=?, suffixName=?, contactNumber=?, email=? WHERE guestId=?";
                try (PreparedStatement stmtUpdate = con.prepareStatement(queryUpdate)) {
                    stmtUpdate.setString(1, prefix);
                    stmtUpdate.setString(2, firstname);
                    stmtUpdate.setString(3, lastname);
                    stmtUpdate.setString(4, suffix);
                    stmtUpdate.setString(5, phonenumber);
                    stmtUpdate.setString(6, emailaddress);
                    stmtUpdate.setInt(7, guestId);
                    int rowsAffected = stmtUpdate.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Guest data updated successfully");
                    } else {
                        System.out.println("Failed to update guest data");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminDatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Failed to insert new guest record.");
        }
    }

    public int getLastInsertedGuestId() {
        int lastGuestId = -1;
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT MAX(guestId) AS lastId FROM guestdb";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    lastGuestId = rs.getInt("lastId");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GuestInputModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastGuestId;
    }
    
        public String[] retrieveCheckInOutDates(int guestId) {
        String[] dates = new String[2]; 
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT checkinDate, checkoutDate FROM newbookingdb WHERE guestId = ?")) {
            statement.setInt(1, guestId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                dates[0] = resultSet.getString("checkinDate");
                dates[1] = resultSet.getString("checkoutDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }
        
        public int getRoomNumberForBooking(int bookingId) {
    int roomNumber = -1;
    try (Connection con = DatabaseConnection.getConnection()) {
        String query = "SELECT roomNumber FROM newbookingdb WHERE bookingId = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                roomNumber = rs.getInt("roomNumber");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(RoomAvailabilityModel.class.getName()).log(Level.SEVERE, null, ex);
    }
    return roomNumber;
}
        
        
        
        public String getRoomType(int roomNumber) {
        String roomType = "";
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT roomType FROM roomdb WHERE roomNumber = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, roomNumber);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    roomType = rs.getString("roomType");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoomAvailabilityModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return roomType;
    }
        
    public int getLastInsertedBookingId() {
        int lastBookingId = -1;
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT MAX(bookingId) AS lastId FROM newbookingdb";
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
    public int getLastInsertedPaymentId() {
        int paymentId = -1;
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT MAX(paymentId) AS lastId FROM paymentdb";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    paymentId = rs.getInt("lastId");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GuestInputModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paymentId;
    }
    public void updateOption1(int bookingId, String status) {
    String query = "UPDATE newbookingdb SET AddOption1 = ? WHERE bookingId = ?";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(query)) {

        stmt.setString(1, status);
        stmt.setInt(2, bookingId);

        stmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(BookingModel.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    public void updateOption2(int bookingId, String status) {
    String query = "UPDATE newbookingdb SET AddOption2 = ? WHERE bookingId = ?";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(query)) {

        stmt.setString(1, status);
        stmt.setInt(2, bookingId);

        stmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(BookingModel.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    public void updateOption3(int bookingId, String status) {
    String query = "UPDATE newbookingdb SET AddOption3 = ? WHERE bookingId = ?";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(query)) {

        stmt.setString(1, status);
        stmt.setInt(2, bookingId);

        stmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(BookingModel.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    
    public void updatePaymentTotal(int paymentId, double paymentTotal) {
    String query = "UPDATE paymentdb SET paymentAmount = ? WHERE paymentId = ?";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(query)) {

        stmt.setDouble(1, paymentTotal);
        stmt.setInt(2, paymentId);

        stmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(BookingModel.class.getName()).log(Level.SEVERE, null, ex);
    }
}
}
