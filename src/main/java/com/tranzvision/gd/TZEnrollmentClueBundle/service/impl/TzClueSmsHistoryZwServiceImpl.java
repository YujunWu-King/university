package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 查看短信发送历史正文
 */
@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzClueSmsHistoryZwServiceImpl")
public class TzClueSmsHistoryZwServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	@Override
	public String tzQuery(String comParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			String slid = jacksonUtil.getString("slid");
			if (slid != null && !"".equals(slid)) {
				String sql = "SELECT A.TZ_DX_ZHWEN"
						+ " FROM PS_TZ_DXZWLSHI_TBL A WHERE A.TZ_RWSL_ID = ?";
				String zw = jdbcTemplate.queryForObject(sql,
						new Object[] { slid},"String");
				returnJsonMap.put("zw", zw);
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取页面信息";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
