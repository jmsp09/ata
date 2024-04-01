package com.unir.ata;

import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class TunerProcess implements Runnable {

    //Instancia única de la clase
    private static TunerProcess tunerProcess;

    //Variables
    private final Tuner tuner;
    private final Handler handler;

    //Constantes del formato de audio
    private final static int RATE = 8000;
    private final static int CHANNEL = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private final static int ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    //Constantes para el buffer
    private final static int BF_MS = 3000;
    private final static int BF_BYTES = RATE * BF_MS / 1000 * 2;

    //Constantes para el segmento analizado
    private final static int NUMBER_OF_BITS_FFT = 12;
    private final static int SEGMENT = 4096;
    private final static int SEGMENT_MS = 1000 * SEGMENT / RATE;
    private final static int SEGMENT_BYTES = RATE * SEGMENT_MS / 1000 * 2;

    //Frecuencias mínimas y máximas
    private final static int MIN_FREQ = 25;
    private final static int MAX_FREQ = 4200;
    private final static int MIN_FREQ_BY_RATE = (MIN_FREQ * SEGMENT / RATE);
    private final static int MAX_FREQ_BY_RATE = (MAX_FREQ * SEGMENT / RATE);



    private TunerProcess(Tuner tuner) {
        this.tuner = tuner;
        this.handler = new Handler();
    }

    public static TunerProcess getInstance(Tuner tuner) {
        if (tunerProcess == null) {
            tunerProcess = new TunerProcess(tuner);
        }
        return tunerProcess;
    }

    public void run() {

        Log.d("!!!********Run ", "!!!********");

        //Damos prioridad para la detección de la nota
        android.os.Process
                .setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        AudioRecord audioRecord;
        try {

            //Comprobamos acceso al micrófono
            if (ActivityCompat.checkSelfPermission(
                    this.tuner.activity, 
                    android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                Log.d("!!!********EError ", "!!!********Sin microfono");
                return;
            }

            audioRecord = new AudioRecord(AudioSource.MIC,
                    RATE,
                    CHANNEL,
                    ENCODING,
                    BF_BYTES);
        } catch (Exception e) {
            Log.d("!!!********Error79 ", "!!!********" + e);
            throw e; //TODO revisar
        }


        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            showResults(null, 0.0, true, "No se pudo inicializar el micrófono");
            Log.d("!!!********EError ", "!!!********No se pudo inicializar el micrófono");
            return;
        }

        //Variables para almacenar el audio recogido
        short[] dataAudio = new short[BF_BYTES / 2];
        double[] realPart = new double[SEGMENT];
        double[] imaginaryPart = new double[SEGMENT];
        FFT fft = new FFT(NUMBER_OF_BITS_FFT);

        Log.d("!!!********Start recording ", "!!!********");
        //Inicializamos la grabación
        audioRecord.startRecording();
        Log.d("!!!********Start recording2 ", "!!!********");

        //Capturamos el audio constantemente
        while (!Thread.interrupted()) {

            //Leemos el audio
            audioRecord.read(dataAudio, 0, SEGMENT_BYTES / 2);


            //Inicializamos el audio
            for (int i = 0; i < SEGMENT; i++) {
                realPart[i] = dataAudio[i];
                imaginaryPart[i] = 0;
            }

            //Aplicamos fft
            fft.doFFT(realPart, imaginaryPart, false);


            //Calculamos la frecuencia a partir de los datos de la FFT
            double bestFrequency = MIN_FREQ_BY_RATE;
            double bestAmplitude = 0;
            float db;

            for (int i = MIN_FREQ_BY_RATE; i <= MAX_FREQ_BY_RATE; i++) {
                final double current_frequency = i * 1.0 * RATE
                        / SEGMENT;
                final double current_amplitude = Math.pow(realPart[i], 2)
                        + Math.pow(imaginaryPart[i], 2);
                if (current_amplitude > bestAmplitude) {
                    bestFrequency = current_frequency;
                    bestAmplitude = current_amplitude;
                }

            }
            //Calculamos los decibelios
            db = 20 * (float)(Math.log10(bestAmplitude));


            if (bestFrequency > MIN_FREQ && db > 50) {
                Log.d("!!!********!< DB: " + db, "!!!********!< DB: " + db);
                Log.d("!!!bestFrequency: " + bestFrequency, "!!!bestFrequency: " + bestFrequency);
                Log.d("!!!bestAmplitude: " + bestAmplitude, "!!!bestAmplitude: " + bestAmplitude);
                Log.d("!!!!!!!!!!!!", "!!!!!!!!!!!!!!!! ");
                postResultsByHandler(getNoteName(bestFrequency), bestFrequency, false, null);
            } else {
                // No se recoge ningún sonido
                postResultsByHandler("Not sound", 0.0, false, null);
            }

        }
        audioRecord.stop();
        audioRecord.release();
    }

    public String getNoteName(double frecuencia) {
        int interval;
        String noteName;

        interval = (int) Math
                .round(((((Math.log(frecuencia)) - (Math.log(440))) / (Math
                        .log(2))) * 12));

        if (interval > 11 || interval < 0) {
            interval = interval % 12;
        }
        if (interval < 0) {
            interval = 12 + interval;
        }
        switch (interval) {
            case 0:
                noteName = "La";
                break;
            case 1:
                noteName = "Si#";
                break;
            case 2:
                noteName = "Si";
                break;
            case 3:
                noteName = "Do";
                break;
            case 4:
                noteName = "Do#";
                break;
            case 5:
                noteName = "Re";
                break;
            case 6:
                noteName = "Re#";
                break;
            case 7:
                noteName = "Mi";
                break;
            case 8:
                noteName = "Fa";
                break;
            case 9:
                noteName = "Fa#";
                break;
            case 10:
                noteName = "Sol";
                break;
            case 11:
                noteName = "Sol#";
                break;
            default:
                noteName = "";
                break;
        }

        return noteName;
    }


    protected void showResults(String note, double frequency, boolean isError, String errMsg) {
        tuner.showResults(note, frequency, isError, errMsg);
    }

    private void postResultsByHandler(String note, double frequency, boolean isError, String errMsg) {
        handler.post(() -> showResults(note, frequency, isError, errMsg));
    }



}
