package com.tranzvision.gd.TZTemplateBundle.model;

public class PsTzTmpParaTblKey {
    private String tzJgId;

    private String tzYmbId;

    private String tzParaId;

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

    public String getTzParaId() {
        return tzParaId;
    }

    public void setTzParaId(String tzParaId) {
        this.tzParaId = tzParaId == null ? null : tzParaId.trim();
    }
}