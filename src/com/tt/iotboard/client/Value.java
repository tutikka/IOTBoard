package com.tt.iotboard.client;

/**
 * @author Tuomas Tikka
 */
public class Value {

    // the name of the measured item
    private String name;

    // the measured value
    private Object value;

    /**
     * Constructor
     *
     * @param name The name of the measured item
     * @param value The measured value
     */
    public Value(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /*** getters and setters ***/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
