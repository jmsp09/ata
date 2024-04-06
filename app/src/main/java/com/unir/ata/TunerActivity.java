package com.unir.ata;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TunerActivity extends AppCompatActivity {

    //Variables
    private Tuner tuner;

    //Elementos visuales
    private TextView textViewNote;
    private TextView textViewFreq;

    //Constantes
    private static final int PERMISSION_RECORD_AUDIO = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tuner);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Mantenemos la pantalla encendida
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Inicializar settings
        //TODO

        //Inicializar botones, eventos
        //TODO
        textViewNote = (TextView) findViewById(R.id.textViewNote);
        textViewFreq = (TextView) findViewById(R.id.textViewFreq);

        //Requerimos permiso de grabación e iniciamos el afinador
        requestAudioPermissions();
    }


    private void requestAudioPermissions() {

        //Comprobamos si tenemos permiso de grabación
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //Si el permiso no está concedido, mostramos un mensaje de por qué es necesario
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this,
                        "Es necesario grabar a través del micrófono para poder afinar"
                        , Toast.LENGTH_LONG).show();

                //Requerimos al usuario el permiso
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.RECORD_AUDIO},
                        PERMISSION_RECORD_AUDIO);

            } else {
                // Mostramos al usuario un diálogo para permitir el acceso de grabación
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.RECORD_AUDIO},
                        PERMISSION_RECORD_AUDIO);
            }

        //Si el permiso está concedio inicializamos el afinador
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {


            //Iniciamos el afinador
            initTuner();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_RECORD_AUDIO: {

                //Permiso concedido
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initTuner();

                } else {
                    //Permiso denegado
                    //TODO mostrar error

                    Toast.makeText(this,
                            "Permiso denegado",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void initTuner() {
        tuner = Tuner.getInstance(this);
        tuner.start();
    }

    public void showTunerResults(DetectedNote note, boolean isError, String errMsg) {

        //if(isError) //TODO
        if (isError || note == null) {
            errMsg = errMsg == null || errMsg == ""? "Sin sonido" : errMsg;
            textViewNote.setText(errMsg);
            textViewFreq.setText("");
        } else {
            textViewNote.setText(note.getName());
            textViewFreq.setText("" + note.getFrequency() + " Hz");
        }
    }
}