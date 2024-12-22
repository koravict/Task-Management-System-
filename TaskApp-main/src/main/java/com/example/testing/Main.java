package com.example.testing;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Main extends Application {
    private MainController mainController;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/icon.png")));
        primaryStage.setTitle("Media Lab Assistant");
        primaryStage.setScene(scene);
        primaryStage.show();
        mainController = fxmlLoader.getController();
        displayDelayedTasksPopup(mainController.getDelayedTasks());
    }
    private void displayDelayedTasksPopup(List<String> delayedTasks) {
        if (delayedTasks != null && !delayedTasks.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delayed Tasks");
            alert.setHeaderText("You have the following delayed tasks:");

            // Create a string with all delayed tasks
            String content = String.join("\n", delayedTasks);
            alert.setContentText(content);

            alert.showAndWait();
        }
    }
    @Override
    public void stop() {
        if (mainController != null) {
            mainController.saveDataOnExit();  // Save data on application close
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
