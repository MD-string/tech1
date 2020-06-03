package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

import androidx.annotation.Nullable;
import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.setting.bean.ClearZeroModel;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/*
 *清零设置
 */
public class ClearZeroSettingAct extends Activity {
    private Context context;
    private LinearLayout ll_back;
    private EditText et_1,et_2,et_3,et_4,et_5,et_6,et_7,et_8,et_9,et_10,et_11,et_12,et_13,et_14,et_15,et_16;
    private ACache acache;
    private TextView tv_para_title;
    private Button btn_save;
    private TextView tv_close;
    private boolean isClose=false;
    private EditText et_time;
    private boolean isn1,isn2,isn3,isn4,isn5,isn6,isn7,isn8,isn9,isn10,isn11,isn12,isn13,isn14,isn15,isn16;

    public static void start(Context context) {
        Intent intent = new Intent(context, ClearZeroSettingAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_clear_zero);
        acache= ACache.get(context,"WeightFragment");
        initView();
    }
    private void initView() {
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("清零设置");
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_close=(TextView)findViewById(R.id.tv_close);
        et_time=(EditText)findViewById(R.id.et_time);
        String et1=et_time.getText().toString().trim();
        if(!Tools.isEmpty(et1)){
            et_time.setSelection(et1.length());
        }
        et_1=(EditText)findViewById(R.id.et_1);
        et_2=(EditText)findViewById(R.id.et_2);
        et_3=(EditText)findViewById(R.id.et_3);
        et_4=(EditText)findViewById(R.id.et_4);
        et_5=(EditText)findViewById(R.id.et_5);
        et_6=(EditText)findViewById(R.id.et_6);
        et_7=(EditText)findViewById(R.id.et_7);
        et_8=(EditText)findViewById(R.id.et_8);
        et_9=(EditText)findViewById(R.id.et_9);
        et_10=(EditText)findViewById(R.id.et_10);
        et_11=(EditText)findViewById(R.id.et_11);
        et_12=(EditText)findViewById(R.id.et_12);
        et_13=(EditText)findViewById(R.id.et_13);
        et_14=(EditText)findViewById(R.id.et_14);
        et_15=(EditText)findViewById(R.id.et_15);
        et_16=(EditText)findViewById(R.id.et_16);

        btn_save=(Button)findViewById(R.id.btn_save);

        String str=acache.getAsString("clear_btn");//2 表示未开启   1.表示开启
        if(!Tools.isEmpty(str) && str.equals("2")){
            isClose=true;
            tv_close.setText("开启");
            setEnable(false);
            acache.put("clear_btn","2");
            btn_save.setBackgroundColor(getResources().getColor(R.color.lightGray1));
        }else{
            isClose=false;
            tv_close.setText("关闭");
            setEnable(true);
            acache.put("clear_btn","1");
            btn_save.setBackgroundColor(getResources().getColor(R.color.hand_blue));
        }
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClose){
                    isClose=true;
                    tv_close.setText("开启");
                    setEnable(false);
                    btn_save.setBackgroundColor(getResources().getColor(R.color.lightGray1));
                    acache.put("clear_btn","2");
                }else{
                    isClose=false;
                    tv_close.setText("关闭");
                    setEnable(true);
                    btn_save.setBackgroundColor(getResources().getColor(R.color.hand_blue));
                    acache.put("clear_btn","1");
                }

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    public void doSave(){
        ClearZeroModel model=new ClearZeroModel();
        String time=et_time.getText().toString().trim();
        if(Tools.isEmpty(time) || (!Tools.isEmpty(time) && Double.valueOf(time) <2)){
            showTips("时间必须大于或等于2");
        }else{
            model.setCtime(time);
        }
        String et1=et_1.getText().toString().trim();
        if(!Tools.isEmpty(et1)){
            isn1=false;
            model.setChannel1(et1);
        }else{
            isn1=true;
        }
        String et2=et_2.getText().toString().trim();
        if(!Tools.isEmpty(et2)){
            isn2=false;
            model.setChannel2(et2);
        }else{
            isn2=true;
        }
        String et3=et_3.getText().toString().trim();
        if(!Tools.isEmpty(et3)){
            isn3=false;
            model.setChannel3(et3);
        }else{
            isn3=true;
        }
        String et4=et_4.getText().toString().trim();
        if(!Tools.isEmpty(et4)){
            isn4=false;
            model.setChannel4(et4);
        }else{
            isn4=true;
        }
        String et5=et_5.getText().toString().trim();
        if(!Tools.isEmpty(et5)){
            isn5=false;
            model.setChannel5(et5);
        }else{
            isn5=true;
        }
        String et6=et_6.getText().toString().trim();
        if(!Tools.isEmpty(et6)){
            isn6=false;
            model.setChannel6(et6);
        }else{
            isn6=true;
        }
        String et7=et_7.getText().toString().trim();
        if(!Tools.isEmpty(et7)){
            isn7=false;
            model.setChannel7(et7);
        }else{
            isn7=true;
        }
        String et8=et_8.getText().toString().trim();
        if(!Tools.isEmpty(et8)){
            isn8=false;
            model.setChannel8(et8);
        }else{
            isn8=true;
        }
        String et9=et_9.getText().toString().trim();
        if(!Tools.isEmpty(et9)){
            isn9=false;
            model.setChannel9(et9);
        }else{
            isn9=true;
        }
        String et10=et_10.getText().toString().trim();
        if(!Tools.isEmpty(et10)){
            isn10=false;
            model.setChannel10(et10);
        }else{
            isn10=true;
        }
        String et11=et_11.getText().toString().trim();
        if(!Tools.isEmpty(et11)){
            isn11=false;
            model.setChannel11(et11);
        }else{
            isn11=true;
        }
        String et12=et_12.getText().toString().trim();
        if(!Tools.isEmpty(et12)){
            isn12=false;
            model.setChannel12(et12);
        }else{
            isn12=true;
        }
        String et13=et_13.getText().toString().trim();
        if(!Tools.isEmpty(et13)){
            isn13=false;
            model.setChannel13(et13);
        }else{
            isn13=true;
        }
        String et14=et_14.getText().toString().trim();
        if(!Tools.isEmpty(et14)){
            isn14=false;
            model.setChannel14(et14);
        }else{
            isn14=true;
        }
        String et15=et_15.getText().toString().trim();
        if(!Tools.isEmpty(et15)){
            isn15=false;
            model.setChannel15(et15);
        }else{
            isn15=true;
        }
        String et16=et_16.getText().toString().trim();
        if(!Tools.isEmpty(et16)){
            isn16=false;
            model.setChannel16(et16);
        }else{
            isn16=true;
        }
        if(isn1 || isn2 || isn3 || isn4 || isn5 || isn6 || isn7 || isn8 || isn9 || isn10 || isn11 || isn12 || isn13 || isn14 || isn15 || isn16 ){
            showTips("阀值必须大于等于0");
        }else{
            acache.put("clear_zero_model",(Serializable)model);
            showTips("保存成功");
        }
    }

    public void setEnable(boolean isUseable){
        btn_save.setEnabled(isUseable);
        et_time.setEnabled(isUseable);
        et_1.setEnabled(isUseable);
        et_2.setEnabled(isUseable);
        et_3.setEnabled(isUseable);
        et_4.setEnabled(isUseable);
        et_5.setEnabled(isUseable);
        et_6.setEnabled(isUseable);
        et_7.setEnabled(isUseable);
        et_8.setEnabled(isUseable);
        et_9.setEnabled(isUseable);
        et_10.setEnabled(isUseable);
        et_11.setEnabled(isUseable);
        et_12.setEnabled(isUseable);
        et_13.setEnabled(isUseable);
        et_14.setEnabled(isUseable);
        et_15.setEnabled(isUseable);
        et_16.setEnabled(isUseable);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
