package com.tranzvision.gd.TZClassSetBundle.model;

public class PsTzClsBmlchfTKey {
    private String tzClassId;

    private String tzAppproId;

    private String tzAppproHfBh;

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }

    public String getTzAppproId() {
        return tzAppproId;
    }

    public void setTzAppproId(String tzAppproId) {
        this.tzAppproId = tzAppproId == null ? null : tzAppproId.trim();
    }

    public String getTzAppproHfBh() {
        return tzAppproHfBh;
    }

    public void setTzAppproHfBh(String tzAppproHfBh) {
        this.tzAppproHfBh = tzAppproHfBh == null ? null : tzAppproHfBh.trim();
    }
}