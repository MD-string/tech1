package cn.hand.tech.ui.weight;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDKRModel;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.OnMyItemClickListener;
import cn.hand.tech.common.OnTwoClickListener;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.weight.bean.AddTruckInfo;
import cn.hand.tech.ui.weight.bean.CompanyBean;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.bean.TruckChildBean;
import cn.hand.tech.ui.weight.bean.UserfulData;
import cn.hand.tech.ui.weight.bean.UserfulTwoData;
import cn.hand.tech.ui.weight.presenter.BasicPresenter;
import cn.hand.tech.utils.CameraUtil;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.FileUtils;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.BottomSelectPopupWindow;
import cn.hand.tech.weiget.CustomDialog;
import cn.hand.tech.weiget.TwoWheelSelectPopupWindow;


/**
 * A simple {@link Fragment} subclass.
 * describe:添加车辆页  基本信息 页面
 */
public class AddTruckBasicActivity extends Activity implements View.OnClickListener,IBasicView {

    private String companyId = "";
    private String token;
    private List<CompanyBean> companyList = new ArrayList<>();
    private Context mContext;
    private BasicPresenter mpresenter;
    private OptionsPickerView pvCustomOptions;
    private boolean isConnect;
    private String  deviceId;
    private CompanyBean mCompanyBean;//当前选择的公司
    private static final String TAG = "WeightFragment";
    private EditText  et_count, et_phone;
    private TextView et_company,et_stuck_number;
    private TextView et_id;
    private Button btn_next;
    private int REQUEST_WRITE_EXTERNAL_STORAGE=22;
    public static final int REQUEST_CODE_CAMERA_P = 28;
    private ACache acache;
    private LinearLayout ll_back;
    private EditText et_thruck_type;
    private Spinner spinner_zhou, spinner_moto_type;
    private String zhou, mType;
    private String truckNum;
    private boolean isEnter;
    private CustomDialog mdialog;
    private AlertDialog adddialog;
    private String unitStr;

    private String zhou_position,mType_position;
    private AlertDialog tempDialog,mtempDialog;
    private String passCheckStr; //0 表示 通道检测通过 ，可以走完全流程   1表示 通道检测未通过，只能填基本信息

    private TextView tv_useful;
    private TwoWheelSelectPopupWindow pupWindow;
    private List<UserfulData> ulist;


    private LinearLayout ll_all;
    private String parentCode,childCode;

