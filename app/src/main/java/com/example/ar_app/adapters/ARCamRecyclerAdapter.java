package com.example.ar_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_app.R;
import com.example.ar_app.models.ARCamRecyclerChildModel;

import java.util.ArrayList;

public class ARCamRecyclerAdapter extends RecyclerView.Adapter<ARCamRecyclerAdapter.ARCamAdapterViewHolder> {
    private ArrayList<ARCamRecyclerChildModel> data;

    public ARCamRecyclerAdapter(ArrayList<ARCamRecyclerChildModel> data){
        this.data = data;
    }

    @Override
    public ARCamAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ar_cam_recycler_child_layout,parent,false);
        return new ARCamAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ARCamRecyclerAdapter.ARCamAdapterViewHolder holder, int position) {
        ARCamRecyclerChildModel currentItem = data.get(position);
        holder.title.setText(currentItem.getAssetTitle());
        holder.image.setImageResource(currentItem.getAssetImageResource());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ARCamAdapterViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        public ARCamAdapterViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.ar_asset_image);
            title = itemView.findViewById(R.id.ar_asset_title);
        }
    }
}
