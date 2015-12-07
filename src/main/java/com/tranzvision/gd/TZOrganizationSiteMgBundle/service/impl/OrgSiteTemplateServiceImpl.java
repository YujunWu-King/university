package com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiTempTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiTempTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiTempTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;

/*
 * 原PS类：TZ_GD_ZDDY_PKG:TZ_MB_COM_CLS
 * 高端产品-机构站点设置-站点模板集合
 * 
 */
@Service("com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl.OrgSiteTemplateServiceImpl")
public class OrgSiteTemplateServiceImpl extends FrameworkImpl {
	
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSiteiTempTMapper psTzSiteiTempTMapper;

	/* 添加站点模板集合设置 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("templateId", "");

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
				String templateId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_TEMP_T", "TZ_TEMP_ID"));

				PsTzSiteiTempTWithBLOBs psTzSiteiTempTWithBLOBs = new PsTzSiteiTempTWithBLOBs();
				psTzSiteiTempTWithBLOBs.setTzSiteiId(siteId);
				psTzSiteiTempTWithBLOBs.setTzTempId(templateId);
				psTzSiteiTempTWithBLOBs.setTzTempState(templateState);
				psTzSiteiTempTWithBLOBs.setTzTempName(templateName);
				psTzSiteiTempTWithBLOBs.setTzTempType(templateType);
				psTzSiteiTempTWithBLOBs.setTzTempPccode(templatePCCode);
				psTzSiteiTempTWithBLOBs.setTzTempMscode(templateMBCode);

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
				

				PsTzSiteiTempTWithBLOBs psTzSiteiTempTWithBLOBs = new PsTzSiteiTempTWithBLOBs();
				psTzSiteiTempTWithBLOBs.setTzSiteiId(siteId);
				psTzSiteiTempTWithBLOBs.setTzTempId(templateId);
				psTzSiteiTempTWithBLOBs.setTzTempState(templateState);
				psTzSiteiTempTWithBLOBs.setTzTempName(templateName);
				psTzSiteiTempTWithBLOBs.setTzTempType(templateType);
				psTzSiteiTempTWithBLOBs.setTzTempPccode(templatePCCode);
				psTzSiteiTempTWithBLOBs.setTzTempMscode(templateMBCode);

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

}
