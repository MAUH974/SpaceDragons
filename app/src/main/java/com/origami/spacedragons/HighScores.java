package com.origami.spacedragons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Babar on 03/11/2017.
 */

public class HighScores extends AppCompatActivity {

    ArrayList<String> allScores;
    TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //On recupere la liste des scores
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            allScores= null;
        } else {
            allScores= extras.getStringArrayList("allScoresIntent");
        }

        setContentView(R.layout.print_high_scores);

        TextView textViewScore = (TextView) findViewById(R.id.SCORESLIST);
        textViewScore.setTextSize(15);
     //   allScores = new ArrayList<>();
     //   allScores.add("1er");

        String accumulation = "";
        int i=0;
        for (String s : allScores) {
            i++;
            if(i%4==1) accumulation+= "Mr ";
            if(i%4==2) accumulation+= "a fait le score : ";
            if(i%4==3) accumulation+= "Latitude : ";
            if(i%4==0) accumulation+= "Longitude : ";
            s = s.replace(s.substring(s.length()-1), "");
            accumulation+= s + '\n';
            if(i%4==0) accumulation+= '\n';
        }
        textViewScore.setText(accumulation);

    }



}
