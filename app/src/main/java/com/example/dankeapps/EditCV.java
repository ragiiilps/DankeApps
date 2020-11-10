package com.example.dankeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditCV extends AppCompatActivity {

    TextInputLayout editNama, editAlamat, editUsia,editPhone,
            editPendidikan, editTawar, editKeahlian,editPengalaman;
    Button updateCVBtn, backbtn;
    FirebaseAuth fAuth;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cv);

        editNama = findViewById(R.id.nama_editcv);
        editAlamat = findViewById(R.id.alamat_editcv);
        editUsia = findViewById(R.id.usia_editcv);
        editPhone = findViewById(R.id.phone_editcv);
        editPendidikan = findViewById(R.id.pendidikan_editcv);
        editTawar = findViewById(R.id.tawarkerja_editcv);
        editKeahlian = findViewById(R.id.keahlian_editcv);
        editPengalaman = findViewById(R.id.pengalaman_editcv);
        updateCVBtn = findViewById(R.id.update_btn);
        backbtn = findViewById(R.id.back3_btn);

        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        rootNode = FirebaseDatabase.getInstance();
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

                editNama.getEditText().setText(name);
                editAlamat.getEditText().setText(alamat);
                editUsia.getEditText().setText(usia);
                editPhone.getEditText().setText(phone);
                editPendidikan.getEditText().setText(pendidikan);
                editTawar.getEditText().setText(tawar);
                editKeahlian.getEditText().setText(keahlian);
                editPengalaman.getEditText().setText(pengalaman);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateCVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                        if(!name.equals(editNama.getEditText().getText().toString())){
                            databaseReference.child("name").setValue(editNama.getEditText().getText().toString());
                            editNama.getEditText().setText(name);
                            Toast.makeText(EditCV.this,"CV berhasil diupdate",Toast.LENGTH_SHORT).show();
                        }
                        if(!alamat.equals(editAlamat.getEditText().getText().toString())){
                            databaseReference.child("alamat").setValue(editAlamat.getEditText().getText().toString());
                            editAlamat.getEditText().setText(alamat);
                            Toast.makeText(EditCV.this,"CV berhasil diupdate",Toast.LENGTH_SHORT).show();
                        }
                        if(!usia.equals(editUsia.getEditText().getText().toString())){
                            databaseReference.child("usia").setValue(editUsia.getEditText().getText().toString());
                            editUsia.getEditText().setText(usia);
                            Toast.makeText(EditCV.this,"CV berhasil diupdate",Toast.LENGTH_SHORT).show();
                        }
                        if(!phone.equals(editPhone.getEditText().getText().toString())){
                            databaseReference.child("phone").setValue(editPhone.getEditText().getText().toString());
                            editPhone.getEditText().setText(phone);
                            Toast.makeText(EditCV.this,"CV berhasil diupdate",Toast.LENGTH_SHORT).show();
                        }
                        if(!pendidikan.equals(editPendidikan.getEditText().getText().toString())){
                            databaseReference.child("pendidikan").setValue(editPendidikan.getEditText().getText().toString());
                            editPendidikan.getEditText().setText(pendidikan);
                            Toast.makeText(EditCV.this,"CV berhasil diupdate",Toast.LENGTH_SHORT).show();
                        }
                        if(!tawar.equals(editTawar.getEditText().getText().toString())){
                            databaseReference.child("tawar").setValue(editTawar.getEditText().getText().toString());
                            editTawar.getEditText().setText(tawar);
                            Toast.makeText(EditCV.this,"CV berhasil diupdate",Toast.LENGTH_SHORT).show();
                        }
                        if(!keahlian.equals(editKeahlian.getEditText().getText().toString())){
                            databaseReference.child("keahlian").setValue(editKeahlian.getEditText().getText().toString());
                            editKeahlian.getEditText().setText(keahlian);
                            Toast.makeText(EditCV.this,"CV berhasil diupdate",Toast.LENGTH_SHORT).show();
                        }
                        if(!pengalaman.equals(editPengalaman.getEditText().getText().toString())){
                            databaseReference.child("pengalaman").setValue(editPengalaman.getEditText().getText().toString());
                            editPengalaman.getEditText().setText(pengalaman);
                            Toast.makeText(EditCV.this,"CV berhasil diupdate",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Account.class));
            }
        });
    }
}
