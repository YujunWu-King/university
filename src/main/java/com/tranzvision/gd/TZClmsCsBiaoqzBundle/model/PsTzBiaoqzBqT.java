package com.tranzvision.gd.TZClmsCsBiaoqzBundle.model;

public class PsTzBiaoqzBqT extends PsTzBiaoqzBqTKey {
    private String tzBiaoqzName;

    private String tzDesc;

    private String tzBiaoqzJava;

    public String getTzBiaoqzName() {
        return tzBiaoqzName;
    }

    public void setTzBiaoqzName(String tzBiaoqzName) {
        this.tzBiaoqzName = tzBiaoqzName == null ? null : tzBiaoqzName.trim();
    }

    public String getTzDesc() {
        return tzDesc;
    }

    public void setTzDesc(String tzDesc) {
        this.tzDesc = tzDesc == null ? null : tzDesc.trim();
    }

    public String getTzBiaoqzJava() {
        return tzBiaoqzJava;
    }

    public void setTzBiaoqzJava(String tzBiaoqzJava) {
        this.tzBiaoqzJava = tzBiaoqzJava == null ? null : tzBiaoqzJava.trim();
    }
}