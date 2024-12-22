package com.example.testing;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class EditCategoryController {

    @FXML
    private ListView<String> categoryListView;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCategoryList(List<String> categories) {
        categoryListView.getItems().setAll(categories);
    }

    @FXML
    private void initialize() {
        // Add buttons for each category in the ListView
        categoryListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String category, boolean empty) {
                super.updateItem(category, empty);
                if (empty || category == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(10);
                    Label label = new Label(category);
                    Button deleteButton = new Button("Delete");
                    Button editButton = new Button("Edit");

                    deleteButton.setOnAction(event -> {
                        mainController.removeCategoryFromList(category);
                        categoryListView.getItems().remove(category);
                    });

                    editButton.setOnAction(event -> openEditCategoryForm(category));

                    hBox.getChildren().addAll(label, editButton, deleteButton);
                    setGraphic(hBox);
                    setText(null);
                }
            }
        });
    }
    @FXML
    private void openAddCategoryForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCategory.fxml"));
            Pane root = loader.load();

            AddCategoryController controller = loader.getController();
            controller.setMainController(mainController);

            Stage stage = new Stage();
            stage.setTitle("Add Category");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the ListView after adding a new category
            categoryListView.getItems().setAll(mainController.getCategoryList());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openEditCategoryForm(String category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCategoryName.fxml"));
            VBox root = loader.load();

            EditCategoryNameController controller = loader.getController();
            controller.setMainController(mainController);
            controller.setOldCategoryName(category);

            Stage stage = new Stage();
            stage.setTitle("Edit Category");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the ListView after editing a category
            categoryListView.getItems().setAll(mainController.getCategoryList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

