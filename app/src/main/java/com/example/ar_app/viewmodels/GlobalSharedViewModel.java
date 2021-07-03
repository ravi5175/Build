package com.example.ar_app.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.ar_app.views.Init;
import com.google.firebase.auth.FirebaseAuth;

public class GlobalSharedViewModel extends ViewModel {
    FirebaseAuth fAuth;
    Init initContext;

    public void generateFirebaseAuth(FirebaseAuth auth){
        fAuth = auth;
    }

    public FirebaseAuth getFirebaseAuth(){
        return fAuth;
    }

    public void InitActivityContext(Init context){
        initContext = context;
    }

    public Init getInitContext(){
        return initContext;
    }

}
