package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.module;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.module.InterviewStatistics;

/**
 * 面试评审详情、考生列表以及汇总统计
 * 
 * @author Administrator
 * @since 2019/08/01
 */
@Service
public class InterviewDescription{
	
	@Autowired
	HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private InterviewStatistics interviewStatistics;
	
	/*
	 * 业务处理器
	 * 
	 * @param  strParams 	请求报文json字符串
	 * @param  errorMsg	   	错误信息
	 * 						errorMsg[0] String 错误编码，"0"表示正常，"1"表示错误
	 * 						errorMsg[1] String 错误消息
	 * @return String    	务必返回json格式的字符串
	 * @description			通过解析strParams中的如type参数
	 * 						或者通过request中的type参数判断用户的具体操作执行不同的方法
	 *
	 * */
	public String processor(String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String mapRet = "";
		try{
			String CLASSID = jacksonUtil.getString("CLASSID");
			String BATCHID = jacksonUtil.getString("BATCHID");
			
			String OPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			if(OPRID==null||"".equals(OPRID)){
				return "";
			}
			Map<String, Object> flag = getFlag(OPRID, CLASSID, BATCHID);
			if("0".equals(flag.get("flag").toString())){
				return jacksonUtil.Map2json(flag);
			}
			//面试页面页签
			String pageType = jacksonUtil.getString("PAGETYPE");
			
			switch (pageType) {
			case "Instructions":
				mapRet = this.getInstruction(strParams);
				break;
			case "StudentList":
				mapRet = this.getStudentList(strParams);
				break;
			case "Collect":
				mapRet = interviewStatistics.getContent(strParams);
				break;
			default:
				break;
			}
			
		}catch (Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "数据异常，请重试！";
		}
		
		return mapRet;
	}
	
	private String getInstruction(String strParams) throws Exception{
		JacksonUtil params = new JacksonUtil();
		params.json2Map(strParams);
		String CLASSID = params.getString("CLASSID");
		String BATCHID = params.getString("BATCHID");
		//面试说明
		String sql = "SELECT TZ_MSPS_SM "
				+ "FROM PS_TZ_MSPS_GZ_TBL "
				+ "WHERE tz_class_id = ? AND tz_apply_pc_id = ?";
		String queryData = sqlQuery.queryForObject(sql, new Object[]{CLASSID,BATCHID}, "String");
		HashMap<String, Object> hashMap = new HashMap<String,Object>();
		hashMap.put("MSSM", queryData==null?"":queryData);
		return JSONObject.fromObject(hashMap).toString();
	}
	
