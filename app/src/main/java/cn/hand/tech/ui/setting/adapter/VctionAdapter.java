package cn.hand.tech.ui.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.hand.tech.R;

/**
 * Created by hxz on 2018/7/24.
 */
public class VctionAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> mlist;
    private int mnub =0;

    public VctionAdapter(Context context, List<String> list) {
        this.context = context;
        this.mlist = list;
        inflater = LayoutInflater.from(this.context);
    }
    public void updateListView(List<String> list) {
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
       final ViewHold viewHold;
       final String item = mlist.get(position);
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView =inflater.inflate(R.layout.adapter_guide_item_1, null);
            viewHold.tv_1=(TextView)convertView.findViewById(R.id.tv_1);

            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold)convertView.getTag();
        }
        if(item != null ){
            int po=position+1;
            String number=po+".";
            viewHold.tv_1.setText(number+item);
        }
        return convertView;
    }

    class ViewHold {
        TextView tv_1;
    }
}


