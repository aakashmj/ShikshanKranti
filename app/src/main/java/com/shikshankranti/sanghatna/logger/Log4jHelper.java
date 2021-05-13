package com.shikshankranti.sanghatna.logger;

import android.os.Environment;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Log4jHelper {
	private final static LogConfigurator mLogConfigrator = new LogConfigurator();

	static {
		configureLog4j();
	}

	private static void configureLog4j() {
		Date tdate = new Date();
		DateFormat	dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.US);
		String time = dateFormat.format(tdate);
		time = time + ".txt";
		String fileName = Environment.getExternalStorageDirectory()
				+ "/ShikshanKranti/Logs/" + time;
		String logfileName = Environment.getExternalStorageDirectory()
				+ "/ShikshanKranti/Logs/";
		String rootfileName = Environment.getExternalStorageDirectory()
				+ "/ShikshanKranti/";
		String filePattern = "%d - [%c] - %p : %m%n";
		int maxBackupSize = 10;
		long maxFileSize = 1024 * 1024;
		File rf=new File(rootfileName);
		if(!rf.exists())
			rf.mkdir();
		File f=new File(logfileName);
		if(!f.exists()) {
			f.mkdir();
		}
		configure(fileName, filePattern, maxBackupSize, maxFileSize);
	}

	private static void configure(String fileName, String filePattern,
			int maxBackupSize, long maxFileSize) {
		mLogConfigrator.setFileName(fileName);
		mLogConfigrator.setMaxFileSize(maxFileSize);
		mLogConfigrator.setFilePattern(filePattern);
		mLogConfigrator.setMaxBackupSize(maxBackupSize);
		mLogConfigrator.setUseLogCatAppender(true);
		mLogConfigrator.configure();

	}

	public static org.apache.log4j.Logger getLogger(String name) {
		return org.apache.log4j.Logger
				.getLogger(name);
	}
}