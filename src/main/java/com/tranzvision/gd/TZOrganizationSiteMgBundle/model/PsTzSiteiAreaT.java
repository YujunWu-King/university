package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

import java.util.Date;

public class PsTzSiteiAreaT extends PsTzSiteiAreaTKey {
    private String tzColuId;

    private String tzAreaState;

    private String tzAreaName;

    private String tzAreaTypeId;

    private String tzAreaType;

    private String tzAreaPosition;

    private Integer tzAreaXh;

    private Date tzAddedDttm;

    private String tzAddedOprid;

    private Date tzLastmantDttm;

    private String tzLastmantOprid;

    public String getTzColuId() {
        return tzColuId;
    }

    public void setTzColuId(String tzColuId) {
        this.tzColuId = tzColuId == null ? null : tzColuId.trim();
    }

    public String getTzAreaState() {
        return tzAreaState;
    }

    public void setTzAreaState(String tzAreaState) {
        this.tzAreaState = tzAreaState == null ? null : tzAreaState.trim();
    }

    public String getTzAreaName() {
        return tzAreaName;
    }

    public void setTzAreaName(String tzAreaName) {
        this.tzAreaName = tzAreaName == null ? null : tzAreaName.trim();
    }

    public String getTzAreaTypeId() {
        return tzAreaTypeId;
    }

    public void setTzAreaTypeId(String tzAreaTypeId) {
        this.tzAreaTypeId = tzAreaTypeId == null ? null : tzAreaTypeId.trim();
    }

    public String getTzAreaType() {
        return tzAreaType;
    }

    public void setTzAreaType(String tzAreaType) {
        this.tzAreaType = tzAreaType == null ? null : tzAreaType.trim();
    }

    public String getTzAreaPosition() {
        return tzAreaPosition;
    }

    public void setTzAreaPosition(String tzAreaPosition) {
        this.tzAreaPosition = tzAreaPosition == null ? null : tzAreaPosition.trim();
    }

    public Integer getTzAreaXh() {
        return tzAreaXh;
    }

    public void setTzAreaXh(Integer tzAreaXh) {
        this.tzAreaXh = tzAreaXh;
    }

    public Date getTzAddedDttm() {
        return tzAddedDttm;
    }

    public void setTzAddedDttm(Date tzAddedDttm) {
        this.tzAddedDttm = tzAddedDttm;
    }

    public String getTzAddedOprid() {
        return tzAddedOprid;
    }

    public void setTzAddedOprid(String tzAddedOprid) {
        this.tzAddedOprid = tzAddedOprid == null ? null : tzAddedOprid.trim();
    }

    public Date getTzLastmantDttm() {
        return tzLastmantDttm;
    }

    public void setTzLastmantDttm(Date tzLastmantDttm) {
        this.tzLastmantDttm = tzLastmantDttm;
    }

    public String getTzLastmantOprid() {
        return tzLastmantOprid;
    }

    public void setTzLastmantOprid(String tzLastmantOprid) {
        this.tzLastmantOprid = tzLastmantOprid == null ? null : tzLastmantOprid.trim();
    }
}