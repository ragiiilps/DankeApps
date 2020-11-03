package com.example.dankeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {

    TextInputLayout mUsername, mPassword;
    ImageView gambarLogo;
    Button loginBtn;
    TextView registerText, forgetpassText, logoText;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;


    public Boolean validateUsername (){
        String val = mUsername.getEditText().getText().toString();

        if (val.isEmpty()) {
            mUsername.setError("Field cannot be empty");
            return false;
        }
        else{
            mUsername.setError(null);
            mUsername.setErrorEnabled(false);
            return true;
        }
    }
    public Boolean validatePassword() {
        String val = mPassword.getEditText().getText().toString();
        if (val.isEmpty()) {
            mPassword.setError("Field cannot be empty");
            return false;
        } else {
            mPassword.setError(null);
            mPassword.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.login_username);
        mPassword = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.button_login);
        registerText = findViewById(R.id.create_text);
        forgetpassText = findViewById(R.id.forget_password);
        logoText = findViewById(R.id.logo_text);
        gambarLogo = findViewById(R.id.gambar_logo);
        progressBar = findViewById(R.id.progress_bar);
        fAuth = FirebaseAuth.getInstance();

        //Tombol Login
        loginBtn.setOnClickListener(new View.OnClickListener() {

            //kemungkinan login
            @Override
            public void onClick(View v) {
                String email = mUsername.getEditText().getText().toString().trim();
                String password = mPassword.getEditText().getText().toString().trim();

                //Kemungkinan Error
                if (!validateUsername() | !validatePassword()) {
                    return;
                }else{
                    isUser();
                }
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        //end tombol login

        //Text Register
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
        //End Text Register

        //Tombol ForgetPass
        forgetpassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetEmail = new EditText(v.getContext());
                AlertDialog.Builder resetPasswordDialog = new AlertDialog.Builder(v.getContext());
                resetPasswordDialog.setTitle("Reset Password?");
                resetPasswordDialog.setMessage("Enter Your Email to Receive Reset Link!");
                resetPasswordDialog.setView(resetEmail);

                //tombol Yes
                resetPasswordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String email = resetEmail.getText().toString();
                        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent to Your Email", Toast.LENGTH_SHORT).show();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Reset Link is't Sent " + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                //End tombol Yes

                //tombol No
                resetPasswordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //End tombol no

                resetPasswordDialog.create().show();
            }
        });
        //End ForgetPass
    }

    private void isUser() {
        final String userUsername = mUsername.getEditText().getText().toString().trim();
        final String userPassword = mPassword.getEditText().getText().toString().trim();
        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("Users");

        Query checkUser = databaseReference.orderByChild("username").equalTo(userUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mUsername.setError(null);
                    mUsername.setErrorEnabled(false);

                    String passwordDB = dataSnapshot.child(userUsername).child("password").getValue(String.class);

                    if(passwordDB.equals(userPassword)){

                        mUsername.setError(null);
                        mUsername.setErrorEnabled(false);

                        String nameDB = dataSnapshot.child("name").getValue(String.class);
                        String usernameDB = dataSnapshot.child("username").getValue(String.class);
                        String emailDB = dataSnapshot.child("email").getValue(String.class);
                        String datebirthDB = dataSnapshot.child("datebirth").getValue(String.class);
                        String phoneDB = dataSnapshot.child("phone").getValue(String.class);

                        Intent intent  = new Intent(getApplicationContext(),MainActivity.class);

                        intent.putExtra("name",nameDB);
                        intent.putExtra("username",usernameDB);
                        intent.putExtra("email",emailDB);
                        intent.putExtra("datebirth",datebirthDB);
                        intent.putExtra("phone",phoneDB);

                        startActivity(intent);
                        Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                    } else {
                        mPassword.setError("Wrong Password");
                        mPassword.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else {
                    mUsername.setError("No such user exist");
                    mUsername.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}

