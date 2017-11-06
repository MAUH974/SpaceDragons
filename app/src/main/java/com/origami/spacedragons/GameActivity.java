package com.origami.spacedragons;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    public static int deviceWidth, deviceHeight;
    public static int score;
    public static float zPosition;

    private GameScene gameScene;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private Intent intent;
    private ImageButton replay, map;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
        deviceHeight = displayMetrics.heightPixels;
        score = 0;
        zPosition = 0;



        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);

        setContentView(R.layout.gamescene);
        gameScene = (GameScene) findViewById(R.id.GAMESCENE);
        gameScene.setGameActivity(this);

        replay = (ImageButton) findViewById(R.id.REPLAY);
        replay.setVisibility(View.GONE);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                intent = new Intent(view.getContext(), GameActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        map = (ImageButton) findViewById(R.id.MAP);
        map.setVisibility(View.GONE);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                try {
                    intent = new Intent(view.getContext(), InputScore.class);
                    startActivityForResult(intent, 0);
                }catch(Exception e){
                    Log.d("Lancement","activer geolocalisation!");
                    System.exit(-1);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameScene.pause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameScene.resume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            zPosition = event.values[2];
            zPosition -= 9;
            gameScene.getJeanMarc().update();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public ImageButton getReplay() {
        return replay;
    }

    public ImageButton getMap() {
        return map;
    }

}
