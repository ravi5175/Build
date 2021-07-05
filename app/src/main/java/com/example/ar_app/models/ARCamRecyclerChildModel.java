package com.example.ar_app.models;

public class ARCamRecyclerChildModel {
    String title;
    int imageResource;

    public ARCamRecyclerChildModel(String title, int imageResource){
        this.title=title;
        this.imageResource = imageResource;
    }

    public String getAssetTitle() {
        return title;
    }

    public int getAssetImageResource(){
        return imageResource;
    }
}
