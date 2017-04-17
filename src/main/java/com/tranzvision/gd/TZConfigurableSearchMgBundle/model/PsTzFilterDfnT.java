package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFilterDfnT extends PsTzFilterDfnTKey {
    private String tzAdvanceModel;

    private String tzBaseSchEdit;

    private Integer tzResultMaxNum;

    private String tzCfgVSql;

    public String getTzAdvanceModel() {
        return tzAdvanceModel;
    }

    public void setTzAdvanceModel(String tzAdvanceModel) {
        this.tzAdvanceModel = tzAdvanceModel == null ? null : tzAdvanceModel.trim();
    }

    public String getTzBaseSchEdit() {
        return tzBaseSchEdit;
    }

    public void setTzBaseSchEdit(String tzBaseSchEdit) {
        this.tzBaseSchEdit = tzBaseSchEdit == null ? null : tzBaseSchEdit.trim();
    }

    public Integer getTzResultMaxNum() {
        return tzResultMaxNum;
    }

    public void setTzResultMaxNum(Integer tzResultMaxNum) {
        this.tzResultMaxNum = tzResultMaxNum;
    }

    public String getTzCfgVSql() {
        return tzCfgVSql;
    }

    public void setTzCfgVSql(String tzCfgVSql) {
        this.tzCfgVSql = tzCfgVSql == null ? null : tzCfgVSql.trim();
    }
}