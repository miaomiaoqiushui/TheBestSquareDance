package com.liu.dance.shop;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.dance.R;
import com.liu.dance.data.VideoConstant;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TakeTurnActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshAdapterView.OnListLoadListener{
    private SwipeRefreshRecyclerView swipeRefreshRecyclerView;
    private RecyclerViewAdapter2 adapter;
    private List<String> objects = new ArrayList<>();
    private int index;
    private int footerIndex = 8;
    static int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_turn);
        Intent intent = getIntent();
        int data = intent.getIntExtra("num_data",0);
        num = data;
        getSupportActionBar().setTitle(VideoConstant.shopTurn[data]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshRecyclerView = (SwipeRefreshRecyclerView) findViewById(R.id.take_trun_shopRefresh);
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

    @Override
    public void onRefresh() {
        index = 0;
//        VideoConstant.videoUrls[0][0] = VideoConstant.videoUrls[0][5];
        swipeRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                objects.add(0, "下拉 = " + (--index));
//                topRefresh = (topRefresh + 1) % 4;
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

    class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return objects.size();
        }


        @Override
        public RecyclerViewAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerViewAdapter2.MyViewHolder holder = new RecyclerViewAdapter2.MyViewHolder(LayoutInflater.from(TakeTurnActivity.this)
                    .inflate(R.layout.layout_shop_turn_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Picasso.with(getApplication()).load(VideoConstant.shop_goods_picture[(position * 2 + num) % 12]).into(((MyViewHolder) holder).image_one);
            Picasso.with(getApplication()).load(VideoConstant.shop_goods_picture[(position * 2 + 1 + num) % 12]).into(((MyViewHolder) holder).image_two);
            ((MyViewHolder) holder).txt_one.setText(VideoConstant.shop_goods_title[(position * 2 + num) % 12]);
            ((MyViewHolder) holder).txt_two.setText(VideoConstant.shop_goods_title[(position * 2 + 1 + num) % 12]);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView image_one, image_two;
            TextView txt_one, txt_two;
            public MyViewHolder(View itemView) {
                super(itemView);
                image_one = (ImageView)itemView.findViewById(R.id.shop_picture_one11);
                image_two = (ImageView)itemView.findViewById(R.id.shop_picture_two22);
                txt_one = (TextView)itemView.findViewById(R.id.shop_picture_text_one11);
                txt_two = (TextView)itemView.findViewById(R.id.shop_picture_text_two22);

            }
        }

    }
}
