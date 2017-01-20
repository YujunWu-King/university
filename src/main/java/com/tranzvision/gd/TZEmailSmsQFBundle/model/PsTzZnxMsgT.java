package com.tranzvision.gd.TZEmailSmsQFBundle.model;

import java.util.Date;

public class PsTzZnxMsgT {
    private String tzZnxMsgid;

    private String tzZnxSendid;

    private String tzMsgSubject;

    private String tzMlsmQfpcId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzZnxMsgid() {
        return tzZnxMsgid;
    }

    public void setTzZnxMsgid(String tzZnxMsgid) {
        this.tzZnxMsgid = tzZnxMsgid == null ? null : tzZnxMsgid.trim();
    }

    public String getTzZnxSendid() {
        return tzZnxSendid;
    }

    public void setTzZnxSendid(String tzZnxSendid) {
        this.tzZnxSendid = tzZnxSendid == null ? null : tzZnxSendid.trim();
    }

    public String getTzMsgSubject() {
        return tzMsgSubject;
    }

    public void setTzMsgSubject(String tzMsgSubject) {
        this.tzMsgSubject = tzMsgSubject == null ? null : tzMsgSubject.trim();
    }

    public String getTzMlsmQfpcId() {
        return tzMlsmQfpcId;
    }

    public void setTzMlsmQfpcId(String tzMlsmQfpcId) {
        this.tzMlsmQfpcId = tzMlsmQfpcId == null ? null : tzMlsmQfpcId.trim();
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