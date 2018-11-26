package com.example.yami.yamiycp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yami.yamiycp.model.OrderingBean;
import com.example.yami.yamiycp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OrderingAdapter extends RecyclerView.Adapter<OrderingAdapter.ViewHolder> {

    private List<OrderingBean> orderingBeanList;
    private Context context;
    /**
     * type=1:预约中
     * type=2:已经预约
     * type=3:已经取消
     */
    private int type;
    private String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

    public OrderingAdapter(List<OrderingBean> orderingBeans,int type){
        this.type = type;
        if (type == 1){
            Collections.reverse(orderingBeans);
        }
        this.orderingBeanList = orderingBeans;
    }

    @NonNull
    @Override
    public OrderingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context==null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.base_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderingAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemTeacher.setText(orderingBeanList.get(i).getTitle().split("\\(")[0]);
        viewHolder.learnProject.setText("所学科目：" + orderingBeanList.get(i).getLearning());
        String phone = orderingBeanList.get(i).getTitle();
        viewHolder.phone.setText("电话：" + phone.substring(phone.length()-11));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date date = null;
        try {
            date = simpleDateFormat.parse(orderingBeanList.get(i).getOrderDate());
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;

        viewHolder.date.setText("预约日期：" + orderingBeanList.get(i).getOrderDate() + " " +  weekDays[weekday]);
        viewHolder.time.setText("预约时间：" + orderingBeanList.get(i).getOrderTime());
        if (type == 2){
            viewHolder.cancel.setText("已经培训");
        }else if (type == 3){
            viewHolder.cancel.setText("已经取消");
        }
    }

    @Override
    public int getItemCount() {
        return orderingBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTeacher ;
        TextView learnProject;
        TextView phone;
        TextView date;
        TextView time;
        Button cancel;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTeacher = itemView.findViewById(R.id.item_teacher);
            learnProject = itemView.findViewById(R.id.textView2);
            phone = itemView.findViewById(R.id.textView4);
            date = itemView.findViewById(R.id.textView3);
            time = itemView.findViewById(R.id.textView5);
            cancel = itemView.findViewById(R.id.cancel);
        }
    }
}
