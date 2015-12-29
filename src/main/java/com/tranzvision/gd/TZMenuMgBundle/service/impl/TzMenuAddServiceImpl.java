/**
 * 
 */
package com.tranzvision.gd.TZMenuMgBundle.service.impl;

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
import com.tranzvision.gd.TZMenuMgBundle.dao.PsTzAqCdjdTblMapper;
import com.tranzvision.gd.TZMenuMgBundle.model.PsTzAqCdjdTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 功能菜单信息类，原PS：TZ_GD_GNCDGL_PKG:TZ_GD_GNCDADD_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-16
 */
@Service("com.tranzvision.gd.TZMenuMgBundle.service.impl.TzMenuAddServiceImpl")
public class TzMenuAddServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzMenuTreeNodeServiceImpl tzMenuTreeNodeServiceImpl;

	@Autowired
	private PsTzAqCdjdTblMapper psTzAqCdjdTblMapper;

	/**
	 * 新增功能菜单信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date lastupddttm = new Date();
			String lastupdoprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String menuTree = getSysHardCodeVal.getMenuTreeName();
			// tzMenuTreeNodeServiceImpl.createTree(menuTree);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				Map<String, Object> mapData = jacksonUtil.getMap("data");

				// 机构编号
				String menuOrg = mapData.get("menuOrg").toString();
				String sql = "select TZ_JG_NAME from PS_TZ_JG_BASE_T where TZ_JG_ID=?";
				String orgDesc = sqlQuery.queryForObject(sql, new Object[] { menuOrg }, "String");

				// 复制源机构编号
				String sourceOrg = mapData.get("sourceOrg").toString();

				sql = "select 'Y' from PSTREENODE WHERE TREE_NAME=? and TREE_LEVEL_NUM=2 and UPPER(TREE_NODE)=UPPER(?)";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { menuTree, menuOrg }, "String");

				if (null != recExists) {
					// 如果存在，则删除
					sql = "select TREE_NODE_NUM,TREE_NODE_NUM_END from PSTREENODE WHERE TREE_NAME=? and TREE_LEVEL_NUM=2 and TREE_NODE=?";
					Map<String, Object> mapNodeNum = sqlQuery.queryForMap(sql, new Object[] { menuTree, menuOrg });

					int treeNodeNum = Integer.parseInt(mapNodeNum.get("TREE_NODE_NUM").toString());
					int treeNodeNumEnd = Integer.parseInt(mapNodeNum.get("TREE_NODE_NUM_END").toString());

					sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzDeleteMenuMultiLang");
					sqlQuery.update(sql, new Object[] { menuTree, menuTree, treeNodeNum, treeNodeNumEnd });

					sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzDeleteMenuNode");
					sqlQuery.update(sql, new Object[] { menuTree, menuTree, treeNodeNum, treeNodeNumEnd });

					tzMenuTreeNodeServiceImpl.deleteNode(menuTree, treeNodeNum, treeNodeNumEnd);

				} else {
					// 查询是否已经被树的其他节点占用了
					sql = "select 'Y' from PSTREENODE WHERE TREE_NAME=? and TREE_LEVEL_NUM<>2 and UPPER(TREE_NODE)=UPPER(?)";
					recExists = sqlQuery.queryForObject(sql, new Object[] { menuTree, menuOrg }, "String");
					if (null != recExists) {
						return strRet;
					}

				}

				// 如果是新建
				if (null != menuOrg && !"".equals(menuOrg) && (null == sourceOrg || "".equals(sourceOrg))) {
					// 若复制源为空
					boolean boolRst = tzMenuTreeNodeServiceImpl.createChildNode(menuTree, "ROOT", menuOrg);

					mapRet.put("success", "false");

					if (boolRst) {
						mapRet.replace("success", "true");

						sql = "select 'Y' from PS_TZ_AQ_CDJD_TBL where TREE_NAME=? and TZ_MENU_NUM=?";
						recExists = sqlQuery.queryForObject(sql, new Object[] { menuTree, "ROOT" }, "String");
						if (null == recExists) {
							PsTzAqCdjdTbl psTzAqCdjdTbl = new PsTzAqCdjdTbl();

							psTzAqCdjdTbl.setTreeName(menuTree);
							psTzAqCdjdTbl.setTzMenuNum("ROOT");
							psTzAqCdjdTbl.setTzMenuMc("高端产品功能菜单根节点");
							psTzAqCdjdTbl.setTzYxx("Y");
							psTzAqCdjdTbl.setRowAddedDttm(lastupddttm);
							psTzAqCdjdTbl.setRowAddedOprid(lastupdoprid);
							psTzAqCdjdTbl.setRowLastmantDttm(lastupddttm);
							psTzAqCdjdTbl.setRowLastmantOprid(lastupdoprid);

							psTzAqCdjdTblMapper.insert(psTzAqCdjdTbl);
						}

						sql = "select 'Y' from PS_TZ_AQ_CDJD_TBL where TREE_NAME=? and TZ_MENU_NUM=?";
						recExists = sqlQuery.queryForObject(sql, new Object[] { menuTree, menuOrg }, "String");
						if (null == recExists) {
							PsTzAqCdjdTbl psTzAqCdjdTbl = new PsTzAqCdjdTbl();

							psTzAqCdjdTbl.setTreeName(menuTree);
							psTzAqCdjdTbl.setTzMenuNum(menuOrg);
							psTzAqCdjdTbl.setTzMenuMc(orgDesc);
							psTzAqCdjdTbl.setTzYxx("Y");
							psTzAqCdjdTbl.setRowAddedDttm(lastupddttm);
							psTzAqCdjdTbl.setRowAddedOprid(lastupdoprid);
							psTzAqCdjdTbl.setRowLastmantDttm(lastupddttm);
							psTzAqCdjdTbl.setRowLastmantOprid(lastupdoprid);

							psTzAqCdjdTblMapper.insert(psTzAqCdjdTbl);
						}

					}

					strRet = jacksonUtil.Map2json(mapRet);

				} else if (null != menuOrg && !"".equals(menuOrg) && null != sourceOrg && !"".equals(sourceOrg)) {
					// 若复制源不为空
					// 查找源节点
					sql = "select 'Y' from PSTREENODE WHERE TREE_NAME=? and TREE_LEVEL_NUM=2 and UPPER(TREE_NODE)=UPPER(?)";
					recExists = sqlQuery.queryForObject(sql, new Object[] { menuTree, sourceOrg }, "String");

					if ("Y".equals(recExists)) {
						sql = "select TREE_NODE_NUM,TREE_NODE_NUM_END from PSTREENODE WHERE TREE_NAME=? and TREE_LEVEL_NUM=2 and TREE_NODE=?";
						Map<String, Object> mapNodeNum = sqlQuery.queryForMap(sql,
								new Object[] { menuTree, sourceOrg });
						int treeNodeNum = Integer.parseInt(mapNodeNum.get("TREE_NODE_NUM").toString());
						int treeNodeNumEnd = Integer.parseInt(mapNodeNum.get("TREE_NODE_NUM_END").toString());

						ArrayList<String> aryNodes = new ArrayList<String>();
						ArrayList<String> aryMenuOrgs = new ArrayList<String>();

						// 循环所有源节点和子节点
						sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzSelectAllNodes");
						List<?> listSrcTreeNodes = sqlQuery.queryForList(sql,
								new Object[] { menuTree, menuTree, treeNodeNum, treeNodeNumEnd });
						for (Object objSrcTreeNode : listSrcTreeNodes) {

							Map<String, Object> mapSrcTreeNode = (Map<String, Object>) objSrcTreeNode;

							int nodeNum = Integer.parseInt(mapSrcTreeNode.get("TREE_NODE_NUM").toString());
							int nodeNumEnd = Integer.parseInt(mapSrcTreeNode.get("TREE_NODE_NUM_END").toString());

							String nodeName = mapSrcTreeNode.get("TREE_NODE").toString();
							String parentenode = mapSrcTreeNode.get("PARENT_NODE_NAME").toString();

							if (nodeNum == treeNodeNum && nodeNumEnd == treeNodeNumEnd) {

								boolean boolRst = tzMenuTreeNodeServiceImpl.createChildNode(menuTree, parentenode,
										menuOrg);

								if (boolRst) {
									PsTzAqCdjdTbl psTzAqCdjdTbl = new PsTzAqCdjdTbl();

									psTzAqCdjdTbl.setTreeName(menuTree);
									psTzAqCdjdTbl.setTzMenuNum(menuOrg);
									psTzAqCdjdTbl.setTzMenuMc(orgDesc);
									psTzAqCdjdTbl.setTzYxx(mapSrcTreeNode.get("TZ_YXX").toString());
									psTzAqCdjdTbl.setTzComId(mapSrcTreeNode.get("TZ_COM_ID").toString());
									psTzAqCdjdTbl.setTzMenuLimg(mapSrcTreeNode.get("TZ_MENU_LIMG").toString());
									psTzAqCdjdTbl.setTzMenuSimg(mapSrcTreeNode.get("TZ_MENU_SIMG").toString());
									psTzAqCdjdTbl.setTzMenuNrid(mapSrcTreeNode.get("TZ_MENU_NRID").toString());
									psTzAqCdjdTbl.setRowAddedDttm(lastupddttm);
									psTzAqCdjdTbl.setRowAddedOprid(lastupdoprid);
									psTzAqCdjdTbl.setRowLastmantDttm(lastupddttm);
									psTzAqCdjdTbl.setRowLastmantOprid(lastupdoprid);

									psTzAqCdjdTblMapper.insert(psTzAqCdjdTbl);

									aryNodes.add(nodeName);
									aryMenuOrgs.add(menuOrg);

									// 复制当前组织结构功能菜单节点在多语言表中对应的记录
									sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzJgMenuCreate");
									sqlQuery.update(sql, new Object[] { menuOrg, menuTree, sourceOrg, menuOrg });
								}

							} else {

								int num2 = aryNodes.indexOf(parentenode);

								if (num2 >= 0) {

								} else {
									continue;
								}

								String newNodeName = nodeName.replaceAll(sourceOrg, menuOrg);
								if (newNodeName.equals(nodeName)) {
									newNodeName = menuOrg + "_" + nodeName;
									if (newNodeName.length() > 19) {
										newNodeName = newNodeName.substring(0, 19);
									}
								}

								// 查看该名字是否已经有了
								sql = "select 'Y' from PSTREENODE where TREE_NAME=? and TREE_NODE=?";
								recExists = sqlQuery.queryForObject(sql, new Object[] { menuTree, newNodeName },
										"String");

								if ("Y".equals(recExists)) {
									boolean reCreateNewNode = true;
									while (reCreateNewNode) {
										newNodeName = menuOrg + "_" + Math.round(Math.random() * 899999999 + 100000000);
										if (newNodeName.length() > 19) {
											newNodeName = newNodeName.substring(0, 19);
										}
										sql = "select 'Y' from PSTREENODE where TREE_NAME=? and TREE_NODE=?";
										recExists = sqlQuery.queryForObject(sql, new Object[] { menuTree, newNodeName },
												"String");
										if (!"Y".equals(recExists)) {
											reCreateNewNode = false;
										}
									}
								}

								boolean boolRst = tzMenuTreeNodeServiceImpl.createChildNode(menuTree,
										aryMenuOrgs.get(num2), newNodeName);

								if (boolRst) {
									PsTzAqCdjdTbl psTzAqCdjdTbl = new PsTzAqCdjdTbl();

									psTzAqCdjdTbl.setTreeName(menuTree);
									psTzAqCdjdTbl.setTzMenuNum(newNodeName);
									psTzAqCdjdTbl.setTzMenuMc(mapSrcTreeNode.get("TZ_MENU_MC").toString());
									psTzAqCdjdTbl.setTzYxx(mapSrcTreeNode.get("TZ_YXX").toString());
									psTzAqCdjdTbl.setTzComId(mapSrcTreeNode.get("TZ_COM_ID").toString());
									psTzAqCdjdTbl.setTzMenuLimg(mapSrcTreeNode.get("TZ_MENU_LIMG").toString());
									psTzAqCdjdTbl.setTzMenuSimg(mapSrcTreeNode.get("TZ_MENU_SIMG").toString());
									psTzAqCdjdTbl.setTzMenuNrid(mapSrcTreeNode.get("TZ_MENU_NRID").toString());
									psTzAqCdjdTbl.setRowAddedDttm(lastupddttm);
									psTzAqCdjdTbl.setRowAddedOprid(lastupdoprid);
									psTzAqCdjdTbl.setRowLastmantDttm(lastupddttm);
									psTzAqCdjdTbl.setRowLastmantOprid(lastupdoprid);

									psTzAqCdjdTblMapper.insert(psTzAqCdjdTbl);

									aryNodes.add(nodeName);
									aryMenuOrgs.add(newNodeName);

									// 复制当前组织结构功能菜单节点在多语言表中对应的记录
									sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzJgMenuCreate");
									sqlQuery.update(sql, new Object[] { newNodeName, menuTree, nodeName, newNodeName });
								}

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

		return strRet;
	}

	/**
	 * 
	 * @param strParams
	 * @return
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {

		Map<String, Object> mapRet = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String menuOrg = jacksonUtil.getString("menuOrg");

		String menuTree = getSysHardCodeVal.getMenuTreeName();

		// 已经有该跟节点
		String sql = "select 'Y' from PSTREENODE where TREE_NAME=? and TREE_LEVEL_NUM=2 and UPPER(TREE_NODE)=UPPER(?)";
		String recExist = sqlQuery.queryForObject(sql, new Object[] { menuTree, menuOrg }, "String");
		if ("Y".equals(recExist)) {

			mapRet.put("success", "false");

		} else {

			// 判断是否已经被其他树的节点占用了
			sql = "select 'Y' from PSTREENODE where TREE_NAME=? and TREE_LEVEL_NUM<>2 and UPPER(TREE_NODE)=UPPER(?)";
			recExist = sqlQuery.queryForObject(sql, new Object[] { menuTree, menuOrg }, "String");

			if ("Y".equals(recExist)) {
				mapRet.put("success", "fail");
			} else {
				mapRet.put("success", "true");
			}

		}

		return jacksonUtil.Map2json(mapRet);
	}

}
