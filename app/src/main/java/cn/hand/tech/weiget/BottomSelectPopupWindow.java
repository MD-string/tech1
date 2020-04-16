package cn.hand.tech.weiget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
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

import cn.hand.tech.R;
import cn.hand.tech.common.OnMyItemClickListener;
import cn.hand.tech.utils.CommonUtils;


public class BottomSelectPopupWindow extends PopupWindow {

	private boolean mIsDismiss;
	
	private LinearLayout mContent;
	private View contentView;
	private OnMyItemClickListener miItemClickListener;
	
	String mTitle;
	
	String[] mItems;

	public BottomSelectPopupWindow(Context context, String[] items,String title) {
		super(context);
		this.mTitle = title;
		init(context, items);
		
	}
	public BottomSelectPopupWindow(Context context, String[] items) {
		init(context, items);
	}
	private void init(Context context, String[] items){
		mItems = items;
		
		LinearLayout pContent = new LinearLayout(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		pContent.setLayoutParams(lp);
		pContent.setGravity(Gravity.BOTTOM);
		
		
		mContent = new LinearLayout(context);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mContent.setLayoutParams(lp);
		mContent.setOrientation(LinearLayout.VERTICAL);
		mContent.setBackgroundColor(Color.WHITE);
		
		pContent.addView(mContent);
		super.setContentView(pContent);
		if(!TextUtils.isEmpty(mTitle)){
			int padding = CommonUtils.dip2px(context, 15);
			TextView tv_title = new TextView(context);
			tv_title.setText(mTitle);
			tv_title.setGravity(Gravity.CENTER);
			tv_title.setTextSize(12);
			tv_title.setTextColor(context.getResources().getColor(R.color.gg_6c));
			tv_title.setPadding(0, padding, 0, padding);
			tv_title.setClickable(true);
			mContent.addView(tv_title);
			
		}
		for (int i = 0; i < mItems.length; i++) {
			final int position = i;
			contentView = LayoutInflater.from(context).inflate(R.layout.bottom_selecte_item, null);
			contentView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (miItemClickListener != null) {
						miItemClickListener.onItemClick(mContent, contentView, position);
					}
					dismiss();
				}
			});
			
			TextView tv_item = (TextView) contentView.findViewById(R.id.tv_item);
			tv_item.setText(mItems[i]);
			if(!TextUtils.isEmpty(mTitle)){//如果有标题
				tv_item.setTextColor(context.getResources().getColor(R.color.gg_6c));
			}else{
				if (i == 0) {
					View v_line = contentView.findViewById(R.id.v_line);
					v_line.setVisibility(View.GONE);
				}
			}
			mContent.addView(contentView);
		}
		// 取消按钮
		contentView = LayoutInflater.from(context).inflate(R.layout.bottom_selecte_item, null);
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		TextView tv_item = (TextView) contentView.findViewById(R.id.tv_item);
		tv_item.setText("取消");
		
		mContent.addView(contentView);
		
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(mContent.getResources().getColor(R.color.col_645));
		this.setBackgroundDrawable(dw);
		setOutsideTouchable(true);
		pContent.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dismiss();
			}
		});
	}
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		mContent.startAnimation(AnimationUtils.loadAnimation(mContent.getContext(), R.anim.push_bottom_in));
		super.showAtLocation(parent, gravity, x, y);
	}

	public void dismiss() {
		if (!mIsDismiss) {
			mIsDismiss = true;

			Animation ou = AnimationUtils.loadAnimation(mContent.getContext(), R.anim.push_bottom_out);
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
					BottomSelectPopupWindow.super.dismiss();
					mIsDismiss = false;
				}
			});
			mContent.startAnimation(ou);
		}
	}
	
	public void setOnItemClickListener(OnMyItemClickListener ilClickListener) {
		miItemClickListener = ilClickListener;
	}

}
