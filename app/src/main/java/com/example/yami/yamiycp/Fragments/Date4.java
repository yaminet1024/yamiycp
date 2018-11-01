package com.example.yami.yamiycp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class Date4 extends Fragment {
    View view;
    Teacher teacher;
    protected boolean isVisible;
    private boolean isPrepared = false;
    private boolean success = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.date4,container,false);
        this.view=view;
        return view;
    }

    private void loadData() {
        if (!isPrepared || !isVisible || success){
            return;
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+5); //让日期加2
        String time = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-"+ calendar.get(Calendar.DATE);
        Log.d("fuck", "loadData: " + time + "  " + teacher.getTeacherNumber());
        YuyueService yuyueService = new YuyueService(getActivity(),teacher.getTeacherNumber(),time);
        yuyueService.getYuyueList(new YuyueService.OnListListener() {
            @Override
            public void onReponse(final List<String> data, final OkHttpClient client) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = view.findViewById(R.id.recyclerView4);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
                        DateAdapter adapter = new DateAdapter(data,client,teacher);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setAdapter(adapter);
                        success = true;
                        view.findViewById(R.id.progress_4).setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(Teacher teacher) {
        this.teacher = teacher;
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
        EventBus.getDefault().unregister(this);
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
