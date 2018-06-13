package com.thefractory.fractalart;

import java.awt.image.BufferedImage;

import javafx.scene.control.Tab;

public abstract class Artwork {
	
	private String nameDefault;
	private double[] coordinatesDefault = {0,0};
	private double angleDefault = 0;
	private double scaleDefault = 1;
	private int previewResolutionDefault = 300;
	private double proportionsDefault = 1;
	
	private String name = nameDefault;
	private double[] coordinates = coordinatesDefault.clone();
	private double angle = angleDefault;
	private double scale = scaleDefault;
	
	private BufferedImage image;
	private BufferedImage fullScreenImage;
	private int lowResolution = 50;
	private int previewResolution = previewResolutionDefault;
	
	private double proportions = proportionsDefault;
		
	public void initialize() {
		
	}
	
	
	
	public boolean resetDefaults() {
		this.coordinates = coordinatesDefault.clone();
		this.angle = angleDefault;
		this.scale = scaleDefault;
		this.previewResolution = previewResolutionDefault;
		this.proportions = proportionsDefault;
		
		return true;
	}
	
	public boolean recenter() {
		this.coordinates = coordinatesDefault.clone();
		this.angle = angleDefault;
		this.scale = scaleDefault;
		this.previewResolution = previewResolutionDefault;
		this.proportions = proportionsDefault;
		
		return true;
	}
	
	public void getFile() {
		
	}
	
	public Artwork openFromFile() {
		return null;
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
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public BufferedImage getFullScreenImage() {
		return fullScreenImage;
	}
	public void setFullScreenImage(BufferedImage fullScreenImage) {
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
