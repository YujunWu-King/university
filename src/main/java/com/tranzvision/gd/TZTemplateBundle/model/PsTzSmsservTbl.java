package com.tranzvision.gd.TZTemplateBundle.model;

import java.util.Date;

public class PsTzSmsservTbl {
    private String tzSmsServId;

    private String tzSmsServName;

    private String tzSmsUsrName;

    private String tzSmsUsrPwd;

    private String tzSourceNo;

    private String tzDescr254;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzSmsServId() {
        return tzSmsServId;
    }

    public void setTzSmsServId(String tzSmsServId) {
        this.tzSmsServId = tzSmsServId == null ? null : tzSmsServId.trim();
    }

    public String getTzSmsServName() {
        return tzSmsServName;
    }

    public void setTzSmsServName(String tzSmsServName) {
        this.tzSmsServName = tzSmsServName == null ? null : tzSmsServName.trim();
    }

    public String getTzSmsUsrName() {
        return tzSmsUsrName;
    }

    public void setTzSmsUsrName(String tzSmsUsrName) {
        this.tzSmsUsrName = tzSmsUsrName == null ? null : tzSmsUsrName.trim();
    }

    public String getTzSmsUsrPwd() {
        return tzSmsUsrPwd;
    }

    public void setTzSmsUsrPwd(String tzSmsUsrPwd) {
        this.tzSmsUsrPwd = tzSmsUsrPwd == null ? null : tzSmsUsrPwd.trim();
    }

    public String getTzSourceNo() {
        return tzSourceNo;
    }

    public void setTzSourceNo(String tzSourceNo) {
        this.tzSourceNo = tzSourceNo == null ? null : tzSourceNo.trim();
    }

    public String getTzDescr254() {
        return tzDescr254;
    }

    public void setTzDescr254(String tzDescr254) {
        this.tzDescr254 = tzDescr254 == null ? null : tzDescr254.trim();
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