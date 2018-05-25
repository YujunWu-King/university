package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

public class PsTzXsxsInfoTWithBLOBs extends PsTzXsxsInfoT {
    private String tzCataLong;

    private String tzBz;

    public String getTzCataLong() {
        return tzCataLong;
    }

    public void setTzCataLong(String tzCataLong) {
        this.tzCataLong = tzCataLong == null ? null : tzCataLong.trim();
    }

    public String getTzBz() {
        return tzBz;
    }

    public void setTzBz(String tzBz) {
        this.tzBz = tzBz == null ? null : tzBz.trim();
    }
}