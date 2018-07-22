package com.liu.dance.video;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liu.dance.R;
import com.liu.dance.data.VideoConstant;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class TeachVideoManyActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshAdapterView.OnListLoadListener{
    private SwipeRefreshRecyclerView swipeRefreshRecyclerView;
    private RecyclerViewAdapter2 adapter;
    private List<String> objects = new ArrayList<>();
    private int index = 0;
    private int footerIndex = 8;
    public static int topRefresh = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_video_many);
        Intent intent = getIntent();
        String data = intent.getStringExtra("teach_data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(data);
        swipeRefreshRecyclerView = (SwipeRefreshRecyclerView) findViewById(R.id.many_videoRefresh);

        swipeRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        LinearLayoutManager.VERTICAL, false));
        swipeRefreshRecyclerView.setOnListLoadListener(this);
        swipeRefreshRecyclerView.setOnRefreshListener(this);

        for (int i = 0; i < footerIndex; i++) {
            objects.add("数据 = " + i);
        }
        adapter = new RecyclerViewAdapter2();
        swipeRefreshRecyclerView.setAdapter(adapter);
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

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
////        super.onActivityCreated(savedInstanceState);
////        setHasOptionsMenu(true);
//
//    }

    @Override
    public void onRefresh() {
        index = 0;
//        VideoConstant.videoUrls[0][0] = VideoConstant.videoUrls[0][5];
        swipeRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                objects.add(0, "下拉 = " + (--index));
                topRefresh = (topRefresh + 1) % 4;
                adapter.notifyDataSetChanged();

                swipeRefreshRecyclerView.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onListLoad() {
        ++index;
        swipeRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                swipeRefreshRecyclerView.setLoading(false);
            }
        }, 1500);
    }

    class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return objects.size();
        }


        @Override
        public RecyclerViewAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerViewAdapter2.MyViewHolder holder = new RecyclerViewAdapter2.MyViewHolder(LayoutInflater.from(TeachVideoManyActivity.this)
                    .inflate(R.layout.layout_many_video_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ((MyViewHolder) holder).jcVideoPlayer.setUp(
                    VideoConstant.videoUrls[0][position % 10], JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            Picasso.with(((MyViewHolder) holder).jcVideoPlayer.getContext())
                    .load(VideoConstant.videoThumbs[0][position % 10])
                    .into(((MyViewHolder) holder).jcVideoPlayer.thumbImageView);

            ((MyViewHolder) holder).jcVideoPlayer1.setUp(
                    VideoConstant.videoUrls[0][(position+1) % 10], JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            Picasso.with(((MyViewHolder) holder).jcVideoPlayer1.getContext())
                    .load(VideoConstant.videoThumbs[0][(position+1) % 10])
                    .into(((MyViewHolder) holder).jcVideoPlayer1.thumbImageView);

            ((MyViewHolder) holder).jcVideoPlayer2.setUp(
                    VideoConstant.videoUrls[0][(position+2) % 10], JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            Picasso.with(((MyViewHolder) holder).jcVideoPlayer2.getContext())
                    .load(VideoConstant.videoThumbs[0][(position+2) % 10])
                    .into(((MyViewHolder) holder).jcVideoPlayer2.thumbImageView);

            ((MyViewHolder) holder).jcVideoPlayer3.setUp(
                    VideoConstant.videoUrls[0][(position+3) % 10], JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            Picasso.with(((MyViewHolder) holder).jcVideoPlayer3.getContext())
                    .load(VideoConstant.videoThumbs[0][(position+3) % 10])
                    .into(((MyViewHolder) holder).jcVideoPlayer3.thumbImageView);

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            JCVideoPlayerStandard jcVideoPlayer;
            JCVideoPlayerStandard jcVideoPlayer1;
            JCVideoPlayerStandard jcVideoPlayer2;
            JCVideoPlayerStandard jcVideoPlayer3;
            public MyViewHolder(View itemView) {
                super(itemView);
                jcVideoPlayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer_many);
                jcVideoPlayer1 = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer_many1);
                jcVideoPlayer2 = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer_many2);
                jcVideoPlayer3 = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer_many3);
            }
        }

    }
}
