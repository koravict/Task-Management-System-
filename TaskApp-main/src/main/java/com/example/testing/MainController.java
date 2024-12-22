package com.example.testing;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class MainController {
    private static final String PRIORITIES_FILE_PATH = "priorities.json";
    private final List<String> priorityList = new ArrayList<>();
    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> categoryColumn;
    @FXML
    private TableColumn<Task, String> priorityColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableColumn<Task, Date> deadlineColumn;
    @FXML
    private TableColumn<Task, Void> actionColumn;
    @FXML
    private TextField titleSearchField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> priorityComboBox;
    private StringProperty totalTasksProperty = new SimpleStringProperty("0");
    @FXML
    private Label totalTasksLabel;
    @FXML
    private Label completedTasksLabel;
    @FXML
    private Label delayedTasksLabel;
    @FXML
    private Label dueSoonTasksLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private StringProperty completedTasksProperty = new SimpleStringProperty("0");
   @FXML
    private StringProperty delayedTasksProperty = new SimpleStringProperty("0");
   @FXML
    private StringProperty dueSoonTasksProperty = new SimpleStringProperty("0");
    public void updateSummaryStatistics() {
        int totalTasks = taskList.size();
        totalTasksProperty.set(String.valueOf(totalTasks));
        long completedTasks = taskList.stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .count();
        completedTasksProperty.set(String.valueOf(completedTasks));

        // Delayed tasks
        long delayedTasks = taskList.stream()
                .filter(task -> "Delayed".equalsIgnoreCase(task.getStatus()))
                .count();
        delayedTasksProperty.set(String.valueOf(delayedTasks));

        // Due Soon tasks (within 7 days)
        long dueSoonTasks = taskList.stream()
                .filter(task -> {
                    if (task.getDeadline() == null) return false;
                    LocalDate deadlineDate = task.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate today = LocalDate.now();
                    return !deadlineDate.isBefore(today) && deadlineDate.isBefore(today.plusDays(7));
                })
                .count();
        dueSoonTasksProperty.set(String.valueOf(dueSoonTasks));
    }
    public List<String> getDelayedTasks() {
        // Filter tasks to get only those with "Delayed" status
        return taskList.stream()
                .filter(task -> "Delayed".equalsIgnoreCase(task.getStatus()))
                .map(Task::getTitle)  // We assume you want only the title of delayed tasks
                .toList();
    }
    // Assuming priority is numeric
    public void setCategoryList(List<String> categories) {
        categoryComboBox.getItems().setAll(categories);
    }
    public void setPriorityList(List<String> priorities) {
        priorityComboBox.getItems().setAll(priorities);
    }
    @FXML
    private void handleSearchButtonClick() {
        String titleCriteria = titleSearchField.getText().toLowerCase();
        String categoryCriteria = categoryComboBox.getValue();
        String priorityCriteria = priorityComboBox.getValue();

        // Filter tasks based on criteria
        List<Task> filteredTasks = taskList.stream()
                .filter(task -> titleCriteria.isEmpty() || task.getTitle().toLowerCase().contains(titleCriteria))
                .filter(task -> categoryCriteria == null || task.getCategory().equalsIgnoreCase(categoryCriteria))
                .filter(task -> priorityCriteria == null || task.getPriority().equals(priorityCriteria))
                .toList();

        // Update the TableView with filtered tasks
        taskTableView.setItems(FXCollections.observableArrayList(filteredTasks));
    }
    @FXML
    private void handleResetButtonClick() {
        titleSearchField.clear();
        categoryComboBox.setValue(null);
        priorityComboBox.setValue(null);

        // Show all tasks in the TableView
        taskTableView.setItems(FXCollections.observableArrayList(taskList));
    }

    @FXML

    private void openPrioritiesForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditPriority.fxml"));
            Pane root = loader.load();

            EditPriorityController controller = loader.getController();
            controller.setMainController(this);
            controller.setPriorityList(priorityList);

            Stage stage = new Stage();
            stage.setTitle("Priorities");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Existing methods for categories, tasks, etc.

    // Methods for managing priorities

    public void addPriorityToList(String priority) {
        priorityList.add(priority);

    }

    public void removePriorityFromList(String priority) {
        if (priorityList.contains(priority)) {
            // Update tasks with the removed priority to "Default"
            updateTasksWithDefaultPriority(priority);

            // Remove the priority
            priorityList.remove(priority);

        }
    }
    private void updateTasksWithDefaultPriority(String removedPriority) {
        for (Task task : taskList) {
            if (task.getPriority().equals(removedPriority)) {
                task.setPriority("Default");
            }
        }
        taskTableView.refresh();
        taskTableView.setItems(FXCollections.observableArrayList(taskList));
        // Refresh the ListView
    }

    public List<String> getPriorityList() {
        return priorityList;
    }

    // Save priorities to JSON
    private void savePrioritiesToJson() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(PRIORITIES_FILE_PATH)) {
            gson.toJson(priorityList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load priorities from JSON
    private void loadPrioritiesFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(PRIORITIES_FILE_PATH)) {
            Type priorityListType = new TypeToken<List<String>>() {}.getType();
            List<String> loadedPriorities = gson.fromJson(reader, priorityListType);
            if (loadedPriorities != null) {
                priorityList.addAll(loadedPriorities);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updatePriorityName(String oldName, String newName) {
        int index = priorityList.indexOf(oldName);
        if (index != -1) {
            priorityList.set(index, newName);

            // Update tasks with the new priority name if necessary
            for (Task task : taskList) {
                if (task.getPriority().equals(oldName)) {
                    task.setPriority(newName);
                }
            }

            // Refresh the table view if necessary
            taskTableView.refresh();
        }
    }


    @FXML
    private ListView<Task> taskListView;
private void deleteNotificationsForTask(Task task) {
    List<Notification> notifications = notificationManager.getNotifications();
    notifications.removeIf(notification -> notification.getTaskTitle().equals(task.getTitle()));
    notificationManager.saveNotificationsToJson();
}
    private final ObservableList<Task> taskList = FXCollections.observableArrayList();
    private final List<String> categoryList = new ArrayList<>();
    private static final String TASKS_FILE_PATH = "tasks.json";
    private static final String CATEGORIES_FILE_PATH = "categories.json";


    public void initialize() {
        loadTasksFromJson();  // Loading tasks from JSON
        loadCategoriesFromJson();  // Loading categories from JSON
        loadPrioritiesFromJson();
        notificationManager = NotificationManager.getInstance();
        updateTaskStatuses();
        totalTasksLabel.textProperty().bind(totalTasksProperty);
        completedTasksLabel.textProperty().bind(completedTasksProperty);
        delayedTasksLabel.textProperty().bind(delayedTasksProperty);
        dueSoonTasksLabel.textProperty().bind(dueSoonTasksProperty);
        // Update the task count initially
        updateSummaryStatistics();
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryList));
        priorityComboBox.setItems(FXCollections.observableArrayList(priorityList));
        if (!priorityList.contains("Default")) {
            priorityList.add("Default");
        }

            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

            taskTableView.setItems(FXCollections.observableArrayList(taskList));

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button notificationButton = new Button("Notify");
            private final Button editButton = new Button("Edit");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                HBox buttonGroup = new HBox(notificationButton, editButton);
                buttonGroup.setSpacing(5);  // Adjust space between buttons

                Task task = getTableView().getItems().get(getIndex());


                if ("Completed".equalsIgnoreCase(task.getStatus()) || "Delayed".equalsIgnoreCase(task.getStatus())) {
                    notificationButton.setDisable(true);
                } else {
                    notificationButton.setDisable(false);
                    notificationButton.setOnAction(event -> openTaskNotificationForm(task));
                }


                editButton.setOnAction(event -> openEditTaskForm(task));

                setGraphic(buttonGroup);
            }
        });
        // Set custom cell factory for taskListView



        taskTableView.setItems(FXCollections.observableArrayList(taskList));  // Add loaded tasks to the ListView
    }
    private void updateTaskStatuses() {
        boolean tasksUpdated = false;

        for (Task task : taskList) {
            Date now = new Date();
            if (!"Completed".equalsIgnoreCase(task.getStatus()) && task.getDeadline().before(now)) {
                task.setStatus("Delayed");
                removeNotificationsForTask(task);
                tasksUpdated = true;
            } else if ("Completed".equalsIgnoreCase(task.getStatus())) {
                removeNotificationsForTask(task);
            }
        }

        // Save updated tasks to JSON if changes were made
        if (tasksUpdated) {

        }
    }

    public void removeNotificationsForTask(Task task) {
        notificationManager.removeNotificationsForTask(task);
    }
    @FXML
    private ListView<String> notificationsListView;

    private NotificationManager notificationManager;









        @FXML
        private void handleNotificationsButtonClick() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("notifications.fxml"));
                Parent root = loader.load();

                // Pass Singleton NotificationManager to the NotificationPopupController
                NotificationsController popupController = loader.getController();
                popupController.initializeWithManager(notificationManager);

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Notifications");
                popupStage.setScene(new Scene(root));
                popupStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Other methods unchanged...



    private void openTaskNotificationForm(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskNotification.fxml"));
            Parent root = loader.load();
            TaskNotificationController controller = loader.getController();
            controller.setTask(task);

            // Create a new scene for the notification form
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void openAddTaskForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddTask.fxml"));
            Pane root = loader.load();

            AddTaskController controller = loader.getController();
            controller.setMainController(this);
            controller.setCategoryList(categoryList);
            controller.setPriorityList(priorityList);

            Stage stage = new Stage();
            stage.setTitle("Add Task");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh ListView after adding a task
            taskTableView.setItems(FXCollections.observableArrayList(taskList));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCategoryName(String oldName, String newName) {
        int index = categoryList.indexOf(oldName);
        if (index != -1) {
            categoryList.set(index, newName);

            // Update tasks with the new category name if necessary
            for (Task task : taskList) {
                if (task.getCategory().equals(oldName)) {
                    task.setCategory(newName);
                }
            }

            // Refresh views if necessary
            taskTableView.refresh();
        }
    }
    @FXML
    private void openCategoriesForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCategory.fxml"));
            Pane root = loader.load();

            EditCategoryController controller = loader.getController();
            controller.setMainController(this);
            controller.setCategoryList(categoryList);

            Stage stage = new Stage();
            stage.setTitle("Categories");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void removeCategoryFromList(String category) {
        categoryList.remove(category);

        removeTasksByCategory(category); // Remove tasks associated with this category
    }

    private void removeTasksByCategory(String category) {
        // Remove tasks associated with the deleted category
        taskList.removeIf(task -> task.getCategory().equals(category));

        // Save the updated task list to the JSON file


        // Refresh the ListView
        taskTableView.setItems(FXCollections.observableArrayList(taskList));
    }


    private void openEditTaskForm(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditTask.fxml"));
            Pane root = loader.load();

            // Get the controller
            EditTaskController controller = loader.getController();
            controller.setMainController(this);
            controller.setTask(task);
            controller.setCategoryList(categoryList); // Pass category list to populate the ComboBox
            controller.setPriorityList(priorityList);
            // Show the form
            Stage stage = new Stage();
            stage.setTitle("Edit Task");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            taskTableView.refresh();
            // Refresh the ListView after editing
            taskTableView.setItems(FXCollections.observableArrayList(taskList));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addCategoryToList(String category) {
        categoryList.add(category);

    }

    public void addTaskToList(Task task) {
        if (task.getPriority() == null || task.getPriority().isEmpty()) {
            task.setPriority("Default");
        }
            taskList.add(task);
        taskTableView.getItems().add(task);

    }

    public void removeTaskFromList(Task task) {
        taskList.remove(task);
        deleteNotificationsForTask(task);

        taskTableView.setItems(FXCollections.observableArrayList(taskList));

    }

    public void saveTasksToJson() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(TASKS_FILE_PATH)) {
            gson.toJson(taskList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveCategoriesToJson() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(CATEGORIES_FILE_PATH)) {
            gson.toJson(categoryList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(TASKS_FILE_PATH)) {
            Type taskListType = new TypeToken<List<Task>>() {}.getType();
            List<Task> loadedTasks = gson.fromJson(reader, taskListType);
            if (loadedTasks != null) {
                taskList.addAll(loadedTasks);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCategoriesFromJson() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(CATEGORIES_FILE_PATH)) {
            Type categoryListType = new TypeToken<List<String>>() {}.getType();
            List<String> loadedCategories = gson.fromJson(reader, categoryListType);
            if (loadedCategories != null) {
                categoryList.addAll(loadedCategories);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> getCategoryList() {
        return categoryList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }
    public void saveDataOnExit() {
        saveTasksToJson();
        saveCategoriesToJson();
        savePrioritiesToJson();
    }
}






















































