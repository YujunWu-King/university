package com.tranzvision.gd.TZClmsCsBiaoqzBundle.model;

public class PsTzBiaoqzTKey {
    private String tzBiaoqzId;

    private String tzJgId;

    public String getTzBiaoqzId() {
        return tzBiaoqzId;
    }

    public void setTzBiaoqzId(String tzBiaoqzId) {
        this.tzBiaoqzId = tzBiaoqzId == null ? null : tzBiaoqzId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
}