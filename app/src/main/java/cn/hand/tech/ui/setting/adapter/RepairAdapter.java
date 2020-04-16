package cn.hand.tech.ui.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ui.setting.bean.A1;

/**
 * Created by hxz on 2018/7/24.
 */
public class RepairAdapter extends BaseAdapter {
    private final onNextListener mOnClickListener;
    private Context context;
    private LayoutInflater inflater;
    private List<A1> mlist=new ArrayList<>();
    private int mnub =0;
    private String mFirst="0";
    private int mCurrentItem =-1;

    public RepairAdapter(Context context, List<A1> list, int nub, String first) {
        this.context = context;
        this.mlist = list;
        this.mnub=nub;
        this.mFirst=first;
        inflater = LayoutInflater.from(this.context);
        mOnClickListener = (onNextListener) context;
    }
    public void updateListView(List<A1> list) {
        this.mlist = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mlist !=null && mlist.size() >0){

            return mlist.size();
        }else {
            return 0;
        }
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
            convertView =inflater.inflate(R.layout.adapter_repair_item, null);
            viewHold.rl_1=(RelativeLayout)convertView.findViewById(R.id.rl_1);
            viewHold.tv_1=(TextView)convertView.findViewById(R.id.tv_1);
            viewHold.img_1=(ImageView)convertView.findViewById(R.id.img_1);
            viewHold.ck_1=(CheckBox)convertView.findViewById(R.id.ck_1);

            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        if(item != null ){
            if(mnub ==0){
                viewHold.tv_1.setText(item.getStr());
                viewHold.tv_1.setVisibility(View.VISIBLE);
                viewHold.img_1.setVisibility(View.VISIBLE);
                viewHold.ck_1.setVisibility(View.GONE);
                viewHold.rl_1.setClickable(true);
                viewHold.rl_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if("2".equals(mFirst) ){
                            mOnClickListener.setNextSecond(position);
                        }else{
                            mOnClickListener.setNext(position);
                        }
                    }
                });
            }else{
                viewHold.tv_1.setVisibility(View.GONE);
                viewHold.img_1.setVisibility(View.GONE);
                viewHold.ck_1.setText(item.getStr());
                viewHold.ck_1.setVisibility(View.VISIBLE);
                viewHold.rl_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(viewHold.ck_1.isChecked()){
                            viewHold.ck_1.setChecked(false);
                            mOnClickListener.setCancle(position,mFirst);
                            viewHold.ck_1.setTextColor(context.getResources().getColor(R.color.black));
                        }else{
                            viewHold.ck_1.setChecked(true);
                            mOnClickListener.setOK(position,mFirst);
                            viewHold.ck_1.setTextColor(context.getResources().getColor(R.color.color_yellow));
                        }
                    }
                });
                if(viewHold.ck_1.isChecked()){
                    viewHold.ck_1.setTextColor(context.getResources().getColor(R.color.color_yellow));
                }else{
                    viewHold.ck_1.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
            if(mCurrentItem==position){
                viewHold.tv_1.setTextColor(context.getResources().getColor(R.color.color_yellow));
                viewHold.rl_1.setBackgroundColor(context.getResources().getColor(R.color.lightGray2));
            }else{
                viewHold.tv_1.setTextColor(context.getResources().getColor(R.color.black));
                viewHold.rl_1.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            boolean isChecked=item.isCheck();
            if(isChecked){
                viewHold.ck_1.setChecked(true);
                viewHold.ck_1.setTextColor(context.getResources().getColor(R.color.color_yellow));
            }else{
                viewHold.ck_1.setChecked(false);
                viewHold.ck_1.setTextColor(context.getResources().getColor(R.color.black));
            }
        }
        return convertView;
    }

    class ViewHold {
        RelativeLayout rl_1;
        CheckBox ck_1;
        TextView tv_1;
        ImageView img_1;
    }
    public void setCurrentItem(int currentItem) {
        this.mCurrentItem = currentItem;
        notifyDataSetChanged();
    }

    public interface onNextListener {
        public void setOK(int num,String mFirst);
        public void setCancle(int num,String mFirst);
        public void setNext(int num);
        public void setNextSecond(int num);
    }

}


