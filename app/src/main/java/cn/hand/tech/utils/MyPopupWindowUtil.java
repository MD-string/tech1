package cn.hand.tech.utils;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import cn.hand.tech.R;


/**
 * Created by feng on 2016/7/30.
 */
public class MyPopupWindowUtil {
    public static PopupWindow popupWindow;

    //定义高度根据内容 宽度充满父类，位置自定义
    public static void setPopWidthMatchParent(final Activity activity, float alpha, View view, int gravity) {
        //设置pop显示时半透明方法
        setAlpha(activity, alpha);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.PopupAnimation2);//从底部往上弹
        //三部曲：1.给popwindow设置背景色，
        setPopBgTocDis(activity);
        popupWindow.showAtLocation(view, gravity, 0, 0);

    }

    //定义根据内容适配的pop，位置自定义
    public static void setPopByWrapContent(final Activity activity, float alpha, View view, int gravity) {
        if(activity.isFinishing()){
            return;
        }
        if(popupWindow !=null){
            popupWindow.dismiss();
            popupWindow=null;
        }
        //设置pop显示时半透明方法
        setAlpha(activity, alpha);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //三部曲：1.给popwindow设置背景色，
        setPopBgTocDis(activity);
        popupWindow.showAtLocation(view, gravity, 0, 0);

    }

    //定义宽根据内容适配的pop，高充满父类，位置自定义
    public static void setPopHeightMatchParent(final Activity activity, float alpha, View view, int gravity) {
        //设置pop显示时半透明方法
        setAlpha(activity, alpha);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        //三部曲：1.给popwindow设置背景色，
        setPopBgTocDis(activity);
        popupWindow.showAtLocation(view, gravity, 0, 0);


    }

    //定义高度根据内容 宽度充满父类，位置居于视图的下面
    public static void setPopwidthMatchAsDropDown(final Activity activity, float alpha, View view, View line) {
        //设置pop显示时半透明方法
        setAlpha(activity, alpha);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        setPopBgTocDis(activity);
        //pop居于视图的下面
        popupWindow.showAsDropDown(line);

    }

    //定义宽高度根据内容适配，位置居于视图的下面
    public static void setPopAsDropDownBycontent(final Activity activity, float alpha, View view, View line) {
        //设置pop显示时半透明方法
        setAlpha(activity, alpha);
        popupWindow = new PopupWindow(view, 300, RelativeLayout.LayoutParams.WRAP_CONTENT);
        setPopBgTocDis(activity);
        //pop居于视图的下面
        popupWindow.showAsDropDown(line);
    }

    //定义根据内容适配的pop，位置自定义(通讯录添加联系人专用)
    public static void setPopByAutoLocation(final Activity activity, float alpha, View parent_view, View target_view) {
        //设置pop显示时半透明方法
        setAlpha(activity, alpha);
        popupWindow = new PopupWindow(parent_view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        setPopBgTocDis(activity);

        int[] location = new int[2];
        target_view.getLocationOnScreen(location);

        popupWindow.showAtLocation(parent_view, Gravity.NO_GRAVITY, location[0] / 2 + 120, location[1] + 20 + target_view.getHeight() - popupWindow.getHeight());

    }




    private static void setPopBgTocDis(final Activity activity) {
        //三部曲：1.给popwindow设置背景色，
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //2.给popwindow设置焦点
        popupWindow.setFocusable(true);
        //3.设置popwindow意外可点击，
        popupWindow.setOutsideTouchable(false);
        //popupWindow.setAnimationStyle(Animation.RESTART);
        //设置popwindow消失后恢复透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(activity, 1.0f);
            }
        });
          /*有虚拟按键的手机会遮挡底部的PopupWindow,做一下设置就行*/
       popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    public static void setAlpha(Activity activity, float alpha) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = alpha;
        activity.getWindow().setAttributes(params);
    }
}
