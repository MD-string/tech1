package cn.hand.tech;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;

import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.FileUtils;

/**
 * APP异常获取
 * @author hxz
 */
public class AppExceptionHandler implements UncaughtExceptionHandler {

	private UncaughtExceptionHandler defaultExceptionHandler = null;
	private static AppExceptionHandler exceptionHandler = null;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

	public void uncaughtException(Thread thread, Throwable ex) {

		if (!handleException(ex) && defaultExceptionHandler != null) {

			ex.printStackTrace();
			String lastContentStr = "";
			
			File file = FileUtils.getFile(FileUtils.getAppSdcardDir() + "/log/exceptionLog.txt");

			FileInputStream fis = null;

			FileOutputStream fos = null;
			PrintStream pWriter = null;
			try {
				if (file != null) {
					if (!file.exists()) {
						file.createNewFile();
					}
					
					fis = new FileInputStream(file);
					byte[] lastContent = new byte[fis.available()];
					fis.read(lastContent, 0, fis.available());
					fis.close();
					lastContentStr = new String(lastContent);

					fos = new FileOutputStream(file);
					pWriter = new PrintStream(fos);
					pWriter.print(sdf.format(System.currentTimeMillis()) + " : ");
					ex.printStackTrace(pWriter);
					pWriter.append("\n\r");
					pWriter.append(lastContentStr);
					fos.flush();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null)
						fos.close();
					if (pWriter != null)
						pWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		CommonUtils.finishProgram();

		android.os.Process.killProcess(android.os.Process.myPid());

	}

	private AppExceptionHandler() {

	}

	public static AppExceptionHandler getInstance() {
		if (exceptionHandler == null) {
			exceptionHandler = new AppExceptionHandler();
		}
		return exceptionHandler;
	}

	public void init() {
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		} else {
			return false;
		}
	}

}
