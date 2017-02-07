package com.tranzvision.gd.TZWebSelectorBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 学校选择器
 * 
 * @author tang
 * 
 *         PS: TZ_GD_COMMON_PKG:TZ_SCHOOL_CLS
 */
@Service("com.tranzvision.gd.TZWebSelectorBundle.service.impl.SchoolClsServiceImpl")
public class SchoolClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Override
	public String tzGetJsonData(String strParams) {
		String result = "";
		ObjectMapper mapper = new ObjectMapper();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);
			String strOType = jacksonUtil.getString("OType");
			String strValue = jacksonUtil.getString("search-text");
			if (strValue == null) {
				strValue = "";
			} else {
				strValue = strValue.trim();
			}

			String sqlFindScholls = "";
			List<Map<String, Object>> list;
			// 通过省市名称查询 BEGIN
			if ("BYSCHOOL".equals(strOType)) {
				sqlFindScholls = "SELECT TZ_SCHOOL_NAME FROM PS_TZ_SCH_LIB_TBL where COUNTRY=? ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
				list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { strValue });
				ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> returnMap = new HashMap<>();
						returnMap.put("schoolName", list.get(i).get("TZ_SCHOOL_NAME"));
						arraylist.add(returnMap);
					}
				}
				try {
					result = mapper.writeValueAsString(arraylist);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
			}

			if ("BYSEARCH".equals(strOType)) {
				sqlFindScholls = "SELECT TZ_SCHOOL_NAME FROM PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME LIKE ? ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
				list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { "%" + strValue + "%" });
				ArrayList<String> arraylist = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						arraylist.add((String) list.get(i).get("TZ_SCHOOL_NAME"));
					}
				}
				try {
					result = mapper.writeValueAsString(arraylist);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
			}

			if ("BYSCHOOLNAME".equals(strOType)) {
				sqlFindScholls = "SELECT TZ_SCHOOL_NAME FROM PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME LIKE ? ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
				list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { "%" + strValue + "%" });
				ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> returnMap = new HashMap<>();
						returnMap.put("schoolName", list.get(i).get("TZ_SCHOOL_NAME"));
						arraylist.add(returnMap);
					}
				}
				try {
					result = mapper.writeValueAsString(arraylist);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
			}

		} catch (Exception e) {

		}
		return result;
	}

	@Override
	// 国家选择器;
	public String tzGetHtmlContent(String strParams) {

		String language = "ZHS";
		String skinId = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		// 是否是报名表;
		if (jacksonUtil.containsKey("siteId")) {
			String siteId = jacksonUtil.getString("siteId");

			// 根据站点id查询;
			String sql = "select TZ_SITE_LANG,TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { siteId });
			language = (String) map.get("TZ_SITE_LANG");
			skinId = (String) map.get("TZ_SKIN_ID");
		} else if (jacksonUtil.containsKey("TPLID")) {
			// 根据报名表模板查询language;
			String TPLID = jacksonUtil.getString("TPLID");
			String sql = "select a.TZ_APP_TPL_LAN,b.TZ_SKIN_ID from PS_TZ_APPTPL_DY_T a, PS_TZ_SITEI_DEFN_T b where a.TZ_JG_ID=b.TZ_JG_ID AND b.TZ_SITEI_ENABLE='Y' and a.TZ_APP_TPL_ID=? limit 0,1";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { TPLID });
			language = (String) map.get("TZ_APP_TPL_LAN");
			skinId = (String) map.get("TZ_SKIN_ID");
		}

		if (language == null || "".equals(language)) {
			language = "ZHS";
		}

		// 统一接口URL;
		String contextUrl = request.getContextPath();
		String tzGeneralURL = contextUrl + "/dispatcher";
		String schoolHtml = "";

		String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
		imgPath = request.getContextPath() + imgPath + "/" + skinId;

		try {
			schoolHtml = tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_SCHOOL_SELECT", tzGeneralURL, contextUrl,
					imgPath);
		} catch (TzSystemException e) {
			e.printStackTrace();
		}
		return schoolHtml;

	}

}
