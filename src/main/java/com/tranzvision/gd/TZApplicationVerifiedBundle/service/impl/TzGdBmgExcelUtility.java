package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;

public class TzGdBmgExcelUtility {
	
	private JdbcTemplate jdbcTemplate;
	
	public TzGdBmgExcelUtility() {
		//构造方法
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		this.jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
	}
	
	
	/*获取志愿名称*/
	public String getBmrClassName(String strAppInsId ,String strAppTplId) throws Exception
	{
		Long appInsId = Long.parseLong(strAppInsId);
		
		String strGetClassName = "";
		
		strGetClassName = jdbcTemplate.queryForObject("SELECT B.TZ_CLASS_NAME FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B "
				+ "WHERE A.TZ_APP_INS_ID = ? AND A.TZ_CLASS_ID = B.TZ_CLASS_ID LIMIT 0,1",
				new Object[]{appInsId},String.class);
		
		return strGetClassName;
		
	}
	
	/*获取志愿批次名称*/
	public String getBmrZyName(String strAppInsId ,String strAppTplId) throws Exception
	{
		Long appInsId = Long.parseLong(strAppInsId);
		
		String strGetBatchName = "";
		
		strGetBatchName = jdbcTemplate.queryForObject("SELECT B.TZ_BATCH_NAME FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLS_BATCH_T B "
				+ "WHERE A.TZ_APP_INS_ID = ? AND A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.TZ_BATCH_ID = B.TZ_BATCH_ID LIMIT 0,1",
				new Object[]{appInsId},String.class);
		
		return strGetBatchName;
		
	}
	
	/*获取申请年份*/
	public String getBmrSqYear(String strAppInsId ,String strAppTplId) throws Exception
	{
		Long appInsId = Long.parseLong(strAppInsId);
		
		String strBmrSqYear = "";
		
		strBmrSqYear = jdbcTemplate.queryForObject("SELECT year(TZ_RX_DT) TZ_RX_DT FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B "
				+ " WHERE A.TZ_APP_INS_ID = ? AND A.TZ_CLASS_ID = B.TZ_CLASS_ID LIMIT 0,1",new Object[]{appInsId},String.class);
		
		if(strBmrSqYear==null){
			strBmrSqYear = "";
		}
		
		return strBmrSqYear;	
	}
	
	/*获取报名人年龄*/
	public String getBmrAge(String strAppInsId ,String strAppTplId) throws Exception
	{
		Long appInsId = Long.parseLong(strAppInsId);
		
		String strBmrBirthdayBhxxx = "";
		
		String strBmrBirthday = "";
		
		Date dtBmrBirthday;
		
		strBmrBirthdayBhxxx = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 0,1",
						new Object[]{"TZ_BMR_BIRTHDAY_XXXBH"},String.class);
		String strBmrAge = "";
		
		strBmrBirthday = jdbcTemplate.queryForObject("SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T A WHERE TZ_APP_INS_ID= ? AND TZ_XXX_BH= ? LIMIT 0,1",
				new Object[]{appInsId,strBmrBirthdayBhxxx},String.class);
		
		
		String strBmrRxdt = "";
		Date dtBmrRxdt;
		
		
		strBmrRxdt = jdbcTemplate.queryForObject("SELECT TZ_RX_DT FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B "
				+ " WHERE A.TZ_APP_INS_ID = ? AND A.TZ_CLASS_ID = B.TZ_CLASS_ID LIMIT 0,1",new Object[]{appInsId},String.class);
		
