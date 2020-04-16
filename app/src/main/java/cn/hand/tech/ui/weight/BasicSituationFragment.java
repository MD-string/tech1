package cn.hand.tech.ui.weight;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDModelData;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.ui.home.BaseFragment;

/**
 *  基本信息
 * @author hxz
 */

public class BasicSituationFragment extends BaseFragment implements OnClickListener {

	private BroadcastReceiver receiver;
	Context mContext;

	private TextView tv_gps_name;
	private TextView tv_sha_che,tv_measure_sta,tv_speed,tv_xiang_jiao,tv_jing,tv_wei,tv_good_have,tv_v,tv_gps_data,tv_weight_data;
	@SuppressLint("HandlerLeak")
	Handler mhandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (getActivity() == null || getActivity().isFinishing()) {
				return;
			}
			switch (msg.what) {
			case 1:
				break;

			default:
				break;
			}
		}
	};


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=getActivity();
		 View view = inflater.inflate(R.layout.frg_basic_sta, container, false);
		EventBus.getDefault().register(this);
		initView(view);
		registerBrodcat();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initView(View view) {
		tv_sha_che=(TextView)view.findViewById(R.id.tv_sha_che); //手刹状态
		tv_measure_sta=(TextView)view.findViewById(R.id.tv_measure_sta);//测量状态
		tv_speed=(TextView)view.findViewById(R.id.tv_speed);//车速
		tv_xiang_jiao=(TextView)view.findViewById(R.id.tv_xiang_jiao);//向角
		tv_jing=(TextView)view.findViewById(R.id.tv_jing);//经度
		tv_wei=(TextView)view.findViewById(R.id.tv_wei);//纬度
		tv_good_have=(TextView)view.findViewById(R.id.tv_good_have);//是否有货物
		tv_v=(TextView)view.findViewById(R.id.tv_v);//电压
		tv_gps_name=(TextView)view.findViewById(R.id.tv_gps_name);
		tv_gps_data=(TextView)view.findViewById(R.id.tv_gps_data);//gps 时间
		tv_weight_data=(TextView)view.findViewById(R.id.tv_weight_data);//重量时间
	}


	@Override
	public void onClick(View v) {
	}

    @Subscribe(threadMode  = ThreadMode.MAIN)  //必须使用EventBus的订阅注解
    public void onEvent(HDModelData uMode){
        HDModelData data=uMode;
        dohandler(data);
    }

	/**
	 * 注册广播
	 */
	private void registerBrodcat() {
		receiver=new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action=intent.getAction();
				if(action.equals(BleConstant.ACTION_BLE_HANDLER_DATA)){//接收数据
					Bundle bundle=intent.getExtras();
					HDModelData data=(HDModelData)bundle.getSerializable("ModelData");
					dohandler(data);
				}

			}

		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(BleConstant.ACTION_BLE_HANDLER_DATA);
		getActivity().registerReceiver(receiver, filter);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			getActivity().unregisterReceiver(receiver);
			receiver = null;
		}

	}
	//fragment刷新数据
	@Override
	public void onBaseRefresh() {

	}

	private String dataConvert(long time) {
		long lcc_time = Long.valueOf(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date(lcc_time * 1000L));
	}

	public void  dohandler(HDModelData data){
		if(data ==null){
			return;
		}
		if (data.nBreak > 0) {
			//未拉手刹
			tv_sha_che.setText("未拉");
		} else {
			tv_sha_che.setText("已拉");
			//已拉手刹
		}
		if (data.nStable > 0) {
			//测量状态:稳定
			tv_measure_sta.setText("稳定");
		} else {
			//测量状态:不稳定
			tv_measure_sta.setText("不稳定");
		}

		long weiTime= Long.valueOf(data.weightTime)*1000L; //new 3/21
		long gpst= Long.valueOf(data.gpsTime)*1000L;
		long cha=Math.abs(weiTime-gpst); //大于30分钟 GPS时间标红  GPS 异常

		tv_weight_data.setText(dataConvert(data.weightTime));
		if(cha > 30*60*1000){
			tv_gps_name.setText("GPS异常");
			tv_gps_name.setTextColor(mContext.getResources().getColor(R.color.red));
			tv_gps_data.setTextColor(mContext.getResources().getColor(R.color.red));
		}else{
			tv_gps_name.setText("GPS时间");
			tv_gps_name.setTextColor(mContext.getResources().getColor(R.color.hand_txt));
			tv_gps_data.setTextColor(mContext.getResources().getColor(R.color.hand_blue));
		}
		tv_gps_data.setText(dataConvert(data.gpsTime));
		tv_xiang_jiao.setText("向角:" + data.nAzimuth);
		tv_speed.setText("车速度:" + data.nSpeed);
		tv_jing.setText(llDegree(data.nLongtitude, "经度"));
		tv_wei.setText(llDegree(data.nLatitude, "纬度"));
		tv_v.setText(formatValue(data.nVoltage, "电压", "0.0"));
		switch (data.nLoadStatus) {
			case 0:
				tv_good_have.setText("无货物");
				break;
			case 1:
				tv_good_have.setText("有货物");
				break;
			case 2:
				tv_good_have.setText("装货");
				break;
			case 3:
				tv_good_have.setText("卸货");
				break;
			default:
				break;
		}
	}

	private String llDegree(float x, String name) {
		float deg = (float) ((int) (x / 100));
		float y = deg + (x - deg * 100) / 60;
		return name + ":" + y;

	}
	private String formatValue(float x, String showName, String sFormat) {
		java.text.DecimalFormat myformat = new java.text.DecimalFormat(sFormat);
		String str = myformat.format(x);
		return showName + ":" + str;
	}

}
