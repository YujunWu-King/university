package com.tranzvision.gd.TZEmailSmsSendBundle.model;

import java.util.Date;

public class PsTzAudienceT {
    private String tzAudienceId;

    private String tzAudMs;

    private String tzAudLy;

    private String tzJgId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzAudienceId() {
        return tzAudienceId;
    }

    public void setTzAudienceId(String tzAudienceId) {
        this.tzAudienceId = tzAudienceId == null ? null : tzAudienceId.trim();
    }

    public String getTzAudMs() {
        return tzAudMs;
    }

    public void setTzAudMs(String tzAudMs) {
        this.tzAudMs = tzAudMs == null ? null : tzAudMs.trim();
    }

    public String getTzAudLy() {
        return tzAudLy;
    }

    public void setTzAudLy(String tzAudLy) {
        this.tzAudLy = tzAudLy == null ? null : tzAudLy.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
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