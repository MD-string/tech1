package cn.hand.tech.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import cn.hand.tech.BApplication;
import cn.hand.tech.net.HttpUtil;

/**
 * 偏好设置utils
 * 
 */
public class DPrefUtil {
	/**
	 * Preference文件名
	 */
	public static final String APP_PREFER_NAME = "preferInfo";

	public static final String KEY_WELCOME_PHOTO_VER = "KEY_WELCOME_PHOTO_VER";
	public static final String KEY_IS_FIRST_LOGIN = "KEY_IS_FIRST_LOGIN";
	public static final String KEY_LOGIN_NAME = "KEY_LOGIN_NAME";         //登录名
	public static final String KEY_PSW = "KEY_PSW";//密码
	public static final String KEY_LOGIN_PWD = "KEY_LOGIN_PWD";
	public static final String KEY_IS_SAVE_USER = "KEY_IS_SAVE_USER";
	public static final String KEY_CURRENT_TIME = "KEY_CURRENT_TIME";//当前时间
	
	public static final String KEY_PERSONAL_HEADER = "KEY_PERSONAL_HEADER";//头像
	public static final String KEY_MYSELF_MEN = "KEY_MYSELF_MEN";  //随机 男
	public static final String KEY_MYSELF_WOMEN = "KEY_MYSELF_WOMEN";  //随机 女
	
	public static final String KEY_PROVING = "KEY_PROVING";  //认证
	public static final String KEY_PHOTO = "KEY_PHOTO";  //相册
	public static final String KEY_VOCIE = "KEY_VOCIE";  //语音
	public static final String KEY_SIGN = "KEY_SIGN";  //签名
	
	public static final String KEY_PROVING_ED = "KEY_PROVING_ED";  //认证 已领金币
	public static final String KEY_PHOTO_ED = "KEY_PHOTO_ED";  //相册 已领金币
	public static final String KEY_VOCIE_ED = "KEY_VOCIE_ED";  //语音 已领金币
	public static final String KEY_SIGN_ED = "KEY_SIGN_ED";  //签名 已领金币
	
	public static final String KEY_CURRENT_TIME_ED = "KEY_CURRENT_TIME_ED";  //时间
	public static final String KEY_FIRST_INTO = "KEY_FIRST_INTO";  //第一次
	
	public static final String KEY_PUT_EVENT = "KEY_PUT_EVENT";  //发布动态
	public static final String KEY_GIFT = "KEY_GIFT";  //送礼
	public static final String KEY_WATCH_ONE = "KEY_WATCH_ONE";  //关注好友
	public static final String KEY_START_CHART = "KEY_START_CHART";  //发起聊天
	public static final String KEY_JUDGE_PERSON = "KEY_JUDGE_PERSON";  //评价
	public static final String KEY_INVITE_FRIEND = "KEY_INVITE_FRIEND";  //邀请好友
	
	public static final String KEY_PUT_EVENT_ED = "KEY_PUT_EVENT_ED";  //发布动态  已领金币
	public static final String KEY_GIFT_ED = "KEY_GIFT_ED";  //送礼  已领金币
	public static final String KEY_WATCH_ONE_ED= "KEY_WATCH_ONE_ED";  //关注好友 已领金币
	public static final String KEY_START_CHART_ED = "KEY_START_CHART_ED";  //发起聊天 已领金币
	public static final String KEY_JUDGE_PERSON_ED = "KEY_JUDGE_PERSON_ED";  //评价 已领金币
	public static final String KEY_INVITE_FRIEND_ED = "KEY_INVITE_FRIEND_ED";  //邀请好友  已领金币
	
	//定位信息
	public static final String KEY_LOCATION_INFO = "locationInfo";
	

