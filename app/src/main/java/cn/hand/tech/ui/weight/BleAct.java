package cn.hand.tech.ui.weight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import cn.hand.tech.R;
import cn.hand.tech.ble.bean.BleDevice;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.weight.adapter.mBleDeviceAdapter;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.ToastUtil;

//蓝牙搜索 显示页面
public class BleAct extends Activity implements View.OnClickListener{

    private Context context;
    private ProgressBar roudProgressBar;
    private TextView title_ble;
    private ListView ble_list;
    private List<BleDevice> listDevices;
    private mBleDeviceAdapter bleDeviceAdapter;
    private BroadcastReceiver receiver;
    private ACache acache;
    private boolean isScan;
    private int mPosition;
    private LinearLayout ll_back;

    public static void startBleAct(Context context){
        Intent i=new Intent(context,BleAct.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_ble_connect);
        acache= ACache.get(context,CommonUtils.TAG);
        List<BleDevice> emlist=new ArrayList<>();
        acache.put("dev_list",(Serializable)emlist);
        acache.put("click_once","false");
        initView();
        registerBrodcat();
    }

    private void initView() {
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        roudProgressBar=(ProgressBar)findViewById(R.id.roudProgressBar);
        roudProgressBar.setVisibility(View.GONE);
        title_ble=(TextView)findViewById(R.id.title_ble);
        title_ble.setOnClickListener(this);

        ble_list=(ListView)findViewById(R.id.ble_list);
        listDevices=new ArrayList<>();
        bleDeviceAdapter=new mBleDeviceAdapter(context,listDevices);
        ble_list.setAdapter(bleDeviceAdapter);
        ble_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                acache.put("click_once","true");
                finish();
                mPosition=position;
                BleDevice bledev=	listDevices.get(position);
                String address =bledev.getMacAddress();
                Intent intent = new Intent(BleConstant.ACTION_BLE_CONNECT);
                intent.putExtra("Ble_Device",address);
                sendBroadcast(intent);
            }
        });

        isScan=true;
        sendBrodcast(BleConstant.ACTION_BLE_START_SEARCH);
        roudProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_ble:
                if(isScan){
                    sendBrodcast(BleConstant.ACTION_BLE_STOP_SEARCH);
                    title_ble.setText("搜索");
                    listDevices.clear();
                    isScan=false;
                    roudProgressBar.setVisibility(View.GONE);
                }else{
                    sendBrodcast(BleConstant.ACTION_BLE_START_SEARCH);
                    title_ble.setText("停止");
                    List<BleDevice> emlist=new ArrayList<>();
                    acache.put("dev_list",(Serializable)emlist);
                    bleDeviceAdapter.setlist(emlist);
                    isScan=true;
                    roudProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }

    }


    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }

    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(action.equals(BleConstant.ACTION_BLE_NONSUPPORT)){
                    showTips("设备不支持蓝牙功能");
                }else if(action.equals(BleConstant.ACTION_BLE_DEVICE_FOUND)){
                    /**
                     * 发现设备
                     */
                    final BleDevice bleDevice = (BleDevice) intent.getSerializableExtra(BleConstant.EXTRA_DEVICE);
                    //                    DLog.e(TAG,"发现设备"+"/MacAddress==>"+bleDevice.getMacAddress());

                    boolean isSame=false;
                    List<BleDevice> list=( List<BleDevice>)acache.getAsObject("dev_list");
                    if(list !=null &&list.size() >0){
                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getMacAddress().equalsIgnoreCase(bleDevice.getMacAddress())){
                                isSame=true;
                                list.remove(i);
                                list.add(i,bleDevice);
                                acache.put("dev_list",(Serializable)list);
                                handler.sendEmptyMessageDelayed(1,400);
                            }
                        }
                        if(!isSame){
                            list.add(bleDevice);
                            acache.put("dev_list",(Serializable)list);
                            handler.sendEmptyMessageDelayed(1,400);
                        }

                    }else{
                        if(list==null ){
                            list=new ArrayList<>();
                        }
                        list.add(bleDevice);
                        acache.put("dev_list",(Serializable)list);
                        handler.sendEmptyMessageDelayed(1,400);
                    }

                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTION_BLE_NONSUPPORT);
        filter.addAction(BleConstant.ACTION_BLE_DEVICE_FOUND);
        registerReceiver(receiver, filter);
    }

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (isFinishing()) {
                return;
            }
            switch (msg.what) {
                case 1://搜索到设备
                    List<BleDevice>list=( List<BleDevice>)acache.getAsObject("dev_list");
                    listDevices=list;
                    if(bleDeviceAdapter !=null ){
                        bleDeviceAdapter.updateListView(list);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public void  sendBrodcast(String str){
        Intent broIntent=new Intent(str);
        sendBroadcast(broIntent);
    }
}
