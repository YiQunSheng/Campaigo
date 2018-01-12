package com.example.admin.campaigo.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.example.admin.campaigo.ui.activity.ChangePwActivity;
import com.example.admin.campaigo.ui.activity.MainActivity;

import com.example.admin.campaigo.model.User;
import com.example.admin.campaigo.R;
import com.example.admin.campaigo.ui.activity.MainActivity;
import com.example.admin.campaigo.ui.activity.login_Activity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 2017/12/15.
 */

public class userFragment extends Fragment implements View.OnClickListener{
    TextView text_name;
    TextView text_class;
    TextView text_position;
    TextView text_NoLogin;
//    Button button_login;
    Button button_logout;
    android.support.v7.widget.Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
         text_name = (TextView) view.findViewById(R.id.text_name);
         text_class = (TextView) view.findViewById(R.id.text_class);
         text_position = (TextView) view.findViewById(R.id.text_position);
//        button_login = (Button) view.findViewById(R.id.button_login);
        toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.User_Toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
//        button_login.setOnClickListener(this);
       /* if(!NoLogedIn())
            button_login.setText("退出登录");
        else button_login.setText("登录");*/

        String json = UserPreferencetoJson();
        User user = JSON.parseObject(json, User.class);
        Log.e("json", user.getUsname());
        if (!NoLogedIn()) {
            text_name.setText(user.getUsname());
            text_class.setText(user.getClassName());
            text_position.setText(user.getPosition());
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.login_menu, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                if (NoLogedIn()) {
                    Intent intent = new Intent(getActivity(), login_Activity.class);
                    startActivity(intent);
//                    button_login.setText("退出登陆");
                    break;
                } else {
                    Toast.makeText(getActivity(), "您已经登录了！", Toast.LENGTH_SHORT).show();
                   break;
                }
            case R.id.menu_logout:
                if (NoLogedIn()) {
                    Toast.makeText(getActivity(), "您没有登录", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("user_Info", getActivity().MODE_PRIVATE).edit();
                    User user = new User();
                    user.setErrorLogin(true);
                    editor.putString("User_Json", JSON.toJSONString(user));
                    editor.apply();
                    text_name.setText("");
                    text_class.setText("");
                    text_position.setText("");
                    Toast.makeText(getActivity(), "登出成功！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_changePasswd:
                if (NoLogedIn()) {
                    Toast.makeText(getActivity(), "您没有登录,不能修改密码", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), ChangePwActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private String UserPreferencetoJson() {
        User user = new User();
        user.init();
        SharedPreferences pref = getActivity().getSharedPreferences("user_Info", MODE_PRIVATE);
        String json = pref.getString("User_Json", JSON.toJSONString(user));
        return json;
    }
    //调用此方法提取手机里的用户信息
    private String myUserPreferencetoJson() {

        SharedPreferences pref = getActivity().getSharedPreferences("user_Info", getActivity().MODE_PRIVATE);
        if (pref.getString("User_Json", "") == "") {
            SharedPreferences.Editor editor =getActivity(). getSharedPreferences("user_Info", getActivity().MODE_PRIVATE).edit();
            User user = new User();
            user.init();
            String UserJson = JSON.toJSONString(user);
            editor.putString("User_Json", UserJson);
            editor.apply();
        }

        Log.e("already get pre", "yes");
        String json = pref.getString("User_Json", "");
        Log.e("getPre",json );
        return json;
    }
    private Boolean NoLogedIn() {
        String userJson = UserPreferencetoJson();
        User user = JSON.parseObject(userJson, User.class);
        Log.e("position", userJson);
        return user.isErrorLogin();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                Log.e("cilck log in", "true");
                if (NoLogedIn()) {
                    Intent intent = new Intent(getActivity(), login_Activity.class);
                    startActivity(intent);
//                    button_login.setText("退出登陆");
                    break;
                } else {
                    Log.e("cilck log in", "true");
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("user_Info", getActivity().MODE_PRIVATE).edit();
                    User user = new User();
                    user.setErrorLogin(true);
                    editor.putString("User_Json", JSON.toJSONString(user));
                    editor.apply();
                    text_name.setText("");
                    text_class.setText("");
                    text_position.setText("");
                    Toast.makeText(getActivity(), "退出成功！", Toast.LENGTH_SHORT).show();
//                    button_login.setText("登录");
                    break;
                }
            default:
                break;

        }
    }
}
