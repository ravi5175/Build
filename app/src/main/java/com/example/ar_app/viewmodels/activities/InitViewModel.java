package com.example.ar_app.viewmodels.activities;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_app.views.activities.Init;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Init Activity View Model Class
 */

@SuppressWarnings("staticFieldLeak")
public class InitViewModel extends ViewModel {

    FirebaseAuth fAuth = null;  //Firebase Auth Variable
    Init initContext = null;    //Init Activity Context

    MutableLiveData<String> phoneNumber = new MutableLiveData<String>(); //Phone Number String

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
