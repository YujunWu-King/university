package com.tranzvision.gd.TZEmailTxTypeBundle.model;

public class PsTzTxLbtjgxTKey {
    private String tzTxTypeId;

    private String tzTxRuleId;

    public String getTzTxTypeId() {
        return tzTxTypeId;
    }

    public void setTzTxTypeId(String tzTxTypeId) {
        this.tzTxTypeId = tzTxTypeId == null ? null : tzTxTypeId.trim();
    }

    public String getTzTxRuleId() {
        return tzTxRuleId;
    }

    public void setTzTxRuleId(String tzTxRuleId) {
        this.tzTxRuleId = tzTxRuleId == null ? null : tzTxRuleId.trim();
    }
}