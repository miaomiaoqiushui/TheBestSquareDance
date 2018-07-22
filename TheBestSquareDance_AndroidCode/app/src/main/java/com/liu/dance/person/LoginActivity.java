package com.liu.dance.person;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.liu.dance.MainActivity;
import com.liu.dance.R;
import com.liu.dance.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.login_edt_username);
        password = (EditText)findViewById(R.id.login_edt_pwd);
        Button btn_login = (Button)findViewById(R.id.login_btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(username.getText().toString().equals("") || password.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this, "用户名或者密码不能为空，请重新登陆！", Toast.LENGTH_LONG).show();
                else {
                    HttpUtil.sendOkHttpRequest("http://120.79.82.151/login", new okhttp3.Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // 在这里根据返回内容执行具体的逻辑
                            showResponse(response.body().string());
                        }
                        @Override
                        public void onFailure(Call call, IOException e) {
                            // 在这里对异常情况进行处理
                        }
                    });
                }
            }
        });
        getSupportActionBar().setTitle("最炫广场舞");
//        getSupportActionBar()
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void click_register(View v) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //解析GET方式请求返回JSON数据
    private void parseJSONWithJSONObject(String jsonData, String txt_name, String txt_pwd) {
        try {
            Boolean judge = false;
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String passward = jsonObject.getString("passward");
                if(name.equals(txt_name) && passward.equals(txt_pwd)) {
//                    Toast.makeText(LoginActivity.this, "登陆成功！！！", Toast.LENGTH_SHORT).show();
                    judge = true;
                    break;
                }
            }
            if(!judge)
                Toast.makeText(LoginActivity.this, "用户名或者密码错误，请重新登陆！", Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                parseJSONWithJSONObject(response, username.getText().toString(), password.getText().toString());
            }
        });
    }


}
