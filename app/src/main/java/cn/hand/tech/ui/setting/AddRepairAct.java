package cn.hand.tech.ui.setting;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.bean.PicBean;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.KeyConstants;
import cn.hand.tech.common.OnMyClickListener;
import cn.hand.tech.common.OnMyItemClickListener;
import cn.hand.tech.common.TextWatcherImpl;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.setting.adapter.GuideAdapter;
import cn.hand.tech.ui.setting.adapter.PhotoGridActAdapter;
import cn.hand.tech.ui.setting.adapter.RepairAdapter;
import cn.hand.tech.ui.setting.adapter.RepairNormalAdapter;
import cn.hand.tech.ui.setting.bean.A1;
import cn.hand.tech.ui.setting.bean.CarNumberInfo;
import cn.hand.tech.ui.setting.bean.OnLineTruckBean;
import cn.hand.tech.ui.setting.bean.RepairModel;
import cn.hand.tech.ui.setting.presenter.AddRepairPresenter;
import cn.hand.tech.ui.weight.bean.InstallerInfo;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.CameraUtil;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.FileUtils;
import cn.hand.tech.utils.LogUtil;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.BottomSelectPopupWindow;
import cn.hand.tech.weiget.MyListView;

/*
 *新增维修记录
 */
public class AddRepairAct extends Activity implements IAddRecodeView, RepairAdapter.onNextListener, GuideAdapter.onGuideListener,RepairNormalAdapter.onNormalListener{
    private Context context;
    private ACache acache;
    private EditText tv_car_number,tv_gudier;
    private Button bt_search;
    private MyListView list_1,list_2,list_3;
    private List<String> vlist=new ArrayList<>();

    private AlertDialog adddialog;
    private String psw;
    private static final String PASSWORD_STRING = "6a9fd0d1a950420b812f27e970afcd8f"; // static final 静态常量
    private AddRepairPresenter mpresenter;
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
    private TextView ll_choose_normal;
    private   List<A1> alist=new ArrayList<>();
    private LinearLayout ll_change_1;
    private TextView tv_choosed_nomarl;
    private String strn;
    private EditText et_dirver_phone,et_dirver_name;
    private Spinner sp_repair_type;
    private String mrepairType;
    private RelativeLayout rl_spinner;
    private Spinner sp_repair_status,sp_repair_status_1,sp_repair_status_2;
    private EditText tv_other_thing;
    private int mtypePosition;
    private LinearLayout rl_spinner_1;
    private String mRepairStatus;
    private String mChuanganqi;
    private String mCgStatus;
    private CarNumberInfo minfo;
    private String repName;
    private TextView tv_gudier_1;
    private List<PicBean> mList=new ArrayList<>();
    private BottomSelectPopupWindow pickPhotoDlg;
    private File cameraFile;
    private MyGridView gridView;
    private PhotoGridActAdapter actAdapter;
    private String  from_Flag="SecondRepairAct";
    public static final int MAX_SHEET_COUNT = 9;// 详情最多张s
    private static final String TAG = "SecondRepairAct";
    private int REQUEST_WRITE_EXTERNAL_STORAGE=22;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_EDIT =15;
    private String path;
    private BroadcastReceiver receiver;
    private AlertDialog mtempDialog;

