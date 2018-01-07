package com.example.admin.campaigo.ui.fragment;

/**
 * Created by admin on 2017/12/15.
 */
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.example.admin.campaigo.Adapter.CampaiAdapter;
import com.example.admin.campaigo.R;
import com.example.admin.campaigo.model.Campaign;
import com.example.admin.campaigo.model.User;
import com.example.admin.campaigo.network.HttpUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class campaignsFragment extends  Fragment {
    List<Campaign> searchCampaigns = new ArrayList<>();
    CampaiAdapter listAdapter;
    String campaignsJson;
    String url ="http://115.159.55.118/campaign/search?wd=";
    String Searchurl;
    String campaignName= "";
    RecyclerView recyclerView;

     private EditText ET;
//    private TextView searchTextView;

//   从此处开始 实现轮播图
    private View view;
    private ViewPager mViewPaper;
    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    private SwipeRefreshLayout swipeRefreshLayout;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.anmdrod1,
            R.drawable.android2,
            R.drawable.android3,
            R.drawable.p4,
            R.drawable.p5
    };
    //存放图片的标题
    private String[] titles = new String[]{
            "技术沙龙",
            "安卓学习",
            "技术讲座",
            "经验分享",
            "项目管理"
    };
    private TextView title;
    private ViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;//定时执行周期性任务

    @Nullable


    private void setView(){

        mViewPaper = (ViewPager)view.findViewById(R.id.vp);
        //显示的图片
        images = new ArrayList<>();
        for(int i = 0; i < imageIds.length; i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //显示的小点
        dots = new ArrayList<>();
        dots.add(view.findViewById(R.id.dot_0));
        dots.add(view.findViewById(R.id.dot_1));
        dots.add(view.findViewById(R.id.dot_2));
        dots.add(view.findViewById(R.id.dot_3));
        dots.add(view.findViewById(R.id.dot_4));

        title = (TextView) view.findViewById(R.id.title);
        title.setText(titles[0]);

        adapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(adapter);

        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageSelected(int position) {
                title.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.dian);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dian);

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /*定义的适配器*/
    public class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub
//          super.destroyItem(container, position, object);
//          view.removeView(view.getChildAt(position));
//          view.removeViewAt(position);
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            // TODO Auto-generated method stub
            view.addView(images.get(position));
            return images.get(position);
        }

    }

    //利用线程池定时执行动画轮播
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPageTask(),2,2, TimeUnit.SECONDS);//参数1：initdelay。参数2：周期
    }


    //图片轮播任务
    private class ViewPageTask implements Runnable{

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            // mHandler.sendEmptyMessage(0);
        }
    }


     // 接收子线程传递过来的数据
    private Handler mHandler = new Handler(){
        @Override
        public void publish(LogRecord logRecord) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }

        public void handleMessage(android.os.Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        };
    };
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }
//轮播功能到此结束


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_campai_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_campaign);
        Searchurl = url+campaignName;
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF4081"),Color.parseColor("#303F9F"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetCampaignsTask().execute();
            }
        });
        setView();
        new GetCampaignsTask().execute();
        //recyclerView = (RecyclerView) view.findViewById(R.id.campai_list_recycler);
      //  LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
      //  recyclerView.setLayoutManager(layoutManager);
       // CampaiAdapter adapter = new CampaiAdapter(campaignList);
       // recyclerView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ET = (EditText) getActivity().findViewById(R.id.search_campaign);
        TextView searchTextView = (TextView) getActivity().findViewById(R.id.activity_search);

        ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "请输入您想查找的活动信息", Toast.LENGTH_LONG).show();
            }
        });
        //这里写的是首页活动模糊搜索的监听
        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里写的是首页活动模糊搜索的监听
                campaignName = ET.getText().toString();
                Searchurl = url+campaignName;
                new GetCampaignsTask().execute();
                Log.e("What's in thedittext??", ET.getText().toString());
            }
        });
    }

    class GetCampaignsTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.e("GetCamsListPreExecute", "Pre Success");


        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.e("GetCamsListPostExecute", "Post Success");
            recyclerView = (RecyclerView)getActivity().findViewById(R.id.campai_list_recycler);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            listAdapter = new CampaiAdapter(searchCampaigns);
            recyclerView.setAdapter(listAdapter);
            recyclerView.setLayoutManager(layoutManager);
            listAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Searchurl).build();
                Response response = client.newCall(request).execute();
                campaignsJson= response.body().string();
                searchCampaigns = JSON.parseArray(campaignsJson, Campaign.class);
                Collections.reverse(searchCampaigns); // 倒序排列
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private String UserPreferencetoJson() {
        User user = new User();
        user.init();
        SharedPreferences pref = getActivity().getSharedPreferences("user_Info", MODE_PRIVATE);
        String json = pref.getString("User_Json", JSON.toJSONString(user));
        return json;
    }
}

