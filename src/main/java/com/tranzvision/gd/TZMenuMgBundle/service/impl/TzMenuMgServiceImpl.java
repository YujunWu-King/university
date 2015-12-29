/**
 * 
 */
package com.tranzvision.gd.TZMenuMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.tree.TreeManager;

/**
 * 安全管理-功能菜单管理列表类，原PS：TZ_GD_GNCDGL_PKG:TZ_GD_GNCDLB_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-16
 */
@Service("com.tranzvision.gd.TZMenuMgBundle.service.impl.TzMenuMgServiceImpl")
public class TzMenuMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzMenuTreeNodeServiceImpl tzMenuTreeNodeServiceImpl;

	/**
	 * 获取功能菜单信息列表
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
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_MENU_NUM", "TZ_MENU_MC" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				int listcount = list.size();
				for (int i = 0; i < listcount; i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("orgId", rowList[0]);
					mapList.put("orgName", rowList[1]);

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

	/**
	 * 删除功能菜单信息
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
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 值转换集合id
				String menuId = jacksonUtil.getString("orgId");

				if (menuId != null && !"".equals(menuId)) {

					String menuTree = getSysHardCodeVal.getMenuTreeName();

					String sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzMenuSelectDelNum");

					Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { menuTree, menuId });

					int treeNodeNum = Integer.parseInt(mapData.get("TREE_NODE_NUM").toString());
					int treeNodeNumEnd = Integer.parseInt(mapData.get("TREE_NODE_NUM_END").toString());

					tzMenuTreeNodeServiceImpl.deleteNode(menuTree, treeNodeNum, treeNodeNumEnd);

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
	 * 验证添加的树节点是否已经被使用
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String menuId = jacksonUtil.getString("menuId");
		String actType = jacksonUtil.getString("actType");

		String menuTree = getSysHardCodeVal.getMenuTreeName();

		String isExist = "";
		String sql = "SELECT 'Y' FROM PSTREENODE WHERE TREE_NAME=? and UPPER(TREE_NODE)=UPPER(?)";
		isExist = sqlQuery.queryForObject(sql, new Object[] { menuTree, menuId }, "String");

		// 不能使用机构id
		if (null == isExist || "".equals(isExist)) {
			sql = "SELECT 'Y' FROM PS_TZ_JG_BASE_T WHERE UPPER(TZ_JG_ID)=UPPER(?)";
			isExist = sqlQuery.queryForObject(sql, new Object[] { menuId }, "String");
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		if (null == isExist || "".equals(isExist) || !"add".equals(actType)) {
			mapRet.put("success", "true");
		} else {
			mapRet.put("success", "false");
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * 刷新菜单
	 */
	@Override
	public String tzOther(String operateType, String comParams, String[] errMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		try {
			String menuTree = getSysHardCodeVal.getMenuTreeName();
			if ("REFRESHMENU".equals(operateType)) {
				TreeManager treeManager = new TreeManager();
				treeManager.setTreeName(menuTree);
				treeManager.setSqlQuery(sqlQuery);
				treeManager.setTZGDObject(tzSQLObject);
				treeManager.setGetSysHardCodeVal(getSysHardCodeVal);
				if (treeManager.openTree("")) {

					treeManager.save();

					treeManager.closeTree();

				}
				mapRet.put("success", "true");
				mapRet.put("errorMsg", "");
			} else {
				mapRet.put("success", "false");
				mapRet.put("errorMsg", "未找到对应的处理类");
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			mapRet.put("success", "true");
			mapRet.put("errorMsg", e.toString());
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}

}
