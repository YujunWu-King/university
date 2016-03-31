package com.tranzvision.gd.TZApplicationProcessBundle.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZApplicationProcessBundle.dao.PsTzAppProStpTMapper;
import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProStpT;
import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProStpTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：报名流程详情类;
 * 原PS类：TZ_GD_PROTMPLATE_PKG:TZ_GD_PRO_DEFN_CLS
 */
@Service("com.tranzvision.gd.TZApplicationProcessBundle.service.impl.proDefnServiceImpl")
public class proDefnServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzAppProStpTMapper PsTzAppProStpTMapper;
	
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
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				/*模版编号*/
				String strAppProcessTmpId = jacksonUtil.getString("TZ_APPPRO_TMP_ID");
				/*流程编号*/
				String strAppProcessId = jacksonUtil.getString("TZ_APPPRO_ID");
				/*流程名称*/
				String strAppProcessName = jacksonUtil.getString("TZ_APPPRO_NAME");
				strAppProcessName = strAppProcessName.trim();
				/*流程名称是否已经存在*/
				int isExistAppProcessNum = 0;
				String sql = "";
				if("".equals(strAppProcessId) ||  "NEXT".equals(strAppProcessId.toUpperCase())){
					sql = "SELECT COUNT(1) FROM PS_TZ_APPPRO_STP_T WHERE TZ_APPPRO_TMP_ID = ? AND TZ_APPPRO_ID <> ? AND TZ_APPPRO_NAME = ?";
				    isExistAppProcessNum = sqlQuery.queryForObject(sql, new Object[] { strAppProcessTmpId,strAppProcessId,strAppProcessName }, "Integer");
				}else{
					sql = "SELECT COUNT(1) FROM PS_TZ_APPPRO_STP_T WHERE TZ_APPPRO_TMP_ID = ? AND TZ_APPPRO_NAME = ?";
					isExistAppProcessNum = sqlQuery.queryForObject(sql, new Object[] { strAppProcessTmpId,strAppProcessName }, "Integer");
				}

				if (isExistAppProcessNum == 0) {
					
					if("".equals(strAppProcessTmpId) ||  "NEXT".equals(strAppProcessTmpId.toUpperCase())){
						errMsg[0] = "1";
						errMsg[1] = "请先保存报名流程模版！";
					}else{
						if("".equals(strAppProcessId) ||  "NEXT".equals(strAppProcessId.toUpperCase())){
							strAppProcessId = "GD_DT_" + String.valueOf(getSeqNum.getSeqNum("PS_TZ_APPPRO_STP_T", "TZ_APPPRO_ID"));
							
							/*查询获得最大的排序序号*/
							String sqlGetMaxSortNum = " SELECT MAX(TZ_SORT_NUM)  FROM PS_TZ_APPPRO_STP_T WHERE TZ_APPPRO_TMP_ID= ?";
							String strMaxSortNum = sqlQuery.queryForObject(sqlGetMaxSortNum, new Object[] { strAppProcessTmpId }, "String");
							int maxSortNum = 0;
							if(strMaxSortNum == null){
								maxSortNum = 1;
							}else{
								maxSortNum = Integer.parseInt(strMaxSortNum) + 1;
							}

							PsTzAppProStpT psTzAppProStpT = new PsTzAppProStpT();
							psTzAppProStpT.setTzAppproTmpId(strAppProcessTmpId);
							psTzAppProStpT.setTzAppproId(strAppProcessId);
							psTzAppProStpT.setTzAppproName(strAppProcessName);
							psTzAppProStpT.setTzSortNum(maxSortNum);
						 
							PsTzAppProStpTMapper.insert(psTzAppProStpT);
							
							Map<String, Object> mapJson = new HashMap<String, Object>();
							mapJson.put("TZ_APPPRO_ID", strAppProcessId);
							mapJson.put("TZ_SORT_NUM", String.valueOf(maxSortNum));
							mapJson.put("TZ_APPPRO_NAME", strAppProcessName);
							mapJson.put("TZ_APPPRO_TMP_ID", strAppProcessTmpId);
							
							returnJson.replace("formData", mapJson);
						}else{
							errMsg[0] = "1";
							errMsg[1] = "报名流程保存失败！";
						}
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "报名流程步骤不能重复！";
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
		returnJson.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				/*模版编号*/
				String strAppProcessTmpId = jacksonUtil.getString("TZ_APPPRO_TMP_ID");
				/*流程编号*/
				String strAppProcessId = jacksonUtil.getString("TZ_APPPRO_ID");
				/*流程名称*/
				String strAppProcessName = jacksonUtil.getString("TZ_APPPRO_NAME");
				/*排序序号*/
				String strSortNum = jacksonUtil.getString("TZ_SORT_NUM");
				
				strAppProcessName = strAppProcessName.trim();

				/*流程名称是否已经存在*/
				int isExistAppProcessNum = 0;
				String sql = "";
				
				sql = "SELECT COUNT(1) FROM PS_TZ_APPPRO_STP_T WHERE TZ_APPPRO_TMP_ID = ? AND TZ_APPPRO_ID <> ? AND TZ_APPPRO_NAME = ?";
				isExistAppProcessNum = sqlQuery.queryForObject(sql, new Object[] { strAppProcessTmpId,strAppProcessId,strAppProcessName }, "Integer");

				if (isExistAppProcessNum == 0) {
					
					String sqlGetAppProcess = "select COUNT(1) from PS_TZ_APPPRO_STP_T WHERE TZ_APPPRO_TMP_ID=? AND TZ_APPPRO_ID=?";
					int count = sqlQuery.queryForObject(sqlGetAppProcess, new Object[] { strAppProcessTmpId,strAppProcessId }, "Integer");
					if(count>0){
						
						PsTzAppProStpT psTzAppProStpT = new PsTzAppProStpT();
						psTzAppProStpT.setTzAppproTmpId(strAppProcessTmpId);
						psTzAppProStpT.setTzAppproId(strAppProcessId);
						psTzAppProStpT.setTzAppproName(strAppProcessName);
						psTzAppProStpT.setTzSortNum(Integer.parseInt(strSortNum));
						PsTzAppProStpTMapper.updateByPrimaryKeySelective(psTzAppProStpT);
						
						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("TZ_APPPRO_ID", strAppProcessId);
						mapJson.put("TZ_SORT_NUM", String.valueOf(strSortNum));
						mapJson.put("TZ_APPPRO_NAME", strAppProcessName);
						mapJson.put("TZ_APPPRO_TMP_ID", strAppProcessTmpId);
						
						returnJson.replace("formData", mapJson);
					}else{
						errMsg[0] = "1";
						errMsg[1] = "报名流程" + strAppProcessName + "不存在"; 
					}	
				}else{
					errMsg[0] = "1";
					errMsg[1] = "报名流程步骤不能重复！";
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
	
	/* 获取流程模版信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "{}";
		// 返回值;
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("TZ_APPPRO_TMP_ID")) {
				// 流程模版编号;
				String strAppProcessTmpId = jacksonUtil.getString("TZ_APPPRO_TMP_ID");
				/*流程编号*/
				String strAppProcessId = jacksonUtil.getString("TZ_APPPRO_ID");
				
				PsTzAppProStpTKey psTzAppProStpTKey = new PsTzAppProStpT();
				psTzAppProStpTKey.setTzAppproTmpId(strAppProcessTmpId);
				psTzAppProStpTKey.setTzAppproId(strAppProcessId);
				PsTzAppProStpT psTzAppProStpT = PsTzAppProStpTMapper.selectByPrimaryKey(psTzAppProStpTKey);
				if (psTzAppProStpT != null) {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("TZ_APPPRO_TMP_ID", strAppProcessTmpId);
					retMap.put("TZ_APPPRO_ID", strAppProcessId);
					retMap.put("TZ_SORT_NUM", String.valueOf(psTzAppProStpT.getTzSortNum()));
					retMap.put("TZ_APPPRO_NAME", psTzAppProStpT.getTzAppproName());
					retMap.put("TZ_DEF_CONTENT", psTzAppProStpT.getTzDefContent());
					strRet = jacksonUtil.Map2json(retMap);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该流程数据不存在！";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "该流程数据不存在！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
