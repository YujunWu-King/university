package com.tranzvision.gd.util.dynamicsBase;

import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 日历本
 * ClassName: DynamicsCalender
 * @author zhongcg 
 * @version 1.0 
 * Create Time: 2019年1月23日 上午9:26:23  
 * Description:
 */
@Service("com.tranzvision.gd.util.dynamicsBase.DynamicsCalender")
public class DynamicsCalender {
	@Autowired
	private SqlQuery sqlQuery;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
   /**
	* Description: 流程超时判断
	* Create Time: 2019年1月23日 上午9:31:49
	* @author zhongcg 
	* @param tzms_wflinsid 流程实例ID
	* @return
	*/
	public boolean judgeTimeoutForLC(String tzms_wflinsid) {
		//查询日历编号,流程开始时间,超时时间
		String sql = "SELECT B.tzms_calendarid,A.tzms_wfstartdt,C.tzms_wflovertime_value FROM tzms_wflins_tbl A,tzms_wfcldn_tBase B,tzms_wflovertime_tBase C WHERE A.tzms_wfcldn_uniqueid = B.tzms_wfcldn_tid AND B.tzms_wfcldn_tid = C.tzms_wflcldnid AND A.tzms_wflinsid=?";
		//日历编号
		String tzms_calendarid = "";
		//流程超时天数
		int tzms_wflovertime_value = 0;
		//流程开始时间
		String tzms_wfstartdt = "";
		try {
			Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] {tzms_wflinsid});
			if(map == null) {
				return false;
			}
			tzms_calendarid = (String) map.get("tzms_calendarid");
			tzms_wflovertime_value =  map.get("tzms_wflovertime_value") == null ? 0 : (int) map.get("tzms_wflovertime_value");
			tzms_wfstartdt = (String) map.get("tzms_wfstartdt");
			if(tzms_calendarid == null || "".equals(tzms_calendarid)) {
				return false;
			}
			if(tzms_wfstartdt == null || "".equals(tzms_wfstartdt)) {
				return false;
			}
			//查询基准日期
			sql = "SELECT B.tzms_interval_date FROM tzms_calendar_tBase A,tzms_interval_days_tBase B WHERE A.tzms_calendar_tid = B.tzms_calendar_uniqueid AND A.tzms_calendar_tid = ?";
			String tzms_interval_date = sqlQuery.queryForObject(sql, new Object[] {tzms_calendarid}, "Stirng");
			
			Date tzms_interval_date2 = sdf.parse(tzms_interval_date);
			Date tzms_wfstartdt2 = sdf.parse(tzms_wfstartdt);
			
			//开始日期距离基准日期天数
			int day1 = sqlQuery.queryForObject("SELECT DATEDIFF(DD,?,?)", new Object[] {tzms_interval_date2, tzms_wfstartdt2}, "int");
			//当前日期距离基准日期天数
			int day2 = sqlQuery.queryForObject("SELECT DATEDIFF(DD,?,GETDATE())", new Object[] {tzms_interval_date2}, "int");
			
