package com.thefractory.fractalart;

import java.io.IOException;

import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;

public abstract class Artwork {
	
	private double[] coordinatesDefault = {0,0};
	private double angleDefault = 0;
	private double scaleDefault = 1;
	private int previewResolutionDefault = 300;
	private double proportionsDefault = 1;
	
	private String name;
	private double[] coordinates = coordinatesDefault.clone();
	private double angle = angleDefault;
	private double scale = scaleDefault;
	
	protected Image image;
	private Image fullScreenImage;
	private int lowResolution = 50;
	private int previewResolution = previewResolutionDefault;
	
	private double proportions = proportionsDefault;
	
	private Tab tab;
	@FXML protected RightPane rightPane;
    @FXML private SplitPane splitPane;
    @FXML private StackPane mainStackPane;
    @FXML private AnchorPane rightAnchorPane;
	
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
	}
	
	protected void addMainPane(StackPane mainPane) {
        this.mainStackPane.getChildren().add(mainPane);
	}
	
	public void getFile() {
		
	}
	
	public Artwork openFromFile() {
		return null;
	}
	
	private void setImage() {
		rightPane.setImage(this.image);
	}
	
	public abstract void updateImage();
	
	
	
	
	public Tab getTab() {
		return tab;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double[] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
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
	public int getPreviewResolution() {
		return previewResolution;
	}
	public void setPreviewResolution(int previewResolution) {
		this.previewResolution = previewResolution;
	}
	public double getProportions() {
		return proportions;
	}
	public void setProportions(double proportions) {
		this.proportions = proportions;
	}
}
