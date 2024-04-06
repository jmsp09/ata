package com.unir.ata;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    //Constantes
    private static final int TUNER_ACTIVITY = 1;
    private static final int OPTIONS_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); //TODO
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> { //TODO
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Mantenemos la pantalla encendida
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Inicializar settings
        //TODO

        //Inicializar toolbar, botones, eventos
        //Toolbar toolbar = this.findViewById(R.id.toolbar);
        //toolbar.setTitle("");//TODO


        //Mostrar un loading
        //TODO

        //Redirect
        //if settings están configuradas
        redirect(TUNER_ACTIVITY);
        //else
        //Mostrar options


    }

    private void redirect(int activity) {

        if (activity == TUNER_ACTIVITY) {
            Intent intent = new Intent(this, TunerActivity.class);
            startActivity(intent);
        } else if (activity == OPTIONS_ACTIVITY) {
            //TODO
        }
    }

}