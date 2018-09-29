package com.app.vietincome.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {

	public static final SimpleDateFormat FORMAT_MONTH_OF_YEAR = new SimpleDateFormat("MMM yyyy", Locale.US);
	public static final SimpleDateFormat FORMAT_DAY_OF_MONTH = new SimpleDateFormat("MMM dd", Locale.US);
	public static final SimpleDateFormat FORMAT_HOURS = new SimpleDateFormat("HH:mm", Locale.US);
	public static final SimpleDateFormat FORMAT_EVENT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

	public static String getStringTime(long timeStamp, SimpleDateFormat format) {
		if (timeStamp == 0) return "";
		Timestamp timestamp = new Timestamp(timeStamp);
		Date date = new Date(timestamp.getTime());
		return format.format(date);
	}

	public static String getStringTime(String time, SimpleDateFormat format) {
		if (time.isEmpty()) return "";
		Date date = null;
		try {
			date = format.parse(time);
			return format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static long getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}

	public static long getMonthAfterCurrent() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		return cal.getTimeInMillis();
	}

	public static String getStrCurrentDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/YYYY", Locale.US);
		return dfm.format(cal.getTime());
	}

	public static String getFirstDay3Years() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 3);
		SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/YYYY", Locale.US);
		return dfm.format(cal.getTime());
	}

	public static long getDiffMinutes(long startTime, long endTime) {
		long diff = endTime - startTime;
		return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static long toTimestamp(String date) {
		return Timestamp.valueOf(date).getTime();
	}

	public static String parseDate(String date, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
		return formatter.format(formatter.parse(date));
	}
}
