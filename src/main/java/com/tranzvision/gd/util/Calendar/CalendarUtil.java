/**
 * 
 */
package com.tranzvision.gd.util.Calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具类
 * 
 * @author SHIHUA
 * @since 2016-03-07
 */
public class CalendarUtil {

	private Calendar calendar;

	private String lanague;

	/**
	 * 构造函数，默认使用当前系统日期
	 */
	public CalendarUtil() {
		calendar = Calendar.getInstance();
		calendar.setTime(new Date());
	}

	/**
	 * 构造函数，设置指定的日期时间
	 * 
	 * @param date
	 */
	public CalendarUtil(Date date) {
		if (null != date) {
			calendar = Calendar.getInstance();
			calendar.setTime(date);
		}
	}

	/**
	 * 设置日期时间
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		if (null != date) {
			calendar = Calendar.getInstance();
			calendar.setTime(date);
		}
	}

	/**
	 * 设置默认的语言
	 * 
	 * @param lang
	 */
	public void setLanague(String lang) {
		this.lanague = lang;
	}

	/**
	 * 获取年份
	 * 
	 * @return
	 */
	public String getDateYear() {
		if (calendar != null) {
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			return year;
		}
		return "";
	}

	/**
	 * 获取月份
	 * 
	 * @return
	 */
	public String getDateMonth() {
		if (calendar != null) {
			int month = calendar.get(Calendar.MONTH) + 1;
			String strMonth = String.valueOf(month);
			return strMonth;
		}
		return "";
	}

	/**
	 * 获取月份，小于10月的前面会补零
	 * 
	 * @return
	 */
	public String getDateMonth0() {
		if (calendar != null) {
			int month = calendar.get(Calendar.MONTH) + 1;
			String strMonth = this.zeroPrefix(month);
			return strMonth;
		}
		return "";
	}

	/***
	 * 获取月份的中、英文名称
	 * 
	 * @return
	 */
	public String getDateMonthWord(String lang) {
		if (lang == null || "".equals(lang)) {
			lang = this.lanague;
		}
		if (calendar != null) {
			int month = calendar.get(Calendar.MONTH) + 1;
			String strMonth = this.getMonthWord(month, lang);
			return strMonth;
		}
		return "";
	}

	/**
	 * 获取月份的英文缩写
	 * 
	 * @return
	 */
	public String getDateMonthInEnglish() {
		if (calendar != null) {
			int month = calendar.get(Calendar.MONTH) + 1;
			String strMonth = this.getMonthWord(month, "ENG");
			return strMonth;
		}
		return "";
	}

	/**
	 * 获取中文月份名称
	 * 
	 * @return
	 */
	public String getDateMonthInChinese() {
		if (calendar != null) {
			int month = calendar.get(Calendar.MONTH) + 1;
			String strMonth = this.getMonthWord(month, "ZHS");
			return strMonth;
		}
		return "";
	}

	/**
	 * 获取具体日期
	 * 
	 * @return
	 */
	public String getDateDay() {
		if (calendar != null) {
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String strDay = String.valueOf(day);
			return strDay;
		}
		return "";
	}

	/**
	 * 获取具体日期，若小于10则前面补零
	 * 
	 * @return
	 */
	public String getDateDay0() {
		if (calendar != null) {
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String strDay = this.zeroPrefix(day);
			return strDay;
		}
		return "";
	}

	/**
	 * 获取星期几
	 * 
	 * @return
	 */
	public String getWeekDay() {
		if (calendar != null) {
			int weekday = calendar.get(Calendar.DAY_OF_WEEK);
			String strWeekDay = String.valueOf(weekday);
			return strWeekDay;
		}
		return "";
	}

	/**
	 * 获取星期的中、英文名称
	 * 
	 * @param lang
	 * @return
	 */
	public String getWeekDayWord(String lang) {
		if (lang == null || "".equals(lang)) {
			lang = this.lanague;
		}
		if (calendar != null) {
			int weekday = calendar.get(Calendar.DAY_OF_WEEK);
			String strWeekDay = this.getWeekDayWord(weekday, lang);
			return strWeekDay;
		}
		return "";
	}

	/**
	 * 获取星期的中文名称
	 * 
	 * @return
	 */
	public String getWeekDayInChinese() {
		if (calendar != null) {
			int weekday = calendar.get(Calendar.DAY_OF_WEEK);
			String strWeekDay = this.getWeekDayWord(weekday, "ZHS");
			return strWeekDay;
		}
		return "";
	}

