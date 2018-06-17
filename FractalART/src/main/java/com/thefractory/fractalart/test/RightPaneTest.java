package com.thefractory.fractalart.test;

import com.thefractory.fractalart.RightPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class RightPaneTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        RightPane rightPane = new RightPane();

        Scene scene = new Scene(rightPane);

        primaryStage.setTitle("Right Pane Test");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            	 Platform.exit();
            	 System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}