package Controller;

import Model.AdminDatabaseModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AdminSignupController {
    private AdminDatabaseModel model;

    public AdminSignupController() {
        this.model = new AdminDatabaseModel(); 
    }

    public void signUp(String firstName, String lastName, String email, String phoneNumber, String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "All fields are required");
            return;
        }

        model.signUp(firstName, lastName, email, phoneNumber, password);
        JOptionPane.showMessageDialog(new JFrame(), "Registration successful");
    }

    public boolean login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "Email and password are required");
            return false;
        }

        boolean loggedIn = model.login(email, password);
        if (loggedIn) {
            JOptionPane.showMessageDialog(new JFrame(), "Login successful");
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Invalid email or password");
        }
        return loggedIn;
    }
}
