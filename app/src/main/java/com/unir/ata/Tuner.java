package com.unir.ata;


import android.util.Log;

import androidx.annotation.NonNull;

public class Tuner {

    //Atributos
    private static Tuner tuner;
    protected TunerActivity activity;
    protected Thread tunerProcess;

    //Constantes
    public static final int INSTRUMENT_CLARINET = 1;
    public static final int INSTRUMENT_BOMBARDINO = 2;
    public static final int INSTRUMENT_SAXOFON = 3;


    private Tuner (TunerActivity activity) {
        this.activity = activity;
    }

    public static Tuner getInstance(@NonNull TunerActivity activity) {
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
        if (this.tunerProcess != null && !this.tunerProcess.isInterrupted()) {
            this.tunerProcess.interrupt();
        }

    }



    public void showResults(DetectedNote note, boolean isError, String errMsg) {

        activity.showTunerResults(note, isError, errMsg);


    }
}
