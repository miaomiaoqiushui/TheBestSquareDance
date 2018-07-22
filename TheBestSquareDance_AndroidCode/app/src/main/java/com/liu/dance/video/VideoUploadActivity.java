package com.liu.dance.video;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.dance.R;
import com.liu.dance.util.HttpUtil;
import com.liu.dance.util.ProgressListener;

import java.io.File;
import java.net.URI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class VideoUploadActivity extends AppCompatActivity {
    public static final String TAG = VideoUploadActivity.class.getName();
    public  final static int VEDIO_KU = 101;
    private String path = "";//文件路径
    private ProgressBar post_progress;
    private TextView post_text;
    public static String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/okhttp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);
        getSupportActionBar().setTitle("视频上传");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText video_name = (EditText)findViewById(R.id.upload_video_name);
        post_progress = (ProgressBar) findViewById(R.id.post_progress);
        post_text = (TextView) findViewById(R.id.post_text);
        findViewById(R.id.video_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleteVedio();
                video_name.setText(path);
            }
        });
        findViewById(R.id.video_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(VideoUploadActivity.this, "路径："+basePath, Toast.LENGTH_LONG).show();
                if(path.equals(""))
                    Toast.makeText(VideoUploadActivity.this, "请选择视频后，再点击上传！", Toast.LENGTH_LONG).show();
                else {
                    File file = new File( path);
                    String postUrl = "http://120.79.82.151/api/upload";

                    HttpUtil.postFile(postUrl, new ProgressListener() {
                        @Override
                        public void onProgress(long currentBytes, long contentLength, boolean done) {
                            Log.i(TAG, "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
                            int progress = (int) (currentBytes * 100 / contentLength);
                            post_progress.setProgress(progress);
                            post_text.setText(progress + "%");
                        }
                    }, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response != null) {
                                String result = response.body().string();
                                Log.i(TAG, "result===" + result);
                            }
                        }
                    }, file);

                }

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

    public void seleteVedio() {
        // TODO 启动相册
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,VideoUploadActivity.VEDIO_KU);
    }

    /**
     * 选择回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // TODO 视频
            case VideoUploadActivity.VEDIO_KU:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        uri = geturi(this, data);
                        File file = null;
                        if (uri.toString().indexOf("file") == 0) {
                            file = new File(new URI(uri.toString()));
                            path = file.getPath();
                        } else {
                            path = getPath(uri);
                            file = new File(path);
                        }
                        if (!file.exists()) {
                            break;
                        }
                        if (file.length() > 100 * 1024 * 1024) {
//                            "文件大于100M";
                            break;
                        }
                        //视频播放
//                        mVideoView.setVideoURI(uri);
//                        mVideoView.start();
                        //开始上传视频，
//                        submitVedio();
                    } catch (Exception e) {
                        String  a=e+"";
                    } catch (OutOfMemoryError e) {
                        String  a=e+"";
                    }
                }
                break;
        }

    }

    public static Uri geturi(Context context, android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                        Log.i("urishi", uri.toString());
                    }
                }
            }
        }
        return uri;
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
