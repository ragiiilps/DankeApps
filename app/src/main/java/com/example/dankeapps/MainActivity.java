package com.example.dankeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.dankeapps.content.DetailContent;
import com.example.dankeapps.content.ModelContent;
import com.example.dankeapps.content.PostJasa;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class MainActivity extends AppCompatActivity {

    Button UpJasaBtn;
    RecyclerView mRecyclerView;
    FirebaseApp mMySecondApp;
    FirebaseFirestore mSecondFirestore;
    FirestoreRecyclerAdapter<ModelContent, ContentHolder> adapter;
    GridLayoutManager gridLayoutManager;
    TextView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);
        mSearchView = findViewById(R.id.BarCari);
        UpJasaBtn = findViewById(R.id.UpJasaBtn);

        initSecondFirebaseAcct();
        init();
        getContentList();
        initSearch();

        UpJasaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PostJasa.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_fav :
                        startActivity(new Intent(getApplicationContext(), Favorite.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_home :
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

    private void getContentList() {
        Query query = mSecondFirestore.collection("Content")
                .orderBy("createdOn", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ModelContent> Content = new FirestoreRecyclerOptions.Builder<ModelContent>()
                .setQuery(query, ModelContent.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<ModelContent, ContentHolder>(Content) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final ContentHolder holder, final int position, @NonNull final ModelContent model) {
                holder.mJudulPst.setText(model.getJudul());
                holder.mUpahPst.setText("Rp. " + model.getUpah());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), DetailContent.class);

                        String Judul = model.getJudul();
                        String Upah = model.getUpah();
                        String Deskripsi = model.getDeskripsi();

                        i.putExtra("Judul", Judul);
                        i.putExtra("Upah", Upah);
                        i.putExtra("Deskripsi", Deskripsi);

                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public ContentHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.model_content_layout, group, false);
                return new ContentHolder(view);
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

        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            mJudulPst = itemView.findViewById(R.id.card_content_judul);
            mUpahPst = itemView.findViewById(R.id.card_content_upah);

        }
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

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
        mSecondFirestore = FirebaseFirestore.getInstance(mMySecondApp);
    }

    private void init() {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    public void initSearch(){
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Query query;
                String cari;
                if (s.toString().isEmpty()){
                    query = mSecondFirestore.collection("Content")
                            .orderBy("createdOn", Query.Direction.DESCENDING);
                } else {
                    String[] search = s.toString().split(" ");
                    for (int i = 0; i < search.length; i++){
                        search[i] = search[i].substring(0, 1).toUpperCase() +
                                search[i].substring(1).toLowerCase();
                    }
                    cari = String.join(" ", search);

                    query = mSecondFirestore.collection("Content")
                            .whereGreaterThanOrEqualTo("Judul", cari)
                            .whereLessThan("Judul", cari+"z");
                }
                FirestoreRecyclerOptions<ModelContent> Content = new FirestoreRecyclerOptions.Builder<ModelContent>()
                        .setQuery(query, ModelContent.class)
                        .build();
                adapter.updateOptions(Content);
            }
        });
    }

}
