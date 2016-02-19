/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 其他信息，原PS：TZ_GD_BJGL_CLS:TZ_BJ_QTXX
 * 
 * @author SHIHUA
 * @since 2016-02-03
 */
@Service("com.tranzvision.gd.TZClassSetBundle.service.impl.TzClassOtherMsgServiceImpl")
public class TzClassOtherMsgServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String bj_id = jacksonUtil.getString("bj_id");

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetOtherMsgList");
			List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { bj_id, orgid });

			Map<String, Object> mapRet = new HashMap<String, Object>();
			
			Map<String, Object> mapJsons = new HashMap<String, Object>();

			for (Map<String, Object> mapData : listData) {

				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("show_name", mapData.get("TZ_ATTRIBUTE_NAME") == null ? ""
						: String.valueOf(mapData.get("TZ_ATTRIBUTE_NAME")));
				mapJson.put("fldType",
						mapData.get("TZ_CONTROL_TYPE") == null ? "" : String.valueOf(mapData.get("TZ_CONTROL_TYPE")));
				mapJson.put("zd_name", mapData.get("TZ_ATTRIBUTE_VALUE") == null ? ""
						: String.valueOf(mapData.get("TZ_ATTRIBUTE_VALUE")));

				mapJsons.put(mapData.get("TZ_ATTRIBUTE_ID") == null ? "" : String.valueOf(mapData.get("TZ_ATTRIBUTE_ID")),
						mapJson);

			}
			
			mapRet.put("formData", mapJsons);

			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "查询失败！" + e.getMessage();
		}

		return strRet;
	}

}
