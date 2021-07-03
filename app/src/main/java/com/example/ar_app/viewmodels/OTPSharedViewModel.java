package com.example.ar_app.viewmodels;

import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OTPSharedViewModel extends ViewModel {
    MutableLiveData<String> phoneNumber = new MutableLiveData<String>();
    MutableLiveData<String> timerTime = new MutableLiveData<String>();
    MutableLiveData<Boolean> isTimerRunning = new MutableLiveData<Boolean>();

    public static OTPSharedViewModel instance;

    public static synchronized OTPSharedViewModel getInstance(){
        if(instance == null){
            instance = new OTPSharedViewModel();
            return instance;
        }else{
            return instance;
        }
    }

    public void setPhoneNumber(String phone){
        phoneNumber.setValue(phone);
    }

    public MutableLiveData<String> getPhoneNumber(){
        return phoneNumber;
    }


}
