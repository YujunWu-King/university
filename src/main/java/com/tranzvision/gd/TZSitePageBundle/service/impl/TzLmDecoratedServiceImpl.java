/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.RegisteServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_LM_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-16
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzLmDecoratedServiceImpl")
public class TzLmDecoratedServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;

	@Autowired
	private RegisteServiceImpl registeServiceImpl;

	@Autowired
	private TzSiteMgServiceImpl tzSiteMgServiceImpl;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");

			String strAreaId = jacksonUtil.getString("areaId");

			String strAreaZone = jacksonUtil.getString("areaZone");

			String strAreaType = jacksonUtil.getString("areaType");

			if (null == strAreaId || "".equals(strAreaId)) {
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
			}

			strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzYtForm", strSiteId, strAreaId, strAreaZone,
					strAreaType);

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
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strSiteId = jacksonUtil.getString("siteId");

				String strAreaId = jacksonUtil.getString("areaId");

				// String strAreaZone = jacksonUtil.getString("areaZone");

				// String strAreaType = jacksonUtil.getString("areaType");

				String strAreaCode = jacksonUtil.getString("areaCode");

				// String strHeadCode = jacksonUtil.getString("headCode");

				String strBodyCode = jacksonUtil.getString("bodyCode");

				String strPageType = jacksonUtil.getString("pagetype");

				String strOperate = jacksonUtil.getString("oprate");
				if (null == strOperate || "".equals(strOperate)) {
					strOperate = "D";
				}

				if ((null == strSiteId || "".equals(strSiteId)) || (null == strAreaId || "".equals(strAreaId))) {
					mapRet.put("success", false);
					errMsg[0] = "1";
					errMsg[1] = "请求编辑的区域异常！";
					return jacksonUtil.Map2json(mapRet);
				}

				if (null != strAreaCode && !"".equals(strAreaCode)) {

					String ctxPath = request.getContextPath();
					if (!"".equals(ctxPath)) {
						strAreaCode = strAreaCode.replace(ctxPath + "/", "{ContextPath}/");
					}
					PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

					psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
					psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);

					if ("D".equals(strOperate)) {
						psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strAreaCode);
					} else {
						psTzSiteiAreaTWithBLOBs.setTzAreaPubcode(strAreaCode);
					}

					psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);
				}

				if (null != strBodyCode && !"".equals(strBodyCode)) {

					switch (strPageType) {
					case "homepage":
						tzSiteMgServiceImpl.saveHomepage(strBodyCode, strSiteId, errMsg);
						break;

					case "loginpage":
						tzSiteMgServiceImpl.saveLoginpage(strBodyCode, strSiteId, errMsg);
						break;

					case "enrollpage":
						String strEnrollPageCode = registeServiceImpl.handleEnrollPage(strSiteId);
						registeServiceImpl.saveEnrollpage(strEnrollPageCode, strSiteId, errMsg);
						break;
					}

				}

				if (errMsg.length > 0 && !"0".equals(errMsg[0])) {
					mapRet.put("success", false);
				} else {
					errMsg[0] = "0";
					mapRet.put("success", true);
				}
				strRet = jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "请求编辑的区域异常！";
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;
	}

}
