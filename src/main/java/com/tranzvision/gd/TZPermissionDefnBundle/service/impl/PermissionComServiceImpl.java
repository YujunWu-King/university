package com.tranzvision.gd.TZPermissionDefnBundle.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsClassDefn;
import com.tranzvision.gd.util.base.PaseJsonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

import net.sf.json.JSONObject;
/*
 * @author tang
 * 许可权组件定义，原PS类：TZ_GD_PLST_PKG:TZ_GD_PERMCOMP_CLS
 */
@Service("com.tranzvision.gd.TZPermissionDefnBundle.service.impl.PermissionComServiceImpl")
public class PermissionComServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/*获取组件授权页面列表*/
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		 // 返回值;
		 String strRet = "{}";
		 // 授权组件页面列表内容;
		 String strComContent = "";
		 
	     // 将字符串转换成json;
	     JSONObject CLASSJson = PaseJsonUtil.getJson(comParams);
	     // 许可权编号;
	     String strPermID = CLASSJson.getString("permID");
	     // 组件ID;
	     String strComID = CLASSJson.getString("comID");
	     // 页面ID,页面名称，只读，修改;
	     String strPageID, strPageName, strReadonly = "false", strModify = "false";
	     int numReadonly, numModify;
	      
	     // 授权组件页面列表sql;
	     String sqlComPageList;
	     
	     List<Map<String, Object>> list = null;
	     if(numLimit == 0){
	    	 sqlComPageList = "SELECT TZ_PAGE_ID,TZ_PAGE_MC FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=?  ORDER BY TZ_PAGE_ID";
	    	 list = jdbcTemplate.queryForList(sqlComPageList,new Object[]{strComID});
	     }else{
	    	 sqlComPageList = "SELECT TZ_PAGE_ID,TZ_PAGE_MC FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=?  ORDER BY TZ_PAGE_ID limit ?,?"; 
	    	 list = jdbcTemplate.queryForList(sqlComPageList,new Object[]{strComID,numStart,numLimit});
	     }
	     
	     if(list != null){
	    	 for(int i = 0; i<list.size();i++){
	    		 strReadonly = "false";
	    		 strModify = "false";
	    		 strPageID = (String) list.get(i).get("TZ_PAGE_ID");
	    		 strPageName = (String) list.get(i).get("TZ_PAGE_MC");
	    		 String sql = "SELECT DISPLAYONLY,TZ_EDIT_FLG FROM PS_TZ_AQ_COMSQ_TBL WHERE CLASSID=? AND TZ_COM_ID=? AND TZ_PAGE_ID=?";
	    		 Map<String, Object> map =jdbcTemplate.queryForMap(sql,new Object[]{strPermID,strComID,strPageID});
	    		 if(map != null){
	    			 numReadonly = (int) map.get("DISPLAYONLY");
	    			 numModify = (int) map.get("TZ_EDIT_FLG");
	    			 if(1 ==numReadonly ){
	    				 strReadonly = "true";
	    			 }else{
	    				 strReadonly = "false";
	    			 }
	    			 
	    			 if(1 == numModify ){
	    				 strModify = "true";
	    			 }else{
	    				 strModify = "false";
	    			 }
	    		 }
	    		 strComContent = strComContent + ",{\"permID\":\""+strPermID+"\",\"comID\":\""+strComID+"\",\"pageID\":\""+strPageID+"\",\"pageName\":\""+strPageName+"\",\"readonly\":"+strReadonly+",\"modify\":"+strModify+"}";
	    	 }
	    	 if(!"".equals(strComContent)){
	    		 strComContent = strComContent.substring(1);
	    	 }
	     }
	     
	     // 获取授权组件页面信息总数;
	     String totalsql = "SELECT COUNT(1)  FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=?";
	     int numTotal = jdbcTemplate.queryForObject(totalsql, new Object[]{strComID},"Integer");
	     strRet = "{\"total\":"+numTotal+",\"root\":["+strComContent+"]}";
		 return strRet;
		
	}
	
	
	/* 修改许可权信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// 信息内容;
				String infoData = CLASSJson.getString("data");
				JSONObject Json = PaseJsonUtil.getJson(infoData);
				// 许可权编号;
			    String strPermID = Json.getString("permID");
			    // 授权组件编号;
			    String strComID = Json.getString("comID");
			    // 页面编号，显示，修改;
			    String strPageID, strReadonly, strModify;
			    strPageID = Json.getString("pageID");
			    strReadonly = Json.getString("readonly");
			    strModify = Json.getString("modify");
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
