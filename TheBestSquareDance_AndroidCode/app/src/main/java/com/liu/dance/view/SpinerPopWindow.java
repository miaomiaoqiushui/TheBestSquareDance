package com.liu.dance.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.liu.dance.R;
import com.liu.dance.adapter.SpinerAdapter;

import java.util.List;

/**
 * Created by 舞动的心 on 2018/1/31.
 */

public class SpinerPopWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private SpinerAdapter mAdapter;
    private SpinerAdapter.IOnItemSelectListener mItemSelectListener;

    public SpinerPopWindow(Context context) {
        super(context);

        mContext = context;
        init();
    }

    public void setItemListener(SpinerAdapter.IOnItemSelectListener listener) {
        mItemSelectListener = listener;
    }

    public void setAdatper(SpinerAdapter adapter) {
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }


    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_spiner_window, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);


        mListView = (ListView) view.findViewById(R.id.span_listview);
        mListView.setOnItemClickListener(this);
    }


    public void refreshData(List<String> list, int selIndex) {
        if (list != null && selIndex != -1) {
            if (mAdapter != null) {
                mAdapter.refreshData(list, selIndex);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        dismiss();
        if (mItemSelectListener != null) {
            mItemSelectListener.onItemClick(pos);
        }
    }

}
