package com.liu.dance;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.liu.dance.adapter.TabPagerAdapter;
import com.liu.dance.data.VideoConstant;
import com.liu.dance.model.ProvinceModel;
import com.liu.dance.navigation.DanceFragment;
import com.liu.dance.navigation.LocationFragment;
import com.liu.dance.navigation.NavgationFragment;
import com.liu.dance.person.ApplyDanceGroupActivity;
import com.liu.dance.person.LoginActivity;
import com.liu.dance.person.MyCollectionActivity;
import com.liu.dance.person.MyDanceFriendActivity;
import com.liu.dance.person.MyDownloadActivity;
import com.liu.dance.person.MyInfoActivity;
import com.liu.dance.person.MyUploadActivity;
import com.liu.dance.person.TimeServiceActivity;
import com.liu.dance.shop.GoodsSearchActivity;
import com.liu.dance.shop.ShopFavoriteActivity;
import com.liu.dance.shop.ShopGoogsActivity;
import com.liu.dance.shop.TakeTurnActivity;
import com.liu.dance.util.NetworkImageHolderView;
import com.liu.dance.video.AloneVideoActivity;
import com.liu.dance.video.PopularFragment;
import com.liu.dance.video.TeachFragment;
import com.liu.dance.video.TeachVideoManyActivity;
import com.liu.dance.xml.XmlParserHandler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by 舞动的心 on 2018/1/8.
 */

public class MainFragment extends Fragment implements OnItemClickListener {
    private FrameLayout fragmentContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private List<String> networkImages;
    private String[] images = VideoConstant.shop_banner_picture;
    private TextView tv_shop_location;
    OptionsPickerView shop_location_Options;

