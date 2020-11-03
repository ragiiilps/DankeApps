package com.example.dankeapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountInfo extends AppCompatActivity {

    TextInputLayout mName, mUsername, mPhone, mEmail;
    Button simpanBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        mName = findViewById(R.id.name_info);
        mUsername = findViewById(R.id.username_info);
        mPhone = findViewById(R.id.phone_info);
        mEmail = findViewById(R.id.email_info);
        simpanBtn = findViewById(R.id.simpan_btn);
        backBtn = findViewById(R.id.back_btn);

        //ShowAllData
        showUserData();


        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Account.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Account.class));
            }
        });
    }

    private void showUserData() {
        Intent intent = getIntent();

        String fullName = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");

        mName.getEditText().setText(fullName);
        mUsername.getEditText().setText(username);
        mEmail.getEditText().setText(email);
        mPhone.getEditText().setText(phone);


    }
}
