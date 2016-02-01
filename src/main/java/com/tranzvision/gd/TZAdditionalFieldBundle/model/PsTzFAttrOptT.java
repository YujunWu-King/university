package com.tranzvision.gd.TZAdditionalFieldBundle.model;

public class PsTzFAttrOptT extends PsTzFAttrOptTKey {
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