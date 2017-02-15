package com.tranzvision.gd.TZClmsCsBiaoqzBundle.model;

public class PsTzBiaoqzBqTKey {
    private String tzBiaoqzId;

    private String tzJgId;

    private String tzBiaoqId;

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

    public String getTzBiaoqId() {
        return tzBiaoqId;
    }

    public void setTzBiaoqId(String tzBiaoqId) {
        this.tzBiaoqId = tzBiaoqId == null ? null : tzBiaoqId.trim();
    }
}