package com.example.admin.campaigo.ui.fragment;

/**
 * Created by admin on 2017/12/17.
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.admin.campaigo.Adapter.CampaiAdapter;
import com.example.admin.campaigo.R;
import com.example.admin.campaigo.model.Campaign;
import com.example.admin.campaigo.model.User;
import com.example.admin.campaigo.network.HttpUtil;
import com.example.admin.campaigo.ui.activity.ApplyActivity;
import com.example.admin.campaigo.ui.activity.login_Activity;

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
    String url;
    String CampaignsJson;
    RecyclerView recyclerView;
    CampaiAdapter adapter;
    FloatingActionButton fab;
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        url = DOMIN + getUserId();
        Log.e("url,", url);
        inCampaigns = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_yours, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swpie_yours);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.primary),getResources().getColor(R.color.accent));

        recyclerView = (RecyclerView) view.findViewById(R.id.list_yours_campaigns);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if(getPosition().equals("stu"))
            fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ApplyActivity.class);
                startActivity(intent);
            }
        });
        toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.Yours_Toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);
        final BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.bottom_yours);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.item_yours_comming);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (navigation.getSelectedItemId()) {
                    case R.id.item_yours_comming:
                        new GetCampaignsTask().execute();
                        break;
                    case R.id.item_yours_finished:
                        new GetPassedCampaignsTask().execute();
                        break;
                    default:
                        break;

                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_search,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
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

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_search:
                    Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return true;
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
            swipeRefreshLayout.setRefreshing(false);
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Log.e("GetCamsListError", "Net Error");
                    swipeRefreshLayout.setRefreshing(false);
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
            swipeRefreshLayout.setRefreshing(false);
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
    private String getPosition() {
        String userJson = UserPreferencetoJson();
        User user = JSON.parseObject(userJson, User.class);
        Log.e("position", userJson);
        return user.getPosition();
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
