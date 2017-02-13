package com.tranzvision.gd.TZCertTypeGlBundle.model;

import java.util.Date;

public class PsTzCertTypeTbl extends PsTzCertTypeTblKey {
    private String tzCertTypeName;

    private String tzUseFlag;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzCertTypeName() {
        return tzCertTypeName;
    }

    public void setTzCertTypeName(String tzCertTypeName) {
        this.tzCertTypeName = tzCertTypeName == null ? null : tzCertTypeName.trim();
    }

    public String getTzUseFlag() {
        return tzUseFlag;
    }

    public void setTzUseFlag(String tzUseFlag) {
        this.tzUseFlag = tzUseFlag == null ? null : tzUseFlag.trim();
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