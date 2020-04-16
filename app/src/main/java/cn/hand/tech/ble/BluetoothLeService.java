package cn.hand.tech.ble;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.hand.tech.bean.HDSendDataModel;
import cn.hand.tech.ble.bean.BleDevice;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.setting.bean.GuJianBean;
import cn.hand.tech.utils.NotNull;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothLeService extends Service {

    private static final String TAG = "BluetoothLeService";
    private BluetoothManager mBluetoothManager;
    public BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private String mBluetoothDeviceAddress;
    protected BluetoothGattCharacteristic mNotifyCharacteristic;

    public boolean mScanning;
    /**
     * 连接状态，默认断开连接状态
     */
    public final static  UUID SER_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public final static  UUID SPP_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;//断开
    public static final int STATE_CONNECTING = 1;//连接中
    public static final int STATE_CONNECTED = 2;//已连接
    public static final int STATE_BLE_READY = 3; //准备

    private boolean isRssiWeek;
    private final IBinder mBinder = new LocalBinder();
    private boolean isConnected;
    private ACache achace;
    private boolean isReconnect=false;
    private int ctime;//重连次数
    private BluetoothDevice currentDev;//当前连接设备
    private BroadcastReceiver receiver;

    public class LocalBinder extends Binder {

        public BluetoothLeService getService() {
            if (!getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_BLUETOOTH_LE)) {
                broadcastUpdate(BleConstant.ACTION_BLE_NONSUPPORT);
            }
            return BluetoothLeService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        closeGatt();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerBrodcat();
    }
    /* --------------------------  蓝牙初始化 —---------------------- */

    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(action.equals(BleConstant.ACTION_BLE_READ_NOTY)){
                    doSendReadPara();
                }else if(action.equals(BleConstant.ACTION_BLE_WRITE_COE)){
                    HDSendDataModel mode=   (HDSendDataModel)intent.getExtras().getSerializable("coefficient_act");
                    doSendWritePara(mode);

                }else if(action.equals(BleConstant.ACTION_BLE_WRITE_KB)){
                    HDSendDataModel mode=   (HDSendDataModel)intent.getExtras().getSerializable("local_data_detail");
                    doSendWritePara(mode);
                }else if(action.equals(BleConstant.ACTION_BLE_START_SEARCH)){
                    startSearchBle();
                }else if(action.equals(BleConstant.ACTION_BLE_STOP_SEARCH)){
                   stopSearchBle();
                }
                else if(action.equals(BleConstant.ACTION_RELOAD_SYS)){ //重启主机
                    doReloadSYS();
                }
                else if(action.equals(BleConstant.ACTION_RETURN_BACK)){ //恢复出厂设置
                    doReturnBack();
                }  else if(action.equals(BleConstant.ACTION_READ_HARD_SOFT)){
                    int id=  intent.getIntExtra("n_id",0);
                    getHardSoft(id);
                }
                else if(action.equals(BleConstant.ACTION_READ_SOFT)){
                    int id2=  intent.getIntExtra("n_id2",0);
                    getsoft(id2);
                }
                else if(action.equals(BleConstant.ACTION_ACUTO_CHECK)){ //自动检测
                    int aid=  intent.getIntExtra("auto_id",0);
                    ReadAutoCheck(aid);
                }
                else if(action.equals(BleConstant.ACTION_START_UPDATE_BIN)){ //准备固件升级
                    GuJianBean mode=   (GuJianBean) achace.getAsObject("start_bin");
                    doStartUpdate(mode);
                }
                else if(action.equals(BleConstant.ACTION_UPDATE_BIN)){ //固件升级
                    double number=   intent.getDoubleExtra("start_bin_1",0.0);
                    GuJianBean mode=   (GuJianBean) achace.getAsObject("start_bin");
                    doUpdateBin(mode,number);
                }
                else if(action.equals(BleConstant.ACTION_BIN_OVER)){ //固件数据包上传完毕 发送完成命令
                    GuJianBean mode=   (GuJianBean) achace.getAsObject("start_bin");
                    doFinishBin(mode);
                }

                else if(action.equals(BleConstant.ACTION_READ_DEVICE_ID)){ //获取设备ID
                    doGetDeviceID();
                }

            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTION_BLE_READ_NOTY);
        filter.addAction(BleConstant.ACTION_BLE_WRITE_COE);
        filter.addAction(BleConstant.ACTION_BLE_WRITE_KB);
        filter.addAction(BleConstant.ACTION_BLE_START_SEARCH);
        filter.addAction(BleConstant.ACTION_BLE_STOP_SEARCH);
        filter.addAction(BleConstant.ACTION_RELOAD_SYS);
        filter.addAction(BleConstant.ACTION_RETURN_BACK);
        filter.addAction(BleConstant.ACTION_READ_HARD_SOFT);
        filter.addAction(BleConstant.ACTION_READ_SOFT);
        filter.addAction(BleConstant.ACTION_ACUTO_CHECK);
        filter.addAction(BleConstant.ACTION_UPDATE_BIN);
        filter.addAction(BleConstant.ACTION_READ_DEVICE_ID);
        filter.addAction(BleConstant.ACTION_START_UPDATE_BIN);
        filter.addAction(BleConstant.ACTION_BIN_OVER);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver !=null){
            unregisterReceiver(receiver);
        }
    }

    /**
     * Bluetooth 初始化
     *
     * @return 返回是否初始化成功
     */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                DLog.e(TAG,"无法初始化BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            DLog.e(TAG,"无法获取BluetoothAdapter.");
            return false;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            // Intent enableBtIntent = new
            // Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // startActivity(enableBtIntent);
            mBluetoothAdapter.enable();
        }
        achace= ACache.get(this,"WeightFragment");//list数据缓存
        return true;
    }


    public BluetoothAdapter getBletoothAdapter() {
        if (mBluetoothAdapter == null) {
            initialize();
        }
        return mBluetoothAdapter;
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 4://连接成功
                    if(currentDev ==null){
                        DLog.e(TAG,"4.连接成功，连接设备为空，异常");
                        return;
                    }
                    isConnected = true;
                    Bundle bun=new Bundle();
                    bun.putParcelable("current_dev",currentDev);
                    Intent cintent=new Intent(BleConstant.ACTION_BLE_CONNECTED);
                    cintent.putExtras(bun);
                    sendBroadcast(cintent);
                    break;
                case 5://连接断开
                    isConnected = false;
                    broadcastUpdate(BleConstant.ACTION_BLE_DISCONNECT);
                    DLog.e(TAG,"5.连接已经断开");
                    break;
                case 6://连接异常  133  122  等异常情况
                    //尝试重连三次
                    isConnected = false;
                    closeGatt();
//                    if(!isReconnect){
//                        if(ctime==2){
//                            isReconnect=true;
//                        }
//                        ctime++;
//                        mHandler.postDelayed(new Runnable() {//重连
//                            @Override
//                            public void run() {
//                                connect(mBluetoothDeviceAddress);
//                            }
//                        },1000);
//                        mConnectionState=STATE_CONNECTING;
//                        DLog.e(TAG,"连接异常，进行重连第"+ctime+"次");
//                    }else{
                        mConnectionState=STATE_DISCONNECTED;
                        broadcastUpdate(BleConstant.ACTION_BLE_CONNECTION_EXCEPTION);
                        DLog.e(TAG,"立刻又返回133等其他状态时不再去重连");
//                    }
                    break;
                case 7:
                    isConnected = false;
                    broadcastUpdate(BleConstant.ACTION_BLE_DISCONNECT);
                    DLog.e(TAG,"7.连接已经断开");
                    break;
                case 8:
                    isConnected = false;
                    broadcastUpdate(BleConstant.ACTION_BLE_DISCONNECT);
                    DLog.e(TAG,"8.连接已经断开");
                    break;
                case 9://可能连接成功 但服务未获取到
                    DLog.e(TAG,"蓝牙设备服务未获取到断开连接");
                    break;
                case 10://信号强弱
                    Intent reIntent=new Intent(BleConstant.ACTION_BLE_CONNECTION_RSSI);
                    reIntent.putExtra("bel_rssi",msg.arg1);
                    sendBroadcast(reIntent);
                    DLog.e(TAG,"蓝牙信号("+  msg.arg1 + ")—弱，请靠近设备！");
                    break;
                default:
                    break;
            }
        }
    };
    /* -----------------------扫描设备---------------------------*/


    /**
     * 扫描设备
     *
     * @param enable
     */
    public void scanLeDev(final boolean enable){
        ctime=0;
        isReconnect=false;
        new Thread("scan_device") {
            public void run() {
                try {
                    if (enable) {

                        if(mScanning){
                            mScanning = false;
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        }
                        //                        mHandler.postDelayed(new Runnable() {
                        //                            @Override
                        //                            public void run() {
                        //                                mScanning = false;
                        //                                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        //                            }
                        //                        }, SCAN_PERIOD);

                        sleep(400);
                        mScanning = true;
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                    } else {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

    }
    /**
     * 扫描的回调
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {

            String name = device.getName();
            String address = device.getAddress();
//            DLog.d( TAG,"//蓝牙地址==>"+device.getAddress() + "//名称==>" + name);
            if (name != null) {
                name = name.trim();
            }
            if (TextUtils.isEmpty(name)) {
                return;
            }
            BleDevice bleDevice = new BleDevice();
            bleDevice.setMacAddress(address);
            bleDevice.setRssi(rssi);
            bleDevice.setRealName(name);
//            String addr=achace.getAsString("address_1");

            if(
                    name.contains("HD")
                            || name.contains("LanQian")
                 //        && !address.equalsIgnoreCase(addr)

                    ){


                Intent intent = new Intent(BleConstant.ACTION_BLE_DEVICE_FOUND);
                Bundle b = new Bundle();
                b.putSerializable(BleConstant.EXTRA_DEVICE,bleDevice);
                intent.putExtras(b);
                sendBroadcast(intent);
                achace.put("address_1",device.getAddress());
            }

        }
    };

    /* -----------------------连接蓝牙  / 断开蓝牙---------------------------*/


    /**
     * 连接蓝牙
     *
     * @param address
     * @return
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            DLog.e(TAG,"蓝牙未初始化或未指定地址.");
            return false;
        }
        if(mScanning){
            scanLeDev(false);
        }

        if (mBluetoothGatt != null) {
            mNotifyCharacteristic = null;
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            DLog.e(TAG,"设备未找到,不能连接.");
            return false;
        }

        mConnectionState=STATE_BLE_READY;  //准备状态
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        DLog.e(TAG,"试图创建新的连接.");
        mBluetoothDeviceAddress = address;
        currentDev=device;
        if (mConnectionState == STATE_BLE_READY) {
            return true;
        }
        return false;
    }

    /**
     * 断开连接
     */
    public void disConnect() {
        try{
            if (mBluetoothAdapter == null || mBluetoothGatt == null) {
                DLog.e(TAG,"BluetoothAdapter或mBluetoothGatt未初始化");

                mConnectionState=STATE_DISCONNECTED;
                DLog.e(TAG,"蓝牙已断开Disconnected");
                Message message = new Message();
                message.what = 5;
                mHandler.sendMessage(message);

                return;
            }
            mBluetoothGatt.disconnect();
            achace.put("address_1","");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //蓝牙状态返回mHandler
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            String StateChange = "StateChange,status=" + status + ",newState==" + newState;
            DLog.e(TAG,"蓝牙状态变化："+StateChange);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    if (gatt != null && mConnectionState == STATE_BLE_READY ) { //
                        mConnectionState=STATE_CONNECTED;

                        Message message = new Message();//表示蓝牙连接成功
                        message.what = 4;
                        mHandler.sendMessageDelayed(message,100);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gatt.discoverServices();
                            }
                        },1000);
                        DLog.e(TAG,"蓝牙连接成功，去发现连接GATTServer");
                    }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    mConnectionState=STATE_DISCONNECTED;
                    DLog.e(TAG,"蓝牙已断开Disconnected");
                    Message message = new Message();
                    message.what = 5;
                    mHandler.sendMessage(message);
                }
            } else if (status == 133) {
                DLog.e(TAG,"出现status" + status);
                Message message = new Message();
                message.what = 6;
                mHandler.sendMessage(message);
            } else if (status == 8) {
                mConnectionState=STATE_DISCONNECTED;
                /*某些原因自动断开，距离远断开*/
                DLog.e(TAG,"自动断开了status=" + status);
                Message message = new Message();
                message.what = 7;
                mHandler.sendMessage(message);
            } else if (status == 40) {
                mConnectionState=STATE_DISCONNECTED;
                //是信号弱或者距离远
                DLog.e(TAG,"距离远了" + status);
                Message message = new Message();
                message.what = 8;
                mHandler.sendMessage(message);
            } else {
                //一般主动点击连接 会出现status=22或者status=129或者status=62或者status=
                //且不会回调
                DLog.e(TAG,"是其他状态status" + status);
                Message message = new Message();
                message.what = 6;
                mHandler.sendMessage(message);
            }
        }

        //当设备是否找到服务时，会回调该函数
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            DLog.e(TAG,"Discovered，status==" + status + ",Services()==" + gatt.getServices());
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //激活要开启的服务,连接到服务才会走这里
                DLog.e(TAG,"Discovered发现服务成功");
                displayGattServices(gatt.getServices(), gatt);
            } else {
                DLog.e(TAG,"服务发现状态失败status==" + status);
                //                disconnect();
                try {
                    Thread.sleep(1000); //
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                gatt.discoverServices();

            }
        }


        //当读取设备时会回调该函数
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                DLog.e(TAG,"读取ReadCharacteristic");
            }
        }

        //当向设备Descriptor中写数据时，会回调该函数
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //点击清零会走这里
            DLog.e(TAG,"向设备写status=" + status + ", characteristic =" + characteristic.getUuid().toString());
        }

        //设备发出通知时会调用到该接口
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            /*gatt.readRemoteRssi();//在这里读取信号强度会一秒钟返回20条信号，*/
            //这里是不断去走的
            //  LogUtil.e("属性状态改变通知CharacteristicChanged=="+characteristic);
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                for (byte byteChar : data) {
                    HandlerBleData.addToBuffer((byteChar & 0xff)); //接收数据
                }

            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    HandlerBleData.analyzeData(); //解析数据
                }
            },300);

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, final int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            DLog.e(TAG,"连接后的设备==" + gatt.getDevice().getAddress() + ",信号强度==" + rssi);
            if (rssi < -86) {
                isRssiWeek = true;
            } else {
                isRssiWeek = false;

                return;
            }
            if (mHandler != null) {
                if(isRssiWeek){
                    Message msg=new Message();
                    msg.what=10;
                    msg.arg1=rssi;
                    mHandler.sendMessageDelayed(msg,2000);
                }
            }
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            DLog.e("BLE","onMtuChanged "+mtu+" status="+status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            //Success 0  ; WriteNotPermitted 3 ;  RequestNotSupported 6  ;ReadNotPermitted 2
            DLog.e(TAG, "onDescriptorWrite="+status);
        }
    };


    //打开服务
    private void displayGattServices(List<BluetoothGattService> gattServices, BluetoothGatt gatt) {
        if (!NotNull.isNotNull(gattServices)) {
            DLog.e(TAG, "未获取到蓝牙服务");
            Message message = new Message();
            message.what = 9;
            mHandler.sendMessage(message);
            return;
        }
        if (gatt != null) {
            BluetoothGattService gattService =gatt.getService(SER_UUID);//直接获取服务 省略搜索麻烦
            BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(SPP_UUID);
            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(BleConstant.CONFIG_DESCRIPTOR_UUID);//蓝牙模块会收到开启通知信息
            if(descriptor !=null){
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                boolean iswrite=gatt.writeDescriptor(descriptor);
                DLog.e(TAG, "displayGattServices="+iswrite);
            }
            final int charaProp = gattCharacteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = gattCharacteristic;
                mNotifyCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                gatt.setCharacteristicNotification(gattCharacteristic, true);
            }

//            boolean isMtu=setMTU(53);
        }

    }


    //设置 写入 字节数

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean setMTU(int mtu){
        DLog.e("BLE","setMTU "+mtu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(mtu>20){
                boolean ret =   mBluetoothGatt.requestMtu(mtu);
                DLog.e("BLE","requestMTU "+mtu+" ret="+ret);
                return ret;
            }
        }

        return false;
    }
    /*---------------------------------------  写入数据 ----------------------------*/
    //开始搜索蓝牙
    public void startSearchBle(){
        if (mBluetoothAdapter != null) {
            closeGatt();
            scanLeDev(true);
        }
    }
    //停止搜索蓝牙
    public void stopSearchBle(){
        if (mBluetoothAdapter != null) {
            scanLeDev(false);
        }
    }
    //清零指令
    public void clearZero(){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.SendClearZero(mNotifyCharacteristic, mBluetoothGatt);
        }
    }

    //发送重量
    public void sendWeight(double weight){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.SendWeightConfiguration(mNotifyCharacteristic, mBluetoothGatt, weight);
        }
    }
    //发送硬件版本
    public void getHardSoft(int  id){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.ReadHardSotf(mNotifyCharacteristic, mBluetoothGatt, id);
        }
    }
    //自动检测
    public void ReadAutoCheck(int  id){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.SendAutoCheck(mNotifyCharacteristic, mBluetoothGatt,id);
        }
    }

    //发送软件版本
    public void getsoft(int  id){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.ReadVerCode(mNotifyCharacteristic, mBluetoothGatt, id);
        }
    }
    //写数据
    public void doSendWritePara(HDSendDataModel para){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.SendWritePara(mNotifyCharacteristic, mBluetoothGatt, para);
        }
    }
    //写固件更新
    public void doStartUpdate(GuJianBean mode) {
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.SendStartUpdate(mNotifyCharacteristic, mBluetoothGatt,mode);
        }
    }
            //写固件更新
    public void doUpdateBin(GuJianBean mode,double number){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.SendupdateBin(mNotifyCharacteristic, mBluetoothGatt, mode,number);
        }
    }

    //完成固件更新
    public void doFinishBin(GuJianBean mode){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.sendFinishBin(mNotifyCharacteristic, mBluetoothGatt, mode);
        }
    }
    //读取设备ID
    public void doGetDeviceID(){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.readDeviceID(mNotifyCharacteristic, mBluetoothGatt);
        }
    }

    //发送读取命令
    public void doSendReadPara(){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            HDBLESend.SendReadPara(mNotifyCharacteristic, mBluetoothGatt);
        }
    }
    //重启主机
    public void doReloadSYS(){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            boolean isReloadSuccess=HDBLESend.ReloadSYS(mNotifyCharacteristic, mBluetoothGatt);
            if(isReloadSuccess){
                Intent readIntent=new Intent(BleConstant.ACTION_RELOAD_SYS_SUCCESS);
                sendBroadcast(readIntent);
            }
        }
    }
    //恢复出厂设置
    public void doReturnBack(){
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            boolean isReturnSuccess=HDBLESend.doRenturnBack(mNotifyCharacteristic, mBluetoothGatt);
            if(isReturnSuccess){
                Intent readIntent=new Intent(BleConstant.ACTION_RETURN_BACK_SUCCESS);
                sendBroadcast(readIntent);
            }
        }
    }

    //每次写入20个字节 把数据分解成16个字节一个部分一个部分
    private List<byte[]> splitSendData(byte[] data) {
        if(data == null) return null;
        List<byte[]> datas = new ArrayList<byte[]>();
        if(data.length > 16){
            int num = data.length % 16== 0 ? data.length / 16 : data.length / 16 + 1;
            for(int i = 0; i < num; i ++) {
                if(data.length % 16 == 0){
                    byte[] d = new byte[16];
                    for(int j = 0; j < 16 && i * 16 + j < data.length ; j ++) {
                        d[j] = data[i * 16 + j];
                    }
                    datas.add(d);
                } else {
                    byte[] d;
                    if(i == num - 1) {
                        d = new byte[data.length - i * 16];
                    } else {
                        d = new byte[16];
                    }
                    for(int j = 0; j < 16 && i * 16 + j < data.length ; j ++) {
                        d[j] = data[i * 16 + j];
                    }
                    datas.add(d);
                }
            }
        } else {
            datas.add(data);
        }
        return datas;
    }


    /*-------------------------------- 关闭所有 ---------------------*/

    public  void closeGatt() {
        if (mBluetoothGatt != null) {
            mHandler.removeCallbacksAndMessages(null);
            if (mBluetoothAdapter.isEnabled()) {
                try {
                    refreshCache(mBluetoothGatt);
                } catch (DeadObjectException e) {
                    e.printStackTrace();
                }
                if(mBluetoothGatt !=null){
                    mBluetoothGatt.close();
                }
                mBluetoothGatt = null;
                achace.put("address_1","");
            }
        }
    }

    private void refreshCache(BluetoothGatt gatt) throws DeadObjectException {

        try {
            BluetoothGatt localBluetoothGatt = gatt;
            Method localMethod = localBluetoothGatt.getClass().getMethod("refresh");
            if (localMethod == null) {
                return;
            }
            localMethod.invoke(localBluetoothGatt);
            DLog.e(TAG,"刷新蓝牙设备缓存成功");
        } catch (Exception localException) {
            DLog.e(TAG,"当刷新设备时Exception==" + localException.getMessage().toString());
        }
    }


    /**
     * 获取当前连接状态
     *
     * @return
     */
    public int getCurrentConnState() {
        return mConnectionState;
    }

    /**
     * 更新广播
     *
     * @param action
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }



}
