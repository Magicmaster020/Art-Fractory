package com.thefractory.customcomponents;
//https://stackoverflow.com/questions/33961606/javafx-how-to-drag-object-along-given-svg-path-like-a-slider


import java.util.ArrayList;
import java.util.List;
import javafx.animation.PathTransition;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import java.io.IOException;

//import org.apache.batik.parser.PathParser;


public class DoubleCircularSlider extends AnchorPane {

    private final DoubleProperty sliderMin;
    private final DoubleProperty sliderMax;
    private final DoubleProperty sliderMin2;
    private final DoubleProperty sliderMax2;
    
    @FXML Path path;
    @FXML Path path2;
    @FXML private Circle _circle;
    @FXML private Circle _circle2;
    
    private final static double ANIMATION_DURATION = 500.0d;

    private double _initX;
    private double _initY;
    private Point2D _dragAnchor;
    private double _initX2;
    private double _initY2;
    private Point2D _dragAnchor2;

    private PathTransition _pathTransition;
    private PathTransition _pathTransition2;
    
    private List<Point2D> _pathPointList;
    private List<Point2D> _pathPointList2;

    private int _actIndex;
    private int _actIndex2;

    private double _sliderIndex = 0;
    private double _sliderIndex2 = 0;
    
    public DoubleCircularSlider(@NamedArg(value="sliderMin", defaultValue="360") double sliderMin,
    		@NamedArg(value="sliderMax", defaultValue="0") double sliderMax, 
    		@NamedArg(value="sliderMin2", defaultValue="2") double sliderMin2,
    		@NamedArg(value="sliderMax2", defaultValue="0") double sliderMax2) {
    	
    	this.sliderMin = new SimpleDoubleProperty(this, "sliderMin", sliderMin);
    	this.sliderMax = new SimpleDoubleProperty(this, "sliderMax", sliderMax);
    	this.sliderMin2 = new SimpleDoubleProperty(this, "sliderMin2", sliderMin2);
    	this.sliderMax2 = new SimpleDoubleProperty(this, "sliderMax2", sliderMax2);
    	
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DoubleCircularSlider.fxml"));
		fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        

	    // Parse the SVG Path with Apache Batik and create a Path
	    //PathParser parser = new PathParser();
	    //JavaFXPathElementHandler handler = new JavaFXPathElementHandler("track");
	    //parser.setPathHandler(handler);
	
	    // SVG Path for circle
	    //parser.parse("M60,60 M60,10 A50,50 1 0,1 60,110 A50,50 1 0,1 60,10 z");
	
	    //path = handler.getPath();
	
	    // Moving image
	    //_circle = new Circle(8);
	    //_circle.setFill(Color.GRAY);
	
	    // Path Transition
	    _pathTransition = new PathTransition();
	    _pathTransition.setDuration(Duration.seconds(ANIMATION_DURATION));
	    _pathTransition.setPath(path);
	    _pathTransition.setNode(_circle);
	    _pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	    _pathTransition.setCycleCount(1);
	    _pathTransition.playFromStart();
	    _pathTransition.pause();
	    _pathTransition.jumpTo("end");

	    _pathTransition2 = new PathTransition();
	    _pathTransition2.setDuration(Duration.seconds(ANIMATION_DURATION));
	    _pathTransition2.setPath(path2);
	    _pathTransition2.setNode(_circle2);
	    _pathTransition2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	    _pathTransition2.setCycleCount(1);
	    _pathTransition2.playFromStart();
	    _pathTransition2.pause();
	    _pathTransition2.jumpTo("end");
	    
	    // Save the circle positions on the path
	    _pathPointList = new ArrayList<>();
	    savePositions(_pathPointList, _pathTransition, _circle);
	    _pathPointList2 = new ArrayList<>();
	    savePositions(_pathPointList2, _pathTransition2, _circle2);
	
	
	    // Mouse presssed handler
	    _circle.setOnMousePressed(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent me) {
	             // Store initial position
	            _initX = _circle.getTranslateX();
	            _initY = _circle.getTranslateY();
	            _dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());                
	        }
	    });
	
	    _circle2.setOnMousePressed(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent me) {
	             // Store initial position
	            _initX2 = _circle2.getTranslateX();
	            _initY2 = _circle2.getTranslateY();
	            _dragAnchor2 = new Point2D(me.getSceneX(), me.getSceneY());                
	        }
	    });
	
	    // Mouse dragged handler
	    _circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent me) {
	            double dragX = me.getSceneX() - _dragAnchor.getX();
	            double dragY = me.getSceneY() - _dragAnchor.getY();
	
	            // Calculate new position of the circle
	            double newXPosition = _initX + dragX;
	            double newYPosition = _initY + dragY;
	
	            // Get the nearest index (= second) of the animation
	            _actIndex = getAnimationIndex(newXPosition, newYPosition, _pathPointList);
	
	            // Show animation at the given second
	            _pathTransition.jumpTo(Duration.seconds(_actIndex));                                
	
	            // Get slider index
	            _sliderIndex = remap(_actIndex, ANIMATION_DURATION, 0, getSliderMin(), getSliderMax());
	            System.out.println(_sliderIndex);
	        }
	    });
	    
	    _circle2.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent me) {
	            double dragX2 = me.getSceneX() - _dragAnchor2.getX();
	            double dragY2 = me.getSceneY() - _dragAnchor2.getY();
	
	            // Calculate new position of the circle
	            double newXPosition2 = _initX2 + dragX2;
	            double newYPosition2 = _initY2 + dragY2;
	
	            // Get the nearest index (= second) of the animation
	            _actIndex2 = getAnimationIndex(newXPosition2, newYPosition2, _pathPointList2);
	
	            // Show animation at the given second
	            _pathTransition2.jumpTo(Duration.seconds(_actIndex2));                                
	
	            // Get slider index
	            _sliderIndex2 = remap(_actIndex2, ANIMATION_DURATION, 0, getSliderMin2(), getSliderMax2());
	            System.out.println(_sliderIndex2);
	        }
	    });
    }

    //Getters and setters
    public final double getSliderMin(){
		return sliderMin.getValue();
	}
	public final void setSliderMin(int value){
		sliderMin.setValue(value);
	}
	public final DoubleProperty sliderMinProperty(){
		return sliderMin;
	}
	public final double getSliderMax(){
		return sliderMax.getValue();
	}
	public final void setSliderMax(int value){
		sliderMax.setValue(value);
	}
	public final DoubleProperty sliderMaxProperty(){
		return sliderMax;
	}
	public final double getSliderMin2(){
		return sliderMin2.getValue();
	}
	public final void setSliderMin2(int value){
		sliderMin2.setValue(value);
	}
	public final DoubleProperty sliderMinProperty2(){
		return sliderMin2;
	}
	public final double getSliderMax2(){
		return sliderMax2.getValue();
	}
	public final void setSliderMax2(int value){
		sliderMax2.setValue(value);
	}
	public final DoubleProperty sliderMaxProperty2(){
		return sliderMax2;
	}
    
    /**
     * Save the position of the circle for every second of the animation in
     * a list.
     */
    private void savePositions(List<Point2D> _pathPointList3, PathTransition _pathTransition3, Circle _circle3) {

        if (_pathPointList3 == null)
            return;

        for (int i=0; i<=(int)ANIMATION_DURATION; i++) {
            _pathTransition3.jumpTo(Duration.seconds(i));
            
            _pathPointList3.add(new Point2D(_circle3.getTranslateX(), _circle3.getTranslateY()));
        }

    }

    /**
     * Returns the index 
     * @param mousePosX
     * @param mousePosY
     * @return 
     */
    private int getAnimationIndex(double mousePosX, double mousePosY, List<Point2D> _pathPointList3) {

        int nearestIndex = 0;

        int i = 0;
        double dx;
        double dy;
        double old_dist = Double.MAX_VALUE;
        double act_dist;

        for (Point2D pathPos : _pathPointList3) {

            // Get distance between mouse position and saved position on path
            // with pythagoras
            dx = mousePosX - pathPos.getX();
            dy = mousePosY - pathPos.getY();
            act_dist = Math.sqrt(dx * dx + dy * dy);

            if (act_dist < old_dist) {
                old_dist = act_dist;
                nearestIndex = i;
            }

            i++;            
        }

        return nearestIndex;
    }

    /**
     * Remaps the given value from one to anoter range
     * 
     * @param value
     * @param from1
     * @param to1
     * @param from2
     * @param to2
     * @return 
     */
    private double remap (int value, double from1, double to1, double from2, double to2) {
        double tmp = (value - from1) / (to1 - from1) * (to2 - from2) + from2;        
        return tmp;
    }
}
