package cn.hand.tech.net;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.text.TextUtils;

import cn.hand.tech.utils.Tools;
import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

public class HttpUtil {
	private static Gson mGson = new Gson();
	/**
	 * 网络请求数据成功
	 */
	public final static int EXECUTE_SUCCESS = 200;
	/**
	 * 网络请求业务错误
	 */
	public final static int EXECUTE_ERROR = 1;
	
	/**
	 * 请求的头部需要加入这个字段
	 */
	public final static String REQUEST_HEADER_TOKEN = "BINGO-TOKEN";

	/**
	 * 系统标示
	 */
	public final static String REQUEST_PARAM_OS = "Platform";

	public final static String REQUEST_PARAM_OS_VALUE = "Android";
	
	/**
	 * 品牌
	 */
	public final static String REQUEST_BRAND = "Brand";
	public final static String REQUEST_BRAND_VALUE = "bingo";
	
	public final static String REQUEST_VERSION = "Version";
	
	/**
	 * 请求的头部需要加入这个字段
	 */
	public final static String REQUEST_HEADER_USERID = "BINGO-USERID";
	/**
	 * 请求的头部需要加入这个字段
	 */
	public final static String REQUEST_HEADER_SIGN = "sign";
	
	/**
	 * 请求的头部需要加入这个字段
	 */
	public final static String REQUEST_HEADER_TIME = "ddtimestamp";
	
	/**
	 * 请求接口签名参数
	 */
	public final static String REQUEST_PARAM_TOKEN = "token";
	
	/**
	 * 请求接口签名参数
	 */
	public final static String REQUEST_PARAM_JSON = "json";
	
	/**
	 * 删除字符串中的换行以及空白符
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;

	}
	

	/**
	 * get请求拼接参数
	 * @param map 参数map
	 * @return
	 */
	public static String formatPara(Map<String, String> map) {

		if (map == null) return "";
		StringBuilder sb = new StringBuilder();

		Set<Entry<String, String>> entries = map.entrySet();
		Iterator<Entry<String, String>> iterator = entries.iterator();

		Entry<String, String> entry;
		while (iterator.hasNext()) {
			sb.append("&");
			entry = iterator.next();
			if (iterator.hasNext()) {
				sb.append(entry.getKey() + "=" + entry.getValue() + "&");
			} else {
				sb.append(entry.getKey() + "=" + entry.getValue());
			}

		}

		return sb.toString();
	}
	
