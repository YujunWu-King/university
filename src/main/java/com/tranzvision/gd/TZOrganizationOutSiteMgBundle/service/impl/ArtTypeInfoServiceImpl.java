package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

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
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao.PsTzArtTypeTMapper;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao.PsTzContFldefTMapper;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzArtTypeT;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzContFldefT;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzContFldefTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author zhangbb
 * @version 创建时间：2016年8月16日 下午16:28:30 类说明 内容类型定义
 */
@Service("com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl.ArtTypeInfoServiceImpl")
public class ArtTypeInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzArtTypeTMapper psTzArtTypeTMapper;
	
	@Autowired
	private PsTzContFldefTMapper PsTzContFldefTMapper;
	
	/* 查询类型类型列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		System.out.println("不生效吗");
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		String artTypeId = jacksonUtil.getString("artTypeId");
		try {
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_CONT_FLDEF_T where TZ_ART_TYPE_ID = ?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { artTypeId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;

			sql = "SELECT TZ_FIELD_VALUE,TZ_FIELD_DESC,TZ_SEQ,IS_ENABLED_FLG FROM PS_TZ_CONT_FLDEF_T where TZ_ART_TYPE_ID = ? ORDER BY TZ_SEQ ASC";
			list = jdbcTemplate.queryForList(sql, new Object[] { artTypeId });
			Map<String, Object> jsonMap;
			if (list != null && total > 0 ) {

				for (int i = 0; i < list.size(); i++) {
					boolean used = false;
					if ("Y".equals(list.get(i).get("IS_ENABLED_FLG"))) {
						used = true;
					}
					jsonMap = new HashMap<String, Object>();
					jsonMap.put("fieldValue", list.get(i).get("TZ_FIELD_VALUE"));
					jsonMap.put("fieldDescr", list.get(i).get("TZ_FIELD_DESC"));
					jsonMap.put("seq", list.get(i).get("TZ_SEQ"));
					jsonMap.put("isused", used);
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
				strRet = jacksonUtil.Map2json(returnJsonMap);
			}else{
				total = 9;
				for (int i = 0; i < 9; i++) {
					if(i<4){
						jsonMap = new HashMap<String, Object>();
						jsonMap.put("fieldValue", "TZ_TXT" + String.valueOf(i + 1));
						jsonMap.put("fieldDescr", "描述字段" + String.valueOf(i + 1 ));
						jsonMap.put("seq", String.valueOf(i + 1));
						jsonMap.put("isused", false);
						arraylist.add(jsonMap);
					}else if(i<7){
						jsonMap = new HashMap<String, Object>();
						jsonMap.put("fieldValue", "TZ_LONG" + String.valueOf(i - 3 ));
						jsonMap.put("fieldDescr", "描述字段" + String.valueOf(i + 1));
						jsonMap.put("seq", String.valueOf(i + 1));
						jsonMap.put("isused", false);
						arraylist.add(jsonMap);
					}else{
						jsonMap = new HashMap<String, Object>();
						jsonMap.put("fieldValue", "TZ_DATE" + String.valueOf(i - 6));
						jsonMap.put("fieldDescr", "描述字段" + String.valueOf(i + 1));
						jsonMap.put("seq", String.valueOf(i + 1));
						jsonMap.put("isused", false);
						arraylist.add(jsonMap);
					}
				}
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 获取内容类型定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("artTypeId")) {
				// hardcode ID;
				String artTypeId = jacksonUtil.getString("artTypeId");
				PsTzArtTypeT psTzArtTypeT=  psTzArtTypeTMapper.selectByPrimaryKey(artTypeId);
				if (psTzArtTypeT != null) {
					returnJsonMap.put("artTypeId", psTzArtTypeT.getTzArtTypeId());
					returnJsonMap.put("artTypeName", psTzArtTypeT.getTzArtTypeName());
					returnJsonMap.put("isused", psTzArtTypeT.getIsEnabledFlg());
					
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该内容类型数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该内容类型数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	@Override
	/* 新增内容类型 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String artTypeId = "";
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				
				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData;
				
				if ("ARTTYPEINFO".equals(typeFlag)) {
					mapData = jacksonUtil.getMap("data");
					artTypeId = String.valueOf(mapData.get("artTypeId"));
					String artTypeName = String.valueOf(mapData.get("artTypeName"));
					String isused = String.valueOf(mapData.get("isused"));
	
					String sql = "select COUNT(1) from PS_TZ_ART_TYPE_T WHERE TZ_ART_TYPE_NAME=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { artTypeName }, "Integer");
					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "类型名称：" + artTypeName + ",已经存在";
					} else {
						
						if("".equals(artTypeId) ||  "NEXT".equals(artTypeId.toUpperCase())){
							artTypeId = String.valueOf(getSeqNum.getSeqNum("PS_TZ_ART_TYPE_T", "TZ_ART_TYPE_ID"));
						}
						String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
						PsTzArtTypeT psTzArtTypeT = new PsTzArtTypeT();
						psTzArtTypeT.setTzArtTypeId(artTypeId);
						psTzArtTypeT.setTzArtTypeName(artTypeName);
						psTzArtTypeT.setIsEnabledFlg(isused);
						psTzArtTypeT.setRowAddedDttm(new Date());
						psTzArtTypeT.setRowAddedOprid(oprid);
						psTzArtTypeT.setRowLastmantDttm(new Date());
						psTzArtTypeT.setRowLastmantOprid(oprid);
						psTzArtTypeTMapper.insert(psTzArtTypeT);
						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("artTypeId", artTypeId);
						strRet = jacksonUtil.Map2json(mapJson);
					}
				}
				
				if ("saveGrid".equals(typeFlag)) {

					if(!"".equals(artTypeId) && !"NEXT".equals(artTypeId)){
						List<?> listGridData = jacksonUtil.getList("data");
						for (Object objGrid : listGridData) {
							Map<String, Object> mapGridRow = (Map<String, Object>) objGrid;
							String fieldValue = String.valueOf(mapGridRow.get("fieldValue"));
							String sql = "select count(1) from PS_TZ_CONT_FLDEF_T where TZ_ART_TYPE_ID=? and TZ_FIELD_VALUE=?";
							int count = jdbcTemplate.queryForObject(sql, new Object[] { artTypeId, fieldValue }, "Integer");
							
							String fieldDescr = String.valueOf(mapGridRow.get("fieldDescr"));
							int seq = Integer.parseInt(String.valueOf(mapGridRow.get("seq")));
							String isused = "";
							if (Boolean.parseBoolean(String.valueOf(mapGridRow.get("isused")))) {
								isused = "Y";
							} else {
								isused = "N";
							}
							PsTzContFldefT psTzContFldefT = new PsTzContFldefT();
							psTzContFldefT.setTzArtTypeId(artTypeId);
							psTzContFldefT.setTzFieldValue(fieldValue);
							psTzContFldefT.setTzFieldDesc(fieldDescr);
							psTzContFldefT.setIsEnabledFlg(isused);
							psTzContFldefT.setTzSeq(seq);
							if (count > 0) {
								PsTzContFldefTMapper.updateByPrimaryKeySelective(psTzContFldefT);
							}else{
								PsTzContFldefTMapper.insert(psTzContFldefT);
							}
						}
					}else{
						errMsg[0] = "1";
						errMsg[1] = "类型编号生成出错。";
					}
				}	
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	@Override
	/* 修改内容类型方法 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String artTypeId = "";
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				
				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData;
				if ("ARTTYPEINFO".equals(typeFlag)) {
					mapData = jacksonUtil.getMap("data");
					artTypeId = String.valueOf(mapData.get("artTypeId"));
					String artTypeName = String.valueOf(mapData.get("artTypeName"));
					String isused = String.valueOf(mapData.get("isused"));
					String sql = "select COUNT(1) from PS_TZ_ART_TYPE_T WHERE TZ_ART_TYPE_NAME=? AND TZ_ART_TYPE_ID <> ?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { artTypeName,artTypeId }, "Integer");
					
					sql = "select COUNT(1) from PS_TZ_ART_TYPE_T WHERE TZ_ART_TYPE_ID = ?";
					int count2 = jdbcTemplate.queryForObject(sql, new Object[] { artTypeId }, "Integer");
					
					if (count2 > 0) {
						if (count > 0) {
							errMsg[0] = "1";
							errMsg[1] = "类型名称：" + artTypeName + ",已经存在";
						}else{
							String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
							PsTzArtTypeT psTzArtTypeT = new PsTzArtTypeT();
							psTzArtTypeT.setTzArtTypeId(artTypeId);
							psTzArtTypeT.setTzArtTypeName(artTypeName);
							psTzArtTypeT.setIsEnabledFlg(isused);
							psTzArtTypeT.setRowAddedDttm(new Date());
							psTzArtTypeT.setRowAddedOprid(oprid);
							psTzArtTypeT.setRowLastmantDttm(new Date());
							psTzArtTypeT.setRowLastmantOprid(oprid);
							psTzArtTypeTMapper.updateByPrimaryKey(psTzArtTypeT);
							Map<String, Object> mapJson = new HashMap<String, Object>();
							mapJson.put("artTypeId", artTypeId);
							strRet = jacksonUtil.Map2json(mapJson);
						}
					}else {
						errMsg[0] = "1";
						errMsg[1] = "更新的内容类型：" + artTypeName + ",不存在";
					}
				}
				if ("saveGrid".equals(typeFlag)) {
					System.out.println(artTypeId+"T1");
					if(!"".equals(artTypeId) && !"NEXT".equals(artTypeId)){
						System.out.println(artTypeId+"T2");
						List<?> listGridData = jacksonUtil.getList("data");
						for (Object objGrid : listGridData) {
							
							Map<String, Object> mapGridRow = (Map<String, Object>) objGrid;
							String fieldValue = String.valueOf(mapGridRow.get("fieldValue"));
							String sql = "select count(1) from PS_TZ_CONT_FLDEF_T where TZ_ART_TYPE_ID=? and TZ_FIELD_VALUE=?";
							int count = jdbcTemplate.queryForObject(sql, new Object[] { artTypeId, fieldValue }, "Integer");
							
							String fieldDescr = String.valueOf(mapGridRow.get("fieldDescr"));
							int seq = Integer.parseInt(String.valueOf(mapGridRow.get("seq")));
							String isused = "";
							if (Boolean.parseBoolean(String.valueOf(mapGridRow.get("isused")))) {
								isused = "Y";
							} else {
								isused = "N";
							}
							PsTzContFldefT psTzContFldefT = new PsTzContFldefT();
							psTzContFldefT.setTzArtTypeId(artTypeId);
							psTzContFldefT.setTzFieldValue(fieldValue);
							psTzContFldefT.setTzFieldDesc(fieldDescr);
							psTzContFldefT.setIsEnabledFlg(isused);
							psTzContFldefT.setTzSeq(seq);
							if (count > 0) {
								PsTzContFldefTMapper.updateByPrimaryKeySelective(psTzContFldefT);
							}else{
								PsTzContFldefTMapper.insert(psTzContFldefT);
							}
						}
					}else{
						errMsg[0] = "1";
						errMsg[1] = "类型编号生成出错。";
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
