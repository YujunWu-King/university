package com.tranzvision.gd.TZAutomaticTagBundle.model;

public class PsQynDbblKsTKey {
    private String tzPrjId;

    private String tzKsssyear;

    private String tzMshId;

    public String getTzPrjId() {
        return tzPrjId;
    }

    public void setTzPrjId(String tzPrjId) {
        this.tzPrjId = tzPrjId == null ? null : tzPrjId.trim();
    }

    public String getTzKsssyear() {
        return tzKsssyear;
    }

    public void setTzKsssyear(String tzKsssyear) {
        this.tzKsssyear = tzKsssyear == null ? null : tzKsssyear.trim();
    }

    public String getTzMshId() {
        return tzMshId;
    }

    public void setTzMshId(String tzMshId) {
        this.tzMshId = tzMshId == null ? null : tzMshId.trim();
    }
}