package com.example.ar_app.views.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.ar_app.databinding.ActivityInitBinding;
import com.example.ar_app.viewmodels.activities.InitViewModel;
import com.example.ar_app.views.fragments.ARCam;
import com.example.ar_app.views.fragments.Gallery;
import com.example.ar_app.views.fragments.OTPAuth;
import com.example.ar_app.views.fragments.WelcomeScreen;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Launcher Acitivity
 *  handles User Authentication
 */
public class Init extends AppCompatActivity {

    ActivityInitBinding binding;

    FragmentManager     fragmentManager;
    WelcomeScreen       fWelcomeScreen;
    OTPAuth             fOTPAuth;
    ARCam               fARCam;
    Gallery             fGallery;

    InitViewModel       initViewModel;

    FirebaseAuth        FAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Permission Check
        RunPermissionCheck();

        //View Model Initializing
        initViewModel = new ViewModelProvider(this).get(InitViewModel.class);

        //Firebase Auth Initializing
        FAuth = FirebaseAuth.getInstance();

        //View Model Variables Update
        initViewModel.setFirebaseAuth(FAuth);
        initViewModel.setInitContext(this);

        //Fragment Manager Initialising
        fragmentManager = getSupportFragmentManager();

        //Fragments Initialising
        fARCam          = new ARCam();
        fWelcomeScreen  = new WelcomeScreen();
        fOTPAuth        = new OTPAuth();
        fGallery        = new Gallery();

        if(FAuth.getCurrentUser()!=null){
            Log.d("INIT-USER","user found");
            MainActivityIntent();
        }else{
            Log.d("INIT-USER","user not found");
            transaction_to_Welcome();
        }
    }

    /**
     *  Fragment Transaction to OTP Auth fragment
     */
    public void transaction_to_Auth(){
        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(), fOTPAuth, null)
                .addToBackStack("fAuth")
                .setReorderingAllowed(true)
                .commit();
    }
    /**
     *  Fragment Transaction to Welcome fragment
     */
    public void transaction_to_Welcome(){
        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(), fWelcomeScreen, null)
                .setReorderingAllowed(true)
                .commit();
    }

    /**
     *  Starts Main Activity
     */
    public void MainActivityIntent(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     *  Request Permissions from User for the First Time
     *  Permissions - Internet
     *              - Storage[Read/Write]
     *              - Camera
     */
    private void RunPermissionCheck() {
        String[] Permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        ActivityCompat.requestPermissions(this,Permissions , 1);
    }
}