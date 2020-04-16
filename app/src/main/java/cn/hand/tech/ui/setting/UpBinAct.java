package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.hand.tech.BApplication;
import cn.hand.tech.R;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.ble.bleUtil.BluetoothUtil;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.setting.adapter.UpBinAdapter;
import cn.hand.tech.ui.setting.bean.BGuJianBean;
import cn.hand.tech.ui.setting.bean.CarNumberInfo;
import cn.hand.tech.ui.setting.bean.GuJianBean;
import cn.hand.tech.ui.setting.presenter.UpdateGujianPresenter;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.Aes;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/*
 *固件升级
 */
public class UpBinAct extends Activity implements IUpdateView {
    private Context context;
    private LinearLayout ll_back;
    private TextView tv_para_title;
    private Button bt_up_bin;
    private UpdateGujianPresenter mpresenter;
    private static final String PASSWORD_STRING = "6a9fd0d1a950420b812f27e970afcd8f"; // static final 静态常量
    private ACache acache;
    private String mdevId;
    private boolean isConnected;
    private int REQUEST_WRITE_EXTERNAL_STORAGE=2;
    private String mtoken;
    private TextView tv_bin_name,tv_bin_size;
    private BroadcastReceiver receiver;
    private GuJianBean mGJbean;
    private TextView tv_ready,tv_ready_success,tv_start_up;
    private TextView tv_bao_number;
    private TextView tv_bao_over,tv_bin_over,tv_all_over;
    private Button bt_upload_log;
    private String mStatus="99"; //0:等待升级、1:正在升级、2:升级成功、3:升级超时、-1:升级失败
    private String isUpper;//0准备  1开始  2 结束
    private ACache mcache;
    private AlertDialog adddialog;
    private String psw;
    private boolean isAgain=false;
    private CarNumberInfo mCarNumberInfo; //公司信息
    private String mbinInfoId=""; //选择的 固件id
    private TextView tv_company_name,tv_car_number,tv_dev_id,tv_software_version;
    private List<BGuJianBean> mgList=new ArrayList<>();
    private ExpandableListView exlist_1;
    private UpBinAdapter madapter;
    private AlertDialog tempDialog;
    private TextView tv_bin1_name,tv_bin_content;
    private LinearLayout ll_bin_1;
    private AlertDialog mtempDialog;

    public static void start(Context context) {
        Intent intent = new Intent(context, UpBinAct.class);
        context.startActivity(intent);
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    //                String md5=model.getMD5();
                    //                String md5Str= MD5.getMD5(str.toString());

                    //                if(md5.equalsIgnoreCase(md5Str)){  //MD5校验 数据是否正确
                    Intent binIntent=new Intent(BleConstant.ACTION_START_UPDATE_BIN);//开始升级
                    context.sendBroadcast(binIntent);
                    tv_ready.setVisibility(View.VISIBLE);
                    //                }else{
                    //                    tv_ready.setText("MD5校验不一致");
                    //                    tv_ready.setVisibility(View.VISIBLE);
                    //                }
                    break;
                case 2:
                    double number=mGJbean.getNumber();
                    String existStr= tv_bao_number.getText().toString();

                    int  xuhao=(int)number;
                    tv_bao_number.setVisibility(View.VISIBLE);
                    if(Tools.isEmpty(existStr)){
                        StringBuffer str=new StringBuffer();
                        str.append("0,"+xuhao);
                        tv_bao_number.setText(str.toString());
                    }else{
                        StringBuffer str1=new StringBuffer();
                        str1.append(existStr+","+xuhao);
                        tv_bao_number.setText(str1.toString());
                    }
                    break;
                case 10://登录成功  获取公司信息
                    if(isConnected){
                        HashMap<String,String> compMap=new HashMap<>();
                        compMap.put("token",mtoken);
                        compMap.put("deviceId",mdevId);//462328153
                        mpresenter.getCompanyInfo(compMap);
                    }else{
                        showTips("请先连接蓝牙");
                        return;
                    }

                    break;
                case 11://登录成功 获取固件列表
                    tv_company_name.setText(mCarNumberInfo.getParentName());//>?
                    tv_car_number.setText(mCarNumberInfo.getCarNumber());
                    tv_dev_id.setText(mCarNumberInfo.getDeviceId());
                    tv_software_version.setText(mCarNumberInfo.getHwVersion());

                    String companyid=mCarNumberInfo.getCompanyId();
                    String hmVerId=mCarNumberInfo.getHwVersion();
                    HashMap<String,String> listmap=new HashMap<>();
                    listmap.put("token",mtoken);
                    listmap.put("deviceId",mdevId);
                    listmap.put("companyId",companyid);//公司ID
                    listmap.put("hwVersion",hmVerId);//硬件版本号 4：V4 和 7:V7 和 8：V8
                    mpresenter.updateBinList(listmap);
                case 12:
                    madapter.updateListView(mgList);
                    //遍历所有group,将所有项设置成默认展开
                    int groupCount =mgList.size();
                    for (int i=0; i<groupCount; i++)
                    {
                        exlist_1.expandGroup(i);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_up_bin);
        acache= ACache.get(context,"WeightFragment");
        mcache= ACache.get(BApplication.mContext,"BIN");
        mdevId=  acache.getAsString("n_id");
        //        mdevId="462602692";
        mpresenter = new UpdateGujianPresenter(context, this);
        String isCon = acache.getAsString("is_connect");
        if ("2".equals(isCon)) {
            isConnected = true;
        } else {
            isConnected = false;
        }

        initView();
        doAddTruck();
        registerBrodcat();

    }

