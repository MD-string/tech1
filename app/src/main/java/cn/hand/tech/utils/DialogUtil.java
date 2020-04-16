package cn.hand.tech.utils;

import android.app.ProgressDialog;

/**
 * Created by Administrator on 2016/6/20 0020.
 */
public class DialogUtil {

    public static void setProgressDialog(ProgressDialog dialog,String title) {

        // dialog.setTitle("提示");
        dialog.setMessage(title);
        //设置圆形进度条 默认圆形，不写这些
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        //dialog.setIcon(R.mipmap.ic_launcher);
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的

        //设置水平进度条
        //禁止或取消对话框
        dialog.setCancelable(true);
        dialog.show();
    }

}
