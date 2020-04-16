package cn.hand.tech.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.hand.tech.R;
import cn.hand.tech.weiget.ToastTextView;

/**
 * 封装Toast
 * 
 * 
 */
public class ToastUtil {
	private static ToastUtil mToastUtil;
	private Toast mToast;
	private TextView textView;
	private ImageView tiptextImage;

    public static final int LENGTH_SHORT = 0;

    public static final int LENGTH_LONG = 1;
    
	public ToastUtil() {
	}

	// 唯一实例
	public static ToastUtil getInstance() {
		if (mToastUtil == null) {
			mToastUtil = new ToastUtil();
		}
		return mToastUtil;
	}

	/**
	 * 系统的toast
	 */
	public void showMessage(Context context, String message, int duration) {
		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(context, message, duration);
		mToast.show();
	}

	/**
	 * 系统的toast
	 */
	public void showMessage(Context context, int resId, int duration) {
		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(context, resId, duration);
		mToast.show();
	}

	public void showTopNavMessage(Context context, String message) {
		float topHeight = context.getResources().getDimension(R.dimen.sw_height_1);
		showTopMessage(context, message, (int)(topHeight + CommonUtils.dip2px(context, 10)));
	}
	
	public void showMessage(Context context, String message) {
		showTopMessage(context, message, CommonUtils.dip2px(context, 10));
	}
	
	public void showCenterMessage(Context context, String message) {
		showTopMessage(context, message, context.getResources().getDimensionPixelSize(R.dimen.tiptext_bottom_margin));
	}

	/**
	 * 自定义的toast
	 */
	private void showTopMessage(Context context, String message, int top) {
		if (context instanceof Activity && ((Activity)context).isFinishing()) {
			return;
		}
		View view = View.inflate(context, R.layout.toast, null);
		textView = (ToastTextView) view.findViewById(R.id.message);
		textView.setText(message);
		
		if (mToast == null) {
			mToast = new Toast(context);
		}
		mToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, top);
		mToast.setView(view);
		
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();
	}

}
