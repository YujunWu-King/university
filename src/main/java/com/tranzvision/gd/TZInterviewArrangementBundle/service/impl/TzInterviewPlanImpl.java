package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMssjArrTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMssjArrTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;

/**
 * 功能说明：自动生成面试安排计划
 * @author zhang lang
 * 原PS：TZ_GD_MS_ARR_PKG:TZ_GD_MS_JH_CLS
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewPlanImpl")
public class TzInterviewPlanImpl extends FrameworkImpl{
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsTzMssjArrTblMapper psTzMssjArrTblMapper;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/*
	 * 保存面试安排
	 * @see com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl#tzAdd(java.lang.String[], java.lang.String[])
	 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String classID = jacksonUtil.getString("classID");
				String batchID = jacksonUtil.getString("batchID");
				String startDate = jacksonUtil.getString("startDate");
				String endDate = jacksonUtil.getString("endDate");
				String startTime = jacksonUtil.getString("startTime");
				String endTime = jacksonUtil.getString("endTime");
				
				String msLocation = jacksonUtil.getString("msLocation");
				Short maxPerson = Short.parseShort(jacksonUtil.getString("maxPerson"));
				int jg_time = Integer.parseInt(jacksonUtil.getString("jg_time"));
				
				int groupPersonNum = "".equals(jacksonUtil.getString("groupPersonNum")) ? 0 : Integer.parseInt(jacksonUtil.getString("groupPersonNum"));
				int groupPersonDis = "".equals(jacksonUtil.getString("groupPersonDis")) ? 0 : Integer.parseInt(jacksonUtil.getString("groupPersonDis"));

				String dtFormat = getSysHardCodeVal.getDateTimeHMFormat();
				SimpleDateFormat dtSimpleDateFormat = new SimpleDateFormat(dtFormat);
				
				Date startDatetime = dtSimpleDateFormat.parse(startDate+" "+startTime);
				Date endDatetime = dtSimpleDateFormat.parse(endDate+" "+endTime);
				
				if(startDatetime.after(endDatetime) || startDatetime.equals(endDatetime)){
					errMsg[0] = "1";
					errMsg[1] = "开始时间必须小于结束时间";
				}else{
					int count = 0;
					Date planSdate = null,//预约计划安排开始时间
					 	 planEdate = null;//预约计划安排结束时间
					while(true){
						if(count == 0){
							planSdate = startDatetime;
						}else{
							planSdate = planEdate;
							if(groupPersonNum>0 && groupPersonDis>0){
								if(count % groupPersonNum == 0){
									//加上休息时间
									planSdate = this.addToDateForMinute(planEdate,groupPersonDis);
								}
							}
						}
						planEdate = this.addToDateForMinute(planSdate,jg_time);

						if(planEdate.after(endDatetime)){
							//时间大于结束时间，退出循环
							break;
						}
						
						if(!isSameDate(planSdate,planEdate)){//开始结束日期在不允许跨天
							continue;
						}
						
						PsTzMssjArrTbl psTzMssjArrTbl = new PsTzMssjArrTbl();
						
						psTzMssjArrTbl.setTzClassId(classID);
						psTzMssjArrTbl.setTzBatchId(batchID);
						//生成面试计划编号
						String msPlanSeq = String.valueOf(getSeqNum.getSeqNum("TZ_MSSJ_ARR_TBL", "TZ_MS_PLAN_SEQ"));
						psTzMssjArrTbl.setTzMsPlanSeq(msPlanSeq);
						
						psTzMssjArrTbl.setTzMsDate(planSdate);
						psTzMssjArrTbl.setTzStartTm(planSdate);
						psTzMssjArrTbl.setTzEndTm(planEdate);
						
						psTzMssjArrTbl.setTzMsLocation(msLocation);
						psTzMssjArrTbl.setTzMsyyCount(maxPerson);
						psTzMssjArrTbl.setTzMsOpenSta("Y");//默认开启
						psTzMssjArrTbl.setTzMsPubSta("N");//默认未发布
						
						psTzMssjArrTbl.setRowAddedOprid(oprid);
						psTzMssjArrTbl.setRowAddedDttm(dateNow);
						psTzMssjArrTbl.setRowLastmantOprid(oprid);
						psTzMssjArrTbl.setRowLastmantDttm(dateNow);
						
						int rst =psTzMssjArrTblMapper.insert(psTzMssjArrTbl);
						if (rst == 0) {
							errMsg[0] = "1";
							errMsg[1] = "保存数据时发生错误";
						}
						count++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	private Date addToDateForMinute(Date date, int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(Calendar.MINUTE, minute);
		date = calendar.getTime();
		
		return date;
	}
	
	/**
	 * 判断两个时间是否为同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	private boolean isSameDate(Date date1, Date date2) {
       Calendar cal1 = Calendar.getInstance();
       cal1.setTime(date1);

       Calendar cal2 = Calendar.getInstance();
       cal2.setTime(date2);

       boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
               .get(Calendar.YEAR);
       boolean isSameMonth = isSameYear
               && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
       boolean isSameDate = isSameMonth
               && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                       .get(Calendar.DAY_OF_MONTH);

       return isSameDate;
   }
}
