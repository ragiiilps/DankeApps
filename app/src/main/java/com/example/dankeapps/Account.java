package com.example.dankeapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dankeapps.content.PostJasa;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class Account extends AppCompatActivity {

    Toolbar toolbar;
    TextView namaText,Vnama,Valamat,Vusia,Vphone,Vpendidikan,Vtawar,Vkeahlian,Vpengalaman, Vcv, Vkelamin,
            Pnama, Palamat, Pusia, Pphone, Ppendidikan, Ptawar, Pkeahlian, Ppengalaman, Pkelamin, simpanUri;
    ImageView profilPic;
    Button notifBtn,editInfoBtn,editCVBtn,logoutBtn,isiCVBtn;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);

        toolbar = findViewById(R.id.toolbar);

        simpanUri = findViewById(R.id.simpanUri);
        Vkelamin = findViewById(R.id.kelamincv_text);
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
        Pnama = findViewById(R.id.place_nama);
        Palamat = findViewById(R.id.place_alamat);
        Pusia = findViewById(R.id.place_usia);
        Pphone = findViewById(R.id.place_phone);
        Pkelamin = findViewById(R.id.place_kelamin);
        Ppendidikan = findViewById(R.id.place_pendidikan);
        Ptawar = findViewById(R.id.place_tawar);
        Pkeahlian = findViewById(R.id.place_keahlihan);
        Ppengalaman = findViewById(R.id.place_pengalaman);
        logoutBtn = findViewById(R.id.logout_btn);
        namaText = findViewById(R.id.nama_text);
        profilPic = findViewById(R.id.account_pic);
        notifBtn = findViewById(R.id.notif_btn);
        editInfoBtn = findViewById(R.id.editprofil_btn);
        editCVBtn = findViewById(R.id.editcv_btn);
        storage = FirebaseStorage.getInstance();
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
                String gender = dataSnapshot.child("gender").getValue(String.class);

                Pnama.setText(name);
                Palamat.setText(alamat);
                Pusia.setText(usia);
                Pphone.setText(phone);
                Ppendidikan.setText(pendidikan);
                Ptawar.setText(tawar);
                Pkeahlian.setText(keahlian);
                Ppengalaman.setText(pengalaman);
                Pkelamin.setText(gender);
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
                //finish();
                //startActivity(new Intent(getApplicationContext(),Login.class));
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

    private long backPressedTime;
    private Toast backToast;
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            moveTaskToBack(true);
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
