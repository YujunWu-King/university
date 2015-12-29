package com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiColuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiColuT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiColuTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;

/**
 * 站点栏目设置；原：TZ_GD_ZDDY_PKG:TZ_GD_ZDLM_CLS
 * 
 * @author tang
 * @since 2015-11-17
 * 
 */
@Service("com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl.OrgSiteColumnServiceImpl")
public class OrgSiteColumnServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSiteiColuTMapper psTzSiteiColuTMapper;
	
	/* 添加站点栏目设置 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("lm_id", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String lm_name = jacksonUtil.getString("lm_name");
				String lm_lx = jacksonUtil.getString("lm_lx");
				String lm_mb = jacksonUtil.getString("lm_mb");
				String lm_nrlx = jacksonUtil.getString("lm_nrlx");
				String lm_nrmb = jacksonUtil.getString("lm_nrmb");
				String lm_yxzt = jacksonUtil.getString("lm_yxzt");
				String lm_id = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_COLU_T", "TZ_COLU_ID"));

				PsTzSiteiColuT psTzSiteiColuT = new PsTzSiteiColuT();
				psTzSiteiColuT.setTzSiteiId(siteId);
				psTzSiteiColuT.setTzColuId(lm_id);
				psTzSiteiColuT.setTzColuName(lm_name);
				psTzSiteiColuT.setTzColuType(lm_lx);
				psTzSiteiColuT.setTzTempId(lm_mb);
				psTzSiteiColuT.setTzContType(lm_nrlx);
				psTzSiteiColuT.setTzContTemp(lm_nrmb);
				psTzSiteiColuT.setTzColuState(lm_yxzt);
				int i = psTzSiteiColuTMapper.insert(psTzSiteiColuT);
				if(i > 0){
					returnJsonMap.replace("lm_id", lm_id);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点栏目信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 添加站点栏目设置 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("lm_id", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String lm_id = jacksonUtil.getString("lm_id");
				String lm_name = jacksonUtil.getString("lm_name");
				String lm_lx = jacksonUtil.getString("lm_lx");
				String lm_mb = jacksonUtil.getString("lm_mb");
				String lm_nrlx = jacksonUtil.getString("lm_nrlx");
				String lm_nrmb = jacksonUtil.getString("lm_nrmb");
				String lm_yxzt = jacksonUtil.getString("lm_yxzt");

				PsTzSiteiColuT psTzSiteiColuT = new PsTzSiteiColuT();
				psTzSiteiColuT.setTzSiteiId(siteId);
				psTzSiteiColuT.setTzColuId(lm_id);
				psTzSiteiColuT.setTzColuName(lm_name);
				psTzSiteiColuT.setTzColuType(lm_lx);
				psTzSiteiColuT.setTzTempId(lm_mb);
				psTzSiteiColuT.setTzContType(lm_nrlx);
				psTzSiteiColuT.setTzContTemp(lm_nrmb);
				psTzSiteiColuT.setTzColuState(lm_yxzt);
				int i = psTzSiteiColuTMapper.updateByPrimaryKey(psTzSiteiColuT);
				if(i > 0){
					returnJsonMap.replace("lm_id", lm_id);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点栏目信息保存失败";
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
				// 站点id 栏目id;
				String siteId = jacksonUtil.getString("siteId");
				String lm_id = jacksonUtil.getString("templateId");
				PsTzSiteiColuTKey psTzSiteiColuTKey = new PsTzSiteiColuTKey();
				psTzSiteiColuTKey.setTzSiteiId(siteId);
				psTzSiteiColuTKey.setTzColuId(lm_id);
				
				PsTzSiteiColuT psTzSiteiColuT = psTzSiteiColuTMapper.selectByPrimaryKey(psTzSiteiColuTKey);
				if(psTzSiteiColuT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("lm_id", lm_id);
					jsonMap.put("lm_name", psTzSiteiColuT.getTzColuName());
					jsonMap.put("lm_lx", psTzSiteiColuT.getTzColuType());
					jsonMap.put("lm_mb", psTzSiteiColuT.getTzTempId());
					jsonMap.put("lm_nrlx",psTzSiteiColuT.getTzContType() );
					jsonMap.put("lm_nrmb",psTzSiteiColuT.getTzContTemp());
					jsonMap.put("lm_yxzt",psTzSiteiColuT.getTzColuState());
					returnJsonMap.replace("formData", jsonMap);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "未找到对应栏目信息";
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
