package com.unir.ata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Instrument {

    //Constantes
    public static final int INSTRUMENT_CLARINET = 0;
    public static final int INSTRUMENT_BOMBARDINO = 1;
    public static final int INSTRUMENT_SAXOFON = 2;


    public static Properties getInstrumentProperties(int instrument) {

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
                MIN_DB = 60; //100 //TODO
                NOTE_NAMES = new String[]{"Si", "Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#", "La", "La#"};
                FREQ_REF = 440.0;
                MAX_DEVIATION = 26.16;
                NOTE_REF = "Si";
                break;
            case Instrument.INSTRUMENT_BOMBARDINO:
                MIN_FREQ = 49;
                MAX_FREQ = 587;
                MIN_DB = 110;
                NOTE_NAMES = new String[]{"Si", "Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#", "La", "La#"};
                FREQ_REF = 440.0;
                MAX_DEVIATION = 26.16;
                NOTE_REF = "Si";
                break;
            case Instrument.INSTRUMENT_SAXOFON:
                MIN_FREQ = 175;
                MAX_FREQ = 698;
                MIN_DB = 120;
                NOTE_NAMES = new String[]{"Fa#", "Sol", "Sol#", "La", "La#", "Si", "Do", "Do#", "Re", "Re#", "Mi", "Fa"};
                FREQ_REF = 440.0;
                MAX_DEVIATION = 26.16;
                NOTE_REF = "Fa#";
                break;
            default:
                MIN_FREQ = 40;
                MAX_FREQ = 4200;
                MIN_DB = 70;
                NOTE_NAMES = new String[]{"La", "La#", "Si", "Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#"};
                FREQ_REF = 440.0;
                MAX_DEVIATION = 26.16;
                NOTE_REF = "Si";
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
