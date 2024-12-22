package com.example.testing;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Task {
    private String title;
    private String description;
    private String category;
    private String priority; // Numeric priority
    private Date deadline; // Changed to Date
    private String status;

    // No-args constructor (required by Gson for deserialization)


    // Constructor with all fields
    public Task(String title, String description, String category, String priority, Date deadline, String status) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
    }
    public LocalDate getDeadlineAsLocalDate() {
        return deadline.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        if (priority == null || priority.isEmpty()) {
            this.priority = "Default";
        } else {
            this.priority = priority;
        }
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return title + " (" + category + ")";
    }
}










