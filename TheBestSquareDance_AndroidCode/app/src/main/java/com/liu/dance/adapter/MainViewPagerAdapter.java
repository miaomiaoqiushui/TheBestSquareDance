package com.liu.dance.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.liu.dance.MainFragment;

import java.util.ArrayList;

/**
 * Created by 舞动的心 on 2018/1/8.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<MainFragment> fragments = new ArrayList<>();
    private MainFragment currentFragment;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.clear();
        fragments.add(MainFragment.newInstance(0));
        fragments.add(MainFragment.newInstance(1));
        fragments.add(MainFragment.newInstance(2));
        fragments.add(MainFragment.newInstance(3));
        fragments.add(MainFragment.newInstance(4));
    }

    @Override
    public MainFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((MainFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public MainFragment getCurrentFragment() {
        return currentFragment;
    }
}