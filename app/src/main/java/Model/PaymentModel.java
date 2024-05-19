/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zyron
 */
public class PaymentModel {
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
        
        
 public void cardInformation(String paymentMethod, String cardNumber, String cardExpiration, String cardName, String cardCvv, String paymentDate) {
    int paymentId = getLastInsertedPaymentId(); 

    if (paymentId != -1) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String queryUpdate = "UPDATE paymentdb SET cardNumber=?, cardExpiration=?, cardName=?, cardCvv=?, paymentDate=?, paymentMethod=? WHERE paymentId=?";
            try (PreparedStatement stmtUpdate = con.prepareStatement(queryUpdate)) {
                stmtUpdate.setString(1, cardNumber);
                stmtUpdate.setString(2, cardExpiration);
                stmtUpdate.setString(3, cardName);
                stmtUpdate.setString(4, cardCvv);

                // Parse the payment date from MM-dd-yyyy format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate localDate = LocalDate.parse(paymentDate, formatter);

                // Set the date in SQL format
                stmtUpdate.setDate(5, java.sql.Date.valueOf(localDate));
                stmtUpdate.setString(6, paymentMethod);
                stmtUpdate.setInt(7, paymentId);

                int rowsAffected = stmtUpdate.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Payment information updated successfully for payment ID: " + paymentId);
                } else {
                    System.out.println("Failed to update payment information.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminDatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    } else {
        System.out.println("No valid payment record found to update.");
    }
}

    
        public void gcashInformation(String paymentMethod, String gcashnumber, String gcashname, String paymentDate) {
        int paymentid = getLastInsertedPaymentId(); // Retrieve the last inserted guestId

        if (paymentid != -1) {
            try (Connection con = DatabaseConnection.getConnection()) {
                String queryUpdate = "UPDATE paymentdb SET gcashNumber=?, gcashName=?, paymentDate=?, paymentMethod=? WHERE paymentId=?";
                try (PreparedStatement stmtUpdate = con.prepareStatement(queryUpdate)) {
                    stmtUpdate.setString(1, gcashnumber);
                    stmtUpdate.setString(2, gcashname);
                    
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate localDate = LocalDate.parse(paymentDate, formatter);
                stmtUpdate.setDate(3, java.sql.Date.valueOf(localDate));
                    stmtUpdate.setString(4, paymentMethod);
                    stmtUpdate.setInt(5, paymentid);
                    int rowsAffected = stmtUpdate.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("updated successfully");
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
        
        public void cashReceived(String paymentMethod,int cashreceived, String paymentDate) {
        int paymentid = getLastInsertedPaymentId(); 

        if (paymentid != -1) {
            try (Connection con = DatabaseConnection.getConnection()) {
                String queryUpdate = "UPDATE paymentdb SET cashReceived=?, paymentDate=?, paymentMethod=? WHERE paymentId=?";
                try (PreparedStatement stmtUpdate = con.prepareStatement(queryUpdate)) {
                    stmtUpdate.setInt(1, cashreceived);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate localDate = LocalDate.parse(paymentDate, formatter);
                stmtUpdate.setDate(2, java.sql.Date.valueOf(localDate));
                stmtUpdate.setString(3, paymentMethod);
                stmtUpdate.setInt(4, paymentid);
                    
                    
                    int rowsAffected = stmtUpdate.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("updated successfully");
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
        
    public double getPaymentTotal(int bookingId) {
        double paymentTotal = 0;
        String query = "SELECT paymentAmount FROM paymentdb WHERE bookingId = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                paymentTotal = rs.getDouble("paymentAmount");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paymentTotal;
    }
    
    public double getPaymentAmount(int paymentId) {
        String query = "SELECT paymentAmount FROM paymentdb WHERE paymentId = ?";
        double paymentAmount = 0.0; 

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, paymentId); 

            ResultSet rs = stmt.executeQuery(); 
            if (rs.next()) { 
                paymentAmount = rs.getDouble("paymentAmount"); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookingModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return paymentAmount; 
    }
}
