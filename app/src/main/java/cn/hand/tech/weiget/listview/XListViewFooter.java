/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package cn.hand.tech.weiget.listview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.hand.tech.R;
import cn.hand.tech.weiget.RoundProgressBar;


public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	
	public final static int LOAD_TYPE_PROGRESS_BAR = 0;
	public final static int LOAD_TYPE_HUI = 1;
	
	private int mLoadType;

	private Context mContext;

	private View mContainer;
	private View mProgressBar;
	private TextView mHintView;
	
	private View mErrorTextview;
	private RoundProgressBar mRoundProgressBar;
	private ImageView mHuiImageView;
	
	private Animation mRotateAnim;
	
	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}
	
	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	
	public void setLoadType(int type, boolean enablePullLoad) {
		mLoadType = type;
		
		if (!enablePullLoad) {
			return;
		}
//		mProgressBar.clearAnimation();
		mProgressBar.setVisibility(View.INVISIBLE);
		mErrorTextview.setVisibility(View.GONE);
		if (mLoadType == LOAD_TYPE_HUI) {
			mHintView.setVisibility(View.GONE);
			
			mRoundProgressBar.setVisibility(View.VISIBLE);
			mHuiImageView.setVisibility(View.VISIBLE);
			
		} else {
			mHintView.setVisibility(View.VISIBLE);
			
			mRoundProgressBar.clearAnimation();
			mRoundProgressBar.setVisibility(View.GONE);
			mHuiImageView.setVisibility(View.GONE);
		}
	}

	public void setState(int state, int progress) {
		if (mLoadType == LOAD_TYPE_HUI) {
			loadHui(state, progress);
		} else {
			loadProgressBar(state);
		}
	}
	
	/**
	 * 菊花刷新
	 */
	private void loadProgressBar(int state) {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.INVISIBLE);
		
		if (state == STATE_READY) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_ready);
		} else if (state == STATE_LOADING) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_normal);
		}
	}
	
	/**
	 * 惠字刷新
	 */
	private void loadHui(int state, int progress) {
		mHuiImageView.setVisibility(View.VISIBLE);
		mRoundProgressBar.setVisibility(View.VISIBLE);
		
		mErrorTextview.setVisibility(View.GONE);
		
		if (state == STATE_LOADING) {
			mRoundProgressBar.setProgress(75);
		} else {
			progress -= 90;
			
			progress /= 1.365;
			if (progress > 0) {
				if (progress > 75) {
					progress = 75;
				}
				mRoundProgressBar.setProgress(progress);
			}
		}
		
		if (state == STATE_READY) {
			mRoundProgressBar.clearAnimation();
		} else if (state == STATE_LOADING) {
			mRoundProgressBar.startAnimation(mRotateAnim);
		} else {
			mRoundProgressBar.clearAnimation();
			mHintView.setVisibility(View.GONE);
		}
	}
	
	public void setHeight(int height) {
		//DLog.d("list_x", "setHeight height:" + height);
		if (height < 0) return ;
		LayoutParams lp = (LayoutParams)mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}
	
	public void setBottomMargin(int margin) {
		//DLog.d("list_x", "setBottomMargin height:" + height);
		LayoutParams lp = (LayoutParams)mContainer.getLayoutParams();
		lp.bottomMargin = margin;
		mContainer.setLayoutParams(lp);
	}
	
	public int getBottomMargin() {
		LayoutParams lp = (LayoutParams)mContainer.getLayoutParams();
		return lp.bottomMargin;
	}

	public void showFinish(){
		showTip(getContext().getResources().getString(R.string.xlistview_footer_hint_finish));
	}
	
	public void showErrorTip(){
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.INVISIBLE);
		mHuiImageView.setVisibility(View.GONE);
		mRoundProgressBar.clearAnimation();
		mRoundProgressBar.setVisibility(View.GONE);
		
		mErrorTextview.setVisibility(View.VISIBLE);
	}
	
	public void showTip(String tip){
		mHintView.setVisibility(View.VISIBLE);
		mHintView.setText(tip);
		
		mProgressBar.setVisibility(View.INVISIBLE);
		mHuiImageView.setVisibility(View.GONE);
		mRoundProgressBar.clearAnimation();
		mRoundProgressBar.setVisibility(View.GONE);
		mErrorTextview.setVisibility(View.GONE);
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}
	
	
	/**
	 * loading status 
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LayoutParams lp = (LayoutParams)mContainer.getLayoutParams();
		lp.height = 0;
		mContainer.setLayoutParams(lp);
		
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.INVISIBLE);
		mHuiImageView.setVisibility(View.GONE);
		mRoundProgressBar.clearAnimation();
		mRoundProgressBar.setVisibility(View.GONE);
		mErrorTextview.setVisibility(View.GONE);
	}
	
	/**
	 * show footer
	 */
	public void show() {
		LayoutParams lp = (LayoutParams)mContainer.getLayoutParams();
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		mContainer.setLayoutParams(lp);
	}
	
	private void initView(Context context) {
		mContext = context;
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mContainer = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		addView(mContainer, lp);
		
		mProgressBar = mContainer.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView)mContainer.findViewById(R.id.xlistview_footer_hint_textview);
		mErrorTextview = mContainer.findViewById(R.id.xlistview_footer_error_textview);
		
		mRoundProgressBar = (RoundProgressBar)findViewById(R.id.xlistview_footer_round_bar);
		mHuiImageView = (ImageView)findViewById(R.id.xlistview_footer_hui);
		
		mRotateAnim = new RotateAnimation(0.0f, 360.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnim.setDuration(500);
		mRotateAnim.setFillAfter(true);
		mRotateAnim.setRepeatCount(-1);
		mRotateAnim.setInterpolator(new LinearInterpolator());
	}
	
	public int getVisiableHeight() {
		return mContainer.getHeight();
	}
	
	
}
