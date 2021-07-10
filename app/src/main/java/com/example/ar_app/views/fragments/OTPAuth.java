package com.example.ar_app.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ar_app.databinding.FragmentOtpAuthBinding;
import com.example.ar_app.viewmodels.activities.InitViewModel;
import com.example.ar_app.viewmodels.fragments.OTPAuthViewModel;
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

import java.util.concurrent.TimeUnit;

/**
 * OTPAuth Fragment
 * - user authentication handler
 */

public class OTPAuth extends Fragment {

    FragmentOtpAuthBinding binding;

    OTPAuthViewModel otpAuthViewModel;
    InitViewModel initViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        binding = FragmentOtpAuthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ViewModel Initialization
        otpAuthViewModel = new ViewModelProvider(requireActivity()).get(OTPAuthViewModel.class);
        initViewModel = new ViewModelProvider(requireActivity()).get(InitViewModel.class);

        binding.displayPhoneNumber.setText(initViewModel.getPhoneNumber().getValue());

        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {
                 PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpAuthViewModel.verificationId,otp);
                 signInWithPhoneAuthCredential(credential);
            }
        });

        binding.resendOtp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                initiateOTP(initViewModel.getPhoneNumber().getValue());
                Toast.makeText(requireContext(), "successfully signed out", Toast.LENGTH_SHORT).show();
            }
        });

        binding.backToWelcomeScreen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                initViewModel.getInitContext().transaction_to_Welcome();
            }
        });

        otpAuthViewModel.countDown.observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged(Object o) {
                binding.otpTimer.setText(otpAuthViewModel.countDown.getValue());
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
     * @return
     */
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks(){
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                otpStatus("OTP verification completed");
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
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
                //mResendToken = token;
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
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            otpStatus("Logging In...");
                            initViewModel.getInitContext().MainActivityIntent();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                otpStatus(task.getException().getLocalizedMessage());
                            }
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