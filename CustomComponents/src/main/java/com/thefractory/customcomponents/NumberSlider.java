package com.thefractory.customcomponents;

import java.io.IOException;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;

public class NumberSlider extends StackPane {

    //Properties
    private final BooleanProperty logarithmic;
    private final BooleanProperty ticks;

    //Variables
    @SuppressWarnings("unused")
    private boolean lock = false;

    //Structural Elements
    @FXML private Label label;
    @FXML private Slider slider;
    @FXML public NumberField field;
    @FXML private InfoIcon info;

    public NumberSlider(@NamedArg(value="logarithmic", defaultValue="false") boolean logarithmic, 
    		@NamedArg(value="ticks", defaultValue="false") boolean ticks, 
    		@NamedArg(value="intSlider", defaultValue="false") boolean intSlider, 
    		@NamedArg(value="value", defaultValue="50") double value, 
    		@NamedArg(value="min", defaultValue="0") double min, 
    		@NamedArg(value="max", defaultValue="100") double max) {
        this.logarithmic = new SimpleBooleanProperty(this, "logarithmic", logarithmic);
        this.ticks = new SimpleBooleanProperty(this, "ticks", ticks);
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NumberSlider.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        field.reconfigureLogic(value, min, max, intSlider);

        if(field.getIntField()) {
        	if(logarithmic) {
                slider.setValue(Math.log10((int) value));
                slider.setMin(Math.log10((int) min));
                slider.setMax(Math.log10((int) max));
        		        		
                slider.valueProperty().addListener(new ChangeListener<Number>(){
        			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        field.setValue((double) Math.round(Math.pow(10, newValue.doubleValue())));
                        slider.setValue(Math.log10(Math.round(Math.pow(10, newValue.doubleValue()))));
        			}        			
                }); 
                
                field.valueProperty().addListener(new ChangeListener<Number>(){
        			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        slider.setValue(Math.log10(Math.round(newValue.doubleValue())));
        			} 
                });        		
        	} else {
                slider.setValue((int) value);
                slider.setMin((int) min);
                slider.setMax((int) max);
        		
                slider.setShowTickMarks(true);
                slider.setMajorTickUnit((field.getMax() - field.getMin())/10.0);
                slider.setShowTickMarks(ticks);
                slider.valueProperty().addListener(new ChangeListener<Number>(){
        			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        field.setValue((double) Math.round(newValue.doubleValue()));
                        slider.setValue(Math.round(newValue.doubleValue()));
        			}        			
                }); 
                
                field.valueProperty().addListener(new ChangeListener<Number>(){
        			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        slider.setValue(Math.round(newValue.doubleValue()));
        			} 
                });
        	}
        } else {
        	if(logarithmic) {
                slider.setValue(Math.log10(value));
                slider.setMin(Math.log10(min));
                slider.setMax(Math.log10(max));
        		
                slider.valueProperty().addListener(new ChangeListener<Number>(){
        			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        				if(newValue.doubleValue() == slider.getMin()) {
        					field.setValue(field.getMin());
        				} else if(newValue.doubleValue() == slider.getMax()) {
        					field.setValue(field.getMax());
        				} else {
                            field.setValue(Math.pow(10, newValue.doubleValue()));	
        				}
        			}        			
                }); 
                
                field.valueProperty().addListener(new ChangeListener<Number>(){
        			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        				if(newValue.doubleValue() == Math.pow(10,  Math.log10(newValue.doubleValue()))) {
                            slider.setValue(Math.log10(newValue.doubleValue()));	
        				}
        			} 
                });
        	} else {
                slider.setValue(value);
                slider.setMin(min);
                slider.setMax(max);
        		
                slider.setMajorTickUnit((field.getMax() - field.getMin())/10.0);
                slider.setShowTickMarks(ticks);
                
                slider.valueProperty().addListener(new ChangeListener<Number>(){
        			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        field.setValue((double) newValue.doubleValue());
        			}        			
                }); 
                
                field.valueProperty().addListener(new ChangeListener<Number>(){
        			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        slider.setValue(newValue.doubleValue());
        			} 
                });
        	}
        }
    }    
        
    /*
    Setting up all properties.
    */
    public final double getValue() {
		return slider.getValue();
	}
	public void setValue(double value) {
        slider.setValue(value);
	}
	public final DoubleProperty valueProperty() {
		return slider.valueProperty();
	}
	public final boolean getIntSlider() {
		return field.getIntField();
	}
	public final void setIntSlider(boolean value) {
		field.setIntField(value);
    }
	public final BooleanProperty intSliderProperty() {
		return field.intFieldProperty();
	}
	public final double getMin() {
		return slider.getMin();
	}
	public final void setMin(double value) {
		slider.setMin(value);
    }
	public final DoubleProperty minProperty() {
		return slider.minProperty();
	}
	public final double getMax() {
		return slider.getMax();
	}
	public final void setMax(double value) {
		slider.setMax(value);
    }
	public final DoubleProperty maxProperty() {
		return slider.maxProperty();
	}
	public final String getTitle() {
        return label.getText();
    }
	public final void setTitle(String value) {
        label.setText(value);
    }
    public final StringProperty titleProperty() {
        return label.textProperty();
    }
    public final boolean getLogarithmic() {
        return logarithmic.getValue();
    }
    public final void setLogarithmic(boolean value) {
        logarithmic.set(value);
    }
    public final BooleanProperty logarithmicProperty() {
        return logarithmic;
    }
    public final boolean getTicks() {
        return ticks.getValue();
    }
    public final void setTicks(boolean value) {
        ticks.setValue(value);
    }
    public final BooleanProperty ticksProperty() {
        return ticks;
    }
    public final String getTip() {
            return info.getTipText();
    }
    public final void setTip(String value) {
            Number min = field.getMin();
            Number max = field.getMax();
            String extra;
            if(field.getIntField()) {
                extra = "\n[" + min.intValue() + ", " + max.intValue() + "]";
            } else {
                extra = "\n[" + min + ", " + max + "]";	
            }

            info.setTipText(value + extra);
    }
    public final StringProperty tipProperty() {
            return info.tipTextProperty();
    }
}