package cn.hand.tech.ui.setting;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.hand.tech.BApplication;
import cn.hand.tech.R;
import cn.hand.tech.ble.bean.GPRSbean;
import cn.hand.tech.ble.bean.GPSTimebean;
import cn.hand.tech.ble.bean.GPSbean;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.home.BaseFragment;
import cn.hand.tech.ui.setting.bean.AutoCheckFrgBean;
import cn.hand.tech.ui.setting.bean.CarNumberInfo;
import cn.hand.tech.ui.setting.bean.CheckBean;
import cn.hand.tech.ui.setting.bean.SensorInfo;
import cn.hand.tech.ui.setting.presenter.AutoCheckPresenter;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.Aes;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/*
 *自动检测  主机检测
 */
public class BaseCheckFragment extends BaseFragment implements  IAutoCheckView{
    private static final String TAG ="AutoCheckAct" ;
    private Context context;
    private TextView tv_carNumber,tv_devid,tv_online_ornot,tv_gprs_online,tv_hard_ver,tv_hardver_dev,tv_soft_ver,tv_softver_dev;
    private ImageView img_dianya,img_check_dianya,img_network,img_net_status;
    private TextView tv_dianya,tv_dy_status,tv_network_bg,tv_sim_status,tv_net_status,tv_signal_status,tv_tcp_status;
    private RelativeLayout rl_prgbar_ing,rl_network_probar;
    private ImageView img_gps,img_check_end,img_cjq;
    private TextView tv_gps_bg,tv_stars_number,tv_location_status,tv_tianxian_status;
    private RelativeLayout rl_gps_checking,rl_cjq_pbar;
    private TextView tv_cjq,tv_xcai_tag,tv_cjq_status,tv_chuangan;
    private ImageView img_cjq_status;
    private ImageView img_chuangan,img_chuangan_staus;
    private RelativeLayout rl_chuangan_probar;
    private TextView tv_chuangan_xian;
    private Button bt_check_again;
    private AutoCheckPresenter mpresenter;
    private ACache acache;
    private boolean isConnected;
    private String mdevId;
    private static final String PASSWORD_STRING = "6a9fd0d1a950420b812f27e970afcd8f"; // static final 静态常量
    private CarNumberInfo mInfo;
    private BroadcastReceiver receiver;
    private Timer timer; //定时器
    private TextView tv_sim;
    private SensorInfo mSenorInfo;
    private TextView tv_pass_status_1, tv_pass_status_2, tv_pass_status_3, tv_pass_status_4, tv_pass_status_5, tv_pass_status_6, tv_pass_status_7, tv_pass_status_8, tv_pass_status_9, tv_pass_status_10,
            tv_pass_status_11, tv_pass_status_12, tv_pass_status_13, tv_pass_status_14, tv_pass_status_15, tv_pass_status_16;
    private TextView tv_v47_status_1, tv_v47_status_2, tv_v47_status_3, tv_v47_status_4, tv_v47_status_5, tv_v47_status_6, tv_v47_status_7, tv_v47_status_8, tv_v47_status_9, tv_v47_status_10,
            tv_v47_status_11, tv_v47_status_12, tv_v47_status_13, tv_v47_status_14, tv_v47_status_15, tv_v47_status_16;
    private ACache bacache;
    private LinearLayout ll_sensor;
    private LinearLayout ll_sensor_v47;
    private boolean isDianya;
    private boolean isGprs,isGps,isCollection,isSensor;
    private TextView tv_next_check;
    private LinearLayout ll_gudie_advise;
    private TextView tv_dy_1,tv_gprs_2,tv_colletion_3,tv_sensor_4;
    private LinearLayout ll_gps_date;
    private TextView tv_gps_time;
    private LinearLayout ll_stars;
    private ImageView img_inputfra,img_input_chang;
    private TextView tv_input_cang,tv_cause_content,tv_measure_content;
    private RelativeLayout rl_input_chang;
    private AutoCheckFrgBean mAutoCheckFrgBean;
    private TextView tv_check_status;
    private boolean isZhuji;
    private  List<String> sensorHaveList=new ArrayList<>();
    private TextView tv_check_result;
    private CheckBean cbean;
    private String isHaveAllPass="0";
    private boolean isDyOver,isGpsOver,isGprsOver,isConllOver,isSenOver;
    private TextView tv_uninstall_sensior;

