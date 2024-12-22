package com.example.testing;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TaskNotificationController {

    @FXML
    private Label taskTitleLabel;  // Display the task title
    @FXML
    private ComboBox<String> notificationCategoryComboBox;  // ComboBox for selecting the notification category
    @FXML
    private Button saveNotificationButton;
    @FXML
    private DatePicker specificDatePicker; // New DatePicker
    private NotificationManager notificationManager;
    private Task currentTask;  // The task for which this notification is being set

    public void initialize() {
        notificationManager = NotificationManager.getInstance();

        // Populate the ComboBox with notification categories
        notificationCategoryComboBox.getItems().setAll("One day before ", "One week before", "One month before", "Choose date");
        notificationCategoryComboBox.setOnAction(event -> {
            String selected = notificationCategoryComboBox.getValue();
            specificDatePicker.setVisible("Choose date".equals(selected));
        });
    }


    public void setTask(Task task) {
        currentTask = task;
        taskTitleLabel.setText(task.getTitle());
    }



    // Usage in your saveNotification method
    @FXML
    private void saveNotification() {
        String selectedCategory = notificationCategoryComboBox.getValue();
        LocalDate notificationDate = null;

        if (selectedCategory == null || (selectedCategory.equals("Choose date") && specificDatePicker.getValue() == null)) {
            System.out.println("Please select a notification category or date.");
            return;
        }

        LocalDate deadlineLocalDate = currentTask.getDeadlineAsLocalDate();

        if ("One day before".equals(selectedCategory)) {
            notificationDate = deadlineLocalDate.minusDays(1);
        } else if ("One week before".equals(selectedCategory)) {
            notificationDate = deadlineLocalDate.minusWeeks(1);}
            else if ("One month before".equals(selectedCategory)) {
                notificationDate = deadlineLocalDate.minusMonths(1);
        } else if ("Choose date".equals(selectedCategory)) {
            notificationDate = specificDatePicker.getValue();
        }

        // Check if notificationDate is valid and proceed
        if (notificationDate != null && !notificationDate.isBefore(LocalDate.now())) {
            Date notificationDateAsDate = Date.from(notificationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String notificationDetail = String.format("Notification on %s", notificationDate);

            // Adjust this part if needed for your Notification class
            Notification newNotification = new Notification(currentTask.getTitle(), notificationDetail);
            notificationManager.addNotification(newNotification);

            Stage stage = (Stage) saveNotificationButton.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("Invalid notification date selected.");
        }
    }
}


