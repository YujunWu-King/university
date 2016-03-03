/**
 * 
 */
package com.tranzvision.gd.TZMenuMgBundle.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZMenuMgBundle.dao.PsTreeNodeMapper;
import com.tranzvision.gd.TZMenuMgBundle.model.PsTreeNode;
import com.tranzvision.gd.TZMenuMgBundle.service.TzMenuTreeNodeService;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 树及树节点相关操作方法定义，原PS：TZ_GD_GNCDGL_PKG:TZ_GD_TREE_NODE
 * 
 * @author SHIHUA
 * @since 2015-11-16
 */
@Service("com.tranzvision.gd.TZMenuMgBundle.service.impl.TzMenuTreeNodeServiceImpl")
public class TzMenuTreeNodeServiceImpl implements TzMenuTreeNodeService {

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTreeNodeMapper psTreeNodeMapper;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private GetSeqNum getSeqNum;

	private String createDt = "2015-11-11";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZMenuMgBundle.service.TzMenuTreeNodeService#
	 * createTree(java.lang.String)
	 */
	@Override
	@Transactional
	public void createTree(String treeName) {
		String dtFormat = getSysHardCodeVal.getDateFormat();
		SimpleDateFormat format = new SimpleDateFormat(dtFormat);
		Date effdt;
		try {
			effdt = format.parse(createDt);

			PsTreeNode psTreeNode = new PsTreeNode();
			psTreeNode.setSetid("");
			psTreeNode.setSetcntrlvalue("");
			psTreeNode.setTreeBranch("");
			psTreeNode.setTreeName(treeName);
			psTreeNode.setEffdt(effdt);
			psTreeNode.setTreeNodeNum(1);
			psTreeNode.setTreeNode("ROOT");
			psTreeNode.setTreeNodeNumEnd(2000000000);
			psTreeNode.setTreeLevelNum((short) 1);
			psTreeNode.setTreeNodeType("G");
			psTreeNode.setParentNodeNum(0);
			psTreeNode.setOldTreeNodeNum("N");

			psTreeNodeMapper.insert(psTreeNode);

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZMenuMgBundle.service.TzMenuTreeNodeService#
	 * createChildNode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public boolean createChildNode(String treeName, String parentTreeNode, String treeNode) {

		try {
			String dtFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat format = new SimpleDateFormat(dtFormat);
			Date effdt = format.parse(createDt);

			String nodeExistYet;
			String sql = "select 'Y' from PSTREENODE where TREE_NAME=? and TREE_NODE=? and EFFDT<=? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))=''";

			nodeExistYet = sqlQuery.queryForObject(sql, new Object[] { treeName, treeNode, effdt }, "String");
			// 节点存在则不创建
			if ("Y".equals(nodeExistYet)) {
				return true;
			}

			sql = "select TREE_NODE_NUM,TREE_NODE_NUM_END,TREE_LEVEL_NUM from PSTREENODE where TREE_NAME=? and TREE_NODE=? and EFFDT<=? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))=''";
			Map<String, Object> mapParentNode = sqlQuery.queryForMap(sql,
					new Object[] { treeName, parentTreeNode, effdt });
			if (null == mapParentNode) {
				return false;
			}

			int parentNodeNum = Integer.parseInt(mapParentNode.get("TREE_NODE_NUM").toString());
			int parentNodeEnd = Integer.parseInt(mapParentNode.get("TREE_NODE_NUM_END").toString());
			int parentLevelNum = Short.parseShort(mapParentNode.get("TREE_LEVEL_NUM").toString());

			// 插入节点的开始序号，插入节点的结束序号
			int insertTreeNodeNum, insertTreeNodeNumEnd;

			// 查看父节点下子节点的最小开始序号
			sql = "select min(TREE_NODE_NUM) from PSTREENODE where TREE_NAME=? and PARENT_NODE_NAME=? and PARENT_NODE_NUM=? and EFFDT<=? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))=''";
			String strMinNodeNum = sqlQuery.queryForObject(sql,
					new Object[] { treeName, parentTreeNode, parentNodeNum, effdt }, "String");

			int minNodeNum = strMinNodeNum == null ? 0 : Integer.parseInt(strMinNodeNum);

			if (minNodeNum > 0) {
				// 如果存在子节点
				// 插入节点的结束序号为父节点存在的子节点的最小序号减一
				insertTreeNodeNumEnd = minNodeNum - 1;
			} else {
				// 如果不存在子节点
				// 插入节点的最大序号为父节点的最大结束序号
				insertTreeNodeNumEnd = parentNodeEnd;
			}

			// 插入节点的开始序号为（父节点的开始序号 + 当前节点的结束序号）/2; 对计算值进行四舍五入
			long thisNodeNum = (long) parentNodeNum + (long) insertTreeNodeNumEnd;
			BigDecimal newTreeNodeNum = new BigDecimal(thisNodeNum / 2);
			insertTreeNodeNum = newTreeNodeNum.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			if (insertTreeNodeNum >= insertTreeNodeNumEnd) {
				insertTreeNodeNum = insertTreeNodeNumEnd;
			}

			// 创建新节点数据
			PsTreeNode psTreeNode = new PsTreeNode();
			psTreeNode.setSetid("");
			psTreeNode.setSetcntrlvalue("");
			psTreeNode.setTreeBranch("");
			psTreeNode.setTreeName(treeName);
			psTreeNode.setEffdt(effdt);
			psTreeNode.setTreeNodeNum(insertTreeNodeNum);
			psTreeNode.setTreeNode(treeNode);
			psTreeNode.setTreeNodeNumEnd(insertTreeNodeNumEnd);
			psTreeNode.setTreeLevelNum((short) (parentLevelNum + 1));
			psTreeNode.setTreeNodeType("G");
			psTreeNode.setParentNodeNum(parentNodeNum);
			psTreeNode.setParentNodeName(parentTreeNode);
			psTreeNode.setOldTreeNodeNum("N");

			psTreeNodeMapper.insert(psTreeNode);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZMenuMgBundle.service.TzMenuTreeNodeService#
	 * createBrotherNode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public boolean createBrotherNode(String treeName, String brotherTreeNode, String treeNode) {

		try {
			String dtFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat format = new SimpleDateFormat(dtFormat);
			Date effdt = format.parse(createDt);

			String nodeExistYet;
			String sql = "select 'Y' from PSTREENODE where TREE_NAME=? and TREE_NODE=? and EFFDT<=? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))=''";

			nodeExistYet = sqlQuery.queryForObject(sql, new Object[] { treeName, treeNode, effdt }, "String");
			// 节点存在则不创建
			if ("Y".equals(nodeExistYet)) {
				return true;
			}

			sql = "select TREE_NODE_NUM,TREE_NODE_NUM_END,TREE_LEVEL_NUM,PARENT_NODE_NUM ,PARENT_NODE_NAME from PSTREENODE where TREE_NAME=? and TREE_NODE=? and EFFDT<=? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))=''";
			Map<String, Object> mapBrotherNode = sqlQuery.queryForMap(sql,
					new Object[] { treeName, brotherTreeNode, effdt });
			if (null == mapBrotherNode) {
				return false;
			}

			int brotherNodeNum = Integer.parseInt(mapBrotherNode.get("TREE_NODE_NUM").toString());
			int brotherNodeEnd = Integer.parseInt(mapBrotherNode.get("TREE_NODE_NUM_END").toString());
			short brotherLevelNum = Short.parseShort(mapBrotherNode.get("TREE_LEVEL_NUM").toString());
			int brotherParentNodeNum = Integer.parseInt(mapBrotherNode.get("PARENT_NODE_NUM").toString());
			String brotherParentNode = mapBrotherNode.get("PARENT_NODE_NAME").toString();

			// 插入节点的开始序号，插入节点的结束序号
			int insertTreeNodeNum, insertTreeNodeNumEnd;

			// 插入节点的结束序号为操作兄弟节点的结束序号
			insertTreeNodeNumEnd = brotherNodeEnd;

			// 插入节点的开始序号为（操作兄弟节点的开始序号 + 当前节点的结束序号）/2；对计算值进行四舍五入
			long thisNodeNum = (long) brotherNodeNum + (long) insertTreeNodeNumEnd;
			BigDecimal newTreeNodeNum = new BigDecimal(thisNodeNum / 2);
			insertTreeNodeNum = newTreeNodeNum.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

			// 操作兄弟节点的结束序号位插入节点的开始序号-1
			brotherNodeEnd = insertTreeNodeNum - 1;

			// 创建新节点数据
			PsTreeNode psTreeNode = new PsTreeNode();
			psTreeNode.setSetid("");
			psTreeNode.setSetcntrlvalue("");
			psTreeNode.setTreeBranch("");
			psTreeNode.setTreeName(treeName);
			psTreeNode.setEffdt(effdt);
			psTreeNode.setTreeNodeNum(insertTreeNodeNum);
			psTreeNode.setTreeNode(treeNode);
			psTreeNode.setTreeNodeNumEnd(insertTreeNodeNumEnd);
			psTreeNode.setTreeLevelNum(brotherLevelNum);
			psTreeNode.setTreeNodeType("G");
			psTreeNode.setParentNodeNum(brotherParentNodeNum);
			psTreeNode.setParentNodeName(brotherParentNode);
			psTreeNode.setOldTreeNodeNum("N");

			int rst = psTreeNodeMapper.insert(psTreeNode);

			if (rst > 0) {
				this.updateNodeNum(treeName, brotherTreeNode, brotherNodeNum, brotherNodeEnd);
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZMenuMgBundle.service.TzMenuTreeNodeService#
	 * deleteNode(java.lang.String, int, int)
	 */
	@Override
	@Transactional
	public void deleteNode(String treeName, int treeNodeNum, int treeNodeNumEnd) {

		try {
			String dtFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat format = new SimpleDateFormat(dtFormat);
			Date effdt = format.parse(createDt);

			// 查找同级兄弟节点的上节点
			String sql = "select PARENT_NODE_NAME,TREE_LEVEL_NUM from PSTREENODE where TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM = ?  and TREE_NODE_NUM_END = ? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))=''";
			Map<String, Object> mapNode = sqlQuery.queryForMap(sql,
					new Object[] { treeName, effdt, treeNodeNum, treeNodeNumEnd });

			String parentNode = mapNode.get("PARENT_NODE_NAME").toString();
			short levelNum = Short.parseShort(mapNode.get("TREE_LEVEL_NUM").toString());

			sql = "select TREE_NODE,TREE_NODE_NUM from PSTREENODE where TREE_NAME=? and EFFDT<=? and PARENT_NODE_NAME = ? AND TREE_LEVEL_NUM=? AND TREE_NODE_NUM_END < ? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))='' order by TREE_NODE_NUM_END desc";
			Map<String, Object> mapPrevNode = sqlQuery.queryForMap(sql,
					new Object[] { treeName, effdt, parentNode, levelNum, treeNodeNum });

			// 删除多语言表中对应的树节点记录
			sql = "delete from PS_TZ_AQ_CDJD_LNG where TREE_NAME=? and exists (select TREE_NODE from PSTREENODE A where A.TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM>=? and TREE_NODE_NUM_END<=? and A.TREE_NODE=TZ_MENU_NUM)";
			sqlQuery.update(sql, new Object[] { treeName, treeName, effdt, treeNodeNum, treeNodeNumEnd });

			// 删除树节点详细信息表中的记录
			sql = "delete from PS_TZ_AQ_CDJD_TBL where TREE_NAME=? and exists (select TREE_NODE from PSTREENODE A where A.TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM>=? and TREE_NODE_NUM_END<=? and A.TREE_NODE=TZ_MENU_NUM)";
			sqlQuery.update(sql, new Object[] { treeName, treeName, effdt, treeNodeNum, treeNodeNumEnd });

			// 删除树节点
			sql = "delete from PSTREENODE where ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))='' and TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM >= ?  and TREE_NODE_NUM_END <= ?";
			sqlQuery.update(sql, new Object[] { treeName, effdt, treeNodeNum, treeNodeNumEnd });

			// 存在同级兄弟节点的上节点,把上个兄弟节点最大结束序号改为当前删除的节点的最大结束序号
			if (null != mapPrevNode) {
				String prevNodeName = mapPrevNode.get("TREE_NODE").toString();
				int prevNodeNum = Integer.parseInt(mapPrevNode.get("TREE_NODE_NUM").toString());
				this.updateNodeNum(treeName, prevNodeName, prevNodeNum, treeNodeNumEnd);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZMenuMgBundle.service.TzMenuTreeNodeService#
	 * changeNode(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@Transactional
	public void changeNode(String treeName, String oldNode, String preNode, String patentNode) {

		int seqNum = getSeqNum.getSeqNum("TZ_TREENODE_TMP", "TZ_SEQNUM");

		try {

			String dtFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat format = new SimpleDateFormat(dtFormat);
			Date effdt = format.parse(createDt);

			String sql = "select TREE_NODE_NUM,TREE_NODE_NUM_END,TREE_LEVEL_NUM from PSTREENODE where TREE_NAME=? and TREE_NODE=? and EFFDT<=? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))=''";
			Map<String, Object> mapOldNode = sqlQuery.queryForMap(sql, new Object[] { treeName, oldNode, effdt });

			int oldTreeNodeNum = Integer.parseInt(mapOldNode.get("TREE_NODE_NUM").toString());
			int oldTreeNodeNumEnd = Integer.parseInt(mapOldNode.get("TREE_NODE_NUM_END").toString());
			// short oldTreeLevelNum =
			// Short.parseShort(mapOldNode.get("TREE_LEVEL_NUM").toString());

			if (mapOldNode != null) {

				sql = tzSQLObject.getSQLText("SQL.TZMenuMgBundle.TzInsertTreeNodeTmp");
				sqlQuery.update(sql, new Object[] { seqNum, treeName, effdt, oldTreeNodeNum, oldTreeNodeNumEnd });

				// 查找同级兄弟节点的上节点
				sql = "select PARENT_NODE_NAME,TREE_LEVEL_NUM from PSTREENODE where ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))='' and TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM = ?  and TREE_NODE_NUM_END = ?";
				Map<String, Object> mapNode1 = sqlQuery.queryForMap(sql,
						new Object[] { treeName, effdt, oldTreeNodeNum, oldTreeNodeNumEnd });

				String y_pNode = mapNode1.get("PARENT_NODE_NAME").toString();
				short levelNum = Short.parseShort(mapNode1.get("TREE_LEVEL_NUM").toString());

				sql = "select TREE_NODE, TREE_NODE_NUM from PSTREENODE where ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))='' and TREE_NAME=? and EFFDT<=? and PARENT_NODE_NAME = ? and TREE_LEVEL_NUM=? and TREE_NODE_NUM_END < ? order by TREE_NODE_NUM_END desc";
				Map<String, Object> mapNode2 = sqlQuery.queryForMap(sql,
						new Object[] { treeName, effdt, y_pNode, levelNum, oldTreeNodeNum });

				String prevNodeName = mapNode2.get("TREE_NODE").toString();
				int prevNodeNum = Integer.parseInt(mapNode2.get("TREE_NODE_NUM").toString());

				sql = "delete from PSTREENODE where ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))='' and TREE_NAME=? and EFFDT<=? and TREE_NODE_NUM >= ?  and TREE_NODE_NUM_END <= ?";
				sqlQuery.update(sql, new Object[] { treeName, effdt, oldTreeNodeNum, oldTreeNodeNumEnd });

				// 存在同级兄弟节点的上节点,把上个兄弟节点最大结束序号改为当前删除的节点的最大结束序号
				if (mapNode2 != null) {
					this.updateNodeNum(treeName, prevNodeName, prevNodeNum, oldTreeNodeNumEnd);
				}

			}

			if (preNode == null || "".equals(preNode)) {
				/****** 表示插入子节点 *****/
				this.changeNodeToParent(seqNum, treeName, oldNode, patentNode);
			} else {
				/****** 表示插入兄弟节点 *****/
				this.changeNodeToBrother(seqNum, treeName, oldNode, preNode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZMenuMgBundle.service.TzMenuTreeNodeService#
	 * changeNodeToParent(int, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void changeNodeToParent(int seqNum, String treeName, String oldNode, String patentNode) {

		this.createChildNode(treeName, patentNode, oldNode);

		String sql = "select TREE_NODE from PS_TZ_TREENODE_TMP where TZ_SEQNUM=? and TREE_NAME=? and PARENT_NODE_NAME=? order by TREE_NODE_NUM desc";
		List<?> listTreeNodes = sqlQuery.queryForList(sql, new Object[] { seqNum, treeName, oldNode });

		for (Object objNode : listTreeNodes) {
			Map<String, Object> mapNode = (Map<String, Object>) objNode;

			String childTreeNode = mapNode.get("TREE_NODE").toString();

			this.changeNodeToParent(seqNum, treeName, childTreeNode, oldNode);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZMenuMgBundle.service.TzMenuTreeNodeService#
	 * changeNodeToBrother(int, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void changeNodeToBrother(int seqNum, String treeName, String oldNode, String brotherTreeNode) {

		this.createBrotherNode(treeName, brotherTreeNode, oldNode);

		String sql = "select TREE_NODE from PS_TZ_TREENODE_TMP where TZ_SEQNUM=? and TREE_NAME=? and PARENT_NODE_NAME=? order by TREE_NODE_NUM desc";
		List<?> listTreeNodes = sqlQuery.queryForList(sql, new Object[] { seqNum, treeName, oldNode });

		for (Object objNode : listTreeNodes) {
			Map<String, Object> mapNode = (Map<String, Object>) objNode;

			String childTreeNode = mapNode.get("TREE_NODE").toString();

			this.changeNodeToParent(seqNum, treeName, childTreeNode, oldNode);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZMenuMgBundle.service.TzMenuTreeNodeService#
	 * updateNodeNum(java.lang.String, java.lang.String, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateNodeNum(String treeName, String updateTreeNode, int updateTreeNodeNum, int updateTreeNodeNumEnd) {

		try {
			String dtFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat format = new SimpleDateFormat(dtFormat);
			Date effdt = format.parse(createDt);

			String sql = "update PSTREENODE set TREE_NODE_NUM=?, TREE_NODE_NUM_END=? where TREE_NAME=? and TREE_NODE=? and EFFDT<=? and ltrim(rtrim(SETID))='' and ltrim(rtrim(SETCNTRLVALUE))=''";
			sqlQuery.update(sql,
					new Object[] { updateTreeNodeNum, updateTreeNodeNumEnd, treeName, updateTreeNode, effdt });

			sql = "select TREE_NODE from PSTREENODE where TREE_NAME=? and PARENT_NODE_NAME=? and EFFDT<=? order by TREE_NODE_NUM desc";
			List<?> listNodes = sqlQuery.queryForList(sql, new Object[] { treeName, updateTreeNode, effdt });

			String childTreeNode;
			int num = 0;
			int childTreeNodeNumEnd, childTreeNodeNum = 0;
			for (Object objNode : listNodes) {

				Map<String, Object> mapNode = (Map<String, Object>) objNode;

				childTreeNode = mapNode.get("TREE_NODE").toString();

				if (num == 0) {
					childTreeNodeNumEnd = updateTreeNodeNumEnd;
					// 插入节点的开始序号为（父节点的开始序号 + 当前节点的结束序号）/2;
					BigDecimal newTreeNodeNum = new BigDecimal((updateTreeNodeNum + updateTreeNodeNumEnd) / 2)
							.setScale(0, BigDecimal.ROUND_HALF_UP);
					childTreeNodeNum = newTreeNodeNum.intValue();
				} else {
					childTreeNodeNumEnd = childTreeNodeNum - 1;
					BigDecimal newTreeNodeNum = new BigDecimal((updateTreeNodeNum + childTreeNodeNumEnd) / 2)
							.setScale(0, BigDecimal.ROUND_HALF_UP);
					childTreeNodeNum = newTreeNodeNum.intValue();
				}

				num++;

				this.updateNodeNum(treeName, childTreeNode, childTreeNodeNum, childTreeNodeNumEnd);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
