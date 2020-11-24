package com.example.dankeapps.content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dankeapps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayCV extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    TextView Tusername,Tname,Talamat,Tusia,Tphone,Tpendidikan,Tkeahlian,Tpengalaman,Tgender,Ttawar,Temail;
    Button back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cv);
        Tusername = findViewById(R.id.cvUsername);
        Tname = findViewById(R.id.cvName);
        Tusia = findViewById(R.id.cvusia);
        Talamat = findViewById(R.id.cvalamat);
        Tpendidikan = findViewById(R.id.cvpendidikan);
        Tphone = findViewById(R.id.cvphone);
        Tkeahlian = findViewById(R.id.cvkeahlian);
        Tpengalaman = findViewById(R.id.cvpengalaman);
        Tgender = findViewById(R.id.cvgender);
        Ttawar = findViewById(R.id.cvtawar);
        Temail = findViewById(R.id.cvemail);
        back = findViewById(R.id.exitBtn);

        Intent intent = getIntent();
        String Uid = intent.getStringExtra("Uid");

        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("Users").child(Uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String username = dataSnapshot.child("username").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);
                String usia = dataSnapshot.child("usia").getValue(String.class);
                String pendidikan = dataSnapshot.child("pendidikan").getValue(String.class);
                String pengalaman = dataSnapshot.child("pengalaman").getValue(String.class);
                String tawar = dataSnapshot.child("tawar").getValue(String.class);
                String keahlian = dataSnapshot.child("keahlian").getValue(String.class);
                String alamat = dataSnapshot.child("alamat").getValue(String.class);
                String gender = dataSnapshot.child("gender").getValue(String.class);

                Tusername.setText(username);
                Tname.setText(name);
                Tusia.setText(usia);
                Tgender.setText(gender);
                Tphone.setText(phone);
                Temail.setText(email);
                Talamat.setText(alamat);
                Tpendidikan.setText(pendidikan);
                Ttawar.setText(tawar);
                Tkeahlian.setText(keahlian);
                Tpengalaman.setText(pengalaman);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DetailContent.class));
                finish();
            }
        });

    }
}
