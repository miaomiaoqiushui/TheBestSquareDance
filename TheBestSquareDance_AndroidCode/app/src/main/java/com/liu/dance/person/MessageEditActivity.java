package com.liu.dance.person;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.liu.dance.R;

public class MessageEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_edit);

        getSupportActionBar().setTitle("新建短信");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String data = intent.getStringExtra("message_name");
        SuperTextView message_name = (SuperTextView)findViewById(R.id.message_get_name);
        message_name.setRightString(data);
        final EditText send_mesg = (EditText)findViewById(R.id.message_friend_send);
        final Button send_btn = (Button)findViewById(R.id.btn_send_message);
        send_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SmsManager sManager = SmsManager.getDefault();
                // 创建一个PendingIntent对象
                PendingIntent pi = PendingIntent.getActivity(MessageEditActivity.this, 0, new Intent(), 0);
                String message = "";
                if(send_mesg.getText().toString().equals("") || send_mesg.getText().toString().equals(null))
                    message = "你好呀，今晚大家一起去跳舞哟";
                else
                    message = send_mesg.getText().toString();
                //发送短信
                sManager.sendTextMessage("15927175720", null, message, pi, null);
                Toast.makeText(MessageEditActivity.this,"短信发送成功！",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
