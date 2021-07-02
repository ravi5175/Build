package com.example.ar_app.views;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.ar_app.databinding.ActivityInitBinding;

public class Init extends AppCompatActivity {

    ActivityInitBinding binding;
    FragmentManager fragmentManager = getSupportFragmentManager();
    WelcomeScreen fWelcomeScreen;
    BlankFragment fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //fragmentManager = getSupportFragmentManager();
        fWelcomeScreen = WelcomeScreen.getInstance(this);
        fAuth = new BlankFragment();

        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(),fWelcomeScreen, null)
                .setReorderingAllowed(true)
                .addToBackStack("fwelcome")
                .commit();
    }

    public void transaction_to_Auth(){
        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(), fAuth, null)
                .setReorderingAllowed(true)
                .addToBackStack("fauth")
                .commit();
    }





}