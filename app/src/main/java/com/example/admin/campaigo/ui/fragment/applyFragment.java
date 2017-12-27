package com.example.admin.campaigo.ui.fragment;



        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.alibaba.fastjson.JSON;
        import com.example.admin.campaigo.R;
        import com.example.admin.campaigo.model.Campaign;
        import com.example.admin.campaigo.model.User;
        import com.example.admin.campaigo.network.HttpUtil;
        import com.example.admin.campaigo.ui.activity.login_Activity;


        import java.io.IOException;
        import java.sql.Timestamp;
        import java.text.SimpleDateFormat;

        import okhttp3.Call;
        import okhttp3.Response;

        import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shengyiqun on 2017/12/16.
 */

public class applyFragment extends Fragment {
    @Nullable
    String DOMIN = "http://115.159.55.118/campaign/ask?id=";
    String url;
    EditText edit_apply_name;
    EditText edit_apply_startTime;
    EditText edit_apply_endTime;
    EditText edit_apply_endeadTime;
    EditText edit_apply_describe;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply, container, false);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss.ss");
        Button buttonApply = (Button) view.findViewById(R.id.button_ApplyCampaign);
        edit_apply_name=(EditText) view.findViewById(R.id.edit_apply_name);
        edit_apply_startTime = (EditText) view.findViewById(R.id.edit_apply_starttime);
        edit_apply_endTime=(EditText) view.findViewById(R.id.edit_apply_finishedline);
        edit_apply_endeadTime=(EditText) view.findViewById(R.id.edit_apply_endedline);
        edit_apply_describe = (EditText) view.findViewById(R.id.edit_apply_describe);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNameValied(edit_apply_name.getText().toString()) || !isStartValied(edit_apply_startTime.getText().toString())
                        || !isendValied(edit_apply_endTime.getText().toString()) || !isendearValied(edit_apply_endeadTime.getText().toString())
                        || !isdescribeValied(edit_apply_describe.getText().toString())) {
                    Toast.makeText(getActivity(), "格式错误", Toast.LENGTH_SHORT).show();
                } else {
                    Campaign testCam = new Campaign();
                    testCam.setCaname(edit_apply_name.getText().toString());
                    testCam.setStartline(Timestamp.valueOf(edit_apply_startTime.getText().toString()));
                    testCam.setEndeadline(Timestamp.valueOf(edit_apply_endTime.getText().toString()));
                    testCam.setEndline(Timestamp.valueOf(edit_apply_endeadTime.getText().toString()));
                    testCam.setDescribe(edit_apply_describe.getText().toString());
                    String testJson = JSON.toJSONString(testCam);
                    url = DOMIN+getUserId()+"&info="+testJson;
                    Log.e("Succeed", url);
                    new ApplyTask().execute();
                }
            }
        });
        return view;
    }
    class ApplyTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("ApplyPreExecute", "Success");

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.d("ApplyPostExecute", "Success");
            Toast.makeText(getActivity(), "ApplySuccess", Toast.LENGTH_SHORT).show();
//            Toast.makeText(CampaignInfoActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("ApplyError", "Net Error");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e("Applyresponse", "ok"+response);
                }
            });
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
    private boolean isNameValied(String name) {
        return name.length() > 0;
    }
    private boolean isStartValied(String start) {
        return start.length() > 0;
    }
    private boolean isendValied(String end) {
        return end.length() > 0;
    }
    private boolean isendearValied(String endead) {
        return endead.length() > 0;
    }
    private boolean isdescribeValied(String describe) {
        return describe.length() > 0;
    }
    private String getUserId() {
        String UserJson = UserPreferencetoJson();
        User user = JSON.parseObject(UserJson, User.class);
        return user.getId();
    }
}

