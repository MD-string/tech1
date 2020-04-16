package cn.hand.tech.ui.weight;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDModelData;
import cn.hand.tech.bean.WeightDataBean;
import cn.hand.tech.ble.bean.BleDevice;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.Constr;
import cn.hand.tech.dao.WeightDao;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.home.BaseFragment;
import cn.hand.tech.ui.home.MainFragmentActivity;
import cn.hand.tech.ui.setting.AutoCheckAct;
import cn.hand.tech.ui.setting.PassSettingActNew;
import cn.hand.tech.ui.setting.RepairAct;
import cn.hand.tech.ui.setting.UpBinAct;
import cn.hand.tech.ui.setting.bean.ClearZeroModel;
import cn.hand.tech.ui.weight.adapter.BleDeviceAdapter;
import cn.hand.tech.ui.weight.adapter.WFragmentAdapter;
import cn.hand.tech.ui.weight.bean.CarInfo;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.ui.weight.presenter.EventListPresenter;
import cn.hand.tech.ui.weight.presenter.SaveWeightDataTask;
import cn.hand.tech.utils.Aes;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.DataUtil;
import cn.hand.tech.utils.EditTextUtil;
import cn.hand.tech.utils.MyPopupWindowUtil;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.NavigationConfig;

/**
 *  发现 ----问答
 * @author hxz(暂时不做)
 */

@NavigationConfig(isShow = false, titleId = R.string.main_title, showLeft = false)
public class WeightFragment extends BaseFragment implements OnClickListener,IEventView {

