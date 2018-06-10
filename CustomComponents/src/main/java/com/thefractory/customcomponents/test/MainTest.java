package com.thefractory.customcomponents.test;

import com.thefractory.customcomponents.InfoIcon;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        InfoIcon infoIcon = new InfoIcon();
        infoIcon.setTipText("Default Tooltip");

        Scene scene = new Scene(infoIcon);

        primaryStage.setTitle("Main Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}