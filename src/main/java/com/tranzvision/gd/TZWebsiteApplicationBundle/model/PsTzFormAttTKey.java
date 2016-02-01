package com.tranzvision.gd.TZWebsiteApplicationBundle.model;

public class PsTzFormAttTKey {
    private Long tzAppInsId;

    private String tzXxxBh;

    private Integer tzIndex;

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

    public Integer getTzIndex() {
        return tzIndex;
    }

    public void setTzIndex(Integer tzIndex) {
        this.tzIndex = tzIndex;
    }
}