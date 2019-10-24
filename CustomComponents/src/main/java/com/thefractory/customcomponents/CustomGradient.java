package com.thefractory.customcomponents;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;

public abstract class CustomGradient extends VBox {
	
	protected final ObjectProperty<Gradient> palette = new SimpleObjectProperty<Gradient>();
}
