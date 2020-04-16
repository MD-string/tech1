package cn.hand.tech.net;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class NetBeanCallBack<T> implements NetCallBack{

	private Gson mGson = new Gson();

	public NetBeanCallBack() {
	}

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

		Type mySuperClass = getClass().getGenericSuperclass();
		Type type = Object.class;
		if (mySuperClass instanceof ParameterizedType) {
			type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
		}

		try {
			T o = mGson.fromJson(result, type);
			onSuccess(o, executeStatus);
		} catch (Exception e) {
			onSuccess((T)null, executeStatus);
		}
	}

	public abstract void onSuccess(T o, String executeStatus);
}
