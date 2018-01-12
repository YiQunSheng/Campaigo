package com.example.admin.campaigo.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.campaigo.R;
import com.example.admin.campaigo.model.Campaign;
import com.example.admin.campaigo.model.User;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by shengyiqun on 2018/1/10.
 */

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder>{
    private List<User> users;

    public FollowerAdapter(List<User> mUser) {
        users = mUser;
    }

    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item,parent,false);
        final FollowerAdapter.ViewHolder holder = new FollowerAdapter.ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(FollowerAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getUsname());
        holder.college.setText(user.getClassName());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView college;
        View campaignsView;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_followerName);
            college = (TextView) itemView.findViewById(R.id.item_followerCollege);
        }
    }
}
