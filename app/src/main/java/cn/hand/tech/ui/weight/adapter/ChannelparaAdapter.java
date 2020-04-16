package cn.hand.tech.ui.weight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ui.weight.bean.ChannealBean;
import cn.hand.tech.utils.Tools;

/*
 * 
 * 通道 mv/v
 */
public class ChannelparaAdapter  extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<ChannealBean> mList;

	public ChannelparaAdapter(Context context, List<ChannealBean>list){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mList=list;
	}

	@Override
	public int getCount() {
		if (mList != null ){
			return mList.size();
		}else{
			return 0;
		}
	}

	public void setList(List<ChannealBean> list) {
		if (list != null) {
			this.mList = new ArrayList<ChannealBean>(list);
		} else {
			this.mList = new ArrayList<ChannealBean>();
		}
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHold viewHold = null;
		ChannealBean item = mList.get(position);
		if (convertView == null) {
			viewHold = new ViewHold();
			convertView =inflater.inflate(R.layout.frg_grid_item, null);
			viewHold.tv_1=(TextView)convertView.findViewById(R.id.tv_1);
			viewHold.tv_mvv=(TextView)convertView.findViewById(R.id.tv_mvv);
			viewHold.pass_1=(TextView)convertView.findViewById(R.id.pass_1);
			viewHold.mv_1=(TextView)convertView.findViewById(R.id.mv_1);
			viewHold.rl_2=(RelativeLayout)convertView.findViewById(R.id.rl_2);
			viewHold.line_2=(TextView)convertView.findViewById(R.id.line_2);
			convertView.setTag(viewHold);
		} else {
			viewHold = (ViewHold) convertView.getTag();
		}
		if(item != null ){
			viewHold.tv_1.setText(item.getPassName());

			viewHold.tv_mvv.setText(item.getMvv());
			String p1=item.getPassValue();
			String m1=item.getMvValue();
			String str1= Tools.getStringOrderPriceFormat(p1);
			String mv1=Tools.getStringOrderPriceFormat(m1);
			viewHold.pass_1.setText(str1);
			viewHold.mv_1.setText(mv1);
			int size= mList.size();
			if(size%2 != 0){//不是2的倍数
				if(position == size-1){//最后一个
					viewHold.rl_2.setVisibility(View.GONE);//占位
					viewHold.line_2.setVisibility(View.GONE);
				}else{
					viewHold.rl_2.setVisibility(View.GONE);
					viewHold.line_2.setVisibility(View.GONE);
				}
			}else{
				viewHold.rl_2.setVisibility(View.GONE);
				viewHold.line_2.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	class ViewHold {
		RelativeLayout rl_2;
		TextView line_2;
		TextView tv_1;
		TextView tv_mvv;
		TextView pass_1;
		TextView mv_1;
	}
}
