package cn.hand.tech.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.BaseActivity;
import cn.hand.tech.ui.setting.bean.AutoCheckFrgBean;
import cn.hand.tech.ui.setting.bean.CarNumberInfo;
import cn.hand.tech.ui.setting.presenter.EnterPlantCheckPresenter;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.Aes;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.NavigationConfig;

/*
 *进厂检测
 */
@NavigationConfig(isShow = false, titleId = R.string.main_title, showLeft = false)
public class EnterPlantCheckAct extends BaseActivity implements  IEnterPlantCheckView {
    private Context context;
    private EnterPlantCheckPresenter mpresenter;
    private EditText et_dev_id,et_car_number;
    private Button bt_enter_plant_check;
    private TextView tv_result;
    private static final String PASSWORD_STRING = "6a9fd0d1a950420b812f27e970afcd8f"; // static final 静态常量
    private ACache acache;
    private String token;
    private TextView tv_plant_address,tv_dev_status;
    private AlertDialog adddialog;
    private String psw;

    public static void start(Context context) {
        Intent intent = new Intent(context, EnterPlantCheckAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_enterplant_check);
        acache = ACache.get(context,  CommonUtils.TAG);
        token = acache.getAsString("login_token");
        mpresenter = new EnterPlantCheckPresenter(context, this);
        initView();
        init();
    }
    private void initView() {
        LinearLayout   ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_dev_id=(EditText)findViewById(R.id.et_dev_id);
        et_car_number=(EditText)findViewById(R.id.et_car_number);
        bt_enter_plant_check=(Button)findViewById(R.id.bt_enter_plant_check);

        tv_plant_address=(TextView)findViewById(R.id.tv_plant_address);
        tv_dev_status=(TextView)findViewById(R.id.tv_dev_status);
        tv_result=(TextView)findViewById(R.id.tv_result);

        tv_dev_status.setText("");
        tv_plant_address.setText("");
        tv_result.setText("");
        bt_enter_plant_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String devId=et_dev_id.getText().toString();
                String carNm=et_car_number.getText().toString();
                if(Tools.isEmpty(devId) && Tools.isEmpty(carNm)){
                    showTips("请输入设备ID或车牌号码");
                    return;
                }
                if(!Tools.isEmpty(devId)){
                    HashMap<String,String>hmap=new HashMap<>(); //获取车辆信息
                    hmap.put("deviceId",devId);
                    mpresenter.checkCarNumberExisit(hmap);

                }else {
                    HashMap<String,String>hmap=new HashMap<>(); //获取车辆信息
                    hmap.put("carNumber",carNm);
                    mpresenter.checkCarNumberExisit(hmap);
                }

            }
        });
    }


    //获取允许进厂数据
    private void doGetDataFragory(String mtoken,String mdevId){
        HashMap<String,String> hmap=new HashMap<>(); //
        hmap.put("deviceId",mdevId);
        hmap.put("token",mtoken);
        hmap.put("lastData","1");
        hmap.put("version","2");
        mpresenter.getOrNotInputFragory(hmap);
    }

    private void init() {

    }


    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }

    @Override
    public void doLogin(UserResultBean bean) {

        closedialog();
        showTips("登录成功,请重新检测");
        UserResultBean userBean=bean;
        token=bean.getToken();
        String uname=bean.getResult().getUserName();

        acache.put("login_name",uname);
        acache.put("login_token",token);
        acache.put("log_psw",psw);
    }

    @Override
    public void doLoginFail(String bean) {
        showTips(bean);
    }

    @Override
    public void doInfo(CarNumberInfo companyResult) {
        if(companyResult !=null){
            String devid=companyResult.getDeviceId();
            doGetDataFragory(token,devid);


            String companyNmae=companyResult.getCompanyName();
            if(!Tools.isEmpty(companyNmae)){
                tv_plant_address.setText(companyNmae);
            }else{
                tv_plant_address.setText("");
            }
            String onLineOr=companyResult.getRunStatus();
            if("1".equals(onLineOr)){
                tv_dev_status.setText("在线");
            }else{
                tv_dev_status.setText("离线");
            }

        }

    }

    @Override
    public void doError(String msg) {
        showTips(msg);
    }

    @Override
    public void doFragoryFinish(AutoCheckFrgBean companyResult) {
        if(companyResult !=null){
            doshow(companyResult);
            showTips("进厂检测完成");
        }else{
            showTips("数据为空");
        }
    }

    public void doshow(AutoCheckFrgBean companyResult){
        String type=companyResult.getAlarmType();
        if(Tools.isEmpty(type)){
            DLog.e("EnterPlantCheckAct","mAutoCheckFrgBean.AlarmType"+"为空");
            return;
        }
        DLog.e("EnterPlantCheckAct","mAutoCheckFrgBean.AlarmType"+type);

        tv_result.setTextColor(getResources().getColor(R.color.red));
        if("0".equals(type)){
            tv_result.setText("检测正常");
            tv_result.setTextColor(getResources().getColor(R.color.rep_green));

        }else if("1".equals(type)){

            tv_result.setText("司机断电【检查主机是否上线】");

        }else if("2".equals(type)){
            tv_result.setText("设备离线【检查主机是否上线】");

        }else if("3".equals(type)){
            tv_result.setText("重量异常【检查传感器】");

        }else if("4".equals(type)){
            tv_result.setText("主机异常【维修后请提交维修记录】");

        }else if("5".equals(type)){
            tv_result.setText("GPS异常【维修后请提交维修记录】");

        }else if("6".equals(type)){
            tv_result.setText("传感器异常【维修后请提交维修记录】");
        }else{
            DLog.e("AutoCheck","mAutoCheckFrgBean.AlarmType"+"不在范围内");
        }
    }

    @Override
    public void doFragoryError(String msg) {
        showTips(msg);
        if(msg.contains("重新登录")){ //
            doAddTruck();
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
    /**
     * 关闭进度对话框
     */
    private void closedialog() {
        if (adddialog != null) {
            adddialog.dismiss();
            adddialog = null;
        }
    }

}
