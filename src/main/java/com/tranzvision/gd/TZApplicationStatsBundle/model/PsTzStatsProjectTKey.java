package com.tranzvision.gd.TZApplicationStatsBundle.model;

public class PsTzStatsProjectTKey {
    private Long tzStatsId;

    private String tzClassId;

    public Long getTzStatsId() {
        return tzStatsId;
    }

    public void setTzStatsId(Long tzStatsId) {
        this.tzStatsId = tzStatsId;
    }

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }
}