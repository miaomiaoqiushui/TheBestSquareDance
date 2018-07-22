package com.liu.dance.person;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.liu.dance.R;
import com.liu.dance.adapter.UploadListAdapter;
import com.liu.dance.adapter.UploadListAdapter.Callback;
import com.liu.dance.dao.ListVideo;
import com.liu.dance.data.VideoConstant;
import com.liu.dance.video.VideoUploadActivity;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;
import java.util.List;

public class MyUploadActivity extends AppCompatActivity implements Callback {
    private List<ListVideo> videoList = new ArrayList<ListVideo>();
    private boolean isNeedChecked; // 是否需要出现选择的按钮
    private TextView edit_video;
    private UploadListAdapter adapter;
    private LinearLayout select;
    private TextView all_select, all_edit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_upload);
        getSupportActionBar().hide();
        TextView video_upload_back = (TextView) findViewById(R.id.btn_video_upload_back);
        video_upload_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        initVideos();
        adapter = new UploadListAdapter(MyUploadActivity.this, R.layout.layout_upload_video_item, videoList,this);
        ListView listView1 = (ListView) findViewById(R.id.list_upload);
        listView1.setAdapter(adapter);
//        listView1.setOnItemClickListener(this);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListVideo video = videoList.get(position);

                if(!isNeedChecked)
                    Toast.makeText(MyUploadActivity.this, video.getName(), Toast.LENGTH_SHORT).show();
                else {
                    if(!video.isChecked())
                        video.setChecked(true);
                    else
                        video.setChecked(false);
                    adapter.notifyDataSetChanged();
                }

            }
        });

        select = (LinearLayout) findViewById(R.id.am_upload_select);
        all_select = (TextView) findViewById(R.id.am_upload_all_select);
        all_edit = (TextView) findViewById(R.id.am_upload_edit);


        edit_video = (TextView)findViewById(R.id.edit_upload);

        edit_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isNeedChecked) {
                    select.setVisibility(v.GONE);
                    adapter.setNeedCheck(false);
                    isNeedChecked = false;
                } else {
                    select.setVisibility(v.VISIBLE);
                    adapter.setNeedCheck(true);
                    isNeedChecked = true;
                }
                adapter.notifyDataSetChanged();
            }
        });

        all_select .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplication(), "全选啦啦啦啦啦", Toast.LENGTH_SHORT).show();
                all_edit.setTextColor(Color.parseColor("#fd5353"));
            }
        });

        all_edit .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplication(), "删除啦啦啦啦啦", Toast.LENGTH_SHORT).show();
                all_edit.setTextColor(Color.parseColor("#b9b6b6"));
            }
        });

//        FloatingActionMenu video_add = (FloatingActionMenu) findViewById(R.id.menu_add_upload_video);
//
//        video_add .setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getApplication(), "添加视频啦啦啦啦啦", Toast.LENGTH_SHORT).show();
//
////                all_edit.setTextColor(Color.parseColor("#b9b6b6"));
//            }
//        });

    }

    public void video_add(View view) {
//        Toast.makeText(getApplication(), "添加视频啦啦啦啦啦", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MyUploadActivity.this, VideoUploadActivity.class);
        startActivity(intent);
    }

    private void initVideos() {
        for(int i = 3;i < 8;i++) {
            ListVideo video = new ListVideo("视频名称："+ VideoConstant.videoTitles[0][i], VideoConstant.videoThumbs[0][i], false);
            videoList.add(video);
        }
    }

//    @Override
//        public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
//        Toast.makeText(this, "测试上述", Toast.LENGTH_SHORT).show();
//        }

    @Override
         public void click(View v) {
                ShareEntity testBean = new ShareEntity("我是标题", "我是内容，描述内容。");
                testBean.setUrl("https://www.baidu.com"); //分享链接
                testBean.setImgUrl("https://www.baidu.com/img/bd_logo1.png");
                ShareUtil.showShareDialog(MyUploadActivity.this,testBean, ShareConstant.REQUEST_CODE);
          }
}
