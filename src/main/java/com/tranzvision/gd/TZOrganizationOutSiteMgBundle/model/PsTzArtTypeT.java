package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model;

import java.util.Date;

public class PsTzArtTypeT {
    private String tzArtTypeId;

    private String tzArtTypeName;

    private String isEnabledFlg;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzArtTypeId() {
        return tzArtTypeId;
    }

    public void setTzArtTypeId(String tzArtTypeId) {
        this.tzArtTypeId = tzArtTypeId == null ? null : tzArtTypeId.trim();
    }

    public String getTzArtTypeName() {
        return tzArtTypeName;
    }

    public void setTzArtTypeName(String tzArtTypeName) {
        this.tzArtTypeName = tzArtTypeName == null ? null : tzArtTypeName.trim();
    }

    public String getIsEnabledFlg() {
        return isEnabledFlg;
    }

    public void setIsEnabledFlg(String isEnabledFlg) {
        this.isEnabledFlg = isEnabledFlg == null ? null : isEnabledFlg.trim();
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