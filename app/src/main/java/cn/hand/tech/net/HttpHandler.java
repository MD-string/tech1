package cn.hand.tech.net;

import android.os.Handler;
import android.text.TextUtils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.hand.tech.BApplication;
import cn.hand.tech.log.DLog;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.FileUtils;
import cn.hand.tech.utils.Tools;


public class HttpHandler {
	private static final String TAG = "HttpHandler";
	public static class MIME {
		public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		public static final MediaType STREAM = MediaType.parse("application/octet-stream");
		public static final MediaType Data= MediaType.parse("multipart/form-data");

		public static final MediaType FORM_CONTENT_TYPE
				= MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
	}
	private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");

	private HttpHandler() {
	}

	private static OkHttpClient mOkHttpClient;
	private static final Handler handler = new Handler();
	private static HttpHandler mInstance;

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

	public static HttpHandler getInstance() {
		if (mInstance == null) {
			mInstance = new HttpHandler();
			mOkHttpClient = new OkHttpClient();
			init();
		}
		return mInstance;
	}

	public void get(String url, NetCallBack callBack) {
		get(url, null, null, callBack);
	}

	public void get(String url, Map<String, String> map, NetCallBack callBack) {
		get(url, map, null, callBack);
	}

	public void get(String url, String tag, NetCallBack callBack) {
		get(url, null, tag, callBack);
	}

	public void get(String url, Map<String, String> map, String tag, NetCallBack callBack) {
		//url和callBack不能为null
		if (url == null || callBack == null) {
			callBack.onError(new Exception("请求的参数不能为null"));
			return;
		}

		//参数处理
		String para = HttpUtil.formatPara(map);
		url += para;

		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}

		//url和tag 构造request
		Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
		requestBuilder.header("User-Agent",  addAgent());



		Request request = requestBuilder.build();

