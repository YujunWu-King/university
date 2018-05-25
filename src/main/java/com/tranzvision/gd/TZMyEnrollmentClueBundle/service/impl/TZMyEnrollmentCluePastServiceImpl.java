package com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
@Service("com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl.TZMyEnrollmentCluePastServiceImpl")
public class TZMyEnrollmentCluePastServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
 
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("leadDesc", "");
		mapRet.put("content", "[]");
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String tzLeadId = jacksonUtil.getString("TZ_LEAD_ID");
		
		try {
			if(tzLeadId!=null&&!"".equals(tzLeadId)){
				String leadDesc=sqlQuery.queryForObject("select CONCAT_WS('-',TZ_REALNAME,TZ_COMP_CNAME) from PS_TZ_XSXS_INFO_T where TZ_LEAD_ID=?", new Object[]{tzLeadId},"String");
				List<Map<String,Object>> list=sqlQuery.queryForList("select TZ_LEAD_STATUS1,TZ_LEAD_STATUS2,TZ_OPERATE_DESC,TZ_DEMO,ROW_ADDED_OPRID,DATE_FORMAT(ROW_ADDED_DTTM,'%Y-%m-%d %H:%i:%s') ROW_ADDED_DTTM from PS_TZ_XSXS_LOG_T  where TZ_LEAD_ID=?", new Object[]{tzLeadId});
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
				   if(map!=null){
					    String ROW_ADDED_OPRID=map.get("ROW_ADDED_OPRID").toString();
					    String ROW_ADDED_DTTM=map.get("ROW_ADDED_DTTM").toString();
					    String name=sqlQuery.queryForObject("select TZ_REALNAME from PS_TZ_YHZH_NB_VW where OPRID=?", new Object[]{ROW_ADDED_OPRID}, "String");
					    mapList.put("name", name);
						mapList.put("time", name+"<br/>"+ROW_ADDED_DTTM);
						mapList.put("statusBefore", map.get("TZ_LEAD_STATUS1").toString());
						mapList.put("status", map.get("TZ_LEAD_STATUS2").toString());
						mapList.put("descript", map.get("TZ_OPERATE_DESC").toString()+"<br>"+map.get("TZ_DEMO").toString());
						mapList.put("headerImgSrc", "");
				   }
				   listData.add(mapList); 
				}
				mapRet.replace("leadDesc", leadDesc);
				mapRet.replace("content", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
}
