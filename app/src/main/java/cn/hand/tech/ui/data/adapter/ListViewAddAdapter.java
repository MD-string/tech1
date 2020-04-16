package cn.hand.tech.ui.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDKRModel;
import cn.hand.tech.bean.WeightDataBean;

/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class ListViewAddAdapter extends BaseAdapter implements View.OnClickListener {
    private List<WeightDataBean> listValues;
    private LayoutInflater mLayoutInflater;
    private HDKRModel channelMode;
    public HashMap<Integer, Boolean> isMapSelected;//记录选中状态
    private boolean mIsEditor = true;
    public TreeSet<Integer> delContactsIdSet;
    private Callback mCallback;//接口

    public ListViewAddAdapter(Context context, List<WeightDataBean> listValues, HDKRModel model, Callback callback) {
        mLayoutInflater = LayoutInflater.from(context);
        this.listValues = listValues;
        channelMode = model;
        this.mCallback=callback;
        isMapSelected = new HashMap<Integer, Boolean>();
        delContactsIdSet = new TreeSet<Integer>();
        initData();
    }

    public void initData() {
        for (int i = 0; i < listValues.size(); i++) {
            isMapSelected.put(i, false);
        }
    }

    public void changeSelected(List<WeightDataBean> listData, boolean isEditor) { //刷新方法
        this.listValues = listData;
        this.mIsEditor = isEditor;
        notifyDataSetChanged();
    }

    public void setChannelChange(List<WeightDataBean> listData, HDKRModel model) { //刷新方法
        this.listValues = listData;
        this.channelMode = model;
        notifyDataSetChanged();
    }



    public void addItem(WeightDataBean item) {
        listValues.add(item);
    }

    public void deleteItem(int position) {
        listValues.remove(position);
    }

    @Override
    public int getCount() {
        return listValues.size();
    }

    @Override
    public Object getItem(int position) {
        return listValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.frg_data_item, null);
            holder.cb_weight_item = (CheckBox) convertView.findViewById(R.id.cb_weight_item);
            holder.tv_nub = (TextView) convertView.findViewById(R.id.tv_nub);
            holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.item_label_1 = (TextView) convertView.findViewById(R.id.tv_item_label1);
            holder.item_label_2 = (TextView) convertView.findViewById(R.id.tv_item_label2);
            holder.item_label_3 = (TextView) convertView.findViewById(R.id.tv_item_label3);
            holder.item_label_4 = (TextView) convertView.findViewById(R.id.tv_item_label4);
            holder.item_label_5 = (TextView) convertView.findViewById(R.id.tv_item_label5);
            holder.item_label_6 = (TextView) convertView.findViewById(R.id.tv_item_label6);
            holder.item_label_7 = (TextView) convertView.findViewById(R.id.tv_item_label7);
            holder.item_label_8 = (TextView) convertView.findViewById(R.id.tv_item_label8);
            holder.item_label_9 = (TextView) convertView.findViewById(R.id.tv_item_label9);
            holder.item_label_10 = (TextView) convertView.findViewById(R.id.tv_item_label10);
            holder.item_label_11 = (TextView) convertView.findViewById(R.id.tv_item_label11);
            holder.item_label_12 = (TextView) convertView.findViewById(R.id.tv_item_label12);
            holder.item_label_13 = (TextView) convertView.findViewById(R.id.tv_item_label13);
            holder.item_label_14 = (TextView) convertView.findViewById(R.id.tv_item_label14);
            holder.item_label_15 = (TextView) convertView.findViewById(R.id.tv_item_label15);
            holder.item_label_16 = (TextView) convertView.findViewById(R.id.tv_item_label16);


            holder.item_value_1 = (TextView) convertView.findViewById(R.id.tv_item_value1);
            holder.item_value_2 = (TextView) convertView.findViewById(R.id.tv_item_value2);
            holder.item_value_3 = (TextView) convertView.findViewById(R.id.tv_item_value3);
            holder.item_value_4 = (TextView) convertView.findViewById(R.id.tv_item_value4);
            holder.item_value_5 = (TextView) convertView.findViewById(R.id.tv_item_value5);
            holder.item_value_6 = (TextView) convertView.findViewById(R.id.tv_item_value6);
            holder.item_value_7 = (TextView) convertView.findViewById(R.id.tv_item_value7);
            holder.item_value_8 = (TextView) convertView.findViewById(R.id.tv_item_value8);
            holder.item_value_9 = (TextView) convertView.findViewById(R.id.tv_item_value9);
            holder.item_value_10 = (TextView) convertView.findViewById(R.id.tv_item_value10);
            holder.item_value_11 = (TextView) convertView.findViewById(R.id.tv_item_value11);
            holder.item_value_12 = (TextView) convertView.findViewById(R.id.tv_item_value12);
            holder.item_value_13 = (TextView) convertView.findViewById(R.id.tv_item_value13);
            holder.item_value_14 = (TextView) convertView.findViewById(R.id.tv_item_value14);
            holder.item_value_15 = (TextView) convertView.findViewById(R.id.tv_item_value15);
            holder.item_value_16 = (TextView) convertView.findViewById(R.id.tv_item_value16);
            holder.original_weight=(TextView)convertView.findViewById(R.id.original_weight);
            holder.tv_error=(TextView)convertView.findViewById(R.id.tv_error);
            holder.item_weight = (TextView) convertView.findViewById(R.id.tv_item_weight);
            holder.ll_item_location= (LinearLayout) convertView.findViewById(R.id.ll_item_location);
            holder.rl_w=(RelativeLayout)convertView.findViewById(R.id.rl_w);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WeightDataBean model = listValues.get(position);
        String deviceWeight=model.getWeightFromDevice();
        String realWeight=model.getWeightFromReal();
        float error=Float.valueOf(realWeight)-Float.valueOf(deviceWeight);
        holder.original_weight.setText(deviceWeight);
        holder.tv_error.setText(error+"");
        if (channelMode.bChannel1) {
            holder.item_label_1.setVisibility(View.VISIBLE);
            holder.item_value_1.setVisibility(View.VISIBLE);
            holder.item_value_1.setText(getMvvValue(Float.valueOf(model.getAch1()),Float.valueOf( model.getSch1())) + "");
        } else {
            holder.item_label_1.setVisibility(View.GONE);
            holder.item_value_1.setVisibility(View.GONE);
        }

        if (channelMode.bChannel2) {
            holder.item_label_2.setVisibility(View.VISIBLE);
            holder.item_value_2.setVisibility(View.VISIBLE);
           holder.item_value_2.setText(getMvvValue(Float.parseFloat(model.getAch2()), Float.parseFloat(model.getSch2())) + "");
        } else {
            holder.item_label_2.setVisibility(View.GONE);
            holder.item_value_2.setVisibility(View.GONE);
        }
        if (channelMode.bChannel3) {
            holder.item_label_3.setVisibility(View.VISIBLE);
            holder.item_value_3.setVisibility(View.VISIBLE);
          holder.item_value_3.setText(getMvvValue(Float.parseFloat(model.getAch3()), Float.parseFloat(model.getSch3())) + "");
        } else {
            holder.item_label_3.setVisibility(View.GONE);
            holder.item_value_3.setVisibility(View.GONE);
        }
        if (channelMode.bChannel4) {
            holder.item_label_4.setVisibility(View.VISIBLE);
            holder.item_value_4.setVisibility(View.VISIBLE);
           holder.item_value_4.setText(getMvvValue(Float.parseFloat(model.getAch4()), Float.parseFloat(model.getSch4())) + "");
        } else {
            holder.item_label_4.setVisibility(View.GONE);
            holder.item_value_4.setVisibility(View.GONE);
        }
        if (channelMode.bChannel5) {
            holder.item_label_5.setVisibility(View.VISIBLE);
            holder.item_value_5.setVisibility(View.VISIBLE);
            holder.item_value_5.setText(getMvvValue(Float.parseFloat(model.getAch5()), Float.parseFloat(model.getSch5())) + "");
        } else {
            holder.item_label_5.setVisibility(View.GONE);
            holder.item_value_5.setVisibility(View.GONE);
        }
        if (channelMode.bChannel6) {
            holder.item_label_6.setVisibility(View.VISIBLE);
            holder.item_value_6.setVisibility(View.VISIBLE);
           holder.item_value_6.setText(getMvvValue(Float.parseFloat(model.getAch6()), Float.parseFloat(model.getSch6())) + "");
        } else {
            holder.item_label_6.setVisibility(View.GONE);
            holder.item_value_6.setVisibility(View.GONE);
        }
        if (channelMode.bChannel7) {
            holder.item_label_7.setVisibility(View.VISIBLE);
            holder.item_value_7.setVisibility(View.VISIBLE);
            holder.item_value_7.setText(getMvvValue(Float.parseFloat(model.getAch7()), Float.parseFloat(model.getSch7())) + "");
        } else {
            holder.item_label_7.setVisibility(View.GONE);
            holder.item_value_7.setVisibility(View.GONE);
        }
        if (channelMode.bChannel8) {
            holder.item_label_8.setVisibility(View.VISIBLE);
            holder.item_value_8.setVisibility(View.VISIBLE);
            holder.item_value_8.setText(getMvvValue(Float.parseFloat(model.getAch8()), Float.parseFloat(model.getSch8())) + "");
        } else {
            holder.item_label_8.setVisibility(View.GONE);
            holder.item_value_8.setVisibility(View.GONE);
        }
        if (channelMode.bChannel9) {
            holder.item_label_9.setVisibility(View.VISIBLE);
            holder.item_value_9.setVisibility(View.VISIBLE);
            holder.item_value_9.setText(getMvvValue(Float.parseFloat(model.getAch9()), Float.parseFloat(model.getSch9())) + "");
        } else {
            holder.item_label_9.setVisibility(View.GONE);
            holder.item_value_9.setVisibility(View.GONE);
        }
        if (channelMode.bChannel10) {
            holder.item_label_10.setVisibility(View.VISIBLE);
            holder.item_value_10.setVisibility(View.VISIBLE);
            holder.item_value_10.setText(getMvvValue(Float.parseFloat(model.getAch10()), Float.parseFloat(model.getSch10())) + "");
        } else {
            holder.item_label_10.setVisibility(View.GONE);
            holder.item_value_10.setVisibility(View.GONE);
        }
        if (channelMode.bChannel11) {
            holder.item_label_11.setVisibility(View.VISIBLE);
            holder.item_value_11.setVisibility(View.VISIBLE);
            holder.item_value_11.setText(getMvvValue(Float.parseFloat(model.getAch11()), Float.parseFloat(model.getSch11())) + "");
        } else {
            holder.item_label_11.setVisibility(View.GONE);
            holder.item_value_11.setVisibility(View.GONE);
        }

        if (channelMode.bChannel12) {
            holder.item_label_12.setVisibility(View.VISIBLE);
            holder.item_value_12.setVisibility(View.VISIBLE);
            holder.item_value_12.setText(getMvvValue(Float.parseFloat(model.getAch12()), Float.parseFloat(model.getSch12())) + "");
        } else {
            holder.item_label_12.setVisibility(View.GONE);
            holder.item_value_12.setVisibility(View.GONE);
        }
        if (channelMode.bChannel13) {
            holder.item_label_13.setVisibility(View.VISIBLE);
            holder.item_value_13.setVisibility(View.VISIBLE);
            holder.item_value_13.setText(getMvvValue(Float.parseFloat(model.getAch13()), Float.parseFloat(model.getSch13())) + "");
        } else {
            holder.item_label_13.setVisibility(View.GONE);
            holder.item_value_13.setVisibility(View.GONE);
        }
        if (channelMode.bChannel14) {
            holder.item_label_14.setVisibility(View.VISIBLE);
            holder.item_value_14.setVisibility(View.VISIBLE);
            holder.item_value_14.setText(getMvvValue(Float.parseFloat(model.getAch14()), Float.parseFloat(model.getSch14())) + "");
        } else {
            holder.item_label_14.setVisibility(View.GONE);
            holder.item_value_14.setVisibility(View.GONE);
        }
        if (channelMode.bChannel15) {
            holder.item_label_15.setVisibility(View.VISIBLE);
            holder.item_value_15.setVisibility(View.VISIBLE);
            holder.item_value_15.setText(getMvvValue(Float.parseFloat(model.getAch15()), Float.parseFloat(model.getSch15())) + "");
        } else {
            holder.item_label_15.setVisibility(View.GONE);
            holder.item_value_15.setVisibility(View.GONE);
        }
        if (channelMode.bChannel16) {
            holder.item_label_16.setVisibility(View.VISIBLE);
            holder.item_value_16.setVisibility(View.VISIBLE);
            holder.item_value_16.setText(getMvvValue(Float.parseFloat(model.getAch16()), Float.parseFloat(model.getSch16())) + "");
        } else {
            holder.item_label_16.setVisibility(View.GONE);
            holder.item_value_16.setVisibility(View.GONE);
        }

        holder.tv_nub.setText(listValues.size()-position  + "");
        holder.tv_location.setText(model.getLocation() + "");
        holder.tv_time.setText(model.getUploadDate());
        holder.item_weight.setText(realWeight + "");
        if (mIsEditor) {
            holder.cb_weight_item.setVisibility(View.GONE);
        } else {
            holder.cb_weight_item.setVisibility(View.VISIBLE);
        }
        if(isMapSelected !=null && isMapSelected.get(position) !=null){
            holder.cb_weight_item.setChecked(isMapSelected.get(position));
            if (isMapSelected.get(position)) {
                delContactsIdSet.add(position);
            } else {
                delContactsIdSet.remove(position);
            }
        }
        holder.ll_item_location.setOnClickListener(this);
        holder.ll_item_location.setTag(position);

        holder.rl_w.setOnClickListener(this);
        holder.rl_w.setTag(position);
//        holder.item_weight.setOnClickListener(this);
//        holder.item_weight.setTag(position);


        return convertView;
    }

    @Override
    public void onClick(View v) {
        //将点击事件传递出来
        mCallback.toclick(v);
    }

    public interface Callback {
        public void toclick(View view);
    }
  public   class ViewHolder {
        TextView item_value_1, item_value_2, item_value_3, item_value_4, item_value_5, item_value_6, item_value_7, item_value_8, item_value_9, item_value_10;
        TextView item_value_11, item_value_12, item_value_13, item_value_14, item_value_15, item_value_16;
        TextView item_label_1, item_label_2, item_label_3, item_label_4, item_label_5, item_label_6, item_label_7, item_label_8, item_label_9, item_label_10;
        TextView item_label_11, item_label_12, item_label_13, item_label_14, item_label_15, item_label_16;
        TextView item_weight, tv_nub, tv_location, tv_time;
        TextView original_weight,tv_error;
        RelativeLayout rl_w;
       public CheckBox cb_weight_item;
        LinearLayout ll_item_location;
    }

    private float getMvvValue(float ad, float zero) {
        float x = (ad - zero) / 5;
        DecimalFormat myformat = new DecimalFormat("0.000");
        String str = myformat.format(x);
        float mvvValue = Float.parseFloat(str);
        return mvvValue;
    }
}
