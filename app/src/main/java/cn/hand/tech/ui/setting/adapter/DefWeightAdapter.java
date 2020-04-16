package cn.hand.tech.ui.setting.adapter;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.TextWatcherImpl;

/**
 * Created by hxz on 2018/7/24.
 */
public class DefWeightAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> mlist;
    private doClose mlistener;
    private boolean mOpen;

    public DefWeightAdapter(Context context,  List<String> list) {
        this.context = context;
        this.mlist = list;
        inflater = LayoutInflater.from(this.context);
    }
    public void updateListView(List<String> list,boolean isOpen) {
        this.mlist = list;
        this.mOpen=isOpen;
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
            convertView =inflater.inflate(R.layout.def_weight_item, null);
            viewHold.tv_id=(TextView)convertView.findViewById(R.id.tv_id);
            viewHold.et_number=(EditText)convertView.findViewById(R.id.et_number);

            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        if(item != null ){
            viewHold.tv_id.setText(position+1+":");
            viewHold.et_number.setText(item);
            viewHold.et_number.addTextChangedListener(new TextWatcherImpl(){
                @Override
                public void afterTextChanged(Editable arg0) {
                    String number=arg0.toString();
                }
            });
           viewHold.et_number.setEnabled(mOpen);


        }
        return convertView;
    }

    class ViewHold {
        TextView tv_id;
        EditText et_number;
    }

    public static interface  doClose{
        public void close();
        public void open();
    }
    public void setConnectClick(doClose listener) {
        mlistener = listener;
    }
}


