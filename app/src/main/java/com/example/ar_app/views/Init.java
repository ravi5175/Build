package com.example.ar_app.views;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.ar_app.databinding.ActivityInitBinding;

public class Init extends AppCompatActivity {

    private ActivityInitBinding binding;
    private FragmentManager fragmentManager;
    private WelcomeScreen fWelcomeScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();
        fWelcomeScreen = new WelcomeScreen(this);
        fragmentManager.beginTransaction()
                .replace(binding.initFrameLayout.getId(),fWelcomeScreen, null)
                .setReorderingAllowed(true)
                .addToBackStack("welcome")
                .commit();


    }






}