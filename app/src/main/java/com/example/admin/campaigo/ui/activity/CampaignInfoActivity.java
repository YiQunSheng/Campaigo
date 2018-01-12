package com.example.admin.campaigo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.admin.campaigo.R;
import com.example.admin.campaigo.model.Campaign;
import com.example.admin.campaigo.model.User;
import com.example.admin.campaigo.network.HttpUtil;

import java.io.IOException;
import java.sql.Timestamp;

import okhttp3.Call;
import okhttp3.Response;

public class CampaignInfoActivity extends AppCompatActivity {
    TextView text_name;
    TextView text_start;
    TextView text_end ;
    TextView text_endead ;
    TextView text_describe ;
    TextView text_organizer;
    TextView text_followers;
    String campaignId;
    String OrganiserName="";
    String DOMIN="http://115.159.55.118/";
    String url="http://115.159.55.118/campaign/register?id=";
    String hasTokenPartInurl = "http://115.159.55.118/campaign/regStatus?campaiId=";
    String hasToken;
    Button button_TakePart;
    Timestamp endeadTimestamp;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_info);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.Info_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text_name= (TextView) findViewById(R.id.text_campaignInfo_name);
        text_start = (TextView) findViewById(R.id.text_campaignInfo_start);
        text_endead = (TextView) findViewById(R.id.text_campaignInfo_endedtime);
        text_end= (TextView) findViewById(R.id.text_campaignInfo_end);
        text_describe=(TextView) findViewById(R.id.text_campaignInfo_describe);
        text_organizer=(TextView) findViewById(R.id.text_campaignInfo_organizer);
        text_followers = (TextView) findViewById(R.id.text_campaignInfo_follower);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Intent intent = getIntent();
        Log.e("OrganizeID", String.valueOf(intent.getIntExtra("id",0)));
        getOrganiserName(String.valueOf(intent.getIntExtra("id",0)));
        text_name.setText(intent.getStringExtra("name"));
        text_start.setText(intent.getStringExtra("startTime"));
        text_endead.setText(intent.getStringExtra("endeadTime"));
        text_end.setText(intent.getStringExtra("endTime"));
        text_describe.setText(intent.getStringExtra("describe"));
        text_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(CampaignInfoActivity.this, FollowerActivity.class);
                intent1.putExtra("CampaignID",intent.getIntExtra("id",0));
                startActivity(intent1);
            }
        });
        endeadTimestamp = Timestamp.valueOf(intent.getStringExtra("endeadTime"));
        button_TakePart = (Button) findViewById(R.id.button_takePartIn);
        if (getPosition().equals("org") ||(getPosition().equals("tea") )/*||hasToken.equals("true")*/)
            button_TakePart.setVisibility(View.INVISIBLE);
        else
            button_TakePart.setVisibility(View.VISIBLE);

        campaignId = String.valueOf(intent.getIntExtra("id",0));
        Log.d("id", campaignId);
        hasTokenPartInurl = hasTokenPartInurl + campaignId + "&userid=" + getId();
        url = url + getId() + "&caid=" + campaignId;
        Log.d("=====》》》", url);
        HttpUtil.sendOkHttpRequest(hasTokenPartInurl, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TakePartInError", "Net Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                hasToken = response.body().string();
                Log.e("has??", hasToken);
            }
        });
        button_TakePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPosition().equals("stu")&&hasToken.equals("false")&&!isPassendeadTime()) {
                    new TakePartTask().execute();
                    HttpUtil.sendOkHttpRequest(hasTokenPartInurl, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("TakePartInError", "Net Error");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            hasToken = response.body().string();
                            Log.e("has??", hasToken);
                        }
                    });
                    return;
                } else if (getPosition().equals("stu") && hasToken.equals("true")) {
                    Toast.makeText(CampaignInfoActivity.this, "您已经参与过", Toast.LENGTH_SHORT).show();
                } else if (isPassendeadTime()) {
                    Toast.makeText(CampaignInfoActivity.this, "活动报名时间已过", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CampaignInfoActivity.this, "您的用户身份不能参加活动", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    class TakePartTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("TakePartPreExecute", "Success");

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.d("TakePartPostExecute", "Success");
            Toast.makeText(CampaignInfoActivity.this, "参与成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("TakePartInError", "Net Error");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e("TakePartinresponse", "ok!!!!");
                }
            });
            return null;
        }
    }

    private void getOrganiserName(String CampaignID) {
//        Log.e("CampaginID", CampaignID);
        String url="http://115.159.55.118/campaign/lender?campaiId=";
        HttpUtil.sendOkHttpRequest(url+CampaignID, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OrganiserName = "网络错误，无法获取组织者信息";
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("Organize",response.body().string());
                final User user = JSON.parseObject(response.body().string(), User.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_organizer.setText("由"+user.getUsname()+" 发布的活动");
                    }
                });
            }
        });
    }
    private String UserPreferencetoJson() {
        User user = new User();
        user.init();
        SharedPreferences pref = getSharedPreferences("user_Info", MODE_PRIVATE);
        String json = pref.getString("User_Json", JSON.toJSONString(user));
        return json;
    }
    private String getPosition() {
        String userJson = UserPreferencetoJson();
        User user = JSON.parseObject(userJson, User.class);
        Log.e("position", userJson);
        return user.getPosition();
    }
    private String getId() {
        String userJson = UserPreferencetoJson();
        User user = JSON.parseObject(userJson, User.class);
        Log.e("id", userJson);
        return user.getId();
    }
    //活动超时判断
    private Boolean isPassendeadTime() {
        return (new Timestamp(System.currentTimeMillis()).compareTo(endeadTimestamp) > 0);
    }
}
