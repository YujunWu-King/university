package com.tranzvision.gd.TZAdditionalInformationBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAdditionalInformationBundle.dao.PsTzSbmInfRepTMapper;
import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfRepT;
import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfRepTKey;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：报名流程详情类;
 * 原PS类：TZ_GD_SMTDTMG_PKG:TZ_GD_data3_CLS
 */
@Service("com.tranzvision.gd.TZAdditionalInformationBundle.service.impl.additionalInfoReplyListServiceImpl")
public class additionalInfoReplyListServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSbmInfRepTMapper PsTzSbmInfRepTMapper;
	
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
		//提交资料模版编号
		String strAdditionalInfoTmpId = jacksonUtil.getString("smtDtTmpID");
		//提交资料内容编号
		String strAdditionalInfoContentId = jacksonUtil.getString("smtDtID");
		
		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try{
			int total = 0;
			// 查询总数;
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SBMINF_REP_T WHERE TZ_SBMINF_TMP_ID=? AND TZ_SBMINF_ID=?";
			total = sqlQuery.queryForObject(totalSQL, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId },"Integer");
			String sql = "SELECT TZ_SBMINF_REP_ID,TZ_SBMINF_REP,TZ_SORT_NUM FROM PS_TZ_SBMINF_REP_T WHERE TZ_SBMINF_TMP_ID= ? AND TZ_SBMINF_ID= ? LIMIT ?,?";
			List<?> listData = sqlQuery.queryForList(sql, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId,numStart,numLimit });
			for (Object objData : listData) {

				Map<String, Object> mapData = (Map<String, Object>) objData;
				String strMsgId = "";
				String strMsgName = "";
				String strOrderNum = "";
				
				strMsgId = String.valueOf(mapData.get("TZ_SBMINF_REP_ID"));
				strMsgName = String.valueOf(mapData.get("TZ_SBMINF_REP"));
				strOrderNum = String.valueOf(mapData.get("TZ_SORT_NUM"));
				
				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("smtDtTmpID", strAdditionalInfoTmpId);
				mapJson.put("smtDtID", strAdditionalInfoContentId);
				mapJson.put("msgId", strMsgId);
				mapJson.put("msgContent", strMsgName);
				mapJson.put("order", strOrderNum);
				
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
	 * 新增常用回复短语
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

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("PAGE".equals(typeFlag)) {

					String strAdditionalInfoTmpId = String.valueOf(mapData.get("smtDtTmpID"));
					String strAdditionalInfoContentId = String.valueOf(mapData.get("smtDtID"));
					String strMsgId = String.valueOf(mapData.get("msgId"));
					String strMsgContent = String.valueOf(mapData.get("msgContent"));
					String strOrderNum = String.valueOf(mapData.get("order"));
					if(strMsgContent == null || "".equals(strMsgContent)){
						errMsg[0] = "1";
						errMsg[1] = "回复短语内容不能为空。";
					}else if(strMsgContent.length()>100){
						//strMsgContent = strMsgContent.substring(1, 100);
						errMsg[0] = "1";
						errMsg[1] = "回复短语内容长度必须小于100字。";
					}else{
						if (strMsgId == null || "".equals(strMsgId)) {
							
							strMsgId = "GD_MSG_" + String.valueOf(getSeqNum.getSeqNum("TZ_SBMINF_REP_T", "TZ_SBMINF_REP_ID"));
						}
						
						String sqlGetMsgInfoTmp = "select COUNT(1) from PS_TZ_SBMINF_REP_T WHERE TZ_SBMINF_TMP_ID=? AND TZ_SBMINF_ID = ? AND TZ_SBMINF_REP_ID = ?";
						int count = sqlQuery.queryForObject(sqlGetMsgInfoTmp, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId,strMsgId }, "Integer");
						if (count > 0) {
							PsTzSbmInfRepT psTzSbmInfRepT = new PsTzSbmInfRepT();
							psTzSbmInfRepT.setTzSbminfTmpId(strAdditionalInfoTmpId);
							psTzSbmInfRepT.setTzSbminfId(strAdditionalInfoContentId);
							psTzSbmInfRepT.setTzSbminfRepId(strMsgId);
							psTzSbmInfRepT.setTzSbminfRep(strMsgContent);
							psTzSbmInfRepT.setTzSortNum(Integer.parseInt(strOrderNum));
							
							PsTzSbmInfRepTMapper.updateByPrimaryKeySelective(psTzSbmInfRepT);
						}else{
							PsTzSbmInfRepT psTzSbmInfRepT = new PsTzSbmInfRepT();
							psTzSbmInfRepT.setTzSbminfTmpId(strAdditionalInfoTmpId);
							psTzSbmInfRepT.setTzSbminfId(strAdditionalInfoContentId);
							psTzSbmInfRepT.setTzSbminfRepId(strMsgId);
							psTzSbmInfRepT.setTzSbminfRep(strMsgContent);
							psTzSbmInfRepT.setTzSortNum(Integer.parseInt(strOrderNum));
							
							PsTzSbmInfRepTMapper.insert(psTzSbmInfRepT);
						}
					}

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
	 * 更新常用回复短语
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
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

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("PAGE".equals(typeFlag)) {

					String strAdditionalInfoTmpId = String.valueOf(mapData.get("smtDtTmpID"));
					String strAdditionalInfoContentId = String.valueOf(mapData.get("smtDtID"));
					String strMsgId = String.valueOf(mapData.get("msgId"));
					String strMsgContent = String.valueOf(mapData.get("msgContent"));
					String strOrderNum = String.valueOf(mapData.get("order"));
					if(strMsgContent == null || "".equals(strMsgContent)){
						errMsg[0] = "1";
						errMsg[1] = "回复短语内容不能为空。";
					}else if(strMsgContent.length()>100){
						errMsg[0] = "1";
						errMsg[1] = "回复短语内容长度必须小于100字。";
					}else{
						if (strMsgId == null || "".equals(strMsgId)) {
							
							strMsgId = "GD_MSG_" + String.valueOf(getSeqNum.getSeqNum("TZ_SBMINF_REP_T", "TZ_SBMINF_REP_ID"));
						}
						
						String sqlGetMsgInfoTmp = "select COUNT(1) from PS_TZ_SBMINF_REP_T WHERE TZ_SBMINF_TMP_ID=? AND TZ_SBMINF_ID = ? AND TZ_SBMINF_REP_ID = ?";
						int count = sqlQuery.queryForObject(sqlGetMsgInfoTmp, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId,strMsgId }, "Integer");
						if (count > 0) {
							
							PsTzSbmInfRepT psTzSbmInfRepT = new PsTzSbmInfRepT();
							psTzSbmInfRepT.setTzSbminfTmpId(strAdditionalInfoTmpId);
							psTzSbmInfRepT.setTzSbminfId(strAdditionalInfoContentId);
							psTzSbmInfRepT.setTzSbminfRepId(strMsgId);
							psTzSbmInfRepT.setTzSbminfRep(strMsgContent);
							psTzSbmInfRepT.setTzSortNum(Integer.parseInt(strOrderNum));
							
							PsTzSbmInfRepTMapper.updateByPrimaryKeySelective(psTzSbmInfRepT);
						}else{
							PsTzSbmInfRepT psTzSbmInfRepT = new PsTzSbmInfRepT();
							psTzSbmInfRepT.setTzSbminfTmpId(strAdditionalInfoTmpId);
							psTzSbmInfRepT.setTzSbminfId(strAdditionalInfoContentId);
							psTzSbmInfRepT.setTzSbminfRepId(strMsgId);
							psTzSbmInfRepT.setTzSbminfRep(strMsgContent);
							psTzSbmInfRepT.setTzSortNum(Integer.parseInt(strOrderNum));
							
							PsTzSbmInfRepTMapper.insert(psTzSbmInfRepT);
						}
					}

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
	 * 更新常用回复短语
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

				String strAdditionalInfoTmpId = String.valueOf(jacksonUtil.getString("smtDtTmpID"));
				String strAdditionalInfoContentId = String.valueOf(jacksonUtil.getString("smtDtID"));
				String strMsgId = String.valueOf(jacksonUtil.getString("msgId"));
				
				String sqlGetMsgInfoTmp = "select COUNT(1) from PS_TZ_SBMINF_REP_T WHERE TZ_SBMINF_TMP_ID=? AND TZ_SBMINF_ID = ? AND TZ_SBMINF_REP_ID = ?";
				int count = sqlQuery.queryForObject(sqlGetMsgInfoTmp, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId,strMsgId }, "Integer");
				if (count > 0) {
					PsTzSbmInfRepTKey psTzSbmInfRepTKey = new PsTzSbmInfRepTKey();
					psTzSbmInfRepTKey.setTzSbminfTmpId(strAdditionalInfoTmpId);
					psTzSbmInfRepTKey.setTzSbminfId(strAdditionalInfoContentId);
					psTzSbmInfRepTKey.setTzSbminfRepId(strMsgId);
					PsTzSbmInfRepTMapper.deleteByPrimaryKey(psTzSbmInfRepTKey);
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
