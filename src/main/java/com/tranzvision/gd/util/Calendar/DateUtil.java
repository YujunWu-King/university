package com.tranzvision.gd.util.Calendar;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间的格式转换
 * @author CAOY
 *
 */
public class DateUtil {

	public DateUtil() {

	}

	/**
	 * dataString: 2002-10-11, 2002/12/12, 2002.12.12
	 * 
	 * @param dateString
	 * @author: CAOY
	 * @date: 2002-10-20
	 */
	public static java.util.Date parse(String dateString) {
		java.util.StringTokenizer s = new java.util.StringTokenizer(dateString, "-,/,.");

		if (s.countTokens() == 3) {
			int year = Integer.parseInt(s.nextToken());
			int month = Integer.parseInt(s.nextToken()) - 1;
			int day = Integer.parseInt(s.nextToken());

			java.util.Calendar c = java.util.Calendar.getInstance();
			c.set(year, month, day, 0, 0, 0);
			java.util.Date d = c.getTime();
			c = null;
			return d;
		}
		return null;
	}

	/**
	 * dataString: 2002-10-11 12:12:12, 2002/12/12 12:12:12, 2002.12.12 12:12:12
	 * 
	 * @param dateString
	 * @author: Li.Wang
	 * @date: 2002-10-20
	 */
	public static java.util.Date parseTimeStamp(String dateString) {
		java.util.StringTokenizer ss = new java.util.StringTokenizer(dateString, " ");
		if (ss.countTokens() == 2) {
			java.util.StringTokenizer s = new java.util.StringTokenizer(ss.nextToken(), "-,/,.");
			java.util.StringTokenizer _s = new java.util.StringTokenizer(ss.nextToken(), ":");
			if (s.countTokens() == 3 && _s.countTokens() == 3) {
				int year = Integer.parseInt(s.nextToken());
				int month = Integer.parseInt(s.nextToken()) - 1;
				int day = Integer.parseInt(s.nextToken());

				int hour = Integer.parseInt(_s.nextToken());
				int minute = Integer.parseInt(_s.nextToken());
				int second = Integer.parseInt(_s.nextToken());

				java.util.Calendar c = java.util.Calendar.getInstance();
				c.set(year, month, day, hour, minute, second);
				java.util.Date d = c.getTime();
				c = null;
				return d;
			}
		}
		return null;
	}

	/**
	 * parseDate(String)
	 * 
	 * @param dataString
	 *            MUST be "19990101" formal
	 * @return java.util.Date
	 */
	public static java.util.Date parseDate(String dateString) {
		if (dateString.length() != 8)
			return null;

		int year = Integer.parseInt(dateString.substring(0, 4));
		// Debug.p("year=" + year);
		int month = Integer.parseInt(dateString.substring(4, 6));
		// Debug.p("month=" + month);
		int date = Integer.parseInt(dateString.substring(6, 8));
		// Debug.p("date=" + date);
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.set(year, month - 1, date, 0, 0, 0);
		return c.getTime();
	}

	/**
	 * ISOSECDateString
	 * 
	 * @param java.util.Date
	 *            instance
	 * @return String such as: 2002-10-11
	 */
	public static String ISOSECDateString(java.util.Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		StringBuffer dateStringBuffer = new StringBuffer();
		dateStringBuffer.append(c.get(Calendar.YEAR)).append("-").append(c.get(Calendar.MONTH) + 1).append("-")
				.append(c.get(Calendar.DATE));
		c.clear();
		c = null;
		return dateStringBuffer.toString();
	}

	/**
	 * formatDate
	 * 
	 * @param d
	 *            java.util.Date instance
	 * @param formatString
	 *            such as : yyyy-MM-dd hh:mm:ss, yyyy-MM-dd
	 * @return formatedDateString
	 */
	public static String formatDate(java.util.Date d, String formatString) {
		if (d == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		StringBuffer sb = sdf.format(d, new StringBuffer(), new FieldPosition(0));
		return sb.toString();
	}

	public static String formatLongDate(java.util.Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatShortDate(java.util.Date date) {
		return formatDate(date, "yyyyMMdd");
	}

	public static String getYear(java.util.Date date) {
		return formatShortDate(date).substring(0, 4);
	}

	public static String getMonth(java.util.Date date) {
		return formatShortDate(date).substring(4, 6);
	}

	public static String getDay(java.util.Date date) {
		return formatShortDate(date).substring(6, 8);
	}

	public static void main(String[] args) {
		String a = "yyyyMMddHHmmss";
		System.out.println(a.substring(0, 6));
		System.out.println("----begin----");
		// try {
		// java.lang.Thread.sleep(100000);
		// } catch (InterruptedException ex) {
		// }
		System.out.println("----end----");

		System.out.println(formatLongDate(parseTimeStamp("2002-10-11 12:13:14")));
		System.out.println(System.currentTimeMillis());
		System.out.println(formatLongDate(new java.util.Date(System.currentTimeMillis())));
		// System.out.println(Date.parse("2002-10-10"));
		// System.out.println(Date.parse("2002.11.12"));
		// System.out.println(Date.parse("2003/12/12"));

	}
}
