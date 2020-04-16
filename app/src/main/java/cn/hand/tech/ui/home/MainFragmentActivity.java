package cn.hand.tech.ui.home;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tencent.bugly.beta.Beta;

import java.io.Serializable;

import cn.hand.tech.R;
import cn.hand.tech.ble.BluetoothLeService;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.ConsTantsCode;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.NetBroadcastReceiver;
import cn.hand.tech.ui.BaseActivity;
import cn.hand.tech.ui.curve.view.CurveFragment;
import cn.hand.tech.ui.data.DataFragment;
import cn.hand.tech.ui.setting.SettingFragment;
import cn.hand.tech.ui.weight.WeightFragment;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.LatLng;
import cn.hand.tech.utils.NotNull;
import cn.hand.tech.weiget.NavigationConfig;

/**
 * 主窗口
 * @date  2016.12.5
 */
@SuppressLint({ "HandlerLeak", "NewApi" })
@NavigationConfig(isShow = false, titleId = R.string.main_title, showLeft = false)
public class MainFragmentActivity extends BaseActivity {

	private int[] mTabIconIds = { R.id.tab_icon01, R.id.tab_icon02, R.id.tab_icon03,R.id.tab_icon04 };
	private int[] mTabBtnIds = { R.id.tab_menu01, R.id.tab_menu02, R.id.tab_menu03, R.id.tab_menu04 };
	private int[] mTablayIds = { R.id.tab_lay01, R.id.tab_lay02, R.id.tab_lay03, R.id.tab_lay04};
	private int[] iconRes = { R.mipmap.btm_weight_no, R.mipmap.btm_data_no, R.mipmap.icon_qu_xian, R.mipmap.btm_setting_no};
	private int[] iconResChecked = { R.mipmap.btm_weight_checked, R.mipmap.btm_data_checked, R.mipmap.icon_qu_xian_ed,R.mipmap.btm_setting_checked};


	private TextView[] mTabBtn;//

	private ImageView[] mTabIcon;


	private RelativeLayout[] mTablay;

	private BaseFragment[] fragments;

	private BaseFragment mCurFragment;

	private FragmentManager mFragmentManager;

	static private Context mContext;

