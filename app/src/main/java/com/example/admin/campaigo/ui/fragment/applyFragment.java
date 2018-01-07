package com.example.admin.campaigo.ui.fragment;



        import android.app.DatePickerDialog;
        import android.app.TimePickerDialog;
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
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.TimePicker;
        import android.widget.Toast;

        import com.alibaba.fastjson.JSON;
        import com.example.admin.campaigo.R;
        import com.example.admin.campaigo.model.Campaign;
        import com.example.admin.campaigo.model.User;
        import com.example.admin.campaigo.network.HttpUtil;
        import com.example.admin.campaigo.ui.activity.login_Activity;


        import java.io.IOException;
        import java.sql.Timestamp;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.Locale;
        import java.util.regex.Pattern;

        import okhttp3.Call;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.Response;

        import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shengyiqun on 2017/12/16.
 */

public class applyFragment extends Fragment {
    public static final String REGEX_USERNAME ="^[A-Za-z0-9\u4e00-\u9fa5]+";//利用正则表达式，规定只能是字母，数字，中文
    @Nullable
    String DOMIN = "http://115.159.55.118/campaign/ask?id=";
    String url;
    EditText edit_apply_name;
    TextView edit_apply_startTime;
    TextView edit_apply_endTime;
    TextView edit_apply_endeadTime;
    EditText edit_apply_describe;
    String flag;
    Calendar recordCalendar;//创建Calendear
    Calendar recordCalendarend;//创建Calendear
    Calendar recordCalendarendead;//创建Calendear
    DateFormat recordDateFormate;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply, container, false);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss.ss");
        Button buttonApply = (Button) view.findViewById(R.id.button_ApplyCampaign);
        edit_apply_name=(EditText) view.findViewById(R.id.edit_apply_name);
        edit_apply_startTime = (TextView) view.findViewById(R.id.edit_apply_starttime);
        edit_apply_endTime=(TextView) view.findViewById(R.id.edit_apply_finishedline);
        edit_apply_endeadTime=(TextView) view.findViewById(R.id.edit_apply_endedline);
        edit_apply_describe = (EditText) view.findViewById(R.id.edit_apply_describe);
        recordCalendar = Calendar.getInstance(Locale.CHINA);//
        recordCalendarend = Calendar.getInstance(Locale.CHINA);//
        recordCalendarendead = Calendar.getInstance(Locale.CHINA);//
        recordDateFormate = DateFormat.getDateTimeInstance();//

        edit_apply_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(),StarttimeSetListener,recordCalendar.get(Calendar.HOUR_OF_DAY),recordCalendar.get(Calendar.MINUTE),true).show();
                new DatePickerDialog(getActivity(),StartdateSetListener,recordCalendar.get(Calendar.YEAR),recordCalendar.get(Calendar.MONTH),recordCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        edit_apply_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(),EndtimeSetListener,recordCalendarend.get(Calendar.HOUR_OF_DAY),recordCalendarend.get(Calendar.MINUTE),true).show();
                new DatePickerDialog(getActivity(),EnddateSetListener,recordCalendarend.get(Calendar.YEAR),recordCalendarend.get(Calendar.MONTH),recordCalendarend.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        edit_apply_endeadTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(),EndeadtimeSetListener,recordCalendarendead.get(Calendar.HOUR_OF_DAY),recordCalendarendead.get(Calendar.MINUTE),true).show();
                new DatePickerDialog(getActivity(),EndeaddateSetListener,recordCalendarendead.get(Calendar.YEAR),recordCalendarendead.get(Calendar.MONTH),recordCalendarendead.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNameValied(edit_apply_name.getText().toString()) || !isStartValied(edit_apply_startTime.getText().toString())
                        || !isendValied(edit_apply_endTime.getText().toString()) || !isendearValied(edit_apply_endeadTime.getText().toString())
                        || !isdescribeValied(edit_apply_describe.getText().toString())) {
                    Toast.makeText(getActivity(), "格式错误", Toast.LENGTH_SHORT).show();
                }else if (recordCalendar.getTimeInMillis() < System.currentTimeMillis()) {
                    Toast.makeText(getActivity(), "活动开始时间不能早于现在时间", Toast.LENGTH_SHORT).show();
                }
                else if (recordCalendar.getTimeInMillis() >recordCalendarend.getTimeInMillis()) {
                    Toast.makeText(getActivity(), "活动结束时间不能早于开始时间", Toast.LENGTH_SHORT).show();
                }
                else if (recordCalendar.getTimeInMillis()<recordCalendarendead.getTimeInMillis()) {
                    Toast.makeText(getActivity(), "活动报名截止时间不能晚于开始时间", Toast.LENGTH_SHORT).show();
                }
                else if (recordCalendarendead.getTimeInMillis() < System.currentTimeMillis()) {
                    Toast.makeText(getActivity(), "活动报名截止时间不能早于现在时间", Toast.LENGTH_SHORT).show();
                }
                else {
                    Campaign testCam = new Campaign();
                    testCam.setCaname(edit_apply_name.getText().toString());
                    testCam.setStartline(Timestamp.valueOf(edit_apply_startTime.getText().toString()));
                    testCam.setEndeadline(Timestamp.valueOf(edit_apply_endeadTime.getText().toString()));
                    testCam.setEndline(Timestamp.valueOf(edit_apply_endTime.getText().toString()));
                    testCam.setDescribe(edit_apply_describe.getText().toString());
                    String testJson = JSON.toJSONString(testCam);
                    url = DOMIN + getUserId() + "&info=" + testJson;
                    Log.d("Succeed", url);
                    new ApplyTask().execute();
                }
            }
        });
        return view;
    }
    final DatePickerDialog.OnDateSetListener StartdateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
            recordCalendar.set(Calendar.YEAR,year);
            recordCalendar.set(Calendar.MONTH,month);
            recordCalendar.set(Calendar.DAY_OF_MONTH,date);
            updateStartDate();
        }
    };

    final TimePickerDialog.OnTimeSetListener StarttimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker timePicker, int hour, int min) {
            recordCalendar.set(Calendar.HOUR_OF_DAY,hour);
            recordCalendar.set(Calendar.MINUTE,min);
            updateStartDate();
        }
    };
    final DatePickerDialog.OnDateSetListener EnddateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
            recordCalendarend.set(Calendar.YEAR,year);
            recordCalendarend.set(Calendar.MONTH,month);
            recordCalendarend.set(Calendar.DAY_OF_MONTH,date);
            updateEndDate();
        }
    };

    final TimePickerDialog.OnTimeSetListener EndtimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker timePicker, int hour, int min) {
            recordCalendarend.set(Calendar.HOUR_OF_DAY,hour);
            recordCalendarend.set(Calendar.MINUTE,min);
            updateEndDate();
        }
    };
    final DatePickerDialog.OnDateSetListener EndeaddateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
            recordCalendarendead.set(Calendar.YEAR,year);
            recordCalendarendead.set(Calendar.MONTH,month);
            recordCalendarendead.set(Calendar.DAY_OF_MONTH,date);
            updateEndeadDate();
        }
    };

    final TimePickerDialog.OnTimeSetListener EndeadtimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker timePicker, int hour, int min) {
            recordCalendarendead.set(Calendar.HOUR_OF_DAY,hour);
            recordCalendarendead.set(Calendar.MINUTE,min);
            updateEndeadDate();
        }
    };
    class ApplyTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("ApplyPreExecute", "Success");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.d("ApplyPostExecute", "Success");
            if (flag.equals("true")) {
                Toast.makeText(getActivity(), "申请成功，请等待活动通过", Toast.LENGTH_SHORT).show();
                edit_apply_endeadTime.setText("");
                edit_apply_endTime.setText("");
                edit_apply_startTime.setText("");
                edit_apply_describe.setText("");
                edit_apply_name.setText("");
            }

            else{
                Toast.makeText(getActivity(), "活动已经申请过", Toast.LENGTH_SHORT).show();}
