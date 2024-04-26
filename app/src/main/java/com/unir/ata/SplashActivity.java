package com.unir.ata;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    //Constantes
    private static final long SPLASH_DELAY = 1500;
    private static final long PULSE_DURATION = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Iniciamos la animación
        startAnimation();

        // Retardo para finalizar la actividad y redirigir al main
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Finish the activity
                finish();

                // Start the next activity, for example, MainActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, SPLASH_DELAY);


    }

    private void startAnimation() {
        ImageView splashImage = findViewById(R.id.splashImage);

        // Animación para ampliar
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(splashImage, "scaleX", 0.5f, 1.0f);
        scaleUpX.setDuration(PULSE_DURATION);
        scaleUpX.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(splashImage, "scaleY", 0.5f, 1.0f);
        scaleUpY.setDuration(PULSE_DURATION);
        scaleUpY.setInterpolator(new AccelerateInterpolator());

        // Animación para reducir
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(splashImage, "scaleX", 1.0f, 0.5f);
        scaleDownX.setDuration(PULSE_DURATION);
        scaleDownX.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(splashImage, "scaleY", 1.0f, 0.5f);
        scaleDownY.setDuration(PULSE_DURATION);
        scaleDownY.setInterpolator(new DecelerateInterpolator());

        // Creamos una set de animaciones de ampliar y reducir
        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(scaleUpX, scaleUpY);

        AnimatorSet scaleUp2 = new AnimatorSet();
        scaleUp2.playTogether(scaleUpX, scaleUpY);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(scaleDownX, scaleDownY);

        // Create the pulse animation by playing the scale up followed by the scale down animation
        AnimatorSet pulse = new AnimatorSet();
        pulse.playSequentially(scaleUp, scaleDown, scaleUp2);

        pulse.start();

    }
}

