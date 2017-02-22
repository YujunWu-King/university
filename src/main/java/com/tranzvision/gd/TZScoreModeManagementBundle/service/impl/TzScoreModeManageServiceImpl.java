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
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMenuMgBundle.dao.PsTreeNodeMapper;
import com.tranzvision.gd.TZMenuMgBundle.model.PsTreeNode;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzCjBphTblMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzModalDtTblMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzRsModalTblMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzTreeDefnMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzZjcjxXzxTMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzCjBphTbl;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzCjBphTblKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTblKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTblWithBLOBs;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzRsModalTbl;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzRsModalTblKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzTreeDefn;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzTreeDefnKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzZjcjxXzxT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZScoreModeManagementBundle.service.impl.TzScoreModeManageServiceImpl")
public class TzScoreModeManageServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal; 
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzRsModalTblMapper psTzRsModalTblMapper;
	
	@Autowired
	private PsTzModalDtTblMapper psTzModalDtTblMapper;
	
	@Autowired
	private PsTzTreeDefnMapper psTzTreeDefnMapper;
	
	@Autowired
	private PsTreeNodeMapper psTreeNodeMapper;
	
	@Autowired
	private PsTzZjcjxXzxTMapper psTzZjcjxXzxTMapper;
	
	@Autowired
	private PsTzCjBphTblMapper psTzCjBphTblMapper;
	

	/**
	 * 成绩模型管理列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			//TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.TZ_RS_MODAL_TBL
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_JG_ID", "TZ_SCORE_MODAL_ID", "TZ_MODAL_NAME", "TREE_NAME", "TZ_MODAL_FLAG"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				int num = list.size();
				for (int i = 0; i < num; i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("orgId", rowList[0]);
					mapList.put("modeId", rowList[1]);
					mapList.put("modeDesc", rowList[2]);
					mapList.put("treeName", rowList[3]);
					
					//状态取值
					String status = rowList[4];
					String sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_ISVALID' AND TZ_ZHZ_ID=?";
					status = jdbcTemplate.queryForObject(sql, new Object[]{status}, "String");
					
					mapList.put("status", status);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);
	}
	
	
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRtn = "";
		Map<String, Object> mapForm = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String orgId = jacksonUtil.getString("orgId");
			String modeId = jacksonUtil.getString("modeId");

			if (null != orgId && !"".equals(orgId) && null != modeId && !"".equals(modeId)) {
				PsTzRsModalTblKey psTzRsModalTblKey = new PsTzRsModalTblKey();
				psTzRsModalTblKey.setTzJgId(orgId);
				psTzRsModalTblKey.setTzScoreModalId(modeId);
				PsTzRsModalTbl psTzRsModalTbl = psTzRsModalTblMapper.selectByPrimaryKey(psTzRsModalTblKey);
				if(psTzRsModalTbl != null){
					String modeDesc = psTzRsModalTbl.getTzModalName();
					String treeName = psTzRsModalTbl.getTreeName()==null ? "" : psTzRsModalTbl.getTreeName();
					String status = psTzRsModalTbl.getTzModalFlag();
					String totalScoreModel = psTzRsModalTbl.getTzMFbdzId()==null ? "" : psTzRsModalTbl.getTzMFbdzId();
					
					String sql = "SELECT TZ_JG_NAME FROM PS_TZ_JG_BASE_T WHERE TZ_JG_ID=?";
					String orgName = jdbcTemplate.queryForObject(sql, new Object[]{orgId}, "String");
					
					String addOprid = psTzRsModalTbl.getRowAddedOprid();
					String updateOprid = psTzRsModalTbl.getRowLastmantOprid();
					
					sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
					String addOprName = jdbcTemplate.queryForObject(sql, new Object[]{addOprid}, "String");
					String updateOprName = jdbcTemplate.queryForObject(sql, new Object[]{updateOprid}, "String");
					
					Date addDate = psTzRsModalTbl.getRowAddedDttm();
					Date updateDate = psTzRsModalTbl.getRowLastmantDttm();
					
					String dttmFormat = getSysHardCodeVal.getDateTimeHMFormat();
					SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
					String addoprDttm =  addDate == null ? "" : dttmSimpleDateFormat.format(addDate);
					String updateOprDttm = updateDate == null ? "" : dttmSimpleDateFormat.format(updateDate);
					
					mapForm.put("orgId", orgId);
					mapForm.put("modelId", modeId);
					mapForm.put("modeName", modeDesc);
					mapForm.put("status", status);
					mapForm.put("treeName", treeName);
					mapForm.put("totalScoreModel", totalScoreModel);
					mapForm.put("orgDesc", orgName);
					mapForm.put("addOprName", addOprName);
					mapForm.put("addoprDttm", addoprDttm);
					mapForm.put("updateOprName", updateOprName);
					mapForm.put("updateOprDttm", updateOprDttm);
				}
			}else{
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRtn = jacksonUtil.Map2json(mapForm);
		return strRtn;
	}
	
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
				case "tzCopyScoreModel":
					//复制成绩模型
					strRet = this.tzCopyScoreModel(strParams,errorMsg);
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
	 * 复制成绩模型
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzCopyScoreModel(String strParams, String[] errorMsg){
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			jacksonUtil.json2Map(strParams);
			
			String orgId = jacksonUtil.getString("orgId");
			String modeId = jacksonUtil.getString("modeId");
			String copyModeId = jacksonUtil.getString("copyModeId");
			String copyTreeName = jacksonUtil.getString("copyTreeName");
			
			String sql = "select 'Y' from PS_TZ_RS_MODAL_TBL where TZ_JG_ID=? and TZ_SCORE_MODAL_ID=?";
			String modelExists = jdbcTemplate.queryForObject(sql, new Object[]{ orgId, copyModeId }, "String");
			
			if("Y".equals(modelExists)){
				errorMsg[0] = "1";
				errorMsg[1] = "您输入的成绩模型ID已存在";
			}else{
				sql = "select 'Y' from PSTREENODE where TREE_NAME=? limit 1";
				String treeExists = jdbcTemplate.queryForObject(sql, new Object[]{ copyTreeName }, "String");
				
				if("Y".equals(treeExists)){
					errorMsg[0] = "1";
					errorMsg[1] = "您输入的树名称已存在";
				}else{
					PsTzRsModalTblKey psTzRsModalTblKey = new PsTzRsModalTblKey();
					psTzRsModalTblKey.setTzJgId(orgId);
					psTzRsModalTblKey.setTzScoreModalId(modeId);
					
					PsTzRsModalTbl psTzRsModalTbl = psTzRsModalTblMapper.selectByPrimaryKey(psTzRsModalTblKey);
					if(psTzRsModalTbl != null){
						//成绩模型树
						String treeName = psTzRsModalTbl.getTreeName();
						
						psTzRsModalTbl.setTzScoreModalId(copyModeId);
						psTzRsModalTbl.setTreeName(copyTreeName);
						psTzRsModalTbl.setRowAddedDttm(new Date());
						psTzRsModalTbl.setRowAddedOprid(oprid);
						psTzRsModalTbl.setRowLastmantDttm(new Date());
						psTzRsModalTbl.setRowLastmantOprid(oprid);
						
						int rtn = psTzRsModalTblMapper.insert(psTzRsModalTbl);
						if(rtn != 0){
							//复制成绩模型树
							Boolean boolCopyTree = this.copyTree(treeName, copyTreeName, orgId);
							if(boolCopyTree){
								//成绩扁平化定义
								sql = "select TZ_SCORE_ITEM_ID,TZ_ITEM_S_TYPE from PS_TZ_CJ_BPH_TBL where TZ_JG_ID=? and TZ_SCORE_MODAL_ID=?";
								List<Map<String,Object>> bphList = jdbcTemplate.queryForList(sql, new Object[]{ orgId,modeId });
								
								for(Map<String,Object> bphMap: bphList){
									String itemId = bphMap.get("TZ_SCORE_ITEM_ID").toString();
									String itemType = bphMap.get("TZ_ITEM_S_TYPE").toString();
									
									PsTzCjBphTblKey psTzCjBphTblKey = new PsTzCjBphTblKey();
									psTzCjBphTblKey.setTzJgId(orgId);
									psTzCjBphTblKey.setTzScoreModalId(modeId);
									psTzCjBphTblKey.setTzScoreItemId(itemId);
									psTzCjBphTblKey.setTzItemSType(itemType);
									PsTzCjBphTbl psTzCjBphTbl = psTzCjBphTblMapper.selectByPrimaryKey(psTzCjBphTblKey);
									
									if(psTzCjBphTbl != null){
										psTzCjBphTbl.setTzScoreModalId(copyModeId);
										psTzCjBphTbl.setRowAddedDttm(new Date());
										psTzCjBphTbl.setRowAddedOprid(oprid);
										psTzCjBphTbl.setRowLastmantDttm(new Date());
										psTzCjBphTbl.setRowLastmantOprid(oprid);
										
										psTzCjBphTblMapper.insert(psTzCjBphTbl);
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		return strRet;
	}
	
	
	/**
	 * 复制树结构
	 * @param treeName  被复制树名称
	 * @param copyTreeName  复制的树名称
	 * @param orgId 机构
	 * @return
	 */
	private Boolean copyTree(String treeName,String copyTreeName,String orgId){
		Boolean boolRtn = true;
		try{
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			String sql = "select 'Y' from PS_TZ_TREEDEFN where TREE_NAME=? limit 1";
			String treeExists = jdbcTemplate.queryForObject(sql, new Object[]{ copyTreeName }, "String");
			
			if("Y".equals(treeExists)){
				boolRtn = false;
			}else{
				PsTzTreeDefnKey psTzTreeDefnKey = new PsTzTreeDefnKey();
				psTzTreeDefnKey.setTzJgId(orgId);
				psTzTreeDefnKey.setTreeName(treeName);
				
				PsTzTreeDefn psTzTreeDefn = psTzTreeDefnMapper.selectByPrimaryKey(psTzTreeDefnKey);
				if(psTzTreeDefn != null){
					psTzTreeDefn.setTreeName(copyTreeName);
					psTzTreeDefn.setRowAddedDttm(new Date());
					psTzTreeDefn.setRowAddedOprid(oprid);
					psTzTreeDefn.setRowLastmantDttm(new Date());
					psTzTreeDefn.setRowLastmantOprid(oprid);
					
					int rtn = psTzTreeDefnMapper.insert(psTzTreeDefn);
					if(rtn != 0){
						sql = "SELECT SETID,SETCNTRLVALUE,EFFDT,TREE_NODE_NUM,TREE_NODE,TREE_BRANCH,TREE_NODE_NUM_END,TREE_LEVEL_NUM,TREE_NODE_TYPE,PARENT_NODE_NUM,PARENT_NODE_NAME,OLD_TREE_NODE_NUM,NODECOL_IMAGE,NODEEXP_IMAGE FROM PSTREENODE WHERE TREE_NAME=?";
						List<Map<String,Object>> treeNodeList = jdbcTemplate.queryForList(sql, new Object[]{ treeName });
						
						for(Map<String,Object> treeNodeMap: treeNodeList){
							String SETID = treeNodeMap.get("SETID").toString();
							String SETCNTRLVALUE = treeNodeMap.get("SETCNTRLVALUE").toString();
							String EFFDT = treeNodeMap.get("EFFDT").toString();
							String TREE_NODE_NUM = treeNodeMap.get("TREE_NODE_NUM").toString();
							String TREE_NODE = treeNodeMap.get("TREE_NODE").toString();
							String TREE_BRANCH = treeNodeMap.get("TREE_BRANCH").toString();
							String TREE_NODE_NUM_END = treeNodeMap.get("TREE_NODE_NUM_END").toString();
							String TREE_LEVEL_NUM = treeNodeMap.get("TREE_LEVEL_NUM").toString();
							String TREE_NODE_TYPE = treeNodeMap.get("TREE_NODE_TYPE").toString();
							String PARENT_NODE_NUM = treeNodeMap.get("PARENT_NODE_NUM").toString();
							String OLD_TREE_NODE_NUM = treeNodeMap.get("OLD_TREE_NODE_NUM").toString();
							
							String dtFormat = getSysHardCodeVal.getDateFormat();
							SimpleDateFormat format = new SimpleDateFormat(dtFormat);
							Date effdt = format.parse(EFFDT);
							
							Integer treeNodeNum = Integer.valueOf(TREE_NODE_NUM);
							Integer treeNodeNumEnd = Integer.valueOf(TREE_NODE_NUM_END);
							Short treeLevelNum = Short.valueOf(TREE_LEVEL_NUM);
							Integer parentNodeNum = Integer.valueOf(PARENT_NODE_NUM);
							
							PsTreeNode psTreeNode = new PsTreeNode();
							psTreeNode.setSetid(SETID);
							psTreeNode.setSetcntrlvalue(SETCNTRLVALUE);
							psTreeNode.setEffdt(effdt);
							psTreeNode.setTreeNodeNum(treeNodeNum);
							psTreeNode.setTreeNode(TREE_NODE);
							psTreeNode.setTreeBranch(TREE_BRANCH);
							psTreeNode.setTreeNodeNumEnd(treeNodeNumEnd);
							psTreeNode.setTreeLevelNum(treeLevelNum);
							psTreeNode.setTreeNodeType(TREE_NODE_TYPE);
							psTreeNode.setParentNodeNum(parentNodeNum);
							
							if(treeNodeMap.get("PARENT_NODE_NAME") != null){
								String PARENT_NODE_NAME = treeNodeMap.get("PARENT_NODE_NAME").toString();
								psTreeNode.setParentNodeName(PARENT_NODE_NAME);
							}

							psTreeNode.setOldTreeNodeNum(OLD_TREE_NODE_NUM);
							
							if(treeNodeMap.get("NODECOL_IMAGE") != null){
								String NODECOL_IMAGE = treeNodeMap.get("NODECOL_IMAGE").toString();
								psTreeNode.setNodecolImage(NODECOL_IMAGE);
							}
							if(treeNodeMap.get("NODEEXP_IMAGE") != null){
								String NODEEXP_IMAGE = treeNodeMap.get("NODEEXP_IMAGE").toString();
								psTreeNode.setNodeexpImage(NODEEXP_IMAGE);
							}
							psTreeNode.setTreeName(copyTreeName);
							
							psTreeNodeMapper.insert(psTreeNode);
							
							//复制成绩项信息
							PsTzModalDtTblKey psTzModalDtTblKey = new PsTzModalDtTblKey();
							psTzModalDtTblKey.setTzJgId(orgId);
							psTzModalDtTblKey.setTreeName(treeName);
							psTzModalDtTblKey.setTzScoreItemId(TREE_NODE);
							PsTzModalDtTblWithBLOBs psTzModalDtTbl = psTzModalDtTblMapper.selectByPrimaryKey(psTzModalDtTblKey);
							
							if(psTzModalDtTbl != null){
								psTzModalDtTbl.setTreeName(copyTreeName);
								psTzModalDtTbl.setRowAddedDttm(new Date());
								psTzModalDtTbl.setRowAddedOprid(oprid);
								psTzModalDtTbl.setRowLastmantDttm(new Date());
								psTzModalDtTbl.setRowLastmantOprid(oprid);
								
								psTzModalDtTblMapper.insert(psTzModalDtTbl);
								
								sql = "SELECT TZ_CJX_XLK_XXBH,TZ_CJX_XLK_XXMC,TZ_CJX_XLK_XXFZ,TZ_CJX_XLK_MRZ FROM PS_TZ_ZJCJXXZX_T WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
								List<Map<String,Object>> optList = jdbcTemplate.queryForList(sql, new Object[]{ orgId,treeName,TREE_NODE });
								
								for(Map<String,Object> optMap: optList){
									String TZ_CJX_XLK_XXBH = optMap.get("TZ_CJX_XLK_XXBH").toString();
									String TZ_CJX_XLK_XXMC = optMap.get("TZ_CJX_XLK_XXMC").toString();
									String TZ_CJX_XLK_XXFZ = optMap.get("TZ_CJX_XLK_XXFZ").toString();
									String TZ_CJX_XLK_MRZ = optMap.get("TZ_CJX_XLK_MRZ").toString();
									
									PsTzZjcjxXzxT psTzZjcjxXzxT = new PsTzZjcjxXzxT();
									psTzZjcjxXzxT.setTzJgId(orgId);
									psTzZjcjxXzxT.setTreeName(copyTreeName);
									psTzZjcjxXzxT.setTzScoreItemId(TREE_NODE);
									psTzZjcjxXzxT.setTzCjxXlkXxbh(TZ_CJX_XLK_XXBH);
									psTzZjcjxXzxT.setTzCjxXlkXxmc(TZ_CJX_XLK_XXMC);
									if(!"".equals(TZ_CJX_XLK_XXFZ) && TZ_CJX_XLK_XXFZ != null){
										BigDecimal tzCjxXlkXxfz = BigDecimal.valueOf(Double.valueOf(TZ_CJX_XLK_XXFZ));
										psTzZjcjxXzxT.setTzCjxXlkXxfz(tzCjxXlkXxfz);
									}
									psTzZjcjxXzxT.setTzCjxXlkMrz(TZ_CJX_XLK_MRZ);
									
									psTzZjcjxXzxTMapper.insert(psTzZjcjxXzxT);
								}
							}
						}
					}else{
						boolRtn = false;
					}
				}
			}
		}catch(Exception e){
			boolRtn = false;
			e.printStackTrace();
		}
		return boolRtn;
	}
}
