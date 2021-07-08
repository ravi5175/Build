package com.example.ar_app.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.ar_app.R;
import com.example.ar_app.databinding.ActivityInitBinding;
import com.example.ar_app.databinding.ActivityMainBinding;
import com.example.ar_app.viewmodels.activities.MainViewModel;
import com.example.ar_app.views.fragments.ARCam;
import com.example.ar_app.views.fragments.Gallery;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainViewModel mainViewModel;

    FirebaseAuth FAuth;

    FragmentManager fragmentManager;
    ARCam fARCam;
    Gallery fGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setMainActivityContext(this);

        fragmentManager = getSupportFragmentManager();

        fARCam   = new ARCam();
        fGallery = new Gallery();

        FAuth = FirebaseAuth.getInstance();
        mainViewModel.setfAuth(FAuth);

        transaction_to_ARCam();

    }

    public void transaction_to_ARCam(){
        fragmentManager.beginTransaction()
                .replace(binding.mainActivityFrame.getId(), fARCam, null)
                //.addToBackStack("fARCam")
                .setReorderingAllowed(true)
                .commit();
    }

    public void transaction_to_Gallery(){
        fragmentManager.beginTransaction()
                .replace(binding.mainActivityFrame.getId(), fGallery, null)
                .addToBackStack("fGallery")
                .setReorderingAllowed(true)
                .commit();
    }

    public void InitActivityIntent(){
        Intent intent = new Intent(this,Init.class);
        startActivity(intent);
        finish();
    }
}