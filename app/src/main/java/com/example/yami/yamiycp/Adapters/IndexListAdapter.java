package com.example.yami.yamiycp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.yami.yamiycp.Activity.OrderActivity;
import com.example.yami.yamiycp.R;
import com.example.yami.yamiycp.model.Teacher;

import java.util.List;

public class IndexListAdapter extends RecyclerView.Adapter<IndexListAdapter.ViewHolder> {

    private List<Teacher> teacherList;
    private Context context;

    public IndexListAdapter(List<Teacher> teachers){
        teacherList = teachers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        if (context==null){
            this.context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tearche_item,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(context,OrderActivity.class);
                intent.putExtra("teacher_data",teacherList.get(position));
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Teacher teacher = teacherList.get(i);
        RequestOptions requestOptions = new RequestOptions().error(R.drawable.icon).centerCrop();

        Glide.with(context).load("http://csnfjx.youside.cn/wsyy/upload/" + teacher.getTeacherNumber() + ".jpg").apply(requestOptions).into(viewHolder.teacherPhoto);
        viewHolder.teacherName.setText(teacher.getTeacherName());
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName;
        ImageView teacherPhoto;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacher_name);
            teacherPhoto = itemView.findViewById(R.id.teacher_pic);
        }
    }
}
