package com.example.qrpacking;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.net.URI;

public class AddBoxActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQ = 1;

    private Button chooseImageBtn;
    private Button uploadBtn;
    private ImageView imageView;

    private Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_box);

        chooseImageBtn = findViewById(R.id.chooseImageBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        imageView = findViewById(R.id.imageView);

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQ && resultCode == RESULT_OK
            && data!=null && data.getData()!=null){
            imageURI = data.getData();

            imageView.setImageURI(imageURI);
        }
    }
}
