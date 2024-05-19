/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
/**
 *
 * @author Zyron
 */
import java.sql.SQLException;
import Model.DashboardModel;
import View.Dashboard;
import Model.DatabaseConnection;
import java.util.Map;

public class DashboardController {
    private DashboardModel model;
    private Dashboard view;

    public DashboardController(DashboardModel model, Dashboard view) {
        this.model = model;
        this.view = view;
    }

    public void updateTotalCheckInsToday() {
        try {
            int totalCheckIns = model.getTotalCheckInsToday();
            view.setTotalCheckIns(totalCheckIns);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching total check-ins today.");
        }
    }
    public void updateTotalCheckOutsToday() {
        try {
            int totalCheckOuts = model.getTotalCheckOutsToday();
            view.setTotalCheckOuts(totalCheckOuts);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching total check-outs today.");
        }
    }
    public void updateTotalOccupied() {
        try {
            int totalOccupied = model.getTotalOccupiedRooms();
            view.setTotalOccupied(totalOccupied);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching occupied rooms");
        }
    }
    public void updateTotalAvailable() {
        try {
            int totalAvailable = model.getTotalAvailableRooms();
            view.setTotalAvailable(totalAvailable);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching available rooms.");
        }
    }
    public void updateCheckinWeek() {
        try {
            int weekTotal = model.getCheckinTotal();
            view.setTotalCheckinWeek(weekTotal);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching week Total Check Ins.");
        }
    }
    public void updateOccupiedRoomStatusCounts() {
        try {
            Map<String, Integer> roomStatusCounts = model.getOccupiedRoomCounts();
            view.displayOccupiedRoomStatusCounts(roomStatusCounts);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching room status counts.");
        }
    }
    public void updateAvailableRoomStatusCounts() {
        try {
            Map<String, Integer> roomStatusCounts = model.getAvailableRoomCounts();
            view.displayAvailableRoomStatusCounts(roomStatusCounts);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching room status counts.");
        }
    }
    public void updateRoom1Status() {
        try {
            String roomStatus = model.getRoom1Status();
            view.displayRoom1Status(roomStatus);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching room status.");
        }
    }
    public void updateRoom2Status() {
        try {
            String roomStatus = model.getRoom2Status();
            view.displayRoom2Status(roomStatus);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching room status.");
        }
    }
    public void updateRoom3Status() {
        try {
            String roomStatus = model.getRoom3Status();
            view.displayRoom3Status(roomStatus);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching room status.");
        }
    }
    public void updateRoom4Status() {
        try {
            String roomStatus = model.getRoom4Status();
            view.displayRoom4Status(roomStatus);
        } catch (SQLException e) {
            e.printStackTrace();
            view.displayErrorMessage("Error fetching room status.");
        }
    }
    
    
    
    

    public void closeModelConnection() {
        DatabaseConnection.closeConnection();
    }
}

