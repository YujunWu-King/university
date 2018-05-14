package com.tranzvision.gd.TZAutomaticScreenBundle.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFormLabelTMapper;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTbl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTbl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTblKey;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLabelSetBundle.dao.PsTzLabelDfnTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;


/**
 * 自动初筛详情
 * @author zhanglang
 * 2017-02-16
 */
@Service("com.tranzvision.gd.TZAutomaticScreenBundle.service.impl.TzAutoScreenInfoServiceImpl")
public class TzAutoScreenInfoServiceImpl extends FrameworkImpl{

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal; 
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzCsKsTblMapper psTzCsKsTblMapper;
	
	@Autowired
	private PsTzFormLabelTMapper psTzFormLabelTMapper;
	
	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;
	
	@Autowired
	private PsTzLabelDfnTMapper psTzLabelDfnTMapper;
	
	@Autowired
	private TZGDObject tzSQLObject;
	
	@Override
	public String tzQuery(String strParams, String[] errorMsg) {
		String strRtn = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			String appId = jacksonUtil.getString("appId");
			
			mapRet.put("classId", classId);
			mapRet.put("batchId", batchId);
			mapRet.put("appId", appId);
			
			String sql = "select TZ_KSH_CSJG,TZ_KSH_PSPM,ROW_LASTMANT_OPRID,ROW_LASTMANT_DTTM from PS_TZ_CS_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?;";
			Map<String,Object> csKsMap = jdbcTemplate.queryForMap(sql, new Object[]{ classId,batchId,appId });
			if(csKsMap != null){
				String status = csKsMap.get("TZ_KSH_CSJG") == null ? "" : csKsMap.get("TZ_KSH_CSJG").toString();
				String ranking = csKsMap.get("TZ_KSH_PSPM") == null ? "" : csKsMap.get("TZ_KSH_PSPM").toString();
				String updateOpr = csKsMap.get("ROW_LASTMANT_OPRID") == null ? "" 
						: csKsMap.get("ROW_LASTMANT_OPRID").toString();
				
				mapRet.put("status", status);
				mapRet.put("ranking", ranking);
				
				if(!"".equals(updateOpr)){
					updateOpr = jdbcTemplate.queryForObject("select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=? limit 1"
							, new Object[]{ updateOpr }, "String");
				}
				mapRet.put("updateOpr", updateOpr);
				
				String dttmFormat = getSysHardCodeVal.getDateTimeFormat();
				SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
				
				if(csKsMap.get("ROW_LASTMANT_DTTM") != null){
					Date updateDate = (Date) csKsMap.get("ROW_LASTMANT_DTTM");
					mapRet.put("updateDttm", dttmSimpleDateFormat.format(updateDate));
				}
			}
			// 自动初筛成绩项2018-5-7 
			String sqlScoreId = "select TZ_SCORE_INS_ID from PS_TZ_CS_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?;";
			String strScoreId = jdbcTemplate.queryForObject(sqlScoreId, new Object[]{classId,batchId,appId},"String");
			// 面试成绩项ID
			String sqlScoreIdMs = "select TZ_SCORE_INS_ID from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?;";
			String strScoreIdMs = jdbcTemplate.queryForObject(sqlScoreIdMs, new Object[]{classId,batchId,appId},"String");
			
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			ArrayList<Map<String, String>> columnsList = new ArrayList<Map<String, String>>();
			rtnMap.put("columns", columnsList);
			String sqlAut = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzClassAutoScreenInfo");
			String sqlZj = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzClassAutoScreenInfoZj");

			Map<String, Object> classMap = jdbcTemplate.queryForMap(sqlAut, new Object[] { classId });
			if (classMap != null) {
				String orgId = classMap.get("TZ_JG_ID") == null ? "" : classMap.get("TZ_JG_ID").toString();
				String csTreeName = classMap.get("TREE_NAME") == null ? "" : classMap.get("TREE_NAME").toString();

				if (!"".equals(csTreeName) && csTreeName != null) {

					// 查询初筛模型中成绩项类型为“数字成绩录入项”且启用自动初筛的成绩项
					sqlAut = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzAutoScreenScoreItems");
					List<Map<String, Object>> itemsList = jdbcTemplate.queryForList(sqlAut, new Object[] { orgId, csTreeName });

					for (Map<String, Object> itemMap : itemsList) {
						String itemId = itemMap.get("TZ_SCORE_ITEM_ID").toString();
						String itemName = itemMap.get("DESCR").toString();
						String scoreNum = "0.00";

						if(strScoreId != null){
							String sqlV = "select TZ_SCORE_NUM,TZ_SCORE_DFGC from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?";
							Map<String, Object> scoreMap = jdbcTemplate.queryForMap(sqlV, new Object[] { strScoreId, itemId });
							
							if (scoreMap != null) {
								scoreNum = scoreMap.get("TZ_SCORE_NUM") == null ? "0.00" : scoreMap.get("TZ_SCORE_NUM").toString();
							}
						}
						Map<String, String> colMap = new HashMap<String, String>();
						colMap.put("columnId", itemId);
						colMap.put("columnDescr", itemName);
						colMap.put("columnValue", scoreNum);

						columnsList.add(colMap);
					}
					//rtnMap.replace("columns", columnsList);
				}
			}
			// 专家打分成绩项展示
			Map<String, Object> classMapZj = jdbcTemplate.queryForMap(sqlZj, new Object[] { classId });
			if (classMapZj != null) {
				String orgId = classMapZj.get("TZ_JG_ID") == null ? "" : classMapZj.get("TZ_JG_ID").toString();
				String csTreeName = classMapZj.get("TREE_NAME") == null ? "" : classMapZj.get("TREE_NAME").toString();
				
				if (!"".equals(csTreeName) && csTreeName != null) {
					
					// 查询初筛模型中成绩项类型为“数字成绩录入项”且启用自动初筛的成绩项
					sqlZj = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzAutoScreenScoreItemsZj");
					List<Map<String, Object>> itemsList = jdbcTemplate.queryForList(sqlZj, new Object[] { orgId, csTreeName });
					
					for (Map<String, Object> itemMap : itemsList) {
						String itemId = itemMap.get("TZ_SCORE_ITEM_ID").toString();
						String itemName = itemMap.get("DESCR").toString();
						String scoreNum = "0.00";
						
						if(strScoreIdMs != null){
							String sqlV = "select TZ_SCORE_NUM,TZ_SCORE_DFGC from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?";
							Map<String, Object> scoreMap = jdbcTemplate.queryForMap(sqlV, new Object[] { strScoreIdMs, itemId });
							
							if (scoreMap != null) {
								scoreNum = scoreMap.get("TZ_SCORE_NUM") == null ? "0.00" : scoreMap.get("TZ_SCORE_NUM").toString();
							}
						}
						Map<String, String> colMap = new HashMap<String, String>();
						colMap.put("columnId", itemId);
						colMap.put("columnDescr", itemName);
						colMap.put("columnValue", scoreNum);
						
						columnsList.add(colMap);
					}
					//rtnMap.replace("columns", columnsList);
				}
			}
			mapRet.put("column",columnsList );
			//自动标签
			/*int i = 0;
			sql = "select TZ_ZDBQ_ID from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
			List<Map<String,Object>> ksbqList = jdbcTemplate.queryForList(sql, new Object[]{ classId,batchId,appId });
			String [] ksbqArr = new String[ksbqList.size()];
			for(Map<String,Object> ksbqMap : ksbqList){
				ksbqArr[i] = ksbqMap.get("TZ_ZDBQ_ID").toString();
				i++;
			}
			mapRet.put("autoLabel", ksbqArr);*/
			
			
			//负面清单
			/*sql = "select TZ_FMQD_ID from PS_TZ_CS_KSFM_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
			List<Map<String,Object>> fmqdList = jdbcTemplate.queryForList(sql, new Object[]{ classId,batchId,appId });
			String [] fmbqArr = new String[fmqdList.size()];
			i = 0;
			for(Map<String,Object> fmbqMap : fmqdList){
				fmbqArr[i] = fmbqMap.get("TZ_FMQD_ID").toString();
				i++;
			}
			mapRet.put("negativeList", fmbqArr);*/
			
			
			//手工标签
			/*sql = "select TZ_LABEL_ID from PS_TZ_FORM_LABEL_T where TZ_APP_INS_ID=?";
			List<Map<String,Object>> labelList = jdbcTemplate.queryForList(sql, new Object[]{ appId });
			String [] labelArr = new String[labelList.size()];
			i = 0;
			for(Map<String,Object> labelMap: labelList){
				if(labelMap.get("TZ_LABEL_ID") != null){
					labelArr[i] = labelMap.get("TZ_LABEL_ID").toString();
					i++;
				}
			}
			mapRet.put("manualLabel", labelArr);*/
			
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}
	
	
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String classId = "";
			if(jacksonUtil.containsKey("classId")){
				classId = jacksonUtil.getString("classId");
			}
			