//            Toast.makeText(CampaignInfoActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                flag = response.body().string();
                Log.e("Applyresponse", "flag====>"+flag);
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
    private boolean isNameValied(String name) {
        return (name.length() > 0&&name.length()<16&&(Pattern.matches(REGEX_USERNAME,name)));
    }
    private boolean isStartValied(String start) {
        return (start.length() > 0);
    }
    private boolean isendValied(String end) {
        return end.length() > 0;
    }
    private boolean isendearValied(String endead) {
        return endead.length() > 0;
    }
    private boolean isdescribeValied(String describe) {
        return (describe.length() > 0&&describe.length()<=30&&(Pattern.matches(REGEX_USERNAME,describe)));
    }
    private String getUserId() {
        String UserJson = UserPreferencetoJson();
        User user = JSON.parseObject(UserJson, User.class);
        return user.getId();
    }
    private void updateStartDate(){
        DateFormat df =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        edit_apply_startTime.setText(df.format(recordCalendar.getTimeInMillis()));
        Log.d("TIme", df.format(recordCalendar.getTimeInMillis()));
    }
    private void updateEndDate(){
        DateFormat df =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        edit_apply_endTime.setText(df.format(recordCalendarend.getTimeInMillis()));
        Log.d("TIme", df.format(recordCalendarend.getTimeInMillis()));
    }
    private void updateEndeadDate(){
        DateFormat df =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        edit_apply_endeadTime.setText(df.format(recordCalendarendead.getTimeInMillis()));
        Log.d("Time", df.format(recordCalendarendead.getTimeInMillis()));
    }
}

