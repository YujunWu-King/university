package com.tranzvision.gd.TZScoreModeManagementBundle.service.impl;

import java.text.ParseException;
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
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMenuMgBundle.dao.PsTreeNodeMapper;
import com.tranzvision.gd.TZMenuMgBundle.model.PsTreeNode;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzCjBphTblMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzModalDtTblMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzRsModalTblMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzTreeDefnMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzCjBphTbl;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzCjBphTblKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTbl;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTblKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTblWithBLOBs;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzRsModalTbl;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzRsModalTblKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzTreeDefn;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzTreeDefnKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZScoreModeManagementBundle.service.impl.TzScoreModeDefnServiceImpl")
public class TzScoreModeDefnServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal; 
	
	@Autowired
	private PsTzRsModalTblMapper psTzRsModalTblMapper;
	
	@Autowired
	private PsTzCjBphTblMapper psTzCjBphTblMapper;
	
	@Autowired
	private PsTzTreeDefnMapper psTzTreeDefnMapper;
	
	@Autowired
	private PsTreeNodeMapper psTreeNodeMapper;
	
	@Autowired
	private PsTzModalDtTblMapper psTzModalDtTblMapper;
	
	/**
	 * 成绩模型扁平化grid
	 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String orgId = jacksonUtil.getString("orgId");
			String modeId = jacksonUtil.getString("modeId");
			String type = jacksonUtil.getString("type");

			if(null != orgId && !"".equals(orgId) && null != modeId && !"".equals(modeId)){
				//查询面试安排总数
				String sql = "SELECT COUNT(*) FROM PS_TZ_CJ_BPH_TBL WHERE TZ_JG_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_ITEM_S_TYPE=?";
				int total = jdbcTemplate.queryForObject(sql, new Object[]{orgId, modeId, type}, "int");

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				if (total > 0) {
					sql = "SELECT TZ_SCORE_ITEM_ID,TZ_XS_MC,TZ_PX FROM PS_TZ_CJ_BPH_TBL WHERE TZ_JG_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_ITEM_S_TYPE=? ORDER BY TZ_PX";
					List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[]{orgId, modeId, type});

					for(Map<String,Object> mapData : listData){
						Map<String,Object> mapJson = new HashMap<String,Object>();
						
						String itemId = mapData.get("TZ_SCORE_ITEM_ID").toString();
						String viewName = mapData.get("TZ_XS_MC").toString();
						Integer sortNum = Integer.valueOf(mapData.get("TZ_PX").toString());

						mapJson.put("orgId", orgId);
						mapJson.put("modeId", modeId);
						mapJson.put("itemId", itemId);
						mapJson.put("itemType", type);
						mapJson.put("sortNum", sortNum);
						mapJson.put("viewName", viewName);
						
						listJson.add(mapJson);
					}
					mapRet.replace("total", total);
					mapRet.replace("root", listJson);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//获取当前登录机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String type = jacksonUtil.getString("type");
				Map<String,Object> formDate = jacksonUtil.getMap("data");
				
				if("modeInfo".equals(type)){
					String modelId = formDate.get("modelId").toString();
					String modeName = formDate.get("modeName").toString();
					String status = formDate.get("status").toString();
					String treeName = formDate.get("treeName").toString();
					String totalScoreModel = formDate.get("totalScoreModel").toString();
					
					PsTzRsModalTblKey psTzRsModalTblKey = new PsTzRsModalTblKey();
					psTzRsModalTblKey.setTzJgId(orgId);
					psTzRsModalTblKey.setTzScoreModalId(modelId);
					PsTzRsModalTbl psTzRsModalTbl = psTzRsModalTblMapper.selectByPrimaryKey(psTzRsModalTblKey);
					if(psTzRsModalTbl == null){
						psTzRsModalTbl = new PsTzRsModalTbl();
						psTzRsModalTbl.setTzJgId(orgId);
						psTzRsModalTbl.setTzScoreModalId(modelId);
						psTzRsModalTbl.setTzModalName(modeName);
						psTzRsModalTbl.setTzModalFlag(status);
						psTzRsModalTbl.setTzMFbdzId(totalScoreModel);
						psTzRsModalTbl.setTreeName(treeName);
						//当前时间
						Date currDate = new Date();
						psTzRsModalTbl.setRowAddedOprid(oprid);
						psTzRsModalTbl.setRowLastmantOprid(oprid);
						psTzRsModalTbl.setRowAddedDttm(currDate);
						psTzRsModalTbl.setRowLastmantDttm(currDate);
						
						int rtn = psTzRsModalTblMapper.insert(psTzRsModalTbl);
						if(rtn != 0){
							Map<String, Object> mapForm = new HashMap<String, Object>();
							
							String dttmFormat = getSysHardCodeVal.getDateTimeHMFormat();
							SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
							String addDatetime =  dttmSimpleDateFormat.format(currDate);
							
							String sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
							String oprName = jdbcTemplate.queryForObject(sql, new Object[]{oprid}, "String");
							
							sql = "SELECT TZ_JG_NAME FROM PS_TZ_JG_BASE_T WHERE TZ_JG_ID=?";
							String orgName = jdbcTemplate.queryForObject(sql, new Object[]{orgId}, "String");
							
							mapForm.put("orgId", orgId);
							mapForm.put("orgDesc", orgName);
							mapForm.put("addOprName", oprName);
							mapForm.put("addoprDttm", addDatetime);
							mapForm.put("updateOprName", oprName);
							mapForm.put("updateOprDttm", addDatetime);
							
							mapRet.replace("result", "success");
							mapRet.replace("formData", mapForm);
						}else{
							errMsg[0] = "1";
							errMsg[1] = "保存失败";
							mapRet.replace("result", "fail");
						}
					}else{
						errMsg[0] = "1";
						errMsg[1] = "模型ID已存在";
						mapRet.replace("result", "fail");
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
				
				if("modeInfo".equals(type)){
					Map<String,Object> formDate = jacksonUtil.getMap("data");
					
					String orgId = formDate.get("orgId").toString();
					String modelId = formDate.get("modelId").toString();
					String modeName = formDate.get("modeName").toString();
					String status = formDate.get("status").toString();
					String treeName = formDate.get("treeName").toString();
					String totalScoreModel = formDate.get("totalScoreModel").toString();
					
					PsTzRsModalTblKey psTzRsModalTblKey = new PsTzRsModalTblKey();
					psTzRsModalTblKey.setTzJgId(orgId);
					psTzRsModalTblKey.setTzScoreModalId(modelId);
					PsTzRsModalTbl psTzRsModalTbl = psTzRsModalTblMapper.selectByPrimaryKey(psTzRsModalTblKey);
					if(psTzRsModalTbl != null){
						psTzRsModalTbl.setTzModalName(modeName);
						psTzRsModalTbl.setTzModalFlag(status);
						psTzRsModalTbl.setTzMFbdzId(totalScoreModel);
						psTzRsModalTbl.setTreeName(treeName);
						//当前时间
						Date currDate = new Date();
						psTzRsModalTbl.setRowLastmantOprid(oprid);
						psTzRsModalTbl.setRowLastmantDttm(currDate);
						
						int rtn = psTzRsModalTblMapper.updateByPrimaryKey(psTzRsModalTbl);
						if(rtn != 0){
							Map<String, Object> mapForm = new HashMap<String, Object>();
							
							String dttmFormat = getSysHardCodeVal.getDateTimeHMFormat();
							SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
							String addDatetime =  dttmSimpleDateFormat.format(currDate);
							
							String sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
							String oprName = jdbcTemplate.queryForObject(sql, new Object[]{oprid}, "String");

							mapForm.put("updateOprName", oprName);
							mapForm.put("updateOprDttm", addDatetime);
							
							mapRet.replace("result", "success");
							mapRet.replace("formData", mapForm);
						}else{
							errMsg[0] = "1";
							errMsg[1] = "保存失败";
							mapRet.replace("result", "fail");
						}
					}else{
						errMsg[0] = "1";
						errMsg[1] = "模型ID已存在";
						mapRet.replace("result", "fail");
					}
				}else if("dfItemsInfo".equals(type) || "pwItemsInfo".equals(type)){
					List<Map<String,Object>> itemsList = (List<Map<String, Object>>) jacksonUtil.getList("data");
					Date currDate = new Date();
					
					for(Map<String,Object> itemMap : itemsList){
						String orgId = itemMap.get("orgId").toString();
						String modeId = itemMap.get("modeId").toString();
						String itemId = itemMap.get("itemId").toString();
						String itemType = itemMap.get("itemType").toString();
						Integer sortNum = Integer.valueOf(itemMap.get("sortNum").toString());
						String viewName = itemMap.get("viewName").toString();
						
						PsTzCjBphTblKey psTzCjBphTblKey = new PsTzCjBphTblKey();
						psTzCjBphTblKey.setTzJgId(orgId);
						psTzCjBphTblKey.setTzScoreModalId(modeId);
						psTzCjBphTblKey.setTzScoreItemId(itemId);
						psTzCjBphTblKey.setTzItemSType(itemType);
						
						PsTzCjBphTbl psTzCjBphTbl = psTzCjBphTblMapper.selectByPrimaryKey(psTzCjBphTblKey);
						if(psTzCjBphTbl == null){
							psTzCjBphTbl = new PsTzCjBphTbl();
							psTzCjBphTbl.setTzJgId(orgId);
							psTzCjBphTbl.setTzScoreModalId(modeId);
							psTzCjBphTbl.setTzScoreItemId(itemId);
							psTzCjBphTbl.setTzItemSType(itemType);
							psTzCjBphTbl.setTzXsMc(viewName);
							psTzCjBphTbl.setTzPx(sortNum);
							psTzCjBphTbl.setRowAddedOprid(oprid);
							psTzCjBphTbl.setRowAddedDttm(currDate);
							psTzCjBphTbl.setRowLastmantOprid(oprid);
							psTzCjBphTbl.setRowLastmantDttm(currDate);
							psTzCjBphTblMapper.insert(psTzCjBphTbl);
						}else{
							psTzCjBphTbl.setTzXsMc(viewName);
							psTzCjBphTbl.setTzPx(sortNum);
							psTzCjBphTbl.setRowLastmantOprid(oprid);
							psTzCjBphTbl.setRowLastmantDttm(currDate);
							psTzCjBphTblMapper.updateByPrimaryKeySelective(psTzCjBphTbl);
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
				
				if("dfItemsInfo".equals(type) || "pwItemsInfo".equals(type)){
					List<Map<String,Object>> itemsList = (List<Map<String, Object>>) jacksonUtil.getList("data");

					for(Map<String,Object> itemMap : itemsList){
						String orgId = itemMap.get("orgId").toString();
						String modeId = itemMap.get("modeId").toString();
						String itemId = itemMap.get("itemId").toString();
						String itemType = itemMap.get("itemType").toString();
						
						PsTzCjBphTblKey psTzCjBphTblKey = new PsTzCjBphTblKey();
						psTzCjBphTblKey.setTzJgId(orgId);
						psTzCjBphTblKey.setTzScoreModalId(modeId);
						psTzCjBphTblKey.setTzScoreItemId(itemId);
						psTzCjBphTblKey.setTzItemSType(itemType);
						
						PsTzCjBphTbl psTzCjBphTbl = psTzCjBphTblMapper.selectByPrimaryKey(psTzCjBphTblKey);
						if(psTzCjBphTbl != null){
							psTzCjBphTblMapper.deleteByPrimaryKey(psTzCjBphTblKey);
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
	
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
				case "queryScoreModelTree":
					//查询成绩模型树是否存在
					strRet = this.queryScoreModelTree(strParams, errorMsg);
					break;
				case "createScoreModelTree":
					//创建成绩模型树
					strRet = this.createScoreModelTree(strParams, errorMsg);
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
	 * 查询成绩模型树是否存在
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String queryScoreModelTree(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("treeExists", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		try {
			jacksonUtil.json2Map(strParams);
			
			String treeName = jacksonUtil.getString("treeName");
			
			String sql = "SELECT 'Y' FROM PS_TZ_TREEDEFN WHERE TZ_JG_ID=? and TREE_NAME=? and TZ_TREE_TYPE='A'";
			String treeExists = jdbcTemplate.queryForObject(sql, new Object[]{ orgId, treeName }, "String");

			if("Y".equals(treeExists)){
				rtnMap.replace("treeExists", treeExists);
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
	 * 创建成绩模型树
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String createScoreModelTree(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			
			String treeName = jacksonUtil.getString("treeName");
			
			PsTzTreeDefnKey psTzTreeDefnKey = new PsTzTreeDefnKey();
			psTzTreeDefnKey.setTzJgId(orgId);
			psTzTreeDefnKey.setTreeName(treeName);
			
			PsTzTreeDefn psTzTreeDefn = psTzTreeDefnMapper.selectByPrimaryKey(psTzTreeDefnKey);
			if(psTzTreeDefn == null){
				psTzTreeDefn = new PsTzTreeDefn();
				psTzTreeDefn.setTzJgId(orgId);
				psTzTreeDefn.setTreeName(treeName);
				psTzTreeDefn.setTzTreeType("A");
				psTzTreeDefn.setTzEffect("Y");
				psTzTreeDefn.setDescr("");
				
				Date currDate = new Date();
				psTzTreeDefn.setRowAddedOprid(oprId);
				psTzTreeDefn.setRowAddedDttm(currDate);
				psTzTreeDefn.setRowLastmantOprid(oprId);
				psTzTreeDefn.setRowLastmantDttm(currDate);
				
				int rtn = psTzTreeDefnMapper.insert(psTzTreeDefn);
				if(rtn != 0){
					this.createTreeNode(orgId, treeName, "Total");
					
					rtnMap.replace("result", "success");
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
	
	
	public void createTreeNode(String orgId, String treeName,String root) {
		String dtFormat = getSysHardCodeVal.getDateFormat();
		SimpleDateFormat format = new SimpleDateFormat(dtFormat);
		Date effdt;
		try {
			effdt = format.parse(format.format(new Date()));

			PsTreeNode psTreeNode = new PsTreeNode();
			psTreeNode.setSetid("");
			psTreeNode.setSetcntrlvalue("");
			psTreeNode.setTreeBranch("");
			psTreeNode.setTreeName(treeName);
			psTreeNode.setEffdt(effdt);
			psTreeNode.setTreeNodeNum(1);
			psTreeNode.setTreeNode(root);
			psTreeNode.setTreeNodeNumEnd(2000000000);
			psTreeNode.setTreeLevelNum((short) 1);
			psTreeNode.setTreeNodeType("G");
			psTreeNode.setParentNodeNum(0);
			psTreeNode.setOldTreeNodeNum("N");

			int rtn = psTreeNodeMapper.insert(psTreeNode);
			if(rtn != 0){
				PsTzModalDtTblKey psTzModalDtTblKey = new PsTzModalDtTblKey();
				psTzModalDtTblKey.setTzJgId(orgId);
				psTzModalDtTblKey.setTreeName(treeName);
				psTzModalDtTblKey.setTzScoreItemId(root);

				PsTzModalDtTbl psTzModalDtTbl = psTzModalDtTblMapper.selectByPrimaryKey(psTzModalDtTblKey);
				if(psTzModalDtTbl == null){
					
					PsTzModalDtTblWithBLOBs psTzModalDtTblWithBLOBs = new PsTzModalDtTblWithBLOBs();
					psTzModalDtTblWithBLOBs.setTzJgId(orgId);
					psTzModalDtTblWithBLOBs.setTreeName(treeName);
					psTzModalDtTblWithBLOBs.setTzScoreItemId(root);
					
					psTzModalDtTblMapper.insertSelective(psTzModalDtTblWithBLOBs);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
}
