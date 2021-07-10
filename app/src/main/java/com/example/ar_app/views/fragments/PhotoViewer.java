package com.example.ar_app.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.ar_app.R;
import com.example.ar_app.databinding.FragmentGalleryBinding;
import com.example.ar_app.databinding.FragmentPhotoViewerBinding;
import com.example.ar_app.viewmodels.activities.MainViewModel;

/**
 *  PhotoViewer Fragment
 *  - Serves as a Full Screen PhotoViewer for Gallery Fragment
 */
public class PhotoViewer extends Fragment {

    FragmentPhotoViewerBinding binding;
    MainViewModel mainViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPhotoViewerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //ViewModel Initialization
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        mainViewModel.photoUrl.observe(getViewLifecycleOwner(),
                s -> Glide.with(binding.photoView)
                        .asBitmap()
                        .load(mainViewModel.getPhotoUrl())
                        .into(binding.photoView));
    }
}