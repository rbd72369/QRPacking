package com.example.qrpacking;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UploadsRecyclerAdapter extends RecyclerView.Adapter<UploadsRecyclerAdapter.ImageViewHolder> {
    private Context context;
    private List<Upload> uploadsList;
    private List<QrCode> qrCodeList;
    public static final String TAG = "UploadsRecycler";

    public UploadsRecyclerAdapter(Context context, List<Upload> uploadsList) {
        this.context = context;
        this.uploadsList = uploadsList;
        qrCodeList = new ArrayList<>();
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, viewGroup, false);
        return new ImageViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, final int i) {
        final Upload uploadCurrent = uploadsList.get(i);
        imageViewHolder.textViewName.setText(uploadCurrent.getName());

        //puts image into recyclerview
        Glide.with(context)
                .load(uploadCurrent.getImageUrl())
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(imageViewHolder.imageView);

        imageViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.d(TAG, "onCheckedChanged: "+ uploadCurrent.getName());
                    QrCode qrCode = new QrCode(uploadCurrent.getName(),uploadCurrent.getImageUrl(),context);
                    qrCodeList.add(qrCode);
                }
                else{
                    //qrCodeList.remove(i-1);//TODO this doesn't make sense
                }
            }
        });

    }

    public List<QrCode> getQrCodeList(){
        return qrCodeList;
    }

    @Override
    public int getItemCount() {
        return uploadsList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;
        public CheckBox checkBox;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            checkBox = itemView.findViewById(R.id.check);



        }
    }
}
