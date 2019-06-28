package com.example.qrpacking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AddBoxActivity {

    private TextView accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().setTitle("Account");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        accountName = findViewById(R.id.accountName);
        accountName.setText(user.getEmail());


    }
}
