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

        import java.text.SimpleDateFormat;
        import java.util.List;

/**
 * Created by shengyiqun on 2017/12/18.
 */
public class PassCampaiAdapter extends RecyclerView.Adapter<PassCampaiAdapter.ViewHolder>{
    private List<Campaign> mcampaignList;

    public PassCampaiAdapter(List<Campaign> campaignList) {
        mcampaignList = campaignList;
    }

    @Override
    public PassCampaiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.campai_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Campaign campaign = mcampaignList.get(position);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss.ss");
                Intent intent = new Intent("Pass");
                intent.putExtra("name", campaign.getCaname());
                intent.putExtra("startTime", df.format(campaign.getStartline()));
                intent.putExtra("endeadTime",df.format(campaign.getEndeadline()));
                intent.putExtra("endTime",df.format(campaign.getEndline()));
                intent.putExtra("id", campaign.getCamid());
                intent.putExtra("describe", campaign.getDescribe());
                view.getContext().startActivity(intent);
                Log.e("Pass", "true");
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Campaign campaign = mcampaignList.get(position);
        holder.name.setText(campaign.getCaname());
        holder.StartTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:MM").format(campaign.getStartline()));
//        holder.StartTime.setText(String.valueOf(campaign.getCamid()));
    }



    @Override
    public int getItemCount() {
        return mcampaignList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView StartTime;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_name);
            StartTime = (TextView) itemView.findViewById(R.id.item_StartTime);
        }
    }
}
