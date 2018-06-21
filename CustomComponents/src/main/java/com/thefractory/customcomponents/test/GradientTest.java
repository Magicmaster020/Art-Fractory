package com.thefractory.customcomponents.test;

import com.thefractory.customcomponents.Gradient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GradientTest extends Application {
	
    @Override
    public void start(Stage primaryStage) {
    	
		Gradient gradient = new Gradient("blackandwhite");
		StackPane stack = new StackPane(gradient);
		
        Scene scene = new Scene(stack);

        primaryStage.setTitle("Gradient Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
