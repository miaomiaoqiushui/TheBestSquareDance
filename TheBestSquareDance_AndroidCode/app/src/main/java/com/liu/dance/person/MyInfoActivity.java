package com.liu.dance.person;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.allen.library.SuperTextView;
import com.liu.dance.R;
import com.liu.dance.adapter.CommonAdapter;
import com.liu.dance.interfaces.CropOption;
import com.liu.dance.util.UIUtil;
import com.liu.dance.view.MyInfoRoundImageView;
import com.liu.dance.view.SelectDialog;
import com.liu.dance.view.ViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyInfoActivity extends AppCompatActivity {
//    public SuperTextView my_name_picture;
    public LinearLayout my_info_image_layout;
    public MyInfoRoundImageView mHeadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        getSupportActionBar().setTitle("个人信息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SuperTextView myInfo_many = (SuperTextView)findViewById(R.id.myinfo_many);
        myInfo_many.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
//                string = "整个item的点击事件";
//                Toast.makeText(MyInfoActivity.this, string, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplication(), MainInfoManyActivity.class);
                startActivity(intent);
            }
        });

//        my_name_picture = (SuperTextView)findViewById(R.id.myinfo_name_picture);
////        my_name_picture.setRightTvDrawableRight(R.id.iam_tv_content);
//        my_name_picture.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
//            @Override
//            public void onClickListener(SuperTextView superTextView) {
//                selectPhoto();
//            }
//        });
        my_info_image_layout = (LinearLayout) findViewById(R.id.my_info_image_layout);

        mHeadImage = (MyInfoRoundImageView) findViewById(R.id.main_roundImage_info);
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

    private final int PHOTO_PICKED_FROM_CAMERA = 1; // 用来标识头像来自系统拍照
    private final int PHOTO_PICKED_FROM_FILE = 2; // 用来标识从相册获取头像
    private final int CROP_FROM_CAMERA = 3;

    private void getIconFromPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_PICKED_FROM_FILE);
    }

    private void selectPhoto() {
        List<String> list = new ArrayList<>();
        list.add("拍照");
        list.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        getIconFromCamera();
                        break;
                    case 1:
                        getIconFromPhoto(); // 从系统相册获取
                        break;
                    default:
                        break;
                }
            }
        },list);

    }

    private Uri imgUri; // 由于android手机的图片基本都会很大，所以建议用Uri，而不用Bitmap

    /**
     * 调用系统相机拍照
     */
    private void getIconFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imgUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "avatar_"+String.valueOf(System.currentTimeMillis())+".png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
        startActivityForResult(intent,PHOTO_PICKED_FROM_CAMERA);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> list){
        SelectDialog dialog = new SelectDialog(this,
                R.style.transparentFrameWindowStyle,listener,list);
        dialog.show();
        return dialog;
    }


    /**
     * 尝试裁剪图片
     */
    private void doCrop(){
        final ArrayList<CropOption> cropOptions = new ArrayList<>();
        final Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent,0);
        int size = list.size();
        if (size == 0){
            UIUtil.showToast(this,"当前不支持裁剪图片!");
            return;
        }
        intent.setData(imgUri);
        intent.putExtra("outputX",300);
        intent.putExtra("outputY",300);
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("scale",true);
        intent.putExtra("return-data",true);

        // only one
        if (size == 1){
            Intent intent1 = new Intent(intent);
            ResolveInfo res = list.get(0);
            intent1.setComponent(new ComponentName(res.activityInfo.packageName,res.activityInfo.name));
            startActivityForResult(intent1,CROP_FROM_CAMERA);
        }else {
            // 很多可支持裁剪的app
            for (ResolveInfo res : list) {
                CropOption co = new CropOption();
                co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                co.appIntent = new Intent(intent);
                co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName,res.activityInfo.name));
                cropOptions.add(co);
            }

            CommonAdapter<CropOption> adapter = new CommonAdapter<CropOption>(this,cropOptions,R.layout.layout_crop_selector) {
                @Override
                public void convert(ViewHolder holder, CropOption item) {
                    holder.setImageDrawable(R.id.iv_icon,item.icon);
                    holder.setText(R.id.tv_name,item.title);
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("choose a app");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(cropOptions.get(which).appIntent,CROP_FROM_CAMERA);
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (imgUri != null){
                        getContentResolver().delete(imgUri,null,null);
                        imgUri = null;
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        switch (requestCode) {
            case PHOTO_PICKED_FROM_CAMERA:
                doCrop();
                break;
            case PHOTO_PICKED_FROM_FILE:
                imgUri = data.getData();
                doCrop();
                break;
            case CROP_FROM_CAMERA:
                if (data != null){
                    setCropImg(data);
                }
                break;
            default:
                break;
        }
    }

    private void setCropImg(Intent picData){
        Bundle bundle = picData.getExtras();
        if (bundle != null){
            Bitmap mBitmap = bundle.getParcelable("data");

//            Drawable drawable = new BitmapDrawable(mBitmap);
//            my_name_picture.setRightTvDrawableRight(drawable);

            mHeadImage.setImageBitmap(mBitmap);
            saveBitmap(Environment.getExternalStorageDirectory() + "/crop_"
                    +System.currentTimeMillis() + ".png",mBitmap);
        }
    }

    private void saveBitmap(String fileName,Bitmap bitmap){
        File file = new File(fileName);
        FileOutputStream fout = null;
        try {
            file.createNewFile();
            fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
            fout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout!=null){
                    fout.close();
                }
                UIUtil.showToast(MyInfoActivity.this,"头像修改成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void image_edit_info(View view) {
        selectPhoto();
    }
}
