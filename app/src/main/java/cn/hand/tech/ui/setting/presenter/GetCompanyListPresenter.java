package cn.hand.tech.ui.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.HashMap;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.setting.ICompanyListView;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.CustomDialog;

/*
 * 维修 数据 获取
 */
public class GetCompanyListPresenter {
	private final ACache acache;
	private Context mContext;
	private ICompanyListView mView;
	private CustomDialog dialog;

	public GetCompanyListPresenter(Context context, ICompanyListView view) {
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
