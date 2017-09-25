package com.tranzvision.gd.TZWeChatAppletBugMBundle.model;

public class PsTzBugmgDfnTWithBLOBs extends PsTzBugmgDfnT {
    private String tzBugSm;

    private String tzBugDesc;

    private String tzBugBz;

    public String getTzBugSm() {
        return tzBugSm;
    }

    public void setTzBugSm(String tzBugSm) {
        this.tzBugSm = tzBugSm == null ? null : tzBugSm.trim();
    }

    public String getTzBugDesc() {
        return tzBugDesc;
    }

    public void setTzBugDesc(String tzBugDesc) {
        this.tzBugDesc = tzBugDesc == null ? null : tzBugDesc.trim();
    }

    public String getTzBugBz() {
        return tzBugBz;
    }

    public void setTzBugBz(String tzBugBz) {
        this.tzBugBz = tzBugBz == null ? null : tzBugBz.trim();
    }
}