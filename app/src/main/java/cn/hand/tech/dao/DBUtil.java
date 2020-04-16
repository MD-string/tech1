package cn.hand.tech.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import cn.hand.tech.log.DLog;


/**
 * DB Util
 * 
 * @date 2015.9.20
 */
public class DBUtil {
	private static final String TAG = "DBUtil";
	private static boolean mIsOutLog = false;

	public static Object getObject(Cursor cursor, Class<?> c) {
		if (mIsOutLog) {
			DLog.i(TAG, "class getObject start------------------");
		}
		Object bean = null;

		if (cursor != null) {
			if (c.equals(null)) {
				if (mIsOutLog) {
					DLog.e(TAG, "class is null");
				}
			} else {
				try {
					bean = c.newInstance();
				} catch (Exception e1) {
					return null;
				}
				Field[] fs = c.getDeclaredFields();
				for (int i = 0; i < fs.length; i++) {
					Field f = fs[i];
					if (Modifier.isStatic(f.getModifiers())) {
						continue;
					}
					try {
						f.setAccessible(true);
						Type type = f.getGenericType();
						String value = cursor.getString(cursor.getColumnIndex(f.getName()));
						if (value == null) {
							continue;
						}
						if (mIsOutLog) {
							DLog.e(TAG, f.getName() + "=" + value);
						}
						if (type.equals(int.class)) {
							f.setInt(bean, Integer.valueOf(value));
						} else if (type.equals(long.class)) {
							f.setLong(bean, Long.valueOf(value));
						} else if (type.equals(double.class)) {
							f.setDouble(bean, Double.valueOf(value));
						} else if (type.equals(boolean.class)) {
							f.setBoolean(bean, Boolean.valueOf(value));
						} else {
							f.set(bean, value);
						}
					} catch (Exception e) {
					}
				}
			}
		} else {
			if (mIsOutLog) {
				DLog.e(TAG, "current param cursor is null");
			}
		}
		if (mIsOutLog) {
			DLog.i(TAG, "class getObject end------------------");
		}
		return bean;
	}

	public static ContentValues getContentValues(Object obj) {
		ContentValues cValues = new ContentValues();

		if (obj != null) {
			Field[] fs = obj.getClass().getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				
				if (Modifier.isStatic(f.getModifiers())) {
					continue;
				}
				
				f.setAccessible(true);
				Type type = f.getGenericType();
				
				
				try {
					if (f.get(obj) == null) {
						continue;
					}
					if (type.equals(int.class)) {
						cValues.put(f.getName(), f.getInt(obj));
					} else if (type.equals(long.class)) {
						cValues.put(f.getName(), f.getLong(obj));
					} else if (type.equals(double.class)) {
						cValues.put(f.getName(), f.getDouble(obj));
					} else if (type.equals(boolean.class)) {
//						cValues.put(f.getName(), f.getBoolean(obj));
						cValues.put(f.getName(), String.valueOf(f.get(obj)));
					} else {
						cValues.put(f.getName(), (String)f.get(obj));
					}
				} catch (Exception e) {
				}
			}
		} else {
			if (mIsOutLog) {
				DLog.e(TAG, "current param obj is null");
			}
		}
		return cValues;
	}

}
