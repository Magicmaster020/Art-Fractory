package com.thefractory.customcomponents.test;

import javafx.beans.value.ChangeListener;

import com.thefractory.customcomponents.CircularSlider;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CircularSliderTest extends Application {
	
    @Override
    public void start(Stage primaryStage) {
    	
    	CircularSlider cslider = new CircularSlider(0, 360, "M50,0 A50,50 0 0 0 50,100 A50,50 0 0 0 50,0 z", true, 0);
        CircularSlider cslider2 = new CircularSlider(0, 360, "M50,20 A30,30 0 0 0 50,80 A30,30 0 0 0 50,20 z", true, 0);
        StackPane stack = new StackPane();
        stack.getChildren().addAll(cslider2, cslider);
        Scene scene = new Scene(stack);

        cslider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                System.out.println(arg2);
            }

        });

        primaryStage.setTitle("Circular Slider Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
