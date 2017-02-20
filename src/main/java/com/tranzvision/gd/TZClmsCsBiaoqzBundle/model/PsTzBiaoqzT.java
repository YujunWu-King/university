package com.tranzvision.gd.TZClmsCsBiaoqzBundle.model;

public class PsTzBiaoqzT extends PsTzBiaoqzTKey {
    private String tzBiaoqzName;

    public String getTzBiaoqzName() {
        return tzBiaoqzName;
    }

    public void setTzBiaoqzName(String tzBiaoqzName) {
        this.tzBiaoqzName = tzBiaoqzName == null ? null : tzBiaoqzName.trim();
    }
}