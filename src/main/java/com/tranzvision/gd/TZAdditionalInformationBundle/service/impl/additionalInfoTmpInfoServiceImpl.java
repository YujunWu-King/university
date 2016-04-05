package com.tranzvision.gd.TZAdditionalInformationBundle.service.impl;

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
import com.tranzvision.gd.TZAdditionalInformationBundle.dao.PsTzSbmInfTmpTMapper;
import com.tranzvision.gd.TZAdditionalInformationBundle.dao.PsTzSbmInfStpTMapper;
import com.tranzvision.gd.TZAdditionalInformationBundle.dao.PsTzSbmInfRepTMapper;
import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfTmpT;
import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfStpT;
import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfRepT;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：项目流程配置管理页面;
 * 原PS类：TZ_GD_SMTDTMG_PKG:TZ_GD_data2_CLS
 */
@Service("com.tranzvision.gd.TZAdditionalInformationBundle.service.impl.additionalInfoTmpInfoServiceImpl")
public class additionalInfoTmpInfoServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSbmInfTmpTMapper PsTzSbmInfTmpTMapper;
	@Autowired
	private PsTzSbmInfStpTMapper PsTzSbmInfStpTMapper;
	@Autowired
	private PsTzSbmInfRepTMapper PsTzSbmInfRepTMapper;

	/* 查询报名流程列表*/
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		
		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);

		String strAdditionalInfoTmpId = jacksonUtil.getString("smtDtTmpID");
		
		String strAdditionalInfoTmpName = jacksonUtil.getString("smtDtName");
		
		String strAdditionalInfoTmpIdNew = "";
		
		String strCopyFlag = "";
		//如果是复制的资料流程模版
		if (jacksonUtil.containsKey("addOne")) {
			strCopyFlag = jacksonUtil.getString("addOne");
			if("new".equals(strCopyFlag)){
				//根据模版名称查询
				String sqlGetAdditionalInfoTmp = "SELECT TZ_SBMINF_TMP_ID FROM PS_TZ_SBMINF_TMP_T WHERE TZ_SBMINF_TMP_NAME= ?";
				strAdditionalInfoTmpIdNew =  sqlQuery.queryForObject(sqlGetAdditionalInfoTmp, new Object[] { strAdditionalInfoTmpName },"String");
				if(strAdditionalInfoTmpIdNew != null && !"".equals(strAdditionalInfoTmpIdNew)){
					//复制流程信息
					String sqlGetAdditionalInfo = "SELECT TZ_SBMINF_ID,TZ_SORT_NUM,TZ_CONT_INTRO,TZ_REMARK FROM PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID= ?";
					List<?> listAppProcessInfoData = sqlQuery.queryForList(sqlGetAdditionalInfo, new Object[] { strAdditionalInfoTmpId });
					for (Object objData : listAppProcessInfoData) {
						Map<String, Object> mapData = (Map<String, Object>) objData;
						String strAdditionalInfoContentId = "";
						String strAdditionalInfoContent = "";
						String strAdditionalInfoRemark = "";
						String strSortNum;
						strAdditionalInfoContentId = String.valueOf(mapData.get("TZ_SBMINF_ID"));
						strAdditionalInfoContent = String.valueOf(mapData.get("TZ_CONT_INTRO"));
						strSortNum = String.valueOf(mapData.get("TZ_SORT_NUM"));
						strAdditionalInfoRemark = String.valueOf(mapData.get("TZ_REMARK"));
						
						PsTzSbmInfStpT psTzSbmInfStpT = new PsTzSbmInfStpT();
						psTzSbmInfStpT.setTzSbminfTmpId(strAdditionalInfoTmpIdNew);
						psTzSbmInfStpT.setTzSbminfId(strAdditionalInfoContentId);
						psTzSbmInfStpT.setTzContIntro(strAdditionalInfoContent);
						psTzSbmInfStpT.setTzRemark(strAdditionalInfoRemark);
						psTzSbmInfStpT.setTzSortNum(Integer.parseInt(strSortNum));
						PsTzSbmInfStpTMapper.insert(psTzSbmInfStpT);
						
						/*复制常用回复短语信息*/ 
						String sqlGetMsgInfo = "SELECT TZ_SBMINF_REP_ID,TZ_SBMINF_REP,TZ_SORT_NUM FROM PS_TZ_SBMINF_REP_T WHERE TZ_SBMINF_TMP_ID=? AND TZ_SBMINF_ID=?";
						List<?> listMsgData = sqlQuery.queryForList(sqlGetMsgInfo, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId });
						for (Object objMsgData : listMsgData) {
							Map<String, Object> mapMsgData = (Map<String, Object>) objMsgData;
							
							String strMsgId = String.valueOf(mapMsgData.get("TZ_SBMINF_REP_ID"));
							String strMsgContent = String.valueOf(mapMsgData.get("TZ_SBMINF_REP"));
							String strOrderNum = String.valueOf(mapMsgData.get("TZ_SORT_NUM"));
							
							PsTzSbmInfRepT psTzSbmInfRepT = new PsTzSbmInfRepT();
							psTzSbmInfRepT.setTzSbminfTmpId(strAdditionalInfoTmpIdNew);
							psTzSbmInfRepT.setTzSbminfId(strAdditionalInfoContentId);
							psTzSbmInfRepT.setTzSbminfRepId(strMsgId);
							psTzSbmInfRepT.setTzSbminfRep(strMsgContent);
							psTzSbmInfRepT.setTzSortNum(Integer.parseInt(strOrderNum));
							
							PsTzSbmInfRepTMapper.insert(psTzSbmInfRepT);
							
						}
						strAdditionalInfoTmpId = strAdditionalInfoTmpIdNew;
					}
				}else{
					strAdditionalInfoTmpId = "";
				}
			}
		}
		if(!"".equals(strAdditionalInfoTmpId)){
			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
			try{
				int total = 0;
				// 查询总数;
				String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID=?";
				total = sqlQuery.queryForObject(totalSQL, new Object[] { strAdditionalInfoTmpId },"Integer");
				String sql = "SELECT TZ_SBMINF_ID,TZ_SORT_NUM,TZ_CONT_INTRO,TZ_REMARK FROM PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID= ? order by TZ_SORT_NUM LIMIT ?,?";
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { strAdditionalInfoTmpId,numStart,numLimit });
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;
					String strAdditionalInfoContentId = "";
					String strAdditionalInfoContent = "";
					String strAdditionalInfoRemark = "";
					String strSortNum;

					strAdditionalInfoContentId = String.valueOf(mapData.get("TZ_SBMINF_ID"));
					strAdditionalInfoContent = String.valueOf(mapData.get("TZ_CONT_INTRO"));
					strSortNum = String.valueOf(mapData.get("TZ_SORT_NUM"));
					strAdditionalInfoRemark = String.valueOf(mapData.get("TZ_REMARK"));
					
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("smtDtTmpID", strAdditionalInfoTmpId);
					mapJson.put("smtDtID", strAdditionalInfoContentId);
					mapJson.put("order", strSortNum);
					mapJson.put("content", strAdditionalInfoContent);
					mapJson.put("remark", strAdditionalInfoRemark);
					listJson.add(mapJson);
				}
				mapRet.replace("root", listJson);
				mapRet.replace("total",total);
			} catch (Exception e) {
				e.printStackTrace();
				errorMsg[0] = "1";
				errorMsg[1] = e.toString();
			}
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

				if ("COM".equals(typeFlag)) {

					String strAdditionalInfoTmpId = "";
					String strAdditionalInfoTmpName = String.valueOf(mapData.get("smtDtName"));
					String strAdditionalInfoTmpStatus = String.valueOf(mapData.get("smtDtStatus"));
					/*模版名称是否已经存在*/
					String sql = "SELECT count(1) FROM PS_TZ_SBMINF_TMP_T WHERE TZ_JG_ID= ?  AND TZ_SBMINF_TMP_NAME= ?";

					int isExistAdditionalInfoTmpNum = sqlQuery.queryForObject(sql, new Object[] { orgid,strAdditionalInfoTmpName }, "Integer");
					
					if (isExistAdditionalInfoTmpNum == 0) {
						
						strAdditionalInfoTmpId = String.valueOf(getSeqNum.getSeqNum("TZ_SBMINF_TMP_T", "TZ_SBMINF_TMP_ID"));
						PsTzSbmInfTmpT psTzSbmInfTmpT = new PsTzSbmInfTmpT();
						psTzSbmInfTmpT.setTzSbminfTmpId(strAdditionalInfoTmpId);
						psTzSbmInfTmpT.setTzSbminfTmpName(strAdditionalInfoTmpName);
						psTzSbmInfTmpT.setTzSbminfStatus(strAdditionalInfoTmpStatus);
						psTzSbmInfTmpT.setTzJgId(orgid);
						psTzSbmInfTmpT.setRowAddedDttm(new Date());
						psTzSbmInfTmpT.setRowAddedOprid(oprid);
						psTzSbmInfTmpT.setRowLastmantDttm(new Date());
						psTzSbmInfTmpT.setRowLastmantOprid(oprid);
						
						PsTzSbmInfTmpTMapper.insert(psTzSbmInfTmpT);

						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("smtDtTmpID", strAdditionalInfoTmpId);
						mapJson.put("smtDtName", strAdditionalInfoTmpName);
						mapJson.put("smtDtStatus", strAdditionalInfoTmpStatus);
						returnJson.replace("formData", mapJson);
						
					}else{
						errMsg[0] = "1";
						errMsg[1] = "递交资料模型名称不能重复！";
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

				if ("COM".equals(typeFlag)) {

					String strAdditionalInfoTmpId = String.valueOf(mapData.get("smtDtTmpID"));
					String strAdditionalInfoTmpName = String.valueOf(mapData.get("smtDtName"));
					String strAdditionalInfoTmpStatus = String.valueOf(mapData.get("smtDtStatus"));
					/*模版名称是否已经存在*/
					String sql = "SELECT count(1) FROM PS_TZ_SBMINF_TMP_T WHERE TZ_JG_ID= ? AND TZ_SBMINF_TMP_ID <> ? AND TZ_SBMINF_TMP_NAME= ?";

					int isExistAdditionalInfoTmpNum = sqlQuery.queryForObject(sql, new Object[] { orgid,strAdditionalInfoTmpId,strAdditionalInfoTmpName }, "Integer");
					
					if (isExistAdditionalInfoTmpNum == 0) {
					
						String sqlGetAdditionalInfoTmp = "select COUNT(1) from PS_TZ_SBMINF_TMP_T WHERE TZ_SBMINF_TMP_ID=?";
						int count = sqlQuery.queryForObject(sqlGetAdditionalInfoTmp, new Object[] { strAdditionalInfoTmpId }, "Integer");
						if (count > 0) {
							
							PsTzSbmInfTmpT psTzSbmInfTmpT = new PsTzSbmInfTmpT();
							psTzSbmInfTmpT.setTzSbminfTmpId(strAdditionalInfoTmpId);
							psTzSbmInfTmpT.setTzSbminfTmpName(strAdditionalInfoTmpName);
							psTzSbmInfTmpT.setTzSbminfStatus(strAdditionalInfoTmpStatus);
							psTzSbmInfTmpT.setTzJgId(orgid);
							psTzSbmInfTmpT.setRowLastmantDttm(new Date());
							psTzSbmInfTmpT.setRowLastmantOprid(oprid);
							
							PsTzSbmInfTmpTMapper.updateByPrimaryKeySelective(psTzSbmInfTmpT);
	
							Map<String, Object> mapJson = new HashMap<String, Object>();
							mapJson.put("smtDtTmpID", strAdditionalInfoTmpId);
							mapJson.put("smtDtName", strAdditionalInfoTmpName);
							mapJson.put("smtDtStatus", strAdditionalInfoTmpStatus);
							returnJson.replace("formData", mapJson);
							
						}else{
							errMsg[0] = "1";
							errMsg[1] = "递交资料模型"+ strAdditionalInfoTmpName +"不存在！";
						}	
					}else{
						errMsg[0] = "1";
						errMsg[1] = "递交资料模型名称不能重复！";
					}
				}
				
				if ("PAGE".equals(typeFlag)) {
					
					//提交资料模版编号
					String strAdditionalInfoTmpId = String.valueOf(mapData.get("smtDtTmpID"));
					//提交资料内容编号
					String strAdditionalInfoContentId = String.valueOf(mapData.get("smtDtID"));
					//资料内容简介
					String strAdditionalInfoContent = String.valueOf(mapData.get("content"));
					//资料备注
					String strAdditionalInfoRemark = String.valueOf(mapData.get("remark"));
					//排序序号
					String strAdditionalInfoOrder = String.valueOf(mapData.get("order"));
					
					int numAdditionalInfoOrder = 1;
					
					if(!"".equals(strAdditionalInfoOrder) && strAdditionalInfoOrder != null){
						numAdditionalInfoOrder = Integer.parseInt(strAdditionalInfoOrder);
					}
					
					String sqlGetAppProcess = "select COUNT(1) from PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID=? AND TZ_SBMINF_ID=?";
					int count = sqlQuery.queryForObject(sqlGetAppProcess, new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId }, "Integer");
					if(count>0){
						
						PsTzSbmInfStpT psTzSbmInfStpT = new PsTzSbmInfStpT();
						psTzSbmInfStpT.setTzSbminfTmpId(strAdditionalInfoTmpId);
						psTzSbmInfStpT.setTzSbminfId(strAdditionalInfoContentId);
						psTzSbmInfStpT.setTzContIntro(strAdditionalInfoContent);
						psTzSbmInfStpT.setTzRemark(strAdditionalInfoRemark);
						psTzSbmInfStpT.setTzSortNum(numAdditionalInfoOrder);
						PsTzSbmInfStpTMapper.updateByPrimaryKeySelective(psTzSbmInfStpT);
						
					}else{
						errMsg[0] = "1";
						errMsg[1] = "资料简介" + strAdditionalInfoContent + "不存在"; 
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
	 * 删除递交资料内容
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

				// 递交资料模版模版编号;
				String strAdditionalInfoTmpId = jacksonUtil.getString("smtDtTmpID");
				/*递交资料资料内容编号*/
				String strAdditionalInfoContentId = jacksonUtil.getString("smtDtID");
				
				if (strAdditionalInfoTmpId != null && !"".equals(strAdditionalInfoTmpId) && strAdditionalInfoContentId != null && !"".equals(strAdditionalInfoContentId)) {
					//删除递交资料内容和对应的常用回复短语信息
					Object[] args = new Object[] { strAdditionalInfoTmpId,strAdditionalInfoContentId };
					sqlQuery.update("DELETE FROM PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID = ? AND TZ_SBMINF_ID = ?", args);
					sqlQuery.update("DELETE FROM PS_TZ_SBMINF_REP_T WHERE TZ_SBMINF_TMP_ID = ? AND TZ_SBMINF_ID = ?", args);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	/* 获取流程模版信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			// 流程模版编号;
			String strAdditionalInfoTmpId = jacksonUtil.getString("smtDtTmpID");
			
			// 新的流程模版编号
			String strAdditionalInfoTmpIdNew = "";
			
			String strCopyFlag = "";
			//如果是复制的报名流程模版
			if (jacksonUtil.containsKey("addOne")) {
				strCopyFlag = jacksonUtil.getString("addOne");
				if("new".equals(strCopyFlag)){
					strAdditionalInfoTmpIdNew = String.valueOf(getSeqNum.getSeqNum("TZ_SBMINF_TMP_T", "TZ_SBMINF_TMP_ID"));
					String strAdditionalInfoTmpName = jacksonUtil.getString("smtDtName");
					
					PsTzSbmInfTmpT psTzSbmInfTmpTOld = PsTzSbmInfTmpTMapper.selectByPrimaryKey(strAdditionalInfoTmpId);
					if(psTzSbmInfTmpTOld != null ){
						String strAdditionalInfoTmpStatus = psTzSbmInfTmpTOld.getTzSbminfStatus();
						//添加模版
						PsTzSbmInfTmpT psTzSbmInfTmpT = new PsTzSbmInfTmpT();
						psTzSbmInfTmpT.setTzSbminfTmpId(strAdditionalInfoTmpIdNew);
						psTzSbmInfTmpT.setTzSbminfTmpName(strAdditionalInfoTmpName);
						psTzSbmInfTmpT.setTzSbminfStatus(strAdditionalInfoTmpStatus);
						psTzSbmInfTmpT.setTzJgId(orgid);
						psTzSbmInfTmpT.setRowAddedDttm(new Date());
						psTzSbmInfTmpT.setRowAddedOprid(oprid);
						psTzSbmInfTmpT.setRowLastmantDttm(new Date());
						psTzSbmInfTmpT.setRowLastmantOprid(oprid);
						PsTzSbmInfTmpTMapper.insert(psTzSbmInfTmpT);

						Map<String, Object> retMap = new HashMap<String, Object>();
						retMap.put("smtDtTmpID", strAdditionalInfoTmpIdNew);
						retMap.put("smtDtName", strAdditionalInfoTmpName);
						retMap.put("smtDtStatus", strAdditionalInfoTmpStatus);
						returnJsonMap.replace("formData",retMap);
					}else{
						errMsg[0] = "1";
						errMsg[1] = "资料模版信息加载失败。";
					}	
				}else{
					PsTzSbmInfTmpT psTzSbmInfTmpT = PsTzSbmInfTmpTMapper.selectByPrimaryKey(strAdditionalInfoTmpId);
					if(psTzSbmInfTmpT != null ){
						Map<String, Object> retMap = new HashMap<String, Object>();
						String strAdditionalInfoTmpName = String.valueOf(psTzSbmInfTmpT.getTzSbminfTmpName());
						String strAdditionalInfoTmpStatus = String.valueOf(psTzSbmInfTmpT.getTzSbminfStatus());
						retMap.put("smtDtTmpID", strAdditionalInfoTmpId);
						retMap.put("smtDtName", strAdditionalInfoTmpName);
						retMap.put("smtDtStatus", strAdditionalInfoTmpStatus);
						returnJsonMap.replace("formData",retMap);
					}else{
						errMsg[0] = "1";
						errMsg[1] = "资料模版信息不存在。";
					}	
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
}
