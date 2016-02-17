package com.tranzvision.gd.TZEventsBundle.model;

import java.util.Date;

public class PsTzArtHdTbl {
    private String tzArtId;

    private String tzNactName;

    private Date tzStartDt;

    private Date tzStartTm;

    private Date tzEndDt;

    private Date tzEndTm;

    private String tzNactAddr;

    private String tzHdCs;

    private String tzQyZxbm;

    private Date tzAppfDt;

    private Date tzAppfTm;

    private Date tzAppeDt;

    private Date tzAppeTm;

    private Integer tzXws;

    private String tzXsms;

    public String getTzArtId() {
        return tzArtId;
    }

    public void setTzArtId(String tzArtId) {
        this.tzArtId = tzArtId == null ? null : tzArtId.trim();
    }

    public String getTzNactName() {
        return tzNactName;
    }

    public void setTzNactName(String tzNactName) {
        this.tzNactName = tzNactName == null ? null : tzNactName.trim();
    }

    public Date getTzStartDt() {
        return tzStartDt;
    }

    public void setTzStartDt(Date tzStartDt) {
        this.tzStartDt = tzStartDt;
    }

    public Date getTzStartTm() {
        return tzStartTm;
    }

    public void setTzStartTm(Date tzStartTm) {
        this.tzStartTm = tzStartTm;
    }

    public Date getTzEndDt() {
        return tzEndDt;
    }

    public void setTzEndDt(Date tzEndDt) {
        this.tzEndDt = tzEndDt;
    }

    public Date getTzEndTm() {
        return tzEndTm;
    }

    public void setTzEndTm(Date tzEndTm) {
        this.tzEndTm = tzEndTm;
    }

    public String getTzNactAddr() {
        return tzNactAddr;
    }

    public void setTzNactAddr(String tzNactAddr) {
        this.tzNactAddr = tzNactAddr == null ? null : tzNactAddr.trim();
    }

    public String getTzHdCs() {
        return tzHdCs;
    }

    public void setTzHdCs(String tzHdCs) {
        this.tzHdCs = tzHdCs == null ? null : tzHdCs.trim();
    }

    public String getTzQyZxbm() {
        return tzQyZxbm;
    }

    public void setTzQyZxbm(String tzQyZxbm) {
        this.tzQyZxbm = tzQyZxbm == null ? null : tzQyZxbm.trim();
    }

    public Date getTzAppfDt() {
        return tzAppfDt;
    }

    public void setTzAppfDt(Date tzAppfDt) {
        this.tzAppfDt = tzAppfDt;
    }

    public Date getTzAppfTm() {
        return tzAppfTm;
    }

    public void setTzAppfTm(Date tzAppfTm) {
        this.tzAppfTm = tzAppfTm;
    }

    public Date getTzAppeDt() {
        return tzAppeDt;
    }

    public void setTzAppeDt(Date tzAppeDt) {
        this.tzAppeDt = tzAppeDt;
    }

    public Date getTzAppeTm() {
        return tzAppeTm;
    }

    public void setTzAppeTm(Date tzAppeTm) {
        this.tzAppeTm = tzAppeTm;
    }

    public Integer getTzXws() {
        return tzXws;
    }

    public void setTzXws(Integer tzXws) {
        this.tzXws = tzXws;
    }

    public String getTzXsms() {
        return tzXsms;
    }

    public void setTzXsms(String tzXsms) {
        this.tzXsms = tzXsms == null ? null : tzXsms.trim();
    }
}