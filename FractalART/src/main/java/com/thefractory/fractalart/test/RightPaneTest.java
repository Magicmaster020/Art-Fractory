package com.thefractory.fractalart.test;

import com.thefractory.fractalart.RightPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RightPaneTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        RightPane rightPane = new RightPane();

        Scene scene = new Scene(rightPane);

        primaryStage.setTitle("Main Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}