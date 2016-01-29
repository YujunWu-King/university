package com.tranzvision.gd.TZSiteTemplateBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemTempTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemTempTKey;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemTempTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;

/**
 * 站点模板集合设置；原：TZ_GD_DZMB_PKG:TZ_MBJH_COM_CLS
 * 
 * @author tang
 * 
 */
@Service("com.tranzvision.gd.TZSiteTemplateBundle.service.impl.TemplateModeSetServiceImpl")
public class TemplateModeSetServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSitemTempTMapper psTzSitemTempTMapper;

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
				String siteId = jacksonUtil.getString("siteId");
				String templateState = jacksonUtil.getString("templateState");
				String templateName = jacksonUtil.getString("templateName");
				String templateType = jacksonUtil.getString("templateType");
				String templatePCCode = jacksonUtil.getString("templatePCCode");
				String templateMBCode = jacksonUtil.getString("templateMBCode");
				String pcScriptName = jacksonUtil.getString("pcScriptName");
				String mbScriptName = jacksonUtil.getString("mbScriptName");
				String templateId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEM_TEMP_T", "TZ_TEMP_ID"));

				PsTzSitemTempTWithBLOBs psTzSitemTempTWithBLOBs = new PsTzSitemTempTWithBLOBs();
				psTzSitemTempTWithBLOBs.setTzSitemId(siteId);
				psTzSitemTempTWithBLOBs.setTzTempId(templateId);
				psTzSitemTempTWithBLOBs.setTzTempState(templateState);
				psTzSitemTempTWithBLOBs.setTzTempName(templateName);
				psTzSitemTempTWithBLOBs.setTzTempType(templateType);
				psTzSitemTempTWithBLOBs.setTzTempPccode(templatePCCode);
				psTzSitemTempTWithBLOBs.setTzTempMscode(templateMBCode);
				psTzSitemTempTWithBLOBs.setTzPctempScriptHtml(pcScriptName);
				psTzSitemTempTWithBLOBs.setTzMstempScriptHtml(mbScriptName);

				int i = psTzSitemTempTMapper.insert(psTzSitemTempTWithBLOBs);
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
				String siteId = jacksonUtil.getString("siteId");
				String templateId = jacksonUtil.getString("templateId");
				String templateState = jacksonUtil.getString("templateState");
				String templateName = jacksonUtil.getString("templateName");
				String templateType = jacksonUtil.getString("templateType");
				String templatePCCode = jacksonUtil.getString("templatePCCode");
				String templateMBCode = jacksonUtil.getString("templateMBCode");
				String pcScriptName = jacksonUtil.getString("pcScriptName");
				String mbScriptName = jacksonUtil.getString("mbScriptName");

				PsTzSitemTempTWithBLOBs psTzSitemTempTWithBLOBs = new PsTzSitemTempTWithBLOBs();
				psTzSitemTempTWithBLOBs.setTzSitemId(siteId);
				psTzSitemTempTWithBLOBs.setTzTempId(templateId);
				psTzSitemTempTWithBLOBs.setTzTempState(templateState);
				psTzSitemTempTWithBLOBs.setTzTempName(templateName);
				psTzSitemTempTWithBLOBs.setTzTempType(templateType);
				psTzSitemTempTWithBLOBs.setTzTempPccode(templatePCCode);
				psTzSitemTempTWithBLOBs.setTzTempMscode(templateMBCode);
				psTzSitemTempTWithBLOBs.setTzPctempScriptHtml(pcScriptName);
				psTzSitemTempTWithBLOBs.setTzMstempScriptHtml(mbScriptName);

				int i = psTzSitemTempTMapper.updateByPrimaryKeyWithBLOBs(psTzSitemTempTWithBLOBs);
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

				PsTzSitemTempTKey psTzSitemTempTKey = new PsTzSitemTempTKey();
				psTzSitemTempTKey.setTzSitemId(siteId);
				psTzSitemTempTKey.setTzTempId(templateId);
				PsTzSitemTempTWithBLOBs psTzSitemTempT = psTzSitemTempTMapper.selectByPrimaryKey(psTzSitemTempTKey);
				if (psTzSitemTempT != null) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("templateId", psTzSitemTempT.getTzTempId());
					jsonMap.put("templateName", psTzSitemTempT.getTzTempName());
					jsonMap.put("templateType", psTzSitemTempT.getTzTempType());
					jsonMap.put("templatePCCode", psTzSitemTempT.getTzTempPccode());
					jsonMap.put("templateMBCode", psTzSitemTempT.getTzTempMscode());
					jsonMap.put("templateState", psTzSitemTempT.getTzTempState());
					jsonMap.put("pcScriptName", psTzSitemTempT.getTzPctempScriptHtml());
					jsonMap.put("mbScriptName", psTzSitemTempT.getTzMstempScriptHtml());
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
