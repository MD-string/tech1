package cn.hand.tech.ui.home;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.NetBroadcastReceiver.NetEventHandler;
import cn.hand.tech.ui.BaseActivity;
import cn.hand.tech.weiget.TopNavigation;


/**
 * 公共fragment父类
 * @date 2015.9.20
 * 
 */
public abstract class BaseFragment extends Fragment implements NetEventHandler {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void back() {
		getActivity().finish();
	}

	/**
	 * 如果需要fragment刷新数据，可以调用base的这个方法，在具体的fragmnet处理
	 */
	public void onBaseRefresh() {
		// TODO Auto-generated method stub
		
	}
	
	public void left() {
		DLog.i("BaseFragment", "left~~~~~~~~~~~");
	}
	
	public void right() {
		DLog.i("BaseFragment", "right~~~~~~~~~~~");
	}
	
	public void msg(){
		DLog.i("BaseFragment", "search~~~~~~~~~~~");
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).msg();
		}
	}
	
	public void search(){
		DLog.i("BaseFragment", "search~~~~~~~~~~~");
	}
	/**
	 * 设置导航标题
	 */
	public void setNavTitle(String title) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).setNavTitle(getClass(), title);
		}
	}

	/**
	 * 设置左边文本
	 * @param text
	 */
	public void setLeftText(String text) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).setLeftText(getClass(), text);
		}
	}
	
	/**
	 * 设置右边文本
	 * @param text
	 */
	public void setRightText(String text) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).setRightText(getClass(), text);
		}
	}

	/**
	 * 设置左边文本和颜色
	 * @param text
	 * @param color
	 */
	public void setLeftTextAndColor(String text, int color) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).setLeftTextAndColor(getClass(), text, color);
		}
	}
	
	public void setLeftTextVisibility(boolean isVisibility) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).setLeftTextVisibility(isVisibility);
		}
	}

	/**
	 * 设置右边文本和颜色
	 * @param text
	 * @param color
	 */
	public void setRightTextAndColor(String text, int color) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).setRightTextAndColor(getClass(), text, color);
		}
	}
	
	public void setRightTextVisibility(boolean isVisibility) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).setRightTextVisibility(isVisibility);
		}
	}
	
	public void setRightIcon(int resId) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).setRightIcon(getClass(), resId);
		}
	}
	
	public void setRightIcon2(int resId) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).setRightIcon2(getClass(), resId);
		}
	}
	
	public void showMsgIcon(boolean isShow) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).showMsgIcon(getClass(), isShow);
		}
	}
	
	
	/**
	 * @return
	 */
	public TopNavigation getTopNavigation() {
		if (getActivity() instanceof BaseActivity) {
			return ((BaseActivity)getActivity()).getTopNavigation();
		} else {
			return null;
		}
		
		
	}
	
	@Override
	public void onNetChange() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean clickTab(int position) {
		DLog.i("BaseFragment", "clickTab~~~~~~~~~~~position:" + position);
		return true;
	}
	
	/**
	 * 切换导航标题项
	 */
	public void switchTabTitle(int num) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).switchTabTitle(getClass(), num);
		}
	}
	
	/**
	 * 切换导航标题项
	 */
	public void switchBorderTab(int num) {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity)getActivity()).switchBorderTab(getClass(), num);
		}
	}
	
}
