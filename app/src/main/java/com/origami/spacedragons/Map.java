package com.origami.spacedragons;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Babar on 01/11/2017.
 */

public class Map extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, LocationListener {
    private GoogleMap myMap;

    private static final long MIN_TIME_UPDATE = 5000;
    private static final long MIN_DIST_UPDATE = 10;

    private LocationManager locationManager;
    private String locationProvider;
    private boolean location = false;

    ArrayList<String> allScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        //On recupere la liste des scores
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            allScores= null;
        } else {
            allScores= extras.getStringArrayList("allScoresIntent");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
        }
        myMap.setOnMapClickListener(this);

        // Lit le fichier des scores et affiche les marqueurs
        FileInputStream input = null;
        try {
            input = openFileInput("output.txt");
            int oneByte = 0;
            String resultOfReading = "";
            int i = 0;
            String name = "";
            String score = "";
            String latitude="", longitude ="";
            LatLng posMarker;

            double offset=0.0000000;

            while ((oneByte = input.read()) != -1) {
                if ((char) oneByte == '/' ) {
                    if (i == 0){
                        name= resultOfReading;
                    }
                    if (i == 1){
                        score= resultOfReading;
                    }
                    if (i == 2){
                        latitude = resultOfReading;
                    }
                    if (i == 3){
                        longitude = resultOfReading;
                        offset+= 0.000001;
                        posMarker = new LatLng(Float.parseFloat(latitude)+offset, Float.parseFloat(longitude));
                        myMap.addMarker(new MarkerOptions().position(posMarker).title("Mr " + name + " a fait un score de : " + score));
                        i=-1;
                    }
                    resultOfReading = "";
                    i++;
                } else {
                    resultOfReading += (char) oneByte;
                }
            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePosition();
    }

    @Override
    protected void onPause() {
        Log.v(getClass().getSimpleName() + " :: ", "onPause");

        if (location) {
            try {
                locationManager.removeUpdates(this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            location = false;
        }
        super.onPause();
    }

    private void updatePosition() {
        Log.v("updatePosition", "Update in progress...");

        if (locationManager != null) {
            List<String> providers = locationManager.getProviders(true);

            if (!providers.isEmpty()) {
                location = true;
                locationProvider = providers.get(0);

                try {
                    locationManager.requestLocationUpdates(locationProvider, MIN_TIME_UPDATE, MIN_DIST_UPDATE, this);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("updatePosition", "Provider is empty");
            }
        } else {
            Log.e("updatePosition", "Location manager is empty");
        }
    }
    @Override
    public void onLocationChanged(Location location) {
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.high_scores, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.HIGHSCORES:
                Intent intent = new Intent(this, HighScores.class);
                intent.putExtra("allScoresIntent", allScores);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}