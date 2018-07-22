package com.liu.dance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.liu.dance.view.ViewHolder;
import java.util.List;

/**
 * Created by 舞动的心 on 2018/2/3.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> list;
    private LayoutInflater inflater;
    private int itemLayoutId;


    public CommonAdapter(Context context, List<T> list, int itemLayoutId) {
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = getViewHolder(position,convertView,parent);
        convert(holder,getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder,T item);

    private ViewHolder getViewHolder(int position,View convertView,ViewGroup parent){
        return ViewHolder.get(context,convertView,parent,itemLayoutId,position);
    }

}

