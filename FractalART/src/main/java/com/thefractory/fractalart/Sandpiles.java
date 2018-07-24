package com.thefractory.fractalart;

import java.io.IOException;

import com.thefractory.customcomponents.GradientPicker;
import com.thefractory.customcomponents.NumberSlider;
import com.thefractory.fractalart.utils.EnhancedCallable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;

public class Sandpiles extends Artwork {

	public static String defaultName = "Sandpiles";
	public static String description = "The Sandpiles make an abelian group who happen to create "
			+ "beautiful tile patterns when coloured properly.";
//	public static Image firstImage = new Image("file:src/main/resources/com/thefractory/fractalart/MandelbrotFirstImage.tif");
//	public static Image secondImage = new Image("file:src/main/resources/com/thefractory/fractalart/MandelbrotSecondImage.tif");
	
	@FXML private StackPane controlPanel;
	@FXML private RadioButton triangular;
	@FXML private RadioButton square; 
	@FXML private RadioButton hexagonal;
	@FXML private CheckBox toppleUntilDone;
	@FXML private NumberSlider toppleSlider;
	@FXML private NumberSlider sandSlider;
	@FXML private NumberSlider gridSizeSlider;
	@FXML private GradientPicker gradientPicker;
	
	public Sandpiles() {
		super();
		name = defaultName;
		this.setText(name);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SandPiles.fxml"));
		fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        setMainPane(controlPanel);
		setControlPanelPrefWidth(800);

		toppleUntilDone.selectedProperty().addListener(updateListener);
		toppleSlider.valueProperty().addListener(updateListener);
		sandSlider.valueProperty().addListener(updateListener);
		gradientPicker.paletteProperty().addListener(updateListener);
		
		init();
	}
		
	@Override
    public void init() {}
	
	@Override
	public WritableImage getImage(int height, int length, EnhancedCallable<WritableImage> task) {
		
		
		return null;
	}

}
