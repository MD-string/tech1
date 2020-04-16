package cn.hand.tech.ui.curve.view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDKRModel;
import cn.hand.tech.bean.HDModelData;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.curve.bean.Curvenfo;
import cn.hand.tech.ui.home.BaseFragment;
import cn.hand.tech.ui.home.MainFragmentActivity;
import cn.hand.tech.ui.setting.PassSettingActNew;
import cn.hand.tech.utils.ToastUtil;

/**
 *  曲线
 * @author hxz
 */

public class CurveFragment extends BaseFragment implements OnClickListener {

    private BroadcastReceiver receiver;
    private MainFragmentActivity mactivity;

    private LineChart chart;
    private RelativeLayout rl_rooter;
    private ImageView img_2;
    private TextView tv_no_weightData;
    private HDModelData hdData;
    private List<Float>hlist1=new ArrayList();
    private List<Float>hlist2=new ArrayList();
    private List<Float>hlist3=new ArrayList();
    private List<Float>hlist4=new ArrayList();
    private List<Float>hlist5=new ArrayList();
    private List<Float>hlist6=new ArrayList();
    private List<Float>hlist7=new ArrayList();
    private List<Float>hlist8=new ArrayList();
    private List<Float>hlist9=new ArrayList();
    private List<Float>hlist10=new ArrayList();
    private List<Float>hlist11=new ArrayList();
    private List<Float>hlist12=new ArrayList();
    private List<Float>hlist13=new ArrayList();
    private List<Float>hlist14=new ArrayList();
    private List<Float>hlist15=new ArrayList();
    private List<Float>hlist16=new ArrayList();