			if((day2 - day1) > tzms_wflovertime_value) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return false;
	}
	
	/**
	* Description: 流程步骤超时判断
	* Create Time: 2019年1月23日 上午9:31:49
	* @author zhongcg 
	* @param tzms_stpinsid 步骤实例ID
	* @return
	*/
	public boolean judgeTimeoutForLCBZ(String tzms_stpinsid) {
		//查询日历编号,流程步骤开始时间,超时时间
		//String sql = "SELECT B.tzms_calendarid,A.tzms_wfstartdt,C.tzms_wflovertime_value FROM tzms_wflins_tbl A,tzms_wfcldn_tBase B,tzms_wflovertime_tBase C WHERE A.tzms_wfcldn_uniqueid = B.tzms_wfcldn_tid AND B.tzms_wfcldn_tid = C.tzms_wflcldnid AND A.tzms_wflinsid=?";
		String sql = "SELECT C.tzms_calendarid,A.tzms_stpstartdt,D.tzms_wflovertime_value FROM tzms_stpins_tbl A,tzms_wflstp_tBase B,tzms_wfcldn_tBase C,tzms_stpovertime_tBase D WHERE A.tzms_wflstpid = B.tzms_wflstp_tid AND B.tzms_wfcldn_uniqueid = C.tzms_wfcldn_tid AND B.tzms_wflstp_tid = D.tzms_wflstpid AND A.tzms_stpinsid=?";
		//日历编号
		String tzms_calendarid = "";
		//流程步骤超时天数
		int tzms_wflovertime_value = 0;
		//流程步骤开始时间
		String tzms_wfstartdt = "";
		try {
			Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] {tzms_stpinsid});
			if(map == null) {
				return false;
			}
			tzms_calendarid = (String) map.get("tzms_calendarid");
			tzms_wflovertime_value =  map.get("tzms_wflovertime_value") == null ? 0 : (int) map.get("tzms_wflovertime_value");
			tzms_wfstartdt = (String) map.get("tzms_wfstartdt");
			if(tzms_calendarid == null || "".equals(tzms_calendarid)) {
				return false;
			}
			if(tzms_wfstartdt == null || "".equals(tzms_wfstartdt)) {
				return false;
			}
			//查询基准日期
			sql = "SELECT B.tzms_interval_date FROM tzms_calendar_tBase A,tzms_interval_days_tBase B WHERE A.tzms_calendar_tid = B.tzms_calendar_uniqueid AND A.tzms_calendar_tid = ?";
			String tzms_interval_date = sqlQuery.queryForObject(sql, new Object[] {tzms_calendarid}, "Stirng");
			
			Date tzms_interval_date2 = sdf.parse(tzms_interval_date);
			Date tzms_wfstartdt2 = sdf.parse(tzms_wfstartdt);
			
			//开始日期距离基准日期天数
			int day1 = sqlQuery.queryForObject("SELECT DATEDIFF(DD,?,?)", new Object[] {tzms_interval_date2, tzms_wfstartdt2}, "int");
			//指定日期距离基准日期天数
			int day2 = sqlQuery.queryForObject("SELECT DATEDIFF(DD,?,GETDATE())", new Object[] {tzms_interval_date2}, "int");
			
			if((day2 - day1) > tzms_wflovertime_value) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	* Description: 基于指定日期返回已流逝工作日天数的接口（业务流程）
	* Create Time: 2019年1月23日 上午9:31:49
	* @author zhongcg 
	* @param tzms_wflinsid 流程实例ID
	* @param date 指定日期
	* @return 
	*/
	public int daysForLC(String tzms_wflinsid, Date date) {
		//查询日历编号,流程开始时间,超时时间
		String sql = "SELECT B.tzms_calendarid,A.tzms_wfstartdt,C.tzms_wflovertime_value FROM tzms_wflins_tbl A,tzms_wfcldn_tBase B,tzms_wflovertime_tBase C WHERE A.tzms_wfcldn_uniqueid = B.tzms_wfcldn_tid AND B.tzms_wfcldn_tid = C.tzms_wflcldnid AND A.tzms_wflinsid=?";
		//日历编号
		String tzms_calendarid = "";
		//流程超时天数
		int tzms_wflovertime_value = 0;
		//流程开始时间
		String tzms_wfstartdt = "";
		try {
			Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] {tzms_wflinsid});
			if(map == null) {
				return 0;
			}
			tzms_calendarid = (String) map.get("tzms_calendarid");
			tzms_wflovertime_value =  map.get("tzms_wflovertime_value") == null ? 0 : (int) map.get("tzms_wflovertime_value");
			tzms_wfstartdt = (String) map.get("tzms_wfstartdt");
			if(tzms_calendarid == null || "".equals(tzms_calendarid)) {
				return 0;
			}
			if(tzms_wfstartdt == null || "".equals(tzms_wfstartdt)) {
				return 0;
			}
			//查询基准日期
			sql = "SELECT B.tzms_interval_date FROM tzms_calendar_tBase A,tzms_interval_days_tBase B WHERE A.tzms_calendar_tid = B.tzms_calendar_uniqueid AND A.tzms_calendar_tid = ?";
			String tzms_interval_date = sqlQuery.queryForObject(sql, new Object[] {tzms_calendarid}, "Stirng");
			
			Date tzms_interval_date2 = sdf.parse(tzms_interval_date);
			Date tzms_wfstartdt2 = sdf.parse(tzms_wfstartdt);
			
			//开始日期距离基准日期天数
			int day1 = sqlQuery.queryForObject("SELECT DATEDIFF(DD,?,?)", new Object[] {tzms_interval_date2, tzms_wfstartdt2}, "int");
			//指定日期距离基准日期天数
			int day2 = sqlQuery.queryForObject("SELECT DATEDIFF(DD,?,?)", new Object[] {tzms_interval_date2, date}, "int");
			
			return day2 - day1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	* Description: 基于指定日期返回已流逝工作日天数的接口(流程步骤)
	* Create Time: 2019年1月23日 上午9:31:49
	* @author zhongcg 
	* @param tzms_stpinsid 步骤实例ID
	* @param date 指定日期
	* @return
	*/
	public int daysForLCBZ(String tzms_stpinsid, Date date) {
		//查询日历编号,流程步骤开始时间,超时时间
		//String sql = "SELECT B.tzms_calendarid,A.tzms_wfstartdt,C.tzms_wflovertime_value FROM tzms_wflins_tbl A,tzms_wfcldn_tBase B,tzms_wflovertime_tBase C WHERE A.tzms_wfcldn_uniqueid = B.tzms_wfcldn_tid AND B.tzms_wfcldn_tid = C.tzms_wflcldnid AND A.tzms_wflinsid=?";
		String sql = "SELECT C.tzms_calendarid,A.tzms_stpstartdt,D.tzms_wflovertime_value FROM tzms_stpins_tbl A,tzms_wflstp_tBase B,tzms_wfcldn_tBase C,tzms_stpovertime_tBase D WHERE A.tzms_wflstpid = B.tzms_wflstp_tid AND B.tzms_wfcldn_uniqueid = C.tzms_wfcldn_tid AND B.tzms_wflstp_tid = D.tzms_wflstpid AND A.tzms_stpinsid=?";
		//日历编号
		String tzms_calendarid = "";
		//流程步骤超时天数
		int tzms_wflovertime_value = 0;
		//流程步骤开始时间
		String tzms_wfstartdt = "";
		try {
			Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] {tzms_stpinsid});
			if(map == null) {
				return 0;
			}
			tzms_calendarid = (String) map.get("tzms_calendarid");
			tzms_wflovertime_value =  map.get("tzms_wflovertime_value") == null ? 0 : (int) map.get("tzms_wflovertime_value");
			tzms_wfstartdt = (String) map.get("tzms_wfstartdt");
			if(tzms_calendarid == null || "".equals(tzms_calendarid)) {
				return 0;
			}
			if(tzms_wfstartdt == null || "".equals(tzms_wfstartdt)) {
				return 0;
			}
			//查询基准日期
			sql = "SELECT B.tzms_interval_date FROM tzms_calendar_tBase A,tzms_interval_days_tBase B WHERE A.tzms_calendar_tid = B.tzms_calendar_uniqueid AND A.tzms_calendar_tid = ?";
			String tzms_interval_date = sqlQuery.queryForObject(sql, new Object[] {tzms_calendarid}, "Stirng");
			
			Date tzms_interval_date2 = sdf.parse(tzms_interval_date);
			Date tzms_wfstartdt2 = sdf.parse(tzms_wfstartdt);
			
			//开始日期距离基准日期天数
			int day1 = sqlQuery.queryForObject("SELECT DATEDIFF(DD,?,?)", new Object[] {tzms_interval_date2, tzms_wfstartdt2}, "int");
			//指定日期距离基准日期天数
			int day2 = sqlQuery.queryForObject("SELECT DATEDIFF(DD,?,?)", new Object[] {tzms_interval_date2, date}, "int");
			
			return day2 - day1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
   /**
	* Description: 判断是否是工作日
	* Create Time: 2019年1月23日 下午2:31:43
	* @author zhongcg 
	* @param date 时间
	* @return
	*/
	public boolean isWorkDay(Date date) {
		try {
			if(date == null) {
				return false;
			}
			String date2 = sdf.format(date);
			
			String sql = "SELECT tzms_spcdttype FROM tzms_interval_days_tBase WHERE tzms_interval_date=?";
			//日期类型
			int tzms_spcdttype = sqlQuery.queryForObject(sql, new Object[] {date2}, "int");
			if(tzms_spcdttype == 2) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	 /**
		* Description: 判断是否是节假日
		* Create Time: 2019年1月23日 下午2:31:43
		* @author zhongcg 
		* @param date 时间
		* @return
		*/
		public boolean isHoliDay(Date date) {
			try {
				if(date == null) {
					return false;
				}
				String date2 = sdf.format(date);
				
				String sql = "SELECT tzms_spcdttype FROM tzms_interval_days_tBase WHERE tzms_interval_date=?";
				//日期类型
				int tzms_spcdttype = sqlQuery.queryForObject(sql, new Object[] {date2}, "int");
				if(tzms_spcdttype == 1) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
}
