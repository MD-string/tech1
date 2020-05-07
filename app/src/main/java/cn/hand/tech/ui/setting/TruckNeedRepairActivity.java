package cn.hand.tech.ui.setting;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.setting.adapter.TruckNeedRepairAdapter;
import cn.hand.tech.ui.setting.bean.CarListModel;
import cn.hand.tech.ui.setting.bean.RepairListModel;
import cn.hand.tech.ui.setting.bean.RepairModel;
import cn.hand.tech.ui.setting.presenter.RepairByTruckPresenter;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.bean.TruckChildBean;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.DataUtil;
import cn.hand.tech.utils.LatLng;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 * describe:需要维修车辆
 */
public class TruckNeedRepairActivity extends Activity implements View.OnClickListener,INeedRepairView,TruckNeedRepairAdapter.onPhoneLisenter {


    private ExpandableListView list_1;
    private ACache acache;
    private TruckNeedRepairAdapter madapter;
    private TextView mTvBack;
    private TruckNeedRepairActivity mContext;
    private CompanyResultBean companyResult;
    private List<RepairListModel> mGroupList=new ArrayList<>();
    private ImageView tv_search;
    private TruckChildBean truckModel;
    private String companyId;
    private TextView tv_para_title;
    private RepairByTruckPresenter mPresenter;
    DecimalFormat df = new DecimalFormat("0.00");
    private String token;
    private String companyName;
    private String mId;
    private AlertDialog tempDialog;
    private boolean isCall;
    private long firstCallTime;
    int costTime;
    private MyPhoneListener myPhoneListener;
    TelephonyManager tm;

