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

    public void WelcomeScreen(Init context){}

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
                    Toast.makeText(getContext(),"please enter phone number",Toast.LENGTH_SHORT).show();
                }else if (phoneNumber.length() != 10){
                    Toast.makeText(getContext(),"phone number invalid",Toast.LENGTH_SHORT).show();
                }else{

                }
            }
        });
    }
}