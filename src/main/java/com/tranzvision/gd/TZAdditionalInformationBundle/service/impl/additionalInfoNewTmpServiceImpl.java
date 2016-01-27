package com.tranzvision.gd.TZAdditionalInformationBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.util.base.JacksonUtil;

import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：提交资料模版列表类-用于模版复制;
 * 原PS类：TZ_GD_SMTDTMG_PKG:TZ_GD_data5_CLS
 */
@Service("com.tranzvision.gd.TZAdditionalInformationBundle.service.impl.additionalInfoNewTmpServiceImpl")
public class additionalInfoNewTmpServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;

	/* 获取历史模版信息 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "{}";

		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		// 返回值;
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			String sql = "SELECT TZ_SBMINF_TMP_ID,TZ_SBMINF_TMP_NAME FROM PS_TZ_SBMINF_TMP_T WHERE TZ_JG_ID=? AND TZ_SBMINF_STATUS='Y'";
			List<?> listData = sqlQuery.queryForList(sql, new Object[] { orgid });
			for (Object objData : listData) {
			
				Map<String, Object> mapData = (Map<String, Object>) objData;
				String strAdditionalInfoTmpId = "";
				String strAdditionalInfoTmpName = "";

				strAdditionalInfoTmpId = String.valueOf(mapData.get("TZ_SBMINF_TMP_ID"));
				strAdditionalInfoTmpName = String.valueOf(mapData.get("TZ_SBMINF_TMP_NAME"));
				
				Map<String, Object> mapJson = new HashMap<String, Object>();

				mapJson.put("tplid", strAdditionalInfoTmpId);
				mapJson.put("tplname", strAdditionalInfoTmpName);
				listJson.add(mapJson);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.List2json(listJson);
		return strRet;
	}
}
