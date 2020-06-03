package cn.hand.tech.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.HashMap;

import androidx.fragment.app.FragmentActivity;
import cn.hand.tech.R;
import cn.hand.tech.common.DAppManager;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.NetBroadcastReceiver.NetEventHandler;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.NavigationConfig;
import cn.hand.tech.weiget.TopNavigation;

/**所有独立Activity的基础类
 * @date 2016.12.5
 *
 */
public class BaseActivity extends FragmentActivity implements NetEventHandler{


	private static final String TAG = "BaseActivity";
	protected TopNavigation mDefaultTopNavigation;
	protected View mLinearTopNavigation;
	public ViewGroup mActivityLayout;
	private Class<?> mConfigClass;


	/**
	 * 普通类型的标题
	 */
	public final static int TITLE_TYPE_NORMAL = 0;
	/**
	 * tab类型的标题
	 */
	public final static int Title_TYPE_TAB = 1;
	/**
	 * 搜索类型的标题
	 */
	public final static int Title_TYPE_SEARCH = 2;
	/**
	 * 有边框的tab类型
	 */
	public final static int Title_TYPE_BORDER_TAB = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		CommonUtils.add(this);
		//		overridePendingTransition(R.anim.right_in, R.anim.right_out);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		//		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		mDefaultTopNavigation = new TopNavigation(this);

		mDefaultTopNavigation.measure(0, 0);
			
		int topHeight = mDefaultTopNavigation.getMeasuredHeight();


