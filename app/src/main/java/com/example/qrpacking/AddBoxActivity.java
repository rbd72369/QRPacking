package com.example.qrpacking;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

public class AddBoxActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQ = 1;

    private Button chooseImageBtn;
    private Button uploadBtn;
    private ImageView imageView;
    private EditText fileNameET;

    private Uri imageURI;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_box);

        chooseImageBtn = findViewById(R.id.chooseImageBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        imageView = findViewById(R.id.imageView);
        fileNameET = findViewById(R.id.fileNameET);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
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

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadFile(){
        String imageName;
        if(fileNameET.getText().toString().trim().equals("")){
            imageName = "No Name";
        }
        else imageName = fileNameET.getText().toString().trim();
        String imageEndText = imageName.replaceAll(" ", "_");

        final String imgName = imageName;
        final StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                +imageEndText +"." + getFileExtension(imageURI));
        if (imageURI != null)
        {
            fileRef.putFile(imageURI).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    Toast.makeText(AddBoxActivity.this,"THis workds",Toast.LENGTH_LONG).show();
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();


                        String uploadId = databaseReference.push().getKey();
                        Upload upload = new Upload(uploadId,imgName,
                                downloadUri.toString());
                        databaseReference.child(uploadId).setValue(upload);
                        Toast.makeText(AddBoxActivity.this,"Upload successful",Toast.LENGTH_SHORT).show();
                        //starts QRImageActivity with uri intent
                        Intent intent = new Intent(AddBoxActivity.this,QRImageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("uri", downloadUri.toString());
                        bundle.putString("name", upload.getName());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else
                    {
                        Toast.makeText(AddBoxActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        /* if(imageURI != null){
            StorageReference fileRef = storageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(imageURI));
            fileRef.putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddBoxActivity.this,"Upload successful",Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(fileNameET.getText().toString().trim(),
                                    taskSnapshot.getStorage().getDownloadUrl().toString());
                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddBoxActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(this,"no file selected", Toast.LENGTH_SHORT).show();
        }*/
    }
}
