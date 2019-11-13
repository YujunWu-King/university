package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author ZBB 获得模版对应站点信息
 */
@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormGetSiteClsServiceImpl")
public class AppFormGetSiteClsServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/*查看模版是否已经对应项目，如果已对应项目，则直接取项目对应的站点；否则取机构的内网站点*/
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		ArrayList<Map<String, Object>> listRetJson = new ArrayList<Map<String, Object>>();
		try {
			jacksonUtil.json2Map(comParams);
			String strTplId = jacksonUtil.getString("tplId");
			String sql = "SELECT B.TZ_SITEI_ID FROM PS_TZ_PRJ_INF_T A,PS_TZ_PROJECT_SITE_T B WHERE A.TZ_APP_MODAL_ID = ?"
					+ " AND A.TZ_PRJ_ID = B.TZ_PRJ_ID LIMIT 1";
			String siteId = sqlQuery.queryForObject(sql, new Object[] { strTplId }, "String");
			sql = "SELECT TZ_SITEI_NAME FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID = ?";
			String siteName = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");
			if((siteId != null && "".equals(siteId))){
				//如果站点编号为空
				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("siteId", siteId);
				mapRetJson.put("siteName", siteName);
				listRetJson.add(mapRetJson);
			}else{
				//根据当前机构获取所有内网站点
				String sqlGetSites = "SELECT TZ_SITEI_ID,TZ_SITEI_NAME FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_TYPE = 'C' AND TZ_SITEI_ENABLE = 'Y' AND TZ_JG_ID = ?";
				List<?> listGetSites = sqlQuery.queryForList(sqlGetSites, new Object[] { strOrgId });
				for (Object objSites : listGetSites) {
					Map<String, Object> mapSites = (Map<String, Object>) objSites;
					siteId = mapSites.get("TZ_SITEI_ID") == null ? ""
							: String.valueOf(mapSites.get("TZ_SITEI_ID"));
					siteName = mapSites.get("TZ_SITEI_NAME") == null ? ""
							: String.valueOf(mapSites.get("TZ_SITEI_NAME"));
					Map<String, Object> mapRetJson = new HashMap<String, Object>();
					mapRetJson.put("siteId", siteId);
					mapRetJson.put("siteName", siteName);
					listRetJson.add(mapRetJson);
				}
			}
			mapRet.replace("total", listRetJson.size());
			mapRet.replace("root", listRetJson);
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
}
