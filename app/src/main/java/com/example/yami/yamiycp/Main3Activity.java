package com.example.yami.yamiycp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.example.yami.yamiycp.Adapters.ViewPagerAdapter;
import com.example.yami.yamiycp.Fragments.Date1;
import com.example.yami.yamiycp.Fragments.Date2;
import com.example.yami.yamiycp.Fragments.Date3;
import com.example.yami.yamiycp.Fragments.Date4;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private static final String TAG =  "Main3Activity";

    Teacher selectedTeacher;

    ArrayList<Fragment> fragmentsList = new ArrayList<>();

    private String[] titleList = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        selectedTeacher = (Teacher) getIntent().getSerializableExtra("teacher_data");
        Log.d(TAG, "onCreate: " + selectedTeacher.getTeacherNumber());
        initUI();
    }

    private void initUI() {
        setTabTitle();
        ViewPager viewPager = findViewById(R.id.viewPager);
        fragmentsList.add(new Date1());
        fragmentsList.add(new Date2());
        fragmentsList.add(new Date3());
        fragmentsList.add(new Date4());
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentsList,titleList);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabTitle() {
        String []weekdate = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};


        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+2); //让日期加1
        titleList[0] = calendar.get(Calendar.MONTH)+1  + "/" + calendar.get(Calendar.DATE) + "\n" + weekdate[calendar.get(calendar.DAY_OF_WEEK)-1];
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1); //让日期加1
        titleList[1] = calendar.get(Calendar.MONTH)+1  + "/" + calendar.get(Calendar.DATE) + "\n" + weekdate[calendar.get(calendar.DAY_OF_WEEK)-1];
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1); //让日期加1
        titleList[2] = calendar.get(Calendar.MONTH)+1  + "/" + calendar.get(Calendar.DATE) + "\n" + weekdate[calendar.get(calendar.DAY_OF_WEEK)-1];
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1); //让日期加1
        titleList[3] = calendar.get(Calendar.MONTH)+1  + "/" + calendar.get(Calendar.DATE) + "\n" + weekdate[calendar.get(calendar.DAY_OF_WEEK)-1];
    }

}
