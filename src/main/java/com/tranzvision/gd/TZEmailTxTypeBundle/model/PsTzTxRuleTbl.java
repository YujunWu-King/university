package com.tranzvision.gd.TZEmailTxTypeBundle.model;

public class PsTzTxRuleTbl {
    private String tzTxRuleId;

    private String tzRuleDesc;

    private String tzTxPplx;

    private String tzTxitemKey;

    private String tzIsUsed;

    public String getTzTxRuleId() {
        return tzTxRuleId;
    }

    public void setTzTxRuleId(String tzTxRuleId) {
        this.tzTxRuleId = tzTxRuleId == null ? null : tzTxRuleId.trim();
    }

    public String getTzRuleDesc() {
        return tzRuleDesc;
    }

    public void setTzRuleDesc(String tzRuleDesc) {
        this.tzRuleDesc = tzRuleDesc == null ? null : tzRuleDesc.trim();
    }

    public String getTzTxPplx() {
        return tzTxPplx;
    }

    public void setTzTxPplx(String tzTxPplx) {
        this.tzTxPplx = tzTxPplx == null ? null : tzTxPplx.trim();
    }

    public String getTzTxitemKey() {
        return tzTxitemKey;
    }

    public void setTzTxitemKey(String tzTxitemKey) {
        this.tzTxitemKey = tzTxitemKey == null ? null : tzTxitemKey.trim();
    }

    public String getTzIsUsed() {
        return tzIsUsed;
    }

    public void setTzIsUsed(String tzIsUsed) {
        this.tzIsUsed = tzIsUsed == null ? null : tzIsUsed.trim();
    }
}