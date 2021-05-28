package com.shikshankranti.sanghatna.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.shikshankranti.sanghatna.application.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;


/**
 * UncaughtException handler class
 * 
 * @author jiangdg on 2017/6/27.
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {

	private UncaughtExceptionHandler mDefaultHandler;
	private static final CrashHandler instance = new CrashHandler();
	private Context mContext;
	private final java.util.Map<String, String> infos = new HashMap<>();


	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		return instance;
	}

	public void init(Context context, Class<?> activityClass) {
		mContext = context;
		this.setMainActivityClass();
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}


	@RequiresApi(api = Build.VERSION_CODES.R)
	@Override
	public void uncaughtException( Thread thread,  Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			System.out.println("uncaughtException--->" + ex.getMessage());
//			Log.e(TAG, ex.getMessage());
			logError(ex);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
//				Log.e("debug", "error：", e);
			}
			exitApp();
		}
	}

	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		new Thread(() -> {
			Looper.prepare();
			Toast.makeText(mContext.getApplicationContext(),
					"unknown exception and exiting...Please checking logs in sd card！", Toast.LENGTH_LONG).show();
			Looper.loop();
		}).start();
		collectDeviceInfo(mContext.getApplicationContext());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			logError(ex);
		}
		return true;
	}

	private void exitApp() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException ignored) {
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), Objects.requireNonNull(field.get(null)).toString());
			} catch (Exception ignored) {
			}
		}
	}


	@RequiresApi(api = Build.VERSION_CODES.R)
	private void logError(Throwable ex) {

		StringBuilder sb = new StringBuilder();
		for (java.util.Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key).append("=").append(value).append("\n");
		}
		int num = ex.getStackTrace().length;
		for (int i=0;i<num;i++){
			sb.append(ex.getStackTrace()[i].toString());
			sb.append("\n");
		}

		File file = new File(Environment.getStorageDirectory()+"/" + MyApplication.DIRECTORY_NAME +"/log.txt");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write((sb.toString()+"exception："+ex.getLocalizedMessage()).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				assert fos != null;
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setMainActivityClass() {
	}
}
