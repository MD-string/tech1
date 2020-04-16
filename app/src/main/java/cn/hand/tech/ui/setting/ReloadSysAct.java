package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.hand.tech.R;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.utils.ToastUtil;

/*
 *系统重启
 */
public class ReloadSysAct extends Activity {
    private Context context;
    private LinearLayout ll_back;
    private TextView tv_para_title;
    private Button bt_reload,bt_return;
    private ACache acache;
    private TextView tv_ble_status;
    private boolean isConnected;
    private BroadcastReceiver receiver;

    public static void start(Context context) {
        Intent intent = new Intent(context, ReloadSysAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_reload);
        acache= ACache.get(context,"WeightFragment");
        initView();
        registerBrodcat();
    }
    private void initView() {
        String isCon = acache.getAsString("is_connect");
        if ("2".equals(isCon)) {
            isConnected = true;
        } else {
            isConnected = false;
        }
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("设备重启");
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_ble_status=(TextView)findViewById(R.id.tv_ble_status);
        if(isConnected){
            tv_ble_status.setText("已连接");
            tv_ble_status.setTextColor(getResources().getColor(R.color.hand_blue));
        }else{
            tv_ble_status.setText("未连接");
            tv_ble_status.setTextColor(getResources().getColor(R.color.red));
        }

        bt_reload=(Button)findViewById(R.id.bt_reload);

        bt_return=(Button)findViewById(R.id.bt_return);
        bt_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    showTips("蓝牙未连接");
                    return;
                }
                doshowDialog();
            }
        });

        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    showTips("蓝牙未连接");
                    return;
                }
                doshowRetrun();
            }
        });

    }
    public void doshowDialog(){
        final AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        myDialog.setTitle(getResources().getString(R.string.hint));
        myDialog.setMessage("是否重启主机？");
        myDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent readIntent=new Intent(BleConstant.ACTION_RELOAD_SYS);
                sendBroadcast(readIntent);
            }
        });
        myDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        myDialog.show();
    }

    public void doshowRetrun(){
        final AlertDialog.Builder myDialog1 = new AlertDialog.Builder(context);
        myDialog1.setTitle(getResources().getString(R.string.hint));
        myDialog1.setMessage("是否恢复出厂设置？");
        myDialog1.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent readIntent=new Intent(BleConstant.ACTION_RETURN_BACK);
                sendBroadcast(readIntent);
            }
        });
        myDialog1.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        myDialog1.show();
    }

    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(action.equals(BleConstant.ACTION_RELOAD_SYS_SUCCESS)){
                    showTips("重启命令发送成功");
                }else if(action.equals(BleConstant.ACTION_RETURN_BACK_SUCCESS)){
                    showTips("恢复出厂设置命令发送成功");
                }

            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTION_RELOAD_SYS_SUCCESS);
        filter.addAction(BleConstant.ACTION_RETURN_BACK_SUCCESS);
        registerReceiver(receiver, filter);
    }
    
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
            receiver = null;
        }
    }
}
