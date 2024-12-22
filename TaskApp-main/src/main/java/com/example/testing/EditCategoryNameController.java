package com.example.testing;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCategoryNameController {

    @FXML
    private TextField categoryNameField;

    private MainController mainController;
    private String oldCategoryName;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setOldCategoryName(String oldCategoryName) {
        this.oldCategoryName = oldCategoryName;
        categoryNameField.setText(oldCategoryName);
    }

    @FXML
    private void saveCategoryChanges() {
        String newCategoryName = categoryNameField.getText();

        if (newCategoryName.isEmpty()) {
            System.out.println("Please enter a new category name.");
            return;
        }

        mainController.updateCategoryName(oldCategoryName, newCategoryName);

        // Close the pop-up window
        Stage stage = (Stage) categoryNameField.getScene().getWindow();
        stage.close();
    }
}
