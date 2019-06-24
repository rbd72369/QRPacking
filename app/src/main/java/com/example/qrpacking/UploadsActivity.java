package com.example.qrpacking;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UploadsActivity extends AppCompatActivity {

    public final String TAG = "UploadsActivity";
    private RecyclerView recyclerView;
    private UploadsRecyclerAdapter uploadsRecyclerAdapter;

    private ProgressBar progressCircle;
    private Button pdfButton;
    private List<QrCode> qrCodeList;
    private List<Upload> checkedUploadsList;

    private DatabaseReference databaseReference;
    private List<Upload> uploadsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        recyclerView = findViewById(R.id.uploadsRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TODO check if this shit does anything
        //recyclerView.setItemViewCacheSize(20);
        //recyclerView.setDrawingCacheEnabled(true);

        progressCircle = findViewById(R.id.progressCirc);
        pdfButton = findViewById(R.id.pdf);

        uploadsList = new ArrayList<>();
        checkedUploadsList = new ArrayList<>();
        qrCodeList = new ArrayList<>();

        //gets dbref of upload
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        //adds upload from db to arraylist to put into card views/recyclerview
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadsList.add(upload);
                }

                uploadsRecyclerAdapter = new UploadsRecyclerAdapter(UploadsActivity.this, uploadsList);
                recyclerView.setAdapter(uploadsRecyclerAdapter);
                //makes progress circle invisible
                progressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UploadsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressCircle.setVisibility(View.INVISIBLE);
            }
        });

        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //puts uploads in qrcodelist
                checkedUploadsList = uploadsRecyclerAdapter.getCheckedUploadsList();
                for (int i = 0; i < checkedUploadsList.size(); i++) {
                    Upload upload = checkedUploadsList.get(i);
                    QrCode qrCode = new QrCode(upload.getName(),upload.getImageUrl(),UploadsActivity.this);
                    qrCodeList.add(qrCode);
                    Log.d(TAG,"ADDED: " + qrCode.getName());
                }

                //creates pdf
                Pdf pdf = new Pdf(qrCodeList);
                pdf.createPdf();
            }
        });

    }


}
