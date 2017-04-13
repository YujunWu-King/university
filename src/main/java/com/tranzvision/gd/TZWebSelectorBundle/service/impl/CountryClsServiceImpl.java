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
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 国家选择器
 * 
 * @author tang
 * 
 *         PS: TZ_GD_COMMON_PKG:TZ_COUNTRY_CLS
 */
@Service("com.tranzvision.gd.TZWebSelectorBundle.service.impl.CountryClsServiceImpl")
public class CountryClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;


	@Override
	public String tzGetJsonData(String strParams) {
		String result = "";
		String language = "ZHS";
		ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
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

			if (jacksonUtil.containsKey("LANGUAGE")) {
				language = jacksonUtil.getString("LANGUAGE");
			}
			if (language == null || "".equals(language)) {
				language = "ZHS";
			}

			// 通过国家名称查询 BEGIN;
			if ("BYCOUNTRY".equals(strOType)) {
                //1.从注册表读取数据:
				if(strValue.startsWith("{")&&strValue.endsWith("}")){
					//用于处理默认值:只有一条数据(sure)
					Map<String,Object>countryMap=this.getCountryByOprId(language);
					arraylist.add(countryMap);
				}
				//2.原始处理:
				else{
					String sqlFindScholls = "";
					List<Map<String, Object>> list;
					if ("ENG".equals(language)) {
						sqlFindScholls = "SELECT COUNTRY,DESCR FROM PS_TZ_COUNTRYENG_V WHERE LANGUAGE_CD = ? AND (UPPER(COUNTRY) LIKE ? OR UPPER(DESCR) LIKE ?) ORDER BY COUNTRY";
						list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { language,
								"%" + strValue.toUpperCase() + "%", "%" + strValue.toUpperCase() + "%" });
					} else {
						sqlFindScholls = "SELECT COUNTRY,DESCR FROM PS_TZ_COUNTRY_V WHERE (UPPER(COUNTRY) LIKE ? OR UPPER(DESCR) LIKE ?) ORDER BY COUNTRY";
						list = jdbcTemplate.queryForList(sqlFindScholls,
								new Object[] { "%" + strValue.toUpperCase() + "%", "%" + strValue.toUpperCase() + "%" });
					}
					
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							Map<String, Object> returnMap = new HashMap<>();
							returnMap.put("country", list.get(i).get("COUNTRY"));
							returnMap.put("descr", list.get(i).get("DESCR"));
							arraylist.add(returnMap);
						}
					}
				}
			}

			// 通过州名称查询 BEGIN;
			if ("BYSTATE".equals(strOType)) {

				String strTvalueSQL = "select TZ_ZHZ_ID from PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_CONTINENT' AND TZ_ZHZ_CMS=?";
				String strTvalue = jdbcTemplate.queryForObject(strTvalueSQL, new Object[] { strValue }, "String");

				if (strTvalue != null && !"".equals(strTvalue)) {
					String sqlFindScholls = "";
					List<Map<String, Object>> list;
					if ("ENG".equals(language)) {
						sqlFindScholls = "SELECT COUNTRY,DESCR FROM PS_TZ_COUNTRYENG_V WHERE LANGUAGE_CD = ? AND TZ_CONTINENT= ? ORDER BY COUNTRY";
						list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { language, strTvalue });
					} else {
						sqlFindScholls = "SELECT COUNTRY, DESCR FROM PS_TZ_COUNTRY_V where TZ_CONTINENT=? order by COUNTRY";
						list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { strTvalue });
					}

					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							Map<String, Object> returnMap = new HashMap<>();
							returnMap.put("country", list.get(i).get("COUNTRY"));
							returnMap.put("descr", list.get(i).get("DESCR"));
							arraylist.add(returnMap);
						}
					}
				}
			}
			// 返回所有国家信息 BEGIN;
			if ("ALLCOUNTRY".equals(strOType)) {
                //1.从注册表读取数据:
				if(strValue.startsWith("{")&&strValue.endsWith("}")){
					//用于处理默认值:只有一条数据(sure)
					Map<String,Object>countryMap=this.getCountryByOprId(language);
					arraylist.add(countryMap);
				}//2.原始处理:
				else{
					String sqlFindScholls = "";
					List<Map<String, Object>> list;
					if ("ENG".equals(language)) {
						sqlFindScholls = "SELECT COUNTRY,DESCR FROM PS_TZ_COUNTRYENG_V WHERE LANGUAGE_CD = ? ORDER BY COUNTRY";
						list = jdbcTemplate.queryForList(sqlFindScholls, new Object[] { language});
					} else {
						sqlFindScholls = "SELECT COUNTRY,DESCR FROM PS_TZ_COUNTRY_V ORDER BY COUNTRY";
						list = jdbcTemplate.queryForList(sqlFindScholls);
					}
					
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							Map<String, Object> returnMap = new HashMap<>();
							returnMap.put("country", list.get(i).get("COUNTRY"));
							returnMap.put("descr", list.get(i).get("DESCR"));
							arraylist.add(returnMap);
						}
					}
				}
			}
			try {
				result = mapper.writeValueAsString(arraylist);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
				e.printStackTrace();
		}
		return result;
	}

	@Override
	// 国家选择器;
	public String tzGetHtmlContent(String strParams) {

		String language = "";
		String jgId = "";
		String skinId = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		Map<String, Object> getMap;
		// 是否是报名表;
		if (jacksonUtil.containsKey("siteId")) {
			String siteId = jacksonUtil.getString("siteId");
			// 根据站点id查询;
			String sql = "select TZ_SKIN_ID,TZ_SITE_LANG,TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			getMap = jdbcTemplate.queryForMap(sql, new Object[] { siteId });
			if (getMap != null) {
				language = (String) getMap.get("TZ_SITE_LANG");
				jgId = (String) getMap.get("TZ_JG_ID");
				skinId = (String) getMap.get("TZ_SKIN_ID");
			}
		} else if (jacksonUtil.containsKey("TPLID")) {
			// 根据报名表模板查询language;
			String TPLID = jacksonUtil.getString("TPLID");
			String sql = "SELECT TZ_JG_ID,TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
			getMap = jdbcTemplate.queryForMap(sql, new Object[] { TPLID });
			if (getMap != null) {
				language = (String) getMap.get("TZ_APP_TPL_LAN");
				jgId = (String) getMap.get("TZ_JG_ID");
				// 根据机构id得到skinid;
				String siteSQL = "SELECT TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y' limit 0,1";
				skinId = jdbcTemplate.queryForObject(siteSQL, new Object[] { jgId }, "String");

			}
		} else {
			language = "ZHS";
			jgId = "ADMIN";
		}

		if (language == null || "".equals(language)) {
			language = "ZHS";
		}
		if (jgId == null || "".equals(jgId)) {
			jgId = "ADMIN";
		}

		String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
		imgPath = request.getContextPath() + imgPath + "/" + skinId;

		String title = this.getMessageTextWithLanguageCd(jgId, language, "TZ_GD_COUNTRY_SELECT", "C_TITLE", "国家/地区选择",
				"Country/State Select");
		String chooseState = this.getMessageTextWithLanguageCd(jgId, language, "TZ_GD_COUNTRY_SELECT", "CHOOSE_STATE",
				"选择洲", "Choose State");
		String query = this.getMessageTextWithLanguageCd(jgId, language, "TZ_GD_COUNTRY_SELECT", "QUERY", "搜索",
				"Search");
		String ok = this.getMessageTextWithLanguageCd(jgId, language, "TZ_GD_COUNTRY_SELECT", "OK", "确 认", "OK");

		// 统一接口URL;
		String tzGeneralURL = request.getContextPath() + "/dispatcher";
		String nationalHtml = "";
		try {
			nationalHtml = tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_NATIONAL_SELECT", tzGeneralURL, title,
					query, chooseState, ok, language, request.getContextPath(), imgPath);
		} catch (TzSystemException e) {
			e.printStackTrace();
		}
		return nationalHtml;

	}

	public String getMessageTextWithLanguageCd(String jgId, String strLang, String msgSetId, String msgId,
			String defaultCNMsg, String defaultENMsg) {
		String tmpMsgText = "";
		String superOrgId = "ADMIN";
		String sql = "SELECT ifnull(B.TZ_MSG_TEXT,A.TZ_MSG_TEXT) TZ_MSG_TEXT from PS_TZ_PT_XXDY_TBL A left join PS_TZ_PT_XXDY_TBL B on A.TZ_XXJH_ID = B.TZ_XXJH_ID and A.TZ_JG_ID=B.TZ_JG_ID and A.TZ_MSG_ID=B.TZ_MSG_ID where upper(B.TZ_LANGUAGE_ID)=upper(?) and upper(A.TZ_LANGUAGE_ID)=(SELECT UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' ) AND A.TZ_XXJH_ID=? AND A.TZ_MSG_ID=? AND  UPPER(A.TZ_JG_ID)=UPPER(?)";
		try {
			tmpMsgText = jdbcTemplate.queryForObject(sql, new Object[] { strLang, msgSetId, msgId, jgId }, "String");
		} catch (Exception e) {

		}

		if (tmpMsgText == null || "".equals(tmpMsgText)) {
			try {
				tmpMsgText = jdbcTemplate.queryForObject(sql, new Object[] { strLang, msgSetId, msgId, superOrgId },
						"String");
			} catch (Exception e) {

			}
		}

		if (tmpMsgText == null || "".equals(tmpMsgText)) {
			if (!"ZHS".equals(strLang)) {
				tmpMsgText = defaultENMsg;
			} else {
				tmpMsgText = defaultCNMsg;
			}
		}
		return tmpMsgText;
	}
	//解决"国家绑定"和"院校绑定"，不能同时显示:
	public Map<String,Object> getCountryByOprId(String lang){
		Map<String,Object>returnMap=new HashMap<String,Object>();
		returnMap.put("country","");
		returnMap.put("descr","");
		
		//用户ID:
		String oprId=tzLoginServiceImpl.getLoginedManagerOprid(request);
		//1.根据用户ID,取得“国家CODE”:表PS_TZ_REG_USER_T
		final String SQL="SELECT TZ_SCH_COUNTRY FROM PS_TZ_REG_USER_T WHERE OPRID=?";
		String countryCode=jdbcTemplate.queryForObject(SQL, new Object[]{oprId}, "String");
		//2.根据“国家CODE”获得国家描述+CODE
		if(countryCode!=null){
			String getCountry = "SELECT COUNTRY,DESCR FROM PS_TZ_COUNTRY_V WHERE COUNTRY=?";
			if(lang.equals("ENG")){
				getCountry = "SELECT COUNTRY,DESCR FROM PS_TZ_COUNTRYENG_V WHERE COUNTRY=?";
			}
			Map<String,Object>dataMap=jdbcTemplate.queryForMap(getCountry, new Object[]{countryCode});
			returnMap.replace("country", dataMap.get("COUNTRY")==null?"":dataMap.get("COUNTRY").toString());
			returnMap.replace("descr", dataMap.get("DESCR")==null?"":dataMap.get("DESCR").toString());
		}
		//3.返回数据
		return returnMap;
	}
}
