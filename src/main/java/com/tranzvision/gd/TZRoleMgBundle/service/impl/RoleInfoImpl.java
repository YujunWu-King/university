package com.tranzvision.gd.TZRoleMgBundle.service.impl;

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
 * 角色信息相关类 原PS类：TZ_GD_ROLE_PKG:TZ_GD_ROLEINFO_CLS
 * 
 * @author tang
 * @version 1.0, 2015/10/19
 */
@Service("com.tranzvision.gd.TZRoleMgBundle.service.impl.RoleInfoImpl")
public class RoleInfoImpl extends FrameworkImpl {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/* 新增用户账号信息 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// 类型标志;
				String strFlag = CLASSJson.getString("typeFlag");
				// 信息内容;
				String infoData = CLASSJson.getString("data");
				// 角色信息;
				if ("ROLE".equals(strFlag)) {
					CLASSJson = PaseJsonUtil.getJson(infoData);
					// 角色名称;
					String strRoleName = CLASSJson.getString("roleName");
					// 角色描述;
					String strRoleDesc = CLASSJson.getString("roleDesc");
					// 角色详细描述;
					String strRoleDescLong = CLASSJson
							.getString("roleDescLong");

					// 查看是否已经存在;
					int isExistNum = 0;
					String isExistSQL = "SELECT COUNT(1) FROM PSROLEDEFN WHERE ROLENAME=?";
					try{
						isExistNum = jdbcTemplate.queryForObject(isExistSQL,new Object[]{strRoleName},Integer.class);
					}catch(DataAccessException e){
						
					}
					if (isExistNum > 0) {
						errMsg[0] = "1";
						errMsg[1] = "角色名称为：" + strRoleName + "的信息已经存在，请修改角色名称。";
						return strRet;
					}
					
					
					String insertRoleSQL = "INSERT INTO PSROLEDEFN(ROLENAME,DESCR,DESCRLONG,VERSION,ROLETYPE,ROLESTATUS,ROLE_PCODE_RULE_ON,ROLE_QUERY_RULE_ON,LDAP_RULE_ON,ALLOWNOTIFY,ALLOWLOOKUP,LASTUPDDTTM,LASTUPDOPRID) VALUES(?,?,?,1,'U','A','N','N','N','Y','Y',curdate(),?)";
					try{
						/** TODO %USERID ***/
						jdbcTemplate.update(insertRoleSQL,strRoleName,strRoleDesc,strRoleDescLong,"TZ_7");
					}catch(DataAccessException e){
						
					}
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 修改组件注册信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// 类型标志;
				String strFlag = CLASSJson.getString("typeFlag");
				// 信息内容;
				String infoData = CLASSJson.getString("data");
				// 角色信息;
				if ("ROLE".equals(strFlag)) {
					CLASSJson = PaseJsonUtil.getJson(infoData);
					// 角色名称;
					String strRoleName = CLASSJson.getString("roleName");
					// 角色描述;
					String strRoleDesc = CLASSJson.getString("roleDesc");
					// 角色详细描述;
					String strRoleDescLong = CLASSJson
							.getString("roleDescLong");

					// 查看是否已经存在;
					int isExistNum = 0;
					String isExistSQL = "SELECT count(1) FROM PSROLEDEFN WHERE ROLENAME=?";
					try{
						isExistNum = jdbcTemplate.queryForObject(isExistSQL,new Object[]{strRoleName},Integer.class);
					}catch(DataAccessException e){
						
					}
					
					if (isExistNum <= 0) {
						errMsg[0] = "1";
						errMsg[1] = "角色名称为：" + strRoleName + "的信息不存在，更新失败。";
						return strRet;
					}

					String updateRoleSQL = "UPDATE PSROLEDEFN SET DESCR=?,DESCRLONG=?,LASTUPDDTTM=curdate(),LASTUPDOPRID=? WHERE ROLENAME=?";
					try{
						/** TODO %USERID ***/
						jdbcTemplate.update(updateRoleSQL,strRoleDesc,strRoleDescLong,"TZ_7",strRoleName);
					}catch(DataAccessException e){
						
					}
				}
				
				// 许可权信息;
				if("PLST".equals(strFlag)){
					CLASSJson = PaseJsonUtil.getJson(infoData);
					// 角色名称;
					String strRoleName = CLASSJson.getString("roleName");
					// 许可权编号;
					String strPermID = CLASSJson.getString("permID");
					// 查看是否已经存在;
					int isExistNum = 0;
					String isExistSQL = "SELECT COUNT(1) from PSROLECLASS where ROLENAME=? and CLASSID=?";
					try{
						isExistNum = jdbcTemplate.queryForObject(isExistSQL,new Object[]{strRoleName,strPermID},Integer.class);
					}catch(DataAccessException e){
						
					}
					
					if(isExistNum > 0){
						continue;
					}else{
						String insertSQL = "INSERT INTO PSROLECLASS(ROLENAME, CLASSID) VALUES (?,?)";
						try{
							jdbcTemplate.update(insertSQL,strRoleName,strPermID);
						}catch(DataAccessException e){
							
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 获取角色信息 */
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);

			if (CLASSJson.containsKey("roleName")) {
				// 角色名称, 角色描述，详细信息;
				String strRoleName = "", strRoleDesc = "", strRoleDescLong = "";
				strRoleName = CLASSJson.getString("roleName");

				String sql = "SELECT DESCR,DESCRLONG FROM PSROLEDEFN WHERE ROLENAME=?";
				try{
					Map<String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{strRoleName});
					strRoleDesc = TZUtility
							.transFormchar((String)map.get("DESCR")).trim();
					strRoleDescLong = TZUtility.transFormchar(
							(String)map.get("DESCRLONG")).trim();
				}catch(DataAccessException e){
					
				}

				strRet = "{\"formData\":{\"roleName\":\"" + strRoleName
						+ "\",\"roleDesc\":\"" + strRoleDesc
						+ "\",\"roleDescLong\":\"" + strRoleDescLong + "\"}}";

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择角色";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 获取许可权列表 */
	public String tzQueryList(String comParams, int numLimit, int numStart,
			String[] errorMsg) {
		// 返回值;
		String strRet = "";
		int numTotal = 0;
		
		try {
	
			// 将字符串转换成json;
			JSONObject CLASSJson = PaseJsonUtil.getJson(comParams);
			// 角色名称;
			String strRoleName = CLASSJson.getString("roleName");
			// 许可权编号，许可权描述;
		    String strPermID = "", strPermDesc = "";
		      
		    // 许可权列表sql;
		    String sqlPlstList = "";
		    
		    List<Map<String, Object>> list = null;
		    if(numStart == 0 && numLimit==0){
		    	sqlPlstList = "SELECT A.CLASSID,(SELECT CLASSDEFNDESC FROM PSCLASSDEFN WHERE CLASSID=A.CLASSID) CLASSDEFNDESC FROM PSROLECLASS A WHERE A.ROLENAME=? ORDER BY A.CLASSID";
		    	try{
		    		list = jdbcTemplate.queryForList(sqlPlstList,new Object[]{strRoleName});
					
				}catch(DataAccessException e){
					
				}
		    }else{
		    	sqlPlstList = "SELECT A.CLASSID,(SELECT CLASSDEFNDESC FROM PSCLASSDEFN WHERE CLASSID=A.CLASSID) CLASSDEFNDESC FROM PSROLECLASS A WHERE A.ROLENAME=? ORDER BY A.CLASSID limit ?,?";
		    	try{
		    		list = jdbcTemplate.queryForList(sqlPlstList,new Object[]{strRoleName,numStart,numLimit});
					
				}catch(DataAccessException e){
					
				}
		    }
		    for(int i = 0; i<list.size();i++){
		    	strPermID  = (String) list.get(i).get("CLASSID");
		    	strPermDesc = (String) list.get(i).get("CLASSDEFNDESC");
		    	strRet = strRet + "," + "{\"roleName\":\""+strRoleName+"\",\"permID\":\""+strPermID+"\",\"permDesc\":\""+strPermDesc+"\"}";
		    }
			
		    if(!"".equals(strRet)){
		    	strRet = strRet.substring(1);
		    }
		    
		    //获取许可权信息总数;
		    String totalSQL = "SELECT COUNT(1) FROM PSROLECLASS WHERE ROLENAME=?";
		    try{
		    	numTotal = jdbcTemplate.queryForObject(totalSQL,new Object[]{strRoleName},Integer.class);
				
			}catch(DataAccessException e){
				
			}
	    	
	    	strRet = "{\"total\":"+numTotal+",\"root\":["+strRet+"]}";
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			strRet = "{\"total\":0,\"root\":[]}";
		}
		return strRet;
	}
	
	/*删除角色许可权信息*/
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
				
				// 角色下权限信息;
				String strRoleInfo = actData[num];
				
				JSONObject CLASSJson = PaseJsonUtil.getJson(strRoleInfo);
				// 角色名称ID;
				String sRoleName = CLASSJson.getString("roleName");
			    // 许可权编号;
			    String sPermID = CLASSJson.getString("permID");

				if (sRoleName != null && !"".equals(sRoleName) && (sPermID != null && !"".equals(sPermID))) {

					/* 删除角色下的权限 */
					String PSROLECLASSSQL = "DELETE FROM PSROLECLASS WHERE ROLENAME=? AND CLASSID=?";
					try{
				    	jdbcTemplate.update(PSROLECLASSSQL,sRoleName,sPermID);
					}catch(DataAccessException e){
						
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
}
