package com.tranzvision.gd.TZApplicationTemplateBundle.model;

public class PsTzRqXxxPzTKey {
    private String tzAppTplId;

    private String tzDXxxBh;

    private String tzXxxBh;

    public String getTzAppTplId() {
        return tzAppTplId;
    }

    public void setTzAppTplId(String tzAppTplId) {
        this.tzAppTplId = tzAppTplId == null ? null : tzAppTplId.trim();
    }

    public String getTzDXxxBh() {
        return tzDXxxBh;
    }

    public void setTzDXxxBh(String tzDXxxBh) {
        this.tzDXxxBh = tzDXxxBh == null ? null : tzDXxxBh.trim();
    }

    public String getTzXxxBh() {
        return tzXxxBh;
    }

    public void setTzXxxBh(String tzXxxBh) {
        this.tzXxxBh = tzXxxBh == null ? null : tzXxxBh.trim();
    }
}