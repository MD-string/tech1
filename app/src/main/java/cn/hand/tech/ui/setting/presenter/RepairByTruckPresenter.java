package cn.hand.tech.ui.setting.presenter;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.setting.INeedRepairView;
import cn.hand.tech.ui.setting.bean.RepairModel;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.CustomDialog;

/*
 * 维修 数据 获取
 */
public class RepairByTruckPresenter {
	private final ACache acache;
	private Context mContext;
	private INeedRepairView mView;
	private CustomDialog dialog;

	public RepairByTruckPresenter(Context context, INeedRepairView view) {
		mContext = context;
		mView = view;
		acache= ACache.get(context,"WeightFragment");
	}



	/**
	 * 获取所有设备故障待维修记录
	 */
	public void getRepairInformationByCompanyID(String token,String companyId) {

		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("token",token);
			map.put("companyId",companyId);

			HttpHandler.getInstance().postBasic(null, UrlConstant.HttpUrl.GET_INFORMATION, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					Gson gson = new Gson();
					try {
						if(!Tools.isEmpty(result) && result.length()> 0){
							List<RepairModel> clist = gson.fromJson(result, new TypeToken<List<RepairModel>>(){}.getType());
							mView.doSuccess(clist);
						}
					} catch (Exception e) {
						mView.doError("该公司无车辆需要维修");
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
						mView.doError("上传打电话记录失败");
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
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
}
