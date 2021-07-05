package com.example.ar_app.viewmodels;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ar_app.R;
import com.example.ar_app.models.ARCamRecyclerChildModel;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.ArrayList;

public class ARCamViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ARCamRecyclerChildModel>> recyclerViewData = new MutableLiveData<ArrayList<ARCamRecyclerChildModel>>(new ArrayList<ARCamRecyclerChildModel>());

    public ARCamViewModel(){
        if(this.recyclerViewData.getValue().isEmpty()){
            ARCamRecyclerChildModel bulbasaur = new ARCamRecyclerChildModel("bulbasaur",R.drawable.bulbasaur);
            ARCamRecyclerChildModel cubone = new ARCamRecyclerChildModel("cubone",R.drawable.cubone);
            ARCamRecyclerChildModel jigllypuff = new ARCamRecyclerChildModel("jiglypuff",R.drawable.jigglypuff);
            ARCamRecyclerChildModel weavile = new ARCamRecyclerChildModel("weavile",R.drawable.weavile);

            this.recyclerViewData.getValue().add(bulbasaur);
            this.recyclerViewData.getValue().add(cubone);
            this.recyclerViewData.getValue().add(jigllypuff);
            this.recyclerViewData.getValue().add(weavile);
        }
    }

    public MutableLiveData<ArrayList<ARCamRecyclerChildModel>> getARCamRecyclerData(){
      return this.recyclerViewData;
    };
}
