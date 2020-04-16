
package cn.hand.tech.weiget.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import cn.hand.tech.R;
import cn.hand.tech.log.DLog;


/**
 * 多列 瀑布流 ListView  可以刷新 
 */
public class MultiColumnPullToRefreshListView extends MultiColumnListView implements PLA_AbsListView.OnScrollListener {


	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private Scroller mScrollerFooter; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;

	// -- header view
	private XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private RelativeLayout mFooterViewContent;
	private TextView mHeaderTimeView;
	private TextView mHeaderTipView;
	private int mHeaderViewHeight; // header view's height
	private int mFooterLayoutHeight; // footer view's layout height
	private int mFooterReferHeight; // footer view's refer height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;
	private boolean mLoadMoreTip = false;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	public final static int SCROLLBACK_HEADER = 0;
	public final static int SCROLLBACK_FOOTER = 1;

	public final static int SCROLL_DURATION = 400; // scroll back duration
	public final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
														// at bottom, trigger
														// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.
													// public static Boolean
													// mFlag = true;

	/**
	 * @param context
	 */
	public MultiColumnPullToRefreshListView(Context context) {
		super(context);
		initWithContext(context, null);
	}

	public MultiColumnPullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context, attrs);
	}

	public MultiColumnPullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context, attrs);
	}

	private void initWithContext(Context context, AttributeSet attrs) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		mScrollerFooter = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
		mHeaderTipView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_tip);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new XListViewFooter(context);
		mFooterViewContent = (RelativeLayout) mFooterView.findViewById(R.id.xlistview_footer_content);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mHeaderViewHeight = mHeaderViewContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		
		// init footer height
		mFooterViewContent.measure(0, 0);
		mFooterLayoutHeight = mFooterViewContent.getMeasuredHeight();
		LOG("list_x", "initWithContext footerlayoutHeight:" + mFooterLayoutHeight);
		
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.XListViewAttrs);
		
		int pullType = mTypedArray.getInt(R.styleable.XListViewAttrs_pullType, 0);
		int loadType = mTypedArray.getInt(R.styleable.XListViewAttrs_loadType, 0);
		float loadMargin = mTypedArray.getDimension(R.styleable.XListViewAttrs_loadMargin, 0);
		
		mHeaderView.setPullType(pullType);
		mFooterView.setLoadType(loadType, mEnablePullLoad);
		mFooterView.setBottomMargin((int)loadMargin);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}
	
	public void setPullDown(boolean isPull) {
		mHeaderView.setPullDown(isPull);
	}
	
	public void setPullType(int type) {
		mHeaderView.setPullType(type);
	}
	
	public void setLoadType(int type) {
		mFooterView.setLoadType(type, mEnablePullLoad);
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
			
			resetFooterHeight();
		} else {
			mPullLoading = false;
			mLoadMoreTip = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL, 0);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
			
			resetFooterHeight();
		}
	}

	public void disFooterView(){
		mFooterView.showFinish();
		mFooterView.setOnClickListener(null);
		mLoadMoreTip = true;
	}
	
	/**
	 * 设置更多提示
	 * @param tip
	 */
	public void setLoadMoreTip(String tip) {
		mFooterView.showTip(tip);
		mFooterView.setOnClickListener(null);
		mLoadMoreTip = true;
	}
	
	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		setRefreshTip("");
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mLoadMoreTip = false;
			setPullLoadEnable(true);
		}
	}

	public void hideHeader() {
		mHeaderView.hide();
	}

	public void showHeader() {
		mHeaderView.show();
	}

	public void hideFooter() {
		mFooterView.hide();
	}

	public void showFooter() {
		mFooterView.show();
	}

	/**
	 * set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	/**
	 * 设置下拉提示
	 * @param tip
	 */
	public void setRefreshTip(String tip) {
		if (tip == null || "".equals(tip)) {
			mHeaderTipView.setText("");
			mHeaderTipView.setVisibility(View.GONE);
		} else {
			mHeaderTipView.setText(tip);
			mHeaderTipView.setVisibility(View.VISIBLE);
			mHeaderView.setState(XListViewHeader.STATE_TIP, 0);
		}
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}
	
	private void invokeOnScrollStateChanged(PLA_AbsListView view, int scrollState) {
		if ( mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onScrollStateChanged(view, scrollState);
		}
	}
	
	private void invokeOnScroll(PLA_AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY, mHeaderView.getVisiableHeight());
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL, mHeaderView.getVisiableHeight());
			}
		}
		setSelection(0); // scroll to top each time
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		LOG("list_x", "updateFooterHeight1 visiableHeight:" + mFooterView.getVisiableHeight() + " delta:" + delta);
		LOG("list_x", "updateFooterHeight2 footerReferHeight:" + mFooterReferHeight);
		
		int height = (int) delta + mFooterReferHeight;
		
		LOG("list_x", "updateFooterHeight3 height:" + height);
		if (!mEnablePullLoad || height >= mFooterLayoutHeight) {
			mFooterView.setHeight(height);
		}
		
		mFooterReferHeight = height;
		
		if (mEnablePullLoad && !mPullLoading && !mLoadMoreTip) { // 未处于刷新状态，更新箭头
			if (height > mFooterLayoutHeight) {
				
				mFooterView.setState(XListViewFooter.STATE_READY, mFooterReferHeight);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL, mFooterReferHeight);
			}
		}
		
		if (!mEnablePullLoad || height > mFooterLayoutHeight) {
			setSelection(mTotalItemCount - 1); // scroll to bottom
		}
		
		LOG("list_x", "updateFooterHeight setSelection");
	}

	private void resetFooterHeight() {
		int height = mFooterView.getVisiableHeight();
		LOG("list_x", "resetFooterHeight height:" + height);
		if (height == 0) // not visible.
			return;
		
		int finalHeight = 0;
		if (mEnablePullLoad) {
			finalHeight = mFooterLayoutHeight; // default: scroll back to dismiss header.
		}
		mScrollerFooter.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
		invalidate();
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING, mFooterLayoutHeight);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean ret = super.dispatchTouchEvent(ev);
		if (ret) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;
			}
		}
		return ret;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();

			if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getVisiableHeight() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up.
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight && !mPullRefreshing) {
					mPullRefreshing = true;
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING, mHeaderView.getVisiableHeight());
					if (mListViewListener != null) {
						mListViewListener.onRefresh();
					}
				}
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.
				if (mEnablePullLoad && mFooterReferHeight > mFooterLayoutHeight && !mPullLoading && !mLoadMoreTip) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
				LOG("list_x", "computeScroll header currY:" + mScroller.getCurrY());
			} else {
				if (mFooterReferHeight > mScroller.getCurrY()) {
					mFooterReferHeight = mScroller.getCurrY();
				}
				mFooterView.setHeight(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		if (mScrollerFooter.computeScrollOffset()) {
			if (mFooterReferHeight > mScrollerFooter.getCurrY()) {
				mFooterReferHeight = mScrollerFooter.getCurrY();
			}
			mFooterView.setHeight(mScrollerFooter.getCurrY());
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
		public void onScrollStateChanged(PLA_AbsListView view, int scrollState);
        public void onScroll(PLA_AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}

	public void setBackgroundColor(String color) {
		mFooterView.setBackgroundColor(Color.parseColor(color));
		mHeaderView.setBackgroundColor(Color.parseColor(color));
		this.setBackgroundColor(Color.parseColor(color));
	}

	public boolean isInBottom = false;
	//private Handler mHandler = new Handler();
	private Callback bottomCallback = null;

	public void toBottom() {
//		setSelection(getBottom());
//		mHandler.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
				setSelection(getBottom());
				isInBottom = true;
//			}
//		}, 200); // 等列表更新完再移到底部
	}

	public void toTop() {
//		setSelection(getTop());
//		mHandler.postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
				setSelection(getTop());
				isInBottom = false;
//			}
//		}, 200); // 等列表更新完再移到底部
	}

	public void setOnScrollToBottomLister(Callback c) {
		bottomCallback = c;
	}
	
	private boolean mIsLog = false;
	
	private void LOG(String tag, String msg) {
		if (mIsLog) {
			DLog.d(tag, msg);
		}
	}

	@Override
	public void onScrollStateChanged(PLA_AbsListView view, int scrollState) {
		LOG("list_x", "onScrollStateChanged scrollState:" + scrollState);
		if (mEnablePullLoad && !mPullLoading && !mLoadMoreTip) {
			if (scrollState == 0 && getLastVisiblePosition() == mTotalItemCount - 1) {
				startLoadMore();
			}
		}
		invokeOnScrollStateChanged(view, scrollState);
		
	}

	@Override
	public void onScroll(PLA_AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		LOG("list_x", "onScrollStateChanged firstVisibleItem:" + firstVisibleItem + " visibleItemCount:" + visibleItemCount + " totalItemCount:" + totalItemCount);
		// send to user's listener
		mTotalItemCount = totalItemCount;
		invokeOnScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

		if (firstVisibleItem + visibleItemCount >= totalItemCount - 2) {
			isInBottom = true;
			if (null != bottomCallback) {
				bottomCallback.handleMessage(new Message());
			}
			setStackFromBottom(false);
		} else {
			isInBottom = false;
		}		
	}
	
	public void showErrorTip(){
		mFooterView.showErrorTip();
		mLoadMoreTip = true;
	}
	

}