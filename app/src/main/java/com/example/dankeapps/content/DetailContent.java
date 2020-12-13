package com.example.dankeapps.content;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.MarkEnforcingInputStream;
import com.example.dankeapps.Favorite;
import com.example.dankeapps.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class DetailContent extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {
    TextView detailJudul, detailUpah, detailDeskripsi, detUser, detNama, detEmail, detHP;
    ImageView detailThumbnail;
    FirebaseApp mMySecondApp;
    FirebaseFirestore mSecondDBRef;
    FirebaseStorage mSecondStorage;
    StorageReference storageReference;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference, databaseReferenceCV;
    FirebaseAuth fAuth;
    ToggleButton favBtn;
    Button callBtn, CVbtn;
    String mUri, idKon, mphone, mUid, daerah;
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

        if (intent.getStringExtra("id") != null){
            idKon = intent.getStringExtra("id");
        } else {
            SharedPreferences sharedPrf = getSharedPreferences("id", MODE_PRIVATE);
            idKon = sharedPrf.getString("id", "");
        }

        loadContent();

        SharedPreferences sharedPreferences = getSharedPreferences(idKon, MODE_PRIVATE);
        favBtn.setChecked(sharedPreferences.getBoolean("value", false));

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

                    uploadData(Uid, judul, upah, desk, mUri, idKon, daerah);

                    SharedPreferences.Editor editor = getSharedPreferences(idKon, MODE_PRIVATE).edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    favBtn.setChecked(true);

                }
                else {
                    String Uid = fAuth.getCurrentUser().getUid();
                    SharedPreferences.Editor editor = getSharedPreferences(idKon, MODE_PRIVATE).edit();
                    editor.putBoolean("value", false);
                    editor.apply();
                    setResult(6);
                    deleteFav(Uid, idKon);
                    favBtn.setChecked(false);
                }
            }
        });

        // panggil no hp
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:"+mphone));
                startActivity(call);
            }
        });

        //display CV
        CVbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vCV = new Intent(getApplicationContext(), DisplayCV.class);
                vCV.putExtra("Uid", mUid);

                startActivity(vCV);
            }
        });

        //inisialisasi maps
        mapView = findViewById(R.id.gantimaps);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    //load data dari database post, user, dan cv
    public void loadContent(){
        mSecondDBRef.collection("Content").document(idKon).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String judul = documentSnapshot.getString("Judul");
                            String desc = documentSnapshot.getString("Deskripsi");
                            String link = documentSnapshot.getString("uri");
                            int upah = documentSnapshot.getLong("Upah").intValue();
                            String pay = String.valueOf(upah);
                            String Uid = documentSnapshot.getString("Uid");
                            String username = documentSnapshot.getString("username");
                            String email = documentSnapshot.getString("email");
                            daerah = documentSnapshot.getString("daerah");
                            mUri = link;
                            mUid = Uid;

                            detailJudul.setText(judul);
                            detailUpah.setText(pay);
                            detailDeskripsi.setText(desc);
                            Glide.with(DetailContent.this).load(link).into(detailThumbnail);
                            detUser.setText(username);
                            detEmail.setText(email);

                            //get data dari id user
                            rootNode = FirebaseDatabase.getInstance();
                            databaseReference = rootNode.getReference("Users").child(Uid);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String name = snapshot.child("name").getValue(String.class);
                                    String phone = snapshot.child("phone").getValue(String.class);
                                    mphone = phone;
                                    String mask = phone.replaceAll("\\w(?=\\w{4})", "X");

                                    detNama.setText(name);
                                    detHP.setText(mask);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            //ambil alamat untuk maps
                            databaseReferenceCV = rootNode.getReference("Users").child(Uid).child("CV");
                            databaseReferenceCV.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String alamat = snapshot.child("alamat").getValue(String.class);
                                    TextView tvAttent = findViewById(R.id.warning);
                                    if (alamat!=null){
                                        getLatLng(judul,alamat);
                                        tvAttent.setVisibility(View.GONE);
                                    }
                                    else {
                                        tvAttent.setVisibility(View.VISIBLE);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailContent.this,"Failed to load data",Toast.LENGTH_SHORT).show();
                    }
                });
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

    //inisialisasi map
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

    //add marker
    private void getLatLng(String judul,String alamat){
        Point point = Point.fromLngLat(110.36083,-7.78278);
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(getString(R.string.mapbox_access_token))
                .query(alamat)
                .proximity(point)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                double Latitude = response.body().features().get(0).center().latitude();
                double Longitude = response.body().features().get(0).center().longitude();
                MarkerOptions options = new MarkerOptions();
                options.title(judul);
                options.position(new LatLng(Latitude,Longitude));
                mapboxMap.addMarker(options);

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(new LatLng(Latitude,Longitude))
                                .zoom(12)
                                .build()), 4000);

            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {

            }
        });
    }

    //add lokasi device
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

    private void uploadData(String Uid, String judul, int upah, String desk, String mUri, String idKon, String daerah) {
        Map<String, Object> doc = new HashMap<>();

        doc.put("Uid", Uid);
        doc.put("Judul", judul);
        doc.put("Upah", upah);
        doc.put("Deskripsi", desk);
        doc.put("uri", mUri);
        doc.put("id", idKon);
        doc.put("daerah", daerah);

        mSecondDBRef.collection("Fav").document(Uid).collection("user").document(idKon).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DetailContent.this,"saved to favorite",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailContent.this,"Error, Please check internet connection and Try Again", Toast.LENGTH_SHORT).show();
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