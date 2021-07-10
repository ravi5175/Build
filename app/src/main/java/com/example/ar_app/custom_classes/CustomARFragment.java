package com.example.ar_app.custom_classes;

import android.view.WindowManager;

import com.google.ar.sceneform.ux.ArFragment;

/**
 * Custom AR Fragment Class
 * - overrides window focus changed for ARCam Fragment
 */
public class CustomARFragment extends ArFragment {
    @Override
    protected void onWindowFocusChanged(boolean hasFocus) {
        //super.onWindowFocusChanged(hasFocus);
    }
}
