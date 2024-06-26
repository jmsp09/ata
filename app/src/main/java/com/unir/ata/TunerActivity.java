package com.unir.ata;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
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
import java.util.Objects;

public class TunerActivity extends AppCompatActivity
        implements InstrumentCardAdapter.OnCardItemClickListener {


    //Constantes
    private static final int PERMISSION_RECORD_AUDIO = 1;

    //Variables
    private Tuner tuner;

    //Elementos visuales
    private TextView textViewNote;
    private LinearLayout deviationTop1;
    private LinearLayout deviationTop2;
    private LinearLayout deviationBottom1;
    private LinearLayout deviationBottom2;

    private List<InstrumentCardItem> instrumentCardItems;

    private Persistence persistence;

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
        Navigation.getInstance(this);

        //Inicializar sistema de feedback de audio
        AudioMessage.getInstance(this);

        //Inicializamos afinador
        initTunerFragment();

        //Cerramos la aplicación cuando se pulsa el botón de retroceso
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Cerrar la aplicación
                finishAffinity();
            }
        };

        // Añadir el callback al dispatcher
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void initFragment(int fragment) {

        //Interrumpimos la ejecución del afinador si estamos en una página diferente
        if (fragment != Navigation.TUNER_ACTIVITY) {
            stopTuner();
        }

        //Redirigimos al fragmento deseado
        if (fragment == Navigation.TUNER_ACTIVITY) {
            initTunerFragment();
        } else if (fragment == Navigation.INFO_ACTIVITY) {
            initInfoFragment();
        } else if (fragment == Navigation.OPTIONS_ACTIVITY){
            initInstrumentsFragment();
        }
    }


    private void initTunerFragment() {

        //Inicializar botones, eventos
        LinearLayout tunerFragment =  findViewById(R.id.tuner_fragment);
        textViewNote = tunerFragment.findViewById(R.id.textViewNote);
        deviationTop1 = tunerFragment.findViewById(R.id.deviationTop1);
        deviationTop2 = tunerFragment.findViewById(R.id.deviationTop2);
        deviationBottom1 = tunerFragment.findViewById(R.id.deviationBottom1);
        deviationBottom2 = tunerFragment.findViewById(R.id.deviationBottom2);

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

            //Si el permiso está revocado, pedimos el acceso
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_RECORD_AUDIO) {
            //Permiso concedido
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initTuner();

            } else {
                //Permiso denegado
                AudioMessage.getInstance(this)
                        .playMessage(getString(R.string.microphone_not_allowed),
                                AudioMessage.AM_VIBRATION_INFO);
            }
        }
    }

    private void initTuner() {
        tuner = Tuner.getInstance(this);

        //Obtenemos el instrumento seleccionado
        persistence = Persistence.getInstance(this);
        int instrument = persistence.getInstrument();

        tuner.setInstrument(instrument);

        tuner.start();
    }

    protected void stopTuner() {
        tuner = Tuner.getInstance(this);
        tuner.interrupt();
    }

    public void showTunerResults(DetectedNote note, boolean isError, String errMsg) {

        //Si la aplicación está hablando, no mostramos las desviaciones de afinacion
        if (AudioMessage.getInstance(this).isSpeaking()) {
            return;
        }

        //Parámetros para dibujar la barra de afinación
        LinearLayout.LayoutParams layoutParamsHidden = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                0f
        );
        LinearLayout.LayoutParams layoutParamsAll = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
        );

        //Sin sonido
        if (isError || note == null) {
            errMsg = errMsg == null || errMsg.isEmpty() ?
                    this.getString(R.string.not_sound) : errMsg;
            textViewNote.setText(errMsg);


            if(LastDetections.getNumEqualNotes() >= LastDetections.MIN_EQUALS_NOTES_TO_PRINT
                    || LastDetections.getlastItems().isEmpty()) {

                //Cambiamos altura barra
                deviationBottom1.setLayoutParams(layoutParamsHidden);
                deviationBottom2.setLayoutParams(layoutParamsAll);
                deviationTop1.setLayoutParams(layoutParamsHidden);
                deviationTop2.setLayoutParams(layoutParamsAll);

                //Cambiamos colores
                deviationBottom1.setBackgroundColor(Color.BLACK);
                deviationTop1.setBackgroundColor(Color.BLACK);
            }

        } else {

            //Guardamos las últimas notas en el historial
            LastDetections.addDetection(note);

            if (LastDetections.getNumEqualNotes() >= LastDetections.MIN_EQUALS_NOTES_TO_PRINT) {

                String msgVeryModifier = "";
                String msgDirectionModifier = "";
                int deviation = (int) note.getDeviationInstrument();
                boolean isInstrumentInTune = false;

                if (Math.abs(deviation) < 10) {

                    textViewNote.setText(this.getString(R.string.Instrument_in_tune));
                    isInstrumentInTune = true;

                    deviationBottom1.setLayoutParams(layoutParamsHidden);
                    deviationBottom2.setLayoutParams(layoutParamsAll);
                    deviationTop1.setLayoutParams(layoutParamsHidden);
                    deviationTop2.setLayoutParams(layoutParamsAll);
                } else {
                    boolean directionTop = deviation > 0;
                    deviation = Math.abs(deviation);
                    int deviation2 = 100 - deviation;
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0,
                            deviation
                    );
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0,
                            deviation2
                    );
                    boolean bigDeviation = deviation > 50;
                    int deviationColor =  bigDeviation ? Color.RED : Color.parseColor("#FFA401"); //Naranja
                    msgVeryModifier = bigDeviation? getString(R.string.very) : getString(R.string.a_bit);
                    msgDirectionModifier = directionTop? getString(R.string.high) : getString(R.string.low);


                    if(directionTop) {
                        //Cambiamos altura barra
                        deviationBottom1.setLayoutParams(layoutParamsHidden);
                        deviationBottom2.setLayoutParams(layoutParamsAll);
                        deviationTop1.setLayoutParams(layoutParams1);
                        deviationTop2.setLayoutParams(layoutParams2);

                        //Cambiamos colores
                        deviationBottom1.setBackgroundColor(Color.BLACK);
                        deviationTop1.setBackgroundColor(deviationColor);

                    } else {
                        //Cambiamos altura barra
                        deviationBottom1.setLayoutParams(layoutParams1);
                        deviationBottom2.setLayoutParams(layoutParams2);
                        deviationTop1.setLayoutParams(layoutParamsHidden);
                        deviationTop2.setLayoutParams(layoutParamsAll);

                        //Cambiamos colores
                        deviationBottom1.setBackgroundColor(deviationColor);
                        deviationTop1.setBackgroundColor(Color.BLACK);
                    }

                    //Mostramos el texto adecuado
                    textViewNote.setText(getString(R.string.tuning_on) + note.getName());

                }

                if (LastDetections.getNumEqualNotes() >= LastDetections.MIN_EQUALS_NOTES) {

                    if (isInstrumentInTune) {
                        AudioMessage.getInstance(this)
                                .playMessage(getString(R.string.instrument_is_on_tune),
                                        AudioMessage.AM_VIBRATION_CONFIRM);
                    } else {
                        AudioMessage.getInstance(this)
                                .playMessage(getString(R.string.tune) + msgVeryModifier + msgDirectionModifier ,
                                        AudioMessage.AM_VIBRATION_INFO);
                    }

                    LastDetections.reset();

                }


            } else {
                //Cambiamos altura barra
                deviationBottom1.setLayoutParams(layoutParamsHidden);
                deviationBottom2.setLayoutParams(layoutParamsAll);
                deviationTop1.setLayoutParams(layoutParamsHidden);
                deviationTop2.setLayoutParams(layoutParamsAll);

                //Cambiamos colores
                deviationBottom1.setBackgroundColor(Color.BLACK);
                deviationTop1.setBackgroundColor(Color.BLACK);
            }
        }


    }

    public void initInfoFragment() {

        LinearLayout infoFragment = findViewById(R.id.info_fragment);
        TextView infoTextView = infoFragment.findViewById(R.id.infoTxt);

        infoTextView.setOnClickListener(v ->
                AudioMessage.getInstance(this)
                        .playMessage((String) Objects.requireNonNull(infoTextView.getText()),
                                AudioMessage.AM_VIBRATION_INFO));

        infoTextView.setOnLongClickListener(v -> {
            AudioMessage.getInstance(this)
                    .playMessage((String) infoTextView.getText(),
                            AudioMessage.AM_VIBRATION_CONFIRM);
            return true;
        });
    }

    public void initInstrumentsFragment() {

        // Inicializar RecyclerView
        LinearLayout instrumentsFragment = findViewById(R.id.instruments_fragment);
        //Variables para el fragment de selector de instrumentos
        RecyclerView recyclerView = instrumentsFragment.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar instrucciones
        TextView selectInstrument = instrumentsFragment.findViewById(R.id.selectInstrument);
        selectInstrument.setOnClickListener(v ->
                AudioMessage.getInstance(this)
                .playMessage((String) Objects.requireNonNull(selectInstrument.getTooltipText()),
                        AudioMessage.AM_VIBRATION_INFO));

        // Inicializar card items
        instrumentCardItems = new ArrayList<>();
        instrumentCardItems.add(new InstrumentCardItem(R.drawable.clarinete,
                getString(R.string.clarinete), getString(R.string.clarinete_description)));
        instrumentCardItems.add(new InstrumentCardItem(R.drawable.bombardino,
                getString(R.string.bombardino), getString(R.string.bombardino_description)));
        instrumentCardItems.add(new InstrumentCardItem(R.drawable.saxofon,
                getString(R.string.saxofon), getString(R.string.saxofon_description)));

        // Inicializar el adaptador del recyclerView
        InstrumentCardAdapter cardAdapter = new InstrumentCardAdapter(instrumentCardItems, this);
        recyclerView.setAdapter(cardAdapter);
    }


    @Override
    public void onClick(int position) {
        InstrumentCardItem clickedItem = instrumentCardItems.get(position);

        AudioMessage.getInstance(this)
                .playMessage(clickedItem.getText()
                                + ". " + this.getString(AudioMessage.AM_CLICK_SELECCIONAR),
                        AudioMessage.AM_VIBRATION_INFO);

    }

    @Override
    public void onLongClick(int position) {
        InstrumentCardItem clickedItem = instrumentCardItems.get(position);

        AudioMessage.getInstance(this)
                .playMessage(this.getString(AudioMessage.AM_SELECTED_INSTRUMENT) +
                                clickedItem.getDescription(),
                        AudioMessage.AM_VIBRATION_CONFIRM);

        Persistence persistence = Persistence.getInstance(this);
        persistence.setInstrument(position);

        Navigation.redirect(Navigation.TUNER_ACTIVITY);

    }

    private void initSettings(){

        persistence = Persistence.getInstance(this);
        String language = persistence.getLanguage();

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        getBaseContext().createConfigurationContext(configuration);
    }



}