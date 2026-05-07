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

import com.google.android.gms.maps.model.PolylineOptions;
import android.graphics.Color;

import java.util.HashMap;

public class CarpoolActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private final HashMap<String, LatLng> campusLocations = new HashMap<>();
    private LatLng currentLocation;

    private static final int LOCATION_PERMISSION_REQUEST = 1001;
    //loads xml and initializes location services
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //loads buildin coords
        loadCampusLocations();

        String[] locations = campusLocations.keySet().toArray(new String[0]);

        String[] startOptions = new String[locations.length + 1];
        startOptions[0] = "Current Location";
        System.arraycopy(locations, 0, startOptions, 1, locations.length);

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
        campusLocations.put("LittlePace Village - Mon - Fri - 8.40AM", new LatLng(53.40440740332811, -6.42668576074104));
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //stores map
        mMap = googleMap;

        LatLng campusCenter = new LatLng(53.40558545083936, -6.378970945656393);

        //adds all markers
        for (String name : campusLocations.keySet()) {
            mMap.addMarker(new MarkerOptions()
                    .position(campusLocations.get(name))
                    .title(name));
        }

        //defaults camera to TUD campus
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campusCenter, 17f));

        enableUserLocation();
        hardcodedCarpools();
    }

    //asks user for location permission and displays
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


        //gets last location
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });
    }
    //carpools currently hard coded into app
    private void hardcodedCarpools() {

        LatLng start = campusLocations.get("LittlePace Village - Mon - Fri - 8.40AM");
        LatLng end = campusLocations.get("TU Dublin Blanchardstown");


        mMap.addMarker(new MarkerOptions()
                .position(start)
                .title("Carpool Start"));

        mMap.addMarker(new MarkerOptions()
                .position(end)
                .title("Carpool End"));

        mMap.addPolyline(new PolylineOptions()
                .add(start, end)
                .width(8f)
                .color(Color.BLUE));
    }

}