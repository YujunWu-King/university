package com.tranzvision.gd.TZWebsiteApplicationBundle.model;

import java.util.Date;

public class PsTzAppInsT {
    private Long tzAppInsId;

    private String tzAppFormSta;

    private Date tzAppSubDttm;

    private String tzAppTplId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzAppInsVersion;

    private String tzPwd;

    private String tzAppPreSta;

    private String tzAppSubMed;

    private String tzAppCreMed;

    private String tzAppinsJsonStr;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzAppFormSta() {
        return tzAppFormSta;
    }

    public void setTzAppFormSta(String tzAppFormSta) {
        this.tzAppFormSta = tzAppFormSta == null ? null : tzAppFormSta.trim();
    }

    public Date getTzAppSubDttm() {
        return tzAppSubDttm;
    }

    public void setTzAppSubDttm(Date tzAppSubDttm) {
        this.tzAppSubDttm = tzAppSubDttm;
    }

    public String getTzAppTplId() {
        return tzAppTplId;
    }

    public void setTzAppTplId(String tzAppTplId) {
        this.tzAppTplId = tzAppTplId == null ? null : tzAppTplId.trim();
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

    public String getTzAppInsVersion() {
        return tzAppInsVersion;
    }

    public void setTzAppInsVersion(String tzAppInsVersion) {
        this.tzAppInsVersion = tzAppInsVersion == null ? null : tzAppInsVersion.trim();
    }

    public String getTzPwd() {
        return tzPwd;
    }

    public void setTzPwd(String tzPwd) {
        this.tzPwd = tzPwd == null ? null : tzPwd.trim();
    }

    public String getTzAppPreSta() {
        return tzAppPreSta;
    }

    public void setTzAppPreSta(String tzAppPreSta) {
        this.tzAppPreSta = tzAppPreSta == null ? null : tzAppPreSta.trim();
    }

    public String getTzAppSubMed() {
        return tzAppSubMed;
    }

    public void setTzAppSubMed(String tzAppSubMed) {
        this.tzAppSubMed = tzAppSubMed == null ? null : tzAppSubMed.trim();
    }

    public String getTzAppCreMed() {
        return tzAppCreMed;
    }

    public void setTzAppCreMed(String tzAppCreMed) {
        this.tzAppCreMed = tzAppCreMed == null ? null : tzAppCreMed.trim();
    }

    public String getTzAppinsJsonStr() {
        return tzAppinsJsonStr;
    }

    public void setTzAppinsJsonStr(String tzAppinsJsonStr) {
        this.tzAppinsJsonStr = tzAppinsJsonStr == null ? null : tzAppinsJsonStr.trim();
    }
}