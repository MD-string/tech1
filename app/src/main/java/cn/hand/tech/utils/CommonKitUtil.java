package cn.hand.tech.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import cn.hand.tech.R;

/**
 * @author wangchengfeng
 * @description 工具类集合
 * @createTime 2018/3/13
 */
public class CommonKitUtil {

    /**
     * 启动Activity
     */
    public static void startActivity(Activity activity, Class<?> cls,
                                     Bundle params, boolean isFinish) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtras(params == null ? new Bundle() : params);
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
       // activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


    /**
     * 启动回调Activity
     */
    public static void startActivityForResult(Activity activity, Class<?> cls,
                                              Bundle params, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtras(params == null ? new Bundle() : params);
        activity.startActivityForResult(intent, requestCode);
       // activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * 关闭Activity
     */
    public static void finishActivity(Activity activity) {
        activity.finish();

    }

    // 将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    // 将Json数组解析成相应的映射对象列表
    public static <T> List<T> parseJsonArrayWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        List<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>() {
        }.getType());
        return result;
    }

    /**
     * 透明状态栏
     */
    public static void windowTranslucentStatus(Activity activity) {
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上
            Window window = activity.getWindow();
            //清除导航栏和状态栏的半透明属性
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置系统的ui
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4版本到5.0以下
            Window window = activity.getWindow();
            //将窗口的状态栏设置成透明的
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    /**
     * 隐藏虚拟按键，并且全屏,此方法只是暂时隐藏，点击后又显示虚拟键，还造成隐藏的状态栏显示出来，
     */
    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            //View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE|
            /*SYSTEM_UI_FLAG_LOW_PROFILE 相当于隐藏导航栏
            SYSTEM_UI_FLAG_VISIBLE         导航栏显示
            SYSTEM_UI_FLAG_HIDE_NAVIGATION 要求导航栏完全隐藏-->但这对部分硬件设备有效*/
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LOW_PROFILE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 解决ScrollView嵌套EditText造成EditText不能滑动
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    public static boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    /**
     * 当前应用是否在后台运行 需要权限android.permission.GET_TASKS
     */
    public static boolean isAppInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否安装apk
     */
    public static boolean installNormal(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }

        intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    /**
     * 安装apk
     *
     * @param context 上下文对象
     * @param file    新版本apk文件
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取版本号
     */
    public static int getAppVersionCode(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;

    }

    /**
     * 获取当前版本
     */
    public static String getVersionName(Context context) {
        PackageManager pManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;
    }

    /**
     * 随机生成字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789"; // 生成字符串从此序列中取
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取Application节点小的meta-data下的value
     */
    public static String getMetaDataOfApp(Context context, String name) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (appInfo != null) {
                value = appInfo.metaData.getString(name);
                if (TextUtils.isEmpty(value)) {
                    value = String.valueOf(appInfo.metaData.getInt(name));
                }
            }
        } catch (Exception e) {
        }

        return value;
    }

    /**
     * 判断应用是否安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<String> packageNames = new ArrayList<String>();

        if (!packageInfos.isEmpty()) {
            for (PackageInfo info : packageInfos) {
                if (info.packageName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    //----------------------Mobile---------------------------------

    public static final boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider) return true;
        return false;
    }
    /**
     * 获取ip地址n
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }

    /**
     * 获取手机信息
     */
    public static String getMobileInfo(Activity context) {
        HashMap<String, String> mobileInfos = new HashMap<String, String>();
        TelephonyManager tm = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        mobileInfos.put("DeviceId", tm.getDeviceId());// 设备唯一编码IMEI
        mobileInfos.put("Line1Number", tm.getLine1Number());// MSISDN
        mobileInfos.put("DeviceSoftwareVersion", tm.getDeviceSoftwareVersion());
        mobileInfos.put("SimOperatorName", tm.getSimOperatorName());// ICCID:ICC
        mobileInfos.put("SimSerialNumber", tm.getSimSerialNumber());
        mobileInfos.put("NetworkType", "" + tm.getNetworkType());
        mobileInfos.put("PhoneType", "" + tm.getPhoneType());
        mobileInfos.put("BOARD", Build.BOARD);
        mobileInfos.put("BRAND", Build.BRAND);
        mobileInfos.put("DEVICE", Build.DEVICE);
        mobileInfos.put("DISPLAY", Build.DISPLAY);
        mobileInfos.put("FINGERPRINT", Build.FINGERPRINT);
        mobileInfos.put("ID", Build.ID);
        mobileInfos.put("MODEL", Build.MODEL);//机型：
        mobileInfos.put("PRODUCT", Build.PRODUCT);
        mobileInfos.put("INCREMENTAL", Build.VERSION.INCREMENTAL);
        mobileInfos.put("RELEASE", Build.VERSION.RELEASE);
        mobileInfos.put("SDK", Build.VERSION.SDK);
        mobileInfos.put("HOST", Build.HOST);
        mobileInfos.put("TAGS", Build.TAGS);
        mobileInfos.put("TYPE", Build.TYPE);
        mobileInfos.put("USER", Build.USER);
        mobileInfos.put("TIME", "" + Build.TIME);
        mobileInfos.put("Operator", tm.getSimOperator());
        mobileInfos.put("NetworkOperatorName", tm.getNetworkOperatorName());
        mobileInfos.put("SubId", tm.getSubscriberId());// IMSI
        mobileInfos.put("Country", tm.getNetworkCountryIso());
        mobileInfos.put("PhoneType", "" + tm.getPhoneType());

        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        mobileInfos.put("wifiMacAddress", info.getMacAddress());// MAC?
        mobileInfos.put("wifiState", "" + wifi.getWifiState());
        mobileInfos.put("IP", getLocalIpAddress());
        mobileInfos.put("UserAgent", new WebView(context).getSettings().getUserAgentString());
        mobileInfos.put("versionName", getVersionName(context));
        StringBuffer sb = new StringBuffer("A:");

        int width = context.getWindowManager().getDefaultDisplay().getWidth();
        int heght = context.getWindowManager().getDefaultDisplay().getHeight();
        sb.append(width + "*" + heght + "#");

        for (String key : mobileInfos.keySet()) {
            String v = mobileInfos.get(key);
            if (v != null && v.length() > 0) {
                sb.append(key).append(':').append(v).append('#');
            }
        }
        String mobileInforStr;
        if (sb.length() > 0)
            mobileInforStr = sb.substring(0, sb.length() - 1);
        else {
            mobileInforStr = sb.toString();
        }
        return mobileInforStr;
    }

    /**
     * 功能描述:检查是否存在SDCard
     *
     * @return 类型boolean:true:存在,false:不存在
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取缓存地址
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


    /**
     * 功能描述:得到SD卡路径
     */
    public static String getSDCardPath() {
        if (hasSdcard()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return null;
    }

    public static File getOutputMediaFile(Context context, String uniqueName) {
        File mediaStorageDir = getDiskCacheDir(context,
                uniqueName);
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    /**
     * 获取焦点
     */
    public static void focusView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setText("");
        }
        view.requestFocus();
    }

    /**
     * 获取焦点,并隐藏或显示软键盘
     */
    public static void showOrHideKeyBoard(Context context, boolean isShow, View view) {
        /*获取焦点*/
        if (view instanceof EditText) {
            ((EditText) view).setText("");
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        //请求获得焦点
        view.requestFocus();
        /*隐藏或显示软键盘*/
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /**
     * 获取焦点,并隐藏或显示软键盘
     */
    public static void hideKeyBoard(Context context, View view) {
        /*隐藏或显示软键盘*/
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }


    /**
     * 进入拍照界面
     */
    public static void startCameraActivity(Activity activity, int requestCode, Uri uri) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 进入拨打电话界面
     */
    public static void callPhone(Activity activity, String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            activity.startActivity(intent);
        }
    }

    /**
     * 进入拨打电话界面2
     */
    public static void callPhone2(Activity activity, String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);//ACTION_call就是直接拨打电话
            intent.setData(Uri.parse("tel:" + phoneNumber));
            activity.startActivity(intent);
        }
    }



    //-----------------------------View相关--------------------------------------------

    public static byte[] bmp2ByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    public static void setMargin(View target, int l, int t, int r, int b) {
        if (target.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) target.getLayoutParams();
            p.setMargins(l, t, r, b);
            target.requestLayout();
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }


    //-----------------------数字处理----------------------

    /**
     * 将二进制转化成十进制
     */
    public static int Binary2Dicemal(String data) {
        int result = 0;
        int pow = 0;
        for (int i = data.length() - 1; i >= 0; i--) {
            result += Math.pow(2, pow) * (data.charAt(i) == '1' ? 1 : 0);
            pow++;
        }
        return result;
    }

    /**
     * 保留1为小数
     */
    public static String leftOneDecimal(double value) {
        DecimalFormat format = new DecimalFormat(".#");
        return format.format(value);
    }

    /**
     * 显示保留2位小数的字符串数字
     *
     * @param num
     * @return
     */
    public static String Keep2Decimal(String num) {
        Double f1 = Double.parseDouble(num);
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format((f1)));
    }

    /**
     * 获取某个文件夹的大小 ，单位是kb
     *
     * @param relativePath
     * @return 返回-1表示这是个文件而不是文件夹
     * @author com.tiantian
     */
    public static float getFolderSize(String relativePath) {
        int fileLength = 0;
        File dir = new File(relativePath);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                fileLength += file.length();
            }
        } else {
            return -1;
        }
        return fileLength / 1024.0f;
    }
    //网络

  /*  *
     * 获取wifi  mac地址*/

    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        return mac;
    }


    public static boolean isNetworkAvailable(Context context) {
        //工具类，判断网络是否可用
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


   /* *
     * 判断是否是wifi连接*/

    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

 /*   *
     * 打开网络设置界面*/

    public static void openSetting(final Context context) {
        //提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if (Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }).show();
    }
    /**
     * 获取字符串的长度，中文为2字符，英文为1字符
     *
     * @param s
     * @return
     */
    public static int getLength(String s) {
        if (TextUtils.isEmpty(s))
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }
}
