package com.tranzvision.gd.TZSystemVariableMgBundle.model;

import java.util.Date;

public class PsTzSysvarT {
    private String tzSysvarid;

    private String tzSysvarname;

    private String tzEffflg;

    private String tzSysvardesc;

    private String tzSysvartype;

    private String tzValmethod;

    private String tzAppclsReg;

    private String tzConstant;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzLngstrcont;

    public String getTzSysvarid() {
        return tzSysvarid;
    }

    public void setTzSysvarid(String tzSysvarid) {
        this.tzSysvarid = tzSysvarid == null ? null : tzSysvarid.trim();
    }

    public String getTzSysvarname() {
        return tzSysvarname;
    }

    public void setTzSysvarname(String tzSysvarname) {
        this.tzSysvarname = tzSysvarname == null ? null : tzSysvarname.trim();
    }

    public String getTzEffflg() {
        return tzEffflg;
    }

    public void setTzEffflg(String tzEffflg) {
        this.tzEffflg = tzEffflg == null ? null : tzEffflg.trim();
    }

    public String getTzSysvardesc() {
        return tzSysvardesc;
    }

    public void setTzSysvardesc(String tzSysvardesc) {
        this.tzSysvardesc = tzSysvardesc == null ? null : tzSysvardesc.trim();
    }

    public String getTzSysvartype() {
        return tzSysvartype;
    }

    public void setTzSysvartype(String tzSysvartype) {
        this.tzSysvartype = tzSysvartype == null ? null : tzSysvartype.trim();
    }

    public String getTzValmethod() {
        return tzValmethod;
    }

    public void setTzValmethod(String tzValmethod) {
        this.tzValmethod = tzValmethod == null ? null : tzValmethod.trim();
    }

    public String getTzAppclsReg() {
        return tzAppclsReg;
    }

    public void setTzAppclsReg(String tzAppclsReg) {
        this.tzAppclsReg = tzAppclsReg == null ? null : tzAppclsReg.trim();
    }

    public String getTzConstant() {
        return tzConstant;
    }

    public void setTzConstant(String tzConstant) {
        this.tzConstant = tzConstant == null ? null : tzConstant.trim();
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

    public String getTzLngstrcont() {
        return tzLngstrcont;
    }

    public void setTzLngstrcont(String tzLngstrcont) {
        this.tzLngstrcont = tzLngstrcont == null ? null : tzLngstrcont.trim();
    }
}