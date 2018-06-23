package com.thefractory.fractalart.utils;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public final class JavaFXUtils {

	public static ArrayList<Node> getNodesFromType(Pane parent, @SuppressWarnings("rawtypes") Class nodeType){
		ArrayList<Node> matches = new ArrayList<Node>();
		
		for(Node child : parent.getChildren()) {
			if(child instanceof Pane) {
				matches.addAll(JavaFXUtils.getNodesFromType((Pane) child, nodeType));
			}
			if(child.getClass().equals(nodeType)) {
				matches.add(child);
			}
		}
		
		return matches;
	}
	
}
