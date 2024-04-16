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

    //Navegacion
    private Navigation navigation;

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

        //Inicializamos navegacion
        //navigation = Navigation.getInstance(this);

        //Inicializar toolbar, botones, eventos
        //Toolbar toolbar = this.findViewById(R.id.toolbar);
        //toolbar.setTitle("");//TODO


        //Mostrar un loading
        //TODO

        //Redirect
        //if settings están configuradas
        Navigation.redirect(Navigation.TUNER_ACTIVITY);
        Intent intent = new Intent(this, TunerActivity.class);
        this.startActivity(intent);
        //else
        //Mostrar options


    }

}