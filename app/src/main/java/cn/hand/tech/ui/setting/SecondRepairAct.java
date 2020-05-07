package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.bean.PicBean;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.TextWatcherImpl;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.setting.adapter.GuideAdapter;
import cn.hand.tech.ui.setting.adapter.RepairAdapter;
import cn.hand.tech.ui.setting.adapter.RepairNormalAdapter;
import cn.hand.tech.ui.setting.bean.A1;
import cn.hand.tech.ui.setting.bean.OnLineTruckBean;
import cn.hand.tech.ui.setting.bean.RepairModel;
import cn.hand.tech.ui.setting.presenter.RepairPresenter;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.bean.InstallerInfo;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.MyListView;

/*
 *维修记录
 */
public class SecondRepairAct extends Activity implements IRepairView, RepairAdapter.onNextListener, GuideAdapter.onGuideListener,RepairNormalAdapter.onNormalListener{
    private Context context;
    private ACache acache;
    private TextView tv_car_number;
    private Button bt_search;
    private MyListView list_1,list_2,list_3;
    private List<String> vlist=new ArrayList<>();

    private AlertDialog adddialog;
    private String psw;
    private static final String PASSWORD_STRING = "6a9fd0d1a950420b812f27e970afcd8f"; // static final 静态常量
    private RepairPresenter mpresenter;
    private String token;
    private Button bt_save_re;
    private RepairAdapter madapter;
    private RepairAdapter adapter3;
    private EditText et_remark,et_zong;
    private LinearLayout ll_search;
    private TextView tv_line1;

    private List<A1> list1=new ArrayList<>();
    private List<A1> list2_1=new ArrayList<>();

    private List<A1> list2_2=new ArrayList<>();

    private List<A1> list2_3=new ArrayList<>();
    private List<A1> list2_4=new ArrayList<>();
    private List<A1> list3_1=new ArrayList<>();
    private List<A1> list3_3=new ArrayList<>();
    private List<A1> list2_5=new ArrayList<>();
    private int REQUEST_WRITE_EXTERNAL_STORAGE=22;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_EDIT =15;
    private String  from_Flag="SecondRepairAct";
    public static final int MAX_SHEET_COUNT = 9;// 详情最多张s
    private static final String TAG = "SecondRepairAct";
    private LinearLayout ll_change;
    private TextView tv_choosed;
    private String strC="";
    private LinearLayout ll_content;
    private Spinner repair_person;
    private List<InstallerInfo> inlist;
    private ArrayList<String> list;
    private int mposition;
    private String xp1;
    private LinearLayout ll_repair_info,ll_repair_more;
    private int gudNnumber; //选择的是哪一个维修指导
    private int repNo ;
    private String carNumber;
    private String faultStr,guideStr,repairId;
    private TextView ll_choose_normal;
    private   List<A1> alist=new ArrayList<>();
    private LinearLayout ll_change_1;
    private TextView tv_choosed_nomarl;
    private String strn;
    private EditText et_dirver_phone,et_dirver_name;
    private String driverName,driverPhone;
    private List<PicBean> mList=new ArrayList<>();
    private BroadcastReceiver receiver;
    private AlertDialog mtempDialog;
    private Spinner addRepair_gps_spinner,addRepair_gsm_spinner;
    private String gpsPo,gsmName;

    public static void start(Context context,int number,String fault,String guide,String id,String truck) {
        Intent intent = new Intent(context, SecondRepairAct.class);
        intent.putExtra("list_num",number);
        intent.putExtra("repari_fault",fault);
        intent.putExtra("repari_guide",guide);
        intent.putExtra("repari_id",id);
        intent.putExtra("repari_truck",truck);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_repair_second);
        repNo=getIntent().getIntExtra("list_num",0);
        faultStr=getIntent().getStringExtra("repari_fault");
        guideStr=getIntent().getStringExtra("repari_guide");
        repairId=getIntent().getStringExtra("repari_id");
        carNumber=getIntent().getStringExtra("repari_truck");
        driverName= getIntent().getStringExtra("dirver_name");
        driverPhone=getIntent().getStringExtra("dirver_phone");

