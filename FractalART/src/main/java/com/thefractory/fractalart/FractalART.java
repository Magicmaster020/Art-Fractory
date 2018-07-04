package com.thefractory.fractalart;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FractalART extends Application {

	@Override
    public void start(Stage primaryStage) throws Exception {
		Model m = Model.getInstance();
		View v = new View(m);
		@SuppressWarnings("unused")
		Controller c = new Controller(m, v);
		
		//TODO !!TEMPORARY!!
		v.getTabPane().getTabs().add(new MandelbrotSet());
		//TODO !!TEMPORARY!!
		
	    primaryStage.setTitle("FractalART");
	    primaryStage.setScene(new Scene(v));
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
