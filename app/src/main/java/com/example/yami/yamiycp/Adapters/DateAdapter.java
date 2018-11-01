package com.example.yami.yamiycp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yami.yamiycp.R;
import com.example.yami.yamiycp.Teacher;
import com.example.yami.yamiycp.Utils.ApplicationUtil;

import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {

    private List<String> dataList;
    private Context context;
    private OkHttpClient client;
    private Teacher selectTeacher;

    private String[] realTime = {"08:00-09:00","09:00-10:00","10:00-11:00","11:00-12:00","13:30-14:30","14:30-15:30","15:30-16:30","16:30-17:30"};
    private String[] style = {"A","B","C","D","E","1","2","3"};

    public DateAdapter(List<String> data,OkHttpClient client,Teacher selectTeacher){
        this.dataList = data;
        this.client = client;
        this.selectTeacher = selectTeacher;
    }

    @NonNull
    @Override
    public DateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.yuyue_item,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.icon);
                builder.setTitle("确定预约");
                builder.setMessage("您当前预定的时间为：" + realTime[position]);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context,"预约成功，请到记录查询->预约中查看您预约的课时",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
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
        if (dataList.get(i).equals("可约")){
            viewHolder.title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }else {
            viewHolder.relativeLayout.setClickable(false);
        }
        viewHolder.time.setText(realTime[i]);
        viewHolder.number.setText(String.valueOf(i+1));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView relativeLayout;
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
