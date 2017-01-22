package com.tranzvision.gd.TZImportTplMgBundle.model;

public class TzImpTplFldT extends TzImpTplFldTKey {
    private String tzFieldName;

    private Integer tzSeq;

    private String tzRequired;

    private String tzCover;

    private String tzDisplay;

    private String tzColTitle;

    public String getTzFieldName() {
        return tzFieldName;
    }

    public void setTzFieldName(String tzFieldName) {
        this.tzFieldName = tzFieldName == null ? null : tzFieldName.trim();
    }

    public Integer getTzSeq() {
        return tzSeq;
    }

    public void setTzSeq(Integer tzSeq) {
        this.tzSeq = tzSeq;
    }

    public String getTzRequired() {
        return tzRequired;
    }

    public void setTzRequired(String tzRequired) {
        this.tzRequired = tzRequired == null ? null : tzRequired.trim();
    }

    public String getTzCover() {
        return tzCover;
    }

    public void setTzCover(String tzCover) {
        this.tzCover = tzCover == null ? null : tzCover.trim();
    }

    public String getTzDisplay() {
        return tzDisplay;
    }

    public void setTzDisplay(String tzDisplay) {
        this.tzDisplay = tzDisplay == null ? null : tzDisplay.trim();
    }

    public String getTzColTitle() {
        return tzColTitle;
    }

    public void setTzColTitle(String tzColTitle) {
        this.tzColTitle = tzColTitle == null ? null : tzColTitle.trim();
    }
}