package com.example.ar_app.viewmodels.activities;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_app.views.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Main Activity View Model Class
 */

@SuppressWarnings("staticFieldLeak")
public class MainViewModel extends ViewModel {

    MainActivity mainActivityContext = null; //Main Actitvity Context Variable
    FirebaseAuth fAuth = null ;              //Firebase Auth Variable
    DatabaseReference database = null;       //Firebase Database Reference Variable
    StorageReference storage = null;         //Firebase Storage Referance Variable

    public MutableLiveData<String> photoUrl = new MutableLiveData<String>(null);

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

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl.setValue(photoUrl);
    }

    public String getPhotoUrl(){
        return photoUrl.getValue();
    }

    public void setStorage(StorageReference storage){
        this.storage = storage;
    }

    public StorageReference getStorage(){
        return storage;
    }

    public void setDatabase(DatabaseReference database){
        this.database = database;
    }

    public DatabaseReference getDatabase(){
        return database;
    }
}
