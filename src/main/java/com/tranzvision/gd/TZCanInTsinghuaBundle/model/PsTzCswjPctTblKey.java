package com.tranzvision.gd.TZCanInTsinghuaBundle.model;

public class PsTzCswjPctTblKey {
    private String tzCsWjId;

    private String tzDcWjId;

    private String tzXxxBh;

    private String tzXxxkxzMc;

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

    public String getTzXxxkxzMc() {
        return tzXxxkxzMc;
    }

    public void setTzXxxkxzMc(String tzXxxkxzMc) {
        this.tzXxxkxzMc = tzXxxkxzMc == null ? null : tzXxxkxzMc.trim();
    }
}