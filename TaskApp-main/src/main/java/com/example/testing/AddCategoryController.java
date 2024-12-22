package com.example.testing;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCategoryController {

    @FXML
    private TextField categoryNameField;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void addCategory() {
        String categoryName = categoryNameField.getText();

        if (categoryName.isEmpty()) {
            System.out.println("Please enter a category name.");
            return;
        }

        mainController.addCategoryToList(categoryName);

        // Close the pop-up window
        Stage stage = (Stage) categoryNameField.getScene().getWindow();
        stage.close();
    }
}

