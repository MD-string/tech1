package cn.hand.tech.ui.weight;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.weight.bean.CompanyBean;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.presenter.CompanyListPresenter;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.ToastUtil;


/**
 * describe:添加车辆页
 */
public class AddTruckActivity extends AppCompatActivity implements View.OnClickListener,ICompyanView {
    private LinearLayout mTvBack;
    private LinearLayout ll_select_company;
    private TextView mTvSelectCompany;
    private EditText mEtDeviceNumber;

    private TextView mEtTruckNumber;
    private Button mBtnAddTruck;
    private String companyId="";
    private String token;
    private List<CompanyBean> companyList=new ArrayList<>();
    private InputView mInputView;
    private PopupKeyboard mPopupKeyboard;
    private LinearLayout dialog_truckN_input;
    private RelativeLayout ll_all;
    private Context mContext;
    private CompanyListPresenter mpresenter;
    private OptionsPickerView pvCustomOptions;
    private boolean isConnect;
    private String carNumber,deviceId;
    private CompanyBean mCompanyBean;//当前选择的公司

    private ACache acache;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.fragment_truck_add);
        acache= ACache.get(mContext, CommonUtils.TAG);
        token=acache.getAsString("login_token");
        carNumber=getIntent().getStringExtra("car_number");
        deviceId=getIntent().getStringExtra("device_id");
        isConnect=getIntent().getBooleanExtra("is_connected",false);

        mpresenter=new CompanyListPresenter(mContext,this);
        findViews();
        initCompanyList();
        setListeners();
        initCustomOptionPicker();
    }

    protected void findViews() {
        ll_all=(RelativeLayout)findViewById(R.id.ll_all);
        mTvBack = (LinearLayout) findViewById(R.id.ll_back);
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_select_company=(LinearLayout) findViewById(R.id.ll_select_company);
        mTvSelectCompany = (TextView) findViewById(R.id.et_select_company);
        mEtDeviceNumber = (EditText)findViewById(R.id.et_device_number);
        mEtTruckNumber = (TextView) findViewById(R.id.tv_truck_number);
        mBtnAddTruck = (Button) findViewById(R.id.btn_add_truck);

        if(isConnect){
            mEtDeviceNumber.setText(deviceId);
            mEtDeviceNumber.setEnabled(false);
            mEtTruckNumber.setText(carNumber);
            mEtTruckNumber.setEnabled(false);
        }else{
            mEtDeviceNumber.setText("");
            mEtDeviceNumber.setEnabled(true);
            mEtTruckNumber.setText("");
            mEtTruckNumber.setEnabled(true);
        }

        dialog_truckN_input = (LinearLayout)findViewById(R.id.dialog_truckN_input);//车辆信息键盘
        dialog_truckN_input.setVisibility(View.GONE);
        mInputView = (InputView)findViewById(R.id.input_view);
        EditText  et_input=(EditText)findViewById(R.id.et_input);
        mInputView.setVisibility(View.VISIBLE);
        et_input.setVisibility(View.GONE);
        TextView  tv_other=(TextView)findViewById(R.id.tv_other);
        tv_other.setText("其他");
        tv_other.setVisibility(View.GONE);
        TextView  tv_okTruckN = (TextView)findViewById(R.id.tv_okTruckN);
        tv_okTruckN.setOnClickListener(this);
        TextView   tv_cancelTruckN=(TextView)findViewById(R.id.tv_cancelTruckN);
        tv_cancelTruckN.setVisibility(View.GONE);
        mPopupKeyboard = new PopupKeyboard(mContext);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(mInputView, this);
    }


    protected void setListeners() {
        ll_select_company.setOnClickListener(this);//选择公司*/
        mBtnAddTruck.setOnClickListener(this);
        mEtTruckNumber.setOnClickListener(this);
    }

    /*获取公司列表信息*/
    private void initCompanyList() {
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mpresenter.getCompanyList(mapParams);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*选择公司*/
            case R.id.ll_select_company:
                hideKeyBorad();
                if(companyList !=null && companyList.size() > 0){
                    pvCustomOptions.show(); //弹出自定义条件选择器
                }else{
                    showTips("无公司数据");
                }
                break;
              /*添加车辆*/
            case R.id.btn_add_truck:
                hideKeyBorad();
                submit();
                break;
            /*输入车牌号*/
            case R.id.tv_truck_number:
                hideKeyBorad();
                dialog_truckN_input.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 加上这句才会弹出键盘
                        mInputView.performFirstFieldView();
                        //必须延时显示，否则车牌键盘被顶上去，即使系统键盘已经被隐藏，放到popWindow，dialog都无用
                        mPopupKeyboard.show((Activity) mContext);
                    }
                }, 100);

                break;
            /*确定车牌号*/
            case R.id.tv_okTruckN:
                if (TextUtils.isEmpty(mInputView.getNumber().toString())) {
                    Toast.makeText(mContext, "车牌号码不能为空", Toast.LENGTH_SHORT).show();

                    return;
                } else if (mInputView.getNumber().length() < 7) {
                    Toast.makeText(mContext, "请输入正确的车牌号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                // mCarNumber = mInputView.getNumber();
                mEtTruckNumber.setText(mInputView.getNumber() + "");
                dialog_truckN_input.setVisibility(View.GONE);
                mInputView.updateNumber("");
                mInputView.performFirstFieldView();
                if (mPopupKeyboard.isShown()) {
                    mPopupKeyboard.dismiss((AddTruckActivity)mContext);
                }
                break;
//            case R.id.tv_cancelTruckN:
//                dialog_truckN_input.setVisibility(View.GONE);
//                mInputView.performFirstFieldView();
//                mInputView.updateNumber("");
//                if (mPopupKeyboard.isShown()) {
//                    mPopupKeyboard.dismiss(mContext);
//                }
//
//                break;
        }
    }
    private void hideKeyBorad() {
        InputMethodManager imm1 = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm1 != null) {
            imm1.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0);
        }
    }



    private void submit() {

        String company = mTvSelectCompany.getText().toString().trim();
        if (TextUtils.isEmpty(company)) {
            Toast.makeText(mContext, "公司名称不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.focusView(mTvSelectCompany);
            return;
        }

        String deviceNumber = mEtDeviceNumber.getText().toString().trim();
        if (TextUtils.isEmpty(deviceNumber)) {
            Toast.makeText(mContext, "设备编号不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.showOrHideKeyBoard(mContext,true, mEtDeviceNumber);
            return;
        }

        String truckNumber = mEtTruckNumber.getText().toString().trim();
        if (TextUtils.isEmpty(truckNumber)) {
            Toast.makeText(mContext, "车牌号码不能为空", Toast.LENGTH_SHORT).show();
            CommonKitUtil.showOrHideKeyBoard(mContext,true,mEtTruckNumber);
            return;
        }

        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("type", "1");
        mapParams.put("deviceId", mEtDeviceNumber.getText().toString());
        mapParams.put("gpsId", mCompanyBean.getId());
        mapParams.put("companyId", companyId);
        mapParams.put("carNumber", mEtTruckNumber.getText().toString());
        mapParams.put("phone",mCompanyBean.getTel());
        mapParams.put("loadCapacity", "30");//车辆载重量
        mpresenter.addTruck(mapParams);
    }


    @Override
    public void doSuccess(CompanyResultBean bean) {
        CompanyResultBean companyResult = bean;
        companyList = companyResult.getResult();
        pvCustomOptions.setPicker(companyList);//添加数据
        DLog.d("AddTruckActivity","下载公司列表成功");
    }

    @Override
    public void doError(String str) {
        DLog.d("AddTruckActivity",str);
    }

    @Override
    public void inputSuccess(String str) {
        showTips(str);

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
                mCompanyBean=companyList.get(options1);
                companyId= companyList.get(options1).getId();
                DLog.e("AddTruckActivity","公司companyId=="+companyId);
                String tx = companyList.get(options1).getCompanyName();
                mTvSelectCompany.setText(tx);
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

    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }
}
