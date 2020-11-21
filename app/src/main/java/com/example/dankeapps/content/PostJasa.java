package com.example.dankeapps.content;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dankeapps.R;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class PostJasa extends AppCompatActivity {

    TextInputLayout mJudulPst, mUpahPst, mDeskripsiPst;
    TextView imgUrl, saveUri;
    Button mPostBtn, mImageBtn;
    ProgressBar pb;
    FirebaseAuth mAuth;
    FirebaseApp mMySecondApp;
    FirebaseFirestore mSecondDBRef;
    FirebaseStorage mSecondStorage;
    StorageReference storageReference;
    Uri imageUrl;

    //inisialisasi 2nd firebaseapp
    private void initSecondFirebaseAcct(){
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("danke-apps")
                .setApplicationId("1:680412911024:android:339505bcd4b06c9b527275")
                .setApiKey("AIzaSyB52R0Y-ZM2_4xHmxUNBp2Avw0oEGVYGpE")
                .setDatabaseUrl("https://danke-apps.firebaseio.com")
                .setStorageBucket("danke-apps.appspot.com")
                .build();
        try {
            FirebaseApp.initializeApp(this, options, "dankeapps");
        }
        catch (Exception e){
            Log.d("Firebase error", "App already exist");
        }

        mMySecondApp = FirebaseApp.getInstance("dankeapps");
        mSecondDBRef = FirebaseFirestore.getInstance(mMySecondApp);
        mSecondStorage = FirebaseStorage.getInstance(mMySecondApp);
        storageReference = mSecondStorage.getReference();
        mAuth = FirebaseAuth.getInstance(mMySecondApp);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_jasa);

        mJudulPst = findViewById(R.id.judulPst);
        mDeskripsiPst = findViewById(R.id.deskripsiPst);
        mUpahPst = findViewById(R.id.upahPst);
        mPostBtn = findViewById(R.id.PostBtn);
        mImageBtn = findViewById(R.id.imagePstBtn);
        imgUrl = findViewById(R.id.imgUrl);
        saveUri = findViewById(R.id.saveUri);
        pb = findViewById(R.id.progressbar);


        initSecondFirebaseAcct();

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        //ambil data input konten
        mPostBtn.setOnClickListener(new View.OnClickListener() {
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
                String Upah = mUpahPst.getEditText().getText().toString().trim();

                uploadData(judul, Upah, Deskrpsi);
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
        } else {
            signInAnonymously();
        }
    }

    //metode pilih gambar dari galleri
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    //masukin data gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK &&
                data!=null && data.getData()!=null){
            imageUrl = data.getData();
            uploadPicture();
        }
    }

    //upload gambar ke Firebase storage
    private void uploadPicture() {
        String randomKey = UUID.randomUUID().toString();
        final StorageReference contentImg = storageReference.child("images/" + randomKey);
        contentImg.putFile(imageUrl)
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
                        Toast.makeText(PostJasa.this, "Image Uploaded",  Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(PostJasa.this, "Failed", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                    }
                });
        pb.setVisibility(View.VISIBLE);
    }

    //metode upload data ke firestore
    private void uploadData(String judul, String Upah, String Deskrpsi) {
        String id = UUID.randomUUID().toString();
        String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
        String Guri = saveUri.getText().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("createdOn", currentDateTime);
        doc.put("Judul", judul);
        doc.put("Upah", Upah);
        doc.put("Deskripsi", Deskrpsi);
        doc.put("uri", Guri);

        mSecondDBRef.collection("Content").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PostJasa.this, "Uploaded",  Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostJasa.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
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
}
