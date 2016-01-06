/**
 * 
 */
package com.tranzvision.gd.TZOrganizationMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationMgBundle.dao.PsTzJgBaseTMapper;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 机构管理功能；原：TZ_GD_JGGL_PKG:TZ_GD_JGLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-06
 */
@Service("com.tranzvision.gd.TZOrganizationMgBundle.service.impl.TzOrgMgServiceImpl")
public class TzOrgMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzJgBaseTMapper psTzJgBaseTMapper;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_JG_ID", "TZ_JG_NAME", "TZ_ZHZ_DMS" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("orgId", rowList[0]);
					mapList.put("orgName", rowList[1]);
					mapList.put("orgYxState", rowList[2]);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}

	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 机构id
				String orgId = jacksonUtil.getString("orgId");

				if ("ADMIN".equals(orgId)) {
					errMsg[0] = "1";
					errMsg[1] = "ADMIN为平台预留机构编号，不能删除。";
					return strRet;
				}

				if (orgId != null && !"".equals(orgId)) {

					// 删除机构管理员;
					sqlQuery.update("delete from PS_TZ_JG_MGR_T where TZ_JG_ID=?", new Object[] { orgId });
					// 删除机构角色;
					sqlQuery.update("delete from PS_TZ_JG_ROLE_T where TZ_JG_ID=?", new Object[] { orgId });
					// 删除机构;
					sqlQuery.update("delete from PS_TZ_JG_BASE_T where TZ_JG_ID=?", new Object[] { orgId });

					// 只是删除这三张表吗？

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
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("orgId")) {
				// 机构id
				String orgId = jacksonUtil.getString("orgId");
				PsTzJgBaseT psTzJgBaseT = psTzJgBaseTMapper.selectByPrimaryKey(orgId);
				if (psTzJgBaseT != null) {

					// 生成图片路径
					String orgLoginBjImgUrl = psTzJgBaseT.getTzAttachsysfilena();
					if (null != orgLoginBjImgUrl && !"".equals(orgLoginBjImgUrl)) {
						String attUrl = sqlQuery.queryForObject(
								"select TZ_ATT_A_URL from PS_TZ_JG_LOGINBJ_T where TZ_ATTACHSYSFILENA=?",
								new Object[] { orgLoginBjImgUrl }, "String");

						if (null != attUrl) {
							int sindex = attUrl.lastIndexOf("/");
							int lastIndex = attUrl.length() - 1;
							if (sindex == lastIndex) {
								orgLoginBjImgUrl = attUrl + orgLoginBjImgUrl;
							} else {
								orgLoginBjImgUrl = attUrl + "/" + orgLoginBjImgUrl;
							}
						} else {
							orgLoginBjImgUrl = "";
						}
					}

					Map<String, Object> mapOrg = new HashMap<String, Object>();
					mapOrg.put("orgId", psTzJgBaseT.getTzJgId());
					mapOrg.put("orgName", psTzJgBaseT.getTzJgName());
					mapOrg.put("orgYxState", psTzJgBaseT.getTzJgEffSta());
					mapOrg.put("orgBeizhu", psTzJgBaseT.getTzJgDescr());
					mapOrg.put("orgLxrName", psTzJgBaseT.getTzOrganContact());
					mapOrg.put("orgLxrPhone", psTzJgBaseT.getTzOrganContactph());
					mapOrg.put("orgLxrEmail", psTzJgBaseT.getTzOrganContactem());
					mapOrg.put("staticPath", psTzJgBaseT.getTzJgJtfjPath());
					mapOrg.put("orgLoginInf", psTzJgBaseT.getTzJgLoginInfo());
					mapOrg.put("orgLoginBjImgUrl", orgLoginBjImgUrl);
					mapOrg.put("orgLoginCopr", psTzJgBaseT.getTzJgLoginCopr());

					mapRet.replace("formData", mapOrg);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "请选择机构";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择机构";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

}