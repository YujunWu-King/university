package com.tranzvision.gd.TZScoreModeManagementBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;


@Service("com.tranzvision.gd.TZScoreModeManagementBundle.service.impl.TzTreeManagerServiceImpl")
public class TzTreeManagerServiceImpl extends FrameworkImpl{

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private TZGDObject tzSQLObject;
	
	/**
	 * 获取子节点信息
	 * @param orgId-机构编号
	 * @param treeName - 树名称
	 * @param itemId - 树节点
	 * @param root - 根节点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getChildrenList(String orgId, String treeName, String itemId) {

		List<Map<String, Object>> listRet = new ArrayList<Map<String, Object>>();

		try {
			String sql = tzSQLObject.getSQLText("SQL.TZScoreModelBundle.TzTreeNodeChildrenList");

			List<?> listData = jdbcTemplate.queryForList(sql, new Object[] { orgId, treeName, itemId });

			for (Object objNode : listData) {

				Map<String, Object> mapNode = (Map<String, Object>) objNode;

				String subItemId = mapNode.get("TZ_SCORE_ITEM_ID").toString();
				String subItemDesc = mapNode.get("DESCR").toString();

				// 判断该成绩项是否是叶子菜单
				sql = "select if(count(1)=0,'Y','N') from PSTREENODE where TREE_NAME = ? and PARENT_NODE_NAME=?";

				String isLeaf = jdbcTemplate.queryForObject(sql, new Object[] { treeName, subItemId }, "String");

				Map<String, Object> mapNodeJson = new HashMap<String, Object>();

				mapNodeJson.put("id", subItemId);
				mapNodeJson.put("itemId", subItemId);
				mapNodeJson.put("isRoot", "N");
				mapNodeJson.put("text", subItemId+" - "+subItemDesc);

				if ("Y".equals(isLeaf)) {
					mapNodeJson.put("leaf", true);
				} else {

					List<Map<String, Object>> listChildren = this.getChildrenList( orgId, treeName, subItemId);

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
			
			//查询根节点
			String sql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? AND TREE_LEVEL_NUM='1'";
			String root = jdbcTemplate.queryForObject(sql, new Object[]{ treeName }, "String");
			
			if(!"".equals(root) && root != null){
				//sql = tzSQLObject.getSQLText("SQL.TZScoreModelBundle.TzScoreModelTreeNodeInfo");

				sql = "SELECT DESCR FROM PS_TZ_MODAL_DT_TBL WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
				Map<String, Object> mapRoot = jdbcTemplate.queryForMap(sql, new Object[] { orgId, treeName, root });
				
				if(null==mapRoot){
					return strRtn;
				}

				List<?> listChildren = this.getChildrenList( orgId, treeName, root);

				Map<String, Object> mapRootJson = new HashMap<String, Object>();

				mapRootJson.put("id", root);
				mapRootJson.put("itemId", root);
				mapRootJson.put("isRoot", "Y");
				mapRootJson.put("expanded", "true");
				mapRootJson.put("text", root+" - "+mapRoot.get("DESCR").toString());
				mapRootJson.put("leaf", "false");
				mapRootJson.put("children", listChildren);

				mapRet.put("root", mapRootJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}
}
