package com.tranzvision.gd.TZAdditionalInformationBundle.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZAdditionalInformationBundle.dao.PsTzSbmInfStpTMapper;
import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfStpT;
import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfStpTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：递交资料详情类;
 * 原PS类：TZ_GD_SMTDTMG_PKG:TZ_GD_data4_CLS
 */
@Service("com.tranzvision.gd.TZAdditionalInformationBundle.service.impl.additionalInfoDetailServiceImpl")
public class additionalInfoDetailServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSbmInfStpTMapper PsTzSbmInfStpTMapper;
	
	/**
	 * 添加资料模版
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				//提交资料模版编号
				String strAdditionalInfoTmpId = jacksonUtil.getString("smtDtTmpID");
				//提交资料内容编号
				String strAdditionalInfoContentId = jacksonUtil.getString("smtDtID");
				//资料内容简介
				String strAdditionalInfoContent = jacksonUtil.getString("content");
				//资料备注
				String strAdditionalInfoRemark = jacksonUtil.getString("remark");
				/*资料名称是否已经存在*/
				int isExistAdditionalInfoContentNum = 0;
				String sql = "";
				if("".equals(strAdditionalInfoContentId) ||  "NEXT".equals(strAdditionalInfoContentId.toUpperCase())){
					sql = "SELECT COUNT(1) FROM PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID = ? AND TZ_SBMINF_ID <> ? AND TZ_CONT_INTRO = ?";
					isExistAdditionalInfoContentNum = sqlQuery.queryForObject(sql, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId,strAdditionalInfoContentId }, "Integer");
				}else{
					sql = "SELECT COUNT(1) FROM PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID = ? AND TZ_CONT_INTRO = ?";
					isExistAdditionalInfoContentNum = sqlQuery.queryForObject(sql, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId }, "Integer");
				}

				if (isExistAdditionalInfoContentNum == 0) {
					
					if("".equals(strAdditionalInfoTmpId) ||  "NEXT".equals(strAdditionalInfoTmpId.toUpperCase())){
						errMsg[0] = "1";
						errMsg[1] = "请先保存补充资料模版！";
					}else{
						if("".equals(strAdditionalInfoContentId) ||  "NEXT".equals(strAdditionalInfoContentId.toUpperCase())){
							strAdditionalInfoContentId = "GD_DT_" + String.valueOf(getSeqNum.getSeqNum("TZ_SBMINF_STP_T", "TZ_SBMINF_ID"));
							
							/*查询获得最大的排序序号*/
							String sqlGetMaxSortNum = " SELECT MAX(TZ_SORT_NUM)  FROM PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID= ?";
							String strMaxSortNum = sqlQuery.queryForObject(sqlGetMaxSortNum, new Object[] { strAdditionalInfoTmpId }, "String");
							int maxSortNum = 0;
							if(strMaxSortNum == null){
								maxSortNum = 1;
							}else{
								maxSortNum = Integer.parseInt(strMaxSortNum) + 1;
							}

							PsTzSbmInfStpT psTzSbmInfStpT = new PsTzSbmInfStpT();
							psTzSbmInfStpT.setTzSbminfTmpId(strAdditionalInfoTmpId);
							psTzSbmInfStpT.setTzSbminfId(strAdditionalInfoContentId);
							psTzSbmInfStpT.setTzSortNum(maxSortNum);
							psTzSbmInfStpT.setTzContIntro(strAdditionalInfoContent);
							psTzSbmInfStpT.setTzRemark(strAdditionalInfoRemark);
						 
							PsTzSbmInfStpTMapper.insert(psTzSbmInfStpT);
							
							Map<String, Object> mapJson = new HashMap<String, Object>();
							mapJson.put("smtDtTmpID", strAdditionalInfoTmpId);
							mapJson.put("smtDtID", strAdditionalInfoContentId);
							mapJson.put("order", String.valueOf(maxSortNum));
							mapJson.put("content", strAdditionalInfoContent);
							mapJson.put("remark", strAdditionalInfoRemark);
							strRet = jacksonUtil.Map2json(mapJson);
						}else{
							errMsg[0] = "1";
							errMsg[1] = "补充资料内容保存失败！";
						}
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "内容简介不能重复！";
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
				
				//提交资料模版编号
				String strAdditionalInfoTmpId = jacksonUtil.getString("smtDtTmpID");
				//提交资料内容编号
				String strAdditionalInfoContentId = jacksonUtil.getString("smtDtID");
				//资料内容简介
				String strAdditionalInfoContent = jacksonUtil.getString("content");
				//资料备注
				String strAdditionalInfoRemark = jacksonUtil.getString("remark");

				/*资料简介是否已经存在*/
				int isExistAdditionalInfoContentNum = 0;
				String sql = "";
				
				sql = "SELECT COUNT(1) FROM PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID = ? AND TZ_SBMINF_ID <> ? AND TZ_CONT_INTRO = ?";
				isExistAdditionalInfoContentNum = sqlQuery.queryForObject(sql, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId,strAdditionalInfoContentId }, "Integer");

				if (isExistAdditionalInfoContentNum == 0) {
					
					String sqlGetAppProcess = "select COUNT(1) from PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID=? AND TZ_SBMINF_ID=?";
					int count = sqlQuery.queryForObject(sqlGetAppProcess, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId }, "Integer");
					if(count>0){
						
						PsTzSbmInfStpT psTzSbmInfStpT = new PsTzSbmInfStpT();
						psTzSbmInfStpT.setTzSbminfTmpId(strAdditionalInfoTmpId);
						psTzSbmInfStpT.setTzSbminfId(strAdditionalInfoContentId);
						psTzSbmInfStpT.setTzContIntro(strAdditionalInfoContent);
						psTzSbmInfStpT.setTzRemark(strAdditionalInfoRemark);
					 
						PsTzSbmInfStpTMapper.updateByPrimaryKeySelective(psTzSbmInfStpT);
						
						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("smtDtTmpID", strAdditionalInfoTmpId);
						mapJson.put("smtDtID", strAdditionalInfoContentId);
						mapJson.put("content", strAdditionalInfoContent);
						mapJson.put("remark", strAdditionalInfoRemark);
						
						returnJson.replace("formData", mapJson);
					}else{
						errMsg[0] = "1";
						errMsg[1] = "资料简介" + strAdditionalInfoContent + "不存在"; 
					}	
				}else{
					errMsg[0] = "1";
					errMsg[1] = "内容简介不能重复！";
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

			if (jacksonUtil.containsKey("TZ_SBMINF_TMP_ID") && jacksonUtil.containsKey("TZ_SBMINF_ID")) {
				// 流程模版编号;
				String strAdditionalInfoTmpId = jacksonUtil.getString("TZ_SBMINF_TMP_ID");
				/*流程编号*/ 
				String strAdditionalInfoContentId = jacksonUtil.getString("TZ_SBMINF_ID");
				
				PsTzSbmInfStpTKey psTzSbmInfStpTKey = new PsTzSbmInfStpTKey();
				psTzSbmInfStpTKey.setTzSbminfTmpId(strAdditionalInfoTmpId);
				psTzSbmInfStpTKey.setTzSbminfId(strAdditionalInfoContentId);
				PsTzSbmInfStpT psTzSbmInfStpT = PsTzSbmInfStpTMapper.selectByPrimaryKey(psTzSbmInfStpTKey);
				if (psTzSbmInfStpT != null) {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("smtDtTmpID", strAdditionalInfoTmpId);
					retMap.put("smtDtID", strAdditionalInfoContentId);
					retMap.put("order", String.valueOf(psTzSbmInfStpT.getTzSortNum()));
					retMap.put("content", psTzSbmInfStpT.getTzContIntro());
					retMap.put("remark", psTzSbmInfStpT.getTzRemark());
					strRet = jacksonUtil.Map2json(retMap);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "资料简介信息不存在！";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "资料简介信息不存在！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