    public static void start(Context context) {
        Intent intent = new Intent(context, BaseCheckFragment.class);
        context.startActivity(intent);
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_carNumber.setText(mInfo.getCarNumber());
                    tv_devid.setText(mInfo.getDeviceId());
                    tv_online_ornot.setText("连接");
                    tv_hard_ver.setText(mInfo.getHwVersion()+"");
                    tv_soft_ver.setText(mInfo.getVersion()+"");
                    break;
                case 3:  // 电压 正常
                    img_dianya.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                    tv_dianya.setBackgroundResource(R.mipmap.icon_data_pass);
                    rl_prgbar_ing.setVisibility(View.GONE);
                    img_check_dianya.setVisibility(View.VISIBLE);
                    img_check_dianya.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));
                    isDianya=true;
                    if(cbean !=null) {

                        cbean.setDianya(true);
                    }
                    break;
                case 2://电压 不正常
                    img_dianya.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
                    tv_dianya.setBackgroundResource(R.mipmap.icon_data_ing);
                    rl_prgbar_ing.setVisibility(View.GONE);
                    img_check_dianya.setVisibility(View.VISIBLE);
                    img_check_dianya.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_wrong));

                    isDianya=false;
                    break;

                case 4: //gprs不正常
                    img_network.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
                    tv_network_bg.setBackgroundResource(R.mipmap.icon_data_ing);
                    rl_network_probar.setVisibility(View.GONE);
                    img_net_status.setVisibility(View.VISIBLE);
                    img_net_status.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_wrong));
                    isGprs=false;
                    break;
                case 5:  //gprs正常
                    img_network.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                    tv_network_bg.setBackgroundResource(R.mipmap.icon_data_pass);
                    rl_network_probar.setVisibility(View.GONE);
                    img_net_status.setVisibility(View.VISIBLE);
                    img_net_status.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));
                    isGprs=true;

                    if(cbean !=null){

                        cbean.setNetWork(true);
                    }
                    break;

                case 6: //gps 不正常
                    img_gps.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
                    tv_gps_bg.setBackgroundResource(R.mipmap.icon_data_ing);
                    rl_gps_checking.setVisibility(View.GONE);
                    img_check_end.setVisibility(View.VISIBLE);
                    img_check_end.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_wrong));
                    isGps=false;

                    break;
                case 7://gps 正常
                    img_gps.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                    tv_gps_bg.setBackgroundResource(R.mipmap.icon_data_pass);
                    rl_gps_checking.setVisibility(View.GONE);
                    img_check_end.setVisibility(View.VISIBLE);
                    img_check_end.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));
                    isGps=true;
                    if(cbean !=null) {

                        cbean.setGps(true);
                    }
                    break;

                case 8: //采集器 不正常
                    img_cjq.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
                    tv_cjq.setBackgroundResource(R.mipmap.icon_data_ing);
                    rl_cjq_pbar.setVisibility(View.GONE);
                    img_cjq_status.setVisibility(View.VISIBLE);
                    img_cjq_status.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_wrong));
                    isCollection=false;
                    break;
                case 9:  //采集器 正常
                    img_cjq.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                    tv_cjq.setBackgroundResource(R.mipmap.icon_data_pass);
                    rl_cjq_pbar.setVisibility(View.GONE);
                    img_cjq_status.setVisibility(View.VISIBLE);
                    img_cjq_status.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));
                    isCollection=true;

                    if(cbean !=null) {

                        cbean.setCollection(true);
                    }
                    break;


                case 10: //传感器 不正常
                    img_chuangan.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
                    tv_chuangan.setBackgroundResource(R.mipmap.icon_data_ing);
                    rl_chuangan_probar.setVisibility(View.GONE);
                    img_chuangan_staus.setVisibility(View.VISIBLE);
                    img_chuangan_staus.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_wrong));
                    isSensor=false;
                    DLog.e("传感器","传感器=不正常");
                    break;
                case 11://传感器正常
                    img_chuangan.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                    tv_chuangan.setBackgroundResource(R.mipmap.icon_data_pass);
                    rl_chuangan_probar.setVisibility(View.GONE);
                    img_chuangan_staus.setVisibility(View.VISIBLE);
                    img_chuangan_staus.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));
                    isSensor=true;
                    if(cbean !=null) {

                        cbean.setSensior(true);
                    }
                    DLog.e("传感器","传感器=正常");
                    break;
                case 12://自动检测结束
                    showFragoryStatus();
                    ll_gudie_advise.setVisibility(View.VISIBLE);
                    if(isDianya){
                        tv_dy_1.setVisibility(View.GONE);

                    }else{
                        tv_dy_1.setVisibility(View.VISIBLE);
                    }
                    //
                    //                    String dy=bacache.getAsString("dian_ya"); ///解决电压无返回状态
                    //                    if(!Tools.isEmpty(dy)){
                    //                        Intent dintent = new Intent(BleConstant.ACTION_AUTO_DIANYA);
                    //                        dintent.putExtra("li_volate", dy);
                    //                        context.sendBroadcast(dintent);
                    //                    }

                    if(isGps && isGprs){
                        tv_gprs_2.setVisibility(View.GONE);
                    }else{
                        tv_gprs_2.setVisibility(View.VISIBLE);
                    }

                    if(isCollection){
                        tv_colletion_3.setVisibility(View.GONE);
                    }else{
                        tv_colletion_3.setVisibility(View.VISIBLE);
                    }

                    if(isSensor){
                        tv_sensor_4.setVisibility(View.GONE);
                    }else{
                        tv_sensor_4.setVisibility(View.VISIBLE);
                    }

                    tv_check_result.setVisibility(View.VISIBLE);
                    if(cbean !=null){
                        boolean isd=cbean.isDianya();
                        boolean isgprs=cbean.isNetWork();
                        boolean isgps=cbean.isGps();
                        boolean iscollention=cbean.isCollection();
                        boolean issenior=cbean.isSensior();
                        if(isd &&  isgprs&& isgps &&iscollention && issenior){
                            tv_check_result.setText("检测通过");
                            tv_check_result.setTextColor(context.getResources().getColor(R.color.green_fanxian));
                        }else{
                            tv_check_result.setText("检测不通过");
                            tv_check_result.setTextColor(context.getResources().getColor(R.color.red));
                        }
                    }else{
                        DLog.e("BaseCheckFragment","数据体异常");
                        tv_check_result.setText("");
                    }

                    break;
                case 13://允许进厂
                    if(mAutoCheckFrgBean !=null){
                        String type=mAutoCheckFrgBean.getAlarmType();
                        if(Tools.isEmpty(type)){
                            DLog.e("AutoCheck","mAutoCheckFrgBean.AlarmType"+"为空");
                            return;
                        }
                        DLog.e("AutoCheck","mAutoCheckFrgBean.AlarmType"+type);

                        rl_input_chang.setVisibility(View.VISIBLE);
                        img_inputfra.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
                        tv_input_cang.setBackgroundResource(R.mipmap.icon_data_ing);
                        img_input_chang.setVisibility(View.GONE);
                        rl_input_chang.setVisibility(View.GONE);


                        if("0".equals(type)){
                            tv_cause_content.setText("检测正常");
                            tv_measure_content.setText("");
                            isZhuji=true;

                        }else if("1".equals(type)){

                            tv_cause_content.setText("司机断电【检查主机是否上线】");
                            tv_measure_content.setText("");
                            isZhuji=false;

                        }else if("2".equals(type)){
                            tv_cause_content.setText("设备离线【检查主机是否上线】");
                            tv_measure_content.setText("");
                            isZhuji=false;

                        }else if("3".equals(type)){
                            tv_cause_content.setText("重量异常【检查传感器】");
                            tv_measure_content.setText("");
                            isZhuji=false;

                        }else if("4".equals(type)){
                            tv_cause_content.setText("主机异常【维修后请提交维修记录】");
                            tv_measure_content.setText("");
                            isZhuji=false;

                        }else if("5".equals(type)){
                            tv_cause_content.setText("GPS异常【维修后请提交维修记录】");
                            tv_measure_content.setText("");
                            isZhuji=false;

                        }else if("6".equals(type)){
                            tv_cause_content.setText("传感器异常【维修后请提交维修记录】");
                            tv_measure_content.setText("");
                            isZhuji=false;
                        }else{
                            isZhuji=false;
                            DLog.e("AutoCheck","mAutoCheckFrgBean.AlarmType"+"不在范围内");
                        }

                    }
                    break;
                case 15://
                    int number1=msg.arg1;
                    tv_check_status.setText(number1+"/20");
                    break;

                case 16:
                    tv_check_result.setVisibility(View.VISIBLE);
                    tv_check_result.setText("检测通过");
                    tv_check_result.setTextColor(context.getResources().getColor(R.color.green_fanxian));
                    ll_gudie_advise.setVisibility(View.GONE);

                    img_dianya.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                    tv_dianya.setBackgroundResource(R.mipmap.icon_data_pass);
                    rl_prgbar_ing.setVisibility(View.GONE);
                    img_check_dianya.setVisibility(View.VISIBLE);
                    img_check_dianya.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));

                    img_network.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                    tv_network_bg.setBackgroundResource(R.mipmap.icon_data_pass);
                    rl_network_probar.setVisibility(View.GONE);
                    img_net_status.setVisibility(View.VISIBLE);
                    img_net_status.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));

                    img_gps.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                    tv_gps_bg.setBackgroundResource(R.mipmap.icon_data_pass);
                    rl_gps_checking.setVisibility(View.GONE);
                    img_check_end.setVisibility(View.VISIBLE);
                    img_check_end.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));

                    img_cjq.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                    tv_cjq.setBackgroundResource(R.mipmap.icon_data_pass);
                    rl_cjq_pbar.setVisibility(View.GONE);
                    img_cjq_status.setVisibility(View.VISIBLE);
                    img_cjq_status.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));

                    img_chuangan.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                    tv_chuangan.setBackgroundResource(R.mipmap.icon_data_pass);
                    rl_chuangan_probar.setVisibility(View.GONE);
                    img_chuangan_staus.setVisibility(View.VISIBLE);
                    img_chuangan_staus.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));
                    break;
                //                case 16://  检测到10次 判断一遍  是否 单独所有项全部  都有通过的
                //                    if(cbean !=null){
                //                        boolean isd=cbean.isDianya();
                //                        boolean isgprs=cbean.isNetWork();
                //                        boolean isgps=cbean.isGps();
                //                        boolean iscollention=cbean.isCollection();
                //                        boolean issenior=cbean.isSensior();
                //                        if(isd &&  isgprs&& isgps &&iscollention && issenior){
                //                            mHandler.sendEmptyMessage(12);
                //
                //                            timer.cancel();
                //                            bacache.put("ble_fa","0");
                //                            timer=null;
                //                        }
                //                        DLog.e("AutoCheck","独立检测?电压="+isd+"/GPRS="+isgprs+"/GPS="+isgps+"/iscollention="+iscollention+"/issenior="+issenior);
                //                    }

                //                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        View view = inflater.inflate(R.layout.activity_auto_check_1, container, false);
        mpresenter = new AutoCheckPresenter(context, this);
        acache= ACache.get(context,"WeightFragment");
        bacache= ACache.get(BApplication.mContext,"ble");
        bacache.put("ble_fa","0");
        mdevId=  acache.getAsString("n_id");
