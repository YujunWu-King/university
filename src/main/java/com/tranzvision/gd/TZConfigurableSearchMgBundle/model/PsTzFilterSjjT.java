package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFilterSjjT extends PsTzFilterSjjTKey {
    private String tzFilterFld;

    private String tzQueryViewName;

    private String tzDpqDesc;

    private String tzDpqState;

    private String tzDefaultFlg;

    public String getTzFilterFld() {
        return tzFilterFld;
    }

    public void setTzFilterFld(String tzFilterFld) {
        this.tzFilterFld = tzFilterFld == null ? null : tzFilterFld.trim();
    }

    public String getTzQueryViewName() {
        return tzQueryViewName;
    }

    public void setTzQueryViewName(String tzQueryViewName) {
        this.tzQueryViewName = tzQueryViewName == null ? null : tzQueryViewName.trim();
    }

    public String getTzDpqDesc() {
        return tzDpqDesc;
    }

    public void setTzDpqDesc(String tzDpqDesc) {
        this.tzDpqDesc = tzDpqDesc == null ? null : tzDpqDesc.trim();
    }

    public String getTzDpqState() {
        return tzDpqState;
    }

    public void setTzDpqState(String tzDpqState) {
        this.tzDpqState = tzDpqState == null ? null : tzDpqState.trim();
    }

    public String getTzDefaultFlg() {
        return tzDefaultFlg;
    }

    public void setTzDefaultFlg(String tzDefaultFlg) {
        this.tzDefaultFlg = tzDefaultFlg == null ? null : tzDefaultFlg.trim();
    }
}