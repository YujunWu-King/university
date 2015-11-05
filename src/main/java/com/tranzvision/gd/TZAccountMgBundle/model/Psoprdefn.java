package com.tranzvision.gd.TZAccountMgBundle.model;

import java.util.Date;

public class Psoprdefn {
    private String oprid;

    private String operpswd;

    private Short acctlock;

    private Date lastupddttm;

    private String lastupdoprid;

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getOperpswd() {
        return operpswd;
    }

    public void setOperpswd(String operpswd) {
        this.operpswd = operpswd == null ? null : operpswd.trim();
    }

    public Short getAcctlock() {
        return acctlock;
    }

    public void setAcctlock(Short acctlock) {
        this.acctlock = acctlock;
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