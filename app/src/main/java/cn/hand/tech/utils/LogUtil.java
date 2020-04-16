package cn.hand.tech.utils;

import android.util.Log;

/**
 * Created by k.k on 2016/4/27.
 */
public class LogUtil {

    public static Boolean isDebug=true;//是否需要打印Log日志信息，可以在Application中的onCreate()方法中初始化
    private static final String TAG="TAG";

    public LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /*
    * 默认的Tag函数
    * */
    public static void i(String msg){
        if(isDebug)
        Log.i(TAG,msg);
    }

    public static void d(String msg){
        if(isDebug)
        Log.d(TAG, msg);
    }

    public static void e(String msg){
        if(isDebug)
        Log.e(TAG, msg);
    }

    public static void v(String msg){
        if(isDebug)
        Log.v(TAG, msg);
    }

    /*
    * 自定义的Tag函数
    * */
    public static void i(String tag,String msg){
        if(isDebug)
        Log.i(tag, msg);
    }
    public static void d(String tag,String msg){
        if(isDebug)
            Log.d(tag, msg);
    }
    public static void e(String tag,String msg){
        if(isDebug)
            Log.e(tag, msg);
    }
    public static void v(String tag,String msg){
        if(isDebug)
            Log.v(tag, msg);
    }
}
