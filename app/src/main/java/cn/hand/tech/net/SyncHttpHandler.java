package cn.hand.tech.net;

import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.hand.tech.BApplication;
import cn.hand.tech.common.DAppManager;
import cn.hand.tech.common.DPrefUtil;
import cn.hand.tech.log.DLog;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.Tools;

public class SyncHttpHandler {
	public static class MIME {
		public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		public static final MediaType STREAM = MediaType.parse("application/octet-stream");
	}


	private String lastdata;

	private SyncHttpHandler() {
	}

	private static OkHttpClient mOkHttpClient;
	private static SyncHttpHandler mInstance;

	private static void init() {
		//初始化配置时间长短
		mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);//连接时间长度
		mOkHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);//写的时间长度
		mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);//读的时间长度

		int availableMemory = CommonUtils.getAvailableMemory(BApplication.mContext);

		ConnectionPool pool;
		if (availableMemory > 120) {
			//如果可用内存大于120m
			pool = new ConnectionPool(10, 9999);//z最多可用10个线程池,每个线程池最多连接9999
		} else {
			//如果可用内存小于120m
			pool = new ConnectionPool(5, 9999);//z最多可用5个线程池,每个线程池最多连接9999
		}

		//设置默认的线程池
		mOkHttpClient.setConnectionPool(pool);
