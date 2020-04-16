package cn.hand.tech.download;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * BitmapLruCache类 主要用于对于缓存爆满时，bitmap位图的释放
 * Created by zdc on 2015/10/18.
 */
public class BitmapLruCache extends LruCache<String, Bitmap> {

	public BitmapLruCache(int maxSize) {
		super(maxSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		if (value == null)
			return 0;
		return value.getHeight() *value.getRowBytes() ;
	}
	
	
}
