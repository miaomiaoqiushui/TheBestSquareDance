package com.liu.dance.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.liu.dance.R;
import com.liu.dance.video.AloneVideoActivity;

public class GoodsSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_search);
        getSupportActionBar().hide();
        TextView btn_back = (TextView) findViewById(R.id.btn_shop_serach_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              finish();
            }
        });
        TextView btn_serach_voice = (TextView) findViewById(R.id.txt_voice_end);
        btn_serach_voice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SearchEndActivity.class);
                startActivity(intent);
            }
        });
        TextView btn_serach_voice1 = (TextView) findViewById(R.id.search_goods_vodice_end);
        btn_serach_voice1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SearchEndActivity.class);
                startActivity(intent);
            }
        });

    }
}
