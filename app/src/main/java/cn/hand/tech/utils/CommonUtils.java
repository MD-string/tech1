package cn.hand.tech.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.common.DAppManager;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.home.MainFragmentActivity;

/**
 * 工具处理类
 * 
 * @date 2015.9.20
 * 
 */
public class CommonUtils {

	private static List<Activity> activityList = new ArrayList<Activity>();

	/************************ 模拟堆栈，管理Activity start ************************/

	public synchronized static void remove(Activity activity) {
		activityList.remove(activity);
	}

	public synchronized static void add(Activity activity) {
		activityList.add(activity);
	}

	public synchronized static List<Activity> getActivityList() {
		return activityList;
	}
	

	/**
	 * 清除数据 并自动重新登陆
	 */
	public static void reLoginAccount() {
		DAppManager.saveLoginInfo(null);
		DAppManager.setLoginInfo(null);
//		atuoLogin();
	}
	
	

	/**
	 * 退出程序
	 */
	public static void finishProgram() {
		finishProgram(true);
	}
	
	/**
	 * 退出程序
	 */
	public synchronized static void finishProgram(boolean finishMain) {
//		SDKCoreHelper.logout(); //容联退出
		for (Activity activity : activityList) {
			if (!(activity instanceof MainFragmentActivity) || finishMain) {
				activity.finish();
			}
		}
//		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 先关闭程序再重启
	 */
	public static void finishProgramRestart(Context context, Class<? extends Activity> activity) {
		Intent loginIntent = new Intent(context, activity);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
		context.startActivity(loginIntent);
		try {
			// 重新登录操作
//			CommonUtils.finishProgram();
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * 跳转到登录
	 */
	public static void login(Context context) {
//		Intent intent = new Intent(context, LoginActivity.class);
//		context.startActivity(intent);
	}

	/************************ 模拟堆栈，管理Activity end ************************/

	/**
	 * 返回当前程序版本code
	 */
	public static int getAppVersionCode(Context context) {
		int versioncode = 0;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versioncode = pi.versionCode;

		} catch (Exception e) {
			DLog.e("VersionInfo", "Exception", e);
		}
		return versioncode;
	}
	
	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			DLog.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}
	
	/**
	 * dp > px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 判断是否有网络
	 */
	public static boolean validateInternet(Context context, boolean isOpenSetting) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo[] info = manager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		if (isOpenSetting) {
//			openWirelessSet(context);
		}
		return false;
	}

	/**
	 * 判断是否有网络
	 */
	public static boolean validateInternet(Context context) {

		return validateInternet(context, true);
	}


