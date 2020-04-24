package cn.hand.tech.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.hand.tech.BApplication;
import cn.hand.tech.R;

/**
 * 工具处理类
 * 
 * @author hxz
 * @date 2016.12.5
 * 
 */
public class ImageUtil {
	public static int MAX_WIDTH = 400;
	public static int MAX_HEIGHT = 300;
	public static String APP_PATH = "hande/main";
	public  static String filepath= BApplication.mContext.getCacheDir().getPath() + "/" + APP_PATH;

	//图片黑白处理
	public static Bitmap toGrayscale(Bitmap bmpOriginal)
	{        
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();    

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	public static Bitmap createImageThumbnail(String filePath, int maxNumOfPixels) {
		Bitmap bitmap = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);

		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
		Log.d("ee", "inSampleSize : " + opts.inSampleSize);
		opts.inJustDecodeBounds = false;

		try {
			bitmap = BitmapFactory.decodeFile(filePath, opts);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static String mSaveBitmap(Context contet,String path){
		String mpath="";
		try{

			Uri uriForFile = FileProvider.getUriForFile(contet, contet.getResources().getString(R.string.authorities),new File(path));
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(contet.getContentResolver(), uriForFile);
			// 首先保存图片
			String dirPath = FileUtils.getAppSdcardDir() + "/" + "bg_image" + "/";
			File dirFile = new File(dirPath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			String fileName = System.currentTimeMillis() + ".jpg";
			final File file = new File(dirFile, fileName);

			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			mpath=file.getAbsolutePath();

		}catch (Exception e){
			e.printStackTrace();
		}
		return mpath;
	}

	/**
	 * 将压缩的Bitmap保存
	 * 
	 * @param mBitmap
	 */
	public static String saveBitmap(Bitmap mBitmap, String path) {

		String savePath = filepath + "/" + "pic_csv" + "/" + System.currentTimeMillis() + ".jpg";

		// mBitmap = scaleBitmap(mBitmap, path, 200);

		if (mBitmap == null) {
			return null;
		}

//		int degree = readPictureDegree(path);
		int degree = 0;
		if (degree > 0) {
			mBitmap = ImageUtil.rotaingImageView(degree, mBitmap);
		}

		File f = new File(savePath);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
			mBitmap.compress(CompressFormat.JPEG, 100, fOut);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		} finally {
			try {
				if (fOut != null) {
					fOut.flush();
					fOut.close();
					fOut = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return savePath;
	}

	/**
	 * 根据文件路径将文件转成base64 Alanqiu-2014/4/28
	 * 
	 * @param path
	 * @return
	 */
	public static String encodeBase64File(String path) {
		File file = new File(path);
		byte[] buffer = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			buffer = new byte[(int) file.length()];
			fileInputStream.read(buffer);
			fileInputStream.close();
			return Base64.encodeToString(buffer, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = null;
		try {
			resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
			System.gc();
		}
		return resizedBitmap;
	}

	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 *
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Bitmap getSmallBitmap(String filePath, int maxNumOfPixels) {

		Bitmap bitmap = createImageThumbnail(filePath, maxNumOfPixels);

		return bitmap;
	}

	/**
	 * 根据路径删除图片
	 * 
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			try {
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static Bitmap getImage(String srcPath, float maxWidth, float maxHeight) {
		if (null == srcPath) {
			return null;
		}

		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		} catch (OutOfMemoryError e1) {
			// TODO Auto-generated catch block
			System.gc();
		}

		newOpts.inJustDecodeBounds = false;
		int w = Math.max(newOpts.outWidth, newOpts.outHeight);
		int h = Math.min(newOpts.outWidth, newOpts.outHeight);

		maxWidth = maxWidth < 0 ? MAX_WIDTH : maxWidth;
		maxHeight = maxHeight < 0 ? MAX_HEIGHT : maxHeight;

		float ww = Math.max(maxWidth, maxHeight);
		float hh = Math.min(maxWidth, maxHeight);
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = (int) (Math.max(w / ww, h / hh) + 0.5);

		newOpts.inSampleSize = be > 1 ? be : 1;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		try {
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			System.gc();
		}
		if (null == bitmap) {
			return null;
		}
		// Log.e("PWY_TEST", bitmap.getWidth() + "," + bitmap.getHeight() + "**"
		// + be + "**" + w + "," + h);
		// Log.e("PWY_TEST", ww + "," + hh);
		if (Math.max(bitmap.getWidth(), bitmap.getHeight()) > ww || Math.min(bitmap.getWidth(), bitmap.getHeight()) > hh) {
			w = Math.max(bitmap.getWidth(), bitmap.getHeight());
			h = Math.min(bitmap.getWidth(), bitmap.getHeight());
			double be2 = Math.max(w * 1.0 / ww, h * 1.0 / hh);

			int bitmapW = (int) (bitmap.getWidth() / be2);
			int bitmapH = (int) (bitmap.getHeight() / be2);

			bitmapW = (bitmapW == 0) ? 1 : bitmapW;
			bitmapH = (bitmapH == 0) ? 1 : bitmapH;

			try {
				bitmap = Bitmap.createScaledBitmap(bitmap, bitmapW, bitmapH, true);
			} catch (OutOfMemoryError e) {
				// TODO Auto-generated catch block
				System.gc();
			}
			// Log.e("PWY_TEST", bitmap.getWidth() + "," + bitmap.getHeight() +
			// "--" + be2 + "--" + w + "," + h);
		}
//		int degree = ImageUtil.readPictureDegree(srcPath);
//		if (degree > 0) {
//			bitmap = ImageUtil.rotaingImageView(degree, bitmap);
//		}
		return bitmap;
		// return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 转为圆角图片
	 * 
	 * @param bitmap
	 *            原图
	 * @param pixels
	 *            圆角的度数，数值越大，圆角越大
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 保存app的图片，能再图库中看到
	 */
	public static String saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		String dirPath = FileUtils.getAppSdcardDir() + "/" + "ddhl_image" + "/";
		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(dirFile, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();

			//最后通知图库更新
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(dirFile)));
			return file.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//保存失败返回null
		return null;

	}

	public   static   String   inputStreamString(InputStream   is)   throws   IOException{ 
        ByteArrayOutputStream   baos   =   new   ByteArrayOutputStream(); 
        int   i=-1; 
        while((i=is.read())!=-1){ 
        baos.write(i); 
        } 
       return   baos.toString(); 
}
	
	public static   String   inputStream2String   (InputStream   in)   throws   IOException   { 
        StringBuffer   out   =   new   StringBuffer(); 
        byte[]   b   =   new   byte[4096]; 
        for   (int   n;   (n   =   in.read(b))   !=   -1;)   { 
                out.append(new   String(b,   0,   n)); 
        } 
        in.close();
        return   out.toString(); 
} 
	//把Bitmap转换成流
	public static InputStream datastream (Bitmap bm){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, stream);
		ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
		return is;
	}

	//InputStream转换成String
	public static String convertStreamToString(InputStream is) {   
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
		StringBuilder sb = new StringBuilder();   
		String line = null;   
		try {   
			while ((line = reader.readLine()) != null) {   
				sb.append(line + "/n");   
			}   
		} catch (IOException e) {   
			e.printStackTrace();   
		} finally {   
			try {   
				is.close();   
			} catch (IOException e) {   
				e.printStackTrace();   
			}   

		}  
		return sb.toString();   
	}

	public static File creatNewFile( ){
		File imageFile = null;
		String storagePath;
		File storageDir;
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		try {
			//文件路径是公共的DCIM目录下的/camerademo目录
			storagePath = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
					.getAbsolutePath() + File.separator + "camerademo";
			storageDir = new File(storagePath);
			storageDir.mkdirs();
			imageFile = File.createTempFile(timeStamp, ".jpg", storageDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageFile;
	}


	//质量压缩方法
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		//循环判断如果压缩后图片是否大于1M,大于继续压缩
		while ( baos.toByteArray().length / 1024>1024) {
			//重置baos即清空baos
			baos.reset();
			//这里压缩options%，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 10;//每次都减少10
		}
		//把压缩后的数据baos存放到ByteArrayInputStream中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		//把ByteArrayInputStream数据生成图片
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}


	//图片按比例大小压缩方法（根据路径获取图片并压缩）
	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		//此时返回bm为空
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}
}