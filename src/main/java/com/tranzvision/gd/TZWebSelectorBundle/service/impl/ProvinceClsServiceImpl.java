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
 * 州省选择器
 * 
 * @author tang 
 * 
 * PS: TZ_GD_COMMON_PKG:TZ_PROVINCE_CLS
 */
@Service("com.tranzvision.gd.TZWebSelectorBundle.service.impl.ProvinceClsServiceImpl")
public class ProvinceClsServiceImpl extends FrameworkImpl{
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
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);
			
			if (jacksonUtil.containsKey("TZ_PROV_ID")) {
				pageID = jacksonUtil.getString("TZ_PROV_ID");
			}
			String sql = "SELECT DESCR,STATE FROM PS_STATE_TBL WHERE COUNTRY='CHN' order by convert(DESCR using gbk) asc";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			String proInfo = "";
			if(list != null && list.size() > 0 ){
				for(int i=0; i < list.size(); i++){
					String strDesc = (String)list.get(i).get("DESCR");
					//String strState = (String)list.get(i).get("STATE");
					String states = strDesc.substring(0,strDesc.indexOf(" "));
					proInfo = proInfo + tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_PROVINCE_A_HTML", true, states);
				}
			}
			return tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_PROVINCE_SELECTOR_HTML", true, proInfo,pageID,request.getContextPath());
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
}
