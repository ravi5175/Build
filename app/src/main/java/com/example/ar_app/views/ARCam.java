package com.example.ar_app.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.ar_app.R;
import com.example.ar_app.databinding.ActivityArcamBinding;
import com.example.ar_app.databinding.ActivityInitBinding;
import com.example.ar_app.viewmodels.GlobalSharedViewModel;

public class ARCam extends AppCompatActivity {
    ActivityArcamBinding binding;

    GlobalSharedViewModel globalSharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArcamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        globalSharedViewModel = new ViewModelProvider(this).get(GlobalSharedViewModel.class);

        binding.arCamLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                globalSharedViewModel.getFirebaseAuth().signOut();
            }
        });


    }


}