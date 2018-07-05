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
    private final DoubleProperty sliderIndex;
    
    @FXML private SVGPath path;
    @FXML private Circle circle;
    
    private boolean lock = false;
    
    private final static double ANIMATION_DURATION = 500.0d;

    private double initX;
    private double initY;
    
    private Point2D dragAnchor;

    private PathTransition pathTransition;
    
    private List<Point2D> pathPointList;

    private int actIndex;
	
    
    public CircularSlider(@NamedArg(value="sliderMin", defaultValue="0") double sliderMin,
    		@NamedArg(value="sliderMax", defaultValue="360") double sliderMax,
    		@NamedArg(value="svgPath", defaultValue="M50,0 A50,50 0 0 0 50,100 A50,50 0 0 0 50,0 z") String svgPath,
    		@NamedArg(value="countRevs", defaultValue="true") boolean countRevs,
    		@NamedArg(value="value", defaultValue="0") double value) {
    	
    	this.sliderMin = new SimpleDoubleProperty(this, "sliderMin", sliderMin);
    	this.sliderMax = new SimpleDoubleProperty(this, "sliderMax", sliderMax);
    	this.svgPath = new SimpleStringProperty(this, "path", svgPath);
    	this.countRevs = new SimpleBooleanProperty(this, "countRevs", countRevs);
    	this.value = new SimpleDoubleProperty(this, "value", value);
    	
        revs = new SimpleIntegerProperty((int)((value-value%sliderMax)/sliderMax));
    	sliderIndex = new SimpleDoubleProperty(this, "sliderIndex", value%sliderMax);
        
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
        

      //Listeners binding value and index  
      
      //TODO Think of better solution.
        this.value.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(!lock) {
                    double sliderMax = getSliderMax();

                    int newRevs = (int) ((newValue.doubleValue()-newValue.doubleValue()%sliderMax)/sliderMax);
                    double newIndex = newValue.doubleValue()%sliderMax;

                    revs.setValue(newRevs);
                    sliderIndex.setValue(newIndex);
                }

            }
        });
        sliderIndex.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                lock = true;
                CircularSlider.this.value.setValue(newValue.doubleValue() + revs.getValue() * getSliderMax());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println(revs.getValue());
                lock = false;
            }
        });
        
        
	    pathTransition = new PathTransition();
	    pathTransition.setDuration(Duration.seconds(ANIMATION_DURATION));
	    pathTransition.setPath(path);
	    pathTransition.setNode(circle);
	    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	    pathTransition.setCycleCount(1);
	    pathTransition.playFromStart();
	    pathTransition.pause();
	    pathTransition.jumpTo("end");

	    // Save the circle positions on the path
	    pathPointList = new ArrayList<>();
	    savePositions();
	
	
	    // Mouse presssed handler
	    circle.setOnMousePressed(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent me) {
	             // Store initial position
	            initX = circle.getTranslateX();
	            initY = circle.getTranslateY();
	            dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());                
	        }
	    });
	
	
	    // Mouse dragged handler
	    circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        public void handle(MouseEvent me) {
	            double dragX = me.getSceneX() - dragAnchor.getX();
	            double dragY = me.getSceneY() - dragAnchor.getY();
	
	            // Calculate new position of the circle
	            double newXPosition = initX + dragX;
	            double newYPosition = initY + dragY;
	
	            // Get the nearest index (= second) of the animation
	            actIndex = getAnimationIndex(newXPosition, newYPosition);
	
	            // Show animation at the given second
	            pathTransition.jumpTo(Duration.seconds(actIndex));                                
	
	            // Get slider index
	            sliderIndex.setValue(remap(actIndex, ANIMATION_DURATION, 0, getSliderMin(), getSliderMax()));
	            System.out.println(getValue());
	        }
	    });
    }

    //Getters and setters
    public final double getSliderMin(){
		return sliderMin.getValue();
	}
	public final void setSliderMin(double value){
		sliderMin.setValue(value);
	}
	public final DoubleProperty sliderMinProperty(){
		return sliderMin;
	}
	public final double getSliderMax(){
		return sliderMax.getValue();
	}
	public final void setSliderMax(double value){
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
        	sliderIndex.setValue(sliderIndex.getValue() - revs.getValue()*sliderMax.getValue());
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

        if (pathPointList == null)
            return;

        for (int i=0; i<=(int)ANIMATION_DURATION; i++) {
            pathTransition.jumpTo(Duration.seconds(i));

            pathPointList.add(new Point2D(circle.getTranslateX(), circle.getTranslateY()));
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
        double olddist = Double.MAX_VALUE;
        double actdist;

        for (Point2D pathPos : pathPointList) {

            // Get distance between mouse position and saved position on path
            // with pythagoras
            dx = mousePosX - pathPos.getX();
            dy = mousePosY - pathPos.getY();
            actdist = Math.sqrt(dx * dx + dy * dy);

            if (actdist < olddist) {
                olddist = actdist;
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
        	if((tmp-sliderIndex.getValue())*(tmp-sliderIndex.getValue()) > (sliderMax.getValue()-sliderMin.getValue())*(sliderMax.getValue()-sliderMin.getValue())/4) { //new revolution
        		if(tmp-sliderIndex.getValue() > (sliderMax.getValue()-sliderMin.getValue())/2) {
	        		revs.setValue(revs.getValue() - 1);
	        	}else {
	        		revs.setValue(revs.getValue() + 1);
	        	}
	        }
	    }
        return tmp;
    }
}
