package com.tranzvision.gd.TZScoreModeManagementBundle.service.impl;

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

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzSrmbaInsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzSrmbaInsTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;


/**
 * 成绩实例计算函数封装,去掉全局变量,从原文件复制修改
 * @author luyan
 * 2018/06/13	
 */
@Service
public class TzScoreInsCalculationNewObject {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TZGDObject tzSQLObject;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal; 
	
	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;
	
	@Autowired
	private PsTzSrmbaInsTblMapper psTzSrmbaInsTblMapper;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;

	
	
	/**
	 * 评委打分保存并计算成绩分值
	 * 参数说明：
	 * @param type  评审类型， MS - 面试评审，CL - 材料评审
	 * @param classId  班级编号
	 * @param batchId  批次编号
	 * @param bmbId    报名表编号
	 * @param itemsScoreParams  成绩项分值参数,itemsScoreParams为一个json字符串，至少包含所有成绩项的成绩项ID及对应分值，
	 * 
	 * 例如：
	 * {
	 * 		JYBJ:99，   //教育背景 —— 成绩项编号:打分分值
	 *		YYSP:1.5，
	 *		ZYSP:8，
	 *		CJDJ:4，
	 *		PY:“评语”
	 * }
	 * 
	 * @return 返回长度为2的数组，第一个元素为错误码errorCode，第二个元素为错误描述errorMsg
	 * 	errorCode: 0 - 表示保存成功，其他返回值保存失败
	 * 	errorMsg: 错误描述
	 */
	public String[] PwdfSaveUpdate(String type, String classId, String batchId, String bmbId, String itemsScoreParams){
		//返回值
		String[] strRtn = new String [2];
		String errorCode = "";
		String errorMsg = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//当前登录人
			String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			System.out.println("调用保存成绩项的方法Begin");
			System.out.println("当前机构："+orgId+"-->当前登录人："+oprId);
			
			if(classId != null && !"".equals(classId)
					&& batchId != null && !"".equals(batchId)){
				jacksonUtil.json2Map(itemsScoreParams);
				
				//成绩模型树
				String treeName = "";
				//打分成绩项分数实例
				Map<String, String[]> itemsScoreValMap = new HashMap<String, String[]>();
				//成绩单ID
				Long tzScoreInsId = (long) 0;
				
				//成绩模型ID
				String scoreModeId = "";								
				//查询成绩模型
				String sql = "SELECT TZ_ZLPS_SCOR_MD_ID,TZ_MSCJ_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
				Map<String,Object> scoreMdMap = sqlQuery.queryForMap(sql, new Object[]{ classId });
				
				String pwkshSql = "";//查询成绩单ID
				if("CL".equals(type)){
					System.out.println("材料评审评委进行打分");
					scoreModeId = scoreMdMap.get("TZ_ZLPS_SCOR_MD_ID").toString();
					pwkshSql = "SELECT TZ_SCORE_INS_ID FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?";
				}else if("MS".equals(type)){
					System.out.println("面试评审评委进行打分");
					scoreModeId = scoreMdMap.get("TZ_MSCJ_SCOR_MD_ID").toString();
					pwkshSql = "SELECT TZ_SCORE_INS_ID FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?";
				}
				
				if(scoreModeId != null && !"".equals(scoreModeId)){
					
					//查询成绩模型树
					sql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? and TZ_JG_ID=?";
					treeName = sqlQuery.queryForObject(sql, new Object[]{ scoreModeId, orgId }, "String");
					
					//查询所有需要保存的成绩项
					sql = "SELECT TZ_SCORE_ITEM_ID,TZ_SCORE_ITEM_TYPE,DESCR FROM PS_TZ_MODAL_DT_TBL WHERE TZ_JG_ID=? AND TREE_NAME=?";
					List<Map<String,Object>> itemsList = sqlQuery.queryForList(sql, new Object[]{ orgId, treeName });
					
					/**
					 * 存放成绩项分值，{成绩项ID:[类型，分值]}
					 */
					Map<String, String[]> itemValMap = new HashMap<String, String[]>();
					
					for(Map<String,Object> itemsMap : itemsList){
						String itemId = itemsMap.get("TZ_SCORE_ITEM_ID").toString();
						String itemtype = itemsMap.get("TZ_SCORE_ITEM_TYPE").toString();
						
						//提交的参数是否包含当前成绩项ID
						if(jacksonUtil.containsKey(itemId)){
							String itemValue = jacksonUtil.getString(itemId);
							String[] itemValArr = {itemtype,itemValue};
							//将成绩项分值放入Map中
							itemValMap.put(itemId, itemValArr);
						}
					}
					itemsScoreValMap = itemValMap;
					//更新材料评审评委考生关系表标志
					String updateFlag = "";
					//查询成绩单编号
					String scoreInsId = sqlQuery.queryForObject(pwkshSql, new Object[]{classId,batchId,bmbId,oprId}, "String");
					if(scoreInsId != null && !"".equals(scoreInsId)){
						if(!"0".equals(scoreInsId)) {
							tzScoreInsId = Long.valueOf(scoreInsId);
						} else {
							tzScoreInsId = Long.valueOf(0);
							updateFlag = "Y";
						}
					} else {
						tzScoreInsId = Long.valueOf(0);
						updateFlag = "Y";
					}
					
					System.out.println("成绩单实例ID："+tzScoreInsId+"-->更新标志："+updateFlag);
					
					//保存打分
					String rtn = SaveScore(orgId,oprId,treeName,scoreModeId,tzScoreInsId,itemsScoreValMap);
					jacksonUtil.json2Map(rtn);
					errorCode = jacksonUtil.getString("errorCode");
					tzScoreInsId = jacksonUtil.getLong("scoreInsId");
					
					if("0".equals(errorCode)){
						//保存成功
						System.out.println("保存打分成功后，更新标志："+updateFlag);
						
						/*评委类型：英语A或其他O*/
						String pwType = sqlQuery.queryForObject("SELECT TZ_PWEI_TYPE FROM PS_TZ_MSPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?", new Object[] {classId,batchId,oprId},"String");
											
						if(!"A".equals(pwType)) {
							//非英语评委，删除成绩单中英语成绩
							
							//成绩模型中英语成绩项ID
							String englishCjxId = getHardCodePoint.getHardCodePointVal("TZ_MSPS_YYNL_CJX_ID");
							
							sqlQuery.update("DELETE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?",
									new Object[]{tzScoreInsId,englishCjxId});
							sqlQuery.execute("commit");
						}
						
						if("Y".equals(updateFlag)) {
							//更新材料评审评委考生关系表，卢艳添加，2017-4-11
							if("CL".equals(type)){
								sqlQuery.update("UPDATE PS_TZ_CP_PW_KS_TBL SET TZ_SCORE_INS_ID=? WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?",
										new Object[]{tzScoreInsId,classId,batchId,bmbId,oprId});
								sqlQuery.execute("commit");
								System.out.println("更新材料评审评委考生关系表。成绩单实例ID："+tzScoreInsId+"-->班级编号："+classId+"-->批次编号："+batchId+"-->报名表实例编号："+bmbId+"-->评委OPRID："+oprId);
							} else {
								if("MS".equals(type)){
									//更新面试评审评委考生关系表，卢艳添加，2017-4-14
									sqlQuery.update("UPDATE PS_TZ_MP_PW_KS_TBL SET TZ_SCORE_INS_ID=? WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?",
											new Object[]{tzScoreInsId,classId,batchId,bmbId,oprId});
									sqlQuery.execute("commit");
									System.out.println("更新面试评审评委考生关系表。成绩单实例ID："+tzScoreInsId+"-->班级编号："+classId+"-->批次编号："+batchId+"-->报名表实例编号："+bmbId+"-->评委OPRID："+oprId);
								}
							}
						}

					}else{
						errorMsg = "保存失败";
					}
				}else{
					errorCode = "-1";
					errorMsg = "没有配置成绩模型";
				}
			}else{
				errorCode = "-1";
				errorMsg = "参数错误";
			}
			
			System.out.println("调用保存成绩项的方法返回数据errorCode："+errorCode+"-->errorMsg："+errorMsg);
			System.out.println("调用保存成绩项的方法End");
			
		}catch(Exception e){
			errorCode = "-1";
			errorMsg = e.getMessage();
			e.printStackTrace();
		}
		
