package com.example.yami.yamiycp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class Main3Activity extends AppCompatActivity {

    private static final String TAG =  "Main3Activity";

    Teacher selectedTeacher;

    private String[] titleList;

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
        ViewPager viewPager = findViewById(R.id.viewPager);


    }

}