	/**
	 * 判断当前网络是否为WIFI连接
	 */
	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI")) {
			return true;
		}
		return false;
	}

	private static Context mDialogContext;


	/**
	 * 获取屏幕分辨率
	 * 
	 * @param context
	 * @return
	 */
	public static final Integer[] getDisplayMetrics(Context context) {
		if (context != null) {
			DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
			int screenWidth = dm.widthPixels;
			int screenHeight = dm.heightPixels;
			return new Integer[] { screenWidth, screenHeight };
		} else {
			return new Integer[] { 0, 0 };
		}
	}

	/**
	 * View总体长度
	 * 
	 * @param tv
	 * @return
	 */
	@SuppressLint("NewApi") public static float getCharacterWidth(TextView tv) {
		if (null == tv) {
			return 0f;
		}
		String text = tv.getText().toString().trim();
		float size = tv.getTextSize();
		if (null == text || "".equals(text)) {
			return 0;
		}
		// float width = 0;
		Paint paint = new Paint();
		paint.setTextSize(size);
		float text_width = paint.measureText(text);// 得到总体长度
		// width = text_width/text.length();//每一个字符的长度
		return text_width * tv.getScaleX();
	}

	private static long lastClickTime;

	/**
	 * 判断两次点击间隔
	 * 
	 * 防止点击两次view而引起两次事件的发生
	 */
	public static boolean isFastDoubleClick(long longTime) {
		long lastTime = longTime > 0?longTime:lastClickTime;
		long time = System.currentTimeMillis();
		long timeD = time - lastTime;
		if (0 < timeD && timeD < 800) {
			return true;
		}
		if (longTime <= 0) lastClickTime = time;
		return false;
	}
	
	/**
	 * 判断两次点击间隔
	 * 
	 * 防止点击两次view而引起两次事件的发生
	 */
	public static boolean isFastDoubleClick() {
		return isFastDoubleClick(-1);
	}

	/**
	*
	* 描述：获取可用内存.
	* @return
	*/
	public static long getAvailMemory(Context context) {
		// 获取android当前可用内存大小
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		// 当前系统可用内存 ,将获得的内存大小规格化
		return memoryInfo.availMem;
	}

	/**
	 * 返回可用内存
	 * @return
	 */
	public static int getAvailableMemory(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		long avi = mi.availMem;

		int round = Math.round(avi / 1024 / 1024);
		return round;
	}
	
	public static int toInt(String s) {
		try {
			return Integer.valueOf(s);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static double toDouble(String s) {
		try {
			return Double.valueOf(s);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static BigDecimal toBigDecimal(String s) {
		try {
			return new BigDecimal(s);
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	
	/**
	 * 本程序是否在前台运行
	 * 
	 * @return
	 */
	public static boolean isAppOnForeground() {
		String packageName = DAppManager.getContext().getPackageName();
		DLog.i("commom", "packageName=====>"+packageName);
		ActivityManager activityManager = (ActivityManager) DAppManager.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
	
		if (tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				return true;
			}
		}
		return false;
	}
	

	
	/**
	 * 根据年和月获取当月的天数
	 */
	public static int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		if ((year % 4) == 0) {
			flag = true;
		} else {
			flag = false;
		}
		
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}
	
	/**
	 * 清除登录信息,不要自动登录
	 * @return
	 */
	public static void clearLoginAccount() {
		DAppManager.saveAutoAccount(null);
		DAppManager.saveLoginInfo(null);
		
		DAppManager.destroy();
		

	}
	
//	/**
//	 * 设置版本cookie
//	 */
//	public static void syncVersionCookies() {
//		
//		HashMap<String, String> urls = new HashMap<String, String>();
//		urls.put(UrlConstant.getHostUrl(), "appversion="+DAppManager.COOKIE_APPVERSION);
//		
//		CookieUtils.syncCookiesMap(DAppManager.getContext(), urls, true);
//		
//	}
	/**
	 * 弹出软键盘
	 * 
	 * @param view
	 */
	public static void ShowSoftInput(View view) {
		InputMethodManager inputManager = (InputMethodManager) view
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(view, 1);
	}

	/**
	 * 收起软键盘
	 * 
	 * @param view
	 */
	public static void HintSoftInput(View view) {
		if (view == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

	}
	
	/**
	 * 触摸事件关闭键盘
	 */
	public static void onTouchHintSoftInput(final View view) {
		if (view == null) {
			return;
		}
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				CommonUtils.HintSoftInput(view);
				return false;
			}
		});
	}
	
	/**
	 * 比较两个距离
	 */
	public static int compareDistance(String strDistance1, String strDistance2) {
		boolean distance1IsDouble = true;
		boolean distance2IsDouble = true;
		double distance1 = 0;
		try {
			distance1 = Double.valueOf(strDistance1);
		} catch (Exception e) {
			distance1IsDouble = false;
		}
		
		double distance2 = 0;
		try {
			distance2 = Double.valueOf(strDistance2);
		} catch (Exception e) {
			distance2IsDouble = false;
		}
		
		if (!distance1IsDouble && !distance2IsDouble) {
			return 0;
		}
		
		if (distance1IsDouble && !distance2IsDouble) {
			return 1;
		}
		
		if (!distance1IsDouble && distance2IsDouble) {
			return 2;
		}
		
		if (distance1 < distance2) {
			return 3;
		} else if (distance1 == distance2) {
			return 4;
		} else {
			return 5;
		}
	}
	

	
	private static long mUrlTimestamp;
	
	/**
	 *  获取时间戳
	 * @return
	 */
	public static long getUrlTimestamp () {
		if (mUrlTimestamp == 0) {
			mUrlTimestamp = System.currentTimeMillis();
		}
		
		return mUrlTimestamp;
	}


}
