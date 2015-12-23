/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_PH_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-15
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzPhDecoratedServiceImpl")
public class TzPhDecoratedServiceImpl extends FrameworkImpl {

	@Autowired
	private JacksonUtil jacksonUtil;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;
	
	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";

		try {

			jacksonUtil.json2Map(strParams);
			String strSiteId = jacksonUtil.getString("siteId");
			String strAreaId = jacksonUtil.getString("areaId");
			String strAreaZone = jacksonUtil.getString("areaZone");
			String strAreaType = jacksonUtil.getString("areaType");

			if (null != strAreaId && !"".equals(strAreaId)) {
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
			}

			strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzYtForm", strSiteId, strAreaId, strAreaZone,
					strAreaType);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.getMessage();
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

				String strSiteId = jacksonUtil.getString("siteId");

				String strAreaId = jacksonUtil.getString("areaId");

				//String strAreaZone = jacksonUtil.getString("areaZone");

				//String strAreaType = jacksonUtil.getString("areaType");

				String strAreaCode = jacksonUtil.getString("areaCode");

				String strHeadCode = jacksonUtil.getString("headCode");

				String strBodyCode = jacksonUtil.getString("bodyCode");

				String strPageType = jacksonUtil.getString("pagetype");

				String strOperate = jacksonUtil.getString("oprate");

				if (null == strOperate || "".equals(strOperate)) {
					strOperate = "D";
				}

				if ((null == strSiteId || "".equals(strSiteId)) || (null == strAreaId || "".equals(strAreaId))) {
					errMsg[0] = "1";
					errMsg[1] = "请求编辑的区域异常！";
					mapRet.put("success", false);
					return jacksonUtil.Map2json(mapRet);
				}

				

				if (null != strAreaCode && !"".equals(strAreaCode)) {

					PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

					psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
					psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
					psTzSiteiAreaTWithBLOBs.setTzLastmantDttm(dateNow);
					psTzSiteiAreaTWithBLOBs.setTzLastmantOprid(oprid);
					if ("D".equals(strOperate)) {
						psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strAreaCode);
					} else {
						psTzSiteiAreaTWithBLOBs.setTzAreaPubcode(strAreaCode);
					}

					psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);
				}

				if (null != strHeadCode && null!=strBodyCode) {

					String strPageCode = "<html>" + strHeadCode + "<body>" + strBodyCode + "</body></html>";
					
					String strDecoratedplugincss = jacksonUtil.getString("decoratedplugincss");
					String strDecoratedpluginjs = jacksonUtil.getString("decoratedpluginjs");
					String strDecoratedpluginbar = jacksonUtil.getString("decoratedpluginbar");

					String strPrePageCode = strPageCode;
					strPrePageCode = strPrePageCode.replace(strDecoratedplugincss, "");
					strPrePageCode = strPrePageCode.replace(strDecoratedpluginjs, "");
					strPrePageCode = strPrePageCode.replace(strDecoratedpluginbar, "");
					
					PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();
					
					psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
					
					switch (strPageType) {
					case "homepage":
						psTzSiteiDefnTWithBLOBs.setTzIndexSavecode(strPageCode);
						psTzSiteiDefnTWithBLOBs.setTzIndexPrecode(strPrePageCode);
						break;

					case "loginpage":
						psTzSiteiDefnTWithBLOBs.setTzLonginSavecode(strPageCode);
						psTzSiteiDefnTWithBLOBs.setTzLoginPrecode(strPrePageCode);
						break;

					case "enrollpage":
						psTzSiteiDefnTWithBLOBs.setTzEnrollSavecode(strPageCode);
						psTzSiteiDefnTWithBLOBs.setTzEnrollPrecode(strPrePageCode);
						break;
					}
					
					psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);
				}
				
				errMsg[0] = "0";
				mapRet.put("success", true);
				strRet = jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "0";
			errMsg[1] = "请求编辑的区域异常！";
		}

		return strRet;
	}
	
	

}