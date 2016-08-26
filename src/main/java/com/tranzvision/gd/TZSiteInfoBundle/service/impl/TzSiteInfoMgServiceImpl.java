/**
 * 
 */
package com.tranzvision.gd.TZSiteInfoBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 站点信息设置功能实现类，原PS：TZ_GD_SITESET_PKG:TZ_SITESET_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-03
 */
@Service("com.tranzvision.gd.TZSiteInfoBundle.service.impl.TzSiteInfoMgServiceImpl")
public class TzSiteInfoMgServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;
	
	@Autowired
	private GetSeqNum getSeqNum;

	/**
	 * 站点信息查询
	 * 
	 * @param strParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			//多站点将不根据当前登录机构查询，而是根据传入的站点id查询；
			//String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("siteId")){
				String siteId = jacksonUtil.getString("siteId");
				String sql = "select TZ_SITEI_ID,TZ_JG_ID,TZ_SITE_LANG,TZ_SITEI_NAME,TZ_SITEM_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ENABLE='Y' and TZ_SITEI_ID=?";

				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { siteId });

				if (null != mapData) {

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("orgId", String.valueOf(mapData.get("TZ_JG_ID")));
					mapRet.put("siteId", String.valueOf(mapData.get("TZ_SITEI_ID")));
					mapRet.put("siteLanguage", String.valueOf(mapData.get("TZ_SITE_LANG")));
					mapRet.put("siteName", String.valueOf(mapData.get("TZ_SITEI_NAME")));
					mapRet.put("siteMid", String.valueOf(mapData.get("TZ_SITEM_ID")));
					
					strRet = jacksonUtil.Map2json(mapRet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 添加站点信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String conflictKeys = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date datenow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String siteId = "";
				boolean bolRst = false;
				int loopTimes = 5;
				while (!bolRst && loopTimes > 0) {
					 
					siteId = String.valueOf(getSeqNum.getSeqNum("PS_TZ_SITEI_DEFN_T", "TZ_SITEI_ID"));
					
					String sql = "select 'Y' from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");

					if (null == recExists) {
						bolRst = true;
						conflictKeys = "";
					} else {
						conflictKeys = "0";
					}

					loopTimes--;
				}

				if (bolRst) {

					String orgId = jacksonUtil.getString("orgId");
					String siteName = jacksonUtil.getString("siteName");
					String siteLanguage = jacksonUtil.getString("siteLanguage");

					PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();
					psTzSiteiDefnTWithBLOBs.setTzSiteiId(siteId);
					psTzSiteiDefnTWithBLOBs.setTzJgId(orgId);
					psTzSiteiDefnTWithBLOBs.setTzSiteiName(siteName);
					psTzSiteiDefnTWithBLOBs.setTzSiteiDescr(siteName);
					psTzSiteiDefnTWithBLOBs.setTzSiteiEnable("Y");
					psTzSiteiDefnTWithBLOBs.setTzSiteLang(siteLanguage);
					psTzSiteiDefnTWithBLOBs.setTzSiteiType("C");
					psTzSiteiDefnTWithBLOBs.setTzAddedDttm(datenow);
					psTzSiteiDefnTWithBLOBs.setTzAddedOprid(oprid);
					psTzSiteiDefnTWithBLOBs.setTzLastmantDttm(datenow);
					psTzSiteiDefnTWithBLOBs.setTzLastmantOprid(oprid);

					psTzSiteiDefnTMapper.insertSelective(psTzSiteiDefnTWithBLOBs);

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("siteId", siteId);
					strRet = jacksonUtil.Map2json(mapJson);
				}

			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "生成站点编号失败，请重试";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 更新站点信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String errorMsg = "";
		String comma = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date lastupddttm = new Date();
			String lastupdoprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String siteId = jacksonUtil.getString("siteId");
				String siteLanguage = jacksonUtil.getString("siteLanguage");
				String siteName = jacksonUtil.getString("siteName");

				String sql = "select 'Y' from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");

				if (null != recExists) {

					PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();
					psTzSiteiDefnTWithBLOBs.setTzSiteiId(siteId);
					psTzSiteiDefnTWithBLOBs.setTzSiteiName(siteName);
					psTzSiteiDefnTWithBLOBs.setTzSiteLang(siteLanguage);
					psTzSiteiDefnTWithBLOBs.setTzLastmantDttm(lastupddttm);
					psTzSiteiDefnTWithBLOBs.setTzLastmantOprid(lastupdoprid);

					psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				} else {
					if (!"".equals(errorMsg)) {
						comma = ",";
					}
					errorMsg += comma + siteId;

				}

			}

			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "站点信息：" + errorMsg + " 不存在。";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
