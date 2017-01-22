package com.tranzvision.gd.TZScoreModeManagementBundle.model;

import java.util.Date;

public class PsTzRsModalTbl extends PsTzRsModalTblKey {
    private String tzModalName;

    private String treeName;

    private String tzModalFlag;

    private String tzMFbdzId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzModalName() {
        return tzModalName;
    }

    public void setTzModalName(String tzModalName) {
        this.tzModalName = tzModalName == null ? null : tzModalName.trim();
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName == null ? null : treeName.trim();
    }

    public String getTzModalFlag() {
        return tzModalFlag;
    }

    public void setTzModalFlag(String tzModalFlag) {
        this.tzModalFlag = tzModalFlag == null ? null : tzModalFlag.trim();
    }

    public String getTzMFbdzId() {
        return tzMFbdzId;
    }

    public void setTzMFbdzId(String tzMFbdzId) {
        this.tzMFbdzId = tzMFbdzId == null ? null : tzMFbdzId.trim();
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