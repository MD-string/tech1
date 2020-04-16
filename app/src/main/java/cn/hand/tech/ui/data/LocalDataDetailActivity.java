package cn.hand.tech.ui.data;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDKRModel;
import cn.hand.tech.bean.HDSendDataModel;
import cn.hand.tech.bean.WeightDataBean;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.ConsTantsCode;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.data.adapter.ListViewAddAdapter;
import cn.hand.tech.ui.data.bean.LocalDataTimeModel;
import cn.hand.tech.ui.data.presenter.DataPresenter;
import cn.hand.tech.ui.data.presenter.SaveMVVData;
import cn.hand.tech.ui.weight.presenter.SaveWeightDataTask;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.DialogUtil;
import cn.hand.tech.utils.MD5;
import cn.hand.tech.utils.MyPopupWindowUtil;
import cn.hand.tech.utils.SPUtil;
import cn.hand.tech.utils.ToastUtil;

public class LocalDataDetailActivity extends AppCompatActivity implements View.OnClickListener,ListViewAddAdapter.Callback,IDataView {
    private static final String TAG = "LocalDataDetailActivity";
    private ImageView mIvBack;
    private TextView mTvBack, mTvParaTitle;
    private TextView mBtnParaDelete;
    private ListView mLvLocalDetailData;
    private Button mBtnSendLocalEmailDetail, mBtnSaveLocalDetailData, mBtnDataDetailPara, mBtnDataDetailChannel;
    private LinearLayout ll_back;
    private List<WeightDataBean> listData;
    private ProgressDialog dialog;
    private String B_values = "";//"B=[";
    private String K_values = "";//"K=[";
    private HDKRModel kModel;
    private HDKRModel bModel;
    private HDKRModel channelModel;
    private String MD5Source = "";
    private String passwordMD5 = "";
    private final static String USER_NAME = "test";
    private final static String CAR_NUMBER = "AutoWeigh";
    private final static String PASSWORD = "12345678";
    private int mType;
    private ProgressDialog progressDialog;
    Context mcontext;
    private ACache acache;
    private boolean isConnected;
    private DataPresenter mpresenter;
    private ListViewAddAdapter listViewAdapter;
    private boolean isEditor = true;
    private String kv,bv;
    private BroadcastReceiver receiver;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    closeProgressDialog();
                    SPUtil.put(mcontext, "bValue", bv);
                    SPUtil.put(mcontext, "kValue", kv);

