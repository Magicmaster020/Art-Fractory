package com.thefractory.customcomponents.test;

import com.thefractory.customcomponents.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SpringSliderTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        Slider springSlider = new SpringSlider();

        StackPane stack = new StackPane();
        stack.getChildren().add(springSlider);
        Scene scene = new Scene(stack);

        primaryStage.setTitle("InfoIcon Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}