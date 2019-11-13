package com.tranzvision.gd.TZCostRelatedAEBundle.model;

public class TzKsJxjTKey {
    private Long tzAppInsId;

    private String tzJxjId;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzJxjId() {
        return tzJxjId;
    }

    public void setTzJxjId(String tzJxjId) {
        this.tzJxjId = tzJxjId == null ? null : tzJxjId.trim();
    }
}