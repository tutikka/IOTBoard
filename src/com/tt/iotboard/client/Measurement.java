package com.tt.iotboard.client;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tuomas Tikka
 */
public class Measurement {

    // a label identifying the sensor in question
    private String sensor;

    // measurement time
    private long timestamp = System.currentTimeMillis();

    // the number of measurements to keep (x-axis)
    private int history = 100;

    // name/value pairs of measured items
    private List<Value> values = new ArrayList<>();

    /**
     * Constructor
     *
     * @param sensor The sensor label
     */
    public Measurement(String sensor) {
        this.sensor = sensor;
    }

    /**
     * Constructor
     *
     * @param sensor The sensor label
     * @param timestamp The measurement time
     */
    public Measurement(String sensor, long timestamp) {
        this.sensor = sensor;
        this.timestamp = timestamp;
    }

    /**
     * Constructor
     *
     * @param sensor The sensor label
     * @param timestamp The measurement time
     * @param history The number of measurements to keep
     */
    public Measurement(String sensor, long timestamp, int history) {
        this.sensor = sensor;
        this.timestamp = timestamp;
        this.history = history;
    }

    /*** getters and setters ***/

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

}
