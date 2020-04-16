package cn.hand.tech.ui.weight.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.weight.IBasicView;
import cn.hand.tech.ui.weight.bean.AddTruckInfo;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.FilePathUtils;
import cn.hand.tech.utils.ImageUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.CustomDialog;

/*
 * 动态  数据处理
 */
public class BasicPresenter {
	private final ACache acache;
	private Context mContext;
	private IBasicView mView;
	private CustomDialog dialog;

	public BasicPresenter(Context context, IBasicView view) {
		mContext = context;
		mView = view;
		acache= ACache.get(context,"WeightFragment");
	}

//	/**
//	 *判断车辆是否在线
//	 */
//	public void checkCarOnline(HashMap<String, String> mapParams) {
//
//		try {
//			HttpHandler.getInstance().postBle(null, UrlConstant.IS_ONLINE, mapParams, new NetCallBack() {
//				@Override
//				public void onSuccess(String result, String executeStatus) {//
//					JSONObject jsonObject = null;
//					String runStatus="0";
//					try {
//						if(!Tools.isEmpty(result) ){
//							jsonObject = new JSONObject(result.toString());
//							runStatus = jsonObject.getString("runStatus");
//						}else{
//							runStatus="0";
//						}
//						mView.onlineSuccess(runStatus);
//					} catch (Exception e) {
//						mView.onlineSuccess(runStatus);
//						//                        e.printStackTrace();
//					}
//
//				}
//				@Override
//				public void onException(String errorCode, String errorMsg) {
//					mView.onlineFail("获取车辆在线状况信息失败");
//
//				}
//				@Override
//				public void onError(Exception e) {
//					mView.onlineFail("获取车辆在线状况信息失败");
//				}
//			});
//
//		} catch (Exception e1) {
//			mView.onlineFail("获取车辆在线状况信息失败");
//			e1.printStackTrace();
//		}
//	}

	/**
	 * 检查ID是否已经录入
	 */
	public void checkID(String id,String token) {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("deviceId",id);
			map.put("token",token);

			HttpHandler.getInstance().postBasic(null, UrlConstant.HttpUrl.CHECK_ID, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {//0:已经存在   1：不存在
					JSONObject jsonObject = null;
					try {
						jsonObject = new JSONObject(result.toString());
						String code = jsonObject.getString("code");
						mView.doSuccess(code);

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doError("检查ID失败");

				}
				@Override
				public void onError(Exception e) {
					mView.doError("检查ID失败");
				}
			});

		} catch (Exception e1) {
			mView.doError("检查ID失败");
			e1.printStackTrace();
		}
	}

	/**
	 * 公司列表接口
	 */
	public void getCompanyList( final HashMap<String, String> map) {
		try {
			String  url=UrlConstant.HttpUrl.COMPANY_LIST;
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
					mView.doError("下载公司列表失败");

				}
				@Override
				public void onError(Exception e) {
					mView.doError("下载公司列表失败");
				}
			});

		} catch (Exception e1) {
			mView.doError("下载公司列表失败");
			e1.printStackTrace();
		}
	}

	/**
	 * 录车
	 * 上传基本信息数据
	 */
	public void sendBasicData( AddTruckInfo bean,String token,String softVer,String fmVer,String sensorChannel,String companyid,String unitStr,String parent,String child,String dirverName,String path) {
		try {

            Bitmap bitmap = ImageUtil.getImage(path,480,800); //此时返回 bm 为空
            // 计算图片缩放比例

            // 首先保存图片
            String dirPath = FilePathUtils.getRecvFilePath();
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            final File file = new File(dirPath,fileName);
            if(!file.exists()){
                file.createNewFile();
            }

            //            copyFile(path,file.getAbsolutePath());

            saveBitmap(bitmap,dirPath,fileName);


            HashMap<String, String> map = new HashMap<String, String>();
            map.put("file", file.getAbsolutePath());
            map.put("token",token);
			map.put("type","1");
			map.put("deviceId",bean.getId());
			map.put("carNumber",bean.getTruckNumber());
			map.put("unit",unitStr);
			map.put("useTypeL1",parent);
			map.put("useTypeL2",child);
			map.put("hwVersion",softVer); //硬件版本号
			map.put("fwVersion",fmVer);  //固件版本号
			map.put("sensorChannel",sensorChannel);
			if(!Tools.isEmpty(dirverName)){
				map.put("driverName",dirverName);
			}
			String phone =bean.getPhone();
			if(!Tools.isEmpty(phone)){
				map.put("phone",phone);
			}
			String weig =bean.getWeight();
			if(!Tools.isEmpty(weig)){
				map.put("loadCapacity",weig);
			}
			if(!Tools.isEmpty(companyid)){
				map.put("companyId",companyid);
			}
			String sensorType=bean.getSensorType();
			if(!Tools.isEmpty(sensorType)){
				sensorType=sensorType.trim();
				if("应变计".equals(sensorType)){
					map.put("sensorType","1");
				}else if("17-4".equals(sensorType)){
					map.put("sensorType","2");
				}else{
					map.put("sensorType","3");
				}
			}
			String sensorNum=bean.getSensorNumber();
			if(!Tools.isEmpty(sensorNum)){
				map.put("sensorAmount",sensorNum);
			}
			String alxesNumber=bean.getAlxesNumber();
			if(!Tools.isEmpty(alxesNumber)){
				map.put("carAxleAmount",alxesNumber);
			}
			String truckType=bean.getTruckType();
			if(!Tools.isEmpty(truckType)){
				map.put("carType",truckType);
			}

			HttpHandler.getInstance().postMap(null, UrlConstant.HttpUrl.ENTER_CAR, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					try {

						mView.doEnterCar(result);

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.doError("录车失败");

				}
				@Override
				public void onError(Exception e) {
					mView.doError("录车失败");
				}
			});

		} catch (Exception e1) {
			mView.doError("录车失败");
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

    // android中保存Bitmap图片到指定文件夹中的方法。

    /** 保存方法 */
    public void saveBitmap(Bitmap bm,String path,String name) {
        File f = new File(path, name);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
