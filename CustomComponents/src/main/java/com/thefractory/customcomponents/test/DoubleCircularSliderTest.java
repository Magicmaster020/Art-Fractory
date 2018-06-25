package com.thefractory.customcomponents.test;

import com.thefractory.customcomponents.DoubleCircularSlider;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DoubleCircularSliderTest extends Application {
	
    @Override
    public void start(Stage primaryStage) {
    	
		DoubleCircularSlider cslider = new DoubleCircularSlider(360,0,2,0);
		StackPane stack = new StackPane(cslider);
		
        Scene scene = new Scene(stack);

        primaryStage.setTitle("Double Circular Slider Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
