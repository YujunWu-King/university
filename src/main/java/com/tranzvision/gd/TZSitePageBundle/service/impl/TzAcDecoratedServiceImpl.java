/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.TZSitePageBundle.dao.PsSiteBmClassSzTMapper;
import com.tranzvision.gd.TZSitePageBundle.model.PsSiteBmClassSzT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：application center 编辑
 * 
 * @author SHIHUA
 * @since 2015-12-16
 */

@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzAcDecoratedServiceImpl")
public class TzAcDecoratedServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;

	@Autowired
	private TzSiteMgServiceImpl tzSiteMgServiceImpl;
	
	@Autowired
	private PsSiteBmClassSzTMapper psSiteBmClassSzTMapper;

	@SuppressWarnings("unchecked")
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

			String strEleId = jacksonUtil.getString("eleid");

			String strColuId = "";

			String strColuName = "";

			String sql = "";

			if (null == strAreaId || "".equals(strAreaId)) {
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreaidColuidAreaNameBySiteidAreaTypeid");
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strAreaType });
				strAreaId = mapData.get("TZ_AREA_ID") == null ? "" : String.valueOf(mapData.get("TZ_AREA_ID"));
				strColuId = mapData.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapData.get("TZ_COLU_ID"));
				strColuName = mapData.get("TZ_AREA_NAME") == null ? "" : String.valueOf(mapData.get("TZ_AREA_NAME"));
			} else {
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetColuidAreaNameBySiteidAreaid");
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strAreaId });
				strColuId = mapData.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapData.get("TZ_COLU_ID"));
				strColuName = mapData.get("TZ_AREA_NAME") == null ? "" : String.valueOf(mapData.get("TZ_AREA_NAME"));
			}

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteAreaType");
			List<Object> listAreaType = sqlQuery.queryForList(sql, new Object[] { strSiteId });

			ArrayList<ArrayList<String>> listJsonAreaType = new ArrayList<>();

			for (Object obj : listAreaType) {
				Map<String, Object> mapAreaType = (Map<String, Object>) obj;

				ArrayList<String> listJson = new ArrayList<>();

				listJson.add(
						mapAreaType.get("TZ_AREA_TYPE") == null ? "" : String.valueOf(mapAreaType.get("TZ_AREA_TYPE")));
				listJson.add(mapAreaType.get("TZ_AREA_TYPE_NAME") == null ? ""
						: String.valueOf(mapAreaType.get("TZ_AREA_TYPE_NAME")));

				listJsonAreaType.add(listJson);
			}
			String strAreaJson = jacksonUtil.List2json(listJsonAreaType);

			sql = "select TZ_CONT_TYPE from PS_TZ_SITEI_COLU_T where TZ_COLU_ID=?";
			String strColuType = sqlQuery.queryForObject(sql, new Object[] { strColuId }, "String");

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteColusByColuType");
			List<Object> listColus = sqlQuery.queryForList(sql, new Object[] { strSiteId, strColuType });

			ArrayList<ArrayList<String>> listJsonColus = new ArrayList<>();

			for (Object obj : listColus) {
				Map<String, Object> mapColus = (Map<String, Object>) obj;

				ArrayList<String> listJson = new ArrayList<>();

				listJson.add(mapColus.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapColus.get("TZ_COLU_ID")));
				listJson.add(mapColus.get("TZ_COLU_NAME") == null ? "" : String.valueOf(mapColus.get("TZ_COLU_NAME")));

				listJsonColus.add(listJson);
			}
			String strColusJson = jacksonUtil.List2json(listJsonColus);
			
			// 班级显示设置;
			String check1 = "false";
			String check2 = "false";
			String bmbClassShow = sqlQuery.queryForObject(" select TZ_BM_CLASS_SHOW from PS_SITE_BMCLASS_SZ_T where TZ_SITEI_ID=?", new Object[]{strSiteId},"String");
			if("A".equals(bmbClassShow)){
				check1 = "true";
			}
			
			if("B".equals(bmbClassShow)){
				check2 = "true";
			}
			
			strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzAcForm", strSiteId, strAreaId, strAreaZone,
					strAreaType, strAreaJson, strColuId, strColusJson, strColuName, strEleId,check1,check2);
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

				String strAreaName = jacksonUtil.getString("areaName");

				// String strAreaZone = jacksonUtil.getString("areaZone");

				// String strAreaType = jacksonUtil.getString("areaType");

				String strAreaCode = jacksonUtil.getString("areaCode");

				// String strHeadCode = jacksonUtil.getString("headCode");

				String strBodyCode = jacksonUtil.getString("bodyCode");

				String strPageType = jacksonUtil.getString("pagetype");

				String strOperate = jacksonUtil.getString("oprate");
				
				//保存报名中心的显示设置;
				try{
					sqlQuery.update("delete from PS_SITE_BMCLASS_SZ_T where TZ_SITEI_ID=?",new Object[]{strSiteId});
					if(jacksonUtil.containsKey("bmbClassShow")){
						String bmbClassShow = jacksonUtil.getString("bmbClassShow"); 
						if(bmbClassShow != null && !"".equals(bmbClassShow)){
							PsSiteBmClassSzT psSiteBmClassSzT = new PsSiteBmClassSzT();
							psSiteBmClassSzT.setTzSiteiId(strSiteId);
							psSiteBmClassSzT.setTzBmClassShow(bmbClassShow);
							psSiteBmClassSzTMapper.insert(psSiteBmClassSzT);
						}
					}
						
				}catch(Exception e1){
						
				}
				

				if (null == strOperate || "".equals(strOperate)) {
					strOperate = "D";
				}

				if (null != strAreaCode && !"".equals(strAreaCode)) {
					PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

					psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
					psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
					psTzSiteiAreaTWithBLOBs.setTzAreaName(strAreaName);
					psTzSiteiAreaTWithBLOBs.setTzLastmantDttm(dateNow);
					psTzSiteiAreaTWithBLOBs.setTzLastmantOprid(oprid);

					if ("D".equals(strOperate)) {
						psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strAreaCode);
					} else {
						psTzSiteiAreaTWithBLOBs.setTzAreaPubcode(strAreaCode);
					}

					psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);
				}

				if (null != strBodyCode && !"".equals(strBodyCode)) {
					boolean boolResult = false;
					switch (strPageType) {
					case "homepage":
						boolResult = tzSiteMgServiceImpl.saveHomepage(strBodyCode, strSiteId, errMsg);
						break;
					case "loginpage":
						boolResult = tzSiteMgServiceImpl.saveLoginpage(strBodyCode, strSiteId, errMsg);
						break;
					}

					if (boolResult) {
						errMsg[0] = "0";
						errMsg[1] = "站点保存完成！";
					} else {
						mapRet.put("success", false);
						errMsg[0] = "1";
						errMsg[1] = "站点保存失败！";
					}

				}

			}

			if (!mapRet.containsKey("success")) {
				mapRet.put("success", true);
			}

			errMsg[0] = "0";
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
			errMsg[0] = "1";
			errMsg[1] = "请求编辑的区域异常！";
		}

		return strRet;
	}
	
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		// TODO Auto-generated method stub
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			String strSiteId = jacksonUtil.getString("siteId");
			if(strSiteId != null && !"".equals(strSiteId)){
				sqlQuery.update("delete from PS_SITE_BMCLASS_SZ_T where TZ_SITEI_ID=?",new Object[]{strSiteId});
				if(jacksonUtil.containsKey("bmbClassShow")){
					String bmbClassShow = jacksonUtil.getString("bmbClassShow"); 
					if(bmbClassShow != null && !"".equals(bmbClassShow)){
						PsSiteBmClassSzT psSiteBmClassSzT = new PsSiteBmClassSzT();
						psSiteBmClassSzT.setTzSiteiId(strSiteId);
						psSiteBmClassSzT.setTzBmClassShow(bmbClassShow);
						psSiteBmClassSzTMapper.insert(psSiteBmClassSzT);
					}
				}
				mapRet.put("success", true);
			}else{
				mapRet.put("success", false);
			}
			
		}catch(Exception e){
			mapRet.put("success", false);
		}
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
}
