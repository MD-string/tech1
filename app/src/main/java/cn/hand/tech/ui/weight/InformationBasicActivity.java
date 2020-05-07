package cn.hand.tech.ui.weight;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDSendDataModel;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.KeyConstants;
import cn.hand.tech.common.OnMyItemClickListener;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.setting.UpBinActFromAddCar;
import cn.hand.tech.ui.weight.bean.AddTruckInfo;
import cn.hand.tech.ui.weight.bean.CompanyBean;
import cn.hand.tech.ui.weight.bean.InstallerInfo;
import cn.hand.tech.ui.weight.bean.XlsData;
import cn.hand.tech.ui.weight.bean.XlsDataTimeModel;
import cn.hand.tech.ui.weight.presenter.DoExcelData;
import cn.hand.tech.ui.weight.presenter.InformationBasicPresenter;
import cn.hand.tech.utils.CameraUtil;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.FileUtils;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.BottomSelectPopupWindow;
import cn.hand.tech.weiget.CustomDatePicker;
import cn.hand.tech.weiget.CustomDialog;


/**
 * describe:安装人员信息
 */
public class InformationBasicActivity extends AppCompatActivity implements View.OnClickListener,IInformationBasicView {

    private String companyId="";
    private String token;
    private List<CompanyBean> companyList=new ArrayList<>();
    private Context mContext;
    private InformationBasicPresenter mpresenter;
    private OptionsPickerView pvCustomOptions;
    private boolean isConnect;
    private String carNumber,deviceId;
    private CompanyBean mCompanyBean;//当前选择的公司
    private Button btn_next;
    private int REQUEST_WRITE_EXTERNAL_STORAGE=2;
    private ACache acache;
    private Spinner xipian_1,xipian_2,tiepian_1,tiepian_2,fengjiao_1,fengjiao_2,chuanxian_1,chuanxian_2,install_1,install_2;
    private LinearLayout ll_back;
    private List<InstallerInfo> inlist;
    private ArrayAdapter<String> adapter;
    private List<String> list;
    private String xp1,xp2,tp1,tp2,fj1,fj2,cx1,cx2,it1,it2;
    private String name_xp1,name_xp2,name_tp1,name_tp2,name_fj1,name_fj2,name_cx1,name_cx2,name_it1,name_it2;
    private String stuckNumber;
    private CustomDialog dialog;
    private AlertDialog adddialog;

    public static final int MAX_SHEET_COUNT = 1;// 详情最多张s
    public static final int REQUEST_CODE_CAMERA_P = 28;
    public static final int REQUEST_CODE_EDIT_P =17;

    private BroadcastReceiver receiver;
    private File cameraFile;
    private BottomSelectPopupWindow pickPhotoDlg;
    private RelativeLayout ll_all;
    private String filepath;
    private LinearLayout ll_choose;
    private int mposition =-1;
    private boolean isEnterCar=false;
    private TextView tv_start_time;
    private CustomDatePicker customDatePickerS;
    private AlertDialog inftempDialog;
    private String mchildCode,mSensorNumb;
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Timer timer;
    private boolean isWriteOK;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case  1:
                    list=new ArrayList<>();
                    if(inlist !=null && inlist.size() >0){
                        for(int i=0;i<inlist.size();i++){
                            list.add(inlist.get(i).getName());
                        }
                        list.add(0,"请选择");
                        adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        xipian_1.setAdapter(adapter); xipian_2.setAdapter(adapter);
                        tiepian_1.setAdapter(adapter); tiepian_2.setAdapter(adapter);
                        fengjiao_1.setAdapter(adapter); fengjiao_2.setAdapter(adapter);
                        chuanxian_1.setAdapter(adapter); chuanxian_2.setAdapter(adapter);
                        install_1.setAdapter(adapter); install_2.setAdapter(adapter);

                    }

