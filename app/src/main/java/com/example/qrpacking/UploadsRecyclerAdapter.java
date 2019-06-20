package com.example.qrpacking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadsRecyclerAdapter extends RecyclerView.Adapter<UploadsRecyclerAdapter.ImageViewHolder> {
    private Context context;
    private List<Upload> uploadsList;

    public UploadsRecyclerAdapter(Context context, List<Upload> uploadsList){
        this.context = context;
        this.uploadsList = uploadsList;
    }



    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, viewGroup,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Upload uploadCurrent = uploadsList.get(i);
        imageViewHolder.textViewName.setText(uploadCurrent.getName());
        /*Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .fit()
                .centerCrop()
                .into(imageViewHolder.imageView);
                */
        //puts image into recyclerview
        Glide.with(context)
                .load(uploadCurrent.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(imageViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return uploadsList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);

        }
    }
}
