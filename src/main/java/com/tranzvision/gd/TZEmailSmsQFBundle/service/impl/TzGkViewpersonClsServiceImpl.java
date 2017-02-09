package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 功能描述：EDM统计-查看收件人
 * TZ_GK_EDM_PKG:TZ_GK_VIEWPERSON_CLS
 *
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzGkViewpersonClsServiceImpl")
public class TzGkViewpersonClsServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		
		//返回;
		Map<String, Object> returnMap = new HashMap<>();
		ArrayList<Map<String, Object>> returnList = new ArrayList<>();
		// 邮件群发批次编号;
		String emailID = jacksonUtil.getString("emailID");
		
		String page = request.getParameter("page");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		int count = 0;
		
		if(page != null && !"".equals(page)
				&& start != null && !"".equals(start)
				&& limit != null && !"".equals(limit)){
			int startNum = Integer.valueOf(start);
			int limitNum = Integer.valueOf(limit);
			
			
			String strRealName = "", strEmail = "",strMobile="";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(
					"SELECT TZ_AUD_XM,TZ_ZY_EMAIL,TZ_ZY_SJ FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID IN (SELECT TZ_AUDIENCE_ID FROM PS_TZ_DXYJFSRW_TBL A WHERE A.TZ_MLSM_QFPC_ID=?) ORDER BY TZ_AUDCY_ID LIMIT ?,?", new Object[] { emailID,startNum,limitNum });
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					strRealName = (String)list.get(i).get("TZ_AUD_XM");
					strEmail = (String)list.get(i).get("TZ_ZY_EMAIL");
					strMobile = (String)list.get(i).get("TZ_ZY_SJ");

					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("personName", strRealName);
					map1.put("personEmail",strEmail);
					map1.put("personMobile", strMobile);
					returnList.add(map1);
					count = count + 1;
				}
			}
			/*
			//短信邮件群发收件人表;
			List<Map<String, Object>> list = jdbcTemplate.queryForList("select TZ_AUDCY_ID,TZ_EMAIL from PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=? ORDER BY TZ_AUDCY_ID LIMIT ?,?",new Object[]{emailID,startNum,limitNum});
			if(list != null && list.size()>0){
				for(int i=0; i<list.size();i++){
					String strAudID = String.valueOf(list.get(i).get("TZ_AUDCY_ID"));
					String strEmail = String.valueOf(list.get(i).get("TZ_EMAIL"));
					Map<String, Object> xmSjMap = jdbcTemplate.queryForMap("select TZ_AUD_XM,TZ_ZY_SJ from PS_TZ_AUDCYUAN_T where TZ_AUDCY_ID=? and TZ_ZY_EMAIL=? limit 0,1",new Object[]{strAudID, strEmail});
					String strRealName = "",strMobile="";
					if(xmSjMap != null){
						strRealName = (String)xmSjMap.get("TZ_AUD_XM"); 
						strMobile = (String)xmSjMap.get("TZ_ZY_SJ"); 
					}

					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("personName", strRealName);
					map1.put("personEmail",strEmail);
					map1.put("personMobile", strMobile);
					returnList.add(map1);
					count = count + 1;
				}
			}
			
			//短信邮件群发听众表;
			
			//短信邮件群发excel导入内容表;
			list = jdbcTemplate.queryForList("select A.TZ_AUDCY_ID,A.TZ_ZY_EMAIL from (select distinct TZ_AUDCY_ID,TZ_ZY_EMAIL from PS_TZ_AUDCYUAN_T where  TZ_AUDCY_ID in (select TZ_AUDCY_ID from PS_TZ_MLSM_DRNR_T where TZ_MLSM_QFPC_ID=?)) A ORDER BY A.TZ_AUDCY_ID limit ?,?",new Object[]{emailID,startNum,limitNum});
			if(list != null && list.size()>0){
				for(int i=0; i<list.size();i++){
					String strAudID = String.valueOf(list.get(i).get("TZ_AUDCY_ID"));
					String strEmail = String.valueOf(list.get(i).get("TZ_EMAIL"));
					Map<String, Object> xmSjMap = jdbcTemplate.queryForMap("select TZ_AUD_XM,TZ_ZY_SJ from PS_TZ_AUDCYUAN_T where TZ_AUDCY_ID=? and TZ_ZY_EMAIL=? limit 0,1",new Object[]{strAudID, strEmail});
					String strRealName = "",strMobile="";
					if(xmSjMap != null){
						strRealName = (String)xmSjMap.get("TZ_AUD_XM"); 
						strMobile = (String)xmSjMap.get("TZ_ZY_SJ"); 
					}

					Map<String, Object> map1 = new HashMap<String, Object>();
					map1.put("personName", strRealName);
					map1.put("personEmail",strEmail);
					map1.put("personMobile", strMobile);
					returnList.add(map1);
					count = count + 1;
				}
			}
			*/
		}
		
	    returnMap.put("total", count);
	    returnMap.put("root", returnList);
	    return jacksonUtil.Map2json(returnMap);
	}
}
