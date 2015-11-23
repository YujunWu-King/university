package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiColuT extends PsTzSiteiColuTKey {
    private String tzColuName;

    private String tzColuType;

    private String tzTempId;

    private String tzContType;

    private String tzContTemp;

    private String tzMenuTypeId;

    private String tzColuState;

    public String getTzColuName() {
        return tzColuName;
    }

    public void setTzColuName(String tzColuName) {
        this.tzColuName = tzColuName == null ? null : tzColuName.trim();
    }

    public String getTzColuType() {
        return tzColuType;
    }

    public void setTzColuType(String tzColuType) {
        this.tzColuType = tzColuType == null ? null : tzColuType.trim();
    }

    public String getTzTempId() {
        return tzTempId;
    }

    public void setTzTempId(String tzTempId) {
        this.tzTempId = tzTempId == null ? null : tzTempId.trim();
    }

    public String getTzContType() {
        return tzContType;
    }

    public void setTzContType(String tzContType) {
        this.tzContType = tzContType == null ? null : tzContType.trim();
    }

    public String getTzContTemp() {
        return tzContTemp;
    }

    public void setTzContTemp(String tzContTemp) {
        this.tzContTemp = tzContTemp == null ? null : tzContTemp.trim();
    }

    public String getTzMenuTypeId() {
        return tzMenuTypeId;
    }

    public void setTzMenuTypeId(String tzMenuTypeId) {
        this.tzMenuTypeId = tzMenuTypeId == null ? null : tzMenuTypeId.trim();
    }

    public String getTzColuState() {
        return tzColuState;
    }

    public void setTzColuState(String tzColuState) {
        this.tzColuState = tzColuState == null ? null : tzColuState.trim();
    }
}