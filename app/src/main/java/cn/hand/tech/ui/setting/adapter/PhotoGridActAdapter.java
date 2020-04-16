package cn.hand.tech.ui.setting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.bean.PicBean;
import cn.hand.tech.common.OnMyClickListener;

public class PhotoGridActAdapter  extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private List<PicBean> mList;
	private OnMyClickListener photoClickListener;
	private String mFrom;
	public PhotoGridActAdapter(Context context,List<PicBean> list,String  from){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mList=list;
		this.mFrom=from;

	}
	

	public void setList(List<PicBean> list) {
		if (list != null) {
			this.mList = list;
		} else {
			this.mList = new ArrayList<PicBean>();
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mList != null ){
			return mList.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHold viewHold = null;
		PicBean item = mList.get(position);
		if (convertView == null) {
			viewHold = new ViewHold();
			convertView =inflater.inflate(R.layout.user_photo_act_item, null);
			viewHold.img_photo=(ImageView)convertView.findViewById(R.id.img_photo);

			convertView.setTag(viewHold);
		} else {
			viewHold = (ViewHold) convertView.getTag();
		}
		if(item != null ){
			if(position ==0  &&  "1".equals(mFrom)){
				viewHold.img_photo.setImageDrawable(context.getResources().getDrawable(R.mipmap.user_photo_add));
				viewHold.img_photo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						photoClickListener.onClick(v, position);
					}
				});
			}else{
				final String   personUrl= item.getUrl();
				Bitmap map=getBitmap(personUrl,100,100);
				viewHold.img_photo.setImageBitmap(map);

			}

			
		}
		return convertView;
	}
	
	//预览
	public void setPhotoClickListener(OnMyClickListener listener) {
		photoClickListener = listener;
	}

	class ViewHold {
		ImageView img_photo;
	}

	/**
	 * 根据路径获取图片
	 *
	 * @param filePath  文件路径
	 * @param maxWidth  图片最大宽度
	 * @param maxHeight 图片最大高度
	 * @return bitmap
	 */
	private static Bitmap getBitmap(final String filePath, final int maxWidth, final int maxHeight) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * Return the sample size.
	 *
	 * @param options   The options.
	 * @param maxWidth  The maximum width.
	 * @param maxHeight The maximum height.
	 * @return the sample size
	 */
	private static int calculateInSampleSize(final BitmapFactory.Options options,
											 final int maxWidth,
											 final int maxHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		while ((width >>= 1) >= maxWidth && (height >>= 1) >= maxHeight) {
			inSampleSize <<= 1;
		}
		return inSampleSize;
	}
}
