package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiTempTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiTempTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiTempTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy
 * @version 创建时间：2016年8月23日 下午5:26:04 类说明
 */
@Service("com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl.OrgTempMgServiceImpl")
public class OrgTempMgServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private PsTzSiteiTempTMapper psTzSiteiTempTMapper;

	/* 查询列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);

		String siteId = String.valueOf(jacksonUtil.getString("siteId"));

		System.out.println("siteId:" + siteId);
		try {

			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_TEMP_T where  TZ_SITEI_ID = ?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { siteId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;

			if (numLimit > 0) {
				sql = "SELECT A.TZ_SITEI_ID,A.TZ_TEMP_ID,A.TZ_TEMP_NAME,B.TZ_ZHZ_DMS TZ_TEMP_TYPE FROM PS_TZ_SITEI_TEMP_T A left join (SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_TEMP_TYPE' AND TZ_EFF_STATUS='A') B on A.TZ_TEMP_TYPE = B.TZ_ZHZ_ID WHERE TZ_SITEI_ID=? limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId, numStart, numLimit });
			} else {
				sql = "SELECT A.TZ_SITEI_ID,A.TZ_TEMP_ID,A.TZ_TEMP_NAME,B.TZ_ZHZ_DMS TZ_TEMP_TYPE FROM PS_TZ_SITEI_TEMP_T A left join (SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_TEMP_TYPE' AND TZ_EFF_STATUS='A') B on A.TZ_TEMP_TYPE = B.TZ_ZHZ_ID WHERE TZ_SITEI_ID=?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId });
			}
			// System.out.println("sql:" + sql);
			if (list != null) {
				// System.out.println("list:" + list.size());
				Map<String, Object> jsonMap= null;
				for (int i = 0; i < list.size(); i++) {
					jsonMap = new HashMap<String, Object>();
					jsonMap.put("siteId", list.get(i).get("TZ_SITEI_ID"));
					jsonMap.put("templateId", list.get(i).get("TZ_TEMP_ID"));
					jsonMap.put("templateName", list.get(i).get("TZ_TEMP_NAME"));
					jsonMap.put("templateType", list.get(i).get("TZ_TEMP_TYPE"));
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
				//strRet = jacksonUtil.Map2json(returnJsonMap);
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 添加站点模板集合设置 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("templateId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = (String) jacksonUtil.getString("siteId");
				String templateState = jacksonUtil.getString("templateState");
				String templateName = jacksonUtil.getString("templateName");
				String templateType = jacksonUtil.getString("templateType");
				String templatePCCode = jacksonUtil.getString("templatePCCode");
				String templateMBCode = jacksonUtil.getString("templateMBCode");
				String pcScriptName = jacksonUtil.getString("pcScriptName");
				String mbScriptName = jacksonUtil.getString("mbScriptName");
				String templateId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_TEMP_T", "TZ_TEMP_ID"));

				PsTzSiteiTempTWithBLOBs psTzSiteiTempTWithBLOBs = new PsTzSiteiTempTWithBLOBs();
				psTzSiteiTempTWithBLOBs.setTzSiteiId(siteId);
				psTzSiteiTempTWithBLOBs.setTzTempId(templateId);
				psTzSiteiTempTWithBLOBs.setTzTempState(templateState);
				psTzSiteiTempTWithBLOBs.setTzTempName(templateName);
				psTzSiteiTempTWithBLOBs.setTzTempType(templateType);
				psTzSiteiTempTWithBLOBs.setTzTempPccode(templatePCCode);
				psTzSiteiTempTWithBLOBs.setTzTempMscode(templateMBCode);
				psTzSiteiTempTWithBLOBs.setTzPctempScriptHtml(pcScriptName);
				psTzSiteiTempTWithBLOBs.setTzMstempScriptHtml(mbScriptName);

				int i = psTzSiteiTempTMapper.insert(psTzSiteiTempTWithBLOBs);
				if (i > 0) {
					returnJsonMap.replace("templateId", templateId);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "站点模板集合信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 修改站点模板集合设置 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("templateId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String templateId = jacksonUtil.getString("templateId");
				String templateState = jacksonUtil.getString("templateState");
				String templateName = jacksonUtil.getString("templateName");
				String templateType = jacksonUtil.getString("templateType");
				String templatePCCode = jacksonUtil.getString("templatePCCode");
				String templateMBCode = jacksonUtil.getString("templateMBCode");
				String pcScriptName = jacksonUtil.getString("pcScriptName");
				String mbScriptName = jacksonUtil.getString("mbScriptName");

				PsTzSiteiTempTWithBLOBs psTzSiteiTempTWithBLOBs = new PsTzSiteiTempTWithBLOBs();
				psTzSiteiTempTWithBLOBs.setTzSiteiId(siteId);
				psTzSiteiTempTWithBLOBs.setTzTempId(templateId);
				psTzSiteiTempTWithBLOBs.setTzTempState(templateState);
				psTzSiteiTempTWithBLOBs.setTzTempName(templateName);
				psTzSiteiTempTWithBLOBs.setTzTempType(templateType);
				psTzSiteiTempTWithBLOBs.setTzTempPccode(templatePCCode);
				psTzSiteiTempTWithBLOBs.setTzTempMscode(templateMBCode);
				psTzSiteiTempTWithBLOBs.setTzPctempScriptHtml(pcScriptName);
				psTzSiteiTempTWithBLOBs.setTzMstempScriptHtml(mbScriptName);

				int i = psTzSiteiTempTMapper.updateByPrimaryKeyWithBLOBs(psTzSiteiTempTWithBLOBs);
				if (i > 0) {
					returnJsonMap.replace("templateId", templateId);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "站点模板集合信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("templateId")) {
				// 站点id 模板id;
				String siteId = jacksonUtil.getString("siteId");
				String templateId = jacksonUtil.getString("templateId");

				PsTzSiteiTempTKey psTzSiteiTempTKey = new PsTzSiteiTempTKey();
				psTzSiteiTempTKey.setTzSiteiId(siteId);
				psTzSiteiTempTKey.setTzTempId(templateId);
				PsTzSiteiTempTWithBLOBs psTzSiteiTempT = psTzSiteiTempTMapper.selectByPrimaryKey(psTzSiteiTempTKey);
				if (psTzSiteiTempT != null) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("templateId", psTzSiteiTempT.getTzTempId());
					jsonMap.put("templateName", psTzSiteiTempT.getTzTempName());
					jsonMap.put("templateType", psTzSiteiTempT.getTzTempType());
					jsonMap.put("templatePCCode", psTzSiteiTempT.getTzTempPccode());
					jsonMap.put("templateMBCode", psTzSiteiTempT.getTzTempMscode());
					jsonMap.put("templateState", psTzSiteiTempT.getTzTempState());
					jsonMap.put("pcScriptName", psTzSiteiTempT.getTzPctempScriptHtml());
					jsonMap.put("mbScriptName", psTzSiteiTempT.getTzMstempScriptHtml());
					returnJsonMap.replace("formData", jsonMap);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "未找到对应站点集合信息";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 删除站点模板 */
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
				// 提交信息
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// 站点ID;
				String templateId = jacksonUtil.getString("templateId");
				if (templateId != null && !"".equals(templateId)) {

					// 删除机构站点模板信息;
					String deleteSQL = "DELETE from PS_TZ_SITEI_TEMP_T where TZ_TEMP_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { templateId });
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
