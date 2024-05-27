package com.unir.ata;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private final int DELAY = 2000;
    private boolean keep = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen;
        if (Build.VERSION.SDK_INT >= 31){
            splashScreen = SplashScreen.installSplashScreen(this); //init new splash screen api for Android 12+

            //Keep returning false to Should Keep On Screen until ready to begin.
            splashScreen.setKeepOnScreenCondition(() -> keep);
            Handler handler = new Handler();
            handler.postDelayed(runner, DELAY);
            super.onCreate(savedInstanceState);
        } else{
            setTheme(R.style.Theme_AccessibleTunerApp); //else use old approach
            super.onCreate(savedInstanceState);
            start();
        }



    }

    private final Runnable runner = () -> {
        keep = false;
        start();

    };

    private void start() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Mantenemos la pantalla encendida
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

       //Comprobamos si la persona tiene el móvil silenciado. Es importante que pueda recibir el feedback auditivo
        if(isSoundMuted(this)) {
            showTurnUpVolumeMessage();
        } else {
            redirect();
        }
    }

    private void showTurnUpVolumeMessage() {

        AppCompatButton acceptButton = findViewById(R.id.acceptButton);
        acceptButton.setVisibility(View.VISIBLE);
        acceptButton.setOnClickListener(v -> redirect());

        TextView textView = findViewById(R.id.textViewIni);
        textView.setText(R.string.turnVolumeUp);
        textView.setOnClickListener(v ->
                AudioMessage.getInstance(this)
                        .playMessage((String) Objects.requireNonNull(textView.getText()),
                                AudioMessage.AM_VIBRATION_INFO));
    }


    private void redirect() {

        //Redirect
        Navigation.redirect(Navigation.TUNER_ACTIVITY);
        Intent intent = new Intent(this, TunerActivity.class);
        this.startActivity(intent);
    }

    public static boolean isSoundMuted(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager != null) {
            int ringerMode = audioManager.getRingerMode();
            if (ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
                return true; // El dispositivo está silenciado o en modo vibración
            }

            //Comprobamos si está el volumen al 0
            int musicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (musicVolume == 0) {
                return true;
            }
        }

        return false;
    }

}