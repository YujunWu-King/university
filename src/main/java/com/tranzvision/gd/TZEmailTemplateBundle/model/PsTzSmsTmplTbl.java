package com.tranzvision.gd.TZEmailTemplateBundle.model;

import java.util.Date;

public class PsTzSmsTmplTbl extends PsTzSmsTmplTblKey {
    private String tzTmplName;

    private String tzUseFlag;

    private String tzDynamicFlag;

    private String tzYmbId;

    private String tzSmsServId;

    private String tzTmplDesc;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzSmsContent;

    public String getTzTmplName() {
        return tzTmplName;
    }

    public void setTzTmplName(String tzTmplName) {
        this.tzTmplName = tzTmplName == null ? null : tzTmplName.trim();
    }

    public String getTzUseFlag() {
        return tzUseFlag;
    }

    public void setTzUseFlag(String tzUseFlag) {
        this.tzUseFlag = tzUseFlag == null ? null : tzUseFlag.trim();
    }

    public String getTzDynamicFlag() {
        return tzDynamicFlag;
    }

    public void setTzDynamicFlag(String tzDynamicFlag) {
        this.tzDynamicFlag = tzDynamicFlag == null ? null : tzDynamicFlag.trim();
    }

    public String getTzYmbId() {
        return tzYmbId;
    }

    public void setTzYmbId(String tzYmbId) {
        this.tzYmbId = tzYmbId == null ? null : tzYmbId.trim();
    }

    public String getTzSmsServId() {
        return tzSmsServId;
    }

    public void setTzSmsServId(String tzSmsServId) {
        this.tzSmsServId = tzSmsServId == null ? null : tzSmsServId.trim();
    }

    public String getTzTmplDesc() {
        return tzTmplDesc;
    }

    public void setTzTmplDesc(String tzTmplDesc) {
        this.tzTmplDesc = tzTmplDesc == null ? null : tzTmplDesc.trim();
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

    public String getTzSmsContent() {
        return tzSmsContent;
    }

    public void setTzSmsContent(String tzSmsContent) {
        this.tzSmsContent = tzSmsContent == null ? null : tzSmsContent.trim();
    }
}