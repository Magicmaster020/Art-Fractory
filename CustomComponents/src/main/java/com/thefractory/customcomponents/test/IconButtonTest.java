package com.thefractory.customcomponents.test;

import com.thefractory.customcomponents.IconButton;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class IconButtonTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        Button iconButton = new IconButton("fullScreen");

        StackPane stack = new StackPane();
        stack.getChildren().add(iconButton);
        Scene scene = new Scene(stack);

        primaryStage.setTitle("InfoIcon Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
