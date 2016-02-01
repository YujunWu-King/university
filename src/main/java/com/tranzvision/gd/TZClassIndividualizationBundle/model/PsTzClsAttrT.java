package com.tranzvision.gd.TZClassIndividualizationBundle.model;

import java.util.Date;

public class PsTzClsAttrT extends PsTzClsAttrTKey {
    private Integer tzSortNum;

    private String tzAttributeName;

    private String tzControlType;

    private String tzIsUsed;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public Integer getTzSortNum() {
        return tzSortNum;
    }

    public void setTzSortNum(Integer tzSortNum) {
        this.tzSortNum = tzSortNum;
    }

    public String getTzAttributeName() {
        return tzAttributeName;
    }

    public void setTzAttributeName(String tzAttributeName) {
        this.tzAttributeName = tzAttributeName == null ? null : tzAttributeName.trim();
    }

    public String getTzControlType() {
        return tzControlType;
    }

    public void setTzControlType(String tzControlType) {
        this.tzControlType = tzControlType == null ? null : tzControlType.trim();
    }

    public String getTzIsUsed() {
        return tzIsUsed;
    }

    public void setTzIsUsed(String tzIsUsed) {
        this.tzIsUsed = tzIsUsed == null ? null : tzIsUsed.trim();
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