package com.tranzvision.gd.TZWebSelectorBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 城市选择器
 * 
 * @author tang
 * 
 *         PS: TZ_GD_COMMON_PKG:TZ_CITY_CLS
 */
@Service("com.tranzvision.gd.TZWebSelectorBundle.service.impl.CityClsServiceImpl")
public class CityClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Override
	// 选择省;
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String pageID = "";
			// String language = "ZHS";
			String skinId = "";
			jacksonUtil.json2Map(strParams);
			String TPLID = "";
			String siteId = "";
			
			if (jacksonUtil.containsKey("siteId")) {
				siteId = jacksonUtil.getString("siteId");
			}
			if("".equals(siteId)){
				if (jacksonUtil.containsKey("TPLID")) {
					TPLID = jacksonUtil.getString("TPLID");
				}
			}
			// 是否是报名表;
			if (!"".equals(siteId)) {
				// 根据站点id查询;
				String sql = "select TZ_SITE_LANG,TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { siteId });
				// language = (String)map.get("TZ_SITE_LANG");
				skinId = (String) map.get("TZ_SKIN_ID");
			}else if(!"".equals(TPLID)) {
				// 根据报名表模板查询language;
				
				String sql = "select a.TZ_APP_TPL_LAN,b.TZ_SKIN_ID from PS_TZ_APPTPL_DY_T a, PS_TZ_SITEI_DEFN_T b where a.TZ_JG_ID=b.TZ_JG_ID AND b.TZ_SITEI_ENABLE='Y' and a.TZ_APP_TPL_ID=? limit 0,1";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { TPLID });
				// language = (String)map.get("TZ_APP_TPL_LAN");
				skinId = (String) map.get("TZ_SKIN_ID");
			}  
			
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			if (jacksonUtil.containsKey("TZ_CITY_ID")) {
				pageID = jacksonUtil.getString("TZ_CITY_ID");
			}

			String strOType = jacksonUtil.getString("OType");
			if ("CITY".equals(strOType)) {
				// 得到省份
				String sql = "SELECT DESCR,STATE FROM PS_STATE_TBL WHERE COUNTRY='CHN' order by convert(DESCR using gbk) asc";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
				String proInfo = "";
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String strDesc = (String) list.get(i).get("DESCR");
						String strState = (String) list.get(i).get("STATE");
						String states = strDesc.substring(0, strDesc.indexOf(" "));
						proInfo = proInfo + tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_ENROLL_PRO2",
								states, strState);
					}
				}
				// 得到热门城市
				sql = "SELECT TZ_CITY FROM PS_TZ_CITY_TBL WHERE COUNTRY='CHN' AND TZ_CITY_ISHOT='Y' ORDER BY TZ_CITY_ORDER";
				list = jdbcTemplate.queryForList(sql);
				String hotcity = "";
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String strCity = (String) list.get(i).get("TZ_CITY");
						hotcity = hotcity + tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_ENROLL_CITYBYPRO3", strCity);
					}
				}
				hotcity = hotcity
						+ tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_ENROLL_CITYBYPRO3", "新加坡");

				String contextUrl = request.getContextPath();
				String tzGeneralURL = contextUrl + "/dispatcher";
				return tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_GD_CHOOSE_CITY", hotcity, proInfo,
						tzGeneralURL, pageID, contextUrl,imgPath,siteId,TPLID);
			}

			if ("C4PROVINCE".equals(strOType)) {
				String procode = jacksonUtil.getString("PROCODE");
				// 具体省份下的城市
				String sql = "SELECT TZ_CITY FROM PS_TZ_CITY_TBL WHERE STATE = ? AND COUNTRY='CHN' ORDER BY convert(TZ_CITY using gbk) asc";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { procode });
				String cityInfo = "";
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String city = (String) list.get(i).get("TZ_CITY");
						cityInfo = cityInfo
								+ tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_ENROLL_CITYBYPRO3", city);
					}
				}

				String contextUrl = request.getContextPath();
				return tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_ENROLL_CITY4", cityInfo, pageID,
						contextUrl,imgPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}
}
