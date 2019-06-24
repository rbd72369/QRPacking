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
    private List<Upload> checkedUploadsList;
    public static final String TAG = "UploadsRecycler";



    public UploadsRecyclerAdapter(Context context, List<Upload> uploadsList) {
        this.context = context;
        this.uploadsList = uploadsList;
        checkedUploadsList = new ArrayList<>();
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
        imageViewHolder.checkBox.setOnCheckedChangeListener(null);
        imageViewHolder.checkBox.setChecked(uploadCurrent.isSelected());

        imageViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox chk = (CheckBox) v;
                if(chk.isChecked()){
                    checkedUploadsList.add(uploadsList.get(pos));
                    uploadCurrent.setSelected(true);
                    Log.d(TAG,"added: " + uploadsList.get(pos).getName());
                }
                else{
                    checkedUploadsList.remove(uploadsList.get(pos));
                    uploadCurrent.setSelected(false);
                    Log.d(TAG,"removed: " + uploadsList.get(pos).getName());
                }
            }
        });

    }

    public List<Upload> getCheckedUploadsList(){
        return checkedUploadsList;
    }

    @Override
    public int getItemCount() {
        return uploadsList.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName;
        public ImageView imageView;
        public CheckBox checkBox;
        private ItemClickListener itemClickListener;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            checkBox = itemView.findViewById(R.id.check);

            checkBox.setOnClickListener(this);
        }
        public  void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
    }
}
