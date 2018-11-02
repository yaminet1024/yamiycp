package com.example.yami.yamiycp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yami.yamiycp.Adapters.DateAdapter;
import com.example.yami.yamiycp.R;
import com.example.yami.yamiycp.Teacher;
import com.example.yami.yamiycp.YuyueService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;

public class Date3 extends Fragment {
    View view;
    Teacher teacher;
    protected boolean isVisible;
    public boolean isPrepared = false;
    private boolean success = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.date3,container,false);
        final SwipeRefreshLayout swipeRefreshLayout  = view.findViewById(R.id.refresh3);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                success = false;
                loadData();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
            }
        });
        this.view=view;
        return view;
    }

    private void loadData() {
        if (!isPrepared || !isVisible || success){
            return;
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        int increaseTime = 3;
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 21){
            increaseTime = 4;
        }
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+increaseTime); //让日期加2
        final String time = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-"+ String.format("%02d",calendar.get(Calendar.DATE));
        Log.d("fuck", "loadData: " + time + "  " + teacher.getTeacherNumber());
        YuyueService yuyueService = new YuyueService(getActivity(),teacher.getTeacherNumber(),time);
        yuyueService.getYuyueList(new YuyueService.OnListListener() {
            @Override
            public void onReponse(final List<String> data, final OkHttpClient client) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = view.findViewById(R.id.recyclerView3);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
                        DateAdapter adapter = new DateAdapter(data,client,teacher,time);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setAdapter(adapter);
                        success = true;
                        view.findViewById(R.id.progress_3).setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(Teacher teacher) {
        EventBus.getDefault().unregister(this);
        this.teacher = teacher;
        Log.d("date1", "onMessageEvent: " + this.teacher.toString());
        isPrepared = true;
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("EventBus", "onStart: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("EventBus", "onDestroy: ");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible = true;
            loadData();
        }else {
            isVisible = false;
        }
    }
}
