package com.example.ar_app.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ar_app.databinding.FragmentWelcomeScreenBinding;
import com.example.ar_app.viewmodels.activities.InitViewModel;

import java.util.Objects;

/**
 * Welcome Screen Fragement
 * - serves as welcome screen for user
 * - take user's phone number as input
 */
public class WelcomeScreen extends Fragment {

    FragmentWelcomeScreenBinding binding;

    InitViewModel initViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false);
        binding.countryCodePicker.registerCarrierNumberEditText(binding.phoneNumber);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ViewModel Initialization
        initViewModel = new ViewModelProvider(requireActivity()).get(InitViewModel.class);

        binding.authenticate.setOnClickListener(v -> {
            PhoneNumberAcquire();
        });
    }

    /**
     *  Take user phone number input , validates phone number and start transaction to OTPAuth Fragment
     */
    private void PhoneNumberAcquire(){
        String phoneNumber = Objects.requireNonNull(binding.phoneNumber.getText()).toString();
        if(phoneNumber.length() == 0){
            generateToast("Please fill your phone number");
        }else if (phoneNumber.length() != 10){
            generateToast("Invalid Phone Number");
        }else{
            initViewModel.setPhoneNumber(binding.countryCodePicker.getFullNumberWithPlus().replace(" ",""));
            initViewModel.getInitContext().transaction_to_Auth();
        }
    }

    /**
     * generates toast messages on WelcomeScreen Fragment
     * @param message to be displayed over the screen
     */
    private void generateToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}