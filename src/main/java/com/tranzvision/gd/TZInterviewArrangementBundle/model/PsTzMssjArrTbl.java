package com.tranzvision.gd.TZInterviewArrangementBundle.model;

import java.util.Date;

public class PsTzMssjArrTbl extends PsTzMssjArrTblKey {
    private Short tzMsyyCount;

    private Date tzMsDate;

    private Date tzStartTm;

    private Date tzEndTm;

    private String tzMsLocation;

    private String tzMsPubSta;

    private String tzMsOpenSta;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzMsArrDemo;

    public Short getTzMsyyCount() {
        return tzMsyyCount;
    }

    public void setTzMsyyCount(Short tzMsyyCount) {
        this.tzMsyyCount = tzMsyyCount;
    }

    public Date getTzMsDate() {
        return tzMsDate;
    }

    public void setTzMsDate(Date tzMsDate) {
        this.tzMsDate = tzMsDate;
    }

    public Date getTzStartTm() {
        return tzStartTm;
    }

    public void setTzStartTm(Date tzStartTm) {
        this.tzStartTm = tzStartTm;
    }

    public Date getTzEndTm() {
        return tzEndTm;
    }

    public void setTzEndTm(Date tzEndTm) {
        this.tzEndTm = tzEndTm;
    }

    public String getTzMsLocation() {
        return tzMsLocation;
    }

    public void setTzMsLocation(String tzMsLocation) {
        this.tzMsLocation = tzMsLocation == null ? null : tzMsLocation.trim();
    }

    public String getTzMsPubSta() {
        return tzMsPubSta;
    }

    public void setTzMsPubSta(String tzMsPubSta) {
        this.tzMsPubSta = tzMsPubSta == null ? null : tzMsPubSta.trim();
    }

    public String getTzMsOpenSta() {
        return tzMsOpenSta;
    }

    public void setTzMsOpenSta(String tzMsOpenSta) {
        this.tzMsOpenSta = tzMsOpenSta == null ? null : tzMsOpenSta.trim();
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

    public String getTzMsArrDemo() {
        return tzMsArrDemo;
    }

    public void setTzMsArrDemo(String tzMsArrDemo) {
        this.tzMsArrDemo = tzMsArrDemo == null ? null : tzMsArrDemo.trim();
    }
}