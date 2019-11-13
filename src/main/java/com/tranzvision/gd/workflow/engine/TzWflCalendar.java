package com.tranzvision.gd.workflow.engine;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日历本
 * @author 张浪
 *
 */
public class TzWflCalendar {
	private TZGDObject tzGDObject;
	private SqlQuery sqlQuery;
	
	private String m_CalendarID;
	private TzRecord m_CalendarCfgRecord;
	
	
	//记录日志
	private static final Logger logger = Logger.getLogger("WorkflowEngine");
	
	
	
	public String getM_CalendarID() {
		return m_CalendarID;
	}
	public void setM_CalendarID(String m_CalendarID) {
		this.m_CalendarID = m_CalendarID;
	}
	public TzRecord getM_CalendarCfgRecord() {
		return m_CalendarCfgRecord;
	}
	public void setM_CalendarCfgRecord(TzRecord m_CalendarCfgRecord) {
		this.m_CalendarCfgRecord = m_CalendarCfgRecord;
	}
	
	
	
	
	
	/**
	 * 构造函数
	 * @param m_CalendarID
	 */
	public TzWflCalendar(String m_CalendarID) {

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		tzGDObject = (TZGDObject) getSpringBeanUtil.getAutowiredSpringBean("TZGDObject");
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		
		this.m_CalendarID = m_CalendarID;
		
		
		//获取工作流业务类别配置信息
		TzRecord calendar_Record = null;
		try {
			calendar_Record = tzGDObject.createRecord("tzms_calendar_tBase");
			calendar_Record.setColumnValue("tzms_calendar_tid", m_CalendarID, true);
			
			if(calendar_Record.selectByKey() == false){
				logger.error("获取日历本定义失败");
				throw new TzException("获取日历本定义失败");
			}
			
			this.m_CalendarCfgRecord = calendar_Record;
		} catch (TzException e) {
			e.printStackTrace();
		}
	}


	
	
	/**
	 * 日期天数加减
	 * @param date	要操作的日期
	 * @param dats	正数表示“加”，负数表示“减”
	 * @return
	 */
	private Date addToDate(Date date, int dats){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(Calendar.DAY_OF_YEAR, dats);//日期加减
		
		return calendar.getTime();
	}
	
	
	
	
	
	/**
	 * 是否工作日
	 * @param date
	 * @return
	 */
	private boolean IsWorkDay(Date date){
		try{
			if(date == null) {
				return false;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date _date = dateFormat.parse(dateFormat.format(date));
			
			//根据日期类型判断是否工作日
			String isWordDay = sqlQuery.queryForObject("select 'Y' FROM tzms_interval_days_tBase WHERE tzms_interval_date=? and tzms_spcdttype=2", 
					new Object[]{ _date }, "String");
			
			if("Y".equals(isWordDay)) {
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	/**
	 * 判断指定日期是否计算
	 * @param rtnDate
	 * @return
	 */
	private boolean isDateCalculate(Date rtnDate) {
		String exists = sqlQuery.queryForObject("select top 1 'Y' FROM tzms_interval_days_tBase WHERE tzms_interval_date=?", 
				new Object[]{ rtnDate }, "String");
		if("Y".equals(exists)) {
			return true;
		}else {
			return false;
		}
	}
	
	
	
	
	/**
	 * 根据指定的日期时间返回可以开始计时的日期
	 * @param date
	 * @return
	 */
	public Date GetStartTimingDate(Date date){
		Date rtnDate = null;
		
		if(date == null){
			date = new Date();
		}
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			String time = dateFormat.format(date);
			
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			Date _date = dateFormat2.parse(dateFormat2.format(date));
			
			//工作日计时结束时间
			String wkdttime = m_CalendarCfgRecord.getTzString("tzms_wkdttime").getValue();
			
			if(time.compareTo(wkdttime) <= 0){
				rtnDate = _date;
			}else{
				rtnDate = this.addToDate(_date, 1);
			}
			
			while(this.IsWorkDay(rtnDate) == false){
				if(this.isDateCalculate(rtnDate)) {
					rtnDate = this.addToDate(rtnDate, -1);
				}else {
					rtnDate = null;
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return rtnDate;
	}
	
	
	
	
	/**
	 * 根据指定的日期返回截止计算日期， 如果日历本没有计算，直接返回开始日期加上工作日天数后的日期
	 * @param s_datetime	开始日期
	 * @param WorkDataNum	工作日天数
	 * @return
	 */
	public Date GetEndTimingDate(Date s_datetime, int WorkDataNum){
		//获取开始计时日期
		Date date = this.GetStartTimingDate(s_datetime);
		
		//开始计时日期算第一天
		if(WorkDataNum > 0) WorkDataNum --;
		
		if(date == null) {	//指定日期在日历本中没有计算
			logger.info("指定日期【"+ s_datetime +"】在日历本中没有计算");
			//直接返回指定日期加上工作日天数
			return this.addToDate(s_datetime, WorkDataNum);
		}else {	
			Integer days = sqlQuery.queryForObject("select tzms_interval_days from tzms_interval_days_t where tzms_calendar_uniqueid=? and tzms_interval_date=?", 
					new Object[]{ m_CalendarID, date }, "Integer");
			
			if(days != null && days > 0){
				days += WorkDataNum;
				
				Date rtnDate = sqlQuery.queryForObject("select tzms_interval_date from tzms_interval_days_t where tzms_calendar_uniqueid=? and tzms_interval_days=? and tzms_spcdttype=2", 
						new Object[]{ m_CalendarID, days }, "Date");
				
				if(rtnDate != null) {	
					return rtnDate;
				}else {
					//没有计算
					return this.addToDate(s_datetime, WorkDataNum);
				}
			}else{
				return this.addToDate(s_datetime, WorkDataNum);
			}
		}
	}
}