    public static void start(Context context,int number,String fault,String guide,String id,String truck) {
        Intent intent = new Intent(context, AddRepairAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_repair_add);

        acache= ACache.get(context,"WeightFragment");
        token=acache.getAsString("login_token");
        mpresenter = new AddRepairPresenter(context, this);
        initData();

        initView();
        mpresenter.getRepairPersoninfo(token);
        acache.put("truck_number","");
        registerBrodcat();
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


        tv_car_number=(EditText) findViewById(R.id.tv_car_number); //车牌号码

        tv_car_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str=s.toString();
                if(!Tools.isEmpty(str)){
                    HashMap hmap=new HashMap();
                    hmap.put("token",token);
                    hmap.put("carNumber",str);
                    mpresenter.checkCarNumberExisit(hmap);
                }

            }
        });


        sp_repair_type=(Spinner)findViewById(R.id.sp_repair_type);
        rl_spinner=(RelativeLayout)findViewById(R.id.rl_spinner);
        sp_repair_status=(Spinner)findViewById(R.id.sp_repair_status);
        rl_spinner.setVisibility(View.GONE);
        tv_other_thing=(EditText)findViewById(R.id.tv_other_thing);
        tv_other_thing.setVisibility(View.GONE);
        rl_spinner_1=(LinearLayout)findViewById(R.id.rl_spinner_1);
        sp_repair_status_1=(Spinner)findViewById(R.id.sp_repair_status_1);
        sp_repair_status_2=(Spinner)findViewById(R.id.sp_repair_status_2);
        rl_spinner_1.setVisibility(View.GONE);

        sp_repair_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] type  =  getResources().getStringArray(R.array.repairtype);
                if(position != 0){
                    mrepairType=type[position];
                }else{
                    mrepairType="";
                }
                mtypePosition=position;
                if(position == 1){
                    rl_spinner.setVisibility(View.VISIBLE);
                    rl_spinner_1.setVisibility(View.GONE);
                    tv_other_thing.setVisibility(View.GONE);

                    tv_gudier_1.setVisibility(View.VISIBLE);
                    tv_gudier.setVisibility(View.GONE);

                    List<String> zhulist=new ArrayList<>();
                    zhulist.add("请选择");
                    zhulist.add("离线");
                    zhulist.add("未识别上货");
                    ArrayAdapter     zadapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,zhulist);

                    zadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_repair_status.setAdapter(zadapter);
                }else if(position ==2){
                    rl_spinner.setVisibility(View.GONE);
                    rl_spinner_1.setVisibility(View.VISIBLE);
                    tv_other_thing.setVisibility(View.GONE);

                    tv_gudier_1.setVisibility(View.VISIBLE);
                    tv_gudier.setVisibility(View.GONE);
                }else if(position ==3){
                    rl_spinner.setVisibility(View.VISIBLE);
                    rl_spinner_1.setVisibility(View.GONE);
                    tv_other_thing.setVisibility(View.GONE);

                    tv_gudier_1.setVisibility(View.VISIBLE);
                    tv_gudier.setVisibility(View.GONE);

                    List<String> gpslist=new ArrayList<>();
                    gpslist.add("请选择");
                    gpslist.add("GPS丢失");
                    gpslist.add("GPS漂移");
                    ArrayAdapter     gpadapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,gpslist);

                    gpadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_repair_status.setAdapter(gpadapter);

                }else if(position ==4){
                    rl_spinner.setVisibility(View.VISIBLE);
                    rl_spinner_1.setVisibility(View.GONE);
                    tv_other_thing.setVisibility(View.GONE);

                    tv_gudier_1.setVisibility(View.VISIBLE);
                    tv_gudier.setVisibility(View.GONE);

                    List<String> gpslist=new ArrayList<>();
                    gpslist.add("请选择");
                    gpslist.add("无通道");
                    gpslist.add("全部平直");
                    gpslist.add("过磅传感器不跳");
                    ArrayAdapter     gpadapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,gpslist);

                    gpadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_repair_status.setAdapter(gpadapter);
                }else if(position ==5){
                    rl_spinner.setVisibility(View.GONE);
                    rl_spinner_1.setVisibility(View.GONE);
                    tv_other_thing.setVisibility(View.VISIBLE);
                    tv_gudier_1.setVisibility(View.GONE);
                    tv_gudier.setVisibility(View.VISIBLE);
                }else{
                    tv_gudier_1.setText("");
                    tv_gudier.setText("");
                    tv_gudier_1.setVisibility(View.GONE);
                    tv_gudier.setVisibility(View.GONE);
                    rl_spinner.setVisibility(View.GONE);
                    rl_spinner_1.setVisibility(View.GONE);
                    tv_other_thing.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_repair_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringBuffer sbuffer=new StringBuffer();
                if(mtypePosition ==1){
                    String[] zhuji  =  getResources().getStringArray(R.array.zhuji); //主机
                    if(position !=0){
                        sbuffer.append(zhuji[position]);

                        if(1==position){
                            tv_gudier_1.setText("检查电源、主机工作状态等");
                        }else if(position ==2 ){
                            tv_gudier_1.setText("更换主机");
                        }else {
                            tv_gudier_1.setText("");
                        }

                    }else{
                        sbuffer.append("");
                        tv_gudier_1.setText("");
                    }
                }else if(mtypePosition ==3){
                    String[] gps  =  getResources().getStringArray(R.array.gpswenti); //GPS
                    if(position !=0){
                        sbuffer.append(gps[position]);

                        if(1==position){
                            tv_gudier_1.setText("检查天线");
                        }else if(position ==2 ){
                            tv_gudier_1.setText("检查天线");
                        }else {
                            tv_gudier_1.setText("");
                        }
                    }else{
                        sbuffer.append("");
                        tv_gudier_1.setText("");
                    }
                }
                else if(mtypePosition ==4){
                    String[] xiancai  =  getResources().getStringArray(R.array.xiancai);
                    if(position !=0){
                        sbuffer.append(xiancai[position]);

                        tv_gudier_1.setText("检查接线、4pin接口等");

                    }else{
                        sbuffer.append("");
                        tv_gudier_1.setText("");
                    }
                }
                mRepairStatus=sbuffer.toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_repair_status_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringBuffer sbuffer1=new StringBuffer();
                String[] chuanganqi  =  getResources().getStringArray(R.array.chuanganqi);
                if(position !=0){
                    sbuffer1.append(chuanganqi[position]);
                }else{
                    sbuffer1.append("");
                }
                mChuanganqi=sbuffer1.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_repair_status_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  //传感器
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringBuffer sbuffer2=new StringBuffer();
                String[] cgqstatus  =  getResources().getStringArray(R.array.cgqstatus);
                if(position !=0){
                    sbuffer2.append(mChuanganqi+"-"+cgqstatus[position]);
                    if(1==position){
                        tv_gudier_1.setText("更换传感器");
                    }else if(position ==2 || position ==3 || position ==4 ){
                        tv_gudier_1.setText("检查接线、4pin接口等");
                    }else if(position ==5 || position ==6 ){
                        tv_gudier_1.setText("更换传感器");
                    }else {
                        tv_gudier_1.setText("");
                    }


                }else{
                    sbuffer2.append("");
                    tv_gudier_1.setText("");
                }
                mCgStatus=sbuffer2.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        tv_gudier=(EditText)findViewById(R.id.tv_gudier); //维修指导
        tv_gudier.setVisibility(View.GONE);
        tv_gudier_1=(TextView)findViewById(R.id.tv_gudier_1);
        tv_gudier_1.setVisibility(View.VISIBLE);

        tv_line1=(TextView)findViewById(R.id.tv_line1);
        ll_search=(LinearLayout)findViewById(R.id.ll_search);

        et_dirver_name=(EditText)findViewById(R.id.et_dirver_name); //司机姓名
        et_dirver_phone=(EditText)findViewById(R.id.et_dirver_phone);//司机电话


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
                    repName=inlist.get(position-1).getName();
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
                //                if(Tools.isEmpty(repairId)){
                //                    showTips("故障维修记录标识id不能空");
                //                    return;
                //                }

                String carNumer=tv_car_number.getText().toString().trim();
                if(Tools.isEmpty(carNumer)){
                    showTips("车牌号码不能空");
                    return;
                }

                if(Tools.isEmpty(mrepairType)){
                    showTips("故障分类不能空");
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
                StringBuffer sbuffer0=new StringBuffer();     //故障现象
                StringBuffer sbuffer1=new StringBuffer();     //维修指导
                if(1==mtypePosition || 3 ==mtypePosition || 4 ==mtypePosition){
                    sbuffer0.append(mRepairStatus);
                    String str=tv_gudier_1.getText().toString().trim();
                    sbuffer1.append(str);
                }else if(2==mtypePosition){
                    sbuffer0.append(mCgStatus);
                    String str=tv_gudier_1.getText().toString().trim();
                    sbuffer1.append(str);
                }else if(5 ==mtypePosition){
                    String edStr=tv_other_thing.getText().toString().trim();
                    if(Tools.isEmpty(edStr)){
                        edStr="";
                    }
                    sbuffer0.append(edStr);
                    String str=tv_gudier.getText().toString().trim();
                    sbuffer1.append(str);
                }
                String guzhangStatus=sbuffer0.toString();//故障现象
                DLog.e("AddRepairAct","故障现象:"+guzhangStatus);

                String repairGudie=sbuffer1.toString();//维修指导
                DLog.e("AddRepairAct","维修指导:"+repairGudie);

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

                if(minfo !=null ){
                    String devId=minfo.getDeviceId();
                    if(Tools.isEmpty(devId)){
                        showTips("该车辆设备ID为空");
                        return;
                    }
                    mpresenter.sendRepairRecord(devId,token,carNumer,mrepairType,guzhangStatus,repairGudie,repairedRecords,zongjie,mark,repName,dirverName,dirverPhone,mList);
                }else{
                    showTips("该车辆信息为空");
                }

            }
        });


        gridView=(MyGridView)findViewById(R.id.pic_gridview);

        PicBean bean=new PicBean();
        List<PicBean> list=new ArrayList<PicBean>();
        list.add(bean);
        mList=list;

        actAdapter=new PhotoGridActAdapter(context, mList,"1");
        actAdapter.setPhotoClickListener(new OnMyClickListener() {

            @Override
            public void onClick(View view, int position) {
                checkPermission();

            }
        });
        gridView.setAdapter(actAdapter);


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
//        finish();
        showDialogSuccess(context,msg);
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
                case  2:
                    actAdapter.setList(mList);
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
    public void doCompanySuccess(CarNumberInfo info) {
        minfo=info;
        showTips("获取车辆信息成功");
        LogUtil.d("根据车牌号获取车辆信息成功=" + info.getDeviceId());

    }

    @Override
    public void doCompanyError(String msg) {
        showTips(msg);
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



    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                //                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限READ_EXTERNAL_STORAGE
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_WRITE_EXTERNAL_STORAGE);

        } else {//已经授权

            photoDialogPop();//

            DLog.e("AddRepairAct","TAG_SERVICE"+ "checkPermission: 已经授权！");
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
                pickPhotoDlg = new BottomSelectPopupWindow(this, new String[] { "拍照", "从相册选取" }, null);
                pickPhotoDlg.setOnItemClickListener(new OnMyItemClickListener() {


                    @Override
                    public void onItemClick(View parent, View view, int which) {
                        if (which == 0) {
                            pickToCamera();
                        } else {
                            pickToAlbum();
                        }
                    }
                });
            }
            pickPhotoDlg.showAtLocation(ll_content, Gravity.BOTTOM, 0, 0);
        }catch ( Exception e){
            e.printStackTrace();
        }
    }
    private void pickToAlbum() {
        LookImageActivity.startPick(this, MAX_SHEET_COUNT, "选择照片",from_Flag);
    }


    private void pickToCamera() {
        try{
            cameraFile = new File(FileUtils.getAppSdcardDir() + "/" + System.currentTimeMillis() + ".jpg");
            cameraFile.getParentFile().mkdirs();
            //        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), REQUEST_CODE_CAMERA_P);

            CameraUtil.openCamera(this,cameraFile,REQUEST_CODE_CAMERA);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA) {// 返回拍照结果
            if (cameraFile != null && cameraFile.exists()) {
                ArrayList<String> path = new ArrayList<String>();
                path.add(cameraFile.getAbsolutePath());
                Intent intent = new Intent(KeyConstants.BROADCAST_PATH);
                intent.putExtra(KeyConstants.IMAGE_FROM_FLAG, from_Flag);
                intent.putExtra("path", path);
                sendBroadcast(intent);
                DLog.e(TAG,"拍照发送照片路径："+cameraFile.getAbsolutePath());
            }
        }else if(requestCode==REQUEST_CODE_EDIT){//编辑照片页面返回
            initData();
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
                // 选择图片返回图片路径
                if (intent.getAction().equals(KeyConstants.BROADCAST_PATH) && intent.getStringArrayListExtra("path") != null
                        && intent.getStringExtra(KeyConstants.IMAGE_FROM_FLAG).equals(from_Flag)) {
                    ArrayList<String> pickedImg=intent.getStringArrayListExtra("path");
                    DLog.e(TAG,"接收照片路径:"+ Arrays.asList(pickedImg));
                    savePic(pickedImg);
                }else if(intent.getAction().equals(KeyConstants.ACTION_ADD_PHOTO_PRIVATE)
                        || intent.getAction().equals(KeyConstants.ACTION_SET_PHOTO_PRIVATE)
                        || (intent.getAction().equals(KeyConstants.ACTION_DELETE_PHOTO) &&  !"1".equals(intent.getStringExtra("flag_from")))){
                    initData();
                }
            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(KeyConstants.BROADCAST_PATH);
        filter.addAction(KeyConstants.ACTION_ADD_PHOTO_PRIVATE);
        filter.addAction(KeyConstants.ACTION_SET_PHOTO_PRIVATE);
        filter.addAction(KeyConstants.ACTION_DELETE_PHOTO);
        this.registerReceiver(receiver, filter);
    }
    public void savePic(   ArrayList<String> pickedImg ){ //保存图片 添加图片显示
        try{
            for(int i=0;i<pickedImg.size();i++){
                String path=pickedImg.get(i);
                Uri uriForFile = FileProvider.getUriForFile(this, context.getResources().getString(R.string.authorities),new File(path));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriForFile);
                // 首先保存图片
                String dirPath = FileUtils.getAppSdcardDir() + "/" + "bg_image" + "/";
                File dirFile = new File(dirPath);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                final File file = new File(dirFile, fileName);

                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

                PicBean bean=new PicBean();
                bean.setUrl(file.getAbsolutePath());
                mList.add(bean);
                mHandler.sendEmptyMessage(2);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}
