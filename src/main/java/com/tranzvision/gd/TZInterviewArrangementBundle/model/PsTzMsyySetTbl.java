package com.tranzvision.gd.TZInterviewArrangementBundle.model;

import java.util.Date;

public class PsTzMsyySetTbl extends PsTzMsyySetTblKey {
    private Date tzOpenDt;

    private Date tzOpenTm;

    private Date tzCloseDt;

    private Date tzCloseTm;

    private String tzOpenSta;

    private String tzShowFront;

    private String tzDescr;

    private String tzMaterial;
    public Date getTzOpenDt() {
        return tzOpenDt;
    }

    public void setTzOpenDt(Date tzOpenDt) {
        this.tzOpenDt = tzOpenDt;
    }

    public Date getTzOpenTm() {
        return tzOpenTm;
    }

    public void setTzOpenTm(Date tzOpenTm) {
        this.tzOpenTm = tzOpenTm;
    }

    public Date getTzCloseDt() {
        return tzCloseDt;
    }

    public void setTzCloseDt(Date tzCloseDt) {
        this.tzCloseDt = tzCloseDt;
    }

    public Date getTzCloseTm() {
        return tzCloseTm;
    }

    public void setTzCloseTm(Date tzCloseTm) {
        this.tzCloseTm = tzCloseTm;
    }

    public String getTzOpenSta() {
        return tzOpenSta;
    }

    public void setTzOpenSta(String tzOpenSta) {
        this.tzOpenSta = tzOpenSta == null ? null : tzOpenSta.trim();
    }

    public String getTzShowFront() {
        return tzShowFront;
    }

    public void setTzShowFront(String tzShowFront) {
        this.tzShowFront = tzShowFront == null ? null : tzShowFront.trim();
    }

    public String getTzDescr() {
        return tzDescr;
    }

    public void setTzDescr(String tzDescr) {
        this.tzDescr = tzDescr == null ? null : tzDescr.trim();
    }

	public String getTzMaterial() {
		return tzMaterial;
	}

	public void setTzMaterial(String tzMaterial) {
		this.tzMaterial = tzMaterial;
	}
    
    
}