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
	public String getBmrClassName(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		String strGetClassName = "";
		if("".equals(strAppInsId)){
			strGetClassName = "";
		}else{
			Long appInsId = Long.parseLong(strAppInsId);
			
			strGetClassName = jdbcTemplate.queryForObject("SELECT B.TZ_CLASS_NAME FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B "
					+ "WHERE A.TZ_APP_INS_ID = ? AND A.TZ_CLASS_ID = B.TZ_CLASS_ID LIMIT 0,1",
					new Object[]{appInsId},String.class);
		}
		return strGetClassName;
	}
	
	/*获取志愿批次名称*/
	public String getBmrZyName(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		String strGetBatchName = "";
		if("".equals(strAppInsId)){
			strGetBatchName = "";
		}else{
			Long appInsId = Long.parseLong(strAppInsId);
			
			strGetBatchName = jdbcTemplate.queryForObject("SELECT B.TZ_BATCH_NAME FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLS_BATCH_T B "
					+ "WHERE A.TZ_APP_INS_ID = ? AND A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.TZ_BATCH_ID = B.TZ_BATCH_ID LIMIT 0,1",
					new Object[]{appInsId},String.class);
		}
		return strGetBatchName;
		
	}
	
	/*获取考生编号*/
	public String getBmrKsbh(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		String strBmrksbh = "";
		if("".equals(strAppInsId)){
			strBmrksbh = "";
		}else{
			
			Long appInsId = Long.parseLong(strAppInsId);
			
			strBmrksbh = jdbcTemplate.queryForObject("SELECT TZ_STU_NUM FROM TZ_IMP_LKBM_TBL WHERE TZ_APP_INS_ID = ? LIMIT 0,1",new Object[]{appInsId},String.class);
			
			if(strBmrksbh==null){
				strBmrksbh = "";
			}
		}
		return strBmrksbh;	
	}
	
	/*获取申请年份*/
	public String getBmrSqYear(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		String strBmrSqYear = "";
		if("".equals(strAppInsId)){
			strBmrSqYear = "";
		}else{
			Long appInsId = Long.parseLong(strAppInsId);
			
			strBmrSqYear = jdbcTemplate.queryForObject("SELECT year(TZ_RX_DT) TZ_RX_DT FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B "
					+ " WHERE A.TZ_APP_INS_ID = ? AND A.TZ_CLASS_ID = B.TZ_CLASS_ID LIMIT 0,1",new Object[]{appInsId},String.class);
			
			if(strBmrSqYear==null){
				strBmrSqYear = "";
			}
		}
		return strBmrSqYear;	
	}
	
	/*获取报名人姓名*/
	public String getBmrRealName(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		String strBmrRealName = "";
		if("".equals(strAppInsId)){
			if("".equals(strOprId)){
				strBmrRealName = "";
			}else{
				strBmrRealName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T B WHERE OPRID = ? LIMIT 0,1",new Object[]{strOprId},String.class);
				
				if(strBmrRealName==null){
					strBmrRealName = "";
				}
			}
		}else{
			Long appInsId = Long.parseLong(strAppInsId);
			
			strBmrRealName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T B "
					+ " WHERE A.TZ_APP_INS_ID = ? AND A.OPRID = B.OPRID LIMIT 0,1",new Object[]{appInsId},String.class);
			
			if(strBmrRealName==null){
				strBmrRealName = "";
			}
		}
		
		return strBmrRealName;	
	}
	
	/*获取报名人性别*/
	public String getBmrSex(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		String strBmrSex = "";
		if("".equals(strAppInsId)){
			if("".equals(strOprId)){
				strBmrSex = "";
			}else{
				strBmrSex = jdbcTemplate.queryForObject("SELECT CASE TZ_GENDER WHEN 'M' THEN '男' WHEN 'F' THEN '女' ELSE '' END AS TZ_GENDER FROM PS_TZ_REG_USER_T "
						+ " WHERE OPRID = ? LIMIT 0,1",new Object[]{strOprId},String.class);
				
				if(strBmrSex==null){
					strBmrSex = "";
				}
			}
		}else{
			Long appInsId = Long.parseLong(strAppInsId);
			
			strBmrSex = jdbcTemplate.queryForObject("SELECT CASE B.TZ_GENDER WHEN 'M' THEN '男' WHEN 'F' THEN '女' ELSE '' END AS TZ_GENDER FROM PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T B "
					+ " WHERE A.TZ_APP_INS_ID = ? AND A.OPRID = B.OPRID LIMIT 0,1",new Object[]{appInsId},String.class);
			
			if(strBmrSex==null){
				strBmrSex = "";
			}
		}
		return strBmrSex;	
	}
	
	/*获取报名人证件号码*/
	public String getBmrNationalId(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		String strBmrNationalId = "";
		if("".equals(strAppInsId)){
			if("".equals(strOprId)){
				strBmrNationalId = "";
			}else{
				strBmrNationalId = jdbcTemplate.queryForObject("SELECT NATIONAL_ID FROM PS_TZ_REG_USER_T "
						+ " WHERE OPRID = ? LIMIT 0,1",new Object[]{strOprId},String.class);
				
				if(strBmrNationalId==null){
					strBmrNationalId = "";
				}
			}
		}else{
			Long appInsId = Long.parseLong(strAppInsId);
			
			strBmrNationalId = jdbcTemplate.queryForObject("SELECT NATIONAL_ID FROM PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T B "
					+ " WHERE A.TZ_APP_INS_ID = ? AND A.OPRID = B.OPRID LIMIT 0,1",new Object[]{appInsId},String.class);
			
			if(strBmrNationalId==null){
				strBmrNationalId = "";
			}
		}
		
		return strBmrNationalId;	
	}
	
	/*获取报名人面试申请号*/
	public String getBmrMshId(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		String strBmrMshId = "";
		if("".equals(strAppInsId)){
			if("".equals(strOprId)){
				strBmrMshId = "";
			}else{
				strBmrMshId = jdbcTemplate.queryForObject("SELECT TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL "
						+ " WHERE OPRID = ? LIMIT 0,1",new Object[]{strOprId},String.class);
				
				if(strBmrMshId==null){
					strBmrMshId = "";
				}
			}
		}else{
			Long appInsId = Long.parseLong(strAppInsId);
			
			strBmrMshId = jdbcTemplate.queryForObject("SELECT TZ_MSH_ID FROM PS_TZ_FORM_WRK_T A,PS_TZ_AQ_YHXX_TBL B "
					+ " WHERE A.TZ_APP_INS_ID = ? AND A.OPRID = B.OPRID LIMIT 0,1",new Object[]{appInsId},String.class);
			
			if(strBmrMshId==null){
				strBmrMshId = "";
			}
		}
		return strBmrMshId;
	}
	
	/*获取报名人出生日期*/
	public String getBmrBirthday(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		String strBmrBirthday = "";
		if("".equals(strAppInsId)){
			if("".equals(strOprId)){
				strBmrBirthday = "";
			}else{
				strBmrBirthday = jdbcTemplate.queryForObject("SELECT BIRTHDATE FROM PS_TZ_REG_USER_T "
						+ " WHERE OPRID = ? LIMIT 0,1",new Object[]{strOprId},String.class);
				if(strBmrBirthday==null){
					strBmrBirthday = "";
				}
			}
		}else{
			Long appInsId = Long.parseLong(strAppInsId);
			
			strBmrBirthday = jdbcTemplate.queryForObject("SELECT BIRTHDATE FROM PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T B "
					+ " WHERE A.TZ_APP_INS_ID = ? AND A.OPRID = B.OPRID LIMIT 0,1",new Object[]{appInsId},String.class);
			
			if(strBmrBirthday==null){
				strBmrBirthday = "";
			}
		}
		return strBmrBirthday;	
	}
	
	/*获取报名人年龄*/
	public String getBmrAge(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		
		String strBmrBirthday = "";
		
		Long appInsId = Long.parseLong(strAppInsId);
		
		Date dtBmrBirthday;
		
		String strBmrAge = "";
		
		String strBmrRxdt = "";
		Date dtBmrRxdt;

		if("".equals(strAppInsId)){
			if("".equals(strOprId)){
				strBmrAge = "";
			}else{
				strBmrBirthday = jdbcTemplate.queryForObject("SELECT BIRTHDATE FROM PS_TZ_REG_USER_T "
						+ " WHERE OPRID = ? LIMIT 0,1",new Object[]{strOprId},String.class);
				strBmrRxdt=null;
			}
		}else{
			strBmrBirthday = jdbcTemplate.queryForObject("SELECT BIRTHDATE FROM PS_TZ_FORM_WRK_T A,PS_TZ_REG_USER_T B "
					+ " WHERE A.TZ_APP_INS_ID = ? AND A.OPRID = B.OPRID LIMIT 0,1",new Object[]{appInsId},String.class);
			
			strBmrRxdt = jdbcTemplate.queryForObject("SELECT TZ_RX_DT FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B "
					+ " WHERE A.TZ_APP_INS_ID = ? AND A.TZ_CLASS_ID = B.TZ_CLASS_ID LIMIT 0,1",new Object[]{appInsId},String.class);
			
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		if (strBmrBirthday != null && !"".equals(strBmrBirthday)) {
			try {
				dtBmrBirthday = dateFormat.parse(strBmrBirthday);
				if(strBmrRxdt==null){
					dtBmrRxdt = new Date();
				}else{
					dtBmrRxdt = dateFormat.parse(strBmrRxdt);
				}
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
	
		
		return strBmrAge;	
	}
	
	/*申请记录 -申请年+报考方向+批次*/
	public String getBmrSqInfo(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		
		String strBmrSqInfo = "";
		if("".equals(strAppInsId)){
			strBmrSqInfo = "";
		}else{
			Long appInsId = Long.parseLong(strAppInsId);
			
			Map<String, Object> getBmrSqInfoMap;

			String strGetBatchName = "";
			//String strBmrSqYear = "";
			String strGetClassName = "";
			
			getBmrSqInfoMap = jdbcTemplate.queryForMap("SELECT B.TZ_CLASS_NAME,C.TZ_BATCH_NAME FROM "
					+ " PS_TZ_FORM_WRK_T A JOIN PS_TZ_CLASS_INF_T B ON A.TZ_CLASS_ID = B.TZ_CLASS_ID"
					+ " LEFT JOIN PS_TZ_CLS_BATCH_T C"
					+ " ON A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_BATCH_ID = C.TZ_BATCH_ID"
					+ " WHERE A.TZ_APP_INS_ID = ? LIMIT 0,1",
					new Object[]{appInsId});
			if(getBmrSqInfoMap != null){
				//strBmrSqYear = getBmrSqInfoMap.get("TZ_RX_DT") == null ? "":String.valueOf(getBmrSqInfoMap.get("TZ_RX_DT"));
				strGetClassName = getBmrSqInfoMap.get("TZ_CLASS_NAME") == null ? "":String.valueOf(getBmrSqInfoMap.get("TZ_CLASS_NAME"));
				strGetBatchName = getBmrSqInfoMap.get("TZ_BATCH_NAME") == null ? "":String.valueOf(getBmrSqInfoMap.get("TZ_BATCH_NAME"));
				/*
				if(!"".equals(strBmrSqYear)){
					strBmrSqYear = strBmrSqYear + "年";
				}*/
				strBmrSqInfo = strGetClassName  + strGetBatchName;
			}
		}
		return strBmrSqInfo;	
	}
	
	/*获取报名人学校类型*/
	public String getBmrSchoolType(String strAppInsId ,String strOprId,String strAppTplId) throws Exception
	{
		String strBmrSchoolType = "";
		
		Long appInsId = Long.parseLong(strAppInsId);
		
		if("".equals(strAppInsId)){
			strBmrSchoolType = "";
		}else{
			
			String strBmrSchoolBhxxx = "";
			
			String strBmrSchool = "";
	
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
		}
		return strBmrSchoolType;	
	}
}
