package cn.hand.tech.ui.setting.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.setting.IRepairView;
import cn.hand.tech.ui.setting.bean.OnLineTruckBean;
import cn.hand.tech.ui.setting.bean.RepairModel;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.bean.InstallerInfo;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.CustomDialog;

/*
 * 维修 数据 获取
 */
public class RepairPresenter {
	private final ACache acache;
	private Context mContext;
	private IRepairView mView;
	private CustomDialog dialog;

	public RepairPresenter(Context context, IRepairView view) {
		mContext = context;
		mView = view;
		acache= ACache.get(context,"WeightFragment");
	}
	//登录
	public void postLogin( final HashMap<String, String> map) {
		try {
			showProgressDialog();

			String url =  UrlConstant.HttpUrl.LOGIN;
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
	 * 公司列表接口
	 */
	public void getCompanyList( final HashMap<String, String> map) {
		try {
			String  url= UrlConstant.HttpUrl.COMPANY_LIST;
			HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					Gson gson = new Gson();
					try {
						if(!Tools.isEmpty(result) && result.length()> 0){
							CompanyResultBean companyResult = CommonKitUtil.parseJsonWithGson(result.toString(), CompanyResultBean.class);
							mView.doCompanySuccess(companyResult);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doCompanyError("下载公司列表失败");

				}
				@Override
				public void onError(Exception e) {
					mView.doCompanyError("下载公司列表失败");
				}
			});

		} catch (Exception e1) {
			mView.doCompanyError("下载公司列表失败");
			e1.printStackTrace();
		}
	}

	/**
	 *获取维修人员信息
	 */
	public void getRepairPersoninfo(String token) {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("token",token);

			HttpHandler.getInstance().postName(null,  UrlConstant.HttpUrl.GET_INSTALL, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {//
					JSONObject jsonObject = null;
					Gson gson = new Gson();
					try {
						//                        jsonObject = new JSONObject(result.toString());
						//                        String message = jsonObject.getString("result");
						if(!Tools.isEmpty(result) && result.length()> 0){
							List<InstallerInfo> depts = gson.fromJson(result, new TypeToken<List<InstallerInfo>>(){}.getType());
							mView.sendPersonSuccess(depts);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.sendPersonError("获取维修人员信息失败");

				}
				@Override
				public void onError(Exception e) {
					mView.sendPersonError("获取维修人员信息失败");
				}
			});

		} catch (Exception e1) {
			mView.sendPersonError("获取维修人员信息失败");
			e1.printStackTrace();
		}
	}


	/**
	 * 获取该车辆故障待维修记录
	 */
	public void haveRepairInformation(String carNumber,String token) {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("token",token);
			map.put("carNumber",carNumber);

			HttpHandler.getInstance().postBasic(null,  UrlConstant.HttpUrl.GET_INFORMATION, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					Gson gson = new Gson();
					try {
						if(!Tools.isEmpty(result) && result.length()> 0){
							List<RepairModel> clist = gson.fromJson(result, new TypeToken<List<RepairModel>>(){}.getType());
							mView.doSuccess(clist);
						}
					} catch (Exception e) {
						mView.doError("获取该设备维修记录失败");
						e.printStackTrace();
					}
				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doError(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					mView.doError("获取该设备维修记录失败");
				}
			});

		} catch (Exception e1) {
			mView.doError("获取该设备维修记录失败");
			e1.printStackTrace();
		}
	}

	/**
	 * 获取所有辆故障待维修记录
	 */
	public void getAllRepairInformation(String token) {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("token",token);
			map.put("version","V2");
			HttpHandler.getInstance().postBasic(null,  UrlConstant.HttpUrl.GET_INFORMATION, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					Gson gson = new Gson();
					try {
						if(!Tools.isEmpty(result) && result.length()> 0){
							List<RepairModel> clist = gson.fromJson(result, new TypeToken<List<RepairModel>>(){}.getType());
							mView.doAllSuccess(clist);
						}
					} catch (Exception e) {
						mView.doAllError("获取该设备维修记录失败");
						e.printStackTrace();
					}
				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doAllError(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					mView.doAllError("获取该设备维修记录失败");
				}
			});

		} catch (Exception e1) {
			mView.doError("获取该设备维修记录失败");
			e1.printStackTrace();
		}
	}

	/**
	 * 上传打电话记录
	 */
	public void inputPhoneDate(String token,String date,String id) {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("token",token);
			map.put("callDuration",date);
			map.put("id",id);

			HttpHandler.getInstance().postSpecial(null,  UrlConstant.HttpUrl.REPAIR_RECORD, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					JSONObject jsonObject = null;
					String mMessage="";

					try {
						jsonObject = new JSONObject(result.toString());
						mMessage = jsonObject.getString("message");
						if(!Tools.isEmpty(mMessage) && "success".equals(mMessage)){
							Toast.makeText(mContext,"上传电话时长记录成功",Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						mView.doAllError("上传打电话记录失败");
						e.printStackTrace();
					}
				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doAllError(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					mView.doAllError("获取该设备维修记录失败");
				}
			});

		} catch (Exception e1) {
			mView.doError("获取该设备维修记录失败");
			e1.printStackTrace();
		}
	}



	/**
	 * *获取在线车辆列表
	 */
	public void getOnlineTruckListInfo(String token) {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("token",token);
			map.put("lastData","1");

			HttpHandler.getInstance().postBasic(null, UrlConstant.HttpUrl.CAR_REAL_TIME_LIST, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					Gson gson = new Gson();
					try {
						if(!Tools.isEmpty(result) && result.length()> 0){
							List<OnLineTruckBean> clist = gson.fromJson(result, new TypeToken<List<OnLineTruckBean>>(){}.getType());
//							OnLineTruckListBean onLineTurckResultBean = CommonKitUtil.parseJsonWithGson(result.toString(), OnLineTruckListBean.class);
//							String message = onLineTurckResultBean.getMessage();
							if (clist !=null && clist.size() >0) {
								mView.doOnlineSuccess(clist);
							}else{
								mView.doAllError(result);
							}
						}
					} catch (Exception e) {
						mView.doAllError("获取在线车辆列表失败");
						e.printStackTrace();
					}
				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doAllError(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					mView.doAllError("获取在线车辆列表失败");
				}
			});

		} catch (Exception e1) {
			mView.doError("获取在线车辆列表失败");
			e1.printStackTrace();
		}
	}

	//	/*获取在线车辆列表*/
	//	public void getOnlineTruckListInfo(Map<String, String> map) {
	//
	//		showProgressDialog();
	//
	//		String url = Constants.HttpUrl.CAR_REAL_TIME_LIST;
	//		LogUtil.e("获取在线车辆請求列表URL==" + url + map.toString());
	//		OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
	//			@Override
	//			public void onError(Call call, Exception e, int id) {
	//				closeProgressDialog();
	//				mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
	//				ToastUtil.showMsgShort(mContext, ConstantsCode.SERVICE_FAILURE);
	//			}
	//
	//			@Override
	//			public void onResponse(String response, int id) {
	//				LogUtil.e("获取在线车辆列表请求返回的response==" + response.toString());
	//				if (!TextUtils.isEmpty(response)) {
	//					closeProgressDialog();
	//					OnLineTruckListBean onLineTurckResultBean = CommonKitUtil.parseJsonWithGson(response.toString(), OnLineTruckListBean.class);
	//					String message = onLineTurckResultBean.getMessage();
	//					if ("success".equals(message)) {
	//						mHandler.sendMessage(mHandler.obtainMessage(ConstantsCode.MSG_REQUEST_SUCCESS1, onLineTurckResultBean));
	//					} else {
	//						ToastUtil.showMsgShort(mContext, message);
	//						mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
	//					}
	//
	//				} else {
	//					closeProgressDialog();
	//					mHandler.sendEmptyMessage(ConstantsCode.MSG_REQUEST_FAIL1);
	//				}
	//
	//			}
	//		});
	//
	//	}
	/**
	 * 上传故障待维修记录
	 */
	public void sendRepairRecord(String id,String token,String repairedRecords,String repairedConclusion,String remark,String repairId,String dirverName,String dirverPhone) {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id",id);
			map.put("token",token);
			map.put("repairedRecords",repairedRecords);
			map.put("repairedUser",repairId);
			if(!Tools.isEmpty(repairedConclusion)){  //维修总结
				map.put("repairedConclusion",repairedConclusion);
			}
			if(!Tools.isEmpty(remark)){  //备注
				map.put("remark",remark);
			}
			if(!Tools.isEmpty(dirverName)){  //司机姓名
				map.put("driverName",dirverName);
			}
			if(!Tools.isEmpty(dirverPhone)){  //司机电话
				map.put("driverPhone",dirverPhone);
			}

			HttpHandler.getInstance().postBasic(null,  UrlConstant.HttpUrl.REPAIR_RECORD, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					try {
						mView.sendSuccess("维修记录上传成功");
					} catch (Exception e) {
						mView.sendError("维修记录上传失败");
						e.printStackTrace();
					}
				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.sendError(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					mView.sendError("维修记录上传失败");
				}
			});

		} catch (Exception e1) {
			mView.sendError("维修记录上传失败");
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
