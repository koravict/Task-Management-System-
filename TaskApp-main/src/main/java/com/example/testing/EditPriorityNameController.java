package com.example.testing;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditPriorityNameController {

    @FXML
    private TextField priorityNameField;

    private MainController mainController;
    private String oldPriorityName;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setOldPriorityName(String oldPriorityName) {
        this.oldPriorityName = oldPriorityName;
        priorityNameField.setText(oldPriorityName);
    }

    @FXML
    private void savePriorityChanges() {
        String newPriorityName = priorityNameField.getText();

        if (newPriorityName.isEmpty()) {
            System.out.println("Please enter a new priority name.");
            return;
        }

        mainController.updatePriorityName(oldPriorityName, newPriorityName);

        // Close the window
        Stage stage = (Stage) priorityNameField.getScene().getWindow();
        stage.close();
    }
}
