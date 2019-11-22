package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.module;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 面试评审申请人信息
 * 
 * @author Administrator
 * @since 2019/08/01
 */
@Service
public class InterviewApplicant{
	
	@Autowired
	HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	
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
		
		String classId = jacksonUtil.getString("classId");
		
		String appTplId = sqlQuery.queryForObject("SELECT TZ_PS_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?", new Object[]{classId}, "String");
		
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("appTplId", appTplId);
		
		return jacksonUtil.Map2json(rtn);
	}
	
}