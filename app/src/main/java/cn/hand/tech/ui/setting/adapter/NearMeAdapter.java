package cn.hand.tech.ui.setting.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.setting.bean.RepairModel;
import cn.hand.tech.utils.Tools;

/**
 * Created by hxz on 2018/6/24.  离我最近
 */
public class NearMeAdapter extends BaseAdapter {
    private final ACache acache;
    private final onPhoneLisenter mPhoneLisenter;
    private Context context;
    private LayoutInflater inflater;
    private List<RepairModel> mlist;
    DecimalFormat df = new DecimalFormat("0.00");
    private AlertDialog tempDialog;


    public NearMeAdapter(Context context, List<RepairModel> list) {
        this.context = context;
        this.mlist = list;
        inflater = LayoutInflater.from(this.context);
        mPhoneLisenter = (onPhoneLisenter) context;
        acache = ACache.get(context, "WeightFragment");
    }

    public void updateListView(List<RepairModel> list) {
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
        final RepairModel item = mlist.get(position);
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView = inflater.inflate(R.layout.adapter_near_me, null);
            viewHold.tv_car_num = (TextView) convertView.findViewById(R.id.tv_car_num);
            viewHold.tv_dirver_name = (TextView) convertView.findViewById(R.id.tv_dirver_name);
            viewHold.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHold.tv_current_distance = (TextView) convertView.findViewById(R.id.tv_current_distance);
            viewHold.rl_phone = (RelativeLayout) convertView.findViewById(R.id.rl_phone);

            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        if (item != null) {
            String carNum = item.getCarNumber();
            viewHold.tv_car_num.setText(carNum + "");
            String dirverName = item.getDriverName();
            viewHold.tv_dirver_name.setText(dirverName + "");
            final String phone = item.getDriverPhone();

            if (!Tools.isEmpty(phone)) {
                viewHold.tv_phone.setText(phone);
                viewHold.tv_phone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            } else {
                viewHold.tv_phone.setText("");
            }
            String distance = item.getDistance();
            if(Tools.isEmpty(distance)){
                distance="99999999";
            }
            String str = df.format(Double.parseDouble(distance));
            viewHold.tv_current_distance.setText("离我" + str + "公里");
            viewHold.rl_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Tools.isEmpty(phone)) {
                        mPhoneLisenter.telPhone(item);
//                        showDialog(context, phone);
                    }
                }
            });

        }
        return convertView;
    }

    class ViewHold {
        TextView tv_car_num;
        TextView tv_dirver_name;
        TextView tv_phone;
        TextView tv_current_distance;
        RelativeLayout rl_phone;
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent1 = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent1.setData(data);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(intent1);
    }

    //录车提示框
    public void showDialog(final Context context,final String phone) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_phone, null);
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(phone);
                tempDialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempDialog.dismiss();
            }
        });
        dialog.setView(view);//给对话框添加一个EditText输入文本框
        //下面是弹出键盘的关键处
        tempDialog = dialog.create();
        tempDialog.setView(view, 0, 0, 0, 0);
        tempDialog.show();
    }

    public interface onPhoneLisenter {
        public void telPhone(RepairModel num);
    }


}