    /**
     * Create a new instance of the fragment
     */
    public static MainFragment newInstance(int index) {
        MainFragment fragment = new MainFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments().getInt("index", 0) == 3) {
            View view = inflater.inflate(R.layout.fragment_person, container, false);

            initMyInfo(view);
            return view;
        } else if (getArguments().getInt("index", 0) == 0) {
            View view = inflater.inflate(R.layout.fragment_navigation, container, false);
            tabLayout = (TabLayout) view.findViewById(R.id.tab_layout1);
            viewPager = (ViewPager) view.findViewById(R.id.pager);
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();
            return view;
        } else if(getArguments().getInt("index", 0) == 1) {
            View view = inflater.inflate(R.layout.fragment_video, container, false);
            tabLayout = (TabLayout) view.findViewById(R.id.video_layout1);
            viewPager = (ViewPager) view.findViewById(R.id.pager_video);
            setupViewPager1(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons1();
            return view;
        }else {
            View view = inflater.inflate(R.layout.fragment_shop, container, false);
//            initDemoList(view);
//                        initDemoSettings(view);
            initShop(view);
            return view;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new LocationFragment(), "我的位置");
        adapter.addFragment(new DanceFragment(), "周边舞群");
        adapter.addFragment(new NavgationFragment(), "线路导航");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager1(ViewPager viewPager) {
        TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new PopularFragment(), "热门推荐");
        adapter.addFragment(new TeachFragment(), "分类教学");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tab, null);
        TextView tabOne =(TextView)view.findViewById(R.id.tab_text);
        tabOne.setText("我的位置");
        tabOne.setTextColor(getResources().getColor(R.color.black2));
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_black_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tab, null);
        TextView tabTwo =(TextView)view1.findViewById(R.id.tab_text);
        tabTwo.setText("周边舞群");
        tabTwo.setTextColor(getResources().getColor(R.color.black2));
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tab, null);
        TextView tabThree =(TextView)view2.findViewById(R.id.tab_text);
        tabThree.setText("线路导航");
        tabThree.setTextColor(getResources().getColor(R.color.black2));
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setupTabIcons1() {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tab, null);
        TextView tabOne =(TextView)view.findViewById(R.id.tab_text);
        tabOne.setText("热门推荐");
        tabOne.setTextColor(getResources().getColor(R.color.black2));
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_black_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tab, null);
        TextView tabTwo =(TextView)view1.findViewById(R.id.tab_text);
        tabTwo.setText("分类教学");
        tabTwo.setTextColor(getResources().getColor(R.color.black2));
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    /**
     * Init demo settings
     */
    private void initDemoSettings(View view) {

        final MainActivity mainActivity = (MainActivity) getActivity();
        final SwitchCompat switchColored = (SwitchCompat) view.findViewById(R.id.fragment_demo_switch_colored);
        final SwitchCompat switchFiveItems = (SwitchCompat) view.findViewById(R.id.fragment_demo_switch_five_items);
        final SwitchCompat showHideBottomNavigation = (SwitchCompat) view.findViewById(R.id.fragment_demo_show_hide);
        final SwitchCompat showSelectedBackground = (SwitchCompat) view.findViewById(R.id.fragment_demo_selected_background);
        final SwitchCompat switchForceTitleHide = (SwitchCompat) view.findViewById(R.id.fragment_demo_force_title_hide);
        final SwitchCompat switchTranslucentNavigation = (SwitchCompat) view.findViewById(R.id.fragment_demo_translucent_navigation);

        switchColored.setChecked(mainActivity.isBottomNavigationColored());
        switchFiveItems.setChecked(mainActivity.getBottomNavigationNbItems() == 5);
        switchTranslucentNavigation.setChecked(getActivity()
                .getSharedPreferences("shared", Context.MODE_PRIVATE)
                .getBoolean("translucentNavigation", false));
        switchTranslucentNavigation.setVisibility(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? View.VISIBLE : View.GONE);

        switchTranslucentNavigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getActivity()
                        .getSharedPreferences("shared", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("translucentNavigation", isChecked)
                        .apply();
                mainActivity.reload();
            }
        });
        switchColored.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mainActivity.updateBottomNavigationColor(isChecked);
            }
        });
        switchFiveItems.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mainActivity.updateBottomNavigationItems(isChecked);
            }
        });
        showHideBottomNavigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mainActivity.showOrHideBottomNavigation(isChecked);
            }
        });
        showSelectedBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mainActivity.updateSelectedBackgroundVisibility(isChecked);
            }
        });
        switchForceTitleHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mainActivity.setForceTitleHide(isChecked);
            }
        });
    }

    private void initMyInfo(View view) {
        final MainActivity mainActivity = (MainActivity) getActivity();
        SuperTextView myInfo_txt = (SuperTextView)view.findViewById(R.id.text_myInfo);
        myInfo_txt.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
//                string = "整个item的点击事件";
//                Toast.makeText(MyInfoActivity.this, string, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                startActivity(intent);
            }
        });

        SuperTextView myFriend_txt = (SuperTextView)view.findViewById(R.id.text_myFriend);
        myFriend_txt.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
//                string = "整个item的点击事件";
//                Toast.makeText(MyInfoActivity.this, string, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MyDanceFriendActivity.class);
                startActivity(intent);
            }
        });

        SuperTextView myCollection_txt = (SuperTextView)view.findViewById(R.id.text_myCollection);
        myCollection_txt.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
//                string = "整个item的点击事件";
//                Toast.makeText(MyInfoActivity.this, string, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MyCollectionActivity.class);
                startActivity(intent);
            }
        });

        SuperTextView myUpload_txt = (SuperTextView)view.findViewById(R.id.text_myUpload);
        myUpload_txt.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
//                string = "整个item的点击事件";
//                Toast.makeText(MyInfoActivity.this, string, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MyUploadActivity.class);
                startActivity(intent);
            }
        });

        SuperTextView myDownload_txt = (SuperTextView)view.findViewById(R.id.text_myDownload);
        myDownload_txt.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
//                string = "整个item的点击事件";
//                Toast.makeText(MyInfoActivity.this, string, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MyDownloadActivity.class);
                startActivity(intent);
            }
        });

        SuperTextView myDanceApply_txt = (SuperTextView)view.findViewById(R.id.text_applyDance);
        myDanceApply_txt.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
