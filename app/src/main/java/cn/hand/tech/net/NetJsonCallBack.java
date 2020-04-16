package cn.hand.tech.net;

import org.json.JSONObject;

public class NetJsonCallBack implements NetCallBack{

	@Override
	public void onError(Exception e) {
		onFailure(e, String.valueOf(HttpUtil.EXECUTE_ERROR), "");
	}

	@Override
	public void onException(String errorCode, String errorMsg) {
		onFailure(new Exception("business error"), errorCode, errorMsg);
	}
	
	public void onFailure(Exception e, String errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(String result, String executeStatus) {
		JSONObject json = null;
		try {
			json = new JSONObject(result);
		} catch (Exception e) {
			json = new JSONObject();
		}
		onSuccess(json, executeStatus);
	}
	
	public void onSuccess(JSONObject json, String executeStatus) {
		// TODO Auto-generated method stub
		
	}
}
