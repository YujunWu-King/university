package com.tranzvision.gd.TZRoleMgBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZRoleMgBundle.dao.PsRoleclassMapper;
import com.tranzvision.gd.TZRoleMgBundle.dao.PsRoledefnMapper;
import com.tranzvision.gd.TZRoleMgBundle.model.PsRoleclassKey;
import com.tranzvision.gd.TZRoleMgBundle.model.PsRoledefn;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 角色信息相关类 原PS类：TZ_GD_ROLE_PKG:TZ_GD_ROLEINFO_CLS
 * 
 * @author tang
 * @version 1.0, 2015/10/19
 */
@Service("com.tranzvision.gd.TZRoleMgBundle.service.impl.RoleInfoImpl")
public class RoleInfoImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsRoledefnMapper psRoledefnMapper;
	@Autowired
	private PsRoleclassMapper psRoleclassMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;

	/* 新增用户账号信息 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				// 角色信息;
				if ("ROLE".equals(strFlag)) {
					// 角色名称;
					String strRoleName = (String) infoData.get("roleName");
					// 角色描述;
					String strRoleDesc = (String) infoData.get("roleDesc");
					// 角色详细描述;
					String strRoleDescLong = (String) infoData.get("roleDescLong");

					// 查看是否已经存在;
					int isExistNum = 0;
					String isExistSQL = "SELECT COUNT(1) FROM PSROLEDEFN WHERE ROLENAME=?";
					isExistNum = jdbcTemplate.queryForObject(isExistSQL, new Object[] { strRoleName }, "Integer");

					if (isExistNum > 0) {
						errMsg[0] = "1";
						errMsg[1] = "角色名称为：" + strRoleName + "的信息已经存在，请修改角色名称。";
						return strRet;
					}

					PsRoledefn psRoledefn = new PsRoledefn();
					psRoledefn.setRolename(strRoleName);
					psRoledefn.setDescr(strRoleDesc);
					psRoledefn.setDescrlong(strRoleDescLong);
					psRoledefn.setVersion(1);
					psRoledefn.setRoletype("U");
					psRoledefn.setRolestatus("A");
					psRoledefn.setRolePcodeRuleOn("N");
					psRoledefn.setRoleQueryRuleOn("N");
					psRoledefn.setLdapRuleOn("N");
					psRoledefn.setAllownotify("Y");
					psRoledefn.setAllowlookup("Y");
					psRoledefn.setLastupddttm(new Date());
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psRoledefn.setLastupdoprid(oprid);
					int i = psRoledefnMapper.insert(psRoledefn);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
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
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				// 角色信息;
				if ("ROLE".equals(strFlag)) {
					// 角色名称;
					String strRoleName = (String) infoData.get("roleName");
					// 角色描述;
					String strRoleDesc = (String) infoData.get("roleDesc");
					// 角色详细描述;
					String strRoleDescLong = (String) infoData.get("roleDescLong");

					// 查看是否已经存在;
					int isExistNum = 0;
					String isExistSQL = "SELECT COUNT(1) FROM PSROLEDEFN WHERE ROLENAME=?";
					isExistNum = jdbcTemplate.queryForObject(isExistSQL, new Object[] { strRoleName }, "Integer");

					if (isExistNum <= 0) {
						errMsg[0] = "1";
						errMsg[1] = "角色名称为：" + strRoleName + "的信息不存在。";
						return strRet;
					}

					PsRoledefn psRoledefn = new PsRoledefn();
					psRoledefn.setRolename(strRoleName);
					psRoledefn.setDescr(strRoleDesc);
					psRoledefn.setDescrlong(strRoleDescLong);
					psRoledefn.setVersion(1);
					psRoledefn.setRoletype("U");
					psRoledefn.setRolestatus("A");
					psRoledefn.setRolePcodeRuleOn("N");
					psRoledefn.setRoleQueryRuleOn("N");
					psRoledefn.setLdapRuleOn("N");
					psRoledefn.setAllownotify("Y");
					psRoledefn.setAllowlookup("Y");
					psRoledefn.setLastupddttm(new Date());
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psRoledefn.setLastupdoprid(oprid);
					int i = psRoledefnMapper.updateByPrimaryKeyWithBLOBs(psRoledefn);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
					}
				}

				// 许可权信息;
				if ("PLST".equals(strFlag)) {
					// 角色名称;
					String strRoleName = (String) infoData.get("roleName");
					// 许可权编号;
					String strPermID = (String) infoData.get("permID");
					// 查看是否已经存在;
					int isExistNum = 0;
					String isExistSQL = "SELECT COUNT(1) from PSROLECLASS where ROLENAME=? and CLASSID=?";
					isExistNum = jdbcTemplate.queryForObject(isExistSQL, new Object[] { strRoleName, strPermID },
							"Integer");

					if (isExistNum > 0) {
						continue;
					} else {
						PsRoleclassKey psRoleclassKey = new PsRoleclassKey();
						psRoleclassKey.setRolename(strRoleName);
						psRoleclassKey.setClassid(strPermID);
						psRoleclassMapper.insert(psRoleclassKey);
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
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();		
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("roleName")) {
				// 角色名称, 角色描述，详细信息;
				String strRoleName = "";
				strRoleName = jacksonUtil.getString("roleName");

				PsRoledefn psRoledefn = psRoledefnMapper.selectByPrimaryKey(strRoleName);
				if (psRoledefn != null) {
					// 角色信息;
					Map<String, Object> jsonMap = new HashMap<>();
					jsonMap.put("roleName", strRoleName);
					jsonMap.put("roleDesc", psRoledefn.getDescr());
					jsonMap.put("roleDescLong", psRoledefn.getDescrlong());
					returnJsonMap.replace("formData", jsonMap);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "请选择角色";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择角色";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 获取许可权列表 */
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 将字符串转换成json;
			jacksonUtil.json2Map(comParams);
			// 角色名称;
			String strRoleName = jacksonUtil.getString("roleName");

			// 许可权列表sql;
			String sqlPlstList = "";
			String totalSQL = "";
			List<Map<String, Object>> list = null;
			if (numStart == 0 && numLimit == 0) {
				sqlPlstList = "SELECT A.CLASSID,(SELECT CLASSDEFNDESC FROM PSCLASSDEFN WHERE CLASSID=A.CLASSID) CLASSDEFNDESC FROM PSROLECLASS A WHERE A.ROLENAME=? ORDER BY A.CLASSID";
				list = jdbcTemplate.queryForList(sqlPlstList, new Object[] { strRoleName });

			} else {
				sqlPlstList = "SELECT A.CLASSID,(SELECT CLASSDEFNDESC FROM PSCLASSDEFN WHERE CLASSID=A.CLASSID) CLASSDEFNDESC FROM PSROLECLASS A WHERE A.ROLENAME=? ORDER BY A.CLASSID limit ?,?";
				list = jdbcTemplate.queryForList(sqlPlstList, new Object[] { strRoleName, numStart, numLimit });
			}
			int total = 0;
			if(list != null && list.size() > 0){
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("roleName", strRoleName);
					mapList.put("permID", list.get(i).get("CLASSID"));
					mapList.put("permDesc",list.get(i).get("CLASSDEFNDESC"));
					
					listData.add(mapList);
				}
				totalSQL = "SELECT COUNT(1) FROM PSROLECLASS A WHERE A.ROLENAME=?";
				total = jdbcTemplate.queryForObject(totalSQL,new Object[]{strRoleName},"Integer");

				mapRet.replace("total",total);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/* 删除角色许可权信息 */
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";
		
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {

				// 角色下权限信息;
				String strRoleInfo = actData[num];
				jacksonUtil.json2Map(strRoleInfo);
				
				// 角色名称ID;
				String sRoleName = jacksonUtil.getString("roleName");
				// 许可权编号;
				String sPermID = jacksonUtil.getString("permID");

				if (sRoleName != null && !"".equals(sRoleName) && (sPermID != null && !"".equals(sPermID))) {

					/* 删除角色下的权限 */
					PsRoleclassKey psRoleclassKey = new PsRoleclassKey();
					psRoleclassKey.setRolename(sRoleName);
					psRoleclassKey.setClassid(sPermID);
					psRoleclassMapper.deleteByPrimaryKey(psRoleclassKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
}
