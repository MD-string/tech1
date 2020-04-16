package cn.hand.tech.ui.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.adapter.ImageGridAdapter;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.ImageItem;
import cn.hand.tech.common.KeyConstants;
import cn.hand.tech.log.DLog;
import cn.hand.tech.utils.AlbumHelper;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.ImageUtil;
import cn.hand.tech.utils.ToastUtil;

public class ImageGridActivity extends Activity implements OnClickListener {
	private static final String ARG_MAX_COUNT = "max_count";
	private int maxCount = 5;//最多可以选几张
	// ArrayList<Entity> dataList;
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;
	private Button submit_btn, cancel_btn;
	// private BadgeView subCountView;
	private CheckBox mapCheckBox;
	private boolean isclick = false;
	private boolean checkedState = false;
	private String imageFileName;
	private TextView msgMark;

	private int width;
	private int height;
	private int mCount;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择"+maxCount+"张图片", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};
	private Context mContext;
	private ACache acache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=this;
		setContentView(R.layout.activity_image_grid);
		
		maxCount = getIntent().getIntExtra(ARG_MAX_COUNT, 5);
		
		imageFileName = getIntent().getStringExtra("imageFileName");

		Display display = getWindowManager().getDefaultDisplay();

		width = display.getWidth();
		height = display.getHeight();

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		acache= ACache.get(mContext,"WeightFragment");
		

		initView();

	}

	private void initView() {
		LinearLayout	ll_back=(LinearLayout)findViewById(R.id.ll_back);
		ll_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		gridView = (GridView) findViewById(R.id.gridview);
		submit_btn = (Button) findViewById(R.id.submit_btn);
		submit_btn.setOnClickListener(this);

		// cancel_btn = (Button) findViewById(R.id.cancel_btn);
		// cancel_btn.setOnClickListener(this);

		mapCheckBox = (CheckBox) findViewById(R.id.mapCheckBox);
		mapCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					checkedState = true;
				} else {
					checkedState = true;
				}
			}
		});



		msgMark = (TextView) findViewById(R.id.msg_mark);

		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		dataList=	(List<ImageItem>)acache.getAsObject("imagelist");
//		dataList = (List<ImageItem>) getIntent().getSerializableExtra("imagelist");
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList, mHandler, gridView);
		adapter.setMaxPickCount(maxCount);
		gridView.setAdapter(adapter);

		// subCountView = new BadgeView(this, submit_btn);
		adapter.setTextCallback(new ImageGridAdapter.TextCallback() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onListen(int count) {
				// TODO Auto-generated method stub
				if (count > 0) {
					isclick = true;
					mCount=count;
					msgMark.setText(count + "");
					msgMark.setVisibility(View.VISIBLE);
					mapCheckBox.setVisibility(View.VISIBLE);
					submit_btn.setEnabled(true);
				} else {
					isclick = false;
					msgMark.setVisibility(View.GONE);
					mapCheckBox.setVisibility(View.GONE);
					submit_btn.setEnabled(false);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:
			if(CommonUtils.isFastDoubleClick()){//短时间多次点击
				return;
			}
			if (isclick) {
				// ArrayList<String> list = new ArrayList<String>();
				// Collection<String> c = adapter.map.values();
				// Iterator<String> it = c.iterator();
				// for (; it.hasNext();) {
				// list.add(it.next());
				// }
				// // String path = list.get(0);
				// startActivity(intent);

//				final LoadDialog loadDialog = new LoadDialog(this, R.style.DialogNoTitleStyle);
//				loadDialog.setCancelable(false);
//				loadDialog.setText("正在发送中");
//				loadDialog.show();

				final int maxNumOfPixels = (width / 2) * (height / 2);

				new Thread(new Runnable() {

					ArrayList<String> list = new ArrayList<String>();

					@Override
					public void run() {
						Iterator<String> iterator = adapter.map.values().iterator();
						while (iterator.hasNext()) {
							String value = iterator.next();
							if (value == null) {
								continue;
							}
							DLog.e("ImageGridActivity","选择图片路径："+value);
							list.add(ImageUtil.mSaveBitmap(mContext, value));
						}

						runOnUiThread(new Runnable() {
							public void run() {
								// 发送广播通知有图片发出
								Intent intent = new Intent();
								intent.setAction(KeyConstants.BROADCAST_PATH);
								intent.putExtra(KeyConstants.IMAGE_FROM_FLAG, getIntent()!=null?getIntent().getStringExtra(KeyConstants.IMAGE_FROM_FLAG):null);
								intent.putExtra("path", list);
								intent.putExtra("count", mCount+"");//当前选中图片数量
								intent.putExtra("checkedState", checkedState);
								sendBroadcast(intent);
//								loadDialog.dismiss();
								LookImageActivity.lookImageActivity.finish();
								finish();
							}
						});
					}
				}).start();

			} else {
				ToastUtil.getInstance().showMessage(this, "请选择图片", 400);
			}

			break;

		// case R.id.cancel_btn:
		// // adapter.cancleSelect();
		// finish();
		// LookImageActivity.isFinish = true;
		// break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (adapter != null)
			adapter.clear();
	}

}
