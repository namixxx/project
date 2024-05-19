package Model;

import static Model.DatabaseConnection.getConnection;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author Zyron
 */
//nakoy gi delete 5/17 check errors
public class BookingModel {
    
     public void updateAdultCount(int newAdultCount) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE guestcat SET adults = ?")) {
            statement.setInt(1, newAdultCount);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     public void updateChildrenCount(int newChildrenCount) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE guestcat SET children = ?")) {
            statement.setInt(1, newChildrenCount);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     
     
public int addGuest() {
    int guestId = 0; // Initialize guestId
    try (Connection connection = getConnection()) {
        String sql = "INSERT INTO guestdb (firstName, lastName, prefixName, suffixName, contactNumber, email, address) "
                   + "VALUES (NULL, NULL, NULL, NULL, NULL, NULL, NULL)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    guestId = generatedKeys.getInt(1); // Get the generated guestId
                }
                System.out.println("New guest record created successfully with guestId: " + guestId);
            } else {
                System.out.println("Failed to create new guest record");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return guestId;
}
public int addRoom() {
    int roomNumber = -1; 

    try (Connection connection = getConnection()) {
        String sql = "SELECT roomNumber FROM roomdb WHERE roomType = 'Lounge'"; 
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    roomNumber = resultSet.getInt("roomNumber"); 
                    System.out.println("Default roomNumber retrieved: " + roomNumber);
                } else {
                    System.out.println("Failed to retrieve default roomNumber");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return roomNumber;
}public int addPayment(int guestId) {
    int paymentId = 0; 
    try (Connection connection = DatabaseConnection.getConnection()) {
        String sql = "INSERT INTO paymentdb (guestId, paymentDate, paymentAmount, paymentMethod, cardNumber, cardExpiration, cardName, cardCvv, gcashNumber, gcashName, cashReceived) "
                   + "VALUES (?, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, guestId); 

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    paymentId = generatedKeys.getInt(1); 
                }
                System.out.println("New payment record created successfully with paymentId: " + paymentId);
            } else {
                System.out.println("Failed to create new payment record");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return paymentId;
}


public void addBooking(int guestId, int paymentId, Date checkinDate, Date checkoutDate, int adults, int children ) {
    int roomNumber = addRoom();
    String AddOption1 = null;
    String AddOption2 = null;
    String AddOption3 = null;
    try (Connection connection = getConnection()) { 
        String sql = "INSERT INTO newbookingdb (guestId, paymentId, roomNumber, checkinDate, checkoutDate, adults, children, addOption1, addOption2, addOption3) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            java.sql.Date sqlCheckinDate = new java.sql.Date(checkinDate.getTime());
            java.sql.Date sqlCheckoutDate = new java.sql.Date(checkoutDate.getTime());
            
            statement.setInt(1, guestId);
            statement.setInt(2, paymentId);
            statement.setInt(3, roomNumber);
            statement.setDate(4, sqlCheckinDate);
            statement.setDate(5, sqlCheckoutDate);            
            statement.setInt(6, adults);
            statement.setInt(7, children);
            statement.setString(8, AddOption1);
            statement.setString(9, AddOption2);
            statement.setString(10, AddOption3);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("New booking created successfully");
            } else {
                System.out.println("Failed to create new booking");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//fini
    }
     
     

