package com.example.admin.campaigo.ui.fragment;



        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v4.widget.SwipeRefreshLayout;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.alibaba.fastjson.JSON;
        import com.example.admin.campaigo.Adapter.PassCampaiAdapter;
        import com.example.admin.campaigo.R;
        import com.example.admin.campaigo.model.Campaign;
        import com.example.admin.campaigo.network.HttpUtil;


        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;

        import okhttp3.Call;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.Response;

/**
 * Created by shengyiqun on 2017/12/17.
 */

public class PassCampaignFragment extends Fragment {
    List<Campaign> searchCampaigns = new ArrayList<>();
    PassCampaiAdapter adapter;
    String url ="http://115.159.55.118/org/loadnopass";
    String campaignName= "";
    String campaignsJson;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pass_campaign, container, false);
        url = url + campaignName;
        recyclerView = (RecyclerView)view.findViewById(R.id.nopass_list_recycler);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_pass);
        refreshLayout.setColorSchemeColors(Color.parseColor("#FF4081"),Color.parseColor("#303F9F"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetCampaignsTask().execute();
            }
        });
        new GetCampaignsTask().execute();
        return view;
    }
    class GetCampaignsTask extends AsyncTask<Void, Integer, Boolean>
    {
        @Override
        protected void onPreExecute() {
            Log.e("GetCamsListPreExecute", "Pre Success");


        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("GetCamsListPostExecute", "Post Success");
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new PassCampaiAdapter(searchCampaigns);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
                campaignsJson= response.body().string();
                searchCampaigns = JSON.parseArray(campaignsJson, Campaign.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
