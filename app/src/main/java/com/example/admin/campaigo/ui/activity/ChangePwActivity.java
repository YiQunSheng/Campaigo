package com.example.admin.campaigo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.admin.campaigo.R;
import com.example.admin.campaigo.model.User;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChangePwActivity extends AppCompatActivity {
    public static final String REGEX_USERNAME ="^[A-Za-z0-9]+";
    static String DOMIN = "http://115.159.55.118/";
    EditText edit_oldPasswd;
    EditText edit_newPasswd;
    Button button_modify;
    String oldPasswd;
    String newPasswd;
    ProgressBar progressBar;
    String url="login/modify?userid=xxx&old=xx&new=xxx";
    String encodedoldPasswd;
    String encodednewPasswd;
    android.support.v7.widget.Toolbar toolbar;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.login_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_modify);
        edit_oldPasswd = (EditText) findViewById(R.id.edit_oldpasswd);
        edit_newPasswd = (EditText)findViewById(R.id.edit_newpasswd);
        oldPasswd = edit_oldPasswd.getText().toString();
        newPasswd = edit_newPasswd.getText().toString();
        button_modify = (Button) findViewById(R.id.button_modify);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Log.e("User", edit_newPasswd.getText().toString());
                    url = DOMIN + "login/modify?userid="+getUserId()+"&old="+oldPasswd+"&new="+newPasswd;
                attemptedModify();
            }
        });

    }
    private void attemptedModify() {
        boolean cancel = false;
        View focusView = null;
        oldPasswd = edit_oldPasswd.getText().toString();
        newPasswd = edit_newPasswd.getText().toString();
        if (!(oldPasswd.length() > 5 && oldPasswd.length() < 15 && (Pattern.matches(REGEX_USERNAME, oldPasswd)))) {
            Toast.makeText(ChangePwActivity.this, "PassWord Length Error", Toast.LENGTH_SHORT);
            edit_oldPasswd.setError("Length Error");
            focusView = edit_oldPasswd;
            cancel = true;

        }
        else  if (!(newPasswd.length() > 5 && newPasswd.length() < 15 && (Pattern.matches(REGEX_USERNAME, newPasswd)))) {
            Toast.makeText(ChangePwActivity.this, "PassWord Length Error", Toast.LENGTH_SHORT);
            edit_newPasswd.setError("Length Error");
            focusView = edit_oldPasswd;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            url = DOMIN + "login/modify?userid="+getUserId()+"&old="+oldPasswd+"&new="+newPasswd;
            new ModifyTask().execute();
            }


    }
    private String UserPreferencetoJson() {
        User user = new User();
        user.init();
        SharedPreferences pref = getSharedPreferences("user_Info", MODE_PRIVATE);
        String json = pref.getString("User_Json", JSON.toJSONString(user));
        return json;
    }
    private String getUserId() {
        String UserJson = UserPreferencetoJson();
        User user = JSON.parseObject(UserJson, User.class);
        return user.getId();
    }
    class ModifyTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.e("PreExecute", "Success");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.e("PostExecute", "Success");
            progressBar.setVisibility(View.INVISIBLE);
            if (flag.equals("true")) {
                Toast.makeText(ChangePwActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ChangePwActivity.this, "老密码匹配错误", Toast.LENGTH_SHORT).show();
            }

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
                flag = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}


