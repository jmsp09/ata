package com.unir.ata;


import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import org.jtransforms.fft.DoubleFFT_1D;

public class TunerProcess2 implements Runnable {

    private static TunerProcess2 tunerProcess;

    //Variables
    private final Tuner tuner;
    private final Handler handler;

    private final static int MIN_FREQ = 40;
    private final static int MAX_FREQ = 4200;
    int sampleRate = 44100; // Sample rate in Hz (CD quality)
    int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);


    private TunerProcess2(Tuner tuner) {
        this.tuner = tuner;
        this.handler = new Handler();
    }

    public static TunerProcess2 getInstance(Tuner tuner) {
        if (tunerProcess == null) {
            tunerProcess = new TunerProcess2(tuner);
        }
        return tunerProcess;
    }




    public void run() {

        AudioRecord audioRecord;
        try

        {

            //Comprobamos acceso al micrófono
            if (ActivityCompat.checkSelfPermission(
                    this.tuner.activity,
                    android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                Log.d("!!!********EError ", "!!!********Sin microfono");
                return;
            }

            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleRate, channelConfig, audioFormat, bufferSize);
        } catch(
                Exception e)

        {
            Log.d("!!!********Error79 ", "!!!********" + e);
            throw e; //TODO revisar
        }
///

        audioRecord.startRecording();

        short[] buffer = new short[bufferSize];
        while (!Thread.interrupted()) {
            audioRecord.read(buffer, 0, bufferSize);

            // Process audio data (e.g., perform FFT analysis)
            // Perform FFT on audio data
            double[] audioData = new double[bufferSize];
            for (int i = 0; i < bufferSize; i++) {
                audioData[i] = (double) buffer[i];
            }

            DoubleFFT_1D fft = new DoubleFFT_1D(bufferSize);
            fft.realForward(audioData);

// Analyze frequency spectrum to find dominant frequency
            double dominantFrequency = calculateDominantFrequency(audioData, sampleRate);
            postResultsByHandler(getDetectedNote(dominantFrequency, 77), false, null);

            // Calculate frequency of the captured sound
        }

        audioRecord.stop();
        audioRecord.release();
    }

    private double calculateDominantFrequency(double[] audioData, int sampleRate) {
        int n = audioData.length;
        double[] magnitude = new double[n / 2];

        for (int i = 0; i < n / 2; i++) {
            double real = audioData[2 * i];
            double imag = audioData[2 * i + 1];
            magnitude[i] = Math.sqrt(real * real + imag * imag);
        }

        int maxIndex = 0;
        double maxMagnitude = magnitude[0];

        for (int i = 1; i < n / 2; i++) {
            if (magnitude[i] > maxMagnitude) {
                maxMagnitude = magnitude[i];
                maxIndex = i;
            }
        }

        return maxIndex * ((double) sampleRate) / n;
    }


    protected void showResults(DetectedNote note, boolean isError, String errMsg) {
        tuner.showResults(note, isError, errMsg);
    }

    private void postResultsByHandler(DetectedNote note, boolean isError, String errMsg) {
        handler.post(() -> showResults(note, isError, errMsg));
    }

    public DetectedNote getDetectedNote(double frecuency, double decibels) {

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

        //La, Si#, Si, Do, Do#, Re... //TODO
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
        note.setDecibels(decibels);

        return note;
    }



}