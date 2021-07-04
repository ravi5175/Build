package com.example.ar_app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_app.views.Init;
import com.google.firebase.auth.FirebaseAuth;

public class InitViewModel extends ViewModel {

    InitViewModel instance;
    FirebaseAuth fAuth;
    Init initContext;

    MutableLiveData<String> phoneNumber = new MutableLiveData<String>();

    public void setInitContext(Init context){
        initContext = context;
    }
    public Init getInitContext(){
        return initContext;
    }

    public void setFirebaseAuth(FirebaseAuth auth){
        fAuth = auth;
    }
    public FirebaseAuth getFirebaseAuth(){
        return fAuth;
    }

    public void setPhoneNumber(String phone){
        phoneNumber.setValue(phone);
    }
    public MutableLiveData<String> getPhoneNumber(){
        return phoneNumber;
    }

}
