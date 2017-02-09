package com.tranzvision.gd.TZScoreModeManagementBundle.service.impl;

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
import com.tranzvision.gd.TZMenuMgBundle.service.impl.TzMenuTreeNodeServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
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
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal; 
	
	@Autowired
	private TzMenuTreeNodeServiceImpl tzMenuTreeNodeServiceImpl;
	
	
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

			List<?> listData = jdbcTemplate.queryForList(sql, new Object[] { orgId, treeName, itemId, treeName });

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
	
	/***
	 * 获取树节点信息
	 */
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
	
	
	
	/**
	 * 删除指定树节点
	 * @param treeName
	 * @param treeNodeNum
	 * @param treeNodeNumEnd
	 */
	private void deleteNode(String orgId, String treeName, int treeNodeNum, int treeNodeNumEnd) {
		try {
			String dtFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat format = new SimpleDateFormat(dtFormat);
			Date effdt = format.parse("2015-11-11");

			// 查找同级兄弟节点的上节点
			String sql = "select PARENT_NODE_NAME,TREE_LEVEL_NUM from PSTREENODE where TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM = ?  and TREE_NODE_NUM_END = ? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))=''";
			Map<String, Object> mapNode = jdbcTemplate.queryForMap(sql,
					new Object[] { treeName, effdt, treeNodeNum, treeNodeNumEnd });

			String parentNode = mapNode.get("PARENT_NODE_NAME").toString();
			short levelNum = Short.parseShort(mapNode.get("TREE_LEVEL_NUM").toString());

			sql = "select TREE_NODE,TREE_NODE_NUM from PSTREENODE where TREE_NAME=? and EFFDT<=? and PARENT_NODE_NAME = ? AND TREE_LEVEL_NUM=? AND TREE_NODE_NUM_END < ? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))='' order by TREE_NODE_NUM_END desc";
			Map<String, Object> mapPrevNode = jdbcTemplate.queryForMap(sql,
					new Object[] { treeName, effdt, parentNode, levelNum, treeNodeNum });

			// 删除树节点详细信息表中的记录
			sql = "delete from PS_TZ_MODAL_DT_TBL where TZ_JG_ID=? and TREE_NAME=? and exists (select TREE_NODE from PSTREENODE A where A.TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM>=? and TREE_NODE_NUM_END<=? and A.TREE_NODE=TZ_SCORE_ITEM_ID)";
			jdbcTemplate.update(sql, new Object[] { orgId, treeName, treeName, effdt, treeNodeNum, treeNodeNumEnd });

			// 删除成绩项下拉选项
			sql = "delete from PS_TZ_ZJCJXXZX_T where TZ_JG_ID=? and TREE_NAME=? and exists (select TREE_NODE from PSTREENODE A where A.TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM>=? and TREE_NODE_NUM_END<=? and A.TREE_NODE=TZ_SCORE_ITEM_ID)";
			jdbcTemplate.update(sql, new Object[] { orgId, treeName, treeName, effdt, treeNodeNum, treeNodeNumEnd });

						
			// 删除树节点
			sql = "delete from PSTREENODE where ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))='' and TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM >= ?  and TREE_NODE_NUM_END <= ?";
			jdbcTemplate.update(sql, new Object[] { treeName, effdt, treeNodeNum, treeNodeNumEnd });

			// 存在同级兄弟节点的上节点,把上个兄弟节点最大结束序号改为当前删除的节点的最大结束序号
			if (null != mapPrevNode) {
				String prevNodeName = mapPrevNode.get("TREE_NODE").toString();
				int prevNodeNum = Integer.parseInt(mapPrevNode.get("TREE_NODE_NUM").toString());
				tzMenuTreeNodeServiceImpl.updateNodeNum(treeName, prevNodeName, prevNodeNum, treeNodeNumEnd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 删除树节点
	 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try{
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String orgId = jacksonUtil.getString("orgId");
				String treeName = jacksonUtil.getString("treeName");
				String itemId = jacksonUtil.getString("itemId");
				
				String sql = "select TREE_NODE_NUM,TREE_NODE_NUM_END from PSTREENODE A where A.TREE_NAME=? and TREE_NODE=?";

				Map<String, Object> mapNode = jdbcTemplate.queryForMap(sql, new Object[] { treeName, itemId });

				int treeNodeNum = Integer.parseInt(mapNode.get("TREE_NODE_NUM").toString());
				int treeNodeNumEnd = Integer.parseInt(mapNode.get("TREE_NODE_NUM_END").toString());

				this.deleteNode(orgId, treeName, treeNodeNum, treeNodeNumEnd);
			}
			
			mapRet.replace("result", "success");
			strRet = jacksonUtil.Map2json(mapRet);
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
				case "tzTreeNodeSort":
					//保存批次面试预约安排 
					strRet = this.tzTreeNodeSort(strParams,errorMsg);
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
	 * 树节点排序
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzTreeNodeSort(String strParams, String[] errorMsg){
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String treeName = jacksonUtil.getString("treeName");
			String parentId = jacksonUtil.getString("parentId");
			String dragitemId = jacksonUtil.getString("dragitemId");
			String dropitemId = jacksonUtil.getString("dropitemId");
			String dropPosition = jacksonUtil.getString("dropPosition");
			String preItemId = "";
			
			if("after".equals(dropPosition)){
				preItemId = dropitemId;
			}else if("before".equals(dropPosition)){
				//查找前一个兄弟节点 
				String sql = tzSQLObject.getSQLText("SQL.TZScoreModelBundle.TzPreTreeNode");
				preItemId = jdbcTemplate.queryForObject(sql, new Object[]{ treeName, dropitemId }, "String");
			}
			
			this.changeNode(treeName, dragitemId, preItemId, parentId);
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		return strRet;
	}
	
	
	
	public void changeNode(String treeName, String oldNode, String preNode, String patentNode) {

		int seqNum = getSeqNum.getSeqNum("TZ_TREENODE_TMP", "TZ_SEQNUM");
		try {

			String dtFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat format = new SimpleDateFormat(dtFormat);
			Date effdt = format.parse("2015-11-11");

			String sql = "select TREE_NODE_NUM,TREE_NODE_NUM_END,TREE_LEVEL_NUM from PSTREENODE where TREE_NAME=? and TREE_NODE=? and EFFDT<=? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))=''";
			Map<String, Object> mapOldNode = jdbcTemplate.queryForMap(sql, new Object[] { treeName, oldNode, effdt });

			int oldTreeNodeNum = Integer.parseInt(mapOldNode.get("TREE_NODE_NUM").toString());
			int oldTreeNodeNumEnd = Integer.parseInt(mapOldNode.get("TREE_NODE_NUM_END").toString());
			// short oldTreeLevelNum =
			// Short.parseShort(mapOldNode.get("TREE_LEVEL_NUM").toString());

			if (mapOldNode != null) {

				sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzInsertTreeNodeTmp");
				jdbcTemplate.update(sql, new Object[] { seqNum, treeName, effdt, oldTreeNodeNum, oldTreeNodeNumEnd });

				// 查找同级兄弟节点的上节点
				sql = "select PARENT_NODE_NAME,TREE_LEVEL_NUM from PSTREENODE where ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))='' and TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM = ?  and TREE_NODE_NUM_END = ?";
				Map<String, Object> mapNode1 = jdbcTemplate.queryForMap(sql,
						new Object[] { treeName, effdt, oldTreeNodeNum, oldTreeNodeNumEnd });

				String y_pNode = mapNode1.get("PARENT_NODE_NAME").toString();
				short levelNum = Short.parseShort(mapNode1.get("TREE_LEVEL_NUM").toString());

				sql = "select TREE_NODE, TREE_NODE_NUM from PSTREENODE where ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))='' and TREE_NAME=? and EFFDT<=? and PARENT_NODE_NAME = ? and TREE_LEVEL_NUM=? and TREE_NODE_NUM_END < ? order by TREE_NODE_NUM_END desc";
				Map<String, Object> mapNode2 = jdbcTemplate.queryForMap(sql,
						new Object[] { treeName, effdt, y_pNode, levelNum, oldTreeNodeNum });

				
				sql = "delete from PSTREENODE where ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))='' and TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM >= ?  and TREE_NODE_NUM_END <= ?";
				jdbcTemplate.update(sql, new Object[] { treeName, effdt, oldTreeNodeNum, oldTreeNodeNumEnd });

				// 存在同级兄弟节点的上节点,把上个兄弟节点最大结束序号改为当前删除的节点的最大结束序号
				if (mapNode2 != null) {
					
					String prevNodeName = mapNode2.get("TREE_NODE").toString();
					int prevNodeNum = Integer.parseInt(mapNode2.get("TREE_NODE_NUM").toString());
					
					tzMenuTreeNodeServiceImpl.updateNodeNum(treeName, prevNodeName, prevNodeNum, oldTreeNodeNumEnd);
				}

			}

			if (preNode == null || "".equals(preNode)) {
				/****** 表示插入子节点 *****/
				tzMenuTreeNodeServiceImpl.changeNodeToParent(seqNum, treeName, oldNode, patentNode);
			} else {
				/****** 表示插入兄弟节点 *****/
				tzMenuTreeNodeServiceImpl.changeNodeToBrother(seqNum, treeName, oldNode, preNode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
