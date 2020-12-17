package com.example.dankeapps.content;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dankeapps.MainActivity;
import com.example.dankeapps.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import timber.log.Timber;

public class EditPostActivity extends AppCompatActivity {

    RecyclerView editRecyclerView;
    FirebaseApp mMySecondApp;
    FirebaseFirestore mSecondFirestore;
    FirebaseAuth fAuth;
    FirestoreRecyclerAdapter<ModelContent, EditPostActivity.ContentHolder> adapter;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        editRecyclerView = findViewById(R.id.editRecycler);

        fAuth = FirebaseAuth.getInstance();
        String Uid = fAuth.getCurrentUser().getUid();

        initSecondFirebaseAcct();
        init();
        getContentList(Uid);

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
            Timber.tag("Firebase error").d("App already exist");
        }

        mMySecondApp = FirebaseApp.getInstance("dankeapps");
        mSecondFirestore = FirebaseFirestore.getInstance(mMySecondApp);
    }

    //metode ambil konten dari firestore
    private void getContentList(String Uid) {
        Query query = mSecondFirestore.collection("Content")
                .whereEqualTo("Uid", Uid)
                .orderBy("createdOn", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ModelContent> Content = new FirestoreRecyclerOptions.Builder<ModelContent>()
                .setQuery(query, ModelContent.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ModelContent, EditPostActivity.ContentHolder>(Content) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final EditPostActivity.ContentHolder holder, final int position, @NonNull final ModelContent model) {
                holder.mJudulPst.setText(model.getJudul());
                holder.mUpahPst.setText("Rp. " + model.getUpah());
                holder.mdaerahPst.setText(model.getDaerah());
                Glide.with(getApplicationContext()).load(model.getUri()).into(holder.mThumbnail);

                //passing data ke detail konten
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), UpdatePostActivity.class);

                        String id = model.getId();

                        i.putExtra("id", id);
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public EditPostActivity.ContentHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.model_content_layout, group, false);
                return new EditPostActivity.ContentHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e){
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        editRecyclerView.setAdapter(adapter);
    }

    //holder dari content
    public class ContentHolder extends RecyclerView.ViewHolder{
        private TextView mJudulPst, mUpahPst, mdaerahPst;
        private ImageView mThumbnail;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            mJudulPst = itemView.findViewById(R.id.card_content_judul);
            mUpahPst = itemView.findViewById(R.id.card_content_upah);
            mThumbnail = itemView.findViewById(R.id.content_thumbnail);
            mdaerahPst = itemView.findViewById(R.id.card_content_daerah);

        }
    }

    //inisialisasi recylerview
    private void init() {
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        editRecyclerView.setLayoutManager(gridLayoutManager);
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