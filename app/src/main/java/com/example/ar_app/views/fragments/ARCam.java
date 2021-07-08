package com.example.ar_app.views.fragments;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ar_app.R;
import com.example.ar_app.adapters.ARCamRecyclerAdapter;
import com.example.ar_app.custom_classes.CustomARFragment;
import com.example.ar_app.databinding.FragmentArCamBinding;
import com.example.ar_app.models.ARCamRecyclerChildModel;
import com.example.ar_app.models.ImageDownloadUrl;
import com.example.ar_app.viewmodels.activities.MainViewModel;
import com.example.ar_app.viewmodels.fragments.ARCamViewModel;
import com.example.ar_app.viewmodels.activities.InitViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.collision.Ray;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class ARCam extends Fragment {

    FragmentArCamBinding binding;
    ARCamViewModel arCamViewModel;
    MainViewModel mainViewModel;

    LinearLayoutManager arCamRecyclerLayoutManager;
    ARCamRecyclerAdapter arCamRecyclerAdapter;

    CustomARFragment arCamFragment;

    DatabaseReference database = FirebaseDatabase.getInstance("https://ar-app-11eb0-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Image");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    int count = 0;

    String modelRenderableId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArCamBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arCamViewModel = new ViewModelProvider(this).get(ARCamViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        binding.arcamSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        modelRenderableId = arCamViewModel.modelRenderableId.getValue();

        arCamRecyclerAdapter = new ARCamRecyclerAdapter(arCamViewModel.recyclerViewData.getValue(),arCamViewModel);
        arCamRecyclerLayoutManager = new LinearLayoutManager(mainViewModel.getMainActivityContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.arCamRecycler.setLayoutManager(arCamRecyclerLayoutManager);
        binding.arCamRecycler.setAdapter(arCamRecyclerAdapter);

        arCamFragment = (CustomARFragment)getChildFragmentManager().findFragmentById(R.id.arFragment);

        arCamFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            ModelRenderable.builder()
                     .setSource(requireContext(), Uri.parse(modelRenderableId))
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
                if(arCamViewModel.cameraMode.getValue()){
                    takePhoto();
                }else
                    placeModel();
                    binding.nodeCount.setText(String.valueOf(count+=1));
            }
        });


        binding.galleryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //initViewModel.getInitContext().transaction_to_Gallery();
            }
        });

        arCamViewModel.cameraMode.observe(getViewLifecycleOwner(),new Observer<Boolean>(){
                    @Override
                    public void onChanged(Boolean mode) {
                        if(mode){
                            binding.arCamModeStatus.setText("CAPTURE");
                        }else{
                            binding.arCamModeStatus.setText("AR");
                        }
                    }
        });

        arCamViewModel.modelRenderableId.observe(getViewLifecycleOwner(), new Observer<String>(){
            @Override
            public void onChanged(String s) {
                modelRenderableId = s;
            }
        });

        arCamViewModel.recyclerViewData.observe(getViewLifecycleOwner(),new Observer<ArrayList<ARCamRecyclerChildModel>>(){
                    @Override
                    public void onChanged(ArrayList<ARCamRecyclerChildModel> arCamRecyclerChildModels) {
                        arCamRecyclerAdapter.notifyDataSetChanged();
                        Log.d("ar_cam","notifydatasetchanged called");
                    }
                });
    }

    // AR mode
    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arCamFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arCamFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }

    // AR air mode
    private void placeModel(){
        ModelRenderable.builder()
                .setSource(requireContext(), Uri.parse("toy_truck.sfb"))
                .build()
                .thenAccept(modelRenderable -> addModelOnClick(modelRenderable))
                .exceptionally(throwable ->{
                    Toast.makeText(requireActivity(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void addModelOnClick(ModelRenderable modelRenderable){
        Node node = new Node();
        node.setParent(arCamFragment.getArSceneView().getScene());
        node.setRenderable(modelRenderable);
        Camera camera = arCamFragment.getArSceneView().getScene().getCamera();
        Ray ray = camera.screenPointToRay(1080/2f,2480/2f);
        Vector3 newPosition = ray.getPoint(1f);
        node.setLocalPosition(newPosition);
    }


    private void takePhoto() {
        binding.imageCaptureStatusLayout.setVisibility(View.VISIBLE);
        binding.imageCaptureStatus.setText("Capturing Image...");
        ArSceneView view = arCamFragment.getArSceneView();
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();

        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                binding.imageCaptureStatus.setText("Captured Image");
                uploadPhoto(bitmap);
            } else {
                Log.d("takePhoto","pixel copy failed");
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }


    private void uploadPhoto(Bitmap bitmap){
        binding.imageCaptureStatus.setText("Uploading Image");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference uploadReference = storageRef.child("images/"+ Calendar.getInstance().getTimeInMillis() +".jpg");

        UploadTask uploadTask = uploadReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                binding.imageCaptureStatus.setText("Upload Image Failed");
                binding.imageCaptureStatusLayout.setVisibility(View.GONE);
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
                        binding.imageCaptureStatus.setText("Upload Image Success");
                        binding.imageCaptureStatusLayout.setVisibility(View.GONE);
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

    private void showDialog(){
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.settings_dialog_layout);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout logout = dialog.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.getFAuth().signOut();
                mainViewModel.getMainActivityContext().InitActivityIntent();
                dialog.cancel();
            }
        });

        LinearLayout arMode = dialog.findViewById(R.id.ar_mode_button);
        arMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arCamViewModel.cameraMode.setValue(false);
                binding.arCamRecycler.setVisibility(View.VISIBLE);
                dialog.cancel();
            }
        });

        LinearLayout captureMode = dialog.findViewById(R.id.capture_mode_button);
        captureMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arCamViewModel.cameraMode.setValue(true);
                binding.arCamRecycler.setVisibility(View.INVISIBLE);
                dialog.cancel();
            }
        });

    }
}