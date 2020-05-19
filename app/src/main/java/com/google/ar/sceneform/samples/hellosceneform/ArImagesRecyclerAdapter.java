package com.google.ar.sceneform.samples.hellosceneform;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ArImagesRecyclerAdapter extends RecyclerView.Adapter<ArImagesRecyclerAdapter.ArImageViewHolder> {

    private List<Integer> imageList;
//    private ArImageOnClickListener mImageOnClickListener;

    ArImagesRecyclerAdapter(ArrayList<Integer> horizontalList) {
        this.imageList = horizontalList;
//        this.mImageOnClickListener = imageOnClickListener;
    }

    @NonNull
    @Override
    public ArImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ar_image_list_child_view, viewGroup, false);
        return new ArImageViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ArImageViewHolder arImageViewHolder, int i) {
        arImageViewHolder.ivArImage.setImageResource(imageList.get(i));

//        arImageViewHolder.ivArImage.setOnClickListener();
    }

    class ArImageViewHolder extends RecyclerView.ViewHolder {

        ImageView ivArImage;

        ArImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivArImage = itemView.findViewById(R.id.iv_ar_image);
        }
    }

//    interface ArImageOnClickListener{
//        void onImageClickListener(int positon);
//    }
}
