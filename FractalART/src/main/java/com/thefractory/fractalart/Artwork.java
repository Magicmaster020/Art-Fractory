package com.thefractory.fractalart;

import java.awt.image.BufferedImage;

import javafx.scene.control.Tab;

public abstract class Artwork {
	
	private String name;
	private String defaultName;
	private double coordinates[] = {0,0};
	private double angle = 0;
	private double scale = 1;
	
	private BufferedImage image;
	private BufferedImage fullScreenImage;
	private int lowResolution = 50;
	private int previewResolution = 300;
	
	private double proportions;
	
	private Tab tab;
	
	

}
