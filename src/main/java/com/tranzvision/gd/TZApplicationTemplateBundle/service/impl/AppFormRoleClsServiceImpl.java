package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzApptplRTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplRTKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author WRL TZ_ONLINE_REG_PKG:TZ_ONREG_ROLESET_CLS 报名表模板角色配置
 */
@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormRoleClsServiceImpl")
public class AppFormRoleClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzApptplRTMapper psTzApptplRTMapper;

	/* 查询报名表角色 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(comParams);
			String tplId = jacksonUtil.getString("tplid");

			if (StringUtils.isBlank(tplId)) {
				errorMsg[0] = "1";
				errorMsg[1] = "参数-报名表模板ID为空！";
			}

			List<?> resultlist = sqlQuery.queryForList(
					tzSQLObject.getSQLText("SQL.TZApplicationTemplateBundle.TzGetJgRoles"), new Object[] { orgId });
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String roleName = result.get("ROLENAME") == null ? "" : String.valueOf(result.get("ROLENAME"));
				String roleDesc = result.get("DESCR") == null ? "" : String.valueOf(result.get("DESCR"));

				String isChecked = sqlQuery.queryForObject(
						"SELECT 'Y' FROM PS_TZ_APPTPL_R_T WHERE TZ_JG_ID = ? AND TZ_APP_TPL_ID = ? AND ROLENAME = ?",
						new Object[] { orgId, tplId, roleName }, "String");


				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("jgid", orgId);
				mapRetJson.put("rolename", roleName);
				mapRetJson.put("roledesc", roleDesc);
				mapRetJson.put("tplid", tplId);
				if (StringUtils.equals(isChecked, "Y")) {
					mapRetJson.put("isChecked", Boolean.TRUE);
				}else{
					mapRetJson.put("isChecked", Boolean.FALSE);
				}

				listData.add(mapRetJson);
			}

			mapRet.replace("total", resultlist.size());
			mapRet.replace("root", listData);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * 更新报名表模板角色数据
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";

		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		if (StringUtils.isBlank(orgId)) {
			errMsg[0] = "1";
			errMsg[1] = "您当前没有机构，不能更新报名表模板角色数据！";
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int dataLength = actData.length;

			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String jgid = jacksonUtil.getString("jgid");
				String rolename = jacksonUtil.getString("rolename");
				String tplid = jacksonUtil.getString("tplid");
				String isChecked = jacksonUtil.getString("isChecked");
				if (StringUtils.equals("true", isChecked)) {
					PsTzApptplRTKey psTzApptplRTKey = new PsTzApptplRTKey();
					psTzApptplRTKey.setTzJgId(jgid);
					psTzApptplRTKey.setTzAppTplId(tplid);
					psTzApptplRTKey.setRolename(rolename);

					psTzApptplRTMapper.insert(psTzApptplRTKey);
				} else {
					PsTzApptplRTKey psTzApptplRTKey = new PsTzApptplRTKey();
					psTzApptplRTKey.setTzJgId(jgid);
					psTzApptplRTKey.setTzAppTplId(tplid);
					psTzApptplRTKey.setRolename(rolename);

					psTzApptplRTMapper.deleteByPrimaryKey(psTzApptplRTKey);
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
