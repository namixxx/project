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
public class ConfirmationModel {
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
    
public int getRoomNumber() {
    int bookingId = getLastInsertedBookingId();
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
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return roomType;
}

public String getRoomNameByType(String roomType) {
    String roomName = "N/A";
    try (Connection con = DatabaseConnection.getConnection()) {
        String query = "SELECT roomName FROM typedb WHERE roomType = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, roomType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                roomName = rs.getString("roomName");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return roomName;
}

public String getRoomDescriptionByType(String roomType) {
    String roomDescription = "N/A";
    try (Connection con = DatabaseConnection.getConnection()) {
        String query = "SELECT roomDescription FROM typedb WHERE roomType = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, roomType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                roomDescription = rs.getString("roomDescription");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return roomDescription;
}
public String getGuestFirstName(int guestId) {
    String firstname = "N/A";
    try (Connection con = DatabaseConnection.getConnection()) {
        String query = "SELECT firstName FROM guestdb WHERE guestId = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, guestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                firstname = rs.getString("firstName");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return firstname;
}
public String getGuestLastName(int guestId) {
    String lastname = "N/A";
    try (Connection con = DatabaseConnection.getConnection()) {
        String query = "SELECT lastName FROM guestdb WHERE guestId = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, guestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lastname = rs.getString("lastName");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return lastname;
}
public String getGuestNumber(int guestId) {
    String guestnumber = "N/A";
    try (Connection con = DatabaseConnection.getConnection()) {
        String query = "SELECT contactNumber FROM guestdb WHERE guestId = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, guestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                guestnumber = rs.getString("contactNumber");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return guestnumber;
}

public String getGuestEmail(int guestId) {
    String guestemail = "N/A";
    try (Connection con = DatabaseConnection.getConnection()) {
        String query = "SELECT email FROM guestdb WHERE guestId = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, guestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                guestemail = rs.getString("email");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return guestemail;
}
       public int getLastPaymentId() {
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
       
public double getPaymentAmount(int paymentId) {
    int paymentAmount = 0;
    try (Connection con = DatabaseConnection.getConnection()) {
        String query = "SELECT paymentAmount FROM paymentdb WHERE paymentId = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                paymentAmount = rs.getInt("paymentAmount");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return paymentAmount;
}
public String getPaymentMethod(int paymentId) {
    String paymentMethod = "N/A";
    try (Connection con = DatabaseConnection.getConnection()) {
        String query = "SELECT paymentMethod FROM paymentdb WHERE paymentId = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                paymentMethod = rs.getString("paymentMethod");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return paymentMethod;
}

        public String[] retrieveCheckInOutDates(int bookingId) {
        String[] dates = new String[2]; // Array to store check-in and check-out dates
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT checkinDate, checkoutDate FROM newbookingdb WHERE bookingId = ?")) {
            statement.setInt(1, bookingId);
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
        
public List<String> getAddOnOptions(int bookingId) {
    List<String> addOns = new ArrayList<>();
    try (Connection con = DatabaseConnection.getConnection()) {
        String query = "SELECT AddOption1, AddOption2, AddOption3 FROM newbookingdb WHERE bookingId = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if ("Yes".equals(rs.getString("AddOption1"))) {
                    addOns.add("Room Service");
                }
                if ("Yes".equals(rs.getString("AddOption2"))) {
                    addOns.add("Eat all you can buffet");
                }
                if ("Yes".equals(rs.getString("AddOption3"))) {
                    addOns.add("Spa treatment");
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
    }
    return addOns;
}

}
