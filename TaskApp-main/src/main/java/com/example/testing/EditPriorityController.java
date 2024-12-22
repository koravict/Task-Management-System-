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

public class EditPriorityController {

    @FXML
    private ListView<String> priorityListView;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setPriorityList(List<String> priorities) {
        priorityListView.getItems().setAll(priorities);
    }

    @FXML
    private void initialize() {
        priorityListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String priority, boolean empty) {
                super.updateItem(priority, empty);
                if (empty || priority == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(10);
                    Label label = new Label(priority);
                    Button deleteButton = new Button("Delete");
                    Button editButton = new Button("Edit");

                    if (!"Default".equals(priority)) {
                        // Allow delete and edit for non-default priorities
                        deleteButton.setOnAction(event -> {
                            mainController.removePriorityFromList(priority);
                            priorityListView.getItems().remove(priority);
                        });

                        editButton.setOnAction(event -> openEditPriorityForm(priority));
                    } else {
                        // Disable for default priority
                        deleteButton.setDisable(true);
                        editButton.setDisable(true);
                    }

                    hBox.getChildren().addAll(label, editButton, deleteButton);
                    setGraphic(hBox);
                    setText(null);
                }
            }
        });
    }

    private void openEditPriorityForm(String priority) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditPriorityName.fxml"));
            VBox root = loader.load();

            EditPriorityNameController controller = loader.getController();
            controller.setMainController(mainController);
            controller.setOldPriorityName(priority);

            Stage stage = new Stage();
            stage.setTitle("Edit Priority");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the ListView after editing a priority
            priorityListView.getItems().setAll(mainController.getPriorityList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void openAddPriorityForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPriority.fxml"));
            Pane root = loader.load();

            AddPriorityController controller = loader.getController();
            controller.setMainController(mainController);

            Stage stage = new Stage();
            stage.setTitle("Add Priority");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh the ListView after adding a new category
            priorityListView.getItems().setAll(mainController.getPriorityList());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

