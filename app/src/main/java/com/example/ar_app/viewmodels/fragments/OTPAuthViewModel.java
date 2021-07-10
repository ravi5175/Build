package com.example.ar_app.viewmodels.fragments;

import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * OTPAuth Fragment View Model Class
 */
public class OTPAuthViewModel extends ViewModel {

    public String verificationId = "";
    public CountDownTimer timer;

    public MutableLiveData<String> countDown = new MutableLiveData<String>();
    public MutableLiveData<Boolean> isTimerRunning = new MutableLiveData<Boolean>(false);
    public MutableLiveData<Boolean> resendOTPAllowed = new MutableLiveData<Boolean>(false);

    /**
     * function countDownTimer
     * - handles timer for OTP expiry
     * @return
     */
    public CountDownTimer countDownTimer(){
        CountDownTimer tm = new CountDownTimer(TimeUnit.MINUTES.toMillis(1),1000){
            @Override
            public void onTick(long millisUntilFinished) {
               countDown.setValue( String.format(Locale.ENGLISH,"%02d",
            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                -TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished
            ))));
            }
            @Override
            public void onFinish() {
                isTimerRunning.setValue(false);
                resendOTPAllowed.setValue(true);
            }
        };
        return tm;
    }

    /**
     * function timerStart
     * - starts OTP Expiry timer
     */
    public void timerStart(){
        isTimerRunning.setValue(true);
        if(timer==null){
            timer = countDownTimer();
            timer.start();
            return;
        }
        timer.start();
    }
}
