package com.thefractory.fractalart;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;

public class Cell {
	private Cell[] neighbours; //list of neighbours
	private final IntegerProperty value; //grains of sand
	private final int numberOfNeighbours; //number of neighbours
	private int tempValue; //for simultaneous toppling
	private final int sideLength = 2;
	private final int toppleHeight;
	
	public Cell(int number, int numberOfNeighbours, int toppleHeight, Grid grid) {
		neighbours = new Cell[numberOfNeighbours];
		this.value = new SimpleIntegerProperty(this, "value", number);
		this.numberOfNeighbours = numberOfNeighbours;
		this.toppleHeight = toppleHeight;
		tempValue = 0;
	}
	
	public boolean addNeighbour(Cell neighbour) {
		boolean added = false;
		for(int i = 0; i < numberOfNeighbours; i++) {
			if(neighbours[i] == null) {
				neighbours[i] = neighbour;
				added = true;
				break;
			}
		}
		return added;
	}
	
	public boolean topple(boolean discardRemainder) {
		if(getValue() < toppleHeight) {//do not topple
			return false;
		} else {
			if(toppleHeight >= numberOfNeighbours) {//then we can topple
				int giveNumber = (int) (getValue() - getValue()%numberOfNeighbours)/numberOfNeighbours;
				for(Cell neighbour : neighbours) {
					if(neighbour != null) {
						neighbour.add(giveNumber);
					}
				}
				if(discardRemainder) {
					setValue(0);
				} else {			
					int newValue = (int) getValue()%numberOfNeighbours;
					setValue(newValue);
				}
				return true;
			} else {
				return false;
			}
		}
	}
	
	public void add(int number) {
		tempValue += number;
	}
	
	public void update() {//Adds the temporary value to the value. This value comes from the current toppling run.
		this.value.setValue(getValue() + tempValue);
		tempValue = 0;
	}
	
	public double[] getShapeX() {
		double[] xcord = new double[numberOfNeighbours];
		if(numberOfNeighbours == 4) {
			xcord[0] = 0;
			xcord[1] = sideLength;
			xcord[2] = sideLength;
			xcord[3] = 0;
		} else if(numberOfNeighbours == 3) {
			xcord[0] = sideLength/2;
			xcord[1] = sideLength;
			xcord[2] = 0;
		} else if(numberOfNeighbours == 6) {
			xcord[0] = sideLength/2;
			xcord[1] = 3*sideLength/2;
			xcord[2] = 2*sideLength;
			xcord[3] = 3*sideLength/2;
			xcord[4] = sideLength/2;
			xcord[5] = 0;
		}
		return xcord;
	}
	
	public double[] getShapeY() {
		double[] ycord = new double[numberOfNeighbours];
		if(numberOfNeighbours == 4) {
			ycord[0] = 0;
			ycord[1] = 0;
			ycord[2] = sideLength;
			ycord[3] = sideLength;
		} else if(numberOfNeighbours == 3) {
			ycord[0] = 0;
			ycord[1] = sideLength*Math.sin(Math.PI/3);
			ycord[2] = sideLength*Math.sin(Math.PI/3);
		} else if(numberOfNeighbours == 6) {
			ycord[0] = 0;
			ycord[1] = 0;
			ycord[2] = sideLength*Math.sin(Math.PI/3);
			ycord[3] = 2*sideLength*Math.sin(Math.PI/3);
			ycord[4] = 2*sideLength*Math.sin(Math.PI/3);
			ycord[5] = sideLength*Math.sin(Math.PI/3);
		}
		return ycord;
	}
	
	//TODO DEPRECATED
	public Color getColor() {
		Color col = Color.web("#ffffff");
		if(getValue() == 1) {
			col = Color.web("#f7bbe2");
		}else if(getValue() == 2) {
			col = Color.web("#000000");
		}else if(getValue() == 3) {
			col = Color.web("#eaffea");
		}else if(getValue() == 4) {
			col = Color.web("#ce147d");
		}else if(getValue() == 5) {
			col = Color.web("#bf1e79");
		}else if(getValue() > 5) {
			col = Color.web("#ffffff");
		}
		return col;
	}
	
	public long[] getOffsetLength() {
		if(numberOfNeighbours == 4) {
			return new long[]{sideLength, sideLength};
		} else if(numberOfNeighbours == 3) {
			return new long[]{sideLength/2, (long) (sideLength*Math.cos(Math.PI/6))};
		} else {//=6
			return new long[] {(long) (3*sideLength/2), (long) (2*sideLength*Math.sin(Math.PI/3))};
		}
	}
	
	public int getSideLength() {
		return sideLength;
	}
	
	
	//Getters and setters for properties
	public final int getValue(){
		return value.getValue();
	}
	public final void setValue(int value){
		this.value.setValue(value);
	}
	public final IntegerProperty valueProperty(){
		return value;
	}
	
	
}
