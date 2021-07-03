package com.example.ar_app.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ar_app.R;
import com.example.ar_app.databinding.FragmentOtpAuthBinding;
import com.example.ar_app.databinding.FragmentWelcomeScreenBinding;
import com.example.ar_app.viewmodels.GlobalSharedViewModel;
import com.example.ar_app.viewmodels.OTPAuthViewModel;
import com.example.ar_app.viewmodels.OTPSharedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;

import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class OTPAuth extends Fragment {

    FragmentOtpAuthBinding binding;

    OTPAuthViewModel otpAuthViewModel;
    OTPSharedViewModel otpSharedViewModel;
    GlobalSharedViewModel globalViewModel;

    String otpVerificationId;

    long timerDuration = TimeUnit.MINUTES.toMillis(1);

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        binding = FragmentOtpAuthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        otpAuthViewModel = new ViewModelProvider(requireActivity()).get(OTPAuthViewModel.class);
        otpSharedViewModel = new ViewModelProvider(requireActivity()).get(OTPSharedViewModel.class);
        globalViewModel = new ViewModelProvider(getActivity()).get(GlobalSharedViewModel.class);

        binding.displayPhoneNumber.setText(otpSharedViewModel.getPhoneNumber().getValue());

        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {
                 PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpVerificationId,otp);
                 signInWithPhoneAuthCredential(credential);
            }
        });

        binding.resendOtp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //globalViewModel.getFirebaseAuth().signOut();
                initiateOTP(otpSharedViewModel.getPhoneNumber().getValue());
                Toast.makeText(requireContext(), "successfully signed out", Toast.LENGTH_SHORT).show();
            }
        });

        binding.backToWelcomeScreen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                globalViewModel.getInitContext().transaction_to_Welcome();
            }
        });

        otpAuthViewModel.countDown.observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged(Object o) {
                binding.otpTimer.setText(otpAuthViewModel.countDown.getValue());
            }
        });

        if(!otpAuthViewModel.isTimerRunning.getValue()){
            initiateOTP(otpSharedViewModel.getPhoneNumber().getValue());
        }else{
            binding.otpTimerLayout.setVisibility(View.VISIBLE);
        }
    }


    private void initiateOTP(String phoneNumber){
        binding.otpStatus.setVisibility(View.VISIBLE);
        otpStatus("sending OTP");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(globalViewModel.getFirebaseAuth())
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(globalViewModel.getInitContext())                 // Activity (for callback binding)
                        .setCallbacks(callbacks())          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks(){
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                otpStatus("OTP verification completed");
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                //Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    otpStatus("OTP verification failed\n"+e.getLocalizedMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    otpStatus("OTP verification failed\n"+e.getLocalizedMessage());
                }
                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                otpStatus("OTP sent");
                binding.otpTimerLayout.setVisibility(View.VISIBLE);
                otpAuthViewModel.timerStart();
                otpVerificationId = verificationId;
                //mResendToken = token;
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        globalViewModel.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            otpStatus("Logging In...");
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.d(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                otpStatus(task.getException().getLocalizedMessage());
                            }
                        }
                    }
                });
    }

    public void otpStatus(String status){
        binding.otpStatus.setText(status);
    }
}