		strRtn[0] = errorCode;
		strRtn[1] = errorMsg;
		return strRtn;
	}
	
	
	
	/**
	 * 保存打分
	 * @param scoreInsId	//成绩单ID
	 * @return
	 */
	@SuppressWarnings("unused")
	private String SaveScore(String orgId,String oprId,String treeName,String scoreModelId,Long scoreInsId,Map<String,String[]> itemsScoreValMap){
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		
		String errorCode = "";
		String scoreInsIdRet = "0";
		//根据TREE NAME，获取该TREE的根节点的 TREE_NODE，及TREE_NODE_NUM;
		int treeNodeNum;
		String treeNode;
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try{
			System.out.println("保存打分Begin");
			
			String sql = "SELECT TREE_NODE_NUM,TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? AND PARENT_NODE_NUM=0";
			Map<String,Object> rootMap = sqlQuery.queryForMap(sql, new Object[]{ treeName });
			
			if(rootMap != null){
				treeNodeNum = Integer.valueOf(rootMap.get("TREE_NODE_NUM").toString());
				treeNode = rootMap.get("TREE_NODE").toString();
				//初始化-待保存成绩项分值
				List<Map<String,Object>> itemsScoreValListTmp = new ArrayList<Map<String,Object>>();
				//错误信息
				String errorMsg = "";
				//遍历成绩模型树
				String rootScoreAmount = this.TraverseTree(orgId,treeName,treeNodeNum, treeNode,itemsScoreValMap,itemsScoreValListTmp);
				jacksonUtil.json2Map(rootScoreAmount);
				errorMsg = jacksonUtil.getString("errorMsg");
				
				
				if(errorMsg != null && !"".equals(errorMsg)){
					//有错误信息，保存失败
					errorCode = "-1";
					System.out.println("保存打分错误信息："+errorMsg);
				} else {
					/**
					 * 保存打分数据到成绩项实例表PS_TZ_CJX_TBL，数据存放在itemsScoreValListTmp
					 */
					String doSaveRet = doSaveDate(oprId,scoreModelId,scoreInsId,itemsScoreValListTmp);
					jacksonUtil.json2Map(doSaveRet);
					
					String errorCodeRet = jacksonUtil.getString("errorCode");
					scoreInsIdRet = jacksonUtil.getString("scoreInsId");
					
					if("0".equals(errorCodeRet)){
						//保存成功
						errorCode = "0";
					}else{
						//保存失败
						errorCode = "-2";
					}
				}
			}
		}catch(Exception e){
			errorCode = "-1";
			e.printStackTrace();
		}
		
		mapRet.put("errorCode", errorCode);
		mapRet.put("scoreInsId", scoreInsIdRet);
		
		strRet = jacksonUtil.Map2json(mapRet);
		
		System.out.println("保存打分返回值："+strRet);
		System.out.println("保存打分End");
		
		return strRet;
	}
	
	
	/**
	 * 遍历成绩模型树
	 * @param treeNodeNum
	 * @param treeNode
	 */
	private String TraverseTree(String orgId,String treeName,int treeNodeNum, String treeNode,Map<String,String[]> itemsScoreValMap,List<Map<String,Object>> itemsScoreValListTmp){
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String errorCode = "0";
		String errorMsg = "";
		//当前节点的总分值（若是叶子节点，则为 分值*权重的和；若非叶子，则是其下级叶子的分值和）;
		Float nodeScoreAmount = new Float(0);
		

		try{
			//获取子节点数据
			String sql = tzSQLObject.getSQLText("SQL.TZScoreModelBundle.TzChildNodeList");
			List<Map<String,Object>> childNodeList = sqlQuery.queryForList(sql, new Object[]{ treeNodeNum, treeName, orgId });

			for(Map<String,Object> childNodeMap: childNodeList){
				
				int childNodeNum =  Integer.valueOf(childNodeMap.get("TREE_NODE_NUM").toString());
				String childNode = childNodeMap.get("TREE_NODE").toString();
				//是否存在子节点
				String hasChild = childNodeMap.get("TZ_HAS_CHILD") == null? "N" 
						: childNodeMap.get("TZ_HAS_CHILD").toString();
				
				if("Y".equals(hasChild)){
					//存在子节点，递归
					String childRet = this.TraverseTree(orgId,treeName,childNodeNum, childNode,itemsScoreValMap,itemsScoreValListTmp);
					jacksonUtil.json2Map(childRet);
					Float nodeScoreAmountRet = Float.valueOf(jacksonUtil.getString("nodeScoreAmount"));							
					nodeScoreAmount = nodeScoreAmount + nodeScoreAmountRet;
				}else{
					
					if(itemsScoreValMap.containsKey(childNode)){
						Boolean boolVaild = true;
						String itemDesc = childNodeMap.get("DESCR").toString();
						
						String[] itemValArr =  itemsScoreValMap.get(childNode);
						String itemType = itemValArr[0];
						String itemVal = itemValArr[1];
						
						Map<String,Object> itemScoreMapTmp = new HashMap<String,Object>();
						itemScoreMapTmp.put("itemId", childNode);
						
						switch(itemType){
						case "B":	//数字成绩录入项
							//下限操作符
							String lowerOper = childNodeMap.get("TZ_M_FBDZ_MX_XX_JX").toString();
							//上限操作符
							String upperOper = childNodeMap.get("TZ_M_FBDZ_MX_SX_JX").toString();
							//分值下限
							Float lowerLimit = Float.valueOf(childNodeMap.get("TZ_SCORE_LIMITED2").toString());
							//分值上限
							Float upperLimit = Float.valueOf(childNodeMap.get("TZ_SCORE_LIMITED").toString());
							//权重
							Float weight= Float.valueOf(childNodeMap.get("TZ_SCORE_QZ").toString());
							Float val = Float.valueOf(itemVal);
							
							//校验分值
							boolVaild = this.validScoreValue(val, lowerOper, lowerLimit, upperOper, upperLimit);
							if(boolVaild){
								//计算该成绩项的权重值
								nodeScoreAmount = nodeScoreAmount + (val*weight)/100;
								
								itemScoreMapTmp.put("ScoreVal", val);
							}else{
								errorCode = "1";
								errorMsg = errorMsg + "【"+itemDesc+"】分数填写错误，请重新填写！";
							}
							break;
						case "C":	//评语
							//字数下限
							int wordLower = Integer.valueOf(childNodeMap.get("TZ_SCORE_PY_ZSLIM0").toString());
							//字数上限
							int wordUpper = Integer.valueOf(childNodeMap.get("TZ_SCORE_PY_ZSLIM").toString());
							
							//校验评语字数
							boolVaild = this.validPyValue(itemVal, wordLower, wordUpper);
							if(boolVaild){
								itemScoreMapTmp.put("pyVal", itemVal);
							}else{
								errorCode = "1";
								errorMsg = errorMsg + "【"+itemDesc+"】评语字数范围不正确，请重新填写！";
							}
							break;
						case "D":	//下拉框
							/**
							 * 先判断下拉框是否转换为分值，如果转换为分值则计算否则不计算
							 */
							Float optVal = null;
							String transFz = childNodeMap.get("TZ_SCR_TO_SCORE").toString();
							//下拉框权重
							Float comboWeight= Float.valueOf(childNodeMap.get("TZ_SCR_SQZ").toString());
							if("Y".equals(transFz)){
								String optSql = "SELECT TZ_CJX_XLK_XXFZ FROM PS_TZ_ZJCJXXZX_T WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=? AND TZ_CJX_XLK_XXBH=?";
								optVal = sqlQuery.queryForObject(optSql, new Object[]{ orgId, treeName, childNode, itemVal }, "Float");
								if(optVal != null){
									//计算该成绩项的权重值
									nodeScoreAmount = nodeScoreAmount + (optVal*comboWeight)/100;
								}
							}
							
							itemScoreMapTmp.put("optId", itemVal);
							itemScoreMapTmp.put("ScoreVal", optVal);
							break;
						}

						if(boolVaild){
							itemsScoreValListTmp.add(itemScoreMapTmp);
						}
					}
				}
			}
			
			//当前节点向上汇总系数
			String hzSql = "SELECT TZ_SCORE_HZ FROM PS_TZ_MODAL_DT_TBL WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
			Float HzNum = sqlQuery.queryForObject(hzSql, new Object[]{ orgId, treeName, treeNode }, "Float");
			
			nodeScoreAmount = nodeScoreAmount*HzNum/100;
			
			Map<String,Object> itemScoreMapTmp2 = new HashMap<String,Object>();
			itemScoreMapTmp2.put("itemId", treeNode);
			itemScoreMapTmp2.put("ScoreVal", nodeScoreAmount);
			itemsScoreValListTmp.add(itemScoreMapTmp2);
						
			mapRet.put("errorCode", errorCode);
			mapRet.put("errorMsg", errorMsg);
			mapRet.put("nodeScoreAmount", nodeScoreAmount);
			mapRet.put("itemsScoreValListTmp", itemsScoreValListTmp);
			
			strRet = jacksonUtil.Map2json(mapRet);

		}catch(Exception e){
			e.printStackTrace();
		}
		//return nodeScoreAmount;
		return strRet;
	}

	
	/**
	 * 保存打分数据到数据库表，数据暂存在itemsScoreValListTmp中
	 * @return
	 */
	private String doSaveDate(String oprId,String tzScoreModeId,Long tzScoreInsId,List<Map<String,Object>> itemsScoreValListTmp){
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String errorCode = "0"; 
		
		String dtFormat = getSysHardCodeVal.getDateFormat();
		SimpleDateFormat format = new SimpleDateFormat(dtFormat);
		try{
			System.out.println("保存打分数据到数据库表Begin");
			System.out.println("成绩单实例ID："+tzScoreInsId);
			
			if(tzScoreInsId == 0){
				tzScoreInsId = this.creatNewScoreInstanceId();
			
				System.out.println("新增-成绩单实例ID："+tzScoreInsId);
				
				//psTzSrmbaInsTblMapper
				
				PsTzSrmbaInsTbl psTzSrmbaInsTbl = new PsTzSrmbaInsTbl();
				psTzSrmbaInsTbl.setTzScoreInsId(tzScoreInsId);
				psTzSrmbaInsTbl.setTzScoreModalId(tzScoreModeId);
				
				Date scoreInsDate = format.parse("2099-12-31");
				psTzSrmbaInsTbl.setTzScoreInsDate(scoreInsDate);
				psTzSrmbaInsTbl.setRowAddedDttm(new Date());
				psTzSrmbaInsTbl.setRowAddedOprid(oprId);
				psTzSrmbaInsTbl.setRowLastmantDttm(new Date());
				psTzSrmbaInsTbl.setRowLastmantOprid(oprId);
				
				psTzSrmbaInsTblMapper.insert(psTzSrmbaInsTbl);
				
				
			}
			
			if(tzScoreInsId == 0){
				
			} else  {
				System.out.println(itemsScoreValListTmp.toString());
				
				for(Map<String,Object> itemScoreMap: itemsScoreValListTmp){
					String itemId = itemScoreMap.get("itemId").toString();
					
					sqlQuery.update("DELETE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID= ?", new Object[]{ tzScoreInsId,itemId });
					sqlQuery.execute("commit");
					
					PsTzCjxTblKey PsTzCjxTblKey = new PsTzCjxTblKey();
					PsTzCjxTblKey.setTzScoreInsId(tzScoreInsId);
					PsTzCjxTblKey.setTzScoreItemId(itemId);
					PsTzCjxTblWithBLOBs psTzCjxTbl = psTzCjxTblMapper.selectByPrimaryKey(PsTzCjxTblKey);
					if(psTzCjxTbl == null){
						psTzCjxTbl = new PsTzCjxTblWithBLOBs();
						psTzCjxTbl.setTzScoreInsId(tzScoreInsId);
						psTzCjxTbl.setTzScoreItemId(itemId);
					}
				
					//分值
					if(itemScoreMap.containsKey("ScoreVal")){
						BigDecimal ScoreVal = itemScoreMap.get("ScoreVal") == null? new BigDecimal(0) 
								: BigDecimal.valueOf(Double.valueOf(itemScoreMap.get("ScoreVal").toString()));
						psTzCjxTbl.setTzScoreNum(ScoreVal);
					}
					//评语值
					if(itemScoreMap.containsKey("pyVal")){
						String pyVal = itemScoreMap.get("pyVal").toString();
						psTzCjxTbl.setTzScorePyValue(pyVal);
					}
					//选项编号
					if(itemScoreMap.containsKey("optId")){
						String optId = itemScoreMap.get("optId").toString();
						psTzCjxTbl.setTzCjxXlkXxbh(optId);
					}
				
					psTzCjxTblMapper.insert(psTzCjxTbl);
				}
			}
						
		}catch(Exception e){
			errorCode = "1";
			e.printStackTrace();
		}
		
		mapRet.put("errorCode", errorCode);
		mapRet.put("scoreInsId", tzScoreInsId);
		
		strRet = jacksonUtil.Map2json(mapRet);
		
		System.out.println("保存打分数据到数据库表返回值："+strRet);
		
		System.out.println("保存打分数据到数据库表End");
		
		return strRet;
	}
	
	
	
	/**
	 * 校验数字输入项分值是否合法
	 * @param scoreVal	分值
	 * @param lowerOper	下限操作符
	 * @param lowerLimit 分值下限
	 * @param upperOper	上限操作符
	 * @param upperLimit 分值上限
	 * @return
	 */
	public Boolean validScoreValue(Float scoreVal,String lowerOper,Float lowerLimit,String upperOper,Float upperLimit){
		Boolean boolValid = false;
		switch(lowerOper){
		case ">":
			if("<".equals(upperOper)){
				if(scoreVal>lowerLimit && scoreVal<upperLimit){
					boolValid = true;
				}
			}else if("<=".equals(upperOper)){
				if(scoreVal>lowerLimit && scoreVal<=upperLimit){
					boolValid = true;
				}
			}
		case ">=":
			if("<".equals(upperOper)){
				if(scoreVal>=lowerLimit && scoreVal<upperLimit){
					boolValid = true;
				}
			}else if("<=".equals(upperOper)){
				if(scoreVal>=lowerLimit && scoreVal<=upperLimit){
					boolValid = true;
				}
			}
		}
		
		return boolValid;
	}
	
	
	/**
	 * 校验评语字数限制
	 * @param pyVal	评语
	 * @param lowerLimit	字数下限
	 * @param upperLimit	字数上限
	 * @return
	 */
	public Boolean validPyValue(String pyVal,int lowerLimit,int upperLimit){
		Boolean boolValid = false;
		
		int len = pyVal.length();
		if(len>=lowerLimit && len<=upperLimit){
			boolValid = true;
		}
	
		return boolValid;
	}
	
	
	/***
	 * 生成成绩单编号
	 * @return
	 */
	public Long creatNewScoreInstanceId(){
		long scoreInsId = getSeqNum.getSeqNum("TZ_SRMBAINS_TBL", "TZ_SCORE_INS_ID");
		
		return scoreInsId;
	}
}
