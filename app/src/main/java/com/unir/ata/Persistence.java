package com.unir.ata;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class Persistence {

    private final String SHARED_PREFS_FILE = "ataPreferences";
    private final String LANGUAGE = "language";
    private final String INSTRUMENT = "instrument";
    private static Persistence persistence;
    private Context context;


    private Persistence(Context context){
        this.context = context;
    }

    public static Persistence getInstance(@NonNull Context context) {
        if (persistence == null) {
            persistence = new Persistence(context);
        }
        return persistence;
    }
    private SharedPreferences getSettings() {
        return context.getSharedPreferences(SHARED_PREFS_FILE, 0);
    }

    public String getLanguage() {
        return getSettings().getString(LANGUAGE, "es");
    }

    public void setLanguage(String language) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(LANGUAGE, language);
        editor.commit();
    }

    public int getInstrument() {
        return getSettings().getInt(INSTRUMENT, Tuner.INSTRUMENT_CLARINET);
    }

    public void setInstrument(int instrument) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putInt(INSTRUMENT, instrument);
        editor.commit();
    }


}
