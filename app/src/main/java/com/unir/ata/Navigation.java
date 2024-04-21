package com.unir.ata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper ;

import androidx.annotation.NonNull;

public class Navigation {

    @SuppressLint("StaticFieldLeak")
    private static Navigation navigation;

    @SuppressLint("StaticFieldLeak")
    private static TunerActivity activity;

    //Navegables
    protected static final int TUNER_ACTIVITY = 0;
    protected static final int INFO_ACTIVITY = 1;
    protected static final int OPTIONS_ACTIVITY = 2;
    private static ViewFlipper viewFlipper;

    //Elementos
    private View homeButton;
    private View infoButton;
    private View optionsButton;

    private Navigation() {
    }

    protected static Navigation getInstance(@NonNull TunerActivity activity){
        if (navigation == null) {
            navigation = new Navigation();
        }
        Navigation.activity = activity;
        viewFlipper = activity.findViewById(R.id.viewFlipper);
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

                homeButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        AudioMessage.getInstance(activity)
                                .playMessage((String) homeButton.getContentDescription(),
                                        AudioMessage.AM_VIBRATION_TOUCH);
                        return false;
                    }
                });
            }

            if (infoButton != null) {
                infoButton.setOnClickListener(v -> Navigation.redirect(INFO_ACTIVITY));

                infoButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        AudioMessage.getInstance(activity)
                                .playMessage((String) infoButton.getContentDescription(),
                                        AudioMessage.AM_VIBRATION_TOUCH);
                        return false;
                    }
                });
            }


            if (optionsButton != null) {
                optionsButton.setOnClickListener(v -> {
                    Navigation.redirect(OPTIONS_ACTIVITY);

                });

                optionsButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        AudioMessage.getInstance(activity)
                                .playMessage((String) optionsButton.getContentDescription(),
                                        AudioMessage.AM_VIBRATION_TOUCH);
                        return false;
                    }
                });
            }

        }



    }


    public static TunerActivity getActivity() {
        return activity;
    }

    protected static void redirect(int fragment) {

        if (getActivity() == null) {
            //No se ha inicializado correctamente
            return;
        }
        if (viewFlipper == null) {
            Class<?> class2Redirect = TunerActivity.class;
            if (fragment == Navigation.TUNER_ACTIVITY) {
                class2Redirect = TunerActivity.class;
            } /*else if (fragment == Navigation.INFO_ACTIVITY) {
                class2Redirect = TunerActivity.class; //TODO
            } else if (fragment == Navigation.OPTIONS_ACTIVITY){
                class2Redirect = InstrumentsActivity.class; //TODO
            }*/

            if (Navigation.getActivity().getClass() != class2Redirect) {
                Intent intent = new Intent(getActivity(), class2Redirect);
                getActivity().startActivity(intent);
            }
        } else {
            viewFlipper.setDisplayedChild(fragment);
            activity.initFragment(fragment);

        }

    }
}
