package cn.hand.tech.ui.weight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.weight.bean.ChannelDetectModel;

/**
 * Created by wcf on 2018/3/24.
 */
public class ChannelDetectAdapter extends BaseAdapter {
    private final ACache acache;
    private Context context;
    private LayoutInflater inflater;
    private List<ChannelDetectModel> mlist;
    private static final String TAG = "WeightFragment";

    public ChannelDetectAdapter(Context context, List<ChannelDetectModel> list) {
        this.context = context;
        this.mlist = list;
        inflater = LayoutInflater.from(this.context);
        acache = ACache.get(context, TAG);

    }
    public void updateListView(List<ChannelDetectModel> list) {
        this.mlist = list;
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
        ViewHolder holder = null;
        final ChannelDetectModel item=mlist.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.gv_item_channel, null);
            holder.ll_channel=(LinearLayout)convertView.findViewById(R.id.ll_channel);
            holder.tv_channel = (TextView) convertView.findViewById(R.id.tv_channel);
            holder.tv_ad=(TextView)convertView.findViewById(R.id.tv_ad);
            holder.iv_channel_normal = (ImageView) convertView.findViewById(R.id.iv_channel_normal);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String channelName=item.getChannelName();
        holder.tv_channel.setText(channelName);

        if (mlist.get(position).isChannelNormal()){
            holder.iv_channel_normal.setSelected(true);
        }else {
            holder.iv_channel_normal.setSelected(false);
        }
        String channelNumber=channelName.replace("通道","");
       String isCheck= acache.getAsString("ad_check_"+channelNumber);
        DLog.e("ChannelDetectAdapter","ChannelDetectAdapter="+(position+1)+"/channelNumber="+channelNumber+"/"+isCheck);
        if("1".equals(isCheck)){
            holder.tv_ad.setText("AD通过");
            holder.tv_ad.setTextColor(context.getResources().getColor(R.color.black));
        }else{
            holder.tv_ad.setText("AD不通过");
            holder.tv_ad.setTextColor(context.getResources().getColor(R.color.red));

        }

        return convertView;
    }

    class ViewHolder {
        LinearLayout ll_channel;
        TextView tv_channel;
        ImageView iv_channel_normal;
        TextView tv_ad;
    }
}
