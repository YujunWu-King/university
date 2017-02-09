package com.tranzvision.gd.TZSiteTemplateBundle.service.impl;

import java.net.Inet4Address;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemColuTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemColuT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemColuTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 站点栏目设置；原：TZ_GD_ZDLM_PKG:TZ_GD_ZDLM_CLS
 * 
 * @author tang
 * @since 2015-11-13
 * 
 */
@Service("com.tranzvision.gd.TZSiteTemplateBundle.service.impl.TemplateModelColumnServiceImpl")
public class TemplateModelColumnServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSitemColuTMapper psTzSitemColuTMapper;
	@Autowired
	private SqlQuery jdbcTemplate;

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
				String lm_id = String.valueOf(getSeqNum.getSeqNum("TZ_SITEM_COLU_T", "TZ_COLU_ID"));
				
				PsTzSitemColuT psTzSitemColuT = new PsTzSitemColuT();
            
				psTzSitemColuT.setTzSitemId(siteId);
				psTzSitemColuT.setTzColuId(lm_id);
				psTzSitemColuT.setTzColuName(lm_name);
				//修改添加活动类型
				psTzSitemColuT.setTzColuType(lm_lx);
				//psTzSitemColuT.setTzArtTypeId(lm_lx);
				psTzSitemColuT.setTzTempId(lm_mb);
				psTzSitemColuT.setTzArtTypeId(lm_nrlx);
				psTzSitemColuT.setTzContTemp(lm_nrmb);
				int i = psTzSitemColuTMapper.insert(psTzSitemColuT);
				if(i > 0){
					returnJsonMap.replace("lm_id", lm_id);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点模板栏目信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 修改站点栏目设置 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("lm_id", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = (String) jacksonUtil.getString("siteId");
				String lm_id = (String) jacksonUtil.getString("lm_id");
				String lm_name = jacksonUtil.getString("lm_name");
				String lm_lx = jacksonUtil.getString("lm_lx");
				String lm_mb = jacksonUtil.getString("lm_mb");
				String lm_nrlx = jacksonUtil.getString("lm_nrlx");
				String lm_nrmb = jacksonUtil.getString("lm_nrmb");

				PsTzSitemColuT psTzSitemColuT = new PsTzSitemColuT();
				psTzSitemColuT.setTzSitemId(siteId);
				psTzSitemColuT.setTzColuId(lm_id);
				psTzSitemColuT.setTzColuName(lm_name);
				//修改添加活动类型
				//psTzSitemColuT.setTzColuType(lm_lx);
				psTzSitemColuT.setTzColuType(lm_lx);
				//psTzSitemColuT.setTzArtTypeId(lm_lx);
				psTzSitemColuT.setTzTempId(lm_mb);
				psTzSitemColuT.setTzArtTypeId(lm_nrlx);
				psTzSitemColuT.setTzContTemp(lm_nrmb);
				int i = psTzSitemColuTMapper.updateByPrimaryKey(psTzSitemColuT);
				if(i > 0){
					returnJsonMap.replace("lm_id", lm_id);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点模板栏目信息保存失败";
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
				PsTzSitemColuTKey psTzSitemColuTKey = new PsTzSitemColuTKey();
				psTzSitemColuTKey.setTzSitemId(siteId);
				psTzSitemColuTKey.setTzColuId(lm_id);
				
				PsTzSitemColuT psTzSitemColuT = psTzSitemColuTMapper.selectByPrimaryKey(psTzSitemColuTKey);
				if(psTzSitemColuT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("lm_id", lm_id);
					jsonMap.put("lm_name", psTzSitemColuT.getTzColuName());
					//jsonMap.put("lm_lx", psTzSitemColuT.getTzColuType());
					
					jsonMap.put("lm_lx", psTzSitemColuT.getTzArtTypeId());
					jsonMap.put("lm_mb", psTzSitemColuT.getTzTempId());
					jsonMap.put("lm_nrlx",psTzSitemColuT.getTzContType() );
					jsonMap.put("lm_nrmb",psTzSitemColuT.getTzContTemp());
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
