package com.liu.dance.util;

import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 舞动的心 on 2018/3/5.
 */

public class HttpUtil {
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000,TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS).build();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");


    //使用Get方式获取服务器上数据
    public static void sendOkHttpRequest(final String address, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //使用Post方式向服务器上提交数据并获取返回提示数据
    public static void sendOkHttpResponse(final String address, final RequestBody requestBody, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
//        JSONObject object; OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    public static void postFile(String url,  final ProgressListener listener, okhttp3.Callback callback, File...files){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Log.i("huang","files[0].getName()=="+files[0].getName());
        //第一个参数要与Servlet中的一致
        builder.addFormDataPart("myfile",files[0].getName(), RequestBody.create(MediaType.parse("application/octet-stream"),files[0]));

        MultipartBody multipartBody = builder.build();

        Request request  = new Request.Builder().url(url).post(new ProgressRequestBody(multipartBody,listener)).build();
        okHttpClient.newCall(request).enqueue(callback);
    }


}
