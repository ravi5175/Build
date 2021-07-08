package com.example.ar_app.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_app.R;
import com.example.ar_app.models.ARCamRecyclerChildModel;
import com.example.ar_app.viewmodels.fragments.ARCamViewModel;

import java.util.ArrayList;

public class ARCamRecyclerAdapter extends RecyclerView.Adapter<ARCamRecyclerAdapter.ARCamAdapterViewHolder> {
    private ArrayList<ARCamRecyclerChildModel> data;
    private ARCamViewModel arCamViewModel;

    public ARCamRecyclerAdapter(ArrayList<ARCamRecyclerChildModel> data , ARCamViewModel arCamViewModel){
        this.data = data;
        this.arCamViewModel = arCamViewModel;
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

        if(data.get(position).getIsSelected()){
            holder.layout.setBackgroundResource(R.drawable.ar_asset_frame_selected);
        }else{
            holder.layout.setBackgroundResource(R.drawable.ar_asset_frame_not_selected);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ar_cam","recycler image clicked");
                for(ARCamRecyclerChildModel model : data){
                    model.setIsSelected(false);
                }
                arCamViewModel.modelRenderableId.setValue(currentItem.id);
                data.get(position).setIsSelected(true);
                arCamViewModel.recyclerViewData.setValue(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ARCamAdapterViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        LinearLayout layout;
        public ARCamAdapterViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.ar_asset_image);
            title = itemView.findViewById(R.id.ar_asset_title);
            layout = itemView.findViewById(R.id.ar_asset_selection);
        }
    }
}
