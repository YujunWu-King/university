package com.tranzvision.gd.TZLeaguerDataItemBundle.model;

import java.util.Date;

public class PsTzUserregMbT {
    private String tzJgId;

    private String tzFabuState;

    private String tzActivateType;

    private String tzIsShowPhoto;

    private String tzIsShowPhoto2;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzRegMbHtml;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzFabuState() {
        return tzFabuState;
    }

    public void setTzFabuState(String tzFabuState) {
        this.tzFabuState = tzFabuState == null ? null : tzFabuState.trim();
    }

    public String getTzActivateType() {
        return tzActivateType;
    }

    public void setTzActivateType(String tzActivateType) {
        this.tzActivateType = tzActivateType == null ? null : tzActivateType.trim();
    }

    public String getTzIsShowPhoto() {
        return tzIsShowPhoto;
    }

    public void setTzIsShowPhoto(String tzIsShowPhoto) {
        this.tzIsShowPhoto = tzIsShowPhoto == null ? null : tzIsShowPhoto.trim();
    }

    public String getTzIsShowPhoto2() {
        return tzIsShowPhoto2;
    }

    public void setTzIsShowPhoto2(String tzIsShowPhoto2) {
        this.tzIsShowPhoto2 = tzIsShowPhoto2 == null ? null : tzIsShowPhoto2.trim();
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

    public String getTzRegMbHtml() {
        return tzRegMbHtml;
    }

    public void setTzRegMbHtml(String tzRegMbHtml) {
        this.tzRegMbHtml = tzRegMbHtml == null ? null : tzRegMbHtml.trim();
    }
}