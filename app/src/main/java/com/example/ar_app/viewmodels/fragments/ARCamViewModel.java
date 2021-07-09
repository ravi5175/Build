package com.example.ar_app.viewmodels.fragments;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_app.R;
import com.example.ar_app.models.ARCamRecyclerChildModel;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.ArrayList;

public class ARCamViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ARCamRecyclerChildModel>> recyclerViewData = new MutableLiveData<ArrayList<ARCamRecyclerChildModel>>(new ArrayList<ARCamRecyclerChildModel>());

    public MutableLiveData<Boolean> cameraMode = new MutableLiveData<Boolean>(false);

    public MutableLiveData<String> modelRenderableId = new MutableLiveData<String>("toy_truck.sfb");
    public MutableLiveData<String> tempModelRenderableId = new MutableLiveData<String>("blank");

    // model renderable names with their extensions
    String toy_truck_id = "toy_truck.sfb";
    String larvitar_id  = "larvitar.sfb";
    String bulbasaur_id = "bulbasaur.sfb";
    String grass1_id = "grass1.sfb";
    String grass_patch = "grass_patch.sfb";

    public ARCamViewModel(){
        if(this.recyclerViewData.getValue().isEmpty()){
            ARCamRecyclerChildModel bulbasaur = new ARCamRecyclerChildModel(bulbasaur_id,"bulbasaur",R.drawable.bulbasaur);
            ARCamRecyclerChildModel larvitar = new ARCamRecyclerChildModel(larvitar_id,"cubone",R.drawable.cubone);
            ARCamRecyclerChildModel jigllypuff = new ARCamRecyclerChildModel(grass_patch,"jiglypuff",R.drawable.jigglypuff);
            ARCamRecyclerChildModel weavile = new ARCamRecyclerChildModel(toy_truck_id,"weavile",R.drawable.weavile);

            this.recyclerViewData.getValue().add(bulbasaur);
            this.recyclerViewData.getValue().add(larvitar);
            this.recyclerViewData.getValue().add(jigllypuff);
            this.recyclerViewData.getValue().add(weavile);
        }
    }

    public MutableLiveData<ArrayList<ARCamRecyclerChildModel>> getARCamRecyclerData(){
      return this.recyclerViewData;
    };
}
