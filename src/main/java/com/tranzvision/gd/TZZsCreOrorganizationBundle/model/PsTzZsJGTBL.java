package com.tranzvision.gd.TZZsCreOrorganizationBundle.model;

import java.util.Date;

public class PsTzZsJGTBL extends PsTzZsJGTBLKey {
    private String tzCertJgName;

    private String tzLinkedinEduId;

    private String tzUseFlag;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzCertJgName() {
        return tzCertJgName;
    }

    public void setTzCertJgName(String tzCertJgName) {
        this.tzCertJgName = tzCertJgName == null ? null : tzCertJgName.trim();
    }

    public String getTzLinkedinEduId() {
        return tzLinkedinEduId;
    }

    public void setTzLinkedinEduId(String tzLinkedinEduId) {
        this.tzLinkedinEduId = tzLinkedinEduId == null ? null : tzLinkedinEduId.trim();
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