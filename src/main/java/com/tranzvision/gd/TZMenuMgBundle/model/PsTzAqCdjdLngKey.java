package com.tranzvision.gd.TZMenuMgBundle.model;

public class PsTzAqCdjdLngKey {
    private String treeName;

    private String tzMenuNum;

    private String tzLanguageId;

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

    public String getTzLanguageId() {
        return tzLanguageId;
    }

    public void setTzLanguageId(String tzLanguageId) {
        this.tzLanguageId = tzLanguageId == null ? null : tzLanguageId.trim();
    }
}