package com.example.yami.yamiycp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArrayList;
    String[] titleList;


    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList, String[] titleList) {
        super(fm);
        this.fragmentArrayList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentArrayList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }
}
