package cn.hand.tech;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.bugly.Bugly;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.hand.tech.common.DAppManager;

/**
 * Application
 * @author hxz
 */
public class BApplication extends Application {

	public static Context mContext;
	public static final String APP_ID = "530c6c8f10"; // TODO 替换成bugly上注册的appid
	public static final String APP_CHANNEL = "DEBUG"; // TODO 自定义渠道
	public static int environment =0;//项目当前所用的服务器(0代表测试环境，1代表正式环境)  等接口发布了 改成正式环境

	public static boolean isTestLocal=false;//是否执行本地调试数据，打包的时候一定要false，(测试数据)
	public static boolean isUseLocal=false;//是否用本地算法去核算重量

	public static boolean isNeedNetWork=false;//是否需要网络
	public static String  VersionNumer="0002";//
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();

		DAppManager.setContext(mContext);
		File loaderCacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), ".android/hand/imageLoader/Cache");

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				//				.showImageForEmptyUri(R.drawable.defalt_picture)
				//				.showImageOnFail(R.drawable.defalt_picture)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())//
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(100)
				.writeDebugLogs()
				.discCache(new UnlimitedDiscCache(loaderCacheDir, null, new FileNameGenerator() {

					@Override
					public String generate(String imageUri) {
						return String.valueOf(imageUri.hashCode() + ".jpg");
					}
				}))
				.build();//
		ImageLoader.getInstance().init(config);

		// 初始化 UncatchException自定义异常
		AppExceptionHandler eHandler = AppExceptionHandler.getInstance();
		eHandler.init();
		doset();//bugly自动更新功能
		closeAndroidPDialog(); //去掉在Android P上的提醒弹窗 （Detected problems with API compatibility)
	}

	public  void  doset(){
		//		/**** Beta高级设置*****/
		//		/**
		//		 * true表示app启动自动初始化升级模块；
		//		 * false不好自动初始化
		//		 * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
		//		 * 在后面某个时刻手动调用
		//		 */
		//		Beta.autoInit = false;
		//
		//		 * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
		//		 */
		//		Beta.autoCheckUpgrade = true;
		//
		//		/**
		//		 * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
		//		 */
		//		Beta.initDelay = 1 * 1000;
		//
		//		/**
		//		 * 设置通知栏大图标，largeIconId为项目中的图片资源；
		//		 */
		//		Beta.largeIconId = R.mipmap.icon_logo;
		//
		//		/**
		//		 * 设置状态栏小图标，smallIconId为项目中的图片资源id;
		//		 */
		//		Beta.smallIconId = R.mipmap.icon_logo;
		//
		//
		//		/**
		//		 * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
		//		 * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
		//		 */
		//		Beta.defaultBannerId = R.mipmap.icon_logo;
		//
		//		/**
		//		 * 设置sd卡的Download为更新资源保存目录;
		//		 * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
		//		 */
		//		Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		//
		//		/**
		//		 * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
		//		 */
		//		Beta.showInterruptedStrategy = false;
		//
		//		/**
		//		 * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
		//		 * 不设置会默认所有activity都可以显示弹窗;
		//		 */
		//		Beta.canShowUpgradeActs.add(MainFragmentActivity.class);


		/**
		 * 已经接入Bugly用户改用上面的初始化方法,不影响原有的crash上报功能;
		 * init方法会自动检测更新，不需要再手动调用Beta.checkUpdate(),如需增加自动检查时机可以使用Beta.checkUpdate(false,false);
		 * 参数1： applicationContext
		 * 参数2：appId
		 * 参数3：是否开启debug
		 */
		Bugly.init(mContext, APP_ID, false);

	}

	private void closeAndroidPDialog(){

		if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
			try {
				Class aClass = Class.forName("android.content.pm.PackageParser$Package");
				Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
				declaredConstructor.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Class cls = Class.forName("android.app.ActivityThread");
				Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
				declaredMethod.setAccessible(true);
				Object activityThread = declaredMethod.invoke(null);
				Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
				mHiddenApiWarningShown.setAccessible(true);
				mHiddenApiWarningShown.setBoolean(activityThread, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onTerminate() { //真机不调用
		super.onTerminate();
	}
}
