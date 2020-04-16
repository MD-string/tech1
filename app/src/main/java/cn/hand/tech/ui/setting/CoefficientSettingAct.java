package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDSendDataModel;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.utils.SPUtil;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/*
 *系数设定
 */
public class CoefficientSettingAct extends Activity {
    private Context context;
    private LinearLayout ll_back;
    private EditText et_1,et_2,et_3,et_4,et_5,et_6,et_7,et_8,et_9,et_10,et_11,et_12,et_13,et_14,et_15,et_16;
    private Button bt_read,bt_write;
    private BroadcastReceiver receiver;
    private boolean bIsRead;
    private ACache acache;
    private boolean isConnected;
    private TextView tv_para_title;

    public static void start(Context context) {
        Intent intent = new Intent(context, CoefficientSettingAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_coefficient);
        acache= ACache.get(context,"WeightFragment");
        initView();
        registerBrodcat();
    }
    private void initView() {
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("系数设定");
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_1=(EditText)findViewById(R.id.et_1);
        String et1=et_1.getText().toString().trim();
        if(!Tools.isEmpty(et1)){
            et_1.setSelection(et1.length());
        }
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

        bt_read=(Button)findViewById(R.id.bt_read);
        bt_write=(Button)findViewById(R.id.bt_write);
        getChannelFlag();
        bt_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  isCon=acache.getAsString("is_connect");
                if("2".equals(isCon)){
                    isConnected=true;
                }else{
                    isConnected=false;
                }
                if (isConnected) {
                    bIsRead = true;
                    showTips( "发送读取命令");

                    Intent readIntent=new Intent(BleConstant.ACTION_BLE_READ_NOTY);
                    sendBroadcast(readIntent);
                } else {
                    showTips( "蓝牙未连接");
                    
                }
            }
        });
        bt_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  isCon=acache.getAsString("is_connect");
                if("2".equals(isCon)){
                    isConnected=true;
                }else{
                    isConnected=false;
                }
                if (isConnected) {
                    if (channelNotNull()) {
                        return;
                    }

                    bIsRead = false;
                    showTips( "发送写命令");
                    
                    //发送命令 不需要写时间
                    HDSendDataModel model = new HDSendDataModel();
                    model.mmv1 = Float.parseFloat(et_1.getText().toString());
                    model.mmv2 = Float.parseFloat(et_2.getText().toString());
                    model.mmv3 = Float.parseFloat(et_3.getText().toString());
                    model.mmv4 = Float.parseFloat(et_4.getText().toString());
                    model.mmv5 = Float.parseFloat(et_5.getText().toString());
                    model.mmv6 = Float.parseFloat(et_6.getText().toString());
                    model.mmv7 = Float.parseFloat(et_7.getText().toString());
                    model.mmv8 = Float.parseFloat(et_8.getText().toString());
                    model.mmv9 = Float.parseFloat(et_9.getText().toString());
                    model.mmv10 = Float.parseFloat(et_10.getText().toString());
                    model.mmv11 = Float.parseFloat(et_11.getText().toString());
                    model.mmv12 = Float.parseFloat(et_12.getText().toString());
                    model.mmv13 = Float.parseFloat(et_13.getText().toString());
                    model.mmv14 = Float.parseFloat(et_14.getText().toString());
                    model.mmv15 = Float.parseFloat(et_15.getText().toString());
                    model.mmv16 = Float.parseFloat(et_16.getText().toString());

                    Bundle bundle=new Bundle();
                    bundle.putSerializable("coefficient_act",model);
                    Intent readIntent=new Intent(BleConstant.ACTION_BLE_WRITE_COE);
                    readIntent.putExtras(bundle);
                    sendBroadcast(readIntent);
                } else {
                    showTips( "蓝牙未连接");
                }
            }
        });
    }
    private boolean channelNotNull() {
        if (TextUtils.isEmpty(et_1.getText().toString())) {
            showTips( "通道1不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_2.getText().toString())) {
            showTips( "通道2不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_3.getText().toString())) {
            showTips( "通道3不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_4.getText().toString())) {
            showTips( "通道4不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_5.getText().toString())) {
            showTips( "通道5不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_6.getText().toString())) {
            showTips( "通道6不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_7.getText().toString())) {
            showTips( "通道7不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_8.getText().toString())) {
            showTips( "通道8不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_9.getText().toString())) {
            showTips( "通道9不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_10.getText().toString())) {
            showTips( "通道10不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_11.getText().toString())) {
            showTips( "通道11不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_12.getText().toString())) {
            showTips( "通道12不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_13.getText().toString())) {
            showTips( "通道13不能为空");
            
            return true;

        }

        if (TextUtils.isEmpty(et_15.getText().toString())) {
            showTips( "通道14不能为空");
            
            return true;

        }
        if (TextUtils.isEmpty(et_16.getText().toString())) {
            showTips( "通道15不能为空");
            
            return true;

        }

        if (TextUtils.isEmpty(et_14.getText().toString())) {
            showTips( "通道16不能为空");
            
            return true;

        }
        return false;
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    private void getChannelFlag() {
        int mType = (int) SPUtil.get(context, "kbType", -1);
        HDSendDataModel sMode = new HDSendDataModel();
        if (mType == 0) {
            //必须加f
            sMode.mmv1=  (float) SPUtil.get(context, "kValue1", 0.00000000f);
            sMode.mmv2= (float) SPUtil.get(context, "kValue2", 0.00000000f);
            sMode.mmv3= (float) SPUtil.get(context, "kValue3", 0.00000000f);
            sMode.mmv4= (float) SPUtil.get(context, "kValue4", 0.00000000f);
            sMode.mmv5= (float) SPUtil.get(context, "kValue5", 0.00000000f);
            sMode.mmv6= (float) SPUtil.get(context, "kValue6", 0.00000000f);
            sMode.mmv7= (float) SPUtil.get(context, "kValue7", 0.00000000f);
            sMode.mmv8= (float) SPUtil.get(context, "kValue8", 0.00000000f);
            sMode.mmv9= (float) SPUtil.get(context, "kValue9", 0.00000000f);
            sMode.mmv10= (float) SPUtil.get(context, "kValue10", 0.00000000f);
            sMode.mmv11= (float) SPUtil.get(context, "kValue11", 0.00000000f);
            sMode.mmv12= (float) SPUtil.get(context, "kValue12", 0.00000000f);
            sMode.mmv13= (float) SPUtil.get(context, "kValue13", 0.00000000f);
            sMode.mmv14= (float) SPUtil.get(context, "kValue14", 0.00000000f);
            sMode.mmv15= (float) SPUtil.get(context, "kValue15", 0.00000000f);
            sMode.mmv16= (float)  SPUtil.get(context, "kValue16", 0.00000000f);

        } else if (mType == 1) {
            sMode.mmv1= (float) SPUtil.get(context, "bValue1", 0.00000000f);
            sMode.mmv2= (float) SPUtil.get(context, "bValue2", 0.00000000f);
            sMode.mmv3= (float) SPUtil.get(context, "bValue3", 0.00000000f);
            sMode.mmv4= (float) SPUtil.get(context, "bValue4", 0.00000000f);
            sMode.mmv5= (float) SPUtil.get(context, "bValue5", 0.00000000f);
            sMode.mmv6= (float) SPUtil.get(context, "bValue6", 0.00000000f);
            sMode.mmv7= (float) SPUtil.get(context, "bValue7", 0.00000000f);
            sMode.mmv8= (float) SPUtil.get(context, "bValue8", 0.00000000f);
            sMode.mmv9= (float) SPUtil.get(context, "bValue9", 0.00000000f);
            sMode.mmv10= (float) SPUtil.get(context, "bValue10", 0.00000000f);
            sMode.mmv11= (float) SPUtil.get(context, "bValue11", 0.00000000f);
            sMode.mmv12= (float) SPUtil.get(context, "bValue12", 0.00000000f);
            sMode.mmv13= (float) SPUtil.get(context, "bValue13", 0.00000000f);
            sMode.mmv14= (float) SPUtil.get(context, "bValue14", 0.00000000f);
            sMode.mmv15= (float) SPUtil.get(context, "bValue15", 0.00000000f);
            sMode.mmv16= (float)  SPUtil.get(context, "bValue16", 0.00000000f);
        }
        // 显示所有收到的消息及其细节
        // setEditTextValue(sMode);
    }

    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(action.equals(BleConstant.ACTION_SEND_DATA)){
                    HDSendDataModel sMode = (HDSendDataModel) intent.getSerializableExtra("data");
                    DLog.e("CoefficientSettingAct","sMode==" + sMode.weight);
                    DLog.e("CoefficientSettingAct","sMode111==" + sMode.mmv1);
                    // 显示所有收到的消息及其细节
                    setEditTextValue(sMode);
                    if (bIsRead) {
                        showTips("读取成功");
                    } else {
                        showTips( "写入成功");

                    }

                }

            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTION_SEND_DATA);
        registerReceiver(receiver, filter);
    }
    public void setEditTextValue(HDSendDataModel model) {
        DecimalFormat myformat = new DecimalFormat("0.00000000");
        et_1.setText(myformat.format(model.mmv1));
        et_2.setText(myformat.format(model.mmv2));
        et_3.setText(myformat.format(model.mmv3));
        et_4.setText(myformat.format(model.mmv4));
        et_5.setText(myformat.format(model.mmv5));
        et_6.setText(myformat.format(model.mmv6));
        et_7.setText(myformat.format(model.mmv7));
        et_8.setText(myformat.format(model.mmv8));
        et_9.setText(myformat.format(model.mmv9));
        et_10.setText(myformat.format(model.mmv10));
        et_11.setText(myformat.format(model.mmv11));
        et_12.setText(myformat.format(model.mmv12));
        et_13.setText(myformat.format(model.mmv13));
        et_14.setText(myformat.format(model.mmv14));
        et_15.setText(myformat.format(model.mmv15));
        et_16.setText(myformat.format(model.mmv16));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
           this.unregisterReceiver(receiver);
            receiver = null;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
