package com.densoftinfotech.densoftpaysmart.app_utilities;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {

	public static String formatDate(Date content, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
		try {
			return dateFormat.format(content);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getSqliteTime() {
		return getSqliteTime(Calendar.getInstance());
	}

	public static String getSqliteTime(Calendar dt) {
		
		dt.set(Calendar.MONTH, dt.get(Calendar.MONTH)+1);
		StringBuilder sb = new StringBuilder();
		sb.append(dt.get(Calendar.YEAR) + "-");
		if (dt.get(Calendar.MONTH) < 10) {
			sb.append("0" + dt.get(Calendar.MONTH));
		} else
			sb.append(dt.get(Calendar.MONTH) + "");
		sb.append("-");
		if (dt.get(Calendar.DATE) < 10) {
			sb.append("0" + dt.get(Calendar.DATE));
		} else
			sb.append(dt.get(Calendar.DATE) + "");

		sb.append(" ");

		if (dt.get(Calendar.HOUR_OF_DAY) < 10)
			sb.append("0" + dt.get(Calendar.HOUR_OF_DAY));
		else
			sb.append(dt.get(Calendar.HOUR_OF_DAY) + "");
		sb.append(":");
		if (dt.get(Calendar.MINUTE) < 10)
			sb.append("0" + dt.get(Calendar.MINUTE));
		else
			sb.append(dt.get(Calendar.MINUTE) + "");
		sb.append(":");
		if (dt.get(Calendar.SECOND) < 10)
			sb.append("0" + dt.get(Calendar.SECOND));
		else
			sb.append(dt.get(Calendar.SECOND) + "");

		return sb.toString();
	}

	public static boolean calculate_validity(String days_from, String days_to) {
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");


		try {
			Date dateBefore = myFormat.parse(days_from);
			Date dateAfter = myFormat.parse(days_to);

			if (dateAfter.after(dateBefore))
				return true;

			if (dateAfter.equals(dateBefore))
				return false;
			else
				return false;

		} catch (Exception e) {
			return false;
		}
	}

	public static boolean calculate_time_validity(String time_from, String time_to) {
		SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm");


		try {
			Date timeBefore = myFormat.parse(time_from);
			Date timeAfter = myFormat.parse(time_to);

			if (timeAfter.after(timeBefore))
				return true;

			if (timeAfter.equals(timeBefore))
				return false;
			else
				return false;

		} catch (Exception e) {
			return false;
		}
	}

	public static long getDateDiff(long timeUpdate, long timeNow, TimeUnit timeUnit)
	{
		long diffInMillies = Math.abs(timeNow - timeUpdate);
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	public static String convertDate(String dateInMilliseconds,String dateFormat) {
		return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
	}

	public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if(unit == 'm'){
			dist = dist * 1.609344 * 1000;
		}else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

}
