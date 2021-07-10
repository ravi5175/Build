package com.example.ar_app.views.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ar_app.databinding.FragmentOtpAuthBinding;
import com.example.ar_app.viewmodels.activities.InitViewModel;
import com.example.ar_app.viewmodels.fragments.OTPAuthViewModel;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * OTPAuth Fragment
 * - user authentication handler
 */

@SuppressWarnings("ConstantConditions")
public class OTPAuth extends Fragment {

    FragmentOtpAuthBinding binding;

    OTPAuthViewModel otpAuthViewModel;
    InitViewModel initViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        binding = FragmentOtpAuthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ViewModel Initialization
        otpAuthViewModel = new ViewModelProvider(requireActivity()).get(OTPAuthViewModel.class);
        initViewModel = new ViewModelProvider(requireActivity()).get(InitViewModel.class);

        binding.displayPhoneNumber.setText(initViewModel.getPhoneNumber().getValue());

        binding.otpView.setOtpCompletionListener(otp -> {
             try{
                 PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpAuthViewModel.verificationId,otp);
                 signInWithPhoneAuthCredential(credential);
             }catch(Exception e){
                 otpStatus(e.getMessage());
             }

        });

        binding.resendOtpButton.setOnClickListener(v -> {
            initiateOTP(initViewModel.getPhoneNumber().getValue());
            otpAuthViewModel.resendOTPAllowed.setValue(false);
        });

        binding.backToWelcomeScreen.setOnClickListener(v ->
                initViewModel.getInitContext().transaction_to_Welcome());

        otpAuthViewModel.countDown.observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged(Object o) {
                binding.otpTimer.setText(otpAuthViewModel.countDown.getValue());
            }
        });

        otpAuthViewModel.resendOTPAllowed.observe(getViewLifecycleOwner(), change -> {
            if(change){
                binding.resendOtpButton.setEnabled(change);
                binding.resendOtpButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F6D779")));
                binding.resendOtpButton.setTextColor(Color.parseColor("#646464"));
            }else{
                binding.resendOtpButton.setEnabled(change);
                binding.resendOtpButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#646464")));
                binding.resendOtpButton.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        if(!otpAuthViewModel.isTimerRunning.getValue()){
            initiateOTP(initViewModel.getPhoneNumber().getValue());
        }else{
            binding.otpTimerLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * initiateOTP
     * - request for otp verification
     * @param phoneNumber user phone number
     */
    private void initiateOTP(String phoneNumber){
        binding.otpStatus.setVisibility(View.VISIBLE);
        otpStatus("sending OTP");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(initViewModel.getFirebaseAuth())
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(initViewModel.getInitContext())
                        .setCallbacks(callbacks())
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    /**
     * Verification callbacks for PhoneAuthProvider
     */
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks(){
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                otpStatus("OTP verification completed");
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    otpStatus("OTP verification failed\n"+e.getLocalizedMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    otpStatus("OTP verification failed\n"+e.getLocalizedMessage());
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                otpStatus("OTP sent");
                binding.otpTimerLayout.setVisibility(View.VISIBLE);
                otpAuthViewModel.timerStart();
                otpAuthViewModel.verificationId = verificationId;
            }
        };
    }

    /**
     * signInWithPhoneAuthCredential
     * - in case phone number provided, not belongs to active device
     *   authentication will be called manually.
     * @param credential PhoneAuthCredential
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        initViewModel.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        otpStatus("Logging In...");
                        initViewModel.getInitContext().MainActivityIntent();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            otpStatus(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    /**
     * updates authentication status on OTPAuthFragment UI
     * @param status OTPAuthentication Status message
     */
    public void otpStatus(String status){
        binding.otpStatus.setText(status);
    }
}