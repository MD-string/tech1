package cn.hand.tech.weiget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.hand.tech.R;
import cn.hand.tech.utils.Tools;

/**
 * Activity的导航条
 * @author hxz
 * @date 2016.12.5
 *
 */
@SuppressLint("NewApi") public class TopNavigation extends LinearLayout {
	private LinearLayout mNavigationView;
	private ImageButton mLeftIcon;
	private ImageButton mRightIcon;
	private ImageButton mRightIcon2;
	
	private FrameLayout nav_dot_layout;
	private ImageView nav_dot_view;
	private TextView mNavTitle;
	private TextView mNavBottomTitle;
	private TextView nav_left_tx;
	private TextView nav_right_tx;
	private RelativeLayout top_layout;
	private FrameLayout mMsgIcon;
	private TextView mMsgUnreadDot; //非系统消息要计数
	private View mNavTitleView;
	private View mSearchView;
	private View mNavLine;
	private RelativeLayout mNavTabTitleView;
	private TextView mNavTabTitle1;
	private TextView mNavTabTitle2;
	private RelativeLayout mNavBorderTabView;
	private TextView mNavBorderTab1;
	private TextView mNavBorderTab2;
	
	private NavigationConfig navigationConfig;
	
	private boolean showMsgIcon;
	
	private LinearLayout navigation_root_ll;
	private TextView red_dot_no;//系统消息显示圆点
	
	public TopNavigation(Context context) {
		super(context);
		init(context);
	}

	public TopNavigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mNavigationView = (LinearLayout) inflate(context, R.layout.top_navigation, this);
		navigation_root_ll=(LinearLayout)findViewById(R.id.navigation_root_ll);
		top_layout=(RelativeLayout)findViewById(R.id.top_layout);
		mNavTitle = (TextView) findViewById(R.id.nav_tv_title);
		mNavBottomTitle = (TextView) findViewById(R.id.nav_tv_title_bottom);
		mLeftIcon = (ImageButton) mNavigationView.findViewById(R.id.nav_left_img);
		mRightIcon = (ImageButton) mNavigationView.findViewById(R.id.nav_right_img);
		mMsgIcon = (FrameLayout) mNavigationView.findViewById(R.id.nav_msg);
		
		nav_dot_layout = (FrameLayout) mNavigationView.findViewById(R.id.nav_dot_layout);
		nav_dot_view = (ImageView) mNavigationView.findViewById(R.id.nav_dot_view);
		
		mMsgUnreadDot = (TextView) mNavigationView.findViewById(R.id.red_dot);
		red_dot_no=(TextView)mNavigationView.findViewById(R.id.red_dot_no);
		
		mRightIcon2 = (ImageButton) mNavigationView.findViewById(R.id.nav_right_img_2);
		nav_left_tx = (TextView) mNavigationView.findViewById(R.id.nav_left_tx);
		nav_right_tx = (TextView) mNavigationView.findViewById(R.id.nav_right_tx);
		mNavTitleView = mNavigationView.findViewById(R.id.nav_title);
		mSearchView = mNavigationView.findViewById(R.id.nav_search);
		mNavLine = mNavigationView.findViewById(R.id.nav_line);
		mNavTabTitleView = (RelativeLayout)mNavigationView.findViewById(R.id.nav_tab_title);
		mNavTabTitle1 = (TextView)mNavigationView.findViewById(R.id.nav_tab_title_1);
		mNavTabTitle2 = (TextView)mNavigationView.findViewById(R.id.nav_tab_title_2);
		
