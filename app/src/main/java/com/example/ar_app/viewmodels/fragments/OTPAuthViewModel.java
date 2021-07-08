package com.example.ar_app.viewmodels.fragments;

import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OTPAuthViewModel extends ViewModel {
    public static OTPAuthViewModel instance;

    public String verificationId = "";

    CountDownTimer timer;

    public MutableLiveData<String> countDown = new MutableLiveData<String>();
    public MutableLiveData<Boolean> isTimerRunning = new MutableLiveData<Boolean>(false);
    //MutableLiveData<Long> timerDuration = new MutableLiveData<Long>();


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
            }
        };
        return tm;
    }

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
