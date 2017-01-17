package com.tranzvision.gd.TZZnxQfBundle.service.impl;

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

@Service("com.tranzvision.gd.TZZnxQfBundle.service.impl.TzZnxDetClsServiceImpl")
public class TzZnxDetClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSeqNum getSeqNum;

	/* 获取页面信息 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strComContent = "{}";

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("queryID")) {
			String queryID = jacksonUtil.getString("queryID");
			if ("recever".equals(queryID)) {
				strComContent = this.queryReceverList(comParams, errorMsg);
			}

			if ("znxtmpl".equals(queryID)) {
				strComContent = this.queryZnxTmplList(comParams, errorMsg);
			}

			if ("atta".equals(queryID)) {
				strComContent = this.queryAttaList(comParams, errorMsg);
			}
		} else {
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}

		return strComContent;
	}

	/* 获取页面信息 */
	@Override
	public String tzOther(String OperateType, String comParams, String[] errorMsg) {
		// 返回值;
		String strResultConten = "{}";
		boolean bl = false;

		if ("getCreInfo".equals(OperateType)) {
			bl = true;
			strResultConten = this.getCreInfo(comParams, errorMsg);
		}
		// 得到站内信模板信息
		if ("getZnxTmpInfo".equals(OperateType)) {
			bl = true;
			strResultConten = this.getZnxTmplInfo(comParams, errorMsg);
		}
		// 得到站内信模板信息项
		if ("getZnxTmpItem".equals(OperateType)) {
			strResultConten = this.getZnxTmplItem(comParams, errorMsg);
			bl = true;
		}

		if (bl == false) {
			errorMsg[0] = "1";
			errorMsg[1] = "未找到[" + OperateType + "]对应处理方法.";
		}

		return strResultConten;
	}

	/* 站内信收件人 */
	private String queryReceverList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();
		/*
		 * jacksonUtil.json2Map(comParams);
		 * 
		 * String strEmlQfId = ""; if (jacksonUtil.containsKey("emlQfId")) {
		 * strEmlQfId = jacksonUtil.getString("emlQfId"); } else { return
		 * jacksonUtil.Map2json(mapRet); }
		 */

		// 考生;
		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String ksSQL = "select OPRID,IF(TZ_REALNAME is NULL,OPRID,TZ_REALNAME) TZ_REALNAME FROM PS_TZ_QFKSXX_VW WHERE TZ_JG_ID=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(ksSQL, new Object[] { strOrgId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String strID, strDesc;
			for (i = 0; i < list.size(); i++) {
				strID = (String) list.get(i).get("OPRID");
				strDesc = (String) list.get(i).get("TZ_REALNAME");
				if (strID != null && !"".equals(strID) && strDesc != null && !"".equals(strDesc)) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", strID);
					map.put("desc", strDesc);
					listData.add(map);
				}
			}
		}

		// 听众;
		String strSql = "SELECT TZ_AUD_ID,TZ_AUD_NAM FROM PS_TZ_AUDIENCE_VW WHERE TZ_JG_ID=?";
		List<Map<String, Object>> audlist = jdbcTemplate.queryForList(strSql, new Object[] { strOrgId });
		if (audlist != null && audlist.size() > 0) {
			int i = 0;
			String strID, strDesc;
			for (i = 0; i < audlist.size(); i++) {
				strID = (String) audlist.get(i).get("TZ_AUD_ID");
				strDesc = (String) audlist.get(i).get("TZ_AUD_NAM");
				if (strID != null && !"".equals(strID) && strDesc != null && !"".equals(strDesc)) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", strID);
					map.put("desc", strDesc);
					listData.add(map);
				}
			}

		}

		mapRet.replace("root", listData);

		/*
		 * // 邮件群发听众; String strSql =
		 * "select A.TZ_AUDIENCE_ID,B.TZ_AUD_NAM from PS_TZ_DXYJQAUD_T A,PS_TZ_AUD_DEFN_T B WHERE A.TZ_AUDIENCE_ID=B.TZ_AUD_ID AND A.TZ_MLSM_QFPC_ID=?"
		 * ; List<Map<String, Object>> audlist =
		 * jdbcTemplate.queryForList(strSql, new Object[] { strEmlQfId }); if
		 * (audlist != null && audlist.size() > 0) { int i = 0; String strID,
		 * strDesc; for (i = 0; i < audlist.size(); i++) { strID = (String)
		 * audlist.get(i).get("TZ_AUDIENCE_ID"); strDesc = (String)
		 * audlist.get(i).get("TZ_AUD_NAM"); if (strID != null &&
		 * !"".equals(strID) && strDesc != null && !"".equals(strDesc)) {
		 * Map<String, Object> map = new HashMap<>(); map.put("id", strID);
		 * map.put("desc", strDesc); listData.add(map); } }
		 * mapRet.replace("root", listData); }
		 * 
		 * // 邮件群发收件人; strSql =
		 * "SELECT TZ_AUDCY_ID,TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?"
		 * ; List<Map<String, Object>> list = jdbcTemplate.queryForList(strSql,
		 * new Object[] { strEmlQfId }); if (list != null && list.size() > 0) {
		 * int i = 0; String strID, strDesc; for (i = 0; i < list.size(); i++) {
		 * strID = (String) list.get(i).get("TZ_AUDCY_ID"); strDesc = (String)
		 * list.get(i).get("TZ_EMAIL"); if (strID != null && !"".equals(strID)
		 * && strDesc != null && !"".equals(strDesc)) { Map<String, Object> map
		 * = new HashMap<>(); map.put("id", strID); map.put("desc", strDesc);
		 * listData.add(map); } } mapRet.replace("root", listData); }
		 */
		return jacksonUtil.Map2json(mapRet);

	}

	/* 查询站内信模版 */
	private String queryZnxTmplList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();

		// 当前登录人的机构;
		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		System.out.println("------strOrgId------>" + strOrgId);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"SELECT TZ_TMPL_ID,TZ_TMPL_NAME FROM PS_TZ_ZNXTMPL_TBL WHERE TZ_JG_ID=? AND TZ_USE_FLAG='Y'",
				new Object[] { strOrgId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String strZnxTmpl, strDesc;
			for (i = 0; i < list.size(); i++) {
				strZnxTmpl = (String) list.get(i).get("TZ_TMPL_ID");
				strDesc = (String) list.get(i).get("TZ_TMPL_NAME") == null ? ""
						: (String) list.get(i).get("TZ_TMPL_NAME");

				Map<String, Object> map = new HashMap<>();
				map.put("znxtmpl", strZnxTmpl);
				map.put("desc", strDesc);
				listData.add(map);
			}
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);

	}

	/* 查询附件 */
	private String queryAttaList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		String strznxQfId = ""; /* 群发任务id */
		if (jacksonUtil.containsKey("znxQfId")) {
			strznxQfId = jacksonUtil.getString("znxQfId");
		} else {
			return jacksonUtil.Map2json(mapRet);
		}

		// 查询附件;
		String strSql = "SELECT TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FILE_PATH FROM PS_TZ_YJQFFJXX_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY CAST(TZ_FJIAN_ID as SIGNED) DESC";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(strSql, new Object[] { strznxQfId });
		if (list != null && list.size() > 0) {
			int i = 0;
			String strAttaID, strAttaName, strAttaURl;
			for (i = 0; i < list.size(); i++) {
				strAttaID = (String) list.get(i).get("TZ_FJIAN_ID");
				strAttaName = (String) list.get(i).get("TZ_FJIAN_MC") == null ? ""
						: (String) list.get(i).get("TZ_FJIAN_MC");
				strAttaURl = (String) list.get(i).get("TZ_FILE_PATH") == null ? ""
						: (String) list.get(i).get("TZ_FILE_PATH");
				if (!"".equals(strAttaURl)) {
					strAttaURl = request.getContextPath() + strAttaURl;
				}

				Map<String, Object> map = new HashMap<>();
				map.put("znxQfId", strznxQfId);
				map.put("attaID", strAttaID);
				map.put("attaName", strAttaName);
				map.put("attaUrl", strAttaURl);
				listData.add(map);
			}
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);

	}

	// 查询创建人;
	private String getCreInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		String znxQfId = String.valueOf(getSeqNum.getSeqNum("TZ_DXYJQF_DY_T", "TZ_MLSM_QFPC_ID"));

		map.put("znxQfId", znxQfId);
		map.put("crePer", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String crePer = jdbcTemplate.queryForObject(
				"select a.TZ_REALNAME from (select * from  PS_TZ_AQ_YHXX_TBL  where OPRID=?) a left join PS_TZ_LXFSINFO_TBL b ON  a.TZ_RYLX  = b.TZ_LXFS_LY AND a.OPRID=b.TZ_LYDX_ID limit 0,1",
				new Object[] { oprid }, "String");
		if (crePer == null) {
			crePer = "";
		}

		map.replace("crePer", crePer);

		return jacksonUtil.Map2json(map);
	}

	// 查询邮件模版信息
	private String getZnxTmplInfo(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		map.put("znxSubj", "");
		map.put("znxCont", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("znxTmpId")) {
			String tmpId = jacksonUtil.getString("znxTmpId");
			String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			Map<String, Object> znxMap = jdbcTemplate.queryForMap(
					"select TZ_ZNX_SUBJUECT,TZ_ZNX_CONTENT from PS_TZ_ZNXTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?",
					new Object[] { strOrgId, tmpId });
			if (znxMap != null) {
				String znxSubj = znxMap.get("TZ_ZNX_SUBJUECT") == null ? "" : (String) znxMap.get("TZ_ZNX_SUBJUECT");
				String znxCont = znxMap.get("TZ_ZNX_CONTENT") == null ? "" : (String) znxMap.get("TZ_ZNX_CONTENT");
				map.replace("znxSubj", znxSubj);
				map.replace("znxCont", znxCont);
			}

		}
		return jacksonUtil.Map2json(map);
	}

	// 功能说明：查询邮件模版信息项
	private String getZnxTmplItem(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> map = new HashMap<>();
		int numItem = 0;
		map.put("total", numItem);
		ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
		map.put("root", arrayList);

		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("znxTmpId")) {
			String tmpId = jacksonUtil.getString("znxTmpId");
			if (tmpId != null && !"".equals(tmpId)) {
				Map<String, Object> map1 = jdbcTemplate.queryForMap(
						"SELECT A.TZ_YMB_ID,B.TZ_YMB_CSLBM FROM PS_TZ_ZNXTMPL_TBL A,PS_TZ_TMP_DEFN_TBL B WHERE A.TZ_JG_ID=? AND A.TZ_TMPL_ID=? AND A.TZ_YMB_ID=B.TZ_YMB_ID",
						new Object[] { strOrgId, tmpId });
				if (map1 != null) {
					String strRestEmlId = map1.get("TZ_YMB_ID") == null ? "" : (String) map1.get("TZ_YMB_ID");
					String str_ymb_clsbm = map1.get("TZ_YMB_CSLBM") == null ? "" : (String) map1.get("TZ_YMB_CSLBM");
					List<Map<String, Object>> list1 = jdbcTemplate.queryForList(
							"SELECT TZ_PARA_ID,TZ_PARA_ALIAS FROM PS_TZ_TMP_PARA_TBL WHERE TZ_YMB_ID=?",
							new Object[] { strRestEmlId });
					if (list1 != null && list1.size() > 0) {
						for (int i = 0; i < list1.size(); i++) {
							String str_ParaId = list1.get(i).get("TZ_PARA_ID") == null ? ""
									: (String) list1.get(i).get("TZ_PARA_ID");
							String str_paraAlias = list1.get(i).get("TZ_PARA_ALIAS") == null ? ""
									: (String) list1.get(i).get("TZ_PARA_ALIAS");

							numItem++;
							String str_ParaItem = "[" + str_ymb_clsbm + "." + str_ParaId + "." + str_paraAlias + "]";

							Map<String, Object> returnMap1 = new HashMap<>();
							returnMap1.put("parainfoitem", str_ParaItem);
							arrayList.add(returnMap1);
						}
					}
				}
			}
		}

		
		map.replace("total", numItem);
		map.replace("root", arrayList);
		return jacksonUtil.Map2json(map);
	}
}
