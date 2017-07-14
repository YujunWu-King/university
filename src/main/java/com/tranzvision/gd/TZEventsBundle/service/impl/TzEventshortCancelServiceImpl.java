package com.tranzvision.gd.TZEventsBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventshortCancelServiceImpl")
public class TzEventshortCancelServiceImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzEventApplyBarServiceImpl tzEventApplyBarServiceImpl;
	
	
	public  String  eventCancel(String actid,String actqdid) {
		String strRet="";
		String actMbrid="";
		
		try {
			String sql="SELECT TZ_HD_BMR_ID FROM PS_TZ_NAUDLIST_T WHERE TZ_ART_ID=? AND TZ_HD_QDM=? ";			
			actMbrid=sqlQuery.queryForObject(sql, new Object[]{actid ,actqdid}, "String");
			String parameter="{\"APPLYID\":"+actid+",\"BMRID\":"+actMbrid+"}";	
			strRet=tzEventApplyBarServiceImpl.tzGetJsonData(parameter);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return strRet;
		
	}

}
