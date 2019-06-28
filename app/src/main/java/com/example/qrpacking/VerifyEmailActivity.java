package com.example.qrpacking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmailActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);



        if (user.isEmailVerified()) {
            // User is signed in
            //Log.d("USER", user.getDisplayName());
            Intent i = new Intent(this, AddBoxActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            //Log.d(TAG, "onAuthStateChanged:signed_out");
        }

        Button verify = findViewById(R.id.button);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.reload();
                if (user.isEmailVerified()) {
                    // User is signed in
                    //Log.d("USER", user.getDisplayName());
                    Intent i = new Intent(VerifyEmailActivity.this, AddBoxActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(VerifyEmailActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
