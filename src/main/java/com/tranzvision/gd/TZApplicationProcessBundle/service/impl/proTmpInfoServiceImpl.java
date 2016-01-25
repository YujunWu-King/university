package com.tranzvision.gd.TZApplicationProcessBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZApplicationProcessBundle.dao.PsTzAppProTmpTMapper;
import com.tranzvision.gd.TZApplicationProcessBundle.dao.PsTzAppProStpTMapper;
import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProTmpT;
import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProStpTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：项目流程配置管理页面;
 * 原PS类：TZ_GD_PROTMPLATE_PKG:TZ_GD_PROTMPLATEXX_CLS
 */
@Service("com.tranzvision.gd.TZApplicationProcessBundle.service.impl.proTmpInfoServiceImpl")
public class proTmpInfoServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzAppProTmpTMapper PsTzAppProTmpTMapper;
	@Autowired
	private PsTzAppProStpTMapper PsTzAppProStpTMapper;	
	
	/* 查询报名流程列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		
		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);

		String strAppProcessTmpId = jacksonUtil.getString("TZ_APPPRO_TMP_ID");
		
		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try{
			int total = 0;
			// 查询总数;
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_APPPRO_STP_T WHERE TZ_APPPRO_TMP_ID=?";
			total = sqlQuery.queryForObject(totalSQL, new Object[] { strAppProcessTmpId },"Integer");
			String sql = "SELECT TZ_APPPRO_ID,TZ_SORT_NUM, TZ_APPPRO_NAME,TZ_DEF_CONTENT FROM PS_TZ_APPPRO_STP_T WHERE TZ_APPPRO_TMP_ID= ? order by TZ_SORT_NUM LIMIT ?,?";
			List<?> listData = sqlQuery.queryForList(sql, new Object[] { strAppProcessTmpId,numStart,numLimit });
			for (Object objData : listData) {

				Map<String, Object> mapData = (Map<String, Object>) objData;
				String strAppProcessId = "";
				String strAppProcessName = "";
				String strSortNum;
				String strAppProcessDefContent = "";

				strAppProcessId = String.valueOf(mapData.get("TZ_APPPRO_ID"));
				strAppProcessName = String.valueOf(mapData.get("TZ_APPPRO_NAME"));
				strSortNum = String.valueOf(mapData.get("TZ_SORT_NUM"));
				strAppProcessDefContent = String.valueOf(mapData.get("TZ_DEF_CONTENT"));
				
				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("TZ_APPPRO_ID", strAppProcessId);
				mapJson.put("TZ_SORT_NUM", strSortNum);
				mapJson.put("TZ_APPPRO_NAME", strAppProcessName);
				mapJson.put("TZ_DEF_CONTENT", strAppProcessDefContent);
				mapJson.put("TZ_APPPRO_TMP_ID", strAppProcessTmpId);
				listJson.add(mapJson);
				mapRet.replace("total",total);
			}
			mapRet.replace("root", listJson);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
 
		return jacksonUtil.Map2json(mapRet);
	}
	
	/**
	 * 新建模版
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJson = new HashMap<String, Object>();
		returnJson.put("formData", "{}");
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("ORG".equals(typeFlag)) {

					String strAppProcessTmpId = String.valueOf(mapData.get("TZ_APPPRO_TMP_ID"));
					String strAppProcessTmpName = String.valueOf(mapData.get("TZ_APPPRO_TMP_NAME"));
					String strAppProcessTmpStatus = String.valueOf(mapData.get("TZ_APPPRO_STATUS"));
					/*模版名称是否已经存在*/
					String sql = "SELECT count(1) FROM PS_TZ_APPPRO_TMP_T WHERE TZ_JG_ID= ? AND TZ_APPPRO_TMP_ID <> ? AND TZ_APPPRO_TMP_NAME= ?";

					int isExistAppProcessTmpNum = sqlQuery.queryForObject(sql, new Object[] { orgid,strAppProcessTmpId,strAppProcessTmpName }, "Integer");
					
					if (isExistAppProcessTmpNum == 0) {
						
						if("".equals(strAppProcessTmpId) ||  "NEXT".equals(strAppProcessTmpId.toUpperCase())){
							strAppProcessTmpId = String.valueOf(getSeqNum.getSeqNum("PS_TZ_APPPRO_TMP_T", "TZ_APPPRO_TMP_ID"));
						}
						
						PsTzAppProTmpT psTzAppProTmpT = new PsTzAppProTmpT();
						psTzAppProTmpT.setTzAppproTmpId(strAppProcessTmpId);
						psTzAppProTmpT.setTzAppproTmpName(strAppProcessTmpName);
						psTzAppProTmpT.setTzAppproStatus(strAppProcessTmpStatus);
						psTzAppProTmpT.setTzJgId(orgid);
						psTzAppProTmpT.setRowAddedDttm(new Date());
						psTzAppProTmpT.setRowAddedOprid(oprid);
						psTzAppProTmpT.setRowLastmantDttm(new Date());
						psTzAppProTmpT.setRowLastmantOprid(oprid);
						
						PsTzAppProTmpTMapper.insert(psTzAppProTmpT);

						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("TZ_APPPRO_TMP_ID", strAppProcessTmpId);
						mapJson.put("TZ_APPPRO_TMP_NAME", strAppProcessTmpName);
						mapJson.put("TZ_APPPRO_STATUS_ID", strAppProcessTmpStatus);
						returnJson.replace("formData", mapJson);
						
					}else{
						errMsg[0] = "1";
						errMsg[1] = "报名流程模板名称不能重复！";
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJson);
		return strRet;
	}
	
	/**
	 * 更新模版
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJson = new HashMap<String, Object>();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("ORG".equals(typeFlag)) {

					String strAppProcessTmpId = String.valueOf(mapData.get("TZ_APPPRO_TMP_ID"));
					String strAppProcessTmpName = String.valueOf(mapData.get("TZ_APPPRO_TMP_NAME"));
					String strAppProcessTmpStatus = String.valueOf(mapData.get("TZ_APPPRO_STATUS"));
					/*模版名称是否已经存在*/
					String sql = "SELECT count(1) FROM PS_TZ_APPPRO_TMP_T WHERE TZ_JG_ID= ? AND TZ_APPPRO_TMP_ID <> ? AND TZ_APPPRO_TMP_NAME= ?";

					int isExistAppProcessTmpNum = sqlQuery.queryForObject(sql, new Object[] { orgid,strAppProcessTmpId,strAppProcessTmpName }, "Integer");
					
					if (isExistAppProcessTmpNum == 0) {
					
						String sqlGetAppProcessTmp = "select COUNT(1) from PS_TZ_APPPRO_TMP_T WHERE TZ_APPPRO_TMP_ID=?";
						int count = sqlQuery.queryForObject(sqlGetAppProcessTmp, new Object[] { strAppProcessTmpId }, "Integer");
						if (count > 0) {
							PsTzAppProTmpT psTzAppProTmpT = new PsTzAppProTmpT();
							psTzAppProTmpT.setTzAppproTmpId(strAppProcessTmpId);
							psTzAppProTmpT.setTzAppproTmpName(strAppProcessTmpName);
							psTzAppProTmpT.setTzAppproStatus(strAppProcessTmpStatus);
							psTzAppProTmpT.setTzJgId(orgid);
							psTzAppProTmpT.setRowAddedDttm(new Date());
							psTzAppProTmpT.setRowAddedOprid(oprid);
							psTzAppProTmpT.setRowLastmantDttm(new Date());
							psTzAppProTmpT.setRowLastmantOprid(oprid);
							
							PsTzAppProTmpTMapper.updateByPrimaryKeySelective(psTzAppProTmpT);
	
							Map<String, Object> mapJson = new HashMap<String, Object>();
							mapJson.put("TZ_APPPRO_TMP_ID", strAppProcessTmpId);
							mapJson.put("TZ_APPPRO_TMP_NAME", strAppProcessTmpName);
							mapJson.put("TZ_APPPRO_STATUS_ID", strAppProcessTmpStatus);
							returnJson.replace("formData", mapJson);
							
						}else{
							errMsg[0] = "1";
							errMsg[1] = "报名流程模版"+ strAppProcessTmpName +"不存在！";
						}	
					}else{
						errMsg[0] = "1";
						errMsg[1] = "报名流程模板名称不能重复！";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJson);
		return strRet;
	}
	
	/**
	 * 删除报名流程
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 流程模版编号;
				String strAppProcessTmpId = jacksonUtil.getString("TZ_APPPRO_TMP_ID");
				/*流程编号*/
				String strAppProcessId = jacksonUtil.getString("TZ_APPPRO_ID");
				
				if (strAppProcessTmpId != null && !"".equals(strAppProcessTmpId) && strAppProcessId != null && !"".equals(strAppProcessId)) {
					PsTzAppProStpTKey psTzAppProStpTKey = new PsTzAppProStpTKey();
					psTzAppProStpTKey.setTzAppproTmpId(strAppProcessTmpId);
					psTzAppProStpTKey.setTzAppproId(strAppProcessId);
					PsTzAppProStpTMapper.deleteByPrimaryKey(psTzAppProStpTKey);
					/*同时删除流程对应的常用回复短语信息*/
					Object[] args = new Object[] { strAppProcessTmpId,strAppProcessId };
					sqlQuery.update("DELETE FROM PS_TZ_APPPRO_HF_T WHERE TZ_APPPRO_TMP_ID = ? AND TZ_APPPRO_ID=?", args);
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
