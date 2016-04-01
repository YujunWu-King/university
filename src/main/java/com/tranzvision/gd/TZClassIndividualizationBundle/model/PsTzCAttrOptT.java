package com.tranzvision.gd.TZClassIndividualizationBundle.model;

public class PsTzCAttrOptT extends PsTzCAttrOptTKey {
    private String tzDropDownValue;

    private String tzIsUsed;

    private Integer tzOrder;

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

    public Integer getTzOrder() {
        return tzOrder;
    }

    public void setTzOrder(Integer tzOrder) {
        this.tzOrder = tzOrder;
    }
}