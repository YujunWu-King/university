package com.tranzvision.gd.TZAccountMgBundle.service.impl;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.PaseJsonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 安全管理-用户账号管理列表类
 * 原PS类：TZ_GD_YHZHGL_PKG:TZ_GD_YHZHLB_CLS
 * @author tang
 * @version 1.0, 2015/10/19
 */
@Service("com.tranzvision.gd.TZAccountMgBundle.service.impl.AccountListImpl")
public class AccountListImpl extends FrameworkImpl {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*获取用户账号信息列表*/
	@Override
	public String tzQueryList(String comParams,int numLimit, int numStart, String[] errorMsg){
		// 返回值;
		String strRet = "";
		FliterForm fliterForm = new FliterForm();
				
		//排序字段如果没有不要赋值
		//String[][] orderByArr = new String[][]{{"TZ_COM_ID","ASC"}};
		//fliterForm.orderByArr = orderByArr;
				
		//json数据要的结果字段;
		String[] resultFldArray = { "TZ_DLZH_ID", "TZ_JG_ID", "OPRID", "TZ_REALNAME", "TZ_EMAIL", "TZ_MOBILE", "TZ_JIHUO_ZT_DESC", "TZ_JIHUO_FS_DESC", "TZ_RYLX", "ACCTLOCK"};
		String jsonString = "";
				
		//可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, comParams, numLimit,numStart, errorMsg);
				
		if (obj == null || obj.length == 0 ) {
			strRet = "{\"total\":0,\"root\":[]}";
		} else {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				jsonString = jsonString + ",{\"usAccNum\":\""+rowList[0]+"\",\"orgId\":\""+rowList[1]+"\",\"oprid\":\""+rowList[2]+"\",\"usName\":\""+rowList[3]+"\",\"email\":\""+rowList[4]+"\",\"mobile\":\""+rowList[5]+"\",\"jhState\":\""+rowList[6]+"\",\"jhMethod\":\""+rowList[7]+"\",\"rylx\": \""+rowList[8]+"\",\"acctLock\":"+rowList[9]+"}";
			}
			if(!"".equals(jsonString)){
				jsonString = jsonString.substring(1);
			}
					
			strRet = "{\"total\":"+obj[0]+",\"root\":[" + jsonString + "]}";
		}
	
