package com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl;

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
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao.PsTzArtTypeTMapper;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao.PsTzContFldefTMapper;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzArtTypeT;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzContFldefT;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzContFldefTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author zhangbb
 * @version 创建时间：2016年8月17日 下午16:28:30 类说明 内容发布管理
 */
@Service("com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl.ArtListServiceImpl")
public class ArtListServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private TZGDObject tzSQLObject;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzArtTypeTMapper psTzArtTypeTMapper;
	
	@Autowired
	private PsTzContFldefTMapper PsTzContFldefTMapper;
	
	/* 查询类型类型列表 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_MAX_ZD_SEQ", "DESC" }, { "TZ_ART_NEWS_DT", "DESC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_SITE_ID", "TZ_COLU_ID", "TZ_ART_ID", "TZ_ART_TITLE", "TZ_ART_NEWS_DT",
					"TZ_REALNAME", "TZ_ART_PUB_STATE", "TZ_MAX_ZD_SEQ", "TZ_PAGE_REFCODE" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr,strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("siteId", rowList[0]);
					mapList.put("columnId", rowList[1]);
					mapList.put("articleId", rowList[2]);
					mapList.put("articleTitle", rowList[3]);
					mapList.put("releaseTime", rowList[4]);
					mapList.put("lastUpdate", rowList[5]);
					mapList.put("releaseOrUndo", rowList[6]);
					mapList.put("topOrUndo", rowList[7]);
					mapList.put("classId", rowList[8]);

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
	
	/* 获取内容栏目树类型定义*/
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 获取登录的机构;
			String strJgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			jacksonUtil.json2Map(strParams);

			// 查询类型
			String typeFlag = jacksonUtil.getString("typeFlag");

			String sql;
			Map<String, Object> mapRet = new HashMap<String, Object>();
			if ("SITE".equals(typeFlag)) {
				//站点;

				ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
				mapRet.put("TransList", arraylist);
				String getSiteSQL = "SELECT TZ_SITEI_ID,TZ_SITEI_NAME,TZ_SITEI_DESCR FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ENABLE='Y' and TZ_JG_ID=?";

				List<Map<String, Object>> list = sqlQuery.queryForList(getSiteSQL, new Object[] { strJgid });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> jsonMap = new HashMap<>();
						jsonMap.put("TValue", list.get(i).get("TZ_SITEI_ID"));
						jsonMap.put("TSDesc", list.get(i).get("TZ_SITEI_NAME"));
						jsonMap.put("TLDesc", list.get(i).get("TZ_SITEI_DESCR"));

						arraylist.add(jsonMap);
					}
					mapRet.replace("TransList", arraylist);
				}
				strRet = jacksonUtil.Map2json(mapRet);
			}else if ("TREE".equals(typeFlag)) {
				// 站点编号
				String siteId = jacksonUtil.getString("siteid");
				// 一次性获取一整颗树的所有数据放入List
				sql = tzSQLObject.getSQLText("SQL.TZOutSiteMgBundle.TzSelectOutSiteColuList");

				List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { siteId });

				if (null == listData || listData.size() <= 0) {
					/*
					errMsg[0] = "1";
					errMsg[1] = "树不存在";
					*/
					Map<String, Object> mapRootJson = new HashMap<String, Object>();
					mapRootJson.put("id", "NAN");
					mapRootJson.put("nodeId", "NAN");
					mapRootJson.put("siteId", "");
					mapRootJson.put("text", "");
					mapRootJson.put("NodeType", "");
					mapRootJson.put("operateNode", "");
					mapRootJson.put("rootNode", "");
					mapRootJson.put("expanded", "true");
					mapRootJson.put("children", "{}");
					mapRet.put("root", mapRootJson);
				}else{

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
	
						List<Map<String, Object>> listChildren = this.getMenuList(coluId, listData);
	
						Map<String, Object> mapRootJson = new HashMap<String, Object>();
	
						// text : root.text,
						// //nodeId : root.nodeId,
						// id : root.coluId,
						// coluState : root.coluState,
						// coluPath : root.coluPath,
						// coluTempletId : root.coluTempletId,
						// contentTypeId : root.contentTypeId,
						// coluTempletName : root.coluTempletName,
						// contentTypeName : root.contentTypeName,
						// NodeType : root.NodeType,
						// operateNode : root.operateNode,
						// rootNode : config.coluId,
						// expanded : root.expanded,
						// children : me.getChartNavItems(items)
						System.out.println("id=" + coluId);
	
						mapRootJson.put("id", coluId);
						mapRootJson.put("nodeId", coluId);
						mapRootJson.put("siteId", siteId);
						mapRootJson.put("coluUrl", mapData.get("TZ_OUT_URL").toString());
						mapRootJson.put("text", mapData.get("TZ_COLU_NAME").toString());
						mapRootJson.put("coluState", mapData.get("TZ_COLU_STATE").toString());
						mapRootJson.put("coluType", mapData.get("TZ_COLU_TYPE").toString());
						mapRootJson.put("coluPath", mapData.get("TZ_COLU_PATH").toString());
						mapRootJson.put("coluTempletId", mapData.get("TZ_TEMP_ID").toString());
						mapRootJson.put("contentTypeId", mapData.get("TZ_ART_TYPE_ID").toString());
						mapRootJson.put("coluTempletName", mapData.get("TZ_TEMP_NAME").toString());
						mapRootJson.put("contentTypeName", mapData.get("TZ_ART_TYPE_NAME").toString());
	
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
	 * 获取菜单的下级菜单列表
	 * 
	 * @param strMenuID
	 * @param baseLanguage
	 * @param targetLanguage
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMenuList(String FcoluId, List<Map<String, Object>> listData) {

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
					mapNodeJson.put("text", mapNode.get("TZ_COLU_NAME").toString());
					mapNodeJson.put("coluState", mapNode.get("TZ_COLU_STATE").toString());
					mapNodeJson.put("coluPath", mapNode.get("TZ_COLU_PATH").toString());
					mapNodeJson.put("coluTempletId", mapNode.get("TZ_TEMP_ID").toString());
					mapNodeJson.put("contentTypeId", mapNode.get("TZ_ART_TYPE_ID").toString());
					mapNodeJson.put("coluTempletName", mapNode.get("TZ_TEMP_NAME").toString());
					mapNodeJson.put("contentTypeName", mapNode.get("TZ_ART_TYPE_NAME").toString());
					mapNodeJson.put("coluUrl", mapNode.get("TZ_OUT_URL").toString());
					mapNodeJson.put("coluType", mapNode.get("TZ_COLU_TYPE").toString());

					mapNodeJson.put("NodeType", "");
					mapNodeJson.put("operateNode", "");
					mapNodeJson.put("rootNode", "");

					isLeaf = this.isLeaf(coluId, listData);
					if (isLeaf) {
						mapNodeJson.put("leaf", false);
						mapNodeJson.put("expanded", true);
						mapNodeJson.put("children", this.getMenuList(coluId, listData));
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
}
