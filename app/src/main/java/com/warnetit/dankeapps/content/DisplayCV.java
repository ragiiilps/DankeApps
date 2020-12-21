package com.warnetit.dankeapps.content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.warnetit.dankeapps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayCV extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference databaseReference, databaseReference2;
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

                Tusername.setText(username);
                Tname.setText(name);
                Tphone.setText(phone);
                Temail.setText(email);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference2 = rootNode.getReference("Users").child(Uid).child("CV");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usia = snapshot.child("usia").getValue(String.class);
                String pendidikan = snapshot.child("pendidikan").getValue(String.class);
                String pengalaman = snapshot.child("pengalaman").getValue(String.class);
                String tawar = snapshot.child("tawar").getValue(String.class);
                String keahlian = snapshot.child("keahlian").getValue(String.class);
                String alamat = snapshot.child("alamat").getValue(String.class);
                String gender = snapshot.child("gender").getValue(String.class);

                Talamat.setText(alamat);
                Tpendidikan.setText(pendidikan);
                Ttawar.setText(tawar);
                Tkeahlian.setText(keahlian);
                Tpengalaman.setText(pengalaman);
                Tusia.setText(usia);
                Tgender.setText(gender);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
