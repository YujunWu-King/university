package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 查看短信发送历史
 */
@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzClueSmsHistoryServiceImpl")
public class TzClueSmsHistoryServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			if (jacksonUtil.containsKey("mobile")) {
				String mobile = jacksonUtil.getString("mobile");
				int count = 0;
				String sql = "SELECT A.TZ_RWSL_ID,A.TZ_SJ_HM,A.TZ_FS_ZT,A.TZ_FS_DT"
						+ " FROM PS_TZ_DXFSLSHI_TBL A WHERE A.TZ_SJ_HM = ?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
						new Object[] { mobile});
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						count++;
						/*String oprid = (String) list.get(i).get("ROW_ADDED_OPRID");
						String sql2 = "SELECT B.TZ_REALNAME"
								+ " FROM PS_TZ_DXFSLSHI_TBL A,PS_TZ_AQ_YHXX_TBL B"
								+ " WHERE A.ROW_ADDED_OPRID = B.OPRID";
						String operator= jdbcTemplate.queryForObject(sql2,
								new Object[] { oprid},"String");*/
						
						String slid = (String) list.get(i).get("TZ_RWSL_ID");
						String status = (String) list.get(i).get("TZ_FS_ZT");
						String phone = (String) list.get(i).get("TZ_SJ_HM");
						String sendDt = formatter.format(list.get(i).get("TZ_FS_DT"));
						Map<String, Object> jsonMap = new HashMap<String, Object>();
						jsonMap.put("slid", slid);
						jsonMap.put("status", status);
						jsonMap.put("phone", phone);
						//jsonMap.put("operator", operator);
						jsonMap.put("sendDt", sendDt);
						
						arraylist.add(jsonMap);
					}

					returnJsonMap.replace("total", count);
					returnJsonMap.replace("root", arraylist);
				}
			} else {
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
}
