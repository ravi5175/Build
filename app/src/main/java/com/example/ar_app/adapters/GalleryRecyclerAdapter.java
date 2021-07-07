package com.example.ar_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ar_app.R;
import com.example.ar_app.models.ImageDownloadUrl;

import java.util.ArrayList;

public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.GalleryRecyclerAdapterViewHolder> {

    public ArrayList<ImageDownloadUrl> data;
    public GalleryRecyclerAdapter(ArrayList<ImageDownloadUrl> data){
        this.data = data;
    }

    @Override
    public GalleryRecyclerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.gallery_recycler_child_layout,parent,false);
        return new GalleryRecyclerAdapter.GalleryRecyclerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryRecyclerAdapter.GalleryRecyclerAdapterViewHolder holder, int position) {
            ImageDownloadUrl imageUrlModel = data.get(position);
            Glide.with(holder.imageView).asBitmap().load(imageUrlModel.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class GalleryRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public GalleryRecyclerAdapterViewHolder(View itemView){
            super(itemView);
            this.imageView = itemView.findViewById(R.id.gallery_child_image);
        }
    }
}
