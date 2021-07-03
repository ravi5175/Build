package com.example.ar_app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OTPSharedViewModel extends ViewModel {
    MutableLiveData<String> phoneNumber = new MutableLiveData<String>();

    public void setPhoneNumber(String phone){
        phoneNumber.setValue(phone);
    }

    public MutableLiveData<String> getPhoneNumber(){
        return phoneNumber;
    }

}
