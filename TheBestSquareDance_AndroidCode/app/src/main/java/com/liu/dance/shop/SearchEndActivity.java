package com.liu.dance.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.liu.dance.R;

public class SearchEndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_end);
        getSupportActionBar().hide();
        TextView btn_back = (TextView) findViewById(R.id.btn_shop_serach_end_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
