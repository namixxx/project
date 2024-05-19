/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
/**
 *
 * @author Zyron
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DashboardModel {
 public int getTotalCheckInsToday() throws SQLException {
        int totalCheckIns = 0;
        LocalDate today = LocalDate.now();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM bookingdb WHERE DATE(checkinDate) = ?")) {
            statement.setDate(1, java.sql.Date.valueOf(today));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalCheckIns = resultSet.getInt("total");
                }
            }
        }

        return totalCheckIns;
    }
  public int getTotalCheckOutsToday() throws SQLException {
        int totalCheckOuts = 0;
        LocalDate today = LocalDate.now();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM bookingdb WHERE DATE(checkoutDate) = ?")) {
            statement.setDate(1, java.sql.Date.valueOf(today));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalCheckOuts = resultSet.getInt("total");
                }
            }
        }

        return totalCheckOuts;
    }

    public int getTotalOccupiedRooms() throws SQLException {
        int totalOccupiedRooms = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM roomdb WHERE roomStatus = 'Occupied'")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalOccupiedRooms = resultSet.getInt("total");
                }
            }
        }

        return totalOccupiedRooms;
    }
    public int getTotalAvailableRooms() throws SQLException {
        int totalAvailableRooms = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM roomdb WHERE roomStatus = 'Available'")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalAvailableRooms = resultSet.getInt("total");
                }
            }
        }

        return totalAvailableRooms;
    }
    public int getCheckinTotal() throws SQLException {
        int totalCheckins = 0;
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM bookingdb WHERE checkinDate >= ?")) {
            statement.setDate(1, java.sql.Date.valueOf(oneWeekAgo));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalCheckins = resultSet.getInt("total");
                }
            }
        }

        return totalCheckins;
    }
     public Map<String, Integer> getOccupiedRoomCounts() throws SQLException {
    Map<String, Integer> roomStatusCounts = new HashMap<>();

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT roomUpdate, COUNT(*) AS total FROM roomdb WHERE roomStatus = 'Occupied' GROUP BY roomUpdate")) {
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String roomUpdate = resultSet.getString("roomUpdate");
                int count = resultSet.getInt("total");
                roomStatusCounts.put(roomUpdate, count);
            }
        }
    }

    return roomStatusCounts;
}
    public Map<String, Integer> getAvailableRoomCounts() throws SQLException {
    Map<String, Integer> roomStatusCounts = new HashMap<>();

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT roomUpdate, COUNT(*) AS total FROM roomdb WHERE roomStatus = 'Available' GROUP BY roomUpdate")) {
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String roomUpdate = resultSet.getString("roomUpdate");
                int count = resultSet.getInt("total");
                roomStatusCounts.put(roomUpdate, count);
            }
        }
    }

    return roomStatusCounts;
}

    public String getRoom1Status() throws SQLException {
    String roomStatus = "";
    int totalRooms = 0;
    int occupiedRooms = 0;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM roomdb WHERE roomType = 'Deluxe'");
         ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
            totalRooms = resultSet.getInt("total");
        }
    }

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM roomdb WHERE roomType = 'Deluxe' AND roomStatus = 'Occupied'");
         ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
            occupiedRooms = resultSet.getInt("total");
        }
    }
    
        roomStatus = occupiedRooms + "/" + totalRooms;

    return roomStatus;
}
    public String getRoom2Status() throws SQLException {
    String roomStatus = "";
    int totalRooms = 0;
    int occupiedRooms = 0;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM roomdb WHERE roomType = 'Premier'");
         ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
            totalRooms = resultSet.getInt("total");
        }
    }

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM roomdb WHERE roomType = 'Premier' AND roomStatus = 'Occupied'");
         ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
            occupiedRooms = resultSet.getInt("total");
        }
    }
    
        roomStatus = occupiedRooms + "/" + totalRooms;

    return roomStatus;
}
    public String getRoom3Status() throws SQLException {
    String roomStatus = "";
    int totalRooms = 0;
    int occupiedRooms = 0;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM roomdb WHERE roomType = 'Executive'");
         ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
            totalRooms = resultSet.getInt("total");
        }
    }

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM roomdb WHERE roomType = 'Executive' AND roomStatus = 'Occupied'");
         ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
            occupiedRooms = resultSet.getInt("total");
        }
    }
    
        roomStatus = occupiedRooms + "/" + totalRooms;

    return roomStatus;
}
    public String getRoom4Status() throws SQLException {
    String roomStatus = "";
    int totalRooms = 0;
    int occupiedRooms = 0;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM roomdb WHERE roomType = 'Presidential'");
         ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
            totalRooms = resultSet.getInt("total");
        }
    }

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total FROM roomdb WHERE roomType = 'Presidential' AND roomStatus = 'Occupied'");
         ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
            occupiedRooms = resultSet.getInt("total");
        }
    }
    
        roomStatus = occupiedRooms + "/" + totalRooms;

    return roomStatus;
}

    
    
}

