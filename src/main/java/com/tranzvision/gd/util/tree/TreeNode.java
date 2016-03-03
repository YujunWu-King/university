/**
 * 
 */
package com.tranzvision.gd.util.tree;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.util.base.Arith;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 树管理器，原PS：TZTREEMANAGER:TreeNode
 * 
 * @author SHIHUA
 * @since 2015-11-16
 */
public class TreeNode {

	private SqlQuery sqlQuery;

	private TZGDObject tzSQLObject;

	private GetSysHardCodeVal getSysHardCodeVal;

	private List<TreeNode> listChildNodes;

	private List<TreeNode> listDeleteChildNodes;

	private Map<String, Object> mapNode;

	private boolean m_NotSavedFlag;

	private int m_TotalChildNum;

	/**
	 * 记录当前节点的属性在save方法被调用时是否需要执行实际保存动作的变量
	 */
	private boolean mNotSavedFlag;

	public void setSqlQuery(SqlQuery sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public void setTZGDObject(TZGDObject tzSQLObject) {
		this.tzSQLObject = tzSQLObject;
	}

	public void setGetSysHardCodeVal(GetSysHardCodeVal getSysHardCodeVal) {
		this.getSysHardCodeVal = getSysHardCodeVal;
	}

	/**
	 * 构造函数，初始化树节点参数值
	 * 
	 * @param setId
	 * @param setCNTRValue
	 * @param treeName
	 * @param effDT
	 * @param treeNode
	 * @param parentNode
	 */
	public TreeNode(String setId, String setCNTRValue, String treeName, Date effDT, String treeNode,
			String parentNode) {

		this.m_NotSavedFlag = true;

		this.mapNode = new HashMap<String, Object>();

		this.setSetId(setId);
		this.setSetCntrlValue(setCNTRValue);
		this.setTreeName(treeName);
		this.setEffdt(effDT);
		this.setTreeNode(treeNode);
		this.setParentNodeName(parentNode);

		this.setTreeBranch("");
		this.setTreeNodeType("G");
		this.setOldTreeNodeNum("N");
		this.setNodecolImage("");
		this.setNodeexpImage("");

		this.m_TotalChildNum = 0;

		listChildNodes = new ArrayList<TreeNode>();

	}

	/**
	 * 保存树节点的方法
	 * 
	 * @param refreshNodeNum
	 * @param level
	 */
	public void save(boolean refreshNodeNum, int level) {

		try {

			if (refreshNodeNum) {
				this.refreshNodeNum(1, 2000000000, 0, true);
			}

			if (m_NotSavedFlag) {
				String sql = "SELECT 'X' FROM PSTREENODE WHERE ltrim(rtrim(SETID))=? AND TREE_NAME=? AND TREE_NODE=?";
				String nodeExsitsFlag = sqlQuery.queryForObject(sql,
						new Object[] { this.getSetId(), this.getTreeName(), this.getTreeNode() }, "String");

				if ("X".equals(nodeExsitsFlag)) {
					sql = tzSQLObject.getSQLText("SQL.TZUtilTree.TzTreeNodeUpdate");

					sqlQuery.update(sql,
							new Object[] { this.getSetId(), this.getSetCntrlValue(), this.getTreeName(),
									this.getEffdt(), this.getTreeNodeNum(), this.getTreeNode(), this.getTreeBranch(),
									this.getTreeNodeNumEnd(), this.getTreeLevelNum(), this.getTreeNodeType(),
									this.getParentNodeNum(), this.getParentNodeName(), this.getOldTreeNodeNum(),
									this.getNodecolImage(), this.getNodeexpImage(), this.getSetId(), this.getTreeName(),
									this.getTreeNode() });

				} else {

					sql = tzSQLObject.getSQLText("SQL.TZUtilTree.TzTreeNodeInsert");

					sqlQuery.update(sql,
							new Object[] { this.getSetId(), this.getSetCntrlValue(), this.getTreeName(),
									this.getEffdt(), this.getTreeNodeNum(), this.getTreeNode(), this.getTreeBranch(),
									this.getTreeNodeNumEnd(), this.getTreeLevelNum(), this.getTreeNodeType(),
									this.getParentNodeNum(), this.getParentNodeName(), this.getOldTreeNodeNum(),
									this.getNodecolImage(), this.getNodeexpImage() });

				}

			}

			TreeNode tmpNode;
			if (null != listDeleteChildNodes) {
				while (listDeleteChildNodes.size() >= 1) {
					tmpNode = listDeleteChildNodes.get(0);
					tmpNode.deleteNodeFromDB();
					listDeleteChildNodes.remove(0);
				}
			}

			if (null != listChildNodes) {
				int listnum = listChildNodes.size();
				for (int i = 0; i < listnum; i++) {
					tmpNode = listChildNodes.get(i);
					tmpNode.save(false, level + 1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断树节点是否已保存的方法
	 * 
	 * @return boolean
	 */
	public boolean isNotSaved() {

		if (mNotSavedFlag) {
			return true;
		}

		int listnum = listChildNodes.size();
		for (int i = 0; i < listnum; i++) {
			if (listChildNodes.get(i).isNotSaved() == true) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 重置未保存状态的方法
	 */
	public void resetSaveFlag() {

		mNotSavedFlag = false;

		int listnum = listChildNodes.size();
		for (int i = 0; i < listnum; i++) {
			listChildNodes.get(i).resetSaveFlag();
		}

	}

	/**
	 * 加载树节点信息的方法
	 * 
	 * @param rootNodeFlag
	 */
	@SuppressWarnings("unchecked")
	public void load(boolean rootNodeFlag) {
		try {
			String sql = tzSQLObject.getSQLText("SQL.TZUtilTree.TzSelectNodeInfo");

			String setId = this.getSetId();
			String treeName = this.getTreeName();
			String treeNode = this.getTreeNode();

			m_NotSavedFlag = false;

			mapNode.clear();
			mapNode = sqlQuery.queryForMap(sql, new Object[] { setId, treeName, treeNode });

			if (rootNodeFlag) {

				if (this.getTreeNodeNum() != 1) {
					this.setTreeNodeNum(1);
					m_NotSavedFlag = true;
				}

				if (this.getTreeNodeNumEnd() != 2000000000) {
					this.setTreeNodeNumEnd(2000000000);
					m_NotSavedFlag = true;
				}
			}

			sql = tzSQLObject.getSQLText("SQL.TZUtilTree.TzSelectChildrenNodes");

			List<?> listNodes = sqlQuery.queryForList(sql, new Object[] { setId, treeName, treeNode });

			for (Object objChild : listNodes) {

				Map<String, Object> mapChild = (Map<String, Object>) objChild;

				String strNodeId = mapChild.get("TREE_NODE").toString();

				TreeNode childTreeNode = new TreeNode(this.getSetId(), this.getSetCntrlValue(), this.getTreeName(),
						this.getEffdt(), strNodeId, this.getTreeNode());
				childTreeNode.setSqlQuery(sqlQuery);
				childTreeNode.setTZGDObject(tzSQLObject);
				childTreeNode.setGetSysHardCodeVal(getSysHardCodeVal);

				listChildNodes.add(childTreeNode);

				childTreeNode.load(false);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取当前节点所有孙子节点总数的方法
	 * 
	 * @return Integer
	 */
	public int getTotalChildNodeNum() {

		return m_TotalChildNum;
	}

	/**
	 * 判断当前节点及其所有的子节点、孙子节点的是否满足节点编号大于父节点编号、小于等于父节点结束编号的规则的方法;
	 * 判断当前节点的子节点编号、结束编号之间有没有重叠的方法;
	 * 
	 * @param nodeNum
	 * @param nodeNumEnd
	 * @return boolean
	 */
	public boolean isValidNodeNum(int nodeNum, int nodeNumEnd) {
		// 当前节点的编号没有在父节点的节点编号和结束编号之间，则返回false
		int m_TreeNodeNum = this.getTreeNodeNum();
		if ((m_TreeNodeNum <= nodeNum && m_TreeNodeNum > 1) || m_TreeNodeNum > nodeNumEnd) {
			return false;
		}

		// 判断当前节点的所有子节点的节点编号、结束编号之间是否有重叠的情况
		int previousNodeNumEnd = m_TreeNodeNum;
		int listnum = listChildNodes.size();
		for (int i = 0; i < listnum; i++) {

			if (i >= 1) {
				// 如果当前节点的子节点的节点编号、结束编号之间有重叠的情况，则直接返回false
				if (previousNodeNumEnd >= listChildNodes.get(i).getTreeNodeNum()) {
					return false;
				}
			}
			previousNodeNumEnd = listChildNodes.get(i).getTreeNodeNumEnd();

			// 递归判断当前节点的子节点的节点编号和结束编号是否满足规则
			if (listChildNodes.get(i).isValidNodeNum(m_TreeNodeNum, this.getTreeNodeNumEnd()) == false) {
				return false;
			}

		}

		return true;
	}

	/**
	 * 在指定位置插入子节点
	 * 
	 * @param cNode
	 * @param index
	 * @return boolean
	 */
	public boolean inserChildNode(TreeNode cNode, int index) {
		int pIndex = index;
		int listnum = listChildNodes.size();
		if (pIndex > listnum) {
			pIndex = listnum;
		}
		if (pIndex < 0) {
			pIndex = 0;
		}

		cNode.copyParentNode(this);

		List<TreeNode> tmpChildNodes = new ArrayList<TreeNode>();
		if (pIndex == 0) {
			tmpChildNodes.add(cNode);
		}

		for (int i = 0; i < listnum; i++) {

			tmpChildNodes.add(listChildNodes.get(i));

			if (i == pIndex) {
				tmpChildNodes.add(cNode);
			}

		}

		listChildNodes = tmpChildNodes;

		// 重新计算当前节点的各子节点的编号和结束编号
		try {
			int m_TreeNodeNum, m_TreeNodeNumEnd, m_ParentNodeNum;
			m_TreeNodeNum = this.getTreeNodeNum();
			m_TreeNodeNumEnd = this.getTreeNodeNumEnd();
			m_ParentNodeNum = this.getParentNodeNum();
			this.refreshNodeNum(m_TreeNodeNum, m_TreeNodeNumEnd, m_ParentNodeNum, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 删除指定的子节点
	 * 
	 * @param nodeId
	 * @return boolean
	 */
	public boolean deleteChildNode(String nodeId) {
		String deleteFlag = "N";
		int listnum = listChildNodes.size();
		int i;
		List<TreeNode> tmpChildNodes = new ArrayList<TreeNode>();
		for (i = 0; i < listnum; i++) {
			TreeNode tmpNode = listChildNodes.get(i);
			if (tmpNode.getTreeNode() == nodeId) {
				listDeleteChildNodes.add(tmpNode);
				deleteFlag = "Y";
			} else {
				tmpChildNodes.add(tmpNode);
			}
		}

		if (deleteFlag == "Y") {
			listChildNodes = tmpChildNodes;
			m_NotSavedFlag = true;
		} else {

			for (i = 0; i < listnum; i++) {

				if (listChildNodes.get(i).deleteChildNode(nodeId) == true) {
					deleteFlag = "Y";
					break;
				}

			}

		}

		return (deleteFlag == "Y");
	}

	/**
	 * 复制父节点信息的方法
	 * 
	 * @param pNode
	 */
	public void copyParentNode(TreeNode pNode) {

		this.setSetId(pNode.getSetId());
		this.setSetCntrlValue(pNode.getSetCntrlValue());
		this.setTreeName(pNode.getTreeName());
		this.setEffdt(pNode.getEffdt());
		this.setTreeLevelNum(pNode.getTreeLevelNum() + 1);
		this.setTreeNodeType(pNode.getTreeNodeType());
		this.setParentNodeNum(pNode.getTreeNodeNum());
		this.setParentNodeName(pNode.getTreeNode());
		this.setOldTreeNodeNum(pNode.getOldTreeNodeNum());
		this.setNodecolImage(pNode.getNodecolImage());
		this.setNodeexpImage(pNode.getNodeexpImage());

		m_NotSavedFlag = true;
	}

	/**
	 * 根据指定的子节点名称查找子节点对象
	 * 
	 * @param nodeId
	 * @return TreeNode
	 */
	public TreeNode findChildNode(String nodeId) {

		int listnum = listChildNodes.size();
		for (int i = 0; i < listnum; i++) {

			TreeNode retChildNode = listChildNodes.get(i);

			if (nodeId == retChildNode.getTreeNode()) {
				return retChildNode;
			}

			retChildNode = retChildNode.findChildNode(nodeId);
			if (retChildNode != null) {
				return retChildNode;
			}

		}

		return null;
	}

	/**
	 * 重新刷新计算树节点编号的方法
	 * 
	 * @param nodeNum
	 * @param nodeNumEnd
	 * @param pNodeNum
	 * @param reCalculateFlag
	 * @throws Exception
	 */
	private void refreshNodeNum(int nodeNum, int nodeNumEnd, int pNodeNum, boolean reCalculateFlag) throws Exception {
		
		if (reCalculateFlag == true) {
			this.getChildNodeCount();
		}

		int tmpTotalNodeNum = m_TotalChildNum + 1;

		int m_TreeNodeNum = this.getTreeNodeNum();
		int m_TreeNodeNumEnd = this.getTreeNodeNumEnd();
		int m_ParentNodeNum = this.getParentNodeNum();

		if (m_TreeNodeNum != nodeNum || m_TreeNodeNumEnd != nodeNumEnd || m_ParentNodeNum != pNodeNum) {

			m_TreeNodeNum = nodeNum;
			m_TreeNodeNumEnd = nodeNumEnd;
			m_ParentNodeNum = pNodeNum;
			
			this.setTreeNodeNum(m_TreeNodeNum);
			this.setTreeNodeNumEnd(m_TreeNodeNumEnd);
			this.setParentNodeNum(m_ParentNodeNum);

			m_NotSavedFlag = true;

		}

		int childTotal1 = 0;
		int childTotal2 = 0;
		int preEedNodeNum = 0;
		int startNodeNum = 0;
		int endNodeNum = 0;
		int listnum = listChildNodes.size();

		for (int i = 0; i < listnum; i++) {

			if (i == 0) {
				childTotal1 = 1;
				childTotal2 = listChildNodes.get(i).getTotalChildNodeNum() + 1;
			} else {
				childTotal1 = childTotal1 + listChildNodes.get(i - 1).getTotalChildNodeNum() + 1;
				childTotal2 = childTotal2 + listChildNodes.get(i).getTotalChildNodeNum() + 1;
			}

			double calAvgNodeNum = Arith.div((double) childTotal1, (double) tmpTotalNodeNum, 4);
			long calNodeNumEnd = nodeNumEnd - nodeNum;
			double calNodeNum = calAvgNodeNum * (double)calNodeNumEnd;
			int calNodeNumFloor = (int) Math.floor(calNodeNum);

			startNodeNum = calNodeNumFloor + nodeNum;
			// startNodeNum = (int) Math.floor((childTotal1 / tmpTotalNodeNum) *
			// (nodeNumEnd - nodeNum)) + nodeNum;
			if (startNodeNum == nodeNum) {
				startNodeNum = startNodeNum + 1;
			}
			if (startNodeNum >= nodeNumEnd) {
				startNodeNum = startNodeNum - 1;
			}

			calAvgNodeNum = Arith.div((double) (1 + childTotal2), (double) tmpTotalNodeNum, 4);
			calNodeNumEnd = nodeNumEnd - nodeNum;
			calNodeNum = calAvgNodeNum * (double)calNodeNumEnd;
			calNodeNumFloor = (int) Math.floor(calNodeNum);

			endNodeNum = calNodeNumFloor + nodeNum;
			// endNodeNum = (int) Math.floor(((1 + childTotal2) /
			// tmpTotalNodeNum) * (nodeNumEnd - nodeNum)) + nodeNum;
			if (i < (listnum - 1)) {
				endNodeNum = endNodeNum - 1;
			}

			if (startNodeNum <= nodeNum || startNodeNum >= nodeNumEnd || startNodeNum >= endNodeNum
					|| startNodeNum <= preEedNodeNum) {
				throw new Exception("树节点太多，已超出树管理器管理节点数的极限。");
			}

			listChildNodes.get(i).refreshNodeNum(startNodeNum, endNodeNum, m_TreeNodeNum, false);

			preEedNodeNum = endNodeNum;

		}
	}

	/**
	 * 获取子节点、孙子节点数量的方法
	 * 
	 * @return Integer
	 */
	private int getChildNodeCount() {
		int childCount = 0;

		int listnum = listChildNodes.size();
		for (int i = 0; i < listnum; i++) {
			childCount = childCount + listChildNodes.get(i).getChildNodeCount();
		}

		m_TotalChildNum = childCount;

		return childCount + 1;
	}

	/**
	 * 从数据库中删除已标识为删除的节点及其所有的子节点
	 */
	private void deleteNodeFromDB() {
		TreeNode tmpNode;

		while (listChildNodes.size() >= 1) {
			tmpNode = listChildNodes.get(0);
			tmpNode.deleteNodeFromDB();
			listChildNodes.remove(0);
		}

		while (listDeleteChildNodes.size() >= 1) {
			tmpNode = listDeleteChildNodes.get(0);
			tmpNode.deleteNodeFromDB();
			listDeleteChildNodes.remove(0);
		}

		String sql = "delete from PSTREENODE where ltrim(rtrim(SETID))=? and TREE_NAME=? and TREE_NODE=?";

		sqlQuery.update(sql, new Object[] { this.getSetId(), this.getTreeName(), this.getTreeNode() });

	}

	/************************
	 * 以下是对 mapNode 的赋值和取值
	 ************************/
	public String getSetId() {
		if (mapNode != null) {
			return mapNode.get("SETID") == null ? "" : mapNode.get("SETID").toString();
		} else {
			return null;
		}
	}

	public String getSetCntrlValue() {
		if (mapNode != null) {
			return mapNode.get("SETCNTRLVALUE").toString();
		} else {
			return null;
		}
	}

	public String getTreeName() {
		if (mapNode != null) {
			return mapNode.get("TREE_NAME") == null ? "" : mapNode.get("TREE_NAME").toString();
		} else {
			return null;
		}
	}

	public Date getEffdt() {
		if (mapNode != null) {
			String dateFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat format = new SimpleDateFormat(dateFormat);
			try {
				return format.parse(mapNode.get("EFFDT").toString());
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	public int getTreeNodeNum() {
		if (mapNode != null) {
			return Integer.parseInt(mapNode.get("TREE_NODE_NUM").toString());
		} else {
			return -999999999;
		}
	}

	public String getTreeNode() {
		if (mapNode != null) {
			return mapNode.get("TREE_NODE") == null ? "" : mapNode.get("TREE_NODE").toString();
		} else {
			return null;
		}
	}

	public String getTreeBranch() {
		if (mapNode != null) {
			return mapNode.get("TREE_BRANCH") == null ? "" : mapNode.get("TREE_BRANCH").toString();
		} else {
			return null;
		}
	}

	public int getTreeNodeNumEnd() {
		if (mapNode != null && mapNode.get("TREE_NODE_NUM_END") != null) {
			return Integer.parseInt(mapNode.get("TREE_NODE_NUM_END").toString());
		} else {
			return -999999999;
		}
	}

	public int getTreeLevelNum() {
		if (mapNode != null && mapNode.get("TREE_LEVEL_NUM") != null) {
			return Integer.parseInt(mapNode.get("TREE_LEVEL_NUM").toString());
		} else {
			return -999999999;
		}
	}

	public String getTreeNodeType() {
		if (mapNode != null) {
			return mapNode.get("TREE_NODE_TYPE") == null ? "" : mapNode.get("TREE_NODE_TYPE").toString();
		} else {
			return null;
		}
	}

	public int getParentNodeNum() {
		if (mapNode != null && mapNode.get("PARENT_NODE_NUM") != null) {
			return Integer.parseInt(mapNode.get("PARENT_NODE_NUM").toString());
		} else {
			return -999999999;
		}
	}

	public String getParentNodeName() {
		if (mapNode != null) {
			return mapNode.get("PARENT_NODE_NAME") == null ? "" : mapNode.get("PARENT_NODE_NAME").toString();
		} else {
			return null;
		}
	}

	public String getOldTreeNodeNum() {
		if (mapNode != null) {
			return mapNode.get("OLD_TREE_NODE_NUM") == null ? "" : mapNode.get("OLD_TREE_NODE_NUM").toString();
		} else {
			return null;
		}
	}

	public String getNodecolImage() {
		if (mapNode != null) {
			return mapNode.get("NODECOL_IMAGE") == null ? "" : mapNode.get("NODECOL_IMAGE").toString();
		} else {
			return null;
		}
	}

	public String getNodeexpImage() {
		if (mapNode != null) {
			return mapNode.get("NODEEXP_IMAGE") == null ? "" : mapNode.get("NODEEXP_IMAGE").toString();
		} else {
			return null;
		}
	}

	public void setSetId(String setid) {
		if (mapNode.containsKey("SETID")) {
			mapNode.replace("SETID", setid);
		} else {
			mapNode.put("SETID", setid);
		}
	}

	public void setSetCntrlValue(String setcntrlvalue) {
		if (mapNode.containsKey("SETCNTRLVALUE")) {
			mapNode.replace("SETCNTRLVALUE", setcntrlvalue);
		} else {
			mapNode.put("SETCNTRLVALUE", setcntrlvalue);
		}
	}

	public void setTreeName(String treename) {
		if (mapNode.containsKey("TREE_NAME")) {
			mapNode.replace("TREE_NAME", treename);
		} else {
			mapNode.put("TREE_NAME", treename);
		}
	}

	public void setEffdt(Date effdt) {
		if (mapNode.containsKey("EFFDT")) {
			mapNode.replace("EFFDT", effdt);
		} else {
			mapNode.put("EFFDT", effdt);
		}
	}

	public void setTreeNodeNum(int treenodenum) {
		if (mapNode.containsKey("TREE_NODE_NUM")) {
			mapNode.replace("TREE_NODE_NUM", treenodenum);
		} else {
			mapNode.put("TREE_NODE_NUM", treenodenum);
		}
	}

	public void setTreeNode(String treenode) {
		if (mapNode.containsKey("TREE_NODE")) {
			mapNode.replace("TREE_NODE", treenode);
		} else {
			mapNode.put("TREE_NODE", treenode);
		}
	}

	public void setTreeBranch(String treebranch) {
		if (mapNode.containsKey("TREE_BRANCH")) {
			mapNode.replace("TREE_BRANCH", treebranch);
		} else {
			mapNode.put("TREE_BRANCH", treebranch);
		}
	}

	public void setTreeNodeNumEnd(int treenodenumend) {
		if (mapNode.containsKey("TREE_NODE_NUM_END")) {
			mapNode.replace("TREE_NODE_NUM_END", treenodenumend);
		} else {
			mapNode.put("TREE_NODE_NUM_END", treenodenumend);
		}
	}

	public void setTreeLevelNum(int treelevelnum) {
		if (mapNode.containsKey("TREE_LEVEL_NUM")) {
			mapNode.replace("TREE_LEVEL_NUM", treelevelnum);
		} else {
			mapNode.put("TREE_LEVEL_NUM", treelevelnum);
		}
	}

	public void setTreeNodeType(String treenodetype) {
		if (mapNode.containsKey("TREE_NODE_TYPE")) {
			mapNode.replace("TREE_NODE_TYPE", treenodetype);
		} else {
			mapNode.put("TREE_NODE_TYPE", treenodetype);
		}
	}

	public void setParentNodeNum(int parentnodenum) {
		if (mapNode.containsKey("PARENT_NODE_NUM")) {
			mapNode.replace("PARENT_NODE_NUM", parentnodenum);
		} else {
			mapNode.put("PARENT_NODE_NUM", parentnodenum);
		}
	}

	public void setParentNodeName(String parentnodename) {
		if (mapNode.containsKey("PARENT_NODE_NAME")) {
			mapNode.replace("PARENT_NODE_NAME", parentnodename);
		} else {
			mapNode.put("PARENT_NODE_NAME", parentnodename);
		}
	}

	public void setOldTreeNodeNum(String oldtreenodenum) {
		if (mapNode.containsKey("OLD_TREE_NODE_NUM")) {
			mapNode.replace("OLD_TREE_NODE_NUM", oldtreenodenum);
		} else {
			mapNode.put("OLD_TREE_NODE_NUM", oldtreenodenum);
		}
	}

	public void setNodecolImage(String nodecolimage) {
		if (mapNode.containsKey("NODECOL_IMAGE")) {
			mapNode.replace("NODECOL_IMAGE", nodecolimage);
		} else {
			mapNode.put("NODECOL_IMAGE", nodecolimage);
		}
	}

	public void setNodeexpImage(String nodeexpimage) {
		if (mapNode.containsKey("NODEEXP_IMAGE")) {
			mapNode.replace("NODEEXP_IMAGE", nodeexpimage);
		} else {
			mapNode.put("NODEEXP_IMAGE", nodeexpimage);
		}
	}

}
