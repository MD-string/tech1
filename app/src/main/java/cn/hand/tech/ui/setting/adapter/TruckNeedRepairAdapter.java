package cn.hand.tech.ui.setting.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ui.setting.RepairAct;
import cn.hand.tech.ui.setting.bean.CarListModel;
import cn.hand.tech.ui.setting.bean.RepairListModel;
import cn.hand.tech.ui.setting.bean.RepairModel;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.Tools;


/**
 * Created by wcf on 2018-03-08.
 * describe:公司及子条目车辆适配器
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class TruckNeedRepairAdapter extends BaseExpandableListAdapter {
    private final onPhoneLisenter mPhoneLisenter;
    private Context mContext;
    private List<RepairListModel> mList;
    DecimalFormat df = new DecimalFormat("#.00");

    public TruckNeedRepairAdapter(Context mContext, List<RepairListModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mPhoneLisenter = (onPhoneLisenter) mContext;
    }
    public void updateListView(List<RepairListModel> list){
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
            List<CarListModel> list= mList.get(i).getChildren();
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
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
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
       final RepairListModel cb1=mList.get(i);
        holder.tv_company.setText(cb1.getName());
        List<CarListModel> listb=cb1.getChildren();
        if(listb !=null && listb.size() >0){
            holder.iv_truckInfo_arrow.setVisibility(View.VISIBLE);
        }else{
            holder.iv_truckInfo_arrow.setVisibility(View.GONE);
            holder.rl_mian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view == null) {
            holder = new ChildHolder();
            view =LayoutInflater.from(mContext).inflate(R.layout.adapter_chang_repair, null);
            holder.tv_car_num=(TextView)view.findViewById(R.id.tv_car_num);
            holder.tv_dirver_name=(TextView)view.findViewById(R.id.tv_dirver_name);
            holder.tv_phone=(TextView)view.findViewById(R.id.tv_phone);
            holder.tv_current_distance=(TextView)view.findViewById(R.id.tv_current_distance);
            holder.rl_phone=(RelativeLayout)view.findViewById(R.id.rl_phone);
            holder.ll_1=(LinearLayout)view.findViewById(R.id.ll_1);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
        final RepairListModel cb1=mList.get(i);
        final CarListModel cb = cb1.getChildren().get(i1);

        final   String compName=cb1.getName();

        final List<RepairModel>  listrepair=cb.getChildren();
      final   RepairModel item=listrepair.get(0);
        String carNum=item.getCarNumber();
        holder.tv_car_num.setText(carNum+"");
        String dirverName=item.getDriverName();
        holder.tv_dirver_name.setText(dirverName+"");
        final String phone=item.getDriverPhone();

        if(!Tools.isEmpty(phone)){
            holder.tv_phone.setText(phone);
            holder.tv_phone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
        }else{
            holder.tv_phone.setText("");
        }
        String distance=item.getDistance();
        holder.tv_current_distance.setText(distance+"公里");
        holder.rl_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Tools.isEmpty(phone)){
//                    callPhone(phone);
                    mPhoneLisenter.telPhone(item);
                }
            }
        });

        holder.ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TruckChildBean bean1=new TruckChildBean();
//                bean1.setChildId(cb.getChildId());
//                bean1.setName(cb.getName());
                Bundle bundle=new Bundle();
                bundle.putSerializable("need_repair",(Serializable)listrepair);
                bundle.putSerializable("company_name",compName);
//                //                CommonKitUtil.startActivity((Activity) mContext, WeightTrendActivity.class, bundle, false);
                CommonKitUtil.startActivity((Activity) mContext, RepairAct.class, bundle, false);
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
        TextView tv_car_num;
        TextView tv_dirver_name;
        TextView tv_phone;
        TextView tv_current_distance;
        RelativeLayout rl_phone;
        LinearLayout ll_1;
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

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        mContext.startActivity(intent);
    }

    public interface onPhoneLisenter {
        public void telPhone(RepairModel num);
    }
}
