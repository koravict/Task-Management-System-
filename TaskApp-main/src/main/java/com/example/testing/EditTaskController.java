package com.example.testing;



import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;



import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class EditTaskController {

    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> priorityComboBox;
    @FXML
    private TableView<Task> taskTableView;
    @FXML private DatePicker deadlinePicker;
    @FXML private ComboBox<String> statusComboBox;

    private MainController mainController;
    private Task task;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCategoryList(List<String> categories) {
        categoryComboBox.getItems().setAll(categories);
    }
    public void setPriorityList(List<String> priorities) {
        priorityComboBox.getItems().setAll(priorities);
    }

    public void setTask(Task task) {
        this.task = task;
        titleField.setText(task.getTitle());
        descriptionField.setText(task.getDescription());
        categoryComboBox.setValue(task.getCategory());
        priorityComboBox.setValue(task.getPriority());
        deadlinePicker.setValue(task.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        statusComboBox.setValue(task.getStatus());

    }

    @FXML
    private void saveTask() {
        task.setTitle(titleField.getText());
        task.setDescription(descriptionField.getText());
        task.setCategory(categoryComboBox.getValue());
        task.setPriority(priorityComboBox.getValue());
        task.setDeadline(Date.from(deadlinePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        task.setStatus(statusComboBox.getValue());

if(task.getStatus().equals("Completed")|| task.getStatus().equals("Delayed")){
    mainController.removeNotificationsForTask(task);
        }
mainController.updateSummaryStatistics();
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void deleteTask() {
        mainController.removeTaskFromList(task);
mainController.updateSummaryStatistics();
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}


