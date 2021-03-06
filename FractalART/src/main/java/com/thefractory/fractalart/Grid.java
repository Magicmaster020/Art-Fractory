package com.thefractory.fractalart;

import java.awt.Dimension;
import java.util.LinkedHashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.canvas.*;
import javafx.scene.image.WritableImage;

public class Grid {
	private int numberOfNeighbours;
	private boolean discardRemainder;
	private Dimension dim;
	private Cell[][] cells;
	private int toppleHeight;
	private boolean seqorsim; //if true - sequential toppling, if false - simultaneous toppling
	
	
	public Grid(int numberOfNeighbours, int toppleHeight, boolean discardRemainder, int gridSizeX, int gridSizeY, boolean seqorsim) {
		this.numberOfNeighbours = numberOfNeighbours;
		this.toppleHeight = toppleHeight;
		this.discardRemainder = discardRemainder;
		dim = new Dimension(gridSizeX, gridSizeY);
		cells = new Cell[gridSizeX][gridSizeY];
		generateCells(numberOfNeighbours);	
		this.seqorsim = seqorsim;
	}
	
	
	private void generateCells(int numNeighbours) {
		 for(int i = 0; i < dim.getWidth(); i++) {
			 for(int j = 0; j < dim.getHeight(); j++) {
				 cells[i][j] = new Cell(0, numNeighbours, toppleHeight, this);
			 }
		 }
		 generateNeighbours(numNeighbours);
	}
	
	private void generateNeighbours(int numNeighbours) {
		if(numNeighbours == 4) {
			for(int i = 0; i < dim.getWidth(); i++) {
				 for(int j = 0; j < dim.getHeight(); j++) {
					 if(j > 0) {cells[i][j].addNeighbour(cells[i][j-1]);}
					 if(i > 0) {cells[i][j].addNeighbour(cells[i-1][j]);}
					 if(i + 1 < dim.getWidth()) {cells[i][j].addNeighbour(cells[i+1][j]);}
					 if(j + 1 < dim.getHeight()) {cells[i][j].addNeighbour(cells[i][j+1]);}
				 }
			 }
		}else if(numNeighbours == 3) {
			for(int i = 0; i < dim.getWidth(); i++) {
				 for(int j = 0; j < dim.getHeight(); j++) {
					 if((i + j)%2 == 0) {//neighbour downward
						 if(j + 1 < dim.getHeight()) {cells[i][j].addNeighbour(cells[i][j+1]);}
					 }else {//neighbour upward
						 if(j > 0) {cells[i][j].addNeighbour(cells[i][j-1]);}
					 }
					 if(i > 0) {cells[i][j].addNeighbour(cells[i-1][j]);}
					 if(i + 1 < dim.getWidth()) {cells[i][j].addNeighbour(cells[i+1][j]);}
				 }
			 }
		}else if(numNeighbours == 6) {
			for(int i = 0; i < dim.getWidth(); i++) {
				 for(int j = 0; j < dim.getHeight(); j++) {
					 if(i > 0) {cells[i][j].addNeighbour(cells[i-1][j]);}
					 if(i + 1 < dim.getWidth()) {cells[i][j].addNeighbour(cells[i+1][j]);}
					 if(i%2!=0) {
						 if(j > 0) {cells[i][j].addNeighbour(cells[i][j-1]);}
						 if(i > 0 && j + 1 < dim.getHeight()) {cells[i][j].addNeighbour(cells[i-1][j+1]);}
						 if(j + 1 < dim.getHeight()) {cells[i][j].addNeighbour(cells[i][j+1]);}
						 if(i + 1 < dim.getWidth() && j + 1 < dim.getHeight()) {cells[i][j].addNeighbour(cells[i+1][j+1]);}
					 }else {
						 if(j + 1 < dim.getHeight()) {cells[i][j].addNeighbour(cells[i][j+1]);}
						 if(i > 0 && j > 0) {cells[i][j].addNeighbour(cells[i-1][j-1]);}
						 if(j > 0) {cells[i][j].addNeighbour(cells[i][j-1]);}
						 if(i + 1 < dim.getWidth() && j > 0) {cells[i][j].addNeighbour(cells[i+1][j-1]);}
					 }		 
				 }
			 }
		}else {
			System.out.println("The specified grid is not periodic");
		}
	}
	
	public boolean toppleGrid() {
		if(seqorsim) {//Choose topple method
			return sequentialTopple();
		}else {
			return simultaneousTopple();
		}
	}
	
	private boolean sequentialTopple() {
		LinkedHashSet<Cell> toppleList = new LinkedHashSet<Cell>((int)(dim.getWidth()*dim.getHeight()));
		int maxHeight = 0;
		for(int i = 0; i < dim.getWidth(); i++) {//Find the cells which are highest and put them in a list
			 for(int j = 0; j < dim.getHeight(); j++) {
				 if(cells[i][j].getValue() < maxHeight) {
					 //Do nothing
				 } else if(cells[i][j].getValue() == maxHeight) {
					 toppleList.add(cells[i][j]);
				 } else if(cells[i][j].getValue() > maxHeight) {
					 maxHeight = cells[i][j].getValue();
					 toppleList = new LinkedHashSet<Cell>((int)(dim.getWidth()*dim.getHeight()));
					 toppleList.add(cells[i][j]);
				 }
			}
		}
		System.out.println(maxHeight);
		if(maxHeight >= toppleHeight) {
			for(Cell cell:toppleList) {
				cell.topple(discardRemainder);
			}
			updateGrid();
			//System.out.println(toString());
			return true;
		}else {
			return false;
		}
	}
	
