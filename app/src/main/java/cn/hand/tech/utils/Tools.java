package cn.hand.tech.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hand.tech.common.DAppManager;
import cn.hand.tech.log.DLog;


public class Tools {
	/**
	 * 处理空字符串
	 * 
	 * @param str
	 * @return String
	 */
	public static String doEmpty(String str) {
		return doEmpty(str, "");
	}

	/**
	 * 处理空字符串
	 * 
	 * @param str
	 * @param defaultValue
	 * @return String
	 */
	public static String doEmpty(String str, String defaultValue) {
		if (str == null || str.equalsIgnoreCase("null")
				|| str.trim().equals("") || str.trim().equals("－请选择－")) {
			str = defaultValue;
		} else if (str.startsWith("null")) {
			str = str.substring(4, str.length());
		}
		return str.trim();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.equals("") || str.equals("null");
	}

	/**
	 * 请选择
	 */
	final static String PLEASE_SELECT = "请选择...";

	public static boolean notEmpty(Object o) {
		return o != null && !"".equals(o.toString().trim())
				&& !"null".equalsIgnoreCase(o.toString().trim())
				&& !"undefined".equalsIgnoreCase(o.toString().trim())
				&& !PLEASE_SELECT.equals(o.toString().trim());
	}

	public static boolean empty(Object o) {
		return o == null || "".equals(o.toString().trim())
				|| "null".equalsIgnoreCase(o.toString().trim())
				|| "undefined".equalsIgnoreCase(o.toString().trim())
				|| PLEASE_SELECT.equals(o.toString().trim());
	}

