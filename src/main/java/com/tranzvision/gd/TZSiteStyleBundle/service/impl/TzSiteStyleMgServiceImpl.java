/**
 * 
 */
package com.tranzvision.gd.TZSiteStyleBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tranzvision.gd.TZBaseBundle.service.Framework;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 站点风格选择处理类，原PS：TZ_SITE_DECORATED_APP:TZ_STYLE_MG_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-04
 */
@Service("com.tranzvision.gd.TZSiteStyleBundle.service.impl.TzSiteStyleMgServiceImpl")
public class TzSiteStyleMgServiceImpl extends FrameworkImpl {

	@Autowired
    private ApplicationContext ctx;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			//多站点，不是根据有效的站点查询，而是直接传入;
			//String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			jacksonUtil.json2Map(strParams);
			String siteId = "";
			if(jacksonUtil.containsKey("siteId")){
				siteId = jacksonUtil.getString("siteId");
			}
			if(siteId == null ||  "".equals(siteId)){
				return jacksonUtil.Map2json(mapRet);
			}
			
			//String sql = tzSQLObject.getSQLText("SQL.TZSiteStyleBundle.TzSelectSiteInfo");
			String sql = "select TZ_SITEI_ID,TZ_SITEM_ID,TZ_SKIN_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ENABLE='Y' and TZ_SITEI_ID=?";
			Map<String, Object> mapSiteInfo = sqlQuery.queryForMap(sql, new Object[] { siteId });

			String strSiteMid = "";
			String strSkinType = "";
			String strSiteIid = "";
			String siteLang = "";
			String currSkinId = "";
			String siteModeId = "";

			if (null == mapSiteInfo) {
				strSiteMid = "%";
				strSkinType = "add";
			} else {

				strSiteIid = mapSiteInfo.get("TZ_SITEI_ID") == null ? ""
						: String.valueOf(mapSiteInfo.get("TZ_SITEI_ID"));
				siteLang = mapSiteInfo.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteInfo.get("TZ_SITE_LANG"));
				currSkinId = mapSiteInfo.get("TZ_SKIN_ID") == null ? "" : String.valueOf(mapSiteInfo.get("TZ_SKIN_ID"));
				siteModeId = mapSiteInfo.get("TZ_SITEM_ID") == null ? ""
						: String.valueOf(mapSiteInfo.get("TZ_SITEM_ID"));

				if ("".equals(siteModeId)) {
					strSiteMid = "%";
					strSkinType = "add";
				} else {
					strSiteMid = "%" + siteModeId + "%";
					strSkinType = "update";
				}

			}

			sql = tzSQLObject.getSQLText("SQL.TZSiteStyleBundle.TzSelectSkinList");

			List<?> listSkins = sqlQuery.queryForList(sql, new Object[] { siteLang, strSiteMid });

			for (Object objSkin : listSkins) {

				Map<String, Object> mapSkin = (Map<String, Object>) objSkin;

				String strSiteId = String.valueOf(mapSkin.get("TZ_SITEM_ID"));
				String strSkinId = String.valueOf(mapSkin.get("TZ_SKIN_ID"));
				String strSiteJg = String.valueOf(mapSkin.get("TZ_JGBH"));
				String strSkinName = String.valueOf(mapSkin.get("TZ_SKIN_NAME"));
				String strSkinImg = String.valueOf(mapSkin.get("TZ_IMG_VIEW"));

				String isCurrSkin = "N";

				if (!"".equals(currSkinId) && currSkinId.equals(strSkinId)) {
					isCurrSkin = "Y";
				}

				Map<String, Object> mapJson = new HashMap<String, Object>();

				mapJson.put("siteId", strSiteId);
				mapJson.put("skinId", strSkinId);
				mapJson.put("siteJg", strSiteJg);
				mapJson.put("skinName", strSkinName);
				mapJson.put("skinImg", strSkinImg);
				mapJson.put("skinType", strSkinType);
				mapJson.put("siteIId", strSiteIid);
				mapJson.put("isCurrSkin", isCurrSkin);
				mapJson.put("siteInsMid", siteModeId);
				
				listData.add(mapJson);

			}

			mapRet.replace("total", listSkins.size());
			mapRet.replace("root", listData);

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.clear();
			mapRet.put("success", false);
			errorMsg[0]="1";
			errorMsg[1]="未配置站点模板数据！";
		}

		return jacksonUtil.Map2json(mapRet);

	}
	
	/**
	 * 更新站点样式选择
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				//String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> formData = jacksonUtil.getMap("data");
				
				String strAreaId = formData.get("areaId")==null?"":String.valueOf(formData.get("areaId"));
				//String strAreaZone = formData.get("areaZone")==null?"":String.valueOf(formData.get("areaZone"));
				String strAreaType = formData.get("areaType")==null?"":String.valueOf(formData.get("areaType"));
				
				String sql = "";
				String strAreaTypeId = "";
				if("".equals(strAreaId)){
					sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_AREA_T where TZ_AREA_ID=?";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[]{strAreaId}, "String");
				}else{
					sql = tzSQLObject.getSQLText("select TZ_AREA_TYPE_ID from PS_TZ_SITEI_AREA_T where TZ_AREA_TYPE=?");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[]{strAreaType}, "String");
				}

				if("".equals(strAreaTypeId)){
					strRet = "{success:false}";
					errMsg[0]="1";
					errMsg[1]="请求编辑的区域不存在！";
					return strRet;
				}
				
				sql = "select TZ_AREA_SET_CODE from PS_TZ_SITEI_ATYP_T where TZ_AREA_TYPE_ID=?";
				String strAreaAppObj = sqlQuery.queryForObject(sql, new Object[]{strAreaTypeId}, "String");
				
				if(null!=strAreaAppObj && !"".equals(strAreaAppObj)){
					
					Object obj = ctx.getBean(strAreaAppObj);
					if(null==obj){
						strRet = "{success:false}";
						errMsg[0]="1";
						errMsg[1]="请求编辑的区域不存在！";
						return strRet;
					}
					
					Framework objIns = (Framework)obj;
					strRet = objIns.tzUpdate(new String[]{strForm}, errMsg);
					errMsg[0]="0";
					
				}else{
					strRet = "{success:false}";
					errMsg[0]="1";
					errMsg[1]="请求编辑的区域不存在！";
					return strRet;
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
