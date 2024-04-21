package com.unir.ata;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Locale;

public class AudioMessage {

    //Variables
    private static AudioMessage audioMessage;
    private final Context context;
    private static TextToSpeech textToSpeech;
    private static boolean statusOK = false;
    private static String lastMessage = "";
    private static boolean isSpeaking = false;
    private static UtteranceProgressListener utteranceListener;


    //Mensajes
    public static int AM_CLICK_SELECCIONAR = R.string.am_click_seleccionar;
    public static int AM_CLICK_AFINAR = R.string.am_click_afinar;
    public static int AM_CLICK_INSTRUMENTOS = R.string.am_click_instrumentos;

    //Vibraciones
    public static final int AM_VIBRATION_TOUCH = 1;
    public static final int AM_VIBRATION_CLICK = 2;




    private AudioMessage(Context context) {
        this.context = context;
    }

    public static AudioMessage getInstance(@NonNull Context context) {
        if (audioMessage == null) {
            audioMessage = new AudioMessage(context);
            audioMessage.init();
        }
        return audioMessage;
    }

    private void init() {

        //Inicializamos sintetizador de texto
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Persistence persistence = Persistence.getInstance(context);

                    //textToSpeech.setLanguage(Locale.);
                    statusOK = true;
                    utteranceListener = new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {

                        }

                        @Override
                        public void onDone(String utteranceId) {
                            isSpeaking = false;
                        }

                        @Override
                        public void onError(String utteranceId) {
                            isSpeaking = false;
                        }
                    };
                }
            }
        });
    }

    protected void playMessage(@NonNull String text, int typeVibration) {

        Log.d("!!!!-----------" + statusOK, "!!!!!!00");
        Log.d("!!!!1lastMessage" + lastMessage, "!!!!!!11" + (lastMessage == null) );
        Log.d("!!!!text" + lastMessage, "!!!!!!22" + text);
        Log.d("!!!!4lastMessage" + lastMessage, "!!!!!!44");

        if (!textToSpeech.isSpeaking()) {
            isSpeaking = false;
        }
        if (statusOK && (lastMessage == null || lastMessage.isEmpty() || (
                lastMessage != null && !isSpeaking))) {
            Log.d("!!!!5", "!!!!!!55");
            isSpeaking = true;
            lastMessage = text;
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

            switch (typeVibration) {
                case AM_VIBRATION_TOUCH:
                    touchVibration();
                    break;
                case AM_VIBRATION_CLICK:
                    clickVibration();
                    break;
                default:
                    break;
            }
        }
    }

    protected boolean touchVibration() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Verificar si el dispositivo soporta la vibración y si el permiso está concedido
        if (vibrator != null && vibrator.hasVibrator()) {
            // Vibrar durante 100 milisegundos
                        /*vibrator.vibrate(VibrationEffect
                                .createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));*/
            //TODO https://developer.android.com/develop/ui/views/haptics/actuators
            long[] timings = new long[] { 40, 50, 20 }; //TODO Vibración corta para onTouch, investigar una larga para onClick
            int[] amplitudes = new int[] { 17, 29, 44};
            int repeatIndex = -1; // Do not repeat.

            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex));
        }
        return false;
    }

    protected boolean clickVibration() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Verificar si el dispositivo soporta la vibración y si el permiso está concedido
        if (vibrator != null && vibrator.hasVibrator()) {

            long[] timings = new long[] { 40, 20, 0, 20, 60, 40 };
            int[] amplitudes = new int[] { 44, 27, 0, 17, 54, 44};
            int repeatIndex = -1; // Do not repeat.

            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex));
        }
        return false;
    }

}
