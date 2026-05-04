package com.example.campuscompass;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView searchBox;
    private FusedLocationProviderClient fusedLocationClient;

    private final HashMap<String, LatLng> campusLocations = new HashMap<>();

    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchBox = findViewById(R.id.searchBox);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        loadCampusLocations();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                campusLocations.keySet().toArray(new String[0])
        );

        searchBox.setAdapter(adapter);

        searchBox.setOnItemClickListener((parent, view, position, id) -> {
            String selectedLocation = parent.getItemAtPosition(position).toString();
            moveToCampusLocation(selectedLocation);
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void loadCampusLocations() {
        campusLocations.put("TU Dublin Blanchardstown", new LatLng(53.3937, -6.3772));
        campusLocations.put("Main Entrance", new LatLng(53.3934, -6.3770));
        campusLocations.put("Library", new LatLng(53.3939, -6.3765));
        campusLocations.put("Canteen", new LatLng(53.3941, -6.3771));
        campusLocations.put("Sports Hall", new LatLng(53.3945, -6.3780));
        campusLocations.put("Student Services", new LatLng(53.3936, -6.3762));
        campusLocations.put("Car Park", new LatLng(53.3929, -6.3784));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng tudBlanch = new LatLng(53.3937, -6.3772);

        for (String name : campusLocations.keySet()) {
            mMap.addMarker(new MarkerOptions()
                    .position(campusLocations.get(name))
                    .title(name));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tudBlanch, 17f));

        enableUserLocation();
    }

    private void moveToCampusLocation(String locationName) {
        LatLng location = campusLocations.get(locationName);

        if (location != null && mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 18f));

            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(locationName));
        }
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST
            );
            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18f));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        }
    }
}