package cn.hand.tech.ui.setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.setting.adapter.GuideAdapter;
import cn.hand.tech.ui.setting.adapter.NearMeAdapter;
import cn.hand.tech.ui.setting.adapter.RepairAdapter;
import cn.hand.tech.ui.setting.adapter.VctionAdapter;
import cn.hand.tech.ui.setting.bean.A1;
import cn.hand.tech.ui.setting.bean.OnLineTruckBean;
import cn.hand.tech.ui.setting.bean.RepairModel;
import cn.hand.tech.ui.setting.presenter.RepairPresenter;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.bean.InstallerInfo;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.Aes;
import cn.hand.tech.utils.DataUtil;
import cn.hand.tech.utils.LatLng;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.CustomDialog;
import cn.hand.tech.weiget.listview.XListView;

/*
 *维修记录
 */
public class RepairAct extends Activity implements IRepairView,RepairAdapter.onNextListener,GuideAdapter.onGuideListener,XListView.IXListViewListener,NearMeAdapter.onPhoneLisenter{
    private Context context;
    private ACache acache;
    private TextView tv_car_number;
    private Button bt_search;
    private List<String> vlist=new ArrayList<>();

    private AlertDialog adddialog;
    private String psw;
    private static final String PASSWORD_STRING = "6a9fd0d1a950420b812f27e970afcd8f"; // static final 静态常量
    private RepairPresenter mpresenter;
    private String token;
    private ListView list_viction;
    private VctionAdapter vadapter;
    private GuideAdapter  gadapter;
    private ListView list_guide;
    private LinearLayout ll_search;
    private LinearLayout dialog_truckN_input;
    private InputView mInputView;
    private EditText et_input;
    private TextView tv_other;
    private TextView tv_okTruckN;
    private PopupKeyboard mPopupKeyboard;
    private String truckNum;
    private LinearLayout ll_content;
    private String carNumber;
    private List<RepairModel> dlist;
    private BroadcastReceiver receiver;
    private  List<A1> gmlist=new ArrayList<>();
    private boolean isConnected;
    private String current_truckNum;
    private RelativeLayout rl_0;
    private TextView tv_company_name;
    private TextView tv_line0;
    private LinearLayout ll_tel;
    private XListView list_nearto_me;
    private NearMeAdapter madapter; //离我最近

    private List<RepairModel> nearList=new ArrayList<>();
    private List<RepairModel> reList=new ArrayList<>();
    private CustomDialog dialog;
    private LinearLayout ll_2;
    private List<RepairModel> plist=new ArrayList<>();
    private TextView title_near;
    private LinearLayout ll_near;
    private MyPhoneListener myPhoneListener;
    TelephonyManager tm;
    private boolean isCall;
    private long firstCallTime,callTime;
    int costTime;
    private AlertDialog tempDialog;
    private String mId;
    private TextView tv_repair_add;
    private TextView tv_cancelTruckN;
    private boolean isother;

