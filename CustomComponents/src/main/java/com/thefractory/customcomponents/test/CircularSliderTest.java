package com.thefractory.customcomponents.test;

import com.thefractory.customcomponents.CircularSlider2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CircularSliderTest extends Application {
	
    @Override
    public void start(Stage primaryStage) {
    	
		CircularSlider2 cslider = new CircularSlider2(360,0);
		StackPane stack = new StackPane(cslider);
		
        Scene scene = new Scene(stack);

        primaryStage.setTitle("Circular Slider Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
