package com.app.vietincom.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {

	public static final SimpleDateFormat FORMAT_TASKS_HISTORY = new SimpleDateFormat("MM/dd/yyyy, HH:mmaa", Locale.getDefault());
	public static final SimpleDateFormat FORMAT_TIMELINE = new SimpleDateFormat("MM/dd/yyyy, HH:mm aa", Locale.getDefault());
	public static final SimpleDateFormat FORMAT_CREATE_ORDER = new SimpleDateFormat("HH:mmaa", Locale.getDefault());

	public static String getStringTime(long timeStamp, SimpleDateFormat format) {
		if (timeStamp == 0) return "";
		Date date = new Date(timeStamp * 1000L);
		return format.format(date);
	}

	public static long getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}

	public static long getDiffMinutes(long startTime, long endTime) {
		long diff = (endTime - startTime) * 1000L;
		return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static long toTimestamp(String date){
		return Timestamp.valueOf(date).getTime();
	}
}
