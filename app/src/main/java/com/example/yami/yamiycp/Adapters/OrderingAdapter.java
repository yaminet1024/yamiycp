package com.example.yami.yamiycp.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yami.yamiycp.OrderingBean;
import com.example.yami.yamiycp.R;

import java.util.List;
import java.util.Random;

public class OrderingAdapter extends RecyclerView.Adapter<OrderingAdapter.ViewHolder> {

    List<OrderingBean> orderingBeanList;

    public OrderingAdapter(List<OrderingBean> orderingBeans){
        this.orderingBeanList = orderingBeans;
    }

    @NonNull
    @Override
    public OrderingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.yuyue_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderingAdapter.ViewHolder viewHolder, int i) {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        viewHolder.line.setBackgroundColor(Color.rgb(r,g,b));
        viewHolder.title.setText(orderingBeanList.get(i).getTitle());
        viewHolder.time.setText(orderingBeanList.get(i).getOrderDate() + " " + orderingBeanList.get(i).getOrderTime());
        viewHolder.number.setText(String.valueOf(i+1));
    }

    @Override
    public int getItemCount() {
        return orderingBeanList.size();
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
