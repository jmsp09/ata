package com.unir.ata;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    private String TAG = "DevManager";
    private String KEY = "";
    private String NAME = "";
    private final int DELAY = 2000;
    private boolean keep = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen;
        if (Build.VERSION.SDK_INT >= 31){
            splashScreen = SplashScreen.installSplashScreen(this); //init new splash screen api for Android 12+

            //Keep returning false to Should Keep On Screen until ready to begin.
            splashScreen.setKeepOnScreenCondition(new SplashScreen.KeepOnScreenCondition() {
                @Override
                public boolean shouldKeepOnScreen() {
                    return keep;
                }
            });
            Handler handler = new Handler();
            handler.postDelayed(runner, DELAY);
            super.onCreate(savedInstanceState);
        } else{
            setTheme(R.style.Theme_AccessibleTunerApp); //else use old approach
            super.onCreate(savedInstanceState);
            start();
        }



    }

    private final Runnable runner = new Runnable() {
        @Override
        public void run() {
            keep = false;
            start();

        }
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

        //Redirect
        Navigation.redirect(Navigation.TUNER_ACTIVITY);
        Intent intent = new Intent(this, TunerActivity.class);
        this.startActivity(intent);

    }

    private void logIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null) return;

        Log.d(TAG, "======= logIntent ========= %s");
        Log.d(TAG, "Logging intent data start");

        for (String key : bundle.keySet()) {
            Log.d("YO", "[" + key + "=" + bundle.get(key) + "]");
            KEY = bundle.keySet().iterator().next();
            NAME = String.valueOf(bundle.get(KEY));
            Log.d("LOL", KEY);
            Log.d("LOL", NAME);
            break;
        }

        Log.d(TAG, "Logging intent data complete");
    }

}