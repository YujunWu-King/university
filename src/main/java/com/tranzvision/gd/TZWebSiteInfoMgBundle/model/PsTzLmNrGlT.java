package com.tranzvision.gd.TZWebSiteInfoMgBundle.model;

import java.util.Date;

public class PsTzLmNrGlT extends PsTzLmNrGlTKey {
    private Date tzArtNewsDt;

    private String tzArtPubState;

    private String tzStaticArtUrl;

    private Integer tzArtSeq;

    private Integer tzMaxZdSeq;

    private String tzFbz;

    private String tzBltDept;

    private String tzLastmantOprid;

    private Date tzLastmantDttm;

    public Date getTzArtNewsDt() {
        return tzArtNewsDt;
    }

    public void setTzArtNewsDt(Date tzArtNewsDt) {
        this.tzArtNewsDt = tzArtNewsDt;
    }

    public String getTzArtPubState() {
        return tzArtPubState;
    }

    public void setTzArtPubState(String tzArtPubState) {
        this.tzArtPubState = tzArtPubState == null ? null : tzArtPubState.trim();
    }

    public String getTzStaticArtUrl() {
        return tzStaticArtUrl;
    }

    public void setTzStaticArtUrl(String tzStaticArtUrl) {
        this.tzStaticArtUrl = tzStaticArtUrl == null ? null : tzStaticArtUrl.trim();
    }

    public Integer getTzArtSeq() {
        return tzArtSeq;
    }

    public void setTzArtSeq(Integer tzArtSeq) {
        this.tzArtSeq = tzArtSeq;
    }

    public Integer getTzMaxZdSeq() {
        return tzMaxZdSeq;
    }

    public void setTzMaxZdSeq(Integer tzMaxZdSeq) {
        this.tzMaxZdSeq = tzMaxZdSeq;
    }

    public String getTzFbz() {
        return tzFbz;
    }

    public void setTzFbz(String tzFbz) {
        this.tzFbz = tzFbz == null ? null : tzFbz.trim();
    }

    public String getTzBltDept() {
        return tzBltDept;
    }

    public void setTzBltDept(String tzBltDept) {
        this.tzBltDept = tzBltDept == null ? null : tzBltDept.trim();
    }

    public String getTzLastmantOprid() {
        return tzLastmantOprid;
    }

    public void setTzLastmantOprid(String tzLastmantOprid) {
        this.tzLastmantOprid = tzLastmantOprid == null ? null : tzLastmantOprid.trim();
    }

    public Date getTzLastmantDttm() {
        return tzLastmantDttm;
    }

    public void setTzLastmantDttm(Date tzLastmantDttm) {
        this.tzLastmantDttm = tzLastmantDttm;
    }
}