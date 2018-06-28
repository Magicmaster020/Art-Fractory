package com.thefractory.customcomponents.test;

import com.thefractory.customcomponents.Gradient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GradientTest extends Application {
	
    @Override
    public void start(Stage primaryStage) {
    	
		Gradient gradient = new Gradient();
		StackPane stack = new StackPane(gradient);
		gradient.fitWidthProperty().bind(primaryStage.widthProperty());
		
        Scene scene = new Scene(stack);

        primaryStage.setTitle("Gradient Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
