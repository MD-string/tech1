package cn.hand.tech.ui.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/* 注意事项: 
* 由于ListView和GridView都是可滑动的控件. 
* 所以需要自定义GridView,重写其onMeasure()方法. 
* 在该方法中使GridView的高为wrap_content的大小,否则GridView中 
* 的内容只能显示很小一部分 
*/

public class MyGridView  extends GridView{
	
	public MyGridView(Context context){
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