    private void initData(String binInfoId) {

        if(isConnected){
            doDownLoadJni(mtoken,mdevId,binInfoId);
        }else{
            showTips("请先连接蓝牙");
            return;
        }
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
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    private void initView() {
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("固件升级");
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_company_name=(TextView)findViewById(R.id.tv_company_name);
        tv_car_number=(TextView)findViewById(R.id.tv_car_number);
        tv_dev_id=(TextView)findViewById(R.id.tv_dev_id);
        tv_software_version=(TextView)findViewById(R.id.tv_software_version);

        tv_bin_name=(TextView)findViewById(R.id.tv_bin_name);
        tv_bin_size=(TextView)findViewById(R.id.tv_bin_size);
        bt_up_bin=(Button)findViewById(R.id.bt_up_bin);
        bt_up_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(context);
            }
        });

        tv_ready=(TextView)findViewById(R.id.tv_ready);
        tv_ready.setVisibility(View.GONE);
        tv_ready_success=(TextView)findViewById(R.id.tv_ready_success);
        tv_ready_success.setVisibility(View.GONE);
        tv_start_up=(TextView)findViewById(R.id.tv_start_up);
        tv_start_up.setVisibility(View.GONE);
        tv_bao_number=(TextView)findViewById(R.id.tv_bao_number);
        tv_bao_number.setVisibility(View.GONE);

        tv_bao_over=(TextView)findViewById(R.id.tv_bao_over);
        tv_bao_over.setVisibility(View.GONE);
        tv_bin_over=(TextView)findViewById(R.id.tv_bin_over);
        tv_bin_over.setVisibility(View.GONE);
        tv_all_over=(TextView)findViewById(R.id.tv_all_over);
        tv_all_over.setVisibility(View.GONE);

        ll_bin_1=(LinearLayout)findViewById(R.id.ll_bin_1);
        ll_bin_1.setVisibility(View.GONE);
        tv_bin1_name=(TextView)findViewById(R.id.tv_bin1_name);
        tv_bin_content=(TextView)findViewById(R.id.tv_bin_content);

        bt_upload_log=(Button)findViewById(R.id.bt_upload_log);
        bt_upload_log.setVisibility(View.GONE);
        bt_upload_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("1".equals(mStatus) || "0".equals(mStatus) ){
                    showTips("固件升级还未完成，请等待...");
                    return;
                }
                String gid=mGJbean.getId();
                HashMap<String,String> hmap=new HashMap<>();
                hmap.put("token",mtoken);
                hmap.put("deviceId",mdevId);
                hmap.put("binId",gid);//固件ID
                hmap.put("content","安卓升级固件");//操作内容
                if(Tools.isEmpty(mStatus)){
                    mStatus="0";
                }
                hmap.put("status",mStatus);//0:等待升级、1:正在升级、2:升级成功、3:升级超时、-1:升级失败
                mpresenter.upLoadLog(hmap);
            }
        });

        exlist_1=(ExpandableListView)findViewById(R.id.exlist_1);
        madapter=new UpBinAdapter(context, mgList, new UpBinAdapter.Callback() {
            @Override
            public void onTocheck(View view, int position,String id) {
                madapter.setCheck(position);
                if(mgList !=null && mgList.size() >0){
                    String binId=id;
                    mbinInfoId=binId;//固件ID
                    GuJianBean bean= mgList.get(0).getList().get(position);
                    String binname=bean.getBinName();
                    tv_bin1_name.setText("固件名称:"+binname);
                    String binContent=bean.getDescription();
                    tv_bin_content.setText("固件描述:"+binContent);
                    ll_bin_1.setVisibility(View.VISIBLE);
                }

            }
        });
        exlist_1.setGroupIndicator(null);//不使用系统提供的展开和收起的图标  左边有个下的图标
        exlist_1.setAdapter(madapter);
        int groupCount =mgList.size();

        for (int i=0; i<groupCount; i++) {

            exlist_1.expandGroup(i);

        }
    }
    //开始升级固件
    public void  doUpperBin(){
        if(mStatus.equals("1")){
            showTips("正在升级，请等待...");
            return;
        }
        clearView();
        if(!Tools.isEmpty(mbinInfoId)){

            initData(mbinInfoId);
        }else{
            showTips("请选择固件");
            return;
        }

        isUpper="0";//准备升级
        mcache.put("bin_upper",isUpper);
        mStatus="0";
    }

    public void  clearView(){
        tv_bin_name.setText("");
        tv_bin_size.setText("");
        tv_ready.setVisibility(View.GONE);
        tv_ready_success.setVisibility(View.GONE);
        tv_start_up.setVisibility(View.GONE);
        tv_bao_number.setVisibility(View.GONE);

        tv_bao_over.setVisibility(View.GONE);
        tv_bin_over.setVisibility(View.GONE);
        tv_all_over.setVisibility(View.GONE);
    }



    @Override
    public void doSuccess(final GuJianBean model) {
        //
        //        String dirPath = FileUtils.getAppSdcardDir() + "/" + "bg_image" + "/";
        //        String filename=mdevId+".txt";
        //        File file=new File(dirPath+filename);
        try{
            if(model !=null){
                tv_bin_name.setText(model.getBinName());
                tv_bin_size.setText(model.getBinSize());

                List<String> btey=model.getBinBytesStr();
                DLog.e("SendStartUpdate","SendStartUpdate:" + "====》btey: "+btey.size());
                StringBuffer str=new StringBuffer();
                for(int u=0;u<btey.size();u++){
                    str.append(btey.get(u));
                }
                byte[] bin_by = BluetoothUtil.hex2Bytes(str.toString());
                double size=Double.parseDouble(bin_by.length+"");
                double yu=size/1024d;
                double bao=Math.ceil(yu);
                int bao1=(int)bao;
                model.setPageNumber(bao1);
                model.setBinStr(str.toString());

                mGJbean=model;
                acache.put("start_bin",(Serializable)model);//数据量太大不能通过 广播传输
                mHandler.sendEmptyMessage(1);


                DLog.e("UpBinAct","TAG_SERVICE"+ "下载固件数据成功:"+bao1);



            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void doError(String msg) {
        showTips(msg);
    }

    @Override
    public void doLogin(UserResultBean bean) {

        closedialog();
        showTips("登录成功");
        UserResultBean userBean=bean;
        mtoken=bean.getToken();
        String uname=bean.getResult().getUserName();

        acache.put("login_name",uname);
        acache.put("login_token",mtoken);
        acache.put("log_psw",psw);
        if(isAgain){
            //自动上传日志
            uploadLog();
            isAgain=false;
        }else{
            mHandler.sendEmptyMessage(10);
        }
    }

    private void doDownLoadJni(String token,String mdevId,String mbinInfoId) {
        HashMap<String, String> bin1 = new HashMap<>();
        bin1.put("token", token);
        bin1.put("deviceId", mdevId);
        bin1.put("binInfoId", mbinInfoId);
        mpresenter.downLoadText(bin1,mdevId);
    }

    @Override
    public void doLoginFail(String bean) {
        showTips(bean);
        finish();
    }

    @Override
    public void doLOG(String bean) {
        //        showTips("日志上传成功");
        mStatus="99";//恢复初始状态

        bt_up_bin.setText("日志上传成功");
        bt_up_bin.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_hui));


        showDialogSuccess(context,"升级成功");
    }

    @Override
    public void doLogFail(String bean) {
        showTips(bean);
        if(!Tools.isEmpty(bean)){
            if(bean.contains("失败")|| bean.contains("重新登录")){
                isAgain=true;
                doAddTruck();
            }
        }
    }

    @Override
    public void doBinList(List<GuJianBean> bean) { //固件列表
        if(bean !=null && bean.size() >0){

            List<BGuJianBean> mlist=new ArrayList<>();
            BGuJianBean gbean=new BGuJianBean();
            gbean.setName("固件列表");
            gbean.setList(bean);
            mlist.add(gbean);

            mgList=mlist;
            mHandler.sendEmptyMessage(12);

            DLog.e("BGuJianBean","固件列表=="+bean.size());
        }

    }

    @Override
    public void doBinListFail(String bean) {

    }

    @Override
    public void doCompanySuccess(CarNumberInfo companyResult) {
        if(companyResult !=null){
            mCarNumberInfo=companyResult;
            mHandler.sendEmptyMessage(11);
        }else{
            showTips("公司信息为空");
        }
    }

    @Override
    public void doCompanyError(String msg) {

    }


    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(action.equals(BleConstant.ACTION_UPDATE_BIN_SUCCESS)){
                    DLog.e("UpBinAct","ble_brodcast"+ "准备升级应答成功");//开始上传bin文件
                    tv_ready_success.setVisibility(View.VISIBLE);

                    mGJbean.setNumber(0);
                    Intent binIntent0=new Intent(BleConstant.ACTION_UPDATE_BIN);//正式升级
                    binIntent0.putExtra("start_bin_1",0d);
                    context.sendBroadcast(binIntent0);
                    double bao=mGJbean.getPageNumber(); //包数量
                    tv_start_up.setText("开始上传固件包"+"("+bao+")"+"...");
                    tv_start_up.setVisibility(View.VISIBLE);

                    isUpper="1";//准备升级
                    mcache.put("bin_upper",isUpper);
                    mStatus="1";

                    bt_up_bin.setClickable(false);
                    bt_up_bin.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_hui));

                }else if(action.equals(BleConstant.ACTION_ING_BIN_SUSSESS)){ //成功应答
                    String number=intent.getStringExtra("bin_number");
                    double num=Double.parseDouble(number)+1d;
                    double pageNuber=mGJbean.getPageNumber();
                    mStatus="1";
                    if(num >= pageNuber){
                        DLog.e("UpBinAct","bin_number"+ "bin包上传完毕："+num);//开始上传bin文件
                        tv_bao_over.setVisibility(View.GONE);//
                        mGJbean.setNumber(num);
                        Intent binIntent2=new Intent(BleConstant.ACTION_BIN_OVER);//正式升级
                        context.sendBroadcast(binIntent2);
                        isUpper="2";//完成升级
                        mcache.put("bin_upper",isUpper);

                    }else{
                        DLog.e("UpBinAct","bin_number"+ "开始固件升级成功："+num);//开始上传bin文件
                        mGJbean.setNumber(num);
                        Intent binIntent1=new Intent(BleConstant.ACTION_UPDATE_BIN);//正式升级
                        binIntent1.putExtra("start_bin_1",num);
                        context.sendBroadcast(binIntent1);
                        mHandler.sendEmptyMessage(2);

                    }

                }else if(action.equals(BleConstant.ACTION_BIN_OVER_STATUS)){
                    String binstatus= intent.getStringExtra("over_bstatus");

                    //0：CRC16校验出错
                    //1:升级文件不全
                    //2:Flash无法写入
                    //3:升级成功
                    //4:固件版本不支持（固件和硬件版本不一致）
                    //5:下载完成
                    DLog.e("UpBinAct","over_bstatus"+ "升级成功："+binstatus);//开始上传bin文件
                    float statsu=Float.parseFloat(binstatus);
                    if(statsu==3){
                        mStatus="2";
                        showTips("----[升级成功]----");
                        tv_all_over.setText("----[升级成功]----");
                        tv_all_over.setVisibility(View.VISIBLE);
                        tv_all_over.setTextColor(context.getResources().getColor(R.color.green_fanxian));

                        bt_up_bin.setText("升级成功");
                        bt_up_bin.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_hui));
                    }else {
                        mStatus="-1";
                        showTips("----[升级失败]----");
                        tv_all_over.setText("----[升级失败]----");
                        tv_all_over.setVisibility(View.VISIBLE);
                        tv_all_over.setTextColor(context.getResources().getColor(R.color.red));

                        if(statsu==0){
                            bt_up_bin.setText("CRC16校验出错");
                        }else if(statsu==1){
                            bt_up_bin.setText("升级文件不全");
                        } else if (statsu==2) {
                            bt_up_bin.setText("Flash无法写入");
                        }else if(statsu==4){
                            bt_up_bin.setText("固件版本不支持（固件和硬件版本不一致）");
                        }else if(statsu==5){
                            bt_up_bin.setText("下载完成");
                        }else{
                            bt_up_bin.setText("未知异常");
                        }
                        bt_up_bin.setBackground(context.getResources().getDrawable(R.drawable.shape_bg_hui));
                    }
                    //自动上传日志
                    uploadLog();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTION_UPDATE_BIN_SUCCESS);
        filter.addAction(BleConstant.ACTION_ING_BIN_SUSSESS);
        filter.addAction(BleConstant.ACTION_BIN_OVER_STATUS);
        registerReceiver(receiver, filter);
    }


    //上传日志
    public void uploadLog(){
        String gid=mGJbean.getId();
        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("token",mtoken);
        hmap.put("deviceId",mdevId);
        hmap.put("binId",gid);//固件ID
        hmap.put("content","安卓升级固件");//操作内容
        if(Tools.isEmpty(mStatus)){
            mStatus="0";
        }
        hmap.put("status",mStatus);//0:等待升级、1:正在升级、2:升级成功、3:升级超时、-1:升级失败
        mpresenter.upLoadLog(hmap);
    }

    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
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

    /**
     * 关闭进度对话框
     */
    private void closedialog() {
        if (adddialog != null) {
            adddialog.dismiss();
            adddialog = null;
        }
    }


    //开始升级固件提示框
    public void showDialog(final Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_start_binup, null);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpperBin(); //开始升级固件
                if(tempDialog !=null){
                    tempDialog.dismiss();
                }
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

    //开始升级固件提示框
    public void showDialogSuccess(final Context context,String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_start_binup, null);
        TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
        tv_title.setText(msg);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mtempDialog !=null){
                    mtempDialog.dismiss();
                }
                finish();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mtempDialog !=null){
                    mtempDialog.dismiss();
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        mtempDialog = dialog.create();
        mtempDialog.setView(view, 0, 0, 0, 0);
        mtempDialog.show();
    }

}