//                string = "整个item的点击事件";
//                Toast.makeText(MyInfoActivity.this, string, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ApplyDanceGroupActivity.class);
                startActivity(intent);
            }
        });

        SuperTextView timeService_txt = (SuperTextView)view.findViewById(R.id.text_timeService);
        timeService_txt.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
//                string = "整个item的点击事件";
//                Toast.makeText(MyInfoActivity.this, string, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TimeServiceActivity.class);
                startActivity(intent);
            }
        });

        SuperTextView person_title_txt = (SuperTextView)view.findViewById(R.id.text_person_title);
        person_title_txt .setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {

                if(!superTextView.getSwitchIsChecked()) {
                    superTextView.setSwitchIsChecked(true);
                    mainActivity.setForceTitleHide(true);
                } else {
                    superTextView.setSwitchIsChecked(false);
                    mainActivity.setForceTitleHide(false);
                }

            }

        }).setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(ClickActivity.this, "" + isChecked, Toast.LENGTH_SHORT).show();
                if(isChecked)
                    mainActivity.setForceTitleHide(true);
                else
                    mainActivity.setForceTitleHide(false);
            }
        });;

        SuperTextView person_color_txt = (SuperTextView)view.findViewById(R.id.text_person_color);
        person_color_txt .setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                if(!superTextView.getSwitchIsChecked()) {
                    superTextView.setSwitchIsChecked(true);
                    mainActivity.updateBottomNavigationColor(true);
                } else {
                    superTextView.setSwitchIsChecked(false);
                    mainActivity.updateBottomNavigationColor(false);
                }
            }
        }).setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(ClickActivity.this, "" + isChecked, Toast.LENGTH_SHORT).show();
                if(isChecked)
                    mainActivity.updateBottomNavigationColor(true);
                else
                    mainActivity.updateBottomNavigationColor(false);
            }
        });

        SuperTextView person_big_txt = (SuperTextView)view.findViewById(R.id.text_person_big);
        person_big_txt .setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                if(!superTextView.getSwitchIsChecked()) {
                    superTextView.setSwitchIsChecked(true);
                    mainActivity.setTitleState(true);
                } else {
                    superTextView.setSwitchIsChecked(false);
                    mainActivity.setTitleState(false);
                }
            }
        }).setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(ClickActivity.this, "" + isChecked, Toast.LENGTH_SHORT).show();
                if(isChecked)
                    mainActivity.setTitleState(true);
                else
                    mainActivity.setTitleState(false);
            }
        });
    }

    /**
     * Init the fragment
     */
    private void initDemoList(View view) {

        fragmentContainer = (FrameLayout) view.findViewById(R.id.fragment_container);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_demo_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> itemsData = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            itemsData.add("Fragment " + getArguments().getInt("index", -1) + " / Item " + i);
        }

//        CardItemAdapter adapter = new CardItemAdapter(itemsData);
//        recyclerView.setAdapter(adapter);
    }



    private void initShop(View view) {
        convenientBanner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);
        tv_shop_location = (TextView) view.findViewById(R.id.shop_location);
        tv_shop_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop_location_Options.show();

            }
        });
        EditText serach_edit = (EditText) view.findViewById(R.id.shop_search_edit);
        serach_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), GoodsSearchActivity.class);
                startActivity(intent);
            }
        });
        CardView shop_card_one = (CardView)view.findViewById(R.id.card_shop_view1);
        shop_card_one.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int num_goods = 0;
                Intent intent = new Intent(getActivity(), ShopGoogsActivity.class);
                intent.putExtra("goods_data", num_goods);
