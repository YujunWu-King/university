package com.tranzvision.gd.TZApplicationProcessBundle.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZApplicationProcessBundle.dao.PsTzAppProHfTMapper;
import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProHfT;
import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProHfTKey;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：常用回复短语详情类;
 * 原PS类：TZ_GD_PROTMPLATE_PKG:TZ_GD_BACKDT_CLS
 */
@Service("com.tranzvision.gd.TZApplicationProcessBundle.service.impl.proReplyInfoServiceImpl")
public class proReplyInfoServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzAppProHfTMapper PsTzAppProHfTMapper;
	 
	/**
	 * 添加回复短语
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
				
				String strMsgId = "";
				String strMsgName = "";
				String strMsgColor = "";
				String strMsgContent = "";
				String strSysvar = "";
				String strDefaultFlag = "";
				strMsgId = jacksonUtil.getString("TZ_APPPRO_HF_BH");
				strMsgName = jacksonUtil.getString("TZ_CLS_RESULT");
				strMsgColor = jacksonUtil.getString("TZ_APPPRO_COLOR");
				strMsgContent = jacksonUtil.getString("TZ_DEF_CONTENT");
				strSysvar = jacksonUtil.getString("TZ_SYSVAR")==null?"":jacksonUtil.getString("TZ_SYSVAR");
				strDefaultFlag = jacksonUtil.getString("TZ_WFB_DEFALT_BZ");
	
				if("".equals(strAppProcessTmpId)||"NEXT".equals(strAppProcessTmpId.toUpperCase())||"".equals(strAppProcessId)||"NEXT".equals(strAppProcessId.toUpperCase())){
					errMsg[0] = "1";
					errMsg[1] = "保存失败,流程模版编号或流程编号缺失！";
				}else{
					if("".equals(strMsgId) ||  "NEXT".equals(strMsgId.toUpperCase())){
						
						strMsgId = "GD_MSG_" + String.valueOf(getSeqNum.getSeqNum("PS_TZ_APPPRO_HF_T", "TZ_APPPRO_HF_BH"));
					
						PsTzAppProHfT psTzAppProHfT = new PsTzAppProHfT();
						psTzAppProHfT.setTzAppproTmpId(strAppProcessTmpId);
						psTzAppProHfT.setTzAppproId(strAppProcessId);
						psTzAppProHfT.setTzAppproHfBh(strMsgId);
						psTzAppProHfT.setTzClsResult(strMsgName);
						psTzAppProHfT.setTzAppproColor(strMsgColor);
						psTzAppProHfT.setTzAppproContent(strMsgContent);
						psTzAppProHfT.setTzSysvar(strSysvar);
						psTzAppProHfT.setTzWfbDefaltBz(strDefaultFlag);
						PsTzAppProHfTMapper.insert(psTzAppProHfT);
						
						if("on".equals(strDefaultFlag)){
							/*将其他常用回复短语设置为N*/
							Object[] args = new Object[] { strAppProcessTmpId,strAppProcessId,strMsgId };
							sqlQuery.update("UPDATE PS_TZ_APPPRO_HF_T SET TZ_WFB_DEFALT_BZ='N' WHERE TZ_APPPRO_TMP_ID = ? AND TZ_APPPRO_ID=? AND TZ_APPPRO_HF_BH <> ?", args);
						}
						
						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("TZ_APPPRO_ID", strAppProcessId);
						mapJson.put("TZ_APPPRO_TMP_ID", strAppProcessTmpId);
						mapJson.put("TZ_APPPRO_HF_BH", strMsgId);
						returnJson.replace("formData", mapJson);
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
	 * 更新回复短语
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
				
				String strMsgId = "";
				String strMsgName = "";
				String strMsgColor = "";
				String strMsgContent = "";
				String strSysvar = "";
				String strDefaultFlag = "";
				strMsgId = jacksonUtil.getString("TZ_APPPRO_HF_BH");
				strMsgName = jacksonUtil.getString("TZ_CLS_RESULT");
				strMsgColor = jacksonUtil.getString("TZ_APPPRO_COLOR");
				strMsgContent = jacksonUtil.getString("TZ_DEF_CONTENT");
				strSysvar = jacksonUtil.getString("TZ_SYSVAR");
				strDefaultFlag = jacksonUtil.getString("TZ_WFB_DEFALT_BZ");
	
				if("".equals(strAppProcessTmpId) || "NEXT".equals(strAppProcessTmpId.toUpperCase()) || "".equals(strAppProcessId) || "NEXT".equals(strAppProcessId.toUpperCase()) || "".equals(strMsgId) ||  "NEXT".equals(strMsgId.toUpperCase())){
					errMsg[0] = "1";
					errMsg[1] = "保存失败,流程模版编号或流程编号缺失！";
				}else{
					String sqlGetAppProcess = "select COUNT(1) from PS_TZ_APPPRO_HF_T WHERE TZ_APPPRO_TMP_ID=? AND TZ_APPPRO_ID=? AND TZ_APPPRO_HF_BH = ?";
					int count = sqlQuery.queryForObject(sqlGetAppProcess, new Object[] { strAppProcessTmpId,strAppProcessId,strMsgId }, "Integer");
					if(count>0){
						PsTzAppProHfT psTzAppProHfT = new PsTzAppProHfT();
						psTzAppProHfT.setTzAppproTmpId(strAppProcessTmpId);
						psTzAppProHfT.setTzAppproId(strAppProcessId);
						psTzAppProHfT.setTzAppproHfBh(strMsgId);
						psTzAppProHfT.setTzClsResult(strMsgName);
						psTzAppProHfT.setTzAppproColor(strMsgColor);
						psTzAppProHfT.setTzAppproContent(strMsgContent);
						psTzAppProHfT.setTzSysvar(strSysvar);
						psTzAppProHfT.setTzWfbDefaltBz(strDefaultFlag);
						PsTzAppProHfTMapper.updateByPrimaryKeyWithBLOBs(psTzAppProHfT);
						if("on".equals(strDefaultFlag)){
							/*将其他常用回复短语设置为N*/
							Object[] args = new Object[] { strAppProcessTmpId,strAppProcessId,strMsgId };
							sqlQuery.update("UPDATE PS_TZ_APPPRO_HF_T SET TZ_WFB_DEFALT_BZ='N' WHERE TZ_APPPRO_TMP_ID = ? AND TZ_APPPRO_ID=? AND TZ_APPPRO_HF_BH <> ?", args);
						}
						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("TZ_APPPRO_ID", strAppProcessId);
						mapJson.put("TZ_APPPRO_TMP_ID", strAppProcessTmpId);
						mapJson.put("TZ_APPPRO_HF_BH", strMsgId);
						returnJson.replace("formData", mapJson);
					}else{
						errMsg[0] = "1";
						errMsg[1] = "该常用回复短语信息不存在。"; 
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
	
	/* 获取常用回复短语信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("formData", "{}");
		// 返回值;
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			

			if (jacksonUtil.containsKey("TZ_APPPRO_TMP_ID") && jacksonUtil.containsKey("TZ_APPPRO_ID") && jacksonUtil.containsKey("TZ_APPPRO_HF_BH")) {
				// 流程模版编号;
				String strAppProcessTmpId = jacksonUtil.getString("TZ_APPPRO_TMP_ID");
				/*流程编号*/
				String strAppProcessId = jacksonUtil.getString("TZ_APPPRO_ID");
				/*回复短语编号*/
				String strMsgId = jacksonUtil.getString("TZ_APPPRO_HF_BH");
				
				PsTzAppProHfTKey psTzAppProHfTKey = new PsTzAppProHfTKey();
				psTzAppProHfTKey.setTzAppproTmpId(strAppProcessTmpId);
				psTzAppProHfTKey.setTzAppproId(strAppProcessId);
				psTzAppProHfTKey.setTzAppproHfBh(strMsgId);
				PsTzAppProHfT psTzAppProHfT = PsTzAppProHfTMapper.selectByPrimaryKey(psTzAppProHfTKey);
				if (psTzAppProHfT != null) {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("TZ_APPPRO_TMP_ID", strAppProcessTmpId);
					retMap.put("TZ_APPPRO_ID", strAppProcessId);
					retMap.put("TZ_APPPRO_HF_BH", String.valueOf(strMsgId));
					retMap.put("TZ_APPPRO_COLOR", String.valueOf(psTzAppProHfT.getTzAppproColor()));
					retMap.put("TZ_CLS_RESULT", String.valueOf(psTzAppProHfT.getTzClsResult()));
					retMap.put("TZ_DEF_CONTENT", String.valueOf(psTzAppProHfT.getTzAppproContent()));
					retMap.put("TZ_SYSVAR", String.valueOf(psTzAppProHfT.getTzSysvar())=="null"?"":String.valueOf(psTzAppProHfT.getTzSysvar()));
					retMap.put("TZ_WFB_DEFALT_BZ", String.valueOf(psTzAppProHfT.getTzWfbDefaltBz()));
					returnMap.replace("formData", retMap);
					 
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该常用回复短语数据不存在！";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "该常用回复短语不存在！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnMap);
		return strRet;
	}
	
	/**
	 * 删除常用回复短语
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
				/*常用回复短语编号*/
				String strMsgId = jacksonUtil.getString("TZ_APPPRO_HF_BH");
				
				if (strAppProcessTmpId != null && !"".equals(strAppProcessTmpId) && strAppProcessId != null && !"".equals(strAppProcessId) && strMsgId != null && !"".equals(strMsgId)) {
					PsTzAppProHfTKey psTzAppProHfTKey = new PsTzAppProHfTKey();
					psTzAppProHfTKey.setTzAppproTmpId(strAppProcessTmpId);
					psTzAppProHfTKey.setTzAppproId(strAppProcessId);
					psTzAppProHfTKey.setTzAppproHfBh(strMsgId);
					PsTzAppProHfTMapper.deleteByPrimaryKey(psTzAppProHfTKey);
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
