package cn.hand.tech.ui.data.presenter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.data.IDataView;
import cn.hand.tech.utils.MD5;

/*
 * 动态  数据处理
 */
public class DataPresenter {
	private Context mContext;
	private IDataView mView;
	private String MD5Source = "";
	private String passwordMD5 = "";
	private String B_values = "";//"B=[";
	private String K_values = "";//"K=[";
	private final static String USER_NAME = "test";
	private final static String CAR_NUMBER = "AutoWeigh";
	private final static String PASSWORD = "12345678";
	public DataPresenter(Context context, IDataView view) {
		mContext = context;
		mView = view;
	}
	/**
	 * 上传数据
	 */
	public void saveData(String Weight,String MV,String Num) {

		try {
			passwordMD5 = MD5.getUtf8md5(PASSWORD);
			MD5Source = USER_NAME + ";" + CAR_NUMBER + ";" + passwordMD5.toLowerCase();//字母转小写

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("type","calculate");
			map.put("CarNumber","AutoWeigh");
			map.put("UserName","test");
			map.put("SignCode", MD5.getUtf8md5(MD5Source) + "");
			map.put("Weight",Weight);
			map.put("MV",MV);
			map.put("Num",Num);

			HttpHandler.getInstance().postSpecial(null, UrlConstant.HttpUrl.SAVE_PARA, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					try {
						final JSONObject jsonObject = new JSONObject(result.toString());
						int status = jsonObject.getInt("Status");
						float[] kValueData = new float[16];
						float[] bValueData = new float[16];
						if (status == 1) {
							JSONArray K_array = jsonObject.getJSONArray("K");
							JSONArray B_array = jsonObject.getJSONArray("B");

							K_values = "[";
							for (int i = 0; i < K_array.length(); i++) {
								double aa = (double) K_array.get(i);
								String kValue = String.format("%.10f", aa);
								float a = Float.parseFloat(kValue);
								//LogUtil.e("K值=="+a);
								if (i != 0) {
									K_values = K_values + ",";
								}
								K_values = K_values + String.format("%.8f", aa);
								kValueData[i] = a;
							}
							K_values = K_values + "]";


							B_values = "[";
							for (int i = 0; i < B_array.length(); i++) {
								double bb = (double) B_array.get(i);
								String bValue = String.format("%.10f", bb);
								float a = Float.parseFloat(bValue);
								if (i != 0) {
									B_values = B_values + ",";
								}
								B_values = B_values + String.format("%.8f", bb);
								bValueData[i] = a;
							}
							B_values = B_values + "]";

							mView.loginSuccess(kValueData,K_values,bValueData,B_values);
						}else{
							mView.loginError("计算错误");
						}
					} catch (Exception e) {
						mView.loginError("解析失败");
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.loginError("加载失败");

				}
				@Override
				public void onError(Exception e) {
					mView.loginError("加载失败");
				}
			});

		} catch (Exception e1) {
			mView.loginError("加载失败");
			e1.printStackTrace();
		}
	}


}
