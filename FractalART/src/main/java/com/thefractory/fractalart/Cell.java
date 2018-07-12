package com.thefractory.fractalart;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Cell {
	private Cell[] neighbors; //list of neighbors
	private final IntegerProperty value; //grains of sand
	private final int numberOfNeighbors; //number of neighbors
	private int tempValue; //for simultaneous toppling
	private final int sideLength = 5;
	private final int toppleHeight;
	
	public Cell(int number, int numberOfNeighbors, int toppleHeight, Grid grid) {
		neighbors = new Cell[numberOfNeighbors];
		this.value = new SimpleIntegerProperty(this, "value", number);
		this.numberOfNeighbors = numberOfNeighbors;
		this.toppleHeight = toppleHeight;
		tempValue = 0;
	}
	
	public void addNeighbor(Cell neighbor) {
		boolean added = false;
		int index = 0;
		while(!added) {
			if(neighbors[index] == null) {
				neighbors[index] = neighbor;
				added = true;
			}else {
				index += 1;
			}
		}
	}
	
	public boolean topple(boolean discardRemainder) {
		if(getValue() < toppleHeight) {//do not topple
			return false;
		}else {
			if(toppleHeight >= numberOfNeighbors) {//then we can topple
				int giveNumber = (int)((getValue() - getValue()%numberOfNeighbors)/numberOfNeighbors);
				for(Cell neighbor:neighbors) {
					if(neighbor != null) {
						neighbor.add(giveNumber);
					}
				}
				if(discardRemainder) {
					setValue(0);
				}else {			
					int newValue = (int)(getValue()%numberOfNeighbors);
					setValue(newValue);
				}
				return true;
			}else {
				return false;}
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
		double[] xcord = new double[numberOfNeighbors];
		if(numberOfNeighbors == 4) {
			xcord[0] = 0;
			xcord[1] = sideLength;
			xcord[2] = sideLength;
			xcord[3] = 0;
		}else if(numberOfNeighbors == 3) {
			xcord[0] = sideLength/2;
			xcord[1] = sideLength;
			xcord[2] = 0;
		}else if(numberOfNeighbors == 6) {
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
		double[] ycord = new double[numberOfNeighbors];
		if(numberOfNeighbors == 4) {
			ycord[0] = 0;
			ycord[1] = 0;
			ycord[2] = sideLength;
			ycord[3] = sideLength;
		}else if(numberOfNeighbors == 3) {
			ycord[0] = 0;
			ycord[1] = sideLength*Math.sin(Math.PI/3);
			ycord[2] = sideLength*Math.sin(Math.PI/3);
		}else if(numberOfNeighbors == 6) {
			ycord[0] = 0;
			ycord[1] = 0;
			ycord[2] = sideLength*Math.sin(Math.PI/3);
			ycord[3] = 2*sideLength*Math.sin(Math.PI/3);
			ycord[4] = 2*sideLength*Math.sin(Math.PI/3);
			ycord[5] = sideLength*Math.sin(Math.PI/3);
		}
		return ycord;
	}
	
	public Color getColor() {
		Color col = Color.BLACK;
		if(getValue() == 1) {
			col = Color.BLUE;
		}else if(getValue() == 2) {
			col = Color.GREEN;
		}else if(getValue() == 3) {
			col = Color.RED;
		}else if(getValue() == 4) {
			col = Color.BROWN;
		}else if(getValue() == 5) {
			col = Color.CYAN;
		}else if(getValue() > 5) {
			col = Color.YELLOW;
		}
		return col;
	}
	
	public long[] getOffsetLength() {
		if(numberOfNeighbors == 4) {
			return new long[]{sideLength, sideLength};
		}else if(numberOfNeighbors == 3) {
			return new long[]{sideLength/2, (long) (sideLength*Math.cos(Math.PI/6))};
		}else {//=6
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
