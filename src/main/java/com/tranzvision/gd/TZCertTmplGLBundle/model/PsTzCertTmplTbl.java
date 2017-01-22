package com.tranzvision.gd.TZCertTmplGLBundle.model;

import java.util.Date;

public class PsTzCertTmplTbl {
	private String tzJgId;
	
    private String tzCertTmpl;

    private String tzTmplName;

    private String tzCertJGID;

    private String tzCertMergHtml1;
    
    private String  tzCertMergHtml2;
    
    private String  tzCertMergHtml3;
    
    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;
    
    public String getTzJgId() {
        return tzJgId;
    }


    public void setTzJgId(String tzAppclsId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
    
    public String getTzCertTmpl() {
        return tzCertTmpl;
    }

    public void setTzCertTmpl(String tzAppclsId) {
        this.tzCertTmpl = tzCertTmpl == null ? null : tzCertTmpl.trim();
    }

    public String getTzTmplName() {
        return tzTmplName;
    }

    public void setTzTmplName(String tzTmplName) {
        this.tzTmplName = tzTmplName == null ? null : tzTmplName.trim();
    }

    public String getTzCertJGID() {
        return tzCertJGID;
    }

    public void setTzCertJGID(String tzCertJGID) {
        this.tzCertJGID = tzCertJGID == null ? null : tzCertJGID.trim();
    }

    public String getTzCertMergHtml1() {
        return tzCertMergHtml1;
    }

    public void setTzCertMergHtml1(String tzCertMergHtml1) {
        this.tzCertMergHtml1 = tzCertMergHtml1 == null ? null : tzCertMergHtml1.trim();
    }

    public String getTzCertMergHtml2() {
        return tzCertMergHtml2;
    }

    public void setTzCertMergHtml2(String tzCertMergHtml2) {
        this.tzCertMergHtml2 = tzCertMergHtml2 == null ? null : tzCertMergHtml2.trim();
    }

    public String getTzCertMergHtml3() {
        return tzCertMergHtml3;
    }

    public void setTzCertMergHtml3(String tzCertMergHtml3) {
        this.tzCertMergHtml3 = tzCertMergHtml3== null ? null : tzCertMergHtml3.trim();
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
}