package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcWjGzgxT extends PsTzDcWjGzgxTKey {
    private String tzIsSelected;

    private String tzDcWjId;

    public String getTzIsSelected() {
        return tzIsSelected;
    }

    public void setTzIsSelected(String tzIsSelected) {
        this.tzIsSelected = tzIsSelected == null ? null : tzIsSelected.trim();
    }

    public String getTzDcWjId() {
        return tzDcWjId;
    }

    public void setTzDcWjId(String tzDcWjId) {
        this.tzDcWjId = tzDcWjId == null ? null : tzDcWjId.trim();
    }
}