package com.example.ar_app.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ar_app.adapters.GalleryRecyclerAdapter;
import com.example.ar_app.databinding.FragmentGalleryBinding;
import com.example.ar_app.models.ImageDownloadUrl;
import com.example.ar_app.viewmodels.fragments.GalleryViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Gallery extends Fragment {

    FragmentGalleryBinding binding;

    DatabaseReference database = FirebaseDatabase.getInstance("https://ar-app-11eb0-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Image");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    ArrayList<ImageDownloadUrl> imageUrlList = new ArrayList<ImageDownloadUrl>();
    private GalleryViewModel galleryViewModel;

    GridLayoutManager galleryLayoutManager;
    GalleryRecyclerAdapter galleryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        galleryLayoutManager = new GridLayoutManager(requireContext(),4);
        binding.galleryRecyclerView.setLayoutManager(galleryLayoutManager);

        galleryAdapter = new GalleryRecyclerAdapter(imageUrlList);
        binding.galleryRecyclerView.setAdapter(galleryAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ImageDownloadUrl imageUrl = dataSnapshot.getValue(ImageDownloadUrl.class);
                    Log.d("image_url",imageUrl.imageUrl);
                    imageUrlList.add(imageUrl);
                }
                galleryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        /*galleryViewModel.imageDownloadUrlList.observe(getViewLifecycleOwner(), new Observer(){
            @Override
            public void onChanged(Object o) {
                galleryAdapter.notifyDataSetChanged();
                Log.d("image_url_list_size",String.valueOf(galleryViewModel.imageDownloadUrlList.getValue().size()));
            }
        });*/
    }
}