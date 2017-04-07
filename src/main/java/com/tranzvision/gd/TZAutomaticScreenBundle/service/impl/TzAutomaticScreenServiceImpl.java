package com.tranzvision.gd.TZAutomaticScreenBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsJcAetMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsJcTMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsLsjcTMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsJcAet;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsJcT;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsJcTKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTbl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTblKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsLsjcTKey;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/***
 * 自动初筛
 * @author zhanglang
 * 2017/02/14
 */
@Service("com.tranzvision.gd.TZAutomaticScreenBundle.service.impl.TzAutomaticScreenServiceImpl")
public class TzAutomaticScreenServiceImpl extends FrameworkImpl{

	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TZGDObject tzSQLObject;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsTzCsJcTMapper psTzCsJcTMapper;
	
	@Autowired
	private TZGDObject tZGDObject;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzCsKsTblMapper psTzCsKsTblMapper;
	
	@Autowired
	private PsTzCsJcAetMapper psTzCsJcAetMapper;
	
	@Autowired
	private PsTzCsLsjcTMapper psTzCsLsjcTMapper;
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("total", 0);
		mapRet.put("root", listData);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			jacksonUtil.json2Map(strParams);
			//成绩项
			List<String> itemsList = (List<String>) jacksonUtil.getList("items");
			
			//TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.TZ_CS_STU_VW
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_CLASS_ID","TZ_BATCH_ID","TZ_APP_INS_ID","TZ_REALNAME","TZ_MSH_ID","TZ_KSH_CSJG","TZ_KSH_PSPM","TZ_SCORE_INS_ID"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				int num = list.size();
				for (int i = 0; i < num; i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					String classId = rowList[0];
					
					mapList.put("classId", classId);
					mapList.put("batchId", rowList[1]);
					mapList.put("appId", rowList[2]);
					mapList.put("name", rowList[3]);
					mapList.put("msApplyId", rowList[4]);
					
					boolean bool_status;
					if("N".equals(rowList[5])){
						bool_status = true;
					}else{
						bool_status = false;
					}
					mapList.put("status", bool_status);
					mapList.put("ranking", rowList[6]);
					
					//成绩单ID
					String scoreInsId = rowList[7];
					mapList.put("scoreInsId", scoreInsId);
					
					/*
					//查询成绩项分数
					String sql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzClassAutoScreenInfo");
					Map<String,Object> classMap = jdbcTemplate.queryForMap(sql, new Object[]{ classId });
					if(classMap != null){
						String orgId = classMap.get("TZ_JG_ID").toString();
						String csTreeName = classMap.get("TREE_NAME").toString();
						
						if(!"".equals(csTreeName) && csTreeName != null){

							//查询初筛模型中成绩项类型为“数字成绩录入项”且启用自动初筛的成绩项
							sql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzAutoScreenScoreItems");
							List<Map<String,Object>> itemsList = jdbcTemplate.queryForList(sql, new Object[]{ orgId, csTreeName });
							
							for(Map<String,Object> itemMap : itemsList){
								String itemId = itemMap.get("TZ_SCORE_ITEM_ID").toString();
								
								sql = "select TZ_SCORE_NUM,TZ_SCORE_DFGC from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?";
								Map<String,Object> scoreMap = jdbcTemplate.queryForMap(sql, new Object[]{ scoreInsId, itemId });
								
								String scoreNum = "0";
								String scoreGc = "";
								
								if(scoreMap != null){
									scoreNum = scoreMap.get("TZ_SCORE_NUM") == null? "0" : scoreMap.get("TZ_SCORE_NUM").toString();
									//打分过程
									scoreGc = scoreMap.get("TZ_SCORE_DFGC").toString();
								}

								mapList.put(itemId, scoreNum);
								mapList.put(itemId+"_label", scoreGc);
							}
						}
					}
					*/
					
					for(String itemId : itemsList){
						String sql = "select TZ_SCORE_NUM,TZ_SCORE_DFGC from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?";
						Map<String,Object> scoreMap = sqlQuery.queryForMap(sql, new Object[]{ scoreInsId, itemId });
						
						String scoreNum = "0";
						String scoreGc = "";
						
						if(scoreMap != null){
							scoreNum = scoreMap.get("TZ_SCORE_NUM") == null? "0" : scoreMap.get("TZ_SCORE_NUM").toString();
							//打分过程
							scoreGc = scoreMap.get("TZ_SCORE_DFGC").toString();
						}

						mapList.put(itemId, scoreNum);
						mapList.put(itemId+"_label", scoreGc);
					}
					
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
				case "queryScoreColumns":	//获取成绩项动态列
					strRet = this.queryScoreColumns(strParams,errorMsg);
					break;
				case "queryWeedOutInfo":	//获取设置淘汰信息
					strRet = this.queryWeedOutInfo(strParams,errorMsg);
					break;
				case "setWeedOutByOrder":	//设置批量淘汰
					strRet = this.setWeedOutByOrder(strParams,errorMsg);
					break;
				case "getLastEngineStatus":	//获取最后一次自动初筛引擎运行状态
					strRet = this.getLastEngineStatus(strParams,errorMsg);
					break;
				case "tzRunBatchProcess":	//运行自动初筛引擎
					strRet = this.tzRunBatchProcess(strParams,errorMsg);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}
	
	
	/**
	 * 获取成绩项动态列
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String queryScoreColumns(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		ArrayList<Map<String,String>> columnsList = new ArrayList<Map<String,String>>();
		rtnMap.put("className", "");
		rtnMap.put("columns", columnsList);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//班级ID
			String classId = jacksonUtil.getString("classId");
			String sql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzClassAutoScreenInfo");
			
			Map<String,Object> classMap = sqlQuery.queryForMap(sql, new Object[]{ classId });
			if(classMap != null){
				String className = classMap.get("TZ_CLASS_NAME") == null ? "" 
						: classMap.get("TZ_CLASS_NAME").toString();
				String orgId = classMap.get("TZ_JG_ID") == null ? "" 
						: classMap.get("TZ_JG_ID").toString();
				String csTreeName = classMap.get("TREE_NAME") == null ? "" 
						: classMap.get("TREE_NAME").toString();
				
				if(!"".equals(csTreeName) && csTreeName != null){
					
					//查询初筛模型中成绩项类型为“数字成绩录入项”且启用自动初筛的成绩项
					sql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzAutoScreenScoreItems");
					List<Map<String,Object>> itemsList = sqlQuery.queryForList(sql, new Object[]{ orgId, csTreeName });
					
					for(Map<String,Object> itemMap : itemsList){
						String itemId = itemMap.get("TZ_SCORE_ITEM_ID").toString();
						String itemName = itemMap.get("DESCR").toString();
						
						Map<String,String> colMap = new HashMap<String,String>();
						colMap.put("columnId", itemId);
						colMap.put("columnDescr", itemName);
						
						columnsList.add(colMap);
					}
					rtnMap.replace("columns", columnsList);
				}
				rtnMap.replace("className", className);
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 获取设置淘汰信息
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String queryWeedOutInfo(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("totalNum", "0");
		rtnMap.put("screenNum", "0");
		rtnMap.put("lastNum", "0");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			
			if(!"".equals(classId) && classId != null 
					&& !"".equals(batchId) && batchId != null){
				//报考总数量
				String sql = "select count(1) from PS_TZ_FORM_WRK_T where TZ_CLASS_ID=? and TZ_BATCH_ID=?";
				int totalNum = sqlQuery.queryForObject(sql, new Object[]{ classId,batchId }, "Integer");
				//参与初筛人数
				sql = "select count(1) from PS_TZ_CS_STU_VW where TZ_CLASS_ID=? and TZ_BATCH_ID=?";
				int screenNum = sqlQuery.queryForObject(sql, new Object[]{ classId,batchId }, "Integer");
				
				//淘汰比率
				sql = "select TZ_TT_BL from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?";
				Float outBl = sqlQuery.queryForObject(sql, new Object[]{ classId }, "Float");
				//淘汰人数
				int lastNum = (int) (screenNum*outBl/100);
				
				rtnMap.replace("totalNum", totalNum);
				rtnMap.replace("screenNum", screenNum);
				rtnMap.replace("lastNum", lastNum);
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	/**
	 * 设置批量淘汰
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String setWeedOutByOrder(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			short outNum = Short.valueOf(jacksonUtil.getString("outNum"));
			
			if(!"".equals(classId) && classId != null 
					&& !"".equals(batchId) && batchId != null){
				/**淘汰后N名考生**/
				
