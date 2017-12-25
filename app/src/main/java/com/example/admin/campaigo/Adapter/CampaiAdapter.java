package com.example.admin.campaigo.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import  com.example.admin.campaigo.R;

import com.example.admin.campaigo.model.*;
import com.example.admin.campaigo.ui.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by shengyiqun on 2017/12/14.
 */

public class CampaiAdapter extends RecyclerView.Adapter<CampaiAdapter.ViewHolder>{
    private List<Campaign> mcampaignList;

    public CampaiAdapter(List<Campaign> campaignList) {
        mcampaignList = campaignList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.campai_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Campaign campaign = mcampaignList.get(position);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss.ss");//2017-12-24 12:20:22.22
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM");
                Intent intent = new Intent("ClickCampaigns");
                intent.putExtra("name", campaign.getCaname());
                intent.putExtra("startTime", df.format(campaign.getStartline()));
                intent.putExtra("endeadTime",df.format(campaign.getEndeadline()));
                intent.putExtra("endTime",df.format(campaign.getEndline()));
                intent.putExtra("id", campaign.getCamid());
                intent.putExtra("describe", campaign.getDescribe());
                view.getContext().startActivity(intent);
                Log.e("click", "true");
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Campaign campaign = mcampaignList.get(position);
        holder.name.setText(campaign.getCaname());
        holder.StartTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:MM").format(campaign.getStartline()));
    }

    @Override
    public int getItemCount() {
        return mcampaignList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView StartTime;
        View campaignsView;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name);
            StartTime = (TextView) itemView.findViewById(R.id.item_StartTime);
        }
    }
}
