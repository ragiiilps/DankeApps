package com.example.dankeapps.content;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dankeapps.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailContent extends AppCompatActivity {
    TextView detailJudul, detailUpah, detailDeskripsi;
    ImageView detailThumbnail;
    FirebaseApp mMySecondApp;
    FirebaseFirestore mSecondDBRef;
    FirebaseStorage mSecondStorage;
    StorageReference storageReference;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_content);

        detailJudul = findViewById(R.id.detailJudul);
        detailUpah = findViewById(R.id.detailUpah);
        detailDeskripsi = findViewById(R.id.detailDeskrip);
        detailThumbnail = findViewById(R.id.detailThumbnailContent);

        initSecondFirebaseAcct();

        //terima data kontent dari main
        Intent intent = getIntent();
        String uri = intent.getStringExtra("uri");
        String Judul = intent.getStringExtra("Judul");
        int Upah = intent.getExtras().getInt("Upah");
        String Deskripsi = intent.getExtras().getString("Deskripsi");


        //display content
        detailJudul.setText(Judul);
        detailUpah.setText("Rp. " + Upah);
        detailDeskripsi.setText(Deskripsi);
        Glide.with(this).load(uri).into(detailThumbnail);

    }

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
    }
}