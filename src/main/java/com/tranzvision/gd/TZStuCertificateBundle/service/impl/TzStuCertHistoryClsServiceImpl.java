package com.tranzvision.gd.TZStuCertificateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZStuCertificateBundle.service.impl.TzStuCertHistoryClsServiceImpl")
public class TzStuCertHistoryClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private TZGDObject tzSQLObject;

	/* 学生证书管理列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			if (jacksonUtil.containsKey("certNo")) {
				String certNo = jacksonUtil.getString("certNo");
				// System.out.println("certNo:" + certNo);
				Map<String, Object> mapList = null;
				String totalSQL = "SELECT COUNT(1) FROM PS_TZ_CERT_OPR_LOG where TZ_ZHSH_ID = ?";
				int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { certNo }, "Integer");

				String sql = tzSQLObject.getSQLText("SQL.TZStuCertificateBundle.TZSelectStuCertHistory");
				List<Map<String, Object>> list = null;
				if (numLimit > 0) {
					sql = sql + " LIMIT ?,?";
					list = jdbcTemplate.queryForList(sql, new Object[] { certNo, numStart, numLimit });
				} else {
					list = jdbcTemplate.queryForList(sql, new Object[] { certNo });
				}
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						mapList = new HashMap<String, Object>();
						mapList.put("certNo", list.get(i).get("TZ_ZHSH_ID").toString());
						mapList.put("certTypeName", list.get(i).get("TZ_CERT_TYPE_NAME").toString());
						mapList.put("operationType", list.get(i).get("TZ_CZ_TYPE").toString());
						// System.out.println(list.get(i).get("TZ_CZ_TYPE").toString());
						mapList.put("operationer", list.get(i).get("TZ_REALNAME").toString());
						mapList.put("timeStamp", list.get(i).get("ROW_ADDED_DTTM").toString());
						listData.add(mapList);
					}
					mapRet.replace("total", total);
					mapRet.replace("root", listData);
					// System.out.println("total:" + list.size());
				}
			} else {
				errorMsg[0] = "1";
				errorMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

}
