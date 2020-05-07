package cn.hand.tech.ui.setting.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ui.setting.TruckNeedRepairActivity;
import cn.hand.tech.ui.weight.bean.CompanyTruckGroupBean;
import cn.hand.tech.ui.weight.bean.TruckChildBean;
import cn.hand.tech.utils.CommonKitUtil;


/**
 * Created by wcf on 2018-03-08.
 * describe:公司及子条目车辆适配器
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class CompanyTruckForRepairAdapter extends BaseExpandableListAdapter {
     private Context mContext;
    private List<CompanyTruckGroupBean> mList;

    public CompanyTruckForRepairAdapter(Context mContext, List<CompanyTruckGroupBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    public void updateListView(List<CompanyTruckGroupBean> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        int size=mList.size();
        if(i >= size){
             return mList.get(size-1).getChildren().size();
        }else{
            List<TruckChildBean> list= mList.get(i).getChildren();
            if(list !=null && list.size() >0){

                return mList.get(i).getChildren().size();
            }else{
                return  0;
            }
        }
    }

    @Override
    public Object getGroup(int i) {
        return mList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mList.get(i).getChildren().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {
        GroupHolder holder;
        if (view == null) {
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lvitem_company_1, null);
            holder.rl_mian=(RelativeLayout)view.findViewById(R.id.rl_mian);
            holder.tv_company = (TextView) view.findViewById(R.id.tv_company);
            holder.iv_truckInfo_arrow = (ImageView ) view.findViewById(R.id.iv_truckInfo_arrow);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }
       final CompanyTruckGroupBean cb1=mList.get(i);
        holder.tv_company.setText(cb1.getName());
        List<TruckChildBean> listb=cb1.getChildren();
        if(listb !=null && listb.size() >0){
            holder.iv_truckInfo_arrow.setVisibility(View.VISIBLE);
            holder.rl_mian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else{
            holder.iv_truckInfo_arrow.setVisibility(View.GONE);
            holder.rl_mian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i=new Intent(mContext, AddTruckBasicActivity.class);
//                    i.putExtra("truckModel",cb1.getName());
//                    i.putExtra("truck_id",cb1.getId());
//                    mContext.startActivity(i);

                    TruckChildBean bean=new TruckChildBean();
                    bean.setChildId(cb1.getId());
                    bean.setName(cb1.getName());
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("truckModel",(Serializable)bean);
                    //                CommonKitUtil.startActivity((Activity) mContext, WeightTrendActivity.class, bundle, false);
                    CommonKitUtil.startActivity((Activity) mContext, TruckNeedRepairActivity.class, bundle, false);
                }
            });
        }
        if(isExpanded){
            holder.iv_truckInfo_arrow.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_arrow_bto));
        }else{
            holder.iv_truckInfo_arrow.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.screen_arrow));
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lvitem_truck, null);
            holder.rl_1=(RelativeLayout)view.findViewById(R.id.rl_1);
            holder.tv_plate_number = (TextView) view.findViewById(R.id.tv_plate_number);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
       final TruckChildBean cb = mList.get(i).getChildren().get(i1);
        holder.tv_plate_number.setText(cb.getName());
        holder.rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TruckChildBean bean1=new TruckChildBean();
                bean1.setChildId(cb.getChildId());
                bean1.setName(cb.getName());
                Bundle bundle=new Bundle();
                bundle.putSerializable("truckModel",(Serializable)bean1);
                //                CommonKitUtil.startActivity((Activity) mContext, WeightTrendActivity.class, bundle, false);
                CommonKitUtil.startActivity((Activity) mContext, TruckNeedRepairActivity.class, bundle, false);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {


        return true;
    }

    class GroupHolder {
        TextView tv_company;
        RelativeLayout rl_mian;
        ImageView iv_truckInfo_arrow;
    }

    class ChildHolder {
        TextView tv_plate_number;
        RelativeLayout rl_1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String  sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }
}