                    showKBSetDialog();
                    showTips("计算成功");
                    break;
                case ConsTantsCode.REQUEST_CODE_SUCCESS:
                    if(dialog !=null){
                        dialog.dismiss();
                    }
                    String message = (String) msg.obj;
                    showTips(message);
                    break;
                case ConsTantsCode.REQUEST_CODE_FAIL:
                    dialog.dismiss();
                    break;
                case 8:
                    HDKRModel channelModel1=(HDKRModel)acache.getAsObject("channel_model");
                    if(channelModel1 !=null ){
                        listViewAdapter.setChannelChange(listData,channelModel1);
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=this;
        setContentView(R.layout.activity_local_data_detail);
        acache= ACache.get(mcontext,"WeightFragment");
        mpresenter=new DataPresenter(mcontext,this);
        initView();
        registerBrodcat();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvParaTitle = (TextView) findViewById(R.id.tv_para_title);
        mBtnParaDelete = (TextView) findViewById(R.id.tv_et);
        mLvLocalDetailData = (ListView) findViewById(R.id.lv_localDetail_data);
        mBtnSendLocalEmailDetail = (Button) findViewById(R.id.btn_send_local_emailDetail);
        mBtnDataDetailPara = (Button) findViewById(R.id.btn_data_detail_para);
        mBtnDataDetailChannel = (Button) findViewById(R.id.btn_data_detail_channel);
        mBtnSaveLocalDetailData = (Button) findViewById(R.id.btn_save_localDetail_send);


        ll_back.setOnClickListener(this);
        mBtnParaDelete.setOnClickListener(this);
        mBtnSendLocalEmailDetail.setOnClickListener(this);
        mBtnSaveLocalDetailData.setOnClickListener(this);
        mBtnDataDetailPara.setOnClickListener(this);
        mBtnDataDetailChannel.setOnClickListener(this);
        mBtnParaDelete.setVisibility(View.INVISIBLE);

        listData = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        String localDate = bundle.getString("localDate");
        listData = (List<WeightDataBean>) bundle.getSerializable("weightDataBeanList");
        mTvParaTitle.setTextSize(14f);
        mTvParaTitle.setText("  本地历史数据(" + localDate + ")");

        if(listData ==null){
            listData=new ArrayList<>();
        }

        kModel = new HDKRModel();
        bModel = new HDKRModel();
        channelModel=new HDKRModel();
        channelModel.bChannel1=true; channelModel.bChannel2=true;channelModel.bChannel3=true;
        channelModel.bChannel4=true;channelModel.bChannel5=true;channelModel.bChannel6=true;
        channelModel.bChannel7=true;channelModel.bChannel8=true;channelModel.bChannel9=true;
        channelModel.bChannel10=true;channelModel.bChannel11=true;   channelModel.bChannel12=true;
        channelModel.bChannel13=true;   channelModel.bChannel14=true;channelModel.bChannel15=true;
        channelModel.bChannel16=true;
        passwordMD5 = MD5.getUtf8md5(PASSWORD);
        MD5Source = USER_NAME + ";" + CAR_NUMBER + ";" + passwordMD5.toLowerCase();//字母转小写

        listViewAdapter = new ListViewAddAdapter(mcontext, listData, channelModel, this);
        mLvLocalDetailData.setAdapter(listViewAdapter);
        mLvLocalDetailData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isEditor) {
                    DLog.e(TAG,"点击的位置position==" + position);
                    ListViewAddAdapter.ViewHolder views = (ListViewAddAdapter.ViewHolder) view.getTag();
                    views.cb_weight_item.toggle();
                    CheckBox delCheckBox = views.cb_weight_item;
                    isEditor = false;
                    listViewAdapter.isMapSelected.put(position, delCheckBox.isChecked());
                    listViewAdapter.delContactsIdSet.add(position);
                    listViewAdapter.changeSelected(listData, isEditor);

                }
            }
        });
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mcontext,tip);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            //发送邮件
            case R.id.btn_send_local_emailDetail:
                if (!CommonKitUtil.isNetworkAvailable(LocalDataDetailActivity.this)) {
                    showTips( "网络未连接，请检查您的网络连接情况");
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                Date curDate = new Date(System.currentTimeMillis());
                String time1 = formatter.format(curDate);
                DLog.e(TAG,"发送邮件时的时间==" + time1);
                List<LocalDataTimeModel> listLocalData = new ArrayList<>();
                LocalDataTimeModel localDataTime = new LocalDataTimeModel();
                localDataTime.setLocalDataTime(time1);
                localDataTime.setWeightDataBeanList(listData);
                listLocalData.add(localDataTime);
                SaveMVVData.saveLocalData(LocalDataDetailActivity.this, listLocalData, time1);
//                SaveMVVData.sendXX(LocalDataDetailActivity.this, time1);
                EmailStateAct.start(this,time1);
                break;
            //发送给服务器
            case R.id.btn_save_localDetail_send:
                dialog = new ProgressDialog(this);
                DialogUtil.setProgressDialog(dialog, "保存中...");
//                new Thread() {
//                    public void run() {
                        SaveWeightDataTask.getInstance(LocalDataDetailActivity.this, mHandler).SaveData(listData);
//                    }
//                }.start();
                break;
            //计算系数
            case R.id.btn_data_detail_para:
                //从服务器获取KB值
                dealSendData();
                break;
            /*K值设定*/
            case R.id.tv_k_set:
                showWriteView(0);
                break;
            /*B值设定*/
            case R.id.tv_b_set:
                showWriteView(1);
                break;

            //选择通道
            case R.id.btn_data_detail_channel:
                channelView();
                break;
            /*取消*/
            case R.id.tv_kb_cancel:
                MyPopupWindowUtil.popupWindow.dismiss();
                break;

        }
    }


    /*选择通道*/
    private void channelView() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.channel_item, null);
        final CheckBox c1 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox1);
        final CheckBox c2 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox2);
        final CheckBox c3 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox3);
        final CheckBox c4 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox4);
        final CheckBox c5 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox5);
        final CheckBox c6 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox6);
        final CheckBox c7 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox7);
        final CheckBox c8 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox8);
        final CheckBox c9 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox9);
        final CheckBox c10 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox10);
        final CheckBox c11 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox11);
        final CheckBox c12 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox12);
        final CheckBox c13 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox13);
        final CheckBox c14 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox14);
        final CheckBox c15 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox15);
        final CheckBox c16 = (CheckBox) textEntryView.findViewById(R.id.channel_checkBox16);

        c1.setChecked(channelModel.isbChannel1());
        c2.setChecked(channelModel.isbChannel2());
        c3.setChecked(channelModel.isbChannel3());
        c4.setChecked(channelModel.isbChannel4());
        c5.setChecked(channelModel.isbChannel5());
        c6.setChecked(channelModel.isbChannel6());
        c7.setChecked(channelModel.isbChannel7());
        c8.setChecked(channelModel.isbChannel8());
        c9.setChecked(channelModel.isbChannel9());
        c10.setChecked(channelModel.isbChannel10());
        c12.setChecked(channelModel.isbChannel11());
        c13.setChecked(channelModel.isbChannel12());
        c14.setChecked(channelModel.isbChannel13());
        c15.setChecked(channelModel.isbChannel14());
        c16.setChecked(channelModel.isbChannel15());
        c11.setChecked(channelModel.isbChannel16());
        AlertDialog.Builder ad1 = new AlertDialog.Builder(this);
        ad1.setTitle("选择通道");
        ad1.setView(textEntryView);
        ad1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                channelModel.setbChannel1(c1.isChecked());
                channelModel.setbChannel2(c2.isChecked());
                channelModel.setbChannel3(c3.isChecked());
                channelModel.setbChannel4(c4.isChecked());
                channelModel.setbChannel5(c5.isChecked());
                channelModel.setbChannel6(c6.isChecked());
                channelModel.setbChannel7(c7.isChecked());
                channelModel.setbChannel8(c8.isChecked());
                channelModel.setbChannel9(c9.isChecked());
                channelModel.setbChannel10(c10.isChecked());
                channelModel.setbChannel11(c11.isChecked());
                channelModel.setbChannel12(c12.isChecked());
                channelModel.setbChannel13(c13.isChecked());
                channelModel.setbChannel14(c14.isChecked());
                channelModel.setbChannel15(c15.isChecked());
                channelModel.setbChannel16(c16.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel1", c1.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel2", c2.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel3", c3.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel4", c4.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel5", c5.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel6", c6.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel7", c7.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel8", c8.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel9", c9.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel10", c10.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel11", c11.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel12", c12.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel13", c13.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel14", c14.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel15", c15.isChecked());
                SPUtil.put(LocalDataDetailActivity.this, "bChannel16", c16.isChecked());
                acache.put("channel_model",(Serializable)channelModel);
                mHandler.sendEmptyMessage(8);

            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();
    }

    /*系数设定*/
    private void dealSendData() {
        if (!CommonKitUtil.isNetworkAvailable(this)) {
           showTips( "网络未连接，请检查您的网络连接情况");
            return;
        }

        int nDataCount = listData.size();
        int myChannelCount = getMyChannelCount();

        if (myChannelCount == 0) {
            showTips( "请选择通道数");
            return;
        }
        if (nDataCount < (myChannelCount + 1)) {
            String str = String.format("数据少于%d组", myChannelCount + 1);
            showTips(  str);
            return;
        }
        String post_weights = "";
        String post_mvv1s = "";
        for (int i = 0; i < nDataCount; i++) {
            WeightDataBean bean =listData.get(i);
            post_weights = post_weights + "," + bean.getWeightFromReal();
            if (channelModel.bChannel1) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch1()), Float.parseFloat(bean.getSch1()));
            }
            if (channelModel.bChannel2) {
                post_mvv1s = post_mvv1s + "," +getMvvValue(Float.parseFloat(bean.getAch2()), Float.parseFloat(bean.getSch2()));

            }
            if (channelModel.bChannel3) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch3()), Float.parseFloat(bean.getSch3()));

            }
            if (channelModel.bChannel4) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch4()), Float.parseFloat(bean.getSch4()));

            }
            if (channelModel.bChannel5) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch5()), Float.parseFloat(bean.getSch5()));

            }
            if (channelModel.bChannel6) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch6()), Float.parseFloat(bean.getSch6()));
            }
            if (channelModel.bChannel7) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch7()), Float.parseFloat(bean.getSch7()));
            }
            if (channelModel.bChannel8) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch8()), Float.parseFloat(bean.getSch8()));
            }
            if (channelModel.bChannel9) {
                post_mvv1s = post_mvv1s + "," +getMvvValue(Float.parseFloat(bean.getAch9()), Float.parseFloat(bean.getSch9()));
            }
            if (channelModel.bChannel10) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch10()), Float.parseFloat(bean.getSch10()));
            }
            if (channelModel.bChannel11) {
                post_mvv1s = post_mvv1s + "," +getMvvValue(Float.parseFloat(bean.getAch11()), Float.parseFloat(bean.getSch11()));
            }
            if (channelModel.bChannel12) {
                post_mvv1s = post_mvv1s + "," +getMvvValue(Float.parseFloat(bean.getAch12()), Float.parseFloat(bean.getSch12()));
            }
            if (channelModel.bChannel13) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch13()), Float.parseFloat(bean.getSch13()));
            }
            if (channelModel.bChannel14) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch14()), Float.parseFloat(bean.getSch14()));
            }
            if (channelModel.bChannel15) {
                post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch15()), Float.parseFloat(bean.getSch15()));
            }
            if (channelModel.bChannel16) {
                post_mvv1s = post_mvv1s + "," +getMvvValue(Float.parseFloat(bean.getAch16()), Float.parseFloat(bean.getSch16()));
            }
        }

        String strMV = post_mvv1s.substring(1);
        String postWeight = post_weights.substring(1);

        DLog.e(TAG,"系数设定postWeight==" + postWeight);
        DLog.e(TAG,"系数设定strMV==" + strMV);
        DLog.e(TAG,"系数设定myChannelCount==" + myChannelCount);
        mpresenter.saveData(postWeight,strMV + "",myChannelCount + "");
    }

    private float getMvvValue(float ad, float zero) {
        float x = (ad - zero) / 5;
        DecimalFormat myformat = new DecimalFormat("0.000000");
        String str = myformat.format(x);
        float mvvValue = Float.parseFloat(str);
        return mvvValue;
    }


    private int getMyChannelCount() {
        int i = 0;
        if (channelModel.isbChannel1()) {
            ++i;
        }
        if (channelModel.isbChannel2()) {
            ++i;
        }
        if (channelModel.isbChannel3()) {
            ++i;
        }
        if (channelModel.isbChannel4()) {
            ++i;
        }
        if (channelModel.isbChannel5()) {
            ++i;
        }
        if (channelModel.isbChannel6()) {
            ++i;
        }
        if (channelModel.isbChannel7()) {
            ++i;
        }
        if (channelModel.isbChannel8()) {
            ++i;
        }
        if (channelModel.isbChannel9()) {
            ++i;
        }
        if (channelModel.isbChannel10()) {
            ++i;
        }
        if (channelModel.isbChannel11()) {
            ++i;
        }
        if (channelModel.isbChannel12()) {
            ++i;
        }
        if (channelModel.isbChannel13()) {
            ++i;
        }
        if (channelModel.isbChannel14()) {
            ++i;
        }
        if (channelModel.isbChannel15()) {
            ++i;
        }
        if (channelModel.isbChannel16()) {
            ++i;
        }
        return i;

    }

    /*KB值设定对话框*/
    private void showKBSetDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        View kbDialogView = factory.inflate(R.layout.layout_kb_set_pop, null);
        TextView tv_k_set = (TextView) kbDialogView.findViewById(R.id.tv_k_set);
        TextView tv_b_set = (TextView) kbDialogView.findViewById(R.id.tv_b_set);
        TextView tv_kb_cancel = (TextView) kbDialogView.findViewById(R.id.tv_kb_cancel);
        MyPopupWindowUtil.setPopByWrapContent(this, 0.5f, kbDialogView, Gravity.CENTER);

        tv_k_set.setOnClickListener(this);
        tv_b_set.setOnClickListener(this);
        tv_kb_cancel.setOnClickListener(this);
    }

    /*KB值设定*/
    private void showWriteView(final int nType) {
        mType = nType;
        String  isCon=acache.getAsString("is_connect");
        if("2".equals(isCon)){
            isConnected=true;
        }else{
            isConnected=false;
        }
        if (isConnected) {
            HDSendDataModel model = new HDSendDataModel();
            if (nType == 0) {
                //"K值参数"
                model.mmv1 = kModel.channel1;
                model.mmv2 = kModel.channel2;
                model.mmv3 = kModel.channel3;
                model.mmv4 = kModel.channel4;
                model.mmv5 = kModel.channel5;
                model.mmv6 = kModel.channel6;
                model.mmv7 = kModel.channel7;
                model.mmv8 = kModel.channel8;
                model.mmv9 = kModel.channel9;
                model.mmv10 = kModel.channel10;
                model.mmv11 = kModel.channel11;
                model.mmv12 = kModel.channel12;
                model.mmv13 = kModel.channel13;
                model.mmv14 = kModel.channel14;
                model.mmv15 = kModel.channel15;
                model.mmv16 = kModel.channel16;
            } else {
                //"B值参数"
                model.mmv1 = bModel.channel1;
                model.mmv2 = bModel.channel2;
                model.mmv3 = bModel.channel3;
                model.mmv4 = bModel.channel4;
                model.mmv5 = bModel.channel5;
                model.mmv6 = bModel.channel6;
                model.mmv7 = bModel.channel7;
                model.mmv8 = bModel.channel8;
                model.mmv9 = bModel.channel9;
                model.mmv10 = bModel.channel10;
                model.mmv11 = bModel.channel11;
                model.mmv12 = bModel.channel12;
                model.mmv13 = bModel.channel13;
                model.mmv14 = bModel.channel14;
                model.mmv15 = bModel.channel15;
                model.mmv16 = bModel.channel16;
            }
            Bundle bundle=new Bundle();
            bundle.putSerializable("local_data_detail",model);
            Intent readIntent=new Intent(BleConstant.ACTION_BLE_WRITE_KB);
            readIntent.putExtras(bundle);
            sendBroadcast(readIntent);
            //通过在mainActivity中回调，然后广播发送，本界面广播接收
        } else {
            showTips("蓝牙未连接");
        }
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
                    SPUtil.put(mcontext, "kbType", mType);
                    if (mType == 0) {
                        SPUtil.put(mcontext, "kValue1", kModel.channel1);
                        SPUtil.put(mcontext, "kValue2", kModel.channel2);
                        SPUtil.put(mcontext, "kValue3", kModel.channel3);
                        SPUtil.put(mcontext, "kValue4", kModel.channel4);
                        SPUtil.put(mcontext, "kValue5", kModel.channel5);
                        SPUtil.put(mcontext, "kValue6", kModel.channel6);
                        SPUtil.put(mcontext, "kValue7", kModel.channel7);
                        SPUtil.put(mcontext, "kValue8", kModel.channel8);
                        SPUtil.put(mcontext, "kValue9", kModel.channel9);
                        SPUtil.put(mcontext, "kValue10", kModel.channel10);
                        SPUtil.put(mcontext, "kValue11", kModel.channel11);
                        SPUtil.put(mcontext, "kValue12", kModel.channel12);
                        SPUtil.put(mcontext, "kValue13", kModel.channel13);
                        SPUtil.put(mcontext, "kValue14", kModel.channel14);
                        SPUtil.put(mcontext, "kValue15", kModel.channel15);
                        SPUtil.put(mcontext, "kValue16", kModel.channel16);
                        showTips("K值设置成功");
                    } else {
                        SPUtil.put(mcontext, "bValue1", bModel.channel1);
                        SPUtil.put(mcontext, "bValue2", bModel.channel2);
                        SPUtil.put(mcontext, "bValue3", bModel.channel3);
                        SPUtil.put(mcontext, "bValue4", bModel.channel4);
                        SPUtil.put(mcontext, "bValue5", bModel.channel5);
                        SPUtil.put(mcontext, "bValue6", bModel.channel6);
                        SPUtil.put(mcontext, "bValue7", bModel.channel7);
                        SPUtil.put(mcontext, "bValue8", bModel.channel8);
                        SPUtil.put(mcontext, "bValue9", bModel.channel9);
                        SPUtil.put(mcontext, "bValue10", bModel.channel10);
                        SPUtil.put(mcontext, "bValue11", bModel.channel11);
                        SPUtil.put(mcontext, "bValue12", bModel.channel12);
                        SPUtil.put(mcontext, "bValue13", bModel.channel13);
                        SPUtil.put(mcontext, "bValue14", bModel.channel14);
                        SPUtil.put(mcontext, "bValue15", bModel.channel15);
                        SPUtil.put(mcontext, "bValue16", bModel.channel16);
                        showTips("B值设置成功");
                    }
                    MyPopupWindowUtil.popupWindow.dismiss();

                }

            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTION_SEND_DATA);
        this.registerReceiver(receiver, filter);
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(LocalDataDetailActivity.this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loginError(String msg) {
        closeProgressDialog();
        showTips(msg);
    }

    @Override
    public void loginSuccess(float[] kValueData, String K_values, float[] bValueData, String B_values) {
        new Thread() {
            public void run() {
                SaveWeightDataTask.getInstance(mcontext, mHandler).SaveData(listData); //发送网络
            }
        }.start();
        int tmpSetKRValueCount = 0;
        kv=K_values;
        bv=B_values;
        if (channelModel.bChannel1) {
            kModel.bChannel1 = true;
            bModel.bChannel1 = true;
            kModel.channel1 = kValueData[tmpSetKRValueCount];
            bModel.channel1 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel1 = false;
            bModel.bChannel1 = false;
            kModel.channel1 = 0;
            bModel.channel1 = 0;
        }
        if (channelModel.bChannel2) {
            kModel.bChannel2 = true;
            bModel.bChannel2 = true;
            kModel.channel2 = kValueData[tmpSetKRValueCount];
            bModel.channel2 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel2 = false;
            bModel.bChannel2 = false;
            kModel.channel2 = 0;
            bModel.channel2 = 0;
        }
        if (channelModel.bChannel3) {
            kModel.bChannel3 = true;
            bModel.bChannel3 = true;
            kModel.channel3 = kValueData[tmpSetKRValueCount];
            bModel.channel3 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel3 = false;
            bModel.bChannel3 = false;
            kModel.channel3 = 0;
            bModel.channel3 = 0;
        }
        if (channelModel.bChannel4) {
            kModel.bChannel4 = true;
            bModel.bChannel4 = true;
            kModel.channel4 = kValueData[tmpSetKRValueCount];
            bModel.channel4 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel4 = false;
            bModel.bChannel4 = false;
            kModel.channel4 = 0;
            bModel.channel4 = 0;
        }
        if (channelModel.bChannel5) {
            kModel.bChannel5 = true;
            bModel.bChannel5 = true;
            kModel.channel5 = kValueData[tmpSetKRValueCount];
            bModel.channel5 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel5 = false;
            bModel.bChannel5 = false;
            kModel.channel5 = 0;
            bModel.channel5 = 0;
        }
        if (channelModel.bChannel6) {
            kModel.bChannel6 = true;
            bModel.bChannel6 = true;
            kModel.channel6 = kValueData[tmpSetKRValueCount];
            bModel.channel6 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel6 = false;
            bModel.bChannel6 = false;
            kModel.channel6 = 0;
            bModel.channel6 = 0;
        }
        if (channelModel.bChannel7) {
            kModel.bChannel7 = true;
            bModel.bChannel7 = true;
            kModel.channel7 = kValueData[tmpSetKRValueCount];
            bModel.channel7 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel7 = false;
            bModel.bChannel7 = false;
            kModel.channel7 = 0;
            bModel.channel7 = 0;
        }
        if (channelModel.bChannel8) {
            kModel.bChannel8 = true;
            bModel.bChannel8 = true;
            kModel.channel8 = kValueData[tmpSetKRValueCount];
            bModel.channel8 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel8 = false;
            bModel.bChannel8 = false;
            kModel.channel8 = 0;
            bModel.channel8 = 0;
        }
        if (channelModel.bChannel9) {
            kModel.bChannel9 = true;
            bModel.bChannel9 = true;
            kModel.channel9 = kValueData[tmpSetKRValueCount];
            bModel.channel9 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel9 = false;
            bModel.bChannel9 = false;
            kModel.channel9 = 0;
            bModel.channel9 = 0;
        }
        if (channelModel.bChannel10) {
            kModel.bChannel10 = true;
            bModel.bChannel10 = true;
            kModel.channel10 = kValueData[tmpSetKRValueCount];
            bModel.channel10 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel10 = false;
            bModel.bChannel10 = false;
            kModel.channel10 = 0;
            bModel.channel10 = 0;
        }
        if (channelModel.bChannel11) {
            kModel.bChannel11 = true;
            bModel.bChannel11 = true;
            kModel.channel11 = kValueData[tmpSetKRValueCount];
            bModel.channel11 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel11 = false;
            bModel.bChannel11 = false;
            kModel.channel11 = 0;
            bModel.channel11 = 0;
        }
        if (channelModel.bChannel12) {
            kModel.bChannel12 = true;
            bModel.bChannel12 = true;
            kModel.channel12 = kValueData[tmpSetKRValueCount];
            bModel.channel12 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel12 = false;
            bModel.bChannel12 = false;
            kModel.channel12 = 0;
            bModel.channel12 = 0;
        }
        if (channelModel.bChannel13) {
            kModel.bChannel13 = true;
            bModel.bChannel13 = true;
            kModel.channel13 = kValueData[tmpSetKRValueCount];
            bModel.channel13 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel13 = false;
            bModel.bChannel13 = false;
            kModel.channel13 = 0;
            bModel.channel13 = 0;
        }
        if (channelModel.bChannel14) {
            kModel.bChannel14 = true;
            bModel.bChannel14 = true;
            kModel.channel14 = kValueData[tmpSetKRValueCount];
            bModel.channel14 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel14 = false;
            bModel.bChannel14 = false;
            kModel.channel14 = 0;
            bModel.channel14 = 0;
        }
        if (channelModel.bChannel15) {
            kModel.bChannel15 = true;
            bModel.bChannel15 = true;
            kModel.channel15 = kValueData[tmpSetKRValueCount];
            bModel.channel15 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel15 = false;
            bModel.bChannel15 = false;
            kModel.channel15 = 0;
            bModel.channel15 = 0;
        }
        if (channelModel.bChannel16) {
            kModel.bChannel16 = true;
            bModel.bChannel16 = true;
            kModel.channel16 = kValueData[tmpSetKRValueCount];
            bModel.channel16 = bValueData[tmpSetKRValueCount];
            tmpSetKRValueCount++;
        } else {
            kModel.bChannel16 = false;
            bModel.bChannel16 = false;
            kModel.channel16 = 0;
            bModel.channel16 = 0;
        }

        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void toclick(View view) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }

    }
}