    public static void start(Context context){
        Intent i=new Intent(context, TruckNeedRepairActivity.class);
        context.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_truck_need_repair);
        acache= ACache.get(mContext, CommonUtils.TAG);
        token = acache.getAsString("login_token");
        mPresenter=new RepairByTruckPresenter(mContext,this);

        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        Bundle bundle =getIntent().getExtras();
        truckModel = (TruckChildBean) bundle.getSerializable("truckModel");
        if(truckModel !=null){
            companyName=truckModel.getName();
            companyId=truckModel.getChildId();
            if(!Tools.isEmpty(companyName)){
                tv_para_title.setText(companyName+"");
            }
        }
        findViews();
    }

    protected void findViews() {

        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setVisibility(View.VISIBLE);
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_search=(ImageView)findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchTruckForRepairActivity.start(mContext);
            }
        });

        list_1=(ExpandableListView)findViewById(R.id.list_1);
        madapter=new TruckNeedRepairAdapter(mContext,mGroupList);
        list_1.setGroupIndicator(null);//不使用系统提供的展开和收起的图标  左边有个下的图标
        list_1.setAdapter(madapter);
        int groupCount =mGroupList.size();

        for (int i=0; i<groupCount; i++) {

            list_1.expandGroup(i);

        }
        tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        myPhoneListener = new MyPhoneListener();
        tm.listen(myPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        //请求数据
        mPresenter.getRepairInformationByCompanyID(token,companyId);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.tv_back:
                finish();
                break;
        }
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }


    @Override
    public void doSuccess(List<RepairModel> bean) {
        if(bean !=null && bean.size() >0){
            List<RepairModel>  mlist=doHandlerAddress(bean);
            //合并相同车牌数据
            List<CarListModel> listbean=  dohandlerData(mlist);
            RepairListModel rbean=new RepairListModel();
            rbean.setCompanyId(companyId);
            rbean.setName(companyName);
            rbean.setChildren(listbean);
            mGroupList.add(rbean);
            madapter.updateListView(mGroupList);
            int groupCount =mGroupList.size();

            for (int i=0; i<groupCount; i++) {

                list_1.expandGroup(i);

            }
        }else{
            showTips("该公司无车辆需要维修");
        }
    }

    public List<RepairModel>   doHandlerAddress( List<RepairModel> list){
        //处理 部分厂区 距离   以及 其他 离我距离
        List<RepairModel> alllist= (List<RepairModel>)acache.getAsObject("all_repair_car"); //所有车辆
        HashMap hmap= DataUtil.getFatoryAddress();
        List<RepairModel> newlist=new ArrayList<>();
         List<RepairModel> istnewList=new ArrayList<>();
        boolean ishave=false;
        LatLng labean=new LatLng(); //厂区地址
        for(int i=0;i<hmap.size();i++){
            LatLng    lab1=(LatLng)hmap.get(companyName);
            if(lab1 !=null){  //表示存在  按照车辆距离 厂区
                ishave=true;
                labean.setLat(lab1.getLat());
                labean.setLon(lab1.getLon());
            } //表示不存在   按照 车辆 距离我 来计算

        }
        if(ishave){
            for(int p=0;p<alllist.size();p++){
                RepairModel rmodl=alllist.get(p);
                String lon=rmodl.getLon();
                String lat=rmodl.getLat();
                if(!Tools.isEmpty(lon) && !Tools.isEmpty(lat)){
                    LatLng mlat=new LatLng(Double.parseDouble(lon),Double.parseDouble(lat));
                    String ditance=DataUtil.dohandlerDistance(labean,mlat);
//                    if(Double.parseDouble(ditance) <=10 ){ //距厂区10公里内 显示
                        String str= df.format(Double.parseDouble(ditance));
                        rmodl.setDistance("离厂区"+str);
                        newlist.add(rmodl);
//                    }
                }
            }
            Collections.sort(newlist, new Comparator<RepairModel>(){

                /*
                 * int compare(Student o1, Student o2) 返回一个基本类型的整型，
                 * 返回负数表示：o1 小于o2，
                 * 返回0 表示：o1和o2相等，
                 * 返回正数表示：o1大于o2。
                 */
                public int compare(RepairModel o1, RepairModel o2) {
                    if(!Tools.isEmpty(o1.getDistance())  && !Tools.isEmpty(o2.getDistance()) ){
                        //按照学生的年龄进行升序排列
                        if(Double.parseDouble(o1.getDistance().replace("离厂区","")) >Double.parseDouble(o2.getDistance().replace("离厂区","")) ){
                            return 1;
                        }
                        if(Double.parseDouble(o1.getDistance().replace("离厂区","")) >Double.parseDouble(o2.getDistance().replace("离厂区",""))){
                            return 0;
                        }
                    }
                    return -1;
                }
            });


            if(newlist !=null && newlist.size() >=50){
                istnewList=newlist.subList(0,50);
            }

            return istnewList;
        }else{
            for(int g=0;g<list.size();g++){
                RepairModel pointList=list.get(g);
                String pCarNumber=pointList.getCarNumber();
                for(int k=0;k<alllist.size();k++){
                    RepairModel cml=alllist.get(k);
                    String carNu=cml.getCarNumber();
                    if(pCarNumber.equals(carNu)){
                        if(!Tools.isEmpty( cml.getLat()) && ! Tools.isEmpty(cml.getLon())){
                            pointList.setLon(cml.getLon());
                            pointList.setLat(cml.getLat());

                            LatLng car=new LatLng(Double.parseDouble(cml.getLon()),Double.parseDouble(cml.getLat()));
                            LatLng me =(LatLng)acache.getAsObject("current_address"); //当前位置
                            String dist= DataUtil.dohandlerDistance(car,me);
                            String str= df.format(Double.parseDouble(dist));
                            pointList.setDistance("离我"+str);
                        }
                    }
                }

            }
            Collections.sort(list, new Comparator<RepairModel>(){

                /*
                 * int compare(Student o1, Student o2) 返回一个基本类型的整型，
                 * 返回负数表示：o1 小于o2，
                 * 返回0 表示：o1和o2相等，
                 * 返回正数表示：o1大于o2。
                 */
                public int compare(RepairModel o1, RepairModel o2) {
                    if(!Tools.isEmpty(o1.getDistance())  && !Tools.isEmpty(o2.getDistance()) ){
                        //按照学生的年龄进行升序排列
                        if(Double.parseDouble(o1.getDistance().replace("离我","")) >Double.parseDouble(o2.getDistance().replace("离我","")) ){
                            return 1;
                        }
                        if(Double.parseDouble(o1.getDistance().replace("离我","")) >Double.parseDouble(o2.getDistance().replace("离我",""))){
                            return 0;
                        }
                    }
                    return -1;
                }
            });
            return  list;
        }
    }

    private List<CarListModel> dohandlerData(List<RepairModel> bean) {
        List<CarListModel> clist=new ArrayList<>();
        for(int i=0;i<bean.size();i++){
            RepairModel model=bean.get(i);
            String carNumber=model.getCarNumber();

            CarListModel cmodel=new CarListModel();
            cmodel.setCarNumber(carNumber);
            List<RepairModel> newList=new ArrayList<>();
            newList.add(model);
            for(int j=bean.size()-1;j > i;j--){
                RepairModel modelj=bean.get(j);
                String carNumberj=modelj.getCarNumber();
                if(carNumber.equals(carNumberj)){
                    newList.add(modelj);
                    bean.remove(j);
                }

            }
            cmodel.setChildren(newList);
            clist.add(cmodel);
        }
        return clist;
    }

    @Override
    public void doError(String str) {
        showTips(str);
        //            finish();
    }

    @Override
    public void inputSuccess(String str) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消电话监听
        if (tm != null&&myPhoneListener!=null) {
            tm.listen(myPhoneListener, PhoneStateListener.LISTEN_NONE);
        }
    }
    @Override
    public void telPhone(RepairModel num) {
        String phone=num.getDriverPhone();
        mId=num.getId();
        showDialog(mContext,phone);
    }

    class MyPhoneListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲

                    if (isCall) {
                       long callTime = System.currentTimeMillis() - firstCallTime;
                        isCall = false;
                        costTime = (int) (callTime / 1000);
                        if (tm != null&&myPhoneListener!=null) {
                            tm.listen(myPhoneListener, PhoneStateListener.LISTEN_NONE);
                        }
                        String date=costTime+""; //秒
                        mPresenter.inputPhoneDate(token,date,mId);
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
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            showTips("请打开拨打电话权限");
            return;
        }
        mContext.startActivity(intent1);
    }
}
