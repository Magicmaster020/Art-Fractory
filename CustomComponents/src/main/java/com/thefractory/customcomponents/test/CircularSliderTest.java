package com.thefractory.customcomponents.test;

import com.thefractory.customcomponents.CircularSlider;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CircularSliderTest extends Application {
	
    @Override
    public void start(Stage primaryStage) {
    	
		CircularSlider cslider = new CircularSlider(0,360,"M50,50 M50,0 A50,50 1 0,0 50,100 A50,50 1 0,0 50,0 z",true);
		StackPane stack = new StackPane(cslider);
		//"M50,50 M50,0 A50,50 1 0,1 50,100 A50,50 1 0,1 50,0 z"
        Scene scene = new Scene(stack);

        primaryStage.setTitle("Circular Slider Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
