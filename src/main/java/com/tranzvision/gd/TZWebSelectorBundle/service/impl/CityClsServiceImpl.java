package com.tranzvision.gd.TZWebSelectorBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 城市选择器
 * 
 * @author tang 
 * 
 * PS: TZ_GD_COMMON_PKG:TZ_CITY_CLS
 */
@Service("com.tranzvision.gd.TZWebSelectorBundle.service.impl.CityClsServiceImpl")
public class CityClsServiceImpl extends FrameworkImpl{
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGdObject;
	
	@Override
	// 选择省;
	public String tzGetHtmlContent(String strParams) {
		try{
			String pageID = "";
			jacksonUtil.json2Map(strParams);
			
			if (jacksonUtil.containsKey("TZ_CITY_ID")) {
				pageID = jacksonUtil.getString("TZ_CITY_ID");
			}
			
			String strOType = jacksonUtil.getString("OType");
			if("CITY".equals(strOType)){
				//得到省份
				String sql = "SELECT DESCR,STATE FROM PS_STATE_TBL WHERE COUNTRY='CHN' order by convert(DESCR using gbk) asc";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
				String proInfo = "";
				if(list != null && list.size() > 0 ){
					for(int i=0; i < list.size(); i++){
						String strDesc = (String)list.get(i).get("DESCR");
						String strState = (String)list.get(i).get("STATE");
						String states = strDesc.substring(0,strDesc.indexOf(" "));
						proInfo = proInfo + tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_ENROLL_PRO2", true, states,strState);
					}
				}
				//得到热门城市
				sql = "SELECT TZ_CITY FROM PS_TZ_CITY_TBL WHERE COUNTRY='CHN' AND TZ_CITY_ISHOT='Y' ORDER BY TZ_CITY_ORDER";
				list = jdbcTemplate.queryForList(sql);
				String hotcity = "";
				if(list != null && list.size() > 0 ){
					for(int i=0; i < list.size(); i++){
						String strCity = (String)list.get(i).get("TZ_CITY");
						hotcity = hotcity + tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_ENROLL_CITYBYPRO3", true, strCity);
					}
				}
				hotcity = hotcity + tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_ENROLL_CITYBYPRO3", true, "新加坡");
				
				
				String contextUrl = request.getContextPath();
				String tzGeneralURL = contextUrl + "/dispatcher";
				return tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_GD_CHOOSE_CITY", true, hotcity, proInfo, tzGeneralURL, pageID,contextUrl);
			}
			
			
			if("C4PROVINCE".equals(strOType)){
				String procode = jacksonUtil.getString("PROCODE");
				//具体省份下的城市
				String sql = "SELECT TZ_CITY FROM PS_TZ_CITY_TBL WHERE STATE = ? AND COUNTRY='CHN' ORDER BY convert(TZ_CITY using gbk) asc";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,new Object[]{procode});
				String cityInfo = "";
				if(list != null && list.size() > 0 ){
					for(int i=0; i < list.size(); i++){
						String city = (String)list.get(i).get("TZ_CITY");
						cityInfo = cityInfo + tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_ENROLL_CITYBYPRO3", true, city);
					}
				}
				
				
				String contextUrl = request.getContextPath();
				return tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_ENROLL_CITY4", true, cityInfo, pageID,contextUrl);
			}
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		return "";
	}
}
