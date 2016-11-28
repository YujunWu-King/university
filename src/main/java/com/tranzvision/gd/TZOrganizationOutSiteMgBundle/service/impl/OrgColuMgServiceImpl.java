package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiColuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiColuT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author caoy
 * @version 创建时间：2016年8月17日 下午5:03:11 类说明 栏目管理
 */
@Service("com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl.OrgColuMgServiceImpl")
public class OrgColuMgServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PsTzSiteiColuTMapper psTzSiteiColuTMapper;

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
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("siteId")) {

				// 站点编号
				String siteId = jacksonUtil.getString("siteId");

				// 查询类型
				String typeFlag = jacksonUtil.getString("typeFlag");

				String sql;
				Map<String, Object> mapRet = new HashMap<String, Object>();
				if ("TREE".equals(typeFlag)) {

					// 一次性获取一整颗树的所有数据放入List
					sql = tzSQLObject.getSQLText("SQL.TZOutSiteMgBundle.TzSelectOutSiteColuList");

					List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { siteId });

					if (null == listData || listData.size() <= 0) {
						errMsg[0] = "1";
						errMsg[1] = "树不存在";
						return strRet;
					}

					// 遍历List 得到树形结构
					int flag = -1;
					String TZ_COLU_LEVEL = "";
					Map<String, Object> mapData = null;

					// 循环得到 根节点
					for (Object objData : listData) {
						mapData = (Map<String, Object>) objData;
						TZ_COLU_LEVEL = String.valueOf(mapData.get("TZ_COLU_LEVEL"));
						if (TZ_COLU_LEVEL.equals("0")) {
							flag = 0;
							break;
						}
					}

					if (flag == 0) {
						String coluId = String.valueOf(mapData.get("TZ_COLU_ID"));

						List<Map<String, Object>> listChildren = this.getMenuList(coluId, listData,siteId);

						Map<String, Object> mapRootJson = new HashMap<String, Object>();

						System.out.println("id=" + coluId);

						mapRootJson.put("id", coluId);
						mapRootJson.put("nodeId", coluId);
						mapRootJson.put("siteId", siteId);
						mapRootJson.put("coluUrl", mapData.get("TZ_OUT_URL").toString());
						mapRootJson.put("text", mapData.get("TZ_COLU_NAME").toString());
						mapRootJson.put("coluState", mapData.get("TZ_COLU_STATE").toString());
						mapRootJson.put("coluType", mapData.get("TZ_COLU_TYPE").toString());
						mapRootJson.put("coluPath", mapData.get("TZ_COLU_PATH").toString());
						mapRootJson.put("coluTempletId", mapData.get("TZ_CONT_TEMP").toString());
						mapRootJson.put("contentTypeId", mapData.get("TZ_ART_TYPE_ID").toString());
						mapRootJson.put("coluTempletName", mapData.get("TZ_TEMP_NAME").toString());
						mapRootJson.put("contentTypeName", mapData.get("TZ_ART_TYPE_NAME").toString());
						mapRootJson.put("coluAbout", mapData.get("TZ_COLU_ABOUT").toString());

						if (listData.size() > 1) {
							mapRootJson.put("leaf", false); // 有子节点
						} else {
							mapRootJson.put("leaf", true); // 没有子节点
						}
						mapRootJson.put("NodeType", "");
						mapRootJson.put("operateNode", "");
						mapRootJson.put("rootNode", "");
						mapRootJson.put("expanded", "true");
						mapRootJson.put("children", listChildren);
						mapRet.put("root", mapRootJson);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "根节点不存在";
						return strRet;
					}
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
	 * 判断该节点是否存在子节点
	 * 
	 * @param fcoluId
	 * @param listData
	 * @return false 不存在 true 存在
	 */
	private boolean isLeaf(String fcoluId, List<?> listData) {
		// boolean isLeaf = false;
		try {
			Map<String, Object> mapNode = null;
			String TZ_F_COLU_ID = "";
			for (Object objNode : listData) {
				mapNode = (Map<String, Object>) objNode;
				TZ_F_COLU_ID = mapNode.get("TZ_F_COLU_ID").toString();
				if (TZ_F_COLU_ID.equals(fcoluId)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 获取栏目的下级栏目列表
	 * 
	 * @param FcoluId
	 *            父栏目ID
	 * @param List<?>
	 *            listData
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMenuList(String FcoluId, List<Map<String, Object>> listData,String siteId) {

		List<Map<String, Object>> listRet = new ArrayList<Map<String, Object>>();

		try {
			Map<String, Object> mapNode = null;
			String TZ_F_COLU_ID = "";
			boolean isLeaf = false;
			Map<String, Object> mapNodeJson = null;
			String coluId = "";
			for (Object objNode : listData) {
				mapNode = (Map<String, Object>) objNode;
				TZ_F_COLU_ID = mapNode.get("TZ_F_COLU_ID").toString();

				if (TZ_F_COLU_ID.equals(FcoluId)) {

					coluId = String.valueOf(mapNode.get("TZ_COLU_ID"));
					mapNodeJson = new HashMap<String, Object>();
					mapNodeJson.put("id", coluId);
					mapNodeJson.put("nodeId", coluId);
					mapNodeJson.put("siteId", siteId);
					mapNodeJson.put("text", mapNode.get("TZ_COLU_NAME").toString());
					mapNodeJson.put("coluState", mapNode.get("TZ_COLU_STATE").toString());
					mapNodeJson.put("coluPath", mapNode.get("TZ_COLU_PATH").toString());
					mapNodeJson.put("coluTempletId", mapNode.get("TZ_CONT_TEMP").toString());
					mapNodeJson.put("contentTypeId", mapNode.get("TZ_ART_TYPE_ID").toString());
					mapNodeJson.put("coluTempletName", mapNode.get("TZ_TEMP_NAME").toString());
					mapNodeJson.put("contentTypeName", mapNode.get("TZ_ART_TYPE_NAME").toString());
					mapNodeJson.put("coluUrl", mapNode.get("TZ_OUT_URL").toString());
					mapNodeJson.put("coluType", mapNode.get("TZ_COLU_TYPE").toString());
					mapNodeJson.put("coluAbout", mapNode.get("TZ_COLU_ABOUT").toString());

					mapNodeJson.put("NodeType", "");
					mapNodeJson.put("operateNode", "");
					mapNodeJson.put("rootNode", "");

					isLeaf = this.isLeaf(coluId, listData);
					if (isLeaf) {
						mapNodeJson.put("leaf", false);
						mapNodeJson.put("expanded", true);
						mapNodeJson.put("children", this.getMenuList(coluId, listData,siteId));
					} else {
						mapNodeJson.put("leaf", true);
					}

					listRet.add(mapNodeJson);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listRet;
	}

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
		String siteId = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			// Date dateNow = new Date();
			int dataLength = actData.length;
			String sql = "";
			String TZ_F_COLU_ID = "";
			String TZ_COLU_LEVEL = "";
			String TZ_COLU_PATH = "";
			PsTzSiteiColuT psTzSiteiColuT = null;
			Map<String, Object> ThisNodeMap = null;
			String parentRealPath = "";
			File dir = null;
			// String thisColuId = "";
			String strForm = "";
			String synchronous = "";
			Map<String, Object> infoData = null;

			String coluId = "";
			String coluName = "";
			String coluPath = "";
			String coluState = "";
			String coluType = "";
			String coluTempletId = "";
			String contentTypeId = "";
			String coluUrl = "";
			String coluAbout = "";
			String NodeType = "";
			String operateNode = "";

			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				synchronous = jacksonUtil.getString("synchronous");

				// 判断是否是同步多语言数据的操作
				if ("true".equals(synchronous)) {
				} else {

					// 信息内容
					infoData = jacksonUtil.getMap("data");

					// 栏目节点编号;

					siteId = infoData.get("siteId").toString();
					// 栏目名称;
					coluName = infoData.get("coluName").toString();
					// 栏目路径
					coluPath = infoData.get("coluPath").toString();
					// 栏目状态
					coluState = infoData.get("coluState").toString();
					// 栏目类型
					coluType = infoData.get("coluType").toString();
					// 栏目模板ID
					coluTempletId = infoData.get("coluTempletId").toString();
					// 栏目类型ID;
					contentTypeId = infoData.get("contentTypeId").toString();
					// 栏目说明
					coluAbout = infoData.get("coluAbout").toString();
					// URL;
					coluUrl = infoData.get("coluUrl").toString();
					// 插入同级节点还是子节点,Y:表示同级节点，'N'表示子节点;
					NodeType = infoData.get("NodeType").toString();

					// 插入同级节点或子节点是在哪个节点上操作的;
					operateNode = infoData.get("operateNode").toString();

					if ((coluName == null || "".equals(coluName)) || (coluState == null || "".equals(coluState))) {
						return "";
					}
					
					
					System.out.println("operateNode:"+operateNode);
					// 修改当前节点
					if (operateNode == null || "".equals(operateNode)) {
						operateNode = infoData.get("coluId").toString();
					}
					
					
					System.out.println("siteId:"+siteId);
					System.out.println("operateNode:"+operateNode);
					

					// 找到该节点的父节点以及级别
					sql = "select ifnull(TZ_F_COLU_ID,\"\") TZ_F_COLU_ID,TZ_COLU_LEVEL from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID=? and TZ_COLU_ID=?";
					ThisNodeMap = sqlQuery.queryForMap(sql, new Object[] { siteId, operateNode });

					if (ThisNodeMap != null && ThisNodeMap.get("TZ_F_COLU_ID") != null) {
						TZ_F_COLU_ID = (ThisNodeMap.get("TZ_F_COLU_ID").toString());
					}
					TZ_COLU_LEVEL = (ThisNodeMap.get("TZ_COLU_LEVEL").toString());

					// 找到跟目录路径
					sql = "select TZ_COLU_PATH from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID=? and TZ_COLU_LEVEL=?";
					ThisNodeMap = sqlQuery.queryForMap(sql, new Object[] { siteId, 0 });

					if (ThisNodeMap != null) {
						TZ_COLU_PATH = (ThisNodeMap.get("TZ_COLU_PATH").toString());
					}

					// boolean boolRst = false;
					switch (NodeType) {
					// 添加同级节点;
					case "Y":
						coluId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_COLU_T", "TZ_COLU_ID"));
						psTzSiteiColuT = new PsTzSiteiColuT();
						psTzSiteiColuT.setTzSiteiId(siteId);
						psTzSiteiColuT.setTzColuId(coluId);
						psTzSiteiColuT.setTzColuName(coluName);
						psTzSiteiColuT.setTzColuPath(coluPath);
						psTzSiteiColuT.setTzColuState(coluState);
						psTzSiteiColuT.setTzColuLevel(new Integer(TZ_COLU_LEVEL));
						psTzSiteiColuT.setTzColuType(coluType);
						psTzSiteiColuT.setTzColuAbout(coluAbout);

						if (coluType.equals("D")) {
							psTzSiteiColuT.setTzContType("A");
						} else {
							psTzSiteiColuT.setTzContType("");
						}
						psTzSiteiColuT.setTzFColuId(TZ_F_COLU_ID);

						psTzSiteiColuT.setTzContTemp(coluTempletId);
						// psTzSiteiColuT.setTzContTemp(coluTempletId);
						psTzSiteiColuT.setTzArtTypeId(contentTypeId);
						psTzSiteiColuT.setTzOutUrl(coluUrl);
						psTzSiteiColuTMapper.insertSelective(psTzSiteiColuT);
						// 增加菜单,同级的菜单
						if (coluPath != null && !coluPath.equals("")) {
							if (!coluPath.startsWith("/")) {
								coluPath = "/" + coluPath;
							}
							parentRealPath = request.getServletContext().getRealPath(TZ_COLU_PATH + coluPath);
							dir = new File(parentRealPath);
							if (!dir.exists()) {
								dir.mkdirs();
							}
						}
						break;
					// 添加子节点;
					case "N":
						coluId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_COLU_T", "TZ_COLU_ID"));
						psTzSiteiColuT = new PsTzSiteiColuT();
						psTzSiteiColuT.setTzSiteiId(siteId);
						psTzSiteiColuT.setTzColuId(coluId);
						psTzSiteiColuT.setTzColuName(coluName);
						psTzSiteiColuT.setTzColuPath(coluPath);
						psTzSiteiColuT.setTzColuState(coluState);
						psTzSiteiColuT.setTzColuLevel(new Integer(TZ_COLU_LEVEL) + 1);
						psTzSiteiColuT.setTzColuType(coluType);
						psTzSiteiColuT.setTzColuAbout(coluAbout);
						if (coluType.equals("D")) {
							psTzSiteiColuT.setTzContType("A");
						} else {
							psTzSiteiColuT.setTzContType("");
						}
						psTzSiteiColuT.setTzFColuId(operateNode);
						psTzSiteiColuT.setTzContTemp(coluTempletId);
						// psTzSiteiColuT.setTzContTemp(coluTempletId);
						psTzSiteiColuT.setTzArtTypeId(contentTypeId);
						psTzSiteiColuT.setTzOutUrl(coluUrl);
						psTzSiteiColuTMapper.insertSelective(psTzSiteiColuT);

						if (coluPath != null && !coluPath.equals("")) {
							if (!coluPath.startsWith("/")) {
								coluPath = "/" + coluPath;
							}
							parentRealPath = request.getServletContext().getRealPath(TZ_COLU_PATH + coluPath);
							dir = new File(parentRealPath);
							if (!dir.exists()) {
								dir.mkdirs();
							}
						}
						break;

					// 修改当前结点
					default:
						psTzSiteiColuT = new PsTzSiteiColuT();
						psTzSiteiColuT.setTzSiteiId(siteId);
						psTzSiteiColuT.setTzColuId(operateNode);
						psTzSiteiColuT.setTzColuName(coluName);
						// psTzSiteiColuT.setTzColuPath(coluPath); 栏目path不能修改
						psTzSiteiColuT.setTzColuState(coluState);
						// psTzSiteiColuT.setTzColuLevel(new
						// Integer(TZ_COLU_LEVEL));
						psTzSiteiColuT.setTzColuType(coluType);
						if (coluType.equals("D")) {
							psTzSiteiColuT.setTzContType("A");
						} else {
							psTzSiteiColuT.setTzContType("");
						}
						// psTzSiteiColuT.setTzFColuId(TZ_F_COLU_ID);
						psTzSiteiColuT.setTzContTemp(coluTempletId);
						psTzSiteiColuT.setTzColuAbout(coluAbout);
						// psTzSiteiColuT.setTzContTemp(coluTempletId);
						psTzSiteiColuT.setTzArtTypeId(contentTypeId);
						psTzSiteiColuT.setTzOutUrl(coluUrl);
						psTzSiteiColuTMapper.updateByPrimaryKeySelective(psTzSiteiColuT);
						break;
					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("success", "true");
					mapRet.put("newColuID", coluId);
					strRet = jacksonUtil.Map2json(mapRet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString() + "siteId=" + siteId;
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
			int dataLength = actData.length;
			String strForm = "";
			Map<String, Object> infoData = null;
			String coluId = "";
			String sitrId = "";
			String sql = "";
			List<Map<String, Object>> listData = null;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				infoData = jacksonUtil.getMap("data");

				coluId = infoData.get("coluId").toString();
				sitrId = infoData.get("siteId").toString();

				// 全表查询
				sql = "select TZ_COLU_ID,ifnull(TZ_F_COLU_ID,\"\") TZ_F_COLU_ID from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID = ?";

				listData = sqlQuery.queryForList(sql, new Object[] { sitrId });

				sql = "delete from PS_TZ_SITEI_COLU_T where  TZ_SITEI_ID = ? and TZ_COLU_ID=?";
				sqlQuery.update(sql, new Object[] { sitrId, coluId });
				this.deleteChild(coluId, listData, sitrId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}
		return strRet;
	}

	private void deleteChild(String FcoluId, List<Map<String, Object>> listData, String sitrId) {
		Map<String, Object> mapNode = null;
		String TZ_F_COLU_ID = "";
		boolean isLeaf = false;
		String sql = "";
		String coluId = "";
		for (Object objNode : listData) {
			mapNode = (Map<String, Object>) objNode;
			TZ_F_COLU_ID = mapNode.get("TZ_F_COLU_ID").toString();

			if (TZ_F_COLU_ID.equals(FcoluId)) {
				coluId = String.valueOf(mapNode.get("TZ_COLU_ID"));
				sql = "delete from PS_TZ_SITEI_COLU_T where  TZ_SITEI_ID = ? and TZ_COLU_ID=?";
				sqlQuery.update(sql, new Object[] { sitrId, coluId });
				isLeaf = this.isLeaf(coluId, listData);
				if (isLeaf) {
					this.deleteChild(coluId, listData, sitrId);
				}
			}
		}

	}
}