//                Intent intent = new Intent(getActivity(), ShopGoogsActivity.class);
                startActivity(intent);
            }
        });
        CardView shop_card_two = (CardView)view.findViewById(R.id.card_shop_view2);
        shop_card_two.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int num_goods = 1;
                Intent intent = new Intent(getActivity(), ShopGoogsActivity.class);
                intent.putExtra("goods_data", num_goods);
                startActivity(intent);
            }
        });
        CardView shop_card_three = (CardView)view.findViewById(R.id.card_shop_view3);
        shop_card_three.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int num_goods = 2;
                Intent intent = new Intent(getActivity(), ShopGoogsActivity.class);
                intent.putExtra("goods_data", num_goods);
                startActivity(intent);
            }
        });
        CardView shop_card_four = (CardView)view.findViewById(R.id.card_shop_view4);
        shop_card_four.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int num_goods = 3;
                Intent intent = new Intent(getActivity(), ShopGoogsActivity.class);
                intent.putExtra("goods_data", num_goods);
                startActivity(intent);
            }
        });

        CardView shop_card_goods = (CardView)view.findViewById(R.id.card_goods_view);
        shop_card_goods.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int num_goods = 0;
                Intent intent = new Intent(getActivity(), ShopFavoriteActivity.class);
                intent.putExtra("goods_view", num_goods);
                startActivity(intent);
            }
        });

        CardView shop_card_goods1 = (CardView)view.findViewById(R.id.card_goods_view1);
        shop_card_goods1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int num_goods = 1;
                Intent intent = new Intent(getActivity(), ShopFavoriteActivity.class);
                intent.putExtra("goods_view", num_goods);
                startActivity(intent);
            }
        });

        CardView shop_card_goods2 = (CardView)view.findViewById(R.id.card_goods_view2);
        shop_card_goods2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int num_goods = 2;
                Intent intent = new Intent(getActivity(), ShopFavoriteActivity.class);
                intent.putExtra("goods_view", num_goods);
                startActivity(intent);
            }
        });

        CardView shop_card_goods3 = (CardView)view.findViewById(R.id.card_goods_view3);
        shop_card_goods3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int num_goods = 3;
                Intent intent = new Intent(getActivity(), ShopFavoriteActivity.class);
                intent.putExtra("goods_view", num_goods);
                startActivity(intent);
            }
        });
        initShopLocationDatas();
        initImageLoader();
        networkImages = Arrays.asList(images);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages)
                .setPageIndicator(new int[]{R.drawable.ic_page_write, R.drawable.ic_page_green}).setOnItemClickListener(this);
        convenientBanner.startTurning(5000);
    }

    @Override
    public void onItemClick(int position) {
        int num_item = position % 7;
        Intent intent = new Intent(getActivity(), TakeTurnActivity.class);
        intent.putExtra("num_data", num_item);
        startActivity(intent);
//        Toast.makeText(getActivity(),"点击了第"+position+"个",Toast.LENGTH_SHORT).show();
    }


    //初始化网络图片缓存库
    private void initImageLoader(){
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.ic_default_adimage)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    ArrayList< ArrayList< ArrayList<String>>> distrList = new ArrayList<>();
    ArrayList< ArrayList<String>> cityList = new ArrayList<>();
    ArrayList<String> proList = new ArrayList<>();

    private void initShopLocationDatas() {
        shop_location_Options = new OptionsPickerView(getContext());
        AssetManager asset = getActivity().getAssets();
        InputStream input = null;
        ArrayList<ProvinceModel> provinceList = null;
        try {
            input = asset.open("province_data.xml");

            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (provinceList != null && provinceList.size() > 0) {
            for (ProvinceModel list : provinceList) {
                proList.add(list.getPickerViewText());
                cityList.add(list.getCityNameList());
                distrList.add(list.getDisNameList());
            }
        }
        shop_location_Options.setPicker(proList, cityList, distrList, true);
        shop_location_Options.setCyclic(false);
        shop_location_Options.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = distrList.get(options1).get(option2).get(options3);
                tv_shop_location.setText(tx);
//                vMasker.setVisibility(View.GONE);
            }
        });
    }



    /**
     * Refresh
     */
    public void refresh() {
        if (getArguments().getInt("index", 0) > 0 && recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    public void willBeHidden() {
        if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }
    }
}
