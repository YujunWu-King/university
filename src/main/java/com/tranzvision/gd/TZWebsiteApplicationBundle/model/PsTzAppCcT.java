package com.tranzvision.gd.TZWebsiteApplicationBundle.model;

public class PsTzAppCcT extends PsTzAppCcTKey {
    private String tzAppSText;

    private String tzKxxQtz;

    private String tzAppLText;

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

    public String getTzAppLText() {
        return tzAppLText;
    }

    public void setTzAppLText(String tzAppLText) {
        this.tzAppLText = tzAppLText == null ? null : tzAppLText.trim();
    }
}