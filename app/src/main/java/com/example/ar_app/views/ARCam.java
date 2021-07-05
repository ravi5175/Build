package com.example.ar_app.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.ar_app.R;
import com.example.ar_app.adapters.ARCamRecyclerAdapter;
import com.example.ar_app.databinding.FragmentArCamBinding;
import com.example.ar_app.databinding.FragmentOtpAuthBinding;
import com.example.ar_app.models.ARCamRecyclerChildModel;
import com.example.ar_app.viewmodels.ARCamViewModel;
import com.example.ar_app.viewmodels.InitViewModel;
import com.example.ar_app.viewmodels.OTPAuthViewModel;

import java.util.ArrayList;


public class ARCam extends Fragment {

    FragmentArCamBinding binding;
    ARCamViewModel arCamViewModel;
    InitViewModel initViewModel;

    LinearLayoutManager arCamRecyclerLayoutManager;
    ARCamRecyclerAdapter arCamRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArCamBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        arCamViewModel = new ViewModelProvider(this).get(ARCamViewModel.class);
        initViewModel = new ViewModelProvider(requireActivity()).get(InitViewModel.class);

        binding.arcamSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        arCamRecyclerAdapter = new ARCamRecyclerAdapter(arCamViewModel.recyclerViewData.getValue());
        arCamRecyclerLayoutManager = new LinearLayoutManager(initViewModel.getInitContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.arCamRecycler.setLayoutManager(arCamRecyclerLayoutManager);
        binding.arCamRecycler.setAdapter(arCamRecyclerAdapter);



    }

    private void showDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.settings_dialog_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        LinearLayout logOutLayout = dialog.findViewById(R.id.logout_button);
        logOutLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                initViewModel.getFirebaseAuth().signOut();
                initViewModel.getInitContext().transaction_to_Welcome();
                dialog.cancel();
            }});
    }
    
}