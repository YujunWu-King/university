package com.tranzvision.gd.TZComRegMgBundle.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.PaseJsonUtil;
import com.tranzvision.gd.util.base.TZUtility;

import net.sf.json.JSONObject;

/**
 * @author tang
 * @version 1.0, 2015-10-9
 * @功能：功能组件注册管理相关类 原ps类：TZ_GD_COMREGMG_PKG:TZ_GD_COMREG_CLS
 */
@Service("com.tranzvision.gd.TZComRegMgBundle.service.impl.ComRegImpl")
public class ComRegImpl extends FrameworkImpl {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*新增组件注册信息*/
	@Override
	public String tzAdd(String[] actData, String[] errMsg){
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		
		try{
			int num = 0;
			for(num = 0; num < actData.length; num ++){
				//表单内容;
			    String strForm = actData [num];
			    //将字符串转换成json;
			    JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
			    // 类型标志;
			    String strFlag = CLASSJson.getString("typeFlag");
			    // 信息内容;
			    String infoData = CLASSJson.getString("data");
			    if("COM".equals(strFlag)){
			    	// 将字符串转换成json;
			        JSONObject CLASSJson1 = PaseJsonUtil.getJson(infoData);
			        // 组件编号;
			        String strComID = CLASSJson1.getString("comID");
			        // 组件名称;
			        String comName = CLASSJson1.getString("comName");
			        
			        //是否已经存在;
			        String comExistSql = "SELECT 'Y' FROM PS_TZ_AQ_COMZC_TBL WHERE TZ_COM_ID=?";
			        String isExist = "";
			        try{
			        	isExist = jdbcTemplate.queryForObject(comExistSql, new Object[]{strComID},String.class);
			        }catch(DataAccessException e){
			        	
			        }
					
					if("Y".equals(isExist)){
						errMsg[0] = "1";
						errMsg[1] = "组件ID为：" + strComID + "的信息已经注册，请修改组件ID。";
						return strRet;
					}
			        
			        String comInsertSql = "INSERT INTO PS_TZ_AQ_COMZC_TBL(TZ_COM_ID,TZ_COM_MC, ROW_ADDED_DTTM, ROW_ADDED_OPRID,ROW_LASTMANT_DTTM,ROW_LASTMANT_OPRID) VALUES(?,?,current_timestamp(),?,current_timestamp(),?)";
			        /******TODO, 应该是当前登录人员 %userid****/
			        try{
				        int i = jdbcTemplate.update(comInsertSql,strComID,comName,"TZ_7","TZ_7");
						if(i == 0){
							errMsg[0] = "1";
							errMsg[1] = "添加失败";
							return strRet;
						}
			        }catch(DataAccessException e){
			        	e.printStackTrace();
			        	errMsg[0] = "1";
						errMsg[1] = "添加失败";
						return strRet;
			        }
			    }
			    
			    
			    if("PAGE".equals(strFlag)){
			    	strRet = this.tzUpdatePageInfo(infoData, errMsg);
			    }
			    
			}
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}
		
		return strRet;
	}
	
	/*修改组件注册信息*/
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		
		try{
			int num = 0;
			for(num = 0; num < actData.length; num ++){
				//表单内容;
			    String strForm = actData [num];
			    //将字符串转换成json;
			    JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
			    // 类型标志;
			    String strFlag = CLASSJson.getString("typeFlag");
			    // 信息内容;
			    String infoData = CLASSJson.getString("data");
			    if("COM".equals(strFlag)){
			    	// 将字符串转换成json;
			        JSONObject CLASSJson1 = PaseJsonUtil.getJson(infoData);
			        // 组件编号;
			        String strComID = CLASSJson1.getString("comID");
			        // 组件名称;
			        String comName = CLASSJson1.getString("comName");
			        
			        String comUpdateSql = "UPDATE PS_TZ_AQ_COMZC_TBL SET TZ_COM_MC=?,ROW_LASTMANT_DTTM=current_timestamp(),ROW_LASTMANT_OPRID=? WHERE TZ_COM_ID=?";
					try{
						/******TODO, 应该是当前登录人员 %userid****/
						int i = jdbcTemplate.update(comUpdateSql,comName,"TZ_7",strComID);
						if(i == 0){
							errMsg[0] = "1";
							errMsg[1] = "更新失败";
							return strRet;
						}
					}catch(DataAccessException e){
						errMsg[0] = "1";
						errMsg[1] = "更新失败";
						return strRet;
					}
			    }
			    
			    if("PAGE".equals(strFlag)){
			    	strRet = this.tzUpdatePageInfo(infoData, errMsg);
			    }
			    
			}
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}
		
