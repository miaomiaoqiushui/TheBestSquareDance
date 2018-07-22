package com.liu.dance.video;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.facebook.drawee.backends.pipeline.Fresco;
import com.liu.dance.R;
import com.liu.dance.adapter.MyDetailSoicalAdapter;
import com.liu.dance.dao.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class AloneVideoActivity extends AppCompatActivity {

    @Bind(R.id.am_lv_comments)
    ListView mAmLvComments;
    @Bind(R.id.am_tv_zan)
    TextView mAmTvZan;
    @Bind(R.id.am_tv_comment)
    TextView mAmTvComment;
    //    @Bind(R.id.am_srl_reflush)
//    SwipeRefreshLayout mAmSrlReflush;
    //添加listview头
    View mView;
    //列表数据
    List<Comment> mCommentList;
    //adapter
    BaseAdapter mBaseAdapter;
    @Bind(R.id.am_et_msg)
    EditText mAmEtMsg;
    @Bind(R.id.am_b_save)
    Button mAmBSave;
    @Bind(R.id.am_ll_liuyan)
    LinearLayout mAmLlLiuyan;
    @Bind(R.id.am_ll_info)
    LinearLayout mAmLlInfo;

    //回复的内容
    String info = "";
    //标记位，是评论还是回复。默true认评论
    boolean isComment=true;
    JCVideoPlayerStandard mJcVideoPlayerStandard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //以下是Fresco相关配置
//        Fresco.initialize(this);
        setContentView(R.layout.activity_alone_video);
        ButterKnife.bind(this);
        initView();
        initData();
        initAdapter();
        initListener();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("播放视频");
        getSupportActionBar().hide();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("我爱广场舞");
        mJcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.video_alone_popular);
        mJcVideoPlayerStandard.setUp("http://video.jiecao.fm/11/23/xin/%E5%81%87%E4%BA%BA.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "\t\t\t\t\t\t我爱广场舞");
        Picasso.with(this)
                .load("http://120.79.82.151:8080/video_teach/teach_004.jpg")
                .into(mJcVideoPlayerStandard.thumbImageView);
        ImageButton btn_video_click = (ImageButton) findViewById(R.id.btn_video_back1);
        btn_video_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(getActivity(), AloneVideoActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent =new Intent(FixPositionActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mView = LayoutInflater.from(this).inflate(R.layout.layout_head_video, null);
    }

    private void initData() {
        mCommentList = new ArrayList<>();
        Comment comment = null;
        for (int i = 0; i < 15; i++) {
            if (i % 2 == 0) {
                comment = new Comment(i + "", "张三" + i, "http://d.hiphotos.baidu.com/image/h%3D360/sign=856d60650933874483c5297a610fd937/55e736d12f2eb938e81944c7d0628535e5dd6f8a.jpg", 1, "武汉市中南民族大学", "今天真开心，敲了一天代码。", "2018-01-04 23:02:06", null);
            }
            if (i % 2 == 1) {
                comment = new Comment(i + "", "张三" + i, "http://g.hiphotos.baidu.com/image/h%3D360/sign=c7fd97e3bc0e7bec3cda05e71f2cb9fa/960a304e251f95ca2f34115acd177f3e6609521d.jpg", 1, "武汉市中南民族大学", "今天真开心，敲了一天代码。", "2018-01-04 23:02:06", "王五" + i);
            }
            mCommentList.add(comment);
        }
    }

    private void initAdapter() {
        mBaseAdapter = new MyDetailSoicalAdapter(this, mCommentList);
        mAmLvComments.addHeaderView(mView);//添加头
        mAmLvComments.setAdapter(mBaseAdapter);
    }

    private void initListener() {
        mAmLvComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int post=position-1;//除去头
                Toast.makeText(AloneVideoActivity.this, "点击了第"+post+"个", Toast.LENGTH_SHORT).show();
                isComment=false;//
                comment(true);//调出评论框
                mAmEtMsg.setText("");//清空
                mAmEtMsg.setHint("回复"+mCommentList.get(post).getNickName());
            }
        });

    }


    @OnClick({R.id.am_tv_zan, R.id.am_tv_comment, R.id.am_et_msg, R.id.am_b_save, R.id.am_ll_liuyan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.am_tv_zan:
                comment(true);
                break;
            case R.id.am_tv_comment:
                //点击评论按钮
                comment(true);
                break;
            case R.id.am_et_msg:
                break;
            case R.id.am_b_save:
                saveComment();
                break;
            case R.id.am_ll_liuyan:
                break;
        }
    }

    private void saveComment() {
        if (!TextUtils.isEmpty(mAmEtMsg.getText())) {
            info = mAmEtMsg.getText().toString();
            mAmEtMsg.setText("");
            updateComment();
        } else {
            Toast.makeText(AloneVideoActivity.this, "请输入内容后在留言", Toast.LENGTH_SHORT).show();
        }
    }

    //这边应该上传用户留言，然后刷新界面
    private void updateComment() {
        Comment comment = null;
        comment = new Comment(666 + "", "张三" + 666, "http://d.hiphotos.baidu.com/image/h%3D360/sign=856d60650933874483c5297a610fd937/55e736d12f2eb938e81944c7d0628535e5dd6f8a.jpg", 1, "武汉市中南民族大学",info, "2018-01-04 23:02:06", null);
        mCommentList.add(0,comment);
        mBaseAdapter.notifyDataSetChanged();
        //还原
        comment(false);
    }

    private void comment(boolean flag) {
        if(flag){
            mAmLlInfo.setVisibility(View.GONE);
            mAmLlLiuyan.setVisibility(View.VISIBLE);
            onFocusChange(flag);
        }else{
            mAmLlInfo.setVisibility(View.VISIBLE);
            mAmLlLiuyan.setVisibility(View.GONE);
            onFocusChange(flag);
        }


    }

    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        AloneVideoActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    mAmEtMsg.setFocusable(true);
                    mAmEtMsg.requestFocus();
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(mAmEtMsg.getWindowToken(), 0);
                }
            }
        }, 100);
    }
}
