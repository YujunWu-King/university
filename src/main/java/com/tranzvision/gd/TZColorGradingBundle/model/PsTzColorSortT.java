package com.tranzvision.gd.TZColorGradingBundle.model;

import java.util.Date;

public class PsTzColorSortT {
    private String tzColorSortId;

    private String tzJgId;

    private String tzColorName;

    private String tzColorCode;

    private String tzColorStatus;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzColorSortId() {
        return tzColorSortId;
    }

    public void setTzColorSortId(String tzColorSortId) {
        this.tzColorSortId = tzColorSortId == null ? null : tzColorSortId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzColorName() {
        return tzColorName;
    }

    public void setTzColorName(String tzColorName) {
        this.tzColorName = tzColorName == null ? null : tzColorName.trim();
    }

    public String getTzColorCode() {
        return tzColorCode;
    }

    public void setTzColorCode(String tzColorCode) {
        this.tzColorCode = tzColorCode == null ? null : tzColorCode.trim();
    }

    public String getTzColorStatus() {
        return tzColorStatus;
    }

    public void setTzColorStatus(String tzColorStatus) {
        this.tzColorStatus = tzColorStatus == null ? null : tzColorStatus.trim();
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