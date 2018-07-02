package com.thefractory.fractalart.test;

import com.thefractory.fractalart.HofstadtersButterfly;
import com.thefractory.fractalart.MandelbrotSet;
import com.thefractory.fractalart.SimpleMandelbrotSet;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ArtworkTest extends Application {

	public static Stage primaryStage;
	
    @Override
    public void start(Stage primaryStage) {

    	ArtworkTest.primaryStage = primaryStage;
    	
		SimpleMandelbrotSet test = new SimpleMandelbrotSet();
//		HofstadtersButterfly test2 = new HofstadtersButterfly();
		TabPane tabPane = new TabPane(test.getTab());
//		tabPane.getTabs().add(test2.getTab());
		
        Scene scene = new Scene(tabPane);
        
        primaryStage.setTitle("InfoIcon Test");
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