package com.tranzvision.gd.TZClassIndividualizationBundle.model;

public class PsTzCAttrOptT extends PsTzCAttrOptTKey {
    private String tzDropDownValue;

    private String tzIsUsed;

    public String getTzDropDownValue() {
        return tzDropDownValue;
    }

    public void setTzDropDownValue(String tzDropDownValue) {
        this.tzDropDownValue = tzDropDownValue == null ? null : tzDropDownValue.trim();
    }

    public String getTzIsUsed() {
        return tzIsUsed;
    }

    public void setTzIsUsed(String tzIsUsed) {
        this.tzIsUsed = tzIsUsed == null ? null : tzIsUsed.trim();
    }
}