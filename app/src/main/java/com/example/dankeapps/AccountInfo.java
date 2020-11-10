package com.example.dankeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountInfo extends AppCompatActivity {

    TextInputLayout mName, mUsername, mPhone, mEmail;
    Button updateBtn, backBtn;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        mName = findViewById(R.id.name_info);
        mUsername = findViewById(R.id.username_info);
        mPhone = findViewById(R.id.phone_info);
        mEmail = findViewById(R.id.email_info);
        updateBtn = findViewById(R.id.update_btn);
        backBtn = findViewById(R.id.back_btn);

        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();

        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("Users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue(String.class);
                String username = dataSnapshot.child("username").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);

                mName.getEditText().setText(name);
                mUsername.getEditText().setText(username);
                mEmail.getEditText().setText(email);
                mPhone.getEditText().setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //ShowAllData
        //showUserData();



        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String name = dataSnapshot.child("name").getValue(String.class);
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String phone = dataSnapshot.child("phone").getValue(String.class);

                        if (!name.equals(mName.getEditText().getText().toString())) {
                            databaseReference.child("name").setValue(mName.getEditText().getText().toString());
                            mName.getEditText().setText(name);
                            Toast.makeText(AccountInfo.this, "User Data Updated", Toast.LENGTH_SHORT).show();
                        }
                        if (!username.equals(mUsername.getEditText().getText().toString())) {
                            databaseReference.child("username").setValue(mUsername.getEditText().getText().toString());
                            mUsername.getEditText().setText(username);
                            Toast.makeText(AccountInfo.this, "User Data Updated", Toast.LENGTH_SHORT).show();
                        }
                        if (!email.equals(mEmail.getEditText().getText().toString())) {
                            databaseReference.child("email").setValue(mEmail.getEditText().getText().toString());
                            mEmail.getEditText().setText(email);
                            Toast.makeText(AccountInfo.this, "User Data Updated", Toast.LENGTH_SHORT).show();
                        }
                        if (!phone.equals(mPhone.getEditText().getText().toString())) {
                            databaseReference.child("phone").setValue(mPhone.getEditText().getText().toString());
                            mPhone.getEditText().setText(phone);
                            Toast.makeText(AccountInfo.this, "User Data Updated", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Account.class));
            }
        });
    }



//    private void showUserData() {
//        Intent intent = getIntent();
//
//        String fullName = intent.getStringExtra("name");
//        String username = intent.getStringExtra("username");
//        String email = intent.getStringExtra("email");
//        String phone = intent.getStringExtra("phone");
//
//        mName.getEditText().setText(fullName);
//        mUsername.getEditText().setText(username);
//        mEmail.getEditText().setText(email);
//        mPhone.getEditText().setText(phone);
//
//
//    }
}
