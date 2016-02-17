package com.tranzvision.gd.TZApplicationTemplateBundle.model;

public class PsTzAppXxSyncTKey {
    private String tzAppTplId;

    private String tzXxxBh;

    private String tzSyncType;

    public String getTzAppTplId() {
        return tzAppTplId;
    }

    public void setTzAppTplId(String tzAppTplId) {
        this.tzAppTplId = tzAppTplId == null ? null : tzAppTplId.trim();
    }

    public String getTzXxxBh() {
        return tzXxxBh;
    }

    public void setTzXxxBh(String tzXxxBh) {
        this.tzXxxBh = tzXxxBh == null ? null : tzXxxBh.trim();
    }

    public String getTzSyncType() {
        return tzSyncType;
    }

    public void setTzSyncType(String tzSyncType) {
        this.tzSyncType = tzSyncType == null ? null : tzSyncType.trim();
    }
}