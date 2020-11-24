package com.example.dankeapps.content;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.dankeapps.Favorite;
import com.example.dankeapps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DetailContent extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {
    TextView detailJudul, detailUpah, detailDeskripsi, detUser, detNama, detEmail, detHP;
    ImageView detailThumbnail;
    FirebaseApp mMySecondApp;
    FirebaseFirestore mSecondDBRef;
    FirebaseStorage mSecondStorage;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    ToggleButton favBtn;
    Button callBtn, CVbtn;
    String mUri, idKon;
    private static Bundle bundle = new Bundle();
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_detail_content);
        fAuth = FirebaseAuth.getInstance();

        detailJudul = findViewById(R.id.detailJudul);
        detailUpah = findViewById(R.id.detailUpah);
        detailDeskripsi = findViewById(R.id.detailDeskrip);
        detailThumbnail = findViewById(R.id.detailThumbnailContent);
        detUser = findViewById(R.id.detUser);
        detNama = findViewById(R.id.detNama);
        detEmail = findViewById(R.id.detEmail);
        detHP = findViewById(R.id.detPhone);
        favBtn = findViewById(R.id.favBtn);
        callBtn = findViewById(R.id.callBtn);
        CVbtn = findViewById(R.id.CVbtn);

        initSecondFirebaseAcct();

        //terima data kontent dari main
        Intent intent = getIntent();
        String uri = intent.getStringExtra("uri");
        mUri = uri;
        String Judul = intent.getStringExtra("Judul");
        int Upah = intent.getExtras().getInt("Upah");
        String hupah = String.valueOf(Upah);
        String Deskripsi = intent.getExtras().getString("Deskripsi");
        idKon = intent.getStringExtra("id");
        String user = intent.getStringExtra("user");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String Uid = intent.getStringExtra("Uid");

        //display content
        detailJudul.setText(Judul);
        detailUpah.setText(hupah);
        detailDeskripsi.setText(Deskripsi);
        Glide.with(this).load(uri).into(detailThumbnail);
        detUser.setText(user);
        detNama.setText(name);
        detEmail.setText(email);
        detHP.setText(phone);

        //favorite button
        favBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String Uid = fAuth.getCurrentUser().getUid();
                    String judul = detailJudul.getText().toString().trim();
                    String pah = detailUpah.getText().toString().trim();
                    int upah = Integer.parseInt(pah);
                    String desk = detailDeskripsi.getText().toString().trim();
                    uploadData(Uid, judul, upah, desk, mUri, idKon);

                }
                else {
                    String Uid = fAuth.getCurrentUser().getUid();
                    startActivity(new Intent(getApplicationContext(), Favorite.class));
                    deleteFav(Uid, idKon);
                }
            }
        });

        // panggil no hp
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:"+phone));
                startActivity(call);
            }
        });

        //display CV
        CVbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vCV = new Intent(getApplicationContext(), DisplayCV.class);
                vCV.putExtra("Uid", Uid);

                startActivity(vCV);
                finish();
            }
        });

        //inisialisasi maps
        mapView = findViewById(R.id.gantimaps);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }


    //map perijinan stuff
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        DetailContent.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    // database favorite
    private void deleteFav(String Uid, String idKon) {
        mSecondDBRef.collection("Fav").document(Uid).collection("user").document(idKon)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailContent.this, "Removed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailContent.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadData(String Uid, String judul, int upah, String desk, String mUri, String idKon) {
        Map<String, Object> doc = new HashMap<>();

        doc.put("Uid", Uid);
        doc.put("Judul", judul);
        doc.put("Upah", upah);
        doc.put("Deskripsi", desk);
        doc.put("uri", mUri);

        mSecondDBRef.collection("Fav").document(Uid).collection("user").document(idKon).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
        mSecondDBRef = FirebaseFirestore.getInstance(mMySecondApp);
        mSecondStorage = FirebaseStorage.getInstance(mMySecondApp);
        storageReference = mSecondStorage.getReference();
    }

    public void onCustomToggleClick(View view) {
        String UID = fAuth.getCurrentUser().getUid();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}