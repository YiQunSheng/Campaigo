package com.example.admin.campaigo.ui.activity;


        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.example.admin.campaigo.R;
        import com.example.admin.campaigo.network.HttpUtil;

        import java.io.IOException;

        import okhttp3.Call;
        import okhttp3.Response;

public class PassCampaginAvtivity extends AppCompatActivity {
    TextView text_name;
    TextView text_start;
    TextView text_end ;
    TextView text_ended ;
    TextView text_describe ;
    String campaignId;
    String DOMIN="http://115.159.55.118/";
    String url="http://115.159.55.118/org/orgnaizepass?campaiId=";
    String hasTokenPartInurl = "http://115.159.55.118/campaign/regStatus?campaiId=";
    String hasPassed;
    Button button_Pass;
    android.support.v7.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_campaign);
        text_name = (TextView) findViewById(R.id.text_pass_name);
        text_start = (TextView) findViewById(R.id.text_pass_start);
        text_ended=(TextView) findViewById(R.id.text_pass_endedtime);
        text_end=(TextView) findViewById(R.id.text_pass_end);
        text_describe=(TextView) findViewById(R.id.text_pass_describe);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.Pass_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        text_name.setText(intent.getStringExtra("name"));
        text_start.setText(intent.getStringExtra("startTime"));
        text_ended.setText(intent.getStringExtra("endeadTime"));
        text_end.setText(intent.getStringExtra("endTime"));
        text_describe.setText(intent.getStringExtra("describe"));
        button_Pass = (Button) findViewById(R.id.button_passCampaign);
        campaignId = String.valueOf(intent.getIntExtra("id", 0));
        Log.d("id", campaignId);
//        hasTokenPartInurl = hasTokenPartInurl + campaignId + "&userid=" + getId();
        url = url + campaignId;
        button_Pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PassAsync().execute();
                finish();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    class PassAsync extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Toast.makeText(PassCampaginAvtivity.this, "成功通过", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("PassError", "Net Error");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    hasPassed = response.body().string();
                    Log.e("PassResult", hasPassed);
                }
            });
            return null;
        }
    }
}