//        mdevId="431381525";                                     //test
        String isCon = acache.getAsString("is_connect");
        if ("2".equals(isCon)) {
            isConnected = true;
        } else {
            isConnected = false;
        }
        timer = new Timer();
        initView(view);
        registerBrodcat();
        initData();
        return view;
    }

    private void initData() {

        try{

            if(!isConnected){
                showTips("蓝牙未连接");
                return;
            }
            if(Tools.isEmpty(mdevId)){
                showTips("设备ID为空");
                return;
            }
            cbean=new CheckBean(); //检测结果收集bean
            cbean.setDianya(false);
            cbean.setGps(false);
            cbean.setNetWork(false);
            cbean.setCollection(false);
            cbean.setSensior(false);


            final int number = 20;//设置运行二十次
            TimerTask task = new TimerTask() {
                int count = 0;	//从0开始计数，每运行一次timertask次数加一，运行制定次数后结束。
                @Override
                public void run() {
                    if(count<number){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                doinitFirstView();
                            }
                        });

                        /*
                         *检测逻辑:前10次 集体通过才算通过  超过10次以后 单项通过 算通过
                         */
                        if(count <=10){
                            if(cbean !=null){
                                boolean isd0=cbean.isDianya();
                                boolean isgprs0=cbean.isNetWork();
                                boolean isgps0=cbean.isGps();
                                boolean iscollention0=cbean.isCollection();
                                boolean issenior0=cbean.isSensior();
                                if(isd0 &&  isgprs0&& isgps0 &&iscollention0 && issenior0){
                                    isHaveAllPass="1";

                                }else{
                                    cbean.setDianya(false);
                                    cbean.setGps(false);
                                    cbean.setNetWork(false);
                                    cbean.setCollection(false);
                                    cbean.setSensior(false);

                                }
                                DLog.e("AutoCheck","独立检测?电压="+count+"==>"+isd0+"/GPRS="+isgprs0+"/GPS="+isgps0+"/iscollention="+iscollention0+"/issenior="+issenior0);
                            }

                            if(10==count){
                                if("1".equals(isHaveAllPass)){
                                    timer.cancel();
                                    bacache.put("ble_fa","0");
                                    timer=null;
                                    mHandler.sendEmptyMessage(16);
                                    isDyOver=true;isSenOver=true;isGpsOver=true;isGprsOver=true;isConllOver=true;
                                }else{

                                    cbean=new CheckBean(); //检测结果收集bean
                                    cbean.setDianya(false);
                                    cbean.setGps(false);
                                    cbean.setNetWork(false);
                                    cbean.setCollection(false);
                                    cbean.setSensior(false);
                                }
                            }

                            if(count ==5 || count ==10 ){//每隔5次  拉取状态

                                String count="huxinzhao"; //去检查gprs是否在线
                                String psw="hd123456";
                                submit(count,psw);

                            }
                        }


                        if(count >10){
                            if(isDianya){
                                mHandler.sendEmptyMessage(3); //正常
                                isDyOver=true;
                            }
                            if(isGprs){
                                mHandler.sendEmptyMessage(5); //正常
                                isGprsOver=true;
                            }
                            if(isGps){
                                mHandler.sendEmptyMessage(7); //正常
                                isGpsOver=true;
                            }
                            if(isCollection){
                                mHandler.sendEmptyMessage(9); //正常
                                isConllOver=true;
                            }
                            if(isSensor){
                                isSenOver=true;
                                mHandler.sendEmptyMessage(10); //正常
                            }
                            if(count ==15 || count ==20){
                                String count="huxinzhao"; //去检查gprs是否在线
                                String psw="hd123456";
                                submit(count,psw);
                            }
                        }

                        Intent autoIntent=new Intent(BleConstant.ACTION_ACUTO_CHECK);
                        int dvid=Integer.parseInt(mdevId);
                        autoIntent.putExtra("auto_id",dvid);
                        context.sendBroadcast(autoIntent);
                        bacache.put("ble_fa","1");

                        HashMap<String,String>hmap=new HashMap<>(); //获取车辆信息
                        hmap.put("deviceId",mdevId); //462328153
                        mpresenter.getSensorByDeviceId(hmap);

                        Message meg=new Message();
                        meg.what=15;
                        meg.arg1=count;
                        mHandler.sendMessage(meg);


                    } else  {

                        timer.cancel();
                        bacache.put("ble_fa","0");
                        timer=null;
                        //自动检测结束

                        mHandler.sendEmptyMessage(12);
                    }

                    count++;
                }
            };
            if(timer ==null){
                timer=new Timer();
            }
            timer.schedule(task, 500,4500);//每隔1000毫秒即一秒运行一次该程序

            //            doInputFragory();//允许状态 栏  初始状态

            HashMap<String,String>hmap=new HashMap<>(); //获取车辆信息
            hmap.put("deviceId",mdevId);
            mpresenter.checkCarNumberExisit(hmap);


            //        Intent i=new Intent(BleConstant.ACTION_READ_HARD_SOFT);
            //        int dvid=Integer.parseInt(mdevId);
            //        i.putExtra("n_id",dvid);
            //
            //
            //       sendBroadcast(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //允许进厂 初始化
    private void doInputFragory(){
        rl_input_chang.setVisibility(View.VISIBLE);
        img_inputfra.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
        tv_input_cang.setBackgroundResource(R.mipmap.icon_data_ing);
        img_input_chang.setVisibility(View.GONE);
        tv_cause_content.setText("");
        isZhuji=false;
        tv_measure_content.setText("");
    }
    //显示进厂检测
    public void showFragoryStatus(){
        tv_check_status.setText("检测完成");
        if(isDianya && isGps && isGprs){
            tv_measure_content.setText("检测正常");
            if(isZhuji){
                img_inputfra.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_pass_next));
                tv_input_cang.setBackgroundResource(R.mipmap.icon_data_pass);
                rl_input_chang.setVisibility(View.GONE);
                img_input_chang.setVisibility(View.VISIBLE);
                img_input_chang.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_right));
            }else{
                img_inputfra.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
                tv_input_cang.setBackgroundResource(R.mipmap.icon_data_ing);
                rl_input_chang.setVisibility(View.GONE);
                img_input_chang.setVisibility(View.VISIBLE);
                img_input_chang.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_wrong));
            }
        }else{
            img_inputfra.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
            tv_input_cang.setBackgroundResource(R.mipmap.icon_data_ing);
            rl_input_chang.setVisibility(View.GONE);
            img_input_chang.setVisibility(View.VISIBLE);
            img_input_chang.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_check_wrong));
            StringBuffer stb=new StringBuffer();
            if(!isDianya){
                stb.append("电压异常;");
            }
            if(!isGps){
                stb.append("GPS异常;");
            }
            if(!isGprs){
                stb.append("无线网络异常;");
            }
            tv_measure_content.setText(stb.toString());
        }
    }

    //获取允许进厂数据
    private void doGetDataFragory(String mtoken){
        HashMap<String,String>hmap=new HashMap<>(); //
        hmap.put("deviceId",mdevId);
        hmap.put("token",mtoken);
        hmap.put("lastData","1");
        hmap.put("version","2");
        mpresenter.getOrNotInputFragory(hmap);
    }

    private void doinitFirstView(){
        rl_prgbar_ing.setVisibility(View.VISIBLE);
        rl_network_probar.setVisibility(View.VISIBLE);
        rl_gps_checking.setVisibility(View.VISIBLE);
        rl_cjq_pbar.setVisibility(View.VISIBLE);
        rl_chuangan_probar.setVisibility(View.VISIBLE);

        img_check_dianya.setVisibility(View.GONE);
        img_net_status.setVisibility(View.GONE);
        img_check_end.setVisibility(View.GONE);
        img_cjq_status.setVisibility(View.GONE);
        img_chuangan_staus.setVisibility(View.GONE);

        tv_stars_number.setVisibility(View.GONE);


        img_dianya.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
        tv_dianya.setBackgroundResource(R.mipmap.icon_data_ing);
        img_gps.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
        tv_gps_bg.setBackgroundResource(R.mipmap.icon_data_ing);
        img_network.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
        tv_network_bg.setBackgroundResource(R.mipmap.icon_data_ing);
        img_chuangan.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
        tv_chuangan.setBackgroundResource(R.mipmap.icon_data_ing);
        img_cjq.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
        tv_cjq.setBackgroundResource(R.mipmap.icon_data_ing);

        isDianya=false;
        isGprs=false;
        isGps=false;
        isSensor=false;
        isCollection=false;
    }

    private void initView(View view) {
        LinearLayout   ll_back=(LinearLayout)view.findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        tv_next_check=(TextView)view.findViewById(R.id.tv_next_check);
        tv_next_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer !=null){
                    timer.cancel();
                    timer=null;
                }

                isDianya=false; isDyOver=false;
                isGprs=false;isGprsOver=false;
                isGps=false;isGpsOver=false;
                isCollection=false;isConllOver=false;
                isSensor=false;isSenOver=false;
                isHaveAllPass="0";
                initData();
                ll_gudie_advise.setVisibility(View.GONE);
                tv_check_result.setVisibility(View.GONE);
            }
        });

        tv_carNumber=(TextView)view.findViewById(R.id.tv_carNumber);
        tv_devid=(TextView)view.findViewById(R.id.tv_devid);
        tv_online_ornot=(TextView)view.findViewById(R.id.tv_online_ornot);
        tv_gprs_online=(TextView)view.findViewById(R.id.tv_gprs_online);
        tv_hard_ver=(TextView)view.findViewById(R.id.tv_hard_ver);
        tv_hardver_dev=(TextView)view.findViewById(R.id.tv_hardver_dev);
        tv_soft_ver=(TextView)view.findViewById(R.id.tv_soft_ver);
        tv_softver_dev=(TextView)view.findViewById(R.id.tv_softver_dev);

        tv_check_status=(TextView)view.findViewById(R.id.tv_check_status);//检测状态

        img_dianya=(ImageView)view.findViewById(R.id.img_dianya);             //电压
        tv_dianya=(TextView)view.findViewById(R.id.tv_dianya);
        rl_prgbar_ing=(RelativeLayout)view.findViewById(R.id.rl_prgbar_ing);
        img_check_dianya=(ImageView)view.findViewById(R.id.img_check_dianya);
        tv_dy_status=(TextView)view.findViewById(R.id.tv_dy_status);
        rl_prgbar_ing.setVisibility(View.GONE);
        img_check_dianya.setVisibility(View.GONE);

        img_network=(ImageView)view.findViewById(R.id.img_network);       //无线网络状态
        tv_network_bg=(TextView)view.findViewById(R.id.tv_network_bg);
        rl_network_probar=(RelativeLayout)view.findViewById(R.id.rl_network_probar);
        img_net_status=(ImageView)view.findViewById(R.id.img_net_status);
        tv_sim=(TextView)view.findViewById(R.id.tv_sim);
        rl_network_probar.setVisibility(View.GONE);
        img_net_status.setVisibility(View.GONE);
        //        tv_net_status=(TextView)view.findViewById(R.id.tv_net_status);
        tv_signal_status=(TextView)view.findViewById(R.id.tv_signal_status);
        //        tv_tcp_status=(TextView)view.findViewById(R.id.tv_tcp_status);


        img_gps=(ImageView)view.findViewById(R.id.img_gps);       //GPS
        tv_gps_bg=(TextView)view.findViewById(R.id.tv_gps_bg);
        rl_gps_checking=(RelativeLayout)view.findViewById(R.id.rl_gps_checking);
        img_check_end=(ImageView)view.findViewById(R.id.img_check_end);
        tv_stars_number=(TextView)view.findViewById(R.id.tv_stars_number);
        tv_location_status=(TextView)view.findViewById(R.id.tv_location_status);
        tv_tianxian_status=(TextView)view.findViewById(R.id.tv_tianxian_status);
        rl_gps_checking.setVisibility(View.GONE);
        img_check_end.setVisibility(View.GONE);

        ll_stars=(LinearLayout)view.findViewById(R.id.ll_stars);
        ll_stars.setVisibility(View.GONE);

        ll_gps_date=(LinearLayout)view.findViewById(R.id.ll_gps_date); //GPS时间
        tv_gps_time=(TextView)view.findViewById(R.id.tv_gps_time);

        img_cjq=(ImageView)view.findViewById(R.id.img_cjq);       //采集器/线材
        tv_cjq=(TextView)view.findViewById(R.id.tv_cjq);
        rl_cjq_pbar=(RelativeLayout)view.findViewById(R.id.rl_cjq_pbar);
        img_cjq_status=(ImageView)view.findViewById(R.id.img_cjq_status);
        tv_xcai_tag=(TextView)view.findViewById(R.id.tv_xcai_tag);
        tv_cjq_status=(TextView)view.findViewById(R.id.tv_cjq_status);
        rl_cjq_pbar.setVisibility(View.GONE);
        img_cjq_status.setVisibility(View.GONE);

        img_chuangan=(ImageView)view.findViewById(R.id.img_chuangan);       //传感器
        tv_chuangan=(TextView)view.findViewById(R.id.tv_chuangan);
        rl_chuangan_probar=(RelativeLayout)view.findViewById(R.id.rl_chuangan_probar);
        img_chuangan_staus=(ImageView)view.findViewById(R.id.img_chuangan_staus);
        tv_chuangan_xian=(TextView)view.findViewById(R.id.tv_chuangan_xian);
        rl_chuangan_probar.setVisibility(View.GONE);
        img_chuangan_staus.setVisibility(View.GONE);
        tv_pass_status_1=(TextView)view.findViewById(R.id.tv_pass_status_1);
        tv_pass_status_2=(TextView)view.findViewById(R.id.tv_pass_status_2);
        tv_pass_status_3=(TextView)view.findViewById(R.id.tv_pass_status_3);
        tv_pass_status_4=(TextView)view.findViewById(R.id.tv_pass_status_4);
        tv_pass_status_5=(TextView)view.findViewById(R.id.tv_pass_status_5);
        tv_pass_status_6=(TextView)view.findViewById(R.id.tv_pass_status_6);
        tv_pass_status_7=(TextView)view.findViewById(R.id.tv_pass_status_7);
        tv_pass_status_8=(TextView)view.findViewById(R.id.tv_pass_status_8);
        tv_pass_status_9=(TextView)view.findViewById(R.id.tv_pass_status_9);
        tv_pass_status_10=(TextView)view.findViewById(R.id.tv_pass_status_10);
        tv_pass_status_11=(TextView)view.findViewById(R.id.tv_pass_status_11);
        tv_pass_status_12=(TextView)view.findViewById(R.id.tv_pass_status_12);
        tv_pass_status_13=(TextView)view.findViewById(R.id.tv_pass_status_13);
        tv_pass_status_14=(TextView)view.findViewById(R.id.tv_pass_status_14);
        tv_pass_status_15=(TextView)view.findViewById(R.id.tv_pass_status_15);
        tv_pass_status_16=(TextView)view.findViewById(R.id.tv_pass_status_16);
        ll_sensor=(LinearLayout)view.findViewById(R.id.ll_sensor);
        ll_sensor.setVisibility(View.GONE);
        tv_uninstall_sensior=(TextView)view.findViewById(R.id.tv_uninstall_sensior);
        tv_uninstall_sensior.setVisibility(View.GONE);


        ll_sensor_v47=(LinearLayout)view.findViewById(R.id.ll_sensor_v47);
        tv_v47_status_1=(TextView)view.findViewById(R.id.tv_v47_status_1);
        tv_v47_status_2=(TextView)view.findViewById(R.id.tv_v47_status_2);
        tv_v47_status_3=(TextView)view.findViewById(R.id.tv_v47_status_3);
        tv_v47_status_4=(TextView)view.findViewById(R.id.tv_v47_status_4);
        tv_v47_status_5=(TextView)view.findViewById(R.id.tv_v47_status_5);
        tv_v47_status_6=(TextView)view.findViewById(R.id.tv_v47_status_6);
        tv_v47_status_7=(TextView)view.findViewById(R.id.tv_v47_status_7);
        tv_v47_status_8=(TextView)view.findViewById(R.id.tv_v47_status_8);
        tv_v47_status_9=(TextView)view.findViewById(R.id.tv_v47_status_9);
        tv_v47_status_10=(TextView)view.findViewById(R.id.tv_v47_status_10);
        tv_v47_status_11=(TextView)view.findViewById(R.id.tv_v47_status_11);
        tv_v47_status_12=(TextView)view.findViewById(R.id.tv_v47_status_12);
        tv_v47_status_13=(TextView)view.findViewById(R.id.tv_v47_status_13);
        tv_v47_status_14=(TextView)view.findViewById(R.id.tv_v47_status_14);
        tv_v47_status_15=(TextView)view.findViewById(R.id.tv_v47_status_15);
        tv_v47_status_16=(TextView)view.findViewById(R.id.tv_v47_status_16);

        ll_gudie_advise=(LinearLayout)view.findViewById(R.id.ll_gudie_advise);
        ll_gudie_advise.setVisibility(View.GONE);
        tv_dy_1=(TextView)view.findViewById(R.id.tv_dy_1);
        tv_gprs_2=(TextView)view.findViewById(R.id.tv_gprs_2);
        tv_colletion_3=(TextView)view.findViewById(R.id.tv_colletion_3);
        tv_sensor_4=(TextView)view.findViewById(R.id.tv_sensor_4);


        tv_check_result=(TextView)view.findViewById(R.id.tv_check_result);
        tv_check_result.setVisibility(View.GONE);


        img_inputfra=(ImageView)view.findViewById(R.id.img_inputfra);             //允许进厂
        tv_input_cang=(TextView)view.findViewById(R.id.tv_input_cang);
        rl_input_chang=(RelativeLayout)view.findViewById(R.id.rl_input_chang);
        img_input_chang=(ImageView)view.findViewById(R.id.img_input_chang);
        tv_cause_content=(TextView)view.findViewById(R.id.tv_cause_content);
        tv_measure_content=(TextView)view.findViewById(R.id.tv_measure_content);
        rl_input_chang.setVisibility(View.GONE);
        img_input_chang.setVisibility(View.GONE);

        //        bt_check_again=(Button)view.findViewById(R.id.bt_check_again);
        //        bt_check_again.setVisibility(View.GONE);
        //        bt_check_again.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                initData();
        //            }
        //        });


    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }

    @Override
    public void doInfo(CarNumberInfo companyResult) {
        mInfo=companyResult;
        mHandler.sendEmptyMessage(1);

        String count="huxinzhao"; //去检查gprs是否在线
        String psw="hd123456";
        submit(count,psw);

        if(companyResult !=null){
            try{
                List<String> sensorList=companyResult.getActiveChannels(); //设备存在多少通道
                if(sensorList !=null && sensorList.size() >0){
                    sensorHaveList=sensorList;
                    DLog.e("AutoCheckAct","doInfo="+sensorList.size()+"/"+ Arrays.asList(sensorHaveList));
                }else{
                    sensorHaveList=new ArrayList<>();
                }
            }catch (Exception e){
                sensorHaveList=new ArrayList<>();
            }
        }
    }

    @Override
    public void doError(String msg) {

    }

    @Override
    public void doLogin(UserResultBean bean) {
        UserResultBean userBean=bean;
        String token=bean.getToken();
        acache.put("login_token",token);
        doCheckID(mdevId);  //判断是否在线

        //        doGetDataFragory(token);//获取是否可以进厂数据
    }

    @Override
    public void doLoginFail(String bean) {

    }

    @Override
    public void onlineSuccess(String msg) {
        String line="不在线";
        if("1".equals(msg)){ //正在运行
            line="在线";
        }else{  //停止运行
            line="不在线";
        }
        tv_gprs_online.setText(line);
    }

    @Override
    public void onlineFail(String msg) {
        showTips(msg);
    }

    @Override
    public void doSennorInfo(final SensorInfo info) {
        if(info !=null){
            try{
                mHandler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
                        //                        String onlienStatus=info.getOffline();
                        //                        if(!Tools.isEmpty(onlienStatus) && "0".equals(onlienStatus)){
                        //                            tv_online_or.setText("默认");
                        //                        }else if(!Tools.isEmpty(onlienStatus) &&"1".equals(onlienStatus)){
                        //                            tv_online_or.setText("离线");
                        //                        }else{
                        //                            tv_online_or.setText("");
                        //                        }
                        if(!isSenOver){


                            String xiancai=info.getLineBad();//线材损坏标识：0：无，1：损坏
                            if(!Tools.isEmpty(xiancai)  && xiancai.contains("1")){
                                tv_chuangan_xian.setText("损坏");
                            }else{
                                tv_chuangan_xian.setText("无");
                            }

                            List<String>  slist=info.getSensorStatus();//传感器状态：0：传感器好，1：传感器坏，2：不检测
                            StringBuffer sbuff=new StringBuffer();
                            StringBuffer numb1=new StringBuffer();
                            for(int i=0;i<slist.size();i++){
                                String sen=slist.get(i);
                                if(!Tools.isEmpty(sen)){
                                    float senf=Float.parseFloat(sen);
                                    if(senf ==0){
                                        sbuff.append("0");
                                        numb1.append(i+",");
                                    }else if(senf ==1){
                                        sbuff.append("1");
                                        numb1.append(i+",");
                                    }else{
                                        sbuff.append("2");
                                        numb1.append(i+",");
                                    }
                                }
                            }
                            int length=sbuff.length();
                            String numm=numb1.toString();
                            String numbStr=numm.substring(0,numm.length() - 1);
                            doshowSennr(sbuff.toString(),numbStr,length);
                            if("2222222222222222".contains(sbuff)){
                                ll_sensor.setVisibility(View.GONE);
                                tv_uninstall_sensior.setVisibility(View.VISIBLE);
                                mHandler.sendEmptyMessage(10);  //不正常
                            }
                        }
                    }
                });

                if(!isSenOver) {
                    String damageSensor = info.getDamageNum();//损坏通道
                    String xiancai0 = info.getLineBad();//线材损坏标识：0：无，1：损坏
                    if (!Tools.isEmpty(damageSensor) && !Tools.isEmpty(xiancai0)) {
                        float damaf = Float.parseFloat(damageSensor);
                        float xianf = Float.parseFloat(xiancai0);
                        if (damaf == 0 && xianf == 0) {
                            mHandler.sendEmptyMessage(11);  //无线材损坏 无通道损坏 表示传感器良好
                        } else {
                            mHandler.sendEmptyMessage(10);  //不正常
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
    //显示传感器状态
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void doshowSennr4(String str, String numb, int length) {
        if(length >=1){
            ll_sensor_v47.setVisibility(View.VISIBLE);
            String[] k= numb.split(",");
            tv_v47_status_1.setVisibility(View.VISIBLE);
            tv_v47_status_1.setText(k[0]);
            int a1 = Integer.parseInt(str.charAt(0)+"");
            if(a1 ==0){
                tv_v47_status_1.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
            }else if(a1==2){
                tv_v47_status_1.setVisibility(View.GONE);
            }else{
                tv_v47_status_1.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
            }
            if(length >=2){
                tv_v47_status_2.setVisibility(View.VISIBLE);
                tv_v47_status_2.setText(k[1]);
                int a2 = Integer.parseInt(str.charAt(1)+"");
                if(a2 ==0){
                    tv_v47_status_2.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                }else if(a2==2){
                    tv_v47_status_2.setVisibility(View.GONE);
                }else{
                    tv_v47_status_2.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                }

                if(length >=3){
                    tv_v47_status_3.setVisibility(View.VISIBLE);
                    tv_v47_status_3.setText(k[2]);
                    int a3 = Integer.parseInt(str.charAt(2)+"");
                    if(a3 ==0){
                        tv_v47_status_3.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                    }else if(a3==2){
                        tv_v47_status_3.setVisibility(View.GONE);
                    }else{
                        tv_v47_status_3.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                    }
                    if(length >=4){
                        tv_v47_status_4.setVisibility(View.VISIBLE);
                        tv_v47_status_4.setText(k[3]);
                        int a4 = Integer.parseInt(str.charAt(3)+"");
                        if(a4 ==0){
                            tv_v47_status_4.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                        }else if(a4==2){
                            tv_v47_status_4.setVisibility(View.GONE);
                        }else{
                            tv_v47_status_4.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                        }

                        if(length >=5){
                            tv_v47_status_5.setVisibility(View.VISIBLE);
                            tv_v47_status_5.setText(k[4]);
                            int a5 = Integer.parseInt(str.charAt(4)+"");
                            if(a5 ==0){
                                tv_v47_status_5.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                            }else if(a5==2){
                                tv_v47_status_5.setVisibility(View.GONE);
                            }else{
                                tv_v47_status_5.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                            }

                            if(length >=6){
                                tv_v47_status_6.setVisibility(View.VISIBLE);
                                tv_v47_status_6.setText(k[5]);
                                int a6 = Integer.parseInt(str.charAt(5)+"");
                                if(a6 ==0){
                                    tv_v47_status_6.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                }else if(a6==2){
                                    tv_v47_status_6.setVisibility(View.GONE);
                                }else{
                                    tv_v47_status_6.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                }
                                if(length >=7){
                                    tv_v47_status_7.setVisibility(View.VISIBLE);
                                    tv_v47_status_7.setText(k[6]);
                                    int a7 = Integer.parseInt(str.charAt(6)+"");
                                    if(a7 ==0){
                                        tv_v47_status_7.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                    }else if(a7==2){
                                        tv_v47_status_7.setVisibility(View.GONE);
                                    }else{
                                        tv_v47_status_7.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                    }

                                    if(length >=8){
                                        tv_v47_status_8.setVisibility(View.VISIBLE);
                                        tv_v47_status_8.setText(k[7]);
                                        int a8 = Integer.parseInt(str.charAt(7)+"");
                                        if(a8 ==0){
                                            tv_v47_status_8.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                        }else if(a8==2){
                                            tv_v47_status_8.setVisibility(View.GONE);
                                        }else{
                                            tv_v47_status_8.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                        }

                                        if(length >=9){
                                            tv_v47_status_9.setVisibility(View.VISIBLE);
                                            tv_v47_status_9.setText(k[8]);
                                            int a9 = Integer.parseInt(str.charAt(8)+"");
                                            if(a9 ==0){
                                                tv_v47_status_9.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                            }else if(a9==2){
                                                tv_v47_status_9.setVisibility(View.GONE);
                                            }else{
                                                tv_v47_status_9.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                            }

                                            if(length >=10){
                                                tv_v47_status_10.setVisibility(View.VISIBLE);
                                                tv_v47_status_10.setText(k[9]);
                                                int a10 = Integer.parseInt(str.charAt(9)+"");
                                                if(a10 ==0){
                                                    tv_v47_status_10.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                }else if(a10==2){
                                                    tv_v47_status_10.setVisibility(View.GONE);
                                                }else{
                                                    tv_v47_status_10.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                }
                                                if(length >=11){
                                                    tv_v47_status_11.setVisibility(View.VISIBLE);
                                                    tv_v47_status_11.setText(k[10]);
                                                    int a11 = Integer.parseInt(str.charAt(10)+"");
                                                    if(a11 ==0){
                                                        tv_v47_status_11.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                    }else if(a11==2){
                                                        tv_v47_status_11.setVisibility(View.GONE);
                                                    }else{
                                                        tv_v47_status_11.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                    }
                                                    if(length >=12){
                                                        tv_v47_status_12.setVisibility(View.VISIBLE);
                                                        tv_v47_status_12.setText(k[11]);
                                                        int a12 = Integer.parseInt(str.charAt(11)+"");
                                                        if(a12 ==0){
                                                            tv_v47_status_12.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                        }else if(a12==2){
                                                            tv_v47_status_12.setVisibility(View.GONE);
                                                        }else{
                                                            tv_v47_status_12.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                        }
                                                        if(length >=13){
                                                            tv_v47_status_13.setVisibility(View.VISIBLE);
                                                            tv_v47_status_13.setText(k[12]);
                                                            int a13 = Integer.parseInt(str.charAt(12)+"");
                                                            if(a13 ==0){
                                                                tv_v47_status_13.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                            }else if(a13==2){
                                                                tv_v47_status_13.setVisibility(View.GONE);
                                                            }else{
                                                                tv_v47_status_13.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                            }

                                                            if(length >=14){
                                                                tv_v47_status_14.setVisibility(View.VISIBLE);
                                                                tv_v47_status_14.setText(k[13]);
                                                                int a14 = Integer.parseInt(str.charAt(13)+"");
                                                                if(a14 ==0){
                                                                    tv_v47_status_14.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                                }else if(a14==2){
                                                                    tv_v47_status_14.setVisibility(View.GONE);
                                                                }else{
                                                                    tv_v47_status_14.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                                }
                                                                if(length >=15){
                                                                    tv_v47_status_15.setVisibility(View.VISIBLE);
                                                                    tv_v47_status_15.setText(k[14]);
                                                                    int a15 = Integer.parseInt(str.charAt(14)+"");
                                                                    if(a15 ==0){
                                                                        tv_v47_status_15.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                                    }else if(a15==2){
                                                                        tv_v47_status_15.setVisibility(View.GONE);
                                                                    }else{
                                                                        tv_v47_status_15.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                                    }
                                                                    if(length >=16){
                                                                        tv_v47_status_16.setVisibility(View.VISIBLE);
                                                                        tv_v47_status_16.setText(k[15]);
                                                                        int a16 = Integer.parseInt(str.charAt(15)+"");
                                                                        if(a16 ==0){
                                                                            tv_v47_status_16.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                                        }else if(a16==2){
                                                                            tv_v47_status_16.setVisibility(View.GONE);
                                                                        }else{
                                                                            tv_v47_status_16.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }

                            }

                        }

                    }

                }

            }

        }
    }

    @Override
    public void doSenorError(String msg) {
        showTips(msg);
        //        img_cjq.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
        //        tv_cjq.setBackgroundResource(R.mipmap.icon_data_ing);
        //        rl_chuangan_probar.setVisibility(View.GONE);
        //        img_chuangan_staus.setVisibility(View.GONE);
        mHandler.sendEmptyMessage(10);
    }

    @Override
    public void doFragoryFinish(AutoCheckFrgBean companyResult) {
        if(companyResult !=null){
            mAutoCheckFrgBean=companyResult;
            mHandler.sendEmptyMessage(13);
        }
    }

    @Override
    public void doFragoryError(String msg) {
        showTips(msg);
        img_input_chang.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_un_pass));
        rl_input_chang.setBackgroundResource(R.mipmap.icon_data_ing);
        rl_input_chang.setVisibility(View.GONE);
        img_input_chang.setVisibility(View.GONE);
        tv_cause_content.setText(msg);
        tv_measure_content.setText("");
    }

    //判断车辆是否在线
    private void  doCheckID(String id){
        //        showProgressDialog();
        String   token = acache.getAsString("login_token");
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("deviceId", id);
        mpresenter.checkCarOnline(mapParams);
    }
    //登录
    private void submit(String  count ,String password) {

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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void doshowSennr(String str, String numb, int length) {
        ll_sensor.setVisibility(View.VISIBLE);
        tv_uninstall_sensior.setVisibility(View.GONE);
        String[] k= numb.split(",");
        tv_pass_status_1.setVisibility(View.VISIBLE);
        tv_pass_status_1.setText(k[0]);
        int a1 = Integer.parseInt(str.charAt(0)+"");
        if(a1 ==1){
            tv_pass_status_1.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
        }else if(2==a1){
            tv_pass_status_1.setVisibility(View.GONE);
        }else{
            tv_pass_status_1.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
        }
        if(length >=2){
            tv_pass_status_2.setVisibility(View.VISIBLE);
            tv_pass_status_2.setText(k[1]);
            int a2 = Integer.parseInt(str.charAt(1)+"");
            if(a2 ==1){
                tv_pass_status_2.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
            }else if(2==a2){
                tv_pass_status_2.setVisibility(View.GONE);
            }else{
                tv_pass_status_2.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
            }

            if(length >=3){
                tv_pass_status_3.setVisibility(View.VISIBLE);
                tv_pass_status_3.setText(k[2]);
                int a3 = Integer.parseInt(str.charAt(2)+"");
                if(a3 ==1){
                    tv_pass_status_3.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                }else if(2==a3){
                    tv_pass_status_3.setVisibility(View.GONE);
                }else{
                    tv_pass_status_3.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                }
                if(length >=4){
                    tv_pass_status_4.setVisibility(View.VISIBLE);
                    tv_pass_status_4.setText(k[3]);
                    int a4 = Integer.parseInt(str.charAt(3)+"");
                    if(a4 ==1){
                        tv_pass_status_4.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                    }else if(2==a4){
                        tv_pass_status_4.setVisibility(View.GONE);
                    }else{
                        tv_pass_status_4.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                    }

                    if(length >=5){
                        tv_pass_status_5.setVisibility(View.VISIBLE);
                        tv_pass_status_5.setText(k[4]);
                        int a5 = Integer.parseInt(str.charAt(4)+"");
                        if(a5 ==1){
                            tv_pass_status_5.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                        }else if(2==a5){
                            tv_pass_status_5.setVisibility(View.GONE);
                        }else{
                            tv_pass_status_5.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                        }

                        if(length >=6){
                            tv_pass_status_6.setVisibility(View.VISIBLE);
                            tv_pass_status_6.setText(k[5]);
                            int a6 = Integer.parseInt(str.charAt(5)+"");
                            if(a6 ==1){
                                tv_pass_status_6.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                            }else if(2==a6){
                                tv_pass_status_6.setVisibility(View.GONE);
                            }else{
                                tv_pass_status_6.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                            }
                            if(length >=7){
                                tv_pass_status_7.setVisibility(View.VISIBLE);
                                tv_pass_status_7.setText(k[6]);
                                int a7 = Integer.parseInt(str.charAt(6)+"");
                                if(a7 ==1){
                                    tv_pass_status_7.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                }else if(2==a7){
                                    tv_pass_status_7.setVisibility(View.GONE);
                                }else{
                                    tv_pass_status_7.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                }

                                if(length >=8){
                                    tv_pass_status_8.setVisibility(View.VISIBLE);
                                    tv_pass_status_8.setText(k[7]);
                                    int a8 = Integer.parseInt(str.charAt(7)+"");
                                    if(a8 ==1){
                                        tv_pass_status_8.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                    }else if(2==a8){
                                        tv_pass_status_8.setVisibility(View.GONE);
                                    }else{
                                        tv_pass_status_8.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                    }

                                    if(length >=9){
                                        tv_pass_status_9.setVisibility(View.VISIBLE);
                                        tv_pass_status_9.setText(k[8]);
                                        int a9 = Integer.parseInt(str.charAt(8)+"");
                                        if(a9 ==1){
                                            tv_pass_status_9.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                        }else if(2==a9){
                                            tv_pass_status_9.setVisibility(View.GONE);
                                        }else{
                                            tv_pass_status_9.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                        }

                                        if(length >=10){
                                            tv_pass_status_10.setVisibility(View.VISIBLE);
                                            tv_pass_status_10.setText(k[9]);
                                            int a10 = Integer.parseInt(str.charAt(9)+"");
                                            if(a10 ==1){
                                                tv_pass_status_10.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                            }else if(2==a10){
                                                tv_pass_status_10.setVisibility(View.GONE);
                                            }else{
                                                tv_pass_status_10.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                            }
                                            if(length >=11){
                                                tv_pass_status_11.setVisibility(View.VISIBLE);
                                                tv_pass_status_11.setText(k[10]);
                                                int a11 = Integer.parseInt(str.charAt(10)+"");
                                                if(a11 ==1){
                                                    tv_pass_status_11.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                }else if(2==a11){
                                                    tv_pass_status_11.setVisibility(View.GONE);
                                                }else{
                                                    tv_pass_status_11.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                }
                                                if(length >=12){
                                                    tv_pass_status_12.setVisibility(View.VISIBLE);
                                                    tv_pass_status_12.setText(k[11]);
                                                    int a12 = Integer.parseInt(str.charAt(11)+"");
                                                    if(a12 ==1){
                                                        tv_pass_status_12.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                    }else if(2==a12){
                                                        tv_pass_status_12.setVisibility(View.GONE);
                                                    }else{
                                                        tv_pass_status_12.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                    }
                                                    if(length >=13){
                                                        tv_pass_status_13.setVisibility(View.VISIBLE);
                                                        tv_pass_status_13.setText(k[12]);
                                                        int a13 = Integer.parseInt(str.charAt(12)+"");
                                                        if(a13 ==1){
                                                            tv_pass_status_13.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                        }else if(2==a13){
                                                            tv_pass_status_13.setVisibility(View.GONE);
                                                        }else{
                                                            tv_pass_status_13.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                        }

                                                        if(length >=14){
                                                            tv_pass_status_14.setVisibility(View.VISIBLE);
                                                            tv_pass_status_14.setText(k[13]);
                                                            int a14 = Integer.parseInt(str.charAt(13)+"");
                                                            if(a14 ==1){
                                                                tv_pass_status_14.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                            }else if(2==a14){
                                                                tv_pass_status_14.setVisibility(View.GONE);
                                                            }else{
                                                                tv_pass_status_14.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                            }
                                                            if(length >=15){
                                                                tv_pass_status_15.setVisibility(View.VISIBLE);
                                                                tv_pass_status_15.setText(k[14]);
                                                                int a15 = Integer.parseInt(str.charAt(14)+"");
                                                                if(a15 ==1){
                                                                    tv_pass_status_15.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                                }else if(2==a15){
                                                                    tv_pass_status_15.setVisibility(View.GONE);
                                                                }else{
                                                                    tv_pass_status_15.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                                }
                                                                if(length >=16){
                                                                    tv_pass_status_16.setVisibility(View.VISIBLE);
                                                                    tv_pass_status_16.setText(k[15]);
                                                                    int a16 = Integer.parseInt(str.charAt(15)+"");
                                                                    if(a16 ==1){
                                                                        tv_pass_status_16.setBackground(context.getResources().getDrawable(R.drawable.oval_red));
                                                                    }else if(2==a16){
                                                                        tv_pass_status_16.setVisibility(View.GONE);
                                                                    }else{
                                                                        tv_pass_status_16.setBackground(context.getResources().getDrawable(R.drawable.oval_green));
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                            }

                        }

                    }

                }

            }

        }
    }
    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(action.equals(BleConstant.ACTION_READ_HARD_SOFT_SUSSCESS)){
                    String str=intent.getStringExtra("hard_ware");
                    tv_hardver_dev.setText("("+str+")");

                    if(!Tools.isEmpty(str)){
                        if(str.contains("V4") || str.contains("v4") ||str.contains("4")){
                            tv_cjq.setText("线材/采集器");
                            tv_xcai_tag.setVisibility(View.VISIBLE);
                            tv_cjq_status.setVisibility(View.GONE);
                            ll_sensor_v47.setVisibility(View.GONE);
                        }else{
                            tv_cjq.setText("采集器芯片");
                            tv_xcai_tag.setVisibility(View.GONE);
                            tv_cjq_status.setVisibility(View.VISIBLE);
                            ll_sensor_v47.setVisibility(View.VISIBLE);
                        }

                    }else{
                        tv_cjq.setText("采集器芯片");
                        tv_xcai_tag.setVisibility(View.GONE);
                        tv_cjq_status.setVisibility(View.VISIBLE);
                        ll_sensor_v47.setVisibility(View.VISIBLE);
                    }

                    //                    Intent i2=new Intent(BleConstant.ACTION_READ_SOFT);
                    //                    int id2=Integer.parseInt(mdevId);
                    //                    i2.putExtra("n_id2",id2);
                    //                    sendBroadcast(i2);
                }else if(action.equals(BleConstant.ACTION_READ_SOFT_SUSSCESS)){
                    String str1=intent.getStringExtra("soft_ware");
                    tv_softver_dev.setText("("+str1+")");
                }else if(action.equals(BleConstant.ACTION_AUTO_DIANYA)){  //外界电源电压值,0~36V, 小于10.0V判断为断电
                    if(!isDyOver){
                        bacache.put("ble_fa","2");
                        String dianya=intent.getStringExtra("li_volate");
                        tv_dy_status.setText(dianya);
                        float volate=Float.parseFloat(dianya);
                        if(volate < 10){
                            mHandler.sendEmptyMessage(2); //断电
                        }else{
                            mHandler.sendEmptyMessage(3); //正常
                        }
                        DLog.e(TAG,"当前电池电压是="+dianya);
                        bacache.put("dian_ya",dianya);
                    }
                }
                else if(action.equals(BleConstant.ACTION_GPRSSIR)) {  //GPRSsir
                    if(!isGprsOver){

                        String gprsRssi=intent.getStringExtra("gps_rssi");  //	< 15 为弱  15 ~ 24 为中  	> 24 为强

                        if(!Tools.isEmpty(gprsRssi)){
                            float rssi=Float.parseFloat(gprsRssi);
                            DLog.e(TAG,"当前GPRS信号强度是="+rssi);
                            if(rssi < 15){
                                tv_signal_status.setText("弱"+"("+rssi+")");     //GPRSrssi信号强度值0~31,<15认为信号强度弱
                            }else if(rssi >= 15 && rssi <= 24){
                                tv_signal_status.setText("中"+"("+rssi+")");
                            }else{
                                tv_signal_status.setText("强"+"("+rssi+")");     //GPRSrssi信号强度值0~31,<15认为信号强度弱
                            }

                        }
                    }

                }
                else if(action.equals(BleConstant.ACTION_GPRS)){  //GPRS
                    if(!isGprsOver){

                        GPRSbean gprs=(GPRSbean)intent.getSerializableExtra("gprs_status");
                        if(gprs !=null){


                            String gprsStr=gprs.getNetStatus();
                            float gprin=Float.parseFloat(gprsStr);
                            DLog.e(TAG,"当前GPRS状态是="+gprin);
                            //0:初始化，
                            //1：已连接连接服务器（在线）
                            //2：AT串口错误
                            //3：模块未识别
                            //4：SIM卡未插入或未识别
                            //5：GPRS网络信号强度过低
                            //6：SIM卡注册失败，SIM卡已失效
                            //7：GPRS网络注册失败，可能欠费
                            //8：GPRSPPP连接失败
                            //9：与服务器建立TCP连接失败

                            if(gprin ==1){
                                mHandler.sendEmptyMessage(5); //正常
                                tv_sim.setText("已连接连接服务器（在线）");
                            }else if( gprin >1){
                                mHandler.sendEmptyMessage(4); //异常
                                if(gprin ==2){
                                    tv_sim.setText("AT串口错误");
                                }else if(gprin==3){
                                    tv_sim.setText("模块未识别");
                                }else if(gprin==4){
                                    tv_sim.setText("SIM卡未插入或未识别");
                                }else if(gprin==5){
                                    tv_sim.setText("GPRS网络信号强度过低");
                                }else if(gprin==6){
                                    tv_sim.setText("SIM卡注册失败，SIM卡已失效");
                                }else if(gprin==7){
                                    tv_sim.setText("GPRS网络注册失败，可能欠费");
                                }else if(gprin==8){
                                    tv_sim.setText("GPRSPPP连接失败");
                                }else if(gprin==9){
                                    tv_sim.setText("与服务器建立TCP连接失败");
                                }

                            }
                        }
                    }
                }else if(action.equals(BleConstant.ACTION_GPS_CHECK_LOC)){ //new 3/21

                    if(!isGpsOver){

                        GPSbean gpscheck=(GPSbean)intent.getSerializableExtra("gps_location");
                        try{

                            if(gpscheck !=null){

                                //                            tv_stars_number.setVisibility(View.VISIBLE);
                                //                            String starsNum=gpscheck.getStarsNumber();//卫星数量
                                //                            if(!Tools.isEmpty(starsNum)  && 0 !=Float.parseFloat(starsNum)){
                                //
                                //                                tv_stars_number.setText(starsNum);
                                //                                String locationStatus = gpscheck.getLocationStatus();//定位状态 1 正常
                                //                                DLog.e(TAG, "当前GPS定位状态=" + locationStatus);
                                //                                if (!Tools.isEmpty(locationStatus) && locationStatus.contains("1")) {
                                //                                    mHandler.sendEmptyMessage(7); //正常
                                //                                    tv_location_status.setText("已定位");
                                //                                } else {
                                //                                    mHandler.sendEmptyMessage(6); //不正常
                                //                                    tv_location_status.setText("无定位");
                                //                                }
                                //
                                //                            }else{
                                //                                mHandler.sendEmptyMessage(6); //不正常
                                //                                tv_stars_number.setText("0");
                                //                            }
                                String locationStatus = gpscheck.getLocationStatus();//定位状态 1 正常
                                DLog.e(TAG, "当前GPS定位状态=" + locationStatus);
                                if (!Tools.isEmpty(locationStatus) && locationStatus.contains("1")) {
                                    mHandler.sendEmptyMessage(7); //正常
                                    tv_location_status.setText("已定位");
                                } else {
                                    mHandler.sendEmptyMessage(6); //不正常
                                    tv_location_status.setText("无定位");
                                }

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }


                }
                else if(action.equals(BleConstant.ACTION_BIN_GPS_TIME)){ //-
                    if(!isGpsOver){

                        GPSTimebean gpsTiem=(GPSTimebean)intent.getSerializableExtra("gps_time");
                        String weitime=gpsTiem.getweightTime();
                        String gpTime=gpsTiem.getgpsTime();
                        long weiTime=0;
                        long gpst=0;
                        long cha=0;
                        if(!Tools.isEmpty(weitime) && !Tools.isEmpty(gpTime)){
                            weiTime= Long.valueOf(weitime)*1000L;
                            gpst= Long.valueOf(gpTime)*1000L;
                            cha=Math.abs(weiTime-gpst); //大于30分钟 GPS时间标红  GPS 异常

                        }

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date= dateFormat.format(new Date(gpst));
                        tv_gps_time.setText(date+"");
                        if(cha > 30*60*1000){
                            ll_gps_date.setVisibility(View.VISIBLE);
                        }else{
                            ll_gps_date.setVisibility(View.GONE);
                        }
                    }
                }
                else if(action.equals(BleConstant.ACTION_GPS_CHECK)){
                    if(!isGpsOver) {
                        String gpscheck = intent.getStringExtra("check_gps");
                        if (!Tools.isEmpty(gpscheck)) {
                            //                        String locationStatus=gpscheck.getLocationStatus();//定位状态 1 正常
                            //                        DLog.e(TAG,"当前GPS定位状态="+locationStatus);
                            //                        if(!Tools.isEmpty(locationStatus) && locationStatus.contains("1")){
                            //                            mHandler.sendEmptyMessage(7); //正常
                            //                            tv_location_status.setText("已定位");
                            //                        }else{
                            //                            mHandler.sendEmptyMessage(6); //不正常
                            //                            tv_location_status.setText("无定位");
                            //                        }
                            //
                            //                        String starsNum=gpscheck.getStarsNumber();
                            //                        if(!Tools.isEmpty(starsNum)){
                            //                            tv_stars_number.setText(starsNum);
                            //                        }else{
                            //                            tv_stars_number.setText("0");
                            //                        }
                            //天线状态 0：未知，1：正常, 2:开路，3:短路，其他值：参数错误
                            String tianxian = gpscheck; //位值0：异常1:正常
                            DLog.e(TAG, "当前天线状态=" + tianxian);
                            if (Tools.isEmpty(tianxian)) {
                                tv_tianxian_status.setText("异常");
                            } else {
                                float ft = Float.parseFloat(tianxian);
                                if (ft == 1) {
                                    tv_tianxian_status.setText("正常");
                                } else if (ft == 0) {
                                    tv_tianxian_status.setText("未知");
                                } else if (ft == 2) {
                                    tv_tianxian_status.setText("开路");
                                } else if (ft == 3) {
                                    tv_tianxian_status.setText("短路");
                                } else {
                                    tv_tianxian_status.setText("参数错误");
                                }
                            }
                        }
                    }
                }
                else if(action.equals(BleConstant.ACTION_CHUAN_GAN_QI)){ //-	1：传感器正常 0不正常
                    if(!isConllOver){
                        try{
                            String sensor=intent.getStringExtra("senor_status");//0110000000000000
                            DLog.e(TAG,"当前传感器状态="+sensor);
                            if(!Tools.isEmpty(sensor)  && !sensor.contains("0")){
                                mHandler.sendEmptyMessage(9);
                            }else{
                                mHandler.sendEmptyMessage(8);
                            }

                            int leng=16;
                            String strb="1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16";
                            String value="";
                            StringBuilder senValue = new StringBuilder("2222222222222222");
                            if(sensorHaveList !=null && sensorHaveList.size() >0){
                                for(int i=0;i<sensorHaveList.size();i++){
                                    int index=Integer.parseInt(sensorHaveList.get(i));
                                    String status= String.valueOf(sensor.charAt(index-1));
                                    senValue.replace(index-1,index,status);
                                }
                                value=senValue.toString();
                            }else{
                                value=sensor;
                            }
                            DLog.e("ACTION_CHUAN_GAN_QI","sensorHaveList="+leng+"/"+senValue);
                            doshowSennr4(value,strb,leng);                     //     2/2,3

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                else if(action.equals(BleConstant.ACTION_CAI_JI_QI)){
                    if(!isConllOver) {
                        String colection = intent.getStringExtra("colection_status");
                        DLog.e(TAG, "当前采集器状态=" + colection);
                        if (!Tools.isEmpty(colection) && !colection.contains("0")) {
                            mHandler.sendEmptyMessage(9);
                        } else {
                            mHandler.sendEmptyMessage(8);
                        }

                        String[] cstr = colection.split(";");
                        String one = cstr[0];
                        StringBuffer sbuff = new StringBuffer();
                        if (Tools.isEmpty(one) || "0".equals(one)) {
                            sbuff.append("第一采集器:通道数" + "0" + "异常;");
                        } else {
                            sbuff.append("第一采集器:通道数" + one + "正常;");
                        }
                        String two = cstr[1];
                        if (Tools.isEmpty(two) || "0".equals(two)) {
                            sbuff.append("第二采集器:通道数" + "0" + "异常");
                        } else {
                            sbuff.append("第二采集器:通道数" + two + "正常");
                        }
                        //                    String three=cstr[2];
                        //                    String four=cstr[3];
                        tv_xcai_tag.setText(sbuff.toString());
                    }
                }else if(action.equals(BleConstant.ACTION_RESTART_AUTO_CHECK)){
                    String tag=intent.getStringExtra("page_tag");
                    if("0".equals(tag)){
                        tv_next_check.performClick();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTION_READ_HARD_SOFT_SUSSCESS);
        filter.addAction(BleConstant.ACTION_READ_SOFT_SUSSCESS);
        filter.addAction(BleConstant.ACTION_AUTO_DIANYA);
        filter.addAction(BleConstant.ACTION_GPRS);
        filter.addAction(BleConstant.ACTION_GPRSSIR);
        filter.addAction(BleConstant.ACTION_BIN_GPS_TIME);
        filter.addAction(BleConstant.ACTION_GPS_CHECK);
        filter.addAction(BleConstant.ACTION_CHUAN_GAN_QI);
        filter.addAction(BleConstant.ACTION_CAI_JI_QI);
        filter.addAction(BleConstant.ACTION_GPS_CHECK_LOC);
        filter.addAction(BleConstant.ACTION_RESTART_AUTO_CHECK);
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(timer !=null){
            timer.cancel();
            timer=null;
        }
        bacache.put("ble_fa","0");
    }

    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }
}