        acache= ACache.get(context,"WeightFragment");
        token=acache.getAsString("login_token");
        mpresenter = new RepairPresenter(context, this);
        initData();

        initView();
        mpresenter.getRepairPersoninfo(token);
        acache.put("truck_number","");
    }

    private void initData() {
        list1=new ArrayList<>();
        A1 a1=new A1();
        a1.setStr("主机终端");
        list1.add(a1);

        A1 a2=new A1();
        a2.setStr("采  集  器");
        list1.add(a2);

        A1 a3=new A1();
        a3.setStr("传  感  器");
        list1.add(a3);

        A1 a4=new A1();
        a4.setStr("线        材");
        list1.add(a4);

        A1 a5=new A1();
        a5.setStr("G    P   S");
        list1.add(a5);
        //        private String[] arr1 = new String[]{"主机终端","采  集  器","传  感  器","线        材"};
        //        private List<String> list1= Arrays.asList(arr1);

        list2_1=new ArrayList<>();
        A1 b1=new A1();
        b1.setStr("主机终端");
        list2_1.add(b1);

        A1 b2=new A1();
        b2.setStr("SIM卡");
        list2_1.add(b2);
        A1 b3=new A1();
        b3.setStr("保险");
        list2_1.add(b3);

        //        private String[] arr2_1 = new String[]{"主机终端"};
        //        private List<String> list2_1= Arrays.asList(arr2_1);


        list2_2=new ArrayList<>();
        A1 c1=new A1();
        c1.setStr("4通道采集器");
        list2_2.add(c1);

        A1 c2=new A1();
        c2.setStr("6通道采集器");
        list2_2.add(c2);

        A1 c3=new A1();
        c3.setStr("一拖三Can总线");
        list2_2.add(c3);

        //        private String[] arr2_2 = new String[]{"4通道采集器","6通道采集器","一拖三Can总线"};
        //        private List<String> list2_2= Arrays.asList(arr2_2);

        list2_3=new ArrayList<>();
        A1 d1=new A1();
        d1.setStr("应   变   计");
        list2_3.add(d1);

        A1 d2=new A1();
        d2.setStr("17     -     4");
        list2_3.add(d2);

        //        private String[] arr2_3 = new String[]{"应变计","17  -  4"};
        //        private List<String> list2_3= Arrays.asList(arr2_3);
        list2_4=new ArrayList<>();
        A1 z0=new A1();
        z0.setStr("组合式天线");
        list2_4.add(z0);

        A1 z1=new A1();
        z1.setStr("20PIN天线");
        list2_4.add(z1);

        A1 z2=new A1();
        z2.setStr("M12-M16公母头线(9米)");
        list2_4.add(z2);

        A1 z3=new A1();
        z3.setStr("M12公母头线(8米)");
        list2_4.add(z3);

        A1 z4=new A1();
        z4.setStr("M12公母头线(6米)");
        list2_4.add(z4);

        A1 z5=new A1();
        z5.setStr("M12公母头线(4米)");
        list2_4.add(z5);

        A1 z6=new A1();
        z6.setStr("M12公母头线(3米)");
        list2_4.add(z6);

        A1 z7=new A1();
        z7.setStr("M12公母头线(2米)");
        list2_4.add(z7);

        A1 z8=new A1();
        z8.setStr("主机连接线(M12-4+4PIN)");
        list2_4.add(z8);

        A1 z9=new A1();
        z9.setStr("485接口连接线");
        list2_4.add(z9);


        list2_5=new ArrayList<>();
        A1 gps0=new A1();
        gps0.setStr("GPS天线");
        list2_5.add(gps0);

        A1 gps1=new A1();
        gps1.setStr("将天线转移至车顶");
        list2_5.add(gps1);

        //
        //        private String[] arr2_4 = new String[]{"组合式天线","20PIN天线","M12-M16公母头线(9米)","M12公母头线(8米)","M12公母头线(6米)","M12公母头线(4米)","M12公母头线(3米)","M12公母头线(2米)","主机连接线(M12-4+4PIN)"};
        //        private List<String> list2_4= Arrays.asList(arr2_4);
        list3_1=new ArrayList<>();
        A1 g1=new A1();
        g1.setStr("1");
        list3_1.add(g1);

        A1 g2=new A1();
        g2.setStr("2");
        list3_1.add(g2);

        A1 g3=new A1();
        g3.setStr("3");
        list3_1.add(g3);

        A1 g4=new A1();
        g4.setStr("4");
        list3_1.add(g4);

        A1 g5=new A1();
        g5.setStr("5");
        list3_1.add(g5);

        A1 g6=new A1();
        g6.setStr("6");
        list3_1.add(g6);

        A1 g7=new A1();
        g7.setStr("7");
        list3_1.add(g7);

        A1 g8=new A1();
        g8.setStr("8");
        list3_1.add(g8);

        A1 g9=new A1();
        g9.setStr("9");
        list3_1.add(g9);

        A1 g10=new A1();
        g10.setStr("10");
        list3_1.add(g10);

        A1 g11=new A1();
        g11.setStr("11");
        list3_1.add(g11);

        A1 g12=new A1();
        g12.setStr("12");
        list3_1.add(g12);

        A1 g13=new A1();
        g13.setStr("13");
        list3_1.add(g13);

        A1 g14=new A1();
        g14.setStr("14");
        list3_1.add(g14);

        A1 g15=new A1();
        g15.setStr("15");
        list3_1.add(g15);

        A1 g16=new A1();
        g16.setStr("16");
        list3_1.add(g16);


        //        private String[] arr3_1 = new String[]{"17-4"};
        //        private List<String> list3_1= Arrays.asList(arr3_1);
        list3_3=new ArrayList<>();
        A1 m1=new A1();
        m1.setStr("1");
        list3_3.add(m1);

        A1 m2=new A1();
        m2.setStr("2");
        list3_3.add(m2);

        A1 m3=new A1();
        m3.setStr("3");
        list3_3.add(m3);

        A1 m4=new A1();
        m4.setStr("4");
        list3_3.add(m4);

        A1 m5=new A1();
        m5.setStr("5");
        list3_3.add(m5);

        A1 m6=new A1();
        m6.setStr("6");
        list3_3.add(m6);

        A1 m7=new A1();
        m7.setStr("7");
        list3_3.add(m7);

        A1 m8=new A1();
        m8.setStr("8");
        list3_3.add(m8);

        A1 m9=new A1();
        m9.setStr("9");
        list3_3.add(m9);

        A1 m10=new A1();
        m10.setStr("10");
        list3_3.add(m10);

        A1 m11=new A1();
        m11.setStr("11");
        list3_3.add(m11);

        A1 m12=new A1();
        m12.setStr("12");
        list3_3.add(m12);

        A1 m13=new A1();
        m13.setStr("13");
        list3_3.add(m13);

        A1 m14=new A1();
        m14.setStr("14");
        list3_3.add(m14);

        A1 m15=new A1();
        m15.setStr("15");
        list3_3.add(m15);

        A1 m16=new A1();
        m16.setStr("16");
        list3_3.add(m16);

    }

    private void initView() {
        LinearLayout   ll_back=(LinearLayout)findViewById(R.id.ll_back);

        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_content=(LinearLayout)findViewById(R.id.ll_content);


        tv_car_number=(TextView)findViewById(R.id.tv_car_number);
        tv_car_number.setText(carNumber);

        TextView  tv_xian=(TextView)findViewById(R.id.tv_xian);
        tv_xian.setText(faultStr);

        TextView  tv_gudier=(TextView)findViewById(R.id.tv_gudier);
        tv_gudier.setText(guideStr);

        tv_line1=(TextView)findViewById(R.id.tv_line1);
        ll_search=(LinearLayout)findViewById(R.id.ll_search);

        et_dirver_name=(EditText)findViewById(R.id.et_dirver_name);
        if(!Tools.isEmpty(driverName) && !driverName.contains("null")){
            et_dirver_name.setText(driverName);
        }
        et_dirver_phone=(EditText)findViewById(R.id.et_dirver_phone);
        if(!Tools.isEmpty(driverPhone) && !driverPhone.contains("null")){
            et_dirver_phone.setText(driverPhone);
        }


        madapter = new RepairAdapter(context, list1,0,"0");

        list_1=(MyListView)findViewById(R.id.list_1);
        list_1.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                list_1.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        list_2=(MyListView)findViewById(R.id.list_2);
        list_2.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                list_2.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        list_3=(MyListView)findViewById(R.id.list_3);
        list_3.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                list_3.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        list_1.setAdapter(madapter);

        ll_change=(LinearLayout)findViewById(R.id.ll_change);
        ll_change.setVisibility(View.GONE);
        tv_choosed=(TextView)findViewById(R.id.tv_choosed);

        ll_change_1=(LinearLayout)findViewById(R.id.ll_change_1);
        ll_change_1.setVisibility(View.GONE);
        tv_choosed_nomarl=(TextView)findViewById(R.id.tv_choosed_nomarl);

        repair_person=(Spinner)findViewById(R.id.repair_person);

        repair_person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(inlist !=null && inlist.size() > 0 && position >0){
                    mposition=position-1;
                    xp1=inlist.get(position-1).getId();
                }else{
                    mposition=-1;
                    xp1="";
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

        ll_repair_info=(LinearLayout)findViewById(R.id.ll_repair_info);
        ll_repair_more=(LinearLayout)findViewById(R.id.ll_repair_more);
        et_remark=(EditText)findViewById(R.id.et_remark);
        et_remark.addTextChangedListener(new TextWatcherImpl(){
            @Override
            public void afterTextChanged(Editable arg0) {
                String str=arg0.toString();
            }
        });

        ll_choose_normal=(TextView)findViewById(R.id.ll_choose_normal); //常见
        ll_choose_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Tools.isEmpty(strC)) {
                    showTips("维修方式不能为空");
                    return;
                }

                List<String> mlist=handlerData(strC);
                showNormalDialog(mlist);
            }
        });
        et_zong=(EditText)findViewById(R.id.et_zong);
        et_zong.addTextChangedListener(new TextWatcherImpl(){
            @Override
            public void afterTextChanged(Editable arg0) {
                String str1=arg0.toString();
            }
        });

        addRepair_gps_spinner=(Spinner)findViewById(R.id.addRepair_gps_spinner);
        addRepair_gps_spinner.setSelection(0);
        addRepair_gsm_spinner =(Spinner)findViewById(R.id.addRepair_gsm_spinner);
        addRepair_gsm_spinner.setSelection(0);
        addRepair_gps_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] gps = getResources().getStringArray(R.array.gps_spinner); //gpsAntenna
                if(0==position){
                    gpsPo="";
                }else{
                    gpsPo = gps[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addRepair_gsm_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] gsm = getResources().getStringArray(R.array.gsm_spinner); //gsmAntenna
                if(0==position){
                    gsmName="";
                }else{
                    gsmName = gsm[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        gridView=(MyGridView)findViewById(R.id.pic_gridview);
//
//        PicBean bean=new PicBean();
//        List<PicBean> list=new ArrayList<PicBean>();
//        list.add(bean);
//        mList=list;
//
//        actAdapter=new PhotoGridActAdapter(context, mList,"1");
//        actAdapter.setPhotoClickListener(new OnMyClickListener() {
//
//            @Override
//            public void onClick(View view, int position) {
//                checkPermission();
//
//            }
//        });
//        gridView.setAdapter(actAdapter);

        bt_save_re=(Button)findViewById(R.id.bt_save_re);
        bt_save_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtils.isFastDoubleClick()){//短时间多次点击
                    return;
                }
                if(Tools.isEmpty(token)){
                    showTips("登录token为空");
                    return;
                }
                if(Tools.isEmpty(repairId)){
                    showTips("故障维修记录标识id不能空");
                    return;
                }

                if(Tools.isEmpty(strC)){
                    showTips("维修方式不能为空");
                    return;
                }

                if(Tools.isEmpty(xp1)){
                    showTips("维修人员不能为空");
                    return;
                }
                String dirverName=et_dirver_name.getText().toString().trim();

                String dirverPhone=et_dirver_phone.getText().toString().trim();
                String zong=et_zong.getText().toString().trim();
                String zongjie="";
                if(!Tools.isEmpty(strn)){
                    if(!Tools.isEmpty(zong)){
                        zongjie=strn+","+zong;
                    }else{
                        zongjie=strn;
                    }

                }else{
                    if(!Tools.isEmpty(zong)){
                        zongjie=zong;
                    }else{
                        zongjie="";
                    }
                }

                String repairedRecords=strC;
                String mark=et_remark.getText().toString().trim();

                mpresenter.sendRepairRecord(repairId,token,repairedRecords,zongjie,mark,xp1,dirverName,dirverPhone,gpsPo,gsmName);
            }
        });


    }




    private  List<String> handlerData(String str){
        List<String> mlist=new ArrayList<>();
        if(str.contains("主机")){
            mlist.add("GPRS灯不正常");
            mlist.add("蓝牙连接后无ID显示或显示为0，计数器不跳");
            mlist.add("手机不能通过蓝牙连接主机");
            mlist.add("主机不断重启");
        }
        if(str.contains("传感器")){
            mlist.add("传感器粘贴处胶松动");
            mlist.add("传感器粘贴处被破坏");
            mlist.add("APP检测通道值显示52万");
            mlist.add("2000表检测通值异常");
            mlist.add("APP显示通道值异常");
            mlist.add("APP显示通道值为0");
            mlist.add("APP显示通道值不跳动");
            mlist.add("APP检测传感器乱跳");
        }

        if(str.contains("GPS")){
            mlist.add("司机遮挡天线");
            mlist.add("GPS天线破损");
        }

        return mlist;

    }

    @Override
    public void setNorOK(int num, String mFirst) {
        alist.get(num).setCheck(true);
    }

    @Override
    public void setNorCancle(int num, String mFirst) {
        alist.get(num).setCheck(false);
    }
    //展示 常见 故障现象描述
    private void showNormalDialog(List<String> list) {
        if(alist !=null && alist.size() >0){
            alist.clear();
        }
        for(int j=0;j<list.size();j++){
            String str1=list.get(j);
            A1 a1=new A1();
            a1.setStr(str1);
            alist.add(a1);
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.layout_repair_normal, null);
        builder.setView(view);
        builder.setCancelable(true);
        ListView list_1=(ListView)view.findViewById(R.id.list_1);
        RepairNormalAdapter madapter = new RepairNormalAdapter(context, alist,1,"9");
        list_1.setAdapter(madapter);
        TextView tv_cancel=(TextView)view.findViewById(R.id.tv_cancel);//取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddialog.cancel();
            }
        });
        TextView tv_ok=(TextView)view.findViewById(R.id.tv_ok);//确定
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddialog.cancel();
                mHandler.sendEmptyMessage(10);
            }
        });
        //取消或确定按钮监听事件处理
        adddialog = builder.create();
        adddialog.show();
    }

    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
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
    }

    @Override
    public void doError(String msg) {
        showTips(msg);
        tv_line1.setVisibility(View.GONE);
        ll_search.setVisibility(View.GONE);
    }

    @Override
    public void doLogin(UserResultBean bean) {

    }

    @Override
    public void doLoginFail(String str) {
    }

    @Override
    public void sendSuccess(String msg) {//上传维修记录成功
//        showTips(msg);
        //        Intent i=new Intent(BleConstant.ACTION_SEND_REPAIR_INFO_SUCCESS);
        //        i.putExtra("rep_number",repNo);
        //        sendBroadcast(i);
//        Intent  i=new Intent();
//        i.putExtra("rep_number",repNo);
//        setResult(10,i);

        showDialogSuccess(context,msg);
    }

    //成功提示对话框
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
                Intent  i=new Intent();
                i.putExtra("rep_number",repNo);
                setResult(10,i);
                finish();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mtempDialog !=null){
                    mtempDialog.dismiss();
                }
                Intent  i=new Intent();
                i.putExtra("rep_number",repNo);
                setResult(10,i);
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
    @Override
    public void sendError(String msg) {
        showTips(msg);
    }

    @Override
    public void sendPersonSuccess(List<InstallerInfo> info) {
        DLog.d("RepairAct","获取维修人员信息成功");
        inlist=info;
        mHandler.sendEmptyMessage(1);
    }

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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        repair_person.setAdapter(adapter);

                    }

                    break;
                case  10:
                    StringBuffer strbuf=new StringBuffer();
                    if(alist !=null && alist.size() >0 ){
                        for(int j=0;j<alist.size();j++){
                            if(alist.get(j).isCheck()){
                                String str1=alist.get(j).getStr();
                                strbuf.append(str1+",");
                            }
                        }

                    }
                    if(Tools.isEmpty(strbuf.toString())){
                        ll_change_1.setVisibility(View.GONE);
                    }else{
                        ll_change_1.setVisibility(View.VISIBLE);
                        strn= strbuf.substring(0,strbuf.length()-1);
                        tv_choosed_nomarl.setText(strn);
                    }
                    break;

            }
        }
    };
    @Override
    public void sendPersonError(String msg) {
        showTips(msg);
    }

    @Override
    public void doCompanySuccess(CompanyResultBean companyResult) {

    }

    @Override
    public void doCompanyError(String msg) {

    }

    @Override
    public void doAllSuccess(List<RepairModel> model) {

    }

    @Override
    public void doAllError(String msg) {

    }

    @Override
    public void doOnlineSuccess(List<OnLineTruckBean> model) {

    }

    @Override
    public void setOK(int num,String mFirst) {
        if("0".equals(mFirst)){
            list2_1.get(num).setCheck(true);
        }else if("1".equals(mFirst)){
            list2_2.get(num).setCheck(true);
        }
        //        else if("2".equals(mFirst)){
        //
        //        }
        else if("3".equals(mFirst)){
            list2_4.get(num).setCheck(true);
        }
        else if("4".equals(mFirst)){
            list2_5.get(num).setCheck(true);
        }
        else if("20".equals(mFirst)){
            list3_3.get(num).setCheck(true);
        }else if("21".equals(mFirst)){
            list3_1.get(num).setCheck(true);
        }
        showChanged();

    }


    @Override
    public void setCancle(int num,String mFirst) {
        if("0".equals(mFirst)){
            list2_1.get(num).setCheck(false);
        }else if("1".equals(mFirst)){
            list2_2.get(num).setCheck(false);
        }
        //        else if("2".equals(mFirst)){
        //
        //        }
        else if("3".equals(mFirst)){

            list2_4.get(num).setCheck(false);

        }
        else if("4".equals(mFirst)){

            list2_5.get(num).setCheck(false);

        }
        else if("20".equals(mFirst)){
            list3_3.get(num).setCheck(false);

        }else if("21".equals(mFirst)){
            list3_1.get(num).setCheck(false);
        }

        showChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }


    //已选择 的更换记录
    private void showChanged() {
        StringBuffer strChange=new StringBuffer(); //已选择
        if(list2_1 !=null && list2_1.size() >0   ){
            for(int w=0;w<list2_1.size();w++){
                if(list2_1.get(w).isCheck()){
                    String str0=list2_1.get(w).getStr();
                    strChange.append("更换主机终端-"+str0+",");
                }
            }
        }

        if(list2_2 !=null && list2_2.size() >0 ){
            for(int i=0;i<list2_2.size();i++){
                if(list2_2.get(i).isCheck()){
                    String str=list2_2.get(i).getStr();
                    strChange.append("更换采集器-"+str+",");
                }
            }
        }

        if(list2_4 !=null && list2_4.size() >0 ){
            for(int j=0;j<list2_4.size();j++){
                if(list2_4.get(j).isCheck()){
                    String str1=list2_4.get(j).getStr();
                    strChange.append("更换线材-"+str1+",");
                }
            }

        }

        if(list2_5 !=null && list2_5.size() >0 ){
            for(int n=0;n<list2_5.size();n++){
                if(list2_5.get(n).isCheck()){
                    String str25=list2_5.get(n).getStr();
                    strChange.append("更换GPS-"+str25+",");
                }
            }

        }

        if(list3_3 !=null && list3_3.size() >0 ){
            for(int k=0;k<list3_3.size();k++){
                if(list3_3.get(k).isCheck()){
                    String str2=list3_3.get(k).getStr();
                    strChange.append("更换传感器-应变计-"+str2+",");
                }
            }
        }

        if(list3_1!=null && list3_1.size() >0 ){
            for(int g=0;g<list3_1.size();g++) {
                if (list3_1.get(g).isCheck()) {
                    String str3 = list3_1.get(g).getStr();
                    strChange.append("更换传感器-17-4-" + str3 + ",");
                }
            }
        }

        if(Tools.isEmpty(strChange.toString())){
            ll_change.setVisibility(View.GONE);
        }else{
            ll_change.setVisibility(View.VISIBLE);
            strC= strChange.substring(0,strChange.length()-1);
            tv_choosed.setText(strC);
        }
    }


    @Override
    public void setNext(int position) {
        madapter.setCurrentItem(position);
        if(position ==0){
            RepairAdapter adapter1 = new RepairAdapter(context, list2_1,1,"0");
            list_2.setAdapter(adapter1);
            list_2.setVisibility(View.VISIBLE);
            list_3.setVisibility(View.INVISIBLE);
        }else if(position==1){
            RepairAdapter adapter2 = new RepairAdapter(context, list2_2,1,"1");
            list_2.setAdapter(adapter2);
            list_2.setVisibility(View.VISIBLE);
            list_3.setVisibility(View.INVISIBLE);
        }else if(position==2){
            adapter3 = new RepairAdapter(context, list2_3,0,"2");
            list_2.setAdapter(adapter3);
            list_2.setVisibility(View.VISIBLE);
        }else if(position==3){
            RepairAdapter adapter4 = new RepairAdapter(context, list2_4,1,"3");
            list_2.setAdapter(adapter4);
            list_2.setVisibility(View.VISIBLE);
            list_3.setVisibility(View.INVISIBLE);
        }else if(position==4){
            RepairAdapter adapter4 = new RepairAdapter(context, list2_5,1,"4");
            list_2.setAdapter(adapter4);
            list_2.setVisibility(View.VISIBLE);
            list_3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setNextSecond(int position) {
        adapter3.setCurrentItem(position);
        if(position ==0){
            RepairAdapter adapter33 = new RepairAdapter(context, list3_3,1,"20");
            list_3.setAdapter(adapter33);
            list_3.setVisibility(View.VISIBLE);
        }else{
            RepairAdapter adapter31 = new RepairAdapter(context, list3_1,1,"21");
            list_3.setAdapter(adapter31);
            list_3.setVisibility(View.VISIBLE);
        }
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
    public void setOK(int num) {
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
