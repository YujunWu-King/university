package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 原PS: TZ_GD_MAILHIS_PKG:searchMailHistory
 * @author tang
 * 2015-12-02
 * 查看给相关人员发送的历史记录
 */
@Service("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.searchMailHistoryServiceImpl")
public class searchMailHistoryServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	/* 查询列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			if(jacksonUtil.containsKey("emailAddress")){
				String emailAddress = jacksonUtil.getString("emailAddress");
				//当前机构;
				String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				int count = 0;
				String mailHistorySql = "SELECT A.TZ_RWSL_ID,A.TZ_FS_EMAIL,A.TZ_EM_ZHUTI, date_format(A.TZ_FS_DT,'%Y-%m-%d %H:%i:%s') TZ_FS_DT ,B.ROW_ADDED_OPRID FROM PS_TZ_YJFSLSHI_TBL A,PS_TZ_DXYJFSRW_TBL B WHERE A.TZ_EML_SMS_TASK_ID =B.TZ_EML_SMS_TASK_ID AND A.TZ_JS_EMAIL=? AND B.TZ_JG_ID=? ORDER BY TZ_FS_DT DESC";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(mailHistorySql,new Object[]{emailAddress,strOrgId});
				if(list != null && list.size()>0){
					for(int i=0; i<list.size(); i++){
						count++;
						String rwInsId = (String) list.get(i).get("TZ_RWSL_ID");
						String sendEmail = (String) list.get(i).get("TZ_FS_EMAIL");
						String subject = (String) list.get(i).get("TZ_EM_ZHUTI");
						String sendTime = (String) list.get(i).get("TZ_FS_DT");
						String operator = (String) list.get(i).get("ROW_ADDED_OPRID");
						
						if(operator != null && !"".equals(operator) && !"TZ_GUEST".equals(operator)){
							String relNameSQL = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
							operator = jdbcTemplate.queryForObject(relNameSQL,new Object[]{operator},"String");
						}
						
						Map<String, Object> jsonMap = new HashMap<String, Object>();
						jsonMap.put("rwInsid", rwInsId);
						jsonMap.put("sender", sendEmail);
						jsonMap.put("sendTime", sendTime);
						jsonMap.put("subject", subject);
						jsonMap.put("operator", operator);
						arraylist.add(jsonMap);
					}
					
					returnJsonMap.replace("total", count);
					returnJsonMap.replace("root", arraylist);
				}
			}else{
				errorMsg[0] = "1";
				errorMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	
	/*查询发送内容详情 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("rwInsID")) {
				// 任务实例id;
				String rwInsID = jacksonUtil.getString("rwInsID");
				
				String sql = " SELECT TZ_FS_EMAIL,TZ_JS_EMAIL,TZ_EM_ZHUTI,date_format(TZ_FS_DT,'%Y-%m-%d %H:%i:%s') TZ_FS_DT,TZ_YJ_ZHWEN FROM PS_TZ_YJFSLSHI_TBL A,PS_TZ_YJZWLSHI_TBL B WHERE A.TZ_RWSL_ID=B.TZ_RWSL_ID AND A.TZ_RWSL_ID=?";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{rwInsID});
				if(map != null){
					returnJsonMap.put("senderEmail", map.get("TZ_FS_EMAIL"));
					returnJsonMap.put("AddresseeEmail",map.get("TZ_JS_EMAIL" ));
					returnJsonMap.put("emailTheme", map.get("TZ_EM_ZHUTI" ));
					returnJsonMap.put("sendTime", map.get("TZ_FS_DT" ));
					returnJsonMap.put("emailContent",map.get("TZ_YJ_ZHWEN" ));
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
