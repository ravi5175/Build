package com.example.ar_app.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_app.models.ImageDownloadUrl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GalleryViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ImageDownloadUrl>> imageDownloadUrlList = new MutableLiveData<ArrayList<ImageDownloadUrl>>(new ArrayList<ImageDownloadUrl>());
    DatabaseReference database = FirebaseDatabase.getInstance("https://ar-app-11eb0-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Image");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public GalleryViewModel(){
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ImageDownloadUrl imageUrl = dataSnapshot.getValue(ImageDownloadUrl.class);
                        Log.d("image_url",imageUrl.imageUrl);
                        imageDownloadUrlList.getValue().add(imageUrl);
                    }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
