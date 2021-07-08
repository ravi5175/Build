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

public class Init extends AppCompatActivity {

    ActivityInitBinding binding;

    FragmentManager fragmentManager;
    WelcomeScreen fWelcomeScreen;
    OTPAuth fOTPAuth;
    ARCam fARCam;
    Gallery fGallery;

    InitViewModel initViewModel;

    FirebaseAuth FAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RunPermissionCheck();

        initViewModel = new ViewModelProvider(this).get(InitViewModel.class);
        FAuth = FirebaseAuth.getInstance();

        initViewModel.setFirebaseAuth(FAuth);
        initViewModel.setInitContext(this);

        fragmentManager = getSupportFragmentManager();

        fARCam = new ARCam();
        fWelcomeScreen = new WelcomeScreen();
        fOTPAuth = new OTPAuth();
        fGallery = new Gallery();

        if(FAuth.getCurrentUser()!=null){
            Log.d("user","user found");
            MainActivityIntent();
        }else{
            transaction_to_Welcome();
        }
    }

    public void transaction_to_Auth(){
        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(), fOTPAuth, null)
                .addToBackStack("fAuth")
                .setReorderingAllowed(true)
                .commit();
    }

    public void transaction_to_Welcome(){
        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(), fWelcomeScreen, null)
                //.addToBackStack("fWelcome")
                .setReorderingAllowed(true)
                .commit();
    }

    public void MainActivityIntent(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

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