	public static String getTestResult(int index) {
		String result1, result2, result3, result4 = "";
		//对象类型:
		result1 = "{ " + 
				"					\"values\": {" + 
				"					      \"dealerName\":\"张军服务号\"," + 
				"					      \"phone\":\"13999999999\"," + 
				"					      \"dealerNumber\":\"sunzone\"," + 
				"					      \"email\":\"zoubaoqi987@126.com\"," + 
				"					      \"stores\":[" + 
				"					            {" + 
				"					                  \"storeAddress\":\"深圳\"," + 
				"					                  \"phone\":\"13999999999\"," + 
				"					                  \"storeNumber\":\"store1\"," + 
				"					                  \"storeName\":\"总店\"," + 
				"					                  \"email\":\"\"" + 
				"					            }," + 
				"					            {" + 
				"					                  \"storeAddress\":\"深圳\"," + 
				"					                  \"phone\":\"13999999999\"," + 
				"					                  \"storeNumber\":\"store2\"," + 
				"					                  \"storeName\":\"南山蛇口店\"," + 
				"					                  \"email\":\"\"" + 
				"					            }," + 
				"					            {" + 
				"					                  \"storeAddress\":\"深圳\"," + 
				"					                  \"phone\":\"13999999999\"," + 
				"					                  \"storeNumber\":\"store3\"," + 
				"					                  \"storeName\":\"宝安龙华店\"," + 
				"					                  \"email\":\"\"" + 
				"					            }" + 
				"					      ]" + 
				"					}," + 
				"					\"executeStatus\": 0" + 
				"					}";
		
		//分页对象嵌套集合对象
		result2 = "{" + 
				"\"values\": {" + 
				"    \"autoCount\": \"true\"," + 
				"    \"first\": \"1\"," + 
				"    \"hasNext\": \"false\"," + 
				"    \"hasPre\": \"false\"," + 
				"    \"last\": \"11\"," + 
				"    \"nextPage\": \"1\"," + 
				"    \"order\": \"\"," + 
				"    \"orderBy\": \"\"," + 
				"    \"orderBySetted\": \"false\"," + 
				"    \"pageNo\": \"1\"," + 
				"    \"pageSize\": \"10\"," + 
				"    \"prePage\": \"1\"," + 
				"    \"result\": [" + 
				"  {" + 
				"      \"dealerName\":\"张军服务号\"," + 
				"      \"phone\":\"13999999999\"," + 
				"      \"dealerNumber\":\"sunzone\"," + 
				"      \"email\":\"zoubaoqi987@126.com\"," + 
				"      \"stores\":[" + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store1\"," + 
				"                  \"storeName\":\"总店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store2\"," + 
				"                  \"storeName\":\"南山蛇口店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store3\"," + 
				"                  \"storeName\":\"宝安龙华店\"," + 
				"                  \"email\":\"\"" + 
				"            }" + 
				"      ]" + 
				"  }," + 
				"  {" + 
				"      \"dealerName\":\"张军服务号\"," + 
				"      \"phone\":\"13999999999\"," + 
				"      \"dealerNumber\":\"sunzone\"," + 
				"      \"email\":\"zoubaoqi987@126.com\"," + 
				"      \"stores\":[" + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store1\"," + 
				"                  \"storeName\":\"总店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store2\"," + 
				"                  \"storeName\":\"南山蛇口店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store3\"," + 
				"                  \"storeName\":\"宝安龙华店\"," + 
				"                  \"email\":\"\"" + 
				"            }" + 
				"      ]" + 
				"  }," + 
				"  {" + 
				"      \"dealerName\":\"张军服务号\"," + 
				"      \"phone\":\"13999999999\"," + 
				"      \"dealerNumber\":\"sunzone\"," + 
				"      \"email\":\"zoubaoqi987@126.com\"," + 
				"      \"stores\":[" + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store1\"," + 
				"                  \"storeName\":\"总店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store2\"," + 
				"                  \"storeName\":\"南山蛇口店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store3\"," + 
				"                  \"storeName\":\"宝安龙华店\"," + 
				"                  \"email\":\"\"" + 
				"            }" + 
				"      ]" + 
				"  }," + 
				"  {" + 
				"      \"dealerName\":\"张军服务号\"," + 
				"      \"phone\":\"13999999999\"," + 
				"      \"dealerNumber\":\"sunzone\"," + 
				"      \"email\":\"zoubaoqi987@126.com\"," + 
				"      \"stores\":[" + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store1\"," + 
				"                  \"storeName\":\"总店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store2\"," + 
				"                  \"storeName\":\"南山蛇口店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store3\"," + 
				"                  \"storeName\":\"宝安龙华店\"," + 
				"                  \"email\":\"\"" + 
				"            }" + 
				"      ]" + 
				"  }" + 
				"    ]," + 
				"    \"totalCount\": 1," + 
				"    \"totalPages\": 1" + 
				"}," + 
				"\"executeStatus\": 0" + 
				"}";
		
		
		//数组集合类型：
		result3 = "{ " + 
				"\"values\": [" + 
				"  {" + 
				"      \"dealerName\":\"张军服务号\"," + 
				"      \"phone\":\"13999999999\"," + 
				"      \"dealerNumber\":\"sunzone\"," + 
				"      \"email\":\"zoubaoqi987@126.com\"," + 
				"      \"stores\":[" + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store1\"," + 
				"                  \"storeName\":\"总店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store2\"," + 
				"                  \"storeName\":\"南山蛇口店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store3\"," + 
				"                  \"storeName\":\"宝安龙华店\"," + 
				"                  \"email\":\"\"" + 
				"            }" + 
				"      ]" + 
				"  }," + 
				"  {" + 
				"      \"dealerName\":\"张军服务号\"," + 
				"      \"phone\":\"13999999999\"," + 
				"      \"dealerNumber\":\"sunzone\"," + 
				"      \"email\":\"zoubaoqi987@126.com\"," + 
				"      \"stores\":[" + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store1\"," + 
				"                  \"storeName\":\"总店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store2\"," + 
				"                  \"storeName\":\"南山蛇口店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store3\"," + 
				"                  \"storeName\":\"宝安龙华店\"," + 
				"                  \"email\":\"\"" + 
				"            }" + 
				"      ]" + 
				"  }," + 
				"  {" + 
				"      \"dealerName\":\"张军服务号\"," + 
				"      \"phone\":\"13999999999\"," + 
				"      \"dealerNumber\":\"sunzone\"," + 
				"      \"email\":\"zoubaoqi987@126.com\"," + 
				"      \"stores\":[" + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store1\"," + 
				"                  \"storeName\":\"总店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store2\"," + 
				"                  \"storeName\":\"南山蛇口店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store3\"," + 
				"                  \"storeName\":\"宝安龙华店\"," + 
				"                  \"email\":\"\"" + 
				"            }" + 
				"      ]" + 
				"  }," + 
				"  {" + 
				"      \"dealerName\":\"张军服务号\"," + 
				"      \"phone\":\"13999999999\"," + 
				"      \"dealerNumber\":\"sunzone\"," + 
				"      \"email\":\"zoubaoqi987@126.com\"," + 
				"      \"stores\":[" + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store1\"," + 
				"                  \"storeName\":\"总店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store2\"," + 
				"                  \"storeName\":\"南山蛇口店\"," + 
				"                  \"email\":\"\"" + 
				"            }," + 
				"            {" + 
				"                  \"storeAddress\":\"深圳\"," + 
				"                  \"phone\":\"13999999999\"," + 
				"                  \"storeNumber\":\"store3\"," + 
				"                  \"storeName\":\"宝安龙华店\"," + 
				"                  \"email\":\"\"" + 
				"            }" + 
				"      ]" + 
				"  }" + 
				"    ]," + 
				"\"executeStatus\": 0" + 
				"}";
		//数组集合类型：
		result4 = "{\"values\":{\"fullname\":\"测试\",\"email\":\"admin@dd.cn\",\"mobile\":\"13800138000\",\"qrCode\":\"http://www.baidu.com\",\"address\":\"测试地址\",\"avatar\":\"\",\"delearId\":\"47\",\"storeId\":\"80\",\"salerId\":\"74\",\"storePhone\":\"13659526324\",\"storeName\":\"什么店\",\"imVoipName\":\"8001081500000012\",\"imVoipToken\":\"IXtl8vW3\"},\"executeStatus\":0}";
		
		switch (index) {
		case 1:
			return trim(result1);
		case 2:
			return trim(result2);
		case 3:
			return trim(result3);
		case 4:
			return trim(result4);
		}
		
		return result1;
	}
	
