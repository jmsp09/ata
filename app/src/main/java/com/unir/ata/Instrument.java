package com.unir.ata;


import android.content.Context;

public class Instrument {

    //Constantes
    public static final int INSTRUMENT_CLARINET = 0;
    public static final int INSTRUMENT_BOMBARDINO = 1;
    public static final int INSTRUMENT_SAXOFON = 2;


    public static Properties getInstrumentProperties(int instrument, Context context) {

        //Transposición
        final String[] NOTE_NAMES;
        final double FREQ_REF;
        final String NOTE_REF;

        //Frecuencias mínimas y máximas
        final int MIN_FREQ;// = 40;
        final int MAX_FREQ;// = 4200;
        final int MIN_DB;

        //Desviación
        final double MAX_DEVIATION;


        switch (instrument) {
            case Instrument.INSTRUMENT_CLARINET:
                MIN_FREQ = 165;
                MAX_FREQ = 1568;
                MIN_DB = 90;
                NOTE_NAMES = new String[]{
                        context.getResources().getString(R.string.nota_si),
                        context.getResources().getString(R.string.nota_do),
                        context.getResources().getString(R.string.nota_do_sostenido),
                        context.getResources().getString(R.string.nota_re),
                        context.getResources().getString(R.string.nota_re_sostenido),
                        context.getResources().getString(R.string.nota_mi),
                        context.getResources().getString(R.string.nota_fa),
                        context.getResources().getString(R.string.nota_fa_sostenido),
                        context.getResources().getString(R.string.nota_sol),
                        context.getResources().getString(R.string.nota_sol_sostenido),
                        context.getResources().getString(R.string.nota_la),
                        context.getResources().getString(R.string.nota_la_sostenido)};
                FREQ_REF = 440.0;
                MAX_DEVIATION = 26.16;
                NOTE_REF = context.getResources().getString(R.string.nota_si);
                break;
            case Instrument.INSTRUMENT_BOMBARDINO:
                MIN_FREQ = 49;
                MAX_FREQ = 587;
                MIN_DB = 100;
                NOTE_NAMES = new String[]{
                        context.getResources().getString(R.string.nota_si),
                        context.getResources().getString(R.string.nota_do),
                        context.getResources().getString(R.string.nota_do_sostenido),
                        context.getResources().getString(R.string.nota_re),
                        context.getResources().getString(R.string.nota_re_sostenido),
                        context.getResources().getString(R.string.nota_mi),
                        context.getResources().getString(R.string.nota_fa),
                        context.getResources().getString(R.string.nota_fa_sostenido),
                        context.getResources().getString(R.string.nota_sol),
                        context.getResources().getString(R.string.nota_sol_sostenido),
                        context.getResources().getString(R.string.nota_la),
                        context.getResources().getString(R.string.nota_la_sostenido)};
                FREQ_REF = 440.0;
                MAX_DEVIATION = 26.16;
                NOTE_REF = context.getResources().getString(R.string.nota_si);
                break;
            case Instrument.INSTRUMENT_SAXOFON:
                MIN_FREQ = 175;
                MAX_FREQ = 698;
                MIN_DB = 110;
                NOTE_NAMES = new String[]{
                        context.getResources().getString(R.string.nota_fa_sostenido),
                        context.getResources().getString(R.string.nota_sol),
                        context.getResources().getString(R.string.nota_sol_sostenido),
                        context.getResources().getString(R.string.nota_la),
                        context.getResources().getString(R.string.nota_la_sostenido),
                        context.getResources().getString(R.string.nota_si),
                        context.getResources().getString(R.string.nota_do),
                        context.getResources().getString(R.string.nota_do_sostenido),
                        context.getResources().getString(R.string.nota_re),
                        context.getResources().getString(R.string.nota_re_sostenido),
                        context.getResources().getString(R.string.nota_mi),
                        context.getResources().getString(R.string.nota_fa)};
                FREQ_REF = 440.0;
                MAX_DEVIATION = 26.16;
                NOTE_REF = context.getResources().getString(R.string.nota_fa_sostenido);
                break;
            default:
                MIN_FREQ = 40;
                MAX_FREQ = 4200;
                MIN_DB = 70;
                NOTE_NAMES = new String[]{
                        context.getResources().getString(R.string.nota_la),
                        context.getResources().getString(R.string.nota_la_sostenido),
                        context.getResources().getString(R.string.nota_si),
                        context.getResources().getString(R.string.nota_do),
                        context.getResources().getString(R.string.nota_do_sostenido),
                        context.getResources().getString(R.string.nota_re),
                        context.getResources().getString(R.string.nota_re_sostenido),
                        context.getResources().getString(R.string.nota_mi),
                        context.getResources().getString(R.string.nota_fa),
                        context.getResources().getString(R.string.nota_fa_sostenido),
                        context.getResources().getString(R.string.nota_sol),
                        context.getResources().getString(R.string.nota_sol_sostenido),};
                FREQ_REF = 440.0;
                MAX_DEVIATION = 26.16;
                NOTE_REF = context.getResources().getString(R.string.nota_la);
                break;
        }


        return new Properties(NOTE_NAMES, FREQ_REF, MIN_FREQ, MAX_FREQ, MIN_DB, MAX_DEVIATION, NOTE_REF);
    }

    public static class Properties {

        //Transposición
        final String[] NOTE_NAMES;
        final double FREQ_REF;
        final String NOTE_REF;

        //Frecuencias mínimas y máximas
        final int MIN_FREQ;// = 40;
        final int MAX_FREQ;// = 4200;
        final int MIN_DB;

        //Desviación
        final double MAX_DEVIATION;

        private Properties(String[] noteNames, double freqRef, int minFreq, int maxFreq, int minDB,
                           double maxDeviation, String noteRef) {
            this.NOTE_NAMES = noteNames;
            this.FREQ_REF = freqRef;
            this.NOTE_REF = noteRef;
            this.MIN_FREQ = minFreq;
            this.MAX_FREQ = maxFreq;
            this.MIN_DB = minDB;
            this.MAX_DEVIATION = maxDeviation;
        }


    }

}
