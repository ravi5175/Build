package com.example.ar_app.models;

public class ARCamRecyclerChildModel {
    public String title;
    public int imageResource;
    public Boolean isSelected;

    public ARCamRecyclerChildModel(String title, int imageResource){
        this.title=title;
        this.imageResource = imageResource;
        this.isSelected = false;
    }

    public String getAssetTitle() {
        return title;
    }

    public int getAssetImageResource(){
        return imageResource;
    }
}
