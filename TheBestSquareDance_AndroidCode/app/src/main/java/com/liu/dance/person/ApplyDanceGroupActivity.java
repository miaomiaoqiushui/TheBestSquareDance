package com.liu.dance.person;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.liu.dance.R;

public class ApplyDanceGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_dance_group);
        getSupportActionBar().hide();
        TextView video_dance_back = (TextView) findViewById(R.id.btn_dancegroup_back);
        video_dance_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
}