    private List<Float>ylist1=new ArrayList();
    private List<Float>ylist2=new ArrayList();
    private List<Float>ylist3=new ArrayList();
    private List<Float>ylist4=new ArrayList();
    private List<Float>ylist5=new ArrayList();
    private List<Float>ylist6=new ArrayList();
    private List<Float>ylist7=new ArrayList();
    private List<Float>ylist8=new ArrayList();
    private List<Float>ylist9=new ArrayList();
    private List<Float>ylist10=new ArrayList();
    private List<Float>ylist11=new ArrayList();
    private List<Float>ylist12=new ArrayList();
    private List<Float>ylist13=new ArrayList();
    private List<Float>ylist14=new ArrayList();
    private List<Float>ylist15=new ArrayList();
    private List<Float>ylist16=new ArrayList();
    private TextView tv_tong_dao,tv_history;
    private ACache acache;
    private   HDKRModel channelModel;
    private List<String> currentList;
    private boolean ishistory=false;
    private boolean isConnected;
    private MediaPlayer myMediaPlayer;
    private TextView tv_y;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mactivity=	(MainFragmentActivity)getActivity();
        getTopNavigation().setNavLineVisibility(View.GONE);
        final View view = inflater.inflate(R.layout.frg_crve, container, false);
        myMediaPlayer = new MediaPlayer();
        acache= ACache.get(mactivity,"WeightFragment");
        initView(view);
        EventBus.getDefault().register(this);
        registerBrodcat();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view) {
        tv_tong_dao=(TextView)view.findViewById(R.id.tv_tong_dao);//通道选择
        tv_tong_dao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PassSettingActNew.start(mactivity,"0");
            }
        });
        tv_history=(TextView)view.findViewById(R.id.tv_history);//历史数据
        tv_history.setText("历史数据");
        tv_history.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ishistory){
                    ishistory=true;
                    tv_history.setText("实时数据");
                    handler.sendEmptyMessage(1);//历史数据
                    tv_history.setTextColor(getResources().getColor(R.color.white));
                }else{
                    ishistory=false;
                    tv_history.setText("历史数据");
                    tv_history.setTextColor(getResources().getColor(R.color.text_color_shen));
                    handler.sendEmptyMessage(2);//实时数据
                }
            }
        });
        rl_rooter=(RelativeLayout)view.findViewById(R.id.rl_rooter);
        chart = (LineChart) view.findViewById(R.id.chart);
        tv_no_weightData = (TextView)view.findViewById(R.id.tv_no_weightData);
        tv_y=(TextView)view.findViewById(R.id.tv_y);
        tv_y.setVisibility(View.GONE);
        chart.setVisibility(View.GONE);
        tv_no_weightData.setVisibility(View.VISIBLE);

        channelModel = (HDKRModel)acache.getAsObject("channel_model");//保存多少通道
        if(channelModel ==null ){
            channelModel=new HDKRModel();
            channelModel.bChannel1=true;channelModel.bChannel2=true;
            channelModel.bChannel3=true;channelModel.bChannel4=true;
            channelModel.bChannel5=true;channelModel.bChannel6=true;
            channelModel.bChannel7=true;channelModel.bChannel8=true;
            channelModel.bChannel9=true;channelModel.bChannel10=true;
            channelModel.bChannel11=false;
            channelModel.bChannel12=false;
            channelModel.bChannel13=false;
            channelModel.bChannel14=false;
            channelModel.bChannel15=false;
            channelModel.bChannel16=false;
            acache.put("channel_model",channelModel);
        }
        initList();
    }
    private void initList(){
        currentList = (List<String>) acache.getAsObject("et_number");//AD阀值
        if(currentList ==null || currentList.size() <=0){
            currentList=new ArrayList<>();
            for(int i=0;i<18;i++){
                if(i==16){
                    currentList.add("25");
                }else if(i==17){
                    currentList.add("0");
                }else{
                    currentList.add("5000");
                }
            }
        }
        String  isCon=acache.getAsString("is_connect");
        if("2".equals(isCon)){
            isConnected=true;
        }else{
            isConnected=false;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }

    }

    private void showResult(final   List<Curvenfo> list, final boolean ishistory) {

        mactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    float xMax=60f;
                    float xmin=0f;
                    float xMid=5f;
                    int count=12;
                    if(ishistory){
                        xMax=700f;
                        xmin=0f;
                        xMid=70f;
                        count=10;
                    }
                    //Y轴获取当前最大最小值 建立坐标系
                    float   maxWei =getMax(list)+7.5f;
                    float   minWei = getMin(list)-7.5f;

                    float   midWei = (maxWei - minWei) / 20f; //平均值

                    final ArrayList<ILineDataSet> dataSets = new ArrayList<>();

                    Description description = new Description();
                    description.setText("时间(s)");
                    description.setTextColor(Color.BLACK);
                    description.setTextSize(10);
                    chart.setDescription(description);//设置图表描述信息
                    chart.setNoDataText("暂无重量数据");//没有数据时显示的文字
                    chart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
                    chart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
                    chart.setDrawBorders(false);//禁止绘制图表边框的线
                    //chart.setBorderColor(); //设置 chart 边框线的颜色。
                    //chart.setBorderWidth(); //设置 chart 边界线的宽度，单位 dp。
                    //chart.setLogEnabled(true);//打印日志
                    //                        chart.notifyDataSetChanged();//刷新数据
                    //                        chart.invalidate();//重绘


                    for(int i=0;i<list.size();i++){
                        Curvenfo info=list.get(i);
                        boolean ishow=info.isShow();
                        if(ishow){
                            //设置数据1  参数1：数据源 参数2：图例名称
                            LineDataSet set1= new LineDataSet(doMath(info), "AD"+i+1);
                            set1.setColor(Color.parseColor(list.get(i).getColorStr()));
                            set1.setCircleColor(Color.RED);
                            set1.setCircleColorHole(Color.RED);
                            set1.setLineWidth(2f);//设置线宽
                            set1.setCircleRadius(3f);//设置焦点圆心的大小
                            set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
                            //                            set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
                            set1.setHighlightEnabled(true);//是否禁用点击高亮线
                            set1.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
                            set1.setValueTextSize(9f);//设置显示值的文字大小
                            set1.setDrawValues(false);//设置是否显示数据文字
                            set1.setDrawFilled(false);//设置禁用范围背景填充
                            set1.setDrawCircles(false);//设置是否显示点
                            set1.setCubicIntensity(2f);//折线弯曲程度
                            dataSets.add(set1);
                        }
                    }


                    //获取此图表的x轴
                    final XAxis xAxis = chart.getXAxis();
                    xAxis.setEnabled(true);//设置轴启用或禁用 如果禁用以下的设置全部不生效
                    xAxis.setDrawAxisLine(true);//是否绘制轴线
                    xAxis.setDrawGridLines(false);//是否显示网格线(x轴上每个点对应的线)
                    xAxis.setDrawLabels(true);//绘制标签  指x轴上的对应数值
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置

                    xAxis.setAvoidFirstLastClipping(true);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
                    xAxis.setLabelRotationAngle(3f);//设置x轴标签的旋转角度
                    xAxis.setGranularity(xMid);//设置X轴坐标之间的最小间隔
                    xAxis.setAxisMaximum(xMax);
                    xAxis.setAxisMinimum(xmin);
                    xAxis.setLabelCount(count,false);

                    //Y轴默认显示左右两个轴线
                    //获取右边的轴线
                    YAxis rightAxis = chart.getAxisRight();
                    //设置图表右边的y轴禁用
                    rightAxis.setEnabled(false);
                    //获取左边的轴线
                    final YAxis leftAxis = chart.getAxisLeft();
                    leftAxis.setEnabled(true);
                    //设置网格线为虚线效果
                    leftAxis.enableGridDashedLine(10f, 10f, 0f);
                    leftAxis.setGridColor(R.color.color3);
                    leftAxis.setDrawGridLines(true);//是否显示网格线
                    leftAxis.setDrawLabels(true);
                    leftAxis.setAxisMinimum(minWei);
                    leftAxis.setAxisMaximum(maxWei);

                    //是否绘制0所在的网格线
                    leftAxis.setDrawZeroLine(false);
                    leftAxis.setTextColor(R.color.back2);
                    leftAxis.setGranularity(midWei);//设置Y轴坐标之间的最小间隔
                    leftAxis.setLabelCount(15, true);
                    //                                                leftAxis.setXOffset(1);//设置Y轴偏移量
//                                            leftAxis.setValueFormatter(new IAxisValueFormatter() {
//                                                @Override
//                                                public String getFormattedValue(float value, AxisBase axis) {
//                                                    return (float) value + " 吨 ";
//                                                }
//                                            });

                    chart.setTouchEnabled(true); // 设置是否可以触摸
                    chart.setDragEnabled(true);// 是否可以拖拽
                    chart.setScaleEnabled(true);// 是否可以缩放 x和y轴, 默认是true
                    chart.setScaleXEnabled(true); //是否可以缩放 仅x轴
                    chart.setScaleYEnabled(false); //是否可以缩放 仅y轴
                    chart.setPinchZoom(false);  //设置x轴和y轴能否同时缩放。默认是否
                    chart.setDoubleTapToZoomEnabled(true);//设置是否可以通过双击屏幕放大图表。默认是true
                    chart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
                    chart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
                    chart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。
                    chart.setFitsSystemWindows(true);

                    Legend l = chart.getLegend();//图例
                    l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);//设置图例的位置
                    l.setTextSize(10f);//设置文字大小
                    l.setForm(Legend.LegendForm.CIRCLE);//正方形，圆形或线
                    l.setFormSize(10f); // 设置Form的大小
                    l.setWordWrapEnabled(true);//是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
                    l.setFormLineWidth(10f);//设置Form的宽度
                    l.setEnabled(false);


                    final LineData lineData = new LineData(dataSets);
                    chart.setData(lineData);
                    chart.setVisibleXRangeMaximum(12000);//设置在曲线图中显示的最大数量
                    chart.invalidate();

                    chart.setVisibility(View.VISIBLE);
                    tv_y.setVisibility(View.VISIBLE);
                    tv_no_weightData.setVisibility(View.GONE);

                    //                    //点击图表时，执行语句
                    //                    chart.setOnClickListener(new View.OnClickListener() {
                    //                        @Override
                    //                        public void onClick(View v) {
                    //                            //LogUtil.e("点击onClick: " + v.getScaleX());
                    //                            if (v.getScaleX() > 35) {
                    //                                set1.setDrawCircles(true);//设置是否显示点
                    //                            } else {
                    //                                set1.setDrawCircles(false);//设置是否显示点
                    //                            }
                    //                        }
                    //                    });
                    //当值被选中的时候，执行语句
                    chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, Highlight h) {


                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (getActivity() == null || getActivity().isFinishing()) {
                return;
            }
            switch (msg.what) {
                case 1:
                    List<Curvenfo> nlist= (List<Curvenfo>)acache.getAsObject("list_history");
                    if(nlist !=null && nlist.size() >0){
                        showResult(nlist,ishistory);
                        chart.setVisibility(View.VISIBLE);
                        tv_y.setVisibility(View.VISIBLE);
                        tv_no_weightData.setVisibility(View.GONE);
                    }else{
                        showTips("历史数据为空");
                        tv_y.setVisibility(View.GONE);
                        chart.setVisibility(View.GONE);
                        tv_no_weightData.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    List<Curvenfo> nowlist= (List<Curvenfo>)acache.getAsObject("list_now");
                    if(nowlist !=null && nowlist.size() >0){
                        showResult(nowlist,ishistory);
                        chart.setVisibility(View.VISIBLE);
                        tv_y.setVisibility(View.VISIBLE);
                        tv_no_weightData.setVisibility(View.GONE);
                    }else{
                        showTips("实时数据为空");
                        tv_y.setVisibility(View.GONE);
                        chart.setVisibility(View.GONE);
                        tv_no_weightData.setVisibility(View.VISIBLE);
                    }

                    break;
                case 3://连接
                    isConnected=true;
                    List<Curvenfo>list =new ArrayList<>();
                    acache.put("list_history",(Serializable) list);
                    acache.put("list_now",(Serializable) list);

                    tv_y.setVisibility(View.GONE);
                    chart.setVisibility(View.GONE);
                    tv_no_weightData.setVisibility(View.VISIBLE);

                    initList();
                    
                    doEmpty();
                    break;
                case 4://通道选择
                    if(ishistory){
                        List<Curvenfo> hislist= (List<Curvenfo>)acache.getAsObject("list_history");
                        if(hislist !=null && hislist.size() ==16){
                            doShow(hislist);
                            chart.setVisibility(View.VISIBLE);
                            tv_y.setVisibility(View.VISIBLE);
                            tv_no_weightData.setVisibility(View.GONE);
                        }else{
                            DLog.e("CurveFragment","历史数据有问题");
                        }
                    }else{
                        if(!isConnected){
//                            showTips("蓝牙已断开");
                            DLog.e("CurveFragment","实时数据不支持断开蓝牙后通道选择");
                        }
                    }
                    break;
                case 5://断开
                    isConnected=false;
                    break;
            }
        }
    };
    //重新显示通道
    private void doShow(List<Curvenfo> hislist) {
        List<Curvenfo> newlist=new ArrayList<>();
        if(channelModel.bChannel1){
            newlist.add(hislist.get(0));
        }
        if(channelModel.bChannel2){
            newlist.add(hislist.get(1));
        }
        if(channelModel.bChannel3){
            newlist.add(hislist.get(2));
        }
        if(channelModel.bChannel4){
            newlist.add(hislist.get(3));
        }
        if(channelModel.bChannel5){
            newlist.add(hislist.get(4));
        }
        if(channelModel.bChannel6){
            newlist.add(hislist.get(5));
        }
        if(channelModel.bChannel7){
            newlist.add(hislist.get(6));
        }
        if(channelModel.bChannel8){
            newlist.add(hislist.get(7));
        }
        if(channelModel.bChannel9){
            newlist.add(hislist.get(8));
        }
        if(channelModel.bChannel10){
            newlist.add(hislist.get(9));
        }
        if(channelModel.bChannel11){
            newlist.add(hislist.get(10));
        }
        if(channelModel.bChannel12){
            newlist.add(hislist.get(11));
        }
        if(channelModel.bChannel13){
            newlist.add(hislist.get(12));
        }
        if(channelModel.bChannel14){
            newlist.add(hislist.get(13));
        }
        if(channelModel.bChannel15){
            newlist.add(hislist.get(14));
        }
        if(channelModel.bChannel16){
            newlist.add(hislist.get(15));
        }
        showResult(newlist,ishistory);
    }

    private void doEmpty() {
        hlist1.clear();hlist2.clear();hlist3.clear();hlist4.clear();
        hlist5.clear();hlist6.clear();hlist7.clear();hlist8.clear();
        hlist9.clear();hlist10.clear();hlist11.clear();hlist12.clear();
        hlist13.clear();hlist14.clear();hlist15.clear();hlist16.clear();

        ylist1.clear();ylist2.clear();ylist3.clear();ylist4.clear();
        ylist5.clear();ylist6.clear();ylist7.clear();ylist8.clear();
        ylist9.clear();ylist10.clear();ylist11.clear();ylist12.clear();
        ylist13.clear();ylist14.clear();ylist15.clear();ylist16.clear();
    }

    @Subscribe (threadMode  = ThreadMode.MAIN)  //必须使用EventBus的订阅注解
    public void onEvent(HDModelData uMode){
        hdData = uMode;
        if(!ishistory){
            doCarche(hdData,channelModel,ishistory);
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
                if(action.equals(BleConstant.ACTION_BLE_HANDLER_DATA)) {//接收数据
                    Bundle bundle = intent.getExtras();
                    hdData = (HDModelData) bundle.getSerializable("ModelData");
                    if(!ishistory){
                        doCarche(hdData,channelModel,ishistory);
                    }

                }else if(action.equals(BleConstant.ACTION_CHANNEL_CHANGE)){//通道选择
                    channelModel = (HDKRModel)acache.getAsObject("channel_model");//保存多少通道
                    currentList = (List<String>) acache.getAsObject("et_number");//AD阀值
                    handler.sendEmptyMessage(4);
                }else if(action.equals(BleConstant.ACTION_BLE_CONNECTED)){
                    handler.sendEmptyMessage(3);
                }else if(action.equals(BleConstant.ACTION_BLE_DISCONNECT)){//断开
                    handler.sendEmptyMessage(5);
                }else if(action.equals(BleConstant.ACTION_BLE_CONNECTION_EXCEPTION)){//异常断开
                    handler.sendEmptyMessage(5);
                }

            }

        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTION_BLE_HANDLER_DATA);
        filter.addAction(BleConstant.ACTION_CHANNEL_CHANGE);
        filter.addAction(BleConstant.ACTION_BLE_CONNECTED);
        filter.addAction(BleConstant.ACTION_BLE_DISCONNECT);
        filter.addAction(BleConstant.ACTION_BLE_CONNECTION_EXCEPTION);
        getActivity().registerReceiver(receiver, filter);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
        if (myMediaPlayer != null ) {
            myMediaPlayer.stop();
            myMediaPlayer.release();
            myMediaPlayer = null;
        }

    }
    //fragment刷新数据
    @Override
    public void onBaseRefresh() {

    }
    public void  showTips(String tip){
        if(!CurveFragment.this.isHidden()){
            ToastUtil.getInstance().showCenterMessage(mactivity,tip);
        }
    }

    //缓存数据
    private void doCarche(HDModelData data,HDKRModel model,boolean ishistory){
        if(ishistory){ //历史数据
            List<Curvenfo> nlist= (List<Curvenfo>)acache.getAsObject("list_history");
            if(nlist !=null && nlist.size() >0){
                showResult(nlist,ishistory);
            }else{
                showTips("历史数据为空");
            }
            return;
        }

        List<Curvenfo>mlist=new ArrayList<>();
        List<Curvenfo>ylist=new ArrayList<>();
        if(hlist1.size()>=60){
            hlist1.remove(0);
        }
        if(ylist1.size() >=660){
            ylist1.remove(0);
        }
        float a1= data.ad1/100f;
        hlist1.add(a1);
        ylist1.add(a1);
        Curvenfo y1=doCurvenfo(ylist1,"#0071bc",1,model.bChannel1);

        if(hlist2.size()>=60){
            hlist2.remove(0);
        }
        if(ylist2.size() >=660){
            ylist2.remove(0);
        }
        float a2= data.ad2/100f;
        hlist2.add(a2);
        ylist2.add(a2);
        Curvenfo y2=doCurvenfo(ylist2,"#d85218",2,model.bChannel2);

        if(hlist3.size()>=60){
            hlist3.remove(0);
        }
        if(ylist3.size() >=660){
            ylist3.remove(0);
        }
        float a3= data.ad3/100f;
        hlist3.add(a3);
        ylist3.add(a3);
        Curvenfo y3=doCurvenfo(ylist3,"#ecb01f",3,model.bChannel3);

        if(hlist4.size()>=60){
            hlist4.remove(0);
        }
        if(ylist4.size() >=660){
            ylist4.remove(0);
        }
        float a4= data.ad4/100f;
        hlist4.add(a4);
        ylist4.add(a4);
        Curvenfo y4=doCurvenfo(ylist4,"#7d2e8d",4,model.bChannel4);

        if(hlist5.size()>=60){
            hlist5.remove(0);
        }
        if(ylist5.size() >=660){
            ylist5.remove(0);
        }
        float a5= data.ad5/100f;
        hlist5.add(a5);
        ylist5.add(a5);
        Curvenfo y5=doCurvenfo(ylist5,"#76ab2f",5,model.bChannel5);


        if(hlist6.size()>=60){
            hlist6.remove(0);
        }
        if(ylist6.size() >=660){
            ylist6.remove(0);
        }
        float a6= data.ad6/100f;
        hlist6.add(a6);
        ylist6.add(a6);
        Curvenfo y6=doCurvenfo(ylist6,"#4cbded",6,model.bChannel6);

        if(hlist7.size()>=60){
            hlist7.remove(0);
        }
        if(ylist7.size() >=660){
            ylist7.remove(0);
        }
        float a7= data.ad7/100f;
        hlist7.add(a7);
        ylist7.add(a7);
        Curvenfo y7=doCurvenfo(ylist7,"#a1132e",7,model.bChannel7);


        if(hlist8.size()>=60){
            hlist8.remove(0);
        }
        if(ylist8.size() >=660){
            ylist8.remove(0);
        }
        float a8= data.ad8/100f;
        hlist8.add(a8);
        ylist8.add(a8);
        Curvenfo y8=doCurvenfo(ylist8,"#ff99c8",8,model.bChannel8);

        if(hlist9.size()>=60){
            hlist9.remove(0);
        }
        if(ylist9.size() >=660){
            ylist9.remove(0);
        }
        float a9= data.ad9/100f;
        hlist9.add(a9);
        ylist9.add(a9);
        Curvenfo y9=doCurvenfo(ylist9,"#007f00",9,model.bChannel9);

        if(hlist10.size()>=60){
            hlist10.remove(0);
        }
        if(ylist10.size() >=660){
            ylist10.remove(0);
        }
        float a10= data.ad10/100f;
        hlist10.add(a10);
        ylist10.add(a10);
        Curvenfo y10=doCurvenfo(ylist10,"#dab3ff",10,model.bChannel10);

        if(hlist11.size()>=60){
            hlist11.remove(0);
        }
        if(ylist11.size() >=660){
            ylist11.remove(0);
        }
        float a11= data.ad11/100f;
        hlist11.add(a11);
        ylist11.add(a11);
        Curvenfo y11=doCurvenfo(ylist11,"#0071bc",11,model.bChannel11);

        if(hlist12.size()>=60){
            hlist12.remove(0);
        }
        if(ylist12.size() >=660){
            ylist12.remove(0);
        }
        float a12= data.ad12/100f;
        hlist12.add(a12);
        ylist12.add(a12);
        Curvenfo y12=doCurvenfo(ylist12,"#d85218",12,model.bChannel12);

        if(hlist13.size()>=60){
            hlist13.remove(0);
        }
        if(ylist13.size() >=660){
            ylist13.remove(0);
        }
        float a13= data.ad13/100f;
        hlist13.add(a13);
        ylist13.add(a13);
        Curvenfo y13=doCurvenfo(ylist13,"#ecb01f",13,model.bChannel13);


        if(hlist14.size()>=60){
            hlist14.remove(0);
        }
        if(ylist14.size() >=660){
            ylist14.remove(0);
        }
        float a14= data.ad14/100f;
        hlist14.add(a14);
        ylist14.add(a14);
        Curvenfo y14=doCurvenfo(ylist14,"#7d2e8d",14,model.bChannel14);

        if(hlist15.size()>=60){
            hlist15.remove(0);
        }
        if(ylist15.size() >=660){
            ylist15.remove(0);
        }
        float a15= data.ad15/100f;
        hlist15.add(a15);
        ylist15.add(a15);
        Curvenfo y15=doCurvenfo(ylist15,"#76ab2f",15,model.bChannel15);

        if(hlist16.size()>=60){
            hlist16.remove(0);
        }
        if(ylist16.size() >=660){
            ylist16.remove(0);
        }
        float a16= data.ad16/100f;
        hlist16.add(a16);
        ylist16.add(a16);
        Curvenfo y16=doCurvenfo(ylist16,"#4cbded",16,model.bChannel16);

        if(model.bChannel1){
            doCompare(1,a1,hlist1,true);
            Curvenfo c1=doCurvenfo(hlist1,"#0071bc",1,true);
            mlist.add(c1);
        }
        if(model.bChannel2){
            Curvenfo c2=doCurvenfo(hlist2,"#d85218",2,true);
            doCompare(2,a2,hlist2,true);
            mlist.add(c2);
        }
        if(model.bChannel3){
            Curvenfo c3=doCurvenfo(hlist3,"#ecb01f",3,true);
            doCompare(3,a3,hlist3,true);
            mlist.add(c3);
        }
        if(model.bChannel4){
            Curvenfo c4=doCurvenfo(hlist4,"#7d2e8d",4,true);
            doCompare(4,a4,hlist4,true);
            mlist.add(c4);
        }

        if(model.bChannel5){
            Curvenfo c5=doCurvenfo(hlist5,"#76ab2f",5,true);
            doCompare(5,a5,hlist5,true);
            mlist.add(c5);
        }

        if(model.bChannel6){
            Curvenfo c6=doCurvenfo(hlist6,"#4cbded",6,true);
            doCompare(6,a6,hlist6,true);
            mlist.add(c6);
        }

        if(model.bChannel7){
            Curvenfo c7=doCurvenfo(hlist7,"#a1132e",7,true);
            doCompare(7,a7,hlist7,true);
            mlist.add(c7);
        }
        if(model.bChannel8){
            Curvenfo c8=doCurvenfo(hlist8,"#ff99c8",8,true);
            doCompare(8,a8,hlist8,true);
            mlist.add(c8);
        }
        if(model.bChannel9){
            Curvenfo c9=doCurvenfo(hlist9,"#007f00",9,true);
            doCompare(9,a9,hlist9,true);
            mlist.add(c9);
        }
        if(model.bChannel10){
            Curvenfo c10=doCurvenfo(hlist10,"#dab3ff",10,true);
            doCompare(10,a10,hlist10,true);
            mlist.add(c10);
        }
        if(model.bChannel11){
            Curvenfo c11=doCurvenfo(hlist11,"#0071bc",11,true);
            doCompare(11,a11,hlist11,true);
            mlist.add(c11);
        }
        if(model.bChannel12){
            Curvenfo c12=doCurvenfo(hlist12,"#d85218",12,true);
            doCompare(12,a12,hlist12,true);
            mlist.add(c12);
        }
        if(model.bChannel13){
            Curvenfo c13=doCurvenfo(hlist13,"#ecb01f",13,true);
            doCompare(13,a13,hlist13,true);
            mlist.add(c13);
        }
        if(model.bChannel14){
            Curvenfo c14=doCurvenfo(hlist14,"#7d2e8d",14,true);
            doCompare(14,a14,hlist14,true);
            mlist.add(c14);
        }
        if(model.bChannel15){
            Curvenfo c15=doCurvenfo(hlist15,"#76ab2f",15,true);
            doCompare(15,a15,hlist15,true);
            mlist.add(c15);
        }
        if(model.bChannel16){
            Curvenfo c16=doCurvenfo(hlist16,"#4cbded",16,true);
            doCompare(16,a16,hlist16,true);
            mlist.add(c16);
        }
        ylist.add(y1);
        ylist.add(y2);
        ylist.add(y3);
        ylist.add(y4);
        ylist.add(y5);
        ylist.add(y6);
        ylist.add(y7);
        ylist.add(y8);
        ylist.add(y9);
        ylist.add(y10);
        ylist.add(y11);
        ylist.add(y12);
        ylist.add(y13);
        ylist.add(y14);
        ylist.add(y15);
        ylist.add(y16);


        showResult(mlist,ishistory);
        acache.put("list_history",(Serializable) ylist);
        acache.put("list_now",(Serializable) mlist);

    }

    private Curvenfo doCurvenfo(List<Float>list,String colorStr,int number,boolean isShow){
        Curvenfo c=new Curvenfo();
        c.setHlist(list);
        c.setColorStr(colorStr);
        c.setId(number);
        c.setShow(isShow);
        return c;
    }

    private ArrayList<Entry>  doMath( Curvenfo info){
        if(info ==null || info.getHlist() ==null || info.getHlist().size() <=0){
            showTips("数据为空");
            return new ArrayList<>();
        }
        List<Float> nlist= info.getHlist();
        final ArrayList<Entry> value = new ArrayList<Entry>();
        for (int i = 0; i < nlist.size() ; i++) {
            float wei=nlist.get(i);
            //获取重量
            value.add(new Entry(i, wei));

        }
        return value;
    }
    //取最大值
    private float getMax(final  List<Curvenfo> mlist ){
        if(mlist ==null  || mlist.size() <=0){
            showTips("数据为空");
            return 0f;
        }
        ArrayList<Float> list=new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");
        for(int i=0;i<mlist.size();i++){
            float maxW = Collections.max(mlist.get(i).getHlist());//取最大值
            maxW = Float.parseFloat(df.format(Math.ceil(maxW)));//向上取整
            list.add(maxW);
        }
        float max=Collections.max(list);
        return  max;
    }
    //取最小值
    private float  getMin(final List<Curvenfo> mlist){
        if(mlist ==null  || mlist.size() <=0){
            showTips("数据为空");
            return 0f;
        }
        ArrayList<Float> list=new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");
        for(int j=0;j<mlist.size();j++){
            float minW = Collections.min(mlist.get(j).getHlist());//取最小值
            minW = Float.parseFloat(df.format(Math.ceil(minW)));//向上取整
            list.add(minW);
        }
        float min=Collections.min(list);
        return  min;
    }
    //判断某一条通道 当前数据和 某个时间点数据 比较 是否超阀值
    private void doCompare(int number,float ad,List<Float> hlist,boolean isShow){
        if(currentList ==null ||currentList.size() != 18){
            DLog.e("CurveFragment","通道阀值设置错误");
            return;
        }
        if(isShow){
            String adstr=currentList.get(number-1);
            String position=currentList.get(16);
            int size=hlist.size();
            int ps=Integer.parseInt(position);
            if(size >=ps){//保存的数据大余要查看的位置
                float hf=hlist.get(size-ps);
                double cha=(double)(ad-hf);
                double last=Math.abs(cha);
                double set=Double.parseDouble(adstr)/100f;
                if(last > set ){ //差值大于阀值 文字提示 加语音提示
                    showTips("通道"+number+"数据异常");
                    if(!CurveFragment.this.isHidden()){
                        String isv=currentList.get(17);
                        if("0".equals(isv)){
                            doPlay("aderror.mp3");
                        }
                    }


                }

            }else{
//                DLog.e("CurveFragment","list数据保存长度未达到通道阀值比较位置");
            }
        }

    }

    public void doPlay(String filename) {
        if(myMediaPlayer.isPlaying()){
            return;
        }
        if (myMediaPlayer != null) {
            /* 把音乐音量强制设置为最大音量*/
            AudioManager mAudioManager = (AudioManager) mactivity.getSystemService(Context.AUDIO_SERVICE);
            int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量
            //					int maxVolume = mAudioManager
            //							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大声音
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0); // 设置为最大声音，可通过SeekBar更改音量大小

            try {
                AssetFileDescriptor fileDescriptor = mactivity.getAssets().openFd(filename);

                myMediaPlayer.reset();
                myMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),

                        fileDescriptor.getStartOffset(),

                        fileDescriptor.getLength());

                myMediaPlayer.prepare();
                myMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden){
            if (myMediaPlayer != null ) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                myMediaPlayer = null;
            }
        }else{
            if(myMediaPlayer ==null){
                myMediaPlayer = new MediaPlayer();
            }
        }
    }


}
