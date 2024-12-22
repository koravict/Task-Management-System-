package com.example.testing;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AddTaskController {

    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> priorityComboBox;
    @FXML private DatePicker deadlinePicker;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCategoryList(List<String> categories) {
        categoryComboBox.getItems().setAll(categories);
    }
    public void setPriorityList(List<String> priorities) {
        priorityComboBox.getItems().setAll(priorities);
    }

    @FXML
    private void addTask() {
        // Retrieve text from each field
        String title = titleField.getText();
        String description = descriptionField.getText();
        String category = categoryComboBox.getValue();
        String priority = priorityComboBox.getValue();

        // Parse priority as an integer


        // Parse deadline as a Date
        LocalDate localDate = deadlinePicker.getValue();
        if (localDate == null) {
            System.out.println("Please select a valid deadline date.");
            return;
        }
        Date deadline = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Check if other fields are populated
        if (title.isEmpty() || description.isEmpty() || category == null) {
            System.out.println("Please fill in all fields.");
            return;
        }

        // Set status automatically to "open"
        String status = "open";

        // Create a new Task object
        Task newTask = new Task(title, description, category, priority, deadline, status);

        // Pass the task to the main controller
        mainController.addTaskToList(newTask);
mainController.updateSummaryStatistics();
        // Close the pop-up window
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}




