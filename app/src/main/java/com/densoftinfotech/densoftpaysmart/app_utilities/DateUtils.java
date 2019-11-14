package com.densoftinfotech.densoftpaysmart.app_utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

}
