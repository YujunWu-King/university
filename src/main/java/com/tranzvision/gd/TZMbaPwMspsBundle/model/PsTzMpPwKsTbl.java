package com.tranzvision.gd.TZMbaPwMspsBundle.model;

import java.util.Date;

public class PsTzMpPwKsTbl extends PsTzMpPwKsTblKey {
    private Long tzScoreInsId;

    private String tzKshPspm;

    private String tzDeleteZt;

    private Date tzMshiKssj;

    private Date tzMshiJssj;

    private String tzPshenZt;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public Long getTzScoreInsId() {
        return tzScoreInsId;
    }

    public void setTzScoreInsId(Long tzScoreInsId) {
        this.tzScoreInsId = tzScoreInsId;
    }

    public String getTzKshPspm() {
        return tzKshPspm;
    }

    public void setTzKshPspm(String tzKshPspm) {
        this.tzKshPspm = tzKshPspm == null ? null : tzKshPspm.trim();
    }

    public String getTzDeleteZt() {
        return tzDeleteZt;
    }

    public void setTzDeleteZt(String tzDeleteZt) {
        this.tzDeleteZt = tzDeleteZt == null ? null : tzDeleteZt.trim();
    }

    public Date getTzMshiKssj() {
        return tzMshiKssj;
    }

    public void setTzMshiKssj(Date tzMshiKssj) {
        this.tzMshiKssj = tzMshiKssj;
    }

    public Date getTzMshiJssj() {
        return tzMshiJssj;
    }

    public void setTzMshiJssj(Date tzMshiJssj) {
        this.tzMshiJssj = tzMshiJssj;
    }

    public String getTzPshenZt() {
        return tzPshenZt;
    }

    public void setTzPshenZt(String tzPshenZt) {
        this.tzPshenZt = tzPshenZt == null ? null : tzPshenZt.trim();
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