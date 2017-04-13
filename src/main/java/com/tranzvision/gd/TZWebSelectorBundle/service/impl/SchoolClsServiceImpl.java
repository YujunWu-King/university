package com.tranzvision.gd.TZWebSelectorBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
			String province = "";
			if(jacksonUtil.containsKey("province")){
				province = jacksonUtil.getString("province");
			}
			String strCountry = "";
			try{
			    strCountry = jacksonUtil.getString("country");
			}catch(Exception e){
			    /*未传递国家参数*/
			}

			if (strValue == null) {
				strValue = "";
			} else {
				strValue = strValue.trim();
			}

			String sqlFindScholls = "";
			List<Map<String, Object>> list;
			// 通过省市名称查询 BEGIN
			if ("BYSCHOOL".equals(strOType)) {				
				if ("".equals(strValue)) {
					sqlFindScholls = "SELECT TZ_SCHOOL_NAME,TZ_SCHOOL_NAMEENG FROM PS_TZ_SCH_LIB_TBL ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
					list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] {});
				} else {
					sqlFindScholls = "SELECT TZ_SCHOOL_NAME,TZ_SCHOOL_NAMEENG FROM PS_TZ_SCH_LIB_TBL where COUNTRY=? ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
					list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { strValue });
				}

				ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> returnMap = new HashMap<>();
						String schoolname_en = (String) list.get(i).get("TZ_SCHOOL_NAMEENG");
						if (list.get(i).get("TZ_SCHOOL_NAMEENG") != null && !"".equals(schoolname_en)) {
							returnMap.put("schoolName", list.get(i).get("TZ_SCHOOL_NAME") + "(" + schoolname_en + ")");
						} else {
							returnMap.put("schoolName", list.get(i).get("TZ_SCHOOL_NAME"));
						}

						arraylist.add(returnMap);
					}
				}
				try {
					result = mapper.writeValueAsString(arraylist);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
			}
			/*国家为中国时，按省份初始化数据*/
			if ("BYPROVINCE".equals(strOType)) {
				sqlFindScholls = "SELECT TZ_SCHOOL_NAME,TZ_SCHOOL_NAMEENG FROM PS_TZ_SCH_LIB_TBL where 1 = 1";

				Object[] params = new Object[] {};
				if(StringUtils.isBlank(province)){
					province = "%";
				}else{
					province += "%";
				}

				if(StringUtils.isNotBlank(strValue)){
					sqlFindScholls += " AND COUNTRY=?  AND TZ_PROVINCES LIKE ? ";
					params = new Object[] {strValue,province};
				}else{
					sqlFindScholls += " AND TZ_PROVINCES LIKE ? ";
					params = new Object[] {province};
				}
				
				sqlFindScholls += " ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
				list = jdbcTemplate.queryForList(sqlFindScholls, params);

				ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> returnMap = new HashMap<>();
						String schoolname_en = (String) list.get(i).get("TZ_SCHOOL_NAMEENG");
						if (list.get(i).get("TZ_SCHOOL_NAMEENG") != null && !"".equals(schoolname_en)) {
							returnMap.put("schoolName", list.get(i).get("TZ_SCHOOL_NAME") + "(" + schoolname_en + ")");
						} else {
							returnMap.put("schoolName", list.get(i).get("TZ_SCHOOL_NAME"));
						}

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
			    	if("".equals(strCountry)||strCountry==null){
			    	    sqlFindScholls = "SELECT TZ_SCHOOL_NAME,TZ_SCHOOL_NAMEENG FROM PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME LIKE ? OR TZ_SCHOOL_NAMEENG LIKE ? ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
			    	    list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { "%" + strValue + "%","%" + strValue + "%"});    
			    	}else{
			    	    sqlFindScholls = "SELECT TZ_SCHOOL_NAME,TZ_SCHOOL_NAMEENG FROM PS_TZ_SCH_LIB_TBL where (TZ_SCHOOL_NAME LIKE ? OR TZ_SCHOOL_NAMEENG LIKE ?) AND COUNTRY=? ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
			    	    list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { "%" + strValue + "%","%" + strValue + "%",strCountry});
			    	}
				
				ArrayList<String> arraylist = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String schoolname_en = (String) list.get(i).get("TZ_SCHOOL_NAMEENG");
						if (list.get(i).get("TZ_SCHOOL_NAMEENG") != null && !"".equals(schoolname_en)) {
							arraylist.add((String) list.get(i).get("TZ_SCHOOL_NAME") + "(" + schoolname_en + ")");
						} else {
							arraylist.add((String) list.get(i).get("TZ_SCHOOL_NAME"));
						}

					}
				}
				try {
					result = mapper.writeValueAsString(arraylist);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
			}

			if ("BYSCHOOLNAME".equals(strOType)) {
				sqlFindScholls = "SELECT TZ_SCHOOL_NAME,TZ_SCHOOL_NAMEENG FROM PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME LIKE ? ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
				list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { "%" + strValue + "%" });
				ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> returnMap = new HashMap<>();
						String schoolname_en = (String) list.get(i).get("TZ_SCHOOL_NAMEENG");
						if (list.get(i).get("TZ_SCHOOL_NAMEENG") != null && !"".equals(schoolname_en)) {
							returnMap.put("schoolName", list.get(i).get("TZ_SCHOOL_NAME") + "(" + schoolname_en + ")");
						} else {
							returnMap.put("schoolName", list.get(i).get("TZ_SCHOOL_NAME"));
						}

						arraylist.add(returnMap);
					}
				}
				try {
					result = mapper.writeValueAsString(arraylist);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
			}
			// 通过省市名称查询 BEGIN
			if ("BYCOUNTRY".equals(strOType)) {				
				if ("".equals(strValue)) {
					sqlFindScholls = "SELECT TZ_SCHOOL_NAME,TZ_SCHOOL_NAMEENG FROM PS_TZ_SCH_LIB_TBL ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
					list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] {});
				} else {
					sqlFindScholls = "SELECT TZ_SCHOOL_NAME,TZ_SCHOOL_NAMEENG FROM PS_TZ_SCH_LIB_TBL where COUNTRY=? ORDER BY convert(TZ_SCHOOL_NAME using gbk) asc";
					list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { strValue });
				}

				ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> returnMap = new HashMap<>();
						String schoolname_en = (String) list.get(i).get("TZ_SCHOOL_NAMEENG");
						if (list.get(i).get("TZ_SCHOOL_NAMEENG") != null && !"".equals(schoolname_en)) {
							returnMap.put("schoolName", list.get(i).get("TZ_SCHOOL_NAME") + "(" + schoolname_en + ")");
						} else {
							returnMap.put("schoolName", list.get(i).get("TZ_SCHOOL_NAME"));
						}

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
		
		
		//A:注册用  B:报名表
		String type = jacksonUtil.getString("Type");
		
		try {
			schoolHtml = tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_SCHOOL_SELECT", tzGeneralURL, contextUrl,
					imgPath,type);
		} catch (TzSystemException e) {
			e.printStackTrace();
		}
		return schoolHtml;

	}

}
