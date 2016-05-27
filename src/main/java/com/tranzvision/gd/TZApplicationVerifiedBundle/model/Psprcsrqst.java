package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

import java.util.Date;

public class Psprcsrqst {
    private Integer prcsinstance;

    private String runId;

    private String runstatus;

    private String oprid;

    private Date rundttm;

    public Integer getPrcsinstance() {
        return prcsinstance;
    }

    public void setPrcsinstance(Integer prcsinstance) {
        this.prcsinstance = prcsinstance;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId == null ? null : runId.trim();
    }

    public String getRunstatus() {
        return runstatus;
    }

    public void setRunstatus(String runstatus) {
        this.runstatus = runstatus == null ? null : runstatus.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public Date getRundttm() {
        return rundttm;
    }

    public void setRundttm(Date rundttm) {
        this.rundttm = rundttm;
    }
}