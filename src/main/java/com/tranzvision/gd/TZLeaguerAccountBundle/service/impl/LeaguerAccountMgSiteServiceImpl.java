package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * 
 * @author tmt
 * @since 2015-11-20
 */
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.LeaguerAccountMgSiteServiceImpl")
@SuppressWarnings("unchecked")
public class LeaguerAccountMgSiteServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private com.tranzvision.gd.util.sql.SqlQuery SqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private TZGDObject tzGdObject;
	
	
	
	
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		

		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);
			Map<String, Object> conditionJson = jacksonUtil.getMap("condition");
			Object jgID=conditionJson.get("TZ_JG_ID-value");

			String sqltext="select TZ_SITEI_ID,TZ_SITEI_NAME,TZ_JG_ID from PS_TZ_WEBSIT_SET_VW where TZ_JG_ID=? ";
			List<Map<String, Object>> mlist =SqlQuery.queryForList(sqltext, new Object[] { jgID });
			
			if(mlist!=null) {
			mapRet.replace("root", mlist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}

}
