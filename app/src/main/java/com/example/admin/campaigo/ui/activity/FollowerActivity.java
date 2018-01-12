package com.example.admin.campaigo.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.admin.campaigo.Adapter.CampaiAdapter;
import com.example.admin.campaigo.Adapter.FollowerAdapter;
import com.example.admin.campaigo.R;
import com.example.admin.campaigo.model.Campaign;
import com.example.admin.campaigo.model.User;
import com.example.admin.campaigo.network.HttpUtil;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class FollowerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FollowerAdapter adapter;
    List<User> users;
    TextView followersNumber;
    Toolbar toolbar;
    String url = "http://115.159.55.118/org/follow?campaiId=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        Intent intent = getIntent();
        int id = intent.getIntExtra("CampaignID", 0);
        Log.e("id", String.valueOf(id));
        url = url + String.valueOf(id);
        toolbar = (Toolbar) findViewById(R.id.Follower_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        followersNumber = (TextView) findViewById(R.id.text_followerNumber);
        recyclerView = (RecyclerView) findViewById(R.id.follower_recyclerview);
        users = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(FollowerActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        new GetCampaignsTask().execute();
    }
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
            followersNumber.setText("共有"+users.size()+"个参与者");
            adapter = new FollowerAdapter(users);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
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
                    users = JSON.parseArray(response.body().string(), User.class);
                }
            });
            return null;
        }
    }
}
