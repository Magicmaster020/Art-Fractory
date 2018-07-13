package com.thefractory.fractalart;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class View extends BorderPane {
	
	private Model model;
	
	@FXML private TabPane tabPane;
	@FXML private HBox progressBox;
	
	public View(Model model) {
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
		this.model = model;
		Model.THREAD_POOL.setContainer(progressBox);
	}
	
	@FXML private void exportAs() {
		model.exportAs((Artwork) tabPane.getSelectionModel().getSelectedItem());
	}
	
	private void newArtwork(Artwork artwork) {
		tabPane.getTabs().add(artwork);
		tabPane.getSelectionModel().select(artwork);
	}
	
	@FXML private void newSimpleMandelbrotSet() {
		newArtwork(new SimpleMandelbrotSet());
	}
	
	@FXML private void newMandelbrotSet() {
		newArtwork(new MandelbrotSet());
	}
	
	@FXML private void newHofstadtersButterfly() {
		newArtwork(new HofstadtersButterfly());
	}
	
	@FXML private void newSandPiles() {
		newArtwork(new SandPiles());
	}
	
	public TabPane getTabPane() {
		return tabPane;
	}
}
