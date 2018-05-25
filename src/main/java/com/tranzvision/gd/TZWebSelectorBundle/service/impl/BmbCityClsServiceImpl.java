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
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 报名报城市选择器 手机版本
 * 
 * @author caoy
 *
 */
@Service("com.tranzvision.gd.TZWebSelectorBundle.service.impl.BmbCityClsServiceImpl")
public class BmbCityClsServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGdObject;

	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
		try {
			String pageID = "";

			jacksonUtil.json2Map(strParams);

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
						proInfo = proInfo
								+ tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_MENROLL_PRO2", states, strState);
					}
				}
				// 得到热门城市
				sql = "SELECT TZ_CITY FROM PS_TZ_CITY_TBL WHERE COUNTRY='CHN' AND TZ_CITY_ISHOT='Y' ORDER BY TZ_CITY_ORDER";
				list = jdbcTemplate.queryForList(sql);
				String hotcity = "";
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String strCity = (String) list.get(i).get("TZ_CITY");
						hotcity = hotcity
								+ tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_MENROLL_CITYBYPRO3", strCity);
					}
				}
				hotcity = hotcity + tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_MENROLL_CITYBYPRO3", "上海");

				String contextUrl = request.getContextPath();
				String tzGeneralURL = contextUrl + "/dispatcher";
				String title = "城市选择";
				return tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_GD_MCHOOSE_CITY", tzGeneralURL, title,
						"热门城市", hotcity, "选择省", proInfo, "选择城市");
			}

			if ("C4PROVINCE".equals(strOType)) {
				String result = "";
				ObjectMapper mapper = new ObjectMapper();
				String procode = jacksonUtil.getString("PROCODE");
				//System.out.println(procode);
				// 具体省份下的城市
				String sql = "SELECT TZ_CITY FROM PS_TZ_CITY_TBL WHERE STATE = ? AND COUNTRY='CHN' ORDER BY convert(TZ_CITY using gbk) asc";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { procode });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> returnMap = new HashMap<>();
						String city = (String) list.get(i).get("TZ_CITY");
						returnMap.put("city", city);
						arraylist.add(returnMap);
					}
				}
				try {
					result = mapper.writeValueAsString(arraylist);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}
				System.out.println(result);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}
}
