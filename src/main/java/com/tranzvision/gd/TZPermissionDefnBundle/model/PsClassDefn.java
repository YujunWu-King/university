package com.tranzvision.gd.TZPermissionDefnBundle.model;

import java.util.Date;

public class PsClassDefn {
    private String classid;

    private Integer version;

    private String classdefndesc;

    private Integer timeoutminutes;

    private String defaultbpm;

    private Boolean startappserver;

    private Boolean allowpswdemail;

    private Date lastupddttm;

    private String lastupdoprid;

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid == null ? null : classid.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getClassdefndesc() {
        return classdefndesc;
    }

    public void setClassdefndesc(String classdefndesc) {
        this.classdefndesc = classdefndesc == null ? null : classdefndesc.trim();
    }

    public Integer getTimeoutminutes() {
        return timeoutminutes;
    }

    public void setTimeoutminutes(Integer timeoutminutes) {
        this.timeoutminutes = timeoutminutes;
    }

    public String getDefaultbpm() {
        return defaultbpm;
    }

    public void setDefaultbpm(String defaultbpm) {
        this.defaultbpm = defaultbpm == null ? null : defaultbpm.trim();
    }

    public Boolean getStartappserver() {
        return startappserver;
    }

    public void setStartappserver(Boolean startappserver) {
        this.startappserver = startappserver;
    }

    public Boolean getAllowpswdemail() {
        return allowpswdemail;
    }

    public void setAllowpswdemail(Boolean allowpswdemail) {
        this.allowpswdemail = allowpswdemail;
    }

    public Date getLastupddttm() {
        return lastupddttm;
    }

    public void setLastupddttm(Date lastupddttm) {
        this.lastupddttm = lastupddttm;
    }

    public String getLastupdoprid() {
        return lastupdoprid;
    }

    public void setLastupdoprid(String lastupdoprid) {
        this.lastupdoprid = lastupdoprid == null ? null : lastupdoprid.trim();
    }
}