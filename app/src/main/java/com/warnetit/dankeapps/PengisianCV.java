package com.warnetit.dankeapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PengisianCV extends AppCompatActivity {


    TextInputLayout mNama, mAlamat, mUsia, mPhone,mPendidikan,mTawar,mKeahlian,mPengalaman;
    Button mBack,mSimpan;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    RadioGroup radioGroup;
    TextView kelaminText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengisian_cv);

        mNama = findViewById(R.id.nama_cv);
        mAlamat = findViewById(R.id.alamat_cv);
        mUsia = findViewById(R.id.usia_cv);
        mPhone = findViewById(R.id.phone_cv);
        mBack = findViewById(R.id.back2_btn);
        mSimpan = findViewById(R.id.simpan_btn);
        mPendidikan = findViewById(R.id.pendidikan_cv);
        kelaminText = findViewById(R.id.kelamin_text);
        mTawar = findViewById(R.id.tawarkerja_cv);
        mKeahlian = findViewById(R.id.keahlian_cv);
        mPengalaman = findViewById(R.id.pengalaman_cv);
        radioGroup = findViewById(R.id.radio_Btn);
        fAuth = FirebaseAuth.getInstance();

        mSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateName()|!validateAlamat()|!validateUsia()|!validatePhone()|!validatePendidikan()|!validateTawar()|!validateKeahlian()|!validatePengalaman()|!validateGender()){
                    return;
                }

                String name = mNama.getEditText().getText().toString().trim();
                String alamat = mAlamat.getEditText().getText().toString().trim();
                String usia = mUsia.getEditText().getText().toString().trim();
                String phone = mPhone.getEditText().getText().toString().trim();
                String pendidikan = mPendidikan.getEditText().getText().toString().trim();
                String tawar = mTawar.getEditText().getText().toString().trim();
                String keahlian = mKeahlian.getEditText().getText().toString().trim();
                String pengalaman = mPengalaman.getEditText().getText().toString().trim();
                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selected_gender = radioGroup.findViewById(checkedId);
                String gender = selected_gender.getText().toString().trim();
                String userId = fAuth.getCurrentUser().getUid();
                rootNode = FirebaseDatabase.getInstance();
                databaseReference = rootNode.getReference("Users");
                DataHelperClass dataHelperClass = new DataHelperClass (name,alamat,usia,phone,pendidikan,tawar,keahlian,pengalaman,gender);
                databaseReference.child(userId).child("CV").setValue(dataHelperClass);

                Toast.makeText(PengisianCV.this,"CV berhasil dibuat",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Account.class));
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (getApplicationContext(),Account.class));
            }
        });
    }

    private boolean validateName() {
        String name = mNama.getEditText().getText().toString().trim();
        if(name.isEmpty()){
            mNama.setError("Data tidak boleh kosong");
            return false;
        }else{
            mNama.setError(null);
            mNama.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateAlamat() {
        String alamat = mAlamat.getEditText().getText().toString().trim();
        if(alamat.isEmpty()){
            mAlamat.setError("Data tidak boleh kosong");
            return false;
        }else{
            mAlamat.setError(null);
            mAlamat.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateUsia() {
        String usia = mUsia.getEditText().getText().toString().trim();
        if(usia.isEmpty()){
            mUsia.setError("Data tidak boleh kosong");
            return false;
        }else{
            mUsia.setError(null);
            mUsia.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePhone() {
        String phone = mPhone.getEditText().getText().toString().trim();
        if(phone.isEmpty()){
            mPhone.setError("Data tidak boleh kosong");
            return false;
        }else{
            mPhone.setError(null);
            mPhone.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePendidikan() {
        String pendidikan = mPendidikan.getEditText().getText().toString().trim();
        if(pendidikan.isEmpty()){
            mPendidikan.setError("Data tidak boleh kosong");
            return false;
        }else{
            mPendidikan.setError(null);
            mPendidikan.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateTawar() {
        String tawar = mTawar.getEditText().getText().toString().trim();
        if(tawar.isEmpty()){
            mTawar.setError("Data tidak boleh kosong");
            return false;
        }else{
            mTawar.setError(null);
            mTawar.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateKeahlian() {
        String keahlian = mKeahlian.getEditText().getText().toString().trim();
        if(keahlian.isEmpty()){
            mKeahlian.setError("Data tidak boleh kosong");
            return false;
        }else{
            mKeahlian.setError(null);
            mKeahlian.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePengalaman() {
        String pengalaman = mPengalaman.getEditText().getText().toString().trim();
        if(pengalaman.isEmpty()){
            mPengalaman.setError("Data tidak boleh kosong");
            return false;
        }else{
            mPengalaman.setError(null);
            mPengalaman.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateGender() {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selected_gender = radioGroup.findViewById(checkedId);

        if (selected_gender == null) {
            Toast.makeText(PengisianCV.this, "Tolong pilih jenis kelamin Anda", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
