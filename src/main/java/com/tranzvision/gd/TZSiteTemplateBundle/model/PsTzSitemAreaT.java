package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemAreaT extends PsTzSitemAreaTKey {
    private String tzColuId;

    private String tzAreaState;

    private String tzAreaName;

    private String tzAreaTypeId;

    private String tzAreaPosition;

    private Integer tzAreaXh;

    private String tzAreaCode;

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

    public String getTzAreaCode() {
        return tzAreaCode;
    }

    public void setTzAreaCode(String tzAreaCode) {
        this.tzAreaCode = tzAreaCode == null ? null : tzAreaCode.trim();
    }
}