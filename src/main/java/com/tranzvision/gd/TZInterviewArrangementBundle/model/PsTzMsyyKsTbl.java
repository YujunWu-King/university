package com.tranzvision.gd.TZInterviewArrangementBundle.model;

import java.util.Date;

public class PsTzMsyyKsTbl extends PsTzMsyyKsTblKey {
    private String tzMsJoinState;

    private String tzMsResult;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzMsJoinState() {
        return tzMsJoinState;
    }

    public void setTzMsJoinState(String tzMsJoinState) {
        this.tzMsJoinState = tzMsJoinState == null ? null : tzMsJoinState.trim();
    }

    public String getTzMsResult() {
        return tzMsResult;
    }

    public void setTzMsResult(String tzMsResult) {
        this.tzMsResult = tzMsResult == null ? null : tzMsResult.trim();
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