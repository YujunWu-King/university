package com.tranzvision.gd.TZControlSetBundle.model;

public class PsTzComDyEng extends PsTzComDyEngKey {
    private String tzComMc;

    public String getTzComMc() {
        return tzComMc;
    }

    public void setTzComMc(String tzComMc) {
        this.tzComMc = tzComMc == null ? null : tzComMc.trim();
    }
}