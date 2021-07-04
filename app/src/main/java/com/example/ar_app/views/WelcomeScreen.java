package com.example.ar_app.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ar_app.databinding.FragmentWelcomeScreenBinding;
import com.example.ar_app.viewmodels.InitViewModel;


public class WelcomeScreen extends Fragment {
    private FragmentWelcomeScreenBinding binding;

    private InitViewModel initViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false);
        binding.countryCodePicker.registerCarrierNumberEditText(binding.phoneNumber);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel = new ViewModelProvider(requireActivity()).get(InitViewModel.class);

        binding.authenticate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.phoneNumber.getText().toString();
                if(phoneNumber.length() == 0){
                    generateToast("Please fill your phone number");
                }else if (phoneNumber.length() != 10){
                    generateToast("Invalid Phone Number");
                }else{
                    initViewModel.setPhoneNumber(binding.countryCodePicker.getFullNumberWithPlus().replace(" ",""));
                    initViewModel.getInitContext().transaction_to_Auth();
                }
            }
        });
    }

    private void generateToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}