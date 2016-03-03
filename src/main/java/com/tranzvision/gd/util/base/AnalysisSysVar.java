package com.tranzvision.gd.util.base;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 
 * @author tang 20151201 解析系统变量: 原：TZ_SYSVAR:AnalysisSysVar
 */

public class AnalysisSysVar {

	private JdbcTemplate jdbcTemplate;
	// 系统变量编号
	private String m_SysVarID;
	// 系统变量参数列表
	private String[] m_SysVarParam;

	public String getM_SysVarID() {
		return m_SysVarID;
	}

	public void setM_SysVarID(String m_SysVarID) {
		this.m_SysVarID = m_SysVarID;
	}

	public String[] getM_SysVarParam() {
		return m_SysVarParam;
	}

	public void setM_SysVarParam(String[] m_SysVarParam) {
		this.m_SysVarParam = m_SysVarParam;
	}

	public AnalysisSysVar(){
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
	}
	// 返回当前指定系统变量的值;
	public Object GetVarValue() {
		// 系统变量的计算结果
		Object l_RetValue = null;
		try {
			String sysVarSQL = "select TZ_EFFFLG,TZ_SYSVARTYPE,TZ_VALMETHOD from PS_TZ_SYSVAR_T where TZ_SYSVARID=?";
			Map<String, Object> sysvarMap = jdbcTemplate.queryForMap(sysVarSQL, new Object[] { m_SysVarID });
			if (sysvarMap == null) {
				return "";
			}
			String effFlg = (String) sysvarMap.get("TZ_EFFFLG");
			String sysvarType = (String) sysvarMap.get("TZ_SYSVARTYPE");
			String valMethod = (String) sysvarMap.get("TZ_VALMETHOD");
			// 系统变量是否无效
			if (!"Y".equals(effFlg)) {
				return "";
			}
			// 取值和系统变量数据类型是否为空
			if (sysvarType == null || "".equals(sysvarType) || valMethod == null || "".equals(valMethod)) {
				return "";
			}
			// 取值是否正确
			if (!"SQL".equals(valMethod) && !"APP".equals(valMethod) && !"CON".equals(valMethod)) {
				return "";
			}

			// 分支1：SQL取值;
			if ("SQL".equals(valMethod)) {
				l_RetValue = this.parseSysVarSql(sysvarType);
				if(l_RetValue==null){
					return "";
				}
				return l_RetValue;
			}

			// 分支2：应用程序类获取
			if ("APP".equals(valMethod)) {
				l_RetValue = this.parseSysVarApp(sysvarType);
				if(l_RetValue==null){
					return "";
				}
				return l_RetValue;
			}

			// 分支3：常量
			if ("CON".equals(valMethod)) {
				l_RetValue = this.parseSysVarCon(sysvarType);
				if(l_RetValue==null){
					return "";
				}
				return l_RetValue;
			}
			return "";

		} catch (Exception e) {
			l_RetValue = "";
		}

		return l_RetValue;
	}

