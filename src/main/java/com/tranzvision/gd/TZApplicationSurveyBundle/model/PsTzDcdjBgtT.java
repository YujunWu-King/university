package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcdjBgtT extends PsTzDcdjBgtTKey {
    private String tzAppSText;

    public String getTzAppSText() {
        return tzAppSText;
    }

    public void setTzAppSText(String tzAppSText) {
        this.tzAppSText = tzAppSText == null ? null : tzAppSText.trim();
    }
}