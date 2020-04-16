package cn.hand.tech.ui.data;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDKRModel;
import cn.hand.tech.bean.HDSendDataModel;
import cn.hand.tech.bean.WeightDataBean;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.ConsTantsCode;
import cn.hand.tech.dao.WeightDao;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.data.adapter.ListViewAddAdapter;
import cn.hand.tech.ui.data.bean.LocalDataTimeModel;
import cn.hand.tech.ui.data.presenter.DataPresenter;
import cn.hand.tech.ui.data.presenter.SaveMVVData;
import cn.hand.tech.ui.home.BaseFragment;
import cn.hand.tech.ui.home.MainFragmentActivity;
import cn.hand.tech.ui.weight.presenter.SaveWeightDataTask;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.DialogUtil;
import cn.hand.tech.utils.EditTextUtil;
import cn.hand.tech.utils.MyPopupWindowUtil;
import cn.hand.tech.utils.SPUtil;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/**
 *  数据
 * @author hxz
 */

public class DataFragment extends BaseFragment implements OnClickListener,ListViewAddAdapter.Callback,IDataView {

	private BroadcastReceiver receiver;
	private TextView tv_et;
	private ListView list_item;
	private Button btnSend;
	private Button btn_send_email;
	private Button btn_local_data;
	private TextView tv_B,tv_K;
	MainFragmentActivity mactivity;
	private ACache acache;
	private static final String TAG = "DataFragment";
	private HDKRModel channelModel;//通道数量
	private ListViewAddAdapter listViewAdapter;
	private List<WeightDataBean> listData;
	private boolean isEditor = true;
	private DataPresenter mpresenter;
	private HDKRModel kModel;
	private HDKRModel bModel;
	private ProgressDialog progressDialog;
	private String kv,bv;
	private int mType;
	private boolean isConnected;
	private List<WeightDataBean> listWeightData;
	private ProgressDialog dialog;
	private boolean isLocation = false;
	private String mValue = "";
	private int editPosition;
	private EditText mEtInput;
	private AlertDialog tempDialog;
	private String time1;//发送邮件时间
	private boolean isSend= false;
	private String truckNum;//车牌

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mactivity=	(MainFragmentActivity)getActivity();
		getTopNavigation().setNavLineVisibility(View.GONE);
		final View view = inflater.inflate(R.layout.fragment_data, container, false);

		acache= ACache.get(mactivity,"WeightFragment");
		truckNum= acache.getAsString("car_num");
		mpresenter=new DataPresenter(mactivity,this);
		MainFragmentActivity act=	(MainFragmentActivity)getActivity();
		initView(view);
		init();
		registerBrodcat();

		return view;
	}