		mNavBorderTabView = (RelativeLayout)mNavigationView.findViewById(R.id.nav_border_tab);
		mNavBorderTab1 = (TextView)mNavigationView.findViewById(R.id.nav_border_tab_1);
		mNavBorderTab2 = (TextView)mNavigationView.findViewById(R.id.nav_border_tab_2);
		showMsgIcon = true;
	}
	
	/**
	 * 有未读消息
	 */
	public void haveNewMsg(long unreadCount,long count){
		if(!showMsgIcon || mMsgIcon == null)
			return;
		if (unreadCount > 99) {
			red_dot_no.setVisibility(View.GONE);
			mMsgUnreadDot.setVisibility(View.VISIBLE);
			mMsgUnreadDot.setText("");
			mMsgUnreadDot.setBackgroundResource(R.mipmap.icon_over_point);
		} else if (unreadCount > 0) {
			red_dot_no.setVisibility(View.GONE);
			mMsgUnreadDot.setVisibility(View.VISIBLE);
			mMsgUnreadDot.setText(String.valueOf(unreadCount));
			mMsgUnreadDot.setBackgroundResource(R.mipmap.icon_num_point);
		} else {
			mMsgUnreadDot.setText("0");
			mMsgUnreadDot.setVisibility(View.GONE);
			if(count > 0){
				red_dot_no.setText("");
				red_dot_no.setVisibility(View.VISIBLE);
			}else{
				red_dot_no.setVisibility(View.GONE);
			}
		}
		
		if (unreadCount > 0 || count > 0) {
			setNavDotViewVisibility(true);
		} else {
			setNavDotViewVisibility(false);
		}
	}
	/**
	 * 清空未读消息数
	 */
	public void cleanMsgUnread(){
		if(!showMsgIcon)
			return;
		
		haveNewMsg(0,0);
	}
	/**
	 * 设置导航条左边图标
	 * 
	 * @param resId
	 */
	public void setLeftIcon(int resId) {
		mLeftIcon.setImageResource(resId);
	}

	public void setLeftIcon(Drawable drawable) {
		mLeftIcon.setImageDrawable(drawable);
	}
	
	/**
	 * 设置导航条右边图标
	 * 
	 * @param resId
	 */
	public void setRightIcon(int resId) {
		
		mRightIcon.setImageResource(resId);
	}
	
	public void setRightIcon(Drawable drawable) {
		mRightIcon.setImageDrawable(drawable);
	}
	
	/**
	 * 设置导航条右边图标2
	 * 
	 * @param resId
	 */
	public void setRightIcon2(int resId) {
		
		mRightIcon2.setImageResource(resId);
	}
	
	public void setRightIcon2(Drawable drawable) {
		mRightIcon2.setImageDrawable(drawable);
	}
	
	/**
	 * 设置小标题
	 */
	public void setBottomTitle(String title) {
		if (Tools.isEmpty(title)) {
			mNavBottomTitle.setVisibility(View.GONE);
		} else {
			mNavBottomTitle.setVisibility(View.VISIBLE);
			mNavBottomTitle.setText(title);
		}
	}
	
	/**
	 * 设置小标题是否可见
	 */
	public void setBottomTitleVisibility(int visibility) {
		mNavBottomTitle.setVisibility(visibility);
	}

	/**
	 * 设置导航条文字内容
	 * 
	 * @param resId
	 */
	public void setTitle(int resId) {
		mNavTitle.setText(resId);
	}

	public void setTitle(String title) {
		mNavTitle.setText(title);
	}
	public void setTitleVisibility(int visibility) {
		mNavTitle.setVisibility(visibility);
	}
	
	public void setTabTitle1(String text) {
		mNavTabTitle1.setText(text);
	}
	
	public void setTabTitle2(String text) {
		mNavTabTitle2.setText(text);
	}
	
	public void setTabTitle1Color(int color) {
		mNavTabTitle1.setTextColor(color);
	}
	
	public void setTabTitle2Color(int color) {
		mNavTabTitle2.setTextColor(color);
	}
	
	public void setBorderTab1(String text) {
		mNavBorderTab1.setText(text);
	}
	
	public void setBorderTab2(String text) {
		mNavBorderTab2.setText(text);
	}
	
	/**
	 * 设置左边的文本
	 */
	public void setLeftText(int resId) {
		nav_left_tx.setText(resId);
	}
	
	/**
	 * 设置左边的文本
	 * @param text
	 */
	public void setLeftText(String text) {
		nav_left_tx.setText(text);
	}
	
	/**
	 * 设置右边的文本
	 */
	public void setRightText(int resId) {
		nav_right_tx.setText(resId);
	}
	
	/**
	 * 设置右边的文本
	 * @param text
	 */
	public void setRightText(String text) {
		nav_right_tx.setText(text);
	}

	public void setOnLeftIconClickListener(OnClickListener clickListener) {
		mLeftIcon.setOnClickListener(clickListener);
	}
	
	public void setOnRightIconClickListener(OnClickListener clickListener) {
		mRightIcon.setOnClickListener(clickListener);
	}
	
	public void setOnRightIcon2ClickListener(OnClickListener clickListener) {
		mRightIcon2.setOnClickListener(clickListener);
	}
	
	public void setOnLeftClickListener(OnClickListener clickListener) {
		nav_left_tx.setOnClickListener(clickListener);
	}
	
	public void setOnRightClickListener(OnClickListener clickListener) {
		nav_right_tx.setOnClickListener(clickListener);
	}
	
	public void setOnMsgIconClickListener(OnClickListener clickListener) {
		mMsgIcon.setOnClickListener(clickListener);
	}
	/**
	 * 设置导航条左边图标是否可见
	 */
	public void setLeftIconVisibility(int visibility) {
		mLeftIcon.setVisibility(visibility);
	}
	
	/**
	 * 设置导航条右边图标是否可见
	 */
	public void setRightIconVisibility(int visibility) {
		mRightIcon.setVisibility(visibility);
	}
	
	/**
	 * 设置导航条右边图标2是否可见
	 */
	public void setRightIcon2Visibility(int visibility) {
		mRightIcon2.setVisibility(visibility);
	}
	
	/**
	 * 设置导航条右边图标的红点容器是否可见
	 */
	public void setNavDotLayoutVisibility(boolean isVisibility) {
		nav_dot_layout.setVisibility(isVisibility?View.VISIBLE:View.GONE);
	}
	
	/**
	 * 设置导航条右边图标的红点是否可见
	 */
	public void setNavDotViewVisibility(boolean isVisibility) {
		nav_dot_view.setVisibility(isVisibility?View.VISIBLE:View.GONE);
	}
	
	/**
	 * 设置导航条左边文本是否可见
	 */
	public void setLeftVisibility(int visibility) {
		nav_left_tx.setVisibility(visibility);
	}
	
	/**
	 * 设置导航条右边文本是否可见
	 */
	public void setRightVisibility(int visibility) {
		nav_right_tx.setVisibility(visibility);
	}
	/**
	 * 设置导航栏消息图表是否可见
	 */
	public void setMsgIconVisibility(int visibility){
		mMsgIcon.setVisibility(visibility);
	}
	public TextView getNavLeftText() {
		return nav_left_tx;
	}
	
	public TextView getNavRightText() {
		return nav_right_tx;
	}

	public RelativeLayout getTop_layout() {
		return top_layout;
	}

	public void setTop_layout(RelativeLayout top_layout) {
		this.top_layout = top_layout;
	}
	
	public void setBackgroundColor(int color){
		navigation_root_ll.setBackgroundColor(color);
	}
	public void setBackgroundRes(int resid){
		navigation_root_ll.setBackgroundResource(resid);
	}
	public void setTitleViewVisibility(int visibility){
		mNavTitleView.setVisibility(visibility);
	}
	public void setTabTitleViewVisibility(int visibility){
		mNavTabTitleView.setVisibility(visibility);
	}
	public void setBorderTabViewVisibility(int visibility){
		mNavBorderTabView.setVisibility(visibility);
	}
	public void setSearchViewVisibility(int visibility){
		mSearchView.setVisibility(visibility);
	}
	
	public void setNavLineVisibility(int visibility) {
		mNavLine.setVisibility(visibility);
	}
	
	public NavigationConfig getNavigationConfig() {
		return navigationConfig;
	}

	public void setNavigationConfig(NavigationConfig navigationConfig) {
		this.navigationConfig = navigationConfig;
	}

	public void setOnSearchViewClickListener(OnClickListener onClickListener) {
		this.mSearchView.setOnClickListener(onClickListener);
		
	}

	public void setOnTabTitle1ClickListener(OnClickListener onClickListener) {
		mNavTabTitle1.setOnClickListener(onClickListener);
	}
	
	public void setOnTabTitle2ClickListener(OnClickListener onClickListener) {
		mNavTabTitle2.setOnClickListener(onClickListener);
	}
	
	public void setOnBorderTab1ClickListener(OnClickListener onClickListener) {
		mNavBorderTab1.setOnClickListener(onClickListener);
	}
	
	public void setOnBorderTab2ClickListener(OnClickListener onClickListener) {
		mNavBorderTab2.setOnClickListener(onClickListener);
	}
	
	/**
	 * 切换导航标题项
	 */
	public void switchTabTitle(int num) {
		if (num == 0) {
			mNavTabTitle1.setClickable(false);
			mNavTabTitle2.setClickable(true);
		} else if (num == 1) {
			mNavTabTitle1.setClickable(true);
			mNavTabTitle2.setClickable(false);
		}
	}

	/**
	 * 切换边框标题项
	 */
	public void switchBorderTab(int num) {
		if (num == 0) {
			mNavBorderTab1.setBackgroundResource(R.drawable.shape_border_2);
			mNavBorderTab2.setBackgroundDrawable(null);
			mNavBorderTab1.setClickable(false);
			mNavBorderTab2.setClickable(true);
		} else if (num == 1) {
			mNavBorderTab1.setBackgroundDrawable(null);
			mNavBorderTab2.setBackgroundResource(R.drawable.shape_border_2);
			mNavBorderTab1.setClickable(true);
			mNavBorderTab2.setClickable(false);
		}
	}
	
	/**
	 * 设置导航栏透明度
	 * @param alpha	0透明 1不透明
	 */
	public void setNavViewAlpha(float alpha) {
		mNavigationView.setAlpha(alpha);
	}
	
	/**
	 * 设置导航栏标题透明度
	 * @param alpha	0透明 1不透明
	 */
	public void setNavTitleViewAlpha(float alpha) {
		mNavTitleView.setAlpha(alpha);
	}
	
	/**
	 * 设置导航背景透明度
	 * @param alpha
	 */
	public void setBackgroundAlpha(float alpha) {
		int alphaColor = (int)(255 * alpha);
		setBackgroundColor(Color.argb(alphaColor, 255, 255, 255));
	}


}
