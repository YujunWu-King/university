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
import com.tranzvision.gd.TZMenuMgBundle.dao.PsTzAqCdjdLngMapper;
import com.tranzvision.gd.TZMenuMgBundle.dao.PsTzAqCdjdTblMapper;
import com.tranzvision.gd.TZMenuMgBundle.model.PsTzAqCdjdLng;
import com.tranzvision.gd.TZMenuMgBundle.model.PsTzAqCdjdTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 功能菜单信息类，原PS：TZ_GD_GNCDGL_PKG:TZ_GD_GNCDXX_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-17
 */
@Service("com.tranzvision.gd.TZMenuMgBundle.service.impl.TzMenuInfoServiceImpl")
public class TzMenuInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetHardCodePoint getHardCodePoint;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzMenuTreeNodeServiceImpl tzMenuTreeNodeServiceImpl;

	@Autowired
	private PsTzAqCdjdTblMapper psTzAqCdjdTblMapper;

	@Autowired
	private PsTzAqCdjdLngMapper psTzAqCdjdLngMapper;

	/**
	 * 功能说明：插入节点信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {

		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData.length == 0) {
			return strRet;
		}

		String baseLanguage = getHardCodePoint.getHardCodePointVal("TZGD_BASIC_LANGUAGE");
		if (null == baseLanguage || "".equals(baseLanguage)) {
			baseLanguage = getSysHardCodeVal.getSysDefaultLanguage();
		} else {
			baseLanguage = baseLanguage.toUpperCase();
		}
		
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			String menuTree = getSysHardCodeVal.getMenuTreeName();
			Date dateNow = new Date();
			String loginOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String synchronous = jacksonUtil.getString("synchronous");

				// 判断是否是同步多语言数据的操作
				if ("true".equals(synchronous)) {

					String sDataLanguage = jacksonUtil.getString("sDataLanguage");
					String tDataLanguage = jacksonUtil.getString("tDataLanguage");
					String rootNodeId = jacksonUtil.getString("menuId");

					return this.synMultiLanguageData(menuTree, rootNodeId, baseLanguage, sDataLanguage, tDataLanguage);

				} else {

					// 信息内容
					Map<String, Object> infoData = jacksonUtil.getMap("data");

					// 获取目标语言代码
					String targetLanguage = jacksonUtil.getString("dataLanguage");

					// 菜单节点编号;
					String menuId = infoData.get("menuId").toString();
					// 菜单名称;
					String menuName = infoData.get("menuName").toString();
					// 有效状态;
					String menuYxState = infoData.get("menuYxState").toString();
					// 菜单对应组件ID;
					String comId = infoData.get("comId").toString();
					// 菜单大图标ID;
					String bigImgId = infoData.get("bigImgId").toString();
					// 菜单小图标ID;
					String smallImgId = infoData.get("smallImgId").toString();
					// 帮助信息内容ID;
					String helpId = infoData.get("helpId").toString();
					// 插入同级节点还是子节点,Y:表示同级节点，'N'表示子节点;
					String NodeType = infoData.get("NodeType").toString();
					// 插入同级节点或子节点是在哪个节点上操作的;
					String operateNode = infoData.get("operateNode").toString();

					if ((menuId == null || "".equals(menuId)) || (menuName == null || "".equals(menuName))) {
						return "";
					}

					boolean boolRst = false;
					switch (NodeType) {
					case "Y":
						boolRst = tzMenuTreeNodeServiceImpl.createBrotherNode(menuTree, operateNode, menuId);
						break;
					case "N":
						boolRst = tzMenuTreeNodeServiceImpl.createChildNode(menuTree, operateNode, menuId);
						break;
					default:
						boolRst = true;
						break;
					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("success", "false");

					if (boolRst) {
						String sql = "select 'Y' from PS_TZ_AQ_CDJD_TBL where TREE_NAME=? and TZ_MENU_NUM=?";
						String recExists = sqlQuery.queryForObject(sql, new Object[] { menuTree, menuId }, "String");
						if ("Y".equals(recExists)) {

							if (baseLanguage.equals(targetLanguage)) {
								// 只有当前目标数据语言代码与基础语言代码一致的时候，才能更新主表中对应的节点数据信息
								PsTzAqCdjdTbl psTzAqCdjdTbl = new PsTzAqCdjdTbl();

								psTzAqCdjdTbl.setTreeName(menuTree);
								psTzAqCdjdTbl.setTzMenuNum(menuId);
								psTzAqCdjdTbl.setTzMenuMc(menuName);
								psTzAqCdjdTbl.setTzYxx(menuYxState);
								psTzAqCdjdTbl.setTzComId(comId);
								psTzAqCdjdTbl.setTzMenuLimg(bigImgId);
								psTzAqCdjdTbl.setTzMenuSimg(smallImgId);
								psTzAqCdjdTbl.setTzMenuNrid(helpId);
								psTzAqCdjdTbl.setRowLastmantDttm(dateNow);
								psTzAqCdjdTbl.setRowLastmantOprid(loginOprid);

								psTzAqCdjdTblMapper.updateByPrimaryKey(psTzAqCdjdTbl);
							}

						} else {

							// 无论如何，如果记录在主表中都有一条与当前基础语言对应的节点记录
							PsTzAqCdjdTbl psTzAqCdjdTbl = new PsTzAqCdjdTbl();

							psTzAqCdjdTbl.setTreeName(menuTree);
							psTzAqCdjdTbl.setTzMenuNum(menuId);
							psTzAqCdjdTbl.setTzMenuMc(menuName);
							psTzAqCdjdTbl.setTzYxx(menuYxState);
							psTzAqCdjdTbl.setTzComId(comId);
							psTzAqCdjdTbl.setTzMenuLimg(bigImgId);
							psTzAqCdjdTbl.setTzMenuSimg(smallImgId);
							psTzAqCdjdTbl.setTzMenuNrid(helpId);
							psTzAqCdjdTbl.setRowLastmantDttm(dateNow);
							psTzAqCdjdTbl.setRowLastmantOprid(loginOprid);

							psTzAqCdjdTblMapper.insert(psTzAqCdjdTbl);

						}

						// 维护多语言节点信息表中的节点数据
						if (!baseLanguage.equals(targetLanguage)) {

							sql = "select 'Y' from PS_TZ_AQ_CDJD_LNG where TREE_NAME=? and TZ_MENU_NUM=? and TZ_LANGUAGE_ID=?";
							recExists = sqlQuery.queryForObject(sql, new Object[] { menuTree, menuId, targetLanguage },
									"String");

							PsTzAqCdjdLng psTzAqCdjdLng = new PsTzAqCdjdLng();

							psTzAqCdjdLng.setTreeName(menuTree);
							psTzAqCdjdLng.setTzMenuNum(menuId);
							psTzAqCdjdLng.setTzLanguageId(targetLanguage);
							psTzAqCdjdLng.setTzMenuMc(menuName);

							if ("Y".equals(recExists)) {
								psTzAqCdjdLngMapper.updateByPrimaryKey(psTzAqCdjdLng);
							} else {
								psTzAqCdjdLngMapper.insert(psTzAqCdjdLng);
							}

						}

						mapRet.put("success", "true");
					}

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

	/**
	 * 删除节点
	 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {

		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			String menuTree = getSysHardCodeVal.getMenuTreeName();
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				Map<String, Object> infoData = jacksonUtil.getMap("data");

				String menuId = infoData.get("menuId").toString();

				String sql = "select TREE_NODE_NUM,TREE_NODE_NUM_END from PSTREENODE A where A.TREE_NAME=? and TREE_NODE=?";

				Map<String, Object> mapNode = sqlQuery.queryForMap(sql, new Object[] { menuTree, menuId });

				int treeNodeNum = Integer.parseInt(mapNode.get("TREE_NODE_NUM").toString());
				int treeNodeNumEnd = Integer.parseInt(mapNode.get("TREE_NODE_NUM_END").toString());

				tzMenuTreeNodeServiceImpl.deleteNode(menuTree, treeNodeNum, treeNodeNumEnd);

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/**
	 * 获取菜单节点信息
	 * 
	 * @param strParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String menuTree = getSysHardCodeVal.getMenuTreeName();
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("menuId")) {

				// 机构编号（菜单树节点编号）
				String menuId = jacksonUtil.getString("menuId");
				// 查询类型
				String typeFlag = jacksonUtil.getString("typeFlag");

				// 获取BaseLanguage
				String baseLanguage = getHardCodePoint.getHardCodePointVal("TZGD_BASIC_LANGUAGE");
				if (null == baseLanguage || "".equals(baseLanguage)) {
					baseLanguage = getSysHardCodeVal.getSysDefaultLanguage();
				} else {
					baseLanguage = baseLanguage.toUpperCase();
				}

				// 获取目标语言代码
				String targetLanguage = jacksonUtil.getString("dataLanguage");

				String strNodeId = request.getParameter("nodeId");
				if (null != strNodeId && !"".equals(strNodeId.trim())) {
					menuId = strNodeId;
					typeFlag = "NODE";
				}

				String sql;
				Map<String, Object> mapRet = new HashMap<String, Object>();
				if ("FORM".equals(typeFlag)) {

					sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzSelectNodeInfoMultiLang");

					Map<String, Object> mapNode = sqlQuery.queryForMap(sql,
							new Object[] { targetLanguage, baseLanguage, menuTree, menuId });

					if(null==mapNode){
						return strRet;
					}
					
					String comId = mapNode.get("TZ_COM_ID").toString();

					sql = "select TZ_COM_MC from PS_TZ_AQ_COMZC_TBL where TZ_COM_ID=?";
					String comName = sqlQuery.queryForObject(sql, new Object[] { comId }, "String");

					Map<String, Object> mapNodeJson = new HashMap<String, Object>();

					mapNodeJson.put("menuId", menuId);
					mapNodeJson.put("menuName", mapNode.get("TZ_MENU_MC").toString());
					mapNodeJson.put("menuYxState", mapNode.get("TZ_YXX").toString());
					mapNodeJson.put("comId", comId);
					mapNodeJson.put("bigImgId", mapNode.get("TZ_MENU_LIMG").toString());
					mapNodeJson.put("smallImgId", mapNode.get("TZ_MENU_SIMG").toString());
					mapNodeJson.put("helpId", mapNode.get("TZ_MENU_NRID").toString());
					mapNodeJson.put("NodeType", "");
					mapNodeJson.put("operateNode", "");
					mapNodeJson.put("rootNode", menuId);
					mapNodeJson.put("comName", comName);

					mapRet.put("formData", mapNodeJson);

				} else if ("TREE".equals(typeFlag)) {

					sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzSelectRootInfoMultiLang");

					Map<String, Object> mapRoot = sqlQuery.queryForMap(sql,
							new Object[] { targetLanguage, baseLanguage, menuTree, menuId });
					
					if(null==mapRoot){
						return strRet;
					}

					List<?> listChildren = this.getMenuList(menuId, baseLanguage, targetLanguage);

					Map<String, Object> mapRootJson = new HashMap<String, Object>();

					mapRootJson.put("id", menuId);
					mapRootJson.put("nodeId", menuId);
					mapRootJson.put("expanded", "true");
					mapRootJson.put("text", mapRoot.get("TZ_MENU_MC_LANG").toString());
					mapRootJson.put("leaf", "false");
					mapRootJson.put("children", listChildren);
					mapRootJson.put("menuYxState", mapRoot.get("TZ_YXX").toString());
					mapRootJson.put("comId", mapRoot.get("TZ_COM_ID").toString());
					mapRootJson.put("bigImgId", mapRoot.get("TZ_MENU_LIMG").toString());
					mapRootJson.put("smallImgId", mapRoot.get("TZ_MENU_SIMG").toString());
					mapRootJson.put("helpId", mapRoot.get("TZ_MENU_NRID").toString());
					mapRootJson.put("NodeType", "");
					mapRootJson.put("operateNode", "");
					mapRootJson.put("rootNode", "");
					mapRootJson.put("comName", mapRoot.get("TZ_COM_MC").toString());

					mapRet.put("root", mapRootJson);
				}

				strRet = jacksonUtil.Map2json(mapRet);

				errMsg[0] = "0";

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 获取菜单的下级菜单列表
	 * 
	 * @param strMenuID
	 * @param baseLanguage
	 * @param targetLanguage
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMenuList(String strMenuID, String baseLanguage, String targetLanguage) {

		List<Map<String, Object>> listRet = new ArrayList<Map<String, Object>>();

		try {
			String menuTree = getSysHardCodeVal.getMenuTreeName();

			String sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzSelectMenuList");

			List<?> listData = sqlQuery.queryForList(sql,
					new Object[] { targetLanguage, baseLanguage, menuTree, strMenuID, menuTree });

			for (Object objNode : listData) {

				Map<String, Object> mapNode = (Map<String, Object>) objNode;

				String strSubMenuID = mapNode.get("TZ_MENU_NUM").toString();

				// 判断该菜单是否是叶子菜单
				sql = "select if(count(1)=0,'Y','N') from PSTREENODE where TREE_NAME = ? and PARENT_NODE_NAME=?";

				String isLeaf = sqlQuery.queryForObject(sql, new Object[] { menuTree, strSubMenuID }, "String");

				Map<String, Object> mapNodeJson = new HashMap<String, Object>();

				mapNodeJson.put("id", strSubMenuID);
				mapNodeJson.put("nodeId", strSubMenuID);
				mapNodeJson.put("text", mapNode.get("TZ_MENU_MC_LANG").toString());
				mapNodeJson.put("menuYxState", mapNode.get("TZ_YXX").toString());
				mapNodeJson.put("comId", mapNode.get("TZ_COM_ID").toString());
				mapNodeJson.put("bigImgId", mapNode.get("TZ_MENU_LIMG").toString());
				mapNodeJson.put("smallImgId", mapNode.get("TZ_MENU_SIMG").toString());
				mapNodeJson.put("helpId", mapNode.get("TZ_MENU_NRID").toString());
				mapNodeJson.put("NodeType", "");
				mapNodeJson.put("operateNode", "");
				mapNodeJson.put("rootNode", "");
				mapNodeJson.put("comName", mapNode.get("TZ_COM_MC").toString());

				if ("Y".equals(isLeaf)) {

					mapNodeJson.put("leaf", true);

				} else {

					List<Map<String, Object>> listChildren = this.getMenuList(strSubMenuID, baseLanguage,
							targetLanguage);

					mapNodeJson.put("leaf", false);
					mapNodeJson.put("expanded", true);
					mapNodeJson.put("children", listChildren);

				}

				listRet.add(mapNodeJson);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listRet;
	}

	/**
	 * 拖拽跟新
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {

		String menuTree = getSysHardCodeVal.getMenuTreeName();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		String nodeId = jacksonUtil.getString("nodeId");
		String prevNodeId = jacksonUtil.getString("prevNodeId");
		String parentNodeId = jacksonUtil.getString("parentNodeId");

		if (null != nodeId && !"".equals(nodeId) && null != prevNodeId && !"".equals(prevNodeId) && null != parentNodeId
				&& !"".equals(parentNodeId)) {
			tzMenuTreeNodeServiceImpl.changeNode(menuTree, nodeId, prevNodeId, parentNodeId);
		}

		return "";
	}

	/**
	 * 同步多语言数据的方法
	 * 
	 * @param treeName
	 * @param nodeName
	 * @param bDataLanguage
	 * @param sDataLanguage
	 * @param tDataLanguage
	 * @return String
	 */
	public String synMultiLanguageData(String treeName, String nodeName, String bDataLanguage, String sDataLanguage,
			String tDataLanguage) {

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", "false");

		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {

			// 如果源数据语言代码与目标数据语言代码相同或者基础数据语言代码与目标数据语言代码相同，则直接返回
			if (sDataLanguage.equals(tDataLanguage) || bDataLanguage.equals(tDataLanguage)) {
				mapRet.replace("success", "true");
				return jacksonUtil.Map2json(mapRet);
			}

			// 读取当前机构对应功能菜单节点的开始编号和结束编号
			String sql = "select TREE_NODE_NUM,TREE_NODE_NUM_END from PSTREENODE where TREE_NAME=? AND TREE_NODE=?";
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { treeName, nodeName });

			int orgStartNum = Integer.parseInt(mapData.get("TREE_NODE_NUM").toString());
			int orgEndNum = Integer.parseInt(mapData.get("TREE_NODE_NUM_END").toString());

			if (orgStartNum <= 0 || orgEndNum <= 0) {
				return jacksonUtil.Map2json(mapRet);
			}

			if (bDataLanguage.equals(sDataLanguage)) {
				// 如果源数据语言代码与基础数据语言代码相同，则从PS_TZ_AQ_CDJD_TBL向PS_TZ_AQ_CDJD_LNG复制生成目标语言代码对应数据

				sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzCopyMenuTxtTbl2Lng");

				sqlQuery.update(sql, new Object[] { tDataLanguage, treeName, orgStartNum, orgEndNum, tDataLanguage });

			} else {
				// 如果源数据语言代码与基础数据语言代码不同，则从PS_TZ_AQ_CDJD_LNG向PS_TZ_AQ_CDJD_LNG复制生成目标语言代码对应数据
				sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzCopyMenuTxtLng2Tbl");

				sqlQuery.update(sql,
						new Object[] { tDataLanguage, treeName, sDataLanguage, orgStartNum, orgEndNum, tDataLanguage });
			}

			mapRet.replace("success", "true");

		} catch (TzSystemException e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}

}
