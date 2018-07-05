package com.thefractory.customcomponents;
//https://stackoverflow.com/questions/33961606/javafx-how-to-drag-object-along-given-svg-path-like-a-slider


import java.util.ArrayList;
import java.util.List;
import javafx.animation.PathTransition;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import java.io.IOException;

//import org.apache.batik.parser.PathParser;


public class CircularSlider extends AnchorPane {

    private final DoubleProperty sliderMin;
    private final DoubleProperty sliderMax;
    private final BooleanProperty countRevs;
    private final StringProperty svgPath;
    private final DoubleProperty value;
    
    private final IntegerProperty revs;
    private final DoubleProperty _sliderIndex;
    
    @FXML private SVGPath path;
    @FXML private Circle _circle;
    
    private final static double ANIMATION_DURATION = 500.0d;

    private double _initX;
    private double _initY;
    
    private Point2D _dragAnchor;

    private PathTransition _pathTransition;
    
    private List<Point2D> _pathPointList;

    private int _actIndex;
	
    
    public CircularSlider(@NamedArg(value="sliderMin", defaultValue="0") double sliderMin,
    		@NamedArg(value="sliderMax", defaultValue="360") double sliderMax,
    		@NamedArg(value="path", defaultValue="M50,50 M50,0 A50,50 1 0,0 50,100 A50,50 1 0,0 50,0 z") String svgPath,
    		@NamedArg(value="countRevs", defaultValue="true") boolean countRevs,
    		@NamedArg(value="value", defaultValue="0") double value2) {
    	
    	this.sliderMin = new SimpleDoubleProperty(this, "sliderMin", sliderMin);
    	this.sliderMax = new SimpleDoubleProperty(this, "sliderMax", sliderMax);
    	this.svgPath = new SimpleStringProperty(this, "path", svgPath);
    	this.countRevs = new SimpleBooleanProperty(this, "countRevs", countRevs);
    	this.value = new SimpleDoubleProperty(this, "value", value2);
    	
        revs = new SimpleIntegerProperty((int)((value2-value2%sliderMax)/sliderMax));
    	_sliderIndex = new SimpleDoubleProperty(this, "_sliderIndex", value2%sliderMax);
        
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CircularSlider.fxml"));
		fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        path.setContent(svgPath);
        this.value.addListener(new ChangeListener<Number>() {
        	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        		revs.setValue(((value.getValue()-value.getValue()%getSliderMax())/getSliderMax()));
        		_sliderIndex.setValue(value.getValue()%getSliderMax());
        	}
        });
        this.value.bind(this.revs.multiply(getSliderMax()).add(this._sliderIndex.getValue()));

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

	    // Save the circle positions on the path
	    _pathPointList = new ArrayList<>();
	    savePositions();
	
	
	    // Mouse presssed handler
	    _circle.setOnMousePressed(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent me) {
	             // Store initial position
	            _initX = _circle.getTranslateX();
	            _initY = _circle.getTranslateY();
	            _dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());                
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
	            _actIndex = getAnimationIndex(newXPosition, newYPosition);
	
	            // Show animation at the given second
	            _pathTransition.jumpTo(Duration.seconds(_actIndex));                                
	
	            // Get slider index
	            _sliderIndex.setValue(remap(_actIndex, ANIMATION_DURATION, 0, getSliderMin(), getSliderMax()));
	            System.out.println(getValue());
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
	public final String getSvgPath() {
        return svgPath.getValue();
    }
    public final void setSvgPath(String value) {
        svgPath.setValue(value);
    }
    public final StringProperty svgPathProperty() {
        return svgPath;
    }
	public final boolean getCountRevs() {
        return countRevs.getValue();
    }
    public final void setCountRevs(boolean value) {
        if(!value) {
        	_sliderIndex.setValue(_sliderIndex.getValue() - revs.getValue()*sliderMax.getValue());
        	revs.setValue(0);
        }
    	countRevs.setValue(value);
    }
    public final BooleanProperty countRevsProperty() {
        return countRevs;
    }
    public final double getValue(){
		return value.getValue();
	}
	public final void setValue(double value){
		this.value.setValue(value);
	}
	public final DoubleProperty valueProperty(){
		return value;
	}
    /**
     * Save the position of the circle for every second of the animation in
     * a list.
     */
    private void savePositions() {

        if (_pathPointList == null)
            return;

        for (int i=0; i<=(int)ANIMATION_DURATION; i++) {
            _pathTransition.jumpTo(Duration.seconds(i));

            _pathPointList.add(new Point2D(_circle.getTranslateX(), _circle.getTranslateY()));
        }

    }

    /**
     * Returns the index 
     * @param mousePosX
     * @param mousePosY
     * @return 
     */
    private int getAnimationIndex(double mousePosX, double mousePosY) {

        int nearestIndex = 0;

        int i = 0;
        double dx;
        double dy;
        double old_dist = Double.MAX_VALUE;
        double act_dist;

        for (Point2D pathPos : _pathPointList) {

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
        //Keep track of revolutions
	    if(countRevs.getValue()) { 
        	if((tmp-_sliderIndex.getValue())*(tmp-_sliderIndex.getValue()) > (sliderMax.getValue()-sliderMin.getValue())*(sliderMax.getValue()-sliderMin.getValue())/4) { //new revolution
        		if(tmp-_sliderIndex.getValue() > (sliderMax.getValue()-sliderMin.getValue())/2) {
	        		revs.setValue(revs.getValue() - 1);
	        	}else {
	        		revs.setValue(revs.getValue() + 1);
	        	}
	        }
	    }
        return tmp;
    }
}
