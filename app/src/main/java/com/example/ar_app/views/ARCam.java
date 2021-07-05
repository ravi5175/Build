package com.example.ar_app.views;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ar_app.R;
import com.example.ar_app.adapters.ARCamRecyclerAdapter;
import com.example.ar_app.databinding.FragmentArCamBinding;

import com.example.ar_app.viewmodels.ARCamViewModel;
import com.example.ar_app.viewmodels.InitViewModel;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;


public class ARCam extends Fragment {

    FragmentArCamBinding binding;
    ARCamViewModel arCamViewModel;
    InitViewModel initViewModel;

    LinearLayoutManager arCamRecyclerLayoutManager;
    ARCamRecyclerAdapter arCamRecyclerAdapter;

    ArFragment arCamFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                initViewModel.getFirebaseAuth().signOut();
                initViewModel.getInitContext().transaction_to_Welcome();
            }
        });

        arCamRecyclerAdapter = new ARCamRecyclerAdapter(arCamViewModel.recyclerViewData.getValue());
        arCamRecyclerLayoutManager = new LinearLayoutManager(initViewModel.getInitContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.arCamRecycler.setLayoutManager(arCamRecyclerLayoutManager);
        binding.arCamRecycler.setAdapter(arCamRecyclerAdapter);

        arCamFragment = (ArFragment)getChildFragmentManager().findFragmentById(R.id.arFragment);
        arCamFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            ModelRenderable.builder()
                     .setSource(requireContext(), Uri.parse("house_plant.sfb"))
                    .build()
                    .thenAccept(modelRenderable -> addModelToScene(anchor,modelRenderable))
                    .exceptionally(throwable ->{
                        Toast.makeText(requireActivity(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        return null;
                    });
        });
    }

    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arCamFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arCamFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }

}