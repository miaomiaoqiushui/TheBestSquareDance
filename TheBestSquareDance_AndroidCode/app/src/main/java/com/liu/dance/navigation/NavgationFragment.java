package com.liu.dance.navigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.liu.dance.MainActivity;
import com.liu.dance.R;
import com.liu.dance.adapter.SpinerAdapter;
import com.liu.dance.data.VideoConstant;
import com.liu.dance.view.SpinerPopWindow;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 舞动的心 on 2018/1/8.
 */

public class NavgationFragment extends Fragment implements  SpinerAdapter.IOnItemSelectListener{
    private List<String> mListType = new ArrayList<String>();  //类型列表
    private TextView mTView;
    public LocationClient mLocationClient;
    private TextView positionText;
    private SpinerAdapter mAdapter;
    private RelativeLayout relativeLayout;
    private SpinerPopWindow mSpinerPopWindow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        View view = inflater.inflate(R.layout.fragment_route_nav, container, false);
        positionText = (TextView) view.findViewById(R.id.route_nav_text);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            requestLocation();
        }

        mTView = (TextView) view.findViewById(R.id.tv_value);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.span_relativelayout);

        //初始化数据
        for(int i = 0;i < VideoConstant.dance_group_name.length;i++)
            mListType.add(VideoConstant.dance_group_name[i]);

        mAdapter = new SpinerAdapter(getActivity().getApplicationContext(), mListType);
        mAdapter.refreshData(mListType, 0);


        //初始化PopWindow
        mSpinerPopWindow = new SpinerPopWindow(getActivity().getApplicationContext());
        mSpinerPopWindow.setAdatper(mAdapter);
        mSpinerPopWindow.setItemListener(this);

        mTView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Toast
//                Toast.makeText(getActivity(), "选择舞群~~~ ", Toast.LENGTH_LONG).show();
                showSpinWindow();//显示SpinerPopWindow
            }
        });

        Button m_nav = (Button)view.findViewById(R.id.dance_route_btn);
        m_nav.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Toast.makeText(getActivity(),"没有安装百度地图客户端,请提前安装百度地图哦", Toast.LENGTH_SHORT).show();
                setUpBaiduAPPByMine();
            }
        });
        return view;
    }

    //设置PopWindow
    private void showSpinWindow() {
        //设置mSpinerPopWindow显示的宽度
        mSpinerPopWindow.setWidth(relativeLayout.getWidth());
        //设置显示的位置在哪个控件的下方
        mSpinerPopWindow.showAsDropDown(relativeLayout);
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(getActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuilder currentPosition = new StringBuilder();
//            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
//            currentPosition.append("经线：").append(location.getLongitude()).append("\n");
//            currentPosition.append("国家：").append(location.getCountry()).append("\n");
//            currentPosition.append("省：").append(location.getProvince()).append("\n");
            currentPosition.append(location.getCity());
            currentPosition.append(location.getDistrict());
            currentPosition.append(location.getStreet());
//            currentPosition.append("定位方式：");
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                currentPosition.append("GPS");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                currentPosition.append("网络");
//            }
            positionText.setText(currentPosition);
        }

    }

//    @Override
//    public void onClick(View v) {
////        switch (v.getId()) {
////            case R.id.span_relativelayout:
//////                showSpinWindow();//显示SpinerPopWindow
////                break;
////        }
//    }


    /**
     * SpinerPopWindow中的条目点击监听
     * @param pos
     */
    @Override
    public void onItemClick(int pos) {

        String value = mListType.get(pos);
        mTView.setText(value.toString());

    }

//    public void click_navigation_baidu11(View v) {
//        Toast.makeText(getActivity(),"没有安装百度地图客户端,请提前安装百度地图哦", Toast.LENGTH_SHORT).show();
////        setUpBaiduAPPByMine();
//    }



    /**
     * 我的位置到终点通过百度地图
     */
    void setUpBaiduAPPByMine() {

        try {
            Intent intent = Intent.getIntent("intent://map/direction?origin=我的位置&destination="+"华中科技大学南二门"+"&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if (isInstallByread("com.baidu.BaiduMap")) {
                startActivity(intent);
                Log.e(TAG, "百度地图客户端已经安装");
            } else {
                Log.e(TAG, "没有安装百度地图客户端");
                Toast.makeText(getActivity(),"没有安装百度地图客户端,请提前安装百度地图哦", Toast.LENGTH_SHORT).show();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }



    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }



}