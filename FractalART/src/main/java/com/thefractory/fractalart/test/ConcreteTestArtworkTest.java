package com.thefractory.fractalart.test;

import com.thefractory.fractalart.ConcreteTestArtwork;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ConcreteTestArtworkTest extends Application {

    @Override
    public void start(Stage primaryStage) {


		ConcreteTestArtwork test = new ConcreteTestArtwork("test");
		TabPane tp = new TabPane(test.getTab());
		
        StackPane stack = new StackPane();
        stack.getChildren().add(tp);
        Scene scene = new Scene(stack);

        primaryStage.setTitle("InfoIcon Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
