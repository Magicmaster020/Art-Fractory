package com.thefractory.fractalart;

import javafx.scene.Scene;

public class View {
	
	private Model model;
	
	private Scene scene;
	
	public View(Model model) {
		this.model = model;
		
	}

	public Scene getScene() {
		return scene;	
	}
	
}
