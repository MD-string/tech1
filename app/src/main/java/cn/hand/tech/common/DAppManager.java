package cn.hand.tech.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import cn.hand.tech.bean.Account;
import cn.hand.tech.bean.LocationInfo;
import cn.hand.tech.bean.LoginInfo;
import cn.hand.tech.utils.Tools;


/**
 * APP管理工具类
 *@date 2016.12.5
 */
public class DAppManager {

	/**Android 应用上下文*/
	public  static Context mContext = null;
	/**包名*/
	public static String pkgName = "cn.hand.tech";
	/**SharedPreferences 存储名字前缀*/
	public static final String PREFIX = "cn.hand.tech.app_";
	public static String getPackageName() {
		return pkgName;
	}
	/** 登录接口返回的接口内容 */
	private static LoginInfo mLoginInfo;

	/** 登录等账号和密码*/
	private static Account mLoginAccount;

	/**
	 * 返回上下文对象
	 * @return
	 */
	public static Context getContext(){
		return mContext;
	}

	/**
	 * 欢迎图片版本
	 */
	public static int WELCOME_PHOTO_VERSION = 2;

	/**
	 * 设置上下文对象
	 * @param context
	 */
	public static void setContext(Context context) {
		mContext = context;
		pkgName = context.getPackageName();
	}

	/**
	 * 获取userId 
	 * @return
	 */
	public static String getUserId() {
		LoginInfo lInfo = getLoginInfo();
		if (lInfo != null) {
			return lInfo.getUserid();
		}
		return  null;
	}


	/**
	 * 获取token
	 * @return
	 */
	public static String handlerToken() {
		LoginInfo lInfo = getLoginInfo();
		if (lInfo != null) {
			return lInfo.getToken();
		}
		return  null;
	}


	/**
	 * 获取登录用户的唯一ID，取userId
	 * @return
	 */
	public static String getLoginId() {
		return getUserId();
	}

	/**
	 * 是否登录
	 * @return
	 */
	public static boolean isLogin() {
		if (Tools.isEmpty(getUserId())){ 
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 登录资料保存本地
	 * @param info
	 */
	public static void saveLoginInfo(LoginInfo info) {
		if (info != null) {
			DPrefUtil.putString(DPrefSettings.SETTINGS_LOGIN_INFO.getId(), info.toString());
		} else {
			DPrefUtil.putString(DPrefSettings.SETTINGS_LOGIN_INFO.getId(), null);
		}
	}

	/**
	 * 缓存登录账号的个人资料
	 * @param user
	 */
	public static void setLoginInfo(LoginInfo user) {
		mLoginInfo = user;
	}

	/**
	 * 获取登录账号的个人资料
	 * @return
	 */
	public static LoginInfo getLoginInfo() {
		if(mLoginInfo != null) {
			return mLoginInfo;
		}

		return new LoginInfo();
	}

	/**
	 * 获取自动登录账号登录信息
	 * @return
	 */
	public static LoginInfo getAutoLoginInfo() {
		DPrefSettings loginInfoSettings = DPrefSettings.SETTINGS_LOGIN_INFO;
		String loginInfo = DPrefUtil.getString(loginInfoSettings.getId(), (String) loginInfoSettings.getDefaultValue());
		if(!TextUtils.isEmpty(loginInfo)) {
			return LoginInfo.from(loginInfo);
		}
		return null;
	}


	/**
	 * 缓存账号登录信息
	 */
	public static void setLoginAccount(Account account) {
		mLoginAccount = account;
	}

	/**
	 * 获取账号登录信息
	 * @return
	 */
	public static Account getLoginAccount() {
		return mLoginAccount;
	}

	/**
	 * 获取自动登录账号登录信息
	 * @return
	 */
	public static Account getAutoLoginAccount() {
		try {
			String registAccount = getAutoRegistAccount();
			if(!TextUtils.isEmpty(registAccount)) {
				return Account.from(registAccount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存自动登录参数
	 * @return
	 */
	public static void saveAutoAccount(Account account) {
		if (account != null) {
			DPrefUtil.putString(DPrefSettings.SETTINGS_REGIST_AUTO.getId(), account.toString());
		} else {
			DPrefUtil.putString(DPrefSettings.SETTINGS_REGIST_AUTO.getId(), null);
		}
	}

	/**
	 * 是否自动登录
	 * @return
	 */
	private static String getAutoRegistAccount() {
		DPrefSettings registAuto = DPrefSettings.SETTINGS_REGIST_AUTO;
		String registAccount = DPrefUtil.getString(registAuto.getId(), (String) registAuto.getDefaultValue());
		return registAccount;
	}

	/**
	 * 是否第一次登录
	 */
	public static boolean isFirstlogin() {
		return DPrefUtil.getBoolean(DPrefUtil.KEY_IS_FIRST_LOGIN, true);
	}

	public static void setFirstLogin(boolean isFirstLogin) {
		DPrefUtil.putBoolean(DPrefUtil.KEY_IS_FIRST_LOGIN, isFirstLogin);
	}


	public static void setWelcomePhotoVersion(int version) {
		DPrefUtil.putInt(DPrefUtil.KEY_WELCOME_PHOTO_VER, version);
	}
	/**
	 * 获取 AES 加密的 key
	 * @return
	 */
	public static String getAESkey() throws Exception {
		String  unicode=DAppManager.getLoginInfo().getUnicode();

		int index=unicode.indexOf("$");
		String  key=unicode.substring(0,index);
		return key;

	}

	/**
	 * 获取 AES 加密的 iv   偏量
	 * @return
	 */
	public static String getAESiv() throws  Exception {

		String  unicode=DAppManager.getLoginInfo().getUnicode();

		int index=unicode.indexOf("$");
		String iv=unicode.substring(index+1,unicode.length());
		return  iv;

	}


	private static LocationInfo mLocInfo;

	/**
	 * 获取定位城市信息
	 * @return
	 */
	public static LocationInfo getLocationInfo() {
		if (mLocInfo == null) {
			mLocInfo = DPrefUtil.getObject(DPrefUtil.KEY_LOCATION_INFO, LocationInfo.class);
			if (mLocInfo != null) {
				mLocInfo.setLocationSuccess(false);			//本地缓存的定位数据，非定位成功
			}
		}

		if (mLocInfo == null) {
			return new LocationInfo();
		}

		return mLocInfo;
	}
	public static void setLocationInfo(LocationInfo info) {
		DPrefUtil.putObject(DPrefUtil.KEY_LOCATION_INFO, info);
		mLocInfo = info;
	}


	/**
	 * 欢迎图片版本控制
	 */
	public static int getWelcomePhotoVersion() {
		return DPrefUtil.getInt(DPrefUtil.KEY_WELCOME_PHOTO_VER, 0);
	}


	/**
	 * 退出时清除数据
	 */
	public static void destroy() {
		mLoginInfo = null;
		mLoginAccount = null;
	}
	//版本名
	public static String getVersionName() {
		return getPackageInfo().versionName;
	}

	//版本号
	public static int getVersionCode() {
		return getPackageInfo().versionCode;
	}

	private static PackageInfo getPackageInfo() {
		PackageInfo pi = null;

		try {
			PackageManager pm = mContext.getPackageManager();
			pi = pm.getPackageInfo(mContext.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}


}
