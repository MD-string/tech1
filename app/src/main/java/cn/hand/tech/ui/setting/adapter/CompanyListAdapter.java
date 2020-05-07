package cn.hand.tech.ui.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ui.weight.bean.CompanyTruckGroupBean;
import cn.hand.tech.ui.weight.bean.TruckChildBean;


/**
 * Created by wcf on 2018-03-08.
 * describe:公司及子条目车辆适配器
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class CompanyListAdapter extends BaseExpandableListAdapter {
     private Context mContext;
    private List<CompanyTruckGroupBean> mList;

    public CompanyListAdapter(Context mContext, List<CompanyTruckGroupBean> mList) {
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
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        GroupHolder holder;
        if (view == null) {
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lvitem_company_2, null);
            holder.rl_mian=(RelativeLayout)view.findViewById(R.id.rl_mian);
            holder.tv_company = (TextView) view.findViewById(R.id.tv_company);
            holder.iv_truckInfo_arrow = (ImageView ) view.findViewById(R.id.iv_truckInfo_arrow);
            holder.comListAct_comListAdp_checkbox = (CheckBox) view.findViewById(R.id.comListAct_comListAdp_checkbox);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }
       final CompanyTruckGroupBean cb1=mList.get(groupPosition);
        holder.tv_company.setText(cb1.getName());

        holder.comListAct_comListAdp_checkbox.setChecked(cb1.isSelectedOr());
        holder.comListAct_comListAdp_checkbox.setFocusable(false);
        holder.comListAct_comListAdp_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb1.setSelectedOr(true);
                }else{
                    cb1.setSelectedOr(false);
                }

                // 将 Children 的 isChecked 全部设置跟 Group一样
                int childrenCount = cb1.getChildren().size();
                boolean groupIsChecked = cb1.isSelectedOr();
                for (int i = 0; i < childrenCount; i++)
                    cb1.getChildren().get(i).setSelectedOrChrend(groupIsChecked);

                // 注意，一定要通知 ExpandableListView 数据已经改变，
                //ExpandableListView 会重新绘制
                notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lvitem_truck_1, null);
            holder.rl_1=(RelativeLayout)view.findViewById(R.id.rl_1);
            holder.tv_plate_number = (TextView) view.findViewById(R.id.tv_plate_number);
            holder.comListAct_comListAdp_checkbox_child = (CheckBox) view.findViewById(R.id.comListAct_comListAdp_checkbox_child);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
       final TruckChildBean cb = mList.get(groupPosition).getChildren().get(i1);
        holder.tv_plate_number.setText(cb.getName());
        holder.comListAct_comListAdp_checkbox_child.setChecked(cb.isSelectedOrChrend());
        holder.comListAct_comListAdp_checkbox_child.setFocusable(false);
        holder.comListAct_comListAdp_checkbox_child.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb.setSelectedOrChrend(true);
                }else{
                    cb.setSelectedOrChrend(false);
                }
            }
        });
        return view;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int i, int i1) {
//        DLog.e("isChildSelectable","isChildSelectable==>"+i+"/"+i1);

        return true;
    }

    class GroupHolder {
        TextView tv_company;
        RelativeLayout rl_mian;
        ImageView iv_truckInfo_arrow;
        CheckBox comListAct_comListAdp_checkbox;
    }

    class ChildHolder {
        TextView tv_plate_number;
        RelativeLayout rl_1;
        CheckBox comListAct_comListAdp_checkbox_child;
    }



}
