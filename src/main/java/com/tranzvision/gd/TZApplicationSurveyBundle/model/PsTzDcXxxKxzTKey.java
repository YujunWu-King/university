package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcXxxKxzTKey {
    private String tzAppTplId;

    private String tzXxxBh;

    private String tzXxxkxzMc;

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

    public String getTzXxxkxzMc() {
        return tzXxxkxzMc;
    }

    public void setTzXxxkxzMc(String tzXxxkxzMc) {
        this.tzXxxkxzMc = tzXxxkxzMc == null ? null : tzXxxkxzMc.trim();
    }
}