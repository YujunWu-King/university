package com.tranzvision.gd.TZMbaPwClpsBundle.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpsGzTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
/**
 * 材料评审规则设置
 * 
 * @author WRL (TZ_GD_CLPS_PKG:TZ_GD_RULE_CLS)
 * @2017/02/23
 */
@Service("com.tranzvision.gd.TZMbaPwClpsBundle.service.impl.TzReviewRuleServiceImpl")
public class TzReviewRuleServiceImpl extends FrameworkImpl {
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private HttpServletRequest request;	
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private PsTzClpsGzTblMapper psTzClpsGzTblMapper;
	
	@Override
	public String tzQuery(String strParams,String[] errMsg) {
		String strRtn = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);
			
			//班级编号
			String classId = jacksonUtil.getString("classID");
			//批次编号
			String batchID = jacksonUtil.getString("batchID");

			/*评审规则详细信息：初审开始日期，初审开始时间，初审结束日期，初审结束日期，材料评审说明，当前评审状态，每生初审评审人;*/
			String strStartDate = "", strStartTime = "", strEndDate = "", strEndTime = "", strDesc = "", strReviewStatus = "";
			/* 报考考生数量,材料评审考生*/
			int numReviewCount = 0;

			String sql = "SELECT date_format(TZ_PYKS_RQ,'%Y-%m-%d') AS TZ_PYKS_RQ, date_format(TZ_PYKS_SJ,'%i:%s') AS TZ_PYKS_SJ, date_format(TZ_PYJS_RQ,'%Y-%m-%d') AS TZ_PYJS_RQ, date_format(TZ_PYJS_SJ,'%i:%s') AS TZ_PYJS_SJ, TZ_CLPS_SM,TZ_MSPY_NUM,CASE WHEN TZ_DQPY_ZT = 'A' THEN '进行中' WHEN TZ_DQPY_ZT = 'B' THEN '已结束' ELSE '未开始' END AS TZ_DQPY_ZT FROM PS_TZ_CLPS_GZ_TBL  WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?";

			Map<String,Object> rulesMap = sqlQuery.queryForMap(sql, new Object[]{classId,batchID});
			if(rulesMap != null){
				strStartDate = rulesMap.get("TZ_PYKS_RQ") == null ? "" : String.valueOf(rulesMap.get("TZ_PYKS_RQ"));
				strStartTime = rulesMap.get("TZ_PYKS_SJ") == null ? "" : String.valueOf(rulesMap.get("TZ_PYKS_SJ"));
				strEndDate = rulesMap.get("TZ_PYJS_RQ") == null ? "" : String.valueOf(rulesMap.get("TZ_PYJS_RQ"));
				strEndTime = rulesMap.get("TZ_PYJS_SJ") == null ? "" : String.valueOf(rulesMap.get("TZ_PYJS_SJ"));
				strDesc = rulesMap.get("TZ_CLPS_SM") == null ? "" : String.valueOf(rulesMap.get("TZ_CLPS_SM"));
				numReviewCount = rulesMap.get("TZ_MSPY_NUM") == null ? 0 : Integer.parseInt(String.valueOf(rulesMap.get("TZ_MSPY_NUM")));
				strReviewStatus = rulesMap.get("TZ_DQPY_ZT") == null ? "" : String.valueOf(rulesMap.get("TZ_DQPY_ZT"));
			}
			
			if(StringUtils.isBlank(strStartTime)){
				strStartTime = "08:30";
			}
			
			if(StringUtils.isBlank(strEndTime)){
				strEndTime = "17:30";
			}

			/*报考考生数量*/
			String sqlAppNumber = "SELECT COUNT(*) FROM PS_TZ_FORM_WRK_T WRK,PS_TZ_APP_INS_T INS WHERE WRK.TZ_APP_INS_ID = INS.TZ_APP_INS_ID AND TZ_APP_FORM_STA = 'S' AND WRK.TZ_CLASS_ID = ? AND TZ_BATCH_ID = ?";
			int numApplicantsNumber = sqlQuery.queryForObject(sqlAppNumber, new String[]{classId,batchID}, "int");
			/*材料评审考生数量*/
			String sqlReviewAppNumber = "SELECT COUNT(*) FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?";
			int numMaterialsReviewApplicantsNumber = sqlQuery.queryForObject(sqlReviewAppNumber, new String[]{classId,batchID}, "int");
			
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("classID", classId);
			mapData.put("batchID", batchID);
			mapData.put("startDate", strStartDate);
			mapData.put("startTime", strStartTime);
			mapData.put("endDate", strEndDate);
			mapData.put("endTime", strEndTime);
			mapData.put("desc", strDesc);
			mapData.put("reviewCount", numReviewCount);
			mapData.put("reviewStatus", strReviewStatus);
			mapData.put("applicantsNumber", numApplicantsNumber);
			mapData.put("materialsReviewApplicantsNumber", numMaterialsReviewApplicantsNumber);

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("formData", mapData);

