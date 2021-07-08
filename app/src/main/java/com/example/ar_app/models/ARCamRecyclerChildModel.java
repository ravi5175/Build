package com.example.ar_app.models;

public class ARCamRecyclerChildModel {
    public String id;
    public String title;
    public int imageResource;
    public Boolean isSelected;

    public ARCamRecyclerChildModel(String id ,String title, int imageResource){
        this.id = id;
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

    public Boolean getIsSelected() {
        return isSelected;
    }
    public void setIsSelected(Boolean bool){
        this.isSelected = bool;
    }
}