	// sql
	public Object parseSysVarSql(String sysvarType) {
		/* STEP: 取出SQL String */
		String sqlString;
		String sqlStringSQL = "SELECT TZ_LNGSTRCONT FROM PS_TZ_SYSVAR_T WHERE TZ_SYSVARID=?";
		sqlString = jdbcTemplate.queryForObject(sqlStringSQL, new Object[] { m_SysVarID }, String.class);
		if (sqlString == null || "".equals(sqlStringSQL)) {
			return null;
		}
		try {
			// STEP: 看该系统变量有无对其他系统变量的引用，若有，则先求值
			int hitCount;
			String hitCountSQL = "SELECT COUNT(1) FROM PS_TZ_SV_CHAIN CHN WHERE CHN.TZ_SYSVARID = ?";
			hitCount = jdbcTemplate.queryForObject(hitCountSQL, new Object[] { m_SysVarID }, Integer.class);

			if (hitCount > 0) {
				// STEP: 求出被引用系统变量的值;
				String sysVarRefSQL = "SELECT CHN.TZ_C_SYSVARID FROM PS_TZ_SV_CHAIN CHN WHERE CHN.TZ_SYSVARID = ?";
				List<Map<String, Object>> sysVarRefList = jdbcTemplate.queryForList(sysVarRefSQL,
						new Object[] { m_SysVarID });
				if (sysVarRefList != null && sysVarRefList.size() > 0) {
					for (int i = 0; i < sysVarRefList.size(); i++) {

						String tmp_SysVarID = this.m_SysVarID;
						// 被引用系统变量
						String cSysvarid = (String) sysVarRefList.get(i).get("TZ_C_SYSVARID");
						this.m_SysVarID = cSysvarid;
						String str = (String) this.GetVarValue();
						sqlString = sqlString.replaceAll("\\$" + cSysvarid + "\\$", str);
						this.m_SysVarID = tmp_SysVarID;
					}
				}
			}
			// 字符串
			if ("CHR".equals(sysvarType)) {
				try {
					String returnString = jdbcTemplate.queryForObject(sqlString, String.class);
					return returnString;
				} catch (Exception e) {
					return null;
				}
			}

			// 数字
			if ("NUM".equals(sysvarType)) {
				try {
					int returnInt = jdbcTemplate.queryForObject(sqlString, Integer.class);
					return returnInt;
				} catch (Exception e) {
					return null;
				}
			}

			// 日期
			if ("DAT".equals(sysvarType) || "DTM".equals(sysvarType)) {
				try {
					Date returnDate = jdbcTemplate.queryForObject(sqlString, Date.class);
					return returnDate;
				} catch (Exception e) {
					return null;
				}
			}

			// 时间
			if ("TIM".equals(sysvarType)) {
				try {
					Time returnTime = jdbcTemplate.queryForObject(sqlString, Time.class);
					return returnTime;
				} catch (Exception e) {
					return null;
				}
			}

			// 布尔
			if ("BUL".equals(sysvarType)) {
				try {
					List<Map<String, Object>> returnList = jdbcTemplate.queryForList(sqlString);
					if (returnList != null && returnList.size() > 0) {
						return true;
					} else {
						return false;
					}
				} catch (Exception e) {
					return null;
				}
			}

			// 字符串数组;数字数组;日期数组;时间数组;日期时间数组;
			if ("ACH".equals(sysvarType) || "ANU".equals(sysvarType) || "ADT".equals(sysvarType)
					|| "ATM".equals(sysvarType) || "ADM".equals(sysvarType)) {
				try {
					List<Map<String, Object>> returnList = jdbcTemplate.queryForList(sqlString);
					return returnList;
				} catch (Exception e) {
					return null;
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 常量
	public Object parseSysVarCon(String sysvarType) {
		/* STEP: 获取值 */
		System.out.println("==========1=================>"+m_SysVarID+"=======>"+sysvarType);
		String conSQL = "SELECT VAR.TZ_CONSTANT FROM PS_TZ_SYSVAR_T VAR WHERE VAR.TZ_SYSVARID = ?";
		String constValue = jdbcTemplate.queryForObject(conSQL, new Object[] { m_SysVarID }, String.class);
		System.out.println("==========2=================>"+m_SysVarID+"=======>"+sysvarType);
		if (constValue == null) {
			return null;
		}
		System.out.println("==========3=================>"+m_SysVarID+"=======>"+sysvarType);
		try {
			// 字符串
			if ("CHR".equals(sysvarType)) {
				return constValue;
			}

			// 数字
			if ("NUM".equals(sysvarType)) {
				return Integer.parseInt(constValue);
			}

			// 日期
			if ("DAT".equals(sysvarType)) {
				SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
				return dateFormate.parse(constValue);
			}

			// 日期时间
			if ("DTM".equals(sysvarType)) {
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return datetimeFormate.parse(constValue);
			}

			// 时间
			if ("TIM".equals(sysvarType)) {
				SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm:ss");
				return timeFormate.parse(constValue);
			}

			// 布尔
			if ("BUL".equals(sysvarType)) {
				if ("TRUE".equals(constValue.toUpperCase())) {
					return true;
				} else {
					if ("FALSE".equals(constValue.toUpperCase())) {
						return false;
					} else {
						return null;
					}
				}
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("==========4=================>"+m_SysVarID+"=======>"+sysvarType);
			return null;
		}
	}

	// class
	public Object parseSysVarApp(String sysvarType) {
		String packageClassSQL = "select TZ_APPCLS_PATH ,TZ_APPCLS_NAME,TZ_APPCLS_METHOD from PS_TZ_APPCLS_TBL where TZ_APPCLS_ID in (select TZ_APPCLS_REG from PS_TZ_SYSVAR_T where TZ_SYSVARID=?)";
		Map<String, Object> packageClassMap = jdbcTemplate.queryForMap(packageClassSQL, new Object[]{m_SysVarID});
		if(packageClassMap == null){
			return null;
		}
		String clsPath = (String) packageClassMap.get("TZ_APPCLS_PATH");
		String clsName = (String) packageClassMap.get("TZ_APPCLS_NAME");
		String clsMethod = (String) packageClassMap.get("TZ_APPCLS_METHOD");
		   
		String[] parameterTypes = new String[] {"String[]" };
		Object[] arglist = new Object[] { m_SysVarParam};

		Object objs = ObjectDoMethod.Load(clsPath + "." + clsName, clsMethod,
				parameterTypes, arglist);
		return objs;
	}

}
