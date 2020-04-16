package cn.hand.tech.weiget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Window;
import android.widget.TextView;

import cn.hand.tech.R;

/**
 * 自定义加载数据对话框
 * 
 * 
 */
public class LoadDialog extends Dialog {

	public LoadDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public LoadDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		// TODO Auto-generated method stub
		Window window = this.getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
		window.setBackgroundDrawable(new BitmapDrawable());
		setContentView(R.layout.load_dialog);
	}

	/**
	 * 设置加载标题　通过字符串
	 * 
	 * @param loadMsg
	 */
	public void setText(String loadMsg) {
		((TextView) findViewById(R.id.tv_load_dialog)).setText(loadMsg);
	}

	/**
	 * 设置加载标题　通过id
	 * 
	 * @param loadMsg
	 */
	public void setText(int loadMsg) {
		((TextView) findViewById(R.id.tv_load_dialog)).setText(loadMsg);
	}
}
