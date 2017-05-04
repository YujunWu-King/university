package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

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
 * 
 * @author tang
 * TZ_GD_BULKES_PKG:TZ_VIEW_MORESJR_CLS
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzViewMoresjrClsServiceImpl")
public class TzViewMoresjrClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		Map<String, Object> returnMap = new HashMap<>();
		ArrayList<Map<String, Object>> returnArrayList = new ArrayList<>();
		int count = 0;
		returnMap.put("total", 0);
		returnMap.put("root", returnArrayList);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		
		String emlQfId = jacksonUtil.getString("emlQfId");
	    String sendModel = jacksonUtil.getString("sendModel");
	    List<Map<String, Object>> list = new ArrayList<>();
	    if("EXC".equals(sendModel)){
	    	String fieldName = "";
	    	fieldName = jdbcTemplate.queryForObject("SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE='B'", new Object[]{emlQfId},"String");
	        if(fieldName != null){
	        	if(numLimit == 0 && numStart == 0){
	        		list = jdbcTemplate.queryForList("SELECT " + fieldName + " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=?", new Object[]{emlQfId});
	        	}else{
	        		list = jdbcTemplate.queryForList("SELECT " + fieldName + " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=? limit ?,?", new Object[]{emlQfId,numStart,numLimit});
	        	}
	        }
	        
	        count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=?", new Object[]{emlQfId},"Integer");
	        if(list != null && list.size() > 0){
	        	for(int i = 0; i < list.size(); i++){
	        		String emailAddr = list.get(i).get(fieldName) == null ? "" : (String)list.get(i).get(fieldName);
	        		HashMap<String, Object> map = new HashMap<>();
	        		map.put("email", emailAddr);
	        		returnArrayList.add(map);
	        	}
	        }
	    }else{
	    	if(numLimit == 0 && numStart == 0){
	    		list = jdbcTemplate.queryForList("SELECT TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?",new Object[]{emlQfId});	
	        }else{
	        	list = jdbcTemplate.queryForList("SELECT TZ_EMAIL FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=? limit ?,?",new Object[]{emlQfId,numStart,numLimit});	
	        }
	    	
	    	count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PS_TZ_DXYJQFSJR_T WHERE TZ_MLSM_QFPC_ID=?", new Object[]{emlQfId},"Integer");
	    	if(list != null && list.size() > 0){
	        	for(int i = 0; i < list.size(); i++){
	        		String emailAddr = list.get(i).get("TZ_EMAIL") == null ? "" : (String)list.get(i).get("TZ_EMAIL");
	        		HashMap<String, Object> map = new HashMap<>();
	        		map.put("email", emailAddr);
	        		returnArrayList.add(map);
	        	}
	        }
	    }
	    
	    returnMap.replace("total", count);
		returnMap.replace("root", returnArrayList);
	    return jacksonUtil.Map2json(returnMap);
	}
}
