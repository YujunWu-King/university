package com.tranzvision.gd.TZWebsiteApplicationBundle.model;

public class PsTzAppCompTblKey {
    private Long tzAppInsId;

    private String tzXxxBh;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzXxxBh() {
        return tzXxxBh;
    }

    public void setTzXxxBh(String tzXxxBh) {
        this.tzXxxBh = tzXxxBh == null ? null : tzXxxBh.trim();
    }
}