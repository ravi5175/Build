package com.example.ar_app.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.ar_app.databinding.ActivityMainBinding;
import com.example.ar_app.viewmodels.activities.MainViewModel;
import com.example.ar_app.views.fragments.ARCam;
import com.example.ar_app.views.fragments.Gallery;
import com.example.ar_app.views.fragments.PhotoViewer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 *  Main Activity
 *  handles ArCam , Gallery and PhotoViewer Fragments
 */
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding     binding;
    MainViewModel           mainViewModel;

    FirebaseAuth            FAuth;

    FragmentManager         fragmentManager;
    ARCam                   fARCam;
    Gallery                 fGallery;
    PhotoViewer             fPhotoViewer;
    Fragment                activeFragment;

    DatabaseReference       databaseRef;
    StorageReference        storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //ViewModels Initialization
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.setMainActivityContext(this);

        //Fragment Manager Initialization
        fragmentManager = getSupportFragmentManager();

        //Fragments Initialization
        fARCam   = new ARCam();
        fGallery = new Gallery();
        fPhotoViewer = new PhotoViewer();

        activeFragment = fARCam;

        fragmentManager.beginTransaction().add(binding.mainActivityFrame.getId(),fARCam,null).show(fARCam).commit();
        fragmentManager.beginTransaction().add(binding.mainActivityFrame.getId(),fGallery,null).hide(fGallery).commit();
        fragmentManager.beginTransaction().add(binding.mainActivityFrame.getId(),fPhotoViewer,null).hide(fPhotoViewer).commit();

        FAuth = FirebaseAuth.getInstance();

        databaseRef = FirebaseDatabase.getInstance("https://ar-app-11eb0-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(FAuth.getUid()+"Image");
        storageRef = FirebaseStorage.getInstance().getReference();

        //ViewModel Variables Initialization
        mainViewModel.setfAuth(FAuth);
        mainViewModel.setDatabase(databaseRef);
        mainViewModel.setStorage(storageRef);

        transaction_to_ARCam();

    }

    @Override
    public void onBackPressed() {
        if(activeFragment == fARCam){
            super.onBackPressed();
        }else if(activeFragment == fPhotoViewer){
            transaction_to_Gallery();
        }else{
            transaction_to_ARCam();
        }
    }

    /**
     *  Fragment Transaction to ARCam Fragment
     */
    public void transaction_to_ARCam(){
        fragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(fARCam)
                .commit();
        activeFragment = fARCam;
    }

    /**
     *  Fragment Transaction to Gallery Fragment
     */
    public void transaction_to_Gallery(){
        fragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(fGallery)
                .commit();
        activeFragment = fGallery;
    }

    /**
     *  Fragment Transaction to PhotoViewer Fragment
     */
    public void transaction_to_photo_viewer(){
        fragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(fPhotoViewer)
                .commit();
        activeFragment = fPhotoViewer;
    }

    /**
     *  Start Init Activity
     */
    public void InitActivityIntent(){
        Intent intent = new Intent(this,Init.class);
        startActivity(intent);
        finish();
    }
}