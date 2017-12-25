//package com.example.admin.campaigo.ui.activity;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//import com.example.admin.campaigo.R;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}
package com.example.admin.campaigo.ui.activity;

import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.example.admin.campaigo.R;
import com.example.admin.campaigo.model.Campaign;
import com.example.admin.campaigo.model.User;
import com.example.admin.campaigo.ui.fragment.NoAccessFragment;
import com.example.admin.campaigo.ui.fragment.PassCampaignFragment;
import com.example.admin.campaigo.ui.fragment.applyFragment;
import com.example.admin.campaigo.ui.fragment.campaignsFragment;
import com.example.admin.campaigo.ui.fragment.userFragment;
import com.example.admin.campaigo.ui.fragment.yoursFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navi_item_Campaigns);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navi_item_Campaigns:
                    replaceFragment(new campaignsFragment());
                    return true;

                case R.id.navi_item_YoursCampaigns:
                    replaceFragment(new yoursFragment());
                    return true;

                case R.id.navi_item_UpCampaign:
                    if(getPosition().equals("org"))
                        replaceFragment(new applyFragment());
                    else if(getPosition().equals("tea"))
                        replaceFragment(new PassCampaignFragment());
                    else replaceFragment(new NoAccessFragment());
                    return true;

                case R.id.navi_item_Users:
                    replaceFragment(new userFragment());
                    return true;
            }
            return false;
        }

    };
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
}
