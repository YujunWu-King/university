package com.tranzvision.gd.TZUniPrintBundle.model;

public class TzDymbYsTblKey {
    private String tzJgId;

    private String tzDymbId;

    private String tzDymbFieldId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzDymbId() {
        return tzDymbId;
    }

    public void setTzDymbId(String tzDymbId) {
        this.tzDymbId = tzDymbId == null ? null : tzDymbId.trim();
    }

    public String getTzDymbFieldId() {
        return tzDymbFieldId;
    }

    public void setTzDymbFieldId(String tzDymbFieldId) {
        this.tzDymbFieldId = tzDymbFieldId == null ? null : tzDymbFieldId.trim();
    }
}