package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiTempT extends PsTzSiteiTempTKey {
    private String tzTempState;

    private String tzTempName;

    private String tzTempType;

    public String getTzTempState() {
        return tzTempState;
    }

    public void setTzTempState(String tzTempState) {
        this.tzTempState = tzTempState == null ? null : tzTempState.trim();
    }

    public String getTzTempName() {
        return tzTempName;
    }

    public void setTzTempName(String tzTempName) {
        this.tzTempName = tzTempName == null ? null : tzTempName.trim();
    }

    public String getTzTempType() {
        return tzTempType;
    }

    public void setTzTempType(String tzTempType) {
        this.tzTempType = tzTempType == null ? null : tzTempType.trim();
    }
}