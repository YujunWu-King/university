package com.tranzvision.gd.util.dynamicsBase;

import com.tranzvision.gd.util.base.AnalysisClsMethod;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 
 * ClassName: AnalysisDynaSysVar
 * @author zhongcg 
 * @version 1.0 
 * Create Time: 2019年1月14日 上午11:21:06  
 * Description:	解析dynamics系统变量: 原：TZ_SYSVAR:AnalysisSysVar
 */
public class AnalysisDynaSysVar {

	private JdbcTemplate jdbcTemplate;

	private SqlQuery sqlQuery;
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

	public AnalysisDynaSysVar() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
	}

	// 返回当前指定系统变量的值;
	public Object GetVarValue() {
		// 系统变量的计算结果
		Object l_RetValue = null;
		try {
			String sysVarSQL = "select tzms_wf_sysvartype,tzms_valmethod from tzms_sysvar_tBase where tzms_sysvar_tId=?";
			Map<String, Object> sysvarMap = jdbcTemplate.queryForMap(sysVarSQL, new Object[] { m_SysVarID });
			if (sysvarMap == null) {
				return "";
			}
			String sysvarType =  sysvarMap.get("tzms_wf_sysvartype") == null ? "" : String.valueOf(sysvarMap.get("tzms_wf_sysvartype"));
			String valMethod =  sysvarMap.get("tzms_valmethod")== null ? "" : String.valueOf(sysvarMap.get("tzms_valmethod"));
			// 取值和系统变量数据类型是否为空
			if (sysvarType == null || "".equals(sysvarType) || valMethod == null || "".equals(valMethod)) {
				return "";
			}
			// 取值是否正确
			if (!"1".equals(valMethod) && !"2".equals(valMethod) && !"3".equals(valMethod)) {
				return "";
			}

			// 分支1：SQL取值;
			if ("1".equals(valMethod)) {
				l_RetValue = this.parseSysVarSql(sysvarType);
				if (l_RetValue == null) {
					return "";
				}
				return l_RetValue;
			}

			// 分支2：应用程序类获取
			if ("2".equals(valMethod)) {
				l_RetValue = this.parseSysVarApp(sysvarType);
				if (l_RetValue == null) {
					return "";
				}
				return l_RetValue;
			}

			// 分支3：常量
			if ("3".equals(valMethod)) {
				l_RetValue = this.parseSysVarCon(sysvarType);
				if (l_RetValue == null) {
					return "";
				}
				return l_RetValue;
			}
			return "";

		} catch (Exception e) {
			e.printStackTrace();
			l_RetValue = "";
		}

		return l_RetValue;
	}

	// sql
	public Object parseSysVarSql(String sysvarType) {
		/* STEP: 取出SQL String */
		String sqlString;
		String sqlStringSQL = "SELECT tzms_get_sql FROM tzms_sysvar_tBase WHERE tzms_sysvar_tId=?";
		sqlString = jdbcTemplate.queryForObject(sqlStringSQL, new Object[] { m_SysVarID }, String.class);
		if (sqlString == null || "".equals(sqlStringSQL)) {
			return null;
		}
		try {
			// STEP: 看该系统变量有无对其他系统变量的引用，若有，则先求值
			int hitCount;
			String hitCountSQL = "SELECT COUNT(1) FROM tzms_sysvar_t_tzms_sysvar_t WHERE tzms_sysvar_tidone = ?";
			hitCount = jdbcTemplate.queryForObject(hitCountSQL, new Object[] { m_SysVarID }, Integer.class);

			if (hitCount > 0) {
				// STEP: 求出被引用系统变量的值;
				String sysVarRefSQL = "SELECT tzms_sysvar_tidtwo FROM tzms_sysvar_t_tzms_sysvar_t WHERE tzms_sysvar_tidone = ?";
				List<Map<String, Object>> sysVarRefList = jdbcTemplate.queryForList(sysVarRefSQL,
						new Object[] { m_SysVarID });
				if (sysVarRefList != null && sysVarRefList.size() > 0) {
					for (int i = 0; i < sysVarRefList.size(); i++) {

						String tmp_SysVarID = this.m_SysVarID;
						// 被引用系统变量
						String cSysvarid = (String) sysVarRefList.get(i).get("tzms_sysvar_tidtwo");
						
						//引用系统变量ID
						String tzms_sysvar_id = jdbcTemplate.queryForObject("select tzms_sysvar_id from tzms_sysvar_t where tzms_sysvar_tId=?",
								new Object[] { cSysvarid }, String.class);
						
						this.m_SysVarID = cSysvarid;
						String str = (String) this.GetVarValue();
						sqlString = sqlString.replaceAll("\\$" + tzms_sysvar_id + "\\$", str);
						this.m_SysVarID = tmp_SysVarID;
					}
				}
			}
			// 字符串
			if ("11".equals(sysvarType)) {
				try {
					String returnString = jdbcTemplate.queryForObject(sqlString, String.class);
					return returnString;
				} catch (Exception e) {
					return null;
				}
			}

			// 数字
			if ("21".equals(sysvarType)) {
				try {
					int returnInt = jdbcTemplate.queryForObject(sqlString, Integer.class);
					return returnInt;
				} catch (Exception e) {
					return null;
				}
			}

			// 日期
			if ("31".equals(sysvarType) || "51".equals(sysvarType)) {
				try {
					Date returnDate = jdbcTemplate.queryForObject(sqlString, Date.class);
					return returnDate;
				} catch (Exception e) {
					return null;
				}
			}

			// 时间
			if ("41".equals(sysvarType)) {
				try {
					Time returnTime = jdbcTemplate.queryForObject(sqlString, Time.class);
					return returnTime;
				} catch (Exception e) {
					return null;
				}
			}

			// 布尔
			if ("61".equals(sysvarType)) {
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
			if ("12".equals(sysvarType) || "22".equals(sysvarType) || "32".equals(sysvarType)
					|| "42".equals(sysvarType) || "52".equals(sysvarType)) {
				try {
					//List<Map<String, Object>> returnList = jdbcTemplate.queryForList(sqlString);
					List<String> returnList = jdbcTemplate.queryForList(sqlString, String.class);
					return returnList;
				} catch (Exception e) {
					e.printStackTrace();
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
		System.out.println("==========1=================>" + m_SysVarID + "=======>" + sysvarType);
		String conSQL = "SELECT VAR.tzms_constant FROM tzms_sysvar_tBase VAR WHERE VAR.tzms_sysvar_tid = ?";
		String constValue = jdbcTemplate.queryForObject(conSQL, new Object[] { m_SysVarID }, String.class);
		System.out.println("==========2=================>" + m_SysVarID + "=======>" + sysvarType);
		if (constValue == null) {
			return null;
		}
		System.out.println("==========3=================>" + m_SysVarID + "=======>" + sysvarType);
		try {
			// 字符串
			if ("11".equals(sysvarType)) {
				return constValue;
			}

			// 数字
			if ("21".equals(sysvarType)) {
				return Integer.parseInt(constValue);
			}

			// 日期
			if ("31".equals(sysvarType)) {
				SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
				return dateFormate.parse(constValue);
			}

			// 日期时间
			if ("51".equals(sysvarType)) {
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return datetimeFormate.parse(constValue);
			}

			// 时间
			if ("41".equals(sysvarType)) {
				SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm:ss");
				return timeFormate.parse(constValue);
			}

			// 布尔
			if ("61".equals(sysvarType)) {
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
			System.out.println("==========4=================>" + m_SysVarID + "=======>" + sysvarType);
			return null;
		}
	}

	// class
	public Object parseSysVarApp(String sysvarType) {
		try {
			//String packageClassSQL = "select tzms_appcls_path,tzms_appcls_name,tzms_appcls_method,tzms_appcls_type from tzms_appcls_tBase where tzms_appcls_tid in (select tzms_app_class_id from tzms_sysvar_tBase where tzms_sysvar_tid=?)";
			String packageClassSQL = "select tzms_app_class_id from tzms_sysvar_tBase where tzms_sysvar_tid=?";
			Map<String, Object> packageClassMap = jdbcTemplate.queryForMap(packageClassSQL, new Object[] { m_SysVarID });
			if (packageClassMap == null) {
				return null;
			}
			String tzms_app_class_id = String.valueOf(packageClassMap.get("tzms_app_class_id"));
			if(tzms_app_class_id == null || "null".equals(tzms_app_class_id)) {
				return null;
			}
			AnalysisClsMethod analysisCls = new AnalysisClsMethod(tzms_app_class_id);
			
			//设置参数类型，如果是DLL类只能传入基本类型，参数类型需要与参数对应
			String[] parameterTypes = new String[m_SysVarParam.length];
			for(int i = 0; i < m_SysVarParam.length;i++) {
				parameterTypes[i] = "String";
			}
			
			//设置类方法参数
			analysisCls.setJavaClsParameter(parameterTypes, m_SysVarParam);
			
			return analysisCls.execute();
		} catch (TzException e) {
			e.printStackTrace();
			return null;
		}
		/*String clsPath = (String) packageClassMap.get("tzms_appcls_path");
		String clsName = (String) packageClassMap.get("tzms_appcls_name");
		String clsMethod = (String) packageClassMap.get("tzms_appcls_method");
		//取值类型
		String clsType = (String) packageClassMap.get("tzms_appcls_type");

		//Java取值
		if("1".equals(clsType)) {
			
			String[] parameterTypes = new String[] { "String[]" };
			Object[] arglist = new Object[] { m_SysVarParam };
			
			Object objs = ObjectDoMethod.Load(clsPath + "." + clsName, clsMethod, parameterTypes, arglist);
			return objs;
		}
		
		//Dll取值
		if("2".equals(clsType)) {
			//TODO
		}*/
		
		//return "";
	}

}
