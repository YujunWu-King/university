package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpsGzTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpsKshTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsKshTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsKshTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 材料评审考生名单
 * @author LuYan
 * 2017-3-22
 *
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzClpsExamineeServiceImpl")
public class TzClpsExamineeServiceImpl extends FrameworkImpl {
	
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsTzClpsKshTblMapper psTzClpsKshTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzClpsGzTblMapper psTzClpsGzTblMapper;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	
	/*材料评审基本信息*/
	@Override
	public String tzQuery(String strParams,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String,Object>();
		Map<String, Object> mapData = new HashMap<String,Object>();
		
		try {
			jacksonUtil.json2Map(strParams);
			
			//班级编号
			String classId = jacksonUtil.getString("classId");
			//批次
			String batchId = jacksonUtil.getString("batchId");
			
			//班级名称
			String className = "";
			//批次名称
			String batchName = "";
			//当前评审轮次
			Integer dqpsLunc = 0;
			//当前评审状态
			String dqpsStatus = "";
			
			//报考考生数量
			Integer bkksNum=0;
			//材料评审考生
			Integer clpsksNum=0;
			//每生评审人数
			String judgeNumSet="";
			
			String sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialRuleInfo");
			Map<String, Object> mapBasic = sqlQuery.queryForMap(sql,new Object[] {classId,batchId});
			
			if(mapBasic!=null) {
				className = (String) mapBasic.get("TZ_CLASS_NAME");
				batchName = (String) mapBasic.get("TZ_BATCH_NAME");
				dqpsLunc = mapBasic.get("TZ_DQPY_LUNC") == null ? 0 : Integer.valueOf(mapBasic.get("TZ_DQPY_LUNC").toString());
				dqpsStatus = (String) mapBasic.get("TZ_DQPY_ZT"); 
				bkksNum = mapBasic.get("TZ_BKKS_NUM") == null ? 0 : Integer.valueOf(mapBasic.get("TZ_BKKS_NUM").toString());
				clpsksNum = mapBasic.get("TZ_CLPS_KS_NUM") == null ? 0 : Integer.valueOf(mapBasic.get("TZ_CLPS_KS_NUM").toString());
				judgeNumSet = mapBasic.get("TZ_MSPY_NUM") == null ? "" : String.valueOf(mapBasic.get("TZ_MSPY_NUM"));
				
				mapData.put("classId", classId);
				mapData.put("className", className);
				mapData.put("batchId", batchId);
				mapData.put("batchName", batchName);
				mapData.put("dqpsLunc", dqpsLunc);
				mapData.put("dqpsStatus", dqpsStatus);		
				mapData.put("bkksNum", bkksNum);
				mapData.put("clpsksNum", clpsksNum);
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
	
	
	/*考生名单列表*/
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
			
			//排序字段
			String[][] orderByArr = new String[][] {};
			
			//json数据要的结果字段
			String[] resultFldArray = {
					"TZ_CLASS_ID","TZ_APPLY_PC_ID","TZ_REALNAME","TZ_MSSQH","TZ_APP_INS_ID","TZ_GENDER","TZ_GENDER_DESC",
					"TZ_PW_LIST","TZ_PW_ZF","TZ_PWPS_ZT","TZ_MSHI_ZGFLG","TZ_MSHI_ZGFLG_DESC"};
			
			//可配置搜索通用函数
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errMsg);
			
			if(obj!=null && obj.length>0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for(int i=0;i<list.size();i++) {
					String[] rowList = list.get(i);
					
					String sql = "";
					
					String classId = rowList[0];
					String batchId = rowList[1];
					String appinsId = rowList[4];
					
					//评委列表、评审状态
					String pwList = "",reviewStatusDesc = "";
					//评委总分
					Float pwTotal = 0.00f;
					//评委数
					Integer pwNum = 0;
					//每生评审人数
					sql = "SELECT TZ_MSPY_NUM FROM PS_TZ_CLPS_GZ_TBL  WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
					Integer mspsNum = sqlQuery.queryForObject(sql, new Object[] {classId,batchId},"Integer");
					if(mspsNum==null) {
						mspsNum=0;
					}
					
					
					sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsPwInfo");
					List<Map<String, Object>> listPw = sqlQuery.queryForList(sql, new Object[] {classId,batchId,appinsId});
					
					for(Map<String, Object> mapPw : listPw) {	
						
						String pwOprid = mapPw.get("TZ_PWEI_OPRID")  == null ? "" : mapPw.get("TZ_PWEI_OPRID").toString();
						String pwDlzhId = mapPw.get("TZ_DLZH_ID") == null ? "" : mapPw.get("TZ_DLZH_ID").toString();
						String scoreInsId = mapPw.get("TZ_SCORE_INS_ID") == null ? "" : mapPw.get("TZ_SCORE_INS_ID").toString();
						Float scoreNum = mapPw.get("TZ_SCORE_NUM") == null ? Float.valueOf("0") : Float.valueOf(mapPw.get("TZ_SCORE_NUM").toString());
						String submitFlag = mapPw.get("TZ_SUBMIT_YN") == null ? "" : mapPw.get("TZ_SUBMIT_YN").toString();
						
						if("Y".equals(submitFlag)) {
							//已评审
							pwNum++;
						}
						
						if(!"".equals(pwList)) {
							pwList += "," + pwDlzhId;
						} else {
							pwList = pwDlzhId;
						}
						pwTotal += scoreNum;
					}
					
					if(mspsNum.equals(pwNum)) {
						reviewStatusDesc = "已完成";
					} else {
						reviewStatusDesc = "未完成（"+pwNum+"/"+mspsNum+"）";
					}
					
					Map<String, Object> mapList = new HashMap<String,Object>();
					mapList.put("classId", classId);
					mapList.put("batchId", batchId);
					mapList.put("name", rowList[2]);
					mapList.put("mssqh", rowList[3]);
					mapList.put("appinsId", appinsId);
					mapList.put("sex", rowList[5]);
					mapList.put("sexDesc", rowList[6]);
					mapList.put("judgeList", pwList);
					mapList.put("judgeTotal", pwTotal);
					mapList.put("reviewStatusDesc", reviewStatusDesc);
					mapList.put("interviewStatus", rowList[10]);
					mapList.put("interviewStatusDesc", rowList[11]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
			
			
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
				
				if("EXAMINEE".equals(typeFlag)) {
					//考生
					String strExaminee = saveExamineeInfo(mapData, errMsg);
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
			
			int num = 0;
			for(num=0;num<actData.length;num++) {
				String strForm =actData[num];
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String,Object> mapData = jacksonUtil.getMap("data");
				
				if("RULE".equals(typeFlag)) {
					//评审规则基本信息
					String strRule = saveRuleBasic(mapData,errMsg);
				}
				
				if("EXAMINEE".equals(typeFlag)) {
					//考生
					String strExaminee = saveExamineeInfo(mapData, errMsg);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/* 删除考生数据 */
	public String tzDelete(String[] actData,String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			if(actData.length==0 || actData==null) {
				return strRet;
			}
			
			int num = 0;
			for(num=0;num<actData.length;num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				
				String classId = jacksonUtil.getString("classId");
				String batchId = jacksonUtil.getString("batchId");
				String appinsId = jacksonUtil.getString("appinsId");
				
				PsTzClpsKshTblKey psTzClpsKshTblKey = new PsTzClpsKshTblKey();
				psTzClpsKshTblKey.setTzClassId(classId);
				psTzClpsKshTblKey.setTzApplyPcId(batchId);
				psTzClpsKshTblKey.setTzAppInsId(Long.valueOf(appinsId));
				
				psTzClpsKshTblMapper.deleteByPrimaryKey(psTzClpsKshTblKey);
				
				String sql = "DELETE FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
				sqlQuery.update(sql,new Object[] {classId,batchId,appinsId});
				
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
			//导出选中考生评议数据
			if("tzExportExaminee".equals(operateType)) {
				strRet = exportExaminee(strParams,errMsg);
			}		
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	//导出选中考生评议数据
	public String exportExaminee(String strParams,String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);
			
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			List<?> appinsIdList = jacksonUtil.getList("appinsIds");
			
			//当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//当前登录账号
			String currentDlzhId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
			
			String sql = "";
			
			//报名表中信息项编号
			//本/专科院校
			String schoolNameXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_BZKYX_XXX_ID");
			//最高学历
			String highestRecordXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_ZGXL_XXX_ID");
			//工作所在地
			String companyAddressXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_GZSZD_XXX_ID");
			//工作单位
			String companyNameXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_GZDW_XXX_ID");
			//所在部门
			String departmentXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_SZBM_XXX_ID");
			//工作职位
			String positionXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_GZZW_XXX_ID");
			//自主创业全称
			String selfEmploymentXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_ZZCYQC_XXX_ID");
			
			
			//获取文件存储路径
			String fileBasePath = getSysHardCodeVal.getDownloadPath();
			//材料评审excel存储路径
			String clpsExcelPath = "/material/xlsx";
			//完整的存储路径
			String fileDirPath = fileBasePath + clpsExcelPath;
			
			//生成表头
			List<String[]> dataCellKeys = new ArrayList<String[]>();
			dataCellKeys.add(new String[] {"className","报考方向"});
			dataCellKeys.add(new String[] {"nationId","证件号"});
			dataCellKeys.add(new String[] {"mssqh","面试申请号"});
			dataCellKeys.add(new String[] {"name","姓名"});
			dataCellKeys.add(new String[] {"judgeDlzhId","评委账号"});
			dataCellKeys.add(new String[] {"judgeNum","评委评审人数"});
			dataCellKeys.add(new String[] {"rank","考生评审排名"});
			
			//成绩项
			List<String[]> listScoreItemField = new ArrayList<String[]>();
			sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialScoreItemInfo");
			List<Map<String, Object>> listScoreItem = sqlQuery.queryForList(sql,new Object[] {currentOrgId,classId});
			
			for(Map<String, Object> mapScoreItem : listScoreItem) {
				String scoreItemId = mapScoreItem.get("TZ_SCORE_ITEM_ID") == null ? "" : mapScoreItem.get("TZ_SCORE_ITEM_ID").toString();
				String scoreItemName = mapScoreItem.get("DESCR") == null ? "" : mapScoreItem.get("DESCR").toString();
				String scoreItemType = mapScoreItem.get("TZ_SCORE_ITEM_TYPE") == null ? "" : mapScoreItem.get("TZ_SCORE_ITEM_TYPE").toString();
				dataCellKeys.add(new String[] {scoreItemId,scoreItemName});
				listScoreItemField.add(new String[] {scoreItemId,scoreItemType});
			}
				
			dataCellKeys.add(new String[] {"birthday","出生日期"});
			dataCellKeys.add(new String[] {"age","年龄"});
			dataCellKeys.add(new String[] {"sex","性别"});
			dataCellKeys.add(new String[] {"examineeTag","考生标签"});
			dataCellKeys.add(new String[] {"schoolName","本/专科院校"});
			dataCellKeys.add(new String[] {"highestRecord","最高学历"});
			dataCellKeys.add(new String[] {"companyAddress","工作所在地"});
			dataCellKeys.add(new String[] {"companyName","工作单位"});
			dataCellKeys.add(new String[] {"department","所在部门"});
			dataCellKeys.add(new String[] {"position","工作职位"});
			dataCellKeys.add(new String[] {"selfEmployment ","自主创业全称"});
			
			//生成数据
			List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
			
			for(Object objAppinsId : appinsIdList) {
				
				String appinsId = String.valueOf(objAppinsId);
				
				String className = "", nationId = "", mssqh = "", name = "", judgeDlzhId = "", judgeNum = "", rank = "",
						birthday = "", age = "", sex = "", examineeTag = "", schoolName = "", highestRecord = "", companyAddress = "",
						companyName = "", department = "", position = "", selfEmployment = "";
				String scoreInsId = "";
				
				//考生数据
				sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKspyInfo");
				Map<String,Object> mapExaminee = sqlQuery.queryForMap(sql,
						new Object[] {schoolNameXxxId,highestRecordXxxId,companyAddressXxxId,companyNameXxxId,departmentXxxId,positionXxxId,selfEmploymentXxxId,
								appinsId,classId});
				if(mapExaminee!=null) {
					className = mapExaminee.get("TZ_CLASS_NAME") == null ? "" : mapExaminee.get("TZ_CLASS_NAME").toString();
					nationId = mapExaminee.get("NATIONAL_ID") == null ? "" : mapExaminee.get("NATIONAL_ID").toString();
					mssqh = mapExaminee.get("TZ_MSSQH") == null ? "" : mapExaminee.get("TZ_MSSQH").toString();
					name = mapExaminee.get("TZ_REALNAME") == null ? "" : mapExaminee.get("TZ_REALNAME").toString();
					birthday = mapExaminee.get("BIRTHDATE") == null ? "" : mapExaminee.get("BIRTHDATE").toString();
					age = mapExaminee.get("AGE") == null ? "" : mapExaminee.get("AGE").toString();
					sex = mapExaminee.get("TZ_GENDER_DESC") == null ? "" : mapExaminee.get("TZ_GENDER_DESC").toString();
					schoolName = mapExaminee.get("TZ_SCHOOL_NAME") == null ? "" : mapExaminee.get("TZ_SCHOOL_NAME").toString();
					highestRecord = mapExaminee.get("TZ_ZGXL") == null ? "" : mapExaminee.get("TZ_ZGXL").toString();
					companyAddress = mapExaminee.get("TZ_GZ_SZD") == null ? "" : mapExaminee.get("TZ_GZ_SZD").toString();
					companyName = mapExaminee.get("TZ_COMPANY_NAME") == null ? "" : mapExaminee.get("TZ_COMPANY_NAME").toString();
					department = mapExaminee.get("TZ_DEPARTMENT") == null ? "" : mapExaminee.get("TZ_DEPARTMENT").toString();
					position = mapExaminee.get("TZ_POSITION") == null ? "" : mapExaminee.get("TZ_POSITION").toString();
					selfEmployment = mapExaminee.get("TZ_ZZCY_NAME") == null ? "" : mapExaminee.get("TZ_ZZCY_NAME").toString();					
					
					//评委数据
					sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialPwpyInfo");
					List<Map<String, Object>> listJudge = sqlQuery.queryForList(sql,new Object[]{classId,batchId,appinsId});
					
					for(Map<String, Object> mapJudge : listJudge) {
						
						judgeDlzhId = mapJudge.get("TZ_DLZH_ID") == null ? "" : mapJudge.get("TZ_DLZH_ID").toString();
						scoreInsId = mapJudge.get("TZ_SCORE_INS_ID") == null ? "" : mapJudge.get("TZ_SCORE_INS_ID").toString();
						judgeNum = mapJudge.get("TZ_PYKS_XX") == null ? "" : mapJudge.get("TZ_PYKS_XX").toString();
						rank = mapJudge.get("TZ_KSH_PSPM") == null ? "" : mapJudge.get("TZ_KSH_PSPM").toString();
						
						
						Map<String, Object> mapData = new HashMap<String,Object>();
						mapData.put("className", className);
						mapData.put("nationId", nationId);
						mapData.put("mssqh", mssqh);
						mapData.put("name", name);
						mapData.put("judgeDlzhId", judgeDlzhId);
						mapData.put("judgeNum", judgeNum);
						mapData.put("rank", rank);
						
						//成绩项分数
						for(String[] scoreField : listScoreItemField) {
							String scoreItemId = scoreField[0];
							String scoreItemType = scoreField[1];
							
							if("D".equals(scoreItemType)) {
								//下拉框
								sql = "SELECT TZ_CJX_XLK_XXBH FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
							} else {
								if("C".equals(scoreItemType)) {
									//评语
									sql = "SELECT TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
								} else {
									sql = "SELECT TZ_SCORE_NUM FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
								}
							}
							
							String scoreValue = sqlQuery.queryForObject(sql, new Object[]{scoreInsId,scoreItemId},"String");
							if("D".equals(scoreItemType)) {
								sql = "SELECT A.TZ_CJX_XLK_XXMC FROM PS_TZ_ZJCJXXZX_T A,PS_TZ_RS_MODAL_TBL B,PS_TZ_CLASS_INF_T C ";
								sql += " WHERE C.TZ_ZLPS_SCOR_MD_ID=B.TZ_SCORE_MODAL_ID AND A.TZ_JG_ID=B.TZ_JG_ID AND A.TREE_NAME=B.TREE_NAME";
								sql += " AND C.TZ_CLASS_ID=? AND A.TZ_JG_ID=? AND A.TZ_SCORE_ITEM_ID=? AND A.TZ_CJX_XLK_XXBH=?";
								scoreValue = sqlQuery.queryForObject(sql, new Object[]{classId,currentOrgId,scoreItemId,scoreValue},"String");
							}
							
							mapData.put(scoreItemId, scoreValue);
							
						}
						
						mapData.put("birthday", birthday);
						mapData.put("age", age);
						mapData.put("sex", sex);
						
						
						//考生标签：自动标签+手动标签
						sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsBqInfo");
						List<Map<String, Object>> listBq = sqlQuery.queryForList(sql,new Object[]{appinsId,appinsId});
						for(Map<String, Object> mapBq : listBq) {
							String bqName = mapBq.get("TZ_BQ_NAME") == null ? "" : mapBq.get("TZ_BQ_NAME").toString();
							if(!"".equals(examineeTag)) {
								examineeTag += "," + bqName;
							} else {
								examineeTag = bqName;
							}
						}
						mapData.put("examineeTag", examineeTag);
						
						mapData.put("schoolName", schoolName);
						mapData.put("highestRecord", highestRecord);
						mapData.put("companyAddress", companyAddress);
						mapData.put("companyName", companyName);
						mapData.put("department", department);
						mapData.put("position", position);
						mapData.put("selfEmployment", selfEmployment);
						
						dataList.add(mapData);
					}
				}				
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
	
	
	//评审规则保存
	public String saveRuleBasic(Map<String, Object> mapParams,String[] errMsg) {
		String strRet = "";
		
		try {
			
			//当前登录人
			String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
						
			String classId = (String) mapParams.get("classId");
			String batchId = (String) mapParams.get("batchId");
			String dqpsStatus = (String) mapParams.get("dqpsStatus");
			
			PsTzClpsGzTblKey psTzClpsGzTblKey = new PsTzClpsGzTblKey();
			psTzClpsGzTblKey.setTzClassId(classId);
			psTzClpsGzTblKey.setTzApplyPcId(batchId);
			
			PsTzClpsGzTbl psTzClpsGzTbl = psTzClpsGzTblMapper.selectByPrimaryKey(psTzClpsGzTblKey);
			
			if(psTzClpsGzTbl==null) {
				psTzClpsGzTbl = new PsTzClpsGzTbl();
				psTzClpsGzTbl.setTzClassId(classId);
				psTzClpsGzTbl.setTzApplyPcId(batchId);
				psTzClpsGzTbl.setTzDqpyZt(dqpsStatus);
				psTzClpsGzTbl.setRowAddedDttm(new Date());
				psTzClpsGzTbl.setRowAddedOprid(currentOprid);
				psTzClpsGzTbl.setRowLastmantDttm(new Date());
				psTzClpsGzTbl.setRowLastmantOprid(currentOprid);
				psTzClpsGzTblMapper.insertSelective(psTzClpsGzTbl);
			} else {
				/*psTzClpsGzTbl.setTzDqpyZt(dqpsStatus);
				psTzClpsGzTbl.setRowLastmantDttm(new Date());
				psTzClpsGzTbl.setRowLastmantOprid(currentOprid);
				psTzClpsGzTblMapper.updateByPrimaryKey(psTzClpsGzTbl);*/
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	//考生数据保存
	public String saveExamineeInfo(Map<String, Object> mapParams,String[] errMsg) {
		String strRet = "";
		
		try {
			
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			String classId = (String) mapParams.get("classId");
			String batchId = (String) mapParams.get("batchId");
			String appinsId = (String) mapParams.get("appinsId");
				
			PsTzClpsKshTblKey psTzClpsKshTblKey = new PsTzClpsKshTblKey();
			psTzClpsKshTblKey.setTzClassId(classId);
			psTzClpsKshTblKey.setTzApplyPcId(batchId);
			psTzClpsKshTblKey.setTzAppInsId(Long.valueOf(appinsId));
			
			PsTzClpsKshTbl psTzClpsKshTbl = psTzClpsKshTblMapper.selectByPrimaryKey(psTzClpsKshTblKey);
			if(psTzClpsKshTbl==null) {
				psTzClpsKshTbl = new PsTzClpsKshTbl();
				psTzClpsKshTbl.setTzClassId(classId);
				psTzClpsKshTbl.setTzApplyPcId(batchId);
				psTzClpsKshTbl.setTzAppInsId(Long.valueOf(appinsId));
				psTzClpsKshTbl.setTzMshiZgflg("W"); //待定
				psTzClpsKshTbl.setRowAddedDttm(new Date());
				psTzClpsKshTbl.setRowAddedOprid(oprid);
				psTzClpsKshTbl.setRowLastmantDttm(new Date());
				psTzClpsKshTbl.setRowLastmantOprid(oprid);
				psTzClpsKshTblMapper.insertSelective(psTzClpsKshTbl);
			} else {
				psTzClpsKshTblMapper.updateByPrimaryKey(psTzClpsKshTbl);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}

}
