package com.example.dankeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dankeapps.content.DetailContent;
import com.example.dankeapps.content.ModelContent;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class Favorite extends AppCompatActivity {

    FirebaseApp mMySecondApp;
    FirebaseFirestore mSecondFirestore;
    RecyclerView mRecyclerView;
    GridLayoutManager gridLayoutManager;
    FirestoreRecyclerAdapter<ModelContent, Favorite.ContentHolder> adapter;
    FirebaseAuth fAuth;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);

        bottomNavigationView.setSelectedItemId(R.id.menu_fav);
        mRecyclerView = findViewById(R.id.recyclerViewFav);
        fAuth = FirebaseAuth.getInstance();
        Uid = fAuth.getCurrentUser().getUid();

        init();
        initSecondFirebaseAcct();
        getContentList();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_home :
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_fav :
                        return true;
                    case R.id.menu_account :
                        startActivity(new Intent(getApplicationContext(), Account.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
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
        mSecondFirestore = FirebaseFirestore.getInstance(mMySecondApp);
    }

    //metode ambil konten dari firestore
    private void getContentList() {
        Query query = mSecondFirestore.collection("Fav")
                .document(Uid).collection("user");

        FirestoreRecyclerOptions<ModelContent> Content = new FirestoreRecyclerOptions.Builder<ModelContent>()
                .setQuery(query, ModelContent.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<ModelContent, Favorite.ContentHolder>(Content) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final Favorite.ContentHolder holder, final int position, @NonNull final ModelContent model) {
                holder.mJudulPst.setText(model.getJudul());
                holder.mUpahPst.setText("Rp. " + model.getUpah());
                Glide.with(getApplicationContext()).load(model.getUri()).into(holder.mThumbnail);

                //passing data ke detail konten
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), DetailContent.class);

                        String id = model.getId();
                        String uri = model.getUri();
                        String Judul = model.getJudul();
                        int Upah = model.getUpah();
                        String Deskripsi = model.getDeskripsi();

                        i.putExtra("id", id);
                        i.putExtra("uri", uri);
                        i.putExtra("Judul", Judul);
                        i.putExtra("Upah", Upah);
                        i.putExtra("Deskripsi", Deskripsi);

                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public Favorite.ContentHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.model_content_layout, group, false);
                return new Favorite.ContentHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e){
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    public class ContentHolder extends RecyclerView.ViewHolder{
        private TextView mJudulPst, mUpahPst;
        private ImageView mThumbnail;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            mJudulPst = itemView.findViewById(R.id.card_content_judul);
            mUpahPst = itemView.findViewById(R.id.card_content_upah);
            mThumbnail = itemView.findViewById(R.id.content_thumbnail);

        }
    }

    //inisialisasi recylerview
    private void init() {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
