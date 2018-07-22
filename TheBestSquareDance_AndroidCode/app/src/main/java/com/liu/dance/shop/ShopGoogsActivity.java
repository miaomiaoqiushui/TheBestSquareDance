package com.liu.dance.shop;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.liu.dance.R;
import com.liu.dance.data.VideoConstant;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopGoogsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshAdapterView.OnListLoadListener{
    private SwipeRefreshRecyclerView swipeRefreshRecyclerView;
    private RecyclerViewAdapter2 adapter;
    private List<String> objects = new ArrayList<>();
    private int index;
    private int footerIndex = 5;
    public static int num = 0;

    String[] titles = {"超值购","热门活动","品牌街","特色市场"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_googs);

        swipeRefreshRecyclerView = (SwipeRefreshRecyclerView) findViewById(R.id.shop_recyclerView);
        swipeRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        LinearLayoutManager.VERTICAL, false));
        swipeRefreshRecyclerView.setOnListLoadListener(this);
        swipeRefreshRecyclerView.setOnRefreshListener(this);

        for (int i = 0; i < footerIndex; i++) {
            objects.add("数据 = " + i);
        }
        adapter = new RecyclerViewAdapter2();
        swipeRefreshRecyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        int data = intent.getIntExtra("goods_data",0);
        num = data;
        TextView tv_title = (TextView)findViewById(R.id.shop_goods_title);
        tv_title.setText(titles[data]);
        TextView tv_content = (TextView)findViewById(R.id.shop_goods_content);
        tv_content.setText(VideoConstant.shopConcrete[data]);
        getSupportActionBar().setTitle(titles[data]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView sh_image = (ImageView)findViewById(R.id.shop_goods_image);
        Resources resources = getBaseContext().getResources();
        Drawable imageDrawable = resources.getDrawable(R.drawable.ic_shop_dance3); //图片在drawable文件夹下
        if(data == 0)
            imageDrawable = resources.getDrawable(R.drawable.ic_shop_dance3);
        else if(data == 1)
            imageDrawable = resources.getDrawable(R.drawable.ic_shop_dance4);
        else if(data == 2)
            imageDrawable = resources.getDrawable(R.drawable.ic_shop_voice3);
        else
            imageDrawable = resources.getDrawable(R.drawable.ic_shop_voice4);
        sh_image.setBackgroundDrawable(imageDrawable);

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
            RecyclerViewAdapter2.MyViewHolder holder = new RecyclerViewAdapter2.MyViewHolder(LayoutInflater.from(ShopGoogsActivity.this)
                    .inflate(R.layout.layout_shop_goods_item, parent, false));
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
                image_one = (ImageView)itemView.findViewById(R.id.shop_picture_one);
                image_two = (ImageView)itemView.findViewById(R.id.shop_picture_two);
                txt_one = (TextView)itemView.findViewById(R.id.shop_picture_text_one);
                txt_two = (TextView)itemView.findViewById(R.id.shop_picture_text_two);

            }
        }

    }

    public void goods_buy(View view) {
        Toast.makeText(getApplication(), "购买商品啦啦啦啦啦", Toast.LENGTH_SHORT).show();
    }

    public void goods_contact(View view) {
        Toast.makeText(getApplication(), "联系商家啦啦啦啦啦", Toast.LENGTH_SHORT).show();
    }
}