	@SuppressWarnings("unchecked")
	private String getStudentList(String strParams) throws Exception{
		JacksonUtil params = new JacksonUtil();
		params.json2Map(strParams);
		HashMap<String, Object> hashMap = new HashMap<String,Object>();
		
		String CLASSID = params.getString("CLASSID");
		String BATCHID = params.getString("BATCHID");
		//面试组号
		String MSZID = params.getString("MSZID");
		String OPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		System.out.println(OPRID);
		//String sql = tzGDObject.getSQLText("SQL.TZEvaluationSystemBundle.TzInterviewStudentList");
		//sql参数
		
		StringBuilder students = new StringBuilder("SELECT "
				+ "c.TZ_CLASS_ID AS classid,"
				+ "c.TZ_APPLY_PC_ID AS pcid,"
				+ "c.TZ_APP_INS_ID AS insid,"
				+ "e.TZ_REALNAME AS name,"
				+ "(b.TZ_ATT_A_URL + b.TZ_ATTACHSYSFILENA) AS photo,"
				+ "f.TZ_COMPANY_NAME AS COMPANY,"
				+ "f.TZ_COMMENT1 AS postion,"
				+ "d.OPRID AS oprid,"
				+ "c.TZ_SCORE_INS_ID AS scoreid "
				+ "FROM PS_TZ_MP_PW_KS_TBL c "
				+ "LEFT JOIN PS_TZ_FORM_WRK_T d ON (c.TZ_APP_INS_ID = d.TZ_APP_INS_ID) "
				+ "LEFT JOIN PS_TZ_OPR_PHT_GL_T a ON d.OPRID = a.OPRID "
				+ "LEFT JOIN PS_TZ_OPR_PHOTO_T b ON a.TZ_ATTACHSYSFILENA = b.TZ_ATTACHSYSFILENA "
				+ "LEFT JOIN PS_TZ_AQ_YHXX_TBL e ON e.OPRID = d.OPRID LEFT JOIN PS_TZ_REG_USER_T f ON f.OPRID = d.OPRID "
				+ "INNER JOIN PS_TZ_MSPS_KSH_TBL G ON (c.TZ_CLASS_ID = g.TZ_CLASS_ID AND G.TZ_APPLY_PC_ID = c.TZ_APPLY_PC_ID AND c.TZ_APP_INS_ID = G.TZ_APP_INS_ID) "
				+ "WHERE c.TZ_DELETE_ZT = 'N' AND g.TZ_GROUP_ID = ? ");
		
		

		String queryForObject = sqlQuery.queryForObject("SELECT TOP 1 c.TZ_GROUP_ID FROM PS_TZ_MP_PW_KS_TBL a INNER JOIN PS_TZ_MSPS_KSH_TBL b ON (A.TZ_CLASS_ID = b.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = b.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID = b.TZ_APP_INS_ID) INNER JOIN TZ_INTERVIEW_GROUP c ON (c.TZ_CLASS_ID = b.TZ_CLASS_ID AND c.TZ_APPLY_PC_ID = b.TZ_APPLY_PC_ID AND c.TZ_GROUP_ID = b.TZ_GROUP_ID) WHERE A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID = ? AND A.TZ_DELETE_ZT = 'N' AND TZ_GROUP_SPACE = (CASE WHEN DATEDIFF(HOUR,CONVERT(VARCHAR(100),GETDATE(), 8),'12:00:00')>0 THEN 'A' ELSE 'B' END)", new Object[]{CLASSID,BATCHID,OPRID}, "String");
		MSZID = (null==MSZID||"".equals(MSZID.trim()))?queryForObject==null?"":queryForObject:MSZID;
		hashMap.put("MSZID", MSZID);
		
		
		/*
		if(null==MSZID||"".equals(MSZID.trim())){
			//如果没有选面试组号
			students.append("(SELECT TOP 1 TZ_GROUP_ID FROM TZ_INTERVIEW_GROUP WHERE TZ_CLASS_ID = c.TZ_CLASS_ID AND TZ_APPLY_PC_ID = c.TZ_APPLY_PC_ID AND TZ_GROUP_SPACE = (CASE WHEN DATEDIFF(HOUR,CONVERT(VARCHAR(100),GETDATE(), 8),'12:00:00')>0 THEN 'A' ELSE 'B' END)");
			sqlParam = new Object[]{OPRID,CLASSID,BATCHID};
		}else{
			//选择面试组号
			students.append("? ");
			sqlParam = new Object[]{MSZID,OPRID,CLASSID,BATCHID};
		}*/
		students.append("AND c.TZ_PWEI_OPRID = ? AND c.TZ_CLASS_ID = ? AND c.TZ_APPLY_PC_ID = ? ORDER BY G.TZ_ORDER ASC");
		
		System.out.println(students.toString());
		
		
		List<Map<String,Object>> STUDENTS = sqlQuery.queryForList(students.toString(),new Object[]{MSZID,OPRID,CLASSID,BATCHID});
		hashMap.put("STUDENTS", STUDENTS);
		
		List<Map<String,Object>> MSZS = sqlQuery.queryForList("SELECT DISTINCT c.TZ_GROUP_ID as ID,c.TZ_GROUP_DESC AS DESCR FROM PS_TZ_MP_PW_KS_TBL a INNER JOIN PS_TZ_MSPS_KSH_TBL b ON (A.TZ_CLASS_ID = b.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = b.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID = b.TZ_APP_INS_ID) INNER JOIN TZ_INTERVIEW_GROUP c ON (c.TZ_CLASS_ID = b.TZ_CLASS_ID AND c.TZ_APPLY_PC_ID = b.TZ_APPLY_PC_ID AND c.TZ_GROUP_ID = b.TZ_GROUP_ID) WHERE A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID = ? AND A.TZ_DELETE_ZT = 'N'", new Object[]{CLASSID,BATCHID,OPRID});
		hashMap.put("MSZS", MSZS);
		
		return JSONObject.fromObject(hashMap).toString();
	}
	
	/**
	 * 验证项目开通状态、批次开通状态、面试评委状态
	 * @param OPRID 人员ID
	 * @param CLASSID 班级ID
	 * @param BATCHID 批次ID
	 * @return Map.flag = 1 验证通过，Map.msg = 错误信息
	 */
	public Map<String, Object> getFlag(String OPRID,String CLASSID,String BATCHID){
		Map<String, Object> flag = new HashMap<String, Object>();
		flag.put("flag", "1");
		flag.put("msg", "");
		String queryForObject0 = "";
		String queryForObject1 = "";
		String queryForObject2 = "";
		try {
			queryForObject0 = sqlQuery.queryForObject("select 'Y' from PS_TZ_PRJ_INF_T c INNER JOIN PS_TZ_CLASS_INF_T d ON (c.tz_prj_id = d.tz_prj_id) WHERE c.TZ_IS_OPEN <> 'Y' and d.TZ_CLASS_ID = ?", new Object[]{CLASSID}, "String");
			queryForObject1 = sqlQuery.queryForObject("select 'Y' from PS_TZ_MSPS_GZ_TBL WHERE TZ_DQPY_ZT <> 'A' and TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?", new Object[]{CLASSID,BATCHID}, "String");
			queryForObject2 = sqlQuery.queryForObject("select 'Y' from ps_TZ_MSPS_PW_TBL WHERE tz_pwei_zhzt NOT IN ('A', 'N') and TZ_pwei_oprid = ? AND TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?", new Object[]{OPRID,CLASSID,BATCHID}, "String");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if("Y".equals(queryForObject0)){
			flag.replace("flag", "0");
			flag.replace("msg", "项目已关闭");
			return flag;
		}
		if("Y".equals(queryForObject1)){
			flag.replace("flag", "0");
			flag.replace("msg", "面试批次已关闭");
			return flag;
		}
		if("Y".equals(queryForObject2)){
			flag.replace("flag", "0");
			flag.replace("msg", "当前账号已停用");
			return flag;
		}
		return flag;
	}

}