	//是否有备注姓名
	public static final String KEY_ET_USER_NAME = "et_user_name";
	
	
	//消息提醒
	public static final String KEY_MSG_NOTIFY = "msg_notify";         //信息提醒
	public static final String KEY_MSG_NOTIFY_VOICE = "msg_notify_voice";  //声音
	public static final String KEY_MSG_NOTIFY_VIBRATE = "msg_notify_vibrate";  //振动
	
	
	/**
	 * 得到缓存对象
	 */
	public static SharedPreferences getDefaultShare() {
		return BApplication.mContext.getSharedPreferences(APP_PREFER_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * 添加Boolean数据
	 * 
	 */
	public static void putBoolean(String key, boolean value) {
		Editor sysEdit = getDefaultShare().edit();
		sysEdit.putBoolean(key, value);
		sysEdit.commit();
	}

	/**
	 * 获取Boolean数据
	 * 
	 */
	public static boolean getBoolean(String key, boolean defValue) {
		SharedPreferences share = getDefaultShare();
		if (share != null) {
			return share.getBoolean(key, defValue);
		}
		return defValue;
	}

	/**
	 * 添加String数据
	 * 
	 */
	public static void putStringByLoginId(String key, String value) {
		putString(DAppManager.getLoginId() + "_" + key, value);
	}

	/**
	 * 获取String数据
	 * 
	 */
	public static String getStringByLoginId(String key) {
		return getString(DAppManager.getLoginId() + "_" + key, "");
	}
	
	/**
	 * 添加String数据
	 * 
	 */
	public static void putString(String key, String value) {
		Editor sysEdit = getDefaultShare().edit();
		sysEdit.putString(key, value);
		sysEdit.commit();
	}
	
	/**
	 * 获取String数据
	 * 
	 */
	public static String getString(String key) {
		return getString(key, "");
	}

	/**
	 * 获取String数据
	 * 
	 */
	public static String getString(String key, String defValue) {
		SharedPreferences share = getDefaultShare();
		if (share != null) {
			return share.getString(key, defValue);
		}
		return defValue;
	}

	/**
	 * 获取Int数据
	 * 
	 */
	public static int getInt(String key, int defValue) {
		SharedPreferences share = getDefaultShare();
		if (share != null) {
			return share.getInt(key, defValue);
		}
		return 0;
	}

	/**
	 * 添加Int数据
	 * 
	 */
	public static void putInt(String key, int value) {
		Editor sysEdit = getDefaultShare().edit();
		sysEdit.putInt(key, value);
		sysEdit.commit();
	}
	
	/**
	 * 获取Long数据
	 * 
	 */
	public static long getLong(String key, long defValue) {
		SharedPreferences share = getDefaultShare();
		if (share != null) {
			return share.getLong(key, defValue);
		}
		return 0;
	}
	
	/**
	 * 添加Long数据
	 * 
	 */
	public static void putLong(String key, long value) {
		Editor sysEdit = getDefaultShare().edit();
		sysEdit.putLong(key, value);
		sysEdit.commit();
	}
	
	/**
	 * 添加Object数据			用loginId区分不同用户数据
	 * 
	 */
	public static void putObjectByLoginId(String key, Object obj) {
		putObject(DAppManager.getLoginId() + "_" + key, obj);
	}
	
	/**
	 * 获取Object数据
	 * 
	 */
	public static <T> T getObjectByLoginId(String key, Class<T> classT) {
		return getObject(DAppManager.getLoginId() + "_" + key, classT);
	}
	
	/**
	 * 获取Object数据
	 * 
	 */
	public static <T> T getObjectByLoginId(String key, T t) {
		return getObject(DAppManager.getLoginId() + "_" + key, t);
	}
	
	/**
	 * 添加Object数据
	 * 
	 */
	public static void putObject(String key, Object obj) {
		Editor sysEdit = getDefaultShare().edit();
		sysEdit.putString(key, new Gson().toJson(obj));
		sysEdit.commit();
	}
	
	public static <T> T getObject(String key, Class<T> classT) {
		SharedPreferences share = getDefaultShare();
		String result = null;
		if (share != null) {
			result = share.getString(key, "");
		}
		return HttpUtil.toBean(classT, result);
	}
	
	public static <T> T getObject(String key, T t) {
		SharedPreferences share = getDefaultShare();
		String result = null;
		if (share != null) {
			result = share.getString(key, "");
		}
		return HttpUtil.toBean(t, result);
	}
	
}
