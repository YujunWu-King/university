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

    private String tzOrginArtChnl;

    private String tzOrginArtId;

    private String tzOrginRefChnl;

    private String tzOrginRefartId;

    private String tzStaticName;

    private String tzStaticAotoName;

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

    public String getTzOrginArtChnl() {
        return tzOrginArtChnl;
    }

    public void setTzOrginArtChnl(String tzOrginArtChnl) {
        this.tzOrginArtChnl = tzOrginArtChnl == null ? null : tzOrginArtChnl.trim();
    }

    public String getTzOrginArtId() {
        return tzOrginArtId;
    }

    public void setTzOrginArtId(String tzOrginArtId) {
        this.tzOrginArtId = tzOrginArtId == null ? null : tzOrginArtId.trim();
    }

    public String getTzOrginRefChnl() {
        return tzOrginRefChnl;
    }

    public void setTzOrginRefChnl(String tzOrginRefChnl) {
        this.tzOrginRefChnl = tzOrginRefChnl == null ? null : tzOrginRefChnl.trim();
    }

    public String getTzOrginRefartId() {
        return tzOrginRefartId;
    }

    public void setTzOrginRefartId(String tzOrginRefartId) {
        this.tzOrginRefartId = tzOrginRefartId == null ? null : tzOrginRefartId.trim();
    }

    public String getTzStaticName() {
        return tzStaticName;
    }

    public void setTzStaticName(String tzStaticName) {
        this.tzStaticName = tzStaticName == null ? null : tzStaticName.trim();
    }

    public String getTzStaticAotoName() {
        return tzStaticAotoName;
    }

    public void setTzStaticAotoName(String tzStaticAotoName) {
        this.tzStaticAotoName = tzStaticAotoName == null ? null : tzStaticAotoName.trim();
    }
}