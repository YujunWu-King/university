package com.tranzvision.gd.TZApplicationProcessBundle.service.impl;

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
 * 功能说明：报名流程详情类;
 * 原PS类：TZ_GD_PROTMPLATE_PKG:TZ_GD_BACKDT_CLS
 */
@Service("com.tranzvision.gd.TZApplicationProcessBundle.service.impl.proNewTmpServiceImpl")
public class proNewTmpServiceImpl extends FrameworkImpl{
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
			
			String sql = "SELECT TZ_APPPRO_TMP_ID,TZ_APPPRO_TMP_NAME FROM PS_TZ_APPPRO_TMP_T WHERE TZ_JG_ID=? AND TZ_APPPRO_STATUS='Y'";
			List<?> listData = sqlQuery.queryForList(sql, new Object[] { orgid });
			for (Object objData : listData) {
			
				Map<String, Object> mapData = (Map<String, Object>) objData;
				String strAppProcessTmpId = "";
				String strAppProcessTmpName = "";

				strAppProcessTmpId = String.valueOf(mapData.get("TZ_APPPRO_TMP_ID"));
				strAppProcessTmpName = String.valueOf(mapData.get("TZ_APPPRO_TMP_NAME"));
				
				Map<String, Object> mapJson = new HashMap<String, Object>();

				mapJson.put("tplid", strAppProcessTmpId);
				mapJson.put("tplname", strAppProcessTmpName);
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