			String batchId = "";
			if(jacksonUtil.containsKey("batchId")){
				batchId = jacksonUtil.getString("batchId");
			}
			
			String appId = jacksonUtil.getString("appId");
			String queryType = jacksonUtil.getString("queryType");
			
			String sql;
			int count = 0;
			Map<String,Object> bqMap = null;
			ArrayList<Map<String, Object>> rootList = new ArrayList<Map<String, Object>>(); 
			if("KSBQ".equals(queryType)){
				sql = "select TZ_ZDBQ_ID,(select TZ_BIAOQZ_NAME from PS_TZ_BIAOQZ_BQ_T where TZ_BIAOQ_ID=TZ_ZDBQ_ID limit 1) as TZ_BIAOQZ_NAME from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
				List<Map<String,Object>> ksbqList = jdbcTemplate.queryForList(sql, new Object[]{ classId,batchId,appId });
				if(ksbqList != null){
					for(Map<String,Object> ksbqMap : ksbqList){
						count++;
						String id = ksbqMap.get("TZ_ZDBQ_ID").toString();
						String desc = ksbqMap.get("TZ_BIAOQZ_NAME") == null ? id : ksbqMap.get("TZ_BIAOQZ_NAME").toString();
						
						bqMap = new HashMap<String, Object>();
						bqMap.put("id", id);
						bqMap.put("desc", desc);
						
						rootList.add(bqMap);
					}
				}
			}else if("FMQD".equals(queryType)){
				sql = "select TZ_FMQD_ID,(select TZ_BIAOQZ_NAME from PS_TZ_BIAOQZ_BQ_T where TZ_BIAOQ_ID=TZ_FMQD_ID limit 1) as TZ_BIAOQZ_NAME from PS_TZ_CS_KSFM_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
				List<Map<String,Object>> fmqdList = jdbcTemplate.queryForList(sql, new Object[]{ classId,batchId,appId });
				if(fmqdList != null){
					for(Map<String,Object> ksbqMap : fmqdList){
						count++;
						String id = ksbqMap.get("TZ_FMQD_ID").toString();
						String desc = ksbqMap.get("TZ_BIAOQZ_NAME") == null ? id : ksbqMap.get("TZ_BIAOQZ_NAME").toString();
						
						bqMap = new HashMap<String, Object>();
						bqMap.put("id", id);
						bqMap.put("desc", desc);
						
						rootList.add(bqMap);
					}
				}
			}else if("SDBQ".equals(queryType)){
				String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				sql = "select TZ_LABEL_ID,TZ_LABEL_NAME from PS_TZ_LABEL_DFN_T where TZ_JG_ID=? and TZ_LABEL_STATUS='Y' union select A.TZ_LABEL_ID,TZ_LABEL_NAME from PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B where TZ_APP_INS_ID=? and A.TZ_LABEL_ID=B.TZ_LABEL_ID and TZ_JG_ID=?";
				List<Map<String,Object>> sgbqList = jdbcTemplate.queryForList(sql, new Object[]{ orgId,appId,orgId });
				if(sgbqList != null){
					for(Map<String,Object> sgbqMap: sgbqList){
						count++;
						String id = sgbqMap.get("TZ_LABEL_ID").toString();
						String desc = sgbqMap.get("TZ_LABEL_NAME") == null ? id : sgbqMap.get("TZ_LABEL_NAME").toString();
						
						bqMap = new HashMap<String, Object>();
						bqMap.put("id", id);
						bqMap.put("desc", desc);
						
						rootList.add(bqMap);
					}
				}
			}
			
