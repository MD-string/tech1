package cn.hand.tech.ui.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.home.BaseFragment;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/*
 *通道选择
 */
public class PassSettingValueFragment extends BaseFragment  {
    private Context context;
    private ACache acache;
    private EditText et_1,et_2,et_3,et_4,et_5,et_6,et_7,et_8,et_9,et_10,et_11,et_12,et_13,et_14,et_15,et_16,et_time;
    private Button bt_sure;
    private List<String> mlist;
    private CheckBox cb_1;
    private String isVideo="0";
    private static final String TAG = "WeightFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        View view = inflater.inflate(R.layout.activity_pass_choose_value, container, false);
        acache= ACache.get(context,TAG);
        initView(view);

        return view;
    }
    private void initView(View view) {
//        tv_para_title=(TextView)view.findViewById(R.id.tv_para_title);
//        tv_para_title.setText("通道选择");
//        ll_back=(LinearLayout)view.findViewById(R.id.ll_back);
//        ll_back.setVisibility(View.GONE);
//        tv_choose=(TextView)view.findViewById(R.id.tv_choose);
//        tv_choose.setText("全不选");
//        tv_choose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!ischecked){
//                    ischecked=true;
//                    tv_choose.setText("全选");
//                    channel_1.setChecked(false); channel_2.setChecked(false);
//                    channel_3.setChecked(false); channel_4.setChecked(false);
//                    channel_5.setChecked(false); channel_6.setChecked(false);
//                    channel_7.setChecked(false); channel_8.setChecked(false);
//                    channel_9.setChecked(false); channel_10.setChecked(false);
//                    channel_11.setChecked(false); channel_12.setChecked(false);
//                    channel_13.setChecked(false); channel_14.setChecked(false);
//                    channel_15.setChecked(false); channel_16.setChecked(false);
//
//                }else{
//                    ischecked=false;
//                    tv_choose.setText("全不选");
//                    channel_1.setChecked(true); channel_2.setChecked(true);
//                    channel_3.setChecked(true); channel_4.setChecked(true);
//                    channel_5.setChecked(true); channel_6.setChecked(true);
//                    channel_7.setChecked(true); channel_8.setChecked(true);
//                    channel_9.setChecked(true); channel_10.setChecked(true);
//                    channel_11.setChecked(true); channel_12.setChecked(true);
//                    channel_13.setChecked(true); channel_14.setChecked(true);
//                    channel_15.setChecked(true); channel_16.setChecked(true);
//                }
//
//            }
//        });

        et_1=(EditText)view.findViewById(R.id.et_1);  et_2=(EditText)view.findViewById(R.id.et_2);
        et_3=(EditText)view.findViewById(R.id.et_3);  et_4=(EditText)view.findViewById(R.id.et_4);
        et_5=(EditText)view.findViewById(R.id.et_5);  et_6=(EditText)view.findViewById(R.id.et_6);
        et_7=(EditText)view.findViewById(R.id.et_7);  et_8=(EditText)view.findViewById(R.id.et_8);
        et_9=(EditText)view.findViewById(R.id.et_9);  et_10=(EditText)view.findViewById(R.id.et_10);
        et_11=(EditText)view.findViewById(R.id.et_11);  et_12=(EditText)view.findViewById(R.id.et_12);
        et_13=(EditText)view.findViewById(R.id.et_13);  et_14=(EditText)view.findViewById(R.id.et_14);
        et_15=(EditText)view.findViewById(R.id.et_15);  et_16=(EditText)view.findViewById(R.id.et_16);
        et_time=(EditText)view.findViewById(R.id.et_time);

        RelativeLayout  rl_1=(RelativeLayout)view.findViewById(R.id.rl_1);
        cb_1=(CheckBox)view.findViewById(R.id.cb_1);
        cb_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isVideo="0";
                }else{
                    isVideo="1";
                }
            }
        });

        mlist=(List<String>)acache.getAsObject("et_number");
        if(mlist !=null && mlist.size() ==18){
            et_1.setText(mlist.get(0));  et_2.setText(mlist.get(1));  et_3.setText(mlist.get(2));
            et_4.setText(mlist.get(3));  et_5.setText(mlist.get(4));  et_6.setText(mlist.get(5));
            et_7.setText(mlist.get(6));  et_8.setText(mlist.get(7));  et_9.setText(mlist.get(8));
            et_10.setText(mlist.get(9));  et_11.setText(mlist.get(10));  et_12.setText(mlist.get(11));
            et_13.setText(mlist.get(12));  et_14.setText(mlist.get(13));  et_15.setText(mlist.get(14));
            et_16.setText(mlist.get(15));  et_time.setText(mlist.get(16));
            String isv=mlist.get(17);
            if("1".equals(isv)){
                cb_1.setChecked(false);
            }else{
                cb_1.setChecked(true);
            }
        }


        bt_sure=(Button)view.findViewById(R.id.bt_sure);
        bt_sure.setVisibility(View.GONE);

        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
                showTips("保存成功");
