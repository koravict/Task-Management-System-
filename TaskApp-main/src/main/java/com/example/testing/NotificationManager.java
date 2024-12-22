package com.example.testing;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class NotificationManager {
    private static final String NOTIFICATIONS_FILE_PATH = "notifications.json";
    private static NotificationManager instance; // Singleton instance
    private ObservableList<Notification> notifications;

    public NotificationManager() {
        notifications = FXCollections.observableArrayList();
        loadNotificationsFromJson();
    }

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public ObservableList<Notification> getNotifications() {
        return notifications;
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
        saveNotificationsToJson();
    }
    public void removeNotificationsForTask(Task task) {
        // Use a JavaFX platform run later to ensure any UI updates occur on the JavaFX Application Thread
        Platform.runLater(() -> {
            notifications.removeIf(n -> n.getTaskTitle().equals(task.getTitle()));
            saveNotificationsToJson(); // Persist changes
        });
    }
    // Other methods unchanged...

    // Save notifications to notifications.json
    public void saveNotificationsToJson() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(NOTIFICATIONS_FILE_PATH)) {
            gson.toJson(notifications, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load notifications from notifications.json
    private void loadNotificationsFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(NOTIFICATIONS_FILE_PATH)) {
            Type notificationListType = new TypeToken<List<Notification>>() {}.getType();
            List<Notification> loadedNotifications = gson.fromJson(reader, notificationListType);
            if (loadedNotifications != null) {
                notifications.addAll(loadedNotifications);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


