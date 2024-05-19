package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDatabaseModel {
    private static final Logger LOGGER = Logger.getLogger(AdminDatabaseModel.class.getName());

    public AdminDatabaseModel() {
    }

   public void signUp(String firstName, String lastName, String email, String phoneNumber, String password) {
        String query = "INSERT INTO admindb (first_name, last_name, email, phone_number, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, password);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to sign up new admin", ex);
        }
    }

    public boolean login(String email, String password) {
        String query = "SELECT * FROM admindb WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Login failed for email: " + email, ex);
            return false;
        }
    }
}
