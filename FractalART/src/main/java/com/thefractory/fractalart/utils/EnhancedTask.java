package com.thefractory.fractalart.utils;

import javafx.concurrent.Task;

public abstract class EnhancedTask extends Task<Object> implements Comparable<EnhancedTask> {
	
	private double urgency = 0;
	private String description = "No description set.";

	@Override
	public int compareTo(EnhancedTask arg0) {
		double diff = this.urgency - arg0.urgency; 
		
		if(diff < 0) return -1;
		if(diff == 0) return 0;
		else return 1;
	}
	
	public double getUrgency() {
		return urgency;
	}
	public void setUrgency(double urgency) {
		this.urgency = urgency;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
