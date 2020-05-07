package cn.hand.tech.ui.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.data.bean.LocalDataTimeModel;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.Tools;

/**
 * Created by WCF on 2018-4-4.
 */

public class LocalDataAdapter extends BaseAdapter {
    private List<LocalDataTimeModel> mList;
    private LayoutInflater mLayoutInflater;
    public HashMap<Integer, Boolean> isMapSelected;//记录选中状态
    public TreeSet<Integer> delContactsIdSet;
    private boolean mIsEditor = true;
    private ACache acache;

    public LocalDataAdapter(List<LocalDataTimeModel> list, Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mList = list;
        isMapSelected = new HashMap<Integer, Boolean>();
        delContactsIdSet = new TreeSet<Integer>();
        initData();
        acache= ACache.get(context, CommonUtils.TAG);

    }

    public void initData() {
        for (int i = 0; i < mList.size(); i++) {
            isMapSelected.put(i, false);
        }
    }

    public void changeSelected(List<LocalDataTimeModel> listData, boolean isEditor) { //刷新方法
        this.mList = listData;
        this.mIsEditor = isEditor;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.lv_item_local_data, null);
            holder.cb_local_item = (CheckBox) view.findViewById(R.id.cb_local_item);
            holder.tv_saveLocal_time = (TextView) view.findViewById(R.id.tv_saveLocal_time);
            holder.tv_dev_id = (TextView) view.findViewById(R.id.tv_dev_id);
            holder.tv_car_number = (TextView) view.findViewById(R.id.tv_car_number);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_saveLocal_time.setText(mList.get(i).getLocalDataTime() + "");
        holder.tv_dev_id.setText("ID:"+mList.get(i).getWeightDataBeanList().get(0).getDeviceId());
        String carNumber=mList.get(i).getWeightDataBeanList().get(0).getCarNumber();
        String truckNumber=acache.getAsString("car_num");
        if(!Tools.isEmpty(carNumber)){
            holder.tv_car_number.setText(carNumber+ "");
        }else{
            holder.tv_car_number.setText(truckNumber+ "");
        }

        if (mIsEditor) {
            holder.cb_local_item.setVisibility(View.GONE);
        } else {
            holder.cb_local_item.setVisibility(View.VISIBLE);
        }
        holder.cb_local_item.setChecked(isMapSelected.get(i));
        if (isMapSelected.get(i)) {
            delContactsIdSet.add(i);
        } else {
            delContactsIdSet.remove(i);
        }

        return view;
    }

    public class ViewHolder {
        TextView tv_saveLocal_time;
        public CheckBox cb_local_item;
        TextView tv_dev_id;
        TextView tv_car_number;
    }
}