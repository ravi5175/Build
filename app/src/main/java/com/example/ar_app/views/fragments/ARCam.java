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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ar_app.R;
import com.example.ar_app.adapters.ARCamRecyclerAdapter;
import com.example.ar_app.custom_classes.CustomARFragment;
import com.example.ar_app.databinding.FragmentArCamBinding;
import com.example.ar_app.models.ImageDownloadUrl;
import com.example.ar_app.viewmodels.activities.MainViewModel;
import com.example.ar_app.viewmodels.fragments.ARCamViewModel;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.collision.Ray;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * ARCam Fragment
 * - All AR functionality is handled here
 */

@SuppressWarnings("ConstantConditions")
public class ARCam extends Fragment {

    FragmentArCamBinding    binding;
    ARCamViewModel          arCamViewModel;
    MainViewModel           mainViewModel;

    LinearLayoutManager     arCamRecyclerLayoutManager;
    ARCamRecyclerAdapter    arCamRecyclerAdapter;

    CustomARFragment        arCamFragment;

    String                  modelRenderableId;

    int count = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArCamBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ViewModels Initialization
        arCamViewModel = new ViewModelProvider(this).get(ARCamViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        binding.arcamSettings.setOnClickListener(v -> showDialog());

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

        binding.arCamCapture.setOnClickListener(view1 -> {
            if(arCamViewModel.cameraMode.getValue()){
                takePhoto();
            }else {
                // for AR Air Mode
                // RenderableModel will be placed at a 75 cm away from Center of Camera View
                //placeModel();
            }
        });

        binding.galleryButton.setOnClickListener(view12 -> mainViewModel.getMainActivityContext().transaction_to_Gallery());

        arCamViewModel.cameraMode.observe(getViewLifecycleOwner(), mode -> {
            if(mode){
                binding.arCamModeStatus.setText(R.string.capture);
            }else{
                binding.arCamModeStatus.setText(R.string.ar);
            }
        });

        arCamViewModel.modelRenderableId.observe(getViewLifecycleOwner(), s -> modelRenderableId = s);

        arCamViewModel.recyclerViewData.observe(getViewLifecycleOwner(), arCamRecyclerChildModels -> {
            arCamRecyclerAdapter.notifyDataSetChanged();
            Log.d("ARCAM-dataset-changed","notify data set changed called");
        });
    }

    /**
     * function - addModelToScene
     * creates and add, transformable anchor node into the sceneview
     * @param anchor anchor generated by OnPlaneTapListener
     * @param modelRenderable a modelrenderable asset
     */
    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arCamFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arCamFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
        binding.nodeCount.setText(String.valueOf(count+=1));
    }

    /**
     * function - placeModel
     * use to set anchor and attach a modelRenderable in air
     * i.e in center front of camera screen (75 cm default)
     */
    private void placeModel(){
        ModelRenderable.builder()
                .setSource(requireContext(), Uri.parse("toy_truck.sfb"))
                .build()
                .thenAccept(this::addModelOnClick)
                .exceptionally(throwable ->{
                    Toast.makeText(requireActivity(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    /**
     * function - addModelOnClick
     * sets node in air in front of camera view
     * @param modelRenderable ModelRenderable object
     */
    private void addModelOnClick(ModelRenderable modelRenderable){
        Node node = new Node();
        node.setParent(arCamFragment.getArSceneView().getScene());
        node.setRenderable(modelRenderable);
        Camera camera = arCamFragment.getArSceneView().getScene().getCamera();
        Ray ray = camera.screenPointToRay(1080/2f,2480/2f);
        Vector3 newPosition = ray.getPoint(1f);
        node.setLocalPosition(newPosition);
    }

    /**
     *  function takePhoto
     *  - captures a bitmap from sceneview of arCam
     */
    private void takePhoto() {
        binding.imageCaptureStatusLayout.setVisibility(View.VISIBLE);
        binding.imageCaptureStatus.setText(R.string.capturing_image);
        ArSceneView view = arCamFragment.getArSceneView();
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();

        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                uploadPhoto(bitmap);
            } else {
                Log.d("takePhoto","pixel copy failed");
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }

    /**
     * function uploadPhoto
     * uploads bitmap to firebase storage
     * writes uploaded bitmap url to firebase realtimedatabase
     * @param bitmap bitmap object
     */
    private void uploadPhoto(Bitmap bitmap){
        binding.imageCaptureStatus.setText(R.string.uploading_image);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference uploadReference = mainViewModel.getStorage().child("images/"+ Calendar.getInstance().getTimeInMillis() +".jpg");

        UploadTask uploadTask = uploadReference.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            binding.imageCaptureStatus.setText(R.string.uploading_image_failed);
            binding.imageCaptureStatusLayout.setVisibility(View.GONE);

            Toast.makeText(requireContext(),"upload unsuccessful",Toast.LENGTH_SHORT).show();
            Log.d("Firebase Storage",exception.toString());
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(requireContext(),"upload successful",Toast.LENGTH_SHORT).show();
            uploadReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Log.d("ref uri",uri.toString());
                ImageDownloadUrl imageDownloadUrl = new ImageDownloadUrl(uri.toString());
                String imageDownloadUrlId = mainViewModel.getDatabase().push().getKey();
                Log.d("ref key",imageDownloadUrlId);
                mainViewModel.getDatabase().child(imageDownloadUrlId).setValue(imageDownloadUrl);
                binding.imageCaptureStatus.setText(R.string.uploading_image_success);
                binding.imageCaptureStatusLayout.setVisibility(View.GONE);
            }).addOnFailureListener(e ->
                    Toast.makeText(requireContext(),"rt database update FAILED "+e.getMessage(),Toast.LENGTH_SHORT).show());
        });
    }

    /**
     * function showDialog
     * creates a setting dialog
     * - switch camera mode from AR to Capture
     * - log out from current session
     */
    private void showDialog(){
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.settings_dialog_layout);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout logout = dialog.findViewById(R.id.logout_button);
        logout.setOnClickListener(view -> {
            mainViewModel.getFAuth().signOut();
            mainViewModel.getMainActivityContext().InitActivityIntent();
            dialog.cancel();
        });

        LinearLayout arMode = dialog.findViewById(R.id.ar_mode_button);
        arMode.setOnClickListener(view -> {
            arCamViewModel.cameraMode.setValue(false);
            binding.arCamRecycler.setVisibility(View.VISIBLE);
            arCamFragment.getArSceneView().getPlaneRenderer().setEnabled(true);
            arCamViewModel.modelRenderableId.setValue(arCamViewModel.tempModelRenderableId.getValue());
            dialog.cancel();
        });

        LinearLayout captureMode = dialog.findViewById(R.id.capture_mode_button);
        captureMode.setOnClickListener(view -> {
            arCamViewModel.cameraMode.setValue(true);
            binding.arCamRecycler.setVisibility(View.INVISIBLE);
            arCamViewModel.tempModelRenderableId.setValue(arCamViewModel.modelRenderableId.getValue());
            arCamViewModel.modelRenderableId.setValue("blank");
            arCamFragment.getArSceneView().getPlaneRenderer().setEnabled(false);
            dialog.cancel();
        });

    }
}