package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.adapter.ImageBucketAdapter;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.ImageBucket;
import cn.hand.tech.common.KeyConstants;
import cn.hand.tech.utils.AlbumHelper;

public class LookImageActivity extends Activity {
	private static final String ARG_MAX_COUNT = "max_count";
	private static final String ARG_TITLE = "title";
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<ImageBucket> dataList;
	ListView buckt_list;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	private int maxCount = 5;//最多可以选几张
	public static Bitmap bimap;
	public static LookImageActivity lookImageActivity;

	public static boolean isFinish;
	private Context context;
	private ACache acache;

	//	
//	public static void startPick(Context context,int maxCount,String from){
//		startPick(context, maxCount, null,null);
//	}
	public static void startPick(Context context,int maxCount,String title,String  from){
		Intent intent = new Intent(context,LookImageActivity.class);
		intent.putExtra(ARG_MAX_COUNT, maxCount);
		intent.putExtra(KeyConstants.IMAGE_FROM_FLAG, from);
		intent.putExtra(ARG_TITLE, title);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this;
		setContentView(R.layout.activity_image_bucket);
		maxCount = getIntent().getIntExtra(ARG_MAX_COUNT, maxCount);
		if(getIntent().getStringExtra(ARG_TITLE) != null){
			((TextView)findViewById(R.id.tv_para_title)).setText(getIntent().getStringExtra(ARG_TITLE));
		}
		lookImageActivity = this;
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		acache= ACache.get(context,"WeightFragment");

		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				dataList = helper.getImagesBucketList(false);
				bimap = BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.ic_failure_photo));
				adapter = new ImageBucketAdapter(LookImageActivity.this, dataList, buckt_list);

				runOnUiThread(new Runnable() {
					public void run() {
						buckt_list.setAdapter(adapter);
					}
				});
			}
		}).start();

	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		try{
			LinearLayout	ll_back=(LinearLayout)findViewById(R.id.ll_back);
			ll_back.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			buckt_list = (ListView) findViewById(R.id.buckt_list);
			buckt_list.setDivider(null);
			buckt_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					/**
					 * 根据position参数，可以获得跟GridView的子View相绑定的实体类，然后根据它的isSelected状态，
					 * 来判断是否显示选中效果。 至于选中效果的规则，下面适配器的代码中会有说明
					 */
					/**
					 * 通知适配器，绑定的数据发生了改变，应当刷新视图
					 */
					Intent intent = new Intent(LookImageActivity.this, ImageGridActivity.class);
					intent.putExtra(KeyConstants.IMAGE_FROM_FLAG, getIntent()!=null?getIntent().getStringExtra(KeyConstants.IMAGE_FROM_FLAG):null);
//					intent.putExtra("imagelist", (Serializable) dataList.get(position).imageList);
					acache.put("imagelist",(Serializable) dataList.get(position).imageList);
					intent.putExtra("imageFileName", dataList.get(position).bucketName);
					intent.putExtra(ARG_MAX_COUNT, maxCount);
					startActivity(intent);
				}

			});
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isFinish) {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (helper != null)
			helper.clearBuckets();
		if (adapter != null)
			adapter.clear();
	}

//	@Override
//	public void onClick(View v) {
//		if (v.getId() == R.id.lookimage_cancel) {
//			finish();
//		}
//	}
}