				//最后名次
				String sql = "select max(TZ_KSH_PSPM) from PS_TZ_CS_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?";
				int lastMc = 0;
				String lastMci = sqlQuery.queryForObject(sql, new Object[]{ classId, batchId }, "String");
				if(!"".equals(lastMci) && lastMci != null){
					lastMc = Integer.valueOf(lastMci);
				}
				
				int i;
				for(i=0;i<outNum;i++){
					//淘汰名次
					int outMc = lastMc - i;
					if(outMc>0){
						sqlQuery.update("update PS_TZ_CS_KS_TBL set TZ_KSH_CSJG='N' where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_KSH_PSPM=?", new Object[]{ classId, batchId, outMc });
						sqlQuery.execute("commit");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 获取最后一次自动初筛引擎运行状态
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String getLastEngineStatus(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("status", "");
		rtnMap.put("processIns", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			
			if(!"".equals(classId) && classId != null 
					&& !"".equals(batchId) && batchId != null){
				String sql = "select PRCSINSTANCE from PS_TZ_CS_JC_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?";
				Map<String,Object> procMap = sqlQuery.queryForMap(sql, new Object[]{ classId,batchId });
				
				if(procMap != null){
					int processIns = Integer.valueOf(procMap.get("PRCSINSTANCE").toString());
					
					sql = "select TZ_JOB_YXZT from TZ_JC_SHLI_T where TZ_JCSL_ID=?";
					String status = sqlQuery.queryForObject(sql, new Object[]{ processIns }, "String");
					
					rtnMap.replace("processIns", processIns);
					rtnMap.replace("status", status);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 运行自动初筛引擎
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzRunBatchProcess(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("status", "");
		rtnMap.put("processIns", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			
			if(!"".equals(classId) && classId != null 
					&& !"".equals(batchId) && batchId != null){
				//当前用户;
				String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				/*生成运行控制ID*/
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			    String s_dtm = datetimeFormate.format(new Date());
				String runCntlId = "ZDCS" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");
				
				
				PsTzCsJcAet psTzCsJcAet = new PsTzCsJcAet();
				psTzCsJcAet.setRunId(runCntlId);
				psTzCsJcAet.setTzClassId(classId);
				psTzCsJcAet.setTzApplyPcId(batchId);
				psTzCsJcAet.setOprid(currentOprid);
				psTzCsJcAetMapper.insert(psTzCsJcAet);
				
				String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
				String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				BaseEngine tmpEngine = tZGDObject.createEngineProcess(currentOrgId, "TZ_AUTO_SCREEN_PROC");
				//指定调度作业的相关参数
				EngineParameters schdProcessParameters = new EngineParameters();

				schdProcessParameters.setBatchServer("");
				schdProcessParameters.setCycleExpression("");
				schdProcessParameters.setLoginUserAccount(currentAccountId);
				schdProcessParameters.setPlanExcuteDateTime(new Date());
				schdProcessParameters.setRunControlId(runCntlId);
				
				//调度作业
				tmpEngine.schedule(schdProcessParameters);
				
				// 进程id;
				int processinstance = sqlQuery.queryForObject("SELECT TZ_JCSL_ID FROM TZ_JC_SHLI_T where TZ_YUNX_KZID = ? limit 0,1", new Object[] { runCntlId },"Integer");
				
				PsTzCsJcTKey psTzCsJcTKey = new PsTzCsJcTKey();
				psTzCsJcTKey.setTzClassId(classId);
				psTzCsJcTKey.setTzApplyPcId(batchId);
				PsTzCsJcT psTzCsJcT = psTzCsJcTMapper.selectByPrimaryKey(psTzCsJcTKey);
				if(psTzCsJcT != null){
					psTzCsJcT.setPrcsinstance(processinstance);
					psTzCsJcTMapper.updateByPrimaryKey(psTzCsJcT);
				}else{
					psTzCsJcT = new PsTzCsJcT();
					psTzCsJcT.setTzClassId(classId);
					psTzCsJcT.setTzApplyPcId(batchId);
					psTzCsJcT.setPrcsinstance(processinstance);
					psTzCsJcTMapper.insert(psTzCsJcT);
				}
				
				PsTzCsLsjcTKey psTzCsLsjcTKey = new PsTzCsLsjcTKey();
				psTzCsLsjcTKey.setTzClassId(classId);
				psTzCsLsjcTKey.setTzApplyPcId(batchId);
				psTzCsLsjcTKey.setPrcsinstance(processinstance);
				psTzCsLsjcTMapper.insert(psTzCsLsjcTKey);
				
				rtnMap.replace("processIns", processinstance);
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
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
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String type = jacksonUtil.getString("type");
				//保存grid修改
				if("GRIDSAVE".equals(type)){
					List<Map<String,String>> stuList = (List<Map<String, String>>) jacksonUtil.getList("data");
					for(Map<String,String> stuMap: stuList){
						String classId = stuMap.get("classId");
						String batchId = stuMap.get("batchId");
						Long appId = Long.valueOf(stuMap.get("appId"));
						String status = stuMap.get("status");
						
						PsTzCsKsTblKey psTzCsKsTblKey = new PsTzCsKsTblKey();
						psTzCsKsTblKey.setTzClassId(classId);
						psTzCsKsTblKey.setTzApplyPcId(batchId);
						psTzCsKsTblKey.setTzAppInsId(appId);
						PsTzCsKsTbl psTzCsKsTbl = psTzCsKsTblMapper.selectByPrimaryKey(psTzCsKsTblKey);
						
						if(psTzCsKsTbl != null){
							psTzCsKsTbl.setTzKshCsjg(status);
							psTzCsKsTbl.setRowLastmantDttm(new Date());
							psTzCsKsTbl.setRowLastmantOprid(oprid);
							psTzCsKsTbl.setRowLastmantDttm(new Date());
							psTzCsKsTbl.setRowLastmantOprid(oprid);
							psTzCsKsTblMapper.updateByPrimaryKey(psTzCsKsTbl);
						}else{
							psTzCsKsTbl = new PsTzCsKsTbl();
							psTzCsKsTbl.setTzClassId(classId);
							psTzCsKsTbl.setTzApplyPcId(batchId);
							psTzCsKsTbl.setTzAppInsId(appId);
							psTzCsKsTbl.setTzKshCsjg(status);
							psTzCsKsTbl.setRowAddedDttm(new Date());
							psTzCsKsTbl.setRowAddedOprid(oprid);
							psTzCsKsTbl.setRowLastmantDttm(new Date());
							psTzCsKsTbl.setRowLastmantOprid(oprid);
							psTzCsKsTblMapper.insert(psTzCsKsTbl);
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
