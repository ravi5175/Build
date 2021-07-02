package com.example.ar_app.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ar_app.databinding.FragmentWelcomeScreenBinding;


public class WelcomeScreen extends Fragment {
    private FragmentWelcomeScreenBinding binding;
    private Init initContext;

    public static WelcomeScreen getInstance(Init context){
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        welcomeScreen.initContext = context;
        return welcomeScreen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.authenticate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.phoneNumber.getText().toString();
                if(phoneNumber.length() == 0){
                    generateToast("Please fill your phone number");
                }else if (phoneNumber.length() != 10){
                    generateToast("Invalid Phone Number");
                }else{
                    initContext.transaction_to_Auth();
                }
            }
        });
    }

    private void generateToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}