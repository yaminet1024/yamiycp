package com.example.yami.yamiycp.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yami.yamiycp.R;

import java.util.List;
import java.util.Random;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {

    private List<String> dataList;

    private String[] realTime = {"08:00-09:00","09:00-10:00","10:00-11:00","11:00-12:00","13:30-14:30","14:30-15:30","15:30-16:30","16:30-17:30"};
    private String[] style = {"A","B","C","D","E","1","2","3"};

    public DateAdapter(List<String> data){
        this.dataList = data;
    }

    @NonNull
    @Override
    public DateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.yuyue_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DateAdapter.ViewHolder viewHolder, int i) {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        viewHolder.line.setBackgroundColor(Color.rgb(r,g,b));
        viewHolder.title.setText(dataList.get(i));
        viewHolder.time.setText(realTime[i]);
        viewHolder.number.setText(String.valueOf(i+1));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        TextView number;
        TextView title;
        TextView time;
        View line;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.yuyue);
            number = itemView.findViewById(R.id.schedule_number);
            title = itemView.findViewById(R.id.schedule_title);
            time = itemView.findViewById(R.id.schedule_info);
            line = itemView.findViewById(R.id.schedule_line);
        }
    }
}
