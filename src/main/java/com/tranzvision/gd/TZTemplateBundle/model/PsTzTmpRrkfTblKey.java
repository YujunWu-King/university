package com.tranzvision.gd.TZTemplateBundle.model;

public class PsTzTmpRrkfTblKey {
    private String tzJgId;

    private String tzYmbId;

    private String tzKeyName;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzYmbId() {
        return tzYmbId;
    }

    public void setTzYmbId(String tzYmbId) {
        this.tzYmbId = tzYmbId == null ? null : tzYmbId.trim();
    }

    public String getTzKeyName() {
        return tzKeyName;
    }

    public void setTzKeyName(String tzKeyName) {
        this.tzKeyName = tzKeyName == null ? null : tzKeyName.trim();
    }
}