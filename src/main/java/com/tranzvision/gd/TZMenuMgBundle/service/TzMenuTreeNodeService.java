/**
 * 
 */
package com.tranzvision.gd.TZMenuMgBundle.service;

/**
 * 树操作相关接口方法定义
 * 
 * @author SHIHUA
 * @since 2015-11-16
 */
public interface TzMenuTreeNodeService {

	/**
	 * 创建树
	 * 
	 * @param treeName
	 */
	public void createTree(String treeName);

	/**
	 * 创建子节点
	 * 
	 * @param treeName
	 * @param parentTreeNode
	 * @param treeNode
	 * @return boolean
	 */
	public boolean createChildNode(String treeName, String parentTreeNode, String treeNode);

	/**
	 * 创建同级节点
	 * 
	 * @param treeName
	 * @param brotherTreeNode
	 * @param treeNode
	 * @return boolean
	 */
	public boolean createBrotherNode(String treeName, String brotherTreeNode, String treeNode);

	/**
	 * 删除节点
	 * 
	 * @param treeName
	 * @param treeNodeNum
	 * @param treeNodeNumEnd
	 */
	public void deleteNode(String treeName, int treeNodeNum, int treeNodeNumEnd);

	/**
	 * 移动节点
	 * 
	 * @param treeName
	 * @param oldNode
	 * @param preNode
	 * @param patentNode
	 */
	public void changeNode(String treeName, String oldNode, String preNode, String patentNode);

	/**
	 * 递归移动节点到父节点下
	 * 
	 * @param seqNum
	 * @param treeName
	 * @param oldNode
	 * @param patentNode
	 */
	public void changeNodeToParent(int seqNum, String treeName, String oldNode, String patentNode);

	/**
	 * 递归移动节点到兄弟节点下
	 * 
	 * @param seqNum
	 * @param treeName
	 * @param oldNode
	 * @param brotherTreeNode
	 */
	public void changeNodeToBrother(int seqNum, String treeName, String oldNode, String brotherTreeNode);

	/**
	 * 如果因为一个节点拖拽而使前面一个兄弟节点的开始或结束序号发生变换则其子节点序号也要发生变换
	 * 
	 * @param treeName
	 * @param updateTreeNode
	 * @param updateTreeNodeNum
	 * @param updateTreeNodeNumEnd
	 */
	public void updateNodeNum(String treeName, String updateTreeNode, int updateTreeNodeNum, int updateTreeNodeNumEnd);

}
