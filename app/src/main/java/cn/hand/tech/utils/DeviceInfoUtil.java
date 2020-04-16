package cn.hand.tech.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

public class DeviceInfoUtil {
	
	public static final String PLATFORM = "android"; //系统平台
	
	/**
	 * 获取设备系统平台
	 * 
	 * @return
	 */
	public static String getPlatform() {
		return PLATFORM;
	}

	/**
	 * 获取设备的ID号
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getDeviceId();
	}
	
	/**
	 * 获取系统软件版本
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceSoftwareVersion(Context context) {
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getDeviceSoftwareVersion();
	}
	
	/**
	 * 获取设备的手机号
	 * 
	 * @param context
	 * @return
	 */
	public static String getLine1Number(Context context) {
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getLine1Number();
	}


	/**
	 * 获得当前版本
	 * 
	 * @param context
	 * @return
	 */
	public static String getSoftVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

}
