package com.tranzvision.gd.TZApplicationProcessBundle.model;

public class PsTzAppProHfTKey {
    private String tzAppproTmpId;

    private String tzAppproId;

    private String tzAppproHfBh;

    public String getTzAppproTmpId() {
        return tzAppproTmpId;
    }

    public void setTzAppproTmpId(String tzAppproTmpId) {
        this.tzAppproTmpId = tzAppproTmpId == null ? null : tzAppproTmpId.trim();
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