package cn.hand.tech.weiget.listview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.hand.tech.R;
import cn.hand.tech.weiget.RoundProgressBar;


public class XListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	
	private RoundProgressBar mRoundProgressBar;
	private ImageView mHuiImageView;
	private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	private Animation mRotateAnim;
	
	private final static int ROTATE_ANIM_DURATION = 180;
	
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
	public final static int STATE_TIP = 3;
	
	/**
	 * 聊天界面不是下拉方式
	 */
	private boolean mIsPullDown = true;
	
	
	public final static int PULL_TYPE_ARROW = 0;
	public final static int PULL_TYPE_HUI = 1;
	
	private int mPullType;

	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		//
		LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mArrowImageView = (ImageView)findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView)findViewById(R.id.xlistview_header_hint_textview);
		mProgressBar = (ProgressBar)findViewById(R.id.xlistview_header_progressbar);
		mRoundProgressBar = (RoundProgressBar)findViewById(R.id.xlistview_header_round_bar);
		mHuiImageView = (ImageView)findViewById(R.id.xlistview_header_hui);
		
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
		
		mRotateAnim = new RotateAnimation(0.0f, 360.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnim.setDuration(500);
		mRotateAnim.setFillAfter(true);
		mRotateAnim.setRepeatCount(-1);
		mRotateAnim.setInterpolator(new LinearInterpolator());
	}
	
	public void setPullDown(boolean isPull) {
		mIsPullDown = isPull;
		if (isPull) {
			setPullType(mPullType);
		} else {
			mProgressBar.clearAnimation();
			mProgressBar.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mHintTextView.setVisibility(View.GONE);
			
			mRoundProgressBar.clearAnimation();
			mRoundProgressBar.setVisibility(View.GONE);
			mHuiImageView.setVisibility(View.GONE);
		}
	}
	
	public void setPullType(int type) {
		mPullType = type;
		
		mProgressBar.clearAnimation();
		mProgressBar.setVisibility(View.INVISIBLE);
		if (mPullType == PULL_TYPE_HUI) {
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mHintTextView.setVisibility(View.GONE);
			
			mRoundProgressBar.setVisibility(View.VISIBLE);
			mHuiImageView.setVisibility(View.VISIBLE);
			
		} else {
			mArrowImageView.setVisibility(View.VISIBLE);
			mHintTextView.setVisibility(View.VISIBLE);
			
			mRoundProgressBar.clearAnimation();
			mRoundProgressBar.setVisibility(View.GONE);
			mHuiImageView.setVisibility(View.GONE);
		}
	}

	public void setState(int state, int progress) {
		if (mIsPullDown == false) {
			return;
		}
		
		if (mPullType == PULL_TYPE_HUI) {
			pullHui(state, progress);
		} else {
			pullArrow(state);
		}
		
		mState = state;
	}
	
	/**
	 * 箭头刷新
	 */
	private void pullArrow(int state) {
		if (state == STATE_TIP) {
			mProgressBar.setVisibility(View.INVISIBLE);
			return;
		}
		
		if (state == mState) return ;
		
		if (state == STATE_REFRESHING) {	// 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {	// 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch(state){
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setVisibility(View.VISIBLE);
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setVisibility(View.VISIBLE);
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			mHintTextView.setVisibility(View.GONE);
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			
			break;
			default:
		}
	}
	
	/**
	 * 惠字刷新
	 */
	private void pullHui(int state, int progress) {
		if (state == STATE_TIP) {
			mRoundProgressBar.clearAnimation();
			mRoundProgressBar.setVisibility(View.GONE);
			mHuiImageView.setVisibility(View.GONE);
			return;
		}
		mHuiImageView.setVisibility(View.VISIBLE);
		mRoundProgressBar.setVisibility(View.VISIBLE);
		
		if (state == STATE_REFRESHING) {
			mRoundProgressBar.setProgress(95);
		} else {
			progress -= 50;
			progress /= 1.68;
			if (progress > 0) {
				if (progress > 95) {
					progress = 95;
				}
				mRoundProgressBar.setProgress(progress);
			}
		}
		
		if (state == mState) return ;
		
		switch(state){
		case STATE_NORMAL:
			if (mState == STATE_REFRESHING) {
				mRoundProgressBar.clearAnimation();
			}
			mHintTextView.setVisibility(View.GONE);
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			break;
		case STATE_REFRESHING:
			mHintTextView.setVisibility(View.GONE);
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			
			mRoundProgressBar.startAnimation(mRotateAnim);
			
			break;
			default:
		}
	}
	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		mContainer.setVisibility(View.GONE);
	}
	/**
	 * show footer when disable pull load more
	 */
	public void show() {
		mContainer.setVisibility(View.VISIBLE);
	}
	public void setVisiableHeight(int height) {
		//DLog.d("list_x", "setVisiableHeight height:" + height);
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}