//
//                Intent i=new Intent(BleConstant.ACTION_CHANNEL_CHANGE);
//                context.sendBroadcast(i);
            }
        });


//        channel_1=(CheckBox)view.findViewById(R.id.channel_1);
//        channel_2=(CheckBox)view.findViewById(R.id.channel_2);
//        channel_3=(CheckBox)view.findViewById(R.id.channel_3);
//        channel_4=(CheckBox)view.findViewById(R.id.channel_4);
//        channel_5=(CheckBox)view.findViewById(R.id.channel_5);
//        channel_6=(CheckBox)view.findViewById(R.id.channel_6);
//        channel_7=(CheckBox)view.findViewById(R.id.channel_7);
//        channel_8=(CheckBox)view.findViewById(R.id.channel_8);
//        channel_9=(CheckBox)view.findViewById(R.id.channel_9);
//        channel_10=(CheckBox)view.findViewById(R.id.channel_10);
//        channel_11=(CheckBox)view.findViewById(R.id.channel_11);
//        channel_12=(CheckBox)view.findViewById(R.id.channel_12);
//        channel_13=(CheckBox)view.findViewById(R.id.channel_13);
//        channel_14=(CheckBox)view.findViewById(R.id.channel_14);
//        channel_15=(CheckBox)view.findViewById(R.id.channel_15);
//        channel_16=(CheckBox)view.findViewById(R.id.channel_16);
//
//        channelModel = (HDKRModel)acache.getAsObject("channel_model");//保存多少通道
//        if(channelModel == null ) {
//            channelModel = new HDKRModel();
//        }else{
//            if(channelModel.bChannel1){
//                channel_1.setChecked(true);
//            }else{
//                channel_1.setChecked(false);
//            }
//            if(channelModel.bChannel2){
//                channel_2.setChecked(true);
//            }else{
//                channel_2.setChecked(false);
//            }
//            if(channelModel.bChannel3){
//                channel_3.setChecked(true);
//            }else{
//                channel_3.setChecked(false);
//            }
//            if(channelModel.bChannel4){
//                channel_4.setChecked(true);
//            }else{
//                channel_4.setChecked(false);
//            }
//            if(channelModel.bChannel5){
//                channel_5.setChecked(true);
//            }else{
//                channel_5.setChecked(false);
//            }
//            if(channelModel.bChannel6){
//                channel_6.setChecked(true);
//            }else{
//                channel_6.setChecked(false);
//            }
//            if(channelModel.bChannel7){
//                channel_7.setChecked(true);
//            }else{
//                channel_7.setChecked(false);
//            }
//
//            if(channelModel.bChannel8){
//                channel_8.setChecked(true);
//            }else{
//                channel_8.setChecked(false);
//            }
//
//            if(channelModel.bChannel9){
//                channel_9.setChecked(true);
//            }else{
//                channel_9.setChecked(false);
//            }
//            if(channelModel.bChannel10){
//                channel_10.setChecked(true);
//            }else{
//                channel_10.setChecked(false);
//            }
//            if(channelModel.bChannel11){
//                channel_11.setChecked(true);
//            }else{
//                channel_11.setChecked(false);
//            }
//            if(channelModel.bChannel12){
//                channel_12.setChecked(true);
//            }else{
//                channel_12.setChecked(false);
//            }
//            if(channelModel.bChannel13){
//                channel_13.setChecked(true);
//            }else{
//                channel_13.setChecked(false);
//            }
//            if(channelModel.bChannel14){
//                channel_14.setChecked(true);
//            }else{
//                channel_14.setChecked(false);
//            }
//            if(channelModel.bChannel15){
//                channel_15.setChecked(true);
//            }else{
//                channel_15.setChecked(false);
//            }
//            if(channelModel.bChannel16){
//                channel_16.setChecked(true);
//            }else{
//                channel_16.setChecked(false);
//            }
//        }
//
//        channel_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel1=true;
//                }else{
//                    channelModel.bChannel1=false;
//                }
//            }
//        });
//
//        channel_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel2=true;
//                }else{
//                    channelModel.bChannel2=false;
//                }
//            }
//        });
//
//        channel_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel3=true;
//                }else{
//                    channelModel.bChannel3=false;
//                }
//            }
//        });
//
//        channel_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel4=true;
//                }else{
//                    channelModel.bChannel4=false;
//                }
//            }
//        });
//        channel_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel5=true;
//                }else{
//                    channelModel.bChannel5=false;
//                }
//            }
//        });
//        channel_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel6=true;
//                }else{
//                    channelModel.bChannel6=false;
//                }
//            }
//        });
//        channel_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel7=true;
//                }else{
//                    channelModel.bChannel7=false;
//                }
//            }
//        });
//        channel_8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel8=true;
//                }else{
//                    channelModel.bChannel8=false;
//                }
//            }
//        });
//        channel_9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel9=true;
//                }else{
//                    channelModel.bChannel9=false;
//                }
//            }
//        });
//        channel_10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel10=true;
//                }else{
//                    channelModel.bChannel10=false;
//                }
//            }
//        });
//        channel_11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel11=true;
//                }else{
//                    channelModel.bChannel11=false;
//                }
//            }
//        });
//        channel_12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel12=true;
//                }else{
//                    channelModel.bChannel12=false;
//                }
//            }
//        });
//        channel_13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel13=true;
//                }else{
//                    channelModel.bChannel13=false;
//                }
//            }
//        });
//        channel_14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel14=true;
//                }else{
//                    channelModel.bChannel14=false;
//                }
//            }
//        });
//        channel_15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel15=true;
//                }else{
//                    channelModel.bChannel15=false;
//                }
//            }
//        });
//        channel_16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    channelModel.bChannel16=true;
//                }else{
//                    channelModel.bChannel16=false;
//                }
//            }
//        });

    }
    private void doSave(){
        String et1=et_1.getText().toString().trim();
        if(Tools.isEmpty(et1)){
            et1="5000";
        }
        String et2=et_2.getText().toString().trim();
        if(Tools.isEmpty(et2)){
            et2="5000";
        }
        String et3=et_3.getText().toString().trim();
        if(Tools.isEmpty(et3)){
            et3="5000";
        }
        String et4=et_4.getText().toString().trim();
        if(Tools.isEmpty(et4)){
            et4="5000";
        }
        String et5=et_5.getText().toString().trim();
        if(Tools.isEmpty(et5)){
            et5="5000";
        }
        String et6=et_6.getText().toString().trim();
        if(Tools.isEmpty(et6)){
            et6="5000";
        }
        String et7=et_7.getText().toString().trim();
        if(Tools.isEmpty(et7)){
            et7="5000";
        }
        String et8=et_8.getText().toString().trim();
        if(Tools.isEmpty(et8)){
            et8="5000";
        }
        String et9=et_9.getText().toString().trim();
        if(Tools.isEmpty(et9)){
            et9="5000";
        }
        String et10=et_10.getText().toString().trim();
        if(Tools.isEmpty(et10)){
            et10="5000";
        }
        String et11=et_11.getText().toString().trim();
        if(Tools.isEmpty(et11)){
            et11="5000";
        }
        String et12=et_12.getText().toString().trim();
        if(Tools.isEmpty(et12)){
            et12="5000";
        }
        String et13=et_13.getText().toString().trim();
        if(Tools.isEmpty(et13)){
            et13="5000";
        }
        String et14=et_14.getText().toString().trim();
        if(Tools.isEmpty(et14)){
            et14="5000";
        }
        String et15=et_15.getText().toString().trim();
        if(Tools.isEmpty(et15)){
            et15="5000";
        }
        String et16=et_16.getText().toString().trim();
        if(Tools.isEmpty(et16)){
            et16="5000";
        }
        String ettime=et_time.getText().toString().trim();
        if(Tools.isEmpty(ettime)){
            ettime="25";
        }
        List<String> list =new ArrayList<>();
        list.add(et1);list.add(et2);list.add(et3);
        list.add(et4); list.add(et5); list.add(et6);
        list.add(et7); list.add(et8); list.add(et9);
        list.add(et10); list.add(et11); list.add(et12);
        list.add(et13); list.add(et14); list.add(et15);
        list.add(et16);list.add(ettime);list.add(isVideo);
        acache.put("et_number",(Serializable)list);
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    @Override
    public void onPause() {

        bt_sure.performClick();
        super.onPause();
    }

}
