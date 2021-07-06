package com.example.ar_app.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ar_app.R;
import com.example.ar_app.databinding.FragmentArCamBinding;
import com.example.ar_app.databinding.FragmentGalleryBinding;


public class Gallery extends Fragment {

    FragmentGalleryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}