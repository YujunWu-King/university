package com.tranzvision.gd.TZRoleMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZRoleMgBundle.dao.PsRoledefnMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;


/**
 * 角色管理相关类 原PS类：TZ_GD_ROLE_PKG:TZ_GD_ROLELIST_CLS
 * 
 * @author tang
 * @version 1.0, 2015/10/20
 */
@Service("com.tranzvision.gd.TZRoleMgBundle.service.impl.RoleListImpl")
public class RoleListImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsRoledefnMapper psRoledefnMapper;
	@Autowired
	private FliterForm fliterForm;
	
	/* 查询角色列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart,
			String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "ROLENAME", "ASC" } };
	
			// json数据要的结果字段;
			String[] resultFldArray = { "ROLENAME", "DESCR" };
	
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,
					numLimit, numStart, errorMsg);
			
			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
	
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("roleName", rowList[0]);
					mapList.put("roleDesc", rowList[1]);
					
					listData.add(mapList);
				}
	
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	// 功能说明：删除角色信息;
	@Override
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
				
				// 角色信息;
				String strRoleInfo = actData[num];
				jacksonUtil.json2Map(strRoleInfo);
				// 角色名称ID;
				String sRoleName = jacksonUtil.getString("roleName");
				if (sRoleName != null && !"".equals(sRoleName)) {

					/* 删除该角色名称下的所有相关表信息 */
					/*角色定义表*/
					psRoledefnMapper.deleteByPrimaryKey(sRoleName);
					
					 /*用户角色表*/
					String PSROLEUSERSQL = "DELETE FROM PSROLEUSER WHERE ROLENAME=?";
					jdbcTemplate.update(PSROLEUSERSQL, new Object[]{sRoleName});
					
					/*角色类别表*/
					String PSROLECLASSSQL = "DELETE FROM PSROLECLASS WHERE ROLENAME=?";
					jdbcTemplate.update(PSROLECLASSSQL, new Object[]{sRoleName});

					/*机构角色表*/
					String PS_TZ_JG_ROLE_TSQL = "DELETE FROM PS_TZ_JG_ROLE_T WHERE ROLENAME=?";
					jdbcTemplate.update(PS_TZ_JG_ROLE_TSQL, new Object[]{sRoleName});
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
