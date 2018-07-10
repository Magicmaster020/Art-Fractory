package com.thefractory.fractalart.utils;

import java.util.concurrent.Callable;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

public abstract class EnhancedCallable<T> implements Callable<T> {
	
	private String description = "No description set.";
	private final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();

	public EnhancedCallable() {
		super();
	}
	
	public EnhancedCallable(String description) {
		this();
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public ReadOnlyDoubleProperty progressProperty() {
        return progress.getReadOnlyProperty() ;
    }
    public final double getProgress() {
        return progressProperty().get();
    }
    public final void setProgress(double progress) {
    	if(progress >= 0 && progress <= 1) {
    		this.progress.set(progress);
    	}
    }
}