			mapRet.replace("total", count);
			mapRet.replace("root", rootList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String classId = jacksonUtil.getString("classId");
				String batchId = jacksonUtil.getString("batchId");
				Long appId = Long.valueOf(jacksonUtil.getString("appId"));
				
				//String status = jacksonUtil.getString("status");
				
				/*String manualLabel = jacksonUtil.getString("manualLabel");
				List<String> manualLabelList = new ArrayList<String>();
				if(manualLabel != null && !"".equals(manualLabel)){
					manualLabelList = (List<String>) jacksonUtil.getList("manualLabel");
				}*/

				PsTzCsKsTblKey psTzCsKsTblKey = new PsTzCsKsTblKey();
				psTzCsKsTblKey.setTzClassId(classId);
				psTzCsKsTblKey.setTzApplyPcId(batchId);
				psTzCsKsTblKey.setTzAppInsId(appId);
				PsTzCsKsTbl psTzCsKsTbl = psTzCsKsTblMapper.selectByPrimaryKey(psTzCsKsTblKey);
				
				PsTzCjxTblKey psTzCjxTblKey=new PsTzCjxTblKey();
				PsTzCjxTblWithBLOBs psTzCjxTblWithBLOBs=new PsTzCjxTblWithBLOBs();
				Long strScoreId=0l;
				
				Date currDate = new Date();
				if(psTzCsKsTbl != null){
					strScoreId=psTzCsKsTbl.getTzScoreInsId();
					psTzCsKsTbl.setTzKshCsjg("Y");
					psTzCsKsTbl.setRowLastmantDttm(currDate);
					psTzCsKsTbl.setRowLastmantOprid(oprid);
					psTzCsKsTblMapper.updateByPrimaryKey(psTzCsKsTbl);
					
					Map<String,Object> formMap = new HashMap<String,Object>();
					
					String dttmFormat = getSysHardCodeVal.getDateTimeFormat();
					SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
					formMap.put("updateDttm", dttmSimpleDateFormat.format(currDate));
					
					String updateOpr = "";
					if(!"".equals(oprid)){
						updateOpr = jdbcTemplate.queryForObject("select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=? limit 1"
								, new Object[]{ oprid }, "String");
						if("".equals(updateOpr) || updateOpr == null){
							updateOpr = oprid;
						}
					}
					formMap.put("updateOpr", updateOpr);
					
					mapRet.replace("formData", formMap);
				}else{
					psTzCsKsTbl = new PsTzCsKsTbl();
					psTzCsKsTbl.setTzClassId(classId);
					psTzCsKsTbl.setTzApplyPcId(batchId);
					psTzCsKsTbl.setTzAppInsId(appId);
					psTzCsKsTbl.setTzKshCsjg("Y");
					psTzCsKsTbl.setRowAddedOprid(oprid);
					psTzCsKsTbl.setRowAddedDttm(currDate);
					psTzCsKsTbl.setRowLastmantDttm(currDate);
					psTzCsKsTbl.setRowLastmantOprid(oprid);
					psTzCsKsTblMapper.insert(psTzCsKsTbl);
				}
				Double total = 0.0;
				Double dScore = 0.0;
				BigDecimal newTotal= new BigDecimal(0.0);
				// 自动初筛成绩项2018-5-7
				if(!"".equals(strScoreId)&& strScoreId!=null){
					// 查询分数更改前的自动成绩总分
					String sqlOldTotal = "SELECT DISTINCT TZ_SCORE_NUM FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID='Total'";
					String oldTotal = jdbcTemplate.queryForObject(sqlOldTotal, new Object[] { strScoreId }, "String");
					
					String sqlAut = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzClassAutoScreenInfo");

					Map<String, Object> classMap = jdbcTemplate.queryForMap(sqlAut, new Object[] { classId });
					if (classMap != null) {
						String orgId = classMap.get("TZ_JG_ID") == null ? "" : classMap.get("TZ_JG_ID").toString();
						String csTreeName = classMap.get("TREE_NAME") == null ? "" : classMap.get("TREE_NAME").toString();

						if (!"".equals(csTreeName) && csTreeName != null) {

							// 查询初筛模型中成绩项类型为“数字成绩录入项”且启用自动初筛的成绩项
							sqlAut = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzAutoScreenScoreItems");
							List<Map<String, Object>> itemsList = jdbcTemplate.queryForList(sqlAut, new Object[] { orgId, csTreeName });
							//Double total = 0.0;
							for (Map<String, Object> itemMap : itemsList) {
								String itemId = itemMap.get("TZ_SCORE_ITEM_ID").toString();
								String itemName = itemMap.get("DESCR").toString();
								String scoreNum = jacksonUtil.getString(itemId);
								psTzCjxTblKey.setTzScoreInsId(strScoreId);
								psTzCjxTblKey.setTzScoreItemId(itemId);
								PsTzCjxTbl psTzCjxTbl = psTzCjxTblMapper.selectByPrimaryKey(psTzCjxTblKey);
								//Double dScore = 0.0;
								try {
									dScore = Double.parseDouble(scoreNum);
									total += dScore;
								} catch (NumberFormatException e) {
									e.printStackTrace();
									errMsg[0] = "1";
									errMsg[1] = "分数有误！";
									return strRet;
								}
								if(psTzCjxTbl != null){
									psTzCjxTbl.setTzScoreNum(java.math.BigDecimal.valueOf(dScore));
									psTzCjxTblMapper.updateByPrimaryKey(psTzCjxTbl);
									
									psTzCjxTblWithBLOBs =psTzCjxTblMapper.selectByPrimaryKey(psTzCjxTbl);
									if(psTzCjxTblWithBLOBs != null){
										String scoreDfcg=psTzCjxTblWithBLOBs.getTzScoreDfgc();
										if(scoreDfcg!=null){
											int i=scoreDfcg.lastIndexOf("|")+1;
											scoreDfcg= scoreDfcg.substring(0,i);
											psTzCjxTblWithBLOBs.setTzScoreDfgc(scoreDfcg+dScore+"分");
											psTzCjxTblMapper.updateByPrimaryKeyWithBLOBs(psTzCjxTblWithBLOBs);
										}
									}
								}else{
									psTzCjxTblWithBLOBs.setTzScoreInsId(strScoreId);
									psTzCjxTblWithBLOBs.setTzScoreItemId(itemId);
									psTzCjxTblWithBLOBs.setTzScoreNum(java.math.BigDecimal.valueOf(dScore));
									psTzCjxTblWithBLOBs.setTzCjxXlkXxbh(null);
									psTzCjxTblWithBLOBs.setTzScoreBz(null);
									psTzCjxTblWithBLOBs.setTzScorePyValue(null);
									psTzCjxTblWithBLOBs.setTzScoreDfgc(itemName+"|"+dScore+"分");
									psTzCjxTblMapper.insert(psTzCjxTblWithBLOBs);
								}
							}
							// 更新自动成绩项总分
							psTzCjxTblKey.setTzScoreInsId(strScoreId);
							psTzCjxTblKey.setTzScoreItemId("Total");
							PsTzCjxTbl psTzCjxTbl = psTzCjxTblMapper.selectByPrimaryKey(psTzCjxTblKey);
							if(psTzCjxTbl != null){
								psTzCjxTbl.setTzScoreNum(java.math.BigDecimal.valueOf(total));
								psTzCjxTblMapper.updateByPrimaryKey(psTzCjxTbl);
							}else{
								psTzCjxTblWithBLOBs.setTzScoreInsId(strScoreId);
								psTzCjxTblWithBLOBs.setTzScoreItemId("Total");
								psTzCjxTblWithBLOBs.setTzScoreNum(java.math.BigDecimal.valueOf(total));
								psTzCjxTblWithBLOBs.setTzCjxXlkXxbh(null);
								psTzCjxTblWithBLOBs.setTzScoreBz(null);
								psTzCjxTblWithBLOBs.setTzScorePyValue(null);
								psTzCjxTblWithBLOBs.setTzScoreDfgc(null);
								psTzCjxTblMapper.insert(psTzCjxTblWithBLOBs);
							}
							// 更新自动成绩+面试成绩总分（总分+现在-原来）
							// 查询原自动成绩和面试的总分
							// 查询总分的成绩项ID
							String sqlScoreItme = "SELECT TZ_SCORE_INS_ID FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
							String scoreInsId = jdbcTemplate.queryForObject(sqlScoreItme, new Object[] { classId,batchId,appId }, "String");
							
							String SumTotal="",scoreDfgc="";
							String sqlSumTotal = "SELECT TZ_SCORE_NUM,TZ_SCORE_DFGC FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID='SumTotal'";
							Map<String , Object> map = jdbcTemplate.queryForMap(sqlSumTotal, new Object[] { scoreInsId });
							if(map !=null){
								SumTotal = map.get("TZ_SCORE_NUM")== null ? "" :map.get("TZ_SCORE_NUM").toString();
								scoreDfgc = map.get("TZ_SCORE_DFGC")== null ? "" :map.get("TZ_SCORE_DFGC").toString();
							}
							BigDecimal numOldTotal = new BigDecimal(0.0),numSumTotal = new BigDecimal(0.0);
							//BigDecimal newTotal= new BigDecimal(0.0);
							BigDecimal newSumTotal= new BigDecimal(0.0);
							if(!"".equals(oldTotal)&& oldTotal!=null){
								numOldTotal=new BigDecimal(oldTotal);
							}
							if(!"".equals(SumTotal)&& SumTotal!=null){
								numSumTotal=new BigDecimal(SumTotal);
							}
							
							newTotal = BigDecimal.valueOf(total);
							newSumTotal=numSumTotal.add(newTotal).subtract(numOldTotal);
							// 评分标准
							String bzId = jdbcTemplate.queryForObject(
									"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?",
									new Object[] { "TZ_MBA_MS_RESULT" }, "String");
							String bzsql = "select TZ_M_FBDZ_MX_SM from PS_TZ_FBDZ_MX_TBL where TZ_M_FBDZ_ID=? and TZ_M_FBDZ_MX_SX>=? and TZ_M_FBDZ_MX_XX<=? ";
							// 计算档次
							String bzDsc = jdbcTemplate.queryForObject(bzsql, new Object[] { bzId, String.valueOf(newSumTotal), String.valueOf(newSumTotal) }, "String");
							if(bzDsc==null){
								bzDsc="";
							}
							if(!"".equals(scoreDfgc)){
								int j=scoreDfgc.lastIndexOf("|");
								scoreDfgc= scoreDfgc.substring(j);
								scoreDfgc="自动打分/管理员打分" + String.valueOf(newTotal) +scoreDfgc;
							}
							String isExist = "SELECT COUNT(1) FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID='SumTotal'";
							int count = jdbcTemplate.queryForObject(isExist, new Object[] { scoreInsId }, "Integer");
							if (count > 0) {
								String strUpdateSql = "UPDATE PS_TZ_CJX_TBL SET TZ_SCORE_NUM='" + newSumTotal
										+ "',TZ_SCORE_BZ='"+bzDsc+"',TZ_SCORE_DFGC='"+scoreDfgc+"' WHERE TZ_SCORE_INS_ID='" + scoreInsId + "'AND TZ_SCORE_ITEM_ID='" + "SumTotal" + "'";
								jdbcTemplate.update(strUpdateSql, new Object[] {});
							}else{
								String strInsertSql = "INSERT INTO PS_TZ_CJX_TBL(TZ_SCORE_INS_ID,TZ_SCORE_ITEM_ID,TZ_SCORE_NUM,TZ_SCORE_BZ,TZ_SCORE_DFGC) VALUES(?,?,?,?,?)";
								jdbcTemplate.update(strInsertSql, new Object[] { scoreInsId, "SumTotal", newSumTotal,bzDsc,scoreDfgc });
							}
						}
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "请先运行自动初筛";
				}
				
				// 更新专家打分
				String sqlScoreIdMs = "select TZ_SCORE_INS_ID from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?;";
				String strScoreIdMs = jdbcTemplate.queryForObject(sqlScoreIdMs, new Object[]{classId,batchId,appId},"String");
				if(!"".equals(strScoreIdMs)){
					// 查询原成绩
					String sqlZyxx = "SELECT DISTINCT TZ_SCORE_NUM FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID='ZYXX'";
					String oldTotal = jdbcTemplate.queryForObject(sqlZyxx, new Object[] { strScoreIdMs }, "String");
					
					String sqlZj = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzClassAutoScreenInfoZj");

					Map<String, Object> classMap = jdbcTemplate.queryForMap(sqlZj, new Object[] { classId });
					if (classMap != null) {
						String orgId = classMap.get("TZ_JG_ID") == null ? "" : classMap.get("TZ_JG_ID").toString();
						String csTreeName = classMap.get("TREE_NAME") == null ? "" : classMap.get("TREE_NAME").toString();

						if (!"".equals(csTreeName) && csTreeName != null) {

							// 查询初筛模型中成绩项类型为“数字成绩录入项”且启用自动初筛的成绩项
							sqlZj = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzAutoScreenScoreItemsZj");
							List<Map<String, Object>> itemsList = jdbcTemplate.queryForList(sqlZj, new Object[] { orgId, csTreeName });
							Double dScoreZj = 0.0;
							for (Map<String, Object> itemMap : itemsList) {
								String itemId = itemMap.get("TZ_SCORE_ITEM_ID").toString();
								String itemName = itemMap.get("DESCR").toString();
								String scoreNum = jacksonUtil.getString(itemId);
								psTzCjxTblKey.setTzScoreInsId(Long.valueOf(strScoreIdMs));
								psTzCjxTblKey.setTzScoreItemId(itemId);
								PsTzCjxTbl psTzCjxTbl = psTzCjxTblMapper.selectByPrimaryKey(psTzCjxTblKey);
								
								try {
									dScore = Double.parseDouble(scoreNum);
								} catch (NumberFormatException e) {
									e.printStackTrace();
									errMsg[0] = "1";
									errMsg[1] = "分数有误！";
									return strRet;
								}
								if(psTzCjxTbl != null){
									psTzCjxTbl.setTzScoreNum(java.math.BigDecimal.valueOf(dScore));
									psTzCjxTblMapper.updateByPrimaryKey(psTzCjxTbl);
									
									psTzCjxTblWithBLOBs =psTzCjxTblMapper.selectByPrimaryKey(psTzCjxTbl);
									if(psTzCjxTblWithBLOBs != null){
										String scoreDfcg=psTzCjxTblWithBLOBs.getTzScoreDfgc();
										if(scoreDfcg!=null){
											int i=scoreDfcg.lastIndexOf("|")+1;
											scoreDfcg= scoreDfcg.substring(0,i);
											psTzCjxTblWithBLOBs.setTzScoreDfgc(scoreDfcg+dScore+"分");
											psTzCjxTblMapper.updateByPrimaryKeyWithBLOBs(psTzCjxTblWithBLOBs);
										}
									}
								}else{
									psTzCjxTblWithBLOBs.setTzScoreInsId(Long.valueOf(strScoreIdMs));
									psTzCjxTblWithBLOBs.setTzScoreItemId(itemId);
									psTzCjxTblWithBLOBs.setTzScoreNum(java.math.BigDecimal.valueOf(dScore));
									psTzCjxTblWithBLOBs.setTzCjxXlkXxbh(null);
									psTzCjxTblWithBLOBs.setTzScoreBz(null);
									psTzCjxTblWithBLOBs.setTzScorePyValue(null);
									psTzCjxTblWithBLOBs.setTzScoreDfgc(itemName+"|"+dScore+"分");
									psTzCjxTblMapper.insert(psTzCjxTblWithBLOBs);
								}
							}
							// 更新自动成绩+面试成绩总分（总分+现在-原来）
							// 查询原自动成绩和面试的总分
							// 查询总分的成绩项ID
							String sqlScoreItme = "SELECT TZ_SCORE_INS_ID FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
							String scoreInsId = jdbcTemplate.queryForObject(sqlScoreItme, new Object[] { classId,batchId,appId }, "String");
							
							String SumTotal="",scoreDfgc="";
							String sqlSumTotal = "SELECT TZ_SCORE_NUM,TZ_SCORE_DFGC FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID='SumTotal'";
							Map<String , Object> map = jdbcTemplate.queryForMap(sqlSumTotal, new Object[] { scoreInsId });
							if(map !=null){
								SumTotal = map.get("TZ_SCORE_NUM")== null ? "" :map.get("TZ_SCORE_NUM").toString();
								scoreDfgc = map.get("TZ_SCORE_DFGC")== null ? "" :map.get("TZ_SCORE_DFGC").toString();
							}
							BigDecimal numOldTotal = new BigDecimal(0.0),numSumTotal = new BigDecimal(0.0);
							BigDecimal newTotalZj= new BigDecimal(0.0),newSumTotal= new BigDecimal(0.0),
									newSumTotaldf= new BigDecimal(0.0);
							if(!"".equals(oldTotal)&& oldTotal!=null){
								numOldTotal=new BigDecimal(oldTotal);
							}
							if(!"".equals(SumTotal)&& SumTotal!=null){
								numSumTotal=new BigDecimal(SumTotal);
							}
							
							newTotalZj = BigDecimal.valueOf(dScore);
							newSumTotal=numSumTotal.add(newTotalZj).subtract(numOldTotal);
							newSumTotaldf=newTotal.add(newTotalZj).subtract(numOldTotal);
							
							// 评分标准
							String bzId = jdbcTemplate.queryForObject(
									"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?",
									new Object[] { "TZ_MBA_MS_RESULT" }, "String");
							String bzsql = "select TZ_M_FBDZ_MX_SM from PS_TZ_FBDZ_MX_TBL where TZ_M_FBDZ_ID=? and TZ_M_FBDZ_MX_SX>=? and TZ_M_FBDZ_MX_XX<=? ";
							// 计算档次
							String bzDsc = jdbcTemplate.queryForObject(bzsql, new Object[] { bzId, String.valueOf(newSumTotal), String.valueOf(newSumTotal) }, "String");
							if(bzDsc==null){
								bzDsc="";
							}
							if(!"".equals(scoreDfgc)){
								int j=scoreDfgc.lastIndexOf("|");
								scoreDfgc= scoreDfgc.substring(j);
								scoreDfgc="自动打分/管理员打分" + String.valueOf(newSumTotaldf) +scoreDfgc;
							}
							String isExist = "SELECT COUNT(1) FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID='SumTotal'";
							int count = jdbcTemplate.queryForObject(isExist, new Object[] { strScoreIdMs }, "Integer");
							if (count > 0) {
								String strUpdateSql = "UPDATE PS_TZ_CJX_TBL SET TZ_SCORE_NUM='" + newSumTotal
										+ "',TZ_SCORE_BZ='"+bzDsc+"',TZ_SCORE_DFGC='"+scoreDfgc+"' WHERE TZ_SCORE_INS_ID='" + scoreInsId + "'AND TZ_SCORE_ITEM_ID='" + "SumTotal" + "'";
								jdbcTemplate.update(strUpdateSql, new Object[] {});
							}else{
								String strInsertSql = "INSERT INTO PS_TZ_CJX_TBL(TZ_SCORE_INS_ID,TZ_SCORE_ITEM_ID,TZ_SCORE_NUM,TZ_SCORE_BZ,TZ_SCORE_DFGC) VALUES(?,?,?,?,?)";
								jdbcTemplate.update(strInsertSql, new Object[] { strScoreIdMs, "SumTotal", newSumTotal,bzDsc,scoreDfgc });
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
}
