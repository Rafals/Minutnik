package org.example.timerapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class TimerApp extends Application {

    private int seconds = 0;
    private Timeline timeline;
    private Label timeLabel;
    private double xOffset = 0;
    private double yOffset = 0;
    private static final int RESIZE_MARGIN = 6;

    @Override
    public void start(Stage stage) {
        Label title = new Label("🕒 Minutnik");
        title.getStyleClass().add("top-bar-label");

        Button closeButton = new Button("✖");
        Button minimizeButton = new Button("—");

        closeButton.setOnAction(e -> stage.close());
        minimizeButton.setOnAction(e -> stage.setIconified(true));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox();
        topBar.setMaxWidth(Double.MAX_VALUE);
        topBar.getStyleClass().add("top-bar");
        topBar.setSpacing(10);
        topBar.setPrefHeight(28);
        topBar.setStyle("-fx-padding: 5px 15px;");
        HBox.setHgrow(title, javafx.scene.layout.Priority.ALWAYS);
        topBar.getChildren().addAll(title,spacer, minimizeButton, closeButton);

        // Umożliwiamy przeciąganie okna
        topBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        topBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        timeLabel = new Label(formatTime(seconds));
        timeLabel.getStyleClass().add("label");

        Button startButton = new Button("▶ Start");
        Button stopButton = new Button("⏸ Stop");
        Button resetButton = new Button("🔄 Reset");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            timeLabel.setText(formatTime(seconds));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        startButton.setOnAction(e -> timeline.play());
        stopButton.setOnAction(e -> timeline.pause());
        resetButton.setOnAction(e -> {
            timeline.stop();
            seconds = 0;
            timeLabel.setText(formatTime(seconds));
        });

        HBox buttonBox = new HBox(15, startButton, stopButton, resetButton);
        buttonBox.setStyle("-fx-alignment: center;");

        VBox root = new VBox(topBar, timeLabel, buttonBox);
        //root.setStyle("-fx-alignment: center; -fx-padding: 40px;");
        timeLabel.setAlignment(Pos.CENTER);
        timeLabel.setMaxWidth(Double.MAX_VALUE);

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Minutnik");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/timer.jpg")));
        stage.setScene(scene);
        stage.show();

        scene.setOnMouseMoved(event -> {
            double x = event.getSceneX();
            double y = event.getSceneY();
            double width = stage.getWidth();
            double height = stage.getHeight();

            if (x >= width - RESIZE_MARGIN && y >= height - RESIZE_MARGIN) {
                scene.setCursor(Cursor.SE_RESIZE);
            } else {
                scene.setCursor(Cursor.DEFAULT);
            }
        });

        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            if (scene.getCursor() == Cursor.SE_RESIZE) {
                stage.setWidth(event.getSceneX());
                stage.setHeight(event.getSceneY());
            } else {
                // Przeciąganie okna
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void main(String[] args) {
        launch();
    }
}
