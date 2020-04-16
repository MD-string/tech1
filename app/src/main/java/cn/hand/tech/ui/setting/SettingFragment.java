package cn.hand.tech.ui.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ui.home.BaseFragment;
import cn.hand.tech.ui.home.MainFragmentActivity;

/**
 *  设定
 * @author hxz
 */

public class SettingFragment extends BaseFragment implements OnClickListener {

	private BroadcastReceiver receiver;
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();

	private LinearLayout ll_pass,ll_xishu,ll_jixie,ll_bind,ll_danwei,ll_sef;
	private MainFragmentActivity mactivity;
	private LinearLayout ll_clear_zero;
	private LinearLayout ll_save_switch;
	private LinearLayout ll_default_weight;
	private LinearLayout ll_checked;
	private ScrollView sc_1;
	private LinearLayout ll_pass_checked;
	private LinearLayout ll_car_input;
	private LinearLayout ll_repair;
	private LinearLayout ll_address;
	private LinearLayout ll_reload;
	private LinearLayout ll_auto_check;
	private LinearLayout ll_update_bin;
	private LinearLayout ll_plant_enter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mactivity=	(MainFragmentActivity)getActivity();
		getTopNavigation().setNavLineVisibility(View.GONE);
		final View view = inflater.inflate(R.layout.frg_setting, container, false);

		initView(view);
		registerBrodcat();


		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initView(View view) {
		sc_1=(ScrollView)view.findViewById(R.id.sc_1);
		sc_1.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		ll_auto_check=(LinearLayout)view.findViewById(R.id.ll_auto_check);
		ll_update_bin=(LinearLayout)view.findViewById(R.id.ll_update_bin);

		ll_plant_enter=(LinearLayout)view.findViewById(R.id.ll_plant_enter);

		ll_pass=(LinearLayout)view.findViewById(R.id.ll_pass);//通道选择
		ll_pass_checked=(LinearLayout)view.findViewById(R.id.ll_pass_checked);
		ll_checked=(LinearLayout)view.findViewById(R.id.ll_checked);//通道和mv/v检测
		ll_car_input=(LinearLayout)view.findViewById(R.id.ll_car_input); //车辆录入
		ll_xishu=(LinearLayout)view.findViewById(R.id.ll_xishu);//系数设定
		ll_address=(LinearLayout)view.findViewById(R.id.ll_address);//地址设定
		ll_jixie=(LinearLayout)view.findViewById(R.id.ll_jixie);//机械臂系数
		ll_clear_zero=(LinearLayout)view.findViewById(R.id.ll_clear_zero);//清零设置
		ll_bind=(LinearLayout)view.findViewById(R.id.ll_bind);//绑定车牌
		ll_repair=(LinearLayout)view.findViewById(R.id.ll_repair);//维修记录
		ll_save_switch=(LinearLayout)view.findViewById(R.id.ll_save_switch);
		ll_danwei=(LinearLayout)view.findViewById(R.id.ll_danwei);//重量单位
		ll_sef=(LinearLayout)view.findViewById(R.id.ll_sef);//关于我们
		ll_default_weight=(LinearLayout)view.findViewById(R.id.ll_default_weight);//重量设置

		ll_reload=(LinearLayout)view.findViewById(R.id.ll_reload);

		ll_auto_check.setOnClickListener(this);
		ll_update_bin.setOnClickListener(this);
		ll_plant_enter.setOnClickListener(this);
		ll_pass_checked.setOnClickListener(this);
		ll_pass.setOnClickListener(this);
		ll_checked.setOnClickListener(this);
		ll_address.setOnClickListener(this);
		ll_car_input.setOnClickListener(this);
		ll_xishu.setOnClickListener(this);
		ll_jixie.setOnClickListener(this);
		ll_bind.setOnClickListener(this);
		ll_repair.setOnClickListener(this);
		ll_save_switch.setOnClickListener(this);
		ll_clear_zero.setOnClickListener(this);
		ll_danwei.setOnClickListener(this);
		ll_sef.setOnClickListener(this);
		ll_default_weight.setOnClickListener(this);
		ll_reload.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ll_auto_check:
				AutoCheckAct.start(mactivity);
				break;
			case R.id.ll_update_bin:
				UpBinAct.start(mactivity);
				break;
			case R.id.ll_plant_enter:
				EnterPlantCheckAct.start(mactivity);
				break;
			case R.id.ll_pass:
				PassSettingActNew.start(mactivity,"0");
				break;
			case R.id.ll_pass_checked:
				PassCheckAct.start(mactivity);
				break;
			case R.id.ll_address:
				IPAddressAct.start(mactivity);
				break;
			case R.id.ll_checked:
				MvvCheckAct.start(mactivity);
				break;
			case R.id.ll_car_input:
//				CarInputAct.start(mactivity);
				break;
			case R.id.ll_xishu:
				CoefficientSettingAct.start(mactivity);
				break;
//			case R.id.ll_jixie:
//				ArmAct.start(mactivity);
//				break;
			case R.id.ll_clear_zero:
				ClearZeroSettingAct.start(mactivity);
				break;
			case R.id.ll_bind:
				BindCarNOAct.start(mactivity);
				break;
			case R.id.ll_repair://维修记录
				RepairAct.start(mactivity);
				break;
			case R.id.ll_save_switch:
				SaveBtnAct.start(mactivity);
				break;
			case R.id.ll_danwei:
				UnitSettingAct.start(mactivity);
				break;
			case R.id.ll_sef:
				AboutUsAct.start(mactivity);
				break;
			case R.id.ll_default_weight:
				WeightSettingAct.start(mactivity);
				break;
			case R.id.ll_reload://系统重启
				ReloadSysAct.start(mactivity);
				break;
		}

	}


	/**
	 * 注册广播
	 */
	private void registerBrodcat() {
		receiver=new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action=intent.getAction();

			}

		};
		IntentFilter filter = new IntentFilter();
		getActivity().registerReceiver(receiver, filter);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			getActivity().unregisterReceiver(receiver);
			receiver = null;
		}

	}
	//fragment刷新数据
	@Override
	public void onBaseRefresh() {

	}
}
