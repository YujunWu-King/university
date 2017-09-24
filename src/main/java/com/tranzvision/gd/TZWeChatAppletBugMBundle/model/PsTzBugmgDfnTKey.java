package com.tranzvision.gd.TZWeChatAppletBugMBundle.model;

public class PsTzBugmgDfnTKey {
    private String tzBugId;

    private String tzJgId;

    public String getTzBugId() {
        return tzBugId;
    }

    public void setTzBugId(String tzBugId) {
        this.tzBugId = tzBugId == null ? null : tzBugId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
}