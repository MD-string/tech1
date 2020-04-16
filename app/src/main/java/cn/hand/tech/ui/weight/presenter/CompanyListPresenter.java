package cn.hand.tech.ui.weight.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.weight.ICompyanView;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.Tools;

/*
 * 动态  数据处理
 */
public class CompanyListPresenter {
	private Context mContext;
	private ICompyanView mView;
	public CompanyListPresenter(Context context, ICompyanView view) {
		mContext = context;
		mView = view;
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
							mView.doSuccess(companyResult);
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
	 * 添加车辆
	 */
	public void addTruck( final HashMap<String, String> map) {

		try {
			String  url=UrlConstant.HttpUrl.CAR_ADD_EDITOR;
			HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
				@Override
				public void onSuccess(String result, String executeStatus) {
					JSONObject jsonObject = null;
					try {
						jsonObject = new JSONObject(result.toString());
						String message = jsonObject.getString("message");
						if (message.contains("成功")){
							mView.inputSuccess("车辆录入成功");
						}else {
							mView.inputSuccess("车辆录入失败");
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				@Override
				public void onException(String errorCode, String errorMsg) {
					mView.inputSuccess("车辆录入失败");

				}
				@Override
				public void onError(Exception e) {
					mView.inputSuccess("车辆录入失败");
				}
			});

		} catch (Exception e1) {
			mView.inputSuccess("车辆录入失败");
			e1.printStackTrace();
		}
	}
}
