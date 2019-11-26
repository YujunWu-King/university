package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.module;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;

/**
 * 面试评审批次列表
 * 
 * @author Administrator
 * @since 2019/08/01
 */
@Service
public class InterviewBatch{
	
	@Autowired
	HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
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
		return this.getBatchList(strParams, errorMsg);
	}
	
	private String getBatchList(String strParams, String[] errorMsg){
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("judgeType", "0");
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			System.out.println(oprid);
			if(oprid==null||"".equals(oprid)){
				jacksonUtil.Map2json(mapRet);
			}
			//批次列表
			String sql = "SELECT a.TZ_CLASS_ID,a.TZ_APPLY_PC_ID,d.TZ_CLASS_NAME,"+
					" (SELECT TZ_BATCH_NAME FROM PS_TZ_CLS_BATCH_T "+
					" WHERE TZ_CLASS_ID = a.TZ_CLASS_ID AND TZ_BATCH_ID =a.TZ_APPLY_PC_ID) AS TZ_BATCH_NAME "+
					" FROM PS_TZ_PRJ_INF_T c INNER JOIN PS_TZ_CLASS_INF_T d ON (c.tz_prj_id = d.tz_prj_id) "+
					" INNER JOIN PS_TZ_MSPS_GZ_TBL a ON d.TZ_CLASS_ID = a.TZ_CLASS_ID "+
					" WHERE a.TZ_DQPY_ZT = 'A' AND c.TZ_IS_OPEN = 'Y' AND c.tz_jg_id = ?"+
					" AND (exists(select 1 from PS_TZ_MSPS_PW_TBL "+
					" where TZ_CLASS_ID = a.TZ_CLASS_ID AND TZ_APPLY_PC_ID = a.TZ_APPLY_PC_ID and tz_pwei_zhzt='A' and TZ_pwei_oprid = ?)"+
					" or exists(select 1 from tz_interview_admin_t "+
					" where TZ_CLASS_ID = a.TZ_CLASS_ID AND TZ_APPLY_PC_ID = a.TZ_APPLY_PC_ID and oprid=? and type='QD' and status='A'))";
					
					List<?> resultlist = sqlQuery.queryForList(sql, new Object[] {orgid,oprid,oprid});
			
			//评委类型
			String judgeType = sqlQuery.queryForObject("(SELECT b.tz_jugtyp_id AS type FROM PS_TZ_JUSR_REL_TBL a INNER JOIN PS_TZ_JUGTYP_TBL b ON (a.TZ_jg_id = b.TZ_jg_id AND a.TZ_JUGTYP_ID = b.TZ_JUGTYP_ID) WHERE b.tz_jugtyp_stat = 0 AND a.OPRID = ? AND a.TZ_jg_id = ?) UNION ALL (SELECT a.type as type FROM tz_interview_admin_t a INNER JOIN PS_TZ_AQ_YHXX_TBL b ON a.oprid = b.oprid WHERE a.status = 'A' AND a.OPRID = ? AND b.TZ_jg_id = ?)",new Object[]{oprid,orgid,oprid,orgid}, "String");
			
			for (Object obj : resultlist) {
				@SuppressWarnings("unchecked")
				Map<String, Object> result = (Map<String, Object>) obj;

				String TZ_CLASS_ID = result.get("TZ_CLASS_ID") == null ? "" : String.valueOf(result.get("TZ_CLASS_ID"));
				String TZ_APPLY_PC_ID = result.get("TZ_APPLY_PC_ID") == null ? "" : String.valueOf(result.get("TZ_APPLY_PC_ID"));
				String TZ_CLASS_NAME = result.get("TZ_CLASS_NAME") == null ? "" : String.valueOf(result.get("TZ_CLASS_NAME"));
				String TZ_BATCH_NAME = result.get("TZ_BATCH_NAME") == null ? "" : String.valueOf(result.get("TZ_BATCH_NAME"));

				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("classId",TZ_CLASS_ID);
				mapRetJson.put("pcId",TZ_APPLY_PC_ID);
				mapRetJson.put("className",TZ_CLASS_NAME);
				mapRetJson.put("pcName",TZ_BATCH_NAME);

				listData.add(mapRetJson);
			}

			mapRet.replace("judgeType", judgeType!=null?judgeType:"QD");
			mapRet.replace("root", listData);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "数据异常，请重试！";
		}

		return jacksonUtil.Map2json(mapRet);
	}
}