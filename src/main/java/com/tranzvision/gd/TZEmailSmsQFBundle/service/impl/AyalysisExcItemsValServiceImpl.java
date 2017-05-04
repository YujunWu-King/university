package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.AyalysisExcItemsValServiceImpl")
public class AyalysisExcItemsValServiceImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	public ArrayList<Map<String, String>> ayalyExcVar(String strPicId, String audCyId){
		ArrayList<Map<String, String>> list = new ArrayList<>();
		
		String itemName = "" ,StoreFieldName ="";
		List<Map<String, Object>> excItemList = jdbcTemplate.queryForList("SELECT TZ_XXX_NAME,TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=?",new Object[]{strPicId});
		if(excItemList != null && excItemList.size() > 0){
			for(int i = 0; i < excItemList.size(); i++){
				itemName = excItemList.get(i).get("TZ_XXX_NAME") == null ? "" : (String)excItemList.get(i).get("TZ_XXX_NAME");
				StoreFieldName = excItemList.get(i).get("TZ_FIELD_NAME") == null ? "" : (String)excItemList.get(i).get("TZ_FIELD_NAME");
				String fieldValue = "";
				if(itemName != null && !"".equals(itemName) && StoreFieldName!= null && !"".equals(StoreFieldName)){
					String selectSql = "SELECT " + StoreFieldName + " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=? AND TZ_AUDCY_ID=?";
					fieldValue = jdbcTemplate.queryForObject(selectSql, new Object[]{strPicId,audCyId},"String");
					if(fieldValue == null){
						fieldValue = "";
					}
					Map<String, String> map = new HashMap<>();
					map.put("name", "[" + itemName + "]");
					map.put("value", fieldValue);
					list.add(map);
				}
				
			}
		}

		return list;
	}
}
