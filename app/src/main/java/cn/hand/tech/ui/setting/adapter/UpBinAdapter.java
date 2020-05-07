package cn.hand.tech.ui.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ui.setting.bean.BGuJianBean;
import cn.hand.tech.ui.setting.bean.GuJianBean;


/**
 * Created by wcf on 2018-03-08.
 * describe:公司及子条目车辆适配器
 * Company: Shen Zhen Hand Hitech(深圳汉德网络科技)
 */

public class UpBinAdapter extends BaseExpandableListAdapter {
     private Context mContext;
    private List<BGuJianBean> mList;
    private Callback mCallback;//接口
    private int mpostion=-1;


    public UpBinAdapter(Context mContext, List<BGuJianBean> mList,Callback callback) {
        this.mContext = mContext;
        this.mList = mList;
        this.mCallback=callback;
    }
    public void updateListView(List<BGuJianBean> list){
        this.mList = list;
        notifyDataSetChanged();
    }
    public void setCheck(int position){
        this.mpostion = position;
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
             return mList.get(size-1).getList().size();
        }else{
            List<GuJianBean> list= mList.get(i).getList();
            if(list !=null && list.size() >0){

                return mList.get(i).getList().size();
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
        return mList.get(i).getList().get(i1);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.lvitem_up_bin_item, null);
            holder.rl_mian=(RelativeLayout)view.findViewById(R.id.rl_mian);
            holder.tv_gujian = (TextView) view.findViewById(R.id.tv_gujian);
            holder.iv_truckInfo_arrow = (ImageView ) view.findViewById(R.id.iv_truckInfo_arrow);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }
       final BGuJianBean cb1=mList.get(i);
        holder.tv_gujian.setText(cb1.getName());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lvitem_bin_item, null);
            holder.rl_1=(LinearLayout)view.findViewById(R.id.rl_1);
            holder.checkbox_1 = (CheckBox) view.findViewById(R.id.checkbox_1);
            holder.tv_gujian_name = (TextView) view.findViewById(R.id.tv_gujian_name);
            holder.tv_gujian_miao = (TextView) view.findViewById(R.id.tv_gujian_miao);
            holder.tv_bin_number = (TextView) view.findViewById(R.id.tv_bin_number);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
       final GuJianBean cb = mList.get(i).getList().get(i1);
        holder.tv_gujian_name.setText(cb.getBinName());
        holder.tv_gujian_miao.setText(cb.getDescription());
        int binNumber=cb.getPacketTotal();
        holder.tv_bin_number.setText(binNumber+"");

        holder.checkbox_1.setChecked(true);
        holder.checkbox_1.setEnabled(false);
//        if(mpostion ==i1){
//            holder.checkbox_1.setChecked(true);
//        }else{
//            holder.checkbox_1.setChecked(false);
////        }
//       final int mpo=i1;
//        final  String binId=cb.getId();
//        mCallback.onTocheck( holder.checkbox_1,mpo,binId);
//        holder.rl_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCallback.onTocheck(v,mpo,binId);
//            }
//        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {


        return true;
    }


    public interface Callback {
        public void onTocheck(View view,int position,String id);
    }
    class GroupHolder {
        TextView tv_gujian;
        RelativeLayout rl_mian;
        ImageView iv_truckInfo_arrow;
    }

    class ChildHolder {
        TextView tv_bin_number;
        TextView tv_gujian_miao;
        TextView tv_gujian_name;
        CheckBox checkbox_1;
        LinearLayout rl_1;
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
