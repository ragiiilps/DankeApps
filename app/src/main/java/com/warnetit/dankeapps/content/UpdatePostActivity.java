package com.warnetit.dankeapps.content;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.warnetit.dankeapps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.UUID;

import timber.log.Timber;

public class UpdatePostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseApp mMySecondApp;
    FirebaseFirestore mSecondFirestore;
    FirebaseStorage mSecondStorage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    TextInputLayout mJudulPst, mUpahPst, mDeskripsiPst, mDaerahPst;
    TextView imgUrl, saveUri;
    Button mPostBtn, mImageBtn, mCancelBtn;
    ProgressBar pb;
    Spinner tag;
    String kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);
        mJudulPst = findViewById(R.id.judulPstUpdate);
        mUpahPst = findViewById(R.id.upahPstUpdate);
        mDeskripsiPst = findViewById(R.id.deskripsiPstUpdate);
        mDaerahPst = findViewById(R.id.daerahPstUpdate);
        imgUrl = findViewById(R.id.imgUrlUpdate);
        saveUri = findViewById(R.id.saveUriUpdate);
        mPostBtn = findViewById(R.id.UpdateBtn);
        mImageBtn = findViewById(R.id.imagePstBtnUpdate);
        mCancelBtn = findViewById(R.id.CancelBtn);
        pb = findViewById(R.id.progressbarupdate);
        tag = findViewById(R.id.tagSpinnerUpdate);

        //spinner kategori
        tag.setOnItemSelectedListener(this);

        initSecondFirebaseAcct();

        Intent intent = getIntent();
        String idpost = intent.getStringExtra("id");
        loadData(idpost);

        //add gambar
        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String title = mJudulPst.getEditText().getText().toString().trim();
                String[] cap = title.split(" ");
                for (int i = 0; i < cap.length; i++){
                    cap[i] = cap[i].substring(0, 1).toUpperCase() +
                            cap[i].substring(1).toLowerCase();
                }
                String judul = String.join(" ", cap);

                String Deskrpsi = mDeskripsiPst.getEditText().getText().toString().trim();
                String Pay = mUpahPst.getEditText().getText().toString().trim();
                int Upah = Integer.parseInt(Pay);
                String Kategori = kategori;
                String daerah = mDaerahPst.getEditText().getText().toString().trim();

                updateData(judul, Deskrpsi, Upah, Kategori, daerah, idpost);
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //jaga-jaga nek error pas ambil uri
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
        } else {
            signInAnonymously();
        }

    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    //masukin data & upload gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK &&
                data!=null && data.getData()!=null){
            Uri imageUrlUpdate = data.getData();
            uploadPicture(imageUrlUpdate);
        }
    }

    private void uploadPicture(Uri imageUrlUpdate) {
        String randomKey = UUID.randomUUID().toString();
        final StorageReference contentImg = storageReference.child("images/" + randomKey);
        contentImg.putFile(imageUrlUpdate)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        contentImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUri = uri;
                                imgUrl.setText(R.string.ganti);
                                saveUri.setText(downloadUri.toString());
                                saveUri.setVisibility(View.GONE);
                            }
                        });
                        Toast.makeText(UpdatePostActivity.this, "Image Uploaded",  Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UpdatePostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                    }
                });
        pb.setVisibility(View.VISIBLE);
    }

    // update data post
    private void updateData(String judul, String deskrpsi, int upah, String kategori, String daerah, String idpost) {
        String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
        String Guri = saveUri.getText().toString();
        DocumentReference noteRef = mSecondFirestore.collection("Content").document(idpost);
        noteRef.update(
                "Judul", judul,
                "Deskripsi", deskrpsi,
                "Upah", upah,
                "Kategori", kategori,
                "daerah", daerah,
                "uri", Guri,
                "createdOn", currentDateTime
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UpdatePostActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                    pb.setVisibility(View.GONE);
                }else {
                    Toast.makeText(UpdatePostActivity.this, "Failed, Please try again", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                }
            }
        });
        pb.setVisibility(View.VISIBLE);
    }

    //inisialisasi 2nd firebaseapp
    private void initSecondFirebaseAcct(){
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("danke-apps")
                .setApplicationId(getString(R.string.AppIdFire2nd))
                .setApiKey(getString(R.string.APIFire2ndKey))
                .setDatabaseUrl(getString(R.string.DatabaseUrl))
                .setStorageBucket(getString(R.string.StorageBucket))
                .build();
        try {
            FirebaseApp.initializeApp(this, options, "dankeapps");
        }
        catch (Exception e){
            Timber.tag("Firebase error").d("App already exist");
        }

        mMySecondApp = FirebaseApp.getInstance("dankeapps");
        mSecondFirestore = FirebaseFirestore.getInstance(mMySecondApp);
        mSecondStorage = FirebaseStorage.getInstance(mMySecondApp);
        storageReference = mSecondStorage.getReference();
        mAuth = FirebaseAuth.getInstance(mMySecondApp);
    }

    //auth anonymous buat dapet token storage
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
    }

    private void loadData(String idpost){
        mSecondFirestore.collection("Content").document(idpost).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String judul = documentSnapshot.getString("Judul");
                        String desc = documentSnapshot.getString("Deskripsi");
                        int upah = documentSnapshot.getLong("Upah").intValue();
                        String pay = String.valueOf(upah);
                        String daerah = documentSnapshot.getString("daerah");
                        String kategori = documentSnapshot.getString("Kategori");
                        String uri = documentSnapshot.getString("uri");

                        mJudulPst.getEditText().setText(judul);
                        mUpahPst.getEditText().setText(pay);
                        mDeskripsiPst.getEditText().setText(desc);
                        mDaerahPst.getEditText().setText(daerah);
                        saveUri.setText(uri);
                        tag.setSelection(0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdatePostActivity.this,"Failed to load data",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //spinner kategori
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        kategori = parent.getSelectedItem().toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}