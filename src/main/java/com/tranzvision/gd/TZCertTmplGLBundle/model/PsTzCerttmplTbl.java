package com.tranzvision.gd.TZCertTmplGLBundle.model;

import java.util.Date;

public class PsTzCerttmplTbl extends PsTzCerttmplTblKey {
    private String tzTmplName;

    private String tzCertTypeId;

    private String tzCertJgId;

    private String tzUseFlag;

    private String tzAttachsysfilena;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzTmplName() {
        return tzTmplName;
    }

    public void setTzTmplName(String tzTmplName) {
        this.tzTmplName = tzTmplName == null ? null : tzTmplName.trim();
    }

    public String getTzCertTypeId() {
        return tzCertTypeId;
    }

    public void setTzCertTypeId(String tzCertTypeId) {
        this.tzCertTypeId = tzCertTypeId == null ? null : tzCertTypeId.trim();
    }

    public String getTzCertJgId() {
        return tzCertJgId;
    }

    public void setTzCertJgId(String tzCertJgId) {
        this.tzCertJgId = tzCertJgId == null ? null : tzCertJgId.trim();
    }

    public String getTzUseFlag() {
        return tzUseFlag;
    }

    public void setTzUseFlag(String tzUseFlag) {
        this.tzUseFlag = tzUseFlag == null ? null : tzUseFlag.trim();
    }

    public String getTzAttachsysfilena() {
        return tzAttachsysfilena;
    }

    public void setTzAttachsysfilena(String tzAttachsysfilena) {
        this.tzAttachsysfilena = tzAttachsysfilena == null ? null : tzAttachsysfilena.trim();
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
}