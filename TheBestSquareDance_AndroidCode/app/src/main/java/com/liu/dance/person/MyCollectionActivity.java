package com.liu.dance.person;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.liu.dance.R;
import com.liu.dance.adapter.ListAdapter;
import com.liu.dance.dao.ListVideo;
import com.liu.dance.data.VideoConstant;

import java.util.ArrayList;
import java.util.List;

public class MyCollectionActivity extends AppCompatActivity {

    private List<ListVideo> videoList = new ArrayList<ListVideo>();
    private boolean isNeedChecked; // 是否需要出现选择的按钮
    private TextView edit_video;
    private ListAdapter adapter;
    private LinearLayout select;
    private TextView all_select, all_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        getSupportActionBar().hide();
        TextView dance_collection_back = (TextView) findViewById(R.id.btn_dance_collection_back);
        dance_collection_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        initVideos();
        adapter = new ListAdapter(MyCollectionActivity.this, R.layout.layout_collection_item, videoList);
        ListView listView = (ListView) findViewById(R.id.list_collection);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListVideo video = videoList.get(position);

                if(!isNeedChecked)
                     Toast.makeText(MyCollectionActivity.this, video.getName(), Toast.LENGTH_SHORT).show();
                else {
                    if(!video.isChecked())
                        video.setChecked(true);
                    else
                        video.setChecked(false);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        select = (LinearLayout) findViewById(R.id.am_collection_select);
        all_select = (TextView) findViewById(R.id.am_collection_all_select);
        all_edit = (TextView) findViewById(R.id.am_collection_edit);


        edit_video = (TextView)findViewById(R.id.edit_collection_video);
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





    }

    private void initVideos() {
        for(int i = 0;i < 10;i++) {
            ListVideo video = new ListVideo("视频名称："+VideoConstant.videoTitles[0][i], VideoConstant.videoThumbs[0][i], false);
            videoList.add(video);
        }
    }


}
