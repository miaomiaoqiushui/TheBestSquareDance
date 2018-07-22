package com.liu.dance.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.liu.dance.R;
import com.liu.dance.data.VideoConstant;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;
import com.squareup.picasso.Picasso;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by 舞动的心 on 2018/1/8.
 */

public class TeachFragment  extends Fragment  implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshAdapterView.OnListLoadListener{

    private SwipeRefreshRecyclerView swipeRefreshRecyclerView;
    private RecyclerViewAdapter1 adapter;
    private List<String> objects = new ArrayList<>();
    private int index;
    private int footerIndex = 6;
    public static int topRefresh = 1;

    public TeachFragment() {

    }

    public static TeachFragment newInstance() {
        TeachFragment fragment = new TeachFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teach_video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshRecyclerView = (SwipeRefreshRecyclerView) view.findViewById(R.id.teach_videoRefresh);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        swipeRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        swipeRefreshRecyclerView.setOnListLoadListener(this);
        swipeRefreshRecyclerView.setOnRefreshListener(this);

        for (int i = 0; i < footerIndex; i++) {
            objects.add("数据 = " + i);
        }
        adapter = new RecyclerViewAdapter1();
        swipeRefreshRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        index = 0;
//        VideoConstant.videoUrls[0][0] = VideoConstant.videoUrls[0][5];
        swipeRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                objects.add(0, "下拉 = " + (--index));
                topRefresh = (topRefresh + 1) % 4;
//                VideoConstant.videoUrls[0][0] = VideoConstant.videoUrls[1][topRefresh];
//                VideoConstant.videoThumbs[0][0] = VideoConstant.videoThumbs[1][topRefresh];
//                VideoConstant.videoTitles[0][0] = VideoConstant.videoTitles[1][topRefresh];
//                VideoConstant.videoUrls[0][1] = VideoConstant.videoUrls[1][(topRefresh+1) % 4];
//                VideoConstant.videoThumbs[0][1] = VideoConstant.videoThumbs[1][(topRefresh+1) % 4];
//                VideoConstant.videoTitles[0][1] = VideoConstant.videoTitles[1][(topRefresh+1) % 4];
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
//                int count = footerIndex + 3;
//                for (int i = footerIndex; i < count; i++) {
//                    objects.add("上拉 = " + i);
//                }
//                footerIndex = count;
                adapter.notifyDataSetChanged();
                swipeRefreshRecyclerView.setLoading(false);
            }
        }, 1500);
    }

    class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return objects.size();
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.layout_card_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MyViewHolder) holder).teachView.setText(VideoConstant.teachVideo[position % 6]);
            final int in_postion = position;
            ((MyViewHolder) holder).teachMany.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String teach_item = VideoConstant.teachVideo[in_postion % 6];
                    Intent intent = new Intent(getActivity(), TeachVideoManyActivity.class);
                    intent.putExtra("teach_data", teach_item);
                    startActivity(intent);
                }
            });


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
            TextView teachView;
            TextView teachMany;
            public MyViewHolder(View itemView) {
                super(itemView);
                teachView = (TextView) itemView.findViewById(R.id.teach_video_text);
                teachMany = (TextView) itemView.findViewById(R.id.teach_video_many);
                jcVideoPlayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer_teach);
                jcVideoPlayer1 = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer_teach1);
                jcVideoPlayer2 = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer_teach2);
                jcVideoPlayer3 = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer_teach3);
            }
        }

    }


}
