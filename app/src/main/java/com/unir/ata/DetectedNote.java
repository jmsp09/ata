package com.unir.ata;

public class DetectedNote {

    private String name;
    private double frequency;
    private double deviation;
    private double instrumentDeviation;



    private double decibels;

    public DetectedNote() {
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getDeviation() {
        return deviation;
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDecibels() {
        return decibels;
    }

    public void setDecibels(double decibels) {
        this.decibels = decibels;
    }

    public double getDeviationInstrument() {
        return instrumentDeviation;
    }
    public void setDeviationInstrument(double deviation) {
        this.instrumentDeviation = deviation;
    }

}
