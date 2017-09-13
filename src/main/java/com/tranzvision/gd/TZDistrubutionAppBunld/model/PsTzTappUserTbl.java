package com.tranzvision.gd.TZDistrubutionAppBunld.model;

public class PsTzTappUserTbl extends PsTzTappUserTblKey {
    private String tzOthName;

    private String tzDlzhId;

    private String tzEnable;

    public String getTzOthName() {
        return tzOthName;
    }

    public void setTzOthName(String tzOthName) {
        this.tzOthName = tzOthName == null ? null : tzOthName.trim();
    }

    public String getTzDlzhId() {
        return tzDlzhId;
    }

    public void setTzDlzhId(String tzDlzhId) {
        this.tzDlzhId = tzDlzhId == null ? null : tzDlzhId.trim();
    }

    public String getTzEnable() {
        return tzEnable;
    }

    public void setTzEnable(String tzEnable) {
        this.tzEnable = tzEnable == null ? null : tzEnable.trim();
    }
}