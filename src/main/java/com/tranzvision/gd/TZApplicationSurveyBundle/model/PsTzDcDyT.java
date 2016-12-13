package com.tranzvision.gd.TZApplicationSurveyBundle.model;

import java.util.Date;

public class PsTzDcDyT {
    private String tzAppTplId;

    private String tzAppTplLan;

    private String tzAppTplLx;

    private String tzAppTplMc;

    private String tzEffexpZt;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzJgId;

    public String getTzAppTplId() {
        return tzAppTplId;
    }

    public void setTzAppTplId(String tzAppTplId) {
        this.tzAppTplId = tzAppTplId == null ? null : tzAppTplId.trim();
    }

    public String getTzAppTplLan() {
        return tzAppTplLan;
    }

    public void setTzAppTplLan(String tzAppTplLan) {
        this.tzAppTplLan = tzAppTplLan == null ? null : tzAppTplLan.trim();
    }

    public String getTzAppTplLx() {
        return tzAppTplLx;
    }

    public void setTzAppTplLx(String tzAppTplLx) {
        this.tzAppTplLx = tzAppTplLx == null ? null : tzAppTplLx.trim();
    }

    public String getTzAppTplMc() {
        return tzAppTplMc;
    }

    public void setTzAppTplMc(String tzAppTplMc) {
        this.tzAppTplMc = tzAppTplMc == null ? null : tzAppTplMc.trim();
    }

    public String getTzEffexpZt() {
        return tzEffexpZt;
    }

    public void setTzEffexpZt(String tzEffexpZt) {
        this.tzEffexpZt = tzEffexpZt == null ? null : tzEffexpZt.trim();
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

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
}