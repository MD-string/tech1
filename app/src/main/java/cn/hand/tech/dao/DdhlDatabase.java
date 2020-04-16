package cn.hand.tech.dao;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.hand.tech.bean.WeightDataBean;


/**
 * 数据库 and.bingo.db
 * 
 * 数据库增删该查时注意事项: 1、要把DATABASE_VERSION的值加1 2、要onCreate()方法中，添加执行sql语句
 * 3、要onUpgrade()方法中，添加步骤2中的要执行的sql语句 4、下一次修改时，要删除onUpgrade()方法中上一次sql记录
 * 
 */
@SuppressLint("NewApi")
public class DdhlDatabase extends SQLiteOpenHelper {

	private static DdhlDatabase database = null;
	// 数据库名称常量
	private static final String DATABASE_NAME = "cn.hande.db";
	// 数据库锁
	private static String DB_LOCK = "db_lock";
	// 数据库版本号
	private static final int DATABASE_VERSION = 1;
	
	static final String TABLE_WEIGHT = "t_weight";

	private DdhlDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		getWritableDatabase().enableWriteAheadLogging();
	}

	/**
	 * 得到DdhlDatabase对象
	 * 
	 * @return
	 */
	public static DdhlDatabase getInstance(Context context) {
		synchronized (DB_LOCK) {
			if (database == null) {
				database = new DdhlDatabase(context);
			}
		}
		return database;
	}

	/**
	 * 执行sq查询语句
	 * 
	 * @param sql
	 * @return
	 */
	public synchronized Cursor rawQuery(String sql) {
		Cursor cursor = null;
		synchronized (DB_LOCK) {
			SQLiteDatabase sqLiteDatabase = getReadableDatabase();
			cursor = sqLiteDatabase.rawQuery(sql, null);
		}
		return cursor;
	}
	
	/**
	 * 执行sq查询语句
	 */
	public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		Cursor cursor = null;
		synchronized (DB_LOCK) {
			SQLiteDatabase sqLiteDatabase = getReadableDatabase();
			cursor = sqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		}
		return cursor;
	}
	
	/**
	 * 执行sq查询语句
	 */
	public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		Cursor cursor = null;
		synchronized (DB_LOCK) {
			SQLiteDatabase sqLiteDatabase = getReadableDatabase();
			cursor = sqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		}
		return cursor;
	}
	
	/**
	 * 执行sq查询语句
	 */
	public synchronized Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		synchronized (DB_LOCK) {
			SQLiteDatabase sqLiteDatabase = getReadableDatabase();
			cursor = sqLiteDatabase.query(table, columns, selection, selectionArgs, null, null, null);
		}
		return cursor;
	}

	/**
	 * 插入数据
	 * 
	 * @param table
	 * @param values
	 */
	public synchronized long insert(String table, ContentValues values) {
		long index = -1;
		synchronized (DB_LOCK) {
			SQLiteDatabase sqLiteDatabase = getWritableDatabase();
			index = sqLiteDatabase.insert(table, null, values);
		}
		return index;
	}

	/**
	 * 删除数据
	 * 
	 * @param table
	 */
	public synchronized int delete(String table, String whereClause, String[] whereArgs) {
		synchronized (DB_LOCK) {
			SQLiteDatabase sqLiteDatabase = getWritableDatabase();
			return sqLiteDatabase.delete(table, whereClause, whereArgs);
		}
	}

	/**
	 * 更新数据
	 * 
	 * @param table
	 * @param values
	 */
	public synchronized void update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		synchronized (DB_LOCK) {
			SQLiteDatabase sqLiteDatabase = getWritableDatabase();
			sqLiteDatabase.update(table, values, whereClause, whereArgs);
		}
	}
	
	/**
	 * 执行sql语句
	 * 
	 */
	public synchronized void execSQL(String sql,Object[] bindArgs) {
		synchronized (DB_LOCK) {
			SQLiteDatabase sqLiteDatabase = getWritableDatabase();
			sqLiteDatabase.execSQL(sql, bindArgs);
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (db != null) {
			// 创建客户表
			String sql1 = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_WEIGHT
					+ "("
					+ WeightDataBean._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ WeightDataBean._NUB + " VARCHAR,"
					+ WeightDataBean._DATE + " VARCHAR,"
					+ WeightDataBean._TAG + " VARCHAR,"
					+ WeightDataBean._DEVICE_ID + " VARCHAR,"
					+ WeightDataBean._CAR_NUMBER + " VARCHAR,"
					+ WeightDataBean._LOCATION + " VARCHAR,"
					+ WeightDataBean._WEIGHT_FORM_DEVICE + " VARCHAR,"
					+ WeightDataBean._WEIGHT_FORM_REAL + " VARCHAR,"
					+ WeightDataBean._HANDBRAK_HARDWARE_STATUS + " VARCHAR,"
					+ WeightDataBean._SENSOR_STATUS + " VARCHAR,"
					+ WeightDataBean._HANDBRAKE_VOLTAGE + " VARCHAR,"
					+ WeightDataBean._UPLOAD_DATE + " VARCHAR,"
					+ WeightDataBean._PACKAGE_NUM + " VARCHAR,"
					+ WeightDataBean._GPS_ID + " VARCHAR,"
					+ WeightDataBean._SPEED + " VARCHAR,"
					+ WeightDataBean._X + " VARCHAR,"
					+ WeightDataBean._Y + " VARCHAR,"
					+ WeightDataBean._GPS_UPLOADDATE + " VARCHAR,"
					+ WeightDataBean._STABLE_STATUS + " VARCHAR,"
					+ WeightDataBean._STABLEVALUE + " VARCHAR,"
					+ WeightDataBean._DEVICE_VOLTAGE + " VARCHAR,"
					+ WeightDataBean._BINVERSION + " VARCHAR,"
					+ WeightDataBean._DEVICEHARDWARE_STATUS + " VARCHAR,"
					+ WeightDataBean._DEVICESOFTWARE_STATUS + " VARCHAR,"
					+ WeightDataBean._DEVICE_WIRESTATUS + " VARCHAR,"
					+ WeightDataBean._SHIPMENT_STATUS + " VARCHAR,"
					+ WeightDataBean._ACH1 + " VARCHAR,"
					+ WeightDataBean._ACH2 + " VARCHAR,"
					+ WeightDataBean._ACH3 + " VARCHAR,"
					+ WeightDataBean._ACH4 + " VARCHAR,"
					+ WeightDataBean._ACH5 + " VARCHAR,"
					+ WeightDataBean._ACH6 + " VARCHAR,"
					+ WeightDataBean._ACH7 + " VARCHAR,"
					+ WeightDataBean._ACH8 + " VARCHAR,"
					+ WeightDataBean._ACH9 + " VARCHAR,"
					+ WeightDataBean._ACH10 + " VARCHAR,"
					+ WeightDataBean._ACH11 + " VARCHAR,"
					+ WeightDataBean._ACH12 + " VARCHAR,"
					+ WeightDataBean._ACH13 + " VARCHAR,"
					+ WeightDataBean._ACH14 + " VARCHAR,"
					+ WeightDataBean._ACH15 + " VARCHAR,"
					+ WeightDataBean._ACH16 + " VARCHAR,"
					+ WeightDataBean._SCH1 + " VARCHAR,"
					+ WeightDataBean._SCH2 + " VARCHAR,"
					+ WeightDataBean._SCH3 + " VARCHAR,"
					+ WeightDataBean._SCH4 + " VARCHAR,"
					+ WeightDataBean._SCH5 + " VARCHAR,"
					+ WeightDataBean._SCH6 + " VARCHAR,"
					+ WeightDataBean._SCH7 + " VARCHAR,"
					+ WeightDataBean._SCH8 + " VARCHAR,"
					+ WeightDataBean._SCH9 + " VARCHAR,"
					+ WeightDataBean._SCH10 + " VARCHAR,"
					+ WeightDataBean._SCH11 + " VARCHAR,"
					+ WeightDataBean._SCH12 + " VARCHAR,"
					+ WeightDataBean._SCH13 + " VARCHAR,"
					+ WeightDataBean._SCH14 + " VARCHAR,"
					+ WeightDataBean._SCH15 + " VARCHAR,"
					+ WeightDataBean._SCH16 + " VARCHAR"
					+ ")";

			db.execSQL(sql1);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
