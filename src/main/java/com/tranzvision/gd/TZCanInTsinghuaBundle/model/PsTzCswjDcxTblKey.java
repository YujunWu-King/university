package com.tranzvision.gd.TZCanInTsinghuaBundle.model;

public class PsTzCswjDcxTblKey {
    private String tzCsWjId;

    private String tzDcWjId;

    private String tzXxxBh;

    public String getTzCsWjId() {
        return tzCsWjId;
    }

    public void setTzCsWjId(String tzCsWjId) {
        this.tzCsWjId = tzCsWjId == null ? null : tzCsWjId.trim();
    }

    public String getTzDcWjId() {
        return tzDcWjId;
    }

    public void setTzDcWjId(String tzDcWjId) {
        this.tzDcWjId = tzDcWjId == null ? null : tzDcWjId.trim();
    }

    public String getTzXxxBh() {
        return tzXxxBh;
    }

    public void setTzXxxBh(String tzXxxBh) {
        this.tzXxxBh = tzXxxBh == null ? null : tzXxxBh.trim();
    }
}