//		List<Interceptor> list = mOkHttpClient.networkInterceptors();
//		if (list != null && list.size() > 0) {
//			list.remove(0);
//		}
	}

	public static SyncHttpHandler getInstance() {
		if (mInstance == null) {
			mInstance = new SyncHttpHandler();
			mOkHttpClient = new OkHttpClient();
			init();
		}
		
		return mInstance;
	}
	
	public String get(String url, boolean isExternal) throws Exception {
		return get(url, null, null, isExternal);
	}
	
	public String get(String url, Map<String, String> map) throws Exception {
		return get(url, map, null, false);
	}
	
	public String get(String url, String tag) throws Exception {
		return get(url, null, tag, false);
	}
	
	public String get(String url, Map<String, String> map, String tag, boolean isExternal) throws Exception {
		//url和callBack不能为null
		if (url == null) {
			throw new Exception("请求的url不能为null");
		}

		//参数处理
		String para = HttpUtil.formatPara(map);
		url += para;

		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
		requestBuilder.header("User-Agent",  addAgent());
		

		//url和tag 构造request
		Request request = requestBuilder.build();

		return httpRequestExecute(request, isExternal);
	}
	
	
	/**
	 * contentType="application/json"方式提交
	 * @throws Exception 
	 */
	public String post(String url, JSONObject jsonObj, boolean isJsonType) throws Exception {
		if (isJsonType && jsonObj != null) {
			String jsonStr = jsonObj.toString();
			
			RequestBody formBody = RequestBody.create(MIME.JSON, jsonStr);
			return 	post(url, formBody, null, false);	
			
		} else {
			return post(url, jsonObj);
		}
	}
	/*
	 * 添加数据加载完  标识
	 */
	public <T> T post(String url, JSONObject jsonObj, T t, boolean isJsonType,String  lastlimitied) throws Exception {
		lastdata=lastlimitied;
		return HttpUtil.toBean(t, post(url, jsonObj, isJsonType));
	}
        /**
	 * contentType="application/json"方式提交
	 * @throws Exception 
	 */
	public <T> T post(String url, JSONObject jsonObj, T t, boolean isJsonType) throws Exception {
		lastdata="";
		return HttpUtil.toBean(t, post(url, jsonObj, isJsonType));
	}
	
	public String post(String url) throws Exception {
		return post(url, (String)null);
	}
	
	public String post(String url, String tag) throws Exception {
		return post(url, (JSONObject)null, tag);
	}
	
	public String post(String url, Object object) throws Exception {
		return post(url, object, null);
	}
	
	public String post(String url, JSONObject jsonObj) throws Exception {
		return post(url, jsonObj, (String)null);
	}
	
	public String post(String url, Map<String, String> map) throws Exception {
		return post(url, map, null, false);
	}
	
	public String post(String url, Object object, String tag) throws Exception {
		HashMap<String, String> map = HttpUtil.beanToMap(object);
		
		return post(url, map, tag, false);
	}
	
	public String post(String url, JSONObject jsonObj, String tag) throws Exception {
		HashMap<String, String> map = HttpUtil.jsonToMap(jsonObj);
		
		return post(url, map, tag, false);
	}
	
	public <T> T post(String url, JSONObject jsonObj, T t) throws Exception {
		return post(url, jsonObj, t, null);
	}
	
	public <T> T post(String url, JSONObject jsonObj, T t, String tag) throws Exception {
		HashMap<String, String> map = HttpUtil.jsonToMap(jsonObj);
		
		String result = post(url, map, tag, false);
		return HttpUtil.toBean(t, result);
	}
	public <T> T post(String url, JSONObject jsonObj, Class<T> classT) throws Exception {
		return post(url, jsonObj, classT, null);
	}
	public <T> T post(String url, JSONObject jsonObj, Class<T> classT, String tag) throws Exception {
		HashMap<String, String> map = HttpUtil.jsonToMap(jsonObj);
		
		String result = post(url, map, tag, false);
		return HttpUtil.toBean(classT, result);
	}
	
	public String post(String url, Map<String, String> map, String tag, boolean isExternal) throws Exception {
		if (map == null) {
			map = new HashMap<String, String>();
		}

		RequestBody formBody = HttpUtil.mapToBody(map);
		
		return post(url, formBody, null, isExternal);
	}
	
	private String post(String url, RequestBody body, String tag,  boolean isExternal) throws Exception {
		//url和callBack不能为null
		if (url == null) {
			throw new Exception("请求的参数不能为null");
		}
		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}
		
		if (body == null) {
			body = RequestBody.create(MIME.JSON, "");
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).post(body).tag(tag);
		//标识   数据体加载
		if(!Tools.isEmpty(lastdata)){
			
			requestBuilder.header("If-Modified-Since",  lastdata);
			DLog.w("sy_Lastmodified", lastdata);
		}
		requestBuilder.header("User-Agent",  addAgent());
		
		Request request = requestBuilder.build();
		//请求
		return httpRequestExecute(request, isExternal);
	}
	
	/**
	 * 构造请求
	 * @param request 请求
	 * @throws Exception
	 */
	private String httpRequestExecute(Request request, boolean isExternal) throws Exception {
		try {
			DLog.e("http", "url:" + request.urlString());
			Response response = mOkHttpClient.newCall(request).execute();
			String exMsg = null;
			String result = null;
			JSONObject jsonObject = null;
			DLog.e("http", "url_rsp:" + request.urlString());
			if (response == null || response.body() == null || !response.isSuccessful()) {
				if (response != null) {
					DLog.e("http", "error code:" + response.code());
				} else {
					DLog.e("http", "error response null");
				}
				exMsg = "请求网络异常!";
			}
			if("304".equals(String.valueOf(response.code()))){
				DLog.e("syncHttphandler", "数据体已加载完！");
			}
			
			if("888".equals(String.valueOf(response.code()))){
				DLog.e("http", "httpHandler executeStatus is 888 token不存在或者失效，重新登录");
				CommonUtils.reLoginAccount();//清除数据 并自动重新登陆
			}
			
			if (exMsg == null) {
				try {
					result = response.body().string();
					DLog.e("http", "responsebody:" + result);
				} catch (IOException e1) {
					exMsg = "请求网络异常!";
				}
			}
			
//			if (isExternal) {
//				if (exMsg == null) {
//					return result;
//				} else {
//					throw new Exception(exMsg);
//				}
//			}
			if (exMsg == null) {
				try {
					jsonObject = new JSONObject(result);
				} catch (Exception e) {
					exMsg = "服务器返回信息格式有误!";
				}
			}
			
			//executeStatus 0 成功
			//executeStatus 1 失败
			String executeStatus = "";
			String errorCode = "";
			if (jsonObject != null) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      				executeStatus = jsonObject.optString("Status");
				exMsg = jsonObject.optString("Message");
				if("200".equals(executeStatus)){
					exMsg=null;
				}else{
					exMsg = "请求网络异常!";
				}
			} else if (exMsg == null) {
				exMsg = "请求网络异常!";
			}

//			if (exMsg == null) {
//				//token不存在或者失效，返回首页
//				if ("3".equals(executeStatus)) {
//					DLog.e("http", "syncHandler executeStatus is 3 token不存在或者失效，重新登录");
//					CommonUtils.logoutProgramRestart(DAppManager.getContext());
//					throw new Exception("当前导购账号已被删除，需要重新登录");
//				}
//			}
			
//			if (exMsg == null) {
//				if (!"200".equals(executeStatus) ) {
//					exMsg = "服务器返回信息格式有误!";
//				}
//			}
			
			if (exMsg == null) {
				return result;
			} else {
				throw new Exception(exMsg);
			}
		} catch (Exception e) {
			DLog.e("http", "error:" + e.getMessage());
			if (e != null && "服务器返回信息格式有误!".equals(e.getMessage())) {
				throw e;
			} else {
				throw new Exception("请求网络异常!");
			}
		}

	}

	public void cancalByTag(String tag) {
		mOkHttpClient.cancel(tag);
	}

	public OkHttpClient getClient() {
		return mOkHttpClient;
	}
	//authorization	头部

	public  String   addAuth(){
		String nStr=DPrefUtil.getString(DPrefUtil.KEY_LOGIN_NAME, "")+"$"+DPrefUtil.getString(DPrefUtil.KEY_CURRENT_TIME,"");
		String encodedString ="";
		DLog.d("Base64","="+encodedString);
		return encodedString;		
	}
	

	//User-Agent	头部

	public  String   addAgent(){
		String model=android.os.Build.MODEL;
		String  nModel=model.replaceAll(" ", "_");
		String str="Mozilla/5.0 (Platform/Android; Brand/bingo; Version/"+DAppManager.getVersionName()+""+"; Language/zh-cn; Channels/0; Category/0; OS/"+android.os.Build.VERSION.RELEASE+"; Model/"+ nModel+")";
		DLog.d("net_User-Agent	头部","="+str);
		return str;		
	}
}
