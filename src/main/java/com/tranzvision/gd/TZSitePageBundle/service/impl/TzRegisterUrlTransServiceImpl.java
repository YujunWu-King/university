/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_REGISTER_URLTRANS_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-18
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzRegisterUrlTransServiceImpl")
public class TzRegisterUrlTransServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzSetEnrollPServiceImpl tzSetEnrollPServiceImpl;

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strOrgId = jacksonUtil.getString("orgid");

			if (null != strOrgId && !"".equals(strOrgId)) {

				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteidByOrgid");
				String strSiteId = sqlQuery.queryForObject(sql, new Object[] {strOrgId}, "String");

				if (null != strSiteId && !"".equals(strSiteId)) {
					Map<String, Object> mapParams = new HashMap<String, Object>();

					mapParams.put("siteId", strSiteId);
					mapParams.put("oprate", "R");

					return tzSetEnrollPServiceImpl.tzGetHtmlContent(jacksonUtil.Map2json(mapParams));
				} else {
					return "站点尚未发布！";
				}

			} else {
				return "站点尚未发布！";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;

	}

}
