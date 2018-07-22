package com.liu.dance.person;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.allen.library.SuperTextView;
import com.liu.dance.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendInfoActivity extends AppCompatActivity {
    public static String friend_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_friend);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });

        Intent intent = getIntent();
        String data = intent.getStringExtra("dancer_name");
        friend_name = data;
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle(data);
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#41a5f6"));//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);//设置收缩后Toolbar上字体的颜色

        mCollapsingToolbarLayout.setStatusBarScrimColor(Color.parseColor("#41a5f6"));

        SuperTextView send_message_txt = (SuperTextView)findViewById(R.id.friend_send_message);
        send_message_txt.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
//                SmsManager sManager = SmsManager.getDefault();
//                // 创建一个PendingIntent对象
//                PendingIntent pi = PendingIntent.getActivity(FriendInfoActivity.this, 0, new Intent(), 0);
//                String message = "你好呀，今晚大家一起去跳舞哟";
//                //发送短信
//                sManager.sendTextMessage("15927175720", null, message, pi, null);
//                Toast.makeText(FriendInfoActivity.this,"短信发送成功！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FriendInfoActivity.this,MessageEditActivity.class);
                intent.putExtra("message_name", friend_name);
                startActivity(intent);

            }
        });

        SuperTextView tel_phone_txt = (SuperTextView)findViewById(R.id.friend_tel_phone);
        tel_phone_txt.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                dialPhoneNumber("15927175720");
            }
        });


    }

    private void dialPhoneNumber(String phoneNumber) {
        Intent intent =new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.item1:
//                Toast.makeText(this,"点击了item1",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FriendInfoActivity.this,FriendEditActivity.class);
                intent.putExtra("message_name", friend_name);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