		if(strBmrRxdt==null){
			strBmrAge = "";
		}else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			if (strBmrBirthday != null && !"".equals(strBmrBirthday)) {
				try {
					dtBmrBirthday = dateFormat.parse(strBmrBirthday);
					dtBmrRxdt = dateFormat.parse(strBmrRxdt);
					Calendar calBegin = Calendar.getInstance();   
				    Calendar calEnd = Calendar.getInstance();  
				    calBegin.setTime(dtBmrBirthday); 
				    calEnd.setTime(dtBmrRxdt);
				    
				    int calBeginYear = calBegin.get(Calendar.YEAR);
				    int calBeginMonth  = calBegin.get(Calendar.MONTH);
				    int calEndYear = calEnd.get(Calendar.YEAR);
				    int calEndMonth = calEnd.get(Calendar.MONTH);
				    
				    int bmrAge = 0;
				    if(calEndYear >= calBeginYear) {
				    	bmrAge = calEndYear - calBeginYear;
				    }
				    
				    if(calEndMonth > calBeginMonth) {
				    	bmrAge = bmrAge + 1;
				    }
				    
				    strBmrAge = String.valueOf(bmrAge);
				    
				} catch (Exception e) {
					strBmrAge = "";
				}
			}else{
				strBmrAge = "";
			}
		}
		
		return strBmrAge;	
	}
	
	/*申请记录 -申请年+报考方向+批次*/
	public String getBmrSqInfo(String strAppInsId ,String strAppTplId) throws Exception
	{
		Long appInsId = Long.parseLong(strAppInsId);
		
		Map<String, Object> getBmrSqInfoMap;
		
		String strBmrSqInfo = "";
		
		String strGetBatchName = "";
		String strBmrSqYear = "";
		String strGetClassName = "";
		
		getBmrSqInfoMap = jdbcTemplate.queryForMap("SELECT year(TZ_RX_DT) TZ_RX_DT,B.TZ_CLASS_NAME,C.TZ_BATCH_NAME FROM "
				+ " PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B,PS_TZ_CLS_BATCH_T C"
				+ " WHERE A.TZ_APP_INS_ID = ? AND A.TZ_CLASS_ID = B.TZ_CLASS_ID"
				+ " AND A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_BATCH_ID = C.TZ_BATCH_ID"
				+ " LIMIT 0,1",
				new Object[]{appInsId});
		if(getBmrSqInfoMap != null){
			strBmrSqYear = getBmrSqInfoMap.get("TZ_RX_DT") == null ? "":String.valueOf(getBmrSqInfoMap.get("TZ_RX_DT"));
			strGetClassName = getBmrSqInfoMap.get("TZ_CLASS_NAME") == null ? "":String.valueOf(getBmrSqInfoMap.get("TZ_CLASS_NAME"));
			strGetBatchName = getBmrSqInfoMap.get("TZ_BATCH_NAME") == null ? "":String.valueOf(getBmrSqInfoMap.get("TZ_BATCH_NAME"));
			
			if(!"".equals(strBmrSqYear)){
				strBmrSqYear = strBmrSqYear + "年";
			}
			strBmrSqInfo = strBmrSqYear + strGetBatchName + strGetClassName;
		}
		
		return strBmrSqInfo;	
	}
	
	/*获取报名人学校类型*/
	public String getBmrSchoolType(String strAppInsId ,String strAppTplId) throws Exception
	{
		Long appInsId = Long.parseLong(strAppInsId);
		
		String strBmrSchoolBhxxx = "";
		
		String strBmrSchool = "";
		
		String strBmrSchoolType = "";
		
		strBmrSchoolBhxxx = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 0,1",
						new Object[]{"TZ_BMR_SCHOOL_XXXBH"},String.class);
		
		strBmrSchool = jdbcTemplate.queryForObject("SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T A WHERE TZ_APP_INS_ID= ? AND TZ_XXX_BH= ? LIMIT 0,1",
				new Object[]{appInsId,strBmrSchoolBhxxx},String.class);
		
		
		if(strBmrSchool!=null || !"".equals(strBmrSchool)){
			strBmrSchoolType = jdbcTemplate.queryForObject("select TZ_SCHOOL_TYPENAME from PS_TZ_SCH_LIB_TBL A, PS_TZ_SCHOOL_TYPE_TBL B WHERE A.TZ_SCHOOL_TYPE = B.TZ_SCHOOL_TYPEID AND A.TZ_SCHOOL_NAME = ? LIMIT 0,1",
					new Object[]{strBmrSchool},String.class);
			if(strBmrSchool==null || "".equals(strBmrSchool)){
				strBmrSchoolType = "";
			}
		}
		
		return strBmrSchoolType;	
	}
	
	
}
