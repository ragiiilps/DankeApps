package com.example.dankeapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;


import com.example.dankeapps.content.DetailContent;
import com.example.dankeapps.content.ModelContent;
import com.example.dankeapps.content.PostJasa;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
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
    FirestoreRecyclerAdapter adapter;
    GridLayoutManager gridLayoutManager;
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);

        initSecondFirebaseAcct();
        init();
        getContentList();

        UpJasaBtn = findViewById(R.id.UpJasaBtn);
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
        final Query query = mSecondFirestore.collection("Content");

        final FirestoreRecyclerOptions<ModelContent> Content = new FirestoreRecyclerOptions.Builder<ModelContent>()
                .setQuery(query, ModelContent.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<ModelContent, ContentHolder>(Content) {
            @Override
            protected void onBindViewHolder(@NonNull final ContentHolder holder, final int position, @NonNull final ModelContent model) {
                holder.mJudulPst.setText(model.getJudul());
                holder.mUpahPst.setText(model.getUpah());

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

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DetailContent.class);

                    intent.putExtra();

                    startActivity(intent);
                }
            });*/
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

}
