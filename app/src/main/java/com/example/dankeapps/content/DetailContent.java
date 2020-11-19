package com.example.dankeapps.content;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dankeapps.R;

public class DetailContent extends AppCompatActivity {
    TextView detailJudul, detailUpah, detailDeskripsi;
    ImageView detailThumbnail;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_content);

        detailJudul = findViewById(R.id.detailJudul);
        detailUpah = findViewById(R.id.detailUpah);
        detailDeskripsi = findViewById(R.id.detailDeskrip);


        Intent intent = getIntent();
        String Judul = intent.getStringExtra("Judul");
        String Upah = intent.getExtras().getString("Upah");
        String Deskripsi = intent.getExtras().getString("Deskripsi");

        detailJudul.setText(Judul);
        detailUpah.setText("Rp. " + Upah);
        detailDeskripsi.setText(Deskripsi);

    }
}