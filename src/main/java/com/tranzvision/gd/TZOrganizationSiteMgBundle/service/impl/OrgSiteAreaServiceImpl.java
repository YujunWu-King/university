package com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;

/**
 * 区域设置；原：TZ_GD_ZDDY_PKG:TZ_GD_AREAGL_CLS
 * 
 * @author tang
 * @since 2015-11-18
 * 
 */
@Service("com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl.OrgSiteAreaServiceImpl")
public class OrgSiteAreaServiceImpl extends FrameworkImpl {
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;
	
	/* 添加区域设置 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("areaId", "");

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String areaname = jacksonUtil.getString("areaname");
				String areastate = jacksonUtil.getString("areastate");
				String areatypeid = jacksonUtil.getString("areatypeid");
				String areaposition = jacksonUtil.getString("areaposition");
				String strAreasxh = jacksonUtil.getString("areasxh");
				String arealm = jacksonUtil.getString("arealm");
				String areacode = jacksonUtil.getString("areacode");
				String areaid = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_AREA_T", "TZ_AREA_ID"));
				
				
				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaT = new PsTzSiteiAreaTWithBLOBs();
				psTzSiteiAreaT.setTzSiteiId(siteId);
				psTzSiteiAreaT.setTzAreaId(areaid);
				psTzSiteiAreaT.setTzAreaName(areaname);
				psTzSiteiAreaT.setTzAreaState(areastate);
				psTzSiteiAreaT.setTzAreaTypeId(areatypeid);
				psTzSiteiAreaT.setTzAreaPosition(areaposition);
				if(strAreasxh != null && !"".equals(strAreasxh.trim())){
					int areasxh = Integer.parseInt(strAreasxh);
					psTzSiteiAreaT.setTzAreaXh(areasxh);
				}
				psTzSiteiAreaT.setTzColuId(arealm);
				psTzSiteiAreaT.setTzAreaCode(areacode);
				/****TODO %userid****/
				psTzSiteiAreaT.setTzAddedDttm(new Date());
				psTzSiteiAreaT.setTzAddedOprid("TZ_7");
				psTzSiteiAreaT.setTzLastmantDttm(new Date());
				psTzSiteiAreaT.setTzLastmantOprid("TZ_7");
				
				int i = psTzSiteiAreaTMapper.insert(psTzSiteiAreaT);
				if(i > 0){
					returnJsonMap.replace("areaId", areaid);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点区域设置信息保存失败";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	/* 添加区域设置 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("areaId", "");

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String areaid = jacksonUtil.getString("areaid");
				String areaname = jacksonUtil.getString("areaname");
				String areastate = jacksonUtil.getString("areastate");
				String areatypeid = jacksonUtil.getString("areatypeid");
				String areaposition = jacksonUtil.getString("areaposition");
				String strAreasxh = jacksonUtil.getString("areasxh");
				String arealm = jacksonUtil.getString("arealm");
				String areacode = jacksonUtil.getString("areacode");
				
				
				
				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaT = new PsTzSiteiAreaTWithBLOBs();
				psTzSiteiAreaT.setTzSiteiId(siteId);
				psTzSiteiAreaT.setTzAreaId(areaid);
				psTzSiteiAreaT.setTzAreaName(areaname);
				psTzSiteiAreaT.setTzAreaState(areastate);
				psTzSiteiAreaT.setTzAreaTypeId(areatypeid);
				psTzSiteiAreaT.setTzAreaPosition(areaposition);
				if(strAreasxh != null && !"".equals(strAreasxh.trim())){
					int areasxh = Integer.parseInt(strAreasxh);
					psTzSiteiAreaT.setTzAreaXh(areasxh);
				}
				psTzSiteiAreaT.setTzColuId(arealm);
				psTzSiteiAreaT.setTzAreaCode(areacode);
				/****TODO %userid****/
				psTzSiteiAreaT.setTzLastmantDttm(new Date());
				psTzSiteiAreaT.setTzLastmantOprid("TZ_7");
				
				int i = psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaT);
				if(i > 0){
					returnJsonMap.replace("areaId", areaid);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点区域设置信息保存失败";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");

		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("siteId")) {
				// 站点id, 区域ID;
				String siteId = jacksonUtil.getString("siteId");
				String areaid = jacksonUtil.getString("areaid");
				
				PsTzSiteiAreaTKey psTzSiteiAreaTKey = new PsTzSiteiAreaTKey();
				psTzSiteiAreaTKey.setTzSiteiId(siteId);
				psTzSiteiAreaTKey.setTzAreaId(areaid);
	
				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaT = psTzSiteiAreaTMapper.selectByPrimaryKey(psTzSiteiAreaTKey);
				if(psTzSiteiAreaT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("areaid", psTzSiteiAreaT.getTzAreaId());
					jsonMap.put("areaname",psTzSiteiAreaT.getTzAreaName() );
					jsonMap.put("areastate", psTzSiteiAreaT.getTzAreaState());
					jsonMap.put("areatypeid",psTzSiteiAreaT.getTzAreaTypeId() );
					jsonMap.put("areaposition", psTzSiteiAreaT.getTzAreaPosition());
					jsonMap.put("areasxh", psTzSiteiAreaT.getTzAreaXh());
					jsonMap.put("arealm", psTzSiteiAreaT.getTzColuId());
					jsonMap.put("areacode",psTzSiteiAreaT.getTzAreaCode());
					returnJsonMap.replace("formData", jsonMap);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "未找到对应区域信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	/* 删除站点区域设置 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// 站点ID;
				String siteId = jacksonUtil.getString("siteId");
				// 区域ID;
				String areaid = jacksonUtil.getString("areaid");
				if (siteId != null && !"".equals(siteId) && areaid != null && !"".equals(areaid)) {
					PsTzSiteiAreaTKey psTzSiteiAreaTKey = new PsTzSiteiAreaTKey();
					psTzSiteiAreaTKey.setTzSiteiId(siteId);
					psTzSiteiAreaTKey.setTzAreaId(areaid);
					psTzSiteiAreaTMapper.deleteByPrimaryKey(psTzSiteiAreaTKey);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
}
