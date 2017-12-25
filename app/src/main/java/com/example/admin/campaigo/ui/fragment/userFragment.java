package com.example.admin.campaigo.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
    TextView textView;
    TextView text_name;
    TextView text_class;
    TextView text_position;
    TextView text_NoLogin;
    Button button_logout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_info, container, false);
         text_name = (TextView) view.findViewById(R.id.text_name);
         text_class = (TextView) view.findViewById(R.id.text_class);
         text_position = (TextView) view.findViewById(R.id.text_position);
        text_NoLogin = (TextView) view.findViewById(R.id.text_NoLogin);
        textView = (TextView) view.findViewById(R.id.text_NoLogin);
        Button button = (Button) view.findViewById(R.id.button_login);
        button_logout = (Button)view.findViewById(R.id.button_logout);
        button.setOnClickListener(this);
        button_logout.setOnClickListener(this);
        String json = UserPreferencetoJson();
        User user = JSON.parseObject(json, User.class);
        Log.e("json", user.getUsname());
        text_name.setText(user.getUsname());
        text_class.setText(user.getClassName());
        text_position.setText(user.getPosition());
        if (!user.isErrorLogin())
            text_NoLogin.setVisibility(View.INVISIBLE);

        return view;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                Log.e("cilck log in", "true");
                Intent intent = new Intent(getActivity(), login_Activity.class);
                startActivity(intent);
                textView.setVisibility(View.INVISIBLE);
                break;
            case R.id.button_logout:
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
            default:
                break;

        }
    }
}
