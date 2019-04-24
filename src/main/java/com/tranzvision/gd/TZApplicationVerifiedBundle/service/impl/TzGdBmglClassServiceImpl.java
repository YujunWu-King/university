package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 原PS类：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_CLASS_CLS
 * @author tang
 * 报名管理-报名表审核-班级管理
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmglClassServiceImpl")
public class TzGdBmglClassServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery jdbcTemplate;
	/*获取班级列表*/
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_CLASS_ID", "TZ_CLASS_NAME", "TZ_BATCH_ID", "TZ_BATCH_NAME", "TZ_APPLY_STATUS","TZ_RX_DT", "TZ_NUM_APPLICANTING","TZ_NUM_SUBMITTED","TZ_NUM_PASS"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classID", rowList[0]);
					mapList.put("className", rowList[1]);
					mapList.put("batchID", rowList[2]);
					mapList.put("batchName", rowList[3]);
					mapList.put("applyStatus", rowList[4]);
					mapList.put("admissionDate", rowList[5]);
					mapList.put("applicantingNumber", rowList[6]);
					mapList.put("submittedNumber", rowList[7]);
					mapList.put("passNumber", rowList[8]);
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
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		if("getRoleList".equals(oprType)) {
			Map<String, Object> map = new HashMap<>();
			List<Map<String,Object>> list = getRoleList();
			map.put("roleList", list);
			return jacksonUtil.Map2json(map);
		}
		return super.tzOther(oprType, strParams, errorMsg);
	}
	/**
	 * 获取当前用户的角色列表
	 * @return
	 */
	private List<Map<String,Object>> getRoleList(){
		String oprID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		// 获取角色信息列表sql;
		String sqlRoleList = "";
		// 角色编号，角色描述，是否该用户角色;
		String roleID = "", roleDesc = "", isRole = "";

		//获取该户下的角色信息及所有角色信息;
		List<Map<String, Object>> list = null;
		if (!"".equals(oprID) && oprID != null) {
			sqlRoleList = "SELECT B.ROLENAME,B.DESCR,'Y' ISROLE FROM PSROLEUSER A,PSROLEDEFN B WHERE A.ROLENAME=B.ROLENAME AND A.ROLEUSER = ? AND A.DYNAMIC_SW='N' UNION SELECT C.ROLENAME,A.DESCR,'N' ISROLE FROM PSROLEDEFN A, PS_TZ_JG_ROLE_T C WHERE C.TZ_JG_ID = ? AND A.ROLENAME = C.ROLENAME AND NOT EXISTS (SELECT 'Y' FROM PSROLEUSER B WHERE A.ROLENAME=B.ROLENAME AND B.ROLEUSER = ? AND B.DYNAMIC_SW='N') ORDER BY ISROLE DESC";
			list = jdbcTemplate.queryForList(sqlRoleList,
						new Object[] { oprID, orgID, oprID});
			
		}

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				roleID = (String) list.get(i).get("ROLENAME");
				roleDesc = (String) list.get(i).get("DESCR");
				isRole = (String) list.get(i).get("ISROLE");
				
				
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("roleID", roleID);
				mapList.put("roleName", roleDesc);
				if ("Y".equals(isRole)) {
					mapList.put("isRole", true);
				} else {
					mapList.put("isRole", false);
				}
				
				listData.add(mapList);
			}
		}

		return listData;
	}
}