	public  BluetoothLeService bLeService;
	private ACache acache;

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		jumpManager();
	}
	Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(isFinishing()){
				return;
			}
			switch (msg.what) {

				default:
					break;
			}
		}
	};
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			DLog.w("MainFragmentActivity","绑定蓝牙服务成功" );
			bLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if(bLeService.initialize()){
				DLog.w("MainFragmentActivity","蓝牙初始化成功" );
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.layout_cust_main);
		mayRequestLocation(1001);//申请辅助蓝牙 的定位权限
		this.bindService(new Intent(this,BluetoothLeService.class), conn, Context.BIND_AUTO_CREATE);
		acache= ACache.get(mContext,"WeightFragment");
		init();// 初始化
		initData();// 初始化基础数据 

		jumpManager();

		NetBroadcastReceiver.addNetListener(this);

		Beta.checkUpgrade(false,false);//自动检测更新
		
		setOnAddress();


	}
	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	//声明定位回调监听器
	public AMapLocationListener mLocationListener=new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation aMapLocation) {
			double lat=aMapLocation.getLatitude();
			double lon=aMapLocation.getLongitude();
			String address=aMapLocation.getAddress();
			DLog.w("MainFragmentActivity",address+"==>"+"lon/lat="+lon+","+ lat);
			LatLng lant=new LatLng(lon,lat);
			acache.put("current_address",(Serializable) lant); //当前位置

		}
	};
	//声明AMapLocationClientOption对象
	public AMapLocationClientOption mLocationOption = null;
	private void setOnAddress() {

		//初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
		//设置定位回调监听
		mLocationClient.setLocationListener(mLocationListener);

		//初始化AMapLocationClientOption对象
		mLocationOption = new AMapLocationClientOption();
		//设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式
		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		//设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
		mLocationOption.setInterval(3000000);
		//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		//启动定位
		mLocationClient.startLocation();
	}

	public int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@Override
	public void onNetChange() {
		super.onNetChange();
	}
	/**
	 * 公共跳转处理
	 */
	private void jumpManager() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String route = extras.getString("route");

			//			if(!Tools.isEmpty(tag)){
			//				PreviewFiveBean bean=(PreviewFiveBean) extras.getSerializable("mbean");
			//				String url=bean.getArticleUrl();
			//				if(Tools.isEmpty(url)){
			//					
			//					artUrl=UrlConstant.GET_DISCOVER_DD_INFO+"id="+bean.getArticleId()+"&"+Tools.getWebUrlParam("1");
			//					shareUrl=UrlConstant.GET_DISCOVER_DD_INFO+"id="+bean.getArticleId()+"&"+Tools.getWebUrlParam("3");
			//				}else{
			//					
			//					artUrl=url+"?"+Tools.getWebUrlParam("1");
			//					shareUrl=url+"?"+Tools.getWebUrlParam("3");
			//				}
			//				GrouponWebActivity.launchHandpickPage(mContext, artUrl, bean.getTitle(), shareUrl, bean.getIntroduction()); 
			//			}

			int tabNum = extras.getInt("tab_num", -1);
			if (tabNum != -1) {
				showTab(tabNum);
			}
		}
	}


	//	}

	private void initData() {

	}

	/**
	 * 初始化 控件
	 */
	@SuppressLint("RestrictedApi")
	private void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//不锁屏
		mFragmentManager = getSupportFragmentManager();

		DLog.i("mainfrag", "Fragments:" + mFragmentManager.getFragments());
		if (mFragmentManager.getFragments() != null && mFragmentManager.getFragments().size() != 0) {
			DLog.i("mainfrag", "Fragments size:" + mFragmentManager.getFragments().size());
			FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
			for (Fragment iterable_element : mFragmentManager.getFragments()) {
				DLog.i("mainfrag", "Fragment" + iterable_element);
				if (iterable_element != null) {
					mFragmentTransaction.remove(iterable_element);
				}

			}
			mFragmentTransaction.commitAllowingStateLoss();
			mFragmentManager.executePendingTransactions();
		}
		fragments = new BaseFragment[mTabIconIds.length];
		//fragment 首页
		fragments[0] = new WeightFragment();
		fragments[1] = new DataFragment();
		fragments[2] = new CurveFragment();
		fragments[3] = new SettingFragment();


		mTabIcon = new ImageView[mTabIconIds.length];
		mTabBtn = new TextView[mTabIconIds.length];
		mTablay = new RelativeLayout[mTabIconIds.length];

		initTab();
	}

	//初始化 tab
	private void initTab(){
		for (int i = 0; i < mTabIconIds.length; i++) {
			mTabIcon[i] = getViewById(mTabIconIds[i]);
			RelativeLayout.LayoutParams 	params = new RelativeLayout.LayoutParams(mTabIcon[i].getLayoutParams());
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			if(i ==0 ){
				params.setMargins(0, CommonUtils.dip2px(mContext, 11) , 0, 0);
				params.height=CommonUtils.dip2px(mContext,20.2f);
				params.width =CommonUtils.dip2px(mContext, 22f);
			}else if (i== 1){
				params.setMargins(0, CommonUtils.dip2px(mContext, 11) , 0, 0);
				params.height=CommonUtils.dip2px(mContext,20f);
				params.width =CommonUtils.dip2px(mContext, 20.2f);
			}else if(i== 2) {
				params.setMargins(0, CommonUtils.dip2px(mContext, 11), 0, 0);
				params.height = CommonUtils.dip2px(mContext, 19.7f);
				params.width = CommonUtils.dip2px(mContext, 20.5f);
			}else if(i== 3) {
				params.setMargins(0, CommonUtils.dip2px(mContext, 11), 0, 0);
				params.height = CommonUtils.dip2px(mContext, 19.7f);
				params.width = CommonUtils.dip2px(mContext, 20.5f);
			}
			mTabIcon[i] .setLayoutParams(params);
			mTabBtn[i] = getViewById(mTabBtnIds[i]);
			mTablay[i] = getViewById(mTablayIds[i]);
			mTablay[i].setOnClickListener(new TabListener(i));
		}

		showTab(0); // Show Table "1"
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bLeService !=null ){
			this.unbindService(conn);
		}
	}

	private int previousTabIndex = -1;


	/**
	 * Show Table
	 *
	 * @param index
	 */
	private void showTab(int index) {
		if( index != 4){

			showTabAnim(index);
		}
		for (int i = 0; i < mTabIconIds.length; i++) {
			if (i == index) {// 当前选中的
				showFragment(fragments[i]);
				mTabBtn[i].setTextColor(getResources().getColor(R.color.gg_titile));
				mTabIcon[i].setImageResource(iconResChecked[i]);
			} else {
				hideFragment(fragments[i]);
				mTabBtn[i].setTextColor(getResources().getColor(R.color.gg_99));
				mTabIcon[i].setImageResource(iconRes[i]);
			}
		}
		switch (index) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			default:
				break;
		}

		previousTabIndex = index;
	}

	@SuppressLint("NewApi") private void showTabAnim(int index) {
		//		//正常切换了
		//		if (previousTabIndex >= 0 && previousTabIndex != index) {
		//			AnimatorSet animatorSet = new AnimatorSet();
		//			animatorSet.setDuration(200);
		//			animatorSet.playTogether(ObjectAnimator.ofFloat(mTablay[previousTabIndex], "scaleX", 1.1f, 1.0f), ObjectAnimator.ofFloat(mTablay[previousTabIndex], "scaleY", 1.1f, 1.0f));
		//			animatorSet.playTogether(ObjectAnimator.ofFloat(mTablay[previousTabIndex], "scaleX", 1.0f, 1.1f), ObjectAnimator.ofFloat(mTablay[previousTabIndex], "scaleY", 1.0f, 1.1f));
		//			animatorSet.start();
		//
		//		}
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(200);
		animatorSet.playTogether(ObjectAnimator.ofFloat(mTablay[index], "scaleX", 1.0f, 1.1f), ObjectAnimator.ofFloat(mTablay[index], "scaleY", 1.0f,1.1f));
		animatorSet.playTogether(ObjectAnimator.ofFloat(mTablay[index], "scaleX", 1.1f, 1.0f), ObjectAnimator.ofFloat(mTablay[index], "scaleY", 1.1f, 1.0f));
		animatorSet.start();
	}

	class TabListener implements OnClickListener {
		int index;

		public TabListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			showTab(this.index); // Show Table
		}
	}



	/**
	 * 隐藏当前fragment
	 *
	 * @param fragment
	 */
	private void hideFragment(BaseFragment fragment) {
		if (!fragment.isAdded()) {
			return;
		}
		FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
		//		mFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
		if (!fragment.isHidden()) {
			mFragmentTransaction.hide(fragment);
		}
		mFragmentTransaction.commitAllowingStateLoss();
		mFragmentManager.executePendingTransactions();
	}

	/**
	 * 显示当前fragment
	 *
	 * @param fragment
	 */
	private void showFragment(BaseFragment fragment) {
		FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
		if (!fragment.isAdded()) {
			mFragmentTransaction.add(R.id.fragment_lay, fragment);
		}
		if (fragment.isHidden()) {
			mFragmentTransaction.show(fragment);
			fragment.onBaseRefresh(); // 用于fragment的更新
		}
		mFragmentTransaction.commitAllowingStateLoss();
		mFragmentManager.executePendingTransactions();

		mCurFragment = fragment;
		initNavigationByConfig(fragment.getClass());
	}

	/**
	 * 以下两个方法实现主界面时点击返回键实现与点击home键同样效果
	 */
	public void back() {
		moveTaskToBack(true);
	}

	@Override
	public void left() {
		if (mCurFragment != null) {
			mCurFragment.left();
		}
	}

	@Override
	public void right() {
		if (mCurFragment != null) {
			mCurFragment.right();
		}
	}

	@Override
	public void search() {
		if (mCurFragment != null) {
			mCurFragment.search();
		}
		super.search();
	}

	@Override
	public boolean clickTab(int position) {
		if (mCurFragment != null) {
			return mCurFragment.clickTab(position);
		}
		return false;
	}
	public static void start(Context context) {
		Intent intent = new Intent(context, MainFragmentActivity.class);
		context.startActivity(intent);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == ConsTantsCode.REQUEST_CODE_EMAIL) {//在HDSaveFragment邮件发送，在所依赖的Activity回调,回调结果再传递给HDSaveFragment
			if (NotNull.isNotNull(fragments[1]) && fragments[1].isVisible()) {
				fragments[1].onActivityResult(requestCode, resultCode, intent);
			}
		}
	}

	private  void mayRequestLocation(int REQUEST_COARSE_LOCATION) {
		if (Build.VERSION.SDK_INT >= 23) {
			int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
			if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
				//判断是否需要 向用户解释，为什么要申请该权限
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
					Toast.makeText(mContext, "动态请求权限", Toast.LENGTH_LONG).show();
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
				return;
			} else {

			}
		} else {

		}
	}


	//系统方法,从requestPermissions()方法回调结果
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		//确保是我们的请求
		if (requestCode == 1001) {
			if (grantResults !=null && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(mContext, "权限被授予", Toast.LENGTH_SHORT).show();
			} else if (grantResults !=null && grantResults.length >0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(mContext, "权限被拒绝", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
