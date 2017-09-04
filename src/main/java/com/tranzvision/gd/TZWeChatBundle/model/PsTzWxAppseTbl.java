package com.tranzvision.gd.TZWeChatBundle.model;

import java.util.Date;

public class PsTzWxAppseTbl extends PsTzWxAppseTblKey {
    private String tzWxSecret;

    private String tzWxName;

    private String tzFromPvalue;

    private String tzWxState;
    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzWxSecret() {
        return tzWxSecret;
    }

    public void setTzWxSecret(String tzWxSecret) {
        this.tzWxSecret = tzWxSecret == null ? null : tzWxSecret.trim();
    }

    public String getTzWxName() {
        return tzWxName;
    }

    public void setTzWxName(String tzWxName) {
        this.tzWxName = tzWxName == null ? null : tzWxName.trim();
    }

    public String getTzFromPvalue() {
        return tzFromPvalue;
    }

    public void setTzFromPvalue(String tzFromPvalue) {
        this.tzFromPvalue = tzFromPvalue == null ? null : tzFromPvalue.trim();
    }

    public String getTzWxState() {
        return tzWxState;
    }

    public void setTzWxState(String tzWxState) {
        this.tzWxState = tzWxState == null ? null : tzWxState.trim();
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