package com.example.qrpacking;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UploadsActivity extends AddBoxActivity {

    public final String TAG = "UploadsActivity";
    private RecyclerView recyclerView;
    private UploadsRecyclerAdapter uploadsRecyclerAdapter;

    private FirebaseStorage firebaseStorage;

    private ProgressBar progressCircle;
    private Button pdfButton;
    private Button deleteBtn;
    private List<QrCode> qrCodeList;
    private List<Upload> checkedUploadsList;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private List<Upload> uploadsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        getSupportActionBar().setTitle("Uploads");

        recyclerView = findViewById(R.id.uploadsRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TODO check if this shit does anything
        //recyclerView.setItemViewCacheSize(20);
        //recyclerView.setDrawingCacheEnabled(true);

        progressCircle = findViewById(R.id.progressCirc);
        pdfButton = findViewById(R.id.pdf);
        deleteBtn = findViewById(R.id.deleteBtn);

        uploadsList = new ArrayList<>();
        checkedUploadsList = new ArrayList<>();
        qrCodeList = new ArrayList<>();

        uploadsRecyclerAdapter = new UploadsRecyclerAdapter(UploadsActivity.this, uploadsList);
        recyclerView.setAdapter(uploadsRecyclerAdapter);

        firebaseStorage = FirebaseStorage.getInstance();

        //gets dbref of upload
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        //adds upload from db to arraylist to put into card views/recyclerview
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadsList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    //TODO upload.setKey(postSnapshot.getKey());
                    uploadsList.add(upload);
                }

                uploadsRecyclerAdapter.notifyDataSetChanged();
                //makes progress circle invisible
                progressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UploadsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressCircle.setVisibility(View.INVISIBLE);
            }
        });

        //creates pdf
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //puts checked uploads in qrcodelist
                checkedUploadsList = uploadsRecyclerAdapter.getCheckedUploadsList();
                if (checkedUploadsList.size() != 0) {
                    for (int i = 0; i < checkedUploadsList.size(); i++) {
                        Upload upload = checkedUploadsList.get(i);
                        QrCode qrCode = new QrCode(upload.getName(), upload.getImageUrl(), UploadsActivity.this);
                        qrCodeList.add(qrCode);
                        Log.d(TAG, "ADDED: " + qrCode.getName());
                    }

                    //creates pdf
                    Pdf pdf = new Pdf(qrCodeList);
                    pdf.createPdf();
                    //TODO make pdf open on creation
                    //TODO save in db based on user

                    Toast.makeText(UploadsActivity.this, "PDF made", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadsActivity.this, "No images selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedUploadsList = uploadsRecyclerAdapter.getCheckedUploadsList();
                String endString;

                if(checkedUploadsList.size() == 1){
                    endString = " image";
                }
                else {
                    endString = " images";
                }
                //check that images are checked
                if(checkedUploadsList.size() != 0){
                    //confirm delete action
                    AlertDialog dialog = new AlertDialog.Builder(UploadsActivity.this)
                            .setTitle("Are you sure you want to delete " + checkedUploadsList.size() + endString + "?")
                            .setMessage(" This permanently deletes" + endString + " from database")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //delete images
                                    deleteImages();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create();
                    dialog.show();
                }
                else {
                    Toast.makeText(UploadsActivity.this, "No images selected", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * removes checked images from storage and db
     */
    public void deleteImages() {
        //checkedUploadsList = uploadsRecyclerAdapter.getCheckedUploadsList();

        for (int i = 0; i < checkedUploadsList.size(); i++) {
            final Upload upload = checkedUploadsList.get(i);
            final String id = upload.getId();
            StorageReference imageRef = firebaseStorage.getReferenceFromUrl(upload.getImageUrl());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    databaseReference.child(id).removeValue();

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadsActivity.this, "ERROR! Images not deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }
        //clear list
        checkedUploadsList.clear();
        Toast.makeText(this, "Images Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //todo comment
        databaseReference.removeEventListener(valueEventListener);
    }
}
