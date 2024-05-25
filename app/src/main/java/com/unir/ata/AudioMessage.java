package com.unir.ata;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;

public class AudioMessage {

    //Variables
    private static AudioMessage audioMessage;
    private final Context context;
    private static TextToSpeech textToSpeech;
    private static boolean statusOK = false;
    private static boolean isSpeaking = false;


    //Mensajes
    public static int AM_CLICK_SELECCIONAR = R.string.am_click_seleccionar;
    public static final int AM_SELECTED_INSTRUMENT = R.string.am_selected_instrument;

    //Vibraciones
    public static final int AM_VIBRATION_INFO = 1;
    public static final int AM_VIBRATION_CONFIRM = 2;




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

                    //textToSpeech.setLanguage(Locale.); //TODO
                    statusOK = true;
                }
            }
        });
    }

    protected void playMessage(@NonNull String text, int typeVibration) {

        if (!textToSpeech.isSpeaking()) {
            isSpeaking = false;
        }

        if (statusOK && (typeVibration == AM_VIBRATION_CONFIRM || !isSpeaking)) {
            isSpeaking = true;
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

            switch (typeVibration) {
                case AM_VIBRATION_INFO:
                    clickVibration();
                    break;
                case AM_VIBRATION_CONFIRM:
                    longClickVibration();
                    break;
                default:
                    break;
            }
        }
    }

    protected void clickVibration() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Verificar si el dispositivo soporta la vibración y si el permiso está concedido
        if (vibrator != null && vibrator.hasVibrator()) {
            //https://developer.android.com/develop/ui/views/haptics/actuators
            long[] timings = new long[] { 40, 50, 20 };
            int[] amplitudes = new int[] { 17, 29, 44};
            int repeatIndex = -1; // Do not repeat.

            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex));
        }
    }

    protected void longClickVibration() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Verificar si el dispositivo soporta la vibración y si el permiso está concedido
        if (vibrator != null && vibrator.hasVibrator()) {

            long[] timings = new long[] { 40, 30, 30, 0, 20, 60};
            int[] amplitudes = new int[] { 44, 27, 30, 40, 17, 84};
            int repeatIndex = -1; // Do not repeat.

            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex));
        }
    }

}
