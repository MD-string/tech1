package cn.hand.tech.ui.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.setting.IAutoCheckView;
import cn.hand.tech.ui.setting.bean.AutoCheckFrgBean;
import cn.hand.tech.ui.setting.bean.CarNumberInfo;
import cn.hand.tech.ui.setting.bean.SensorInfo;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.LogUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.CustomDialog;

/*
 * 新增维修 数据 获取
 */
public class AutoCheckPresenter {
	private final ACache acache;
	private Context mContext;
	private static final String TAG = "AddRepairPresenter";
	private IAutoCheckView mView;
	private CustomDialog dialog;
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public AutoCheckPresenter(Context context, IAutoCheckView view) {
		mContext = context;
		mView = view;
		acache= ACache.get(context,"WeightFragment");
	}

	/**
	 * 判断该车牌是否存在
	 */
	public void checkCarNumberExisit( final HashMap<String, String> map) {
		try {
			String  url= UrlConstant.HttpUrl.CHECK_BY_DEVICE_ID;
			LogUtil.d("根据车牌号获取车辆信息==" + url + map.toString());
			HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					Gson gson = new Gson();
					try {
						if(!Tools.isEmpty(result) && result.length()> 0){
							CarNumberInfo companyResult = CommonKitUtil.parseJsonWithGson(result.toString(), CarNumberInfo.class);
							mView.doInfo(companyResult);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doError(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					mView.doError("根据车牌号获取车辆信息失败");
				}
			});

		} catch (Exception e1) {
			mView.doError("根据车牌号获取车辆信息失败");
			e1.printStackTrace();
		}
	}

	//登录
	public void postLogin( final HashMap<String, String> map) {

		try {
			showProgressDialog();

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
							e.printStackTrace();
						}
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					closeProgressDialog();
					mView.doLoginFail(errorMsg);

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
			//			showProgressDialog("正在检测设备状态...");
			HttpHandler.getInstance().postBle(null, UrlConstant.HttpUrl.IS_ONLINE, mapParams, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {//
					JSONObject jsonObject = null;
					String runStatus="0";
					try {
						if(!Tools.isEmpty(result) ){
							if(result.contains("runStatus")){
								jsonObject = new JSONObject(result.toString());
								runStatus = jsonObject.getString("runStatus");
							}else{
								runStatus="0";
							}
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
					mView.onlineFail(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					mView.onlineFail("获取车辆在线状况信息失败");
				}
			});

		} catch (Exception e1) {
			mView.onlineFail("获取车辆在线状况信息失败");
			e1.printStackTrace();
		}
	}

	/**
	 * 根据设备id获取传感器信息（传感器检测）
	 */
	public void getSensorByDeviceId( final HashMap<String, String> map) {
		try {
			String  url= UrlConstant.HttpUrl.GET_SENSOR_BY_DEVID;
			LogUtil.d("根据设备id获取传感器信息（传感器检测）==" + url + map.toString());
			HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					Gson gson = new Gson();
					try {
						if(!Tools.isEmpty(result) && result.length()> 0){
							SensorInfo companyResult = CommonKitUtil.parseJsonWithGson(result.toString(), SensorInfo.class);
							mView.doSennorInfo(companyResult);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doSenorError(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					mView.doSenorError("根据设备id获取传感器信息失败");
				}
			});

		} catch (Exception e1) {
			mView.doSenorError("根据设备id获取传感器信息失败");
			e1.printStackTrace();
		}
	}


	/**
	 * 获取车辆是否允许进厂
	 */
	public void getOrNotInputFragory( final HashMap<String, String> map) {
		try {
			String  url= UrlConstant.HttpUrl.AUTO_CHECK_INPUT_FRAGORY;
			DLog.e("getOrNotInputFragory"," 获取车辆是否允许进厂）==" + url + map.toString());
			HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					JSONObject jsonObject = null;
					try {
						if(!Tools.isEmpty(result) && result.length()> 0){
							jsonObject = new JSONObject(result.toString());
							String data = jsonObject.getString("result");
							AutoCheckFrgBean companyResult = CommonKitUtil.parseJsonWithGson(data, AutoCheckFrgBean.class);
							mView.doFragoryFinish(companyResult);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doFragoryError(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					mView.doFragoryError("获取允许进厂状态失败");
				}
			});

		} catch (Exception e1) {
			mView.doFragoryError("获取允许进厂状态失败");
			e1.printStackTrace();
		}
	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		dialog = new CustomDialog(mContext, R.style.LoadDialog);
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
