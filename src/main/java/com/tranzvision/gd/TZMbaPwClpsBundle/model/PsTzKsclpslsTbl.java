package com.tranzvision.gd.TZMbaPwClpsBundle.model;

import java.util.Date;

public class PsTzKsclpslsTbl extends PsTzKsclpslsTblKey {
    private String tzGuanxLeix;

    private String tzKshPspm;

    private String tzSubmitYn;

    private String tzIsPwFp;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzGuanxLeix() {
        return tzGuanxLeix;
    }

    public void setTzGuanxLeix(String tzGuanxLeix) {
        this.tzGuanxLeix = tzGuanxLeix == null ? null : tzGuanxLeix.trim();
    }

    public String getTzKshPspm() {
        return tzKshPspm;
    }

    public void setTzKshPspm(String tzKshPspm) {
        this.tzKshPspm = tzKshPspm == null ? null : tzKshPspm.trim();
    }

    public String getTzSubmitYn() {
        return tzSubmitYn;
    }

    public void setTzSubmitYn(String tzSubmitYn) {
        this.tzSubmitYn = tzSubmitYn == null ? null : tzSubmitYn.trim();
    }

    public String getTzIsPwFp() {
        return tzIsPwFp;
    }

    public void setTzIsPwFp(String tzIsPwFp) {
        this.tzIsPwFp = tzIsPwFp == null ? null : tzIsPwFp.trim();
    }

    public Date getRowAddedDttm() {
        return rowAddedDttm;
    }

    public void setRowAddedDttm(Date rowAddedDttm) {
        this.rowAddedDttm = rowAddedDttm;
    }

    public String getRowAddedOprid() {
        return rowAddedOprid;
    }

    public void setRowAddedOprid(String rowAddedOprid) {
        this.rowAddedOprid = rowAddedOprid == null ? null : rowAddedOprid.trim();
    }

    public Date getRowLastmantDttm() {
        return rowLastmantDttm;
    }

    public void setRowLastmantDttm(Date rowLastmantDttm) {
        this.rowLastmantDttm = rowLastmantDttm;
    }

    public String getRowLastmantOprid() {
        return rowLastmantOprid;
    }

    public void setRowLastmantOprid(String rowLastmantOprid) {
        this.rowLastmantOprid = rowLastmantOprid == null ? null : rowLastmantOprid.trim();
    }

    public Integer getSyncid() {
        return syncid;
    }

    public void setSyncid(Integer syncid) {
        this.syncid = syncid;
    }

    public Date getSyncdttm() {
        return syncdttm;
    }

    public void setSyncdttm(Date syncdttm) {
        this.syncdttm = syncdttm;
    }
}