package cn.hand.tech.net;

public interface NetCallBack {
	void onError(Exception e);

	void onException(String errorCode, String errorMsg);

	void onSuccess(String result, String executeStatus);
}
