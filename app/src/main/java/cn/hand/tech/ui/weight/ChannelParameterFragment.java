package cn.hand.tech.ui.weight;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDKRModel;
import cn.hand.tech.bean.HDModelData;
import cn.hand.tech.ble.HandlerBleData;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.home.BaseFragment;
import cn.hand.tech.ui.setting.PassSettingActNew;
import cn.hand.tech.ui.weight.adapter.ChannelDetectAdapter;
import cn.hand.tech.ui.weight.adapter.ChannelparaAdapter;
import cn.hand.tech.ui.weight.bean.CarInfo;
import cn.hand.tech.ui.weight.bean.ChannealBean;
import cn.hand.tech.ui.weight.bean.ChannelDetectModel;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.ui.weight.presenter.ChannelListPresenter;
import cn.hand.tech.utils.Aes;
import cn.hand.tech.utils.MyPopupWindowUtil;
import cn.hand.tech.utils.NotNull;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.MyCustomDialog;
import cn.hand.tech.weiget.MyGridView;

/**
 *  通道参数
 * @author hxz
 */

public class ChannelParameterFragment extends BaseFragment implements IChannelView {

    private BroadcastReceiver receiver;
    private RelativeLayout rl_btn;
    private MyGridView grd_view;
    Context context;
    private float adDifferenceValue = 3f;
    private List<ChannealBean> list;
    private ChannelparaAdapter madapter;
    //	private String strpass="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16";//选取的通道
    private Vibrator vibrator;
    private List<ChannelDetectModel> channelDetectList;
    private ProgressDialog progressDialogDetectChannel;
    private TextView tv_value;
    private ChannelDetectAdapter channelDetectAdapter;
    private static final String TAG = "WeightFragment";
    private ACache acache;
    private boolean isConnected;
    private HDModelData data;
    private boolean bChannelDetect, mvCount;
    public int DETECT_MAX;
    private int detectCount = 0;
    private HDKRModel channelModel;
    private RelativeLayout rl_mv;
    private int chooseId = 0;//0表示未选 1.表示通道检测 2表示mv/v检测
    private ArrayList<Float> list0;
    private ArrayList<Float> list1;
    private ArrayList<Float> list2;
    private ArrayList<Float> list3;
    private ArrayList<Float> list4;
    private ArrayList<Float> list5;
    private ArrayList<Float> list6;
    private ArrayList<Float> list7;
    private ArrayList<Float> list8;
    private ArrayList<Float> list9;
    private ArrayList<Float> list10;
    private ArrayList<Float> list11;
    private ArrayList<Float> list12;
    private ArrayList<Float> list13;
    private ArrayList<Float> list14;
    private ArrayList<Float> list15;
    private AlertDialog tempDialog,btempDialog;
    private AlertDialog adddialog;
    private AlertDialog onlienAdddialog;
    private ChannelListPresenter mpresenter;
    private static final String PASSWORD_STRING = "6a9fd0d1a950420b812f27e970afcd8f"; // static final 静态常量
    private String mdevId;
    private String truckNum;
    private List<String> strList;//是否显示录车显示框
    private MyCustomDialog cheDialog;
    private String psw;
    private String isPassCheck="";
    private AlertDialog chtempDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        final View view = inflater.inflate(R.layout.frg_channel_para, container, false);
        acache = ACache.get(context, TAG);
        EventBus.getDefault().register(this);
        String date = acache.getAsString("mv_date");
        if (Tools.isEmpty(date)) {
            date = "5";
        }
        mpresenter = new ChannelListPresenter(context, this);
        DETECT_MAX = Integer.parseInt(date);
        initData();
        initChannel();
        initFloat();
        initView(view);
        registerBrodcat();
        vibrator = (Vibrator) (context.getSystemService(Context.VIBRATOR_SERVICE));//振动
        return view;
    }

    private void initFloat() {
        list0 = new ArrayList<Float>();
        list1 = new ArrayList<Float>();
        list2 = new ArrayList<Float>();
        list3 = new ArrayList<Float>();
        list4 = new ArrayList<Float>();
        list5 = new ArrayList<Float>();
        list6 = new ArrayList<Float>();
        list7 = new ArrayList<Float>();
        list8 = new ArrayList<Float>();
        list9 = new ArrayList<Float>();
        list10 = new ArrayList<Float>();
        list11 = new ArrayList<Float>();
        list12 = new ArrayList<Float>();
        list13 = new ArrayList<Float>();
        list14 = new ArrayList<Float>();
        list15 = new ArrayList<Float>();
    }


    private void initView(View view) {
        grd_view = (MyGridView) view.findViewById(R.id.grd_view);

        List<ChannealBean> mlist = doHandlerList(list);
        madapter = new ChannelparaAdapter(context, mlist);
        grd_view.setAdapter(madapter);

        rl_btn = (RelativeLayout) view.findViewById(R.id.rl_btn);//通道检测
        rl_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String isCon = acache.getAsString("is_connect");
                if ("2".equals(isCon)) {
                    isConnected = true;
                } else {
                    isConnected = false;
                }

                //                String date = acache.getAsString("mv_date");
                //                if (Tools.isEmpty(date)) {
                //                    date = "5";
                //                }
                //                DETECT_MAX = Integer.parseInt(date);

                if (isConnected) {
                    //                    String value = acache.getAsString("adDifferenceValue");
                    //                    if (Tools.isEmpty(value)) {
                    //                        value = "3";
                    //                    }
                    //                    adDifferenceValue = Float.valueOf(value);

                    List<String> strlist=doJugdeChanneal();
                    if(strlist !=null && strlist.size() >0){
                        chooseId = 1;
                        initFloat();
                        strList = new ArrayList<>();
                        doCheck("通道检测中...", chooseId, "通道");
                    }else{
                        showChooseChanneal(context,"通道为空，请先选择通道","通道选择");
                    }

                } else {
                    showTips("蓝牙未连接");
                }
            }
        });

        rl_mv = (RelativeLayout) view.findViewById(R.id.rl_mv);//mv/V检测
        rl_mv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String isCon = acache.getAsString("is_connect");
                if ("2".equals(isCon)) {
                    isConnected = true;
                } else {
                    isConnected = false;
                }

                String date = acache.getAsString("mv_date");
                if (Tools.isEmpty(date)) {
                    date = "5";
                }
                DETECT_MAX = Integer.parseInt(date);

                if (isConnected) {
                    String value = acache.getAsString("adDifferenceValue");
                    if (Tools.isEmpty(value)) {
                        value = "3";
                    }
                    adDifferenceValue = Float.valueOf(value);
                    chooseId = 2;
                    initFloat();
                    strList = new ArrayList<>();
                    doCheck("MV/V检测中...", 2, "MV");
                } else {
                    showTips("蓝牙未连接");
                }
            }
        });

    }

    private void initChannel() {
        channelModel = (HDKRModel) acache.getAsObject("channel_model");//保存多少通道
        if (channelModel == null) {
            channelModel = new HDKRModel();
            channelModel.bChannel1 = false;
            channelModel.bChannel2 = false;
            channelModel.bChannel3 = false;
            channelModel.bChannel4 = false;
            channelModel.bChannel5 = false;
            channelModel.bChannel6 = false;
            channelModel.bChannel7 = false;
            channelModel.bChannel8 = false;
            channelModel.bChannel9 = false;
            channelModel.bChannel10 = false;
            channelModel.bChannel11 = false;
            channelModel.bChannel12 = false;
            channelModel.bChannel13 = false;
            channelModel.bChannel14 = false;
            channelModel.bChannel15 = false;
            channelModel.bChannel16 = false;
            acache.put("channel_model", (Serializable) channelModel);
        }

    }

    private void doCheck(String title, final int blen, String name) {
        vibrator.vibrate(200);//震动指定时间
        if (2 == blen) {
            mvCount = true;
            bChannelDetect = false;
        } else if (1 == blen) {
            mvCount = false;
            bChannelDetect = true;
        }
        detectCount = 0;
        channelDetectList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            ChannelDetectModel detectModel = new ChannelDetectModel();
            detectModel.setChannelName(name + (i + 1));
            channelDetectList.add(detectModel);
        }
        showDetectChannelProgressDialog(title);
        //        final View viewChannelDetect = LayoutInflater.from(context).inflate(R.layout.pop_channel_detect, null);
        //        final GridView gridView = (GridView) viewChannelDetect.findViewById(R.id.gv_chanel_detect);
        //        ImageView iv_decrease_value = (ImageView) viewChannelDetect.findViewById(R.id.iv_decrease_value);
        //        ImageView iv_add_value = (ImageView) viewChannelDetect.findViewById(R.id.iv_add_value);
        //                    tv_value = (TextView) viewChannelDetect.findViewById(R.id.tv_value);
        //                    tv_value.setText(adDifferenceValue + "");
        //                    iv_decrease_value.setOnClickListener(new OnClickListener() { /*增加阈值*/
        //                        @Override
        //                        public void onClick(View v) {
        //                            int valueDecrease = Integer.parseInt(tv_value.getText().toString().trim());
        //                            adDifferenceValue = valueDecrease - 1;
        //                            tv_value.setText(adDifferenceValue + "");
        //                        }
        //                    });
        //                    iv_add_value.setOnClickListener(new OnClickListener() { /*减少阈值*/
        //                        @Override
        //                        public void onClick(View v) {
        //                            int valueAdd = Integer.parseInt(tv_value.getText().toString().trim());
        //                            adDifferenceValue = valueAdd + 1;
        //                            tv_value.setText(adDifferenceValue + "");
        //                        }
        //                    });

        //        mhandler.postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                closeDetectChannelProgressDialog();
        //                MyPopupWindowUtil.setPopWidthMatchParent(getActivity(), 0.4f, viewChannelDetect, Gravity.CENTER);
        //                channelDetectAdapter = new ChannelDetectAdapter(getActivity(), channelDetectList);
        //                gridView.setAdapter(channelDetectAdapter);
        //                MyPopupWindowUtil.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
        //                    @Override
        //                    public void onDismiss() {
        //                        MyPopupWindowUtil.popupWindow.dismiss();
        //                        MyPopupWindowUtil.setAlpha(getActivity(), 1.0f);
        //                        if(2==blen){
        //                            mvCount=false;
        //                        }else if(1==blen){
        //                            bChannelDetect = false;
        //                        }
        //                        channelDetectList.clear();
        //                        /*把最后一次的通道阈值保存到本地*/
        //                        //                        SPUtil.put(context, "adDifferenceValue", adDifferenceValue);
        //                    }
        //                });
        //
        //            }
        //        }, DETECT_MAX*1000);
    }

    @SuppressLint("HandlerLeak")
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (getActivity() == null || getActivity().isFinishing()) {
                return;
            }
            switch (msg.what) {
                case 1:
                    hanlerData(data);
                    if (list != null && list.size() >= 0) {
                        List<ChannealBean> slist = doHandlerList(list);
                        madapter.setList(slist);
                        //                        DLog.e(TAG, "setp5:ChannelParameterFragmen展示list。");
                    }
                    /*判断通道检测*/
                    if (bChannelDetect) {
                        invokeAD(data);
                    }

                    /*判断MV/V检测*/
                    if (mvCount) {
                        invokeMV(data);
                    }

                    break;
                case 2:
                    int aa=msg.arg1;
                    List<ChannealBean> havelist= doHandlerList(list);//保存的通道  显示 多个通道。
                    List<ChannelDetectModel> newlist=  doHandlerData(havelist,channelDetectList);
                    final View viewChannelDetect = LayoutInflater.from(context).inflate(R.layout.pop_channel_detect, null);
                    final GridView gridView = (GridView) viewChannelDetect.findViewById(R.id.gv_chanel_detect);
                    ImageView iv_decrease_value = (ImageView) viewChannelDetect.findViewById(R.id.iv_decrease_value);
                    ImageView iv_add_value = (ImageView) viewChannelDetect.findViewById(R.id.iv_add_value);
                    TextView tv_cancel=(TextView)viewChannelDetect.findViewById(R.id.tv_cancel);
                    tv_cancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mvCount = false;
                            bChannelDetect = false;
                            MyPopupWindowUtil.popupWindow.dismiss();
                            MyPopupWindowUtil.setAlpha(getActivity(), 1.0f);
                            channelDetectList.clear();
                        }
                    });
                    TextView tv_ok=(TextView)viewChannelDetect.findViewById(R.id.tv_ok);

                    if(aa >0){
                        tv_ok.setVisibility(View.GONE);
                    }else{
                        tv_ok.setVisibility(View.VISIBLE);
                    }
                    tv_ok.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mvCount = false;
                            bChannelDetect = false;
                            MyPopupWindowUtil.popupWindow.dismiss();
                            MyPopupWindowUtil.setAlpha(getActivity(), 1.0f);
                            channelDetectList.clear();
                            /*把最后一次的通道阈值保存到本地*/
                            //                        SPUtil.put(context, "adDifferenceValue", adDifferenceValue);

                            mhandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(1==chooseId) {
                                        int size = strList.size();
                                        StringBuffer str = new StringBuffer();
                                        //                                        for (int i = 0; i < size; i++) {
                                        //                                            String index = strList.get(i);
                                        //                                            if ("0".equals(index)) {
                                        //                                                str.append("0");
                                        //                                            }
                                        //
                                        //                                        }
                                        //                                        int len = str.length();
                                        //                                        if (size == len) {  //说明里面都是 0 //所选通道全部通过检测
                                        //                                            isPassCheck="0";
                                        //                                        } else {             //有通道未通过检测 只能录入基本信息 不能录安装人员信息
                                        //                                            isPassCheck="1";
                                        //                                        }

                                        for (int i = 0; i < size; i++) {
                                            String index = strList.get(i);
                                            str.append(index);
                                        }
                                        String passStr=str.toString();
                                        if (passStr.contains("0")) {  //说明里面  只要有一个通道通过  0 //
                                            isPassCheck="0";
                                        } else {             //全部未通过
                                            isPassCheck="1";
                                        }

                                        doAddTruck();
                                    }

                                }
                            }, 300);
                        }
                    });
                    closeDetectChannelProgressDialog();
                    MyPopupWindowUtil.setPopWidthMatchParent(getActivity(), 0.4f, viewChannelDetect, Gravity.CENTER);
                    channelDetectAdapter = new ChannelDetectAdapter(getActivity(),newlist);
                    gridView.setAdapter(channelDetectAdapter);
                    //
                    //                    MyPopupWindowUtil.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    //                        @Override
                    //                        public void onDismiss() {
                    //                            mvCount = false;
                    //                            bChannelDetect = false;
                    //                            MyPopupWindowUtil.popupWindow.dismiss();
                    //                            MyPopupWindowUtil.setAlpha(getActivity(), 1.0f);
                    //                            channelDetectList.clear();
                    //                            /*把最后一次的通道阈值保存到本地*/
                    //                            //                        SPUtil.put(context, "adDifferenceValue", adDifferenceValue);
                    //
                    //                            mhandler.postDelayed(new Runnable() {
                    //                                @Override
                    //                                public void run() {
                    //                                    if(1==chooseId){
                    //                                        dojudgeShowDialogOrNot();
                    //                                    }
                    //                                }
                    //                            }, 300);
                    //                        }
                    //                    });
                    break;
                default:
                    break;
            }
        }
    };

    private List<ChannelDetectModel> doHandlerData(List<ChannealBean>clist,List<ChannelDetectModel> mlist){
        List<ChannelDetectModel> newList=new ArrayList<>();
        for(int i=0;i<clist.size();i++){
            ChannealBean bean=clist.get(i);
            String pName=bean.getPassName();
            String mvstr=bean.getMvStr();
            for(int j=0;j<mlist.size();j++){
                ChannelDetectModel modlel=mlist.get(j);
                String name=modlel.getChannelName()+":";
                if(pName.equals(name)  ||  mvstr.equals(name)){
                    newList.add(modlel);
                }
            }
        }

        return newList;
    }

    //--------------------------------------
    //判断是否显示 录车提示框    目标通道 是正常的 才显示。
    public void dojudgeShowDialogOrNot() {
        int size = strList.size();
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < size; i++) {
            String index = strList.get(i);
            if ("0".equals(index)) {
                str.append("0");
            }

        }
        int len = str.length();
        if (size == len) {  //说明里面都是 0 //所选通道全部通过检测
            showDialog(context);
        }else{             //有通道未通过检测 只能录入基本信息 不能录安装人员信息
            showBasicDialog(context);
        }

    }

    //录车提示框
    public void showDialog(final Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_check_turck_enter, null);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isPassCheck="0";
                doAddTruck();
                tempDialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDialog.dismiss();
            }
        });
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        tempDialog = dialog.create();
        tempDialog.setView(view, 0, 0, 0, 0);
        tempDialog.show();
    }

    //录车提示框
    public void showBasicDialog(final Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_only_car_input, null);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isPassCheck="1";
                doAddTruck();
                btempDialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btempDialog.dismiss();
            }
        });
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        btempDialog = dialog.create();
        btempDialog.setView(view, 0, 0, 0, 0);
        btempDialog.show();
    }



    //录车提示框
    public void showChooseChanneal(final Context context,String title,String okStr) {
        if(chtempDialog !=null ){
            chtempDialog.dismiss();
            chtempDialog=null;
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_only_car_input, null);
        TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
        tv_title.setText(title);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        tv_ok.setText(okStr);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PassSettingActNew.start(context,"1");
                chtempDialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chtempDialog.dismiss();
            }
        });
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        chtempDialog = dialog.create();
        chtempDialog.setView(view, 0, 0, 0, 0);
        chtempDialog.show();
    }


    private void doAddTruck() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_for_login, null);
        builder.setView(view);
        builder.setCancelable(true);
        final EditText et_login_count = (EditText) view.findViewById(R.id.et_login_count);
        String uname = acache.getAsString("login_name");
        if (!Tools.isEmpty(uname)) {
            et_login_count.setText(uname);
            et_login_count.setSelection(uname.length());
        }
        final EditText et_login_password = (EditText) view.findViewById(R.id.et_login_password);
        String password=   acache.getAsString("log_psw");
        if (!Tools.isEmpty(password)) {
            et_login_password.setText(password);
        }
        TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);//取消
        tv_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adddialog.cancel();
            }
        });
        TextView tv_login = (TextView) view.findViewById(R.id.tv_login);//登录
        tv_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = et_login_count.getText().toString().trim();
                psw = et_login_password.getText().toString().trim();
                if (Tools.isEmpty(count) || Tools.isEmpty(psw)) {
                    showTips("账号或密码不能为空");
                    return;
                }
                submit(count, psw);
            }
        });
        //取消或确定按钮监听事件处理
        adddialog = builder.create();
        adddialog.show();
    }


    private void submit(String count, String password) {

        final String inputString = count + "#" + password;

        try {
            String encryStr = Aes.encrypt(inputString, PASSWORD_STRING);
            HashMap<String, String> mapLogin = new HashMap<>();
            mapLogin.put("token", encryStr);
            mpresenter.postLogin(mapLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //判断是否在线
    private void CheckOnline() {
        mdevId=  acache.getAsString("n_id");
        if(!Tools.isEmpty(mdevId)){
            doCheckOnline(mdevId);  //判断是否在线
        } else {
            showTips("设备ID不正确");
        }

    }

    //判断车辆是否在线
    private void  doCheckOnline(String id){
        //        showProgressDialog();
        String   token = acache.getAsString("login_token");
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("deviceId", id);
        mpresenter.checkCarOnline(mapParams);
    }


    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        cheDialog = new MyCustomDialog(context, R.style.LoadDialog,"正在检测设备状态...");
        cheDialog.show();
        new Thread("cancle_progressDialog") {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                    // cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
                    // 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
                    if(cheDialog !=null ){
                        cheDialog.cancel();
                    }
                    // dialog.dismiss();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }.start();
    }
    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (cheDialog != null) {
            cheDialog.dismiss();
            cheDialog = null;
        }
    }
    //--------------------------------------



    private void invokeAD(HDModelData data1) {

        String date =acache.getAsString("pass_time");
        if(Tools.isEmpty(date)){
            date="5";
        }
        DETECT_MAX=Integer.parseInt(date);
        if (detectCount == DETECT_MAX) {
            Message msg=new Message();
            msg.what=2;
            msg.arg1=0;
            mhandler.sendMessage(msg);

//            mhandler.sendEmptyMessage(2);
            // LogUtil.e("检查通道次数等于5时detectCount=5,=" + detectCount + "");
            doRefresh(100);
        }else if(detectCount > DETECT_MAX){
            DLog.e(TAG,"检测通道需要值："+DETECT_MAX+"/当前值："+detectCount);
            list0.add(data1.getAd1());list0.remove(0);
            list1.add(data1.getAd2());list1.remove(0);
            list2.add(data1.getAd3());list2.remove(0);
            list3.add(data1.getAd4());list3.remove(0);
            list4.add(data1.getAd5());list4.remove(0);
            list5.add(data1.getAd6());list5.remove(0);
            list6.add(data1.getAd7());list6.remove(0);
            list7.add(data1.getAd8());list7.remove(0);
            list8.add(data1.getAd9());list8.remove(0);
            list9.add(data1.getAd10());list9.remove(0);
            list10.add(data1.getAd11());list10.remove(0);
            list11.add(data1.getAd12());list11.remove(0);
            list12.add(data1.getAd13());list12.remove(0);
            list13.add(data1.getAd14());list13.remove(0);
            list14.add(data1.getAd15());list14.remove(0);
            list15.add(data1.getAd16());list15.remove(0);
            doRefresh(0);
        } else {
            list0.add(data1.getAd1());
            list1.add(data1.getAd2());
            list2.add(data1.getAd3());
            list3.add(data1.getAd4());
            list4.add(data1.getAd5());
            list5.add(data1.getAd6());
            list6.add(data1.getAd7());
            list7.add(data1.getAd8());
            list8.add(data1.getAd9());
            list9.add(data1.getAd10());
            list10.add(data1.getAd11());
            list11.add(data1.getAd12());
            list12.add(data1.getAd13());
            list13.add(data1.getAd14());
            list14.add(data1.getAd15());
            list15.add(data1.getAd16());

        }
        DLog.e(TAG,"检测通道:"+detectCount);
        detectCount++;
    }

    private void invokeMV(HDModelData data1) {
        String  date= acache.getAsString( "mv_date");
        if(Tools.isEmpty(date)){
            date="5";
        }
        DETECT_MAX=Integer.parseInt(date);


        float mvv1 = HandlerBleData.getMvvValue(data1.ad1, data1.adZero1);
        float mvv2 = HandlerBleData.getMvvValue(data1.ad2, data1.adZero2);
        float mvv3 = HandlerBleData.getMvvValue(data1.ad3, data1.adZero3);
        float mvv4 = HandlerBleData.getMvvValue(data1.ad4, data1.adZero4);
        float mvv5 = HandlerBleData.getMvvValue(data1.ad5, data1.adZero5);
        float mvv6 = HandlerBleData.getMvvValue(data1.ad6, data1.adZero6);
        float mvv7 = HandlerBleData.getMvvValue(data1.ad7, data1.adZero7);
        float mvv8 = HandlerBleData.getMvvValue(data1.ad8, data1.adZero8);
        float mvv9 = HandlerBleData.getMvvValue(data1.ad9, data1.adZero9);
        float mvv10 = HandlerBleData.getMvvValue(data1.ad10, data1.adZero10);
        float mvv11 = HandlerBleData.getMvvValue(data1.ad11, data1.adZero11);
        float mvv12 = HandlerBleData.getMvvValue(data1.ad12, data1.adZero12);
        float mvv13 = HandlerBleData.getMvvValue(data1.ad13, data1.adZero13);
        float mvv14 = HandlerBleData.getMvvValue(data1.ad14, data1.adZero14);
        float mvv15 = HandlerBleData.getMvvValue(data1.ad15, data1.adZero15);
        float mvv16 = HandlerBleData.getMvvValue(data1.ad16, data1.adZero16);

        if (detectCount == DETECT_MAX) {
            Message msg=new Message();
            msg.what=2;
            msg.arg1=11;
            mhandler.sendMessage(msg);
//            mhandler.sendEmptyMessage(2);
            // LogUtil.e("检查通道次数等于5时detectCount=5,=" + detectCount + "");
            doRefresh(100);
        }else if(detectCount > DETECT_MAX){
            DLog.e(TAG,"检测MV需要值："+DETECT_MAX+"/当前值："+detectCount);
            list0.add(mvv1);list0.remove(0);
            list1.add(mvv2);list1.remove(0);
            list2.add(mvv3);list2.remove(0);
            list3.add(mvv4);list3.remove(0);
            list4.add(mvv5);list4.remove(0);
            list5.add(mvv6);list5.remove(0);
            list6.add(mvv7);list6.remove(0);
            list7.add(mvv8);list7.remove(0);
            list8.add(mvv9);list8.remove(0);
            list9.add(mvv10);list9.remove(0);
            list10.add(mvv11);list10.remove(0);
            list11.add(mvv12);list11.remove(0);
            list12.add(mvv13);list12.remove(0);
            list13.add(mvv14);list13.remove(0);
            list14.add(mvv15);list14.remove(0);
            list15.add(mvv16);list15.remove(0);
            doRefresh(0);
        } else {
            list0.add(mvv1);
            list1.add(mvv2);
            list2.add(mvv3);
            list3.add(mvv4);
            list4.add(mvv5);
            list5.add(mvv6);
            list6.add(mvv7);
            list7.add(mvv8);
            list8.add(mvv9);
            list9.add(mvv10);
            list10.add(mvv11);
            list11.add(mvv12);
            list12.add(mvv13);
            list13.add(mvv14);
            list14.add(mvv15);
            list15.add(mvv16);

        }
        DLog.e(TAG,"检测MV:"+detectCount);
        detectCount++;
    }

    private void doRefresh(int now){
        boolean b1 = channelDetect(list0,1);
        channelDetectList.get(0).setChannelNormal(b1);
        boolean b2 = channelDetect(list1,2);
        channelDetectList.get(1).setChannelNormal(b2);
        boolean b3 = channelDetect(list2,3);
        channelDetectList.get(2).setChannelNormal(b3);
        boolean b4 = channelDetect(list3,4);
        channelDetectList.get(3).setChannelNormal(b4);
        boolean b5 = channelDetect(list4,5);
        channelDetectList.get(4).setChannelNormal(b5);
        boolean b6 = channelDetect(list5,6);
        channelDetectList.get(5).setChannelNormal(b6);
        boolean b7 = channelDetect(list6,7);
        channelDetectList.get(6).setChannelNormal(b7);
        boolean b8 = channelDetect(list7,8);
        channelDetectList.get(7).setChannelNormal(b8);
        boolean b9 = channelDetect(list8,9);
        channelDetectList.get(8).setChannelNormal(b9);
        boolean b10 = channelDetect(list9,10);
        channelDetectList.get(9).setChannelNormal(b10);
        boolean b11 = channelDetect(list10,11);
        channelDetectList.get(10).setChannelNormal(b11);
        boolean b12 = channelDetect(list11,12);
        channelDetectList.get(11).setChannelNormal(b12);
        boolean b13 = channelDetect(list12,13);
        channelDetectList.get(12).setChannelNormal(b13);
        boolean b14 = channelDetect(list13,14);
        channelDetectList.get(13).setChannelNormal(b14);
        boolean b15 = channelDetect(list14,15);
        channelDetectList.get(14).setChannelNormal(b15);
        boolean b16 = channelDetect(list15,16);
        channelDetectList.get(15).setChannelNormal(b16);
        if (NotNull.isNotNull(channelDetectAdapter)) {
            List<ChannealBean> havelist= doHandlerList(list);//保存的通道  显示 多个通道。
            List<ChannelDetectModel> newlist=  doHandlerData(havelist,channelDetectList);
            channelDetectAdapter.updateListView(newlist);

            DLog.e(TAG,"检测通道和MV刷新页面"+list0.size()+"/"+list1.size()+"/"+list2.size()+"/"+list3.size()+list4.size()+"/"+list5.size()+"/"+list6.size()+"/"+list7.size()
                    +"/"+list8.size()+"/"+list9.size()+"/"+list10.size()+"/"+list11.size()+"/"+list12.size()+"/"+list13.size()+"/"+list14.size()+"/"+list15.size());
        }

        if(100 ==now){   //表示通道检测完成。
            channelModel = (HDKRModel)acache.getAsObject("channel_model");//保存多少通道
            if(channelModel.bChannel1){
                if(b1){
                    strList.add("0"); //通过
                }else{
                    strList.add("1"); //没通过
                }
            }

            if(channelModel.bChannel2){
                if(b2){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }

            if(channelModel.bChannel3){
                if(b3){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }


            if(channelModel.bChannel4){
                if(b4){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel5){
                if(b5){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel6){
                if(b6){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel7){
                if(b7){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel8){
                if(b8){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel9){
                if(b9){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }if(channelModel.bChannel10){
                if(b10){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel11){
                if(b11){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel12){
                if(b12){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel13){
                if(b13){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel14){
                if(b14){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel15){
                if(b15){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }
            if(channelModel.bChannel16){
                if(b16){
                    strList.add("0");
                }else{
                    strList.add("1");
                }
            }



        }
    }

    private boolean channelDetect(ArrayList<Float> floatData,int passway) {
        if(floatData ==null ||  floatData.size() <= 0){
            return false;
        }
        // LogUtil.e("变动后的adDifferenceValue==" + adDifferenceValue + "");
        float max = 0;
        float min = 0;
        float pinjun=0;
        // Float[] numbers = { 0.1f, 0.22f, 0.7f, 0.21f,0.4f, 0.9f, 0.5f};
        max = Collections.max(floatData);
        min = Collections.min(floatData);

        //求ad 平均值
        if(bChannelDetect){  // 只通道检测 显示AD
            for(int i=0;i<floatData.size();i++){ //求平均值
                pinjun=pinjun+floatData.get(i);
            }
            pinjun=pinjun/(floatData.size());
            String pjValue =acache.getAsString("setting_ad_value");
            if(Tools.isEmpty(pjValue)){
                pjValue="131072";
            }
            DLog.e("channelDetect","channelDetect="+pinjun+"/"+pjValue);
            float adValue=Float.parseFloat(pjValue);//默认正值
            if(pinjun  < adValue  && pinjun >(adValue*(-1f))){
                acache.put("ad_check_"+passway,"1");
            }else{
                acache.put("ad_check_"+passway,"0");
            }
        }


        DLog.e(TAG,"最大值==" + max + ",=,最小值" + min);
        if(1==chooseId){
            String value =acache.getAsString("pass_value");
            if(Tools.isEmpty(value)){
                value="10";
            }
            float aFloat=Float.parseFloat(value);
            if(floatData.size() >=4){
                if (floatData.get(0) == 0 && floatData.get(1) == 0 && floatData.get(2) == 0 && floatData.get(3) == 0 && floatData.get(4)== 0) {
                    return false;
                }
            }
            if (max > 520000) {
                return false;
            }
            if (min < -520000) {
                return false;
            }
            if (max - min < aFloat) {
                return true;
            } else {
                return false;
            }

        }else if(2==chooseId){
            if (max - min < adDifferenceValue) {
                return true;
            } else {
                return false;
            }

        }else{
            return false;
        }
    }

    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    @Subscribe (threadMode  = ThreadMode.MAIN)  //必须使用EventBus的订阅注解
    public void onEvent(HDModelData uMode){
        data=uMode;
        mhandler.sendEmptyMessage(1);
//        DLog.e(TAG,"setp3:ChannelParameterFragment接收展示。");
    }


    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(action.equals(BleConstant.ACTION_BLE_HANDLER_DATA)){//接收数据
                    Bundle bundle=intent.getExtras();
                    data=(HDModelData)bundle.getSerializable("ModelData");
                    mhandler.sendEmptyMessage(1);
                    DLog.e(TAG,"setp3:ChannelParameterFragment接收展示。");
                }else if(action.equals(BleConstant.ACTION_CHANNEL_CHANGE)){//通道选择
                    channelModel = (HDKRModel)acache.getAsObject("channel_model");//保存多少通道
                    if(data !=null){
                        mhandler.sendEmptyMessage(1);
                    }else{
                        initData();
                        if(list !=null && list.size() >=0){
                            List<ChannealBean> alist= doHandlerList(list);
                            madapter.setList(alist);
                        }
                    }
                    String ntag=intent.getStringExtra("bro_tag");
                    if("1".equals(ntag)){//表示是通道检测过来的
                        rl_btn.performClick();
                    }
                }else if(action.equals(BleConstant.ACTION_BLE_DISCONNECT)){ //断开。
                    closeDetectChannelProgressDialog();
                }

            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTION_BLE_HANDLER_DATA);
        filter.addAction(BleConstant.ACTION_CHANNEL_CHANGE);
        filter.addAction(BleConstant.ACTION_BLE_DISCONNECT);
        getActivity().registerReceiver(receiver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }

    }
    //fragment刷新数据
    @Override
    public void onBaseRefresh() {
    }


    private void  hanlerData(HDModelData data){
        //        DLog.e(TAG,"setp4:ChannelParameterFragmen处理数据。");

        if(list ==null && list.size() <=0 ){
            return;
        }
        if(data ==null ){
            return;
        }
        try{

            double mvv1 = HandlerBleData.getMvvValue(data.getAd1(), data.getAdZero1());
            double mvv2 = HandlerBleData.getMvvValue(data.getAd2(), data.getAdZero2());
            double mvv3 = HandlerBleData.getMvvValue(data.getAd3(), data.getAdZero3());
            double mvv4 = HandlerBleData.getMvvValue(data.getAd4(), data.getAdZero4());
            double mvv5 = HandlerBleData.getMvvValue(data.getAd5(), data.getAdZero5());
            double mvv6 = HandlerBleData.getMvvValue(data.getAd6(), data.getAdZero6());
            double mvv7 = HandlerBleData.getMvvValue(data.getAd7(), data.getAdZero7());
            double mvv8 = HandlerBleData.getMvvValue(data.getAd8(), data.getAdZero8());
            double mvv9 = HandlerBleData.getMvvValue(data.getAd9(), data.getAdZero9());
            double mvv10 = HandlerBleData.getMvvValue(data.getAd10(), data.getAdZero10());
            double mvv11 = HandlerBleData.getMvvValue(data.getAd11(), data.getAdZero11());
            double mvv12 = HandlerBleData.getMvvValue(data.getAd12(), data.getAdZero12());
            double mvv13 = HandlerBleData.getMvvValue(data.getAd13(), data.getAdZero13());
            double mvv14 = HandlerBleData.getMvvValue(data.getAd14(), data.getAdZero14());
            double mvv15 = HandlerBleData.getMvvValue(data.getAd15(), data.getAdZero15());
            double mvv16 = HandlerBleData.getMvvValue(data.getAd16(), data.getAdZero16());
            ChannealBean c1=list.get(0);
            c1.setPassValue(data.getAd1()+"");
            c1.setMvValue(mvv1+"");

            ChannealBean c2=list.get(1);
            c2.setPassValue(data.getAd2()+"");
            c2.setMvValue(mvv2+"");

            ChannealBean c3=list.get(2);
            c3.setPassValue(data.getAd3()+"");
            c3.setMvValue(mvv3+"");

            ChannealBean c4=list.get(3);
            c4.setPassValue(data.getAd4()+"");
            c4.setMvValue(mvv4+"");

            ChannealBean c5=list.get(4);
            c5.setPassValue(data.getAd5()+"");
            c5.setMvValue(mvv5+"");

            ChannealBean c6=list.get(5);
            c6.setPassValue(data.getAd6()+"");
            c6.setMvValue(mvv6+"");

            ChannealBean c7=list.get(6);
            c7.setPassValue(data.getAd7()+"");
            c7.setMvValue(mvv7+"");

            ChannealBean c8=list.get(7);
            c8.setPassValue(data.getAd8()+"");
            c8.setMvValue(mvv8+"");

            ChannealBean c9=list.get(8);
            c9.setPassValue(data.getAd9()+"");
            c9.setMvValue(mvv9+"");


            ChannealBean c10=list.get(9);
            c10.setPassValue(data.getAd10()+"");
            c10.setMvValue(mvv10+"");

            ChannealBean c11=list.get(10);
            c11.setPassValue(data.getAd11()+"");
            c11.setMvValue(mvv11+"");

            ChannealBean c12=list.get(11);
            c12.setPassValue(data.getAd12()+"");
            c12.setMvValue(mvv12+"");

            ChannealBean c13=list.get(12);
            c13.setPassValue(data.getAd13()+"");
            c13.setMvValue(mvv13+"");

            ChannealBean c14=list.get(13);
            c14.setPassValue(data.getAd14()+"");
            c14.setMvValue(mvv14+"");

            ChannealBean c15=list.get(14);
            c15.setPassValue(data.getAd15()+"");
            c15.setMvValue(mvv15+"");

            ChannealBean c16=list.get(15);
            c16.setPassValue(data.getAd16()+"");
            c16.setMvValue(mvv16+"");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showDetectChannelProgressDialog(String title) {
        if(progressDialogDetectChannel !=null ){
            closeDetectChannelProgressDialog();
            progressDialogDetectChannel=null;
        }
        if (progressDialogDetectChannel == null) {
            progressDialogDetectChannel = new ProgressDialog(context);
            progressDialogDetectChannel.setMessage(title);
            progressDialogDetectChannel.setCanceledOnTouchOutside(false);
        }
        progressDialogDetectChannel.show();
    }

    private void closeDetectChannelProgressDialog() {
        if (progressDialogDetectChannel != null) {
            progressDialogDetectChannel.dismiss();
        }
    }

    private void initData(){
        list=new ArrayList<>();
        ChannealBean c1=new ChannealBean();
        c1.setPassName("通道1:");
        c1.setPassValue("0.00");
        c1.setMvv("mv/v1:");
        c1.setMvValue("0.00");
        c1.setMvStr("MV1:");
        list.add(c1);

        ChannealBean c2=new ChannealBean();
        c2.setPassName("通道2:");
        c2.setPassValue("0.00");
        c2.setMvv("mv/v2:");
        c2.setMvValue("0.00");
        c2.setMvStr("MV2:");
        list.add(c2);

        ChannealBean c3=new ChannealBean();
        c3.setPassName("通道3:");
        c3.setPassValue("0.00");
        c3.setMvv("mv/v3:");
        c3.setMvValue("0.00");
        c3.setMvStr("MV3:");
        list.add(c3);

        ChannealBean c4=new ChannealBean();
        c4.setPassName("通道4:");
        c4.setPassValue("0.00");
        c4.setMvv("mv/v4:");
        c4.setMvValue("0.00");
        c4.setMvStr("MV4:");
        list.add(c4);

        ChannealBean c5=new ChannealBean();
        c5.setPassName("通道5:");
        c5.setPassValue("0.00");
        c5.setMvv("mv/v5:");
        c5.setMvValue("0.00");
        c5.setMvStr("MV5:");
        list.add(c5);

        ChannealBean c6=new ChannealBean();
        c6.setPassName("通道6:");
        c6.setPassValue("0.00");
        c6.setMvv("mv/v6:");
        c6.setMvValue("0.00");
        c6.setMvStr("MV6:");
        list.add(c6);

        ChannealBean c7=new ChannealBean();
        c7.setPassName("通道7:");
        c7.setPassValue("0.00");
        c7.setMvv("mv/v7:");
        c7.setMvValue("0.00");
        c7.setMvStr("MV7:");
        list.add(c7);

        ChannealBean c8=new ChannealBean();
        c8.setPassName("通道8:");
        c8.setPassValue("0.00");
        c8.setMvv("mv/v8:");
        c8.setMvValue("0.00");
        c8.setMvStr("MV8:");
        list.add(c8);

        ChannealBean c9=new ChannealBean();
        c9.setPassName("通道9:");
        c9.setPassValue("0.00");
        c9.setMvv("mv/v9:");
        c9.setMvValue("0.00");
        c9.setMvStr("MV9:");
        list.add(c9);

        ChannealBean c10=new ChannealBean();
        c10.setPassName("通道10:");
        c10.setPassValue("0.00");
        c10.setMvv("mv/v10:");
        c10.setMvValue("0.00");
        c10.setMvStr("MV10:");
        list.add(c10);

        ChannealBean c11=new ChannealBean();
        c11.setPassName("通道11:");
        c11.setPassValue("0.00");
        c11.setMvv("mv/v11:");
        c11.setMvValue("0.00");
        c11.setMvStr("MV11:");
        list.add(c11);

        ChannealBean c12=new ChannealBean();
        c12.setPassName("通道12:");
        c12.setPassValue("0.00");
        c12.setMvv("mv/v12:");
        c12.setMvValue("0.00");
        c12.setMvStr("MV12:");
        list.add(c12);

        ChannealBean c13=new ChannealBean();
        c13.setPassName("通道13:");
        c13.setPassValue("0.00");
        c13.setMvv("mv/v13:");
        c13.setMvValue("0.00");
        c13.setMvStr("MV13:");
        list.add(c13);

        ChannealBean c14=new ChannealBean();
        c14.setPassName("通道14:");
        c14.setPassValue("0.00");
        c14.setMvv("mv/v14:");
        c14.setMvValue("0.00");
        c14.setMvStr("MV14:");
        list.add(c14);

        ChannealBean c15=new ChannealBean();
        c15.setPassName("通道15:");
        c15.setPassValue("0.00");
        c15.setMvv("mv/v15:");
        c15.setMvValue("0.00");
        c15.setMvStr("MV15:");
        list.add(c15);

        ChannealBean c16=new ChannealBean();
        c16.setPassName("通道16:");
        c16.setPassValue("0.00");
        c16.setMvv("mv/v16:");
        c16.setMvValue("0.00");
        c16.setMvStr("MV16:");
        list.add(c16);
    }

    //判断是否选择通道
    public  List<String>  doJugdeChanneal(){
        channelModel = (HDKRModel)acache.getAsObject("channel_model");//保存多少通道
        List<String> newlist=new ArrayList<>();
        if(channelModel !=null){
            if(list !=null && list.size() > 0){
                if(channelModel.bChannel1){
                    newlist.add("1");
                }
                if(channelModel.bChannel2){
                    newlist.add("2");
                }
                if(channelModel.bChannel3){
                    newlist.add("3");
                }
                if(channelModel.bChannel4){
                    newlist.add("4");
                }
                if(channelModel.bChannel5){
                    newlist.add("5");
                }
                if(channelModel.bChannel6){
                    newlist.add("6");
                }
                if(channelModel.bChannel7){
                    newlist.add("7");
                }
                if(channelModel.bChannel8){
                    newlist.add("8");
                }
                if(channelModel.bChannel9){
                    newlist.add("9");
                }
                if(channelModel.bChannel10){
                    newlist.add("10");
                }
                if(channelModel.bChannel11){
                    newlist.add("11");
                }
                if(channelModel.bChannel12){
                    newlist.add("12");
                }

                if(channelModel.bChannel13){
                    newlist.add("13");
                }
                if(channelModel.bChannel14){
                    newlist.add("14");
                }
                if(channelModel.bChannel15){
                    newlist.add("15");
                }
                if(channelModel.bChannel16){
                    newlist.add("16");
                }
            }

        }
        return  newlist;
    }

    public  List<ChannealBean>  doHandlerList(List<ChannealBean> list){
        channelModel = (HDKRModel)acache.getAsObject("channel_model");//保存多少通道
        List<ChannealBean> newlist=new ArrayList<>();
        if(channelModel !=null){
            if(list !=null && list.size() > 0){
                if(channelModel.bChannel1){
                    newlist.add(list.get(0));
                }
                if(channelModel.bChannel2){
                    newlist.add(list.get(1));
                }
                if(channelModel.bChannel3){
                    newlist.add(list.get(2));
                }
                if(channelModel.bChannel4){
                    newlist.add(list.get(3));
                }
                if(channelModel.bChannel5){
                    newlist.add(list.get(4));
                }
                if(channelModel.bChannel6){
                    newlist.add(list.get(5));
                }
                if(channelModel.bChannel7){
                    newlist.add(list.get(6));
                }
                if(channelModel.bChannel8){
                    newlist.add(list.get(7));
                }
                if(channelModel.bChannel9){
                    newlist.add(list.get(8));
                }
                if(channelModel.bChannel10){
                    newlist.add(list.get(9));
                }
                if(channelModel.bChannel11){
                    newlist.add(list.get(10));
                }
                if(channelModel.bChannel12){
                    newlist.add(list.get(11));
                }

                if(channelModel.bChannel13){
                    newlist.add(list.get(12));
                }
                if(channelModel.bChannel14){
                    newlist.add(list.get(13));
                }
                if(channelModel.bChannel15){
                    newlist.add(list.get(14));
                }
                if(channelModel.bChannel16){
                    newlist.add(list.get(15));
                }
            }

        }
        return  newlist;
    }

    @Override
    public void loginError(String msg) {

    }

    @Override
    public void loginSuccess(List<CarInfo> carInfo) {

    }

    @Override
    public void downloadSuccess(BluetoothDevice dev) {

    }

    @Override
    public void downloadError(BluetoothDevice dev) {

    }

    @Override
    public void doLogin(UserResultBean bean) {
        closedialog();
        showTips("登录成功");

        UserResultBean userBean=bean;
        String token=bean.getToken();
        String uname=bean.getResult().getUserName();
        acache.put("login_name",uname);
        acache.put("login_token",token);
        acache.put("log_psw",psw);

        CheckOnline();

    }

    //跳转添加车辆页面
    private  void jumpAddTruck(){
        mdevId=  acache.getAsString("n_id");
        truckNum= acache.getAsString("car_num");
        String number="";
        String devid="";
        if(isConnected){
            number=truckNum;
            devid=mdevId;
        }
        Intent addIntent=new Intent(context,AddTruckBasicActivity.class);//?
        addIntent.putExtra("car_number",number);
        addIntent.putExtra("device_id",devid);
        addIntent.putExtra("is_connected",isConnected);
        addIntent.putExtra("is_only_carInput",isPassCheck);
        context.startActivity(addIntent);
    }

    @Override
    public void doLoginFail(String str) {
        closedialog();
        showTips(str);
    }

    @Override
    public void onlineSuccess(String msg) {
        String line="不在线";
        if("1".equals(msg)){ //正在运行
            line="在线";
        }else{  //停止运行
            line="不在线";
        }
        doOnline(truckNum,mdevId,line);
    }

    //显示 是否在线 对话框
    private void  doOnline(String stuckNumer,String id,final String linemsg){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_do_online_or, null);
        builder.setView(view);
        builder.setCancelable(false);
        //        final TextView tv_number=(TextView)view.findViewById(R.id.tv_number);
        //        tv_number.setText(stuckNumer);

        final TextView tv_id=(TextView)view.findViewById(R.id.tv_id);
        tv_id.setText(id);

        final TextView tv_online_or=(TextView)view.findViewById(R.id.tv_online_or);
        tv_online_or.setText(linemsg);

        TextView tv_cancle=(TextView)view.findViewById(R.id.tv_cancle);//取消
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlienAdddialog.cancel();
            }
        });
        TextView tv_login=(TextView)view.findViewById(R.id.tv_login);//下一步 上传图片和excle
        if("不在线".equals(linemsg)){
            tv_login.setEnabled(false);
            tv_login.setBackground(getResources().getDrawable(R.drawable.btn_round_corner_hui));
        }else{
            tv_login.setEnabled(true);
            tv_login.setBackground(getResources().getDrawable(R.drawable.btn_round_corner));
        }
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlienAdddialog.cancel();
                if("在线".equals(linemsg)){
                    jumpAddTruck();
                }
            }
        });
        //取消或确定按钮监听事件处理
        onlienAdddialog = builder.create();
        onlienAdddialog.show();

    }

    @Override
    public void onlineFail(String msg) {
        showTips(msg);
        closeonlienAdddialog();
    }

    /**
     * 关闭进度对话框
     */
    private void closedialog() {
        if (adddialog != null) {
            adddialog.dismiss();
            adddialog = null;
        }
    }

    /**
     * 关闭进度对话框
     */
    private void closeonlienAdddialog() {
        if (onlienAdddialog != null) {
            onlienAdddialog.dismiss();
            onlienAdddialog = null;
        }
    }
}
