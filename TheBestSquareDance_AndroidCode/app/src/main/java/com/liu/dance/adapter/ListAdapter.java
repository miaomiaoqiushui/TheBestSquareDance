package com.liu.dance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.dance.R;
import com.liu.dance.dao.ListVideo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 舞动的心 on 2018/1/27.
 */

public class ListAdapter extends ArrayAdapter<ListVideo> {

    private int resourceId;

    private boolean isNeedCheck;

    public boolean isNeedCheck() {
        return isNeedCheck;
    }

    public void setNeedCheck(boolean isNeedCheck) {
        this.isNeedCheck = isNeedCheck;
    }


    public ListAdapter(Context context, int textViewResourceId,
                       List<ListVideo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListVideo video = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.videoImage = (ImageView) view.findViewById (R.id.collection_image);
            viewHolder.videoName = (TextView) view.findViewById (R.id.collection_name);
            viewHolder.checked = (ImageView) view.findViewById(R.id.iv_video_item_check);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
//        viewHolder.videoImage.setImageResource(video.getImageId());
        viewHolder.videoName.setText(video.getName());
        Picasso.with(getContext()).load(video.getImageId()).into(viewHolder.videoImage);
        if (isNeedCheck) {
            viewHolder.checked.setVisibility(View.VISIBLE);
            if (video.isChecked())
                viewHolder.checked.setImageResource(R.drawable.ic_select1);
            else
                viewHolder.checked.setImageResource(R.drawable.ic_unselect1 );
        } else {
            viewHolder.checked.setVisibility(View.GONE);
        }
        return view;
    }

    class ViewHolder {

        ImageView videoImage, checked;

        TextView videoName;

    }

}