package com.tranzvision.gd.TZProjectSetBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：招生项目详情专业方向、管理人员、班级管理列表查询
 * 原PS类：TZ_GD_PROMG_PKG:TZ_GD_PROINFO_CLS
 */
@Service("com.tranzvision.gd.TZProjectSetBundle.service.impl.tzProInfoClsServiceImpl")
public class tzProInfoClsServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	
	/* 查询项目分类列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		
		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);
		String strQueryType = jacksonUtil.getString("queryType");
		String strProjectId = jacksonUtil.getString("projectId");
		
		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try{
			if ("ZYFX".equals(strQueryType)) {
				String sql = "SELECT TZ_MAJOR_ID,TZ_MAJOR_NAME,TZ_SORT_NUM FROM PS_TZ_PRJ_MAJOR_T WHERE TZ_PRJ_ID= ? ORDER BY TZ_SORT_NUM";
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { strProjectId });
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;
					String strMajorId = "";
					String strMajorName = "";
					String strSortNum = "";
					strMajorId = String.valueOf(mapData.get("TZ_MAJOR_ID"));
					strMajorName = String.valueOf(mapData.get("TZ_MAJOR_NAME"));
					strSortNum = String.valueOf(mapData.get("TZ_SORT_NUM"));
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("professionId", strMajorId);
					mapJson.put("professionName", strMajorName);
					mapJson.put("sortNum", strSortNum);
					mapJson.put("isSaved", "Y");
					listJson.add(mapJson);
				}
			} else if("GLRY".equals(strQueryType)) {
				String sql = "SELECT A.OPRID,TZ_DLZH_ID,TZ_JG_ID,TZ_REALNAME,TZ_MOBILE,TZ_EMAIL FROM PS_TZ_PRJ_ADMIN_T A, PS_TZ_AQ_YHXX_TBL B WHERE A.OPRID = B.OPRID AND TZ_PRJ_ID = ?";
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { strProjectId });
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;
					String strGmOprId = "";
					String strGmDlzhId = "";
					String strGmJgId= "";
					String strGmName = "";
					String strGmMobile = "";
					String strGmEmail = "";
					strGmOprId = String.valueOf(mapData.get("OPRID"));
					strGmDlzhId = String.valueOf(mapData.get("TZ_DLZH_ID"));
					strGmJgId = String.valueOf(mapData.get("TZ_JG_ID"));
					strGmName = String.valueOf(mapData.get("TZ_REALNAME"));
					strGmMobile = String.valueOf(mapData.get("TZ_MOBILE"));
					strGmEmail = String.valueOf(mapData.get("TZ_EMAIL"));
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("managerOprid", strGmOprId);
					mapJson.put("managerAccNo", strGmDlzhId);
					mapJson.put("orgId", strGmJgId);
					mapJson.put("managerName", strGmName);
					mapJson.put("managerPhone", strGmMobile);
					mapJson.put("managerEmail", strGmEmail);
					listJson.add(mapJson);
				}
			}else if("BJGL".equals(strQueryType)) {
				
				int total = 0;
				// 查询总数;
				String totalSQL = "SELECT COUNT(1) FROM PS_TZ_CLASS_INF_T WHERE TZ_PRJ_ID= ?";
				total = sqlQuery.queryForObject(totalSQL, new Object[] { strProjectId },"Integer");
				String sql = "SELECT TZ_CLASS_ID,TZ_CLASS_NAME,TZ_START_DT,TZ_END_DT,TZ_IS_APP_OPEN FROM PS_TZ_CLASS_INF_T WHERE TZ_PRJ_ID= ? LIMIT ?,?";
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { strProjectId, numStart, numLimit});
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;
					String strClassId = "";
					String strClassName = "";
					String strStartDate= "";
					String strEndDate = "";
					String strIsOpen = "";
					String strIsOpenDesc = "";
					strClassId = String.valueOf(mapData.get("TZ_CLASS_ID"));
					strClassName = String.valueOf(mapData.get("TZ_CLASS_NAME"));
					strStartDate = String.valueOf(mapData.get("TZ_START_DT"));
					strEndDate = String.valueOf(mapData.get("TZ_START_DT"));
					strIsOpen = String.valueOf(mapData.get("TZ_IS_APP_OPEN"));
					if("Y".equals(strIsOpen)){
						strIsOpenDesc = "开通";
					}else{
						strIsOpenDesc = "未开通";
					}
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("classId", strClassId);
					mapJson.put("className", strClassName);
					mapJson.put("startDate", strStartDate);
					mapJson.put("endDate", strEndDate);
					mapJson.put("openAppOnline", strIsOpenDesc);
					listJson.add(mapJson);
					mapRet.replace("total",total);
				}
			}
			mapRet.replace("root", listJson);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
