package com.liu.dance.navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.liu.dance.MainActivity;
import com.liu.dance.R;
import com.liu.dance.data.VideoConstant;
import com.liu.dance.shop.GoodsSearchActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by 舞动的心 on 2018/1/8.
 */

public class DanceFragment extends Fragment implements OnGetGeoCoderResultListener {

    //    TextView mTvinfo;
    TextureMapView mMapView = null;
    private BaiduMap mBaiduMap;
    private Marker mMark;

    ////loc//////
    private MyLocationListener mLocationListener;
    private LocationClient mLocationClient;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    //geo
    private GeoCoder mGeoSearch, getmGeoSearch1;

    View view11;
    TextView dance_name;
    LayoutInflater factory;
    static int num = 1;
    String[] place = VideoConstant.dance_group_name;

    Double num_latitude = 0.1;
    Double num_longitude = 0.1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View v = inflater.inflate(R.layout.fragment_dance, container, false);
//        Button btn_pos = (Button) v.findViewById(R.id.fix_position_nav);
//        btn_pos .setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getActivity(), FixPositionActivity.class);
//                startActivity(intent);
//            }
//        });
        mMapView = (TextureMapView) v.findViewById(R.id.bmapDanceGroupView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(14).build()));

        mGeoSearch = GeoCoder.newInstance();
        mGeoSearch.setOnGetGeoCodeResultListener(this);

        initview();
        intloc();
        initGEO();

//        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());


        return v;
    }

    private void initview() {
//        mTvinfo =(TextView)findViewById(R.id.tv_info);
        factory = LayoutInflater.from(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.stop();
        mGeoSearch.destroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        mLocationClient.start();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        mLocationClient.stop();
    }



    //////////////////////////////////////////////////////////////////////////////////
    //定位
    private void intloc() {
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        initLocation();
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            mBaiduMap.setMyLocationEnabled(true);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            //mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
            MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, null);  //第三个参数是位置图片没有就默认
            mBaiduMap.setMyLocationConfigeration(config);
            //以我的位置为中心
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            num_latitude = location.getLatitude();
            num_longitude = location.getLongitude();

            for(int i = 0;i < 6;i++) {
                if(i % 2 == 1)
                    num_latitude -= 0.02 * (i + 1);
                else
                    num_latitude += 0.01 *(i + 1);
                if(i % 2 == 1)
                    num_longitude += 0.015 * (i + 1);
                else
                    num_longitude -= 0.005 * (i + 1);
                final LatLng lat1 = new LatLng(num_latitude, num_longitude);
                mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(lat1));
            }
//            String[] address = {"武汉市华中科技大南二门","武汉市武汉大学正大门","武汉市湖北大学正大门","武汉市武汉理工大学正大门","武汉市中南民族大学南三门"};
//           for(int i = 0;i < 1;i++) {
//               GeoPoint result = getGeoPointBystr("武汉市中南民族大学南三门");
////            mGeoSearch.geocode(new GeoCodeOption().city("武汉").address("华中师范大学正大门"));
//               num_latitude = (result.getLatitudeE6()) / 1e6;
//               num_longitude = (result.getLongitudeE6()) / 1e6;
//               final LatLng lat1 = new LatLng(num_latitude, num_longitude);
//               mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption()
//                       .location(lat1));
//           }

//            Toast.makeText(getActivity(), "经度："+num_latitude+", 纬度："+num_longitude, Toast.LENGTH_SHORT).show();


            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
        }
    }


    /**
     * 定位初始化
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 10000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }


    ///geo
    private void initGEO() {
        mGeoSearch = GeoCoder.newInstance();
        mGeoSearch.setOnGetGeoCodeResultListener(this);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                final LatLng lat = latLng;
                mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(lat));
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }

        });
    }
    ///////////////OnGetGeoCoderResultListener////////////////////////////////////////////
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
//        //设置地图中心点坐标
//        MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(geoCodeResult.getLocation());
//        mBaiduMap.animateMapStatus(status);
//        Toast.makeText(getActivity(), geoCodeResult.getAddress(), Toast.LENGTH_LONG).show();
//        Double s = geoCodeResult.getLocation().latitude;
//        num_latitude = geoCodeResult.getLocation().latitude;
//        num_longitude = geoCodeResult.getLocation().longitude;
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (TextUtils.isEmpty(reverseGeoCodeResult.getAddress())) {
            Toast.makeText(getActivity(), "地点解析失败，请重新选择", Toast.LENGTH_SHORT).show();
        } else {
            if (null != mMark) {
                mMark.remove();
            }

//            mTvinfo.setText(reverseGeoCodeResult.getAddress());
            num++;
            /////show pos
//            LatLng from = new LatLng(reverseGeoCodeResult.getLocation().latitude,
//                    reverseGeoCodeResult.getLocation().longitude);
//            LatLng from1 = new LatLng(reverseGeoCodeResult.getLocation().latitude,
//                    reverseGeoCodeResult.getLocation().longitude + 0.04);
//            BitmapDescriptor bdB = BitmapDescriptorFactory
//                    .fromResource(R.drawable.ic_dance_group);
//            OverlayOptions ooP = new MarkerOptions().position(from).icon(bdB);
//            OverlayOptions ooP1 = new MarkerOptions().position(from1).icon(bdB);
//            mMark = (Marker) (mBaiduMap.addOverlay(ooP));
//            MapStatus mMapStatus = new MapStatus.Builder().target(from)
//                    .build();
//            mMark = (Marker) (mBaiduMap.addOverlay(ooP1));
//            MapStatus mMapStatus1 = new MapStatus.Builder().target(from1)
//                    .build();

            for(int i = 0;i < 2;i++) {
                LatLng from = new LatLng(reverseGeoCodeResult.getLocation().latitude,
                        reverseGeoCodeResult.getLocation().longitude + (0.02+2*i/100));
//                view11.refreshDrawableState();
                view11 = factory.inflate(R.layout.layout_dancegroup_place_item,null);
                dance_name = (TextView)view11.findViewById(R.id.dancegroup_place_name);
                dance_name.setText(place[num % 7]);
                BitmapDescriptor bdB = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view11));
                OverlayOptions ooP = new MarkerOptions().position(from).icon(bdB);
                mMark = (Marker) (mBaiduMap.addOverlay(ooP));
                MapStatus mMapStatus = new MapStatus.Builder().target(from).build();

            }
            /////show pos
        }
    }

    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());
        addViewContent.buildDrawingCache();

        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    public GeoPoint getGeoPointBystr(String str) {
        GeoPoint gpGeoPoint = null;
        if (str!=null) {
            Geocoder gc = new Geocoder(getActivity(), Locale.CHINA);
            List<Address> addressList = null;
            try {
                addressList = gc.getFromLocationName(str, 1);
                if (!addressList.isEmpty()) {
                    Address address_temp = addressList.get(0);
                    //计算经纬度
                    double Latitude=address_temp.getLatitude()*1E6;
                    double Longitude=address_temp.getLongitude()*1E6;
                    System.out.println("经度:"+Latitude);
                    System.out.println("纬度:"+Longitude);
                    //生产GeoPoint
                    gpGeoPoint = new GeoPoint((int)Latitude, (int)Longitude);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gpGeoPoint;
    }

}