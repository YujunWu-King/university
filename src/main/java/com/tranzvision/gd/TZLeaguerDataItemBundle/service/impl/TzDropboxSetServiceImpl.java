/**
 * 
 */
package com.tranzvision.gd.TZLeaguerDataItemBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerDataItemBundle.dao.PsTzYhzcXxzEngMapper;
import com.tranzvision.gd.TZLeaguerDataItemBundle.dao.PsTzYhzcXxzTblMapper;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzYhzcXxzEng;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzYhzcXxzEngKey;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzYhzcXxzTbl;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzYhzcXxzTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 注册项下拉框取值配置实现类，原PS：TZ_USER_REG_PKG:TZ_DROPBOX_SET_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-08
 */
@Service("com.tranzvision.gd.TZLeaguerDataItemBundle.service.impl.TzDropboxSetServiceImpl")
public class TzDropboxSetServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzYhzcXxzTblMapper psTzYhzcXxzTblMapper;
	
	@Autowired
	private PsTzYhzcXxzEngMapper psTzYhzcXxzEngMapper;
	

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	/**
	 * 根据传入参数判断参数是否合法，若不合法，直接返回空;若合法，查询满足条件的数据，并返回json报文
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			String platformOrgID = getSysHardCodeVal.getPlatformOrgID();
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String languageCd = "ENG";
			
			jacksonUtil.json2Map(strParams);
			String siteId = jacksonUtil.getString("siteId");
			String regId = jacksonUtil.getString("regId");

			if (null == regId || "".equals(regId)) {
				errorMsg[0] = "1";
				errorMsg[1] = "参数-注册项ID为空！";
				return jacksonUtil.Map2json(mapRet);
			}
			
			if(siteId == null){
				siteId = "";
			}
			if("".equals(siteId) && !platformOrgID.equals(orgid)){
				errorMsg[0] = "1";
				errorMsg[1] = "站点不存在";
				return jacksonUtil.Map2json(mapRet);
			}

			//String sql = "select TZ_OPT_ID,TZ_OPT_VALUE,TZ_SELECT_FLG,TZ_ORDER from PS_TZ_YHZC_XXZ_TBL where TZ_JG_ID = ? and TZ_REG_FIELD_ID = ? order by TZ_ORDER";
			String sql = "select TZ_OPT_ID,TZ_OPT_VALUE,TZ_SELECT_FLG,TZ_ORDER from PS_TZ_YHZC_XXZ_TBL where TZ_SITEI_ID = ? and TZ_REG_FIELD_ID = ? order by TZ_ORDER";

			List<?> listYhzcXxz = sqlQuery.queryForList(sql, new Object[] { siteId, regId });

			ArrayList<Map<String, Object>> listRetJson = new ArrayList<Map<String, Object>>();

			for (Object obj : listYhzcXxz) {
				Map<String, Object> mapRegField = (Map<String, Object>) obj;

				String strOptValue = mapRegField.get("TZ_OPT_ID") == null ? ""
						: String.valueOf(mapRegField.get("TZ_OPT_ID"));
				String strOptDesc = mapRegField.get("TZ_OPT_VALUE") == null ? ""
						: String.valueOf(mapRegField.get("TZ_OPT_VALUE"));
				String isSelect = mapRegField.get("TZ_SELECT_FLG") == null ? ""
						: String.valueOf(mapRegField.get("TZ_SELECT_FLG"));
				int numOrder = Integer.parseInt(String.valueOf(mapRegField.get("TZ_ORDER")));

				boolean selected = false;
				if ("Y".equals(isSelect)) {
					selected = true;
				}

				//sql = "select TZ_OPT_VALUE from PS_TZ_YHZC_XXZ_ENG where TZ_JG_ID = ? and LANGUAGE_CD = ? and TZ_REG_FIELD_ID = ? and TZ_OPT_ID = ?";
				sql = "select TZ_OPT_VALUE from PS_TZ_YHZC_XXZ_ENG where TZ_SITEI_ID = ? and LANGUAGE_CD = ? and TZ_REG_FIELD_ID = ? and TZ_OPT_ID = ?";
				String strOptEnDesc = sqlQuery.queryForObject(sql,
						new Object[] { siteId, languageCd, regId, strOptValue }, "String");

				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("siteId", siteId);
				mapRetJson.put("order", numOrder);
				mapRetJson.put("optId", strOptValue);
				mapRetJson.put("optName", strOptDesc);
				mapRetJson.put("isSelect", selected);
				mapRetJson.put("optEnName", strOptEnDesc);

				listRetJson.add(mapRetJson);

			}

			mapRet.replace("total", listYhzcXxz.size());
			mapRet.replace("root", listRetJson);

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.clear();
			mapRet.put("success", false);
			errorMsg[0] = "1";
			errorMsg[1] = "数据异常，请重试！";
		}

		return jacksonUtil.Map2json(mapRet);

	}
	
	/**
	 * 更新下拉框选项值列表
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String languageCd = "ENG";
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				String siteId = jacksonUtil.getString("siteId");
				String regId = jacksonUtil.getString("regId");
				
				Map<String,Object> mapData = jacksonUtil.getMap("data");
				
				if(null==mapData){
					errMsg[0] = "1";
					errMsg[1] = "参数错误。";
					return strRet;
				}
				
				int order = mapData.get("order")==null?0:Integer.parseInt(String.valueOf(mapData.get("order")));
				String optId = String.valueOf(mapData.get("optId"));
				String optName = String.valueOf(mapData.get("optName"));
				
				//英文描述
				String optEnName = "";
				if(mapData.containsKey("optEnName")){
					optEnName=String.valueOf(mapData.get("optEnName"));
				}
				
				//是否默认选中
				String isSelect = "N";
				if(mapData.containsKey("isSelect")){
					boolean selected =Boolean.parseBoolean(String.valueOf(mapData.get("isSelect")));
					if(selected){
						isSelect="Y";
					}
				}
				
				//String sql = "select 'Y' from PS_TZ_YHZC_XXZ_TBL where TZ_JG_ID=? and TZ_REG_FIELD_ID=? and TZ_OPT_ID=?";
				String sql = "select 'Y' from PS_TZ_YHZC_XXZ_TBL where TZ_SITEI_ID=? and TZ_REG_FIELD_ID=? and TZ_OPT_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[]{siteId,regId,optId}, "String");
				
				PsTzYhzcXxzTbl psTzYhzcXxzTbl = new PsTzYhzcXxzTbl(); 
				psTzYhzcXxzTbl.setTzSiteiId(siteId);
				psTzYhzcXxzTbl.setTzJgId(orgid);
				psTzYhzcXxzTbl.setTzRegFieldId(regId);
				psTzYhzcXxzTbl.setTzOptId(optId);
				psTzYhzcXxzTbl.setTzOptValue(optName);
				psTzYhzcXxzTbl.setTzSelectFlg(isSelect);
				psTzYhzcXxzTbl.setTzOrder(order);
				
				if(!"Y".equals(recExists)){
					psTzYhzcXxzTblMapper.insert(psTzYhzcXxzTbl);
				}else{
					psTzYhzcXxzTblMapper.updateByPrimaryKey(psTzYhzcXxzTbl);
				}
				
				//处理英文描述部分 - 开始
				//sql = "select 'Y' from PS_TZ_YHZC_XXZ_ENG where TZ_JG_ID=? and TZ_REG_FIELD_ID=? and TZ_OPT_ID=? and LANGUAGE_CD=?";
				sql = "select 'Y' from PS_TZ_YHZC_XXZ_ENG where TZ_SITEI_ID=? and TZ_REG_FIELD_ID=? and TZ_OPT_ID=? and LANGUAGE_CD=?";
				recExists = sqlQuery.queryForObject(sql, new Object[]{siteId,regId,optId,languageCd}, "String");
				
				PsTzYhzcXxzEng psTzYhzcXxzEng = new PsTzYhzcXxzEng();
				psTzYhzcXxzEng.setTzSiteiId(siteId);
				psTzYhzcXxzEng.setTzJgId(orgid);
				psTzYhzcXxzEng.setTzRegFieldId(regId);
				psTzYhzcXxzEng.setTzOptId(optId);
				psTzYhzcXxzEng.setLanguageCd(languageCd);
				psTzYhzcXxzEng.setTzOptValue(optEnName);
				
				if("Y".equals(recExists)){
					psTzYhzcXxzEngMapper.updateByPrimaryKey(psTzYhzcXxzEng);
				}else{
					if(!"".equals(optEnName) && optEnName!=null){
						psTzYhzcXxzEngMapper.insert(psTzYhzcXxzEng);
					}
				}
				//处理英文描述部分 - 结束
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			//String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String languageCd = "ENG";
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 站点ID;
				String siteId = jacksonUtil.getString("siteId");;
				// 注册项ID;
				String regId = jacksonUtil.getString("regId");
				
				Map<String,Object> mapData = jacksonUtil.getMap("data");
				
				//int order = mapData.get("order")==null?0:Integer.parseInt(String.valueOf(mapData.get("order")));
				String optId = String.valueOf(mapData.get("optId"));
				//String optName = String.valueOf(mapData.get("optName"));
				
				if(!"".equals(optId) && null!=optId){
					
					PsTzYhzcXxzTblKey psTzYhzcXxzTblKey = new PsTzYhzcXxzTblKey();
					//psTzYhzcXxzTblKey.setTzJgId(orgid);
					psTzYhzcXxzTblKey.setTzSiteiId(siteId);
					psTzYhzcXxzTblKey.setTzOptId(optId);
					psTzYhzcXxzTblKey.setTzRegFieldId(regId);
					
					psTzYhzcXxzTblMapper.deleteByPrimaryKey(psTzYhzcXxzTblKey);
					
					//删除双语表里面的数据 - 开始
					PsTzYhzcXxzEngKey psTzYhzcXxzEngKey = new PsTzYhzcXxzEngKey();
					psTzYhzcXxzEngKey.setTzSiteiId(siteId);
					//psTzYhzcXxzEngKey.setTzJgId(orgid);
					psTzYhzcXxzEngKey.setTzOptId(optId);
					psTzYhzcXxzEngKey.setTzRegFieldId(regId);
					psTzYhzcXxzEngKey.setLanguageCd(languageCd);
					psTzYhzcXxzEngMapper.deleteByPrimaryKey(psTzYhzcXxzEngKey);
					//删除双语表里面的数据 - 结束
					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

}
