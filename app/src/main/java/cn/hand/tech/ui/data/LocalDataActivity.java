package cn.hand.tech.ui.data;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import cn.hand.tech.R;
import cn.hand.tech.bean.WeightDataBean;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.ConsTantsCode;
import cn.hand.tech.dao.WeightDao;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.data.adapter.LocalDataAdapter;
import cn.hand.tech.ui.data.bean.LocalDataTimeModel;
import cn.hand.tech.ui.data.presenter.SaveMVVData;
import cn.hand.tech.ui.weight.presenter.SaveWeightDataTask;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.DateUtil;
import cn.hand.tech.utils.DialogUtil;
import cn.hand.tech.utils.ToastUtil;

//本地数据
public class LocalDataActivity extends AppCompatActivity implements View.OnClickListener {
    private List<LocalDataTimeModel> listData;
    private List<WeightDataBean> weightDataBeanList;
    private ImageView mIvBack;
    private TextView mTvBack;
    private TextView mTvParaTitle;
    private TextView mBtnParaDelete;
    private ListView mLvLocalData;
    private Button mBtnAllChooseLocal;
    private Button mBtnDelLocal;
    private Button mBtnSendLocalEmail;
    private Button mBtnSaveLocalData;
    private LinearLayout ll_back;
    private LocalDataAdapter localDataAdapter;
    private static final String TAG = "LocalDataActivity";
    private boolean isEditor = true;
    private static final String TAG1 = "WeightFragment";
    private boolean isAllChoose = false;
    private ProgressDialog dialog;
    Context context;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConsTantsCode.REQUEST_CODE_SUCCESS:
                    if(dialog !=null){
                        dialog.dismiss();
                    }
                    initEditor();
                    String message = (String) msg.obj;
                    showTips(message);
                    break;
                case ConsTantsCode.REQUEST_CODE_FAIL:
                    initEditor();
                    dialog.dismiss();
                    break;

            }
        }
    };
    private ACache acache;
    private String truckNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_local_data);
        acache = ACache.get(context, TAG1);
        truckNum= acache.getAsString("car_num");
        initView();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvParaTitle = (TextView) findViewById(R.id.tv_para_title);
        mBtnParaDelete = (TextView) findViewById(R.id.tv_et);
        mLvLocalData = (ListView) findViewById(R.id.lv_local_data);
        mBtnAllChooseLocal = (Button) findViewById(R.id.btn_allChoose_local);
        mBtnDelLocal = (Button) findViewById(R.id.btn_del_local);
        mBtnSendLocalEmail = (Button) findViewById(R.id.btn_send_local_email);
        mBtnSaveLocalData = (Button) findViewById(R.id.btn_save_local_data);
        mTvParaTitle.setText("本地历史数据");
        ll_back.setOnClickListener(this);
        mBtnParaDelete.setOnClickListener(this);
        mBtnAllChooseLocal.setOnClickListener(this);
        mBtnDelLocal.setOnClickListener(this);
        mBtnSendLocalEmail.setOnClickListener(this);
        mBtnSaveLocalData.setOnClickListener(this);

        /*去重合并*/
        initLocalDataList();

        localDataAdapter = new LocalDataAdapter(listData, LocalDataActivity.this);
        mLvLocalData.setAdapter(localDataAdapter);
        mLvLocalData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isEditor) {
                    DLog.e(TAG,"点 击的位置position==" + position);
                    LocalDataAdapter.ViewHolder views = (LocalDataAdapter.ViewHolder) view.getTag();
                    views.cb_local_item.toggle();
                    CheckBox delCheckBox = views.cb_local_item;
                    isEditor = false;
                    isAllChoose = false;
                    localDataAdapter.isMapSelected.put(position, delCheckBox.isChecked());
                    localDataAdapter.changeSelected(listData, isEditor);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("weightDataBeanList", (Serializable) listData.get(position).getWeightDataBeanList());
                    bundle.putString("localDate", listData.get(position).getLocalDataTime());
                    CommonKitUtil.startActivity(LocalDataActivity.this, LocalDataDetailActivity.class, bundle, false);
                }
            }
        });


    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    private void initLocalDataList() {
        listData = new ArrayList<>();
        weightDataBeanList = new ArrayList<>();
        weightDataBeanList = WeightDao.getInstance(context).findWeightDataBeans("1");
        ArrayList dateList = new ArrayList();
        for (int i = 0; i < weightDataBeanList.size(); i++) {
            String date = weightDataBeanList.get(i).getDate();
            if (!dateList.contains(date)) {
                dateList.add(date);
                List<WeightDataBean> weightList = new ArrayList<>();
                for (int j = 0; j < weightDataBeanList.size(); j++) {
                    WeightDataBean weightDataBean = weightDataBeanList.get(j);
                    WeightDataBean model = new WeightDataBean();
                    model.setNub(weightDataBean.getNub());
                    model.setDate(weightDataBean.getDate());
                    model.setLocation(weightDataBean.getLocation());
                    model.setId(weightDataBean.getId());

                    model.setUploadDate(weightDataBean.getUploadDate());
                    model.setDeviceId(weightDataBean.getDeviceId());
                    model.setCarNumber(weightDataBean.getCarNumber());
                    model.setWeightFromDevice(weightDataBean.getWeightFromDevice());
                    model.setWeightFromReal(weightDataBean.getWeightFromReal());
                    model.setHandBrakeHardwareStatus(weightDataBean.getHandBrakeHardwareStatus());
                    model.setPackageNum(weightDataBean.getPackageNum());
                    model.setSpeed(weightDataBean.getSpeed());
                    model.setX(weightDataBean.getX());
                    model.setY(weightDataBean.getY());
                    model.setGpsUploadDate(weightDataBean.getGpsUploadDate());
                    model.setStableStatus(weightDataBean.getStableStatus());
                    model.setDeviceVoltage(weightDataBean.getDeviceVoltage());
                    model.setShipmentStatus(weightDataBean.getShipmentStatus());
                    model.setAch1(weightDataBean.getAch1());
                    model.setAch2(weightDataBean.getAch2());
                    model.setAch3(weightDataBean.getAch3());
                    model.setAch4(weightDataBean.getAch4());
                    model.setAch5(weightDataBean.getAch5());
                    model.setAch6(weightDataBean.getAch6());
                    model.setAch7(weightDataBean.getAch7());
                    model.setAch8(weightDataBean.getAch8());
                    model.setAch9(weightDataBean.getAch9());
                    model.setAch10(weightDataBean.getAch10());
                    model.setAch11(weightDataBean.getAch11());
                    model.setAch12(weightDataBean.getAch12());
                    model.setAch13(weightDataBean.getAch13());
                    model.setAch14(weightDataBean.getAch14());
                    model.setAch15(weightDataBean.getAch15());
                    model.setAch16(weightDataBean.getAch16());
                    model.setSch1(weightDataBean.getSch1());
                    model.setSch2(weightDataBean.getSch2());
                    model.setSch3(weightDataBean.getSch3());
                    model.setSch4(weightDataBean.getSch4());
                    model.setSch5(weightDataBean.getSch5());
                    model.setSch6(weightDataBean.getSch6());
                    model.setSch7(weightDataBean.getSch7());
                    model.setSch8(weightDataBean.getSch8());
                    model.setSch9(weightDataBean.getSch9());
                    model.setSch10(weightDataBean.getSch10());
                    model.setSch11(weightDataBean.getSch11());
                    model.setSch12(weightDataBean.getSch12());
                    model.setSch13(weightDataBean.getSch13());
                    model.setSch14(weightDataBean.getSch14());
                    model.setSch15(weightDataBean.getSch15());
                    model.setSch16(weightDataBean.getSch16());

                    if (model.getDate().equals(date)) {
                        weightList.add(model);
                    }

                }
                LocalDataTimeModel localDataTimeModel = new LocalDataTimeModel();
                localDataTimeModel.setLocalDataTime(date);
                localDataTimeModel.setWeightDataBeanList(weightList);
                listData.add(localDataTimeModel);
            }

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //返回
            case R.id.ll_back:
                setResult(1001);
                finish();
                break;
            //编辑
            case R.id.tv_et:
                if (listData == null || listData.size() == 0) {
                   showTips("无数据");
                    return;
                }
                if (isEditor) {
                    isEditor = false;
                    mBtnParaDelete.setText("停止");
                    localDataAdapter.changeSelected(listData, isEditor);
                } else {
                    initEditor();
                }
                break;
            //全选
            case R.id.btn_allChoose_local:
                if (listData == null || listData.size() == 0) {
                    showTips("无数据");
                    return;
                }
                if (!isEditor) {
                    localDataAdapter.delContactsIdSet.clear();
                    if (!isAllChoose) {
                        isAllChoose = true;
                        for (int j = 0; j < listData.size(); j++) {
                            localDataAdapter.isMapSelected.put(j, isAllChoose);
                            localDataAdapter.delContactsIdSet.add(j);
                        }
                        localDataAdapter.changeSelected(listData, isEditor);
                    } else {
                        isAllChoose = false;
                        for (int j = 0; j < listData.size(); j++) {
                            localDataAdapter.isMapSelected.put(j, isAllChoose);
                            localDataAdapter.delContactsIdSet.remove(j);
                        }
                        localDataAdapter.changeSelected(listData, isEditor);
                    }

                } else {
                    showTips("请编辑");
                }
                break;
            //删除
            case R.id.btn_del_local:
                if (listData == null || listData.size() == 0) {
                    showTips("无数据");
                    return;
                }
                if (!isEditor) {
                    final AlertDialog.Builder myDialog = new AlertDialog.Builder(LocalDataActivity.this);
                    //nomalDialog.setIcon(R.drawable.icon_dialog);
                    myDialog.setTitle("提示");
                    myDialog.setMessage("是否删除?");
                    myDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //TreeSet有序的集合，会Set和HashSet无序集合；
                            TreeSet<Integer> setDelete = localDataAdapter.delContactsIdSet;
                            Iterator<Integer> iterator = setDelete.iterator();
                            while (iterator.hasNext()) {
                                int position = iterator.next();//不能两次iterator.next()
                                DLog.e(TAG,"删除列表的位置position==" + position);
                                if (setDelete.size() > listData.size()) {
                                    return;
                                }
                                List<WeightDataBean> weightDataList = listData.get(position).getWeightDataBeanList();//取到这个位置的数据list
                                if(weightDataList !=null && weightDataList.size() > 0){
                                    String date =weightDataList.get(0).getDate();//用储存时间    只需取第一个数据的date就行
                                    WeightDao.getInstance(context).delWeightDataBean(date,"1");
                                }else{
                                    DLog.e(TAG,"删除位置数据异常");
                                }
                            }
                            /*删除后再从是数据库查询，合并去重*/
                            initLocalDataList();
                            isEditor = true;
                            mBtnParaDelete.setText("编辑");
                            localDataAdapter.delContactsIdSet.clear();
                            localDataAdapter.initData();
                            localDataAdapter.changeSelected(listData, isEditor);
                            showTips("删除成功");

                        }
                    });
                    myDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            initEditor();

                        }
                    });
                    myDialog.show();


                } else {
                    showTips("请编辑");
                }
                break;
            //发送邮件
            case R.id.btn_send_local_email:
                if (!CommonKitUtil.isNetworkAvailable(LocalDataActivity.this)) {
                    showTips("网络未连接，请检查您的网络连接情况");
                    return;
                }
                if (listData == null || listData.size() == 0) {
                    showTips("无数据");
                    return;
                }
                if (!isEditor) {
                    TreeSet<Integer> setDelete = localDataAdapter.delContactsIdSet;
                    if (setDelete.size() > 0) {
                        Iterator<Integer> iterator = setDelete.iterator();
                        List<LocalDataTimeModel> dataList = new ArrayList<>();
                        while (iterator.hasNext()) {
                            int position = iterator.next();//不能两次iterator.next()
                            DLog.e(TAG,"发送邮件选择的position==" + position);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                            String ndate=listData.get(position).getLocalDataTime();
                            String time3 = DateUtil.getDateFor(ndate);
                            List<WeightDataBean> weightDataList = listData.get(position).getWeightDataBeanList();
                            LocalDataTimeModel model = new LocalDataTimeModel();
                            String carNumber=weightDataList.get(0).getCarNumber()+"_";
                            model.setLocalDataTime(carNumber+time3);
                            model.setWeightDataBeanList(weightDataList);
                            dataList.add(model);
                        }
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String time1 =formatter.format(curDate);
                        DLog.e(TAG,"发送邮件时的时间==" + time1);
                        SaveMVVData.saveLocalData(LocalDataActivity.this, dataList, time1);
//                        SaveMVVData.sendXX(LocalDataActivity.this, time1);
                        EmailStateAct.start(this,time1);
                    } else {
                        showTips("请选择要发送的数据");
                    }


                } else {
                    showTips("请编辑");
                }
                break;
            //发送给服务器
            case R.id.btn_save_local_data:
                if (!CommonKitUtil.isNetworkAvailable(LocalDataActivity.this)) {
                    showTips("网络未连接，请检查您的网络连接情况");
                    return;
                }
                if (listData == null || listData.size() == 0) {
                    showTips("无数据");
                    return;
                }
                if (!isEditor) {
                    TreeSet<Integer> setDelete = localDataAdapter.delContactsIdSet;
                    DLog.e(TAG,"选择的大小==" + setDelete.size());
                    if (setDelete.size() > 0) {
                        dialog = new ProgressDialog(this);
                        DialogUtil.setProgressDialog(dialog, "保存中...");
                        Iterator<Integer> iterator = setDelete.iterator();
                        final List<WeightDataBean> dataList = new ArrayList<>();
                        while (iterator.hasNext()) {
                            int position = iterator.next();//不能两次iterator.next()
                            DLog.e(TAG,"发送服务器选择的position==" + position);
                            List<WeightDataBean> weightDataList = listData.get(position).getWeightDataBeanList();
                            dataList.addAll(weightDataList);
                        }
                        new Thread() {
                            public void run() {
                                SaveWeightDataTask.getInstance(LocalDataActivity.this, mHandler).SaveData(dataList);
                            }
                        }.start();


                    } else {
                        showTips("请选择要发送的数据");
                    }


                } else {
                    showTips("请编辑");
                }
                break;
        }
    }

    private void initEditor() {
        isEditor = true;
        localDataAdapter.delContactsIdSet.clear();
        localDataAdapter.initData();
        localDataAdapter.changeSelected(listData, isEditor);
        mBtnParaDelete.setText("编辑");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DLog.e(TAG,"本地发送邮件回调requestCode==" + requestCode + ",resultCode==" + resultCode);
        switch (resultCode) {
            case ConsTantsCode.REQUEST_CODE_EMAIL_SUCCESS:
            case ConsTantsCode.REQUEST_CODE_EMAIL_SUCCESS1:
                initEditor();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(1001);
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DLog.e(TAG,"本地关闭");
    }
}
