package com.tranzvision.gd.TZEventsBundle.model;

public class PsTzHdBmrdcAet {
    private String runCntlId;

    private String tzArtId;

    private String tzRelUrl;

    private String tzJdUrl;

    private String tzParamsStr;

    public String getRunCntlId() {
        return runCntlId;
    }

    public void setRunCntlId(String runCntlId) {
        this.runCntlId = runCntlId == null ? null : runCntlId.trim();
    }

    public String getTzArtId() {
        return tzArtId;
    }

    public void setTzArtId(String tzArtId) {
        this.tzArtId = tzArtId == null ? null : tzArtId.trim();
    }

    public String getTzRelUrl() {
        return tzRelUrl;
    }

    public void setTzRelUrl(String tzRelUrl) {
        this.tzRelUrl = tzRelUrl == null ? null : tzRelUrl.trim();
    }

    public String getTzJdUrl() {
        return tzJdUrl;
    }

    public void setTzJdUrl(String tzJdUrl) {
        this.tzJdUrl = tzJdUrl == null ? null : tzJdUrl.trim();
    }

    public String getTzParamsStr() {
        return tzParamsStr;
    }

    public void setTzParamsStr(String tzParamsStr) {
        this.tzParamsStr = tzParamsStr == null ? null : tzParamsStr.trim();
    }
}