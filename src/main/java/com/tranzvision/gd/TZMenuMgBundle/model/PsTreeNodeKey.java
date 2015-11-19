package com.tranzvision.gd.TZMenuMgBundle.model;

import java.util.Date;

public class PsTreeNodeKey {
    private String setid;

    private String setcntrlvalue;

    private String treeName;

    private Date effdt;

    private Integer treeNodeNum;

    private String treeNode;

    private String treeBranch;

    public String getSetid() {
        return setid;
    }

    public void setSetid(String setid) {
        this.setid = setid == null ? null : setid.trim();
    }

    public String getSetcntrlvalue() {
        return setcntrlvalue;
    }

    public void setSetcntrlvalue(String setcntrlvalue) {
        this.setcntrlvalue = setcntrlvalue == null ? null : setcntrlvalue.trim();
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName == null ? null : treeName.trim();
    }

    public Date getEffdt() {
        return effdt;
    }

    public void setEffdt(Date effdt) {
        this.effdt = effdt;
    }

    public Integer getTreeNodeNum() {
        return treeNodeNum;
    }

    public void setTreeNodeNum(Integer treeNodeNum) {
        this.treeNodeNum = treeNodeNum;
    }

    public String getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(String treeNode) {
        this.treeNode = treeNode == null ? null : treeNode.trim();
    }

    public String getTreeBranch() {
        return treeBranch;
    }

    public void setTreeBranch(String treeBranch) {
        this.treeBranch = treeBranch == null ? null : treeBranch.trim();
    }
}