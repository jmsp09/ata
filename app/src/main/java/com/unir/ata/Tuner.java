package com.unir.ata;


import android.util.Log;

public class Tuner {

    //Atributos
    private static Tuner tuner;
    protected TunerActivity activity;
    protected Thread tunerProcess;

    private Tuner (TunerActivity activity) {
        this.activity = activity;
        this.tunerProcess = new Thread(TunerProcess.getInstance(this));
    }

    public static Tuner getInstance(TunerActivity activity) {
        if (tuner == null) {
            tuner = new Tuner(activity);
        }
        return tuner;
    }

    public void start() {
        Log.d("!!!********Tuner start ", "!!!********");
        this.tunerProcess.start();
    }

    public void interrupt() {
        this.tunerProcess.interrupt();
    }


    //TODO
    public void showResults(DetectedNote note, boolean isError, String errMsg) {


        activity.showTunerResults(note, isError, errMsg);

        //TODO
        if (isError) {

        } else{
        }
    }
}