	private static final int REQUEST_COARSE_LOCATION = 1;
	private BroadcastReceiver receiver;
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private static final String TAG = "WeightFragment";
	private TextView title_ble;
	private LinearLayout ll_zero,ll_save;
	private TextView tv_weight,rl_btn,tv_car_id,tv_ble_id,tv_count;
	private LinearLayout ll_tab_1,ll_tab_2;
	private TextView tv_tab_1,tv_tab_2;
	private TextView mTabLineIv;
	private ViewPager mPageVp;
	private int screenWidth,leftStartMargin;
	private int currentIndex;
	List<double[]> dlist=new ArrayList<double[]>();
	private MainFragmentActivity mactivity;
	//tab数量
	private int tabCount = 2;
	private double clear_flag;
	private WFragmentAdapter mFragmentAdapter;
	private TextView tv_jnit;
	private int REQUEST_WRITE_EXTERNAL_STORAGE=2;
	private ACache acache;
	private BleDeviceAdapter bleDeviceAdapter;
	private List<BleDevice> listDevices = new ArrayList<BleDevice>();
	private Vibrator vibrator;
	List<Double> ulist=new ArrayList<Double>();
	private List<CarInfo> carList =new ArrayList<>();
	private boolean isConnected =false ;
	private ProgressDialog progressDialog;
	private LinearLayout dialog_truckN_input;
	private InputView mInputView;
	private TextView tv_okTruckN,tv_scanner_done;
	private PopupKeyboard mPopupKeyboard;
	private boolean isInitiativeClick = false;//是否是主动点击
	private boolean isInitiativeDisconnect = false;//是否主动断开
	private boolean isEditInfo = false;
	private ProgressBar progress_bar;
	private BleDevice noInfoDev;
	private int clearNum;
	private boolean isClear = false;
	private HDModelData hdData;//当前接收到的蓝牙通知数据
	private AlertDialog tempDialog;
	private BluetoothDevice mBluetoothDevice;//当前连接设备
	private String mCurrentBleAddress;
	private String weightstr="0.00";
	private int rssi;//信号强度
	private int testTime;
	private double[] s2,s3;
	private double weightPredict;
	private EditText dEditText;
	private String weStr,truckNum = "汉A88888";
	private String isDisConnect="0";
	private String isConnect="2";
	private EditText et_input;
	private TextView tv_other;
	private boolean isother=false;
	private String etNumber;//手动输入车牌
	private MediaPlayer myMediaPlayer;
	private boolean isPlay =false; //防止多次提示清零成功
	private static final String PASSWORD_STRING = "6a9fd0d1a950420b812f27e970afcd8f"; // static final 静态常量
	private AlertDialog adddialog;
	private String carNumber;
	private boolean islogin=false;
	private String mdevId;//当前设备id
	private LinearLayout ll_repair,ll_online;
    private EventListPresenter mpresenter;
	private LinearLayout ll_choose_pass;
	private LinearLayout ll_auto_check;
	private LinearLayout ll_bin_upper;
	private Timer timer;
	private AlertDialog devtempDialog;
	private TimerTask task;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mactivity=	(MainFragmentActivity)getActivity();
		final View view = inflater.inflate(R.layout.frg_home_discover, container, false);
		acache= ACache.get(mactivity,TAG);
		EventBus.getDefault().register(this);
		myMediaPlayer = new MediaPlayer();
        mpresenter=new EventListPresenter(mactivity,this);
		initView(view);
		initTabLineWidth();
		init();
		registerBrodcat();
		List<BleDevice> emlist=new ArrayList<>();
		acache.put("dev_list",(Serializable)emlist);
		acache.put("is_connect",isDisConnect);//是否连接
		initClearZero();
		initView();
		return view;
	}

	private void initClearZero() {
		ClearZeroModel model=new ClearZeroModel();
		model.setCtime("2");
		model.setChannel1("3");model.setChannel2("3");
		model.setChannel3("2");model.setChannel4("2");
		model.setChannel5("3");model.setChannel6("3");
		model.setChannel7("3");model.setChannel8("2");
		model.setChannel9("2");model.setChannel10("3");
		model.setChannel11("3");model.setChannel12("3");
		model.setChannel13("3");model.setChannel14("3");
		model.setChannel15("0");model.setChannel16("0");
		acache.put("clear_zero_model",(Serializable)model);
	}

	private void initView(View view) {
		title_ble=(TextView)view.findViewById(R.id.title_ble);//蓝牙
		ll_zero=(LinearLayout)view.findViewById(R.id.ll_zero);//清零
		ll_save=(LinearLayout)view.findViewById(R.id.ll_save);//保存
		ll_repair=(LinearLayout)view.findViewById(R.id.ll_repair);//维修
		ll_online=(LinearLayout)view.findViewById(R.id.ll_online);//检测在线
		ll_choose_pass=(LinearLayout)view.findViewById(R.id.ll_choose_pass);//通道选择

		ll_auto_check=(LinearLayout)view.findViewById(R.id.ll_auto_check);//自动检测

		ll_bin_upper=(LinearLayout)view.findViewById(R.id.ll_bin_upper);//固件升级

		ll_repair.setOnClickListener(this);
		ll_auto_check.setOnClickListener(this);
		ll_bin_upper.setOnClickListener(this);
		ll_online.setOnClickListener(this);
		title_ble.setOnClickListener(this);
		ll_choose_pass.setOnClickListener(this);
		ll_zero.setOnClickListener(this);
		ll_save.setOnClickListener(this);

		tv_weight=(TextView)view.findViewById(R.id.tv_weight);//重量
		tv_jnit=(TextView)view.findViewById(R.id.tv_jnit);//单位
		String str=acache.getAsString("weight_unit");
		if(Tools.isEmpty(str)){
			tv_jnit.setText("公斤");
		}else{
			tv_jnit.setText(str);
		}

		tv_car_id=(TextView)view.findViewById(R.id.tv_car_id);//车牌
		tv_ble_id=(TextView)view.findViewById(R.id.tv_ble_id);//蓝牙mac
		tv_count=(TextView)view.findViewById(R.id.tv_count);//计数器


		ll_tab_1=(LinearLayout)view.findViewById(R.id.ll_tab_1);//基本信息
		tv_tab_1=(TextView)view.findViewById(R.id.tv_tab_1);
		ll_tab_2=(LinearLayout)view.findViewById(R.id.ll_tab_2);//通道参数
		tv_tab_2=(TextView)view.findViewById(R.id.tv_tab_2);
		mTabLineIv=(TextView)view.findViewById(R.id.id_tab_line_iv);//引导线

		mPageVp=(ViewPager)view.findViewById(R.id.id_page_vp);
		ll_tab_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(0,true);
			}
		});
		ll_tab_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(1,true);
			}
		});
		vibrator = (Vibrator)(mactivity. getSystemService(Context.VIBRATOR_SERVICE));//振动

		dialog_truckN_input = (LinearLayout)view.findViewById(R.id.dialog_truckN_input);//车辆信息键盘
		dialog_truckN_input.setVisibility(View.GONE);
		mInputView = (InputView) view.findViewById(R.id.input_view);
		et_input=(EditText)view.findViewById(R.id.et_input);
		mInputView.setVisibility(View.VISIBLE);
		et_input.setVisibility(View.GONE);
		tv_other=(TextView)view.findViewById(R.id.tv_other);
		tv_other.setText("其他");
		tv_okTruckN = (TextView)view.findViewById(R.id.tv_okTruckN);
		tv_okTruckN.setOnClickListener(this);
		tv_other.setOnClickListener(this);

		mPopupKeyboard = new PopupKeyboard(mactivity);
		// 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
		mPopupKeyboard.attach(mInputView, getActivity());
	}

	private void init() {
		BasicSituationFragment bsFragment=new BasicSituationFragment();
		ChannelParameterFragment cpFragment=new ChannelParameterFragment();
		mFragmentList.add(bsFragment);
		mFragmentList.add(cpFragment);

		mFragmentAdapter = new WFragmentAdapter(
				this.getChildFragmentManager(), mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);
		mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {

			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
									   int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();

				DLog.d("offset1:", offset+ "");
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景：
				 * 记x个页面,
				 * 从左到右分别为0,1,2
				 * 0->1; 1->2; 2->1; 1->0
				 */

				int tabWidth = (int)((screenWidth *1.0 -2* CommonUtils.dip2px(mactivity, 20))/ tabCount);
				//
				int leftMarginToLeft = (int) (offset * tabWidth + currentIndex * tabWidth + ((tabWidth - leftStartMargin+CommonUtils.dip2px(mactivity, 42)) / 2));
				int leftMarginToRight = (int) (-(1 - offset) * tabWidth + currentIndex * tabWidth + ((tabWidth - leftStartMargin+CommonUtils.dip2px(mactivity, 42)) / 2));
				//				int leftMarginToLeft  =(int) (offset * (screenWidth * 1.0 / 3) + currentIndex  * (screenWidth / 3)+CommonUtils.dip2px(mactivity, 60)+(tabWidth-leftStartMargin)/2);
				//				int leftMarginToRight= (int) (-(1 - offset)  * (screenWidth * 1.0 /3) + currentIndex   * (screenWidth / 3)+(tabWidth-leftStartMargin)/2+CommonUtils.dip2px(mactivity, 60));

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = leftMarginToLeft;

				} else if (currentIndex == 1 && position == 0) // 1->0
				{

					lp.leftMargin = leftMarginToRight;

				}
				mTabLineIv.setLayoutParams(lp);
			}

			@Override
			public void onPageSelected(int position) {
				switchBorderTab(position);
				resetTextView() ;
				switch (position) {
					case 0:
						tv_tab_1.setTextColor(getResources().getColor(R.color.hand_blue));
						tv_tab_2.setTextColor(getResources().getColor(R.color.hand_txt));
						break;
					case 1:
						tv_tab_1.setTextColor(getResources().getColor(R.color.hand_txt));
						tv_tab_2.setTextColor(getResources().getColor(R.color.hand_blue));
						break;

				}
				currentIndex = position;
			}
		});

		mPageVp.setCurrentItem(0);
	}

	/**
	 * 设置滑动条的宽度为屏幕的1/x(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getActivity().getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		leftStartMargin = (int)((screenWidth *1.0 -2* CommonUtils.dip2px(mactivity, 24))/ tabCount);
		lp.width = leftStartMargin;
		mTabLineIv.setLayoutParams(lp);
	}

	/**
	 * 重置颜色
	 */
	private void resetTextView() {
		tv_tab_1.setTextColor(getResources().getColor(R.color.hand_txt));
		tv_tab_2.setTextColor(getResources().getColor(R.color.hand_txt));
	}

	private void initView(){
		tv_car_id.setText("");//车牌
		tv_ble_id.setText("");//蓝牙mac
		tv_count.setText("");//计数器
		mdevId="";
		truckNum="";
		acache.put("car_num","");
		acache.put("n_id","");
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_ble://开始扫描
				if(mactivity.bLeService ==null){
					showTips("蓝牙未开启成功");
					return;
				}
				if (isConnected) {
					final AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
					//nomalDialog.setIcon(R.drawable.icon_dialog);
					myDialog.setTitle("提示");
					myDialog.setMessage("是否断开蓝牙设备?");
					myDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							isInitiativeDisconnect = true;
							//断开设备
							if (mactivity.bLeService != null) {
								DLog.e(TAG,"去断开设备==");
								mactivity.bLeService.disConnect();
							} else {
								DLog.e(TAG,"实际已断开设备==");
								title_ble.setText("蓝牙");
								isConnected = false;
								acache.put("is_connect",isDisConnect);
								isInitiativeDisconnect = false;
							}

							cancleTimer();
						}
					});
					myDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					});
					myDialog.show();

				} else {
					BleAct.startBleAct(mactivity);
				}
				break;
			case R.id.ll_zero://清零
				vibrator.vibrate(200);//震动指定时间
				if (isConnected) {
					final AlertDialog.Builder myDialog = new AlertDialog.Builder(mactivity);
					//nomalDialog.setIcon(R.drawable.icon_dialog);
					myDialog.setTitle(getResources().getString(R.string.hint));
					myDialog.setMessage(getResources().getString(R.string.is_zero_clearing));
					myDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							// LogUtil.e("蓝牙属性特征==" + mNotifyCharacteristic + "————" + mNotifyCharacteristic);
							if (mactivity.bLeService != null ) {
								isPlay=false;
								mactivity.bLeService.clearZero();
								isClear = true;
								clearNum = 0;
							}

						}
					});
					myDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					});
					myDialog.show();
				} else {
					showTips("蓝牙未连接");
				}
				break;
			case R.id.ll_save://保存
				vibrator.vibrate(200);//震动指定时间
				if (isConnected) {

					String str=acache.getAsString("save_switch");
					if("1".equals(str)){
						showDialog(mactivity);
					}else{
						/*保存上传*/
						if (hdData == null) {
							showTips("设备异常，请重新连接");
							return;
						}
						String deviceId = String.valueOf(hdData.nID);
						if (TextUtils.isEmpty(deviceId)) {
							showTips("设备ID未获取到，请检查蓝牙连接是否正常");
							return;
						}
						//上传数据
						String carNumber=truckNum;
						String weight;
						String str1=acache.getAsString("open_close");
						if(!Tools.isEmpty(str1) && str1.equals("1")){
							weight="0.0";
						}else{
							List<String> list = (List<String>) acache.getAsObject("wei_list");
							if(list !=null  && list.size() > 0){
								int size=list.size();
								List<WeightDataBean> listData= WeightDao.getInstance(mactivity).findweightByasc("0");//升序排列
								if(listData ==null || listData.size() <=0){
									weight=list.get(0);
								}else{
									int dsize=listData.size();
									int dex=dsize%size;
									weight=list.get(dex);
								}
							}else{
								weight="0.0";
							}
						}
						SaveWeightDataTask.getInstance(mactivity, handler).SaveData(hdData, carNumber,weight);
					}
				} else {
					closeProgressDialog();
					showTips("蓝牙未连接");
				}
				break;
			case R.id.tv_ok://
				vibrator.vibrate(200);//震动指定时间
				/*保存上传*/
				if (hdData == null) {
					showTips("设备异常，请重新连接");
					return;
				}
				weStr = dEditText.getText().toString();
				String deviceId = String.valueOf(hdData.nID);
				long time = hdData.weightTime;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date curDate = new Date(time);
				String uploadDate = formatter.format(curDate);
				if (TextUtils.isEmpty(weStr)) {
					showTips("实际重量不能为空");
					return;
				} else if (TextUtils.isEmpty(deviceId)) {
					showTips("设备ID未获取到，请检查蓝牙连接是否正常");
					return;
				} else if (TextUtils.isEmpty(uploadDate)) {
					showTips("载重数据采集时间未获取到，请检查蓝牙连接是否正常");
					return;
				}
				tempDialog.dismiss();
				CommonKitUtil.hideKeyBoard(mactivity, dEditText);
				//上传数据
				String carNumber=getCarId();
				SaveWeightDataTask.getInstance(mactivity, handler).SaveData(hdData, carNumber,weStr);
				break;
			case R.id.tv_cancel:
				/*取消上传*/
				tempDialog.dismiss();
				break;
			/*确定车牌号*/
			case R.id.tv_okTruckN://手动输入车牌
				vibrator.vibrate(200);//震动指定时间
				if(!isother){
					if (TextUtils.isEmpty(mInputView.getNumber().toString())) {
						showTips("车牌号码不能为空");
						return;
					} else if (mInputView.getNumber().length() < 7) {
						showTips("请输入正确的车牌号码");
						return;
					}
					truckNum = mInputView.getNumber();
					mInputView.updateNumber("");

					if (mPopupKeyboard.isShown()) {
						mPopupKeyboard.dismiss(getActivity());
					}
				}else{
					etNumber=et_input.getText().toString().trim();
					if (TextUtils.isEmpty(etNumber)) {
						showTips("车牌号码不能为空");
						return;
					}
					truckNum=etNumber;
					et_input.setText("");
					hideSoft(et_input);
				}
				dialog_truckN_input.setVisibility(View.GONE);
				tv_car_id.setText(truckNum);//车牌
				acache.put("car_num",truckNum);
				showTips("车牌号码添加成功");
				isEditInfo=true;
				//				mactivity.bLeService.connect(noInfoDev.getMacAddress());//连接
				//				showProgressDialog(getResources().getString(R.string.connect_ble_ing));
				//
				try{

					List<CarInfo> clist=(List<CarInfo>) acache.getAsObject("carinfo_list");
					if(clist == null || clist.size() ==0){
						clist=new ArrayList<>();
					}
					CarInfo info=new CarInfo();
					//				String name=noInfoDev.getRealName();
					//				String str=name.substring(3,name.length());
					info.setDeviceId(mdevId);
					info.setCarNumber(truckNum);
					info.setMac(mCurrentBleAddress);
					//				info.setVersion("0");
					//				info.setNew(true);
					clist.add(0,info);
					acache.put("carinfo_list",(Serializable)clist);
				}catch (Exception e){
					e.printStackTrace();
				}

				break;
			case R.id.tv_other://手动输入车牌 兼容 其他
				if(!isother){
					et_input.setVisibility(View.VISIBLE);
					mInputView.setVisibility(View.GONE);
					tv_other.setText("国内");
					isother=true;
					showSoft(et_input);
					if (mPopupKeyboard.isShown()) {
						mPopupKeyboard.dismiss(getActivity());
					}
				}else{
					et_input.setVisibility(View.GONE);
					mInputView.setVisibility(View.VISIBLE);
					tv_other.setText("其他");
					isother=false;
					hideSoft(et_input);
					if(!mPopupKeyboard.isShown()){
						mPopupKeyboard.show(getActivity());
					}
				}
				break;
			case R.id.ll_repair:
				RepairAct.start(mactivity);
				break;
			case R.id.ll_auto_check:
				AutoCheckAct.start(mactivity);
				break;
			case R.id.ll_online:
                doCheckOnline(1);
				break;
			case R.id.ll_choose_pass:
				PassSettingActNew.start(mactivity,"0");
				break;
			case R.id.ll_bin_upper:
				UpBinAct.start(mactivity);
				break;
		}

	}
	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (getActivity() == null || getActivity().isFinishing()) {
				return;
			}
			switch (msg.what) {
				case 1://搜索到设备
					List<BleDevice>list=( List<BleDevice>)acache.getAsObject("dev_list");
					listDevices=list;
					if(bleDeviceAdapter !=null){//需要网络 走网络 不需要提前显示
						bleDeviceAdapter.updateListView(list);
					}
					break;
				case 2://连接成功
					closeProgressDialog();
					isConnected = true;
					acache.put("is_connect",isConnect);
					doPlay("bleconnect.mp3");
					showTips("蓝牙连接成功");
					title_ble.setText(getResources().getString(R.string.discount_ble));

					//出现 车牌 选择
					if(mBluetoothDevice !=null){
						tv_car_id.setText("");
						showCarNumberTiP(mBluetoothDevice);
					}else{
						DLog.d(TAG,"连接的蓝牙设备为空");
						tv_car_id.setText("");
					}

					getDevId();

					//					//判断是否下载算法
					//					if(BApplication.isNeedNetWork){
					//						showProgressDialog("开始配置测重算法...");
					//						handler.postDelayed(new Runnable() {
					//							@Override
					//							public void run() {
					//								dojudgeDownload();
					//							}
					//						},500);
					//					}
					break;
				case 3://断开
					try {
						title_ble.setText(getResources().getString(R.string.connect_ble));
						isConnected = false;
						acache.put("is_connect",isDisConnect);
						clearNum = 0;
						isClear = false;
						tv_weight.setText("0.00");
						doPlay("bledisconnect.mp3");
						closeProgressDialog();
						mactivity.bLeService.closeGatt();
						if (isInitiativeDisconnect) {//是主动断开
							isInitiativeDisconnect = false;
							DLog.e(TAG,"主动断开蓝牙连接");
						} else {
							/*非主动断开清除蓝牙设备列表*/
							DLog.e(TAG,"其他情况断开蓝牙连接后去重连");
						}
						showTips("蓝牙已断开");
						initView();

					} catch (Resources.NotFoundException e) {
						e.printStackTrace();
						DLog.e(TAG,"主动断开异常==" + e.toString());
						initView();
					}
					break;
				case 4://连接异常
					initView();
					doPlay("blefail.mp3");
					title_ble.setText(getResources().getString(R.string.connect_ble));
					isConnected = false;
					acache.put("is_connect",isDisConnect);
					closeProgressDialog();
					mactivity.bLeService.closeGatt();
					showTips("蓝牙设备连接失败,请靠近设备");
					DLog.e(TAG,"立刻又返回133等其他状态时不再去重连");
					break;
				case 5:
					showTips("蓝牙信号("+ rssi+ ")—弱，请靠近设备！");
					break;
				case 6://清零成功
					if(!isPlay){
						showTips("清零成功");
						doPlay("clearzero.mp3");
						isPlay=true;
					}
					break;
				case 7://接收数据
					if(hdData == null){
						showTips("接收蓝牙数据为空");
						return;
					}
					//					carNumber=getCarId();
					//					tv_car_id.setText(carNumber);//车牌
					mdevId=hdData.nID+"";
					tv_ble_id.setText(mdevId);//ID
					acache.put("n_id",mdevId);
					tv_count.setText(hdData.nRunningNumber+"");//计数器
					if(isEditInfo){
						DecimalFormat myformat = new DecimalFormat("0.00");
						String str = myformat.format(hdData.weight);
						tv_weight.setText(str + "");
//						DLog.e(TAG,"不走算法，直接显示重量");
					}
					//					else if (!isEditInfo && BApplication.isTestLocal) {//测试
					//
					//						new Thread("do_weight") {
					//							public void run() {
					//								try {
					//									CanshuBean bean = getBeanData();
					//									double[] d1=doDouble(testTime);
					//									if(bean !=null && d1 !=null && d1.length >0 && s2 !=null && s2.length >0 && s3 !=null && s3.length >0){
					//										weightPredict = loadJNI.getWeight(d1,s2,s3, Constr.clear_flag_arr[testTime], bean);
					//										DecimalFormat myformat = new DecimalFormat("0.00");
					//										weightstr = myformat.format(weightPredict);
					//										testTime++;
					//
					//										if (testTime == 79) {
					//											testTime = 0;
					//										}
					//									}
					//
					//								}catch (Exception e){
					//									e.printStackTrace();
					//								}
					//							}
					//						}.start();
					//
					//						DLog.e(TAG,"jni重量测试第"+testTime+"次数据：/clear_flag_arr:"+Constr.clear_flag_arr[testTime]);
					//						DLog.e(TAG,"jni重量测试第"+testTime+"次："+weightstr);
					//
					//						if(weightstr ==null ){
					//							weightstr="0.00";
					//						}
					//						tv_weight.setText(weightstr + "");
					//
					//						if (mactivity.bLeService != null ) {
					//							mactivity.bLeService.sendWeight(weightPredict);
					//						}
					//
					//					} else if (!isEditInfo  && BApplication.isUseLocal) {//正式
					//						DLog.e(TAG,"算法流程，计算重量");
					//						//是否用本地算法去核算重量
					//						new Thread("do_weight_true") {
					//							public void run() {
					//								try {
					//									double[] sensor_data =doSennar(hdData,3); //sensor_data 数据影响jni里面结构
					//									StringBuffer str=new StringBuffer();
					//									for(int k=0;k<sensor_data.length;k++){
					//										str.append(sensor_data[k]+",");
					//									}
					//									DLog.e(TAG,"sensor_data:"+str);
					//									CanshuBean bean = getBeanData();
					//									if(sensor_data !=null && sensor_data.length ==42){
					//
					//										if( bean !=null && bean.getMu()!=null && bean.getSigma() != null &&  s2 !=null && s2.length >0 && s3 !=null && s3.length >0){
					//											weightPredict = loadJNI.getWeight(sensor_data,s2,s3, clear_flag, bean);
					//										}
					//										DLog.e(TAG,"weightPredict"+weightPredict);
					//										if (clear_flag == 1) {
					//											clear_flag = 0;
					//										}
					//
					//										if (mactivity.bLeService != null ) {
					//											mactivity.bLeService.sendWeight(weightPredict);
					//										}
					//
					//									}
					//
					//								}catch (Exception e){
					//									e.printStackTrace();
					//								}
					//							}
					//						}.start();
					//						DecimalFormat myformat = new DecimalFormat("0.00");
					//						weightstr = myformat.format(weightPredict);
					//						if(weightstr ==null ){
					//							weightstr="0.00";
					//						}
					//						testTime++;
					//						DLog.e(TAG,"jni重量正式第"+testTime+"次："+weightstr);
					//						tv_weight.setText(weightstr + "");
					//
					//					}
					else {
						DecimalFormat myformat = new DecimalFormat("0.00");
						String str = myformat.format(hdData.weight);
						tv_weight.setText(str + "");
//						DLog.e(TAG,"不走算法，直接显示重量");
					}

					break;
				//				case 11://下载算法成功
				//					showTips("算法配置成功");
				//					BluetoothDevice ble0 = mBluetoothDevice;
				//					String mac=ble0.getAddress();
				//					List<CarInfo> nlist=(List<CarInfo>)acache.getAsObject("carinfo_list");
				//					if(nlist !=null && nlist.size() >0){
				//						for(int i=0;i<nlist.size();i++){
				//							String addr=nlist.get(i).getMac();
				//							if(!Tools.isEmpty(addr) && addr.contains(mac)){
				//								nlist.get(i).setNew(false);
				//							}
				//						}
				//					}
				//					acache.put("carinfo_list",(Serializable)nlist); //缓存数据
				//					break;
				//				case 12://下载算法失败
				//					BluetoothDevice ble1 = mBluetoothDevice;
				//					String dname1=ble1.getName();
				//					String dirPath = FileUtils.getAppSdcardDir() + "/" + "bg_image" + "/";
				//					String filename=dname1+".txt";
				//					File file=new File(dirPath+filename);
				//					if(file.exists()){
				//						DLog.e(TAG,"下载算法失败，直接使用上次算法");
				//						acache.put("txt_name",dname1+".txt");
				//						isEditInfo=false;
				//						showTips("算法配置成功");
				//					}else{
				//						DLog.e(TAG,"下载算法失败，也没有所需的算法");
				//						acache.put("txt_name","nofile.txt");
				//						isEditInfo=true;
				//						showTips("算法配置失败");
				//					}
				//					closeProgressDialog();
				//					break;
				default:
					break;
			}
		}
	};

	//检测在线
    private void  doCheckOnline(int number){
        acache.put("add_login",number+"");
        if(!islogin){  //未登录 去登录
            String count="hdApp";
            String psw="Hd@hdApp123";
            submit(count,psw);
        }else{  //已登录
            CheckOnline();
        }

    }
    //判断是否在线
    private void CheckOnline() {
        mdevId=  acache.getAsString("n_id");
        if(!Tools.isEmpty(mdevId)){
            doCheckID(mdevId);  //判断是否在线
        } else {
            showTips("设备ID为空");
        }

    }

    //判断车辆是否在线
    private void  doCheckID(String id){
        //        showProgressDialog();
        String   token = acache.getAsString("login_token");
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("deviceId", id);
        mpresenter.checkCarOnline(mapParams);
    }


    //定时获取设备ID   解决连接蓝牙成功 但是获取不到数据的bug/主动发送命令获取数据

	private void getDevId() {
		try{
			if(!isConnected){
				showTips("蓝牙未连接");
				return;
			}
			if (task != null) {
				task.cancel();
				task = null;
			}
			final int number = 2;//运行2次 第一次主动获取设备ID  第二次 做出提示
			 task = new TimerTask() {
				int count = 1;	//从0开始计数，每运行一次timertask次数加一，运行制定次数后结束。
				@Override
				public void run() {
					DLog.e(TAG,"getDevId="+count);
					if(count<number){
						Intent readIntent=new Intent(BleConstant.ACTION_READ_DEVICE_ID);
						mactivity.sendBroadcast(readIntent);
					} else if(count ==number) {
						if(Tools.isEmpty(mdevId)){
							DLog.e(TAG,"getDevId/mdevId="+"");
							handler.post(new Runnable() {
								@Override
								public void run() {
									showDoDevIdError(mactivity);
								}
							});
						}else{
							DLog.e(TAG,"getDevId/mdevId="+mdevId);
						}

					}else{
						task.cancel();
					}
					count++;
				}
			};
			if(timer ==null){
				timer=new Timer();
			}
			timer.schedule(task, 3000,5000);//每隔1000毫秒即一秒运行一次该程序


		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void cancleTimer(){
    	if(timer !=null){
			timer.cancel();
			timer=null;

		}
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
	//显示车牌选择框
	private void  showCarNumberTiP(BluetoothDevice dev){
		String clickOnce=acache.getAsString("click_once"); //是否点击
		if(!Tools.isEmpty(clickOnce)  && "true".equals(clickOnce)){
			String name=dev.getName();
			String mac=dev.getAddress();
			if(!Tools.isEmpty(name)){
				if( name.contains("HD")){ //包含HD的蓝牙
					boolean isSameCar=false;
					List<CarInfo> clist=(List<CarInfo>) acache.getAsObject("carinfo_list");
					if(clist !=null && clist.size() >0){
						for(int j=0;j<clist.size();j++){
							CarInfo info=clist.get(j);
							String id="HD:"+info.getDeviceId();
							String carNum=info.getCarNumber();
							if(name.equals(id)){
								isSameCar=true;
								if(!Tools.isEmpty(carNum)){
									tv_car_id.setText(carNum);
									acache.put("car_num",carNum);
								}
							}
						}
					}
					if(!isSameCar){
						dialog_truckN_input.setVisibility(View.VISIBLE);
						mPopupKeyboard.show(getActivity());
						// 加上这句才会弹出键盘
						mInputView.performFirstFieldView();
						acache.put("click_once","false");//表示 已经显示过选择车牌了。
					}
				}else{ //没有HD标签的蓝牙
					boolean isSameMac=false;
					List<CarInfo> clist=(List<CarInfo>) acache.getAsObject("carinfo_list");
					if(clist !=null && clist.size() >0){
						for(int j=0;j<clist.size();j++){
							CarInfo info=clist.get(j);
							String carMac=info.getMac();
							String carNum1=info.getCarNumber();
							if(!Tools.isEmpty(mac)  && mac.equals(carMac)){
								isSameMac=true;
								if(!Tools.isEmpty(carNum1)){
									tv_car_id.setText(carNum1);
									acache.put("car_num",carNum1);
								}
							}
						}
					}
					if(!isSameMac){
						dialog_truckN_input.setVisibility(View.VISIBLE);
						mPopupKeyboard.show(getActivity());
						// 加上这句才会弹出键盘
						mInputView.performFirstFieldView();
						acache.put("click_once","false");//表示 已经显示过选择车牌了。
					}
				}
			}else{
				DLog.d(TAG,"蓝牙名称为空");
			}
		}


	}


	@Subscribe (threadMode  = ThreadMode.MAIN)  //必须使用EventBus的订阅注解
	public void onEvent(HDModelData uMode){
		hdData=uMode;
		handler.sendEmptyMessage(7);
//		DLog.e(TAG,"WeightFragment接收展示。");
	}


	/**
	 * 注册广播
	 */
	private void registerBrodcat() {
		receiver=new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action=intent.getAction();
				if(action.equals(BleConstant.ACTION_BLE_NONSUPPORT)){
					showTips("设备不支持蓝牙功能");
				}else if(action.equals(BleConstant.ACTION_BLE_DEVICE_FOUND)){
					/**
					 * 发现设备
					 */
					final BleDevice bleDevice = (BleDevice) intent.getSerializableExtra(BleConstant.EXTRA_DEVICE);
					if(bleDevice ==null){
						return;
					}
					DLog.e(TAG,"发现设备"+"/MacAddress==>"+bleDevice.getMacAddress());

					boolean isSame=false;
					List<BleDevice>list=( List<BleDevice>)acache.getAsObject("dev_list");
					if(list !=null &&list.size() >0){
						for(int i=0;i<list.size();i++){
							if(list.get(i).getMacAddress().equalsIgnoreCase(bleDevice.getMacAddress())){
								isSame=true;
							}
						}
						if(!isSame){
							list.add(bleDevice);
							acache.put("dev_list",(Serializable)list);
							handler.sendEmptyMessageDelayed(1,400);
						}

					}else{
						list.add(bleDevice);
						acache.put("dev_list",(Serializable)list);
						handler.sendEmptyMessageDelayed(1,400);
					}

				}else if(action.equals(BleConstant.ACTION_BLE_CONNECTED)){
					Bundle bundle=intent.getExtras();
					mBluetoothDevice=(BluetoothDevice)bundle.getParcelable("current_dev");//当前连接设备
					if(mBluetoothDevice != null ){
						mCurrentBleAddress=mBluetoothDevice.getAddress();//当前连接设备地址
					}
					DLog.e(TAG,"蓝牙连接成功"+mCurrentBleAddress);
					handler.sendEmptyMessage(2);
				}else if(action.equals(BleConstant.ACTION_BLE_DISCONNECT)){
					handler.sendEmptyMessage(3);
				}else if(action.equals(BleConstant.ACTION_BLE_CONNECTION_EXCEPTION)){
					handler.sendEmptyMessage(4);
				}else if(action.equals(BleConstant.ACTION_BLE_CONNECTION_RSSI)){
					rssi=intent.getIntExtra("bel_rssi",0);//信号强度
					handler.sendEmptyMessage(5);
				}else if(action.equals(BleConstant.ACTION_BLE_CLEAR_ZERO)){//清零
					handler.sendEmptyMessage(6);
				}
				//				else if(action.equals(BleConstant.ACTION_BLE_HANDLER_DATA)){//接收数据
				//					Bundle bundle=intent.getExtras();
				//					hdData=(HDModelData)bundle.getSerializable("ModelData");
				//					handler.sendEmptyMessage(7);
				//				}
				else if(action.equals(BleConstant.ACTION_UNIT_CHANGE)){//重量单位
					String str=acache.getAsString("weight_unit");
					if(Tools.isEmpty(str)){
						tv_jnit.setText("公斤");
					}else{
						tv_jnit.setText(str);
					}

				}else if(action.equals(BleConstant.ACTION_BLE_CONNECT)){
					String  address =intent.getStringExtra("Ble_Device");
					if(mactivity.bLeService !=null  && !Tools.isEmpty(address)){
						mactivity.bLeService.connect(address);//连接
						showProgressDialog(getResources().getString(R.string.connect_ble_ing));
					}
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(BleConstant.ACTION_BLE_NONSUPPORT);
		filter.addAction(BleConstant.ACTION_BLE_DEVICE_FOUND);
		filter.addAction(BleConstant.ACTION_BLE_CONNECTED);
		filter.addAction(BleConstant.ACTION_BLE_DISCONNECT);
		filter.addAction(BleConstant.ACTION_BLE_CONNECTION_EXCEPTION);
		filter.addAction(BleConstant.ACTION_BLE_CONNECTION_RSSI);
		filter.addAction(BleConstant.ACTION_BLE_CLEAR_ZERO);
		//		filter.addAction(BleConstant.ACTION_BLE_HANDLER_DATA);
		filter.addAction(BleConstant.ACTION_UNIT_CHANGE);
		filter.addAction(BleConstant.ACTION_BLE_CONNECT);
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

	//获取车牌号
	public String   getCarId(){
		String carNo="";
		if(mBluetoothDevice ==null ){
			return "";
		}
		String address=mBluetoothDevice.getAddress();
		if(Tools.isEmpty(address)){
			address="";
		}
		List<CarInfo> clist=(List<CarInfo>) acache.getAsObject("carinfo_list");
		if(clist !=null && clist.size() >0 && mBluetoothDevice != null){
			for(int i=0;i<clist.size();i++){
				CarInfo cinfo=clist.get(i);
				String addr=cinfo.getMac();
				if(addr !=null && addr.equalsIgnoreCase(address)){
					carNo=cinfo.getCarNumber();
				}
			}
		}
		return carNo;
	}

	//	//判断是否下载算法
	//	public void dojudgeDownload(){
	//		boolean iscar=false;
	//		boolean isver=false;
	//		BluetoothDevice ble0 = mBluetoothDevice;
	//		String dname=ble0.getName();
	//		List<CarInfo> list=(List<CarInfo>)acache.getAsObject("carinfo_list");
	//		if(list !=null && list.size() >0){
	//			for(int i=0;i<list.size();i++){
	//				String mac=list.get(i).getMac();
	//				if(mac !=null && mac.contains(mBluetoothDevice.getAddress())){
	//					iscar= list.get(i).isNew; //第一次或者有更新 才去下载算法
	//					String ver=list.get(i).getVersion();
	//					if(ver.contains(BApplication.VersionNumer)){ //0002-0730
	//						isver=true;
	//					}else{
	//						isver=false;
	//					}
	//				}
	//			}
	//		}
	//		//去下载算法txt
	//		if(BApplication.isNeedNetWork && !isEditInfo &&  iscar ){
	//			if(isver){
	//				handler.postDelayed(new Runnable() {
	//					@Override
	//					public void run() {
	//						downTXT();
	//					}
	//				},100);
	//				DLog.e(TAG,"算法第一次下载或者有更新，重新下载");
	//			}else{
	//				DLog.e(TAG,"没有所需的算法");
	//				acache.put("txt_name","nofile.txt");
	//				isEditInfo=true;
	//				showTips("算法参数版本不匹配");
	//				closeProgressDialog();
	//			}
	//		}else{ //判断是否已经下载过算法 --- 有就用上次上算法,走算法流程 /没有有就直接显示重量
	//			String dirPath = FileUtils.getAppSdcardDir() + "/" + "bg_image" + "/";
	//			String filename=dname+".txt";
	//			File file=new File(dirPath+filename);
	//			if(file.exists()){
	//				DLog.e(TAG,"算法已经下载过，直接使用");
	//				acache.put("txt_name",dname+".txt");
	//				isEditInfo=false;
	//				showTips("算法配置成功");
	//			}else{
	//				DLog.e(TAG,"没有所需的算法");
	//				acache.put("txt_name","nofile.txt");
	//				isEditInfo=true;
	//				showTips("算法配置失败");
	//			}
	//			closeProgressDialog();
	//		}
	//
	//	}

	public void downTXT(){
		if(CommonUtils.validateInternet(mactivity)){
			checkPermission();
		}else{
			handler.sendEmptyMessage(12);
		}
	}
	public void  showTips(String tip){
		if(!WeightFragment.this.isHidden()){
			ToastUtil.getInstance().showCenterMessage(mactivity,tip);
		}
	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog(String title) {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage(title);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		new Thread("cancle_progressDialog") {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(7000);
					// cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
					// 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
					if(progressDialog !=null ){
						progressDialog.cancel();
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
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	//对话框
	public void showDialog(final Context context) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        /*dialog.setTitle("提示");
        dialog.setMessage(message);*/
		View view = LayoutInflater.from(context).inflate(R.layout.layout_input, null);
		dEditText = (EditText) view.findViewById(R.id.et_input_weight);
		TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		ImageView iv_clear = (ImageView) view.findViewById(R.id.iv_clear);
		dEditText.setSelection(dEditText.getText().length());
		tv_ok.setOnClickListener(this);
		tv_cancel.setOnClickListener(this);
		EditTextUtil.autoClear(dEditText, iv_clear);
		dialog.setView(view);//给对话框添加一个EditText输入文本框
		//下面是弹出键盘的关键处
		tempDialog = dialog.create();
		tempDialog.setView(view, 0, 0, 0, 0);
		tempDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			public void onShow(DialogInterface dialog) {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(dEditText, InputMethodManager.SHOW_IMPLICIT);
			}
		});
		tempDialog.show();
	}
	private void checkPermission() {
		//检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
		if (ActivityCompat.checkSelfPermission(mactivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			//用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
			if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission
					.WRITE_EXTERNAL_STORAGE)) {
				//                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
			}
			//申请权限
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, REQUEST_WRITE_EXTERNAL_STORAGE);

		} else {//已经授权

			//			mpresenter.downloadTxt(mBluetoothDevice);//下载文档

			DLog.e(TAG,"TAG_SERVICE"+ "checkPermission: 已经授权！");
		}
	}
	//系统方法,从requestPermissions()方法回调结果
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		//确保是我们的请求
		if (requestCode == REQUEST_COARSE_LOCATION) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(mactivity, "权限被授予", Toast.LENGTH_SHORT).show();
			} else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(mactivity, "权限被拒绝", Toast.LENGTH_SHORT).show();
			}
		}else if(requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){//第一次主动授权
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(mactivity, "权限被授予", Toast.LENGTH_SHORT).show();
				//				mpresenter.downloadTxt(mBluetoothDevice);//下载文档

			} else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(mactivity, "权限被拒绝", Toast.LENGTH_SHORT).show();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	public void loginError(String msg) {
		DLog.e(TAG,"loginError:"+msg);
		List<CarInfo> clist=(List<CarInfo>) acache.getAsObject("carinfo_list");
		if(clist ==null || clist.size() ==0){
			clist=new ArrayList<>();
		}
		List<BleDevice>list=( List<BleDevice>)acache.getAsObject("dev_list");
		bleDeviceAdapter.setlist(clist,list);
	}

	@Override
	public void loginSuccess(List<CarInfo> carInfo) {
		if(carInfo !=null && carInfo.size()>0){
			List<CarInfo> clist=(List<CarInfo>) acache.getAsObject("carinfo_list");
			if(clist ==null || clist.size() ==0){
				clist=new ArrayList<>();
			}
			List<CarInfo> newlist= DataUtil.removeDuplicate(clist,carInfo);//去重

			List<BleDevice>list=( List<BleDevice>)acache.getAsObject("dev_list");
			bleDeviceAdapter.setlist(newlist,list);

			acache.put("carinfo_list",(Serializable)newlist); //缓存数据
		}
	}

	@Override
	public void downloadSuccess(BluetoothDevice dev) {
		closeProgressDialog();
		DLog.e(TAG,"下载txt成功");
		handler.sendEmptyMessage(11);
	}

	@Override
	public void downloadError(BluetoothDevice dev) {
		closeProgressDialog();
		DLog.e(TAG,"下载txt失败");
		handler.sendEmptyMessage(12);
	}

    @Override
    public void doLogin(UserResultBean bean) {
        closedialog();
        islogin=true;
        showTips("登录成功");
        UserResultBean userBean=bean;
        String token=bean.getToken();
//        String uname=bean.getResult().getUserName();
//        acache.put("login_name",uname);
        acache.put("login_token",token);

        String number=acache.getAsString("add_login");
        if("1".equals(number)){
            CheckOnline();
        }
    }

    @Override
    public void doLoginFail(final String bean) {
        closedialog();
        handler.post(new Runnable() {
            @Override
            public void run() {
                showTips(bean);
            }
        });

    }

    @Override
    public void onlineSuccess(String msg) {
        String line="不在线";
        if("1".equals(msg)){ //正在运行
            line="在线";
        }else{  //停止运行
            line="不在线";
        }
        showTips("该设备"+line);
    }

    @Override
    public void onlineFail(String msg) {
        showTips(msg);
    }


    //3组一次 接收3次蓝牙数据 调用一次jni /time 表示几组算一次（1,3,5）
	public double[]  doSennar(HDModelData data1,int time){
		double mvv1 = getMvvValue(data1.ad1, data1.adZero1);
		double mvv2 = getMvvValue(data1.ad2, data1.adZero2);
		double mvv3 = getMvvValue(data1.ad3, data1.adZero3);
		double mvv4 = getMvvValue(data1.ad4, data1.adZero4);
		double mvv5 = getMvvValue(data1.ad5, data1.adZero5);
		double mvv6 = getMvvValue(data1.ad6, data1.adZero6);
		double mvv7 = getMvvValue(data1.ad7, data1.adZero7);
		double mvv8 = getMvvValue(data1.ad8, data1.adZero8);
		double mvv9 = getMvvValue(data1.ad9, data1.adZero9);
		double mvv10 = getMvvValue(data1.ad10, data1.adZero10);
		double mvv11 = getMvvValue(data1.ad11, data1.adZero11);
		double mvv12 = getMvvValue(data1.ad12, data1.adZero12);
		double mvv13 = getMvvValue(data1.ad13, data1.adZero13);
		double mvv14 = getMvvValue(data1.ad14, data1.adZero14);
		double mvv15 = getMvvValue(data1.ad15, data1.adZero15);
		clearWeight(mvv1, mvv2, mvv3, mvv4, mvv5, mvv6, mvv7, mvv8, mvv9, mvv10, mvv11, mvv12, mvv13, mvv14, mvv15);
		double[] data = {mvv1, mvv2, mvv3, mvv4, mvv5, mvv6, mvv7, mvv8,mvv9,mvv10,mvv11,mvv12,mvv13,mvv14};

		DLog.e(TAG,"data="+mvv1+"/"+mvv2+"/"+mvv3+"/"+mvv4+"/"+mvv5+"/"+mvv6+"/"+mvv7+"/"+mvv8+"/"+mvv9+"/"+mvv10+"/"+mvv11+"/"+mvv12+"/"+mvv13+"/"+mvv14);

		dlist.add(data);
		if(dlist !=null &&dlist.size()==time){
			return domath(time);
		}else if (dlist !=null && dlist.size()>time){
			dlist.remove(0);//删除首位数据
			return domath(time);
		}else{
			double[] doun=new double[1];
			return doun;
		}
	}

	public  double[]  domath(int time){
		ulist.clear();
		double[] last=new double[14 *time];
		for(int i=0;i<dlist.size();i++){
			double[] a=dlist.get(i);
			for(int j=0;j<a.length;j++){
				ulist.add(a[j]);
			}
		}
		for(int k=0;k<ulist.size();k++){
			last[k]=ulist.get(k);
		}

		return last;
	}
	private float getMvvValue(float ad, float zero) {
		float x = (ad - zero) / 5;
		java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.000000");
		String str = myformat.format(x);
		float mvvValue = Float.parseFloat(str);
		return mvvValue;
	}

	//清零
	private void clearWeight(double mvv1, double mvv2, double mvv3, double mvv4, double mvv5, double mvv6, double mvv7, double mvv8, double mvv9, double mvv10, double mvv11, double mvv12, double mvv13, double mvv14, double mvv15) {
		if (isClear) {
         /* if (mvv2 < 1 && mvv2 < 1 && mvv3 < 1 && mvv4 < 1 && mvv5 < 1 && mvv6 < 1 && mvv7 < 1 && mvv8 < 1 && mvv9 < 1&& mvv10 < 1 && mvv11 < 3 && mvv12 < 3 && mvv13 <3&& mvv14 < 3) {
                clearNum++;
            }*/
			ClearZeroModel model=(ClearZeroModel)acache.getAsObject("clear_zero_model");
			String  time1=model.getCtime();
			if(Tools.isEmpty(time1)){
				time1="2";
			}
			Double time=Double.valueOf(time1);
			Double d1=Double.valueOf(model.getChannel1());
			Double d2=Double.valueOf(model.getChannel2());
			Double d3=Double.valueOf(model.getChannel3());
			Double d4=Double.valueOf(model.getChannel4());
			Double d5=Double.valueOf(model.getChannel5());
			Double d6=Double.valueOf(model.getChannel6());
			Double d7=Double.valueOf(model.getChannel7());
			Double d8=Double.valueOf(model.getChannel8());
			Double d9=Double.valueOf(model.getChannel9());
			Double d10=Double.valueOf(model.getChannel10());
			Double d11=Double.valueOf(model.getChannel11());
			Double d12=Double.valueOf(model.getChannel12());
			Double d13=Double.valueOf(model.getChannel13());
			Double d14=Double.valueOf(model.getChannel14());
			Double d15=Double.valueOf(model.getChannel15());
			Double d16=Double.valueOf(model.getChannel16());
			if (mvv1 < d1 && mvv2 < d2 &&  mvv3 < d3 && mvv4< d4 && mvv5 < d5 && mvv6 <d6 && mvv7 <d7 &&mvv8 <d8 && mvv9 <d9 &&mvv10 < d10 && mvv11 < d11 && mvv12 < d12 && mvv13 < d13 && mvv14 <d14) {
				clearNum++;
			} else {
				clearNum = 0;

			}
			if (mactivity.bLeService != null) {
				mactivity.bLeService.clearZero();
			}
			if (clearNum == time) {
				clearNum = 0;
				isClear = false;
				clear_flag = 1;
				closeProgressDialog();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						showTips("清零成功");
						doPlay("clearzero.mp3");
					}
				}, 100);
			}

		}
	}

	public double[] getDouble(int index, String[] sd,int len){
		try{
			if(sd !=null && sd.length >0 && index < sd.length){
				String s1=sd[index].toString();
				String[] f1=s1.split(",");
				int havlen=f1.length;
				double[] d1=new double[len];
				for(int i=0;i<havlen;i++){
					String str=f1[i];
					d1[i]=Double.parseDouble(str);
				}
				return d1;
			}else{
				double[] d2=new double[len];
				return d2;
			}

		}catch (Exception e){
			double[] d2=new double[len];
			return d2;
		}
	}


	//類似 123， 234， 345
	public double[] doDouble(int testTime){
		double[] sendData =new double[42];
		StringBuffer str=new StringBuffer();
		for(int i=0;i<42;i++){
			sendData[i]= Constr.Sensor_data[testTime*14+i];
			str.append(Constr.Sensor_data[testTime*14+i]+",");
		}

		DLog.e(TAG,"jni重量测试第"+testTime+"次数据：/Sensor_data:"+str);
		return  sendData;
	}


	public void hideSoft(View view) {
		InputMethodManager imm = (InputMethodManager) mactivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
	}

	public void showSoft(View view){
		InputMethodManager imm = (InputMethodManager) mactivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
	}
	public void doPlay(String filename) {
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

	private void  doAddTruck(){
		if(!islogin){  //未登录 去登录
			final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			View view = View.inflate(getActivity(), R.layout.dialog_for_login, null);
			builder.setView(view);
			builder.setCancelable(true);
			final EditText et_login_count=(EditText)view.findViewById(R.id.et_login_count);
			String uname=acache.getAsString("login_name");
			if(!Tools.isEmpty(uname)){
				et_login_count.setText(uname);
				et_login_count.setSelection(uname.length());
			}
			final EditText et_login_password=(EditText)view.findViewById(R.id.et_login_password);
			TextView tv_cancle=(TextView)view.findViewById(R.id.tv_cancle);//取消
			tv_cancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					adddialog.cancel();
				}
			});
			TextView tv_login=(TextView)view.findViewById(R.id.tv_login);//登录
			tv_login.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String count =et_login_count.getText().toString().trim();
					String psw =et_login_password.getText().toString().trim();
					if(Tools.isEmpty(count) || Tools.isEmpty(psw)){
						showTips("账号或密码不能为空");
						return;
					}
					//					submit(count,psw);
				}
			});
			//取消或确定按钮监听事件处理
			adddialog = builder.create();
			adddialog.show();
		}else{  //已登录
			//			jumpAddTruck();
		}

	}


	//	private void submit(String  count ,String password) {
	//
	//		final String inputString = count + "#" + password;
	//
	//		try {
	//			String encryStr = Aes.encrypt(inputString, PASSWORD_STRING);
	//			HashMap<String, String> mapLogin = new HashMap<>();
	//			mapLogin.put("token", encryStr);
	//			mpresenter.postLogin(mapLogin);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//
	//
	//	}

	/**
	 * 关闭进度对话框
	 */
	private void closedialog() {
		if (adddialog != null) {
			adddialog.dismiss();
			adddialog = null;
		}
	}

	//	//跳转添加车辆页面
	//	private  void jumpAddTruck(){
	//		String number="";
	//		String devid="";
	//		if(isConnected){
	//			number=carNumber;
	//			devid=mdevId;
	//		}
	//		Intent addIntent=new Intent(mactivity,AddTruckBasicActivity.class);//?
	//		addIntent.putExtra("car_number",number);
	//		addIntent.putExtra("device_id",devid);
	//		addIntent.putExtra("is_connected",isConnected);
	//		mactivity.startActivity(addIntent);
	//	}

	@Override
	public void onStop() {
		super.onStop();
		if(MyPopupWindowUtil.popupWindow !=null){
			MyPopupWindowUtil.popupWindow.dismiss();
			MyPopupWindowUtil.popupWindow=null;
		}
	}


	//开始升级固件提示框
	public void showDoDevIdError(final Context context) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(context).inflate(R.layout.layout_restart_bleor_dev, null);
		TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				devtempDialog.dismiss();
			}
		});
		tv_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				devtempDialog.dismiss();
			}
		});
		dialog.setView(view);//给对话框添加一个EditText输入文本框
		//下面是弹出键盘的关键处
		devtempDialog = dialog.create();
		devtempDialog.setView(view, 0, 0, 0, 0);
		devtempDialog.show();
	}
}