    private TextView tv_channeal,tv_channeal_number;
    private TruckChildBean truckModel;
    private ImageView img_add;
    private BottomSelectPopupWindow pickPhotoDlg;
    private File cameraFile;
    private String mpath;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mpath=(String)msg.obj;
                    Bitmap map=getBitmap(mpath,100,100);
                    img_add.setImageBitmap(map);
                    //                    mpresenter.sendPersonalHeader(cameraFile.getAbsolutePath(),deviceId,truckNum,token,companyId);
                    break;
            }
        }
    };
    private EditText et_dirver_name;
    private BroadcastReceiver receiver;
    private TextView tv_hard_soft,tv_gu_soft;
    private Button btn_1,btn_2;
    private AlertDialog nexttempDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.fragment_truck_add_basic);
        acache = ACache.get(mContext, TAG);
        token = acache.getAsString("login_token");
        truckNum= acache.getAsString("car_num");
        deviceId = getIntent().getStringExtra("device_id");
        passCheckStr= getIntent().getStringExtra("is_only_carInput");
        if(!Tools.isEmpty(deviceId)){
            acache.put("dev_id",deviceId);
        }else{
            acache.put("dev_id","0");
        }
        isConnect = getIntent().getBooleanExtra("is_connected", false);
        mpresenter = new BasicPresenter(mContext, this);


        findViews();
        initCustomOptionPicker();
        setListeners();
        initData();
        registerBrodcat();

        Intent i=new Intent(BleConstant.ACTION_READ_HARD_SOFT);
        int dvid=Integer.parseInt(deviceId);
        i.putExtra("n_id",dvid);
        sendBroadcast(i);
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
        Bundle bundle =intent.getExtras();
        truckModel = (TruckChildBean) bundle.getSerializable("truckModel");
        if(truckModel !=null){
            String name=truckModel.getName();
            companyId=truckModel.getChildId();
            if(!Tools.isEmpty(name)){
                et_company.setText(name+"");
            }
        }
    }

    protected void findViews() {
        ll_all=(LinearLayout)findViewById(R.id.ll_all);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        et_id = (TextView) findViewById(R.id.et_id);
        et_stuck_number = (TextView) findViewById(R.id.et_stuck_number);
        et_stuck_number.setText(truckNum);
        et_company = (TextView) findViewById(R.id.et_company);
        et_count = (EditText) findViewById(R.id.et_count);
        et_dirver_name=(EditText)findViewById(R.id.et_dirver_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_thruck_type = (EditText) findViewById(R.id.et_thruck_type);

        tv_hard_soft =(TextView)findViewById(R.id.tv_hard_soft); //硬件版本
        btn_1=(Button)findViewById(R.id.btn_1);

        tv_gu_soft=(TextView)findViewById(R.id.tv_gu_soft); //固件版本
        btn_2=(Button)findViewById(R.id.btn_2);

        spinner_zhou = (Spinner) findViewById(R.id.spinner_zhou);
        tv_channeal=(TextView)findViewById(R.id.tv_channeal);
        String channelStr= getChanneal();
        tv_channeal.setText(channelStr);

        tv_channeal_number=(TextView)findViewById(R.id.tv_channeal_number);
        String[] ashu= channelStr.split(",");
        int size=ashu.length;
        tv_channeal_number.setText(size+"");

        spinner_moto_type = (Spinner) findViewById(R.id.spinner_moto_type);
        tv_useful=(TextView)findViewById(R.id.tv_useful);//用途
        btn_next = (Button) findViewById(R.id.btn_next);

        et_id.setText(deviceId);
        mpresenter.checkDeviceExsit(deviceId,token);
        AddTruckInfo bean=(AddTruckInfo)acache.getAsObject("add_truck");
        if(bean !=null){
            String weight=bean.getWeight();
            if(Tools.isEmpty(weight)){
                et_count.setText(weight);
            }

            String carType=bean.getTruckType();
            if(Tools.isEmpty(carType)){
                et_thruck_type.setText(carType);
            }
        }

        //        String mnPostion=  acache.getAsString("mn_positon");
        //        if(!Tools.isEmpty(mnPostion)){
        //            spinner_moto_no.setSelection(Integer.parseInt(mnPostion));
        //        }else{
        //            spinner_moto_no.setSelection(2);
        //        }

        String zhPostion=acache.getAsString("zh_positon");
        if(!Tools.isEmpty(zhPostion)){
            spinner_zhou.setSelection(Integer.parseInt(zhPostion));
        }else{
            spinner_zhou.setSelection(5);
        }
        String mtPostion=  acache.getAsString("mt_positon");
        if(!Tools.isEmpty(mtPostion)){
            spinner_moto_type.setSelection(Integer.parseInt(mtPostion));
        }else{
            spinner_moto_type.setSelection(0);
        }

        img_add=(ImageView)findViewById(R.id.img_add);

        //        et_id.addTextChangedListener(new TextWatcherImpl(){  //方便测试
        //            @Override
        //            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        //                btn_next.setEnabled(true);
        //                btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_round_corner));
        //                initCompanyList();
        //            }
        //        });
        initCompanyList();
    }

    /*获取公司列表信息*/
    private void initCompanyList() {
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("version", "2");
        mpresenter.getCompanyList(mapParams);

    }
    protected void setListeners() {
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        img_add.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        et_company.setOnClickListener(this);
        tv_useful.setOnClickListener(new View.OnClickListener() {//用途
            @Override
            public void onClick(View v) {
                doShowPubwindow();
            }
        });


        spinner_zhou.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] zhouNum = getResources().getStringArray(R.array.zhounumber);
                zhou = zhouNum[position];
                zhou_position=position+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_moto_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] mototype = getResources().getStringArray(R.array.mototype);
                mType = mototype[position];
                mType_position=position+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initData() {
        ulist=new ArrayList<>();
        UserfulData b1=new UserfulData();
        b1.setParentCode("01");
        List<UserfulTwoData> cbean1=new ArrayList<>();
        UserfulTwoData u1=new UserfulTwoData();
        u1.setChildCode("0101");
        u1.setName("散装水泥罐车");
        cbean1.add(u1);

        UserfulTwoData u2=new UserfulTwoData();
        u2.setChildCode("0102");
        u2.setName("袋装水泥汽车");
        cbean1.add(u2);

        UserfulTwoData u3=new UserfulTwoData();
        u3.setChildCode("0103");
        u3.setName("混凝土搅拌车");
        cbean1.add(u3);

        UserfulTwoData u4=new UserfulTwoData();
        u4.setChildCode("0104");
        u4.setName("袋装平板挂车");
        cbean1.add(u4);

        UserfulTwoData u5=new UserfulTwoData();
        u5.setChildCode("0100");
        u5.setName("其他");
        cbean1.add(u5);

        b1.setV1(cbean1);
        ulist.add(b1);

        UserfulData b2=new UserfulData();
        b2.setParentCode("02");
        List<UserfulTwoData> cbean2=new ArrayList<>();

        UserfulTwoData u21=new UserfulTwoData();
        u21.setChildCode("0201");
        u21.setName("侧挂式垃圾车");
        cbean2.add(u21);

        UserfulTwoData u22=new UserfulTwoData();
        u22.setChildCode("0202");
        u22.setName("压缩式垃圾车");
        cbean2.add(u22);

        UserfulTwoData u23=new UserfulTwoData();
        u23.setChildCode("0203");
        u23.setName("勾臂车垃圾车");
        cbean2.add(u23);

        UserfulTwoData u24=new UserfulTwoData();
        u24.setChildCode("0204");
        u24.setName("摆臂式垃圾车");
        cbean2.add(u24);

        UserfulTwoData u25=new UserfulTwoData();
        u25.setChildCode("0205");
        u25.setName("餐厨式垃圾车");
        cbean2.add(u25);

        UserfulTwoData u26=new UserfulTwoData();
        u26.setChildCode("0206");
        u26.setName("尾板垃圾车[4.2M]");
        cbean2.add(u26);

        UserfulTwoData u27=new UserfulTwoData();
        u27.setChildCode("0207");
        u27.setName("尾板垃圾车[4.2M不封顶]");
        cbean2.add(u27);

        UserfulTwoData u28=new UserfulTwoData();
        u28.setChildCode("0208");
        u28.setName("尾板垃圾车[8桶]");
        cbean2.add(u28);

        UserfulTwoData u29=new UserfulTwoData();
        u29.setChildCode("0209");
        u29.setName("三轮垃圾车");
        cbean2.add(u29);

        UserfulTwoData u20=new UserfulTwoData();
        u20.setChildCode("0200");
        u20.setName("其他");
        cbean2.add(u20);
        b2.setV1(cbean2);
        ulist.add(b2);

        UserfulData b3=new UserfulData();
        b3.setParentCode("03");
        List<UserfulTwoData> cbean3=new ArrayList<>();

        UserfulTwoData u31=new UserfulTwoData();
        u31.setChildCode("0301");
        u31.setName("箱式货车[4.2M]");
        cbean3.add(u31);


        UserfulTwoData u32=new UserfulTwoData();
        u32.setChildCode("0302");
        u32.setName("箱式货车[7.6M]");
        cbean3.add(u32);

        UserfulTwoData u33=new UserfulTwoData();
        u33.setChildCode("0303");
        u33.setName("箱式货车[9.6M]");
        cbean3.add(u33);

        UserfulTwoData u34=new UserfulTwoData();
        u34.setChildCode("0304");
        u34.setName("依维柯");
        cbean3.add(u34);

        UserfulTwoData u30=new UserfulTwoData();
        u30.setChildCode("0300");
        u30.setName("其他");
        cbean3.add(u30);

        b3.setV1(cbean3);
        ulist.add(b3);

        UserfulData b4=new UserfulData();
        b4.setParentCode("04");
        List<UserfulTwoData> cbean4=new ArrayList<>();

        UserfulTwoData u41=new UserfulTwoData();
        u41.setChildCode("0401");
        u41.setName("碴土车[2轴]");
        cbean4.add(u41);

        UserfulTwoData u42=new UserfulTwoData();
        u42.setChildCode("0402");
        u42.setName("碴土车[3轴]");
        cbean4.add(u42);

        UserfulTwoData u43=new UserfulTwoData();
        u43.setChildCode("0403");
        u43.setName("碴土车[4轴]");
        cbean4.add(u43);

        UserfulTwoData u40=new UserfulTwoData();
        u40.setChildCode("0400");
        u40.setName("其他");
        cbean4.add(u40);
        b4.setV1(cbean4);
        ulist.add(b4);


        UserfulData b5=new UserfulData();
        b5.setParentCode("05");
        List<UserfulTwoData> cbean5=new ArrayList<>();

        UserfulTwoData u51=new UserfulTwoData();
        u51.setChildCode("0501");
        u51.setName("箱式挂车");
        cbean5.add(u51);

        UserfulTwoData u52=new UserfulTwoData();
        u52.setChildCode("0502");
        u52.setName("平板挂车");
        cbean5.add(u52);


        UserfulTwoData u50=new UserfulTwoData();
        u50.setChildCode("0500");
        u50.setName("其他");
        cbean5.add(u50);
        b5.setV1(cbean5);
        ulist.add(b5);

        UserfulData b6=new UserfulData();
        b6.setParentCode("00");
        List<UserfulTwoData> cbean6=new ArrayList<>();
        UserfulTwoData u00=new UserfulTwoData();
        u00.setChildCode("0000");
        u00.setName("其他");
        cbean6.add(u00);
        b6.setV1(cbean6);
        ulist.add(b6);
    }

    //显示用途选择框
    private void doShowPubwindow() {

        pupWindow=new TwoWheelSelectPopupWindow(this,itemsOnClick);
        pupWindow.showAtLocation(ll_all, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    //为弹出窗口实现监听类
    private OnTwoClickListener itemsOnClick = new OnTwoClickListener(){

        public void onClick(View v,int parent,int child) {
            if(pupWindow !=null ){
                pupWindow.dismiss();
            }
            UserfulData bean= ulist.get(parent);
            parentCode=bean.getParentCode();
            childCode=bean.getV1().get(child).getChildCode();
            tv_useful.setText(bean.getV1().get(child).getName());
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back://返回
                finish();
                break;
            case R.id.btn_1://硬件版本号
                if(!isConnect){
                    showTips("请连接蓝牙");
                    return;
                }
                Intent i=new Intent(BleConstant.ACTION_READ_HARD_SOFT);
                int dvid=Integer.parseInt(deviceId);
                i.putExtra("n_id",dvid);
                sendBroadcast(i);
                break;
            case R.id.btn_2://固件版本号
                if(!isConnect){
                    showTips("请连接蓝牙");
                    return;
                }
                Intent i2=new Intent(BleConstant.ACTION_READ_SOFT);
                int id2=Integer.parseInt(deviceId);
                i2.putExtra("n_id2",id2);
                sendBroadcast(i2);
                break;
            case R.id.img_add://添加照片
                try{
                    checkPermission();//开始拍照
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.et_company://公司
                hideKeyBorad();
                //                if(companyList !=null && companyList.size() > 0){
                //                    pvCustomOptions.show(); //弹出自定义条件选择器
                //                }else{
                //                    if(isEnter){
                //                        showTips("该设备已录入");
                //                    }else{
                //                        showTips("公司列表下载失败");
                //                    }
                //                }
                CompanyTruckActivity.start(mContext);
                break;
            case R.id.btn_next://下一步
                try{


                    if(CommonUtils.isFastDoubleClick()){
                        return;
                    }
                    AddTruckInfo bean = new AddTruckInfo();
                    String id = et_id.getText().toString().trim();
                    if(Tools.isEmpty(id)){
                        showTips("ID不能为空");
                        return;
                    }

                    bean.setId(id);

                    String stuckNumber = et_stuck_number.getText().toString().trim();
                    if(Tools.isEmpty(stuckNumber)){
                        showTips("车牌不能为空");
                        return;
                    }

                    //                    acache.put("car_number",stuckNumber);

                    bean.setTruckNumber(stuckNumber);

                    String commpany = et_company.getText().toString().trim();
                    if (!Tools.isEmpty(commpany)) {
                        bean.setCompany(commpany);
                    }
                    String stuckCount = et_count.getText().toString().trim();
                    if (!Tools.isEmpty(stuckCount)) {
                        bean.setWeight(stuckCount);
                    }
                    String stuckPhone = et_phone.getText().toString().trim();//手机号
                    if (!Tools.isEmpty(stuckPhone)) {
                        int leng=stuckPhone.length();
                        if(leng <6 || leng >11 ){
                            showTips("手机号不正确");
                            return;
                        }
                        String str2=stuckPhone.replace(" ", ""); //去掉空格
                        bean.setPhone(str2);
                    }
                    if (Tools.isEmpty(companyId)) {
                        showTips("请选择公司");
                        return;
                    }

                    String softVer=tv_hard_soft.getText().toString().trim(); //硬件版本号
                    String fmVer=tv_gu_soft.getText().toString().trim();     //固件版本号

                    if(Tools.isEmpty(softVer)){
                        showTips("硬件版本号不能为空");
                        return;
                    }

                    if(Tools.isEmpty(fmVer)){
                        showTips("固件版本号不能为空");
                        return;
                    }

                    String usfulTxt = tv_useful.getText().toString().trim();//用途
                    if (Tools.isEmpty(usfulTxt)) {
                        showTips("请选择用途");
                        return;
                    }
                    if(Tools.isEmpty(mpath)){
                        showTips("请先拍照");
                        return;
                    }


                    String stuckType = et_thruck_type.getText().toString().trim();
                    if (!Tools.isEmpty(stuckType)) {
                        bean.setTruckType(stuckType);
                    }
                    if (!Tools.isEmpty(zhou)) {
                        bean.setAlxesNumber(zhou);
                    }
                    String monu=tv_channeal_number.getText().toString();
                    if (!Tools.isEmpty(monu)) {
                        bean.setSensorNumber(monu);
                    }
                    if (!Tools.isEmpty(mType)) {
                        bean.setSensorType(mType);
                    }

                    String unit=acache.getAsString("weight_unit"); //单位
                    if(Tools.isEmpty(unit) || "吨".equals(unit)){
                        unitStr="1";
                    }else{
                        unitStr="2";
                    }
                    if(Tools.isEmpty(parentCode) ||  Tools.isEmpty(childCode) ){
                        DLog.d("AddTruckBasicActivity","选择用途代码为空");
                    }


                    String channelStr= getChanneal();
                    String dirverName=et_dirver_name.getText().toString().trim();

                    String path=cameraFile.getAbsolutePath();

                    mpresenter.sendBasicData(bean, token,softVer,fmVer,channelStr,companyId,unitStr,parentCode,childCode,dirverName,path);

                    acache.put("add_truck",(Serializable)bean);
                    acache.put("zh_positon",zhou_position);
                    acache.put("mt_positon",mType_position);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

        }
    }
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                //                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_WRITE_EXTERNAL_STORAGE);

        } else {//已经授权

            photoDialogPop();//下载文档

            DLog.e(TAG,"TAG_SERVICE"+ "checkPermission: 已经授权！");
        }
    }

    //系统方法,从requestPermissions()方法回调结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //确保是我们的请求
        if(requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    photoDialogPop();
                }
            });
        }
    }

    /**
     * 弹出选择图片方式询问
     */
    public void  photoDialogPop() {
        try{
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
        }catch ( Exception e){
            e.printStackTrace();
        }
    }

    private void pickToCamera() {
        try{
            cameraFile = new File(FileUtils.getAppSdcardDir() + "/" + System.currentTimeMillis() + ".jpg");
            cameraFile.getParentFile().mkdirs();
            //        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), REQUEST_CODE_CAMERA_P);

            CameraUtil.openCamera(this,cameraFile,REQUEST_CODE_CAMERA_P);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA_P) {// 返回拍照结果
            if (cameraFile != null && cameraFile.exists()) {
                //                ArrayList<String> path = new ArrayList<String>();
                //                path.add(cameraFile.getAbsolutePath());
                //                Intent intent = new Intent(KeyConstants.BROADCAST_PATH);
                //                intent.putExtra("path", path);
                //                //                intent.putExtra(KeyConstants.IMAGE_FROM_FLAG, from_Flag);
                //                sendBroadcast(intent);

                String picPath=cameraFile.getAbsolutePath();
                Message msg=new Message();
                msg.what=1;
                msg.obj=picPath;
                mHandler.sendMessage(msg);
            }else{
                DLog.d(TAG,"拍照结果返回有問題");

            }
        }
    }


    //获取当前传感器通道
    public String getChanneal(){
        HDKRModel channelModel = (HDKRModel) acache.getAsObject("channel_model");//保存多少通道

        StringBuffer sensorChannel=new StringBuffer();
        if(channelModel !=null){
            if(channelModel.bChannel1){
                sensorChannel.append("1,");
            }
            if(channelModel.bChannel2){
                sensorChannel.append("2,");
            }

            if(channelModel.bChannel3){
                sensorChannel.append("3,");
            }

            if(channelModel.bChannel4){
                sensorChannel.append("4,");
            }

            if(channelModel.bChannel5){
                sensorChannel.append("5,");
            }

            if(channelModel.bChannel6){
                sensorChannel.append("6,");
            }
            if(channelModel.bChannel7){
                sensorChannel.append("7,");
            }

            if(channelModel.bChannel8){
                sensorChannel.append("8,");
            }

            if(channelModel.bChannel9){
                sensorChannel.append("9,");
            }
            if(channelModel.bChannel10){
                sensorChannel.append("10,");
            }

            if(channelModel.bChannel11){
                sensorChannel.append("11,");
            }

            if(channelModel.bChannel12){
                sensorChannel.append("12,");
            }
            if(channelModel.bChannel13){
                sensorChannel.append("13,");
            }
            if(channelModel.bChannel14){
                sensorChannel.append("14,");
            }
            if(channelModel.bChannel15){
                sensorChannel.append("15,");
            }
            if(channelModel.bChannel16){
                sensorChannel.append("16,");
            }
        }
        String channelStr=sensorChannel.toString();
        String senStr="";
        if(!Tools.isEmpty(channelStr)){
            senStr= channelStr.substring(0,channelStr.length()-1);
        }
        return senStr;
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


    public void showTips(String tip) {
        ToastUtil.getInstance().showCenterMessage(mContext, tip);
    }

    @Override
    public void doSuccess(String msg) {  //检查ID是否已经录入
        mHandler.sendEmptyMessage(1);
        if ("0".equals(msg)) {
//            showTips("该设备已经录入");
            btn_next.setEnabled(false);
            isEnter=true;
            btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_round_corner_hui));

////            //弹出对话框
////            if("0".equals(passCheckStr)){
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showNextDialog(mContext,passCheckStr);
//                    }
//                },100);
//            }
            mpresenter.checkDeviceExsit(deviceId,token);
        } else {
//            showTips("该设备未被录入");
            isEnter=false;
            btn_next.setEnabled(true);
            btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_round_corner));
        }

    }

    @Override
    public void doCheckAPISuccess(String msg, String msg1) {  //msg车辆是否录入    录入：0    没录入：1
        //msg1  //是否存在安装信息   存在安装记录：0      不存在安装记录：1
        mHandler.sendEmptyMessage(1);

        if("1".equals(msg)){//未录入基本信息
            isEnter=false;
            btn_next.setEnabled(true);
            btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_round_corner));

            if("0".equals(msg1)){
                DLog.d("doCheckAPISuccess", "doCheckAPISuccess录车异常");
            }

        }else{ //录入基本信息
            btn_next.setEnabled(false);
            isEnter=true;
            btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_round_corner_hui));

            if("1".equals(msg1)){ //补录安装人员信息
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNext(mContext,passCheckStr);
                    }
                },100);
            }else{             //基本信息 安装人员信息 都录入完成
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNextDialog(mContext,"1");
                    }
                },100);
            }
        }

    }

    @Override
    public void doError(String msg) {
        showTips(msg);
    }

    @Override
    public void doCompanySuccess(CompanyResultBean bean) {
        CompanyResultBean companyResult = bean;
        //        companyList = companyResult.getResult();
        //        pvCustomOptions.setPicker(companyList);//添加数据
        acache.put("company_truck",(Serializable)companyResult);
        DLog.d("AddTruckActivity", "下载公司列表成功");
    }

    @Override
    public void doEnterCar(String msg) {
        parentCode="";
        childCode="";
        //        InformationBasicActivity.start(mContext);
        //        acache.put("",et_id.getText().toString().trim());

        //        AddTruckInfo bean=(AddTruckInfo)acache.getAsObject("add_truck");//????/
        //        if(bean !=null){
        //
        //        }
        //        finish();

        //        showTips("基本信息添加成功");//1.7.0
        //        if("0".equals(passCheckStr)){ //1.7.0
        //            showNextDialog(mContext);
        //        }else{
        //            finish();
        //        }
        showNext(mContext,passCheckStr);
    }


    @Override
    public void onlineSuccess(String msg) { //是否在线
    }

    @Override
    public void onlineFail(String msg) {
    }




    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                try{
                    mCompanyBean = companyList.get(options1);
                    companyId = companyList.get(options1).getId();
                    acache.put("company_id",companyId);
                    DLog.e("AddTruckActivity", "公司companyId==" + companyId);
                    String tx = companyList.get(options1).getCompanyName();
                    et_company.setText(tx);

                }catch (Exception e){
                    e.printStackTrace();
                    DLog.e("AddTruckActivity", "公司列表："+e.getMessage());
                }
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);

                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                    }
                })
                .isDialog(true)
                .setLineSpacingMultiplier(1.8f)
                .setTextColorCenter(0xFF0E82EB)
                .build();

        pvCustomOptions.setPicker(companyList);//添加数据

    }


    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        mdialog = new CustomDialog(mContext, R.style.LoadDialog);
        mdialog.show();
        new Thread("cancle_progressDialog") {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                    // cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
                    // 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
                    if(mdialog !=null ){
                        mdialog.cancel();
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
        if (mdialog != null) {
            mdialog.dismiss();
            mdialog = null;
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
                if(action.equals(BleConstant.ACTION_READ_HARD_SOFT_SUSSCESS)){
                    String str=intent.getStringExtra("hard_ware");
                    tv_hard_soft.setText(str);
                    btn_2.performClick();
                }else if(action.equals(BleConstant.ACTION_READ_SOFT_SUSSCESS)){
                    String str1=intent.getStringExtra("soft_ware");
                    tv_gu_soft.setText(str1);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTION_READ_HARD_SOFT_SUSSCESS);
        filter.addAction(BleConstant.ACTION_READ_SOFT_SUSSCESS);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
//    //对话框
//    public void showCheckDialog(final Context context) {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//        /*dialog.setTitle("提示");
//        dialog.setMessage(message);*/
//        View view = LayoutInflater.from(context).inflate(R.layout.layout_check_is, null);
//        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
//        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
//        tv_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InformationBasicActivity.start(mContext);
//                if(tempDialog != null){
//                    tempDialog.dismiss();
//                }
//                finish();
//            }
//        });
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(tempDialog != null){
//                    tempDialog.dismiss();
//                }
//                finish();
//            }
//        });
//        dialog.setView(view);//给对话框添加一个EditText输入文本框
//        //下面是弹出键盘的关键处
//        tempDialog = dialog.create();
//        tempDialog.setView(view, 0, 0, 0, 0);
//        tempDialog.show();
//    }


    //对话框    //补录安装人员信息
    public void showNextDialog(final Context context,final String passCheckStr) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_next, null);
        TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        if("0".equals(passCheckStr)){
            tv_title.setText("基本信息已录入,未提交安装人员信息");
            tv_ok.setText("补录");
        }else{
            tv_title.setText("基本信息已录入,已提交安装人员信息");
            tv_ok.setText("确定");
        }
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mtempDialog != null){
                    mtempDialog.dismiss();
                }
                if("0".equals(passCheckStr)){
                    InformationBasicActivity.start(mContext);

                    finish();
                }else{
                    finish();
                }


            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mtempDialog != null){
                    mtempDialog.dismiss();
                }
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        mtempDialog = dialog.create();
        mtempDialog.setView(view, 0, 0, 0, 0);
        mtempDialog.show();
    }

    //对话框   继续录安装人员信息
    public void showNext(final Context context,final String passCheckStr) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_next, null);
        TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        if("0".equals(passCheckStr)){
            tv_title.setText("基本信息已录入,请录入安装人员信息");
            tv_ok.setText("录入");
        }else{
            tv_title.setText("基本信息已录入成功");
            tv_ok.setText("确定");
        }
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nexttempDialog != null){
                    nexttempDialog.dismiss();
                }
                if("0".equals(passCheckStr)){
                    InformationBasicActivity.start(mContext);

                    finish();
                }else{
                    finish();
                }


            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nexttempDialog != null){
                    nexttempDialog.dismiss();
                }
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        nexttempDialog = dialog.create();
        nexttempDialog.setView(view, 0, 0, 0, 0);
        nexttempDialog.show();
    }

    /**
     * 根据路径获取图片
     *
     * @param filePath  文件路径
     * @param maxWidth  图片最大宽度
     * @param maxHeight 图片最大高度
     * @return bitmap
     */
    private static Bitmap getBitmap(final String filePath, final int maxWidth, final int maxHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * Return the sample size.
     *
     * @param options   The options.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return the sample size
     */
    private static int calculateInSampleSize(final BitmapFactory.Options options,
                                             final int maxWidth,
                                             final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while ((width >>= 1) >= maxWidth && (height >>= 1) >= maxHeight) {
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }
}
