package cn.hand.tech.ui.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ui.weight.bean.CarInfo;

/**
 * Created by hxz on 2018/6/24.
 */
public class BindCarAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<CarInfo> mlist;
    private dobindCarNo mlistener;

    public BindCarAdapter(Context context, List<CarInfo> list) {
        this.context = context;
        this.mlist = list;
        inflater = LayoutInflater.from(this.context);
    }
    public void updateListView(List<CarInfo> list) {
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
       final CarInfo item = mlist.get(position);
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView =inflater.inflate(R.layout.bind_car_item, null);
            viewHold.tv_id=(TextView)convertView.findViewById(R.id.tv_id);
            viewHold.et_number=(TextView)convertView.findViewById(R.id.et_number);
            viewHold.btn_bind=(Button)convertView.findViewById(R.id.btn_bind);

            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        if(item != null ){
            String id=item.getDeviceId();
            final   String carNo=item.getCarNumber();
            viewHold.tv_id.setText(id);
            viewHold.et_number.setText(carNo);
//            viewHold.et_number.addTextChangedListener(new TextWatcherImpl(){
//                @Override
//                public void afterTextChanged(Editable arg0) {
//                    String number=arg0.toString();
//                     if(number ==null || number.equals("")){
//                         viewHold.btn_bind.setText("删除");
//                     }
//                }
//            });
            viewHold.btn_bind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String num=viewHold.et_number.getText().toString();
                    mlistener.bindNo(num,item);

                }
            });


        }
        return convertView;
    }

    class ViewHold {
        TextView tv_id;
        TextView et_number;
        Button btn_bind;
    }

    public static interface  dobindCarNo{
        public void bindNo(String str, CarInfo item);
    }
    public void setConnectClick(dobindCarNo listener) {
        mlistener = listener;
    }
}


