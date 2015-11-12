package com.tranzvision.gd.TZTranslateMgBundle.model;

public class PsTzPtZhzxxLngKey {
    private String tzZhzjhId;

    private String tzZhzId;

    private String tzLanguageId;

    public String getTzZhzjhId() {
        return tzZhzjhId;
    }

    public void setTzZhzjhId(String tzZhzjhId) {
        this.tzZhzjhId = tzZhzjhId == null ? null : tzZhzjhId.trim();
    }

    public String getTzZhzId() {
        return tzZhzId;
    }

    public void setTzZhzId(String tzZhzId) {
        this.tzZhzId = tzZhzId == null ? null : tzZhzId.trim();
    }

    public String getTzLanguageId() {
        return tzLanguageId;
    }

    public void setTzLanguageId(String tzLanguageId) {
        this.tzLanguageId = tzLanguageId == null ? null : tzLanguageId.trim();
    }
}