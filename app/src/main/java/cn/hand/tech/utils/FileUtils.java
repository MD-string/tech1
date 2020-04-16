package cn.hand.tech.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

import cn.hand.tech.BApplication;


/**
 * 文件操作工具类
 * @author zhengduanchuan
 * @date 2015.9.20
 *
 */
public class FileUtils {
	public static String APP_PATH = "hande/main";
	
	
	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.delete();
	}
	/**
	 * 删除子目录和子目录的文件，不删除本目录
	 */
	public static void deleteChildDirFile(String path) {
		File file = new File(path);
		if (!file.exists() || file.isFile()) {
			file = file.getParentFile();
		}
		
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles != null && childFiles.length > 0) {
				for (int i = 0; i < childFiles.length; i++) {
					deleteDirFile(childFiles[i]);
				}
			}
		}
	}
	
	/**
	 * 删除本目录及子目录和文件
	 */
	public static boolean deleteDirFile(File file) {
		if (file.isFile()) {
			return file.delete();
		}
		
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				return file.delete();
			}
			
			for (int i = 0; i < childFiles.length; i++) {
				deleteDirFile(childFiles[i]);
			}
			return file.delete();
			
		}
		
		return false;
	}
	
	/**
	 * 获取不重复的目录名
	 */
	public static String getParentDirName(String parentPath, String dirName) {
		String path = parentPath + dirName;
		
		File pFile = new File(path);
		if (!pFile.exists() || !pFile.isDirectory()) {
			return dirName;
		}
		
		for (int i = 0; true; i++) {
			String newPath = path + "_" + i;
			pFile = new File(newPath);
			if (!pFile.exists() || !pFile.isDirectory()) {
				return pFile.getName();
			}
		}
	}
	
	public static File getFile(String Path) {
		File file = new File(Path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
		
	}
	
	
	
	public static boolean moveFile(String fromPath, String toPath) {
		File fromFile = new File(fromPath);
		File toFile = new File(toPath);
		if (!fromFile.renameTo(toFile)) {
			toFile.delete();
			return false;
		}
		return true;
	}
	

	public static String getAppSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_PATH;
		} else {
			return BApplication.mContext.getCacheDir().getPath() + "/" + APP_PATH;
		}
	}
	
	public static String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}
	
	/**
	 * copy assets's file to SdCard
	 */
	public static void copyAssetsToSd(String assFileName, String outFileName, Context context) {
		File file = new File(outFileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		InputStream mInput;
		try {
			OutputStream mOutput = new FileOutputStream(outFileName);
			mInput = context.getAssets().open(assFileName);
			byte[] buffer = new byte[1024];
			int length = mInput.read(buffer);
			while (length > 0) {
				mOutput.write(buffer, 0, length);
				length = mInput.read(buffer);

			}
			mOutput.flush();
			mOutput.close();
			mInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getAssetsBitmap(Context context, String path) {

		InputStream open = null;
		try {
			open = context.getAssets().open(path);
			return BitmapFactory.decodeStream(open);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (open != null) {
				try {
					open.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * read file
	 */
	public static String getFileByPath(String path, String name) {
		try {
			File file = new File(path, name);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readline = "";
			StringBuffer sb = new StringBuffer();
			while ((readline = br.readLine()) != null) {
				sb.append(readline).append("\n");
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * read file
	 */
	public static String getFileByPath(String path) {
		try {
			File file = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readline = "";
			StringBuffer sb = new StringBuffer();
			while ((readline = br.readLine()) != null) {
				sb.append(readline).append("\n");
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//从assets 文件夹中获取文件并读取数据
	@SuppressLint("NewApi") public static String getFromAssets(Context context, String fileName) {
		String result = "";
		try {
			InputStream in = context.getAssets().open(fileName);
			//获取文件的字节数
			int lenght = in.available();
			//创建byte数组
			byte[] buffer = new byte[lenght];
			//将文件中的数据读到byte数组中
			in.read(buffer);
			result = new String(buffer, Charset.forName("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	//从assets 文件夹中获取文件并读取数据
	public static String getDataFromAssets(Context context,String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 获取文件的字节流
	 */
	public static byte[] getFileBytes(String path) {
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		try {
			InputStream in = new FileInputStream(f);
			byte b[]=new byte[(int)f.length()];     //创建合适文件大小的数组
			in.read(b);    //读取文件中的内容到b[]数组
			in.close();
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { //文件存在时
				InputStream inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ( (byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				oldfile.delete();//删除原文件
			}
		}
		catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}
}
