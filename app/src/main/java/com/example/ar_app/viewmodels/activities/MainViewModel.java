package com.example.ar_app.viewmodels.activities;

import androidx.lifecycle.ViewModel;

import com.example.ar_app.views.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainViewModel extends ViewModel {
    MainActivity mainActivityContext = null;
    FirebaseAuth fAuth = null ;

    public void setMainActivityContext(MainActivity context){
        this.mainActivityContext = context;
    }

    public MainActivity getMainActivityContext() {
        return mainActivityContext;
    }

    public void setfAuth(FirebaseAuth auth){
        this.fAuth = auth;
    }

    public FirebaseAuth getFAuth() {
        return fAuth;
    }
}
