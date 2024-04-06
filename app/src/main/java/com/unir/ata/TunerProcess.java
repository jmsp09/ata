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
            showResults(null, true, "No se pudo inicializar el micrófono");
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
                postResultsByHandler(getNoteName(bestFrequency), false, null);
            } else {
                // No se recoge ningún sonido
                postResultsByHandler(null, true, "Not sound");
            }

        }
        audioRecord.stop();
        audioRecord.release();
    }

    public DetectedNote getNoteName(double frecuency) {

        int interval;
        double octaves;
        DetectedNote note = new DetectedNote();
        note.setFrequency(frecuency);

        interval = (int) Math
                .round(((((Math.log(frecuency)) - (Math.log(440))) / (Math
                        .log(2))) * 12));


        octaves = interval / 12;
        interval = (interval % 12 + 12) % 12; // Ajuste para asegurar que interval está entre 0 y 11

        // Convertir las octavas a frecuencia
        octaves = Math.pow(2, octaves);
        Log.d("!!!********!octaves: " + octaves, "!!!********!octaves: " + octaves);

        //La, Si#, Si, Do, Do#, Re...
        double[] frequencies = {440.0, 466.16, 493.88, 523.25, 554.37, 587.33, 622.25, 659.26, 698.46,
                                739.99, 783.99, 830.61};
        String[] noteNames = {"La", "Si#", "Si", "Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#"};

        // Asegurarse de que el intervalo esté dentro del rango
        if (interval < 0 || interval >= frequencies.length) {
            return note;
        }

        // Calcular la frecuencia de referencia (si estuviera afinada)
        double freqReference = frequencies[interval] * octaves;

        //Rellenamos las propiedades de la nota detectada
        note.setName(noteNames[interval]);
        note.setDeviation(frecuency - freqReference);

        return note;
    }


    protected void showResults(DetectedNote note, boolean isError, String errMsg) {
        tuner.showResults(note, isError, errMsg);
    }

    private void postResultsByHandler(DetectedNote note, boolean isError, String errMsg) {
        handler.post(() -> showResults(note, isError, errMsg));
    }



}
