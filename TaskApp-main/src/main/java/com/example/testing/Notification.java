package com.example.testing;

import java.io.Serializable;

public class Notification {
    private String taskTitle;
    private String notificationCategory;  // Category of notification (e.g., "Reminder", "Deadline", etc.)

    public Notification(String taskTitle, String notificationCategory) {
        this.taskTitle = taskTitle;
        this.notificationCategory = notificationCategory;
    }

    // Getters and setters
    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getNotificationCategory() {
        return notificationCategory;
    }

    public void setNotificationCategory(String notificationCategory) {
        this.notificationCategory = notificationCategory;
    }
}