		return strRet;
	}
	
	/*删除用户账号信息*/
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String oprID = "";
				// 用户账号信息;
				String strUserInfo = actData[num];
				// 将字符串转换成json;
				JSONObject CLASSJson = PaseJsonUtil.getJson(strUserInfo);
				// 登录账号;
				String usAccNum = CLASSJson.getString("usAccNum");
				// 机构编号;
				String orgId = CLASSJson.getString("orgId");
				if(usAccNum != null && !"".endsWith(usAccNum) && orgId != null && !"".endsWith(orgId)){
					
					/*获取用户ID*/
					String selectSql = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
					try{
						oprID = jdbcTemplate.queryForObject(selectSql, new Object[]{usAccNum,orgId},String.class);
					}catch(DataAccessException e){
						
					}
				    if(oprID != null && !"".equals(oprID)){
				    	//删除照片信息；
				    	String photoSQL1 = "DELETE FROM PS_TZ_OPR_PHOTO_T WHERE TZ_ATTACHSYSFILENA IN (SELECT TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T WHERE OPRID=?)";
				    	try{
							jdbcTemplate.update(photoSQL1, oprID);
						}catch(DataAccessException e){
							
						}
				    	
				    	String photoSQL2 = "DELETE FROM PS_TZ_OPR_PHT_GL_T WHERE OPRID=?";
				    	try{
							jdbcTemplate.update(photoSQL2, oprID);
						}catch(DataAccessException e){
							
						}
				    	
				    	//删除会员用户的注册信息;
				    	String deleteHYSQL = "DELETE FROM PS_TZ_REG_USER_T WHERE OPRID=?";
				    	try{
							jdbcTemplate.update(deleteHYSQL, oprID);
						}catch(DataAccessException e){
							
						}
				    	
				    	//删除会员用户的注册信息;
				    	String deleteLXFSSQL = "DELETE FROM PS_TZ_LXFSINFO_TBL WHERE ( TZ_LXFS_LY='ZCYH' OR  TZ_LXFS_LY='NBYH') AND TZ_LYDX_ID=?";
				    	try{
							jdbcTemplate.update(deleteLXFSSQL, oprID);
						}catch(DataAccessException e){
							
						}
				    	
				    	//删除用户角色;
				    	String deleteROLESQL = "DELETE FROM PSROLEUSER WHERE ROLEUSER=?";
				    	try{
							jdbcTemplate.update(deleteROLESQL, oprID);
						}catch(DataAccessException e){
							
						}
				    	
				    	//删除用户信息记录信息;
				    	String deleteYHXXSQL = "DELETE FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
				    	try{
							jdbcTemplate.update(deleteYHXXSQL, usAccNum,orgId);
						}catch(DataAccessException e){
							
						}
				    	
				    	//删除用户;
				    	String deleteOPDSQL = "DELETE FROM PSOPRDEFN WHERE OPRID=?";
				    	try{
							jdbcTemplate.update(deleteOPDSQL, oprID);
						}catch(DataAccessException e){
							
						}
				    
				    }
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	
	@Override
	public String tzOther(String oprType, String comParams, String[] errorMsg) {
		String strRet = "{}";

		try {

			// 将字符串转换成json;
			JSONObject CLASSJson = PaseJsonUtil.getJson(comParams);

			JSONArray jsonArray = CLASSJson.getJSONArray("data");
			
			for (int num = 0; num < jsonArray.size(); num++) {
				String strJson = jsonArray.getString(num);
				JSONObject dataJson = PaseJsonUtil.getJson(strJson);
				// 登录账号;
				String usAccNum = dataJson.getString("usAccNum");
				// 机构编号;
				String orgID = dataJson.getString("orgId");
				
				if(usAccNum != null && !"".equals(usAccNum) && orgID != null && !"".equals(orgID)){
					String oprID = "";
					String isExistSQL = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
					try{
						oprID = jdbcTemplate.queryForObject(isExistSQL, new Object[]{usAccNum,orgID}, String.class);
					}catch(DataAccessException e){
						
					}
					if (oprID == null || "".equals(oprID)) {
						errorMsg[0] = "1";
						errorMsg[1] = "机构编号为：" + orgID + "，登录账号为：" + usAccNum
								+ "的信息不存在，更新失败。";
						return strRet;
					}

					
					// 重置密码;
					if ("PWD".equals(oprType)) {
						String password = CLASSJson.getString("password");
						if(password != null && !"".equals(password)){
							String updatePSOPRDEFNSQL = "update PSOPRDEFN set OPERPSWD = ?, LASTUPDDTTM =curdate(),LASTUPDOPRID = ? where OPRID=?";
							try{
								password = DESUtil.encrypt(password,"TZGD_Tranzvision");
								/**** TODO %OPRID *****/
								jdbcTemplate.update(updatePSOPRDEFNSQL,password,"TZ_7",oprID);
							}catch(DataAccessException e){
								
							}

						}
					}

					// 锁定账号;
					if ("LOCK".equals(oprType)) {
						String updatePSOPRDEFNSQL = "update PSOPRDEFN set ACCTLOCK = ?, LASTUPDDTTM =curdate(),LASTUPDOPRID = ? where OPRID=?";
						try{
							/**** TODO %OPRID *****/
							jdbcTemplate.update(updatePSOPRDEFNSQL,1,"TZ_7",oprID);
						}catch(DataAccessException e){
							
						}
					}
				}else{
					errorMsg[0] = "1";
					errorMsg[1] = "未选中账号";
					return strRet;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return strRet;
	}
}
