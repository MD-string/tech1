package cn.hand.tech.ui.weight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ble.bean.BleDevice;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.weight.bean.CarInfo;
import cn.hand.tech.utils.Tools;

/**
 * Created by hand-hitech2 on 2018-03-20.
 */

public class mBleDeviceAdapter extends BaseAdapter {
    private final ACache acache;
    private List<BleDevice> mlist;
    private Context mContext;
    private DoConnectclick textcallback;
    private DoEditClick edtcallback;
    private List<CarInfo> clist;

    public mBleDeviceAdapter(Context context,List<BleDevice> list) {
        this.mlist = list;
        this.mContext = context;
        acache= ACache.get(context,"WeightFragment");
    }

    public void updateListView(List<BleDevice> list) {
        this.mlist = list;
        this.notifyDataSetChanged();
    }
    public void setlist(List<BleDevice> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.ble_item_ble_device, null);
            holder.ble_name = (TextView) view.findViewById(R.id.ble_name);
            holder.ble_mac = (TextView) view.findViewById(R.id.ble_mac);
            holder.ble_rssi = (TextView) view.findViewById(R.id.ble_rssi);
            holder.progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
            holder.ble_state=(TextView)view.findViewById(R.id.ble_state);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String name=item.getRealName();
        holder.ble_name.setText(name);
        String mac=item.getMacAddress();
        holder.ble_mac.setText(mac);
        int rssi=item.getRssi();
        holder.ble_rssi.setText(rssi+"");
        int arssi=Math.abs(rssi);
        holder.progressBar.setProgress(100-arssi);

        clist=(List<CarInfo>) acache.getAsObject("carinfo_list");
        if(!Tools.isEmpty(name)){
            if(name.contains("HD")){
                if(clist !=null && clist.size() >0){
                    for(int j=0;j<clist.size();j++){
                        CarInfo info=clist.get(j);
                        String id="HD:"+info.getDeviceId();
                        if(name.equals(id)){
                            holder.ble_name.setText(info.getCarNumber());
                        }

                    }
                }
            }else{
                if(clist !=null && clist.size() >0){
                    for(int j=0;j<clist.size();j++){
                        CarInfo info=clist.get(j);
                        String macDress=info.getMac();
                        if(!Tools.isEmpty(mac) &&mac.equals(macDress)){
                            holder.ble_name.setText(info.getCarNumber());
                        }

                    }
                }
            }
        }

//        boolean isconnect=item.isHave();
//        if(isconnect){
//            holder.ble_state.setText("已连接");
//            holder.ble_state.setBackgroundColor(mContext.getResources().getColor(R.color.red));
//        }else{
//            holder.ble_state.setText("未连接");
//            holder.ble_state.setBackgroundColor(mContext.getResources().getColor(R.color.hand_blue));
//        }
        return view;
    }

    class ViewHolder {
        TextView ble_name,ble_mac,ble_rssi;
        ProgressBar progressBar;
        TextView ble_state;
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