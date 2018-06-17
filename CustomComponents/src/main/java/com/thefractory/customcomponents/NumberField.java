package com.thefractory.customcomponents;

import java.io.IOException;

import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class NumberField extends StackPane {
	
	//Properties 
	private final BooleanProperty intField;
	private final DoubleProperty value;
	private final DoubleProperty min;
	private final DoubleProperty max;
	
	//Variables
	public Logic logic;
	 
	//Structural Elements
	@FXML private TextField field;
	        
	public NumberField(@NamedArg("intField") boolean intField, @NamedArg("value") double value, @NamedArg("min") double min, 
			@NamedArg("max") double max) {
            this.intField = new SimpleBooleanProperty(this, "intField", intField);
            this.min = new SimpleDoubleProperty(this, "min", min);
            this.max = new SimpleDoubleProperty(this, "max", max);
            this.value = new SimpleDoubleProperty(this, "value", value);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NumberField.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.setClassLoader(getClass().getClassLoader());

            try {
                fxmlLoader.load();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
            reconfigureLogic(value, min, max, intField);
	}
	
	public void reconfigureLogic(double defaultValue, double min, double max, boolean intLogic) {
		if(logic != null) {
			field.textProperty().removeListener(logic.fieldListener);
			//TODO DEPRECATED. NOT NEEDED.
//			removeEventHandler(KeyEvent.KEY_PRESSED, logic.keyHandler);
		}		
		
		if(intLogic) {
            logic = new IntLogic((int) Math.round(defaultValue), new int[]{(int) Math.round(min), (int) Math.round(max)});
	    } else {
	        logic = new DoubleLogic(defaultValue, new double[]{min, max});
	    }
		setIntField(intLogic);
		setMin(min);
		setMax(max);
		value.addListener(logic.valueListener);
		setValue(defaultValue);
	}
 
    /*
    The logic for the RestrictedField.
    */
	public abstract class Logic {
            public abstract double getMin();
            public abstract double getMax();
            public abstract boolean setLogicValue(double text);	
            public abstract boolean isValid(String value); 
            public abstract boolean inRange(double value);   

            ChangeListener<String> fieldListener;  
            ChangeListener<Number> valueListener = new ChangeListener<Number>() {
    			@Override
    			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    logic.setLogicValue(newValue.doubleValue());
    			}
            };
            //TODO DEPRECATED. PREVIOUSLY ADDED AS EVENT FILTER IN LOGIC CONSTRUCTOR.
            EventHandler<KeyEvent> keyHandler = new EventHandler<KeyEvent>() {
            	@Override
            	public void handle(KeyEvent event) {
	                if (!isValid(field.getText())) {
	                    event.consume();
	                }
	        	}  
			};
            
            
            protected void resetColor() { 
                    field.setStyle("-fx-text-fill: #000000;"
                                    + "-fx-control-inner-background: #ffffff");	
            }

            protected void alertColor() {
			field.setStyle("-fx-text-fill: #d80000;"
					+ "-fx-control-inner-background: #ffcece");
		}
	}
	
	public class DoubleLogic extends Logic {
		
        private double[] range;
        private boolean positive = false;

        public DoubleLogic(double defaultValue, double[] range) {
            this.range = range.clone();
            if(range[0] >= 0) {
            	positive = true;
            }
            
            fieldListener = new ChangeListener<String>(){
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!isValid(newValue)) {
                        field.setText(oldValue);
                    } else {
                        try {
                            if(inRange(Double.parseDouble(newValue))){
                                value.setValue(Double.parseDouble(newValue));
                                resetColor();
                            } else {
                                alertColor();
                            }
                        } catch(NumberFormatException e) {}
                    }					
				} 
            };
            
            field.textProperty().addListener(fieldListener);
            setLogicValue(defaultValue);
        }

        @Override
        public double getMin() {
                return range[0];
        }

        @Override
        public double getMax() {
                return range[1];
        }

        @Override
        public boolean isValid(String value) {
            if (value.length() == 0 || value.equals(".") || value.equals("e") || value.equals("E") 
            		|| ((value.equals("-") || value.equals("-.")) && !positive)) {
                return true;
            }
            if (value.length() - value.replace(".", "").length() > 1 || 
		            value.length() - value.replace(" ", "").length() > 0 ||
		            value.length() - value.replace("d", "").length() > 0 ||
		            value.length() - value.replace("D", "").length() > 0 ||
		            value.length() - value.replace("f", "").length() > 0 ||
		            value.length() - value.replace("F", "").length() > 0) {
                return false;
            }

            //Looking for an expression on scientific form. 
            //Note that "E-2" for instance will be mapped to ["", "-2"] if we split on "E".
            //This is the cause of some quirks you will find in this code block.
            if(value.length() - value.replaceAll("e|E", "").length() == 1) {
                String[] scientificNotation = value.split("e|E");
                if(scientificNotation.length == 2 && !scientificNotation[0].equals("")) {
                	boolean begOK = false;
                	boolean endOK = false;
                	
                	//Checking if the expression before the E is valid.
                	try {
                		Double.parseDouble(scientificNotation[0]);
                		begOK = true;
                	} catch(Exception e) {
                		begOK = scientificNotation[0].equals(".")
                				|| ((scientificNotation[0].equals("-") || scientificNotation[0].equals("-.")) && !positive);
                	} 

                	//Checking if the expression after the E is valid.
                	try {
                		Integer.parseInt(scientificNotation[1]);
                		endOK = true;
                	} catch(Exception e) {
                		endOK = scientificNotation[1].equals("-");
                	}
                	
                	if(begOK && endOK) {
                		return true;
                	}
                //If the E is in the beginning of the expression.
                } else if(value.charAt(0) == 'e' || value.charAt(0) == 'E') {
                	if(scientificNotation[1].equals("-")) {
                		return true;
                	} else {
                    	try {
                    		Integer.parseInt(scientificNotation[1]);
                    		return true;
                    	} catch(Exception e) {return false;}	
                	}
                //Else the E is in the end of the expression.
                } else {
                	if((scientificNotation[0].equals("-") || scientificNotation[0].equals("-.")) && !positive) {
                		return true;
                	} else {
                    	try {
    	            		Double.parseDouble(scientificNotation[0]);
    	            		return true;
    	            	} catch(Exception e) {return false;}
                	}
                }
            }

            try {
                @SuppressWarnings("unused")
                double val = Double.parseDouble(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

            @Override
	    public boolean inRange(double value) {
	    	if(range != null) {
                    if(value >= range[0] && value <= range[1]) {
                            return true;
                    }
                    return false;
	    	} else if(positive && value < 0) {
                    return false;
	    	}
	    	return true;
	    }
		
	    @Override
        public boolean setLogicValue(double value) {
            if(inRange(value)) {
            	try {
                	if(Double.parseDouble(field.getText()) != value) {
                        field.setText(Double.toString(value));
                        resetColor();
                        return true;
                	}
            	} catch(Exception e) {
                    field.setText(Double.toString(value));
                    resetColor();
                    return true;
                }
            }
            return false;
        }
	}
        
	public class IntLogic extends Logic {
		
        private int[] range;
        private boolean positive = false;

        public IntLogic(int defaultValue, int[] range) {
            this.range = range.clone();
            if(range[0] >= 0) {
                    positive = true;
            }

            field.textProperty().addListener((ObservableValue<? extends String> observableValue, String oldValue, String newValue) -> {
                if(!isValid(newValue)) {
                    field.setText(oldValue);
                } else {
                    try {
                        if(inRange(Integer.parseInt(newValue))){
                            value.setValue(Integer.parseInt(newValue));
                            resetColor();
                        } else {
                            alertColor();
                        }
                    } catch(NumberFormatException e) {}
                }
            });

            setLogicValue(defaultValue);
        }

        @Override
        public double getMin() {
                return range[0];
        }

        @Override
        public double getMax() {
                return range[1];
        }

        @Override
        public boolean isValid(String value) {
            if (value.length() == 0 || (value.equals("-") && !positive)) {
                return true;
            }

            try {
                @SuppressWarnings("unused")
                    Integer val = Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
	    }

            @Override
	    public boolean inRange(double value) {
	    	if(range != null) {
	        	if(value >= range[0] && value <= range[1]) {
	        		return true;
	        	}
	        	return false;
	    	} else if(positive && value <= 0) {
	    		return false;
	    	}
	    	return true;
	    }	    
		
	    @Override
        public boolean setLogicValue(double value) {
	    	int val = (int) Math.round(value);
		    if(inRange(val)) {
	        	try {
	            	if(Integer.parseInt(field.getText()) != val) {
	                    field.setText(Integer.toString(val));
	                    resetColor();
	                    return true;
	            	}
	        	} catch(Exception e) {
	                field.setText(Integer.toString(val));
	                resetColor();
	                return true;
	            }
	        }
	        return false;
	    }
	}
	        
    /*
    Setting up all properties.
    */
	public double getValue() {
		return value.getValue();
	}
	public void setValue(double value) {
		if(logic.inRange(value)){
	        if(logic instanceof IntLogic){
	            this.value.setValue((int) Math.round(value));
	        } else {
	            this.value.setValue(value);
	        }
	    } else {
	    	if(value > logic.getMax()) {
	            if(logic instanceof IntLogic){
	                this.value.setValue(logic.getMax());
	            } else {
	                this.value.setValue(logic.getMax());
	            }
	    	} else {
	            if(logic instanceof IntLogic){
	                this.value.setValue(logic.getMin());
	            } else {
	                this.value.setValue(logic.getMin());
	            }
	    	}
	    }
	}
	public final DoubleProperty valueProperty() {
		return value;
	}
	public final boolean getIntField() {
		return intField.getValue();
	}
	public final void setIntField(boolean value) {
		intField.set(value);
    }
	public final BooleanProperty intFieldProperty() {
		return intField;
	}
	public final double getMin() {
		return min.getValue();
	}
	public final void setMin(double value) {
		min.set(value);
    }
	public final DoubleProperty minProperty() {
		return min;
	}
	public final double getMax() {
		return max.getValue();
	}
	public final void setMax(double value) {
		max.set(value);
    }
	public final DoubleProperty maxProperty() {
		return max;
	}
	public final String getGhostText() {
		return field.promptTextProperty().getValue();
	}
	public final void setGhostText(String value) {
		field.promptTextProperty().set(value);
    }
	public final StringProperty ghostTextProperty() {
		return field.promptTextProperty();
	}
}