package com.tranzvision.gd.TZScoreModeManagementBundle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMenuMgBundle.service.impl.TzMenuTreeNodeServiceImpl;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzModalDtTblMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzZjcjxXzxTMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTblKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTblWithBLOBs;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzZjcjxXzxT;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzZjcjxXzxTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.tree.TreeManager;


@Service("com.tranzvision.gd.TZScoreModeManagementBundle.service.impl.TzScoreItemsInfoServiceImpl")
public class TzScoreItemsInfoServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private PsTzModalDtTblMapper psTzModalDtTblMapper;
	
	@Autowired
	private PsTzZjcjxXzxTMapper psTzZjcjxXzxTMapper;
	
	@Autowired
	private TzMenuTreeNodeServiceImpl tzMenuTreeNodeServiceImpl;
	
	private String currItemId;
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRtn = "{}";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		try {
			jacksonUtil.json2Map(strParams);

			String treeName = jacksonUtil.getString("treeName");
			String OpeItemId = jacksonUtil.getString("OpeItemId");
			
			
			PsTzModalDtTblKey psTzModalDtTblKey = new PsTzModalDtTblKey();
			psTzModalDtTblKey.setTzJgId(orgId);
			psTzModalDtTblKey.setTreeName(treeName);
			psTzModalDtTblKey.setTzScoreItemId(OpeItemId);
			PsTzModalDtTblWithBLOBs psTzModalDtTbl = psTzModalDtTblMapper.selectByPrimaryKey(psTzModalDtTblKey);
			if(psTzModalDtTbl != null){
				/*成绩项名称*/
				String itemName = psTzModalDtTbl.getDescr() == null ? "" : psTzModalDtTbl.getDescr();
				/*成绩项类型*/
				String itemType = psTzModalDtTbl.getTzScoreItemType() == null ? "" : psTzModalDtTbl.getTzScoreItemType();
				/*汇总系数*/
				BigDecimal UpHzXs = psTzModalDtTbl.getTzScoreHz() == null ? new BigDecimal(0) : psTzModalDtTbl.getTzScoreHz();
				/*权重*/
				BigDecimal weightA = psTzModalDtTbl.getTzScoreQz() == null ? new BigDecimal(0) : psTzModalDtTbl.getTzScoreQz();
				/*下限操作符*/
				String lowerOperator = psTzModalDtTbl.getTzMFbdzMxXxJx() == null ? "" : psTzModalDtTbl.getTzMFbdzMxXxJx();
				/*分值下限*/
				BigDecimal lowerLimit = psTzModalDtTbl.getTzScoreLimited2() == null ? new BigDecimal(0) : psTzModalDtTbl.getTzScoreLimited2();
				/*上限操作符*/
				String upperOperator = psTzModalDtTbl.getTzMFbdzMxSxJx() == null ? "" : psTzModalDtTbl.getTzMFbdzMxSxJx();
				/*分值上限*/
				BigDecimal upperLimit = psTzModalDtTbl.getTzScoreLimited() == null ? new BigDecimal(0) : psTzModalDtTbl.getTzScoreLimited();
				/*评语的字数下限*/
				Integer wordLowerLimit = psTzModalDtTbl.getTzScorePyZslim0() == null ? 0 : psTzModalDtTbl.getTzScorePyZslim0();
				/*评语的字数上限*/
				Integer wordUpperLimit = psTzModalDtTbl.getTzScorePyZslim() == null ? 0 : psTzModalDtTbl.getTzScorePyZslim();
				/*下拉选项转换为分值*/
				String xlTransScore = psTzModalDtTbl.getTzScrToScore() == null ? "" : psTzModalDtTbl.getTzScrToScore();
				/*下拉框权重*/
				BigDecimal weightD = psTzModalDtTbl.getTzScrSqz() == null ? new BigDecimal(0) : psTzModalDtTbl.getTzScrSqz();
				/*自动初筛标记*/
				String autoScreen = psTzModalDtTbl.getTzIfZdcs() == null ? "N" : psTzModalDtTbl.getTzIfZdcs();
				/*成绩项打分规则*/
				String scoringRules = psTzModalDtTbl.getTzZdcsgzId() == null ? "" : psTzModalDtTbl.getTzZdcsgzId();
				/*参考资料*/
				String refDataSet = psTzModalDtTbl.getTzScoreCkzl() == null ? "" : psTzModalDtTbl.getTzScoreCkzl();
				/*参考问题--说明*/
				String  descr = psTzModalDtTbl.getTzScoreItemCkwt() == null ? "" : psTzModalDtTbl.getTzScoreItemCkwt();
				/*打分说明--标准*/
				String standard = psTzModalDtTbl.getTzScoreItemDfsm() == null ? "" : psTzModalDtTbl.getTzScoreItemDfsm();
				/*面试方法*/
				String interviewMethod = psTzModalDtTbl.getTzScoreItemMsff() == null ? "" : psTzModalDtTbl.getTzScoreItemMsff();
				

				mapRet.put("orgId", orgId);
				mapRet.put("treeName", treeName);
				mapRet.put("itemId", OpeItemId);
				mapRet.put("itemName", itemName);
				mapRet.put("itemType", itemType);
				
				mapRet.put("UpHzXs", UpHzXs.toString());
				mapRet.put("weightA", weightA.toString());
				mapRet.put("lowerOperator", lowerOperator);
				mapRet.put("lowerLimit", lowerLimit.toString());
				mapRet.put("upperOperator", upperOperator);
				mapRet.put("upperLimit", upperLimit.toString());
				
				mapRet.put("wordLowerLimit", wordLowerLimit.toString());
				mapRet.put("wordUpperLimit", wordUpperLimit.toString());
				
				mapRet.put("xlTransScore", xlTransScore);
				mapRet.put("weightD", weightD.toString());
				
				mapRet.put("autoScreen", autoScreen);
				mapRet.put("scoringRules", scoringRules);
				
				String refDataDescr = "";
				if(!"".equals(refDataSet) && refDataSet != null){
					String ckzlSql = "select TZ_CKZL_NAME from PS_TZ_CKZL_T where TZ_JG_ID=? and TZ_CKZL_ID=?";
					refDataDescr = sqlQuery.queryForObject(ckzlSql, new Object[]{ orgId,refDataSet }, "String");
				}
				
				mapRet.put("refDataSet", refDataSet);
				mapRet.put("refDataDescr", refDataDescr);
				
				mapRet.put("standard", standard);
				mapRet.put("descr", descr);
				mapRet.put("interviewMethod", interviewMethod);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}
	
	
	/**
	 * 成绩项下拉框选项
	 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "");
		
		try {
			jacksonUtil.json2Map(strParams);
			String orgId = jacksonUtil.getString("orgId");
			String treeName = jacksonUtil.getString("treeName");
			String itemId = jacksonUtil.getString("itemId");
			
			if (null != orgId && !"".equals(orgId) && null != treeName && !"".equals(treeName)) {
				String sql = "SELECT COUNT(*) FROM PS_TZ_ZJCJXXZX_T WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
				int total = jdbcTemplate.queryForObject(sql, new Object[] { orgId, treeName, itemId }, "int");
				
				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

				if (total > 0) {
					sql = "SELECT TZ_CJX_XLK_XXBH,TZ_CJX_XLK_XXMC,TZ_CJX_XLK_XXFZ,TZ_CJX_XLK_MRZ FROM PS_TZ_ZJCJXXZX_T WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";

					List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[] { orgId, treeName, itemId });

					for (Map<String, Object> mapData : listData) {
						Map<String, Object> mapJson = new HashMap<String, Object>();

						mapJson.put("orgId", orgId);
						mapJson.put("treeName", treeName);
						mapJson.put("itemId", itemId);

						mapJson.put("optId", mapData.get("TZ_CJX_XLK_XXBH") == null ? "" : String.valueOf(mapData.get("TZ_CJX_XLK_XXBH")));

						mapJson.put("optName", mapData.get("TZ_CJX_XLK_XXMC") == null ? "" : String.valueOf(mapData.get("TZ_CJX_XLK_XXMC")));

						mapJson.put("optScore", mapData.get("TZ_CJX_XLK_XXFZ") == null ? "" : String.valueOf(mapData.get("TZ_CJX_XLK_XXFZ")));
						
						String mrz = mapData.get("TZ_CJX_XLK_MRZ") == null ? "" : String.valueOf(mapData.get("TZ_CJX_XLK_MRZ"));
						Boolean isMrz;
						if("Y".equals(mrz)){
							isMrz = true;
						}else{
							isMrz = false;
						}
						mapJson.put("isDefault", isMrz);

						listJson.add(mapJson);
					}
					
					mapRet.replace("total", total);
					mapRet.replace("root", listJson);
					strRet = jacksonUtil.Map2json(mapRet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}

		return strRet;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
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
				
				if("nodeInfo".equals(type)){
					Map<String,Object> formDate = jacksonUtil.getMap("data");
					
					String orgId = formDate.get("orgId").toString();
					String treeName = formDate.get("treeName").toString();
					String itemId = formDate.get("itemId").toString();
					String itemName = formDate.get("itemName").toString();
					String itemType = formDate.get("itemType").toString();
					//String UpHzXs = formDate.get("UpHzXs").toString();
					BigDecimal UpHzXs = formDate.get("UpHzXs") == null || "".equals(formDate.get("UpHzXs").toString()) ? new BigDecimal(0) 
							: BigDecimal.valueOf(Double.valueOf(formDate.get("UpHzXs").toString()));
					//String weightA = formDate.get("weightA").toString();
					BigDecimal weightA = formDate.get("weightA") == null || "".equals(formDate.get("weightA").toString()) ? new BigDecimal(0) 
							: BigDecimal.valueOf(Double.valueOf(formDate.get("weightA").toString()));
					String lowerOperator = formDate.get("lowerOperator").toString();
					//String lowerLimit = formDate.get("lowerLimit").toString();
					BigDecimal lowerLimit = formDate.get("lowerLimit") == null || "".equals(formDate.get("lowerLimit").toString()) ? new BigDecimal(0) 
							: BigDecimal.valueOf(Double.valueOf(formDate.get("lowerLimit").toString()));
					String upperOperator = formDate.get("upperOperator").toString();
					//String upperLimit = formDate.get("upperLimit").toString();
					BigDecimal upperLimit = formDate.get("upperLimit") == null || "".equals(formDate.get("upperLimit").toString()) ? new BigDecimal(0) 
							: BigDecimal.valueOf(Double.valueOf(formDate.get("upperLimit").toString()));
					//String wordLowerLimit = formDate.get("wordLowerLimit").toString();
					Integer wordLowerLimit = formDate.get("wordLowerLimit") == null || "".equals(formDate.get("wordLowerLimit")) ? 0 
							: Integer.valueOf(formDate.get("wordLowerLimit").toString());
					//String wordUpperLimit = formDate.get("wordUpperLimit").toString();
					Integer wordUpperLimit = formDate.get("wordUpperLimit") == null || "".equals(formDate.get("wordUpperLimit").toString()) ? 0 
							: Integer.valueOf(formDate.get("wordUpperLimit").toString());
					String xlTransScore = formDate.get("xlTransScore").toString();
					//String weightD = formDate.get("weightD").toString();
					BigDecimal weightD = formDate.get("weightD") == null || "".equals(formDate.get("weightD").toString()) ? new BigDecimal(0) 
							: BigDecimal.valueOf(Double.valueOf(formDate.get("weightD").toString()));
					
					String autoScreen = formDate.get("autoScreen").toString();
					String scoringRules = formDate.get("scoringRules").toString();
					
					String refDataSet = formDate.get("refDataSet").toString();
					//说明
					String descr = formDate.get("standard").toString();
					//标准
					String standard = formDate.get("descr").toString();
					String interviewMethod = formDate.get("interviewMethod").toString();
					
					
					PsTzModalDtTblKey psTzModalDtTblKey = new PsTzModalDtTblKey();
					psTzModalDtTblKey.setTzJgId(orgId);
					psTzModalDtTblKey.setTreeName(treeName);
					psTzModalDtTblKey.setTzScoreItemId(itemId);
					
					PsTzModalDtTblWithBLOBs psTzModalDtTbl = psTzModalDtTblMapper.selectByPrimaryKey(psTzModalDtTblKey);
					
					if(psTzModalDtTbl != null){
						psTzModalDtTbl.setDescr(itemName);
						psTzModalDtTbl.setTzScoreItemType(itemType);
						psTzModalDtTbl.setTzScoreHz(UpHzXs);
						psTzModalDtTbl.setTzScoreQz(weightA);
						
						psTzModalDtTbl.setTzMFbdzMxXxJx(lowerOperator);
						psTzModalDtTbl.setTzScoreLimited2(lowerLimit);
						psTzModalDtTbl.setTzMFbdzMxSxJx(upperOperator);
						psTzModalDtTbl.setTzScoreLimited(upperLimit);
						
						psTzModalDtTbl.setTzScorePyZslim0(wordLowerLimit);
						psTzModalDtTbl.setTzScorePyZslim(wordUpperLimit);
						
						
						psTzModalDtTbl.setTzScrToScore(xlTransScore);
						psTzModalDtTbl.setTzScrSqz(weightD);
						psTzModalDtTbl.setTzIfZdcs(autoScreen);
						psTzModalDtTbl.setTzZdcsgzId(scoringRules);
						psTzModalDtTbl.setTzScoreCkzl(refDataSet);
						
						psTzModalDtTbl.setTzScoreItemDfsm(descr);
						psTzModalDtTbl.setTzScoreItemCkwt(standard);
						psTzModalDtTbl.setTzScoreItemMsff(interviewMethod);
						
						psTzModalDtTbl.setRowLastmantDttm(new Date());
						psTzModalDtTbl.setRowLastmantOprid(oprid);
						
						int rtn = psTzModalDtTblMapper.updateByPrimaryKeyWithBLOBs(psTzModalDtTbl);
						if(rtn != 0){
							mapRet.replace("result", "success");
						}else{
							errMsg[0] = "1";
							errMsg[1] = "保存失败";
							mapRet.replace("result", "fail");
						}
					}else{
						errMsg[0] = "1";
						errMsg[1] = "成绩项不存在";
						mapRet.replace("result", "fail");
					}
				}else if("comboOpt".equals(type)){
					List<Map<String,Object>> listData = (List<Map<String, Object>>) jacksonUtil.getList("data");
					//循环下拉框选项
					for(Map<String,Object> gridMap : listData){
						String orgId = gridMap.get("orgId").toString();
						String treeName = gridMap.get("treeName").toString();
						String itemId = gridMap.get("itemId").toString();
						String optId = gridMap.get("optId").toString();
						String optName = gridMap.get("optName").toString();
						String optScore = gridMap.get("optScore").toString();
						BigDecimal optFz = "".equals(optScore) || optScore == null ? new BigDecimal(0) 
								: BigDecimal.valueOf(Double.valueOf(optScore));
						String isDefault = gridMap.get("isDefault").toString();
						
						
						PsTzZjcjxXzxTKey psTzZjcjxXzxTKey = new PsTzZjcjxXzxTKey();
						psTzZjcjxXzxTKey.setTzJgId(orgId);
						psTzZjcjxXzxTKey.setTreeName(treeName);
						psTzZjcjxXzxTKey.setTzScoreItemId(itemId);
						psTzZjcjxXzxTKey.setTzCjxXlkXxbh(optId);
						
						PsTzZjcjxXzxT psTzZjcjxXzxT = psTzZjcjxXzxTMapper.selectByPrimaryKey(psTzZjcjxXzxTKey);
						if(psTzZjcjxXzxT == null){
							psTzZjcjxXzxT = new PsTzZjcjxXzxT();
							psTzZjcjxXzxT.setTzJgId(orgId);
							psTzZjcjxXzxT.setTreeName(treeName);
							psTzZjcjxXzxT.setTzScoreItemId(itemId);
							psTzZjcjxXzxT.setTzCjxXlkXxbh(optId);
							psTzZjcjxXzxT.setTzCjxXlkXxmc(optName);
							psTzZjcjxXzxT.setTzCjxXlkXxfz(optFz);
							psTzZjcjxXzxT.setTzCjxXlkMrz(isDefault);
							
							psTzZjcjxXzxTMapper.insert(psTzZjcjxXzxT);
						}else{
							psTzZjcjxXzxT.setTzCjxXlkXxmc(optName);
							psTzZjcjxXzxT.setTzCjxXlkXxfz(optFz);
							psTzZjcjxXzxT.setTzCjxXlkMrz(isDefault);
							
							psTzZjcjxXzxTMapper.updateByPrimaryKey(psTzZjcjxXzxT);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	
	/**
	 * 新增成绩项保存
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
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
				String insertType = jacksonUtil.getString("insertType");
				String OperatorItemId = jacksonUtil.getString("OperatorItemId");
				
				if("nodeInfo".equals(type)){
					Map<String,Object> formDate = jacksonUtil.getMap("data");
					
					String orgId = formDate.get("orgId").toString();
					String treeName = formDate.get("treeName").toString();
					String itemId = formDate.get("itemId").toString();
					String itemName = formDate.get("itemName").toString();
					String itemType = formDate.get("itemType").toString();
					BigDecimal UpHzXs = formDate.get("UpHzXs") == null || "".equals(formDate.get("UpHzXs").toString()) ? new BigDecimal(0) 
							: BigDecimal.valueOf(Double.valueOf(formDate.get("UpHzXs").toString()));
					
					BigDecimal weightA = formDate.get("weightA") == null || "".equals(formDate.get("weightA").toString()) ? new BigDecimal(0) 
							: BigDecimal.valueOf(Double.valueOf(formDate.get("weightA").toString()));
					String lowerOperator = formDate.get("lowerOperator").toString();

					BigDecimal lowerLimit = formDate.get("lowerLimit") == null || "".equals(formDate.get("lowerLimit").toString()) ? new BigDecimal(0) 
							: BigDecimal.valueOf(Double.valueOf(formDate.get("lowerLimit").toString()));
					String upperOperator = formDate.get("upperOperator").toString();

					BigDecimal upperLimit = formDate.get("upperLimit") == null || "".equals(formDate.get("upperLimit").toString()) ? new BigDecimal(0) 
							: BigDecimal.valueOf(Double.valueOf(formDate.get("upperLimit").toString()));

					Integer wordLowerLimit = formDate.get("wordLowerLimit") == null || "".equals(formDate.get("wordLowerLimit").toString()) ? 0 
							: Integer.valueOf(formDate.get("wordLowerLimit").toString());

					Integer wordUpperLimit = formDate.get("wordUpperLimit") == null || "".equals(formDate.get("wordUpperLimit").toString()) ? 0 
							: Integer.valueOf(formDate.get("wordUpperLimit").toString());
					String xlTransScore = formDate.get("xlTransScore").toString();

					BigDecimal weightD = formDate.get("weightD") == null || "".equals(formDate.get("weightD").toString()) ? new BigDecimal(0) 
							: BigDecimal.valueOf(Double.valueOf(formDate.get("weightD").toString()));
					
					String autoScreen = formDate.get("autoScreen").toString();
					String scoringRules = formDate.get("scoringRules").toString();
					
					String refDataSet = formDate.get("refDataSet").toString();
					
					String descr = formDate.get("standard").toString();
					String standard = formDate.get("descr").toString();
					String interviewMethod = formDate.get("interviewMethod").toString();
					
					
					PsTzModalDtTblKey psTzModalDtTblKey = new PsTzModalDtTblKey();
					psTzModalDtTblKey.setTzJgId(orgId);
					psTzModalDtTblKey.setTreeName(treeName);
					psTzModalDtTblKey.setTzScoreItemId(itemId);
					
					PsTzModalDtTblWithBLOBs psTzModalDtTbl = psTzModalDtTblMapper.selectByPrimaryKey(psTzModalDtTblKey);
					
					if(psTzModalDtTbl != null){
						errMsg[0] = "1";
						errMsg[1] = "成绩项已存在";
						mapRet.replace("result", "fail");
					}else{
						Boolean bl = false;
						if("A".equals(insertType)){
							//插入同级节点
							bl = tzMenuTreeNodeServiceImpl.createBrotherNode(treeName, OperatorItemId, itemId);
							
						}else if("B".equals(insertType)){
							//插入子节点
							bl = tzMenuTreeNodeServiceImpl.createChildNode(treeName, OperatorItemId, itemId);
						}
						
						if(bl){
							//刷新树结构
							TreeManager treeManager = new TreeManager();
							treeManager.setTreeName(treeName);
							treeManager.setSqlQuery(sqlQuery);
							treeManager.setTZGDObject(tzSQLObject);
							treeManager.setGetSysHardCodeVal(getSysHardCodeVal);
							if (treeManager.openTree("")) {
								treeManager.save();
								treeManager.closeTree();
							}
							
							//保存成绩项信息
							psTzModalDtTbl = new PsTzModalDtTblWithBLOBs();
							psTzModalDtTbl.setTzJgId(orgId);
							psTzModalDtTbl.setTreeName(treeName);
							psTzModalDtTbl.setTzScoreItemId(itemId);
							
							psTzModalDtTbl.setDescr(itemName);
							psTzModalDtTbl.setTzScoreItemType(itemType);
							psTzModalDtTbl.setTzScoreHz(UpHzXs);
							psTzModalDtTbl.setTzScoreQz(weightA);
							
							psTzModalDtTbl.setTzMFbdzMxXxJx(lowerOperator);
							psTzModalDtTbl.setTzScoreLimited2(lowerLimit);
							psTzModalDtTbl.setTzMFbdzMxSxJx(upperOperator);
							psTzModalDtTbl.setTzScoreLimited(upperLimit);
							
							psTzModalDtTbl.setTzScorePyZslim0(wordLowerLimit);
							psTzModalDtTbl.setTzScorePyZslim(wordUpperLimit);
							
							
							psTzModalDtTbl.setTzScrToScore(xlTransScore);
							psTzModalDtTbl.setTzScrSqz(weightD);
							psTzModalDtTbl.setTzIfZdcs(autoScreen);
							psTzModalDtTbl.setTzZdcsgzId(scoringRules);
							psTzModalDtTbl.setTzScoreCkzl(refDataSet);
							
							psTzModalDtTbl.setTzScoreItemDfsm(descr);
							psTzModalDtTbl.setTzScoreItemCkwt(standard);
							psTzModalDtTbl.setTzScoreItemMsff(interviewMethod);
							
							psTzModalDtTbl.setRowAddedDttm(new Date());
							psTzModalDtTbl.setRowAddedOprid(oprid);
							psTzModalDtTbl.setRowLastmantDttm(new Date());
							psTzModalDtTbl.setRowLastmantOprid(oprid);
							
							int rtn = psTzModalDtTblMapper.insert(psTzModalDtTbl);
							if(rtn != 0){
								currItemId = itemId;
								mapRet.replace("result", "success");
							}else{
								errMsg[0] = "1";
								errMsg[1] = "保存失败";
								mapRet.replace("result", "fail");
							}
						}
					}
				}else if("comboOpt".equals(type)){
					if(!"".equals(currItemId) && currItemId != null){
						List<Map<String,Object>> listData = (List<Map<String, Object>>) jacksonUtil.getList("data");
						//循环下拉框选项
						for(Map<String,Object> gridMap : listData){
							String orgId = gridMap.get("orgId").toString();
							String treeName = gridMap.get("treeName").toString();
							
							String optId = gridMap.get("optId").toString();
							String optName = gridMap.get("optName").toString();
							String optScore = gridMap.get("optScore").toString();
							BigDecimal optFz = "".equals(optScore) || optScore == null ? new BigDecimal(0) 
									: BigDecimal.valueOf(Double.valueOf(optScore));
							String isDefault = gridMap.get("isDefault").toString();
							
							
							PsTzZjcjxXzxTKey psTzZjcjxXzxTKey = new PsTzZjcjxXzxTKey();
							psTzZjcjxXzxTKey.setTzJgId(orgId);
							psTzZjcjxXzxTKey.setTreeName(treeName);
							psTzZjcjxXzxTKey.setTzScoreItemId(currItemId);
							psTzZjcjxXzxTKey.setTzCjxXlkXxbh(optId);
							
							PsTzZjcjxXzxT psTzZjcjxXzxT = psTzZjcjxXzxTMapper.selectByPrimaryKey(psTzZjcjxXzxTKey);
							if(psTzZjcjxXzxT == null){
								psTzZjcjxXzxT = new PsTzZjcjxXzxT();
								psTzZjcjxXzxT.setTzJgId(orgId);
								psTzZjcjxXzxT.setTreeName(treeName);
								psTzZjcjxXzxT.setTzScoreItemId(currItemId);
								psTzZjcjxXzxT.setTzCjxXlkXxbh(optId);
								psTzZjcjxXzxT.setTzCjxXlkXxmc(optName);
								psTzZjcjxXzxT.setTzCjxXlkXxfz(optFz);
								psTzZjcjxXzxT.setTzCjxXlkMrz(isDefault);
								
								psTzZjcjxXzxTMapper.insert(psTzZjcjxXzxT);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	
	/**
	 * 删除成绩项下拉框可选项
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String type = jacksonUtil.getString("type");
				
				if("comboOpt".equals(type)){
					List<Map<String,Object>> listData = (List<Map<String, Object>>) jacksonUtil.getList("data");
					//循环下拉框选项
					for(Map<String,Object> gridMap : listData){
						String orgId = gridMap.get("orgId").toString();
						String treeName = gridMap.get("treeName").toString();
						String itemId = gridMap.get("itemId").toString();
						String optId = gridMap.get("optId").toString();
						
						PsTzZjcjxXzxTKey psTzZjcjxXzxTKey = new PsTzZjcjxXzxTKey();
						psTzZjcjxXzxTKey.setTzJgId(orgId);
						psTzZjcjxXzxTKey.setTreeName(treeName);
						psTzZjcjxXzxTKey.setTzScoreItemId(itemId);
						psTzZjcjxXzxTKey.setTzCjxXlkXxbh(optId);
						
						PsTzZjcjxXzxT psTzZjcjxXzxT = psTzZjcjxXzxTMapper.selectByPrimaryKey(psTzZjcjxXzxTKey);
						if(psTzZjcjxXzxT != null){
							
							psTzZjcjxXzxTMapper.deleteByPrimaryKey(psTzZjcjxXzxTKey);
						}
					}
					mapRet.replace("result", "success");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
}
