package com.example.qrpacking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateUserActivity extends AppCompatActivity {

    DatabaseReference db;
    private FirebaseAuth mAuth;
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        db = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        final EditText nameField = findViewById(R.id.nameField);
        Button enterBtn = findViewById(R.id.enterBtn);
        //on enter button click
        //add user to db and go to AddedItemsActivity
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String email = getIntent().getStringExtra("email");
                addUser(name,email);
                startActivity(new Intent(CreateUserActivity.this, AddedItemsActivity.class));

            }
        });

    }
    //adds user to the db with uid from auth
    public void addUser(String name, String email){
        //String id = db.push().getKey();//what does .push().getKey() do??
        String id = mAuth.getCurrentUser().getUid();
        User user = new User(id,name,email);
        new UserDBWriterTask().execute(user);
    }

    //asynchronously add user to db
    private class UserDBWriterTask extends AsyncTask<User, Void, User> {

        @Override
        protected User doInBackground(User... users) {
            db.child(users[0].getId()).setValue(users[0]);
            //db.child(users[0].getId()).child("people owed").child("2wfasd").setValue(6);

            return users[0];
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            Toast.makeText(CreateUserActivity.this, "User added", Toast.LENGTH_LONG).show();
        }
    }*/

}
