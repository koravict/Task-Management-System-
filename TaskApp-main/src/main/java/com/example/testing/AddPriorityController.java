package com.example.testing;



import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPriorityController {

    @FXML
    private TextField priorityNameField;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void addPriority() {
        String priorityName = priorityNameField.getText();

        if (priorityName.isEmpty()) {
            System.out.println("Please enter a priority name.");
            return;
        }

        mainController.addPriorityToList(priorityName);

        // Close the pop-up window
        Stage stage = (Stage) priorityNameField.getScene().getWindow();
        stage.close();
    }
}

