package com.example.dankeapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeScreen extends AppCompatActivity {

    ImageView welcomeLogo;
    TextView welcomeText;
    Button loginWelcome, registerWelcome;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        welcomeLogo = findViewById(R.id.logo_welcome);
        welcomeText = findViewById(R.id.text_welcome);
        loginWelcome = findViewById(R.id.login_welcome);
        registerWelcome = findViewById(R.id.register_welcome);
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        loginWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
        registerWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });


    }
}
