package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.cms.CmsBean;
import com.tranzvision.gd.util.cms.CmsUtils;
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
	
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

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
			int total = jdbcTemplate.queryForObject(totalSQL, "Integer");
			// String sql = "";
			List<Map<String, Object>> list = null;
			// System.out.println("total:" + total);
			// System.out.println("numLimit:" + numLimit);
			StringBuffer sb = new StringBuffer();
			if (numLimit > 0) {
				sb.append(
						"SELECT A.TZ_BATCH_RELEASE_ID,A.TZ_BATCH_RELEASE_TYPE,A.BATCH_DTTM,A.BATCH_OPRID,A.END_DTTM,A.TZ_BATCH_RELEASE_STATE, ");
				sb.append("B.TZ_SITEI_NAME,C.TZ_COLU_NAME ");
				sb.append("FROM PS_TZ_BATCH_RELEASE_T A ");
				sb.append("LEFT JOIN PS_TZ_SITEI_DEFN_T B ");
				sb.append("ON (A.TZ_BATCH_OBJECT_ID=B.TZ_SITEI_ID AND A.TZ_BATCH_RELEASE_TYPE='A') ");
				sb.append("LEFT JOIN PS_TZ_SITEI_COLU_T C ");
				sb.append(
						"ON (A.TZ_BATCH_OBJECT_ID=C.TZ_COLU_ID AND A.TZ_BATCH_RELEASE_TYPE='B') ORDER BY A.BATCH_DTTM DESC LIMIT ?,? ");

				list = jdbcTemplate.queryForList(sb.toString(), new Object[] { numStart, numLimit });
			} else {
				sb.append(
						"SELECT A.TZ_BATCH_RELEASE_ID,A.TZ_BATCH_RELEASE_TYPE,A.BATCH_DTTM,A.BATCH_OPRID,A.END_DTTM,A.TZ_BATCH_RELEASE_STATE, ");
				sb.append("B.TZ_SITEI_NAME,C.TZ_COLU_NAME ");
				sb.append("FROM PS_TZ_BATCH_RELEASE_T A ");
				sb.append("LEFT JOIN PS_TZ_SITEI_DEFN_T B ");
				sb.append("ON (A.TZ_BATCH_OBJECT_ID=B.TZ_SITEI_ID AND A.TZ_BATCH_RELEASE_TYPE='A') ");
				sb.append("LEFT JOIN PS_TZ_SITEI_COLU_T C  ");
				sb.append(
						"ON (A.TZ_BATCH_OBJECT_ID=C.TZ_COLU_ID AND A.TZ_BATCH_RELEASE_TYPE='B') ORDER BY A.BATCH_DTTM DESC");
				list = jdbcTemplate.queryForList(sb.toString());
			}
			// System.out.println("sql:" + sb.toString());
			if (list != null) {
				// System.out.println("list:" + list.size());
				Map<String, Object> jsonMap = null;
				for (int i = 0; i < list.size(); i++) {
					jsonMap = new HashMap<String, Object>();
					jsonMap.put("batchId", list.get(i).get("TZ_BATCH_RELEASE_ID"));
					jsonMap.put("batchType", list.get(i).get("TZ_BATCH_RELEASE_TYPE"));
					// A:按站点发布 B:按栏目发布
					if (jsonMap.get("batchType").toString().equals("A")) {
						jsonMap.put("objectName", list.get(i).get("TZ_SITEI_NAME"));
					} else if (jsonMap.get("batchType").toString().equals("B")) {
						jsonMap.put("objectName", list.get(i).get("TZ_COLU_NAME"));
					}
					jsonMap.put("batchDate", detailDate(list.get(i).get("BATCH_DTTM").toString()));
					jsonMap.put("opr", list.get(i).get("BATCH_OPRID"));
					// System.out.println("endDate:" +
					// detailDate(list.get(i).get("END_DTTM").toString()));
					if (list.get(i).get("END_DTTM")!=null) {
						jsonMap.put("endDate", detailDate(list.get(i).get("END_DTTM").toString()));
					}
					jsonMap.put("batchStatus", list.get(i).get("TZ_BATCH_RELEASE_STATE"));
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
				strRet = jacksonUtil.Map2json(returnJsonMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	private String detailDate(String date) {
		// 2016-09-19 00:00:00
		// 2016-09-19 20:16:14.0
		int i = "2016-09-19 00:00:00".length();
		if (date.length() > i) {
			date = date.substring(0, i);
		}
		return date;
	}

	/**
	 * 批量发布
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzOther(String oprType, String strParams, String[] errMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		System.out.println("oprType:" + oprType);
		System.out.println("strParams:" + strParams);
		JacksonUtil jacksonUtil = new JacksonUtil();
		boolean success = false;
		String msg = "";
		try {
			if ("batchRelease".equals(oprType)) {
				jacksonUtil.json2Map(strParams);
				String batchType = jacksonUtil.getString("batchType");
				String objectId = jacksonUtil.getString("objectId");
				String sql = "select 'Y' from PS_TZ_BATCH_RELEASE_T where TZ_BATCH_RELEASE_TYPE=? and TZ_BATCH_OBJECT_ID=? and TZ_BATCH_RELEASE_STATE='C'";
				String recExists = jdbcTemplate.queryForObject(sql, new Object[] { batchType, objectId }, "String");

				if ("Y".equals(recExists)) {
					success = false;
					msg = "该站点或栏目正在发布中，请不要重复发布";
				} else {
					String id = String.valueOf(getSeqNum.getSeqNum("TZ_BATCH_RELEASE_T", "TZ_BATCH_ID"));
					// (String batchType, String objectId, String opr, String
					// id) {
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					String contentPath = request.getContextPath();
					String rootparth = "http://" + request.getServerName() + ":" + request.getServerPort()
							+ request.getContextPath();
					
					
					String dir = getSysHardCodeVal.getWebsiteEnrollPath();
					
					dir = request.getServletContext().getRealPath(dir);
					
					System.out.println("Begin Thread");

					new BatchReleaseEngine(batchType, objectId, oprid, id, rootparth, contentPath,dir).start();
					success = true;
				}

			}
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		mapRet.put("success", success);
		mapRet.put("msg", msg);
		return jacksonUtil.Map2json(mapRet);
	}
}
