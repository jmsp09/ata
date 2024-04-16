package com.unir.ata;


import android.util.Log;

public class Tuner {

    //Atributos
    private static Tuner tuner;
    protected TunerActivity activity;
    protected Thread tunerProcess;

    private Tuner (TunerActivity activity) {
        this.activity = activity;
    }

    public static Tuner getInstance(TunerActivity activity) {
        if (tuner == null) {
            tuner = new Tuner(activity);
        }
        return tuner;
    }

    public void start() {
        Log.d("!!!********Tuner start ", "!!!********");
        if (this.tunerProcess == null || !this.tunerProcess.isAlive()) {
            this.tunerProcess = new Thread(TunerProcess.getInstance(this));
            this.tunerProcess.start();
        }
    }

    public void interrupt() {
        if (!this.tunerProcess.isInterrupted() || this.tunerProcess.isInterrupted()) {
            this.tunerProcess.interrupt();
        }

    }



    public void showResults(DetectedNote note, boolean isError, String errMsg) {

        activity.showTunerResults(note, isError, errMsg);


    }
}
