package com.unir.ata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ViewFlipper ;

import androidx.annotation.NonNull;

import java.util.Objects;

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
    private static int currentView = TUNER_ACTIVITY;

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
                homeButton.setOnClickListener(v -> {
                    AudioMessage.getInstance(activity)
                            .playMessage((String) Objects.requireNonNull(homeButton.getTooltipText()),
                                    AudioMessage.AM_VIBRATION_INFO);

                });

                homeButton.setOnLongClickListener(v -> {
                    Log.d("!!&&","!!&&"+ (String) homeButton.getTooltipText());
                    AudioMessage.getInstance(activity)
                            .playMessage((String) homeButton.getContentDescription(),
                                    AudioMessage.AM_VIBRATION_CONFIRM);
                    Navigation.redirect(TUNER_ACTIVITY);
                    return true;
                });
            }

            if (infoButton != null) {
                infoButton.setOnClickListener(v -> {
                    AudioMessage.getInstance(activity)
                            .playMessage((String) Objects.requireNonNull(infoButton.getTooltipText()),
                                    AudioMessage.AM_VIBRATION_INFO);

                });

                infoButton.setOnLongClickListener(v -> {
                    AudioMessage.getInstance(activity)
                            .playMessage((String) infoButton.getContentDescription(),
                                    AudioMessage.AM_VIBRATION_CONFIRM);
                    Navigation.redirect(INFO_ACTIVITY);
                    return true;
                });
            }


            if (optionsButton != null) {
                optionsButton.setOnClickListener(v -> {
                    AudioMessage.getInstance(activity)
                            .playMessage((String) Objects.requireNonNull(optionsButton.getTooltipText()),
                                    AudioMessage.AM_VIBRATION_INFO);

                });

                optionsButton.setOnLongClickListener(v -> {
                    AudioMessage.getInstance(activity)
                            .playMessage((String) optionsButton.getContentDescription(),
                                    AudioMessage.AM_VIBRATION_CONFIRM);
                    Navigation.redirect(OPTIONS_ACTIVITY);
                    return true;
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
            }

            if (Navigation.getActivity().getClass() != class2Redirect) {
                Intent intent = new Intent(getActivity(), class2Redirect);
                getActivity().startActivity(intent);
            }
        } else {
            viewFlipper.setDisplayedChild(fragment);
            activity.initFragment(fragment);
        }

        if (currentView == TUNER_ACTIVITY && currentView != fragment) {
            activity.stopTuner();
        }

        currentView = fragment;

    }


}
