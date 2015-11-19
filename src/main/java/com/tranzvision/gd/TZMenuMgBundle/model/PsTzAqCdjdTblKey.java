package com.tranzvision.gd.TZMenuMgBundle.model;

public class PsTzAqCdjdTblKey {
    private String treeName;

    private String tzMenuNum;

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName == null ? null : treeName.trim();
    }

    public String getTzMenuNum() {
        return tzMenuNum;
    }

    public void setTzMenuNum(String tzMenuNum) {
        this.tzMenuNum = tzMenuNum == null ? null : tzMenuNum.trim();
    }
}