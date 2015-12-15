/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_GG_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-15
 */
public class TzGgDecoratedServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private JacksonUtil jacksonUtil;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		String strRet = "";

		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");

			String strAreaId = jacksonUtil.getString("areaId");

			String strAreaZone = jacksonUtil.getString("areaZone");

			String strAreaType = jacksonUtil.getString("areaType");

			String strAreaClass = jacksonUtil.getString("areaClass");

			if (null != strAreaClass && !"".equals(strAreaClass)) {
				String[] aryAreaClass = strAreaClass.split(" ");
				if (aryAreaClass.length > 0) {
					strAreaClass = aryAreaClass[0].trim();
				} else {
					strAreaClass = "";
				}
			}
			
			if(null==strAreaClass || "".equals(strAreaClass)){
				errMsg[0] = "1";
				errMsg[1] = "参数错误！参数[areaClass]为空。";
				return strRet;
			}

			if (null == strAreaId || "".equals(strAreaId)) {
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
			}

			strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzGgForm", strSiteId, strAreaId, strAreaZone,
					strAreaType, strAreaClass);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "取数失败！" + e.getMessage();
		}

		return strRet;

	}
	
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "0";
			errMsg[1] = "";
		}

		return strRet;
	}

}