	private boolean simultaneousTopple() {
		ExecutorService es = Executors.newCachedThreadPool();
		int maxHeight = 0;
		for(int i = 0; i < dim.getWidth(); i++) {//Find the height
			 for(int j = 0; j < dim.getHeight(); j++) {
				 if(cells[i][j].getValue() > maxHeight) {
					 maxHeight = cells[i][j].getValue();
				 }
			}
		}
		if(maxHeight >= toppleHeight) {//if high enough; topple
			CountDownLatch latch = new CountDownLatch((int)(dim.getWidth()*dim.getHeight())+1);
			for(int i = 0; i < dim.getWidth(); i++) {
				 for(int j = 0; j < dim.getHeight(); j++) {
					 Cell thisCell = cells[i][j];
					 es.execute(new Runnable() {
						@Override
						public void run() {
							thisCell.topple(discardRemainder);
							latch.countDown();
						}  
					});			 
				}
			}
			try {
				  latch.await();
			} catch (InterruptedException E) {
				   // handle
			}
			updateGrid();
			es.shutdown();
			return true;
		}else {
			es.shutdown();
			return false;
		}
		
	}
	
	public void setValueAt(int value, int x, int y) {
		cells[x][y].setValue(value);
	}
	
	private void updateGrid() {
		for(int i = 0; i < dim.getWidth(); i++) {
			for(int j = 0; j < dim.getHeight(); j++) {
				 cells[i][j].update();
			}
		}
	}
	
	public WritableImage getImage(int height, int length) {
		Canvas can = new Canvas(height, length);
		GraphicsContext gc = can.getGraphicsContext2D();
		for(int i = 0; i < dim.getWidth(); i++) {
			 for(int j = 0; j < dim.getHeight(); j++) {
				 drawShape(gc, cells[i][j], i, j);
			}
		}
		WritableImage image = new WritableImage(height, length);
		gc.getCanvas().snapshot(null, image);
		return image;
	}
	
	private void drawShape(GraphicsContext gc, Cell cell, int offX, int offY) {
		gc.setFill(cell.getColor());
		double[] xcord = new double[numberOfNeighbours];
		double[] ycord = new double[numberOfNeighbours];
		if(numberOfNeighbours == 4) {
			for(int i = 0; i < numberOfNeighbours; i++) {
				xcord[i] = cell.getShapeX()[i] + offX*cell.getOffsetLength()[0];
			}
			for(int i = 0; i < numberOfNeighbours; i++) {
				ycord[i] = cell.getShapeY()[i] + offY*cell.getOffsetLength()[1];
			}
			gc.fillPolygon(xcord, ycord, numberOfNeighbours);
		}else if(numberOfNeighbours == 3) {
			for(int i = 0; i < numberOfNeighbours; i++) {
				xcord[i] = cell.getShapeX()[i] + offX*cell.getOffsetLength()[0];
			}
			for(int i = 0; i < numberOfNeighbours; i++) {
				ycord[i] = cell.getShapeY()[i] + offY*cell.getOffsetLength()[1];
			}
			if((offX + offY)%2!=0) {//Flip triangle
				ycord[0] += cell.getSideLength()*Math.sin(Math.PI/3);
				ycord[1] -= cell.getSideLength()*Math.sin(Math.PI/3);
				ycord[2] -= cell.getSideLength()*Math.sin(Math.PI/3);
			}
			gc.fillPolygon(xcord, ycord, numberOfNeighbours);
		}else if(numberOfNeighbours == 6) {
			for(int i = 0; i < numberOfNeighbours; i++) {
				xcord[i] = cell.getShapeX()[i] + offX*cell.getOffsetLength()[0];
			}
			for(int i = 0; i < numberOfNeighbours; i++) {
				if(offX%2 == 0) {//Every other column should also be translated a bit down
					ycord[i] = cell.getShapeY()[i] + offY*cell.getOffsetLength()[1];
				}else {
					ycord[i] = cell.getShapeY()[i] + offY*cell.getOffsetLength()[1] + cell.getSideLength()*Math.sin(Math.PI/3);
				}
			}
			gc.fillPolygon(xcord, ycord, numberOfNeighbours);
		}
		
	}
	
	public String toString() {
		StringBuilder thisGrid = new StringBuilder();
		for(int j = 0; j < dim.getHeight(); j++) {
			StringBuilder thisLine = new StringBuilder();
			for(int i = 0; i < dim.getWidth(); i++) {
				thisLine.append((int)cells[i][j].getValue() + " ");
			}
			thisGrid.append(thisLine.toString() + "\n");
			thisLine = new StringBuilder();
		}
		return(thisGrid.toString());
	}
	
}
