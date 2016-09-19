package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

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
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy
 * @version 创建时间：2016年9月18日 下午6:33:53 类说明 批量发布
 */
@Service("com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl.OrgBatchServiceImpl")
public class OrgBatchServiceImpl extends FrameworkImpl {

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private SqlQuery jdbcTemplate;
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
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// System.out.println("orgId:" + orgId);
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_BATCH_RELEASE_T";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { orgId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			// System.out.println("total:" + total);
			// System.out.println("numLimit:" + numLimit);
			StringBuffer sb = new StringBuffer();
			if (numLimit > 0) {
				sb.append(
						"SELECT A.TZ_BATCH_RELEASE_ID,A.TZ_BATCH_RELEASE_TYPE,A.BATCH_DTTM,A.BATCH_OPRID,A.TZ_BATCH_RELEASE_STATE,");
				sb.append("B.TZ_SITEI_NAME,C.TZ_COLU_NAME ");
				sb.append("FROM PS_TZ_BATCH_RELEASE_T A");
				sb.append("LEFT JOIN PS_TZ_SITEI_DEFN_T B ");
				sb.append("ON (A.TZ_BATCH_OBJECT_ID=B.TZ_SITEI_ID AND A.TZ_BATCH_RELEASE_TYPE='A')");
				sb.append("LEFT JOIN PS_TZ_SITEI_COLU_T C ");
				sb.append(
						"ON (A.TZ_BATCH_OBJECT_ID=C.TZ_COLU_ID AND A.TZ_BATCH_RELEASE_TYPE='B') ORDER BY A.BATCH_DTTM LIMIT ?,?");

				list = jdbcTemplate.queryForList(sb.toString(), new Object[] { orgId, numStart, numLimit });
			} else {
				sb.append(
						"SELECT A.TZ_BATCH_RELEASE_ID,A.TZ_BATCH_RELEASE_TYPE,A.BATCH_DTTM,A.BATCH_OPRID,A.TZ_BATCH_RELEASE_STATE,");
				sb.append("B.TZ_SITEI_NAME,C.TZ_COLU_NAME ");
				sb.append("FROM PS_TZ_BATCH_RELEASE_T A");
				sb.append("LEFT JOIN PS_TZ_SITEI_DEFN_T B ");
				sb.append("ON (A.TZ_BATCH_OBJECT_ID=B.TZ_SITEI_ID AND A.TZ_BATCH_RELEASE_TYPE='A')");
				sb.append("LEFT JOIN PS_TZ_SITEI_COLU_T C ");
				sb.append(
						"ON (A.TZ_BATCH_OBJECT_ID=C.TZ_COLU_ID AND A.TZ_BATCH_RELEASE_TYPE='B') ORDER BY A.BATCH_DTTM");
				list = jdbcTemplate.queryForList(sql);
			}
			// System.out.println("sql:" + sql);
			if (list != null) {
				// System.out.println("list:" + list.size());
				Map<String, Object> jsonMap = null;
				for (int i = 0; i < list.size(); i++) {
					jsonMap = new HashMap<String, Object>();
					jsonMap.put("siteId", list.get(i).get("TZ_SITEI_ID"));
					jsonMap.put("sitetemplateName", list.get(i).get("TZ_SITEI_NAME"));
					jsonMap.put("explanation", list.get(i).get("TZ_SITEI_DESCR"));
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
				strRet = jacksonUtil.Map2json(returnJsonMap);
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
