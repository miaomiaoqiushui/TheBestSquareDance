package com.liu.dance.person;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.liu.dance.R;
import com.liu.dance.adapter.SortAdapter;
import com.liu.dance.dao.SortModel;
import com.liu.dance.data.CharacterParser;
import com.liu.dance.interfaces.PinyinComparator;
import com.liu.dance.shop.ShopGoogsActivity;
import com.liu.dance.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyDanceFriendActivity extends AppCompatActivity implements OnClickListener {
    private ListView sortListView;
    private SideBar sideBar; // 右边的引导
    private TextView dialog;
    private SortAdapter adapter; // 排序的适配器

    private CharacterParser characterParser;
    private List<SortModel> SourceDateList; // 数据

    private PinyinComparator pinyinComparator;
    private LinearLayout xuanfuLayout; // 顶部悬浮的layout
    private TextView xuanfaText, QunFa,dance_back; // 悬浮的文字， 和右上角的群发
    private int lastFirstVisibleItem = -1;
    private boolean isNeedChecked; // 是否需要出现选择的按钮
    private LinearLayout select;
    private TextView all_select, all_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dance_friend);
        getSupportActionBar().hide();
        initViews();
    }

    private void initViews() {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        xuanfuLayout = (LinearLayout) findViewById(R.id.top_layout);
        xuanfaText = (TextView) findViewById(R.id.top_char);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        QunFa = (TextView) findViewById(R.id.qunfa);
        dance_back = (TextView)findViewById(R.id.btn_dance_friend_back);
        dance_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              finish();
            }
        });

        QunFa.setOnClickListener(this);
        select = (LinearLayout)findViewById(R.id.am_dancer_message_select);
        all_select = (TextView)findViewById(R.id.am_dancer_all_select);
        all_message = (TextView)findViewById(R.id.am_dancer_message_edit);
        all_select.setOnClickListener(this);
        all_message.setOnClickListener(this);
        sideBar.setTextView(dialog);

        /**
         * 为右边添加触摸事件
         */
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (!isNeedChecked) {
//                    Toast.makeText(getApplication(),
//                            ((SortModel) adapter.getItem(position)).getName(),
//                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyDanceFriendActivity.this, FriendInfoActivity.class);
                    intent.putExtra("dancer_name", ((SortModel) adapter.getItem(position)).getName());
                    startActivity(intent);
//                    Intent intent = new Intent(MyDanceFriendActivity.this, FriendInfoActivity.class);
//                    startActivity(intent);
                } else {
                    SourceDateList.get(position).setChecked(
                            !SourceDateList.get(position).isChecked());
                    adapter.notifyDataSetChanged(); // 这样写效率很低， 以后可以改成
                    // RecycleView 直接notify
                    // item的状态
                }

            }

        });

        SourceDateList = filledData(getResources().getStringArray(R.array.date));// 填充数据

        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);

        /**
         * 设置滚动监听， 实时跟新悬浮的字母的值
         */
        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int section = adapter.getSectionForPosition(firstVisibleItem);
                int nextSecPosition = adapter
                        .getPositionForSection(section + 1);
                if (firstVisibleItem != lastFirstVisibleItem) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) xuanfuLayout
                            .getLayoutParams();
                    params.topMargin = 0;
                    xuanfuLayout.setLayoutParams(params);
                    xuanfaText.setText(String.valueOf((char) section));
                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = xuanfuLayout.getHeight();
                        int bottom = childView.getBottom();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) xuanfuLayout
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            xuanfuLayout.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                xuanfuLayout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });

    }

    /**
     * 填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            sortModel.setSex(i % 2);
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 过滤数据
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qunfa:
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
                break;

            case R.id.am_dancer_all_select:
                Toast.makeText(getApplication(), "全选啦啦啦啦啦", Toast.LENGTH_SHORT).show();
                all_message.setTextColor(Color.parseColor("#fd5353"));
                break;

            case R.id.am_dancer_message_edit:
                Toast.makeText(getApplication(), "编辑短信啦啦啦啦啦", Toast.LENGTH_SHORT).show();
                all_message.setTextColor(Color.parseColor("#b9b6b6"));
                break;

            case R.id.btn_dance_friend_back:
                finish();
                break;

            default:
                break;
        }

    }

    public void dancer_add(View view) {
//        Toast.makeText(getApplication(), "添加舞友啦啦啦啦啦", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MyDanceFriendActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }

}
