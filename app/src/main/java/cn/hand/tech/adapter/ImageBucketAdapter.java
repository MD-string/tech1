package cn.hand.tech.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.hand.tech.R;
import cn.hand.tech.common.ImageBucket;
import cn.hand.tech.download.BitmapCache;
import cn.hand.tech.utils.ImageUtil;

public class ImageBucketAdapter extends BaseAdapter implements OnScrollListener {
	final String TAG = getClass().getSimpleName();

	private static int picPixel;

	Activity act;
	/**
	 * 图片集列表
	 */
	List<ImageBucket> dataList;

	private BitmapCache memoryCache = new BitmapCache();

	public ImageBucketAdapter(Activity act, List<ImageBucket> list, ListView listView) {
		this.act = act;
		dataList = list;
		mListView = listView;
		picPixel = act.getResources().getDisplayMetrics().widthPixels / 3;
		taskCollection = new HashSet<BitmapWorkerTask>();
		mListView.setOnScrollListener(this);
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView name;
		private TextView count;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Holder holder;
		if (arg1 == null) {
			holder = new Holder();
			arg1 = LayoutInflater.from(act).inflate(R.layout.item_image_bucket, null);
			holder.iv = (ImageView) arg1.findViewById(R.id.image);
			holder.selected = (ImageView) arg1.findViewById(R.id.isselected);
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.count = (TextView) arg1.findViewById(R.id.count);
			arg1.setTag(holder);
		} else {
			holder = (Holder) arg1.getTag();
		}
		ImageBucket item = dataList.get(arg0);
		holder.count.setText("(" + item.count + ")");
		holder.name.setText(item.bucketName);
		if (item.imageList != null && item.imageList.size() > 0) {
			String sourcePath = item.imageList.get(0).imagePath;
			holder.iv.setTag(sourcePath);
			setImageView(sourcePath, holder.iv);
		} else {
			holder.iv.setImageBitmap(null);
			Log.e(TAG, "no images in bucket " + item.bucketName);
		}
		return arg1;
	}

	private ListView mListView;
	private Set<BitmapWorkerTask> taskCollection;
	private int mFirstVisibleItem;
	private int mVisibleItemCount;
	private boolean isFirstEnter = true;

	private void setImageView(String imagePath, ImageView iv) {
		Bitmap bitmap = memoryCache.get(imagePath);
		if (null != bitmap) {
			iv.setImageBitmap(bitmap);
			bitmap = null;
		} else {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = ImageUtil.calculateInSampleSize(opts, 40, 60);
			Bitmap b = null;
			try {
				b = BitmapFactory.decodeStream(act.getResources().openRawResource(R.mipmap.img_default_pic),null ,opts);
			} catch (OutOfMemoryError e) {
				// TODO Auto-generated catch block
				System.gc();
			}
			iv.setImageBitmap(b);
			b = null;
		}
	}

	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @param bitmap
	 *            LruCache的键，这里传入从网络上下载的Bitmap对象。
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			memoryCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的Bitmap对象，或者null。
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return memoryCache.get(key);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
		if (scrollState == SCROLL_STATE_IDLE) {
			loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
		} else {
			cancelAllTasks();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
		mVisibleItemCount = visibleItemCount;
		// 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
		// 因此在这里为首次进入程序开启下载任务。
		if (isFirstEnter && visibleItemCount > 0) {
			loadBitmaps(firstVisibleItem, visibleItemCount);
			isFirstEnter = false;
		}
	}

	private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
		try {
			for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
				ImageBucket item = dataList.get(i);
				String imageUrl = item.imageList.get(0).imagePath;
				Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
				if (bitmap == null) {
					BitmapWorkerTask task = new BitmapWorkerTask();
					taskCollection.add(task);
					task.execute(imageUrl);
				} else {
					ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
					if (imageView != null && bitmap != null) {
						imageView.setImageBitmap(bitmap);
						bitmap = null;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消所有正在下载或等待下载的任务。
	 */
	public void cancelAllTasks() {
		if (taskCollection != null) {
			for (BitmapWorkerTask task : taskCollection) {
				task.cancel(false);
			}
		}
	}

	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		/**
		 * 图片的URL地址
		 */
		private String imageUrl;

		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			// 在后台开始下载图片
			Bitmap bitmap = ImageUtil.getImage(imageUrl, picPixel, picPixel);
			// Bitmap bitmap = ImageUtil.getSmallBitmap(imageUrl);
			if (bitmap != null) {
				// 图片下载完成后缓存到LrcCache中
				addBitmapToMemoryCache(params[0], bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			// 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
			ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
				bitmap = null;
			}
			taskCollection.remove(this);
		}
	}

	public void clear() {
		memoryCache.clear();
	}

}