//	@Override
//	public void onResume() {
//	    doshowDialog();
//		super.onResume();
//	}

	private void initView(View view) {
		tv_et=(TextView)view.findViewById(R.id.tv_et);//编辑
		list_item=(ListView)view.findViewById(R.id.save_list_item);
		btnSend = (Button) view.findViewById(R.id.btn_save_send);//计算系数
		btn_send_email = (Button) view.findViewById(R.id.btn_send_email);//发送邮件
		btn_local_data = (Button) view.findViewById(R.id.btn_local_data);//本地数据

		tv_B = (TextView) view.findViewById(R.id.save_b_label);
		tv_K = (TextView) view.findViewById(R.id.save_k_label);

		tv_et.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		btn_send_email.setOnClickListener(this);
		btn_local_data.setOnClickListener(this);
	}

	private void init() {
		setKBValue();
		kModel = new HDKRModel();
		bModel = new HDKRModel();
		channelModel = (HDKRModel)acache.getAsObject("channel_model");//保存多少通道

		listData= WeightDao.getInstance(mactivity).findWeightDataBeans("0");
		if(listData ==null){
			listData=new ArrayList<>();
		}
		listViewAdapter = new ListViewAddAdapter(mactivity, listData, channelModel, this);
		list_item.setAdapter(listViewAdapter);
		list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!isEditor) {
					DLog.e(TAG,"点击的位置position==" + position);
					ListViewAddAdapter.ViewHolder views = (ListViewAddAdapter.ViewHolder) view.getTag();
					views.cb_weight_item.toggle();
					CheckBox delCheckBox = views.cb_weight_item;
					isEditor = false;
					listViewAdapter.isMapSelected.put(position, delCheckBox.isChecked());
					listViewAdapter.delContactsIdSet.add(position);
					listViewAdapter.changeSelected(listData, isEditor);

				}
			}
		});
	}


	private void setKBValue() {
		String kValue = (String) SPUtil.get(getActivity(), "kValue", "[0.0000000]");
		String bValue = (String) SPUtil.get(getActivity(), "bValue", "[0.0000000]");
		tv_K.setText(kValue);
		tv_B.setText(bValue);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tv_et://编辑
				deleteListData();
				break;
			case R.id.btn_save_send://   /*计算系数、发送给服务器，*/
				//从服务器获取KB值
				dealSendData();
				break;
			/*K值设定*/
			case R.id.tv_k_set:
				showWriteView(0);
				break;
			/*B值设定*/
			case R.id.tv_b_set:
				showWriteView(1);
				break;
			/*取消*/
			case R.id.tv_kb_cancel:
				if(!DataFragment.this.isHidden()){
					if(MyPopupWindowUtil.popupWindow !=null){
						MyPopupWindowUtil.popupWindow.dismiss();
					}
				}
				break;
			/*发送邮件*/
			case R.id.btn_send_email:
				sendEmail();
				break;
			/*本地数据*/
			case R.id.btn_local_data:
				//				getActivity().unregisterReceiver(receiver);
				CommonKitUtil.startActivityForResult(getActivity(), LocalDataActivity.class, new Bundle(), 1003);
				break;
			case R.id.tv_ok:
				if (TextUtils.isEmpty(mEtInput.getText().toString())) {
					showTips("值不能为空");
					return;
				}
				mValue = mEtInput.getText().toString();
				if (isLocation) {
					listData.get(editPosition).setLocation(Integer.parseInt(mValue)+"");
					SPUtil.put(getActivity(), "location", mValue);
					WeightDao.getInstance(mactivity) .update(listData.get(editPosition));
				} else {
					listData.get(editPosition).setWeightFromReal(mValue) ;
					SPUtil.put(getActivity(), "weight", mValue);
					WeightDao.getInstance(mactivity).update(listData.get(editPosition));
				}
				listViewAdapter.changeSelected(listData, true);
				tempDialog.dismiss();
				break;
			//
			case R.id.tv_cancel:
				tempDialog.dismiss();
				break;
		}

	}
	@SuppressLint("HandlerLeak")
	Handler mhandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (getActivity() == null || getActivity().isFinishing()) {
				return;
			}
			switch (msg.what) {
				case 1:
					closeProgressDialog();
					tv_B.setText(bv + "");
					tv_K.setText(kv + "");
					SPUtil.put(getActivity(), "bValue", bv);
					SPUtil.put(getActivity(), "kValue", kv);

					showKBSetDialog();
					showTips("计算成功");
					break;
				/*保存本地成功再发送给服务器*/
				case ConsTantsCode.REQUEST_CODE_SAVE_LOCAL_SUCCESS:
					dialog = new ProgressDialog(getActivity());
					DialogUtil.setProgressDialog(dialog, "上传数据到服务器...");
					SaveWeightDataTask.getInstance(getActivity(),mhandler).SaveData(listWeightData);
					break;
				/*保存本地失败*/
				case ConsTantsCode.REQUEST_CODE_SAVE_LOCAL_FAIL:
					dialog.dismiss();
					break;
				case 10://保存到本地
					if(listWeightData !=null && listWeightData.size()  > 0){
						for(int g=0;g<listWeightData.size();g++){
							listWeightData.get(g).setTag("1");
						}
						acache.put("list_weight_data",(Serializable)listWeightData);
					}else{
						DLog.d(TAG,"listWeightData数据为空");
					}

					for (int i = 0; i < listData.size(); i++) {
						WeightDao.getInstance(mactivity).deleteWeightDataBean(listData.get(i).getUploadDate(),"0");
					}
					listData = 	WeightDao.getInstance(mactivity).findWeightDataBeans("0");
					List<WeightDataBean>list=(List<WeightDataBean>)acache.getAsObject("list_weight_data");
					if(list !=null && list.size()>0){
						for(int i=0;i<list.size();i++){
							WeightDao.getInstance(mactivity).updateOrInsert(list.get(i));
						}
						showTips("保存成功");
					}else{
						DLog.e(TAG,"存储数据有问题==" );
					}
					tv_et.setText("编辑");
					isEditor = true;
					isSend=false;
					listViewAdapter.delContactsIdSet.clear();
					listViewAdapter.initData();
					listViewAdapter.changeSelected(listData, isEditor);

//					EmailStateAct.start(mactivity,time1);
					break;
				/*发送服务器成功后再删除原来的*/
				case ConsTantsCode.REQUEST_CODE_SUCCESS:

					String message = (String) msg.obj;
					showTips(message);
					if(dialog !=null){
						dialog.dismiss();
					}
					break;
				/*发送服务器失败*/
				case ConsTantsCode.REQUEST_CODE_FAIL:
					if(dialog !=null){
						dialog.dismiss();
					}
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 注册广播
	 */
	private void registerBrodcat() {
		receiver=new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action=intent.getAction();
				if(action.equals(BleConstant.ACTION_SAVE_SUCCESS)){
					//					listData= WeightDao.getInstance(mactivity).findWeightDataBeans("0");
					//					listViewAdapter.changeSelected(listData,true);
				}else if(action.equals(BleConstant.ACTION_SEND_DATA)){
					SPUtil.put(getActivity(), "kbType", mType);
					if (mType == 0) {
						SPUtil.put(getActivity(), "kValue1", kModel.channel1);
						SPUtil.put(getActivity(), "kValue2", kModel.channel2);
						SPUtil.put(getActivity(), "kValue3", kModel.channel3);
						SPUtil.put(getActivity(), "kValue4", kModel.channel4);
						SPUtil.put(getActivity(), "kValue5", kModel.channel5);
						SPUtil.put(getActivity(), "kValue6", kModel.channel6);
						SPUtil.put(getActivity(), "kValue7", kModel.channel7);
						SPUtil.put(getActivity(), "kValue8", kModel.channel8);
						SPUtil.put(getActivity(), "kValue9", kModel.channel9);
						SPUtil.put(getActivity(), "kValue10", kModel.channel10);
						SPUtil.put(getActivity(), "kValue11", kModel.channel11);
						SPUtil.put(getActivity(), "kValue12", kModel.channel12);
						SPUtil.put(getActivity(), "kValue13", kModel.channel13);
						SPUtil.put(getActivity(), "kValue14", kModel.channel14);
						SPUtil.put(getActivity(), "kValue15", kModel.channel15);
						SPUtil.put(getActivity(), "kValue16", kModel.channel16);
						showTips("K值设置成功");
					} else {
						SPUtil.put(getActivity(), "bValue1", bModel.channel1);
						SPUtil.put(getActivity(), "bValue2", bModel.channel2);
						SPUtil.put(getActivity(), "bValue3", bModel.channel3);
						SPUtil.put(getActivity(), "bValue4", bModel.channel4);
						SPUtil.put(getActivity(), "bValue5", bModel.channel5);
						SPUtil.put(getActivity(), "bValue6", bModel.channel6);
						SPUtil.put(getActivity(), "bValue7", bModel.channel7);
						SPUtil.put(getActivity(), "bValue8", bModel.channel8);
						SPUtil.put(getActivity(), "bValue9", bModel.channel9);
						SPUtil.put(getActivity(), "bValue10", bModel.channel10);
						SPUtil.put(getActivity(), "bValue11", bModel.channel11);
						SPUtil.put(getActivity(), "bValue12", bModel.channel12);
						SPUtil.put(getActivity(), "bValue13", bModel.channel13);
						SPUtil.put(getActivity(), "bValue14", bModel.channel14);
						SPUtil.put(getActivity(), "bValue15", bModel.channel15);
						SPUtil.put(getActivity(), "bValue16", bModel.channel16);
						showTips("B值设置成功");
					}
					if(!DataFragment.this.isHidden()){
						if(MyPopupWindowUtil.popupWindow !=null){
							MyPopupWindowUtil.popupWindow.dismiss();
						}
					}

				}else if(action.equals(BleConstant.ACTION_CHANNEL_CHANGE)){//通道选择
					channelModel = (HDKRModel)acache.getAsObject("channel_model");//保存多少通道
					listViewAdapter.setChannelChange(listData,channelModel);
				}

			}

		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(BleConstant.ACTION_SAVE_SUCCESS);
		filter.addAction(BleConstant.ACTION_SEND_DATA);
		filter.addAction(BleConstant.ACTION_CHANNEL_CHANGE);
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
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	//fragment刷新数据
	@Override
	public void onBaseRefresh() {
		isEditor = true;
		tv_et.setText("编辑");
		listData= WeightDao.getInstance(mactivity).findWeightDataBeans("0");
		listViewAdapter.changeSelected(listData,true);
	}

	@Override
	public void toclick(View view) {
		switch (view.getId()) {
			/*修改位置*/
			case R.id.ll_item_location:
				isLocation = true;
				int position = (Integer) view.getTag();//得到点击的位置
				mValue = String.valueOf(SPUtil.get(getActivity(), "location", "0"));
				editPosition = position;
				showDialog("请输入实际位置");
				break;
			/*修改重量*/
			case R.id.rl_w:
				isLocation = false;
				mValue = String.valueOf(SPUtil.get(getActivity(), "weight", "0"));
				int i = (Integer) view.getTag();//得到点击的位置
				editPosition = i;
				String str=acache.getAsString("weight_unit");
				String unit="公斤";
				if(Tools.isEmpty(str)){
					unit="公斤";
				}else{
					unit=str;
				}
				showDialog("请输入实际重量("+unit+")");
				break;

		}
	}
	public void  showTips(String tip){
		if(!DataFragment.this.isHidden()){
			ToastUtil.getInstance().showCenterMessage(mactivity,tip);
		}
	}
	public void showDialog(String title) {
		final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        /*dialog.setTitle("提示");
        dialog.setMessage(message);*/
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_input, null);
		mEtInput = (EditText) view.findViewById(R.id.et_input_weight);
		TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
		tv_title.setText(title);
		ImageView iv_clear = (ImageView) view.findViewById(R.id.iv_clear);
		mEtInput.setText(mValue + "");
		mEtInput.setSelection(mEtInput.getText().length());
		tv_ok.setOnClickListener(this);
		tv_cancel.setOnClickListener(this);
		EditTextUtil.autoClear(mEtInput, iv_clear);
		dialog.setView(view);//给对话框添加一个EditText输入文本框
		//下面是弹出键盘的关键处
		tempDialog = dialog.create();
		tempDialog.setView(view, 0, 0, 0, 0);
		tempDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			public void onShow(DialogInterface dialog) {
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(mactivity.INPUT_METHOD_SERVICE);
				imm.showSoftInput(mEtInput, InputMethodManager.SHOW_IMPLICIT);
			}
		});
		tempDialog.show();
	}


	private void deleteListData() {
		if (listData == null || listData.size() == 0) {
			showTips("没有可编辑的数据");
			return;
		}
		if (isEditor) {
			isEditor = false;
			tv_et.setText("删除");
			for (int j = 0; j < listData.size(); j++) {
				listViewAdapter.isMapSelected.put(j, false);
				//				listViewAdapter.delContactsIdSet.add(j);
			}
			listViewAdapter.changeSelected(listData, isEditor);

		} else  {
			//TreeSet有序的集合，会Set和HashSet无序集合；
			TreeSet<Integer> setDelete = listViewAdapter.delContactsIdSet;
			if(setDelete == null || setDelete.size() ==0){
				showTips("没有可删除的数据");
				return;
			}
			isEditor = true;
			tv_et.setText("编辑");
			Iterator<Integer> iterator = setDelete.iterator();
			while (iterator.hasNext()) {
				int position = iterator.next();//不能两次iterator.next()
				DLog.e(TAG,"删除列表的位置position==" + position);
				if (setDelete.size() > listData.size()) {
					return;
				}
				WeightDao.getInstance(mactivity).deleteWeightDataBean(listData.get(position).getUploadDate(),"0");
			}
			listData = WeightDao.getInstance(mactivity).findWeightDataBeans("0");
			listViewAdapter.delContactsIdSet.clear();
			listViewAdapter.initData();
			listViewAdapter.changeSelected(listData, isEditor);
			showTips("删除成功");

		}
	}
	private float getMvvValue(float ad, float zero) {
		float x = (ad - zero) / 5;
		DecimalFormat myformat = new DecimalFormat("0.000000");
		String str = myformat.format(x);
		float mvvValue = Float.parseFloat(str);
		return mvvValue;
	}
	/*系数设定*/
	private void dealSendData() {
		if (!CommonKitUtil.isNetworkAvailable(getActivity())) {
			showTips("网络未连接，请检查您的网络连接情况");
			return;
		}

		int nDataCount = listData.size();
		int myChannelCount = getMyChannelCount();

		if (myChannelCount == 0) {
			showTips("请选择通道数");
			return;
		}
		if (nDataCount < (myChannelCount + 1)) {
			String str = String.format("数据少于%d组", myChannelCount + 1);
			showTips(str);
			return;
		}
		String post_weights = "";
		String post_mvv1s = "";
		for (int i = 0; i < nDataCount; i++) {
			WeightDataBean bean =listData.get(i);
			post_weights = post_weights + "," + bean.getWeightFromReal();
			if (channelModel.bChannel1) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch1()), Float.parseFloat(bean.getSch1()));
			}
			if (channelModel.bChannel2) {
				post_mvv1s = post_mvv1s + "," +getMvvValue(Float.parseFloat(bean.getAch2()), Float.parseFloat(bean.getSch2()));

			}
			if (channelModel.bChannel3) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch3()), Float.parseFloat(bean.getSch3()));

			}
			if (channelModel.bChannel4) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch4()), Float.parseFloat(bean.getSch4()));

			}
			if (channelModel.bChannel5) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch5()), Float.parseFloat(bean.getSch5()));

			}
			if (channelModel.bChannel6) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch6()), Float.parseFloat(bean.getSch6()));
			}
			if (channelModel.bChannel7) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch7()), Float.parseFloat(bean.getSch7()));
			}
			if (channelModel.bChannel8) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch8()), Float.parseFloat(bean.getSch8()));
			}
			if (channelModel.bChannel9) {
				post_mvv1s = post_mvv1s + "," +getMvvValue(Float.parseFloat(bean.getAch9()), Float.parseFloat(bean.getSch9()));
			}
			if (channelModel.bChannel10) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch10()), Float.parseFloat(bean.getSch10()));
			}
			if (channelModel.bChannel11) {
				post_mvv1s = post_mvv1s + "," +getMvvValue(Float.parseFloat(bean.getAch11()), Float.parseFloat(bean.getSch11()));
			}
			if (channelModel.bChannel12) {
				post_mvv1s = post_mvv1s + "," +getMvvValue(Float.parseFloat(bean.getAch12()), Float.parseFloat(bean.getSch12()));
			}
			if (channelModel.bChannel13) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch13()), Float.parseFloat(bean.getSch13()));
			}
			if (channelModel.bChannel14) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch14()), Float.parseFloat(bean.getSch14()));
			}
			if (channelModel.bChannel15) {
				post_mvv1s = post_mvv1s + "," + getMvvValue(Float.parseFloat(bean.getAch15()), Float.parseFloat(bean.getSch15()));
			}
			if (channelModel.bChannel16) {
				post_mvv1s = post_mvv1s + "," +getMvvValue(Float.parseFloat(bean.getAch16()), Float.parseFloat(bean.getSch16()));
			}
		}
		String strMV = post_mvv1s.substring(1);
		String postWeight = post_weights.substring(1);

		DLog.e(TAG,"系数设定postWeight==" + postWeight);
		DLog.e(TAG,"系数设定strMV==" + strMV);
		DLog.e(TAG,"系数设定myChannelCount==" + myChannelCount);
		mpresenter.saveData(postWeight,strMV + "",myChannelCount + "");
	}

	private int getMyChannelCount() {
		int i = 0;
		if (channelModel.bChannel1) {
			++i;
		}
		if (channelModel.bChannel2) {
			++i;
		}
		if (channelModel.bChannel3) {
			++i;
		}
		if (channelModel.bChannel4) {
			++i;
		}
		if (channelModel.bChannel5) {
			++i;
		}
		if (channelModel.bChannel6) {
			++i;
		}
		if (channelModel.bChannel7) {
			++i;
		}
		if (channelModel.bChannel8) {
			++i;
		}
		if (channelModel.bChannel9) {
			++i;
		}
		if (channelModel.bChannel10) {
			++i;
		}
		if (channelModel.bChannel11) {
			++i;
		}
		if (channelModel.bChannel12) {
			++i;
		}
		if (channelModel.bChannel13) {
			++i;
		}
		if (channelModel.bChannel14) {
			++i;
		}
		if (channelModel.bChannel15) {
			++i;
		}
		if (channelModel.bChannel16) {
			++i;
		}
		return i;

	}

	@Override
	public void loginError(String msg) {
		closeProgressDialog();
		showTips(msg);
	}

	@Override
	public void loginSuccess(float[] kValueData, String K_values, float[] bValueData, String B_values) {
		new Thread() {
			public void run() {
				SaveWeightDataTask.getInstance(mactivity, mhandler).SaveData(listData); //发送网络
			}
		}.start();

		int tmpSetKRValueCount = 0;
		kv=K_values;
		bv=B_values;
		if (channelModel.bChannel1) {
			kModel.bChannel1 = true;
			bModel.bChannel1 = true;
			kModel.channel1 = kValueData[tmpSetKRValueCount];
			bModel.channel1 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel1 = false;
			bModel.bChannel1 = false;
			kModel.channel1 = 0;
			bModel.channel1 = 0;
		}
		if (channelModel.bChannel2) {
			kModel.bChannel2 = true;
			bModel.bChannel2 = true;
			kModel.channel2 = kValueData[tmpSetKRValueCount];
			bModel.channel2 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel2 = false;
			bModel.bChannel2 = false;
			kModel.channel2 = 0;
			bModel.channel2 = 0;
		}
		if (channelModel.bChannel3) {
			kModel.bChannel3 = true;
			bModel.bChannel3 = true;
			kModel.channel3 = kValueData[tmpSetKRValueCount];
			bModel.channel3 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel3 = false;
			bModel.bChannel3 = false;
			kModel.channel3 = 0;
			bModel.channel3 = 0;
		}
		if (channelModel.bChannel4) {
			kModel.bChannel4 = true;
			bModel.bChannel4 = true;
			kModel.channel4 = kValueData[tmpSetKRValueCount];
			bModel.channel4 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel4 = false;
			bModel.bChannel4 = false;
			kModel.channel4 = 0;
			bModel.channel4 = 0;
		}
		if (channelModel.bChannel5) {
			kModel.bChannel5 = true;
			bModel.bChannel5 = true;
			kModel.channel5 = kValueData[tmpSetKRValueCount];
			bModel.channel5 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel5 = false;
			bModel.bChannel5 = false;
			kModel.channel5 = 0;
			bModel.channel5 = 0;
		}
		if (channelModel.bChannel6) {
			kModel.bChannel6 = true;
			bModel.bChannel6 = true;
			kModel.channel6 = kValueData[tmpSetKRValueCount];
			bModel.channel6 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel6 = false;
			bModel.bChannel6 = false;
			kModel.channel6 = 0;
			bModel.channel6 = 0;
		}
		if (channelModel.bChannel7) {
			kModel.bChannel7 = true;
			bModel.bChannel7 = true;
			kModel.channel7 = kValueData[tmpSetKRValueCount];
			bModel.channel7 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel7 = false;
			bModel.bChannel7 = false;
			kModel.channel7 = 0;
			bModel.channel7 = 0;
		}
		if (channelModel.bChannel8) {
			kModel.bChannel8 = true;
			bModel.bChannel8 = true;
			kModel.channel8 = kValueData[tmpSetKRValueCount];
			bModel.channel8 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel8 = false;
			bModel.bChannel8 = false;
			kModel.channel8 = 0;
			bModel.channel8 = 0;
		}
		if (channelModel.bChannel9) {
			kModel.bChannel9 = true;
			bModel.bChannel9 = true;
			kModel.channel9 = kValueData[tmpSetKRValueCount];
			bModel.channel9 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel9 = false;
			bModel.bChannel9 = false;
			kModel.channel9 = 0;
			bModel.channel9 = 0;
		}
		if (channelModel.bChannel10) {
			kModel.bChannel10 = true;
			bModel.bChannel10 = true;
			kModel.channel10 = kValueData[tmpSetKRValueCount];
			bModel.channel10 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel10 = false;
			bModel.bChannel10 = false;
			kModel.channel10 = 0;
			bModel.channel10 = 0;
		}
		if (channelModel.bChannel11) {
			kModel.bChannel11 = true;
			bModel.bChannel11 = true;
			kModel.channel11 = kValueData[tmpSetKRValueCount];
			bModel.channel11 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel11 = false;
			bModel.bChannel11 = false;
			kModel.channel11 = 0;
			bModel.channel11 = 0;
		}
		if (channelModel.bChannel12) {
			kModel.bChannel12 = true;
			bModel.bChannel12 = true;
			kModel.channel12 = kValueData[tmpSetKRValueCount];
			bModel.channel12 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel12 = false;
			bModel.bChannel12 = false;
			kModel.channel12 = 0;
			bModel.channel12 = 0;
		}
		if (channelModel.bChannel13) {
			kModel.bChannel13 = true;
			bModel.bChannel13 = true;
			kModel.channel13 = kValueData[tmpSetKRValueCount];
			bModel.channel13 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel13 = false;
			bModel.bChannel13 = false;
			kModel.channel13 = 0;
			bModel.channel13 = 0;
		}
		if (channelModel.bChannel14) {
			kModel.bChannel14 = true;
			bModel.bChannel14 = true;
			kModel.channel14 = kValueData[tmpSetKRValueCount];
			bModel.channel14 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel14 = false;
			bModel.bChannel14 = false;
			kModel.channel14 = 0;
			bModel.channel14 = 0;
		}
		if (channelModel.bChannel15) {
			kModel.bChannel15 = true;
			bModel.bChannel15 = true;
			kModel.channel15 = kValueData[tmpSetKRValueCount];
			bModel.channel15 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel15 = false;
			bModel.bChannel15 = false;
			kModel.channel15 = 0;
			bModel.channel15 = 0;
		}
		if (channelModel.bChannel16) {
			kModel.bChannel16 = true;
			bModel.bChannel16 = true;
			kModel.channel16 = kValueData[tmpSetKRValueCount];
			bModel.channel16 = bValueData[tmpSetKRValueCount];
			tmpSetKRValueCount++;
		} else {
			kModel.bChannel16 = false;
			bModel.bChannel16 = false;
			kModel.channel16 = 0;
			bModel.channel16 = 0;
		}

		mhandler.sendEmptyMessage(1);
	}


	/*KB值设定对话框*/
	private void showKBSetDialog() {
		LayoutInflater factory = LayoutInflater.from(mactivity);
		View kbDialogView = factory.inflate(R.layout.layout_kb_set_pop, null);
		TextView tv_k_set = (TextView) kbDialogView.findViewById(R.id.tv_k_set);
		TextView tv_b_set = (TextView) kbDialogView.findViewById(R.id.tv_b_set);
		TextView tv_kb_cancel = (TextView) kbDialogView.findViewById(R.id.tv_kb_cancel);
		MyPopupWindowUtil.setPopByWrapContent(getActivity(), 0.5f, kbDialogView, Gravity.CENTER);

		tv_k_set.setOnClickListener(this);
		tv_b_set.setOnClickListener(this);
		tv_kb_cancel.setOnClickListener(this);
	}

	/*KB值设定*/
	private void showWriteView(final int nType) {
		mType = nType;
		String  isCon=acache.getAsString("is_connect");
		if("2".equals(isCon)){
			isConnected=true;
		}else{
			isConnected=false;
		}
		if (isConnected) {
			HDSendDataModel model = new HDSendDataModel();
			if (nType == 0) {
				//"K值参数"
				model.mmv1 = kModel.channel1;
				model.mmv2 = kModel.channel2;
				model.mmv3 = kModel.channel3;
				model.mmv4 = kModel.channel4;
				model.mmv5 = kModel.channel5;
				model.mmv6 = kModel.channel6;
				model.mmv7 = kModel.channel7;
				model.mmv8 = kModel.channel8;
				model.mmv9 = kModel.channel9;
				model.mmv10 = kModel.channel10;
				model.mmv11 = kModel.channel11;
				model.mmv12 = kModel.channel12;
				model.mmv13 = kModel.channel13;
				model.mmv14 = kModel.channel14;
				model.mmv15 = kModel.channel15;
				model.mmv16 = kModel.channel16;
			} else {
				//"B值参数"
				model.mmv1 = bModel.channel1;
				model.mmv2 = bModel.channel2;
				model.mmv3 = bModel.channel3;
				model.mmv4 = bModel.channel4;
				model.mmv5 = bModel.channel5;
				model.mmv6 = bModel.channel6;
				model.mmv7 = bModel.channel7;
				model.mmv8 = bModel.channel8;
				model.mmv9 = bModel.channel9;
				model.mmv10 = bModel.channel10;
				model.mmv11 = bModel.channel11;
				model.mmv12 = bModel.channel12;
				model.mmv13 = bModel.channel13;
				model.mmv14 = bModel.channel14;
				model.mmv15 = bModel.channel15;
				model.mmv16 = bModel.channel16;
			}
			mactivity.bLeService.doSendWritePara(model);
			//通过在mainActivity中回调，然后广播发送，本界面广播接收
		} else {
			showTips("蓝牙未连接");
		}
	}

	//发送邮件、保存本地、上传服务器
	private void sendEmail() {
		if (!CommonKitUtil.isNetworkAvailable(getActivity())) {
			showTips("网络未连接，请检查您的网络连接情况");
			return;
		}
		if (listData == null || listData.size() == 0) {
			showTips("没有可发送的数据");
			return;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		time1 = formatter.format(curDate);
		DLog.e(TAG,"发送邮件时的时间==" + time1);
		listWeightData = listData;
		for(int i=0;i<listWeightData.size();i++){
			listWeightData.get(i).setDate(time1);
			listData.get(i).setDate(time1);
		}

		List<LocalDataTimeModel> listLocalData = new ArrayList<>();
		LocalDataTimeModel localDataTime = new LocalDataTimeModel();
		localDataTime.setLocalDataTime(truckNum+"_"+time1);
		localDataTime.setWeightDataBeanList(listWeightData);
		listLocalData.add(localDataTime);

		SaveMVVData.saveLocalData(mactivity, listLocalData, truckNum+"_"+time1);

		mhandler.sendEmptyMessage(10);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		DLog.e(TAG,"发送邮件回调requestCode==" + requestCode + ",resultCode==" + resultCode + ",data=" + data);
		switch (resultCode) {
			/*邮件发送成功回调*/
			case ConsTantsCode.REQUEST_CODE_EMAIL_SUCCESS:
			case ConsTantsCode.REQUEST_CODE_EMAIL_SUCCESS1:
			case ConsTantsCode.REQUEST_CODE_SUCCESS:

				break;
			//			/*返回后再次注册广播接收者,再*/
			//			case 1001:
			//				setKBValue();
			//				registerBrodcat();
			//				break;
		}
	}

	private void doshowDialog(){
		if(listWeightData ==null || listWeightData.size() <=0 || isSend){
			return;
		}
		mhandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				isSend=true;
				//把数据保存到本地
				final AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
				//nomalDialog.setIcon(R.drawable.icon_dialog);
				// myDialog.setTitle("提示");
				myDialog.setMessage("是否上传数据到服务器？");
				myDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

						if(listWeightData !=null && listWeightData.size()  > 0){

							mhandler.sendEmptyMessage(ConsTantsCode.REQUEST_CODE_SAVE_LOCAL_SUCCESS);
						}else{
							DLog.d(TAG,"listWeightData数据为空");
						}
					}
				});
				myDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

					}
				});
				myDialog.show();

			}
		}, 100);
	}
}
