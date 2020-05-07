package cn.hand.tech.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.weight.IBasicView;
import cn.hand.tech.ui.weight.InformationBasicActivity;
import cn.hand.tech.ui.weight.bean.AddTruckInfo;
import cn.hand.tech.ui.weight.bean.CompanyBean;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.presenter.BasicPresenter;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.CustomDialog;

/*
 *车辆录入
 */
public class CarInputAct extends AppCompatActivity implements View.OnClickListener,IBasicView {

    private String companyId = "";
    private String token;
    private List<CompanyBean> companyList = new ArrayList<>();
    private Context mContext;
    private BasicPresenter mpresenter;
    private OptionsPickerView pvCustomOptions;
    private boolean isConnect;
    private String  deviceId;
    private CompanyBean mCompanyBean;//当前选择的公司
    private EditText et_count, et_phone;
    private TextView et_company,et_stuck_number;
    private TextView et_id;
    private Button btn_next;

    private ACache acache;
    private LinearLayout ll_back;
    private EditText et_thruck_type;
    private Spinner spinner_zhou, spinner_moto_type;
    private String zhou, moNum, mType;
    private String truckNum;
    private boolean isEnter;
    private CustomDialog mdialog;
    private AlertDialog adddialog;
    private String unitStr;

    private String moNum_position,zhou_position,mType_position;
    private AlertDialog tempDialog,mtempDialog;


    public static void start(Context context) {
        Intent intent = new Intent(context, CarInputAct.class);
        context.startActivity(intent);

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initCompanyList();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.fragment_truck_add_basic);
        acache = ACache.get(mContext,  CommonUtils.TAG);
        token = acache.getAsString("login_token");
        truckNum= acache.getAsString("car_num");
        deviceId = getIntent().getStringExtra("device_id");
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
    }

    protected void findViews() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        et_id = (TextView) findViewById(R.id.et_id);
        et_stuck_number = (TextView) findViewById(R.id.et_stuck_number);
        et_stuck_number.setText(truckNum);
        et_company = (TextView) findViewById(R.id.et_company);
        et_count = (EditText) findViewById(R.id.et_count);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_thruck_type = (EditText) findViewById(R.id.et_thruck_type);
        spinner_zhou = (Spinner) findViewById(R.id.spinner_zhou);
//        spinner_moto_no = (Spinner) findViewById(R.id.spinner_moto_no);
        spinner_moto_type = (Spinner) findViewById(R.id.spinner_moto_type);
        btn_next = (Button) findViewById(R.id.btn_next);

        et_id.setText(deviceId);
        mpresenter.checkID(deviceId,token);
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


        //        et_id.addTextChangedListener(new TextWatcherImpl(){  //方便测试
        //            @Override
        //            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        //                btn_next.setEnabled(true);
        //                btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_round_corner));
        //                initCompanyList();
        //            }
        //        });
    }



    /*获取公司列表信息*/
    private void initCompanyList() {
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mpresenter.getCompanyList(mapParams);

    }
    protected void setListeners() {
        ll_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        et_company.setOnClickListener(this);

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

//        spinner_moto_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String[] motonumber = getResources().getStringArray(R.array.motonumber);
//                moNum = motonumber[position];
//                moNum_position=position+"";
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back://返回
                finish();
                break;
            case R.id.et_company://公司
                hideKeyBorad();
                if(companyList !=null && companyList.size() > 0){
                    pvCustomOptions.show(); //弹出自定义条件选择器
                }else{
                    if(isEnter){
                        showTips("该设备已录入");
                    }else{
                        showTips("公司列表下载失败");
                    }
                }
                break;
            case R.id.btn_next://下一步
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

                acache.put("car_number",stuckNumber);

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
                    bean.setPhone(stuckPhone);
                }

                String stuckType = et_thruck_type.getText().toString().trim();
                if (!Tools.isEmpty(stuckType)) {
                    bean.setTruckType(stuckType);
                }
                if (!Tools.isEmpty(zhou)) {
                    bean.setAlxesNumber(zhou);
                }
                if (!Tools.isEmpty(moNum)) {
                    bean.setSensorNumber(moNum);
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
//                mpresenter.sendBasicData(bean, token,companyId,unitStr);

                acache.put("add_truck",(Serializable)bean);
                acache.put("mn_positon",moNum_position);
                acache.put("zh_positon",zhou_position);
                acache.put("mt_positon",mType_position);
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


    public void showTips(String tip) {
        ToastUtil.getInstance().showCenterMessage(mContext, tip);
    }

    @Override
    public void doSuccess(String msg) {
        mHandler.sendEmptyMessage(1);
        if ("0".equals(msg)) {
            showTips("该设备已被录入");
            btn_next.setEnabled(false);
            isEnter=true;
            btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_round_corner_hui));

            //弹出对话框
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showCheckDialog(mContext);
                }
            },100);
        } else {
            showTips("该设备未被录入");
            isEnter=false;
            btn_next.setEnabled(true);
            btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_round_corner));
        }

    }

    @Override
    public void doCheckAPISuccess(String msg, String msg1) {

    }

    @Override
    public void doError(String msg) {
        showTips(msg);
    }

    @Override
    public void doCompanySuccess(CompanyResultBean bean) {
        CompanyResultBean companyResult = bean;
        companyList = companyResult.getResult();
        pvCustomOptions.setPicker(companyList);//添加数据
        DLog.d("AddTruckActivity", "下载公司列表成功");
    }

    @Override
    public void doEnterCar(String msg) {
        showTips("基本信息添加成功");
        //        InformationBasicActivity.start(mContext);
        //        acache.put("",et_id.getText().toString().trim());

        //        AddTruckInfo bean=(AddTruckInfo)acache.getAsObject("add_truck");//????/
        //        if(bean !=null){
        //
        //        }
        //        finish();
        showNextDialog(mContext);
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
                mCompanyBean = companyList.get(options1);
                companyId = companyList.get(options1).getId();
                acache.put("company_id",companyId);
                DLog.e("AddTruckActivity", "公司companyId==" + companyId);
                String tx = companyList.get(options1).getCompanyName();
                et_company.setText(tx);
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

    //对话框
    public void showCheckDialog(final Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        /*dialog.setTitle("提示");
        dialog.setMessage(message);*/
        View view = LayoutInflater.from(context).inflate(R.layout.layout_check_is, null);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        ImageView iv_clear = (ImageView) view.findViewById(R.id.iv_clear);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationBasicActivity.start(mContext);
                if(tempDialog != null){
                    tempDialog.dismiss();
                }
                finish();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempDialog != null){
                    tempDialog.dismiss();
                }
            }
        });
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        tempDialog = dialog.create();
        tempDialog.setView(view, 0, 0, 0, 0);
        tempDialog.show();
    }


    //对话框
    public void showNextDialog(final Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        /*dialog.setTitle("提示");
        dialog.setMessage(message);*/
        View view = LayoutInflater.from(context).inflate(R.layout.layout_next, null);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        ImageView iv_clear = (ImageView) view.findViewById(R.id.iv_clear);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationBasicActivity.start(mContext);
                if(mtempDialog != null){
                    mtempDialog.dismiss();
                }
                finish();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mtempDialog != null){
                    mtempDialog.dismiss();
                }
            }
        });
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        mtempDialog = dialog.create();
        mtempDialog.setView(view, 0, 0, 0, 0);
        mtempDialog.show();
    }
}
