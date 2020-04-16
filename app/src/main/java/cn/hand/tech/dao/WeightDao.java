package cn.hand.tech.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.bean.WeightDataBean;

public class WeightDao {
	private Context c;

	public WeightDao(Context c) {
		this.c = c;
	}
	private static WeightDao mInstance;

	public static WeightDao getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new WeightDao(context);
		}
		return mInstance;
	}

	public List<WeightDataBean> findWeightDataBeans(String tag) {
		Cursor cursor = DdhlDatabase.getInstance(c).query(DdhlDatabase.TABLE_WEIGHT, null, WeightDataBean._TAG+"=?",
				new String[] {tag}, null, null, WeightDataBean._UPLOAD_DATE + " desc");
		List<WeightDataBean> list = convertToList(cursor);
		cursor.close();
		return list;
	}
	public List<WeightDataBean> findweightByasc(String tag) {
		Cursor cursor = DdhlDatabase.getInstance(c).query(DdhlDatabase.TABLE_WEIGHT, null, WeightDataBean._TAG+"=?",
				new String[] {tag}, null, null, WeightDataBean._UPLOAD_DATE + " asc");
		List<WeightDataBean> list = convertToList(cursor);
		cursor.close();
		return list;
	}

	/**
	 * 插入
	 */
	public String insert(WeightDataBean mInfo) {
		String sId = null;
		try {
			ContentValues values = DBUtil.getContentValues(mInfo);
			long id = DdhlDatabase.getInstance(c).insert(DdhlDatabase.TABLE_WEIGHT, values);
			sId = String.valueOf(id);
			mInfo.setId(sId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sId;
	}

	/**
	 * 更新
	 */
	public void update(WeightDataBean msg) {
		if (msg.getDeviceId() == null || msg.getId() == null) {
			return;
		}
		try {
			ContentValues values = DBUtil.getContentValues(msg);
			DdhlDatabase.getInstance(c).update(DdhlDatabase.TABLE_WEIGHT, values, WeightDataBean._UPLOAD_DATE+"=? and "+WeightDataBean._TAG+"=?",
					new String[] { msg.getUploadDate(),msg.getTag()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除WeightDataBean
	 */
	public void deleteWeightDataBean(String time,String tag) {
		try {
			DdhlDatabase.getInstance(c).delete(DdhlDatabase.TABLE_WEIGHT, WeightDataBean._UPLOAD_DATE+"=? and "+WeightDataBean._TAG+"=?", new String[] { time,tag});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除WeightDataBean----删除某次时间保存的数据
	 */
	public void delWeightDataBean(String date,String tag) {
		try {
			DdhlDatabase.getInstance(c).delete(DdhlDatabase.TABLE_WEIGHT, WeightDataBean._DATE+"=? and "+WeightDataBean._TAG+"=?", new String[] { date,tag});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否存在数据
	 *
	 * @return 存在返回ID，不存返回null
	 */
	public String isExist(String time,String tag) {
		if ( time == null || tag== null ) {
			return null;
		}
		Cursor cursor = DdhlDatabase.getInstance(c).query(DdhlDatabase.TABLE_WEIGHT, null, WeightDataBean._UPLOAD_DATE+"=? and "+WeightDataBean._TAG+"=?",
				new String[] {time,tag}, null, null, null);
		WeightDataBean msg = getEntityByMoveToFirst(cursor);
		cursor.close();

		return msg==null ? null : msg.getId();
	}


	/**
	 * 更新或者插入
	 */
	public String updateOrInsert(WeightDataBean bean) {
		String sId = isExist(bean.getUploadDate(),bean.getTag());
		if (sId == null) {
			sId = insert(bean);
		} else {
			update(bean);
		}
		bean.setId(sId);
		return sId;
	}

	private List<WeightDataBean> convertToList(Cursor cursor) {

		List<WeightDataBean> fileList = new ArrayList<WeightDataBean>();
		if (cursor.moveToFirst()) {
			fileList = new ArrayList<WeightDataBean>();
			do {
				WeightDataBean obj = getEntity(cursor);
				fileList.add(obj);
			} while (cursor.moveToNext());
		}
		return fileList;
	}

	private WeightDataBean getEntityByMoveToFirst(Cursor cursor) {
		WeightDataBean obj = null;

		if (cursor.moveToFirst()) {
			obj = getEntity(cursor);
		}
		return obj;
	}

	/**
	 * 转换对象
	 * @param cursor
	 * @return
	 */
	private WeightDataBean getEntity(Cursor cursor) {
		WeightDataBean obj = (WeightDataBean) DBUtil.getObject(cursor, WeightDataBean.class);
		return obj;
	}

}
