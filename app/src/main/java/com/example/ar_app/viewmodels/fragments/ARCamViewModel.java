package com.example.ar_app.viewmodels.fragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_app.R;
import com.example.ar_app.models.ARCamRecyclerChildModel;

import java.util.ArrayList;

/**
 *  ARCam Fragment View Model Class
 */
public class ARCamViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ARCamRecyclerChildModel>> recyclerViewData = new MutableLiveData<ArrayList<ARCamRecyclerChildModel>>(new ArrayList<ARCamRecyclerChildModel>());

    public MutableLiveData<Boolean> cameraMode = new MutableLiveData<Boolean>(false);

    public MutableLiveData<String> modelRenderableId = new MutableLiveData<String>("toy_truck.sfb");

    public MutableLiveData<String> tempModelRenderableId = new MutableLiveData<String>("blank");

    // model renderable names with their extensions
    String larvitar_id  = "larvitar.sfb";
    String bulbasaur_id = "bulbasaur.sfb";
    String grass_id = "grass.sfb";
    String grass_patch_id = "grass_patch.sfb";

    public ARCamViewModel(){
        if(this.recyclerViewData.getValue().isEmpty()){
            ARCamRecyclerChildModel bulbasaur = new ARCamRecyclerChildModel(bulbasaur_id,"bulbasaur",R.drawable.bulbasaur);
            ARCamRecyclerChildModel larvitar = new ARCamRecyclerChildModel(larvitar_id,"larvitar",R.drawable.larvitar);
            ARCamRecyclerChildModel grass = new ARCamRecyclerChildModel(grass_id,"grass",R.drawable.grass);
            ARCamRecyclerChildModel grass_patch = new ARCamRecyclerChildModel(grass_patch_id,"grass_patch",R.drawable.grass_patch);

            this.recyclerViewData.getValue().add(bulbasaur);
            this.recyclerViewData.getValue().add(larvitar);
            this.recyclerViewData.getValue().add(grass);
            this.recyclerViewData.getValue().add(grass_patch);
        }
    }

    public MutableLiveData<ArrayList<ARCamRecyclerChildModel>> getARCamRecyclerData(){
      return this.recyclerViewData;
    };
}
