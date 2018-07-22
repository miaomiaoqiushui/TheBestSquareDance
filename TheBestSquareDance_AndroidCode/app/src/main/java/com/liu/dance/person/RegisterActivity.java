package com.liu.dance.person;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.liu.dance.R;
import com.liu.dance.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private EditText username;
    private EditText password;
    private EditText nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText)findViewById(R.id.register_edt_username);
        password = (EditText)findViewById(R.id.register_edt_pwd);
        nickname = (EditText)findViewById(R.id.nick_edt_name);
        getSupportActionBar().setTitle("注册页面");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btn_register = (Button)findViewById(R.id.register_btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String json = "";
                try
                {
                    json = getJson(username.getText().toString(),password.getText().toString(),nickname.getText().toString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if(username.getText().toString().equals("") || password.getText().toString().equals("") || nickname.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "注册信息不能为空，请重新注册！", Toast.LENGTH_LONG).show();
                } else {
                    RequestBody requestBody = RequestBody.create(JSON, json);
                    HttpUtil.sendOkHttpResponse("http://120.79.82.151/register",requestBody, new okhttp3.Callback() {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //将提交到服务器数据转换为JSON格式数据字符串
    public String getJson(String name, String passward, String nickname)throws Exception {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("name", name);
        jsonParam.put("passward", passward);
        jsonParam.put("nickname",nickname);
        return jsonParam.toString();
    }

    //解析方式请求返回JSON数据
    private void parseJSONWithJSONObjectPost(String jsonData, String txt_name, String txt_pwd) {
        try {
            Boolean judge = false;
            JSONArray jsonArray = new JSONArray(jsonData);  //注意，此处是一个JSON格式数组
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String passward = jsonObject.getString("passward");
                if(name.equals(txt_name) && passward.equals(txt_pwd)) {
                    judge = true;
                    break;
                }
            }
            if(!judge || txt_name.equals("") || txt_pwd.equals(""))
                Toast.makeText(RegisterActivity.this, "注册失败，请查看网络问题或者重新注册！", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(RegisterActivity.this, "恭喜您，注册成功！！！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                parseJSONWithJSONObjectPost("["+response+"]",username.getText().toString(), password.getText().toString());
            }
        });
    }

}
