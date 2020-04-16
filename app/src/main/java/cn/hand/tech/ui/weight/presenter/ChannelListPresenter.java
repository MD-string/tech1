package cn.hand.tech.ui.weight.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.ble.bean.BleDevice;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.weight.IChannelView;
import cn.hand.tech.ui.weight.bean.CarInfo;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.MyCustomDialog;

/*
 * 动态  数据处理
 */
public class ChannelListPresenter {
	private final ACache acache;
	private Context mContext;
	private IChannelView mView;
	private MyCustomDialog dialog;

	public ChannelListPresenter(Context context, IChannelView view) {
		mContext = context;
		mView = view;
		acache= ACache.get(context,"WeightFragment");
	}
	/**
	 * 蓝牙设备查询接口
	 */
	public void getMacInfo( final List<BleDevice> list ) {

		try {
			StringBuffer macList=new StringBuffer();
			for(int i=0;i<list.size();i++){
				if(i==0){
					String name=list.get(0).getRealName();
					String str=name.substring(3,name.length());
					macList.append(str);
				}else{
					String name1=list.get(i).getRealName();
					String n1=name1.substring(3,name1.length());
					macList.append(","+n1);
				}
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("deviceList",macList.toString());
			map.put("token","");

			HttpHandler.getInstance().postBle(null, UrlConstant.HttpUrl.URL_SEARCH_CARINFO, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					Gson gson = new Gson();
					try {
						if(!Tools.isEmpty(result) && result.length()> 0){
							List<CarInfo> clist = gson.fromJson(result, new TypeToken<List<CarInfo>>(){}.getType());
							doPutInto(clist,list);
							mView.loginSuccess(clist);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.loginError("蓝牙设备查询失败");

				}
				@Override
				public void onError(Exception e) {
					mView.loginError("蓝牙设备查询失败");
				}
			});

		} catch (Exception e1) {
			mView.loginError("蓝牙设备查询失败");
			e1.printStackTrace();
		}
	}


	/**
	 * 下载txt
	 */
	public void downloadTxt( final BluetoothDevice dev ) {
		try {
			final String dname=dev.getName();
			acache.put("txt_name","nofile.txt");
			String txturl="";
			List<CarInfo> list=(List<CarInfo>)acache.getAsObject("carinfo_list");
			if(list !=null && list.size() >0){
				for(int i=0;i<list.size();i++){
					String name="HD:"+list.get(i).getDeviceId();
					if(name.contains(dname)){
						txturl=list.get(i).getUrl();
					}
				}
			}
			if(txturl !=null ){
				HttpHandler.getInstance().downloadFile("1",txturl, dname+".txt", new NetCallBack() {
					@Override
					public void onSuccess(String result, String executeStatus) {

						mView.downloadSuccess(dev);
						acache.put("txt_name",dname+".txt");

					}
					@Override
					public void onException(String errorCode, String errorMsg) {
						mView.downloadError(dev);

					}
					@Override
					public void onError(Exception e) {
						mView.downloadError(dev);
					}
				});
			}else{
				mView.loginError("下载地址为空");
			}

		} catch (Exception e1) {
			mView.downloadError(dev);
			e1.printStackTrace();
		}
	}

	public void doPutInto(List<CarInfo> clist,List<BleDevice> list){
		for(int j=0;j<list.size();j++){
			String realname=list.get(j).getRealName();
			String realmac=list.get(j).getMacAddress();
			if(clist !=null && clist.size() >0){
				for(int i=0;i<clist.size();i++){
					String name="HD:"+clist.get(i).getDeviceId();
					if(name.contains(realname)){
						clist.get(i).setMac(realmac);
					}
				}
			}
		}
	}


	//登录
	public void postLogin( final HashMap<String, String> map) {
		try {
			showProgressDialog("正在加载...");

			String url = UrlConstant.HttpUrl.LOGIN;
			DLog.e("EventListPresenter","登陸請求URL=="+url+map.toString());
			HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					if (!TextUtils.isEmpty(result)) {
						closeProgressDialog();
						UserResultBean user = null;
						try {
							user = CommonKitUtil.parseJsonWithGson(result.toString(), UserResultBean.class);
							String message = user.getMessage();
							if (message.equals("success")) {
								mView.doLogin(user);

							} else {
								mView.doLoginFail("登录失败");
							}

						} catch (Exception e) {
							mView.doLoginFail("登录失败");
							e.printStackTrace();
						}
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					closeProgressDialog();
					mView.doLoginFail("登录失败");

				}
				@Override
				public void onError(Exception e) {
					closeProgressDialog();
					mView.doLoginFail("登录失败");
				}
			});

		} catch (Exception e1) {
			closeProgressDialog();
			mView.doLoginFail("登录失败");
			e1.printStackTrace();
		}
	}

	/**
	 *判断车辆是否在线
	 */
	public void checkCarOnline(HashMap<String, String> mapParams) {
		try {
			showProgressDialog("正在检测设备状态...");
			HttpHandler.getInstance().postBle(null, UrlConstant.HttpUrl.IS_ONLINE, mapParams, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {//
					JSONObject jsonObject = null;
					String runStatus="0";
					closeProgressDialog();
					try {
						if(!Tools.isEmpty(result) ){
							jsonObject = new JSONObject(result.toString());
							runStatus = jsonObject.getString("runStatus");
						}else{
							runStatus="0";
						}
						mView.onlineSuccess(runStatus);
					} catch (Exception e) {
						mView.onlineFail("获取车辆在线状况信息失败");
						//                        e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					closeProgressDialog();
					mView.onlineFail(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					closeProgressDialog();
					mView.onlineFail("获取车辆在线状况信息失败");
				}
			});

		} catch (Exception e1) {
			closeProgressDialog();
			mView.onlineFail("获取车辆在线状况信息失败");
			e1.printStackTrace();
		}
	}
	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog(String text) {
		if(dialog !=null){
			dialog.cancel();
		}
		dialog = new MyCustomDialog(mContext, R.style.LoadDialog,text);
		dialog.show();
		new Thread("cancle_progressDialog") {
			@Override
			public void run() {
				try {
					Thread.sleep(7000);
					// cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
					// 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
					if(dialog !=null ){
						dialog.cancel();
					}
					// dialog.dismiss();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();
	}
	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
}
