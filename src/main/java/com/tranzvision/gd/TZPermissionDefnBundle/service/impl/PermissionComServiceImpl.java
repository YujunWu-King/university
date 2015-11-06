package com.tranzvision.gd.TZPermissionDefnBundle.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPermissionDefnBundle.dao.PsTzAqComsqTblMapper;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsTzAqComsqTbl;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsTzAqComsqTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * @author tang
 * 许可权组件定义，原PS类：TZ_GD_PLST_PKG:TZ_GD_PERMCOMP_CLS
 */
@Service("com.tranzvision.gd.TZPermissionDefnBundle.service.impl.PermissionComServiceImpl")
public class PermissionComServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzAqComsqTblMapper psTzAqComsqTblMapper;
	@Autowired
	private JacksonUtil jacksonUtil;
	
	/*获取组件授权页面列表*/
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		 // 返回值;
		 String strRet = "{}";
		 // 授权组件页面列表内容;
		 String strComContent = "";
		 
	     // 将字符串转换成json;
	     jacksonUtil.json2Map(comParams);
	     // 许可权编号;
	     String strPermID = jacksonUtil.getString("permID");
	     // 组件ID;
	     String strComID = jacksonUtil.getString("comID");
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
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				// 许可权编号;
			    String strPermID = (String) infoData.get("permID");
			    // 授权组件编号;
			    String strComID = (String) infoData.get("comID");
			    // 页面编号，显示，修改;
			    String strPageID;
			    strPageID = (String) infoData.get("pageID");
			    boolean readonly = (boolean) infoData.get("readonly");
			    boolean modify = (boolean) infoData.get("modify");
			    
			    Short numReadonly, numModify;
			    if(readonly == true){
			    	numReadonly = 1;
			    }else{
			    	numReadonly = 0;
			    }
			    
			    if(modify == true){
			    	numModify = 1;
			    }else{
			    	numModify = 0;
			    }
			      
			    
			    PsTzAqComsqTblKey psTzAqComsqTblKey = new PsTzAqComsqTblKey();
			    psTzAqComsqTblKey.setClassid(strPermID);
			    psTzAqComsqTblKey.setTzComId(strComID);
			    psTzAqComsqTblKey.setTzPageId(strPageID);
			    PsTzAqComsqTbl psTzAqComsqTbl = psTzAqComsqTblMapper.selectByPrimaryKey(psTzAqComsqTblKey);
			    if(psTzAqComsqTbl != null){
			    	psTzAqComsqTbl.setDisplayonly(numReadonly);
			    	psTzAqComsqTbl.setTzEditFlg(numModify);
			    	psTzAqComsqTbl.setRowLastmantDttm(new Date());
			    	/*****TODO %USERID****/
			    	psTzAqComsqTbl.setRowLastmantOprid("TZ_7");
			    	psTzAqComsqTblMapper.updateByPrimaryKeySelective(psTzAqComsqTbl);
			    }else{
			    	psTzAqComsqTbl = new PsTzAqComsqTbl();
			    	psTzAqComsqTbl.setClassid(strPermID);;
			    	psTzAqComsqTbl.setTzComId(strComID);
			    	psTzAqComsqTbl.setTzPageId(strPageID);
			    	psTzAqComsqTbl.setDisplayonly(numReadonly);
			    	psTzAqComsqTbl.setTzEditFlg(numModify);
			    	psTzAqComsqTbl.setRowAddedDttm(new Date());
			    	psTzAqComsqTbl.setRowLastmantDttm(new Date());
			    	/*****TODO %USERID****/
			    	psTzAqComsqTbl.setRowAddedOprid("TZ_7");
			    	psTzAqComsqTbl.setRowLastmantOprid("TZ_7");
			    	psTzAqComsqTblMapper.insert(psTzAqComsqTbl);
			    }
			} 
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
