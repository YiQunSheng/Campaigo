package com.example.admin.campaigo.ui.fragment;

/**
 * Created by admin on 2017/12/17.
 */


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.admin.campaigo.Adapter.CampaiAdapter;
import com.example.admin.campaigo.R;
import com.example.admin.campaigo.model.Campaign;
import com.example.admin.campaigo.model.User;
import com.example.admin.campaigo.network.HttpUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 2017/12/15.
 */


public class yoursFragment extends Fragment {
    List<Campaign> inCampaigns;
    List<Campaign> adapterCampaigns;
    List<Campaign> commingCampaigns = new ArrayList<>();
    List<Campaign> passedCampaigns = new ArrayList<>();
    static String DOMIN = "http://115.159.55.118/campaign/myList?id=";
    //    static String DOMIN = "http://115.159.55.118/campaign/GetAll";
    String url;
    String CampaignsJson;
    RecyclerView recyclerView;
    CampaiAdapter adapter;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        url = DOMIN + getUserId();
        Log.e("url,", url);
        inCampaigns = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_yours, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_yours_campaigns);
        final BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.bottom_yours);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.item_yours_finished);
        return view;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_yours_comming:
                    new GetCampaignsTask().execute();
                    return true;
                case R.id.item_yours_finished:
                    new GetPassedCampaignsTask().execute();
                    return true;
            }
            return false;
        }

    };
    //从网络获取该用户的活动，并且更新内存中的活动列表。
    class GetCampaignsTask extends AsyncTask<Void, Integer, Boolean> {
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
            adapter = new CampaiAdapter(commingCampaigns);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.e("Process SIze====>", String.valueOf(commingCampaigns.size()));
            Log.e("Process SIze====>", String.valueOf(passedCampaigns.size()));
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Log.e("GetCamsListError", "Net Error");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    CampaignsJson= response.body().string();
                    inCampaigns = JSON.parseArray(CampaignsJson, Campaign.class);
                    processCampaigns(inCampaigns);
                }
            });
            return null;
        }
    }
    class GetPassedCampaignsTask extends AsyncTask<Void, Integer, Boolean> {
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
            adapter = new CampaiAdapter(passedCampaigns);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.e("Process SIze====>", String.valueOf(commingCampaigns.size()));
            Log.e("Process SIze====>", String.valueOf(passedCampaigns.size()));
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Log.e("GetCamsListError", "Net Error");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    CampaignsJson= response.body().string();
                    inCampaigns = JSON.parseArray(CampaignsJson, Campaign.class);
                    processCampaigns(inCampaigns);
                }
            });
            return null;
        }
    }
    private void processCampaigns(List<Campaign> Campaigns) {
        passedCampaigns.clear();
        commingCampaigns.clear();
        for (Campaign oneCampaign : Campaigns) {
            if(isPassTime(oneCampaign))
                passedCampaigns.add(oneCampaign);
            else commingCampaigns.add(oneCampaign);
        }
    }
    private String UserPreferencetoJson() {
        User user = new User();
        user.init();
        SharedPreferences pref = getActivity().getSharedPreferences("user_Info", MODE_PRIVATE);
        String json = pref.getString("User_Json", JSON.toJSONString(user));
        return json;
    }
    private String getUserId() {
        String UserJson = UserPreferencetoJson();
        User user = JSON.parseObject(UserJson, User.class);
        return user.getId();
    }
    private List<Campaign> getPassedCampaigns(String CampaignsJson) {
        //此处传入用网络方法获得的Json（活动列表）,解析成List，返回已经结束的该同学已经参加的活动。
        List<Campaign> passedCampaigns = new ArrayList<>();
        List<Campaign> allCampaigns = new ArrayList<>();
        allCampaigns = JSON.parseArray(CampaignsJson, Campaign.class);
        for (Campaign campaign : allCampaigns) {
            passedCampaigns.add(campaign);
        }
        return  passedCampaigns;
    }
    private void initCampaigns() {
        new GetCampaignsTask().execute();
        inCampaigns = JSON.parseArray(CampaignsJson, Campaign.class);
    }
    public Boolean isPassTime(Campaign campaign) {
        //>0 前面的大于后面的。 返回1 现在的时间比活动开始时间后面，表示超时了。
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        if (currentTime.compareTo(campaign.getStartline())>=0) {//现在时间比活动开始时间大或者等于
            Log.e("pass time", "no");
            return true;
        }
        Log.e("pass time", "yes");
        return false;
    }//判断活动是否结束了
}
