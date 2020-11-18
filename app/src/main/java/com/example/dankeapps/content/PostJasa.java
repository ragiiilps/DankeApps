package com.example.dankeapps.content;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dankeapps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class PostJasa extends AppCompatActivity {

    TextInputLayout mJudulPst, mUpahPst, mDeskripsiPst;
    Button mPostBtn;
    ProgressBar pb;
    FirebaseApp mMySecondApp;
    FirebaseFirestore mSecondDBRef;

    private void initSecondFirebaseAcct(){
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("danke-apps")
                .setApplicationId("1:680412911024:android:339505bcd4b06c9b527275")
                .setApiKey("AIzaSyB52R0Y-ZM2_4xHmxUNBp2Avw0oEGVYGpE")
                .setDatabaseUrl("https://danke-apps.firebaseio.com")
                .build();
        try {
            FirebaseApp.initializeApp(this, options, "dankeapps");
        }
        catch (Exception e){
            Log.d("Firebase error", "App already exist");
        }

        mMySecondApp = FirebaseApp.getInstance("dankeapps");
        mSecondDBRef = FirebaseFirestore.getInstance(mMySecondApp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_jasa);

        mJudulPst = findViewById(R.id.judulPst);
        mDeskripsiPst = findViewById(R.id.deskripsiPst);
        mUpahPst = findViewById(R.id.upahPst);
        mPostBtn = findViewById(R.id.PostBtn);

        pb = new ProgressBar(this);
        initSecondFirebaseAcct();

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String judul = mJudulPst.getEditText().getText().toString().trim();
                String Deskrpsi = mDeskripsiPst.getEditText().getText().toString().trim();
                String Upah = mUpahPst.getEditText().getText().toString().trim();

                uploadData(judul, Upah, Deskrpsi);
            }
        });
    }

    private void uploadData(String judul, String Upah, String Deskrpsi) {
        String id = UUID.randomUUID().toString();
        String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("createdOn", currentDateTime);
        doc.put("Judul", judul);
        doc.put("Upah", Upah);
        doc.put("Deskripsi", Deskrpsi);

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
}