	public static boolean num(Object o) {
		int n = 0;
		try {
			n = Integer.parseInt(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean decimal(Object o) {
		double n = 0;
		try {
			n = Double.parseDouble(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (n > 0.0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断邮箱是否合法
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return false;
		//Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}

		/**
         * 根据给定的时间字符串，返回月 日 时 分 秒
         *
         * @param allDate
         *            like "yyyy-MM-dd hh:mm:ss SSS"
         * @return
         */
	public static String getMonthTomTime(String allDate) {
		return allDate.substring(5, 19);
	}

	/**
	 * 根据给定的时间字符串，返回月 日 时 分 月到分钟
	 * 
	 * @param allDate
	 *            like "yyyy-MM-dd hh:mm:ss SSS"
	 * @return
	 */
	public static String getMonthTime(String allDate) {
		return allDate.substring(5, 16);
	}

	public static boolean isNullWithTrim(String str) {
		return str == null || str.trim().equals("")||str.trim().equals("null");
	}

	/**
	 * 计算原价和折扣价的折扣值
	 * @param price
	 * @return
	 */
	public static String getStringPriceDiscount(String price, String discountPrice) {
		try {
			BigDecimal bd = new BigDecimal(price);
			BigDecimal bdDisPrice = new BigDecimal(discountPrice);

			BigDecimal value = bdDisPrice.multiply(new BigDecimal(10)).divide(bd, 3, BigDecimal.ROUND_DOWN);

			if (!(value.compareTo(new BigDecimal("10")) == -1)) {
				return "";
			}

			DecimalFormat df = new DecimalFormat("#.#");
			df.setGroupingSize(0);
			df.setRoundingMode(RoundingMode.HALF_UP);
			return df.format(value);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 保留两位小数
	 * @return
	 */
	public static String getStringPriceFormat(BigDecimal bd) {
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			df.setGroupingSize(0);
			df.setRoundingMode(RoundingMode.DOWN);
			return df.format(bd);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 保留两位小数
	 * @param price
	 * @return
	 */
	public static String getStringPriceFormat(String price) {
		try {
			BigDecimal bd = new BigDecimal(price);
			return getStringPriceFormat(bd);
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 保留两位小数，已0补全
	 * @return
	 */
	public static String getStringTotalAmountFormat(BigDecimal bd) {
		try {
			DecimalFormat df = new DecimalFormat("0.00");
			df.setGroupingSize(0);
			df.setRoundingMode(RoundingMode.DOWN);
			return df.format(bd);
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 保留两位小数，已0补全
	 * @param price
	 * @return
	 */
	public static String getStringTotalAmountFormat(String price) {
		try {
			BigDecimal bd = new BigDecimal(price);
			return getStringTotalAmountFormat(bd);
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 保留1位小数，已0补全
	 * @return
	 */
	public static String getTotalAmountFormat(BigDecimal bd) {
		try {
			DecimalFormat df = new DecimalFormat("0.0");
			df.setGroupingSize(0);
			df.setRoundingMode(RoundingMode.DOWN);
			return df.format(bd);
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 保留1位小数，已0补全
	 * @param price
	 * @return
	 */
	public static String getTotalAmountFormat(String price) {
		try {
			BigDecimal bd = new BigDecimal(price);
			return getStringTotalAmountFormat(bd);
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 保留两位小数且4舍5入
	 * @param price
	 * @return
	 */
	public static String getStringPriceFormatHalfUp(String price) {
		try {
			BigDecimal bd = new BigDecimal(price);
			DecimalFormat df = new DecimalFormat("#.##");
			df.setGroupingSize(0);
			df.setRoundingMode(RoundingMode.HALF_UP);
			return df.format(bd);
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 保留1位小数且4舍5入
	 * @param price
	 * @return
	 */
	public static String getStringOrderPriceFormat(String price){
		try {
			BigDecimal bd = new BigDecimal(price);
			DecimalFormat df = new DecimalFormat("#.#");
			df.setGroupingSize(0);
			df.setRoundingMode(RoundingMode.DOWN);
			String str=df.format(bd);
			if(str.equals("-0.0")){
				str="0.0";
			}
			return str;
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 不使用科学记数法
	 * @param price
	 * @return
	 */
	public static String getDoubleToString(double price){
		try {
			DecimalFormat df=new DecimalFormat("0.00");
			df.setGroupingSize(0);
			df.setGroupingUsed(false);
			return df.format(price);
		} catch (Exception e) {
			return "";
		}
	}
	/**
	 * 商品浏览、咨询、分享的数量格式
	 * 数量大于999显示999+
	 */
	public static String productMaxCount(String countStr) {
		try {
			int count = Integer.valueOf(countStr);
			if (count > 999) {
				return "999+";
			}
		} catch (Exception e) {
		}
		return countStr;
	}

	//计算字符长度
	public static long calculateLength(CharSequence c) {  
		double len = 0;  
		for (int i = 0; i < c.length(); i++) {  
			int tmp = (int) c.charAt(i);  
			if (tmp > 0 && tmp < 127) {  
				len += 0.5;  
			} else {  
				len++;  
			}  
		}  
		return Math.round(len);  
	}

	/**
	 * 城市名称是否添加市字
	 * @param isAdd	true则添加市字， false则去掉市字
	 */
	public static String cityNameAddStr(String cityName, boolean isAdd) {
		DLog.d("tools>>", "cityNameAddStr cityName:" + cityName + ",isAdd:" + isAdd);
		if (cityName == null) {
			return "";
		}
		if (isAdd) {
			if (!cityName.endsWith("市")) {
				cityName += "市";
			}
		} else {
			if (cityName.endsWith("市")) {
				cityName = cityName.substring(0, cityName.length() - 1);
			}
		}

		return cityName;
	}


	/**
	 * 距离显示规则 
	 * @param distance   单位 /m
	 * @return
	 */
	public static String  showMDistance(String distance){
		if(Tools.isEmpty(distance)){
			return "";
		}else{
			if(distance.contains("m")){
				return distance;
			}else{

				double mDis=Double.parseDouble(distance);
				BigDecimal big=new BigDecimal(mDis/1000);
				DecimalFormat df=new DecimalFormat("0.0");
				String value = df.format(big);
				double num=Double.valueOf(value);
				if(mDis<100){
					return "<100m";
				}else if(mDis>=100 && mDis<1000){
					return ((int)Math.round(mDis))+"m";
				}else if(mDis>=1000 && num<=5000){
					return  num+"km";	
				}else {
					return ">5000km";
				}		
			}	
		}
	}

	/**
	 * 获取状态栏高度
	 */
	public static int getStatusHeight(Context context){
		int statusHeight = CommonUtils.dip2px(context, 25);

		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}

		if (result <= 0) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
				result = context.getResources().getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (result <= 0) {
			return statusHeight;
		} else {
			return result;
		}
	}
	// 两次点击间隔不能少于1000ms
	private static final int FAST_CLICK_DELAY_TIME = 1000;
	private static long lastClickTime;

	public static boolean isFastClick() {
		boolean flag = true;
		long currentClickTime = System.currentTimeMillis();
		if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME ) {
			flag = false;
		}
		lastClickTime = currentClickTime;
		return flag;
	}

	/**
	 * 获取uuid  随机UUID
	 * @return
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		// 去掉“-”符号
		return uuid.substring(0, 8) + uuid.substring(9, 13)
				+ uuid.substring(14, 18) + uuid.substring(19, 23)
				+ uuid.substring(24);

	}

	/**
	 * 获取手机 deviceId
	 * @return
	 */
	public static String getDeviceId() {
		TelephonyManager telephonyManager = (TelephonyManager) DAppManager.mContext.getSystemService( Context.TELEPHONY_SERVICE);

		//获取本机MIEI号码（仅手机存在）
		String deviceId = telephonyManager.getDeviceId();
		return deviceId;

	}
//list  转 string
	public static String listToString(List<String> list){
		if(list==null){
			return null;
		}
		StringBuilder result = new StringBuilder();
		boolean first = true;
		//第一个前面不拼接","
		for(String string :list) {
			if(first) {
				first=false;
			}else{
				result.append(",");
			}
			result.append(string);
		}
		return result.toString();
	}

	  // 将px值转换为sp值，保证文字大小不变
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    
 // 将px值转换为dip或dp值，保证尺寸大小不变
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
