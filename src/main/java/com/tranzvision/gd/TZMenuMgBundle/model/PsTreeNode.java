package com.tranzvision.gd.TZMenuMgBundle.model;

public class PsTreeNode extends PsTreeNodeKey {
    private Integer treeNodeNumEnd;

    private Short treeLevelNum;

    private String treeNodeType;

    private Integer parentNodeNum;

    private String parentNodeName;

    private String oldTreeNodeNum;

    private String nodecolImage;

    private String nodeexpImage;

    public Integer getTreeNodeNumEnd() {
        return treeNodeNumEnd;
    }

    public void setTreeNodeNumEnd(Integer treeNodeNumEnd) {
        this.treeNodeNumEnd = treeNodeNumEnd;
    }

    public Short getTreeLevelNum() {
        return treeLevelNum;
    }

    public void setTreeLevelNum(Short treeLevelNum) {
        this.treeLevelNum = treeLevelNum;
    }

    public String getTreeNodeType() {
        return treeNodeType;
    }

    public void setTreeNodeType(String treeNodeType) {
        this.treeNodeType = treeNodeType == null ? null : treeNodeType.trim();
    }

    public Integer getParentNodeNum() {
        return parentNodeNum;
    }

    public void setParentNodeNum(Integer parentNodeNum) {
        this.parentNodeNum = parentNodeNum;
    }

    public String getParentNodeName() {
        return parentNodeName;
    }

    public void setParentNodeName(String parentNodeName) {
        this.parentNodeName = parentNodeName == null ? null : parentNodeName.trim();
    }

    public String getOldTreeNodeNum() {
        return oldTreeNodeNum;
    }

    public void setOldTreeNodeNum(String oldTreeNodeNum) {
        this.oldTreeNodeNum = oldTreeNodeNum == null ? null : oldTreeNodeNum.trim();
    }

    public String getNodecolImage() {
        return nodecolImage;
    }

    public void setNodecolImage(String nodecolImage) {
        this.nodecolImage = nodecolImage == null ? null : nodecolImage.trim();
    }

    public String getNodeexpImage() {
        return nodeexpImage;
    }

    public void setNodeexpImage(String nodeexpImage) {
        this.nodeexpImage = nodeexpImage == null ? null : nodeexpImage.trim();
    }
}