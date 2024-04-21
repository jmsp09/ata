package com.unir.ata;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TunerActivity extends AppCompatActivity
        implements InstrumentCardAdapter.OnCardItemClickListener {


    //Constantes
    private static final int PERMISSION_RECORD_AUDIO = 1;

    //Variables
    private Tuner tuner;

    private ViewFlipper viewFlipper;

    //Elementos visuales
    private TextView textViewNote;
    private TextView textViewFreq;
    private TextView textViewLastNotes;
    private TextView textViewLastFreqs;
    private TextView textDBs;

    //Variables para el fragment de selector de instrumentos
    private RecyclerView recyclerView;
    private InstrumentCardAdapter cardAdapter;
    private List<InstrumentCardItem> instrumentCardItems;

    //Navegacion
    private Navigation navigation;
    private Persistence persistence;
    private int instrument;
    private String language;

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
        initSettings();

        //Inicializar navegacion
        viewFlipper = findViewById(R.id.viewFlipper);
        navigation = Navigation.getInstance(this);

        //Inicializamos afinador
        initTunerFragment();
    }

    public void initFragment(@NonNull int fragment) {

        //Interrumpimos la ejecución del afinador si estamos en una página diferente
        if (fragment != Navigation.TUNER_ACTIVITY) {
            stopTuner();
        }

        //Redirigimos al fragmento deseado
        if (fragment == Navigation.TUNER_ACTIVITY) {
            initTunerFragment();
        } else if (fragment == Navigation.INFO_ACTIVITY) {
            //initTunerFragment();
        } else if (fragment == Navigation.OPTIONS_ACTIVITY){
            initInstrumentsFragment();
        }
    }


    private void initTunerFragment() {
        //Inicializar botones, eventos
        //TODO
        LinearLayout tunerFragment =  findViewById(R.id.tuner_fragment);
        textViewNote = (TextView) tunerFragment.findViewById(R.id.textViewNote);
        textViewFreq = (TextView) tunerFragment.findViewById(R.id.textViewFreq);
        textViewLastNotes = (TextView) tunerFragment.findViewById(R.id.textViewLastNotes);
        textViewLastFreqs = (TextView) tunerFragment.findViewById(R.id.textViewLastFreqs);
        textDBs = (TextView) tunerFragment.findViewById(R.id.textDBs);

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_RECORD_AUDIO) {
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

    private void initNavigation() {
        navigation.initNavigation();
    }


    private void initTuner() {
        tuner = Tuner.getInstance(this);
        tuner.start();
    }

    private void stopTuner() {
        tuner = Tuner.getInstance(this);
        tuner.interrupt();
    }

    public void showTunerResults(DetectedNote note, boolean isError, String errMsg) {

        String[] lastDetections =  LastDetections.print();
        if (isError || note == null) {
            errMsg = errMsg == null || errMsg.isEmpty() ? "Sin sonido" : errMsg; //TODO
            textViewNote.setText(errMsg);
            textViewFreq.setText("");
        } else {
            textViewNote.setText(note.getName());
            textViewFreq.setText(note.getFrequency() + " Hz");
            textViewLastFreqs.setText(lastDetections[LastDetections.FREQUENCIES]);
            textViewLastNotes.setText(lastDetections[LastDetections.NOTES]);
            textDBs.setText(""+note.getDecibels() + " DB");

            //Guardamos las últimas notas en el historial
            LastDetections.addDetection(note);
        }
    }

    public void initInstrumentsFragment() {

        // Initialize RecyclerView
        LinearLayout instrumentsFragment = findViewById(R.id.instruments_fragment);
        recyclerView = instrumentsFragment.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Prepare card items
        instrumentCardItems = new ArrayList<>();
        instrumentCardItems.add(new InstrumentCardItem(R.drawable.clarinete,
                getString(R.string.clarinete), getString(R.string.clarinete_description)));
        instrumentCardItems.add(new InstrumentCardItem(R.drawable.bombardino,
                getString(R.string.bombardino), getString(R.string.bombardino_description)));
        instrumentCardItems.add(new InstrumentCardItem(R.drawable.trompeta,
                getString(R.string.trompeta), getString(R.string.trompeta_description)));

        // Set up adapter for RecyclerView
        cardAdapter = new InstrumentCardAdapter(this, instrumentCardItems, this);
        recyclerView.setAdapter(cardAdapter);
    }


    @Override
    public void onTextClick(int position) {
        InstrumentCardItem clickedItem = instrumentCardItems.get(position);

        Persistence persistence = Persistence.getInstance(this);
        persistence.setInstrument(position);

        Toast.makeText(this, "Instrumento seleccionado: "
                + clickedItem.getDescription(), Toast.LENGTH_SHORT).show();
        //TODO

        //Navigation.redirect(Navigation.TUNER_ACTIVITY);
    }

    @Override
    public void onTextTouch(int position) {
        InstrumentCardItem clickedItem = instrumentCardItems.get(position);

        AudioMessage.getInstance(this)
                .playMessage(clickedItem.getText()
                        + ". " + this.getString(AudioMessage.AM_CLICK_SELECCIONAR),
                        AudioMessage.AM_VIBRATION_TOUCH);

    }

    private void initSettings(){

        persistence = Persistence.getInstance(this);

        instrument = persistence.getInstrument();
        language = persistence.getLanguage();

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        getBaseContext().createConfigurationContext(configuration);
    }


}