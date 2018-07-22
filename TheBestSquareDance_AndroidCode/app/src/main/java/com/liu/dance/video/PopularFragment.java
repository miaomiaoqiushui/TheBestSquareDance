package com.liu.dance.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class PopularFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshAdapterView.OnListLoadListener{

    private SwipeRefreshRecyclerView swipeRefreshRecyclerView;
    private RecyclerViewAdapter adapter;
    private List<String> objects = new ArrayList<>();
    private int index;
    private int footerIndex = 10;
    public static int topRefresh = 1;

    public PopularFragment() {
    }

    public static PopularFragment newInstance() {
        PopularFragment fragment = new PopularFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_video, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshRecyclerView = (SwipeRefreshRecyclerView) view.findViewById(R.id.swipeRefresh);
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
        adapter = new RecyclerViewAdapter();
        swipeRefreshRecyclerView.setAdapter(adapter);
        swipeRefreshRecyclerView.setEmptyText("数据又没有了!");
    }


    @Override
    public void onRefresh() {
        index = 0;
        VideoConstant.videoUrls[0][0] = VideoConstant.videoUrls[0][5];
        swipeRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                objects.add(0, "下拉 = " + (--index));
                topRefresh = (topRefresh + 1) % 4;
                VideoConstant.videoUrls[0][0] = VideoConstant.videoUrls[1][topRefresh];
                VideoConstant.videoThumbs[0][0] = VideoConstant.videoThumbs[1][topRefresh];
                VideoConstant.videoTitles[0][0] = VideoConstant.videoTitles[1][topRefresh];
                VideoConstant.videoUrls[0][1] = VideoConstant.videoUrls[1][(topRefresh+1) % 4];
                VideoConstant.videoThumbs[0][1] = VideoConstant.videoThumbs[1][(topRefresh+1) % 4];
                VideoConstant.videoTitles[0][1] = VideoConstant.videoTitles[1][(topRefresh+1) % 4];
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
                int count = footerIndex + 3;
                for (int i = footerIndex; i < count; i++) {
                    objects.add("上拉 = " + i);
                }
                footerIndex = count;
                adapter.notifyDataSetChanged();
                swipeRefreshRecyclerView.setLoading(false);
            }
        }, 1500);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu, menu);
    }


    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return objects.size();
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.layout_video_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Log.i(TAG, "onBindViewHolder [" + holder.jcVideoPlayer.hashCode() + "] position=" + position);

            ((MyViewHolder) holder).jcVideoPlayer.setUp(
                    VideoConstant.videoUrls[0][position % 10], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    VideoConstant.videoTitles[0][position % 10]);
            Picasso.with(((MyViewHolder) holder).jcVideoPlayer.getContext())
                    .load(VideoConstant.videoThumbs[0][position % 10])
                    .into(((MyViewHolder) holder).jcVideoPlayer.thumbImageView);
            ((MyViewHolder) holder).comment_click.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(), AloneVideoActivity.class);
                    startActivity(intent);
                }
            });
            ((MyViewHolder) holder).share_click.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ShareEntity testBean = new ShareEntity("我是标题", "我是内容，描述内容。");
                    testBean.setUrl("https://www.baidu.com"); //分享链接
                    testBean.setImgUrl("https://www.baidu.com/img/bd_logo1.png");
                    ShareUtil.showShareDialog(getActivity(), testBean, ShareConstant.REQUEST_CODE);
                }
            });
            ((MyViewHolder) holder).heartButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    likeButton.setLiked(true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    likeButton.setLiked(false);
                }
            });
            ((MyViewHolder) holder).saveButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    likeButton.setLiked(true);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    likeButton.setLiked(false);
                }
            });
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            JCVideoPlayerStandard jcVideoPlayer;
            Button comment_click;
            Button share_click;
            LikeButton heartButton;
            LikeButton saveButton;
            public MyViewHolder(View itemView) {
                super(itemView);
                jcVideoPlayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer_popular);
                comment_click = (Button) itemView.findViewById(R.id.button_video_comment);
                share_click = (Button) itemView.findViewById(R.id.button_video_share);
                heartButton = (LikeButton)itemView.findViewById(R.id.button_video_heart);
                saveButton = (LikeButton)itemView.findViewById(R.id.button_video_collection);
            }
        }

    }

}