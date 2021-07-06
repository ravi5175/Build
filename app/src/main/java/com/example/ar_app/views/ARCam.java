package com.example.ar_app.views;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ar_app.R;
import com.example.ar_app.adapters.ARCamRecyclerAdapter;
import com.example.ar_app.custom_classes.CustomARFragment;
import com.example.ar_app.databinding.FragmentArCamBinding;
import com.example.ar_app.models.ImageDownloadUrl;
import com.example.ar_app.viewmodels.ARCamViewModel;
import com.example.ar_app.viewmodels.InitViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class ARCam extends Fragment {

    FragmentArCamBinding binding;
    ARCamViewModel arCamViewModel;
    InitViewModel initViewModel;

    LinearLayoutManager arCamRecyclerLayoutManager;
    ARCamRecyclerAdapter arCamRecyclerAdapter;

    CustomARFragment arCamFragment;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Image");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

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

        arCamFragment = (CustomARFragment)getChildFragmentManager().findFragmentById(R.id.arFragment);

        arCamFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            arCamViewModel.anchorList.add(anchor);
            ModelRenderable.builder()
                     .setSource(requireContext(), Uri.parse("toy_truck.sfb"))
                    .build()
                    .thenAccept(modelRenderable -> addModelToScene(anchor,modelRenderable))
                    .exceptionally(throwable ->{
                        Toast.makeText(requireActivity(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        return null;
                    });
        });

        binding.arCamCapture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                    takePhoto();
            }
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


    private void takePhoto() {
        ArSceneView view = arCamFragment.getArSceneView();
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();

        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                //binding.captureArImage.setImageBitmap(bitmap);
                uploadPhoto(bitmap);
            } else {
                Log.d("takePhoto","pixel copy failed");
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }

    private void uploadPhoto(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference uploadReference = storageRef.child("images/"+ Calendar.getInstance().getTimeInMillis() +".jpg");

        UploadTask uploadTask = uploadReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(requireContext(),"upload unsuccessful",Toast.LENGTH_SHORT).show();
                Log.d("Firebase Storage",exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(requireContext(),"upload successful",Toast.LENGTH_SHORT).show();
                uploadReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("ref uri",uri.toString());
                        ImageDownloadUrl imageDownloadUrl = new ImageDownloadUrl(uri.toString());
                        String imageDownloadUrlId = database.push().getKey();
                        Log.d("ref key",imageDownloadUrlId);
                        database.child(imageDownloadUrlId).setValue(imageDownloadUrl);

                    }
                }).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(requireContext(),"rt database update FAILED",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}