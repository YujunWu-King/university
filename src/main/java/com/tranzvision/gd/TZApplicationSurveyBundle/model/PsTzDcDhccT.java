package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcDhccT extends PsTzDcDhccTKey {
    private String tzAppSText;

    private String tzKxxQtz;

    private String tzIsChecked;

    public String getTzAppSText() {
        return tzAppSText;
    }

    public void setTzAppSText(String tzAppSText) {
        this.tzAppSText = tzAppSText == null ? null : tzAppSText.trim();
    }

    public String getTzKxxQtz() {
        return tzKxxQtz;
    }

    public void setTzKxxQtz(String tzKxxQtz) {
        this.tzKxxQtz = tzKxxQtz == null ? null : tzKxxQtz.trim();
    }

    public String getTzIsChecked() {
        return tzIsChecked;
    }

    public void setTzIsChecked(String tzIsChecked) {
        this.tzIsChecked = tzIsChecked == null ? null : tzIsChecked.trim();
    }
}