                    break;

            }
        }
    };

    public static  void start(Context context){
        Intent i=new Intent(context,InformationBasicActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.fragment_install_information);
        acache= ACache.get(mContext, CommonUtils.TAG);
        token=acache.getAsString("login_token");
        deviceId=acache.getAsString("dev_id");
        stuckNumber= acache.getAsString("car_num");
        companyId= acache.getAsString("company_id"); //父类ID

        mchildCode=acache.getAsString("car_childCode");
        mSensorNumb=acache.getAsString("current_sensor");//当前的传感器

        mpresenter=new InformationBasicPresenter(mContext,this);
        findViews();
        initInstallerList();
        setListeners();
        registerBrodcat();
    }

    protected void findViews() {
        ll_all=(RelativeLayout)findViewById(R.id.ll_all);
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_choose=(LinearLayout)findViewById(R.id.ll_choose);
        xipian_1=(Spinner)findViewById(R.id.xipian_1);
        xipian_2=(Spinner)findViewById(R.id.xipian_2);
        tiepian_1=(Spinner)findViewById(R.id.tiepian_1);
        tiepian_2=(Spinner)findViewById(R.id.tiepian_2);
        fengjiao_1=(Spinner)findViewById(R.id.fengjiao_1);
        fengjiao_2=(Spinner)findViewById(R.id.fengjiao_2);
        chuanxian_1=(Spinner)findViewById(R.id.chuanxian_1);
        chuanxian_2=(Spinner)findViewById(R.id.chuanxian_2);
        install_1=(Spinner)findViewById(R.id.install_1);
        install_2=(Spinner)findViewById(R.id.install_2);
        tv_start_time=(TextView)findViewById(R.id.tv_start_time);
        tv_start_time.setOnClickListener(this);
        btn_next=(Button)findViewById(R.id.btn_next);
        ll_choose.setOnClickListener(this);
        btn_next.setOnClickListener(this);


        SimpleDateFormat sdf = getDate();
        Date date = new Date();
        String now = sdf.format(date);
        customDatePickerS = new CustomDatePicker(mContext, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tv_start_time.setText(time);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePickerS.showSpecificTime(true); // 显示时和分
        customDatePickerS.setIsLoop(true); // 允许循环滚动

    }


    protected void setListeners() {
        ll_back.setOnClickListener(this);
        xipian_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(inlist !=null && inlist.size() > 0 && position >0){
                    mposition=position-1;
                    xp1=inlist.get(position-1).getId();
                    name_xp1=inlist.get(position-1).getName();
                }else{
                    mposition=-1;
                    xp1="";
                    name_xp1="";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        xipian_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(inlist !=null && inlist.size() > 0 && position >0) {
                    xp2 = inlist.get(position-1).getId();
                    name_xp2=inlist.get(position-1).getName();
                    if(!Tools.isEmpty(xp2)  &&  xp2.equals(xp1)){
                        xipian_2.setSelection(0);
                        showTips("该人员已经选择,请选择其他人员");
                    }
                }else{
                    xp2="";
                    name_xp2="";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //                if(inlist !=null && inlist.size() > 0) {
                //                    xp2 = inlist.get(0).getId();
                //                    name_xp2=inlist.get(0).getName();
                //                }
            }
        });
        tiepian_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(inlist !=null && inlist.size() > 0 && position >0) {
                    tp1 =inlist.get(position-1).getId();
                    name_tp1=inlist.get(position-1).getName();
                }else{
                    tp1="";
                    name_tp1="";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //                if(inlist !=null && inlist.size() > 0) {
                //                    tp1 =inlist.get(0).getId();
                //                    name_tp1=inlist.get(0).getName();
                //                }
            }
        });
        tiepian_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(inlist !=null && inlist.size() > 0 && position >0) {
                    tp2 = inlist.get(position-1).getId();
                    name_tp2=inlist.get(position-1).getName();
                    if(!Tools.isEmpty(tp2)  &&  tp2.equals(tp1)){
                        tiepian_2.setSelection(0);
                        showTips("该人员已经选择,请选择其他人员");
                    }

                }else{
                    tp2="";
                    name_tp2="";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //                if(inlist !=null && inlist.size() > 0) {
                //                    tp2 = inlist.get(0).getId();
                //                    name_tp2=inlist.get(0).getName();
                //                }
            }
        });
        fengjiao_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(inlist !=null && inlist.size() > 0 && position >0) {
                    fj1 =inlist.get(position-1).getId();
                    name_fj1=inlist.get(position-1).getName();
                }else{
                    fj1="";
                    name_fj1="";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //                if(inlist !=null && inlist.size() > 0) {
                //                    fj1 =inlist.get(0).getId();
                //                    name_fj1=inlist.get(0).getName();
                //                }
            }
        });
        fengjiao_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(inlist !=null && inlist.size() > 0 && position >0) {
                    fj2 = inlist.get(position-1).getId();
                    name_fj2=inlist.get(position-1).getName();
                    if(!Tools.isEmpty(fj2)  &&  fj2.equals(fj1)){
                        fengjiao_2.setSelection(0);
                        showTips("该人员已经选择,请选择其他人员");
                    }

                }else{
                    fj2="";
                    name_fj2="";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //                if(inlist !=null && inlist.size() > 0) {
                //                    fj2 = inlist.get(0).getId();
                //                    name_fj2=inlist.get(0).getName();
                //                }
            }
        });
        chuanxian_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(inlist !=null && inlist.size() > 0 && position >0) {
                    cx1 = inlist.get(position-1).getId();
                    name_cx1=inlist.get(position-1).getName();
                }else{
                    cx1="";
                    name_cx1="";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //                if(inlist !=null && inlist.size() > 0) {
                //                    cx1 = inlist.get(0).getId();
                //                    name_cx1=inlist.get(0).getName();
                //                }

            }
        });
        chuanxian_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(inlist !=null && inlist.size() > 0 && position >0) {
                    cx2 = inlist.get(position-1).getId();
                    name_cx2=inlist.get(position-1).getName();
                    if(!Tools.isEmpty(cx2)  &&  cx2.equals(cx1)){
                        chuanxian_2.setSelection(0);
                        showTips("该人员已经选择,请选择其他人员");
                    }

                }else{
                    cx2="";
                    name_cx2="";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
                //                if(inlist !=null && inlist.size() > 0) {
                //                    cx2 = inlist.get(0).getId();
                //                    name_cx2=inlist.get(0).getName();
                //                }
            }
        });
        install_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(inlist !=null && inlist.size() > 0  && position >0) {
                    it1 = inlist.get(position-1).getId();
                    name_it1=inlist.get(position-1).getName();
                }else{
                    it1="";
                    name_it1="";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //                if(inlist !=null && inlist.size() > 0) {
                //                    it1 = inlist.get(0).getId();
                //                    name_it1=inlist.get(0).getName();
                //                }

            }
        });
        install_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(inlist !=null && inlist.size() > 0  && position >0) {
                    it2 = inlist.get(position-1).getId();
                    name_it2=inlist.get(position-1).getName();

                    if(!Tools.isEmpty(it2)  &&  it2.equals(it1)){
                        install_2.setSelection(0);
                        showTips("该人员已经选择,请选择其他人员");
                    }
                }else{
                    it2="";
                    name_it2="";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //                if(inlist !=null && inlist.size() > 0) {
                //                    it2 = inlist.get(0).getId();
                //                    name_it2=inlist.get(0).getName();
                //                }
            }
        });


    }


    /*获取安装人员信息*/
    private void initInstallerList() {
        mpresenter.getInformationBasic(token,deviceId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back://返回
                finish();
                break;
            case R.id.ll_choose://全选
                if(inlist !=null && inlist.size() > 0){
                    if(mposition >=0){
                        xp1=inlist.get(mposition).getId();
                        name_xp1=inlist.get(mposition).getName();
                        if(!Tools.isEmpty(xp1)){
                            tiepian_1.setSelection(mposition+1,true);
                            fengjiao_1.setSelection(mposition+1,true);
                            chuanxian_1.setSelection(mposition+1,true);
                            install_1.setSelection(mposition+1,true);

                        }
                    }else{
                        showTips("请先选择好第一个");
                    }
                }else{
                    showTips("数据有误");
                }

                break;
            case R.id.tv_start_time://开始安装时间
                customDatePickerS.show(tv_start_time.getText().toString());

                break;
            case R.id.btn_next://下一步
                if(CommonUtils.isFastDoubleClick()){
                    return;
                }
                //                if(isEnterCar){
                //                   showTips("安装人员信息已经上传");
                //                   checkPermission();
                //                   return;
                //                }
                if(Tools.isEmpty(xp1) || Tools.isEmpty(tp1)  || Tools.isEmpty(fj1) || Tools.isEmpty(cx1) || Tools.isEmpty(it1)){
                    showTips("请选择安装人员");
                    return;
                }
                String date=tv_start_time.getText().toString().trim();
                if(Tools.isEmpty(date)){
                    showTips("请选择开始安装时间");
                    return;
                }
                if(Tools.isEmpty(stuckNumber)){
                    showTips("车牌不能为空");
                    return;
                }
                try{
                    String polishInstaller=xp1+","+xp2;//打磨清洗人
                    String patchInstaller=tp1+","+tp2;//贴片人
                    String sealantInstaller=fj1+","+fj2;//封胶
                    String threadingInstaller=cx1+","+cx2;//穿线
                    String hostInstaller=it1+","+it2;//主机安装人员

                    HashMap<String, String> mapParams = new HashMap<>();
                    mapParams.put("token", token);
                    mapParams.put("deviceId", deviceId);
                    mapParams.put("carNumber", stuckNumber);
                    mapParams.put("polishInstaller",polishInstaller);
                    mapParams.put("patchInstaller",patchInstaller);
                    mapParams.put("sealantInstaller",sealantInstaller);
                    mapParams.put("threadingInstaller",threadingInstaller);
                    mapParams.put("hostInstaller",hostInstaller);
                    mapParams.put("startInstallTime",date+":00");

                    mpresenter.sendInstallInformation(mapParams);

                    AddTruckInfo  bean=(AddTruckInfo)acache.getAsObject("add_truck");
                    if(bean !=null ){
                        bean.setPolishInstaller1(name_xp1);
                        bean.setPolishInstaller2(name_xp2);
                        bean.setPatchInstaller1(name_tp1);
                        bean.setPatchInstaller2(name_tp2);
                        bean.setSealantInstaller1(name_fj1);
                        bean.setSealantInstaller2(name_fj2);
                        bean.setThreadingInstaller1(name_cx1);
                        bean.setThreadingInstaller2(name_cx2);
                        bean.setHostInstaller1(name_it1);
                        bean.setHostInstaller1(name_it2);
                        bean.setStartInstallTime(date);
                        acache.put("add_truck",(Serializable)bean);
                    }
                }catch (Exception e){
                    showTips("数据有问题");
                    e.printStackTrace();
                }

                break;
        }
    }
    private void hideKeyBorad() {
        InputMethodManager imm1 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm1 != null) {
            imm1.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0);
        }
    }



    private void submit() {

        //        String company = mTvSelectCompany.getText().toString().trim();
        //        if (TextUtils.isEmpty(company)) {
        //            Toast.makeText(mContext, "公司名称不能为空", Toast.LENGTH_SHORT).show();
        //            CommonKitUtil.focusView(mTvSelectCompany);
        //            return;
        //        }
        //
        //        String deviceNumber = mEtDeviceNumber.getText().toString().trim();
        //        if (TextUtils.isEmpty(deviceNumber)) {
        //            Toast.makeText(mContext, "设备编号不能为空", Toast.LENGTH_SHORT).show();
        //            CommonKitUtil.showOrHideKeyBoard(mContext,true, mEtDeviceNumber);
        //            return;
        //        }
        //
        //        String truckNumber = mEtTruckNumber.getText().toString().trim();
        //        if (TextUtils.isEmpty(truckNumber)) {
        //            Toast.makeText(mContext, "车牌号码不能为空", Toast.LENGTH_SHORT).show();
        //            CommonKitUtil.showOrHideKeyBoard(mContext,true,mEtTruckNumber);
        //            return;
        //        }
        //
        //        HashMap<String, String> mapParams = new HashMap<>();
        //        mapParams.put("token", token);
        //        mapParams.put("type", "1");
        //        mapParams.put("deviceId", mEtDeviceNumber.getText().toString());
        //        mapParams.put("gpsId", mCompanyBean.getId());
        //        mapParams.put("companyId", companyId);
        //        mapParams.put("carNumber", mEtTruckNumber.getText().toString());
        //        mapParams.put("phone",mCompanyBean.getTel());
        //        mapParams.put("loadCapacity", "30");//车辆载重量
        //        mpresenter.addTruck(mapParams);
    }


    @Override
    public void doSuccess(List<InstallerInfo> info ) {
        DLog.d("InformationBasicActivity","获取安装人员信息成功");
        inlist=info;
        mHandler.sendEmptyMessage(1);

    }
    //
    //    //判断车辆是否在线
    //    private void  doCheckOnline(){
    //        HashMap<String, String> mapParams = new HashMap<>();
    //        mapParams.put("token", token);
    //        mapParams.put("deviceId", deviceId);
    //        mpresenter.checkCarOnline(mapParams);
    //    }

    @Override
    public void doError(String str) {
        showTips(str);
        DLog.d("AddTruckActivity",str);
    }

    @Override
    public void sendSuccess(String msg) {  //上传安装人员信息成功  。
        //        showTips("安装人员信息添加成功");
        //        isEnterCar=true;
        //        finish();

        dojudge();
    }

    //判断是否显示升级对话框还是 提示安装成功提示
    private void dojudge() {
        List<String> slist= ( List<String>)acache.getAsObject("CompanyListActivity_company_list"); //可升级的公司id 列表
        if(slist !=null && slist.size() >0  && !Tools.isEmpty(companyId) ){
            boolean isSame=false;
            for(int i=0;i<slist.size();i++){
                String compStr=slist.get(i);
                if(companyId.equals(compStr)){
                    isSame=true;
                    break;
                }
            }

            if(isSame){  //可升级列表中包含选定的公司

                if(!Tools.isEmpty(mchildCode)   && !Tools.isEmpty(mSensorNumb)  &&  (mchildCode.equals("0102") || mchildCode.equals("0104") || mchildCode.equals("0101"))
                        &&  (("1,2").equals(mSensorNumb) ||  ("1,2,3").equals(mSensorNumb) || ("1,2,3,4").equals(mSensorNumb))){

                    showNextDialog(mContext,"安装人员信息添加成功,是否写入系数?","1");
                }else{
                    showNextDialog(mContext,"安装人员信息添加成功,是否升级固件?","2");
                }

            }else{
                showNextDialog(mContext,"安装人员信息添加成功","0");
            }

        }else{
            showNextDialog(mContext,"安装人员信息添加成功","0");
        }
    }


    @Override
    public void loadSuccess(String msg) { //上传图片成功
        showTips("车辆添加成功");
        finish();
    }

    @Override
    public void loadFail(String msg) {//上传图片失败
        showTips("车辆添加失败");
        sendMsgToDD();
    }

    @Override
    public void uploadRadioSuccess(String msg) { //上传日志
        showTips(msg);
        closeProgressDialog();
        showNextDialog(mContext,"上传写入系数日志成功,是否升级固件?","2");
    }

    @Override
    public void uploadFail(String msg) { //上传日志失败
        showTips(msg);
        closeProgressDialog();
        showNextDialog(mContext,"上传写入系数日志失败,是否升级固件?","2");
    }


    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }

    //上传系数
    private void uploadRatio(){
        try{
            showProgressDialog();
            isWriteOK=false;

            String number="0";
            HDSendDataModel model = new HDSendDataModel();
            if(mchildCode.equals("0102") || mchildCode.equals("0104")){//0102 0104 袋装   0101散装
                if(("1,2").equals(mSensorNumb)){
                    model.mmv1=0.0002f;
                    model.mmv2=0.002f;
                    model.setRatioStr("0.0002,0.002");
                    number="2";
                }else if(("1,2,3").equals(mSensorNumb)){
                    model.mmv1=0.0001f;
                    model.mmv2=0.0001f;
                    model.mmv3=0.002f;
                    model.setRatioStr("0.0001,0.0001,0.002");
                    number="3";
                }else if(("1,2,3,4").equals(mSensorNumb)){
                    model.mmv1=0.0001f;
                    model.mmv2=0.0001f;
                    model.mmv3=0.001f;
                    model.mmv4=0.001f;
                    model.setRatioStr("0.0001,0.0001,0.001,0.001");
                    number="4";
                }else{
                    number="0";
                }
            }else if(mchildCode.equals("0101")){
                if(("1,2").equals(mSensorNumb)){
                    model.mmv1=0.0015f;
                    model.mmv2=0.0015f;
                    model.setRatioStr("0.0015,0.0015");
                    number="2";
                }else if(("1,2,3").equals(mSensorNumb)){
                    model.mmv1=0.001f;
                    model.mmv2=0.001f;
                    model.mmv3=0.001f;
                    model.setRatioStr("0.001,0.001,0.001");
                    number="3";
                }else if(("1,2,3,4").equals(mSensorNumb)){
                    model.mmv1=0.001f;
                    model.mmv2=0.001f;
                    model.mmv3=0.001f;
                    model.mmv4=0.001f;
                    model.setRatioStr("0.001,0.001,0.001,0.001");
                    number="4";
                }else{
                    number="0";
                }
            }else{
                DLog.e("UpBinActFromAddCar","uploadRatio=车辆类型=>"+"不是袋装也不是散装");
            }

            acache.put("sensor_string",model.getRatioStr());
            Date date   =   new   Date(System.currentTimeMillis());//获取当前时间
            String now=simpleDateFormat.format(date);
            acache.put("set_sensor_date",now);
            doStartTimer(); //开始计时 8秒钟

            Bundle bundle=new Bundle();
            bundle.putSerializable("coefficient_act1",model);
            bundle.putString("sensor_number",number);
            Intent readIntent=new Intent(BleConstant.ACTION_BLE_WRITE_COE1);
            readIntent.putExtras(bundle);
            sendBroadcast(readIntent);

            DLog.e("UpBinActFromAddCar","uploadRatio=isSendRaio=>"+number+"/"+now);


        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void doStartTimer(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(!isWriteOK){
                    //上传写入失败日志
                    String sersorString= acache.getAsString("sensor_string");
                    String date=acache.getAsString("set_sensor_date");

                    HashMap<String, String> mapParams1 = new HashMap<>();
                    mapParams1.put("token", token);
                    mapParams1.put("deviceId", deviceId);
                    mapParams1.put("type","2");
                    mapParams1.put("status","已下发");
                    mapParams1.put("newCoef",sersorString); //下发系数
                    mapParams1.put("sendTime",date);//设置时间
                    mapParams1.put("remark","Android");

                    mpresenter.upLoadWriteRatio(mapParams1);
                }
            }
        };
        if(timer ==null){
            timer=new Timer();
        }
        timer.schedule(task, 8000);//
    }
    //对话框 tag  0 不提示写入系数和升级  1 提示写入系数 2 提示固件升级
    public void showNextDialog(final Context context, String msg, final String tag) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        /*dialog.setTitle("提示");
        dialog.setMessage(message);*/
        View view = LayoutInflater.from(context).inflate(R.layout.layout_success_msg, null);
        TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
        tv_title.setText(msg);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        if("1".equals(tag)){
            tv_ok.setText("写入系数");
        }else if("2".equals(tag)){
            tv_ok.setText("升级固件");
        }else{
            tv_ok.setText("确定");
        }
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inftempDialog != null){
                    inftempDialog.dismiss();
                }
                if("1".equals(tag)){
                    uploadRatio();
                }else if("2".equals(tag)){
                    UpBinActFromAddCar.start(context,companyId);
                    finish();
                }else{
                    finish();
                }

            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inftempDialog != null){
                    inftempDialog.dismiss();
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        inftempDialog = dialog.create();
        inftempDialog.setView(view, 0, 0, 0, 0);
        inftempDialog.show();
    }




    //上传图片失败 发送信息到叮叮
    private void sendMsgToDD(){
        AddTruckInfo  bean=(AddTruckInfo)acache.getAsObject("add_truck");
        List<XlsData> list=new ArrayList<>();
        XlsData x1=new XlsData();
        x1.setName("ID");
        x1.setV1(bean.getId());
        x1.setV2("");
        list.add(x1);

        XlsData x2=new XlsData();
        x2.setName("车牌");
        x2.setV1(bean.getTruckNumber());
        x2.setV2("");
        list.add(x2);

        XlsData x3=new XlsData();
        x3.setName("公司");
        x3.setV1(bean.getCompany());
        x3.setV2("");
        list.add(x3);

        XlsData x4=new XlsData();
        x4.setName("荷载量");
        x4.setV1(bean.getWeight());
        x4.setV2("");
        list.add(x4);

        XlsData x5=new XlsData();
        x5.setName("司机号码");
        x5.setV1(bean.getPhone());
        x5.setV2("");
        list.add(x5);

        XlsData x6=new XlsData();
        x6.setName("车型");
        x6.setV1(bean.getTruckType());
        x6.setV2("");
        list.add(x6);

        XlsData x7=new XlsData();
        x7.setName("轴数");
        x7.setV1(bean.getAlxesNumber());
        x7.setV2("");
        list.add(x7);

        XlsData x8=new XlsData();
        x8.setName("传感器数量");
        x8.setV1(bean.getSensorNumber());
        x8.setV2("");
        list.add(x8);

        XlsData x9=new XlsData();
        x9.setName("传感器类型");
        x9.setV1(bean.getSensorType());
        x9.setV2("");
        list.add(x9);


        XlsData x10=new XlsData();
        x10.setName("打磨清洗");
        x10.setV1(bean.getPolishInstaller1());
        x10.setV2(bean.getPolishInstaller2());
        list.add(x10);

        XlsData x11=new XlsData();
        x11.setName("贴片");
        x11.setV1(bean.getPatchInstaller1());
        x11.setV2(bean.getPatchInstaller2());
        list.add(x11);

        XlsData x12=new XlsData();
        x12.setName("封胶");
        x12.setV1(bean.getSealantInstaller1());
        x12.setV2(bean.getSealantInstaller2());
        list.add(x12);

        XlsData x13=new XlsData();
        x13.setName("穿线");
        x13.setV1(bean.getThreadingInstaller1());
        x13.setV2(bean.getThreadingInstaller2());
        list.add(x13);

        XlsData x14=new XlsData();
        x14.setName("主机安装");
        x14.setV1(bean.getHostInstaller1());
        x14.setV2(bean.getHostInstaller2());
        list.add(x14);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time1 = formatter.format(curDate);
        String name=stuckNumber+"_"+time1;
        List<XlsDataTimeModel> listLocalData=new ArrayList<>();
        XlsDataTimeModel  model =new XlsDataTimeModel();
        model.setLocalDataTime(name);
        model.setWeightDataBeanList(list);
        listLocalData.add(model);
        DoExcelData.saveLocalData(mContext,listLocalData,name);
        DoExcelData.sendZip(this,name);
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        dialog = new CustomDialog(mContext, R.style.LoadDialog);
        dialog.show();
        //        new Thread("cancle_progressDialog") {
        //            @Override
        //            public void run() {
        //                try {
        ////                    Thread.sleep(7000);
        //                    // cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
        //                    // 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
        //                    if(dialog !=null ){
        //                        dialog.cancel();
        //                    }
        //                    // dialog.dismiss();
        //                } catch (InterruptedException e) {
        //                    // TODO Auto-generated catch block
        //                    e.printStackTrace();
        //                }
        //
        //            }
        //        }.start();
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

    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            private String path;

            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                // 选择图片返回图片路径
                if (intent.getAction().equals(KeyConstants.BROADCAST_PATH) && intent.getStringArrayListExtra("path") != null) {
                    ArrayList<String> pickedImg=intent.getStringArrayListExtra("path");
                    path=pickedImg.get(0);//路径
                    filepath = "file://"+path;
                    mpresenter.sendPersonalHeader(cameraFile.getAbsolutePath(),deviceId,stuckNumber,token,companyId);
                }else if(action.equals(BleConstant.ACTION_SEND_DATA)){
                    HDSendDataModel sMode = (HDSendDataModel) intent.getSerializableExtra("data");
                    DLog.e("UpBinActFromAddCar","sMode==" + sMode.weight);
                    DLog.e("UpBinActFromAddCar","sMode111==" + sMode.mmv1);
                    showTips( "写入成功");
                    isWriteOK=true;
                    //上传系数
                    String sersorString= acache.getAsString("sensor_string");
                    String date=acache.getAsString("set_sensor_date");

                    HashMap<String, String> mapParams1 = new HashMap<>();
                    mapParams1.put("token", token);
                    mapParams1.put("deviceId", deviceId);
                    mapParams1.put("type","3");
                    mapParams1.put("status","设置成功");
                    mapParams1.put("newCoef",sersorString); //下发系数
                    mapParams1.put("sendTime",date);//设置时间
                    mapParams1.put("remark","Android");

                    mpresenter.upLoadWriteRatio(mapParams1);
                }
            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(KeyConstants.BROADCAST_PATH);
        filter.addAction(BleConstant.ACTION_SEND_DATA);
        this.registerReceiver(receiver, filter);
    }
    /**
     * 弹出选择图片方式询问
     */
    public void  photoDialogPop() {
        if (pickPhotoDlg == null) {
            pickPhotoDlg = new BottomSelectPopupWindow(this, new String[] { "拍照" }, null);
            pickPhotoDlg.setOnItemClickListener(new OnMyItemClickListener() {

                @Override
                public void onItemClick(View parent, View view, int which) {
                    //                    if (which == 0) {
                    pickToCamera();
                    //                    } else {
                    //                        pickToAlbum();
                    //                    }
                }
            });
        }
        pickPhotoDlg.showAtLocation(ll_all, Gravity.BOTTOM, 0, 0);
    }

    //    private void pickToAlbum() {
    //        LookImageActivity.startPick(this, MAX_SHEET_COUNT, "选择头像",from_Flag);
    //    }
    private void pickToCamera() {
        cameraFile = new File(FileUtils.getAppSdcardDir() + "/" + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        //        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), REQUEST_CODE_CAMERA_P);

        CameraUtil.openCamera(this,cameraFile,REQUEST_CODE_CAMERA_P);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA_P) {// 返回拍照结果
            if (cameraFile != null && cameraFile.exists()) {
                ArrayList<String> path = new ArrayList<String>();
                path.add(cameraFile.getAbsolutePath());
                Intent intent = new Intent(KeyConstants.BROADCAST_PATH);
                intent.putExtra("path", path);
                //                intent.putExtra(KeyConstants.IMAGE_FROM_FLAG, from_Flag);
                sendBroadcast(intent);
            }else{
                DLog.d( CommonUtils.TAG,"拍照结果返回有問題");

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
//
//    private void checkPermission() {
//        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
//        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
//                    .WRITE_EXTERNAL_STORAGE)) {
//                //                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
//            }
//            //申请权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_WRITE_EXTERNAL_STORAGE);
//
//        } else {//已经授权
//
//            photoDialogPop();//下载文档
//
//            DLog.e( CommonUtils.TAG,"TAG_SERVICE"+ "checkPermission: 已经授权！");
//        }
//    }
//
//    //系统方法,从requestPermissions()方法回调结果
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        //确保是我们的请求
//        if(requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    photoDialogPop();
//                }
//            });
//        }
//    }

    private SimpleDateFormat getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

        //        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        //        calendar.setTime(date);
        //        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date date = calendar.getTime();


        String now = sdf.format(date);
        tv_start_time.setText(now);

        return sdf;

    }
}
