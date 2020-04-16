package cn.hand.tech.log;

import android.util.Log;

/**
 * 日志显示
 */
public class DLog {
	
	private static boolean isDebug = true;
	private static final String TAG = "hande_weight";
	
	public static void i(String tag , String msg){
		if (isDebug) 
			Log.i(TAG, TAG+"=>"+tag+"==>"+msg);
	}
	public static void i(String tag , String msg, Throwable e){
		if (isDebug) 
			Log.i(TAG, TAG+"=>"+tag+"==>"+msg, e);
	}
	
	public static void e(String tag , String msg){
		if (isDebug) 
			Log.e(TAG,TAG+"=>"+tag+"==>"+msg);
	}
	
	public static void e(String tag , String msg, Throwable e){
		if (isDebug) 
			Log.e(TAG,TAG+"=>"+tag+"==>"+msg, e);
	}
	
	public static void d(String tag , String msg){
		if (isDebug) 
			Log.d(TAG,TAG+"=>"+tag+"==>"+msg);
	}
	
	public static void d(String tag , String msg, Throwable e){
		if (isDebug) 
			Log.d(TAG,TAG+"=>"+tag+"==>"+msg, e);
	}
	
	public static void v(String tag , String msg){
		if (isDebug) 
			Log.v(TAG,TAG+"=>"+tag+"==>"+msg);
	}
	
	public static void v(String tag , String msg, Throwable e){
		if (isDebug) 
			Log.v(TAG,TAG+"=>"+tag+"==>"+msg, e);
	}
	
	public static void w(String tag , String msg){
		if (isDebug) 
			Log.w(TAG,TAG+"=>"+tag+"==>"+msg);
	}
	
	public static void w(String tag , String msg, Throwable e){
		if (isDebug) 
			Log.w(TAG,TAG+"=>"+tag+"==>"+msg, e);
	}
	
	public static void setIsDebug(boolean isDebugLog) {
		isDebug = isDebugLog;
	}

}
