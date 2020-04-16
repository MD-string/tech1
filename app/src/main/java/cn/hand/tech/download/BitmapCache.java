package cn.hand.tech.download;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

public class BitmapCache {

	public final String TAG = getClass().getSimpleName();

	private BitmapLruCache imageCache = new BitmapLruCache((int) (Runtime.getRuntime().maxMemory() / 4));
	public final static String CACHE_LOCK = "CACHE_LOCK";

	private static BitmapCache bitmapCache = null;

	public BitmapCache() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static BitmapCache getBitmapCache() {
		if (bitmapCache == null) {
			bitmapCache = new BitmapCache();
		}
		return bitmapCache;
	}


	
	/**
	 * 往内存Cache中添加键值
	 * 
	 * @param key
	 * @param bmp
	 */
	public void put(String key, Bitmap bmp) {
		if (bmp == null || TextUtils.isEmpty(key)) {
			return;
		}
		synchronized (CACHE_LOCK) {
			imageCache.put(key, bmp);
		}
	}

	/**
	 * 从内存Cache中移除键值
	 * 
	 * @param key
	 * @param bmp
	 */
	public void remove(String key) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		synchronized (CACHE_LOCK) {
			imageCache.remove(key);
		}
	}

	/**
	 * 从内存Cache中获取值
	 * 
	 * @param key
	 * @param bmp
	 */
	public Bitmap get(String key) {
		if (TextUtils.isEmpty(key)) {
			return null;
		}
		Bitmap bitmap = null;
		synchronized (CACHE_LOCK) {

			if (imageCache.get(key) != null) {
				bitmap = imageCache.get(key);
				// 通过软引用，获取图片
				if (bitmap == null) {
					return null;
				}
				if (bitmap != null && bitmap.isRecycled()) {
					return null;
				}
			}

		}
		return bitmap;
	}

	/**
	 * 从内存Cache中清楚所有的bitmap缓存
	 * 
	 * @param key
	 * @param bmp
	 */
	public void clear() {
		Iterator<Entry<String, Bitmap>> iter =  imageCache.snapshot().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Bitmap> entry = iter.next();
			Bitmap bitmap = entry.getValue();
			if (null != bitmap && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
			bitmap = null;
		}
		imageCache.evictAll();
		System.gc();
	}

	public class MyWeakReference<E> extends WeakReference<E> {

		public MyWeakReference(E e) {
			super(e);
			// TODO Auto-generated constructor stub
		}

		protected void finalize() throws Throwable {
			// TODO Auto-generated method stub
			super.finalize();
			Log.d("ee", ">>>>>>>>>>>>>>>>>>>>finalize" +((Bitmap) this.get()).isRecycled());
			((Bitmap) this.get()).recycle();
			
			Log.d("ee", ">>>>>>>>>>>>>>>>>>>>state : " +((Bitmap) this.get()).isRecycled());
		}
	}

}
