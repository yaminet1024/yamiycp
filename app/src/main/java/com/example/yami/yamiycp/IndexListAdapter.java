package com.example.yami.yamiycp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class IndexListAdapter extends RecyclerView.Adapter<IndexListAdapter.ViewHolder> {

    private List<Teacher> teacherList;
    private Context context;

    public IndexListAdapter(List<Teacher> teachers){
        teacherList = teachers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (context==null){
            this.context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tearche_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Teacher teacher = teacherList.get(i);
        Glide.with(context).load("http://csnfjx.youside.cn/wsyy/upload/" + teacher.getTeacherNumber() + ".jpg").centerCrop().into(viewHolder.teacherPhoto);
        viewHolder.teacherName.setText(teacher.getTeacherName());
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName;
        ImageView teacherPhoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacher_name);
            teacherPhoto = itemView.findViewById(R.id.teacher_pic);
        }
    }
}
