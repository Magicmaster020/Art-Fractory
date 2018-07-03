package com.thefractory.fractalart;

import java.io.IOException;

import com.thefractory.customcomponents.NumberField;
import com.thefractory.fractalart.utils.JavaFXUtils;

import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

public abstract class Artwork {
		
	private String name;
	
	protected WritableImage image;
	private Image fullScreenImage;
	private int lowResolution = 30;
	
	private Tab tab;
	@FXML protected RightPane rightPane;
    @FXML private SplitPane splitPane;
    @FXML private AnchorPane mainAnchorPane;
    @FXML private AnchorPane rightAnchorPane;
    
    
    protected ChangeListener<Object> updateListener = new ChangeListener<Object>() {
        @Override
        public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
            updateImage((int) rightPane.resolutionField.getValue());
        }
    };
	
	public Artwork(String name) {
		this.name = name; 
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Artwork.fxml"));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
		this.tab = new Tab(name, splitPane);
		for(Node child : JavaFXUtils.getNodesFromType(rightPane, NumberField.class)) {
			((NumberField) child).valueProperty().addListener(updateListener);
		}
	}
	
	protected void setMainPane(StackPane mainPane) {
        this.mainAnchorPane.getChildren().add(mainPane);
        AnchorPane.setTopAnchor(mainPane, 0.0);
        AnchorPane.setRightAnchor(mainPane, 0.0);
        AnchorPane.setBottomAnchor(mainPane, 0.0);
        AnchorPane.setLeftAnchor(mainPane, 0.0);
	}
	
	protected void setControlPanelPrefWidth(double value) {
		mainAnchorPane.setPrefWidth(value);
	}
	
	public void getFile() {
		
	}
	
	public Artwork openFromFile() {
		return null;
	}
	
	protected void setImage() {
		rightPane.setImage(this.image);
	}
	
	public void updateImage(int resolution) {
		setImage(getImage(resolution, resolution));
		setImage();
	}
	
	public abstract void init();
	
	public Tab getTab() {
		return tab;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public WritableImage getImage() {
		return image;
	}
	
	public abstract WritableImage getImage(int height, int length);
	
	public void setImage(WritableImage image) {
		this.image = image;
	}
	public Image getFullScreenImage() {
		return fullScreenImage;
	}
	public void setFullScreenImage(Image fullScreenImage) {
		this.fullScreenImage = fullScreenImage;
	}
	public int getLowResolution() {
		return lowResolution;
	}
	public void setLowResolution(int lowResolution) {
		this.lowResolution = lowResolution;
	}
}
