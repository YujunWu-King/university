package com.tranzvision.gd.TZWeChatMaterialBundle.model;

import java.util.Date;

public class PsTzWxMediaTbl extends PsTzWxMediaTblKey {
    private String tzScName;

    private String tzScRemark;

    private String tzPubState;

    private String tzMediaId;

    private String tzImagePath;

    private String tzMediaType;

    private Date tzSyncDtime;

    private String tzMediaUrl;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzScName() {
        return tzScName;
    }

    public void setTzScName(String tzScName) {
        this.tzScName = tzScName == null ? null : tzScName.trim();
    }

    public String getTzScRemark() {
        return tzScRemark;
    }

    public void setTzScRemark(String tzScRemark) {
        this.tzScRemark = tzScRemark == null ? null : tzScRemark.trim();
    }

    public String getTzPubState() {
        return tzPubState;
    }

    public void setTzPubState(String tzPubState) {
        this.tzPubState = tzPubState == null ? null : tzPubState.trim();
    }

    public String getTzMediaId() {
        return tzMediaId;
    }

    public void setTzMediaId(String tzMediaId) {
        this.tzMediaId = tzMediaId == null ? null : tzMediaId.trim();
    }

    public String getTzImagePath() {
        return tzImagePath;
    }

    public void setTzImagePath(String tzImagePath) {
        this.tzImagePath = tzImagePath == null ? null : tzImagePath.trim();
    }

    public String getTzMediaType() {
        return tzMediaType;
    }

    public void setTzMediaType(String tzMediaType) {
        this.tzMediaType = tzMediaType == null ? null : tzMediaType.trim();
    }

    public Date getTzSyncDtime() {
        return tzSyncDtime;
    }

    public void setTzSyncDtime(Date tzSyncDtime) {
        this.tzSyncDtime = tzSyncDtime;
    }

    public String getTzMediaUrl() {
        return tzMediaUrl;
    }

    public void setTzMediaUrl(String tzMediaUrl) {
        this.tzMediaUrl = tzMediaUrl == null ? null : tzMediaUrl.trim();
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