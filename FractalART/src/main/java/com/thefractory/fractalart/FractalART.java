package com.thefractory.fractalart;
import javafx.application.Application;
import javafx.stage.Stage;

public class FractalART extends Application {

	@Override
    public void start(Stage primaryStage) throws Exception {
		Model m = new Model();
		View v = new View(m);
		@SuppressWarnings("unused")
		Controller c = new Controller(m, v);
		
	    primaryStage.setTitle("Mathematical Art");
	    primaryStage.setScene(v.getScene());
	    primaryStage.show();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
