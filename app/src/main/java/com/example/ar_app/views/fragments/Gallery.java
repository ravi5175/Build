package com.example.ar_app.views.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ar_app.R;
import com.example.ar_app.adapters.GalleryRecyclerAdapter;
import com.example.ar_app.databinding.FragmentGalleryBinding;
import com.example.ar_app.models.ImageDownloadUrl;
import com.example.ar_app.viewmodels.activities.MainViewModel;
import com.example.ar_app.viewmodels.fragments.GalleryViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Gallery Fragment
 * - Serves as a Photo List
 */
public class Gallery extends Fragment {

    FragmentGalleryBinding      binding;

    GalleryViewModel            galleryViewModel;
    MainViewModel               mainViewModel;

    GridLayoutManager           galleryLayoutManager;
    GalleryRecyclerAdapter      galleryAdapter;

    ArrayList<ImageDownloadUrl> imageUrlList = new ArrayList<ImageDownloadUrl>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ViewModels Initialization
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        //Gallery Recycler View
        galleryLayoutManager = new GridLayoutManager(requireContext(),4);
        binding.galleryRecyclerView.setLayoutManager(galleryLayoutManager);

        galleryAdapter = new GalleryRecyclerAdapter(imageUrlList,mainViewModel);
        binding.galleryRecyclerView.setAdapter(galleryAdapter);

        //Firebase Realtime Database Image Url Observer
        mainViewModel.getDatabase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                imageUrlList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ImageDownloadUrl imageUrl = dataSnapshot.getValue(ImageDownloadUrl.class);
                    assert imageUrl != null;
                    Log.d("GALLERY-image-url",imageUrl.imageUrl);
                    imageUrlList.add(imageUrl);
                }
                galleryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("GALLERY-database-error",error.getMessage());
            }
        });

        binding.backToCam.setOnClickListener(v ->
                mainViewModel.getMainActivityContext().transaction_to_ARCam());
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        //super.onMultiWindowModeChanged(isInMultiWindowMode);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(),R.color.teal_700));
    }
}