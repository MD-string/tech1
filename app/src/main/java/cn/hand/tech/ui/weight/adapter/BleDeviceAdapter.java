package cn.hand.tech.ui.weight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ble.bean.BleDevice;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.weight.bean.CarInfo;

/**
 * Created by hand-hitech2 on 2018-03-20.
 */

public class BleDeviceAdapter extends BaseAdapter {
    private final ACache acache;
    private List<BleDevice> mlist;
    private List<CarInfo> clist;
    private Context mContext;
    private DoConnectclick textcallback;
    private DoEditClick edtcallback;

    public BleDeviceAdapter(List<BleDevice> list, Context context,  List<CarInfo>  clist) {
        this.mlist = list;
        this.mContext = context;
        this.clist = clist;
        acache= ACache.get(context,"WeightFragment");
    }

    public void updateListView(List<BleDevice> list) {
        this.mlist = list;
        this.notifyDataSetChanged();
    }
    public void setlist(List<CarInfo>  clist,List<BleDevice> list) {
        this.clist = clist;
        this.mlist = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
       final BleDevice item=mlist.get(i);
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.pop_item_ble_device, null);
            holder.tv_bel_device = (TextView) view.findViewById(R.id.tv_bel_device);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        boolean isSame =false;
        String addr=item.getMacAddress();
        String name=item.getRealName();

         List<CarInfo> mlist=(List<CarInfo>) acache.getAsObject("carinfo_list");
         if(mlist !=null && mlist.size()>0){
             for(int j=0; j<mlist.size();j++){
                 String MAC=mlist.get(j).getMac();
                 if(MAC.equalsIgnoreCase(addr)){     //有相同的蓝牙
                     isSame=true;
                     holder.tv_bel_device.setText(mlist.get(j).getCarNumber()); //显示车牌
                     holder.tv_bel_device.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             textcallback.doConnect(item);
                         }
                     });
                 }
             }
             if(!isSame){                 //没有相同的蓝牙
                 String realShow1="";
                 if(name.contains("HD")){
                     realShow1=name;
                 }else{
                     realShow1="汉德|"+addr;
                 }
                 holder.tv_bel_device.setText(realShow1); //显示车牌
                 holder.tv_bel_device.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         edtcallback.doEdit(item);
                     }
                 });
             }
         }else{
             String realShow="";
             if(name.contains("HD")){
                 realShow=name;
             }else{
                 realShow="汉德|"+addr;
             }
             holder.tv_bel_device.setText(realShow); //显示车牌
             holder.tv_bel_device.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     edtcallback.doEdit(item);
                 }
             });
         }

        return view;
    }

    class ViewHolder {
        TextView tv_bel_device;
    }
    public static interface  DoConnectclick{
        public void doConnect(BleDevice item);

    }

    public  static interface DoEditClick{
        public void doEdit(BleDevice item);
    }

    public void setConnectClick(DoConnectclick listener) {
        textcallback = listener;
    }
    public void setDoEditClick(DoEditClick listener1) {
        edtcallback = listener1;
    }
}