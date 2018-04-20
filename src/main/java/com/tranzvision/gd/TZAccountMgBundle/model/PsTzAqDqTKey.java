package com.tranzvision.gd.TZAccountMgBundle.model;

public class PsTzAqDqTKey {
    private String oprid;

    private String tzTypeLabel;

    private String tzAqdqLabel;

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzTypeLabel() {
        return tzTypeLabel;
    }

    public void setTzTypeLabel(String tzTypeLabel) {
        this.tzTypeLabel = tzTypeLabel == null ? null : tzTypeLabel.trim();
    }

    public String getTzAqdqLabel() {
        return tzAqdqLabel;
    }

    public void setTzAqdqLabel(String tzAqdqLabel) {
        this.tzAqdqLabel = tzAqdqLabel == null ? null : tzAqdqLabel.trim();
    }
}