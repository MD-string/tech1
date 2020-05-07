package cn.hand.tech.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.io.Serializable;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDKRModel;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.home.BaseFragment;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.ToastUtil;

/*
 *通道选择
 */
public class PassSettingFragment extends BaseFragment  {
    private Context context;
    private CheckBox channel_1,channel_2,channel_3,channel_4,channel_5,channel_6,channel_7,channel_8,channel_9,channel_10,channel_11,channel_12,channel_13,channel_14,channel_15,channel_16;
    private ACache acache;
    private HDKRModel channelModel;
    private Button bt_sure;
    private String mtag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        View view = inflater.inflate(R.layout.activity_pass_choose, container, false);
        mtag=getArguments().getString("pass_tag");
        acache= ACache.get(context, CommonUtils.TAG);
        initView(view);

        return view;
    }
    private void initView(View view) {

        bt_sure=(Button)view.findViewById(R.id.bt_sure);
        bt_sure.setVisibility(View.GONE);

        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(BleConstant.ACTION_CHANNEL_CHANGE);
                i.putExtra("bro_tag",mtag);
                context.sendBroadcast(i);
            }
        });


        channel_1=(CheckBox)view.findViewById(R.id.channel_1);
        channel_2=(CheckBox)view.findViewById(R.id.channel_2);
        channel_3=(CheckBox)view.findViewById(R.id.channel_3);
        channel_4=(CheckBox)view.findViewById(R.id.channel_4);
        channel_5=(CheckBox)view.findViewById(R.id.channel_5);
        channel_6=(CheckBox)view.findViewById(R.id.channel_6);
        channel_7=(CheckBox)view.findViewById(R.id.channel_7);
        channel_8=(CheckBox)view.findViewById(R.id.channel_8);
        channel_9=(CheckBox)view.findViewById(R.id.channel_9);
        channel_10=(CheckBox)view.findViewById(R.id.channel_10);
        channel_11=(CheckBox)view.findViewById(R.id.channel_11);
        channel_12=(CheckBox)view.findViewById(R.id.channel_12);
        channel_13=(CheckBox)view.findViewById(R.id.channel_13);
        channel_14=(CheckBox)view.findViewById(R.id.channel_14);
        channel_15=(CheckBox)view.findViewById(R.id.channel_15);
        channel_16=(CheckBox)view.findViewById(R.id.channel_16);

        channelModel = (HDKRModel)acache.getAsObject("channel_model");//保存多少通道
        if(channelModel == null ) {
            channelModel = new HDKRModel();
        }else{
            if(channelModel.bChannel1){
                channel_1.setChecked(true);
            }else{
                channel_1.setChecked(false);
            }
            if(channelModel.bChannel2){
                channel_2.setChecked(true);
            }else{
                channel_2.setChecked(false);
            }
            if(channelModel.bChannel3){
                channel_3.setChecked(true);
            }else{
                channel_3.setChecked(false);
            }
            if(channelModel.bChannel4){
                channel_4.setChecked(true);
            }else{
                channel_4.setChecked(false);
            }
            if(channelModel.bChannel5){
                channel_5.setChecked(true);
            }else{
                channel_5.setChecked(false);
            }
            if(channelModel.bChannel6){
                channel_6.setChecked(true);
            }else{
                channel_6.setChecked(false);
            }
            if(channelModel.bChannel7){
                channel_7.setChecked(true);
            }else{
                channel_7.setChecked(false);
            }

            if(channelModel.bChannel8){
                channel_8.setChecked(true);
            }else{
                channel_8.setChecked(false);
            }

            if(channelModel.bChannel9){
                channel_9.setChecked(true);
            }else{
                channel_9.setChecked(false);
            }
            if(channelModel.bChannel10){
                channel_10.setChecked(true);
            }else{
                channel_10.setChecked(false);
            }
            if(channelModel.bChannel11){
                channel_11.setChecked(true);
            }else{
                channel_11.setChecked(false);
            }
            if(channelModel.bChannel12){
                channel_12.setChecked(true);
            }else{
                channel_12.setChecked(false);
            }
            if(channelModel.bChannel13){
                channel_13.setChecked(true);
            }else{
                channel_13.setChecked(false);
            }
            if(channelModel.bChannel14){
                channel_14.setChecked(true);
            }else{
                channel_14.setChecked(false);
            }
            if(channelModel.bChannel15){
                channel_15.setChecked(true);
            }else{
                channel_15.setChecked(false);
            }
            if(channelModel.bChannel16){
                channel_16.setChecked(true);
            }else{
                channel_16.setChecked(false);
            }
        }

        channel_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel1=true;
                }else{
                    channelModel.bChannel1=false;
                }
            }
        });

        channel_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel2=true;
                }else{
                    channelModel.bChannel2=false;
                }
            }
        });

        channel_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel3=true;
                }else{
                    channelModel.bChannel3=false;
                }
            }
        });

        channel_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel4=true;
                }else{
                    channelModel.bChannel4=false;
                }
            }
        });
        channel_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel5=true;
                }else{
                    channelModel.bChannel5=false;
                }
            }
        });
        channel_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel6=true;
                }else{
                    channelModel.bChannel6=false;
                }
            }
        });
        channel_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel7=true;
                }else{
                    channelModel.bChannel7=false;
                }
            }
        });
        channel_8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel8=true;
                }else{
                    channelModel.bChannel8=false;
                }
            }
        });
        channel_9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel9=true;
                }else{
                    channelModel.bChannel9=false;
                }
            }
        });
        channel_10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel10=true;
                }else{
                    channelModel.bChannel10=false;
                }
            }
        });
        channel_11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel11=true;
                }else{
                    channelModel.bChannel11=false;
                }
            }
        });
        channel_12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel12=true;
                }else{
                    channelModel.bChannel12=false;
                }
            }
        });
        channel_13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel13=true;
                }else{
                    channelModel.bChannel13=false;
                }
            }
        });
        channel_14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel14=true;
                }else{
                    channelModel.bChannel14=false;
                }
            }
        });
        channel_15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel15=true;
                }else{
                    channelModel.bChannel15=false;
                }
            }
        });
        channel_16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    channelModel.bChannel16=true;
                }else{
                    channelModel.bChannel16=false;
                }
            }
        });

    }

    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    @Override
    public void onPause() {
        acache.put("channel_model",(Serializable) channelModel);
        bt_sure.performClick();
        super.onPause();
    }

}
