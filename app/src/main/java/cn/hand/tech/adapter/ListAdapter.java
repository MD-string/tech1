package cn.hand.tech.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * 代码源于jemendo,做了一些修改，添加RecyclerListener
 * ，可以监听ListView非显示项的回收情况，以便对当前执行的任务进行中止和回收。 添加{@link #}
 * 对list滑动的状态进行监听，当滑动停止时，再进行对图片的加载，这种方式避免滑动时加载项过多造成内存溢出。
 * 
 * @param <T>
 */
public abstract class ListAdapter<T> extends BaseAdapter implements RecyclerListener {

	private static final String TAG = "ListAdapter";
	protected List<T> mList;
	protected Context mContext;
	protected ListView mListView;
//	protected final ScrollListenersAdapter defaultListnerAdapter = new ScrollListenersAdapter();
	
	protected boolean hasNextPage = false;

	public ListAdapter(Context context) {
		this.mContext = context;
	}
	public ListAdapter(Context context, List<T> list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if (mList != null)
			return mList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);

	public void setList(List<T> list) {
		if (list != null) {
			this.mList = new ArrayList<T>(list);
		} else {
			this.mList = new ArrayList<T>();
		}
		notifyDataSetChanged();
	}

	public List<T> getList() {
		return mList;
	}

	public void setList(T[] list) {
		ArrayList<T> arrayList;
		if (list != null) {
			arrayList = new ArrayList<T>(list.length);
			for (T t : list) {
				arrayList.add(t);
			}
		} else {
			arrayList = new ArrayList<T>();
		}
		
		setList(arrayList);
	}

	public void addAll(List<T> list) {
		if (this.mList == null) {
			this.mList = new ArrayList<T>();
		}
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	public ListView getListView() {
		return mListView;
	}

	public void setListView(ListView listView) {
		mListView = listView;
	}

	public Object getItemAtPosition(int position) {
		return mList.get(position);
	}

	@Override
	public void onMovedToScrapHeap(View view) {
		// 可继承次方法，在List的某一项被回收的时候使用。
	}

//	public ScrollListenersAdapter getListnersAdapter() {
//		return defaultListnerAdapter;
//	}

//	/**
//	 * 适配器模式，使可设置监听器数量由1个转为多个。
//	 *
//	 */
//	public class ScrollListenersAdapter implements OnXScrollListener {
//		protected final ArrayList<OnScrollListener> mListeners = new ArrayList<OnScrollListener>();
//
//		@Override
//		public void onScrollStateChanged(AbsListView view, int scrollState) {
//			for (OnScrollListener l : mListeners) {
//				l.onScrollStateChanged(view, scrollState);
//			}
//		}
//
//		@Override
//		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//			for (OnScrollListener l : mListeners) {
//				l.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//			}
//		}
//
//		public void addOnScrollListener(OnScrollListener l) {
//			mListeners.add(l);
//		}
//
//		@Override
//		public void onXScrolling(View view) {
//			// TODO Auto-generated method stub
//
//		}
//
//	}

	public void clear() {
		mList.clear();
		notifyDataSetChanged();
	}
	
	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends View> T findViewById(View itemView,int id) {
		return (T) itemView.findViewById(id);
	}

}
