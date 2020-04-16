package cn.hand.tech.net;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * 网络状态监控
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    public static ArrayList<NetEventHandler> mListeners = new ArrayList<NetEventHandler>();
    private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NET_CHANGE_ACTION)) {
//            Application.mNetWorkState = NetUtil.getNetworkState(context);
            if (mListeners.size() > 0)// 通知接口完成加载
                for (NetEventHandler handler : mListeners) {
                    handler.onNetChange();
                }
        }
    }

    public static abstract interface NetEventHandler {

        public abstract void onNetChange();
    }
    
    /**
     * 添加网络状态监听
     * @param object
     */
    public static void addNetListener(NetEventHandler object) {
    	mListeners.add(object);
    }
    
    /**
     * 删除网络监听
     * @param object
     */
    public static void removeListener(NetEventHandler object) {
    	mListeners.remove(object);
    }
    
}