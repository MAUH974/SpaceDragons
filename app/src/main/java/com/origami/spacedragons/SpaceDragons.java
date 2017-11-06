package com.origami.spacedragons;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class SpaceDragons extends AppCompatActivity {
    private Intent intent;
    private ImageButton start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spacedragons);


        start = (ImageButton) findViewById(R.id.START);
        start.setBackgroundResource(0);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), GameActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }
}
