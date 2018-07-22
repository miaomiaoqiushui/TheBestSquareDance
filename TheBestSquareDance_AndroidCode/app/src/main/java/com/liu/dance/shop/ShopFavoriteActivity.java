package com.liu.dance.shop;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.liu.dance.R;
import com.liu.dance.data.VideoConstant;
import com.liu.dance.util.NetworkImageHolderView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class ShopFavoriteActivity extends AppCompatActivity {
    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private String[] images = VideoConstant.shop_goods_favorite_one;
    private List<String> networkImages;
    static int num = 0;

    private LinearLayout mGallery;
    private int[] mImgIds;
    private LayoutInflater mInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_favorite);
        Intent intent = getIntent();
        int data = intent.getIntExtra("goods_view",0);
        num = data;
        if(data == 1)
            images = VideoConstant.shop_goods_favorite_two;
        else if(data == 2)
            images = VideoConstant.shop_goods_favorite_three;
        else if(data == 3)
            images = VideoConstant.shop_goods_favorite_four;

        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner_shop_goods);
        initImageLoader();
        networkImages = Arrays.asList(images);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages)
                .setPageIndicator(new int[]{R.drawable.ic_page_write, R.drawable.ic_page_black});
        getSupportActionBar().hide();
        Button btn_shop_back = (Button)findViewById(R.id.btn_shop_fav);
        btn_shop_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        TextView txt_fav = (TextView)findViewById(R.id.shop_goods_favorite);
        txt_fav.setText(VideoConstant.shop_favorite_title[data]);

        TextView txt_fav_title = (TextView)findViewById(R.id.shop_goods_con_title);
        txt_fav_title.setText(VideoConstant.shop_favorite_con[data]);

        mImgIds = new int[] { R.drawable.ic_shop_dance3, R.drawable.ic_shop_dance4, R.drawable.ic_shop_shoe,
                R.drawable.ic_shop_video, R.drawable.ic_shop_voice3, R.drawable.ic_shop_voice4, R.drawable.ic_shop_dance_01,
                R.drawable.ic_shop_voice_01 };

        mInflater = LayoutInflater.from(this);
        initView();
    }

    private void initView()
    {
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);

        for (int i = 0; i < 12; i++)
        {

            View view = mInflater.inflate(R.layout.activity_index_gallery_item,
                    mGallery, false);
            ImageView img = (ImageView) view
                    .findViewById(R.id.id_index_gallery_item_image);
            Picasso.with(this).load(VideoConstant.shop_goods_picture[(i+num*2)  % 12]).into(img);
//            img.setImageResource(mImgIds[i]);
            TextView txt = (TextView) view
                    .findViewById(R.id.id_index_gallery_item_text);
            txt.setText(VideoConstant.shop_goods_title[(i+num*2) % 12]);
            mGallery.addView(view);
        }
    }

    //初始化网络图片缓存库
    private void initImageLoader(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.ic_default_adimage)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    public void favorite_buy(View view) {
        Toast.makeText(getApplication(), "购买商品啦啦啦啦啦", Toast.LENGTH_SHORT).show();
    }

    public void favorite_contact(View view) {
        Toast.makeText(getApplication(), "联系商家啦啦啦啦啦", Toast.LENGTH_SHORT).show();
    }
}
