package com.example.dankeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity {
    TextInputLayout mName, mUsername, mEmail, mNum, mBirth, mPassword;
    Button registerBtn;
    TextView loginText;
    ProgressBar mprogressBar;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    RadioGroup radioGroup;

    public Boolean validateName (){
        String val = mName.getEditText().getText().toString();

        if (val.isEmpty()){
            mName.setError("Data tidak boleh kosong");
            return false;
        }
        else{
            mName.setError(null);
            mName.setErrorEnabled(false);
            return true;
        }
    }
    public Boolean validateUsername (){
        String val = mUsername.getEditText().getText().toString();

        if (val.isEmpty()){
            mUsername.setError("Data tidak boleh kosong");
            return false;
        }else if(val.length()>=15){
            mUsername.setError("Username terlalu panjang");
            return false;
        }
        else{
            mUsername.setError(null);
            mUsername.setErrorEnabled(false);
            return true;
        }
    }
    public Boolean validateEmail (){
        String val = mEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            mEmail.setError("Data tidak boleh kosong");
            return false;
        } else if(!val.matches(emailPattern)){
            mEmail.setError("Invalid Email");
            return false;
        }
        else{
            mEmail.setError(null);
            mEmail.setErrorEnabled(false);
            return true;
        }
    }
    public Boolean validatePhone (){
        String val = mNum.getEditText().getText().toString();

        if (val.isEmpty()){
            mNum.setError("Data tidak boleh kosong");
            return false;
        }
        else{
            mNum.setError(null);
            mNum.setErrorEnabled(false);
            return true;
        }
    }
    public Boolean validateBirth (){
        String val = mBirth.getEditText().getText().toString();

        if (val.isEmpty()){
            mBirth.setError("Data tidak boleh kosong");
            return false;
        }
        else{
            mBirth.setError(null);
            mBirth.setErrorEnabled(false);
            return true;
        }
    }
    public Boolean validatePassword (){
        String val = mPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z0-9._-])" +      //any letter
                ".{8,}" +                    //at least 8 character
                "$";

        if (val.isEmpty()){
            mPassword.setError("Data tidak boleh kosong");
            return false;
        } else if(!val.matches(passwordVal)){
            mPassword.setError("Passward terlalu lemah");
            return false;
        }
        else{
            mPassword.setError(null);
            mPassword.setErrorEnabled(false);
            return true;
        }
    }
    public Boolean validateGender () {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selected_gender = radioGroup.findViewById(checkedId);

        if(selected_gender==null){
            Toast.makeText(Register.this,"Tolong pilih jenis kelamin Anda",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.fullname_edit);
        mUsername = findViewById(R.id.username_edit);
        mEmail = findViewById(R.id.email_edit);
        mNum = findViewById(R.id.num_edit);
        mBirth = findViewById(R.id.birth_edit);
        mPassword = findViewById(R.id.password_edit);
        registerBtn = findViewById(R.id.register_button);
        loginText = findViewById(R.id.login_text);
        mprogressBar = findViewById(R.id.progress_bar);
        radioGroup = findViewById(R.id.radioBtn);
        fAuth = FirebaseAuth.getInstance();

//        //Tombol Register
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getEditText().getText().toString().trim();
                String password = mPassword.getEditText().getText().toString().trim();

                    if (!validateName()|!validateUsername()| !validateEmail()| !validatePassword()| !validatePhone()| !validateBirth()|!validateGender()){
                        return;
                    }



                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                         String name = mName.getEditText().getText().toString().trim();
                         String username = mUsername.getEditText().getText().toString().trim();
                         String phone = mNum.getEditText().getText().toString().trim();
                         String datebirth = mBirth.getEditText().getText().toString().trim();
                         String email = mEmail.getEditText().getText().toString().trim();
                         String password = mPassword.getEditText().getText().toString().trim();
                         int checkedId = radioGroup.getCheckedRadioButtonId();
                         RadioButton selected_gender = radioGroup.findViewById(checkedId);
                         String gender = selected_gender.getText().toString().trim();
                         String userId = fAuth.getCurrentUser().getUid();

                         rootNode = FirebaseDatabase.getInstance();
                         databaseReference = rootNode.getReference("Users");
                         UserHelperClass hiperClass = new UserHelperClass(name,username,datebirth,phone,email,password,gender);
                         databaseReference.child(userId).setValue(hiperClass);

                             Toast.makeText(Register.this,"User berhasil dibuat", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(getApplicationContext(), Login.class));
                         }else {
                             Toast.makeText(Register.this,"Error!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                             mprogressBar.setVisibility(View.GONE);
                         }
                     }
                });

                    mprogressBar.setVisibility(View.VISIBLE);


            }
        });
//        //End tombol register

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (getApplicationContext(),Login.class));
            }
        });
    }
}
