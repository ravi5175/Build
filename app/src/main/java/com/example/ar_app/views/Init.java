package com.example.ar_app.views;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.ar_app.databinding.ActivityInitBinding;
import com.example.ar_app.viewmodels.InitViewModel;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

public class Init extends AppCompatActivity {

    ActivityInitBinding binding;

    FragmentManager fragmentManager;
    WelcomeScreen fWelcomeScreen;
    OTPAuth fOTPAuth;
    ARCam fARCam;

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
        if(FAuth.getCurrentUser()!=null){
            Log.d("user","user found");
            transaction_to_ARCam();
        }else{
            transaction_to_Welcome();
        }
    }

    public void transaction_to_Auth(){
        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(), fOTPAuth, null)
                .setReorderingAllowed(true)
                .addToBackStack("fAuth")
                .commit();
    }

    public void transaction_to_Welcome(){
        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(), fWelcomeScreen, null)
                .setReorderingAllowed(true)
                .addToBackStack("fWelcome")
                .commit();
    }

    public void transaction_to_ARCam(){
        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(), fARCam, null)
                .setReorderingAllowed(true)
                //.addToBackStack("fARCam")
                .commit();
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