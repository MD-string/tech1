package cn.hand.tech.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hand.tech.R;
import cn.hand.tech.common.Bimp;
import cn.hand.tech.common.ImageItem;
import cn.hand.tech.download.BitmapCache;
import cn.hand.tech.utils.ImageUtil;


public class ImageGridAdapter extends BaseAdapter implements OnScrollListener {
	private int maxCount = 5;
	private TextCallback textcallback = null;
	final String TAG = getClass().getSimpleName();
	Activity act;
	List<ImageItem> dataList;
	public Map<String, String> map = new HashMap<String, String>();
	private Handler mHandler;
	private int selectTotal = 0;
	private boolean iscancle;
	private static int picPixel;
	
	private BitmapCache memoryCache = new BitmapCache();
	
	public static interface TextCallback {
		public void onListen(int count);
	}

	public void setTextCallback(TextCallback listener) {
		textcallback = listener;
	}

	public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler, GridView gridView) {
		this.act = act;
		dataList = list;
		this.mHandler = mHandler;

		// ------- oyb add ----
		mGridView = gridView;
		picPixel = act.getResources().getDisplayMetrics().widthPixels / 3;
		taskCollection = new HashSet<BitmapWorkerTask>();
		mGridView.setOnScrollListener(this);

	}
	public void setMaxPickCount(int maxCount){
		this.maxCount = maxCount;
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
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private RelativeLayout layout;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(act).inflate(R.layout.item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.image_item_layout);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ImageItem item = dataList.get(position);

		holder.iv.setTag(item.imagePath);
		setImageView(item.imagePath, holder.iv);

		if (item.isSelected) {
			holder.selected.setImageResource(R.mipmap.ic_list_checked);
		} else {
			holder.selected.setImageResource(R.mipmap.ic_list_unchecked);
		}

		holder.layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iscancle = true;
				String path = dataList.get(position).imagePath;

				if ((Bimp.drr.size() + selectTotal) < maxCount) {
					item.isSelected = !item.isSelected;
					if (item.isSelected) {
						holder.selected.setImageResource(R.mipmap.ic_list_checked);
						Log.e("imagepath", path);
						selectTotal++;
						if (textcallback != null)
							textcallback.onListen(selectTotal);
						map.put(path, path);

					} else if (!item.isSelected) {
						holder.selected.setImageResource(R.mipmap.ic_list_unchecked);
						selectTotal--;
						if (textcallback != null)
							textcallback.onListen(selectTotal);
						map.remove(path);
					}
				} else if ((Bimp.drr.size() + selectTotal) >= maxCount) {
					if (item.isSelected == true) {
						item.isSelected = !item.isSelected;
						holder.selected.setImageResource(R.mipmap.ic_list_unchecked);
						selectTotal--;
						textcallback.onListen(selectTotal);
						map.remove(path);

					} else {
						Message message = Message.obtain(mHandler, 0);
						message.sendToTarget();
					}
				}
			}
		});

		if (!iscancle) {
			holder.selected.setImageResource(R.mipmap.ic_list_unchecked);
		}

		return convertView;
	}

	/**
	 * 取消选择刷新适配器
	 */
	public void cancleSelect() {
		map.clear();
		selectTotal = 0;
		textcallback.onListen(selectTotal);
		for (int i = 0; i < dataList.size(); i++) {
			dataList.get(i).isSelected = false;
		}
		iscancle = false;
		this.notifyDataSetChanged();

	}

	// ---------------oyb add-----------
	private GridView mGridView;
	private Set<BitmapWorkerTask> taskCollection;
	private int mFirstVisibleItem;
	private int mVisibleItemCount;
	private boolean isFirstEnter = true;

	private void setImageView(String imagePath, ImageView iv) {
		Bitmap bitmap = getBitmapFromMemoryCache(imagePath);
		iv.setImageBitmap(bitmap);
		if (bitmap != null) {
			iv.setBackgroundColor(Color.parseColor("#00000000"));
		} else {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = ImageUtil.calculateInSampleSize(opts, 40, 60);
			Bitmap b = null;
			try {
				b = BitmapFactory.decodeResource(act.getResources(),R.mipmap.img_default_pic);
			} catch (OutOfMemoryError e) {
				// TODO Auto-generated catch block
				System.gc();
			}
			iv.setImageBitmap(b);
			b = null;
		}

		bitmap = null;
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
				String imageUrl = dataList.get(i).imagePath;
				Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
				if (bitmap == null) {
					BitmapWorkerTask task = new BitmapWorkerTask();
					taskCollection.add(task);
					task.execute(imageUrl);
				} else {
					ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
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
			ImageView imageView = (ImageView) mGridView.findViewWithTag(imageUrl);
			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
				bitmap = null;
				imageView.setBackgroundColor(Color.parseColor("#00000000"));
			}
			taskCollection.remove(this);
		}
	}

	public void clear() {
		// cancelAllTasks();
		memoryCache.clear();
	}
}
