package com.tranzvision.gd.TZAutomaticScreenBundle.model;

import java.util.Date;

public class PsTzCsKsTbl extends PsTzCsKsTblKey {
    private Long tzScoreInsId;

    private String tzKshCsjg;

    private Short tzKshPspm;

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

    public String getTzKshCsjg() {
        return tzKshCsjg;
    }

    public void setTzKshCsjg(String tzKshCsjg) {
        this.tzKshCsjg = tzKshCsjg == null ? null : tzKshCsjg.trim();
    }

    public Short getTzKshPspm() {
        return tzKshPspm;
    }

    public void setTzKshPspm(Short tzKshPspm) {
        this.tzKshPspm = tzKshPspm;
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