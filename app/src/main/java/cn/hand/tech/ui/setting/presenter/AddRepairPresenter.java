package cn.hand.tech.ui.setting.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.bean.PicBean;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.setting.IAddRecodeView;
import cn.hand.tech.ui.setting.bean.CarNumberInfo;
import cn.hand.tech.ui.setting.bean.RepairModel;
import cn.hand.tech.ui.weight.bean.InstallerInfo;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.LogUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.CustomDialog;

/*
 * 新增维修 数据 获取
 */
public class AddRepairPresenter {
	private final ACache acache;
	private Context mContext;
	private static final String TAG = "AddRepairPresenter";
	private IAddRecodeView mView;
	private CustomDialog dialog;
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public AddRepairPresenter(Context context, IAddRecodeView view) {
		mContext = context;
		mView = view;
		acache= ACache.get(context,"WeightFragment");
	}


	/**
	 * 判断该车牌是否存在
	 */
	public void checkCarNumberExisit( final HashMap<String, String> map) {
		try {
			String  url= UrlConstant.HttpUrl.CHECK_CARNUMBER_EXIST;
			LogUtil.d("根据车牌号获取车辆信息==" + url + map.toString());
			HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					Gson gson = new Gson();
					JSONObject jsonObject = null;
					try {
						jsonObject = new JSONObject(result.toString());
						String message = jsonObject.getString("result");
						if(!Tools.isEmpty(message) && message.length()> 0){
							CarNumberInfo companyResult = CommonKitUtil.parseJsonWithGson(message.toString(), CarNumberInfo.class);
							mView.doCompanySuccess(companyResult);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doCompanyError(errorMsg);

				}
				@Override
				public void onError(Exception e) {
					mView.doCompanyError("根据车牌号获取车辆信息失败");
				}
			});

		} catch (Exception e1) {
			mView.doCompanyError("根据车牌号获取车辆信息失败");
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
	 * 新增故障待维修记录
	 */
	public void sendRepairRecord(String devid,String token,String carNumer,String mrepairType,String guzhangStatus,String repairGudie,String repairedRecords,String repairedConclusion,String remark,String repName,String dirverName,
								 String dirverPhone,List<PicBean> mList,String gps,String gsm) {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("deviceId",devid);
			map.put("token",token);
			map.put("carNumber",carNumer);
			map.put("faultTypeName",mrepairType); //故障分类
			map.put("repairedRecords",repairedRecords);//维修记录
			map.put("repairedUser",repName);
			map.put("repairedStatus","1");

			Date date   =   new   Date(System.currentTimeMillis());//获取当前时间
			String now=simpleDateFormat.format(date);

			map.put("faultTime",now);

			if(!Tools.isEmpty(gps)){ //GPS天线
				map.put("gpsAntenna",gps);
			}

			if(!Tools.isEmpty(gsm)){  //GSM天线
				map.put("gsmAntenna",gsm);
			}

			if(!Tools.isEmpty(guzhangStatus)){//故障现象
				map.put("faultPhenomenon",guzhangStatus);
			}

			if(!Tools.isEmpty(repairGudie)){  //维修指导
				map.put("repairedGuide",repairGudie);
			}
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
			int size=mList.size();
			if(size >1){
				StringBuffer strb=new StringBuffer();
				for(int k=0;k<size;k++){
					String url=mList.get(k).getUrl();
					if(!Tools.isEmpty(url)){
						strb.append(url+";");
					}
				}
				String str=strb.toString();
				String mstr=str.substring(0,strb.length()-1);
				DLog.e("AddRepairPresenter","上传新增维修图片"+mstr);
				map.put("files",mstr);
				LogUtil.d("新增故障待维修记录有图片==" +  UrlConstant.HttpUrl.ADD_REPAIR_RECODE + map.toString());//postBasic
				HttpHandler.getInstance().postMaps(null,  UrlConstant.HttpUrl.ADD_REPAIR_RECODE, map, new NetCallBack() {
					@Override
					public void onSuccess(String result, String executeStatus) {
						try {
							mView.sendSuccess("新增维修记录成功");
						} catch (Exception e) {
							mView.sendError("新增维修记录失败");
							e.printStackTrace();
						}
					}
					@Override
					public void onException(String errorCode, String errorMsg) {
						mView.sendError(errorMsg);

					}
					@Override
					public void onError(Exception e) {
						mView.sendError("新增维修记录失败");
					}
				});
			}else{
				LogUtil.d("新增故障待维修记录==" +  UrlConstant.HttpUrl.ADD_REPAIR_RECODE + map.toString());//postBasic
				HttpHandler.getInstance().postBasic(null,  UrlConstant.HttpUrl.ADD_REPAIR_RECODE, map, new NetCallBack() {
					@Override
					public void onSuccess(String result, String executeStatus) {
						try {
							mView.sendSuccess("新增维修记录成功");
						} catch (Exception e) {
							mView.sendError("新增维修记录失败");
							e.printStackTrace();
						}
					}
					@Override
					public void onException(String errorCode, String errorMsg) {
						mView.sendError(errorMsg);

					}
					@Override
					public void onError(Exception e) {
						mView.sendError("新增维修记录失败");
					}
				});
			}



		} catch (Exception e1) {
			mView.sendError("新增维修记录失败");
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
