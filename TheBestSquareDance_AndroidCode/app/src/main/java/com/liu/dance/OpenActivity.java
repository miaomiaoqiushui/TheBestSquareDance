package com.liu.dance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.liu.dance.person.LoginActivity;

public class OpenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        getSupportActionBar().hide();   //隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //实现全屏，隐藏手机顶部时间相关信息显示
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent (OpenActivity.this,LoginActivity.class);
                startActivity(intent);
                OpenActivity.this.finish();
            }
        }, 1000);
    }
}
