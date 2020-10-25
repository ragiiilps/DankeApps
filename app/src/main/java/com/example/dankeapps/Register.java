package com.example.dankeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.TextUtilsCompat;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText mName, mUsername, mEmail, mNum, mBirth, mPassword, mCfmPassword;
    Button registerBtn;
    TextView loginText;
    FirebaseAuth fAuth;
    ProgressBar mprogressBar;
    RadioGroup mGender;

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
        mCfmPassword = findViewById(R.id.cfmPass_edit);
        registerBtn = findViewById(R.id.register_button);
        mGender = findViewById(R.id.select_gender);
        loginText = findViewById(R.id.login_text);
        fAuth = FirebaseAuth.getInstance();
        mprogressBar = findViewById(R.id.progress_bar);


        //Tombol Register
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString().trim();
                String username = mUsername.getText().toString().trim();
                String number = mNum.getText().toString().trim();
                String datebirth = mBirth.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //kemungkinan error register
                if(TextUtils.isEmpty(name)){
                    mName.setError("Full Name is Required");
                    return;
                }
                if(TextUtils.isEmpty(username)){
                    mUsername.setError("Username is Required");
                    return;
                }
                if(TextUtils.isEmpty(number)){
                    mNum.setError("No.Hp is Required");
                    return;
                }
                if(TextUtils.isEmpty(datebirth)){
                    mBirth.setError("Date Birth is Required");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    return;
                }
                if(password.length() <6){
                    mPassword.setError("Password must be longer than 6 character");
                    return;
                }
                //End kemungkinan error

                mprogressBar.setVisibility(View.VISIBLE);


                //register the user in firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Toast.makeText(Register.this,"User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }else {
                            Toast.makeText(Register.this,"Error!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mprogressBar.setVisibility(View.GONE);
                        }
                    }
                });
                //end register user
            }
        });
        //End tombol register


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (getApplicationContext(),Login.class));
            }
        });
    }
}
