package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/*
 *通道检测
 */
public class PassCheckAct extends Activity {
    private Context context;
    private LinearLayout ll_back;
    private TextView tv_para_title;
    private EditText et_date,et_fa;
    private Button bt_save_mv;
    private ACache acache;
    private EditText et_ad_value;

    public static void start(Context context) {
        Intent intent = new Intent(context, PassCheckAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_pass_check);
        acache= ACache.get(context,"WeightFragment");
        initView();
    }
    private void initView() {
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("通道检测");
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_date=(EditText)findViewById(R.id.et_date);
        String date =acache.getAsString("pass_time");
        String value =acache.getAsString("pass_value");
        if(!Tools.isEmpty(date)){
            et_date.setText(date);
        }

        String et1=et_date.getText().toString().trim();
        if(!Tools.isEmpty(et1)){
            et_date.setSelection(et1.length());
        }
        et_fa=(EditText)findViewById(R.id.et_fa);
        if(!Tools.isEmpty(value)){
            et_fa.setText(value);
        }
        String et2=et_fa.getText().toString().trim();
        if(!Tools.isEmpty(et2)){
            et_fa.setSelection(et2.length());
        }

        et_ad_value=(EditText)findViewById(R.id.et_ad_value);
        String adValue =acache.getAsString("setting_ad_value");
        if(!Tools.isEmpty(adValue)){
            et_ad_value.setText(adValue);
        }

        bt_save_mv=(Button)findViewById(R.id.bt_save_mv);
        bt_save_mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=et_date.getText().toString().trim();
                String fazhi=et_fa.getText().toString().trim();
                if(Tools.isEmpty(date) || Tools.isEmpty(fazhi)){
                    showTips("时间或域值不能为空");
                    return;
                }
                String adValue=et_ad_value.getText().toString();
                if(Tools.isEmpty(adValue)){
                    adValue="131072";
                }

                acache.put("pass_time",date);
                acache.put("pass_value",fazhi);
                acache.put("setting_ad_value",adValue);
                showTips("保存成功");

            }
        });

    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
