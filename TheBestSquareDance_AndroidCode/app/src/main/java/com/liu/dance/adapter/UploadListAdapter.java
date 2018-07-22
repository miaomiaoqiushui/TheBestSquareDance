package com.liu.dance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.dance.R;
import com.liu.dance.dao.ListVideo;
import com.liu.dance.person.MyUploadActivity;
import com.squareup.picasso.Picasso;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;
import android.view.View.OnClickListener;
import java.util.List;

/**
 * Created by 舞动的心 on 2018/1/27.
 */

public class UploadListAdapter extends ArrayAdapter<ListVideo> implements OnClickListener  {

    private Context context;

    private int resourceId;

    private boolean isNeedCheck;

    public boolean isNeedCheck() {
        return isNeedCheck;
    }

    public void setNeedCheck(boolean isNeedCheck) {
        this.isNeedCheck = isNeedCheck;
    }

    private Callback mCallback;

    public interface Callback {
                public void click(View v);
    }


    public UploadListAdapter(Context context, int textViewResourceId,
                       List<ListVideo> objects, Callback callback) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mCallback = callback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListVideo video = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.videoImage = (ImageView) view.findViewById (R.id.upload_image);
            viewHolder.videoName = (TextView) view.findViewById (R.id.upload_name);
            viewHolder.checked = (ImageView) view.findViewById(R.id.iv_upload_item_check);
            viewHolder.share = (Button) view.findViewById(R.id.button_upload_share);
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
            viewHolder.share.setVisibility(View.GONE);
            if (video.isChecked())
                viewHolder.checked.setImageResource(R.drawable.ic_select1);
            else
                viewHolder.checked.setImageResource(R.drawable.ic_unselect1 );
        } else {
            viewHolder.checked.setVisibility(View.GONE);
            viewHolder.share.setVisibility(View.VISIBLE);
        }

        viewHolder.share.setOnClickListener(this);
        return view;
    }



    class ViewHolder {

        ImageView videoImage, checked;

        TextView videoName;

        Button share;

    }

    //响应按钮点击事件,调用子定义接口，并传入View
        @Override
         public void onClick(View v) {
                 mCallback.click(v);
         }

}
