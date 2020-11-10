package com.example.dankeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Account extends AppCompatActivity {

    Toolbar toolbar;
    TextView namaText,Vnama,Valamat,Vusia,Vphone,Vpendidikan,Vtawar,Vkeahlian,Vpengalaman, Vcv;
    ImageView profilPic;
    Button notifBtn,editInfoBtn,editCVBtn,logoutBtn,isiCVBtn;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);

        toolbar = findViewById(R.id.toolbar);

        isiCVBtn = findViewById(R.id.isicv_btn);
        Vcv = findViewById(R.id.view_cv);
        Vnama = findViewById(R.id.viewnama_cv);
        Valamat = findViewById(R.id.viewalamat_cv);
        Vusia = findViewById(R.id.viewusia_cv);
        Vphone = findViewById(R.id.viewphone_cv);
        Vpendidikan = findViewById(R.id.viewpendidikan_cv);
        Vkeahlian = findViewById(R.id.viewkeahlian_cv);
        Vtawar = findViewById(R.id.viewtawar_cv);
        Vpengalaman = findViewById(R.id.viewpengalaman_cv);
        logoutBtn = findViewById(R.id.logout_btn);
        namaText = findViewById(R.id.nama_text);
        profilPic = findViewById(R.id.account_pic);
        notifBtn = findViewById(R.id.notif_btn);
        editInfoBtn = findViewById(R.id.editprofil_btn);
        editCVBtn = findViewById(R.id.editcv_btn);
        fAuth = FirebaseAuth.getInstance();

        String userId = fAuth.getCurrentUser().getUid();

        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("Users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);

                namaText.setText(""+name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //VIEW CV
        databaseReference = rootNode.getReference("Users").child(userId).child("CV");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String alamat = dataSnapshot.child("alamat").getValue(String.class);
                String usia = dataSnapshot.child("usia").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);
                String pendidikan = dataSnapshot.child("pendidikan").getValue(String.class);
                String tawar = dataSnapshot.child("tawar").getValue(String.class);
                String keahlian = dataSnapshot.child("keahlian").getValue(String.class);
                String pengalaman = dataSnapshot.child("pengalaman").getValue(String.class);

                Vnama.setText("Nama : "+name);
                Valamat.setText("Alamat : "+alamat);
                Vusia.setText("Usia : "+usia);
                Vphone.setText("No.Hp : "+phone);
                Vpendidikan.setText("Pendidikan terakhir : "+pendidikan);
                Vtawar.setText("Pekerjaan yang ditawarkan : "+tawar);
                Vkeahlian.setText("Keahlian : "+keahlian);
                Vpengalaman.setText("Pengalaman bekerja : "+pengalaman);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        bottomNavigationView.setSelectedItemId(R.id.menu_account);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_fav :
                        startActivity(new Intent(getApplicationContext(), Favorite.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_account :
                        return true;
                    case R.id.menu_home  :
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        editInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AccountInfo.class));
            }
        });

        isiCVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PengisianCV.class));
            }
        });
        editCVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditCV.class));
            }
        });

    }

}
