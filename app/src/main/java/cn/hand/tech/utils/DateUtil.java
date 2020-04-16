package cn.hand.tech.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cn.hand.tech.common.DPrefUtil;

/**
 * 时间转换工具类
 * @date 2015.10.1
 */
public class DateUtil {

	public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
	public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat shortYearFormat = new SimpleDateFormat("yy-MM-dd");
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat sequenceFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat yearFormat2 = new SimpleDateFormat("yyyy年MM月dd日");
	public static final SimpleDateFormat monthDayFormat = new SimpleDateFormat("M.d");
	public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	private static final long ONEDAY = 86400000;
	public static final int SHOW_TYPE_SIMPLE = 0;
	public static final int SHOW_TYPE_COMPLEX = 1;
	public static final int SHOW_TYPE_ALL = 2;
	public static final int SHOW_TYPE_CALL_LOG = 3;
	public static final int SHOW_TYPE_CALL_DETAIL = 4;

	/**
	 * 获取当前当天日期   yyyyMMddHHmmss
	 *
	 * @return
	 */
	public static String getCuDTime() {
		Date d = new Date(System.currentTimeMillis());
		String formatDate = sequenceFormat.format(d);
		DPrefUtil.putString(DPrefUtil.KEY_CURRENT_TIME, formatDate);
		return formatDate;

	}


