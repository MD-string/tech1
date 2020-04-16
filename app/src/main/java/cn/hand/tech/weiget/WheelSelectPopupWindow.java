package cn.hand.tech.weiget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.weiget.wheelView.OnWheelChangedListener;
import cn.hand.tech.weiget.wheelView.OnWheelClickedListener;
import cn.hand.tech.weiget.wheelView.OnWheelScrollListener;
import cn.hand.tech.weiget.wheelView.WheelView;
import cn.hand.tech.weiget.wheelView.adapter.NumericWheelAdapter;

/**
 * 年月日三级  选择器
 * @author PAKITE
 *
 */

public class WheelSelectPopupWindow extends PopupWindow {

	private View mSelectView;
	private LinearLayout pop_layout;
	private TextView tv_cancel;
	private TextView tv_ok;
	private WheelView wv_select;
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };  
    String[] months_little = { "4", "6", "9", "11" }; 
    final List<String> list_big = Arrays.asList(months_big);  
    final List<String> list_little = Arrays.asList(months_little);  
	private boolean mIsDismiss;
	private WheelView mouth_select;
	 private static int START_YEAR = 1917, END_YEAR = 2004;
	 Context mContext;

	public WheelSelectPopupWindow(Activity context, final OnClickListener okClick) {
		super(context);
		mContext=context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSelectView = inflater.inflate(R.layout.layout_wheel_select, null);
		pop_layout = (LinearLayout) mSelectView.findViewById(R.id.pop_layout);
		tv_cancel = (TextView) mSelectView.findViewById(R.id.tv_cancel);
		tv_ok = (TextView) mSelectView.findViewById(R.id.tv_ok);
		// 取消按钮
		tv_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dismiss();
			}
		});
		tv_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (okClick != null) {
					okClick.onClick(v);
				}
			}
		});
		this.setContentView(mSelectView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		setOutsideTouchable(true);
		
		mSelectView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				dismiss();
			}
		});

		wv_select = (WheelView) mSelectView.findViewById(R.id.year_select);
		wv_select.setCyclic(false);
//		wv_select.addScrollingListener(mWheelScrollListener);
		wv_select.setViewAdapter(new NumericWheelAdapter(context, START_YEAR,END_YEAR));//设置"年"的显示数据
		wv_select.setVisibleItems(7);
//		wv_select.addClickingListener(mWheelClickedListener);
		wv_select.addChangingListener(wheelListener_year);
		
		
		mouth_select = (WheelView) mSelectView.findViewById(R.id.mouth_select);
		mouth_select.setCyclic(false);
//		mouth_select.addScrollingListener(mWheelScrollListener);
		mouth_select.setViewAdapter(new NumericWheelAdapter(context,1,12));
		mouth_select.setVisibleItems(7);
		
//		mouth_select.addClickingListener(mWheelClickedListener);
		mouth_select.addChangingListener(wheelListener_month);
		
//		day_select = (WheelView) mSelectView.findViewById(R.id.day_select);
//		day_select.setCyclic(false);
////		day_select.addScrollingListener(mWheelScrollListener);
//		day_select.setVisibleItems(7);

//		day_select.addClickingListener(mWheelClickedListener);
		setDate();
	}


    // 添加"年"监听  
    private final OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
        @Override  
        public void onChanged(WheelView wheel, int oldValue, int newValue) {  
//            int year_num = newValue + START_YEAR;
//            // 判断大小月及是否闰年,用来确定"日"的数据
//            if (list_big  .contains(String.valueOf(mouth_select.getCurrentItem() + 1))) {
//            	day_select.setViewAdapter(new NumericWheelAdapter(mContext,1,31));
//            } else if (list_little.contains(String.valueOf(mouth_select
//                    .getCurrentItem() + 1))) {
//            	day_select.setViewAdapter(new NumericWheelAdapter(mContext,1,30));
//            } else {
//                if ((year_num % 4 == 0 && year_num % 100 != 0)
//                        || year_num % 400 == 0)
//                	day_select.setViewAdapter(new NumericWheelAdapter(mContext,1,29));
//                else
//                	day_select.setViewAdapter(new NumericWheelAdapter(mContext,1,28));
//            }
        }  
    };  
    
 // 添加"月"监听  
    private final OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {  
        @Override  
        public void onChanged(WheelView wheel, int oldValue, int newValue) {  
//            int month_num = newValue + 1;
//            // 判断大小月及是否闰年,用来确定"日"的数据
//            if (list_big.contains(String.valueOf(month_num))) {
//            	day_select.setViewAdapter(new NumericWheelAdapter(mContext,1,31));
//            } else if (list_little.contains(String.valueOf(month_num))) {
//            	day_select.setViewAdapter(new NumericWheelAdapter(mContext,1,30));
//            } else {
//                if (((wv_select.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_select
//                        .getCurrentItem() + START_YEAR) % 100 != 0)
//                        || (wv_select.getCurrentItem() + START_YEAR) % 400 == 0)
//                	day_select.setViewAdapter(new NumericWheelAdapter(mContext,1,29));
//                else
//                	day_select.setViewAdapter(new NumericWheelAdapter(mContext,1,28));
//            }
        }  
    };  
    //初始化 当前显示
    public void setDate() {  

    } 


	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		pop_layout.startAnimation(AnimationUtils.loadAnimation(mSelectView.getContext(), R.anim.push_bottom_in));
		super.showAtLocation(parent, gravity, x, y);
	}

	OnWheelClickedListener mWheelClickedListener = new OnWheelClickedListener() {
		
		@Override
		public void onItemClicked(WheelView wheel, int itemIndex) {
			wheel.setCurrentItem(itemIndex, true);
			
		}
	};
	OnWheelScrollListener mWheelScrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
		}
	};

	public void dismiss() {
		if (!mIsDismiss) {
			mIsDismiss = true;

			Animation ou = AnimationUtils.loadAnimation(mSelectView.getContext(), R.anim.push_bottom_out);
			ou.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					WheelSelectPopupWindow.super.dismiss();
					mIsDismiss = false;
				}
			});
			pop_layout.startAnimation(ou);
		}
	}

}