		httpRequest(request, callBack);
	}
	/**
	 * multipart/form-data方式提交
	 */
	public void postForm(String url, JSONObject jsonObj, NetCallBack callBack, boolean isForm) {
		if (isForm && jsonObj != null) {
			String jsonStr = jsonObj.toString();


			RequestBody formBody = RequestBody.create(MIME.Data, jsonStr);
			post(url, formBody, null, callBack);
		} else {
			post(url, jsonObj, callBack);
		}
	}

	public void postSave(String url, String jsonObj, NetCallBack callBack, boolean isJsonType) {
		if (isJsonType && jsonObj != null) {
			String jsonStr =jsonObj;


			RequestBody formBody = RequestBody.create(MIME.JSON, jsonStr);
			post(url, formBody, null, callBack);
		} else {
			post(url, jsonObj, callBack);
		}
	}


	/**
	 * contentType="application/json"方式提交
	 */
	public void post(String url, JSONObject jsonObj, NetCallBack callBack, boolean isJsonType) {
		if (isJsonType && jsonObj != null) {
			String jsonStr = jsonObj.toString();


			RequestBody formBody = RequestBody.create(MIME.JSON, jsonStr);
			post(url, formBody, null, callBack);
		} else {
			post(url, jsonObj, callBack);
		}
	}
	/**
	 * contentType="application/json"方式提交
	 */
	public void postJson(String url, String jsonObj, NetCallBack callBack, boolean isJsonType) {
		if (isJsonType && jsonObj != null) {
			String jsonStr = jsonObj;


			RequestBody formBody = RequestBody.create(MIME.JSON, jsonStr);
			post(url, formBody, null, callBack);
		} else {
			post(url, jsonObj, callBack);
		}
	}



	public void post(String url, NetCallBack callBack) {
		post(url, (JSONObject)null, callBack);
	}

	public void post(String url, String tag, NetCallBack callBack) {
		post(url, (JSONObject)null, tag, callBack);
	}

	public void post(String url, Object object, NetCallBack callBack) {
		post(url, object, null, callBack);
	}
	// 找回 密码 特别处理  (因为找回密码 可能有可能登陆过  或者没登陆过)
	public void postData(String url, JSONObject jsonObj, NetCallBack callBack, boolean isData) {
		post(url, jsonObj, null, callBack,true);
	}

	public void post(String url, JSONObject jsonObj, String tag, NetCallBack callBack, boolean isData) {
		if (isData && jsonObj != null) {
			String jsonStr = jsonObj.toString();

			RequestBody formBody = RequestBody.create(MIME.JSON, jsonStr);
			//url和callBack不能为null
			if (url == null || callBack == null) {
				callBack.onError(new Exception("请求的参数不能为null"));
				return;
			}
			// 取消之前的请求
			if (tag != null) {
				mOkHttpClient.cancel(tag);
			}

			if (formBody == null) {
				formBody = RequestBody.create(MIME.JSON, "");
			}
			Request.Builder requestBuilder = new Request.Builder().url(url).post(formBody).tag(tag);
			requestBuilder.header("User-Agent",  addAgent());

			requestBuilder.header("Authorization", addAuth());

			Request request = requestBuilder.build();

			//请求
			httpRequest(request, callBack);
		}
	}
	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postHeadImage(String tag, String url, Map<String, String>map, NetCallBack callBack) {
		//url和callBack不能为null
		if (url == null || callBack == null) {
			callBack.onError(new Exception("请求的参数不能为null"));
			return;
		}
		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);

		File  file=new File(map.get("file"));
		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM)
				.addPart(
						Headers.of("Content-Disposition", "x-www-form-urlencoded; charset=utf-8; name=\"token\""),  RequestBody.create(null,map.get("token")))
				.addPart(
						Headers.of("Content-Disposition", "x-www-form-urlencoded; charset=utf-8; name=\"type\""),  RequestBody.create(null,  map.get("type")))
				.addPart(
						Headers.of("Content-Disposition", "x-www-form-urlencoded; charset=utf-8; name=\"deviceId\""),  RequestBody.create(null, map.get("deviceId")))
				.addPart(
						Headers.of("Content-Disposition", "x-www-form-urlencoded; charset=utf-8; name=\"carNumber\""),  RequestBody.create(null, map.get("carNumber")))
				.addPart(
						Headers.of("Content-Disposition", "x-www-form-urlencoded; charset=utf-8; name=\"file\";filename=\"" +file.getName()
								+ "\""), RequestBody.create(MIME.STREAM, file)).build();

		Request request = requestBuilder.post(requestBody).build();


		//请求
		httpRequest(request, callBack);

	}
	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postImg(String tag, String url, Map<String, String> map, NetCallBack callBack) {
		//url和callBack不能为null
		if (url == null || callBack == null) {
			callBack.onError(new Exception("请求的参数不能为null"));
			return;
		}
		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
		//		requestBuilder.header("User-Agent",  addAgent());

		File file=new File(map.get("pic"));
		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM)
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"pic\";filename=\"" +file.getName()
								+ "\""), RequestBody.create(MIME.Data, file)).build();

		Request request = requestBuilder.post(requestBody).build();


		//请求
		httpRequest(request, callBack);

	}
	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postBle(String tag, String url, HashMap<String, String> paramsMap, NetCallBack callBack) {

		try{
			//url和callBack不能为null
			if (url == null || callBack == null) {
				callBack.onError(new Exception("请求的参数不能为null"));
				return;
			}
			// 取消之前的请求
			if (tag != null) {
				mOkHttpClient.cancel(tag);
			}

			Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
			//处理参数
			StringBuilder tempParams = new StringBuilder();
			int pos = 0;
			for (String key : paramsMap.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
				pos++;
			}
			//补全请求地址
			String requestUrl = url;
			//生成参数
			String params = tempParams.toString();
			//创建一个请求实体对象 RequestBody
			RequestBody body = RequestBody.create(MIME.FORM_CONTENT_TYPE, params);
			//创建一个请求
			final Request request = requestBuilder.post(body).build();

			//请求
			httpRequest(request, callBack);
		}catch (Exception e){
			e.printStackTrace();
		}


	}
	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postBleBIN(String tag, String url, HashMap<String, String> paramsMap, NetCallBack callBack) {

		try{
			//url和callBack不能为null
			if (url == null || callBack == null) {
				callBack.onError(new Exception("请求的参数不能为null"));
				return;
			}
			// 取消之前的请求
			if (tag != null) {
				mOkHttpClient.cancel(tag);
			}

			Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
			//处理参数
			StringBuilder tempParams = new StringBuilder();
			int pos = 0;
			for (String key : paramsMap.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
				pos++;
			}
			//补全请求地址
			String requestUrl = url;
			//生成参数
			String params = tempParams.toString();
			//创建一个请求实体对象 RequestBody
			RequestBody body = RequestBody.create(MIME.FORM_CONTENT_TYPE, params);
			//创建一个请求
			final Request request = requestBuilder.post(body).build();

			//请求
			httpRequestBIN(request, callBack);
		}catch (Exception e){
			e.printStackTrace();
		}


	}

	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postName(String tag, String url, HashMap<String, String> paramsMap, NetCallBack callBack) {

		try{
			//url和callBack不能为null
			if (url == null || callBack == null) {
				callBack.onError(new Exception("请求的参数不能为null"));
				return;
			}
			// 取消之前的请求
			if (tag != null) {
				mOkHttpClient.cancel(tag);
			}

			Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
			//处理参数
			StringBuilder tempParams = new StringBuilder();
			int pos = 0;
			for (String key : paramsMap.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
				pos++;
			}
			//补全请求地址
			String requestUrl = url;
			//生成参数
			String params = tempParams.toString();
			//创建一个请求实体对象 RequestBody
			RequestBody body = RequestBody.create(MIME.FORM_CONTENT_TYPE, params);
			//创建一个请求
			final Request request = requestBuilder.post(body).build();

			//请求
			httpReuestName(request, callBack);
		}catch (Exception e){
			e.printStackTrace();
		}


	}

	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postBasic(String tag, String url, HashMap<String, String> paramsMap, NetCallBack callBack) {

		try{
			//url和callBack不能为null
			if (url == null || callBack == null) {
				callBack.onError(new Exception("请求的参数不能为null"));
				return;
			}
			// 取消之前的请求
			if (tag != null) {
				mOkHttpClient.cancel(tag);
			}

			Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
			//处理参数
			StringBuilder tempParams = new StringBuilder();
			int pos = 0;
			for (String key : paramsMap.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
				pos++;
			}
			//补全请求地址
			String requestUrl = url;
			//生成参数
			String params = tempParams.toString();
			//创建一个请求实体对象 RequestBody
			RequestBody body = RequestBody.create(MIME.FORM_CONTENT_TYPE, params);
			//创建一个请求
			final Request request = requestBuilder.post(body).build();

			//请求
			nnhttpRequest(request, callBack);
		}catch (Exception e){
			e.printStackTrace();
		}


	}


	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postSpecial(String tag, String url, HashMap<String, String> paramsMap, NetCallBack callBack) {

		try{
			//url和callBack不能为null
			if (url == null || callBack == null) {
				callBack.onError(new Exception("请求的参数不能为null"));
				return;
			}
			// 取消之前的请求
			if (tag != null) {
				mOkHttpClient.cancel(tag);
			}

			Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
			//处理参数
			StringBuilder tempParams = new StringBuilder();
			int pos = 0;
			for (String key : paramsMap.keySet()) {
				if (pos > 0) {
					tempParams.append("&");
				}
				tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
				pos++;
			}
			//补全请求地址
			String requestUrl = url;
			//生成参数
			String params = tempParams.toString();
			//创建一个请求实体对象 RequestBody
			RequestBody body = RequestBody.create(MIME.FORM_CONTENT_TYPE, params);
			//创建一个请求
			final Request request = requestBuilder.post(body).build();

			//请求
			toSpecialhttpRequest(request, callBack);
		}catch (Exception e){
			e.printStackTrace();
		}


	}

	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postXiShu(String tag, String url, Map<String, String> paramsMap, NetCallBack callBack) {

		try{
			//url和callBack不能为null
			if (url == null || callBack == null) {
				callBack.onError(new Exception("请求的参数不能为null"));
				return;
			}
			// 取消之前的请求
			if (tag != null) {
				mOkHttpClient.cancel(tag);
			}

			//补全请求地址
			String requestUrl = url;
			MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
			//追加参数
			for (String key : paramsMap.keySet()) {
				Object object = paramsMap.get(key);
				if (!(object instanceof File)) {
					builder.addFormDataPart(key, object.toString());
				} else {
					File file = (File) object;
					builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
				}
			}
			//创建RequestBody
			RequestBody body = builder.build();
			//创建Request
			final Request request = new Request.Builder().url(requestUrl).post(body).build();

			//请求
			httpRequest(request, callBack);
		}catch (Exception e){
			e.printStackTrace();
		}


	}


	/**
	 * 图片和文件都可以上传  （多张图片 或者文件上传）
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postMap(String tag, String url, Map<String, String> map, NetCallBack callBack) {
		//url和callBack不能为null
		if (url == null || callBack == null) {
			callBack.onError(new Exception("请求的参数不能为null"));
			return;
		}
		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);

		MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
		if (map!=null)
		{
			for (Map.Entry<String,String> entry:map.entrySet())
			{
				String key=entry.getKey();
				if(!"file".equals(key)){
					builder.addFormDataPart(entry.getKey(),entry.getValue());
				}
			}

			String path=map.get("file");
			File file = new File(path);
			if(file.exists() ){
				String TYPE = "application/octet-stream";
				RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG,file);
				builder.addFormDataPart("file",file.getName(), fileBody);
			}

		}
		//构建请求体
		RequestBody requestBody = builder.build();
		Request request = requestBuilder.post(requestBody).build();


		//请求
		httpRequest(request, callBack);

	}



	/**
	 * 图片和文件都可以上传  （多张图片 或者文件上传）
	 * @param tag
	 * @param url
	 */
	public void postMaps(String tag, String url, Map<String, String> map, NetCallBack callBack) {
		//url和callBack不能为null
		if (url == null || callBack == null) {
			callBack.onError(new Exception("请求的参数不能为null"));
			return;
		}
		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);

		MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
		if (map!=null)
		{
			for (Map.Entry<String,String> entry:map.entrySet())
			{
				String key=entry.getKey();
				if(!"files".equals(key)){
					builder.addFormDataPart(entry.getKey(),entry.getValue());
				}
			}

			String fiels=map.get("files");
			DLog.e(TAG,"fiels:" + fiels);
			String[] mpt=fiels.split(";");
			for(int j=0;j<mpt.length;j++){
				String path=mpt[j];
				DLog.e(TAG,"file path:" + path);
				File file = new File(path);
				if(file.exists() ){
					DLog.e(TAG,"file name:" + file.getName());
					String TYPE = "application/octet-stream";
					RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG,file);
					builder.addFormDataPart("files",file.getName(), fileBody);
				}
			}

		}
		//构建请求体
		RequestBody requestBody = builder.build();
		Request request = requestBuilder.post(requestBody).build();


		//请求
		httpRequest(request, callBack);

	}

	/**
	 * 图片和文件都可以上传  （多张图片 或者文件上传）
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postMorePicImage(String tag, String url, Map<String, String> map, NetCallBack callBack) {
		//url和callBack不能为null
		if (url == null || callBack == null) {
			callBack.onError(new Exception("请求的参数不能为null"));
			return;
		}
		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
		requestBuilder.header("User-Agent",  addAgent());
		String str=map.get("upfile");
		List<String> list = Arrays.asList(str.split(","));
		//		File  file=new File(map.get("pic"));
		//		RequestBody requestBody = new MultipartBuilder()
		//		.type(MultipartBuilder.FORM)
		//		.addPart(
		//				Headers.of("Content-Disposition", "form-data; name=\"title\""),  RequestBody.create(null,map.get("title")))
		//				.addPart(
		//	             Headers.of("Content-Disposition", "form-data; name=\"location\""),  RequestBody.create(null,  map.get("location")))
		//				.addPart(
		//				Headers.of("Content-Disposition", "form-data; name=\"upfile\";filename=\"" +file.getName()
		//						+ "\""), RequestBody.create(MIME.Data, file)).build();

		MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
		builder.addFormDataPart("title", null, RequestBody.create(null,map.get("title")));
		builder.addFormDataPart("location", null, RequestBody.create(null,map.get("location")));
		//遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
		for (String path : list) {
			builder.addFormDataPart("upfile[]", new File(path).getName(), RequestBody.create(MIME.Data, new File(path)));
		}

		//构建请求体
		RequestBody requestBody = builder.build();
		Request request = requestBuilder.post(requestBody).build();


		//请求
		httpRequest(request, callBack);

	}

	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postAuthImage(String tag, String url, Map<String, String> map, NetCallBack callBack) {
		//url和callBack不能为null
		if (url == null || callBack == null) {
			callBack.onError(new Exception("请求的参数不能为null"));
			return;
		}
		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
		requestBuilder.header("User-Agent",  addAgent());

		File file=new File(map.get("auth"));
		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM)
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"auth\";filename=\"" +file.getName()
								+ "\""), RequestBody.create(MIME.Data, file)).build();

		Request request = requestBuilder.post(requestBody).build();


		//请求
		httpRequest(request, callBack);

	}


	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void postImage(String tag, String url, Map<String, String> map, NetCallBack callBack) {
		//url和callBack不能为null
		if (url == null || callBack == null) {
			callBack.onError(new Exception("请求的参数不能为null"));
			return;
		}
		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
		requestBuilder.header("User-Agent",  addAgent());

		File file=new File(map.get("path"));
		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM)
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"proname\""),  RequestBody.create(null,map.get("name")))
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"promode\""),  RequestBody.create(null,  map.get("model")))
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"procolor\""),  RequestBody.create(null, map.get("color")))
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"prolink\""),  RequestBody.create(null, map.get("lian")))
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"pic\";filename=\"" +file.getName()
								+ "\""), RequestBody.create(MIME.Data, file)).build();

		Request request = requestBuilder.post(requestBody).build();


		//请求
		httpRequest(request, callBack);

	}


	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param callBack
	 */
	public void uploadImage(String tag, String url, Map<String, String> map, NetCallBack callBack) {
		//url和callBack不能为null
		if (url == null || callBack == null) {
			callBack.onError(new Exception("请求的参数不能为null"));
			return;
		}
		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).tag(tag);
		requestBuilder.header("User-Agent",  addAgent());
		File file=new File(map.get("path"));
		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM)
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"user\""),  RequestBody.create(null,map.get("phone")))
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"usex\""),  RequestBody.create(null,  map.get("sex")))
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"nickname\""),  RequestBody.create(null, map.get("nick")))
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"ubirthday\""),  RequestBody.create(null, map.get("birth")))
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"code\""),  RequestBody.create(null, map.get("code")))
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"pic\";filename=\"" +file.getName()
								+ "\""), RequestBody.create(MIME.Data, file)).build();

		Request request = requestBuilder.post(requestBody).build();


		//请求
		httpRequest(request, callBack);

	}




	public void post(String url, JSONObject jsonObj, NetCallBack callBack) {
		post(url, jsonObj, null, callBack);
	}

	public void post(String url, Map<String, String> map, NetCallBack callBack) {
		post(url, map, null, callBack);
	}

	public void post(String url, Object object, String tag, NetCallBack callBack) {
		HashMap<String, String> map = HttpUtil.beanToMap(object);

		post(url, map, tag, callBack);
	}

	public void post(String url, JSONObject jsonObj, String tag, NetCallBack callBack) {
		HashMap<String, String> map = HttpUtil.jsonToMap(jsonObj);

		post(url, map, tag, callBack);
	}

	public void post(String url, Map<String, String> map, String tag, NetCallBack callBack) {
		if (map == null) {
			map = new HashMap<String, String>();
		}

		RequestBody formBody = HttpUtil.mapToBody(map);

		post(url, formBody, tag, callBack);
	}

	private void post(String url, RequestBody body, String tag, NetCallBack callBack) {
		//url和callBack不能为null
		if (url == null || callBack == null) {
			callBack.onError(new Exception("请求的参数不能为null"));
			return;
		}
		// 取消之前的请求
		if (tag != null) {
			mOkHttpClient.cancel(tag);
		}

		if (body == null) {
			body = RequestBody.create(MIME.JSON, "");
		}
		Request.Builder requestBuilder = new Request.Builder().url(url).post(body).tag(tag);

		Request request = requestBuilder.build();

		//请求
		httpRequest(request, callBack);
	}



	/**
	 * 图片和文件都可以上传
	 * @param tag
	 * @param url
	 * @param file
	 * @param callBack
	 */
	public void uploadFile(String tag, String url, File file, NetCallBack callBack) {

		//参数非空处理
		if (TextUtils.isEmpty(tag) || url == null || file == null || callBack == null) {
			throw new NullPointerException("input args should not been null");
		}
		// 取消之前的请求
		mOkHttpClient.cancel(tag);

		RequestBody fileBody = RequestBody.create(MIME.STREAM, file);

		RequestBody requestBody = new MultipartBuilder()
				.type(MultipartBuilder.FORM)
				.addPart(
						Headers.of("Content-Disposition", "form-data; name=\"mFile\";filename=\"" + file.getName()
								+ "\""), fileBody).build();

		Request request = new Request.Builder().tag(tag).url(url).post(requestBody).build();
		//请求
		httpRequest(request, callBack);

	}

	public void downloadFile(String tag, String url, final String fileName, final NetCallBack callBack) {

		//参数非空处理
		if (TextUtils.isEmpty(tag) || url == null || callBack == null || TextUtils.isEmpty(fileName)) {
			throw new NullPointerException("input args should not been null");
		}

		// 取消之前的请求
		mOkHttpClient.cancel(tag);

		//url和tag 构造request
		Request request = new Request.Builder().url(url).tag(tag).build();

		//请求
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				callBack.onError(e);

			}

			@Override
			public void onResponse(Response response) throws IOException {
				//				InputStream inputStream = response.body().byteStream();
				//
				//				String cacheFileDir = FilePathUtils.getDownloadFilePath() + "/" + fileName;
				//
				//				//文件操作
				//				FileOutputStream fileOutputStream = new FileOutputStream(new File(cacheFileDir));
				//
				//				byte[] buffer = new byte[1024 * 1024];
				//				int len = -1;
				//
				//				while ((len = inputStream.read(buffer)) != -1) {
				//					fileOutputStream.write(buffer, 0, len);
				//					fileOutputStream.flush();
				//				}
				//
				//				callBack.onSuccess(fileName, "0");
				//
				//				fileOutputStream.close();
				//				inputStream.close();



				//将返回结果转化为流，并写入文件
				int len;
				byte[] buf = new byte[2048];
				InputStream inputStream = response.body().byteStream();
				/**
				 * 写入本地文件
				 */
				// 首先保存图片
				String dirPath = FileUtils.getAppSdcardDir() + "/" + "bg_image" + "/";
				File dirFile = new File(dirPath);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}

				File file = null;
				/**
				 *如果服务器没有返回的话,使用自定义的文件名字
				 */
				file = new File(dirPath + fileName);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				while ((len = inputStream.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, len);
				}
				callBack.onSuccess(fileName,"0");
				fileOutputStream.flush();
				fileOutputStream.close();
				inputStream.close();

			}
		});

	}

	/**
	 * 构造请求
	 * @param request 请求
	 * @param callBack  回调
	 */
	private void httpReuestName(final Request request, final NetCallBack callBack) {
		if (request != null) {
			DLog.e(TAG,"http"+ "url:" + request.urlString());
		}
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, final IOException e) {
				handlerError(callBack, e);
			}

			@Override
			public void onResponse(final Response response) {
				try {
					String exMsg = null;
					String result = null;
					JSONObject jsonObject = null;
					DLog.e(TAG,"http、"+"url_rsp:" + request.urlString());
					if (response == null || response.body() == null || !response.isSuccessful()) {
						if (response != null) {
							DLog.e(TAG,"http、"+"error code:" + response.code());
						} else {
							DLog.e(TAG,"http、"+"error response null");
						}
						exMsg = "请求网络异常!";
					}

					if (exMsg == null) {
						try {
							result = response.body().string();
							DLog.e(TAG, "responsebody:" + result);
						} catch (IOException e1) {
							exMsg = "请求网络异常!";
						}
					}

					if(exMsg == null){
						try {
							jsonObject = new JSONObject(result);
						} catch (Exception e) {
							exMsg = "服务器返回信息格式有误!";
						}
					}


					//exMsg success 成功
					//exMsg 非200 失败
					String executeStatus = "";
					if (jsonObject != null) {
						exMsg = jsonObject.optString("message");
						String errorMsg = jsonObject.optString("ErrorMessage");
						String token = jsonObject.optString("token");
						String succ = jsonObject.optString("success");
						if ("success".equals(exMsg) || exMsg.contains("成功") || "true".equals(succ)) {
							String values = jsonObject.optString("result");
							DLog.e(TAG,"responsebody:" + "====》values: "+values);
							if(!Tools.isEmpty(token) || exMsg.contains("数据提交成功") || values ==null || values.equals("") || values.equals("[]") || values.length()  <= 0){
								handlerSuccess(callBack, values, executeStatus);
							}else{
								handlerSuccess(callBack, values, executeStatus);
							}
						}else if(errorMsg.equals("") || exMsg.contains("不")){
							handlerException(callBack, executeStatus, exMsg);
						}else {
							handlerException(callBack, executeStatus, exMsg);
						}
					} else  {
						handlerError(callBack, "请求网络异常!");
					}

				} catch (Exception e) {
					handlerError(callBack, "请求网络异常!");
				}

			}
		});

	}
	/**
	 * 构造请求
	 * @param request 请求
	 * @param callBack  回调
	 */
	private void httpRequest(final Request request, final NetCallBack callBack) {
		if (request != null) {
			DLog.e(TAG,"http"+ "url:" + request.urlString());
		}
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, final IOException e) {
				handlerError(callBack, e);
			}

			@Override
			public void onResponse(final Response response) {
				try {
					String exMsg = null;
					String result = null;
					JSONObject jsonObject = null;
					DLog.e(TAG,"http、"+"url_rsp:" + request.urlString());
					if (response == null || response.body() == null || !response.isSuccessful()) {
						if (response != null) {
							DLog.e(TAG,"http、"+"error code:" + response.code());
						} else {
							DLog.e(TAG,"http、"+"error response null");
						}
						exMsg = "请求网络异常!";
					}

					if (exMsg == null) {
						try {
							result = response.body().string();
							DLog.e(TAG, "responsebody:" + result);
						} catch (IOException e1) {
							exMsg = "请求网络异常!";
						}
					}

					if(exMsg == null){
						try {
							jsonObject = new JSONObject(result);
						} catch (Exception e) {
							exMsg = "服务器返回信息格式有误!";
						}
					}


					//exMsg success 成功
					//exMsg 非200 失败
					String executeStatus = "";
					if (jsonObject != null) {
						exMsg = jsonObject.optString("message");
						String errorMsg = jsonObject.optString("ErrorMessage");
						String token = jsonObject.optString("token");
						String succ = jsonObject.optString("success");
						if ("success".equals(exMsg) || exMsg.contains("成功") || "true".equals(succ)) {
							String values = jsonObject.optString("result");
							DLog.e(TAG,"responsebody:" + "====》values: "+values);
							if(!Tools.isEmpty(token) || exMsg.contains("数据提交成功") || values ==null || values.equals("") || values.equals("[]") || values.length()  <= 0){
								handlerSuccess(callBack, result, executeStatus);
							}else{
								handlerSuccess(callBack, values, executeStatus);
							}
						}else if(errorMsg.equals("") || exMsg.contains("不")){
							handlerException(callBack, executeStatus, exMsg);
						}else {
							handlerException(callBack, executeStatus, exMsg);
						}
					} else  {
						handlerError(callBack, "请求网络异常!");
					}

				} catch (Exception e) {
					handlerError(callBack, "请求网络异常!");
				}

			}
		});

	}
	/**
	 * 构造请求
	 * @param request 请求
	 * @param callBack  回调
	 */
	private void httpRequestBIN(final Request request, final NetCallBack callBack) {
		if (request != null) {
			DLog.e(TAG,"http"+ "url:" + request.urlString());
		}
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, final IOException e) {
				handlerError(callBack, e);
			}

			@Override
			public void onResponse(final Response response) {
				try {
					String exMsg = null;
					String result = null;
					JSONObject jsonObject = null;
					DLog.e(TAG,"http、"+"url_rsp:" + request.urlString());
					if (response == null || response.body() == null || !response.isSuccessful()) {
						if (response != null) {
							DLog.e(TAG,"http、"+"error code:" + response.code());
						} else {
							DLog.e(TAG,"http、"+"error response null");
						}
						exMsg = "请求网络异常!";
					}

					if (exMsg == null) {
						try {
							result = response.body().string();
							DLog.e(TAG, "responsebody:" + result);
						} catch (IOException e1) {
							exMsg = "请求网络异常!";
						}
					}

					if(exMsg == null){
						try {
							jsonObject = new JSONObject(result);
						} catch (Exception e) {
							exMsg = "服务器返回信息格式有误!";
						}
					}


					//exMsg success 成功
					//exMsg 非200 失败
					String executeStatus = "";
					if (jsonObject != null) {
						exMsg = jsonObject.optString("message");
						String errorMsg = jsonObject.optString("ErrorMessage");
						if ("success".equals(exMsg) || exMsg.contains("成功")) {
							handlerSuccess(callBack, result, executeStatus);

						}else if(errorMsg.equals("") || exMsg.contains("不")){
							handlerException(callBack, executeStatus, exMsg);
						}else {
							handlerException(callBack, executeStatus, exMsg);
						}
					} else  {
						handlerError(callBack, "请求网络异常!");
					}

				} catch (Exception e) {
					handlerError(callBack, "请求网络异常!");
				}

			}
		});

	}

	/**
	 * 构造请求
	 * @param request 请求
	 * @param callBack  回调
	 */
	private void nnhttpRequest(final Request request, final NetCallBack callBack) {
		if (request != null) {
			DLog.e(TAG,"http"+ "url:" + request.urlString());
		}
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, final IOException e) {
				handlerError(callBack, e);
			}

			@Override
			public void onResponse(final Response response) {
				try {
					String exMsg = null;
					String result = null;
					JSONObject jsonObject = null;
					DLog.e(TAG,"http、"+"url_rsp:" + request.urlString());
					if (response == null || response.body() == null || !response.isSuccessful()) {
						if (response != null) {
							DLog.e(TAG,"http、"+"error code:" + response.code());
						} else {
							DLog.e(TAG,"http、"+"error response null");
						}
						exMsg = "请求网络异常!";
					}

					if (exMsg == null) {
						try {
							result = response.body().string();
							DLog.e(TAG, "responsebody:" + result);
						} catch (IOException e1) {
							exMsg = "请求网络异常!";
						}
					}

					if(exMsg == null){
						try {
							jsonObject = new JSONObject(result);
						} catch (Exception e) {
							exMsg = "服务器返回信息格式有误!";
						}
					}


					//exMsg success 成功
					//exMsg 非200 失败
					String executeStatus = "";
					if (jsonObject != null) {
						exMsg = jsonObject.optString("message");
						String errorMsg = jsonObject.optString("ErrorMessage");
						String token = jsonObject.optString("token");
						String succ = jsonObject.optString("success");
						if ("success".equals(exMsg) || exMsg.contains("成功") || "true".equals(succ)) {
							String values = jsonObject.optString("result");
							DLog.e(TAG,"responsebody:" + "====》values: "+values);
							if(!Tools.isEmpty(token) ){
								handlerSuccess(callBack, values, executeStatus);
							}else{
								handlerSuccess(callBack, values, executeStatus);
							}
						}else if(errorMsg.equals("") || exMsg.contains("不")){
							handlerException(callBack, executeStatus, exMsg);
						}else {
							handlerException(callBack, executeStatus, exMsg);
						}
					} else  {
						handlerError(callBack, "请求网络异常!");
					}

				} catch (Exception e) {
					handlerError(callBack, "请求网络异常!");
				}

			}
		});

	}


	/**
	 * 构造请求
	 * @param request 请求
	 * @param callBack  回调
	 */
	private void toSpecialhttpRequest(final Request request, final NetCallBack callBack) {
		if (request != null) {
			DLog.e(TAG,"http"+ "url:" + request.urlString());
		}
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, final IOException e) {
				handlerError(callBack, e);
			}

			@Override
			public void onResponse(final Response response) {
				try {
					String exMsg = null;
					String result = null;
					JSONObject jsonObject = null;
					DLog.e(TAG,"http、"+"url_rsp:" + request.urlString());
					if (response == null || response.body() == null || !response.isSuccessful()) {
						if (response != null) {
							DLog.e(TAG,"http、"+"error code:" + response.code());
						} else {
							DLog.e(TAG,"http、"+"error response null");
						}
						exMsg = "请求网络异常!";
					}

					if (exMsg == null) {
						try {
							result = response.body().string();
							DLog.e(TAG, "responsebody:" + result);
						} catch (IOException e1) {
							exMsg = "请求网络异常!";
						}
					}

					if(exMsg == null){
						try {
							jsonObject = new JSONObject(result);
						} catch (Exception e) {
							exMsg = "服务器返回信息格式有误!";
						}
					}


					//exMsg success 成功
					//exMsg 非200 失败
					String executeStatus = "";
					if (jsonObject != null) {
						handlerSuccess(callBack, result, executeStatus);
					} else  {
						handlerError(callBack, "请求网络异常!");
					}

				} catch (Exception e) {
					handlerError(callBack, "请求网络异常!");
				}

			}
		});

	}

	/**
	 * 构造请求
	 * @param request 请求
	 * @param callBack  回调
	 */
	private void mhttpRequest(final Request request, final NetCallBack callBack) {
		if (request != null) {
			DLog.e(TAG,"http"+ "url:" + request.urlString());
		}
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, final IOException e) {
				handlerError(callBack, e);
			}

			@Override
			public void onResponse(final Response response) {
				try {
					String exMsg = null;
					String result = null;
					JSONObject jsonObject = null;
					DLog.e(TAG,"http、"+"url_rsp:" + request.urlString());
					if (response == null || response.body() == null || !response.isSuccessful()) {
						if (response != null) {
							DLog.e(TAG,"http、"+"error code:" + response.code());
						} else {
							DLog.e(TAG,"http、"+"error response null");
						}
						exMsg = "请求网络异常!";
					}

					if (exMsg == null) {
						try {
							result = response.body().string();
							DLog.e(TAG, "responsebody:" + result);
						} catch (IOException e1) {
							exMsg = "请求网络异常!";
						}
					}

					if(exMsg == null){
						try {
							jsonObject = new JSONObject(result);
						} catch (Exception e) {
							exMsg = "服务器返回信息格式有误!";
						}
					}


					//exMsg success 成功
					//exMsg 非200 失败
					String executeStatus = "";
					if (jsonObject != null) {
						exMsg = jsonObject.optString("message");
						String errorMsg = jsonObject.optString("ErrorMessage");
						String token = jsonObject.optString("token");
						if (errorMsg.equals("")) {
							String values = jsonObject.optString("result");
							DLog.e(TAG,"responsebody:" + "====》values: "+values);
							if(!Tools.isEmpty(token) || exMsg.contains("数据提交成功") || values ==null || values.equals("") || values.equals("[]") || values.length()  <= 0){
								if( values.equals("[]") || values.length()  <= 0 || values ==null){
									values="";
								}
								handlerSuccess(callBack, values, executeStatus);
							}else{
								handlerSuccess(callBack, values, executeStatus);
							}
						}else if( exMsg.contains("不")){
							handlerException(callBack, executeStatus, exMsg);
						}else {
							handlerException(callBack, executeStatus, exMsg);
						}
					} else  {
						handlerError(callBack, "请求网络异常!");
					}

					//					if (exMsg == null) {
					//						//token不存在或者失效，返回首页
					//						if ("3".equals(executeStatus)) {
					//							DLog.e("http", "httpHandler executeStatus is 3 token不存在或者失效，重新登录");
					//							CommonUtils.logoutProgramRestart(DAppManager.getContext());
					//							return;
					//
					//						}
					//					}
					//
					//					if (exMsg == null) {
					//						if (!"0".equals(executeStatus) && HttpUtil.ERROR_CODE_SERVICE_EXCEPTION.equals(errorCode)) {
					//							exMsg = "服务器返回信息格式有误!";
					//						}
					//					}

					//					if (exMsg == null) {
					//						String errorMsg = jsonObject.optString("errorMsg");
					//						String values = jsonObject.optString("values");
					//						if ("0".equals(executeStatus)) {
					//							handlerSuccess(callBack, values, executeStatus);
					//						} else {
					//							handlerException(callBack, executeStatus, errorMsg);
					//						}
					//
					//					} else {
					//						handlerError(callBack, exMsg);
					//					}
				} catch (Exception e) {
					handlerError(callBack, "请求网络异常!");
				}

			}
		});

	}

	/**
	 * 处理网络异常
	 * @param callBack
	 */
	private void handlerError(final NetCallBack callBack, final Exception error) {
		DLog.e(TAG, "error:" + error.getMessage());
		if (handler != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					try {
						if (callBack != null) {
							callBack.onError(error);
						}
					} catch (Exception e) {
					}
				}
			});
		}
	}

	/**
	 * 处理网络异常
	 * @param callBack
	 * @param msg
	 */
	private void handlerError(final NetCallBack callBack, final String msg) {
		handlerError(callBack, new Exception(msg));
	}

	/**
	 * 业务数据异常处理
	 * @param callBack
	 * @param errorCode
	 * @param errorMsg
	 */
	private void handlerException(final NetCallBack callBack, final String errorCode, final String errorMsg) {
		if (handler != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					try {
						if (callBack != null) {
							callBack.onException(errorCode, errorMsg);
						}
					} catch (Exception e) {
					}
				}
			});
		}
	}

	/**
	 * 请求成功响应
	 * @param callBack
	 * @param values
	 */
	private void handlerSuccess(final NetCallBack callBack, final String values, final String executeStatus) {
		if (handler != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					try {
						if (callBack != null) {
							callBack.onSuccess(values, executeStatus);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 取消请求
	 * @param tag
	 */
	public void cancalByTag(String tag) {
		mOkHttpClient.cancel(tag);
	}

	public OkHttpClient getClient() {
		return mOkHttpClient;
	}

	//authorization	头部

	public String addAuth(){
		String encodedString ="";
		return encodedString;
	}


	//User-Agent	头部

	public String addAgent(){
		String model=android.os.Build.MODEL;
		String nModel=model.replaceAll(" ", "_");
		String str="";
		DLog.d(TAG,"net_User-Agent	头部"+"="+str);
		return str;
	}
}