	/**
	 * 获取当前当天日期的毫秒数 2012-03-21的毫秒数
	 *
	 * @return
	 */
	public static long getCurrentDayTime() {
		Date d = new Date(System.currentTimeMillis());
		String formatDate = yearFormat.format(d);
		try {
			return (yearFormat.parse(formatDate)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String formatDate(int year, int month, int day) {
		Date d = new Date(year - 1900, month, day);
		return yearFormat.format(d);
	}

	public static long getDateMills(int year, int month, int day) {
		//Date d = new Date(year, month, day);
		// 1960 4 22
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.set(year, month, day);
		TimeZone tz = TimeZone.getDefault();
		calendar.setTimeZone(tz);
		return calendar.getTimeInMillis();
	}

	public static String getDateString(long time, int type) {
		Calendar c = Calendar.getInstance();
		c = Calendar.getInstance(tz);
		c.setTimeInMillis(time);
		long currentTime = System.currentTimeMillis();
		Calendar current_c = Calendar.getInstance();
		current_c = Calendar.getInstance(tz);
		current_c.setTimeInMillis(currentTime);

		int currentYear = current_c.get(Calendar.YEAR);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		long t = currentTime - time;
		long t2 = currentTime - getCurrentDayTime();
		String dateStr = "";
		if (t < t2 && t > 0) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = "今天  ";
			}else {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (t < (t2 + ONEDAY) && t > 0) {
			if (type == SHOW_TYPE_SIMPLE || type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = "昨天  ";
			} else if (type == SHOW_TYPE_COMPLEX ) {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (y == currentYear) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
						+ "日";
			} else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
				dateStr = (m < 10 ? "0" + m : m) + /* 月 */"-"
						+ (d < 10 ? "0" + d : d) + /* 日 */" "
						+ (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = y + "-" + (m < 10 ? "0" + m : m) + "-"
						+ (d < 10 ? "0" + d : d);
			} else {
				dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
						+ "日 " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = y + "-" + (m < 10 ? "0" + m : m) + "-"
						+ (d < 10 ? "0" + d : d);
			} else if (type == SHOW_TYPE_COMPLEX ) {
				dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
						+ (d < 10 ? "0" + d : d) + "日";
			} else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
				dateStr = y + /* 年 */"-" + (m < 10 ? "0" + m : m) + /* 月 */"-"
						+ (d < 10 ? "0" + d : d) + /* 日 */"  "/*
						 * + (hour < 10
						 * ? "0" + hour
						 * : hour) + ":"
						 * + (minute <
						 * 10 ? "0" +
						 * minute :
						 * minute)
						 */;
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = y + "-" + (m < 10 ? "0" + m : m) + "-"
						+ (d < 10 ? "0" + d : d);
			} else {
				dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
						+ (d < 10 ? "0" + d : d) + "日 "
						+ (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		}
		return dateStr;
	}

	/**
	 * 
	 * @return
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis() / 1000;
	}

	public static long getActiveTimeLong(String result) {
		try {
			Date parse = yearFormat.parse(result);
			return parse.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}

	public static Date getDate(String result) {
		try {
			Date parse = yearFormat.parse(result);
			return parse;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 传入yyyy年MM月dd日 返回yyyy-MM-dd
	 * @param result
	 * @return
	 */
	 public static String getDate2FormatStr(String result) {
		 try {
			 Date parse = yearFormat2.parse(result);
			 return yearFormat.format(parse);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return "";
	 }

	/**
	 * 传入yyyy年MM月dd日 返回yyyy-MM-dd
	 * @param result
	 * @return
	 */
	public static String getDateFor(String result) {
		try {
			Date parse = simpleDateFormat.parse(result);
			return formatter.format(parse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	 /**
	  * 获取指定格式日期的毫秒
	  * @param format
	  * @param dateString
	  * @return
	  */
	 public static long getFormatTimeLong(DateFormat format, String dateString) {
		 if (format == null) {
			 return -1;
		 }
		 try {
			 Date parse = format.parse(dateString);
			 return parse.getTime();
		 } catch (ParseException e) {
			 e.printStackTrace();
		 }
		 return -1;
	 }
	 /**
	  * 获取yy-MM-dd格式的字符串
	  * @param dateString
	  * @return
	  */
	 public static String getShortYearDate(String dateString) {
		 try {
			 Date d = simpleDateFormat.parse(dateString);
			 return shortYearFormat.format(d);
		 } catch (Exception e) {
			 return "";
		 }
	 }
	 /**
	  * 获取yyyy-MM-dd格式的字符串
	  * @param dateString
	  * @return
	  */
	 public static String getYearDate(String dateString) {
		 try {
			 Date d = simpleDateFormat.parse(dateString);
			 return yearFormat.format(d);
		 } catch (Exception e) {
			 return "";
		 }
	 }
	 /**
	  * 获取HH:mm格式的字符串
	  * @param dateString
	  * @return
	  */
	 public static String getHoursTime(String dateString) {
		 try {
			 Date d = simpleDateFormat.parse(dateString);
			 return simpleTimeFormat.format(d);
		 } catch (Exception e) {
			 return "";
		 }
	 }
	 /**
	  * 获取MM:dd格式的字符串
	  * @param dateString
	  * @return
	  */
	 public static String getMonthDay(String dateString) {
		 try {
			 Date d = simpleDateFormat.parse(dateString);
			 return monthDayFormat.format(d);
		 } catch (Exception e) {
			 return "";
		 }
	 }

	 /**
	  * 两个时间串比较
	  * @return
	  */
	 public static int compareTo(String dataStr1, String dataStr2) {
		 try {
			 Date date1 = simpleDateFormat.parse(dataStr1);
			 Date date2 = simpleDateFormat.parse(dataStr2);
			 return date1.compareTo(date2);
		 } catch (ParseException e) {
		 }

		 //比较不出来,返回0
		 return 0;

	 }


	 /**
	  *客户端展示的3天以内，5分钟前，10分钟前，15分钟前，30分钟分前，1小时前，,,,,1天前，2天前，3天前，后续就为 年 月 日
	  */
	 public static String getSpecialFormatDate(String dateString) {   				
		 try {
			 Calendar c = Calendar.getInstance();
			 c = Calendar.getInstance(tz);
			 c.setTimeInMillis(simpleDateFormat.parse(dateString).getTime());
			 long currentTime = System.currentTimeMillis();
			 Calendar current = Calendar.getInstance();
			 current= Calendar.getInstance(tz);
			 current.setTimeInMillis(currentTime);

			 int d = c.get(Calendar.DAY_OF_MONTH);
			 int h = c.get(Calendar.HOUR_OF_DAY);
			 int m = c.get(Calendar.MINUTE);
			 int current_d = current.get(Calendar.DAY_OF_MONTH);
			 int current_h = current.get(Calendar.HOUR_OF_DAY);
			 int current_m =current.get(Calendar.MINUTE);
			 if(current_d==d){//当天
				 if(current_h  == h){
					 int min=current_m-m;
					 if(min <5){
						 return"刚刚";
					 }	else if(min>=5 && min <10){
						 return"5分钟前";
					 }else if(min>=10 && min <15){
						 return"10分钟前";
					 }else if(min>=15 && min <30){
						 return"15分钟前";
					 }else {
						 return"30分钟前";
					 }    				
				 }else {
					 int  nh=current_h-h;
					 return  nh +  "小时前";
				 }

			 }else if(current_d == d+1){
				 return"1天前";
			 }else if(current_d == d+2){
				 return"2天前";
			 }else if(current_d == d+3){
				 return"3天前";
			 }else {//年月日
				 Date date=yearFormat.parse(dateString);
				 return yearFormat.format(date);
			 }

		 } catch (Exception e) {
			 // 获取不到时间 就去客户端时间
			 Date  date   =   new   Date(System.currentTimeMillis());//获取当前时间 
			 String now=yearFormat.format(date);
			 return now;
		 }
	 }

	 //获取当前 是哪一年
	 public static int  getCurrentYear(){
		 long currentTime = System.currentTimeMillis();
		 Calendar current = Calendar.getInstance();
		 current= Calendar.getInstance(tz);
		 current.setTimeInMillis(currentTime);
		 return current.get(Calendar.YEAR);				
	 }

	 //比较当前是否是同一天   true不是同一天   false 是同一天
	 public static boolean  compareDay(long  c,long  current){
		 Calendar current_c = Calendar.getInstance();
		 current_c = Calendar.getInstance(tz);
		 current_c.setTimeInMillis(c);
		 int y = current_c.get(Calendar.YEAR);
		 int m = current_c.get(Calendar.MONTH) + 1;
		 int d = current_c.get(Calendar.DAY_OF_MONTH);

		 Calendar current_cu = Calendar.getInstance();
		 current_cu = Calendar.getInstance(tz);
		 current_cu.setTimeInMillis(current);
		 int y1 = current_cu.get(Calendar.YEAR);
		 int m1 = current_cu.get(Calendar.MONTH) + 1;
		 int d1 = current_cu.get(Calendar.DAY_OF_MONTH);
		 if(y !=y1){
			 return  true;
		 }else{
			 if(m != m1){
				 return  true;
			 }else{
				 if(d !=d1){
					 return  true;
				 }else{
					 return false;	
				 }
			 }
		 }
	 }
}