    public static void start(Context context) {
        Intent intent = new Intent(context, RepairAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_repair);
        acache= ACache.get(context,"WeightFragment");
        mpresenter = new RepairPresenter(context, this);
        String  isCon=acache.getAsString("is_connect");
        if("2".equals(isCon)){
            isConnected=true;
        }else{
            isConnected=false;
        }
        current_truckNum=acache.getAsString("car_num");
        initView();
        doAddTruck();
        acache.put("truck_number","");


        tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        myPhoneListener = new MyPhoneListener();
        tm.listen(myPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }



    private void initView() {
        LinearLayout   ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_content=(LinearLayout)findViewById(R.id.ll_content);

        rl_0=(RelativeLayout)findViewById(R.id.rl_0);
        tv_company_name=(TextView)findViewById(R.id.tv_company_name);
        tv_company_name.setVisibility(View.GONE);
        rl_0.setOnClickListener(new View.OnClickListener() {//公司名称
            @Override
            public void onClick(View v) {
                CompanyTruckForRepairActivity.start(context);
            }
        });

        tv_car_number=(TextView)findViewById(R.id.tv_car_number);
        tv_car_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_truckN_input.setVisibility(View.VISIBLE);
                mPopupKeyboard.show((RepairAct)context);
                // 加上这句才会弹出键盘
                mInputView.performFirstFieldView();
                ll_content.setVisibility(View.GONE);
                tv_company_name.setVisibility(View.GONE);
            }
        });

        bt_search=(Button)findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carNumber=tv_car_number.getText().toString();
                if(Tools.isEmpty(carNumber)){
                    showTips("车牌不能为空");
                    return;
                }

                mpresenter.haveRepairInformation(carNumber,token);
            }
        });

        tv_repair_add=(TextView)findViewById(R.id.tv_repair_add); //新增维修记录
        tv_repair_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepairAct.this, AddRepairAct.class);
                startActivity(intent);
            }
        });

        title_near=(TextView)findViewById(R.id.title_near);
        title_near.setVisibility(View.GONE);
        title_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_2.setVisibility(View.VISIBLE);
                ll_tel.setVisibility(View.GONE);
            }
        });

        tv_line0=(TextView)findViewById(R.id.tv_line0);
        ll_tel=(LinearLayout)findViewById(R.id.ll_tel);
        ll_tel.setVisibility(View.GONE);
        ll_near=(LinearLayout)findViewById(R.id.ll_near);
        ll_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_2.setVisibility(View.VISIBLE);
                ll_tel.setVisibility(View.GONE);
            }
        });

        ll_search=(LinearLayout)findViewById(R.id.ll_search);
        ll_search.setVisibility(View.GONE);

        list_viction=(ListView)findViewById(R.id.list_viction);
        list_viction.setVisibility(View.VISIBLE);
        vadapter = new VctionAdapter(context, vlist);
        list_viction.setAdapter(vadapter);
        list_viction.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                list_viction.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        list_guide=(ListView)findViewById(R.id.list_guide);
        gadapter = new GuideAdapter(context, gmlist);
        list_guide.setAdapter(gadapter);
        list_guide.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                list_guide.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        list_nearto_me=(XListView)findViewById(R.id.list_nearto_me);
        list_nearto_me.setPullRefreshEnable(true);
        list_nearto_me.setPullLoadEnable(false);
        list_nearto_me.setXListViewListener(this);
        list_nearto_me.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RepairModel bean=plist.get(position-1);
                String carNumber=bean.getCarNumber();
                tv_car_number.setText(carNumber);
                acache.put("truck_number",carNumber);
                truckNum=carNumber;
                bt_search.performClick();
            }
        });

        madapter=new NearMeAdapter(context,nearList);
        list_nearto_me.setAdapter(madapter);
        ll_2=(LinearLayout)findViewById(R.id.ll_2);
        ll_2.setVisibility(View.VISIBLE);

        dialog_truckN_input = (LinearLayout)findViewById(R.id.dialog_truckN_input);//车辆信息键盘
        dialog_truckN_input.setVisibility(View.GONE);
        mInputView = (InputView)findViewById(R.id.input_view);
        et_input=(EditText)findViewById(R.id.et_input);
        mInputView.setVisibility(View.VISIBLE);
        et_input.setVisibility(View.GONE);
        tv_other=(TextView)findViewById(R.id.tv_other);
        tv_other.setText("其他");
        tv_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isother){
                    et_input.setVisibility(View.VISIBLE);
                    mInputView.setVisibility(View.GONE);
                    tv_other.setText("国内");
                    isother=true;
                    showSoft(et_input);
                    if (mPopupKeyboard.isShown()) {
                        mPopupKeyboard.dismiss(RepairAct.this);
                    }
                }else{
                    et_input.setVisibility(View.GONE);
                    mInputView.setVisibility(View.VISIBLE);
                    tv_other.setText("其他");
                    isother=false;
                    hideSoft(et_input);
                    if(!mPopupKeyboard.isShown()){
                        mPopupKeyboard.show(RepairAct.this);
                    }
                }
            }
        });
        tv_other.setVisibility(View.VISIBLE);
        tv_cancelTruckN=(TextView)findViewById(R.id.tv_cancelTruckN);
        tv_cancelTruckN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_truckN_input.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
                if (mPopupKeyboard.isShown()) {
                    mPopupKeyboard.dismiss(RepairAct.this);
                }
                hideSoft(et_input);
            }
        });
        tv_okTruckN = (TextView)findViewById(R.id.tv_okTruckN);
        tv_okTruckN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isother){
                    String etNumber=mInputView.getNumber().toString().trim();
                    if (TextUtils.isEmpty(etNumber)) {
                        showTips("车牌号码不能为空");
                        return;
                    } else if (etNumber.length() < 7) {
                        showTips("请输入正确的车牌号码");
                        return;
                    }
                    truckNum=etNumber;
                    tv_car_number.setText(truckNum);
                    if (mPopupKeyboard.isShown()) {
                        mPopupKeyboard.dismiss((RepairAct)context);
                    }
                    hideSoft(et_input);
                    dialog_truckN_input.setVisibility(View.GONE);
                    ll_content.setVisibility(View.VISIBLE);

                    String carNum=acache.getAsString("truck_number");
                    if(etNumber.equals(carNum)){
                        ll_search.setVisibility(View.VISIBLE);
                    }else{
                        ll_search.setVisibility(View.GONE);
                    }
                    acache.put("truck_number",etNumber);
                }else{
                    String etNumb1=et_input.getText().toString();
                    if (TextUtils.isEmpty(etNumb1)) {
                        showTips("车牌号码不能为空");
                        return;
                    } else if (etNumb1.length() < 7) {
                        showTips("请输入正确的车牌号码");
                        return;
                    }

                    truckNum=etNumb1;
                    tv_car_number.setText(truckNum);
                    if (mPopupKeyboard.isShown()) {
                        mPopupKeyboard.dismiss((RepairAct)context);
                    }
                    hideSoft(et_input);
                    dialog_truckN_input.setVisibility(View.GONE);
                    ll_content.setVisibility(View.VISIBLE);

                    String carNum=acache.getAsString("truck_number");
                    if(etNumb1.equals(carNum)){
                        ll_search.setVisibility(View.VISIBLE);
                    }else{
                        ll_search.setVisibility(View.GONE);
                    }
                    acache.put("truck_number",etNumb1);
                }
            }
        });
        //        tv_other.setOnClickListener();

        mPopupKeyboard = new PopupKeyboard(this);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(mInputView, this);
    }

    /*获取公司列表信息*/
    private void initCompanyList() {
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("version", "2");
        mpresenter.getCompanyList(mapParams);

    }

    public void showSoft(View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver !=null){
            unregisterReceiver(receiver);
        }
        //取消电话监听
        if (tm != null&&myPhoneListener!=null) {
            tm.listen(myPhoneListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(intent !=null){
            Bundle bundle =intent.getExtras();
            if(bundle !=null){
                String name= bundle.getString("company_name");
                List<RepairModel> rlist= (List<RepairModel>)bundle.getSerializable("need_repair");
                if(dlist !=null && dlist.size() >0){
                    dlist.clear();
                }
                dlist=rlist;
                doHandlerRepairInfo(rlist);
                tv_company_name.setText(name+"");
                tv_company_name.setVisibility(View.VISIBLE);
                if(rlist !=null && rlist.size() >0){
                    RepairModel rml=rlist.get(0);
                    String number =rml.getCarNumber()+"";
                    tv_car_number.setText(number);
                    truckNum=number;
                }
                ll_2.setVisibility(View.GONE);
            }
        }
    }

    private void doAddTruck() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_for_login, null);
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
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddialog.cancel();
                finish();
            }
        });
        TextView tv_login = (TextView) view.findViewById(R.id.tv_login);//登录
        tv_login.setOnClickListener(new View.OnClickListener() {
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

    //开始登录
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
    /**
     * 关闭进度对话框
     */
    private void closedialog() {
        if (adddialog != null) {
            adddialog.dismiss();
            adddialog = null;
        }
    }

    @Override
    public void doSuccess(List<RepairModel> mlist) {
        if(dlist !=null && dlist.size() >0){
            dlist.clear();
        }
        dlist=mlist;
        doHandlerRepairInfo(mlist);
        ll_2.setVisibility(View.GONE);
    }
    public void doHandlerRepairInfo(List<RepairModel> mlist){
        if(mlist !=null && mlist.size() > 0 ){
            List<String>  faultList=new ArrayList<>();
            List<A1>  guideList=new ArrayList<>();

            for(int i=0;i<mlist.size();i++){
                int number =i+1;
                RepairModel   model=mlist.get(i);
                //                repairId=model.getId();
                String faultName=model.getFaultTypeName();//故障类型名称

                List<String> fList=model.getFaultPhenomenon(); //维修现象
                if(fList !=null && fList.size() >0){
                    StringBuffer faStr=new StringBuffer();
                    for(int j=0;j<fList.size();j++){
                        faStr.append(fList.get(j)+",");
                    }
                    String str=faStr.toString();
                    str= str.substring(0,str.length() - 1);
                    String oneFault=faultName+":"+str;
                    faultList.add(oneFault);
                }else{
                    String oneFault1="";
                    faultList.add(oneFault1);
                }

                StringBuffer onGuide=new StringBuffer();
                List<String> gList=model.getRepairedGuide();  //维修指导
                if(gList !=null && gList.size() > 0){
                    onGuide.append(faultName+":");
                    if(gList !=null && gList.size() > 0){
                        StringBuffer guideStr=new StringBuffer();
                        for(int k=0;k<gList.size();k++){
                            guideStr.append(gList.get(k)+",");
                        }
                        String str1=guideStr.toString();
                        str1= str1.substring(0,str1.length() - 1);
                        onGuide.append(str1);
                    }
                }else{
                    onGuide.append(" ");
                }

                A1 bean=new A1();
                bean.setStr(onGuide.toString());
                bean.setCheck(false);
                guideList.add(bean);

            }

            gmlist=guideList;
            vadapter.updateListView(faultList);
            gadapter.updateListView(guideList,-1);

            ll_search.setVisibility(View.VISIBLE);

            ll_tel.setVisibility(View.VISIBLE);


            hideSoftKeyboard(this);

            //            initData();                      //刷新 更换记录选择
            //            madapter.updateListView(list1);
            //            showChanged();
            //            list_1.setVisibility(View.VISIBLE);
            //            list_2.setVisibility(View.INVISIBLE);
            //            list_3.setVisibility(View.INVISIBLE);
            //            madapter.setCurrentItem(-1);
        }else{
            DLog.d("RepairAct"," 获取所有设备故障待维修记录为空");
        }
    }

    @Override
    public void doError(String msg) {
        showTips(msg);
        ll_search.setVisibility(View.GONE);
        ll_tel.setVisibility(View.GONE);
        list_viction.setVisibility(View.VISIBLE);
    }

    @Override
    public void doLogin(UserResultBean bean) {
        closedialog();
        showTips("登录成功");
        UserResultBean userBean=bean;
        token=bean.getToken();
        String uname=bean.getResult().getUserName();

        acache.put("login_name",uname);
        acache.put("login_token",token);
        acache.put("log_psw",psw);

        //        if(isConnected  && !Tools.isEmpty(current_truckNum)){  //已连接并且车牌不为空
        //            mhandler.postDelayed(new Runnable() {
        //                @Override
        //                public void run() {
        //                    tv_car_number.setText(current_truckNum);
        //                    bt_search.performClick();
        //                }
        //            },100);
        //
        //        }
        showProgressDialog();
        mpresenter.getAllRepairInformation(token);

    }

    @SuppressLint("HandlerLeak")
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void doLoginFail(String str) {
        closedialog();
        showTips(str);
        finish();
    }


    @Override
    public void sendSuccess(String msg) {
    }


    @Override
    public void sendError(String msg) {
    }

    @Override
    public void sendPersonSuccess(List<InstallerInfo> info) {
    }

    @Override
    public void sendPersonError(String msg) {
    }

    @Override
    public void doCompanySuccess(CompanyResultBean bean) {
        CompanyResultBean companyResult = bean;
        acache.put("company_truck",(Serializable)companyResult);
        DLog.d("AddTruckActivity", "下载公司列表成功");

    }

    @Override
    public void doCompanyError(String msg) {
        showTips(msg);
    }

    @Override
    public void doAllSuccess(List<RepairModel> model) {
        if(model !=null && model.size() >0) {
            reList=model;

            if(model !=null && model.size() >0){
                doCompareCar(model);

            }
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initCompanyList();//下载公司
                }
            },300);

        }

    }


    @Override
    public void doOnlineSuccess(List<OnLineTruckBean> model) {
        //        if(model !=null && model.size() >0){
        //            doCompareCar(model, reList);
        //
        //        }
        //        mhandler.postDelayed(new Runnable() {
        //            @Override
        //            public void run() {
        //                initCompanyList();//下载公司
        //            }
        //        },300);


    }

    //获取 所有维修车辆的 坐标
    private void  doCompareCar(final List<RepairModel> mlist) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<RepairModel> replist= doDeltDouble(mlist);
                final List<RepairModel> newList=new ArrayList<>();
                List<RepairModel>   istnewList=new ArrayList<>();
                for(int i=0;i<replist.size();i++){
                    RepairModel rem=replist.get(i);
                    String lont=rem.getX(); String latitu=rem.getY();

                    if(!Tools.isEmpty(lont) && !Tools.isEmpty(latitu)){
                        rem.setLon(lont);
                        rem.setLat(latitu);

                        LatLng car=new LatLng(Double.parseDouble(lont),Double.parseDouble(latitu));
                        LatLng me =(LatLng)acache.getAsObject("current_address"); //当前位置
                        String str= DataUtil.dohandlerDistance(car,me);
                        rem.setDistance(str);
                    }else{
                        rem.setDistance("99999999");
                    }
                    newList.add(rem);
                }

                Collections.sort(newList, new Comparator<RepairModel>(){

                    /*
                     * int compare(Student o1, Student o2) 返回一个基本类型的整型，
                     * 返回负数表示：o1 小于o2，
                     * 返回0 表示：o1和o2相等，
                     * 返回正数表示：o1大于o2。
                     */
                    public int compare(RepairModel o1, RepairModel o2) {
                        if(!Tools.isEmpty(o1.getDistance())  && !Tools.isEmpty(o2.getDistance()) ){
                            //按照学生的年龄进行升序排列
                            if(Double.parseDouble(o1.getDistance()) >Double.parseDouble(o2.getDistance()) ){
                                return 1;
                            }
                            if(Double.parseDouble(o1.getDistance()) >Double.parseDouble(o2.getDistance())){
                                return 0;
                            }
                        }
                        return -1;
                    }
                });

                if(newList !=null && newList.size() >=50){
                    istnewList=newList.subList(0,50);
                }

                plist=istnewList;
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        madapter.updateListView(plist);
                    }
                });
                acache.put("all_repair_car",(Serializable)newList);
            }
        }).start();

    }
    //去重
    public List<RepairModel> doDeltDouble(List<RepairModel> relist){
        for(int i=0;i<relist.size();i++){
            String carNum=relist.get(i).getCarNumber();
            for(int k=0;k<relist.size();k++){
                String carNum1=relist.get(k).getCarNumber();
                if(i!=k&&carNum.equals(carNum1)) {
                    relist.remove(k);
                }
            }
        }
        return relist;
    }

    @Override
    public void doAllError(String msg) {
        showTips(msg);
    }

    @Override
    public void setOK(int num,String mFirst) {

    }


    @Override
    public void setCancle(int num,String mFirst) {
    }


    @Override
    public void setNext(int position) {
    }

    @Override
    public void setNextSecond(int position) {
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void hideSoft(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    @Override
    public void setOK(int num) {//跳转第二页

        if(dlist !=null && dlist.size() >0){
            RepairModel bean= dlist.get(num);
            String repairId=bean.getId();
            String faultName=bean.getFaultTypeName();//故障类型名称

            List<String> fList=bean.getFaultPhenomenon(); //维修现象
            String oneFault="(无)";
            if(fList !=null && fList.size() >0 ){
                StringBuffer faStr=new StringBuffer();
                for(int j=0;j<fList.size();j++){
                    faStr.append(fList.get(j)+",");
                }
                String str=faStr.toString();
                str= str.substring(0,str.length() - 1);
                 oneFault=faultName+":"+str;
            }


            StringBuffer onGuide=new StringBuffer();
            List<String> gList=bean.getRepairedGuide();  //维修指导
            if(gList!=null && gList.size() > 0){
                onGuide.append(faultName+":");
                if(gList !=null && gList.size() >0){
                    StringBuffer guideStr=new StringBuffer();
                    for(int k=0;k<gList.size();k++){
                        guideStr.append(gList.get(k)+",");
                    }
                    String str1=guideStr.toString();
                    str1= str1.substring(0,str1.length() - 1);
                    onGuide.append(str1);
                }
            }else{
                onGuide.append("(无)");
            }
            if(!Tools.isEmpty(repairId)){
                //                SecondRepairAct.start(context,num,oneFault,onGuide,repairId,truckNum);

                Intent intent = new Intent(RepairAct.this, SecondRepairAct.class);
                intent.putExtra("list_num",num);
                intent.putExtra("repari_fault",oneFault);
                intent.putExtra("repari_guide",onGuide.toString());
                intent.putExtra("repari_id",repairId);
                intent.putExtra("dirver_name",bean.getDriverName()+"");
                intent.putExtra("dirver_phone",bean.getDriverPhone()+"");
                intent.putExtra("repari_truck",truckNum);
                startActivityForResult(intent,100);

            }else{
                DLog.d("RepairAct","拉取维修现象数据有问题");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==100 && resultCode ==10){
            int number=data.getIntExtra("rep_number",0);
            gmlist.get(number).setCheck(true);
            gadapter.updateListView(gmlist,number);
        }
    }


    @Override
    public void onRefresh() {
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mpresenter.getAllRepairInformation(token);
                onLoad();
            }
        },300);

    }

    @Override
    public void onLoadMore() {

    }
    private void onLoad() {
        list_nearto_me.stopRefresh();
        list_nearto_me.stopLoadMore();
    }


    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        dialog = new CustomDialog(context, R.style.LoadDialog);
        dialog.show();
        new Thread("cancle_progressDialog") {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                    // cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
                    // 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
                    if(dialog !=null ){
                        dialog.cancel();
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
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void telPhone(RepairModel num) {
        String phone=num.getDriverPhone();
        mId=num.getId();
        showDialog(context,phone);
    }

    class MyPhoneListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲

                    if (isCall) {
                        callTime = System.currentTimeMillis() - firstCallTime;
                        isCall = false;
                        costTime = (int) (callTime / 1000);
                        if (tm != null&&myPhoneListener!=null) {
                            tm.listen(myPhoneListener, PhoneStateListener.LISTEN_NONE);
                        }
                        String date=costTime+""; //秒
                        mpresenter.inputPhoneDate(token,date,mId);
                    }

                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                    isCall = true;
                    firstCallTime = System.currentTimeMillis();

                    break;
                default:
                    break;
            }
        }
    }


    //录车提示框
    public void showDialog(final Context context,final String phone) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_phone, null);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(phone);
                //                mpresenter.inputPhoneDate(token,"9",mId);
                tempDialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
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
    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent1 = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent1.setData(data);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            showTips("请打开拨打电话权限");
            return;
        }
        context.startActivity(intent1);
    }
}


