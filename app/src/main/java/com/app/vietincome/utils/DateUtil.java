package com.app.vietincome.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {

	public static final SimpleDateFormat FORMAT_MONTH_OF_YEAR = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
	public static final SimpleDateFormat FORMAT_DAY_OF_MONTH = new SimpleDateFormat("MMM dd", Locale.getDefault());
	public static final SimpleDateFormat FORMAT_HOURS = new SimpleDateFormat("HH:mm", Locale.getDefault());

	public static String getStringTime(long timeStamp, SimpleDateFormat format) {
		if (timeStamp == 0) return "";
		Timestamp timestamp = new Timestamp(timeStamp);
		Date date = new Date(timestamp.getTime());
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
