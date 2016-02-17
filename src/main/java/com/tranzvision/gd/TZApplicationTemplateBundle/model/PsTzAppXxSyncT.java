package com.tranzvision.gd.TZApplicationTemplateBundle.model;

public class PsTzAppXxSyncT extends PsTzAppXxSyncTKey {
    private String tzQyBz;

    private Integer tzSyncOrder;

    private String tzSyncSep;

    public String getTzQyBz() {
        return tzQyBz;
    }

    public void setTzQyBz(String tzQyBz) {
        this.tzQyBz = tzQyBz == null ? null : tzQyBz.trim();
    }

    public Integer getTzSyncOrder() {
        return tzSyncOrder;
    }

    public void setTzSyncOrder(Integer tzSyncOrder) {
        this.tzSyncOrder = tzSyncOrder;
    }

    public String getTzSyncSep() {
        return tzSyncSep;
    }

    public void setTzSyncSep(String tzSyncSep) {
        this.tzSyncSep = tzSyncSep == null ? null : tzSyncSep.trim();
    }
}