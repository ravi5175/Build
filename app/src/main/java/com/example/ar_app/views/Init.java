package com.example.ar_app.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.ar_app.databinding.ActivityInitBinding;
import com.example.ar_app.viewmodels.GlobalSharedViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class Init extends AppCompatActivity {

    ActivityInitBinding binding;

    FragmentManager fragmentManager = getSupportFragmentManager();
    WelcomeScreen fWelcomeScreen;
    OTPAuth fOTPAuth;

    GlobalSharedViewModel globalViewModel;

    FirebaseAuth FAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        globalViewModel = new ViewModelProvider(this).get(GlobalSharedViewModel.class);
        FAuth = FirebaseAuth.getInstance();

        globalViewModel.generateFirebaseAuth(FAuth);
        globalViewModel.InitActivityContext(this);

        if(FAuth.getCurrentUser()!=null){
            intentARcam();
        }

        //fragmentManager = getSupportFragmentManager();
        fWelcomeScreen = WelcomeScreen.getInstance(this);
        fOTPAuth = new OTPAuth();

        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(),fWelcomeScreen, null)
                .setReorderingAllowed(true)
                .addToBackStack("fWelcome")
                .commit();
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

    public void intentARcam(){
        startActivity(new Intent(this,ARCam.class));
        finish();
    }





}