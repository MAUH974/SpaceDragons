package com.origami.spacedragons;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Babar on 31/10/2017.
 */

public class InputScore extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private EditText editTextName;


    private TextView textViewLatitudeNumber, textViewLongitudeNumber;

    private LocationManager locationManager;
    private String locationProvider;

    double latitude, longitude;

    ArrayList<String> allScores;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        GameActivity.deviceWidth = displayMetrics.widthPixels;
        GameActivity.deviceHeight = displayMetrics.heightPixels;

        setContentView(R.layout.input_score);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(GameActivity.deviceWidth / 12, GameActivity.deviceHeight / 8, 0, 0);

        TextView textViewName = (TextView) findViewById(R.id.TEXTVIEWNAME);
        textViewName.setTextSize(32);
        textViewName.setLayoutParams(layoutParams);

        editTextName = (EditText) findViewById(R.id.EDITTEXTNAME);
        //bloque les caracteres spe
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        editTextName.setFilters(new InputFilter[] { filter });

        TextView textViewScore = (TextView) findViewById(R.id.TEXTVIEWSCORE);
        textViewScore.setTextSize(32);

        TextView textViewScoreNumber = (TextView) findViewById(R.id.TEXTVIEWSCORENUMBER);
        textViewScoreNumber.setTextSize(32);
        textViewScoreNumber.setText(Integer.toString(GameActivity.score));

        TextView textViewLatitude = (TextView) findViewById(R.id.TEXTVIEWLATITUDE);
        textViewLatitude.setTextSize(32);

        textViewLatitudeNumber = (TextView) findViewById(R.id.TEXTVIEWLATITUDENUMBER);
        textViewLatitudeNumber.setTextSize(32);

        TextView textViewLongitude = (TextView) findViewById(R.id.TEXTVIEWLONGITUDE);
        textViewLongitude.setTextSize(32);

        textViewLongitudeNumber = (TextView) findViewById(R.id.TEXTVIEWLONGITUDENUMBER);
        textViewLongitudeNumber.setTextSize(32);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);

        locationProvider = providers.get(0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        } else {
            locationManager.requestLocationUpdates(locationProvider, 5000, 10, this);
            Location location = locationManager.getLastKnownLocation(locationProvider);

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            textViewLatitudeNumber.setText(String.format("%.5g%n", latitude));
            textViewLongitudeNumber.setText(String.format("%.5g%n", longitude));
        }

        Button delete = (Button) findViewById(R.id.DELETE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   POUR VIDER LE FICHIER DES SCORES!
                FileOutputStream output2 = null;
                try {
                    output2 = openFileOutput("output.txt", MODE_WORLD_READABLE);
                    output2.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        Button save = (Button) findViewById(R.id.SAVE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (String.valueOf(editTextName.getText()).equals("")) {
                    editTextName.setText("REMPLIR ICI!");
                } else {
                    try {
                        remplir(String.valueOf(editTextName.getText()), Integer.toString(GameActivity.score), Double.toString(latitude), Double.toString(longitude));
                    } catch (IOException e) {
                        editTextName.setText("PROBLEME REMPLIR");
                    }
                    try {
                        finish();
                        Intent intent = new Intent(view.getContext(), Map.class);
                        intent.putExtra("allScoresIntent", allScores);
                        startActivityForResult(intent, 0);
                    } catch (Exception e) {
                        editTextName.setText("PROBLEME G-MAP");
                    }
                }
            }
        });
    }

    public void remplir(String name, String score, String latitude, String longitude) throws IOException {

        name+= "/";
        score+= "/";
        latitude+= "/";
        longitude+= "/";


        //recopie du fichier octet par octet
        FileInputStream input = openFileInput("output.txt");
        allScores = new ArrayList<>();

        int octet;
        String ancienScore = "";
        while ((octet = input.read()) != -1) {
            if (((char) octet) == '/') {
                allScores.add(ancienScore+'/');
                ancienScore = "";
            } else {
                ancienScore += (char) octet;
            }
        }
        input.close();

        //ecrase/ecrit dans le fichier avec les nouvelles valeurs "tabAnciensScores + nouveaux: nom,score,latitude,longitude"
        allScores.add(name);
        allScores.add(score);
        allScores.add(latitude);
        allScores.add(longitude);

        FileOutputStream output = openFileOutput("output.txt", MODE_WORLD_READABLE);
        for (String s : allScores) {
            output.write(s.getBytes());
        }
        output.close();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