		return strRet;
	}
	

	/* 获取组件注册信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		try {
			JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
			if (CLASSJson.containsKey("comID")) {
				String strComID = CLASSJson.getString("comID");
				if (strComID != null && !"".equals(strComID)) {
					String sql = "select TZ_COM_MC from PS_TZ_AQ_COMZC_TBL where TZ_COM_ID=?";
					String comMc = "";
					try{
						comMc = jdbcTemplate.queryForObject(sql, new Object[] { strComID },String.class);
						// 组件注册信息;
						strRet = "{\"formData\":{\"comID\":\"" + TZUtility.transFormchar(strComID) + "\",\"comName\":\""
								+ TZUtility.transFormchar(comMc) + "\"}}";
					}catch(DataAccessException e){
						e.printStackTrace();
						errMsg[0] = "1";
						errMsg[1] = "无法获取组件信息";
					}


				} else {
					errMsg[0] = "1";
					errMsg[1] = "无法获取组件信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 获取页面注册信息列表 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 页面注册信息列表内容;
		String strPageContent = "";
		// 页面注册信息总数;
		int numTotal = 0;

		try {
			JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);

			String strComID = CLASSJson.getString("comID");
			if (strComID != null && !"".equals(strComID)) {

				// 页面ID，页面名称，是否默认首页;
			    String strPageID = "", strPageName = "", isDefault = "";
				// 序号;
				int numOrder = 0;
				// 页面注册信息列表sql;
				String sqlPageList = "";
				/******* 查询组件下的页面列表 ****/
				Object[] obj = null;
				if (numLimit == 0) {
					sqlPageList = "SELECT TZ_PAGE_ID,TZ_PAGE_MC,TZ_PAGE_XH,TZ_PAGE_MRSY FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? ORDER BY TZ_PAGE_XH";
					obj = new Object[] { strComID };
				} else {
					sqlPageList = "SELECT TZ_PAGE_ID,TZ_PAGE_MC,TZ_PAGE_XH,TZ_PAGE_MRSY FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? ORDER BY TZ_PAGE_XH limit ?,?";
					obj = new Object[] { strComID, numStart, numLimit };
				}

				try {
					List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlPageList, obj);
					for (int i = 0; i < list.size(); i++) {
						isDefault = (String) list.get(i).get("TZ_PAGE_MRSY");
						if ("Y".equals(isDefault)) {
							isDefault = "true";
						} else {
							isDefault = "false";
						}
					    strPageID =(String) list.get(i).get("TZ_PAGE_ID");
					    strPageName = (String) list.get(i).get("TZ_PAGE_MC");
					    numOrder = (int) list.get(i).get("TZ_PAGE_XH");
				
					    strPageContent = strPageContent + ",{\"comID\":\"" + TZUtility.transFormchar(strComID) + "\",\"pageID\":\""
								+ TZUtility.transFormchar(strPageID) + "\",\"pageName\":\""
								+ TZUtility.transFormchar(strPageName) + "\",\"orderNum\":" + numOrder
								+ ",\"isDefault\":" + isDefault + "}";
					}
					if(!"".equals(strPageContent)){
						strPageContent = strPageContent.substring(1);
					}

					/******* 查询组件总的页面数 ****/
					String totalSQL = "SELECT COUNT(1) FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=?";
					numTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { strComID }, Integer.class);

					strRet = "{\"total\":" + numTotal + ",\"root\":[" + strPageContent + "]}";

				} catch (Exception e) {
					e.printStackTrace();
					strPageContent = "";
					errMsg[0] = "1";
					errMsg[1] = "无法获取组件页面信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件页面信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/*删除页面注册信息列表*/
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		
		try{
			int num = 0;
			for(num = 0; num < actData.length; num ++){
				//表单内容;
			    String strForm = actData [num];
			    //将字符串转换成json;
			    JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
			    // 组件ID;
			    String sComID = CLASSJson.getString("comID");
			    // 页面ID;
			    String sPageID = CLASSJson.getString("pageID");
			    
			    String comDeleteSql = "DELETE FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";
			    try{
			    	int i = jdbcTemplate.update(comDeleteSql,sComID,sPageID);
			    	if(i == 0){
						errMsg[0] = "1";
						errMsg[1] = "删除页面："+ sPageID +"失败";
						return strRet;
					}
			    }catch(DataAccessException e){
			    	errMsg[0] = "1";
					errMsg[1] = "删除页面："+ sPageID +"失败";
					return strRet;
			    }
			}
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}
		
		return strRet;
	}
	
	private String tzUpdatePageInfo(String strForm, String[] errMsg){
		//返回值;
		String strRet = "{}";
		try{
	    	// 将字符串转换成json;
	        JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
	        // 组件编号;
	        String strComID = CLASSJson.getString("comID");
	        // 组件名称;
	        String strPageID = CLASSJson.getString("pageID");
	        // 序号;
	        int numOrder = CLASSJson.getInt("orderNum");
	        // 默认首页;
	        String isDefault = CLASSJson.getString("isDefault");
	        if("true".equals(isDefault)){
	        	isDefault = "Y";
	        }else{
	        	isDefault = "N";
	        }
	        
	        if("Y".equals(isDefault)){
	        	// 默认首页只能有一个;
	            String updateDefalutNoSQL = "UPDATE PS_TZ_AQ_PAGZC_TBL SET TZ_PAGE_MRSY='N' WHERE TZ_COM_ID=?";
	            try{
	            	jdbcTemplate.update(updateDefalutNoSQL,strComID);
	            }catch(DataAccessException e){
	            	
	            }
	        }
	        
	        //页面注册信息表;
			String pageUpdateSql = "UPDATE PS_TZ_AQ_PAGZC_TBL SET TZ_PAGE_XH=?,TZ_PAGE_MRSY=? WHERE TZ_COM_ID=? AND TZ_PAGE_ID=?";
			try{
				int i = jdbcTemplate.update(pageUpdateSql,numOrder,isDefault,strComID,strPageID);
				if(i == 0){
					errMsg[0] = "1";
					errMsg[1] = "组件ID为：" + strComID + "，页面ID为：" + strPageID + "的信息不存在。";
				}
			}catch(DataAccessException e){
				errMsg[0] = "1";
				errMsg[1] = "组件ID为：" + strComID + "，页面ID为：" + strPageID + "的信息不存在。";
            }
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
