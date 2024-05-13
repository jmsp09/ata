package com.unir.ata;


import android.util.Log;

import androidx.annotation.NonNull;

public class Tuner {

    //Atributos
    private static Tuner tuner;
    protected TunerActivity activity;
    protected Thread tunerProcess;
    private int instrument;

    //Constantes
    public static final int INSTRUMENT_CLARINET = 0;
    public static final int INSTRUMENT_BOMBARDINO = 1;
    public static final int INSTRUMENT_SAXOFON = 2;


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
            TunerProcess tp = TunerProcess.getInstance(this);
            tp.setInstrument(instrument);
            this.tunerProcess = new Thread(tp);
            this.tunerProcess.start();
        }
    }

    public void interrupt() {
        if (this.tunerProcess != null
                && !this.tunerProcess.isInterrupted()) {
            this.tunerProcess.interrupt();
        }

    }



    public void showResults(DetectedNote note, boolean isError, String errMsg) {

        activity.showTunerResults(note, isError, errMsg);


    }

    public void setInstrument(int instrument) {
        this.instrument = instrument;
    }
}
