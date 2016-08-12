package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTzExcSetTbl extends PsTzExcSetTblKey {
    private String tzXxxName;

    private String tzXxxType;

    private String tzFieldName;

    public String getTzXxxName() {
        return tzXxxName;
    }

    public void setTzXxxName(String tzXxxName) {
        this.tzXxxName = tzXxxName == null ? null : tzXxxName.trim();
    }

    public String getTzXxxType() {
        return tzXxxType;
    }

    public void setTzXxxType(String tzXxxType) {
        this.tzXxxType = tzXxxType == null ? null : tzXxxType.trim();
    }

    public String getTzFieldName() {
        return tzFieldName;
    }

    public void setTzFieldName(String tzFieldName) {
        this.tzFieldName = tzFieldName == null ? null : tzFieldName.trim();
    }
}