	/**
	 * 获取网络数据中的VALUES字段数据
	 */
	public static String getValuesJson(String result) throws Exception {
		String exMsg = null;
		JSONObject jsonObject = null;
		
		try {
			jsonObject = new JSONObject(result);
		} catch (Exception e) {
			exMsg = "服务器返回信息格式有误!";
		}
		
		if (exMsg == null) {
			String executeStatus = jsonObject.optString("executeStatus");
			final String errorMsg = jsonObject.optString("errorMsg");
			final String errorCode = jsonObject.optString("errorCode");
			final String values = jsonObject.optString("values");
			
			//executeStatus 0 成功
			//executeStatus 1 失败
			if ("1".equals(executeStatus) && !TextUtils.isEmpty(errorMsg)) {
				throw new Exception("数据错误 errorMsg:" + errorMsg + " errorCode:"+errorCode);
			} else {
				return values;
			}
		} else {
			throw new Exception(exMsg);
		}
		
	}
	
	public static <T> T toBean(T t, String result) {
		Type classT = t.getClass().getGenericSuperclass();

		try {
			return mGson.fromJson(result, classT);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T toBean(Class<T> classT, String result) {
		Type mySuperClass = classT;
		
		try {
			return mGson.fromJson(result, mySuperClass);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * json to body
	 * @param jsonString
	 * @return
	 */
	public static RequestBody jsonToBody(JSONObject jsonObject) {
		FormEncodingBuilder fb = new FormEncodingBuilder();
		if (jsonObject == null) {
			return fb.build();
		}
		try {
		@SuppressWarnings("unchecked")
		Iterator<String> iter = jsonObject.keys();
		String key = null;
		String value = null;
		while (iter.hasNext()) {
			key = iter.next();
			value = (String)jsonObject.get(key);
			if (value != null) {
				fb.add(key, value);
			}
		}
		} catch (Exception e) {
		}
		return fb.build();
	}
	
	/**
	 * json to map
	 * @param jsonString
	 * @return
	 */
	public static HashMap<String, String> jsonToMap(JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		try {
		@SuppressWarnings("unchecked")
		Iterator<String> iter = jsonObject.keys();
		String key = null;
		String value = null;
		while (iter.hasNext()) {
			key = iter.next();
			value = (String)jsonObject.get(key);
			map.put(key, value);
		}
		} catch (Exception e) {
			return null;
		}
		return map;
	}
	
	/**
	 * map to body
	 * @param map
	 * @return
	 */
	public static RequestBody mapToBody(Map<String, String> map) {
		FormEncodingBuilder fb = new FormEncodingBuilder();
		if (map == null) {
			return fb.build();
		}
		Set<String> keyset = map.keySet();
		try {
			for (String key : keyset) {
				String value = map.get(key);
				if (value != null) {
					fb.add(key, value);
				}
			}
		} catch (Exception e) {
		}
		
		return fb.build();
	}
	
	/**
	 * bean to body
	 * @param bean
	 * @return
	 */
	public static RequestBody beanToBody(Object bean) {
		if (bean == null) {
			return null;
		}
		
		try {
			String json = mGson.toJson(bean);
			return jsonToBody(new JSONObject(json));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * bean to map
	 * @param bean
	 * @return
	 */
	public static HashMap<String, String> beanToMap(Object bean) {
		if (bean == null) {
			return null;
		}
		
		try {
			String json = mGson.toJson(bean);
			return jsonToMap(new JSONObject(json));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static boolean isSuccess(String executeStatus) {
		return String.valueOf(EXECUTE_SUCCESS).equals(executeStatus);
	}
	
	/**
	 * 参数排序
	 * 过滤空值的参数
	 * @return
	 */
	public static String sortMap(Map<String, String> map) {
		if (map == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		SortedMap<String, String> sort = new TreeMap<String, String>(map);
		Set<Entry<String, String>> entrySet = sort.entrySet();
		for (Iterator<Entry<String, String>> iter = entrySet.iterator(); iter.hasNext();) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String value = entry.getValue();
			
			if (!Tools.isEmpty(value)) {
				sb.append(key);
				sb.append("=");
				sb.append(value);
				sb.append("&");
			}
		}
		
		return sb.toString();
	}
	
}
