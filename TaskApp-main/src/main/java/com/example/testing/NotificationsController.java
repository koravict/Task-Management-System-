package com.example.testing;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class NotificationsController {

    @FXML
    private ListView<Notification> notificationsListView;

    private NotificationManager notificationManager;

    public void initializeWithManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;

        // Directly bind the ListView to the ObservableList from NotificationManager
        notificationsListView.setItems(notificationManager.getNotifications());

        // The custom cell factory setup remains unchanged


    // Other methods remain unchanged...

        // Set a custom cell factory to display notifications with a delete button
        notificationsListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Notification notification, boolean empty) {
                super.updateItem(notification, empty);

                if (empty || notification == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create an HBox to hold the taskTitle, category, and delete button
                    HBox hBox = new HBox(10); // 10px spacing

                    // Labels for taskTitle and category
                    Label titleLabel = new Label(notification.getTaskTitle());
                    Label categoryLabel = new Label("(" + notification.getNotificationCategory() + ")");

                    // Delete button
                    Button deleteButton = new Button("Delete");
                    deleteButton.setOnAction(e -> {
                        // Remove the notification
                        notificationManager.getNotifications().remove(notification);
                        notificationManager.saveNotificationsToJson();

                    });

                    // Add all elements to the HBox
                    hBox.getChildren().addAll(titleLabel, categoryLabel, deleteButton);
                    setGraphic(hBox);
                }
            }
        });
    }
}



