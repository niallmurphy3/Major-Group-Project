package com.example.campuscompass;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
    private AutoCompleteTextView startPoint, endPoint;
    private FusedLocationProviderClient fusedLocationClient;

    private final HashMap<String, LatLng> campusLocations = new HashMap<>();
    private LatLng currentLocation;

    private static final int LOCATION_PERMISSION_REQUEST = 1001;
    //loads xml and initializes location services
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //start point and end point input boxes
        startPoint = findViewById(R.id.startPoint);
        endPoint = findViewById(R.id.endPoint);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //loads map amp markers
        loadCampusLocations();

        //obtains locations from hashmap
        String[] locations = campusLocations.keySet().toArray(new String[0]);

        String[] startOptions = new String[locations.length + 1];
        startOptions[0] = "Current Location";
        System.arraycopy(locations, 0, startOptions, 1, locations.length);

        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                startOptions
        );

        ArrayAdapter<String> endAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                locations
        );

        //autocomplete suggestions
        startPoint.setAdapter(startAdapter);
        endPoint.setAdapter(endAdapter);

        //takes start and end point whand draws route
        endPoint.setOnItemClickListener((parent, view, position, id) -> {
            String start = startPoint.getText().toString();
            String end = endPoint.getText().toString();
            drawRoute(start, end);
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    //all campus locations marked as markers on the map
    private void loadCampusLocations() {
        campusLocations.put("TU Dublin Blanchardstown", new LatLng(53.40480514878633, -6.378764905256287));
        campusLocations.put("Connect Building", new LatLng(53.40479528384249, -6.379297218342234));
        campusLocations.put("Block AG", new LatLng(53.404443999475205, -6.379385043127654));
        campusLocations.put("Block F", new LatLng(53.404756666616514, -6.378401339216033));
        campusLocations.put("Block E", new LatLng(53.40525306024598, -6.377836757659842));
        campusLocations.put("Block D", new LatLng(53.40574693070048, -6.377472991046981));
        campusLocations.put("Block A", new LatLng(53.40624313740624, -6.376379696779155));
        campusLocations.put("Canteen", new LatLng(53.40541934144406, -6.378592960425783));
        campusLocations.put("Gym", new LatLng(53.40579971973759, -6.381144311266247));
        campusLocations.put("Cafe", new LatLng(53.40636364363634, -6.37978217270747));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng campusCenter = new LatLng(53.40558545083936, -6.378970945656393);

        for (String name : campusLocations.keySet()) {
            mMap.addMarker(new MarkerOptions()
                    .position(campusLocations.get(name))
                    .title(name));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campusCenter, 17f));

        enableUserLocation();
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
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });
    }

    //Draws route by sendin marker coords to map
    private void drawRoute(String startName, String endName) {

        //variables to hold coords
        LatLng startLatLng;
        LatLng endLatLng = campusLocations.get(endName);

        //if it does not exist, stop
        if (endLatLng == null) return;


        //chekcs to use device location or built in locations
        if (startName.equals("Current Location")) {
            if (currentLocation == null) return;
            startLatLng = currentLocation;
        } else {
            startLatLng = campusLocations.get(startName);
        }
        //checks if it has location
        if (startLatLng == null) return;
        //builds link for google maps (start address =)
        String uri = "http://maps.google.com/maps?saddr="
                //start coords
                + startLatLng.latitude + "," + startLatLng.longitude
                //destination address =
                + "&daddr="
                //end coords
                + endLatLng.latitude + "," + endLatLng.longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
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