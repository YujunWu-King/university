package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.swing.border.EtchedBorder;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.tomcat.util.bcel.classfile.ElementValue;
import org.apache.xmlbeans.impl.jam.mutable.MPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao.psTzClpsPwTblMapper;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.psTzClpsPwTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.psTzClpsPwTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpsGzTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 材料评审设置评审规则
 * @author LuYan
 * 2017-3-22
 *
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzClpsRuleServiceImpl")
public class TzClpsRuleServiceImpl extends FrameworkImpl {
	
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private psTzClpsPwTblMapper psTzClpsPwTblMapper;
	@Autowired
	private PsTzClpsGzTblMapper psTzClpsGzTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;

	
	@Override
	public String tzQuery(String strParams,String[] errMsg) {
		String strRet="";
		JacksonUtil jacksonUtil = new JacksonUtil();
		HashMap<String, Object> mapRet = new HashMap<String,Object>();
		HashMap<String, Object> mapData = new HashMap<String,Object>();
		
		try {
			
			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();

			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat(tmFormat);
			
			jacksonUtil.json2Map(strParams);

			//班级编号
			String classId = jacksonUtil.getString("classId");
			//批次编号
			String batchId = jacksonUtil.getString("batchId");
			
			String sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialRuleInfo");
			Map<String, Object> mapBasic = sqlQuery.queryForMap(sql,new Object[] {classId,batchId});
			
			if(mapBasic!=null) {
				String className = (String) mapBasic.get("TZ_CLASS_NAME");
				String batchName = (String) mapBasic.get("TZ_BATCH_NAME");
				Date startDate = mapBasic.get("TZ_PYKS_RQ") == null ? null : dateSimpleDateFormat.parse(String.valueOf(mapBasic.get("TZ_PYKS_RQ")));
				Date startTime = mapBasic.get("TZ_PYKS_SJ") == null ? null : timeSimpleDateFormat.parse(String.valueOf(mapBasic.get("TZ_PYKS_SJ")));
				Date endDate = mapBasic.get("TZ_PYJS_RQ") == null ? null : dateSimpleDateFormat.parse(String.valueOf(mapBasic.get("TZ_PYJS_RQ")));
				Date endTime = mapBasic.get("TZ_PYJS_SJ") == null ? null : timeSimpleDateFormat.parse(String.valueOf(mapBasic.get("TZ_PYJS_SJ")));
				String materialDesc = (String) mapBasic.get("TZ_CLPS_SM");
				String dqpsStatus = (String) mapBasic.get("TZ_DQPY_ZT");
				String dqpsStatusDesc = (String) mapBasic.get("TZ_DQPY_ZT_DESC");
				String bkksNum = mapBasic.get("TZ_BKKS_NUM") == null ? "" : String.valueOf(mapBasic.get("TZ_BKKS_NUM"));
				String clpsksNum = mapBasic.get("TZ_CLPS_KS_NUM") == null ? "" : String.valueOf(mapBasic.get("TZ_CLPS_KS_NUM"));
				String judgeNumSet = mapBasic.get("TZ_MSPY_NUM") == null ? "" : String.valueOf(mapBasic.get("TZ_MSPY_NUM"));
				//每位考生要求被几个评委审批，如果没有，默认为2
				if(!"".equals(judgeNumSet)) {
					
				} else {
					judgeNumSet="2";
				}
				
				String strStartDate = "";
				if(null!=startDate) {
					strStartDate = dateSimpleDateFormat.format(startDate);
				}
				String strStartTime = "";
				if(null!=startTime) {
					strStartTime = timeSimpleDateFormat.format(startTime);
				}
				String strEndDate = "";
				if(null!=endDate) {
					strEndDate = dateSimpleDateFormat.format(endDate);
				}
				String strEndTime = "";
				if(null!=endTime) {
					strEndTime = timeSimpleDateFormat.format(endTime);
				}
 				
				mapData.put("classId", classId);
				mapData.put("batchId", batchId);
				mapData.put("className", className);
				mapData.put("batchName", batchName);
				mapData.put("bkksNum", bkksNum);
				mapData.put("clpsksNum", clpsksNum);
				mapData.put("dqpsStatus", dqpsStatus);
				mapData.put("dqpsStatusDesc", dqpsStatusDesc);
				mapData.put("startDate", strStartDate);
				mapData.put("startTime", strStartTime);
				mapData.put("endDate", strEndDate);
				mapData.put("endTime", strEndTime);
				mapData.put("materialDesc", materialDesc);
				mapData.put("judgeNumSet", judgeNumSet);		
				
				mapRet.put("formData", mapData);
			}
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
			
		return strRet;
	}
	
	
	/*材料评审列表*/
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		mapRet.put("root", listData);

		try {
			
			jacksonUtil.json2Map(strParams);
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			
			Integer count = 0;
			
			String sql = "";
			sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialJudgeInfo");
			List<Map<String, Object>> pwList = sqlQuery.queryForList(sql,new Object[]{classId,batchId});
			
			for(Map<String, Object> pwMap : pwList) {
			
				count++;
				
				String judgeOprid = (String) pwMap.get("TZ_PWEI_OPRID");
				String judgeId = (String) pwMap.get("TZ_DLZH_ID");
				String judgeName = (String) pwMap.get("TZ_REALNAME");
				String judgeMobile = (String) pwMap.get("TZ_MOBILE");
				String judgeEmail = (String) pwMap.get("TZ_EMAIL");
				String judgeGroup = (String) pwMap.get("TZ_PWZBH");
				String judgeGroupDesc = (String) pwMap.get("TZ_CLPS_GR_NAME");
				String judgeExamineeNum = pwMap.get("TZ_PYKS_XX") == null ? "" : String.valueOf(pwMap.get("TZ_PYKS_XX"));
				String judgeStatus = (String) pwMap.get("TZ_PWEI_ZHZT");
				String judgeStatusDesc = (String) pwMap.get("TZ_PWEI_ZHZT_DESC");
			
				Map<String, Object> mapList = new HashMap<String,Object>();
				mapList.put("classId", classId);
				mapList.put("batchId", batchId);
				mapList.put("judgeOprid", judgeOprid);
				mapList.put("judgeId", judgeId);
				mapList.put("judgeName", judgeName);
				mapList.put("judgeMobile", judgeMobile);
				mapList.put("judgeEmail", judgeEmail);
				mapList.put("judgeGroup", judgeGroup);
				mapList.put("judgeGroupDesc", judgeGroupDesc);
				mapList.put("judgeExamineeNum", judgeExamineeNum);
				mapList.put("judgeStatus", judgeStatus);
				mapList.put("judgeStatusDesc", judgeStatusDesc);
				
				listData.add(mapList);
						
			}
			
			mapRet.replace("total", count);
			mapRet.replace("root", listData);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		
		return strRet;
		
	}
	
	/*新增*/
	public String tzAdd(String[] actData,String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			int num = 0;
			for(num=0;num<actData.length;num++) {
				String strForm =actData[num];
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");
				
				if("RULE".equals(typeFlag)) {
					//评审规则基本信息
					String strRule = saveRuleBasic(mapData,errMsg);
				}
				
				if("JUDGE".equals(typeFlag)) {
					//评委
					String strJudge = saveJudgeInfo(mapData, errMsg);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*保存*/
	public String tzUpdate(String[] actData,String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			Integer judgeNumTotal = 0;
			
			int num = 0;
			for(num=0;num<actData.length;num++) {
				String strForm =actData[num];
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");
				
				if("RULE".equals(typeFlag)) {
					//评审规则基本信息
					String strRule = saveRuleBasic(mapData,errMsg);
				}
				
				if("JUDGE".equals(typeFlag)) {
					//评委
					String strJudge = saveJudgeInfo(mapData, errMsg);
					Integer judgeExamineeNum = Integer.valueOf(mapData.get("judgeExamineeNum") == null ? "0" : String.valueOf(mapData.get("judgeExamineeNum")));
					judgeNumTotal += judgeExamineeNum;
					Map<String, Object> mapRet = new HashMap<String,Object>();
					mapRet.put("judgeNumTotal", judgeNumTotal);
					strRet = jacksonUtil.Map2json(mapRet);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*删除材料评委*/
	public String tzDelete(String[] actData,String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			int num = 0;
			for(num=0;num<actData.length;num++) {
				String strForm =actData[num];
				jacksonUtil.json2Map(strForm);
				
				String sql="";

				String classId = jacksonUtil.getString("classId");
				String batchId = jacksonUtil.getString("batchId");
				String judgeOprid = jacksonUtil.getString("judgeOprid");
				
				//删除材料评审评委列表
				psTzClpsPwTblKey psTzClpsPwTblKey = new psTzClpsPwTblKey();
				psTzClpsPwTblKey.setTzClassId(classId);
				psTzClpsPwTblKey.setTzApplyPcId(batchId);
				psTzClpsPwTblKey.setTzPweiOprid(judgeOprid);
				
				psTzClpsPwTbl psTzClpsPwTbl = psTzClpsPwTblMapper.selectByPrimaryKey(psTzClpsPwTblKey);
				if(psTzClpsPwTbl==null) {
					
				} else {
					psTzClpsPwTblMapper.deleteByPrimaryKey(psTzClpsPwTbl);
				}
				
				//删除材料评审评委考生关系表
				sql = "DELETE FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?";
				sqlQuery.update(sql,new Object[]{classId,batchId,judgeOprid});
				
				//更新材料评审考生评审得分历史的提交状态为撤销
				sql = "UPDATE PS_TZ_KSCLPSLS_TBL SET TZ_SUBMIT_YN='C' WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?";
				sqlQuery.update(sql,new Object[]{classId,batchId,judgeOprid});
				
				//更新材料评审评委评审历史的提交状态为撤销
				sql = "UPDATE PS_TZ_CLPWPSLS_TBL SET TZ_SUBMIT_YN='C' WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?";
				sqlQuery.update(sql,new Object[]{classId,batchId,judgeOprid});
				
				//更新材料评审考生评委信息的评委列表信息
				sql = "SELECT A.TZ_APP_INS_ID FROM PS_TZ_CLPS_KSH_TBL A WHERE TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=?";
				List<Map<String, Object>> ksList = sqlQuery.queryForList(sql,new Object[]{classId,batchId});
				
				for(Map<String, Object> ksMap : ksList) {
					String appinsId = ksMap.get("TZ_APP_INS_ID") == null ? "" : ksMap.get("TZ_APP_INS_ID").toString();
					
					String pwDesc = "";
					String sqlPw = "SELECT A.TZ_PWEI_OPRID,	B.TZ_REALNAME FROM  PS_TZ_CP_PW_KS_TBL A LEFT JOIN PS_TZ_AQ_YHXX_TBL B ON A.TZ_PWEI_OPRID=B.OPRID WHERE A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=?";
					List<Map<String, Object>> pwList = sqlQuery.queryForList(sqlPw,new Object[]{classId,batchId,appinsId});
					
					for(Map<String, Object> pwMap : pwList) {
						String judgeName = (String) pwMap.get("TZ_REALNAME");
						
						if(!"".equals(pwDesc)) {
							pwDesc += "," + judgeName;
						} else {
							pwDesc = judgeName;
						}
					}
					
					String sqlUpdate = "UPDATE PS_TZ_CLPSKSPW_TBL SET TZ_CLPW_LIST=? WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
					sqlQuery.update(sqlUpdate,new Object[]{pwDesc,classId,batchId,appinsId});
				
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	public String tzOther(String operateType,String strParams,String[] errMsg) {
		String strRet="";

		try {
			//批量重置选中评委密码
			if("tzResetPassword".equals(operateType)) {
				strRet = resetPassword(strParams,errMsg);
			}
			//给选中评委发送邮件
			if("tzSendEmail".equals(operateType)) {
				strRet = sendEmail(strParams,errMsg);
			}
			//给选中评委发送短信
			if("tzSendMessage".equals(operateType)) {
				strRet = sendMessage(strParams,errMsg);
			}
			//批量导出评委
			if("tzExportJudge".equals(operateType)) {
				strRet = exportJudge(strParams,errMsg);
			}
			//查询评委组
			if("tzGetJudgeGroup".equals(operateType)) {
				strRet = getJudgeGroup(strParams,errMsg);
			}
			//校验评委各组评议人数合是否等于考生人数
			if("tzCheckNum".equals(operateType)) {
				strRet = checkNum(strParams,errMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*规则基本信息保存*/
	public String saveRuleBasic(Map<String, Object> mapParams,String[] errMsg) {
		String strRet = "";
		
		try {
			
			//当前登录人
			String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();
			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat(tmFormat);
						
			String classId = (String) mapParams.get("classId");
			String batchId = (String) mapParams.get("batchId");
			String dqpsStatus = (String) mapParams.get("dqpsStatus");
			String strStartDate = mapParams.get("startDate") == null ? "" : String.valueOf(mapParams.get("startDate"));	
			String strStartTime = mapParams.get("startTime") == null ? "" : String.valueOf(mapParams.get("startTime"));
			String strEndDate = mapParams.get("endDate") == null ? "" : String.valueOf(mapParams.get("endDate"));
			String strEndTime = mapParams.get("endTime") == null ? "" : String.valueOf(mapParams.get("endTime"));
			String materialDesc = (String) mapParams.get("materialDesc");
			Integer judgeNumSet = mapParams.get("judgeNumSet") == null ? 0 : Integer.valueOf((String) mapParams.get("judgeNumSet"));
			
			Date startDate = null;
			if(!"".equals(strStartDate)) {
				startDate = dateSimpleDateFormat.parse(strStartDate);
			}
			Date startTime = null;
			if(!"".equals(strStartTime)) {
				startTime = timeSimpleDateFormat.parse(strStartTime);
			}
			
			Date endDate = null;
			if(!"".equals(strEndDate)) {
				endDate = dateSimpleDateFormat.parse(strEndDate);
			}
			Date endTime = null;
			if(!"".equals(strEndTime)) {
				endTime = timeSimpleDateFormat.parse(strEndTime);
			}
			
			PsTzClpsGzTblKey psTzClpsGzTblKey = new PsTzClpsGzTblKey();
			psTzClpsGzTblKey.setTzClassId(classId);
			psTzClpsGzTblKey.setTzApplyPcId(batchId);
			
			PsTzClpsGzTbl psTzClpsGzTbl = psTzClpsGzTblMapper.selectByPrimaryKey(psTzClpsGzTblKey);
			
			if(psTzClpsGzTbl==null) {
				psTzClpsGzTbl = new PsTzClpsGzTbl();
				psTzClpsGzTbl.setTzClassId(classId);
				psTzClpsGzTbl.setTzApplyPcId(batchId);
				psTzClpsGzTbl.setTzPyksRq(startDate);
				psTzClpsGzTbl.setTzPyksSj(startTime);
				psTzClpsGzTbl.setTzPyjsRq(endDate);
				psTzClpsGzTbl.setTzPyjsSj(endTime);
				psTzClpsGzTbl.setTzClpsSm(materialDesc);
				psTzClpsGzTbl.setTzMspyNum(judgeNumSet);
				psTzClpsGzTbl.setTzDqpyZt(dqpsStatus);
				psTzClpsGzTbl.setRowAddedDttm(new Date());
				psTzClpsGzTbl.setRowAddedOprid(currentOprid);
				psTzClpsGzTbl.setRowLastmantDttm(new Date());
				psTzClpsGzTbl.setRowLastmantOprid(currentOprid);
				psTzClpsGzTblMapper.insertSelective(psTzClpsGzTbl);
			} else {
				psTzClpsGzTbl.setTzPyksRq(startDate);
				psTzClpsGzTbl.setTzPyksSj(startTime);
				psTzClpsGzTbl.setTzPyjsRq(endDate);
				psTzClpsGzTbl.setTzPyjsSj(endTime);
				psTzClpsGzTbl.setTzClpsSm(materialDesc);
				psTzClpsGzTbl.setTzMspyNum(judgeNumSet);
				psTzClpsGzTbl.setTzDqpyZt(dqpsStatus);
				psTzClpsGzTbl.setRowLastmantDttm(new Date());
				psTzClpsGzTbl.setRowLastmantOprid(currentOprid);
				psTzClpsGzTblMapper.updateByPrimaryKey(psTzClpsGzTbl);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*评委保存*/
	public String saveJudgeInfo(Map<String, Object> mapParams,String[] errMsg) {
		String strRet = "";
		
		try {
			
			//当前登录人
			String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			String classId = (String) mapParams.get("classId");
			String batchId = (String) mapParams.get("batchId");
			String judgeOprid = (String) mapParams.get("judgeOprid");
			String judgeGroup = (String) mapParams.get("judgeGroup");
			Integer judgeExamineeNum = Integer.valueOf(mapParams.get("judgeExamineeNum") == null ? "0" : String.valueOf(mapParams.get("judgeExamineeNum")));
			String judgeStatus = (String) mapParams.get("judgeStatus");
			
			psTzClpsPwTblKey psTzClpsPwTblKey = new psTzClpsPwTblKey();
			psTzClpsPwTblKey.setTzClassId(classId);
			psTzClpsPwTblKey.setTzApplyPcId(batchId);
			psTzClpsPwTblKey.setTzPweiOprid(judgeOprid);
			
			psTzClpsPwTbl psTzClpsPwTbl = psTzClpsPwTblMapper.selectByPrimaryKey(psTzClpsPwTblKey);
			
			if(psTzClpsPwTbl==null) {
				psTzClpsPwTbl = new psTzClpsPwTbl();
				psTzClpsPwTbl.setTzClassId(classId);
				psTzClpsPwTbl.setTzApplyPcId(batchId);
				psTzClpsPwTbl.setTzPweiOprid(judgeOprid);
				psTzClpsPwTbl.setTzPyksXx(judgeExamineeNum);
				psTzClpsPwTbl.setTzPweiZhzt(judgeStatus);
				psTzClpsPwTbl.setTzPwzbh(judgeGroup);
				psTzClpsPwTbl.setRowAddedDttm(new Date());
				psTzClpsPwTbl.setRowAddedOprid(currentOprid);
				psTzClpsPwTbl.setRowLastmantDttm(new Date());
				psTzClpsPwTbl.setRowLastmantOprid(currentOprid);
				psTzClpsPwTblMapper.insertSelective(psTzClpsPwTbl);
			} else {
				psTzClpsPwTbl.setTzPweiOprid(judgeOprid);
				psTzClpsPwTbl.setTzPyksXx(judgeExamineeNum);
				psTzClpsPwTbl.setTzPweiZhzt(judgeStatus);
				psTzClpsPwTbl.setTzPwzbh(judgeGroup);
				psTzClpsPwTbl.setRowLastmantDttm(new Date());
				psTzClpsPwTbl.setRowLastmantOprid(currentOprid);
				psTzClpsPwTblMapper.updateByPrimaryKeySelective(psTzClpsPwTbl);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*批量重置评委密码*/
	public String resetPassword(String strParams,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//当前登录人
			String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			jacksonUtil.json2Map(strParams);
			String selectJudgeOprid = jacksonUtil.getString("selectJudgeOprid");
			String[] listJudge = selectJudgeOprid.split(",");
			
			for(Object judgeId : listJudge) {
				String sql = "SELECT TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
				String mobile = sqlQuery.queryForObject(sql, new Object[] {currentOrgId,judgeId},"String");
				
				if(!"".equals(mobile)) {
					
				} else {
					mobile = getHardCodePoint.getHardCodePointVal("TZ_CLPS_PW_PSWD");
				}
				
				String password = DESUtil.encrypt(mobile, "TZGD_Tranzvision");
	
				Psoprdefn psoprdefn = new Psoprdefn();
				psoprdefn = psoprdefnMapper.selectByPrimaryKey(String.valueOf(judgeId));
				
				if(psoprdefn==null) {
					psoprdefn = new Psoprdefn();
					psoprdefn.setOprid(String.valueOf(judgeId));
					psoprdefn.setOperpswd(password);
					psoprdefn.setAcctlock(Short.valueOf("0"));
					psoprdefn.setLastupddttm(new Date());
					psoprdefn.setLastupdoprid(currentOprid);
					psoprdefnMapper.insert(psoprdefn);
				} else {
					psoprdefn.setOprid(String.valueOf(judgeId));
					psoprdefn.setOperpswd(password);
					psoprdefn.setLastupddttm(new Date());
					psoprdefn.setLastupdoprid(currentOprid);
					psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);	
				}
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*给选中评委发送邮件*/
	public String sendEmail(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			jacksonUtil.json2Map(strParams);
			List<?> listJudgeOprid = jacksonUtil.getList("selectJudgeOprid");
			
			//创建邮件发送听众
			String crtAudi = createTaskServiceImpl.createAudience("",currentOrgId,"材料评审评委邮件发送", "CLPS");
			
			if (!"".equals(crtAudi)) {
				
				//添加听众成员
				for(Object judgeOprid : listJudgeOprid) {
					
					String sql = "SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
					Map<String, Object> mapData = sqlQuery.queryForMap(sql,new Object[]{currentOrgId,judgeOprid});
					if(mapData!=null) {
						String name = mapData.get("TZ_REALNAME") == null ? "" : mapData.get("TZ_REALNAME").toString();
						String email = mapData.get("TZ_EMAIL") == null ? "" : mapData.get("TZ_EMAIL").toString();
						String mobile = mapData.get("TZ_MOBILE") == null ? "" : mapData.get("TZ_MOBILE").toString();
				
						createTaskServiceImpl.addAudCy(crtAudi,name, "", mobile, "", email, "", "", String.valueOf(judgeOprid), "",
							"", "");
					}
				}
				
				
				mapRet.put("audienceId", crtAudi);
				strRet = jacksonUtil.Map2json(mapRet);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*给选中评委发送短信*/
	public String sendMessage(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			jacksonUtil.json2Map(strParams);
			List<?> listJudgeOprid = jacksonUtil.getList("selectJudgeOprid");
			
			//创建邮件发送听众
			String crtAudi = createTaskServiceImpl.createAudience("",currentOrgId,"材料评审评委短信发送", "CLPS");
			
			if (!"".equals(crtAudi)) {
				
				//添加听众成员
				for(Object judgeOprid : listJudgeOprid) {
					
					String sql = "SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
					Map<String, Object> mapData = sqlQuery.queryForMap(sql,new Object[]{currentOrgId,judgeOprid});
					if(mapData!=null) {
						String name = mapData.get("TZ_REALNAME") == null ? "" : mapData.get("TZ_REALNAME").toString();
						String email = mapData.get("TZ_EMAIL") == null ? "" : mapData.get("TZ_EMAIL").toString();
						String mobile = mapData.get("TZ_MOBILE") == null ? "" : mapData.get("TZ_MOBILE").toString();
				
						createTaskServiceImpl.addAudCy(crtAudi,name, "", mobile, "", email, "", "", String.valueOf(judgeOprid), "",
							"", "");
					}
				}
				
				
				mapRet.put("audienceId", crtAudi);
				strRet = jacksonUtil.Map2json(mapRet);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*批量导出评委*/
	public String exportJudge(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//当前登录账号
			String currentDlzhId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
			
			jacksonUtil.json2Map(strParams);
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");	
			
			//获取文件存储路径
			String fileBasePath = getSysHardCodeVal.getDownloadPath();
			//材料评审excel存储路径
			String clpsExcelPath = "/material/xlsx";
			//完整的存储路径
			String fileDirPath = fileBasePath + clpsExcelPath;
			
			//生成表头
			List<String[]> dataCellKeys = new ArrayList<String[]>();
			dataCellKeys.add(new String[] {"judgeDlzhId","评委账号"});
			dataCellKeys.add(new String[] {"judgeName","评委姓名"});
			dataCellKeys.add(new String[] {"judgeGroupDesc","评委组"});
			dataCellKeys.add(new String[] {"judgeKsNum","需要评审考生人数"});
			dataCellKeys.add(new String[] {"judgePassword","评委密码"});
			
			//生成数据
			List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
			
			String sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialJudgeInfo");
			List<Map<String, Object>> listJudge = sqlQuery.queryForList(sql,new Object [] {classId,batchId});
			
			for(Map<String, Object> mapJudge : listJudge) {
				String judgeDlzhId = mapJudge.get("TZ_DLZH_ID") == null ? "" : mapJudge.get("TZ_DLZH_ID").toString();
				String judgeName = mapJudge.get("TZ_REALNAME") == null ? "" : mapJudge.get("TZ_REALNAME").toString();
				String judgeGroupDesc = mapJudge.get("TZ_CLPS_GR_NAME") == null ? "" : mapJudge.get("TZ_CLPS_GR_NAME").toString();
				String judgeKsNum = mapJudge.get("TZ_PYKS_XX") == null ? "" : mapJudge.get("TZ_PYKS_XX").toString();
				String judgePasswordJm = mapJudge.get("OPERPSWD") == null ? "" : mapJudge.get("OPERPSWD").toString();
				
				String judgePassword = DESUtil.decrypt(judgePasswordJm, "TZGD_Tranzvision");
				
				Map<String, Object> mapData = new HashMap<String,Object>();
				mapData.put("judgeDlzhId", judgeDlzhId);
				mapData.put("judgeName", judgeName);
				mapData.put("judgeGroupDesc", judgeGroupDesc);
				mapData.put("judgeKsNum", judgeKsNum);
				mapData.put("judgePassword", judgePassword);
			
				dataList.add(mapData);
			}
			
			//生成本次导出的文件名
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Random random = new Random();
			int max = 999999999;
			int min = 100000000;
			String fileName = simpleDateFormat.format(new Date()) + "_" + currentDlzhId.toUpperCase() + "_"
					+ String.valueOf(random.nextInt(max) % (max-min+1) + min) + ".xlsx";
			
			ExcelHandle excelHandle = new ExcelHandle(request,fileDirPath,currentOrgId,"apply");
			boolean rst = excelHandle.export2Excel(fileName, dataCellKeys, dataList);
			if(rst) {
				String fileUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath() + excelHandle.getExportExcelPath();
				mapRet.put("fileUrl", fileUrl);
				strRet = jacksonUtil.Map2json(mapRet);
			} else {
				errMsg[0] = "1";
				errMsg[1] = "导出失败";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	//查询评委组
	public String getJudgeGroup(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String,Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			jacksonUtil.json2Map(strParams);
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			
			String sql = "";
			
			//每位考生要求被几个评委审批
			sql = "SELECT TZ_MSPY_NUM FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			Integer judgeSetNum = sqlQuery.queryForObject(sql, new Object[]{classId,batchId},"Integer");
			if(!"0".equals(judgeSetNum) && judgeSetNum!=null) {
				
			} else {
				judgeSetNum = 2;
			}
			
			sql = "SELECT TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME FROM PS_TZ_CLPS_GR_TBL WHERE TZ_JG_ID=? ORDER BY CAST(TZ_CLPS_GR_ID AS SIGNED INTEGER) LIMIT 0,?";
			List<Map<String, Object>> listGroup = sqlQuery.queryForList(sql, new Object[]{orgId,judgeSetNum});
			
			List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
			
			for(Map<String, Object> mapGroup : listGroup) {
				String groupId = mapGroup.get("TZ_CLPS_GR_ID") == null ? "" : mapGroup.get("TZ_CLPS_GR_ID").toString();
				String groupName = mapGroup.get("TZ_CLPS_GR_NAME") == null ? "" : mapGroup.get("TZ_CLPS_GR_NAME").toString();
				
				Map<String, Object> mapData = new HashMap<String,Object>();
				mapData.put("TZ_CLPS_GR_ID", groupId);
				mapData.put("TZ_CLPS_GR_NAME", groupName);
				
				dataList.add(mapData);	
			}
			
			mapRet.put("groupData", dataList);
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	//校验评委各组评议人数合是否等于考生人数
	public String checkNum(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String,Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			jacksonUtil.json2Map(strParams);
			Integer clpsksNum = Integer.valueOf(jacksonUtil.getString("clpsksNum"));
			Integer judgeNumSet = Integer.valueOf(jacksonUtil.getString("judgeNumSet"));
			List<?> pweiData = jacksonUtil.getList("data");
			
			Boolean success = true;
			
			if(pweiData!=null && !"".equals(pweiData)) {
				
				List<Map<String, Object>> groupNumList = new ArrayList<Map<String,Object>>();
				String sql = "SELECT TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME FROM PS_TZ_CLPS_GR_TBL WHERE TZ_JG_ID=? ORDER BY CAST(TZ_CLPS_GR_ID AS SIGNED INTEGER) LIMIT 0,?";
				List<Map<String, Object>> listGroup = sqlQuery.queryForList(sql, new Object[]{orgId,judgeNumSet});
				
				for(Map<String, Object> mapGroup : listGroup) {
					Integer groupNum = 0;
					String groupId = mapGroup.get("TZ_CLPS_GR_ID") == null ? "" : mapGroup.get("TZ_CLPS_GR_ID").toString();
					for(Object pwei : pweiData) {
						Map<String, Object> mapPwei = (Map<String, Object>) pwei;
						String judgeGroup = mapPwei.get("judgeGroup") == null ? "" : mapPwei.get("judgeGroup").toString();
						Integer judgeExamineeNum = mapPwei.get("judgeExamineeNum") == null ? 0 : Integer.valueOf(mapPwei.get("judgeExamineeNum").toString());
						if(groupId.equals(judgeGroup)) {
							groupNum = groupNum + judgeExamineeNum;
						}
					}
					Map<String, Object> mapGroupNum = new HashMap<String,Object>();
					mapGroupNum.put("groupId", groupId);
					mapGroupNum.put("groupNum", groupNum);
					groupNumList.add(mapGroupNum);
				}
				
				if(listGroup.size()!=groupNumList.size()) {
					success = false;
				} else {
					for(Map<String, Object> mapNum : groupNumList) {
						Integer groupNum = mapNum.get("groupNum") == null ? 0 : Integer.valueOf(mapNum.get("groupNum").toString());
						if(!clpsksNum.equals(groupNum)) {
							success = false;
							break;
						} 
					}
				}				
			} 
			
			mapRet.put("success", success);
			strRet = jacksonUtil.Map2json(mapRet);
					
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
}
