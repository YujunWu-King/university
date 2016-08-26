package com.tranzvision.gd.TZLeaguerDataItemBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 得到机构可以报名的项目列表;
 * @author Administrator
 *
 */

@Service("com.tranzvision.gd.TZLeaguerDataItemBundle.service.impl.TzGetClassProjectServiceImpl")
public class TzGetClassProjectServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, String>> listData = new ArrayList<Map<String, String>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		int total = 0;
		
		try {
			jacksonUtil.json2Map(strParams);
			String siteid = jacksonUtil.getString("siteid");
			String str_jg_id = jdbcTemplate.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?", new Object[]{siteid},"String");
			if(str_jg_id != null){
				//显示所有已经开通的项目;
				//String sql = "  SELECT DISTINCT B.TZ_PRJ_ID,B.TZ_PRJ_NAME from  PS_TZ_CLASS_INF_T A ,PS_TZ_PRJ_INF_T B where A.TZ_PRJ_ID = B.TZ_PRJ_ID AND A.TZ_JG_ID=? and A.TZ_IS_APP_OPEN='Y' and A.TZ_APP_START_DT IS NOT NULL AND A.TZ_APP_START_TM IS NOT NULL AND A.TZ_APP_END_DT IS NOT NULL AND A.TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(A.TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(A.TZ_APP_END_DT,'%Y/%m/%d'),' ', DATE_FORMAT(A.TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() ORDER BY A.TZ_APP_START_DT,A.TZ_APP_END_DT ASC;";
				String sql = "select TZ_PRJ_ID,TZ_PRJ_NAME from PS_TZ_PRJ_INF_T where TZ_IS_OPEN='Y' and TZ_JG_ID=? and TZ_PRJ_ID in (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T where TZ_SITEI_ID=?)";
				List<Map<String, Object>> prjList = jdbcTemplate.queryForList(sql, new Object[] { str_jg_id,siteid });
				if(prjList != null && prjList.size()>0){
					for(int i = 0; i<prjList.size();i++){
						String prjId = (String)prjList.get(i).get("TZ_PRJ_ID");
						String prjName= (String)prjList.get(i).get("TZ_PRJ_NAME");
						if(prjName == null){
							prjName = "";
						}
						
						Map<String, String> prjMap = new HashMap<String, String>();
						prjMap.put("prjId", prjId);
						prjMap.put("prjName", prjName);
						listData.add(prjMap);
						total ++;
					}
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		mapRet.replace("total", total);
		mapRet.replace("root", listData);
		return jacksonUtil.Map2json(mapRet);

	}
}