		mLinearTopNavigation = new View(this);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, topHeight);
		mLinearTopNavigation.setLayoutParams(layoutParams);

		mDefaultTopNavigation.setOnLeftIconClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				left();
			}
		});
		mDefaultTopNavigation.setOnLeftClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				left();
			}
		});
		mDefaultTopNavigation.setOnRightIconClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				right();
			}
		});
		mDefaultTopNavigation.setOnRightClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				right();
			}
		});
		mDefaultTopNavigation.setOnMsgIconClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				msg();
			}
		});
		mDefaultTopNavigation.setOnSearchViewClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				search();
			}
		});
		mDefaultTopNavigation.setOnTabTitle1ClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickTab(0)) {
					switchTabTitle(mConfigClass, 0);
				}
			}
		});
		mDefaultTopNavigation.setOnTabTitle2ClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickTab(1)) {
					switchTabTitle(mConfigClass, 1);
				}
			}
		});
		mDefaultTopNavigation.setOnBorderTab1ClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickTab(0)) {
					switchBorderTab(mConfigClass, 0);
				}
			}
		});
		mDefaultTopNavigation.setOnBorderTab2ClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickTab(1)) {
					switchBorderTab(mConfigClass, 1);
				}
			}
		});

		showUnReadMsgMark(); // 初始化未读消息数目

	}

	@Override
	public void setContentView(int layoutResID) {
		View content = LayoutInflater.from(this).inflate(layoutResID, null);
		if (mActivityLayout != null) {
			mActivityLayout.removeAllViews();
		}

		initNavigationByConfig();

		mActivityLayout = new FrameLayout(this);
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setOrientation(LinearLayout.VERTICAL);

		FrameLayout.LayoutParams flayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		mActivityLayout.addView(contentLayout, flayoutParams);
		mActivityLayout.addView(mDefaultTopNavigation);

		LinearLayout.LayoutParams llayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		contentLayout.addView(mLinearTopNavigation);
		contentLayout.addView(content, llayoutParams);

		setOverlayTopLayout();

		super.setContentView(mActivityLayout);

	}

	/**
	 * 设置头部是否覆盖形式
	 */
	private void setOverlayTopLayout() {
		if(mDefaultTopNavigation.getNavigationConfig().isShow() && !mDefaultTopNavigation.getNavigationConfig().isOverlay()){
			mLinearTopNavigation.setVisibility(View.VISIBLE);
		}else{
			mLinearTopNavigation.setVisibility(View.GONE);
		}
	}

	protected void initNavigationByConfig(Class<?> configClass) {
		mConfigClass = configClass;
		NavigationConfig navigationConfig = configClass.getAnnotation(NavigationConfig.class);
		if (navigationConfig != null) {
			mDefaultTopNavigation.setNavigationConfig(navigationConfig);
			setOverlayTopLayout();

			mDefaultTopNavigation.setBackgroundRes(navigationConfig.backGroundResId());


			showTitleByType(navigationConfig.showNavType(), navigationConfig.tabTitle1Value(), navigationConfig.tabTitle2Value());


			if (navigationConfig.leftIconId() != -1)
				mDefaultTopNavigation.setLeftIcon(navigationConfig.leftIconId());

			if (navigationConfig.rightIconId() != -1)
				mDefaultTopNavigation.setRightIcon(navigationConfig.rightIconId());

			if (titleValues.containsKey(configClass)) {
				String title = titleValues.get(configClass);
				mDefaultTopNavigation.setTitle(title);
			} else if (navigationConfig.titleId() != -1) {
				mDefaultTopNavigation.setTitle(navigationConfig.titleId());
			} else if (navigationConfig.titleValue() != null) {
				mDefaultTopNavigation.setTitle(navigationConfig.titleValue());
			}

			if (!navigationConfig.isShow()) {
				mDefaultTopNavigation.setVisibility(View.GONE);
			} else {
				mDefaultTopNavigation.setVisibility(View.VISIBLE);
			}
			

			String leftTx = navigationConfig.showLeftText();
			if (navigationConfig.showLeftTextId() != -1) {
				leftTx = getResources().getString(navigationConfig.showLeftTextId());
			}

			if (leftTextValues.containsKey(configClass)) {
				leftTx = leftTextValues.get(configClass);
			}

			if (!Tools.isEmpty(leftTx)) {
				mDefaultTopNavigation.setLeftIconVisibility(View.GONE);
				mDefaultTopNavigation.setLeftVisibility(View.VISIBLE);
				mDefaultTopNavigation.setLeftText(leftTx);
			} else {
				if (navigationConfig.showLeftIcon() != -1) {
					mDefaultTopNavigation.setLeftIconVisibility(View.VISIBLE);
					mDefaultTopNavigation.setLeftIcon(navigationConfig.showLeftIcon());
					mDefaultTopNavigation.setLeftVisibility(View.GONE);
				} else if (navigationConfig.showLeft()) {
					mDefaultTopNavigation.setLeftIconVisibility(View.VISIBLE);
					mDefaultTopNavigation.setLeftVisibility(View.GONE);
				} else {
					mDefaultTopNavigation.setLeftIconVisibility(View.GONE);
					mDefaultTopNavigation.setLeftVisibility(View.GONE);
				}
			}

			String rightTx = navigationConfig.showRightText();
			if (navigationConfig.showRightTextId() != -1) {
				rightTx = getResources().getString(navigationConfig.showRightTextId());
			}

			if (rightTextValues.containsKey(configClass)) {
				rightTx = rightTextValues.get(configClass);
			}

			if (!Tools.isEmpty(rightTx)) {
				mDefaultTopNavigation.setRightIconVisibility(View.GONE);
				mDefaultTopNavigation.setRightVisibility(View.VISIBLE);
				mDefaultTopNavigation.setRightText(rightTx);
				showNavDotlayout(-1);
			} else {
				int rightIconResId = navigationConfig.showRightIcon();
				if (rightIconResIds.containsKey(configClass)) {
					rightIconResId = rightIconResIds.get(configClass);
				}

				if (rightIconResId != -1 && rightIconResId != 0) {
					mDefaultTopNavigation.setRightIconVisibility(View.VISIBLE);
					mDefaultTopNavigation.setRightIcon(rightIconResId);
					mDefaultTopNavigation.setRightVisibility(View.GONE);
					showNavDotlayout(rightIconResId);
				} else if (navigationConfig.showRight()) {
					mDefaultTopNavigation.setRightIconVisibility(View.VISIBLE);
					mDefaultTopNavigation.setRightVisibility(View.GONE);
					showNavDotlayout(-1);
				} else {
					mDefaultTopNavigation.setRightIconVisibility(View.GONE);
					mDefaultTopNavigation.setRightVisibility(View.GONE);
					showNavDotlayout(-1);
				}
			}

			if (rightIcon2ResIds.containsKey(configClass)) {
				int rightIcon2ResId = rightIcon2ResIds.get(configClass);
				if (rightIcon2ResId != 0) {
					mDefaultTopNavigation.setRightIcon2Visibility(View.VISIBLE);
					mDefaultTopNavigation.setRightIcon2(rightIcon2ResId);
				}
			}

			boolean showMegIcon = navigationConfig.showMsgIcon();
			if (showMessageIconBools.containsKey(configClass)) {
				showMegIcon = showMessageIconBools.get(configClass);
			}

			if(showMegIcon){
				mDefaultTopNavigation.setMsgIconVisibility(View.VISIBLE);
			}else{
				mDefaultTopNavigation.setMsgIconVisibility(View.GONE);
			}

			//通过setLeftTextColor方法设置的颜色
			int leftColor = 0;
			if (leftTextColors.containsKey(configClass)) {
				leftColor = leftTextColors.get(configClass);
			}

			//通过注解设置的左边文本颜色
			int leftTextColor = navigationConfig.leftTextColor();
			if (leftColor != 0) {
				mDefaultTopNavigation.getNavLeftText().setTextColor(getResources().getColor(leftColor));
			} else if (leftTextColor != -1) {
				mDefaultTopNavigation.getNavLeftText().setTextColor(getResources().getColor(leftTextColor));
			} else {
				mDefaultTopNavigation.getNavLeftText().setTextColor(getResources().getColor(R.color.black));
			}

			int rightColor = 0;
			if (rightTextColors.containsKey(configClass)) {
				rightColor = rightTextColors.get(configClass);
			}
			//右边文本颜色
			int rightTextColor = navigationConfig.rightTextColor();
			if (rightColor != 0) {
				mDefaultTopNavigation.getNavRightText().setTextColor(getResources().getColor(rightColor));
			} else if (rightTextColor != -1) {
				mDefaultTopNavigation.getNavRightText().setTextColor(getResources().getColor(rightTextColor));
			} else {
				mDefaultTopNavigation.getNavRightText().setTextColor(getResources().getColor(R.color.black));
			}

		}
	}

	/**
	 * 导航右边消息红点显示控制
	 */
	private void showNavDotlayout(int rightIconResId) {
		//		if (rightIconResId == R.drawable.icon_s_more) {
		//			mDefaultTopNavigation.setNavDotLayoutVisibility(true);
		//		} else {
		//			mDefaultTopNavigation.setNavDotLayoutVisibility(false);
		//		}
	}

	/**
	 * 设置标题显示类型
	 * @param type
	 */
	public void showTitleByType(int type) {
		showTitleByType(type, null, null);

	}

	/**
	 * 设置标题显示类型
	 */
	public void showTitleByType(int type, String tabTitle1, String tabTitle2) {
		if (type == TITLE_TYPE_NORMAL) {
			mDefaultTopNavigation.setTitleViewVisibility(View.VISIBLE);
			mDefaultTopNavigation.setTabTitleViewVisibility(View.GONE);
			mDefaultTopNavigation.setSearchViewVisibility(View.GONE);
			mDefaultTopNavigation.setBorderTabViewVisibility(View.GONE);
		}

		if (type == Title_TYPE_TAB) {
			mDefaultTopNavigation.setTitleViewVisibility(View.GONE);
			mDefaultTopNavigation.setTabTitleViewVisibility(View.VISIBLE);
			mDefaultTopNavigation.setSearchViewVisibility(View.GONE);
			mDefaultTopNavigation.setBorderTabViewVisibility(View.GONE);
		}

		if (type == Title_TYPE_SEARCH) {
			mDefaultTopNavigation.setTitleViewVisibility(View.GONE);
			mDefaultTopNavigation.setTabTitleViewVisibility(View.GONE);
			mDefaultTopNavigation.setSearchViewVisibility(View.VISIBLE);
			mDefaultTopNavigation.setBorderTabViewVisibility(View.GONE);
		}

		if (type == Title_TYPE_BORDER_TAB) {
			mDefaultTopNavigation.setTitleViewVisibility(View.GONE);
			mDefaultTopNavigation.setTabTitleViewVisibility(View.GONE);
			mDefaultTopNavigation.setSearchViewVisibility(View.GONE);
			mDefaultTopNavigation.setBorderTabViewVisibility(View.VISIBLE);
		}
		if (type == Title_TYPE_TAB) {
			mDefaultTopNavigation.setTabTitle1(tabTitle1);
			mDefaultTopNavigation.setTabTitle2(tabTitle2);

			int num = 0;//默认显示第一项
			if (tabNums.containsKey(mConfigClass)) {
				num = tabNums.get(mConfigClass);
			}

			switchTabTitle(mConfigClass, num);
		}
		if (type == Title_TYPE_BORDER_TAB) {
			mDefaultTopNavigation.setBorderTab1(tabTitle1);
			mDefaultTopNavigation.setBorderTab2(tabTitle2);

			int num = 0;//默认显示第一项
			if (borderTabNums.containsKey(mConfigClass)) {
				num = borderTabNums.get(mConfigClass);
			}

			switchBorderTab(mConfigClass, num);
		}


	}


	private HashMap<Class<?>, String> titleValues = new HashMap<Class<?>, String>();

	public void setNavTitle(String title) {
		setNavTitle(getClass(), title);
	}

	public void setNavTitle(Class<?> classz, String title) {
		if (titleValues != null && mDefaultTopNavigation != null) {
			titleValues.put(classz, title);
			if (mConfigClass == classz) {
				mDefaultTopNavigation.setTitle(title);
			}
		}
	}


	private HashMap<Class<?>, String> rightTextValues = new HashMap<Class<?>, String>();
	private HashMap<Class<?>, String> leftTextValues = new HashMap<Class<?>, String>();

	/**
	 * 设置左边文本和颜色
	 * @param classz
	 * @param text
	 * @param color
	 */
	public void setLeftTextAndColor(Class<?> classz, String text, int color) {
		setLeftText(classz, text);
		setLeftTextColor(classz, color);
	}

	/**
	 * 设置右边文本和颜色
	 * @param classz
	 * @param text
	 * @param color
	 */
	public void setRightTextAndColor(Class<?> classz, String text, int color) {
		setRightText(classz, text);
		setRightTextColor(classz, color);
	}

	public void setLeftText(Class<?> classz, String text) {
		leftTextValues.put(classz, text);
		if (mConfigClass == classz) {
			mDefaultTopNavigation.setLeftText(text);
			mDefaultTopNavigation.setLeftIconVisibility(View.GONE);
			mDefaultTopNavigation.setLeftVisibility(View.VISIBLE);
		}
	}

	public void setRightText(Class<?> classz, String text) {
		rightTextValues.put(classz, text);
		if (mConfigClass == classz) {
			mDefaultTopNavigation.setRightText(text);
			mDefaultTopNavigation.setRightIconVisibility(View.GONE);
			mDefaultTopNavigation.setRightVisibility(View.VISIBLE);
			showNavDotlayout(-1);
		}
	}

	private HashMap<Class<?>, Integer> leftTextColors = new HashMap<Class<?>, Integer>();
	private HashMap<Class<?>, Integer> rightTextColors = new HashMap<Class<?>, Integer>();

	public void setLeftTextColor(Class<?> classz, int color) {
		leftTextColors.put(classz, color);
		if (mConfigClass == classz) {
			mDefaultTopNavigation.getNavLeftText().setTextColor(getResources().getColor(color));
		}
	}

	public void setRightTextColor(Class<?> classz, int color) {
		rightTextColors.put(classz, color);
		if (mConfigClass == classz) {
			mDefaultTopNavigation.getNavRightText().setTextColor(getResources().getColor(color));
		}
	}

	public void setLeftTextVisibility(boolean isVisibility) {
		mDefaultTopNavigation.setLeftVisibility(isVisibility?View.VISIBLE:View.GONE);
	}

	public void setRightTextVisibility(boolean isVisibility) {
		mDefaultTopNavigation.setRightVisibility(isVisibility?View.VISIBLE:View.GONE);
	}


	private HashMap<Class<?>, Integer> rightIconResIds = new HashMap<Class<?>, Integer>();
	private HashMap<Class<?>, Integer> rightIcon2ResIds = new HashMap<Class<?>, Integer>();

	public void setRightIcon(Class<?> classz, int resId) {
		rightIconResIds.put(classz, resId);
		if (mConfigClass == classz) {
			mDefaultTopNavigation.setRightIconVisibility(View.VISIBLE);
			mDefaultTopNavigation.setRightIcon(resId);
			mDefaultTopNavigation.setRightVisibility(View.GONE);
			showNavDotlayout(resId);
		}
	}

	public void setRightIcon2(Class<?> classz, int resId) {
		rightIcon2ResIds.put(classz, resId);
		if (mConfigClass == classz) {
			mDefaultTopNavigation.setRightIcon2(resId);
		}
	}

	private HashMap<Class<?>, Boolean> showMessageIconBools = new HashMap<Class<?>, Boolean>();

	public void showMsgIcon(Class<?> classz, boolean isShow) {
		showMessageIconBools.put(classz, isShow);
		if (mConfigClass == classz) {
			if (isShow) {
				mDefaultTopNavigation.setMsgIconVisibility(View.VISIBLE);
			} else {
				mDefaultTopNavigation.setMsgIconVisibility(View.GONE);
			}
		}
	}

	private void initNavigationByConfig() {
		initNavigationByConfig(getClass());
	}

	public TopNavigation getTopNavigation() {
		return mDefaultTopNavigation;
	}

	public void setBackgroundResource(int resid) {
		mActivityLayout.setBackgroundResource(resid);
	}

	public void setBackgroundDrawable(Drawable background) {
		mActivityLayout.setBackgroundDrawable(background);
	}



	/**
	 * 返回
	 */
	public void back() {
		DLog.i(TAG, "back~~~~~~~~~~~");

		finish();
	}

	/**
	 * 左边点击事件
	 */
	public void left() {
		DLog.i(TAG, "left~~~~~~~~~~~");

		back();
	}

	/**
	 * 右边点击事件
	 */
	public void right() {
		DLog.i(TAG, "right~~~~~~~~~~~");

		//		finish();
	}
	public void msg(){
		DLog.i(TAG, "msg~~~~~~~~~~~");

		//		MessageActivity.start(this);
	}
	public void search(){
		DLog.i(TAG, "search~~~~~~~~~~~");
	}

	public boolean clickTab(int position) {
		DLog.i(TAG, "clickTab~~~~~~~~~~~position:" + position);
		return true;
	}

	private HashMap<Class<?>, Integer> tabNums = new HashMap<Class<?>, Integer>();

	/**
	 * 切换导航标题项
	 */
	public void switchTabTitle(Class<?> classz, int num) {
		//		DLog.i(TAG, "switchTabTitle~~~~~~~~~~~num:" + num);
		tabNums.put(classz, num);
		if (mConfigClass != classz) {
			return;
		}
		if (num == 0) {
			mDefaultTopNavigation.setTabTitle1Color(getResources().getColor(R.color.primary));
			mDefaultTopNavigation.setTabTitle2Color(getResources().getColor(R.color.color_7b7b7b));
		} else if (num == 1) {
			mDefaultTopNavigation.setTabTitle1Color(getResources().getColor(R.color.color_7b7b7b));
			mDefaultTopNavigation.setTabTitle2Color(getResources().getColor(R.color.primary));
		}
		mDefaultTopNavigation.switchTabTitle(num);

	}

	private HashMap<Class<?>, Integer> borderTabNums = new HashMap<Class<?>, Integer>();

	/**
	 * 切换导航标题项
	 */
	public void switchBorderTab(Class<?> classz, int num) {
		//		DLog.i(TAG, "switchBorderTab~~~~~~~~~~~num:" + num);
		borderTabNums.put(classz, num);
		if (mConfigClass != classz) {
			return;
		}
		mDefaultTopNavigation.switchBorderTab(num);

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN &&event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			back();
			return false;  
		}		
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}


	@Override
	protected void onResume() {
		// HSCoreService
		super.onResume();    

	}

	/**
	 * 　 显示未读信息数目
	 */
	private void showUnReadMsgMark() {
		if(mDefaultTopNavigation == null)
			return;

		if (DAppManager.isLogin()) {
			//			long msgcount = NewMsgDao.getInstance(this).getMessageUnReadCount(DAppManager.getLoginId());
			//			long count=NewMsgDao.getInstance(this).getSpecialUnReadCount(DAppManager.getLoginId());
			//			mDefaultTopNavigation.haveNewMsg(msgcount,count);
		} else {
			mDefaultTopNavigation.haveNewMsg(0,0);
		}

	}
	/**
	 * 　 给继承的类处理消息数量
	 */
	protected void showUnReadMsgCount() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CommonUtils.remove(this);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		finish();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T getViewById(int id) {
		return (T) findViewById(id);
	}

	@Override
	public void onNetChange() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		//登录处理
		if (resultCode == RESULT_OK && intent != null) {

		}

	}

	private boolean baseToLogin(int flagCode) {
		DLog.i(TAG, "toLogin~~~~~~~flag:" + flagCode + " isLogin:" + DAppManager.isLogin());
		if (!DAppManager.isLogin()) {

			//			Intent intent = new Intent(this, LoginActivity.class);
			//			intent.putExtra(KeyConstants.EXTRA_TO_LOGIN_FLAG, flagCode);
			//			startActivityForResult(intent, REQUEST_BASE_TO_LOGIN);
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 登录完成
	 */
	public void onLoginOK(int flagCode) {
		DLog.i(TAG, "onLoginOK~~~~~~~flagCode:" + flagCode);
	}




}