	/**
	 * 获取星期的英文缩写
	 * 
	 * @return
	 */
	public String getWeekDayInEnglish() {
		if (calendar != null) {
			int weekday = calendar.get(Calendar.DAY_OF_WEEK);
			String strWeekDay = this.getWeekDayWord(weekday, "ENG");
			return strWeekDay;
		}
		return "";
	}

	/**
	 * 给小于10正数增加前缀0
	 * 
	 * @param number
	 * @return
	 */
	public String zeroPrefix(int number) {
		String strRet = "";
		if (number < 10 && number > 0) {
			strRet = "0" + String.valueOf(number);
		} else {
			strRet = String.valueOf(number);
		}
		return strRet;
	}

	/**
	 * 获取给定月份的英文缩写（ENG）、中文（ZHS）名称
	 * 
	 * @param month
	 * @param lang
	 * @return
	 */
	public String getMonthWord(int month, String lang) {
		String strRet = "";
		switch (month) {
		case 1:
			if ("ENG".equals(lang)) {
				strRet = "Jan";
			} else if ("ZHS".equals(lang)) {
				strRet = "一月";
			}
			break;
		case 2:
			if ("ENG".equals(lang)) {
				strRet = "Feb";
			} else if ("ZHS".equals(lang)) {
				strRet = "二月";
			}
			break;
		case 3:
			if ("ENG".equals(lang)) {
				strRet = "Mar";
			} else if ("ZHS".equals(lang)) {
				strRet = "三月";
			}
			break;
		case 4:
			if ("ENG".equals(lang)) {
				strRet = "Apr";
			} else if ("ZHS".equals(lang)) {
				strRet = "四月";
			}
			break;
		case 5:
			if ("ENG".equals(lang)) {
				strRet = "May";
			} else if ("ZHS".equals(lang)) {
				strRet = "五月";
			}
			break;
		case 6:
			if ("ENG".equals(lang)) {
				strRet = "Jun";
			} else if ("ZHS".equals(lang)) {
				strRet = "六月";
			}
			break;
		case 7:
			if ("ENG".equals(lang)) {
				strRet = "Jul";
			} else if ("ZHS".equals(lang)) {
				strRet = "七月";
			}
			break;
		case 8:
			if ("ENG".equals(lang)) {
				strRet = "Aug";
			} else if ("ZHS".equals(lang)) {
				strRet = "八月";
			}
			break;
		case 9:
			if ("ENG".equals(lang)) {
				strRet = "Sep";
			} else if ("ZHS".equals(lang)) {
				strRet = "九月";
			}
			break;
		case 10:
			if ("ENG".equals(lang)) {
				strRet = "Oct";
			} else if ("ZHS".equals(lang)) {
				strRet = "十月";
			}
			break;
		case 11:
			if ("ENG".equals(lang)) {
				strRet = "Nov";
			} else if ("ZHS".equals(lang)) {
				strRet = "十一月";
			}
			break;
		case 12:
			if ("ENG".equals(lang)) {
				strRet = "Dec";
			} else if ("ZHS".equals(lang)) {
				strRet = "十二月";
			}
			break;
		}
		return strRet;
	}

	/**
	 * 获取给定数字的星期名称，英文缩写（ENG）、中文（ZHS）
	 * 
	 * @param weekday
	 * @param lang
	 * @return
	 */
	public String getWeekDayWord(int weekday, String lang) {
		String strRet = "";
		switch (weekday) {
		case 1:
			if ("ENG".equals(lang)) {
				strRet = "Mon";
			} else if ("ZHS".equals(lang)) {
				strRet = "星期一";
			}
			break;
		case 2:
			if ("ENG".equals(lang)) {
				strRet = "Tues";
			} else if ("ZHS".equals(lang)) {
				strRet = "星期二";
			}
			break;
		case 3:
			if ("ENG".equals(lang)) {
				strRet = "Wed";
			} else if ("ZHS".equals(lang)) {
				strRet = "星期三";
			}
			break;
		case 4:
			if ("ENG".equals(lang)) {
				strRet = "Thur";
			} else if ("ZHS".equals(lang)) {
				strRet = "星期四";
			}
			break;
		case 5:
			if ("ENG".equals(lang)) {
				strRet = "Fri";
			} else if ("ZHS".equals(lang)) {
				strRet = "星期五";
			}
			break;
		case 6:
			if ("ENG".equals(lang)) {
				strRet = "Sat";
			} else if ("ZHS".equals(lang)) {
				strRet = "星期六";
			}
			break;
		case 7:
			if ("ENG".equals(lang)) {
				strRet = "Sun";
			} else if ("ZHS".equals(lang)) {
				strRet = "星期日";
			}
			break;
		}

		return strRet;
	}

}
