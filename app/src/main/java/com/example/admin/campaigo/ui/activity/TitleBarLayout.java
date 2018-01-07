package com.example.admin.campaigo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.campaigo.R;

/**
 * Created by shengyiqun on 2018/1/6.
 */

public class TitleBarLayout extends LinearLayout {
    public TitleBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.login_title_bar, this);
        TextView textView = (TextView) findViewById(R.id.titleBar_city_name);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
