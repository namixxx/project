/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Model.RoomAvailabilityModel;
import View.RoomAvailability;
import java.awt.Color;
import java.util.List;

/**
 *
 * @author Zyron
 */
public class RoomAvailabilityController {
    private RoomAvailabilityModel model;
    private RoomAvailability mainColorView;
    
    public RoomAvailabilityController(RoomAvailabilityModel model, RoomAvailability view) {
        this.model = model;
        this.mainColorView = view;
    }


    
    
public void updateColors(String roomType) {
    System.out.println(roomType);
    List<Integer> roomNumbers = model.getRoomNumbersFromType(roomType);
    for (int roomNumber : roomNumbers) {
        String roomStatus = model.retrieveRoomStatus(roomNumber);
        Color color = getColorForStatus(roomStatus);
        mainColorView.setColor(roomNumber, color);
        System.out.println(roomNumber);
    }
    System.out.println("colors updated");
}

       
private int getRoomNumberFromType(String roomType) {
    RoomAvailabilityModel model = new RoomAvailabilityModel(); 
    List<Integer> roomNumbers = model.getRoomNumbersFromType(roomType);
    if (!roomNumbers.isEmpty()) {
        return roomNumbers.get(0);
    } else {
        return -1;
    }
}

    public Color getColorForStatus(String status) {
        return "Available".equals(status) ? Color.GREEN : Color.RED;
    }

}
