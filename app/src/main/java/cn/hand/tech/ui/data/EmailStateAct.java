package cn.hand.tech.ui.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import cn.hand.tech.R;
import cn.hand.tech.ui.data.presenter.SaveMVVData;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/*
 *Email 参数
 */
public class EmailStateAct extends Activity {
    private Context context;
    private Spinner spinner1;
    private CheckBox channel_1,channel_2,channel_3,channel_4,channel_5,channel_6,channel_7,channel_8,channel_9,channel_10,channel_11,channel_12,channel_13,channel_14,channel_15,channel_16;
    private EditText et_times,et_status,et_location;
    private Button btn_cancle,btn_ok;
    private String pzString="";
    private String time;
    private String pointLocstr="";


    public static void start(Context context,String time1) {
        Intent intent = new Intent(context, EmailStateAct.class);
        intent.putExtra("time_1",time1);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.activity_email_state);
        time=getIntent().getStringExtra("time_1");
        initView();
    }
    private void initView() {
        et_location=(EditText)findViewById(R.id.et_location);
        String et=et_location.getText().toString().trim();
        if(!Tools.isEmpty(et)){
            et_location.setSelection(et.length());
        }
        et_status=(EditText)findViewById(R.id.et_status);
        et_times=(EditText)findViewById(R.id.et_times);
        spinner1=(Spinner)findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] languages = getResources().getStringArray(R.array.languages);

                pointLocstr=languages[position];//点位
                if(0==position){
                    pointLocstr="";
                }
                if(view !=null ){
                    TextView v=(TextView)view;
                    v.setTextColor(getResources().getColor(R.color.lightGray1));
                    v.setTextSize(13f);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pointLocstr="";
            }
        });
        channel_1=(CheckBox)findViewById(R.id.channel_1);
        channel_2=(CheckBox)findViewById(R.id.channel_2);
        channel_3=(CheckBox)findViewById(R.id.channel_3);
        channel_4=(CheckBox)findViewById(R.id.channel_4);
        channel_5=(CheckBox)findViewById(R.id.channel_5);
        channel_6=(CheckBox)findViewById(R.id.channel_6);
        channel_7=(CheckBox)findViewById(R.id.channel_7);
        channel_8=(CheckBox)findViewById(R.id.channel_8);
        channel_9=(CheckBox)findViewById(R.id.channel_9);
        channel_10=(CheckBox)findViewById(R.id.channel_10);
        channel_11=(CheckBox)findViewById(R.id.channel_11);
        channel_12=(CheckBox)findViewById(R.id.channel_12);
        channel_13=(CheckBox)findViewById(R.id.channel_13);
        channel_14=(CheckBox)findViewById(R.id.channel_14);
        channel_15=(CheckBox)findViewById(R.id.channel_15);
        channel_16=(CheckBox)findViewById(R.id.channel_16);

        channel_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"1、";
                }
            }
        });
        channel_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"2、";
                }
            }
        });
        channel_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"3、";
                }
            }
        });
        channel_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"4、";
                }
            }
        });
        channel_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"5、";
                }
            }
        });
        channel_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"6、";
                }
            }
        });     channel_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"7、";
                }
            }
        });
        channel_8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"8、";
                }
            }
        });
        channel_9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"9、";
                }
            }
        });
        channel_10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"10、";
                }
            }
        });     channel_11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"11、";
                }
            }
        });

        channel_12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"12、";
                }
            }
        });
        channel_13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"13、";
                }
            }
        });     channel_14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"14、";
                }
            }
        });
        channel_15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"15、";
                }
            }
        });

        channel_16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pzString=pzString+"16、";
                }
            }
        });


        btn_cancle=(Button)findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_ok=(Button)findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSendEmail();
            }
        });
    }
    private void doSendEmail(){
        String loac=et_location.getText().toString().trim();
        if(Tools.isEmpty(loac) || (!Tools.isEmpty(loac) && Double.valueOf(loac) <1)){
            showTips("请输入位置");
            return;
        }
        String status= et_status.getText().toString().trim();
        if(Tools.isEmpty(status)){
            showTips("请输入类型");
            return;
        }
        String etime= et_times.getText().toString().trim();


        String content=loac;
        if(!Tools.isEmpty(pointLocstr) && !pointLocstr.equals("无")){
            content=content+"_"+pointLocstr.toLowerCase();
        }
//        content=content+etime+"-"+status+"-偏载区";
//        if(!Tools.isEmpty(pzString)){
//            pzString = pzString.substring(0,pzString.length() - 1);
//            content=content+"("+pzString+")";
//        }

        SaveMVVData.sendXX(this, time,content);
        finish();
    }


    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
