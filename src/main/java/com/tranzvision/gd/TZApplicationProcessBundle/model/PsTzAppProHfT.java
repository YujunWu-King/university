package com.tranzvision.gd.TZApplicationProcessBundle.model;

public class PsTzAppProHfT extends PsTzAppProHfTKey {
    private String tzSysvar;

    private String tzAppproColor;

    private String tzClsResult;

    private String tzWfbDefaltBz;

    private String tzAppproContent;

    public String getTzSysvar() {
        return tzSysvar;
    }

    public void setTzSysvar(String tzSysvar) {
        this.tzSysvar = tzSysvar == null ? null : tzSysvar.trim();
    }

    public String getTzAppproColor() {
        return tzAppproColor;
    }

    public void setTzAppproColor(String tzAppproColor) {
        this.tzAppproColor = tzAppproColor == null ? null : tzAppproColor.trim();
    }

    public String getTzClsResult() {
        return tzClsResult;
    }

    public void setTzClsResult(String tzClsResult) {
        this.tzClsResult = tzClsResult == null ? null : tzClsResult.trim();
    }

    public String getTzWfbDefaltBz() {
        return tzWfbDefaltBz;
    }

    public void setTzWfbDefaltBz(String tzWfbDefaltBz) {
        this.tzWfbDefaltBz = tzWfbDefaltBz == null ? null : tzWfbDefaltBz.trim();
    }

    public String getTzAppproContent() {
        return tzAppproContent;
    }

    public void setTzAppproContent(String tzAppproContent) {
        this.tzAppproContent = tzAppproContent == null ? null : tzAppproContent.trim();
    }
}