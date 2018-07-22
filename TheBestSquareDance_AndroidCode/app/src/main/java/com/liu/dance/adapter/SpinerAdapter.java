package com.liu.dance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liu.dance.R;

import java.util.List;

/**
 * Created by 舞动的心 on 2018/1/31.
 */

public class SpinerAdapter extends BaseAdapter {

    public static interface IOnItemSelectListener {
        public void onItemClick(int pos);
    }

    private List<String> mObjects;

    private LayoutInflater mInflater;

    public SpinerAdapter(Context context, List<String> mObjects) {
        this.mObjects = mObjects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void refreshData(List<String> objects, int selIndex) {
        mObjects = objects;
        if (selIndex < 0) {
            selIndex = 0;
        }
        if (selIndex >= mObjects.size()) {
            selIndex = mObjects.size() - 1;
        }
    }


    @Override
    public int getCount() {

        return mObjects.size();
    }

    @Override
    public Object getItem(int pos) {
        return mObjects.get(pos).toString();
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_spiner_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.span_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Object item =  getItem(pos);
        viewHolder.mTextView.setText(mObjects.get(pos));

        return convertView;
    }


    public static class ViewHolder {
        public TextView mTextView;
    }
}
