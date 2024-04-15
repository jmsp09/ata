package com.unir.ata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class Navigation {

    @SuppressLint("StaticFieldLeak")
    private static Navigation navigation;

    @SuppressLint("StaticFieldLeak")
    private static Activity activity;

    //Navegables
    protected static final int TUNER_ACTIVITY = 1;
    protected static final int INFO_ACTIVITY = 2;
    protected static final int OPTIONS_ACTIVITY = 3;

    //Elementos
    View homeButton;
    View infoButton;
    View optionsButton;

    private Navigation() {
    }

    protected static Navigation getInstance(@NonNull Activity activity){
        if (navigation == null) {
            navigation = new Navigation();
        }
        Navigation.activity = activity;
        navigation.initNavigation();
        return navigation;
    }

    public void initNavigation() {

        View navigationLayout = Navigation.getActivity().findViewById(R.id.navigation);

        if (navigationLayout != null) {

            homeButton = navigationLayout.findViewById(R.id.homeButton);
            infoButton = navigationLayout.findViewById(R.id.infoButton);
            optionsButton = navigationLayout.findViewById(R.id.optionsButton);


            if (homeButton != null) {
                homeButton.setOnClickListener(v -> Navigation.redirect(TUNER_ACTIVITY));
            }
            if (infoButton != null) {
                infoButton.setOnClickListener(v -> Navigation.redirect(INFO_ACTIVITY));
            }

            if (optionsButton != null) {
                optionsButton.setOnClickListener(v -> {
                    Navigation.redirect(OPTIONS_ACTIVITY);

                });
            } else {
                Toast.makeText(Navigation.getActivity(), "optionsButton?? FALSE ", Toast.LENGTH_SHORT).show();

            }
        }



    }


    public static Activity getActivity() {
        return activity;
    }

    protected static void redirect(int activity) {

        if (getActivity() == null) {
            //No se ha inicializado correctamente
            return;
        }
        Class<?> class2Redirect = TunerActivity.class;
        if (activity == Navigation.TUNER_ACTIVITY) {
            class2Redirect = TunerActivity.class;
        } else if (activity == Navigation.INFO_ACTIVITY) {
            class2Redirect = TunerActivity.class; //TODO
        } else if (activity == Navigation.OPTIONS_ACTIVITY){
            class2Redirect = InstrumentsActivity.class; //TODO
        }

        if (Navigation.getActivity().getClass() != class2Redirect) {
            Intent intent = new Intent(getActivity(), class2Redirect);
            getActivity().startActivity(intent);
        }
    }
}
