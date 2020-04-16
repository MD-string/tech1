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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.OnTwoClickListener;
import cn.hand.tech.weiget.wheelView.OnWheelChangedListener;
import cn.hand.tech.weiget.wheelView.OnWheelClickedListener;
import cn.hand.tech.weiget.wheelView.OnWheelScrollListener;
import cn.hand.tech.weiget.wheelView.WheelView;
import cn.hand.tech.weiget.wheelView.adapter.StringWheelAdapter;

/**
 * 年月2级  选择器
 * @author PAKITE
 *
 */

public class TwoWheelSelectPopupWindow extends PopupWindow {

	private View mSelectView;
	private LinearLayout pop_layout;
	private TextView tv_cancel;
	private TextView tv_ok;
	private WheelView wv_select;
	String[] shui = { "散装水泥罐车", "袋装水泥汽车", "混凝土搅拌车","袋装平板挂车","其他" };
    String[] huan = { "侧挂式垃圾车", "压缩式垃圾车", "勾臂车垃圾车", "摆臂式垃圾车","餐厨式垃圾车","尾板垃圾车[4.2M]","尾板垃圾车[4.2M不封顶]","尾板垃圾车[8桶]","三轮垃圾车","其他" };
    String[] xiang = { "箱式货车[4.2M]", "箱式货车[7.6M]", "箱式货车[9.6M]", "依维柯","其他" };
    String[] zha = { "碴土车[2轴]", "碴土车[3轴]", "碴土车[4轴]", "其他" };
    String[] gua = { "箱式挂车", "平板挂车", "其他" };
    String[] qta = { "其他" };
    final List<String> shList = Arrays.asList(shui);
    final List<String> huanList = Arrays.asList(huan);
    final List<String> xList = Arrays.asList(xiang);
    final List<String> zhaList = Arrays.asList(zha);
    final List<String> guaList = Arrays.asList(gua);
    final List<String> qlist = Arrays.asList(qta);
	private boolean mIsDismiss;
	private WheelView mouth_select;
	 private static int START_YEAR = 1917, END_YEAR = 2004;
	 Context mContext;
	 int parentNo=0;
    int  childNo=0;

	public TwoWheelSelectPopupWindow(Activity context, final OnTwoClickListener okClick) {
		super(context);
		mContext=context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSelectView = inflater.inflate(R.layout.layout_wheel_select_two, null);
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
					okClick.onClick(v,parentNo,childNo);
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

		List<String>oneList=new ArrayList<>();
		oneList.add("水泥车");
		oneList.add("环卫车");
		oneList.add("厢式货车");
		oneList.add("碴土车");
		oneList.add("挂车");
		oneList.add("其他");
		wv_select = (WheelView) mSelectView.findViewById(R.id.year_select);
		wv_select.setCyclic(false);
		wv_select.setViewAdapter(new StringWheelAdapter(context, 0,10,"",oneList));//设置"年"的显示数据
		wv_select.setVisibleItems(6);
		wv_select.addChangingListener(wheelListener_year);


		mouth_select = (WheelView) mSelectView.findViewById(R.id.mouth_select);
		mouth_select.setCyclic(false);
		mouth_select.setViewAdapter(new StringWheelAdapter(context, 0,10,"",shList));
		mouth_select.setVisibleItems(6);
		
		mouth_select.addChangingListener(wheelListener_month);
		
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
            parentNo=newValue;
			childNo=0;
            if(1==newValue){
                mouth_select.setViewAdapter(new StringWheelAdapter(mContext, 0,10,"",huanList));
				mouth_select.setCurrentItem(childNo);
            }else if(2==newValue){
                mouth_select.setViewAdapter(new StringWheelAdapter(mContext, 0,10,"",xList));
				mouth_select.setCurrentItem(childNo);
            }else if(3==newValue){
                mouth_select.setViewAdapter(new StringWheelAdapter(mContext, 0,10,"",zhaList));
				mouth_select.setCurrentItem(childNo);
            }else if(4==newValue){
                mouth_select.setViewAdapter(new StringWheelAdapter(mContext, 0,10,"",guaList));
				mouth_select.setCurrentItem(childNo);
            }else if(5==newValue){
                mouth_select.setViewAdapter(new StringWheelAdapter(mContext, 0,10,"",qlist));
				mouth_select.setCurrentItem(childNo);
            }else {
                mouth_select.setViewAdapter(new StringWheelAdapter(mContext, 0,10,"",shList));
				mouth_select.setCurrentItem(childNo);
            }
        }  
    };  
    
 // 添加"月"监听  
    private final OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {  
        @Override  
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            childNo=newValue;
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
					TwoWheelSelectPopupWindow.super.dismiss();
					mIsDismiss = false;
				}
			});
			pop_layout.startAnimation(ou);
		}
	}

}
