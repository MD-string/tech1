package cn.hand.tech.ui.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ui.setting.bean.A1;

/**
 * Created by hxz on 2018/7/24.
 */
public class GuideAdapter extends BaseAdapter {
    private final onGuideListener mOnClickListener;
    private Context context;
    private LayoutInflater inflater;
    private List<A1> mlist;
    private int mnub =-1;


    public GuideAdapter(Context context, List<A1> list) {
        this.context = context;
        this.mlist = list;
        inflater = LayoutInflater.from(this.context);
        mOnClickListener = (onGuideListener) context;
    }
    public void updateListView(List<A1> list, int num) {
        this.mnub = num;
        this.mlist = list;
        notifyDataSetChanged();
    }
    public void setPostion(int num){
        this.mnub = num;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHold viewHold;
        final A1 item = mlist.get(position);
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView =inflater.inflate(R.layout.adapter_guide_item, null);
            viewHold.rl_1=(RelativeLayout)convertView.findViewById(R.id.rl_1);
            viewHold.tv_1=(TextView)convertView.findViewById(R.id.tv_1);
            viewHold.tv_tag=(TextView)convertView.findViewById(R.id.tv_tag);
            viewHold.img_next=(ImageView)convertView.findViewById(R.id.img_next);

            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        if(item != null ){
            int po=position+1;
            String number=po+".";
            viewHold.tv_1.setText(number+item.getStr());
            viewHold.rl_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        mOnClickListener.setOK(position);
                }
            });
            boolean isRep=item.isCheck();
            if(isRep){
                viewHold.tv_tag.setText("已维修");
                viewHold.tv_tag.setTextColor(context.getResources().getColor(R.color.rep_green));
                viewHold.img_next.setVisibility(View.GONE);
                viewHold.rl_1.setEnabled(false);
            }else{
                viewHold.tv_tag.setText("待维修");
                viewHold.tv_tag.setTextColor(context.getResources().getColor(R.color.rep_huang));
                viewHold.img_next.setVisibility(View.VISIBLE);
                viewHold.rl_1.setEnabled(true);
            }

        }
        return convertView;
    }

    class ViewHold {
        TextView tv_1;
        RelativeLayout rl_1;
        TextView tv_tag;
        ImageView img_next;
    }

    public interface onGuideListener {
        public void setOK(int num);
    }

}


