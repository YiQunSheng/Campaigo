package com.example.admin.campaigo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.example.admin.campaigo.model.Campaign;
import com.example.admin.campaigo.model.User;
import com.example.admin.campaigo.ui.activity.MainActivity;
import  com.example.admin.campaigo.R;

import com.example.admin.campaigo.network.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2017/12/15.
 */

public class login_Activity extends AppCompatActivity implements View.OnClickListener{
    public static final String REGEX_USERNAME ="^[A-Za-z0-9]+";//利用正则表达式，规定只能是字母
    static String DOMIN = "http://115.159.55.118/";
    final String myUrl = "http://115.159.55.118/login?un=31501337&pw=MTIzNDU2";
    TextView responseText;
    EditText edit_user;//你的文本输入框
    EditText edit_passwd ;
    ProgressBar progressBar;//在这里加入了一个进度条
    Button button_back;
    String url;
//    TextView textForget;
    android.support.v7.widget.Toolbar toolbar;
    String encodedPasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BmobSMS.initialize(this, "069052c8377d7adbb7754f03d03f6fa3");
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.login_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //在界面Java申明edittext
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        edit_user =(EditText) findViewById(R.id.edit_username);
        edit_passwd =(EditText) findViewById(R.id.edit_passwd);
//        textForget = (TextView) findViewById(R.id.text_forgetpw);
        Button button_login = (Button) findViewById(R.id.button_submit);
//        responseText = (TextView) findViewById(R.id.text_response);
        button_login.setOnClickListener(this);//在Onclick调用网络传输
        String name = edit_user.getText().toString();
        String passwd = edit_passwd.getText().toString();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //短信验证部分，暂时无用
        /*textForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobSMS.requestSMSCode(login_Activity.this, "17774009481", "Sendok", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Toast.makeText(login_Activity.this, "Send Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });*/
    }

    @Override
    //个是设置点击事件的 把里面case后面的名字换成你的按钮。里面涉及到的控件要改成你的名字。
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_submit:
                if (!isNameValied(edit_user.getText().toString()) ) {
                    Toast.makeText(login_Activity.this, "用户名格式错误", Toast.LENGTH_SHORT).show();
                    break;
                } else if (!isPasswdValied(edit_passwd.getText().toString())) {
                    Toast.makeText(login_Activity.this, "密码格式错误", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    Log.e("User", edit_user.getText().toString());
                    encodedPasswd = Base64.encodeToString(edit_passwd.getText().toString().getBytes(), Base64.DEFAULT);
                    url = DOMIN + "login?" + "un=" + edit_user.getText().toString() + "&" + "pw=" + encodedPasswd;
                    new LoginTask().execute();

                }
                break;
            default:
                break;
        }


    }



     class LoginTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.e("PreExecute", "Success");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.e("PostExecute", "Success");
            progressBar.setVisibility(View.INVISIBLE);
            if (!isErrorLogin()) {
                Toast.makeText(login_Activity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(login_Activity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(login_Activity.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            /*HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ShowError();
                    Toast.makeText(login_Activity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Log.e("Error", "Net Error");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    UserJsontoPreference(response.body().string());//这里已经把获取的用户信息存入手机。
                }
            });*/
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
//                Log.e("NoNetWorkResponse", response.body().string());
                UserJsontoPreference(response.body().string());//这里已经把获取的用户信息存入手机。
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    //这两个不用调用，是判断输入用的.Name=1~8,Passwd<16
    public boolean isNameValied(String name) {
        return (name.length() > 5&&name.length()<9&&(Pattern.matches(REGEX_USERNAME,name)));
    }
    private boolean isPasswdValied(String passwd) {
        return (passwd.length() >=6&&passwd.length()<=15&&(Pattern.matches(REGEX_USERNAME,passwd)));
    }
    private void UserJsontoPreference(String UserJson) {
        SharedPreferences.Editor editor = getSharedPreferences("user_Info", MODE_PRIVATE).edit();
        editor.putString("User_Json", UserJson);
        editor.apply();
        Log.e("Save in Preference:", UserJson);
    }
    private Boolean isErrorLogin() {
        String UserJson = UserPreferencetoJson();
        User user = JSON.parseObject(UserJson, User.class);
        return user.isErrorLogin();

    }

    private String UserPreferencetoJson() {
        User user = new User();
        user.init();
        SharedPreferences pref = getSharedPreferences("user_Info", MODE_PRIVATE);
        String json = pref.getString("User_Json", JSON.toJSONString(user));
        return json;
    }

    private void ShowError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                responseText.setText("Error");
            }
        });
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

}
