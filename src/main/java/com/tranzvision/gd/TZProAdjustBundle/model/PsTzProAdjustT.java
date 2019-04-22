package com.tranzvision.gd.TZProAdjustBundle.model;

import java.util.Date;

public class PsTzProAdjustT {
    private Integer tzProadjustId;

    private String tzOprid;

    private String classid;

    private String appinsid;

    private String applicationid;

    private String submitstate;

    private Date applyDate;

    private Integer state;

    public Integer getTzProadjustId() {
        return tzProadjustId;
    }

    public void setTzProadjustId(Integer tzProadjustId) {
        this.tzProadjustId = tzProadjustId;
    }

    public String getTzOprid() {
        return tzOprid;
    }

    public void setTzOprid(String tzOprid) {
        this.tzOprid = tzOprid == null ? null : tzOprid.trim();
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid == null ? null : classid.trim();
    }

    public String getAppinsid() {
        return appinsid;
    }

    public void setAppinsid(String appinsid) {
        this.appinsid = appinsid == null ? null : appinsid.trim();
    }

    public String getApplicationid() {
        return applicationid;
    }

    public void setApplicationid(String applicationid) {
        this.applicationid = applicationid == null ? null : applicationid.trim();
    }

    public String getSubmitstate() {
        return submitstate;
    }

    public void setSubmitstate(String submitstate) {
        this.submitstate = submitstate == null ? null : submitstate.trim();
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}