			strRtn = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRtn;
	}
	
	/**
	 * 修改附加字段信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {

		String strRet = "{}";
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if (StringUtils.isBlank(orgId)) {
			errMsg[0] = "1";
			errMsg[1] = "您不属于任何机构，不能修改附加字段定义！";
			return strRet;
		}
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			Date dateNow = new Date();
			DateFormat timeFormat = new SimpleDateFormat("mm:ss");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				/*类型标志*/
				String strFlag = jacksonUtil.getString("typeFlag");

				/*评审规则*/
				if(StringUtils.equals("RULE", strFlag)){
					//信息内容
					Map<String,Object> infoData = jacksonUtil.getMap("data");
					
					String classID = infoData.get("classID") == null ? "" : String.valueOf(infoData.get("classID"));
					String batchID = infoData.get("batchID") == null ? "" : String.valueOf(infoData.get("batchID"));
					String strKSDate = infoData.get("startDate") == null ? null : String.valueOf(infoData.get("startDate"));
					Date startDate = null;
					if(StringUtils.isNotBlank(strKSDate)){
						startDate = dateFormat.parse(strKSDate);
					}
					Date startTime = infoData.get("startTime") == null ? timeFormat.parse("08:30") : timeFormat.parse(infoData.get("startTime").toString());
					
					String strJSDate = infoData.get("endDate") == null ? null : String.valueOf(infoData.get("endDate"));
					Date endDate = null;
					if(StringUtils.isNotBlank(strJSDate)){
						endDate = dateFormat.parse(strJSDate);
					}
					Date endTime = infoData.get("endTime") == null ? timeFormat.parse("17:30") : timeFormat.parse(infoData.get("endTime").toString());
					String desc = infoData.get("desc") == null ? "" : String.valueOf(infoData.get("desc"));
					String reviewCount = infoData.get("reviewCount") == null ? "0" : String.valueOf(infoData.get("reviewCount"));
					
					String sqlExists = "SELECT 'Y' FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?";
					String isExists = sqlQuery.queryForObject(sqlExists, new Object[] { classID, batchID},"String");
					
					if (StringUtils.equals("Y", isExists)) {
						PsTzClpsGzTbl psTzClpsGzTbl = new PsTzClpsGzTbl();
						psTzClpsGzTbl.setTzClassId(classID);
						psTzClpsGzTbl.setTzApplyPcId(batchID);

						psTzClpsGzTbl.setTzPyksRq(startDate);
						psTzClpsGzTbl.setTzPyksSj(startTime);
						psTzClpsGzTbl.setTzPyjsRq(endDate);
						psTzClpsGzTbl.setTzPyjsSj(endTime);
						psTzClpsGzTbl.setTzClpsSm(desc);
						psTzClpsGzTbl.setTzMspyNum(Integer.parseInt(reviewCount));
						psTzClpsGzTbl.setRowLastmantDttm(dateNow);
						psTzClpsGzTbl.setRowLastmantOprid(oprId);
						
						psTzClpsGzTblMapper.updateByPrimaryKeySelective(psTzClpsGzTbl);
					}else{
						PsTzClpsGzTbl psTzClpsGzTbl = new PsTzClpsGzTbl();
						psTzClpsGzTbl.setTzClassId(classID);
						psTzClpsGzTbl.setTzApplyPcId(batchID);
						psTzClpsGzTbl.setTzPyksRq(startDate);
						psTzClpsGzTbl.setTzPyksSj(startTime);
						psTzClpsGzTbl.setTzPyjsRq(endDate);
						psTzClpsGzTbl.setTzPyjsSj(endTime);
						psTzClpsGzTbl.setTzClpsSm(desc);
						psTzClpsGzTbl.setTzMspyNum(Integer.parseInt(reviewCount));
						psTzClpsGzTbl.setRowAddedDttm(dateNow);
						psTzClpsGzTbl.setRowAddedOprid(oprId);
						psTzClpsGzTbl.setRowLastmantDttm(dateNow);
						psTzClpsGzTbl.setRowLastmantOprid(oprId);
						psTzClpsGzTblMapper.insert(psTzClpsGzTbl);
					}
				}
				/*评委列表*/
				if(StringUtils.equals("JUDGE", strFlag)){
					/*
           rem 将字符串转换成json;
            Local JavaObject &judgeJson = &jsonUtil.getJson(&infoData);
            
            &strClassID = &judgeJson.getString("classID");
            &strBatchID = &judgeJson.getString("batchID");
            Local string &strJudgeID = &judgeJson.getString("judgeID");
            rem 评委oprid;
            Local string &strOprID;
            SQLExec("SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=:1 AND TZ_JG_ID=:2", &strJudgeID, &orgID, &strOprID);
            Local boolean &bolGroupID;
            Local string &strUpper = &judgeJson.getString("upper");
            
            Local string &strLower = &judgeJson.getString("lower");
            try
               Local string &strGroupID = &judgeJson.getString("judgeGroup");
               &bolGroupID = True;
            catch Exception &e7
               &bolGroupID = False;
            end-try;
            Local string &strJudgeStatus = &judgeJson.getString("judgeStatus");
            Local Record &TZ_CLPS_PW_TBL = CreateRecord(Record.TZ_CLPS_PW_TBL);
            &TZ_CLPS_PW_TBL.TZ_CLASS_ID.Value = &strClassID;
            &TZ_CLPS_PW_TBL.TZ_APPLY_PC_ID.Value = &strBatchID;
            &TZ_CLPS_PW_TBL.TZ_PWEI_OPRID.Value = &strOprID;
            If All(&strClassID) And
                  All(&strBatchID) And
                  All(&strOprID) Then
               If &TZ_CLPS_PW_TBL.SelectByKey() Then
                  &TZ_CLPS_PW_TBL.TZ_CLASS_ID.Value = &strClassID;
                  &TZ_CLPS_PW_TBL.TZ_APPLY_PC_ID.Value = &strBatchID;
                  &TZ_CLPS_PW_TBL.TZ_PWEI_OPRID.Value = &strOprID;
                  &TZ_CLPS_PW_TBL.TZ_PWZBH.Value = &strGroupID;
                  &TZ_CLPS_PW_TBL.TZ_PYKS_XX.Value = &strLower;
                  &TZ_CLPS_PW_TBL.TZ_PYKS_SX.Value = &strUpper;
                  &TZ_CLPS_PW_TBL.TZ_PWEI_ZHZT.Value = &strJudgeStatus;
                  &TZ_CLPS_PW_TBL.ROW_LASTMANT_DTTM.Value = %Datetime;
                  &TZ_CLPS_PW_TBL.ROW_LASTMANT_OPRID.Value = %UserId;
                  &TZ_CLPS_PW_TBL.Update()
               Else
                  &TZ_CLPS_PW_TBL.TZ_CLASS_ID.Value = &strClassID;
                  &TZ_CLPS_PW_TBL.TZ_APPLY_PC_ID.Value = &strBatchID;
                  &TZ_CLPS_PW_TBL.TZ_PWEI_OPRID.Value = &strOprID;
                  &TZ_CLPS_PW_TBL.TZ_PWZBH.Value = &strGroupID;
                  &TZ_CLPS_PW_TBL.TZ_PYKS_XX.Value = &strLower;
                  &TZ_CLPS_PW_TBL.TZ_PYKS_SX.Value = &strUpper;
                  &TZ_CLPS_PW_TBL.TZ_PWEI_ZHZT.Value = &strJudgeStatus;
                  &TZ_CLPS_PW_TBL.ROW_ADDED_DTTM.Value = %Datetime;
                  &TZ_CLPS_PW_TBL.ROW_ADDED_OPRID.Value = %UserId;
                  &TZ_CLPS_PW_TBL.ROW_LASTMANT_DTTM.Value = %Datetime;
                  &TZ_CLPS_PW_TBL.ROW_LASTMANT_OPRID.Value = %UserId;
                  &TZ_CLPS_PW_TBL.Insert()
               End-If;
            End-If;
         End-If;
					 * */
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			e.printStackTrace();
		}
		return strRet;
	}
}
