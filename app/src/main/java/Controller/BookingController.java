package Controller;

import Model.BookingModel;
import View.Booking;
import View.RoomAvailability;
import com.toedter.calendar.JCalendar;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

public class BookingController {
    private BookingModel model;
    private Booking view;
    private JCalendar jCalendar;
    private RoomAvailability roomA;

    public BookingController(BookingModel model, Booking view, JCalendar jCalendar, RoomAvailability roomA) {
        this.model = model;
        this.view = view;
        this.jCalendar = jCalendar;
        this.roomA = roomA;

        view.addAddButtonListener1(this::handleAddButtonClick1);
        view.addSubtractButtonListener1(this::handleSubtractButtonClick1);
        view.addAddButtonListener2(this::handleAddButtonClick2);
        view.addSubtractButtonListener2(this::handleSubtractButtonClick2);
    }

 

    public void handleAddButtonClick1(ActionEvent e) {
        int currentValue = view.getAdultCount();
        int newValue = currentValue + 1;
        view.setAdultCount(newValue);
        model.updateAdultCount(newValue);
    }

    public void handleSubtractButtonClick1(ActionEvent e) {
        int currentValue = view.getAdultCount();
        if (currentValue > 0) {
            int newValue = currentValue - 1;
            view.setAdultCount(newValue);
            model.updateAdultCount(newValue);
        }
    }

    public void handleAddButtonClick2(ActionEvent e) {
        int currentValue = view.getChildrenCount();
        int newValue = currentValue + 1;
        view.setChildrenCount(newValue);
        model.updateChildrenCount(newValue);
    }

    public void handleSubtractButtonClick2(ActionEvent e) {
        int currentValue = view.getChildrenCount();
        if (currentValue > 0) {
            int newValue = currentValue - 1;
            view.setChildrenCount(newValue);
            model.updateChildrenCount(newValue);
        }
    }

    public void addCalendarActionListener() {
        jCalendar.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("calendar".equals(evt.getPropertyName())) {
                    java.util.Date selectedDate = jCalendar.getDate();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = dateFormat.format(selectedDate);
                    view.setSelectedDateLabel(formattedDate);
                }
            }
        });
    }

}
