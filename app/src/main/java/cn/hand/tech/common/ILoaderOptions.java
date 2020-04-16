package cn.hand.tech.common;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.hand.tech.R;

/**
 * imageLoader配置
 *
 */
public class ILoaderOptions {
	
	public static  DisplayImageOptions options =new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_launcher)
//				.showImageForEmptyUri(R.drawable.ic_launcher)
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//				.showImageOnFail(R.drawable.ic_launcher)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	public static DisplayImageOptions optionsRound = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_failure_photo)
				.showImageForEmptyUri(R.drawable.ic_failure_photo)
				.showImageOnFail(R.drawable.ic_failure_photo)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(360))
				.build();
	public static DisplayImageOptions optionsNotRound = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	public static DisplayImageOptions optionsNotCache = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(false)
				.cacheOnDisk(false)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	
	public static  DisplayImageOptions defaultOptions =new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_failure_photo)
				.showImageForEmptyUri(R.drawable.ic_failure_photo)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.showImageOnFail(R.drawable.ic_failure_photo)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	public static  DisplayImageOptions defaultLogoOptions =new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_default_logo)
//				.showImageForEmptyUri(R.drawable.ic_default_logo)
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//				.showImageOnFail(R.drawable.ic_default_logo)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	
	public static DisplayImageOptions optionsPersonRound = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_default_person)
//				.showImageForEmptyUri(R.drawable.ic_default_person)
//				.showImageOnFail(R.drawable.ic_default_person)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(360))
				.build();
	
	public static  DisplayImageOptions noLoadingOptions =new DisplayImageOptions.Builder()
//				.showImageForEmptyUri(R.drawable.ic_not_product_default)
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//				.showImageOnFail(R.drawable.ic_not_product_default)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	public static  DisplayImageOptions disOptions =new DisplayImageOptions.Builder()
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED )//图片会缩放到目标大小完全
	.showImageOnFail(R.drawable.ic_failure_photo)
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.build();
	
}
